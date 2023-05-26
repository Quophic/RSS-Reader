package com.ncusoft.rssreader.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rss.db";
    private static final String SQL_CREATE_RECORD =
            "CREATE TABLE " +TableContract.TABLE_NAME + "(" +
                    TableContract._ID + " INTEGER PRIMARY KEY," +
                    TableContract.TITLE + " TEXT," +
                    TableContract.LINK + " TEXT)";
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}