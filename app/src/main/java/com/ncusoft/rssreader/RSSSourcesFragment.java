package com.ncusoft.rssreader;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ncusoft.rssreader.RSS.RSSManager;
import com.ncusoft.rssreader.RSS.RSSManagerInterface;
import com.ncusoft.rssreader.RSS.RSSSource;



public class RSSSourcesFragment extends Fragment {
    public static final int MSG_ADD_RSS_SOURCE = 1;
    public static final int MSG_DELETE_RSS_SOURCE = 2;
    public static final String RSS_SOURCE = "rss_source";
    public static void sendAddRSSSourceMsg(){
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = RSSSourcesFragment.MSG_ADD_RSS_SOURCE;
        handler.sendMessage(message);
    }

    public static void sendDeleteRSSSourceMsg(){
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = RSSSourcesFragment.MSG_DELETE_RSS_SOURCE;
        handler.sendMessage(message);
    }
    private List<RSSSource> rssSourceList;
    private RecyclerView rvRSSTitles;
    private FloatingActionButton fabAdd;
    private RSSTitlesAdapter adapter;
    private static Handler handler;
    private RSSManagerInterface manager;
    private int position;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            mainActivity = (MainActivity) context;
            manager = mainActivity.getManager();
            rssSourceList = manager.getRSSSourceList();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_ADD_RSS_SOURCE:
                        adapter.notifyDataSetChanged();
                        break;
                    case MSG_DELETE_RSS_SOURCE:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.deletion_question)
                                .setNegativeButton(R.string.negative, (dialog, which) -> {})
                                .setPositiveButton(R.string.positive, ((dialog, which) -> {
                                    manager.deleteRSSSource(rssSourceList.get(position));
                                    adapter.notifyDataSetChanged();
                                }))
                                .create()
                                .show();
                        break;
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_sources, container, false);
        rvRSSTitles = view.findViewById(R.id.rv_rss_titles);
        registerForContextMenu(rvRSSTitles);
        rvRSSTitles.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RSSTitlesAdapter();
        rvRSSTitles.setAdapter(adapter);
        fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            new GetNewRSSDialog(mainActivity).show();
        });
        return view;
    }

    class RSSTitlesAdapter extends RecyclerView.Adapter<RSSTitlesAdapter.ViewHolder> {

        public RSSTitlesAdapter(){
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_source, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnLongClickListener(v -> {
                position = holder.getAdapterPosition();
                return false;
            });
            holder.itemView.setOnClickListener(v -> {
                RSSSource source = rssSourceList.get(holder.getAdapterPosition());
                RSSItemsFragment fragment = RSSItemsFragment.newInstance(source);
                ((MainActivity)getActivity()).startFragment(fragment);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RSSSource source = rssSourceList.get(position);
            Log.i("Adapter", source.getTitle());
            holder.tvRSSTitle.setText(source.getTitle());
            if(source.getImage() != null){
                holder.ivRSSImage.setImageBitmap(source.getImage());
            }
        }

        @Override
        public int getItemCount() {
            return rssSourceList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView tvRSSTitle;
            public ImageView ivRSSImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRSSTitle = itemView.findViewById(R.id.tv_rss_title);
                ivRSSImage = itemView.findViewById(R.id.iv_rss_image);
            }
        }
    }

}