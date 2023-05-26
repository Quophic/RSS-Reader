package com.ncusoft.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;
import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSUtils;

public class MainActivity extends AppCompatActivity {
    private List<SubscribedRSSInfo> infoList;
    private FloatingActionButton fabAdd;
    private RSSTitlesFragment rssTitlesFragment;
    private DBManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new DBManager(this);
        infoList = manager.getAllSubscribedRSS();
        rssTitlesFragment = new RSSTitlesFragment(infoList);
        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> {
            new InputDialog(MainActivity.this, manager).show();
        });
        startFragment(rssTitlesFragment);
    }

    public void startFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.f_rss_title_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}