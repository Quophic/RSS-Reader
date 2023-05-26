package com.ncusoft.rssreader.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private List<SubscribedRSSInfo> list;
    private SQLiteDatabase db;
    public DBManager(Context context){
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        load();
    }

    private void load(){
        list = new ArrayList<>();
        String[] columns = {
                TableContract.TITLE,
                TableContract.LINK
        };
        Cursor cursor = db.query(
                TableContract.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            SubscribedRSSInfo info = new SubscribedRSSInfo();
            info.setTitle(cursor.getString(0));
            info.setLink(cursor.getString(1));
            list.add(info);
        }
        cursor.close();
    }

    public List<SubscribedRSSInfo> getAllSubscribedRSS() {
        return list;
    }

    public void add(SubscribedRSSInfo info){
        ContentValues values = new ContentValues();
        values.put(TableContract.TITLE, info.getTitle());
        values.put(TableContract.LINK, info.getLink());
        db.insert(TableContract.TABLE_NAME, null, values);
        list.add(info);
    }
    public void close(){
        db.close();
    }
}
