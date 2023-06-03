package com.ncusoft.rssreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ncusoft.rssreader.RSS.RSSManager;
import com.ncusoft.rssreader.RSS.RSSManagerInterface;


public class MainActivity extends AppCompatActivity {
    private RSSManagerInterface manager;
    public RSSManagerInterface getManager(){
        return manager;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        manager = new RSSManager(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.f_rss_title_container, new RSSSourcesFragment())
                .commit();
    }
    public void startFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.f_rss_title_container, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    @Override
    protected void onDestroy() {
        manager.close();
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete:
                RSSSourcesFragment.sendDeleteRSSSourceMsg();
                break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount() >= 1){
                    getSupportFragmentManager().popBackStack();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.exit_question)
                            .setNegativeButton(R.string.negative, (dialog, which) -> {})
                            .setPositiveButton(R.string.positive, (dialog, which) -> finish())
                            .create()
                            .show();
                }
                break;
            case R.id.item_software_details:
                Intent intent = new Intent(this, InformationActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}