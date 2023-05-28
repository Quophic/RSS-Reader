package com.ncusoft.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}