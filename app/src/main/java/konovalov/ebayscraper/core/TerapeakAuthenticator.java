package konovalov.ebayscraper.core;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TerapeakAuthenticator {

    private final OkHttpClient client = HttpClient.getInstance();
    private final LoginStatusListener loginStatusListener;

    public TerapeakAuthenticator(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }

    public void checkIfLoggedIn() {
        Request request = new Request.Builder()
                .url("https://www.ebay.com/sh/research/")
                .headers(basicHeaders)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                loginStatusListener.onStatusReceived(false);
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                loginStatusListener.onStatusReceived(response.code() == 200);
            }
        });
    }

    public void login(String login, String password) {
        Request request = new Request.Builder()
                .url("")
                .headers(basicHeaders)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
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

}
