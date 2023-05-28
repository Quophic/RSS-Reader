package com.ncusoft.rssreader;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.RSS.RSSSource;
import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSUtils;


public class GetNewRSSDialog extends Dialog {
    private EditText etInput;
    private Button btnCancel;
    private Button btnOk;
    private DBManager manager;
    private ProgressBar progressBar;
    public GetNewRSSDialog(@NonNull Context context) {
        super(context);
        manager = new DBManager(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input);
        etInput = findViewById(R.id.et_input);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> dismiss());
        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> {
            String link = etInput.getText().toString();
            Log.i("input", link);
            new RSSTask(link).execute();
        });
        progressBar = findViewById(R.id.progress);
    }
    class RSSTask extends AsyncTask<RSSSource, Void, RSSSource> {
        private String link;
        public RSSTask(String link){
            this.link = link;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected RSSSource doInBackground(RSSSource... infos) {
            try {
                RSSInfo rssInfo = RSSUtils.getRSSInfoFromUrl(link);
                RSSSource info = new RSSSource();
                if(rssInfo.getSource().getImageUrl() != null){
                    Bitmap bitmap = RSSUtils.getImageFromUrl(rssInfo.getSource().getImageUrl());
                    info.setImage(bitmap);
                }
                info.setTitle(rssInfo.getSource().getTitle());
                info.setLink(link);
                return info;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RSSSource info) {
            if(info == null){
                Toast.makeText(getContext(), R.string.illegal_RSS_source, Toast.LENGTH_SHORT).show();
                return;
            }
            manager.insertRSSSource(info);
            manager.close();
            RSSSourcesFragment.sendRefreshMsg(info);
            progressBar.setVisibility(View.GONE);
            dismiss();
        }
    }
}
