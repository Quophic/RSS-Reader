package com.ncusoft.rssreader;

import android.app.WallpaperManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;
import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSItem;
import com.ncusoft.rssreader.RSS.RSSUtils;

import java.util.List;

public class RSSItemsFragment extends Fragment {
    private static final String PARAM = "param";
    private RecyclerView rvRSSItems;
    private List<RSSItem> rssItemList;
    private SubscribedRSSInfo info;
    public static RSSItemsFragment newInstance(SubscribedRSSInfo info) {

        Bundle args = new Bundle();
        args.putSerializable(PARAM, info);
        RSSItemsFragment fragment = new RSSItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                info = getArguments().getSerializable(PARAM, SubscribedRSSInfo.class);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                info = getArguments().getSerializable(PARAM, null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_items, container, false);
        rvRSSItems = view.findViewById(R.id.rv_rss_items);
        rvRSSItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new RSSTask(info.getLink()).execute();
        return view;
    }

    class RSSTask extends AsyncTask<RSSInfo, Void, RSSInfo> {
        private String link;
        public RSSTask(String link){
            this.link = link;
        }
        @Override
        protected RSSInfo doInBackground(RSSInfo... rssInfos) {
            try {
                return RSSUtils.getRSSInfoFromUrl(link);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(RSSInfo rssInfo) {
            rssItemList = rssInfo.getItems();
            rvRSSItems.setAdapter(new RSSItemsAdapter());
        }
    }

    class RSSItemsAdapter extends RecyclerView.Adapter< RSSItemsAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(v -> {
                String url = rssItemList.get(holder.getAdapterPosition()).getLink();
                WebViewFragment fragment = WebViewFragment.newInstance(url);
                ((MainActivity)getActivity()).startFragment(fragment);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RSSItem item = rssItemList.get(position);
            holder.tvItemTitle.setText(item.getTitle());
        }

        @Override
        public int getItemCount() {
            return rssItemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView tvItemTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            }
        }
    }

}