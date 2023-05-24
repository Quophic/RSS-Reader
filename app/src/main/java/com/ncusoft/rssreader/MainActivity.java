package com.ncusoft.rssreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import RSS.RSSInfo;
import RSS.RSSUtils;

public class MainActivity extends AppCompatActivity {
    private static final String RSS_URL = "http://www.people.com.cn/rss/politics.xml";
    private TextView tvTitle;
    private RecyclerView rvRSSItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tv_title);
        rvRSSItems = findViewById(R.id.rv_rss_items);
        rvRSSItems.setLayoutManager(new LinearLayoutManager(this));
        new RSSTask().execute();
    }

    class RSSTask extends AsyncTask<RSSInfo, Void, RSSInfo>{

        @Override
        protected RSSInfo doInBackground(RSSInfo... rssInfos) {
            try {
                return RSSUtils.getRSSInfoFromUrl(RSS_URL);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(RSSInfo rssInfo) {
            tvTitle.setText(rssInfo.getTitle());
            RSSItemsAdapter adapter = new RSSItemsAdapter(rssInfo.getItems());
            rvRSSItems.setAdapter(adapter);
        }
    }
}