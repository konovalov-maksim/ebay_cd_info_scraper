package konovalov.ebayscraper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

import konovalov.ebayscraper.core.TerapeakAuthenticator;

public class LoginAcivity extends AppCompatActivity {

    private WebView loginWv;
    private LoginWebViewClient webViewClient;
    private TerapeakAuthenticator authenticator;

    private final String securedPageUrl = "https://www.ebay.com/sh/research/";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        authenticator = new TerapeakAuthenticator(this::onLoginStatusReceived);
        webViewClient = new LoginWebViewClient(authenticator::checkIfLoggedIn, securedPageUrl);

        loginWv = findViewById(R.id.loginWv);
        loginWv.setWebViewClient(webViewClient);
        loginWv.getSettings().setJavaScriptEnabled(true);
        tryToLogin();
    }

    private void tryToLogin() {
        CookieManager.getInstance().removeAllCookies(aBoolean -> loginWv.loadUrl(securedPageUrl));
    }

    private void onLoginStatusReceived(boolean isLoggedIn) {
        if (isLoggedIn)
            startActivity(new Intent(this, TerapeakActivity.class));
        else
            tryToLogin();
    }

}
