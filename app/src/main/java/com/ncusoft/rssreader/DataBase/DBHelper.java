package com.ncusoft.rssreader.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ncusoft.rssreader.DataBase.Contract.RSSItemsContract;
import com.ncusoft.rssreader.DataBase.Contract.RSSSourcesContract;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "rss.db";
    private static final String SQL_CREATE_TITLES =
            "CREATE TABLE " + RSSSourcesContract.TABLE_NAME + "(" +
                    RSSSourcesContract._ID + " INTEGER PRIMARY KEY," +
                    RSSSourcesContract.TITLE + " TEXT," +
                    RSSSourcesContract.LINK + " TEXT," +
                    RSSSourcesContract.IMAGE + " TEXT)";
    private static final String SQL_CREATE_RSS_ITEM =
            "CREATE TABLE " + RSSItemsContract.TABLE_NAME + "(" +
                    RSSItemsContract._ID + " INTEGER PRIMARY KEY," +
                    RSSItemsContract.SOURCE_ID + "INTEGER," +
                    RSSItemsContract.TITLE + " TEXT," +
                    RSSItemsContract.LINK + " TEXT," +
                    RSSItemsContract.PUB_DATE + " INTEGER," +
                    RSSItemsContract.STATUS + " INTEGER)";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TITLES);
        db.execSQL(SQL_CREATE_RSS_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion){
            case 1:
                String ADD_TYPE = "ALTER TABLE " + RSSSourcesContract.TABLE_NAME +
                        " ADD " + RSSSourcesContract.IMAGE + " TEXT";
                db.execSQL(ADD_TYPE);
                if(newVersion <= 2) break;
            case 2:
                db.execSQL(SQL_CREATE_RSS_ITEM);
                if (newVersion <= 3) break;
        }
    }
}