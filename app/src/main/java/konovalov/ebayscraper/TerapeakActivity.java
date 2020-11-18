package konovalov.ebayscraper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import konovalov.ebayscraper.core.*;
import konovalov.ebayscraper.core.entities.Release;
import konovalov.ebayscraper.core.entities.Result;
import konovalov.ebayscraper.core.entities.TerapeakResult;
import konovalov.ebayscraper.core.terapeak.TerapeakItemsSeeker;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TerapeakActivity extends AppCompatActivity implements
        TerapeakItemsSeeker.ResultsLoadingListener,
        UpcConvertor.ConvertorListener,
        Logger {
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    private Spinner threadsSpn;
    private Spinner conditionSpn;
    private Spinner subcategorySpn;
    private EditText itemsLimitEt;
    private EditText inputQueriesEt;
    private EditText upcEt;
    private TextView categoryTv;

    private ProgressBar upcPb;
    private ProgressBar categoryPb;
    private ProgressBar resultsPb;

    private ImageButton searchBtn;
    private ImageButton stopBtn;
    private ImageButton clearBtn;
    private ImageButton optionsBtn;
    private ImageButton minimizeBtn;
    private ImageButton selectBtn;
    private ImageButton backBtn;
    private ImageButton convertBtn;
    private ImageButton scanBtn;

    private ConstraintLayout optionsCl;

    private TerapeakItemsSeeker itemsSeeker;
    private UpcConvertor convertor;
    private String discogsToken;
    private Category category;
    private boolean isMinimized;


    private final List<Result> results = new ArrayList<>();
    private final Set<String> resultsSet = new HashSet<>();
    private final List<String> notFoundUpcs = new ArrayList<>();

    private ResultAdapter adapter;

    private SharedPreferences sPref;
    private SharedPreferences.Editor sPrefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terapeak);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sPref = getPreferences(MODE_PRIVATE);
        String appName = getString(R.string.ebay_token);
        discogsToken = getString(R.string.discogs_token);

        inputQueriesEt = findViewById(R.id.inputQueriesEt);
        upcEt = findViewById(R.id.upcEt);

        threadsSpn = findViewById(R.id.threadsSpn);
        conditionSpn = findViewById(R.id.conditionSpn);
        subcategorySpn = findViewById(R.id.subcategorySpn);
        itemsLimitEt = findViewById(R.id.itemsLimitTf);
        categoryTv = findViewById(R.id.categoryTv);

        upcPb = findViewById(R.id.upcPb);
        categoryPb = findViewById(R.id.categoryPb);
        resultsPb = findViewById(R.id.resultsPb);
        upcPb.setVisibility(View.INVISIBLE);
        categoryPb.setVisibility(View.INVISIBLE);
        resultsPb.setVisibility(View.INVISIBLE);

        searchBtn = findViewById(R.id.searchBtn);
        stopBtn = findViewById(R.id.stopBtn);
        clearBtn = findViewById(R.id.clearBtn);
        selectBtn = findViewById(R.id.selectBtn);
        backBtn = findViewById(R.id.backBtn);
        convertBtn = findViewById(R.id.convertBtn);
        minimizeBtn = findViewById(R.id.minimizeBtn);
        optionsBtn = findViewById(R.id.optionsBtn);
        scanBtn = findViewById(R.id.scanBtn);
        setButtonListeners();

        optionsCl = findViewById(R.id.optionsCl);
        optionsCl.setVisibility(View.GONE);

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
        searchBtn.setOnClickListener(v -> startSearch());
        stopBtn.setOnClickListener(v -> stopSearch());
        clearBtn.setOnClickListener(v -> clearResults());
        selectBtn.setOnClickListener(v -> selectCategory());
        backBtn.setOnClickListener(v -> selectParentCategory());
        convertBtn.setOnClickListener(v -> convertUpcList());
        optionsBtn.setOnClickListener(v -> {
            if (optionsCl.getVisibility() == View.VISIBLE)
                optionsCl.setVisibility(View.GONE);
            else
                optionsCl.setVisibility(View.VISIBLE);
        });
        minimizeBtn.setOnClickListener(v -> setMinimized(!isMinimized));
        scanBtn.setOnClickListener(v -> launchActivity(ScannerActivity.class));
    }

    private void startSearch() {
        //TODO uncomment
/*        if (inputQueriesEt.getText() == null || inputQueriesEt.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_queries), Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> queries = Arrays.asList(inputQueriesEt.getText().toString().split("\\r?\\n"));*/
        List<String> queries = Arrays.asList("elvis");

        itemsSeeker = new TerapeakItemsSeeker(queries, getCondition(), this);
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
        resultsPb.setVisibility(View.VISIBLE);
        itemsSeeker.start();
    }

    private void stopSearch() {
        if (itemsSeeker != null && itemsSeeker.isRunning()) itemsSeeker.stop();
    }

    private void clearResults() {
        stopBtn.performClick();
        clearOutput();
        stopBtn.setEnabled(false);
        searchBtn.setEnabled(true);
        inputQueriesEt.setText("");
        upcEt.setText("");
        setMinimized(false);
    }

    private void selectCategory() {
        if (subcategorySpn.getSelectedItem() == null) return;
        String categoryId = category.getChildren().get(subcategorySpn.getSelectedItem().toString());
        if (categoryId != null) selectCategory(categoryId);
    }

    private void selectCategory(String categoryId) {
        categoryPb.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().submit(() -> {
            category = Category.findById(categoryId);
            if (category != null) setCategory(category);
        });
    }

    private void selectParentCategory() {
        if (category.getParentId() != null && !category.getParentId().equals("0")) selectCategory(category.getParentId());
    }

    private void convertUpcList() {
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
        upcPb.setVisibility(View.VISIBLE);
        convertor.start();
    }

    private void clearOutput(){
        resultsSet.clear();
        results.clear();
        adapter.notifyDataSetChanged();
    }

    public void log(String message) {
        Log.d("MyLog", message);
    }

    private Condition getCondition(){
        if (conditionSpn.getSelectedItem().equals("New")) return Condition.NEW;
        if (conditionSpn.getSelectedItem().equals("Used")) return Condition.USED;
        return Condition.ALL;
    }

    private void setCategory(Category category) {
        this.runOnUiThread(() -> {
            categoryTv.setText(category.getName());
            categoryPb.setVisibility(View.INVISIBLE);
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
            optionsCl.setVisibility(View.GONE);
            minimizeBtn.setImageResource(R.drawable.down);
        } else {
            inputCl.setVisibility(View.VISIBLE);
            minimizeBtn.setImageResource(R.drawable.up);
        }
    }

    @Override
    public void onResultReceived(TerapeakResult result) {
        Log.d("seeker", "Result received in activity: " + result);
        /*        if (!resultsSet.contains(result.getQuery())) {
            resultsSet.add(result.getQuery());
            results.add(result);
        }
        refreshAdapter();*/
    }

    @Override
    public void onAllResultsReceived() {
        this.runOnUiThread(() -> {
            stopBtn.setEnabled(false);
            searchBtn.setEnabled(true);
            resultsPb.setVisibility(View.INVISIBLE);
        });
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
        this.runOnUiThread(() -> {
            convertBtn.setEnabled(true);
            upcPb.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public void onUpcNotFound(String upc) {
        notFoundUpcs.add(upc);
    }

    @Override
    public void onBackPressed() {
        if (!isMinimized) super.onBackPressed();
        else setMinimized(false);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String upc = data.getStringExtra("barcode");
                String upcs = upcEt.getText().toString()
                        + (upcEt.getText() == null || upcEt.getText().toString().isEmpty() ? "" : "\n")
                        + upc;
                runOnUiThread(() -> upcEt.setText(upcs));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == ZBAR_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mClss != null) {
                    Intent intent = new Intent(this, mClss);
                    startActivity(intent);
                }
            } else
                Toast.makeText(this, getString(R.string.needs_camera_permission), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN &&
            event.getAction() == KeyEvent.ACTION_DOWN) {
            scanBtn.performClick();
            return true;
        }
        else return super.dispatchKeyEvent(event);
    }
}
