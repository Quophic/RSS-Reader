package com.ncusoft.rssreader.RSS;

import java.util.List;

public interface RSSManagerInterface {
    List<RSSSource> getRSSSourceList();
    List<RSSSource> insertRSSSource(RSSSource source);
    List<RSSSource> deleteRSSSource(RSSSource source);
    List<RSSItem> getRSSItemList(RSSSource source);
    void insertRSSItems(RSSInfo info);
    void setRSSItemRead(RSSItem item);
}
