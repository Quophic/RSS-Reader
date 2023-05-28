package com.ncusoft.rssreader.DataBase.Contract;

import android.provider.BaseColumns;

public class RSSItemsContract implements BaseColumns {
    public static final String TABLE_NAME = "rss_items";
    public static final String SOURCE_ID = "source_id";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String PUB_DATE = "pub_date";
    public static final String STATUS = "status";
    public static final int STATUS_NEVER = 0;
    public static final int STATUS_READ = 1;
}
