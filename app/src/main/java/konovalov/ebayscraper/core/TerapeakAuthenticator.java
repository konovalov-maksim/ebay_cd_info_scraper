package konovalov.ebayscraper.core;

import android.util.Log;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TerapeakAuthenticator {

    private final OkHttpClient client = HttpClient.getInstance();
    private final LoginStatusListener loginStatusListener;
    private final String securedPageUrl = "https://www.ebay.com/sh/research/api/search?keywords=metallica";

    public TerapeakAuthenticator(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }

    public void checkIfLoggedIn() {
        Request request = new Request.Builder()
                .url(securedPageUrl)
                .headers(basicHeaders)
                .build();
        client.newCall(request).enqueue(checkLoginCallbackApi);
    }

    public void checkIfLoggedIn(String cookieHeaderValue) {
        Log.d("cookies", "Check for login with Cookie: " + cookieHeaderValue);
        ((PersistentCookieJar) client.cookieJar()).clear();
        Request request = new Request.Builder()
                .url(securedPageUrl)
                .headers(basicHeaders)
                .header("Cookie", cookieHeaderValue)
                .build();
        client.newCall(request).enqueue(checkLoginCallbackApi);
    }

    private final Callback checkLoginCallbackApi = new Callback() {

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try (ResponseBody body = response.body()) {
                String bodyContent = body.string();
                Log.d("cookies", bodyContent);
                JsonObject root = new Gson().fromJson(bodyContent.split("\\n")[0], JsonObject.class);
                if (root.getAsJsonObject().get("searchResultsTitle") != null) {
                    Log.d("cookies", "searchResultsTitle found in JSON");
                    loginStatusListener.onStatusReceived(true);
                } else if (root.getAsJsonObject().get("error") != null) {
                    Log.d("cookies", "ERROR found in JSON");
                    loginStatusListener.onStatusReceived(false);
                } else {
                    Log.d("cookies", "Unknown JSON format :(");
                    loginStatusListener.onStatusReceived(false);
                }
            } catch (Exception e) {
                Log.e("cookies", "Failed to process response: ", e);
                loginStatusListener.onStatusReceived(false);
            }
        }

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            loginStatusListener.onStatusReceived(false);
        }
    };

    private final Headers basicHeaders = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Connection", "keep-alive")
            .add("Accept-Language", "en-US")
            .build();


    public interface LoginStatusListener {
        void onStatusReceived(boolean loggedIn);
    }

}
