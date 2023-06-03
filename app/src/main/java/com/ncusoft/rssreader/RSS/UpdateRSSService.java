package com.ncusoft.rssreader.RSS;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.ncusoft.rssreader.GetNewRSSDialog;
import com.ncusoft.rssreader.R;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.InputStream;
import java.net.URL;


public class UpdateRSSService extends IntentService {
    private static final String ACTION_UPDATE_RSS_SOURCE = "com.ncusoft.rssreader.RSS.ACTION_UPDATE_RSS_SOURCE";
    private static final String ACTION_UPDATE_RSS_ITEM = "com.ncusoft.rssreader.RSS.ACTION_UPDATE_RSS_ITEM";
    public static final String BROADCAST_UPDATE_RSS_SOURCE = "android.intent.action.BROADCAST_UPDATE_RSS_SOURCE";
    public static final String BROADCAST_UPDATE_RSS_SOURCE_FAIL = "android.intent.action.BROADCAST_UPDATE_RSS_SOURCE_FAIL";
    public static final String BROADCAST_UPDATE_RSS_ITEM = "android.intent.action.BROADCAST_UPDATE_RSS_ITEM";

    private static final String PARAM = "com.ncusoft.rssreader.RSS.param";

    public UpdateRSSService() {
        super("UpdateRSSService");
    }

    public static void startActionUpdateRSSSource(Context context, String urlString){
        Intent intent = new Intent(context, UpdateRSSService.class);
        intent.setAction(ACTION_UPDATE_RSS_SOURCE);
        intent.putExtra(PARAM, urlString);
        context.startService(intent);
    }
    public static void startActionUpdateRSSItem(){}
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            switch(action){
                case ACTION_UPDATE_RSS_SOURCE:
                    String urlString = intent.getStringExtra(PARAM);
                    handleActionUpdateRSSSource(urlString);
                    break;
                case ACTION_UPDATE_RSS_ITEM:
                    break;
            }
        }
    }

    private void handleActionUpdateRSSSource(String urlString){
        try {
            RSSManager manager = new RSSManager(this);
            RSSInfo info = getRSSInfoFromUrl(urlString);
            manager.insertRSSSource(info.getSource());
            sendBroadcast(new Intent(BROADCAST_UPDATE_RSS_SOURCE));
        }catch (Exception e){
            sendBroadcast(new Intent(BROADCAST_UPDATE_RSS_SOURCE_FAIL));
        }
    }
    private void handleActionUpdateRSSItem(){}
    public RSSInfo getRSSInfoFromUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));
        RSSInfo info = new RSSInfo();
        RSSSource source = new RSSSource();
        source.setTitle(feed.getTitle());
        source.setLink(urlString);
        if(feed.getImage() != null){
            source.setImageUrl(feed.getImage().getUrl());
            source.setImage(getImageFromUrl(source.getImageUrl()));
        }
        info.setSource(source);
        for(SyndEntry entry : feed.getEntries()){
            RSSItem item = new RSSItem();
            item.setTitle(entry.getTitle());
            item.setLink(entry.getLink());
            item.setPubDate(entry.getPublishedDate());
            info.getItems().add(item);
        }
        return info;
    }
    public Bitmap getImageFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        InputStream inputStream = url.openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
        inputStream.close();
        return bitmap;
    }

}