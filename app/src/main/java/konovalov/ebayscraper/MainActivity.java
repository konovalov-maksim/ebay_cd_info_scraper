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
    private ProgressBar accountPb;
    private TextView accountTv;
    private Button loginBtn, logoutBtn;

    private TerapeakAuthenticator authenticator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        HttpClient.init(this);
        authenticator = new TerapeakAuthenticator();

        findViews();
        setListeners();

        refreshLoginStatus();
    }

    private void findViews() {
        ebayBtn = findViewById(R.id.ebaySearchBtn);
        terapeakBtn = findViewById(R.id.terapeakSearchBtn);
        accountPb = findViewById(R.id.accountPb);
        accountTv = findViewById(R.id.accountTv);
        loginBtn = findViewById(R.id.loginBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
    }

    private void setListeners() {
        ebayBtn.setOnClickListener(v -> startActivity(new Intent(this, EbayActivity.class)));
        terapeakBtn.setOnClickListener(v -> startActivity(new Intent(this, TerapeakActivity.class)));
        loginBtn.setOnClickListener(v -> startLoginActivity());
        logoutBtn.setOnClickListener(v -> logout());
    }

    private void refreshLoginStatus() {
        terapeakBtn.setEnabled(false);
        loginBtn.setVisibility(View.GONE);
        logoutBtn.setVisibility(View.GONE);
        accountPb.setVisibility(View.VISIBLE);
        accountTv.setText(R.string.login_status_update);

        authenticator.getLogin(this::onLoginReceived);
    }

    private void onLoginReceived(String login) {
        runOnUiThread(() -> {
            if (login != null) {
                terapeakBtn.setEnabled(true);
                loginBtn.setVisibility(View.GONE);
                logoutBtn.setVisibility(View.VISIBLE);
                accountPb.setVisibility(View.INVISIBLE);
                accountTv.setText(getString(R.string.logged_in_as, login));
            } else {
                terapeakBtn.setEnabled(false);
                loginBtn.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.GONE);
                accountPb.setVisibility(View.INVISIBLE);
                accountTv.setText(R.string.not_logged_in);
            }
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginAcivity.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshLoginStatus();
    }

    private void logout() {
        authenticator.logOut();
        refreshLoginStatus();
    }

}
