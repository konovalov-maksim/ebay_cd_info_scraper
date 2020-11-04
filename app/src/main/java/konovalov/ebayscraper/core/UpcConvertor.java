package konovalov.ebayscraper.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import konovalov.ebayscraper.core.entities.Release;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;


public class UpcConvertor {

    private final OkHttpClient client = HttpClient.getInstance();
    private Logger logger;
    private Callback callback;
    private final String BASE_URL = "https://api.discogs.com/database/search";

    private int maxThreads = 5;
    private long timeout = 10000;
    private ConvertorListener convertorListener;

    private int threads;
    private boolean isRunning;

    private Deque<String> unprocessed = new ConcurrentLinkedDeque<>();
    private final String TOKEN;

    public UpcConvertor(List<String> upcs, String token, ConvertorListener convertorListener) {
        unprocessed.addAll(upcs);
        this.TOKEN = token;
        this.convertorListener = convertorListener;
        initCallbacks();
    }

    public void start() {
        threads = 0;
        isRunning = true;
        sendNewRequests();
    }

    public void stop() {
        isRunning = false;
        onFinish();
    }

    private void sendNewRequests() {
        while (isRunning && threads < maxThreads && !unprocessed.isEmpty()) {
            String upc = unprocessed.pop();
            HttpUrl url = HttpUrl.parse(BASE_URL);
            url = url.newBuilder()
                    .addQueryParameter("token", TOKEN)
                    .addQueryParameter("barcode", upc)
                    .build();
            Request request = new Request.Builder().url(url).build();
            threads++;
            client.newCall(request).enqueue(callback);
        }
    }

    private void initCallbacks() {
        callback = new Callback() {
            @Override
            public synchronized void onResponse(@NotNull Call call, @NotNull Response response) {
                if (!isRunning) return;
                threads--;
                String upc = call.request().url().queryParameter("barcode");
                try {
                    String jsonData = response.body().string();
                    JsonObject root = new Gson().fromJson(jsonData, JsonObject.class);
                    JsonArray results = root.get("results").getAsJsonArray();
                    if (results.size() > 0) {
                        Release release = new Gson().fromJson(results.get(0), Release.class);
                        convertorListener.onUpcConverted(upc, release);
                    } else {
                        convertorListener.onUpcNotFound(upc);
                        log("No results found for UPC " + upc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log("Failed to convert upc " + upc);
                }
                checkIsComplete();
                sendNewRequests();
            }

            @Override
            public synchronized void onFailure(@NotNull Call call, @NotNull IOException e) {
                threads--;
                log("Failed to convert upc " + call.request().url().queryParameter("barcode"));
                checkIsComplete();
                sendNewRequests();
            }
        };
    }

    private void checkIsComplete() {
        if (threads == 0 && unprocessed.isEmpty()) onFinish();
    }

    private void onFinish() {
        isRunning = false;
        client.connectionPool().evictAll();
        convertorListener.onAllUpcConverted();
    }

    public interface ConvertorListener {
        void onUpcConverted(String upc, Release release);
        void onUpcNotFound(String upc);
        void onAllUpcConverted();
    }

    private void log(String message) {
        if (logger != null) logger.log(message);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
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
}
