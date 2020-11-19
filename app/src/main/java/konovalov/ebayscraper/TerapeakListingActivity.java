package konovalov.ebayscraper;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import konovalov.ebayscraper.core.ItemsActiveAdapter;
import konovalov.ebayscraper.core.entities.TerapeakResult;

public class TerapeakListingActivity extends AppCompatActivity {

    private TerapeakResult result;
    private ItemsActiveAdapter itemsActiveAdapter;
    private ConstraintLayout soldItemsCl, activeItemsCl;

    private TextView soldBtn, activeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapeak_listing);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        result = (TerapeakResult) getIntent().getSerializableExtra("result");

        findViews();
        setListeners();
        showResultParams();
        initRecyclerViews();
        showActiveItems();
    }

    private void findViews() {
        soldBtn = findViewById(R.id.soldBtn);
        activeBtn = findViewById(R.id.activeBtn);
        soldItemsCl = findViewById(R.id.soldItemsCl);
        activeItemsCl = findViewById(R.id.activeItemsCl);
    }

    private void setListeners() {
        soldBtn.setOnClickListener(v -> showSoldItems());
        activeBtn.setOnClickListener(v -> showActiveItems());
    }

    private void showResultParams() {
        ((TextView) findViewById(R.id.queryTv)).setText(result.getQuery());
        ((TextView) findViewById(R.id.activeItemsTv)).setText(getNullable(result.getTotalActive()));
        ((TextView) findViewById(R.id.soldItemsTv)).setText(getNullable(result.getTotalSold()));
        ((TextView) findViewById(R.id.avgListedTv)).setText(getNullable(result.getAvgListingPrice()));
        ((TextView) findViewById(R.id.avgSoldTv)).setText(getNullable(result.getAvgSoldPrice()));
        ((TextView) findViewById(R.id.soldRatioTv)).setText(getNullable(result.getSoldRatio()));
        ((TextView) findViewById(R.id.currentValueTv)).setText(getNullable(result.getCurValue()));
    }

    private void initRecyclerViews() {
        itemsActiveAdapter = new ItemsActiveAdapter(result.getActiveItems(), this);
        ((RecyclerView) findViewById(R.id.activeItemsRv)).setAdapter(itemsActiveAdapter);


//        ((RecyclerView) findViewById(R.id.soldItemsRv)).setAdapter();
    }

    private void showActiveItems() {
        soldItemsCl.setVisibility(View.GONE);
        soldBtn.setBackgroundResource(R.drawable.bg_btn_tab);
        soldBtn.setTypeface(null, Typeface.NORMAL);

        activeItemsCl.setVisibility(View.VISIBLE);
        activeBtn.setBackgroundResource(R.drawable.bg_btn_tab_active);
        activeBtn.setTypeface(null, Typeface.BOLD);

    }

    private void showSoldItems() {
        soldItemsCl.setVisibility(View.VISIBLE);
        soldBtn.setBackgroundResource(R.drawable.bg_btn_tab_active);
        soldBtn.setTypeface(null, Typeface.BOLD);

        activeItemsCl.setVisibility(View.GONE);
        activeBtn.setBackgroundResource(R.drawable.bg_btn_tab);
        activeBtn.setTypeface(null, Typeface.NORMAL);

    }


    private static String getNullable(Number value) {
        if (value == null) return "n/a";
        else return String.valueOf(value);
    }

}