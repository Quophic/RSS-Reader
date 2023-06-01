package com.ncusoft.rssreader.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ncusoft.rssreader.DataBase.Contract.RSSItemsContract;
import com.ncusoft.rssreader.DataBase.Contract.RSSSourcesContract;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rss.db";
    private static final String SQL_CREATE_RSS_SOURCE =
            "CREATE TABLE " + RSSSourcesContract.TABLE_NAME + "(" +
                    RSSSourcesContract._ID + " INTEGER PRIMARY KEY," +
                    RSSSourcesContract.TITLE + " TEXT," +
                    RSSSourcesContract.LINK + " TEXT UNIQUE," +
                    RSSSourcesContract.IMAGE + " TEXT)";
    private static final String SQL_CREATE_RSS_ITEM =
            "CREATE TABLE " + RSSItemsContract.TABLE_NAME + "(" +
                    RSSItemsContract._ID + " INTEGER PRIMARY KEY," +
                    RSSItemsContract.SOURCE_ID + " INTEGER," +
                    RSSItemsContract.TITLE + " TEXT," +
                    RSSItemsContract.LINK + " TEXT UNIQUE," +
                    RSSItemsContract.PUB_DATE + " INTEGER," +
                    RSSItemsContract.STATUS + " INTEGER," +
                    "FOREIGN KEY (" + RSSItemsContract.SOURCE_ID + " )" +
                    "REFERENCES " + RSSSourcesContract.TABLE_NAME + "(" + RSSSourcesContract._ID +"))";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RSS_SOURCE);
        db.execSQL(SQL_CREATE_RSS_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}