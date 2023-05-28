package com.ncusoft.rssreader.RSS;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;


import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class RSSUtils {
    public static RSSInfo getRSSInfoFromUrl(String urlString) throws Exception{
        URL url = new URL(urlString);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));
        RSSInfo info = new RSSInfo();
        info.setTitle(feed.getTitle());
        info.setLink(feed.getLink());
        if(feed.getImage() != null){
            info.setImageUrl(feed.getImage().getUrl());
        }
        for(SyndEntry entry : feed.getEntries()){
            RSSItem item = new RSSItem();
            item.setTitle(entry.getTitle());
            item.setLink(entry.getLink());
            item.setPubDate(entry.getPublishedDate());
            info.getItems().add(item);
        }
        return info;
    }

    public static Bitmap getImageFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        InputStream inputStream = url.openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
        inputStream.close();
        return bitmap;
    }
}
