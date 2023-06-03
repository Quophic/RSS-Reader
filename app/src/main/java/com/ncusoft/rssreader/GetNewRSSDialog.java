package com.ncusoft.rssreader;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ncusoft.rssreader.RSS.UpdateRSSService;


public class GetNewRSSDialog extends Dialog {
    private static final int MESSAGE_SUCCESS = 0;
    private static final int MESSAGE_FAIL = 1;
    private EditText etInput;
    private Button btnCancel;
    private Button btnOk;
    private ProgressBar progressBar;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE_SUCCESS:
                    dismiss();
                    break;
                case MESSAGE_FAIL:
                    Toast.makeText(getContext(), R.string.illegal_RSS_source, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                String action = intent.getAction();
                Message message = new Message();
                switch (action){
                    case UpdateRSSService.BROADCAST_UPDATE_RSS_SOURCE:
                        message.what = MESSAGE_SUCCESS;
                        break;
                    case UpdateRSSService.BROADCAST_UPDATE_RSS_SOURCE_FAIL:
                        message.what = MESSAGE_FAIL;
                        break;
                }
                handler.sendMessage(message);
            }
        }
    };
    public GetNewRSSDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UpdateRSSService.BROADCAST_UPDATE_RSS_SOURCE);
        intentFilter.addAction(UpdateRSSService.BROADCAST_UPDATE_RSS_SOURCE_FAIL);
        getContext().registerReceiver(receiver, intentFilter);
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
        progressBar = findViewById(R.id.progress);
        btnOk.setOnClickListener(v -> {
            String link = etInput.getText().toString();
            Log.i("input", link);
            UpdateRSSService.startActionUpdateRSSSource(getContext(), link);
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        getContext().unregisterReceiver(receiver);
    }
}
