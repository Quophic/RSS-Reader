package com.ncusoft.rssreader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;
import com.ncusoft.rssreader.RSS.RSSInfo;

public class RSSTitlesAdapter extends RecyclerView.Adapter<RSSTitlesAdapter.ViewHolder> {
    private List<SubscribedRSSInfo> infoList;
    private MainActivity mainActivity;
    public RSSTitlesAdapter(List<SubscribedRSSInfo> infoList, Context context){
        this.infoList = infoList;
        mainActivity = (MainActivity) context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_title, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            SubscribedRSSInfo info = infoList.get(holder.getAdapterPosition());
            RSSItemsFragment fragment = RSSItemsFragment.newInstance(info);
            mainActivity.startFragment(fragment);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubscribedRSSInfo info = infoList.get(position);
        Log.i("Adapter", info.getTitle());
        holder.tvRSSTitle.setText(info.getTitle());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvRSSTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRSSTitle = itemView.findViewById(R.id.tv_rss_title);
        }
    }
}
