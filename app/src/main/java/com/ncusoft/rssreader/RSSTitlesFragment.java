package com.ncusoft.rssreader;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;



public class RSSTitlesFragment extends Fragment {
    private List<SubscribedRSSInfo> infoList;
    private RecyclerView rvRSSTitles;
    private FloatingActionButton fabAdd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBManager manager = new DBManager(getContext());
        infoList = manager.getAllSubscribedRSS();
        manager.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_titles, container, false);
        rvRSSTitles = view.findViewById(R.id.rv_rss_titles);
        rvRSSTitles.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvRSSTitles.setAdapter(new RSSTitlesAdapter());
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_title, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(v -> {
                SubscribedRSSInfo info = infoList.get(holder.getAdapterPosition());
                RSSItemsFragment fragment = RSSItemsFragment.newInstance(info);
                ((MainActivity)getActivity()).startFragment(fragment);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SubscribedRSSInfo info = infoList.get(position);
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