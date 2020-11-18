package konovalov.ebayscraper;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import konovalov.ebayscraper.core.entities.TerapeakResult;

public class TerapeakListingActivity extends AppCompatActivity {

    private TerapeakResult result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapeak_listing);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        result = (TerapeakResult) getIntent().getSerializableExtra("result");

        showResult();
    }

    private void showResult() {
        ((TextView) findViewById(R.id.queryTv)).setText(result.getQuery());
        ((TextView) findViewById(R.id.activeItemsTv)).setText(getNullable(result.getTotalActive()));
        ((TextView) findViewById(R.id.soldItemsTv)).setText(getNullable(result.getTotalSold()));
        ((TextView) findViewById(R.id.avgListedTv)).setText(getNullable(result.getAvgListingPrice()));
        ((TextView) findViewById(R.id.avgSoldTv)).setText(getNullable(result.getAvgSoldPrice()));
        ((TextView) findViewById(R.id.soldRatioTv)).setText(getNullable(result.getSoldRatio()));
        ((TextView) findViewById(R.id.currentValueTv)).setText(getNullable(result.getCurValue()));

    }

    private static String getNullable(Number value) {
        if (value == null) return "n/a";
        else return String.valueOf(value);
    }


}