package com.ncusoft.rssreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
    protected void onStop() {
        super.onStop();
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
                RSSTitlesFragment.sendDeleteMsg();
                break;
        }
        return super.onContextItemSelected(item);
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
                            .setPositiveButton(R.string.positive, ((dialog, which) -> {
                                finish();
                            }))
                            .create()
                            .show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}