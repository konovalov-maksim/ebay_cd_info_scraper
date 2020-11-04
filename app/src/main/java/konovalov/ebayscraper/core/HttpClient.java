package konovalov.ebayscraper.core;

import android.content.Context;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import okhttp3.OkHttpClient;

public class HttpClient {

    private static ClearableCookieJar cookieJar;

    private static OkHttpClient client;

    public static OkHttpClient getInstance() {
        if (client != null) return client;
        if (cookieJar == null) throw new RuntimeException("Http client isn't initialized");
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
        return client;
    }

    public static void init(Context context) {
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    private HttpClient() {}

}
