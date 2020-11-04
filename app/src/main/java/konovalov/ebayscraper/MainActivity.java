package konovalov.ebayscraper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        findViewById(R.id.ebaySearchBtn).setOnClickListener(v -> startActivity(new Intent(this, EbayActivity.class)));

        findViewById(R.id.terapeakSearchBtn).setOnClickListener(v -> startActivity(new Intent(this, TerapeakActivity.class)));


    }

}
