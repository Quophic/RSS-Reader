package com.ncusoft.rssreader;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ncusoft.rssreader.DataBase.DBManager;
import com.ncusoft.rssreader.DataBase.SubscribedRSSInfo;
import com.ncusoft.rssreader.RSS.RSSInfo;
import com.ncusoft.rssreader.RSS.RSSUtils;


public class GetNewRSSDialog extends Dialog {
    private EditText etInput;
    private Button btnCancel;
    private Button btnOk;
    private DBManager manager;
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
    }


    class RSSTask extends AsyncTask<RSSInfo, Void, RSSInfo> {
        private String link;
        public RSSTask(String link){
            this.link = link;
        }
        @Override
        protected RSSInfo doInBackground(RSSInfo... rssInfos) {
            try {
                return RSSUtils.getRSSInfoFromUrl(link);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(RSSInfo rssInfo) {
            if(rssInfo == null){
                Toast.makeText(getContext(), R.string.illegal_RSS_source, Toast.LENGTH_SHORT).show();
                return;
            }
            SubscribedRSSInfo info = new SubscribedRSSInfo();
            info.setTitle(rssInfo.getTitle());
            info.setLink(link);
            manager.add(info);
            manager.close();
            dismiss();
        }
    }
}
