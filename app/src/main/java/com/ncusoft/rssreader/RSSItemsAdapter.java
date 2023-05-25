package com.ncusoft.rssreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import RSS.RSSItem;

public class RSSItemsAdapter extends RecyclerView.Adapter< RSSItemsAdapter.ViewHolder> {
    private List<RSSItem> rssItemList;
    public RSSItemsAdapter(List<RSSItem> rssItemList){
        this.rssItemList = rssItemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RSSItem item = rssItemList.get(position);
        holder.tvItemTitle.setText(item.getTitle());
        holder.tvItemDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return rssItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemTitle;
        public TextView tvItemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            tvItemDescription = itemView.findViewById(R.id.tv_item_description);
        }
    }
}
