package com.ncusoft.rssreader;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;
import com.ncusoft.rssreader.RSS.RSSInfo;


public class RSSTitlesFragment extends Fragment {
    private List<SubscribedRSSInfo> infoList;
    private RecyclerView rvRSSTitles;
    private FloatingActionButton fabAdd;
    public RSSTitlesFragment(){
    }
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
        rvRSSTitles.setAdapter(new RSSTitlesAdapter(infoList, getContext()));
        fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            new InputDialog(getContext()).show();
        });
        return view;
    }
}