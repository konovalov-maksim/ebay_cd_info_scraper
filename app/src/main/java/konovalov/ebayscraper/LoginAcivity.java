package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import konovalov.ebayscraper.core.HttpClient;
import konovalov.ebayscraper.core.TerapeakAuthenticator;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginAcivity extends AppCompatActivity {

    private WebView loginWv;
    private LoginWebViewClient webViewClient;
//    private LoginWebViewClient.LoginStatusListener loginStatusListener;
    private TerapeakAuthenticator authenticator;

//    private final String loginUrl = "https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&UsingSSL=1&siteid=0&co_partnerId=2&pageType=2553753&ru=https%3A%2F%2Fwww.ebay.com%2Fsh%2Fresearch";
    private final String securedPageUrl = "https://www.ebay.com/myb/Summary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        webViewClient = new LoginWebViewClient(authenticator::checkIfLoggedIn, securedPageUrl);

        CookieManager.getInstance().removeAllCookies(null);
        loginWv = findViewById(R.id.loginWv);
        loginWv.setWebViewClient(webViewClient);
        loginWv.getSettings().setJavaScriptEnabled(true);
        loginWv.loadUrl(securedPageUrl);


        authenticator = new TerapeakAuthenticator(loggedIn -> {
            if (loggedIn) new Intent(this, TerapeakActivity.class);
            else {
                Log.d("cookies", "Login failed");
                loginWv.loadUrl(securedPageUrl);
            }
        });
    }

    private void saveCookiesAndTryLogin(String cookiesStr) {
        saveLoginCookies(cookiesStr);
        authenticator.checkIfLoggedIn();
    }

    private void saveLoginCookies(String cookiesStr) {
        Log.d("cookies", "cookiesStr " + cookiesStr);
        List<Cookie> cookies = new ArrayList<>();
        HttpUrl url = HttpUrl.parse("https://www.ebay.com");
        for (String cookiePair : cookiesStr.split(";")) {
            String setCookie = cookiePair.trim() + "; Domain=.ebay.com; Path=/; HttpOnly";
            Log.d("cookies", "setCookie " + setCookie);
            Cookie cookie = Cookie.parse(url, setCookie);
            Log.d("cookies", "cookie " + cookie);
            cookies.add(cookie);
        }
//        OkHttpClient client = HttpClient.getInstance();
        Log.d("cookies", "Found " + cookies.size() + " cookies");
        HttpClient.getInstance().cookieJar().saveFromResponse(url, cookies);
        Log.d("cookies", "Cookies saved");
    }
}
