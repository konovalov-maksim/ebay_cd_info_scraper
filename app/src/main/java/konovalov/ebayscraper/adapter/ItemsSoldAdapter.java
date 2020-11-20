package konovalov.ebayscraper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import konovalov.ebayscraper.R;
import konovalov.ebayscraper.core.entities.ItemSold;

public class ItemsSoldAdapter extends RecyclerView.Adapter<ItemsSoldAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ItemSold> items;
    private final Context context;

    public ItemsSoldAdapter(List<ItemSold> items, Context context) {
        this.items = items;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sold, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSold item = items.get(position);
        holder.titleTv.setText(item.getTitle());
        holder.formatTv.setText(item.getFormat());
        holder.avgSoldPriceTv.setText(item.getAvgSoldPrice());
        holder.shippingTv.setText(item.getShipping());
        holder.totalSoldTv.setText(context.getString(R.string.total_sold, item.getTotalSold()));
        holder.totalSalesTv.setText(context.getString(R.string.total_sales, item.getTotalSales()));
        holder.lastSoldDateTv.setText(item.getLastSold());
        Glide.with(context).load(item.getImgUrl()).into(holder.itemIv);
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTv, formatTv, avgSoldPriceTv, shippingTv, totalSoldTv, totalSalesTv, lastSoldDateTv;
        final ImageView itemIv;

        ViewHolder(View view){
            super(view);
            titleTv = view.findViewById(R.id.titleTv);
            formatTv = view.findViewById(R.id.formatTv);
            avgSoldPriceTv = view.findViewById(R.id.avgSoldPriceTv);
            shippingTv = view.findViewById(R.id.shippingTv);
            totalSoldTv = view.findViewById(R.id.totalSoldTv);
            totalSalesTv = view.findViewById(R.id.totalSalesTv);
            lastSoldDateTv = view.findViewById(R.id.lastSoldDateTv);
            itemIv = view.findViewById(R.id.itemIv);
        }
    }

}
