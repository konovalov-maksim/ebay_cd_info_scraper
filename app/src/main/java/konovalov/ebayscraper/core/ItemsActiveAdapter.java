package konovalov.ebayscraper.core;

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
import konovalov.ebayscraper.core.entities.ItemActive;

public class ItemsActiveAdapter extends RecyclerView.Adapter<ItemsActiveAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ItemActive> items;
    private final Context context;

    public ItemsActiveAdapter(List<ItemActive> items, Context context) {
        this.items = items;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.active_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemActive item = items.get(position);
        holder.titleTv.setText(item.getTitle());
        holder.formatTv.setText(item.getFormat());
        holder.listingPriceTv.setText(item.getListingPrice());
        holder.shippingTv.setText(item.getShipping());
        holder.bidsTv.setText(context.getString(R.string.bids, item.getBids()));
        holder.watchersTv.setText(context.getString(R.string.watches, item.getWatchers()));
        holder.startDateTv.setText(item.getStartDate());
        Glide.with(context).load(item.getImgUrl()).into(holder.itemIv);
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView titleTv, formatTv, listingPriceTv, shippingTv, bidsTv, watchersTv, startDateTv;
        final ImageView itemIv;

        ViewHolder(View view){
            super(view);
            titleTv = view.findViewById(R.id.titleTv);
            formatTv = view.findViewById(R.id.formatTv);
            listingPriceTv = view.findViewById(R.id.listingPriceTv);
            shippingTv = view.findViewById(R.id.shippingTv);
            bidsTv = view.findViewById(R.id.bidsTv);
            watchersTv = view.findViewById(R.id.watchersTv);
            startDateTv = view.findViewById(R.id.startDateTv);
            itemIv = view.findViewById(R.id.itemIv);
        }
    }

/*    private static String getNullable(Number value) {
        if (value == null) return "n/a";
        else return String.valueOf(value);
    }*/
}
