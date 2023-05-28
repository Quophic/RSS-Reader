package com.ncusoft.rssreader.RSS;

import java.util.ArrayList;
import java.util.List;

public class RSSInfo{
    private RSSSource source;
    private List<RSSItem> items = new ArrayList<>();

    public RSSSource getSource() {
        return source;
    }

    public void setSource(RSSSource source) {
        this.source = source;
    }

    public List<RSSItem> getItems() {
        return items;
    }

    public void setItems(List<RSSItem> items) {
        this.items = items;
    }
}
