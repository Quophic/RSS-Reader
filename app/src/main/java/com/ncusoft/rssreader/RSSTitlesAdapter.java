package com.ncusoft.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.ncusoft.rssreader.RSS.RSSInfo;

public class RSSTitlesAdapter extends RecyclerView.Adapter<RSSTitlesAdapter.ViewHolder> {
    private List<RSSInfo> rssInfoList;
    private MainActivity mainActivity;
    public RSSTitlesAdapter(List<RSSInfo> rssInfoList, Context context){
        this.rssInfoList = rssInfoList;
        mainActivity = (MainActivity) context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_title, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            RSSInfo info = rssInfoList.get(holder.getAdapterPosition());
            RSSItemsFragment fragment = new RSSItemsFragment();
            fragment.setRssInfo(info);
            mainActivity.startFragment(fragment);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RSSInfo info = rssInfoList.get(position);
        holder.tvRSSTitle.setText(info.getTitle());
    }

    @Override
    public int getItemCount() {
        return rssInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvRSSTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRSSTitle = itemView.findViewById(R.id.tv_rss_title);
        }
    }
}
