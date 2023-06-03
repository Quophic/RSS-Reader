package com.ncusoft.rssreader;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ncusoft.rssreader.DataBase.Contract.RSSItemsContract;
import com.ncusoft.rssreader.RSS.RSSManagerInterface;
import com.ncusoft.rssreader.RSS.RSSSource;
import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSItem;
import com.ncusoft.rssreader.RSS.RSSUtils;
import com.ncusoft.rssreader.RSS.UpdateRSSService;

import java.text.SimpleDateFormat;
import java.util.List;

public class RSSItemsFragment extends Fragment {
    private static final int MSG_UPDATE_RSS_ITEM = 1;
    private static final String PARAM = "param";
    private RecyclerView rvRSSItems;
    private List<RSSItem> rssItemList = null;
    private RSSSource source;
    private RSSManagerInterface manager;
    private String originActionBarTitle;
    private MainActivity context;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MSG_UPDATE_RSS_ITEM:
                    rssItemList = manager.getRSSItemList(source);
                    rvRSSItems.setAdapter(new RSSItemsAdapter());
                    Toast.makeText(getContext(), "已是最新", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                String action = intent.getAction();
                Message message = new Message();
                switch (action) {
                    case UpdateRSSService.BROADCAST_UPDATE_RSS_ITEM:
                        message.what = MSG_UPDATE_RSS_ITEM;
                        break;
                }
                handler.sendMessage(message);
            }
        }
    };
    public static RSSItemsFragment newInstance(RSSSource info) {
        Bundle args = new Bundle();
        args.putSerializable(PARAM, info);
        RSSItemsFragment fragment = new RSSItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateRSSService.BROADCAST_UPDATE_RSS_ITEM);
        context.registerReceiver(receiver, intentFilter);
        if(context instanceof MainActivity){
            this.context = (MainActivity)context;
            manager = this.context.getManager();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                source = getArguments().getSerializable(PARAM, RSSSource.class);
            }else{
                source = (RSSSource) getArguments().getSerializable(PARAM);
            }
        }
        UpdateRSSService.startActionUpdateRSSItem(getContext(), source.getLink(), source.getId());
        originActionBarTitle = context.getSupportActionBar().getTitle().toString();
        context.getSupportActionBar().setTitle(source.getTitle());
    }

    @Override
    public void onDetach() {
        getContext().unregisterReceiver(receiver);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.getSupportActionBar().setTitle(originActionBarTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_items, container, false);
        rvRSSItems = view.findViewById(R.id.rv_rss_items);
        rvRSSItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rssItemList = manager.getRSSItemList(source);
        rvRSSItems.setAdapter(new RSSItemsAdapter());
        return view;
    }
    class RSSItemsAdapter extends RecyclerView.Adapter< RSSItemsAdapter.ViewHolder> {
        private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 E");
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(v -> {
                RSSItem item = rssItemList.get(holder.getAdapterPosition());
                WebViewFragment fragment = WebViewFragment.newInstance(item.getLink());
                ((MainActivity)getActivity()).startFragment(fragment);
                item.setStatus(RSSItemsContract.STATUS_READ);
                manager.setRSSItemRead(item);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RSSItem item = rssItemList.get(position);
            holder.tvItemTitle.setText(item.getTitle());
            if(item.getStatus() == RSSItemsContract.STATUS_NEVER){
                holder.tvItemStatus.setText(R.string.not_read);
            }
            if(item.getPubDate() != null){
                holder.tvPubDate.setText(format.format(item.getPubDate()));
            }
        }

        @Override
        public int getItemCount() {
            return rssItemList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView tvItemTitle;
            public TextView tvItemStatus;
            public TextView tvPubDate;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemTitle = itemView.findViewById(R.id.tv_item_title);
                tvItemStatus = itemView.findViewById(R.id.tv_item_status);
                tvPubDate = itemView.findViewById(R.id.tv_pub_date);
            }
        }
    }

}