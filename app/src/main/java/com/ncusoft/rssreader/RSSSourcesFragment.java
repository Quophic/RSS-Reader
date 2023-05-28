package com.ncusoft.rssreader;


import android.app.AlertDialog;
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
import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.RSS.RSSSource;



public class RSSSourcesFragment extends Fragment {
    public static final int MSG_REFRESH = 1;
    public static final int MSG_DELETE = 2;
    public static final String SUB_RSS_INFO = "sub_rss_info";
    public static final String POSITION = "position";
    public static void sendRefreshMsg(RSSSource info){
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = RSSSourcesFragment.MSG_REFRESH;
        Bundle bundle = new Bundle();
        bundle.putSerializable(RSSSourcesFragment.SUB_RSS_INFO, info);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public static void sendDeleteMsg(){
        if(handler == null){
            return;
        }
        Message message = new Message();
        message.what = RSSSourcesFragment.MSG_DELETE;
        handler.sendMessage(message);
    }
    private List<RSSSource> infoList;
    private RecyclerView rvRSSTitles;
    private FloatingActionButton fabAdd;
    private RSSTitlesAdapter adapter;
    private static Handler handler;
    private DBManager manager;
    private int position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new DBManager(getContext());
        infoList = manager.getAllSubscribedRSS();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                switch (msg.what){
                    case MSG_REFRESH:
                        RSSSource info = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            info = bundle.getSerializable(SUB_RSS_INFO, RSSSource.class);
                        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            info = bundle.getSerializable(SUB_RSS_INFO, null);
                        }
                        if(info == null){
                            return;
                        }
                        infoList.add(info);
                        adapter.notifyDataSetChanged();
                        break;
                    case MSG_DELETE:
                        new AlertDialog.Builder(getContext())
                                .setTitle(R.string.deletion_question)
                                .setNegativeButton(R.string.negative, (dialog, which) -> {})
                                .setPositiveButton(R.string.positive, ((dialog, which) -> {
                                    manager.deleteSubscribedRSS(infoList.get(position));
                                    infoList.remove(position);
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
            new GetNewRSSDialog(getContext()).show();
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
                RSSSource info = infoList.get(holder.getAdapterPosition());
                RSSItemsFragment fragment = RSSItemsFragment.newInstance(info);
                ((MainActivity)getActivity()).startFragment(fragment);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RSSSource info = infoList.get(position);
            Log.i("Adapter", info.getTitle());
            holder.tvRSSTitle.setText(info.getTitle());
            if(info.getImage() != null){
                holder.ivRSSImage.setImageBitmap(info.getImage());
            }
        }

        @Override
        public int getItemCount() {
            return infoList.size();
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