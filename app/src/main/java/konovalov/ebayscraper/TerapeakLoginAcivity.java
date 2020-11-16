package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import konovalov.ebayscraper.core.TerapeakAuthenticator;

public class TerapeakLoginAcivity extends AppCompatActivity {

    private ProgressBar loginPb;
    private EditText loginEt;
    private EditText passwordEt;
    private TextView statusTv;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapeak_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViews();
        setListeners();
    }

    private void findViews() {
        statusTv = findViewById(R.id.loginStatusTv);
        loginEt = findViewById(R.id.loginEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        loginPb = findViewById(R.id.loginPb);
    }

    private void setListeners() {
        loginBtn.setOnClickListener(v -> {
            loginPb.setVisibility(View.VISIBLE);
            statusTv.setVisibility(View.INVISIBLE);
            loginBtn.setEnabled(false);
            AsyncTask.execute(() -> authenticator.login(loginEt.getText().toString(), passwordEt.getText().toString()));
        });
    }

    private final TerapeakAuthenticator authenticator = new TerapeakAuthenticator(loggedIn -> {
        if (loggedIn) new Intent(this, TerapeakActivity.class);
        else {
            loginBtn.setEnabled(true);
            statusTv.setText(R.string.falied_to_login);
            statusTv.setVisibility(View.VISIBLE);
            loginPb.setVisibility(View.INVISIBLE);
        }
    });
}
