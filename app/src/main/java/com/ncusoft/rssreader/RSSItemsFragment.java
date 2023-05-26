package com.ncusoft.rssreader;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncusoft.rssreader.RSS.RSSInfo;

public class RSSItemsFragment extends Fragment {
    private RSSInfo rssInfo;
    private RecyclerView rvRSSItems;
    public void setRssInfo(RSSInfo rssInfo){
        this.rssInfo = rssInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_items, container, false);
        rvRSSItems = view.findViewById(R.id.rv_rss_items);
        rvRSSItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvRSSItems.setAdapter(new RSSItemsAdapter(rssInfo.getItems()));
        return view;
    }
}