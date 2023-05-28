package com.ncusoft.rssreader.DataBase;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SubscribedRSSInfo implements Serializable {
    private long id;
    private String title;
    private String link;
    private Bitmap image;
    public SubscribedRSSInfo(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
