package konovalov.ebayscraper;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import konovalov.ebayscraper.core.HttpClient;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LoginWebViewClient extends WebViewClient {

    private final OkHttpClient okHttpClient = HttpClient.getInstance();

    private LoginStatusListener loginStatusListener;

    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        String cookiesStr = CookieManager.getInstance().getCookie(url);
        System.out.println(url + " ::: " + cookiesStr);
        Log.d("cookies", url + " ::: " + cookiesStr);
        if (url.equals("https://www.ebay.com/")) {
            final HttpUrl responseUrl = HttpUrl.parse(url);
            List<Cookie> responseCookies = new ArrayList<>();
            for (String cookieStr : cookiesStr.split(";")) {
                String[] cookiePair = cookieStr.split("=");
                Cookie cookie = new Cookie.Builder()
                        .domain(responseUrl.topPrivateDomain())
                        .name(cookiePair[0].trim())
                        .value(cookiePair[1].trim())
                        .build();
//                Cookie cookie. = Cookie.parse(Objects.requireNonNull(responseUrl), cookiesStr.trim());
                responseCookies.add(cookie);
            }
            okHttpClient.cookieJar().saveFromResponse(Objects.requireNonNull(responseUrl), responseCookies);
            loginStatusListener.onLoggedIn();
        }
        super.onPageFinished(view, url);
    }

    public interface LoginStatusListener {
        void onLoggedIn();
    }

    public void setLoginStatusListener(LoginStatusListener loginStatusListener) {
        this.loginStatusListener = loginStatusListener;
    }


}
