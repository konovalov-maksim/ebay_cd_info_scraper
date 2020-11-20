package konovalov.ebayscraper.core;

import konovalov.ebayscraper.core.Condition;
import konovalov.ebayscraper.core.Logger;
import konovalov.ebayscraper.core.entities.Item;
import konovalov.ebayscraper.core.entities.Result;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ItemsSeeker {

    private Logger logger;
    private OkHttpClient client;
    private Callback callback;
    private HttpUrl preparedUrlApi;
    private HttpUrl preparedUrlHtml;
    private final ResultsLoadingListener resultsLoadingListener;
    private CallType callType;

    private final String BASE_URL_API = "https://svcs.ebay.com/services/search/FindingService/v1";
    private final String BASE_URL_HTML = "https://www.ebay.com/sch/";

    private boolean isRunning = false;
    private int threads;

    private final Deque<String> unprocessed = new ConcurrentLinkedDeque<>();
    private final String APP_NAME;
    private final Condition condition;

    private final int MAX_ITEMS_PER_PAGE_API = 100; //limit from docs: https://developer.ebay.com/DevZone/finding/CallRef/findItemsByKeywords.html#Request.paginationInput
    private final int MAX_PAGE_NUMBER_API = 100; //limit from docs
    private final int MAX_ITEMS_PER_PAGE_HTML = 200; //limit from ebay web site
    private final int MAX_PAGE_NUMBER_HTML = 100; //some rather big value
    private int itemsLimit = MAX_ITEMS_PER_PAGE_API * MAX_PAGE_NUMBER_API; //default items limit: 10 000
    private int maxThreads = 5;
    private long timeout = 10000;
    private String categoryId = null;

    private final LinkedHashMap<String, Result> results = new LinkedHashMap<>(); //Here stored all found results without duplicates

    public ItemsSeeker(List<String> queries, String appname, Condition condition, ResultsLoadingListener resultsLoadingListener) {
        unprocessed.addAll(queries.stream().distinct().collect(Collectors.toList()));
        this.APP_NAME = appname;
        this.condition = condition;
        this.resultsLoadingListener = resultsLoadingListener;
        initCallback();
        callType = CallType.ACTIVE;
    }

    public void start() {
/*        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-us2.vpnsecure.me", 8080));
        Authenticator proxyAuthenticator = (route, response) -> response.request().newBuilder()
                .header("Proxy-Authorization", Credentials.basic("muddy77", "Wesazx77"))
                .build();*/

        client = new OkHttpClient.Builder().callTimeout(timeout, TimeUnit.MILLISECONDS)
//                .proxy(proxy)
//                .proxyAuthenticator(proxyAuthenticator)
                .build();


        threads = 0;
        prepareUrlApi();
        prepareUrlHtml();
        isRunning = true;
        sendNewRequests();
    }

    public void stop() {
        isRunning = false;
        onFinish();
    }

    private void sendNewRequests() {
        while (isRunning && threads < maxThreads && !unprocessed.isEmpty()) {
            if (callType.equals(CallType.ACTIVE)) {
                sendRequestForActiveItems();
            } else if (callType.equals(CallType.SOLD)) {
                sendRequestForSoldItems();
            } else throw new RuntimeException("Unknown calltype: " + callType);
        }
    }

    private void sendRequestForActiveItems() {
        String query = unprocessed.pop();
        long page;
        long maxOnPage;
        Result result = results.get(query);
        if (result == null) {
            page = 1;
            maxOnPage = Math.min(itemsLimit, MAX_ITEMS_PER_PAGE_API);
        } else {
            long itemsFound = result.getActiveItemsFound();
            page = Math.round(itemsFound * 1.0 / MAX_ITEMS_PER_PAGE_API) + 1;
            maxOnPage = Math.min(itemsLimit - itemsFound, MAX_ITEMS_PER_PAGE_API);
        }
        if (page > MAX_PAGE_NUMBER_API) {
            log(String.format("%-30s%s", query, " - all items found on " + MAX_PAGE_NUMBER_API + " pages"));
            return;
        }

        HttpUrl finalUrl = preparedUrlApi.newBuilder()
                .addQueryParameter("OPERATION-NAME", "findItemsAdvanced")
                .addQueryParameter("keywords", query)
                .addQueryParameter("paginationInput.pageNumber", String.valueOf(page))
                .addQueryParameter("paginationInput.entriesPerPage", String.valueOf(maxOnPage))
                .build();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        System.out.println(finalUrl.url());
        threads++;
        client.newCall(request).enqueue(callback);
    }

    private void sendRequestForSoldItems() {
        String query = unprocessed.pop();
        long page;
        Result result = results.get(query);
        if (result == null) {
            page = 1;
        } else {
            long itemsFound = result.getCompleteItemsFound();
            page = Math.round(itemsFound * 1.0 / MAX_ITEMS_PER_PAGE_HTML) + 1;
        }
        if (page > MAX_PAGE_NUMBER_HTML) {
            log(String.format("%-30s%s", query, " - all items found on " + MAX_PAGE_NUMBER_HTML + " pages"));
            return;
        }

        HttpUrl finalUrl = preparedUrlHtml.newBuilder()
                .addQueryParameter("_nkw", query)
                .addQueryParameter("_pgn", String.valueOf(page))
                .build();

        Request request = new Request.Builder()
                .headers(basicHeaders)
                .url(finalUrl)
                .build();
        System.out.println(finalUrl.url());
        threads++;
        client.newCall(request).enqueue(callback);

    }

    private void initCallback() {
        callback = new Callback() {
            @Override
            public synchronized void onResponse(@NotNull Call call, @NotNull Response response) {
                if (!isRunning) return;
                threads--;
                //Adding results
                Result newResult = callType.equals(CallType.ACTIVE)
                        ? extractResultFromJson(response)
                        : extractResultFromHtml(response);
                Result oldResult = results.get(newResult.getQuery());
                Result result;
                log(String.format("%-30s%s", "Query: " + detectQuery(call.request()), " - page " + detectPageNum(call.request()) + " loaded"));

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
                } else if (callType.equals(CallType.SOLD)) {
                    result.setStatus(Result.Status.COMPLETED);
                    log(String.format("%-30s%s", "Query: " + result.getQuery(), " - all items found: " + result.getItems().size()));
                }

                checkIsComplete();
                sendNewRequests();
                resultsLoadingListener.onResultReceived(result);
            }

            @Override
            public synchronized void onFailure(@NotNull Call call, @NotNull IOException e) {
                if (!isRunning) return;
                threads--;
                String query = detectQuery(call.request());
                Result result = new Result(query);
                result.setStatus(Result.Status.ERROR);
                results.putIfAbsent(result.getQuery(), result);
                log(String.format("%-30s%s", "Query: " + query, " - page " + detectPageNum(call.request()) + ": loading error!"));
                checkIsComplete();
                sendNewRequests();
                resultsLoadingListener.onResultReceived(result);
            }
        };
    }

    private void checkIsComplete() {
        if (threads == 0 && unprocessed.isEmpty())
            if (callType.equals(CallType.ACTIVE)) {
                log("Active items loading is finished. Starting loading of complete items");
                callType = CallType.SOLD;
                unprocessed.addAll(results.keySet());
            } else {
                onFinish();
            }
    }

    private void onFinish() {
        isRunning = false;
        client.connectionPool().evictAll();
        resultsLoadingListener.onAllResultsReceived();
    }

    //Extracting Result object from JSON response body
    private Result extractResultFromJson(Response response) {
        String query = response.request().url().queryParameter("keywords");
        Result result = new Result(query);
        try (ResponseBody responseBody = response.peekBody(Long.MAX_VALUE)){
            String jsonData = responseBody.string();
            JsonObject root = new Gson().fromJson(jsonData, JsonObject.class);
            //Status
            boolean isSuccess = root.getAsJsonArray("findItemsAdvancedResponse")
                    .get(0).getAsJsonObject()
                    .get("ack").getAsString()
                    .equals("Success");
            if (!isSuccess) {
                String errorMessage = root.getAsJsonArray("findItemsAdvancedResponse")
                        .get(0).getAsJsonObject()
                        .get("errorMessage").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("error").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("message").getAsJsonArray()
                        .get(0).getAsString();
                log("Query: " + query + " - error: " + errorMessage);
                return result;
            }
            //Total entries
            int totalItems = root.getAsJsonArray("findItemsAdvancedResponse")
                    .get(0).getAsJsonObject()
                    .get("paginationOutput").getAsJsonArray()
                    .get(0).getAsJsonObject()
                    .get("totalEntries").getAsJsonArray()
                    .get(0).getAsInt();
            result.setActiveItemsTotal(totalItems);
            //Items
            JsonObject searchResult = root.getAsJsonArray("findItemsAdvancedResponse")
                    .get(0).getAsJsonObject()
                    .get("searchResult").getAsJsonArray()
                    .get(0).getAsJsonObject();
            if (!searchResult.has("item")) return result; //No items found
            JsonArray jsonItems = searchResult.get("item").getAsJsonArray();
            for (JsonElement jsonItem : jsonItems) {
                String itemId = jsonItem.getAsJsonObject().get("itemId").getAsString();

                double price = jsonItem.getAsJsonObject()
                        .get("sellingStatus").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("currentPrice").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("__value__").getAsDouble();
                String sellingStatus = jsonItem.getAsJsonObject()
                        .get("sellingStatus").getAsJsonArray()
                        .get(0).getAsJsonObject()
                        .get("sellingState").getAsJsonArray()
                        .get(0).getAsString();
                String itemUrl = jsonItem.getAsJsonObject()
                        .get("viewItemURL").getAsJsonArray()
                        .get(0).getAsString();
                Item item = new Item(itemId, price, sellingStatus, itemUrl);
                log("Item: " + item.toString());

                result.addItem(item);
            }

            result.setIsSuccess(true);
        } catch (IOException | NullPointerException e) {
            log("Query: " + query + " - unable to get response body");
            e.printStackTrace();
        } catch (Exception e) {
            log("Query: " + query + " - unable to process result");
            e.printStackTrace();
        }
        return result;
    }

    private Result extractResultFromHtml(Response response) {
        String query = detectQuery(response.request());
        Result result = new Result(query);
        try (ResponseBody responseBody = response.peekBody(Long.MAX_VALUE)) {
            String html = responseBody.string();
            Document doc = Jsoup.parse(html);

            //Total complete items
            String completeItemsTotalStr = doc.selectFirst("h1.srp-controls__count-heading > span.BOLD").text().replaceAll(",", "");
            result.setCompleteItemsTotal(Integer.parseInt(completeItemsTotalStr));

            Result oldResult = results.get(query);
            long completeItemsFound = oldResult != null ? oldResult.getCompleteItemsFound() : 0;

            Elements itemsEl = doc.select("#srp-river-results li.s-item");
            for (Element itemEl : itemsEl) {
                if (completeItemsFound >= itemsLimit) break;
                //ItemId
                String itemUrlStr = itemEl.selectFirst("a.s-item__link").attr("href");
                HttpUrl itemUrl = HttpUrl.parse(itemUrlStr);
                String itemId = itemUrl.pathSegments().get(itemUrl.pathSize() - 1);
                //Price
                String priceStr = itemEl.selectFirst("span.s-item__price span.POSITIVE").text().replaceAll("[^0-9.]", "");
                double price = Double.parseDouble(priceStr);

                Item item = new Item(itemId, price, "EndedWithSales", itemUrlStr);
                log("Item: " + item.toString());
                result.addItem(item);
                completeItemsFound++;
            }
            result.setIsSuccess(true);
        } catch (IOException | NullPointerException e) {
            log("Query: " + query + " - unable to get response body");
            e.printStackTrace();
        } catch (Exception e) {
            log("Query: " + query + " - unable to process result");
            e.printStackTrace();
        }
        return result;
    }

    //Preparing URL with get parameters
    private void prepareUrlApi() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL_API);
        if (httpUrl == null) {
            log("Unable to detect base url");
            return;
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder()
                .addQueryParameter("GLOBAL-ID", "EBAY-US")
                .addQueryParameter("SERVICE-VERSION", "1.13.0")
                .addQueryParameter("SECURITY-APPNAME", APP_NAME)
                .addQueryParameter("RESPONSE-DATA-FORMAT", "JSON");

        //Condition items filter. Docs - https://developer.ebay.com/DevZone/finding/CallRef/types/ItemFilterType.html
        if (condition.equals(Condition.NEW)) {
            urlBuilder.addQueryParameter("itemFilter(0).name", "Condition")
                    .addQueryParameter("itemFilter(0).value(0)", "1000") //New
                    .addQueryParameter("itemFilter(0).value(1)", "1500") //New other (see details)
                    .addQueryParameter("itemFilter(0).value(2)", "1750"); //New with defects
        } else if (condition.equals(Condition.USED)) {
            urlBuilder.addQueryParameter("itemFilter(0).name", "Condition")
                    .addQueryParameter("itemFilter(0).value(0)", "2000") //Manufacturer refurbished
                    .addQueryParameter("itemFilter(0).value(1)", "2500") //Seller refurbished
                    .addQueryParameter("itemFilter(0).value(2)", "2750") //Like New
                    .addQueryParameter("itemFilter(0).value(3)", "3000") //Used
                    .addQueryParameter("itemFilter(0).value(4)", "4000") //Very Good
                    .addQueryParameter("itemFilter(0).value(5)", "5000") //Good
                    .addQueryParameter("itemFilter(0).value(6)", "6000") //Acceptable
                    .addQueryParameter("itemFilter(0).value(7)", "7000"); //For parts or not working
        }
        //Category filter
        if (categoryId != null) urlBuilder.addQueryParameter("categoryId", categoryId);
        preparedUrlApi = urlBuilder.build();
    }

    private void prepareUrlHtml() {
        HttpUrl httpUrl = HttpUrl.parse(BASE_URL_HTML);
        if (httpUrl == null) {
            log("Unable to detect base html url");
            return;
        }
        HttpUrl.Builder urlBuilder = httpUrl.newBuilder()
                .addQueryParameter("_ipg", String.valueOf(MAX_ITEMS_PER_PAGE_HTML))
                .addQueryParameter("LH_Sold", "1") //sold items
                //.addQueryParameter("LH_Complete", "1") //complete items
                //.addQueryParameter("LH_PrefLoc", "1") //from US only
                ;

        //Condition items filter
        if (condition.equals(Condition.NEW)) {
            urlBuilder.addQueryParameter("LH_ItemCondition", "1000|1500|1750");
        } else if (condition.equals(Condition.USED)) {
            urlBuilder.addQueryParameter("LH_ItemCondition", "2000|2500|2750|3000|4000|5000|6000|7000");
        }
        //Category filter
        if (categoryId != null) urlBuilder.addPathSegment(categoryId);

        urlBuilder.addPathSegment("i.html");
        preparedUrlHtml = urlBuilder.build();
    }

    private String detectPageNum(Request request) {
        String pageNum = "0";
        if (callType.equals(CallType.ACTIVE)) pageNum = request.url().queryParameter("paginationInput.pageNumber");
        else if (callType.equals(CallType.SOLD)) pageNum = request.url().queryParameter("_pgn");
        return pageNum;
    }

    private String detectQuery(Request request) {
        String pageNum = "0";
        if (callType.equals(CallType.ACTIVE)) pageNum = request.url().queryParameter("keywords");
        else if (callType.equals(CallType.SOLD)) pageNum = request.url().queryParameter("_nkw");
        return pageNum;
    }

    private final Headers basicHeaders = new Headers.Builder()
            .add("Host", "www.ebay.com")
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:82.0) Gecko/20100101 Firefox/82.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Accept-Language", "en-US")
            .add("Connection", "keep-alive")
            .add("Upgrade-Insecure-Requests", "1")
            .add("Cache-Control", "max-age=0")
            .build();

    private void log(String message) {
        if (logger != null) logger.log(message);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private enum CallType {
        ACTIVE, SOLD
    }

    public interface ResultsLoadingListener {
        void onResultReceived(Result result);

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
        if (itemsLimit > MAX_ITEMS_PER_PAGE_API * MAX_PAGE_NUMBER_API) this.itemsLimit = MAX_ITEMS_PER_PAGE_API * MAX_PAGE_NUMBER_API;
        else this.itemsLimit = itemsLimit;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public List<Result> getResults() {
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
