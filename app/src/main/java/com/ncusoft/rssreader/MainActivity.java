package com.ncusoft.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSUtils;

public class MainActivity extends AppCompatActivity {
    private static final String[] RSS_URLS = {
            "http://www.people.com.cn/rss/politics.xml",
            "http://www.people.com.cn/rss/world.xml",
            "http://www.people.com.cn/rss/finance.xml",
            "http://www.people.com.cn/rss/sports.xml"
    };
    private List<RSSInfo> rssInfoList;
    private RSSTitlesFragment rssTitlesFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssTitlesFragment = new RSSTitlesFragment();
        new RSSTask().execute();
    }

    public void startFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.f_rss_title_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    class RSSTask extends AsyncTask<List<RSSInfo>, Void, List<RSSInfo>> {

        @Override
        protected List<RSSInfo> doInBackground(List<RSSInfo>... lists) {
            try {
                return RSSUtils.getRSSInfoFromUrl(RSS_URLS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<RSSInfo> list) {
            MainActivity.this.rssInfoList = list;
            rssTitlesFragment.setRssInfoList(list);
            startFragment(rssTitlesFragment);
        }
    }
}