package konovalov.ebayscraper;

import android.util.Log;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginWebViewClient extends WebViewClient {

    private final WebViewLoginListener webViewLoginListener;

    private final String securedPageUrl;

    public LoginWebViewClient(WebViewLoginListener webViewLoginListener, String securedPageUrl) {
        this.webViewLoginListener = webViewLoginListener;
        this.securedPageUrl = securedPageUrl;
    }

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
        Log.d("cookies", "Page loaded: " + url);
        if (url.equals(securedPageUrl)) {
            String cookies = CookieManager.getInstance().getCookie("https://www.ebay.com/sh/research/api/search");
            Log.d("cookies", url + " ::: " + cookies);
            webViewLoginListener.onLoggedIn(cookies);
        }
        super.onPageFinished(view, url);
    }

    public interface WebViewLoginListener {
        void onLoggedIn(String cookies);
    }

}
