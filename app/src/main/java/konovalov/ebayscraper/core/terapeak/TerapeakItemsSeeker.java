package konovalov.ebayscraper.core.terapeak;

import konovalov.ebayscraper.core.Logger;
import konovalov.ebayscraper.core.entities.TerapeakResult;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TerapeakItemsSeeker {

    private Logger logger;
    private OkHttpClient client;
    private Callback callback;
    private HttpUrl preparedUrl;
    private ResultsLoadingListener resultsLoadingListener;

//    private final String BASE_URL = "https://www.ebay.com/sh/research/api/search";
    private final String BASE_URL = "https://www.ebay.com/sh/research/api/search" +
        "?condition=NEW&dayRange=CUSTOM&endDate=1604127600000&keywords=metallica&marketplace=EBAY-US&startDate=1601535600000&tabName=SOLD";

    private boolean isRunning = false;
    private int threads;

    private final Deque<String> unprocessed = new ConcurrentLinkedDeque<>();

    private final Condition condition;
    private String categoryId = null;
    private int dayRange = 90;


    private final int MAX_ITEMS_PER_PAGE = 50;
    private final int MAX_PAGE_NUMBER = 100;
    private int itemsLimit = MAX_ITEMS_PER_PAGE * MAX_PAGE_NUMBER;
    private int maxThreads = 5;
    private long timeout = 10000;

    private LinkedHashMap<String, TerapeakResult> results = new LinkedHashMap<>(); //Here stored all found results without duplicates

    public TerapeakItemsSeeker(List<String> queries, Condition condition, ResultsLoadingListener resultsLoadingListener) {
        unprocessed.addAll(queries.stream().distinct().collect(Collectors.toList()));
        this.condition = condition;
        this.resultsLoadingListener = resultsLoadingListener;
        initCallback();
    }

    public void start() {
        client = new OkHttpClient.Builder().callTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
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
//        while (isRunning && threads < maxThreads && !unprocessed.isEmpty()) {
            String query = unprocessed.pop();


            HttpUrl finalUrl = preparedUrl.newBuilder()
//                    .addQueryParameter("keywords", query)
//                    .addQueryParameter("paginationInput.pageNumber", String.valueOf(page))
//                    .addQueryParameter("paginationInput.entriesPerPage", String.valueOf(maxOnPage))
                    .build();

            Request request = new Request.Builder()
                    .url(finalUrl)
                    .headers(basicHeaders)
                    .build();
            System.out.println(finalUrl.url());

            threads++;
            client.newCall(request).enqueue(callback);
//        }
    }

    private void initCallback() {
        callback = new Callback() {
            @Override
            public synchronized void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.peekBody(Long.MAX_VALUE)) {
                    String responseBody = body.string();
                    System.out.println(responseBody);
                }
                System.out.println(response);
/*                if (!isRunning) return;
                threads--;
                //Adding results
                Result newResult = extractResult(response);
                Result oldResult = results.get(newResult.getQuery());
                Result result;
                log(String.format("%-30s%s", "Query: " + response.request().url().queryParameter("keywords"),
                        " - page " + response.request().url().queryParameter("paginationInput.pageNumber") + " loaded"));
                if (oldResult == null) {
                    results.put(newResult.getQuery(), newResult);
                    result = newResult;
                } else {
                    oldResult.getItems().addAll(newResult.getItems());
                    oldResult.setCompleteItemsTotal(newResult.getCompleteItemsTotal());
                    result = oldResult;
                }

                //Adding to queue again if needed to load remaining pagination pages
                long itemsFound = callType.equals(CallType.ACTIVE) ? result.getActiveItemsFound() : result.getCompleteItemsFound();
                long itemsTotal = callType.equals(CallType.ACTIVE) ? result.getActiveItemsTotal() : result.getCompleteItemsTotal();
                if (itemsFound < itemsTotal && itemsFound < itemsLimit) {
                    unprocessed.add(result.getQuery());
                    result.setStatus(Result.Status.LOADING);
                } else if (callType.equals(CallType.COMPLETED)) {
                    result.setStatus(Result.Status.COMPLETED);
                    log(String.format("%-30s%s", "Query: " + result.getQuery(), " - all items found: " + result.getItems().size()));
                }

                checkIsComplete();
                sendNewRequests();
                resultsLoadingListener.onResultReceived(result);*/
            }

            @Override
            public synchronized void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
/*                if (!isRunning) return;
                threads--;
                String query = call.request().url().queryParameter("keywords");
                Result result = new Result(query);
                result.setStatus(Result.Status.ERROR);
                results.putIfAbsent(result.getQuery(), result);
                log(String.format("%-30s%s", "Query: " + result.getQuery(),
                        " - page " + call.request().url().queryParameter("paginationInput.pageNumber") + ": loading error!"));
                checkIsComplete();
                sendNewRequests();
                resultsLoadingListener.onResultReceived(result);*/
            }
        };
    }

    private void checkIsComplete() {
        if (threads == 0 && unprocessed.isEmpty()) onFinish();
    }

    private void onFinish() {
        isRunning = false;
        client.connectionPool().evictAll();
        resultsLoadingListener.onAllResultsReceived();
    }

    //Extracting Result object from JSON response body
    private TerapeakResult extractResult(Response response) {

        return new TerapeakResult();
    }

    //Preparing URL with get parameters
    private void prepareUrl() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL);
        if (httpUrl == null) {
            log("Unable to detect base url");
            return;
        }
        //https://www.ebay.com/sh/research
        // ?dayRange=90
        // &endDate=1604490339029
        // &keywords=metallica
        // &marketplace=EBAY-US
        // &offset=0
        // &queryCondition=AND
        // &startDate=1596800739029
        // &tabName=SOLD
        // &categoryId=2984

        HttpUrl.Builder urlBuilder = httpUrl.newBuilder()
