package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import konovalov.ebayscraper.core.HttpClient;
import konovalov.ebayscraper.core.TerapeakAuthenticator;

public class MainActivity extends AppCompatActivity {

    private TerapeakAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        HttpClient.init(this);
        setListeners();

        authenticator = new TerapeakAuthenticator(loggedIn -> {
            if (loggedIn) startActivity(new Intent(this, TerapeakActivity.class));
            else startActivity(new Intent(this, LoginAcivity.class));
        });
    }



    private void setListeners() {
        findViewById(R.id.ebaySearchBtn).setOnClickListener(v -> startActivity(new Intent(this, EbayActivity.class)));
        findViewById(R.id.terapeakSearchBtn).setOnClickListener(v -> authenticator.checkIfLoggedIn());
    }
}
