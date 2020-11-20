package konovalov.ebayscraper.core;

import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;

import java.io.IOException;

public class TerapeakAuthenticator {

    private final OkHttpClient client = HttpClient.getInstance();
    private LoginStatusListener loginStatusListener;
    private final String apiUrl = "https://www.ebay.com/sh/research/api/search?keywords=metallica";
    private final String personalPageUrl = "https://www.ebay.com/sh/ovw";

    public void checkIfLoggedIn() {
        Request request = new Request.Builder()
                .url(apiUrl)
                .headers(basicHeaders)
                .build();
        client.newCall(request).enqueue(checkLoginCallbackApi);
    }

    public void checkIfLoggedIn(String cookieHeaderValue) {
        Log.d("cookies", "Check for login with Cookie: " + cookieHeaderValue);
        ((PersistentCookieJar) client.cookieJar()).clear();
        Request request = new Request.Builder()
                .url(apiUrl)
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

    public void getLogin(LoginListener loginListener) {
        Request request = new Request.Builder()
                .url(personalPageUrl)
                .headers(basicHeaders)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!personalPageUrl.equals(response.request().url().toString())) {
                    loginListener.onLoginReceived(null);
                    return;
                }
                try (ResponseBody body = response.peekBody(Long.MAX_VALUE)) {
                    String login = Jsoup.parse(body.string())
                            .getElementById("s0-0-4-3-29-4").attr("href").replaceAll(".*/","");
                    loginListener.onLoginReceived(login);
                } catch (Exception e) {
                    loginListener.onLoginReceived("unknown");
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                loginListener.onLoginReceived(null);
            }
        });
    }

    public void logOut() {
        ((ClearableCookieJar) client.cookieJar()).clear();
    }

    public void setLoginStatusListener(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }

    private final Headers basicHeaders = new Headers.Builder()
            .add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")
            .add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .add("Connection", "keep-alive")
            .add("Accept-Language", "en-US")
            .build();

    public interface LoginStatusListener {
        void onStatusReceived(boolean loggedIn);
    }

    public interface LoginListener {
        void onLoginReceived(@Nullable String login);
    }

}
