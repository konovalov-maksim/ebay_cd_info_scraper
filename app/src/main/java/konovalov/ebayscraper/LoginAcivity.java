package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import konovalov.ebayscraper.core.TerapeakAuthenticator;

public class LoginAcivity extends AppCompatActivity {

    private WebView loginWv;
    private LoginWebViewClient webViewClient = new LoginWebViewClient();
//    private LoginWebViewClient.LoginStatusListener loginStatusListener;
    private TerapeakAuthenticator authenticator;

//    private final String loginUrl = "https://signin.ebay.com/ws/eBayISAPI.dll?SignIn&UsingSSL=1&siteid=0&co_partnerId=2&pageType=2553753&ru=https%3A%2F%2Fwww.ebay.com%2Fsh%2Fresearch";
    private final String loginUrl = "https://signin.ebay.com/signin/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        CookieManager.getInstance().removeAllCookies(null);
        loginWv = findViewById(R.id.loginWv);
        loginWv.setWebViewClient(webViewClient);
        loginWv.getSettings().setJavaScriptEnabled(true);
        loginWv.loadUrl(loginUrl);

        webViewClient.setLoginStatusListener(() -> authenticator.checkIfLoggedIn());

        authenticator = new TerapeakAuthenticator(loggedIn -> {
            if (loggedIn) new Intent(this, TerapeakActivity.class);
            else {
                Log.d("cookies", "Login failed");
                loginWv.loadUrl(loginUrl);
            }
        });
    }


}
