package konovalov.ebayscraper.core.terapeak;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import konovalov.ebayscraper.core.Condition;
import konovalov.ebayscraper.core.HttpClient;
import konovalov.ebayscraper.core.Logger;
import konovalov.ebayscraper.core.entities.Status;
import konovalov.ebayscraper.core.entities.TerapeakResult;
import konovalov.ebayscraper.core.pojo.aggregated.ResearchResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class TerapeakItemsSeeker {

    private final OkHttpClient client = HttpClient.getInstance();
    private Logger logger;
    private HttpUrl preparedUrl;
    private int dayRange = 90;
    private ResultsLoadingListener resultsLoadingListener;

    private final String BASE_URL = "https://www.ebay.com/sh/research/api/search";

    private boolean isRunning = false;
    private int threads;

    private final Deque<String> unprocessed = new ConcurrentLinkedDeque<>();

    private final Condition condition;
    private String categoryId = null;

    private int maxThreads = 6;
    private long timeout = 10000;

    private LinkedHashMap<String, TerapeakResult> results = new LinkedHashMap<>(); //Here stored all found results without duplicates

    public TerapeakItemsSeeker(List<String> queries, Condition condition, ResultsLoadingListener resultsLoadingListener) {
        unprocessed.addAll(queries.stream().distinct().collect(Collectors.toList()));
        this.condition = condition;
        this.resultsLoadingListener = resultsLoadingListener;
    }

    public void start() {
        threads = 0;
        prepareUrl();
        isRunning = true;
        sendNewRequests();
    }

    public void stop() {
        isRunning = false;
        onFinish();
    }

    private void sendNewRequests() {
        while (isRunning && threads < maxThreads && !unprocessed.isEmpty()) {
            String query = unprocessed.pop();

            HttpUrl soldItemsUrl = preparedUrl.newBuilder()
                    .addQueryParameter("keywords", query)
                    .addQueryParameter("tabName", "SOLD")
                    .build();
            Request soldItemsRequest = new Request.Builder()
                    .url(soldItemsUrl)
                    .headers(basicHeaders)
                    .build();
            Log.d("seeker", soldItemsUrl.toString());
            client.newCall(soldItemsRequest).enqueue(callback);
            threads++;

            HttpUrl activeItemsUrl = preparedUrl.newBuilder()
                    .addQueryParameter("keywords", query)
                    .addQueryParameter("tabName", "ACTIVE")
                    .build();
            Request activeItemsRequest = new Request.Builder()
                    .url(activeItemsUrl)
                    .headers(basicHeaders)
                    .build();
            Log.d("seeker", activeItemsUrl.toString());
            client.newCall(activeItemsRequest).enqueue(callback);
            threads++;
        }
    }


    private final Callback callback = new Callback() {
        @Override
        public synchronized void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if (!isRunning) return;
            threads--;
            String query = response.request().url().queryParameter("keywords");
            String tabName = response.request().url().queryParameter("tabName");
            TerapeakResult result = results.getOrDefault(query, new TerapeakResult(query));
            results.putIfAbsent(query, result);
            try (ResponseBody body = response.peekBody(Long.MAX_VALUE)) {
                String bodyContent = body.string();
                Log.d("seeker", bodyContent);
                if ("SOLD".equalsIgnoreCase(tabName)) {
                    extractResultSold(bodyContent, result);
                } else if ("ACTIVE".equalsIgnoreCase(tabName)) {
                    extractResultActive(bodyContent, result);
                }

                if (!result.getStatus().equals(Status.ERROR)) {
                    if (result.isActiveInfoSet() && result.isSoldInfoSet()) result.setStatus(Status.COMPLETED);
                    else result.setStatus(Status.LOADING);
                }
            } catch (Exception e) {
                result.setStatus(Status.ERROR);
                Log.e("seeker", "Failed to process response");
            }

            resultsLoadingListener.onResultReceived(result);
            checkIsComplete();
            sendNewRequests();
        }

        @Override
        public synchronized void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.e("seeker", "Failed to get response", e);
            if (!isRunning) return;
            threads--;
            String query = call.request().url().queryParameter("keywords");
            TerapeakResult result = results.getOrDefault(query, new TerapeakResult(query));
            result.setStatus(Status.ERROR);
            results.putIfAbsent(query, result);
            resultsLoadingListener.onResultReceived(result);
            checkIsComplete();
            sendNewRequests();
        }
    };

    private void extractResultSold(String responseBody, TerapeakResult result) {
        String aggregatedInfoJson = responseBody.split("\\n")[0];
        Log.d("seeker", aggregatedInfoJson);

        ResearchResponse aggregateJson = new Gson().fromJson(aggregatedInfoJson, ResearchResponse.class);

        String avgSoldPriceStr = aggregateJson.getSections().get(0).getDataItems().get(0).getValue().getAccessibilityText();
        double avgSoldPrice = Double.parseDouble(avgSoldPriceStr.replaceAll("[^\\d.]", ""));
        result.setAvgSoldPrice(avgSoldPrice);

        String totalSoldStr = aggregateJson.getSections().get(2).getDataItems().get(0).getValue().getAccessibilityText();
        int totalSold = Integer.parseInt(totalSoldStr.replaceAll("[^\\d]", ""));
        result.setTotalSold(totalSold);

        String sellThroughStr = aggregateJson.getSections().get(2).getDataItems().get(1).getValue().getAccessibilityText();
        double sellThrough = Double.parseDouble(sellThroughStr.replaceAll("[^\\d.]", ""));
        result.setSelfThrough(sellThrough);

        result.setSoldInfoSet(true);
    }

    private void extractResultActive(String responseBody, TerapeakResult result) {
        String aggregatedInfoJson = responseBody.split("\\n")[0];
        String categoryInfoJson = responseBody.split("\\n")[2];
        Log.d("seeker", aggregatedInfoJson);
        Log.d("seeker", categoryInfoJson);

        ResearchResponse aggregateJson = new Gson().fromJson(aggregatedInfoJson, ResearchResponse.class);
        JsonObject categoryJson = new Gson().fromJson(categoryInfoJson, JsonObject.class);

        String avgListingPriceStr = aggregateJson.getSections().get(0).getDataItems().get(0).getValue().getAccessibilityText();
        double avgListingPrice = Double.parseDouble(avgListingPriceStr.replaceAll("[^\\d.]", ""));
        result.setAvgListingPrice(avgListingPrice);

        JsonArray breadcrumbs = categoryJson
                .get("primaryCategories").getAsJsonObject()
                .get("categoryCount").getAsJsonArray();
        String totalActiveStr = breadcrumbs.get(breadcrumbs.size() - 1)
                .getAsJsonObject()
                .get("text").getAsString();
        int totalActiveItems = Integer.parseInt(totalActiveStr.replaceAll("[^\\d]", ""));
        result.setTotalActive(totalActiveItems);

        result.setActiveInfoSet(true);
    }

    private void checkIsComplete() {
        if (threads == 0 && unprocessed.isEmpty()) onFinish();
    }

    private void onFinish() {
        isRunning = false;
        client.connectionPool().evictAll();
        resultsLoadingListener.onAllResultsReceived();
    }

    //Preparing URL with get parameters
    private void prepareUrl() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL);
        if (httpUrl == null) {
            log("Unable to detect base url");
            return;
        }

        Calendar endDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_YEAR, - dayRange + 1);

        HttpUrl.Builder urlBuilder = httpUrl.newBuilder()
                .addQueryParameter("marketplace", "EBAY-US")
                .addQueryParameter("dayRange", String.valueOf(dayRange))
                .addQueryParameter("startDate", String.valueOf(startDate.getTimeInMillis()))
                .addQueryParameter("endDate", String.valueOf(endDate.getTimeInMillis()))
        ;

        if (condition.equals(Condition.NEW)) {
            urlBuilder.addQueryParameter("condition", "NEW"); //New with defects
        } else if (condition.equals(Condition.USED)) {
            urlBuilder.addQueryParameter("condition", "USED");
        }
        //Category filter
        if (categoryId != null) urlBuilder.addQueryParameter("categoryId", categoryId);
        preparedUrl = urlBuilder.build();
    }

    private final Headers basicHeaders = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Connection", "keep-alive")
            .add("Accept-Language", "en-US")
            .add("Cookie", "dp1=bkms/in63653e8d^u1f/Rick63653e8d^u1p/c29ybnk4Ng**63653e8d^bl/US63653e8d^expt/000160373078493360879721^pbf/%238000000000e400e0000081800200000061840b0d^; nonsession=BAQAAAXWQjZC/AAaAAJ0ACGGECw0wMDAwMDAwMAFkAAdjZT6NIzAwMDAwYQAIABxfymSNMTYwMzQ4MDEwOXgyOTMzOTY2OTUzNDR4MHgyWQAzAA5hhAsNNTUzMDktODIwNSxVU0EAywACX6LelTQ2AEAAB2GECw1zb3JueTg2ABAAB2GECw1zb3JueTg2AMoAIGNlPo00YzIxYjBiNDE3NTBhYzNkNWQxMTE3ZTZmZmRlYTEwZQAEAAdheC7fc29ybnk4NgCcADhhhAsNblkrc0haMlByQm1kajZ3Vm5ZK3NFWjJQckEyZGo2d0puWWVsRHBlSHBRaWRqNng5blkrc2VRPT2UAvu4VwHLRLFcdfDRJs70PDBrzg**; npii=btguid/4c21b0b41750ac3d5d1117e6ffdea10e6364fbb4^cguid/4c21b5661750ada5edf067c4fec80a8d6364fbb4^; ns1=BAQAAAXWQjZC/AAaAAKUACmGECw01NDY3NTQ0LzA7ANgASmGECw1jNjl8NjAxXjE2MDMzMDAwODIwODZeXjFeM3wyfDV8NHw3fDExXl5eNF4zXjEyXjEyXjJeMV4xXjBeMV4wXjFeNjQ0MjQ1OTA3NYlgI4ILqHpLIwKC/Bkqs193SkuZ; cid=cUIVtjfWX0Ni105p%23444446483; DG_IID=6D474828-0CF9-3946-994F-093096DC1759; DG_UID=40E42B7E-9DEF-3CC8-B29C-EA929C70C9A7; DG_ZID=B5C9F8DD-C8B9-3A1A-A610-7B72B9AF131D; DG_ZUID=D6A25F53-4687-390A-B2D9-D026AAD609AA; DG_HID=097EEE25-7CAA-3F1D-BD7E-BF79B8314977; DG_SID=93.157.175.39:YAt4dxjKDvEIlIjkBLMiy9QtA6rn83rt6XM1pSpnIB4; s=CgAD4ACBfpCkNNGMyMWIwYjQxNzUwYWMzZDVkMTExN2U2ZmZkZWExMGW0cV4d; ebay=%5Esbf%3D%23%5E; ds2=")
            .build();

    private void log(String message) {
        if (logger != null) logger.log(message);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public interface ResultsLoadingListener {
        void onResultReceived(TerapeakResult result);

        void onAllResultsReceived();
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        if (categoryId == null || categoryId.isEmpty() || categoryId.equals("-1"))
            this.categoryId = null;
        else
            this.categoryId = categoryId;
    }

    public int getDayRange() {
        return dayRange;
    }

    public void setDayRange(int dayRange) {
        this.dayRange = dayRange;
    }
}
