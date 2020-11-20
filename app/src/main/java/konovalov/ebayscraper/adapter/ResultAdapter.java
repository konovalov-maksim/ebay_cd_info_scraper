package konovalov.ebayscraper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import konovalov.ebayscraper.R;
import konovalov.ebayscraper.core.entities.Result;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Result> results;

    public ResultAdapter(List<Result> results, Context context) {
        this.results = results;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.queryTv.setText(result.getQuery());
        holder.statusTv.setText(result.getStatusString());
        holder.activeItemsTv.setText(String.valueOf(result.getActiveItemsFound()));
        holder.soldItemsTv.setText(String.valueOf(result.getSoldItems()));
        holder.avgListedTv.setText(String.valueOf(result.getAvgPriceListed()));
        holder.avgSoldTv.setText(String.valueOf(result.getAvgPriceSold()));
        holder.soldRatioTv.setText(result.getSoldRatioString());
        holder.curValTv.setText(String.valueOf(result.getCurValue()));
    }

    @Override
    public int getItemCount() {
        if (results == null) return 0;
        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView queryTv, statusTv, activeItemsTv, soldItemsTv, avgListedTv, avgSoldTv, soldRatioTv, curValTv;

        ViewHolder(View view){
            super(view);
            queryTv = view.findViewById(R.id.queryTv);
            statusTv = view.findViewById(R.id.statusTv);
            activeItemsTv = view.findViewById(R.id.activeItemsTv);
            soldItemsTv = view.findViewById(R.id.soldItemsTv);
            avgListedTv = view.findViewById(R.id.avgListedTv);
            avgSoldTv = view.findViewById(R.id.avgSoldTv);
            soldRatioTv = view.findViewById(R.id.soldRatioTv);
            curValTv = view.findViewById(R.id.curValTv);
        }

    }
}
