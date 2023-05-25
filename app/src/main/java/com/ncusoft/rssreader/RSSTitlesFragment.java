package com.ncusoft.rssreader;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import RSS.RSSInfo;


public class RSSTitlesFragment extends Fragment {
    private List<RSSInfo> rssInfoList;
    private RecyclerView rvRSSTitles;
    public void setRssInfoList(List<RSSInfo> rssInfoList) {
        this.rssInfoList = rssInfoList;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_titles, container, false);
        rvRSSTitles = view.findViewById(R.id.rv_rss_titles);
        rvRSSTitles.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvRSSTitles.setAdapter(new RSSTitlesAdapter(rssInfoList, getContext()));
        return view;
    }
}