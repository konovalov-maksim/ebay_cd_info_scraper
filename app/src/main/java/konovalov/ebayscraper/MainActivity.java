package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import konovalov.ebayscraper.core.HttpClient;
import konovalov.ebayscraper.core.TerapeakAuthenticator;

public class MainActivity extends AppCompatActivity {

    private Button ebayBtn, terapeakBtn;
    private ProgressBar loginPb;
    private TextView tryingToLoginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        HttpClient.init(this);

        findViews();
        setListeners();

    }

    private void findViews() {
        ebayBtn = findViewById(R.id.ebaySearchBtn);
        terapeakBtn = findViewById(R.id.terapeakSearchBtn);
        loginPb = findViewById(R.id.loginPb);
        tryingToLoginTv = findViewById(R.id.tryingToLoginTv);
    }


    private void setListeners() {
        ebayBtn.setOnClickListener(v -> startActivity(new Intent(this, EbayActivity.class)));
        terapeakBtn.setOnClickListener(v -> checkIfLoggedIn());
    }

    private void checkIfLoggedIn() {
        loginPb.setVisibility(View.VISIBLE);
        tryingToLoginTv.setVisibility(View.VISIBLE);
        ebayBtn.setEnabled(false);
        terapeakBtn.setEnabled(false);

        TerapeakAuthenticator authenticator = new TerapeakAuthenticator(this::onLoginStatusReceived);
        authenticator.checkIfLoggedIn();
    }

    private void onLoginStatusReceived(boolean isLoggedIn) {
        runOnUiThread(() -> {
            loginPb.setVisibility(View.INVISIBLE);
            tryingToLoginTv.setVisibility(View.INVISIBLE);
            ebayBtn.setEnabled(true);
            terapeakBtn.setEnabled(true);
            if (isLoggedIn)
                startActivity(new Intent(this, TerapeakActivity.class));
            else
                startActivity(new Intent(this, LoginAcivity.class));
        });
    }
}
