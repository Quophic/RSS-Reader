package com.ncusoft.rssreader.RSS;

import java.util.List;

public interface RSSManagerInterface {
    List<RSSSource> getRSSSourceList();
    void insertRSSSource(RSSSource source);
    void deleteRSSSource(RSSSource source);
    List<RSSItem> getRSSItemList(RSSSource source);
    void insertRSSItems(RSSInfo info);
    void setRSSItemRead(RSSItem item);
    void close();
}
