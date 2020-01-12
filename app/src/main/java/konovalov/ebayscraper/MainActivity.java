package konovalov.ebayscraper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import konovalov.ebayscraper.core.Category;
import konovalov.ebayscraper.core.ItemsSeeker;
import konovalov.ebayscraper.core.Logger;
import konovalov.ebayscraper.core.ResultAdapter;
import konovalov.ebayscraper.core.UpcConvertor;
import konovalov.ebayscraper.core.entities.Release;
import konovalov.ebayscraper.core.entities.Result;

public class MainActivity extends AppCompatActivity implements
        ItemsSeeker.ResultsLoadingListener,
        UpcConvertor.ConvertorListener,
        Logger {

    private Spinner threadsSpn;
    private Spinner conditionSpn;
    private Spinner subcategorySpn;
    private EditText itemsLimitEt;
    private EditText inputQueriesEt;
    private EditText upcEt;
    private TextView categoryTv;

    private ImageButton searchBtn;
    private ImageButton stopBtn;
    private ImageButton clearBtn;
    private ImageButton selectBtn;
    private ImageButton backBtn;
    private ImageButton convertBtn;
    private ImageButton minimizeBtn;

    private ItemsSeeker itemsSeeker;
    private UpcConvertor convertor;
    private String appName;
    private String discogsToken;
    private Category category;
    private boolean isMinimized;


    private List<Result> results = new ArrayList<>();
    private Set<String> resultsSet = new HashSet<>();
    private List<String> notFoundUpcs = new ArrayList<>();

    ResultAdapter adapter;

    private SharedPreferences sPref;
    private SharedPreferences.Editor sPrefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sPref = getPreferences(MODE_PRIVATE);
        appName = getString(R.string.ebay_token);
        discogsToken = getString(R.string.discogs_token);

        inputQueriesEt = findViewById(R.id.inputQueriesEt);
        upcEt = findViewById(R.id.upcEt);

        threadsSpn = findViewById(R.id.threadsSpn);
        conditionSpn = findViewById(R.id.conditionSpn);
        subcategorySpn = findViewById(R.id.subcategorySpn);
        itemsLimitEt = findViewById(R.id.itemsLimitTf);
        categoryTv = findViewById(R.id.categoryTv);

        searchBtn = findViewById(R.id.searchBtn);
        stopBtn = findViewById(R.id.stopBtn);
        clearBtn = findViewById(R.id.clearBtn);
        selectBtn = findViewById(R.id.selectBtn);
        backBtn = findViewById(R.id.backBtn);
        convertBtn = findViewById(R.id.convertBtn);
        minimizeBtn = findViewById(R.id.minimizeBtn);
        setButtonListeners();

        adapter = new ResultAdapter(results, this);
        ((RecyclerView) findViewById(R.id.resultsRv)).setAdapter(adapter);

        Category.setAppName(appName);
        selectCategory("-1");

        threadsSpn.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));
        conditionSpn.setAdapter(ArrayAdapter.createFromResource(this, R.array.conditions, android.R.layout.simple_spinner_dropdown_item));

        sPref = getPreferences(MODE_PRIVATE);
        itemsLimitEt.setText(String.valueOf(sPref.getInt("itemsLimit", 250)));
        threadsSpn.setSelection(sPref.getInt("maxThreads", 5));

        setMinimized(false);

        stopBtn.setEnabled(false);
    }

    private void setButtonListeners() {
        searchBtn.setOnClickListener(v -> {
            if (inputQueriesEt.getText() == null || inputQueriesEt.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.no_queries), Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> queries = Arrays.asList(inputQueriesEt.getText().toString().split("\\r?\\n"));
            itemsSeeker = new ItemsSeeker(queries, appName, getCondition(), this);
            itemsSeeker.setLogger(this);
            itemsSeeker.setMaxThreads((int) threadsSpn.getSelectedItem());
            sPrefEditor = sPref.edit();
            sPrefEditor.putInt("maxThreads", threadsSpn.getSelectedItemPosition());
            try {
                if (itemsLimitEt.getText() != null && itemsLimitEt.getText().length() > 0) {
                    int itemsLimit = Integer.parseInt(itemsLimitEt.getText().toString());
                    sPrefEditor.putInt("itemsLimit", itemsLimit);
                    itemsSeeker.setItemsLimit(itemsLimit);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.incorrect_items_limit), Toast.LENGTH_SHORT).show();
                return;
            }
            sPrefEditor.apply();
            itemsSeeker.setCategoryId(category.getId());
            resultsSet.clear();
            results.clear();
            stopBtn.setEnabled(true);
            searchBtn.setEnabled(false);
            setMinimized(true);
            itemsSeeker.start();
        });

        stopBtn.setOnClickListener(v -> {
            if (itemsSeeker != null && itemsSeeker.isRunning()) itemsSeeker.stop();
        });

        clearBtn.setOnClickListener(v -> {
            stopBtn.performClick();
            clearOutput();
            stopBtn.setEnabled(false);
            searchBtn.setEnabled(true);
            inputQueriesEt.setText("");
            upcEt.setText("");
            setMinimized(false);
        });

        selectBtn.setOnClickListener(v -> {
            if (subcategorySpn.getSelectedItem() == null) return;
            String categoryId = category.getChildren().get(subcategorySpn.getSelectedItem().toString());
            if (categoryId != null) selectCategory(categoryId);
        });

        backBtn.setOnClickListener(v -> {
            if (category.getParentId() != null && !category.getParentId().equals("0")) selectCategory(category.getParentId());
        });

        convertBtn.setOnClickListener(v -> {
            if (upcEt.getText() == null || upcEt.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.no_upc), Toast.LENGTH_SHORT).show();
                return;
            }
            notFoundUpcs.clear();
            List<String> upcs = Arrays.stream(upcEt.getText().toString().split("\\r?\\n"))
                    .distinct()
                    .filter(u -> u.length() > 0)
                    .collect(Collectors.toList());
            convertor = new UpcConvertor(upcs, discogsToken, this);
            convertor.setLogger(this);
            log("UPCs conversion started");
            convertBtn.setEnabled(false);
            convertor.start();
        });

        minimizeBtn.setOnClickListener(v ->setMinimized(!isMinimized));
    }

    private void clearOutput(){
        resultsSet.clear();
        results.clear();
        adapter.notifyDataSetChanged();
    }

    public void log(String message) {
        Log.d("MyLog", message);
    }

    private ItemsSeeker.Condition getCondition(){
        if (conditionSpn.getSelectedItem().equals("New")) return ItemsSeeker.Condition.NEW;
        if (conditionSpn.getSelectedItem().equals("Used")) return ItemsSeeker.Condition.USED;
        return ItemsSeeker.Condition.ALL;
    }

    private void selectCategory(String categoryId) {
        Executors.newSingleThreadExecutor().submit(() -> {
            category = Category.findById(categoryId);
            if (category != null) setCategory(category);
        });
    }

    private void setCategory(Category category) {
        this.runOnUiThread(() -> {
            categoryTv.setText(category.getName());
            subcategorySpn.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(category.getChildren().keySet()))
            );
        });
    }

    private void refreshAdapter(){
        this.runOnUiThread(() -> adapter.notifyDataSetChanged());
    }

    public void setMinimized(boolean isMinimized) {
        this.isMinimized = isMinimized;
        ConstraintLayout inputCl = findViewById(R.id.inputCl);
        if (isMinimized) {
            inputCl.setVisibility(View.GONE);
            minimizeBtn.setImageResource(R.drawable.down);
        } else {
            inputCl.setVisibility(View.VISIBLE);
            minimizeBtn.setImageResource(R.drawable.up);
        }
    }

    @Override
    public void onResultReceived(Result result) {
        if (!resultsSet.contains(result.getQuery())) {
            resultsSet.add(result.getQuery());
            results.add(result);
        }
        refreshAdapter();
    }

    @Override
    public void onAllResultsReceived() {
        this.runOnUiThread(() -> stopBtn.setEnabled(false));
        this.runOnUiThread(() -> searchBtn.setEnabled(true));
    }

    @Override
    public void onUpcConverted(String upc, Release release) {
        String inputQueries = inputQueriesEt.getText().toString()
                + (inputQueriesEt.getText() == null || inputQueriesEt.getText().toString().isEmpty() ? "" : "\n")
                + release.getTitle();
        this.runOnUiThread(() -> inputQueriesEt.setText(inputQueries));
    }

    @Override
    public void onAllUpcConverted() {
        if (notFoundUpcs.isEmpty()) log("All UPCs converted");
        else
            log("UPCs conversion finished. The following UPCs were not found:\n"
                    + notFoundUpcs.stream().collect(Collectors.joining("\n")));
        this.runOnUiThread(() -> convertBtn.setEnabled(true));
    }

    @Override
    public void onUpcNotFound(String upc) {
        notFoundUpcs.add(upc);
    }


}
