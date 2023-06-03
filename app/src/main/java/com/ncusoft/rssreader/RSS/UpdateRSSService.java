package com.ncusoft.rssreader.RSS;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    private static final String PARAM_1 = "com.ncusoft.rssreader.extra.param1";
    private static final String PARAM_2 = "com.ncusoft.rssreader.extra.param2";

    public UpdateRSSService() {
        super("UpdateRSSService");
    }

    public static void startActionUpdateRSSSource(Context context, String urlString){
        Intent intent = new Intent(context, UpdateRSSService.class);
        intent.setAction(ACTION_UPDATE_RSS_SOURCE);
        intent.putExtra(PARAM_1, urlString);
        context.startService(intent);
    }
    public static void startActionUpdateRSSItem(Context context, String urlString, long id){
        Intent intent = new Intent(context, UpdateRSSService.class);
        intent.setAction(ACTION_UPDATE_RSS_ITEM);
        intent.putExtra(PARAM_1, urlString);
        intent.putExtra(PARAM_2, id);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            switch(action){
                case ACTION_UPDATE_RSS_SOURCE:{
                    String urlString = intent.getStringExtra(PARAM_1);
                    handleActionUpdateRSSSource(urlString);
                    break;
                }
                case ACTION_UPDATE_RSS_ITEM: {
                    String urlString = intent.getStringExtra(PARAM_1);
                    long id = intent.getLongExtra(PARAM_2, -1);
                    handleActionUpdateRSSItem(urlString, id);
                    break;
                }
            }
        }
    }

    private void handleActionUpdateRSSSource(String urlString){
        RSSManager manager = new RSSManager(this);
        try {
            RSSInfo info = getRSSInfoFromUrl(urlString);
            manager.insertRSSSource(info.getSource());
            manager.insertRSSItems(info);
            sendBroadcast(new Intent(BROADCAST_UPDATE_RSS_SOURCE));
        }catch (Exception e){
            e.printStackTrace();
            sendBroadcast(new Intent(BROADCAST_UPDATE_RSS_SOURCE_FAIL));
        }finally {
            manager.close();
        }
    }
    private void handleActionUpdateRSSItem(String urlString, long id){
        RSSManager manager = new RSSManager(this);
        try {
            RSSInfo info = getRSSInfoFromUrl(urlString);
            info.getSource().setId(id);
            manager.insertRSSItems(info);
            sendBroadcast(new Intent(BROADCAST_UPDATE_RSS_ITEM));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            manager.close();
        }
    }
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