//                .addQueryParameter("dayRange", "CUSTOM")
//                .addQueryParameter("marketplace", "EBAY-US")
//                .addQueryParameter("RESPONSE-DATA-FORMAT", "JSON")
                ;

//        //Condition items filter. Docs - https://developer.ebay.com/DevZone/finding/CallRef/types/ItemFilterType.html
//        if (condition.equals(Condition.NEW)) {
//            urlBuilder.addQueryParameter("itemFilter(0).name", "Condition")
//                    .addQueryParameter("itemFilter(0).value(0)", "1000") //New
//                    .addQueryParameter("itemFilter(0).value(1)", "1500") //New other (see details)
//                    .addQueryParameter("itemFilter(0).value(2)", "1750"); //New with defects
//        } else if (condition.equals(Condition.USED)) {
//            urlBuilder.addQueryParameter("itemFilter(0).name", "Condition")
//                    .addQueryParameter("itemFilter(0).value(0)", "2000") //Manufacturer refurbished
//                    .addQueryParameter("itemFilter(0).value(1)", "2500") //Seller refurbished
//                    .addQueryParameter("itemFilter(0).value(2)", "2750") //Like New
//                    .addQueryParameter("itemFilter(0).value(3)", "3000") //Used
//                    .addQueryParameter("itemFilter(0).value(4)", "4000") //Very Good
//                    .addQueryParameter("itemFilter(0).value(5)", "5000") //Good
//                    .addQueryParameter("itemFilter(0).value(6)", "6000") //Acceptable
//                    .addQueryParameter("itemFilter(0).value(7)", "7000"); //For parts or not working
//        }
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

    public enum Condition {
        NEW, USED, ALL
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

    public int getItemsLimit() {
        return itemsLimit;
    }

    public void setItemsLimit(int itemsLimit) {
        if (itemsLimit > MAX_ITEMS_PER_PAGE * MAX_PAGE_NUMBER) this.itemsLimit = MAX_ITEMS_PER_PAGE * MAX_PAGE_NUMBER;
        else this.itemsLimit = itemsLimit;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public List<TerapeakResult> getResults() {
        return new ArrayList<>(results.values());
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
}
