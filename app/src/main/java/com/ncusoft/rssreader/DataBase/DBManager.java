package com.ncusoft.rssreader.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ncusoft.rssreader.DataBase.Contract.RSSSourcesContract;
import com.ncusoft.rssreader.RSS.RSSSource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private List<RSSSource> list;
    private SQLiteDatabase db;
    public DBManager(Context context){
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();
        loadAllRSSSources();
    }

    private void loadAllRSSSources(){
        list = new ArrayList<>();
        String[] columns = {
                RSSSourcesContract._ID,
                RSSSourcesContract.TITLE,
                RSSSourcesContract.LINK,
                RSSSourcesContract.IMAGE
        };
        Cursor cursor = db.query(
                RSSSourcesContract.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            RSSSource info = new RSSSource();
            info.setId(cursor.getLong(0));
            info.setTitle(cursor.getString(1));
            info.setLink(cursor.getString(2));
            byte[] bytes = cursor.getBlob(3);
            if(bytes != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                info.setImage(bitmap);
            }
            list.add(info);
        }
        cursor.close();
    }

    public List<RSSSource> getAllRSSSources() {
        return list;
    }

    public void addRSSSource(RSSSource info){
        ContentValues values = new ContentValues();
        values.put(RSSSourcesContract.TITLE, info.getTitle());
        values.put(RSSSourcesContract.LINK, info.getLink());
        if(info.getImage() != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            info.getImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            values.put(RSSSourcesContract.IMAGE, outputStream.toByteArray());
        }
        long id = db.insert(RSSSourcesContract.TABLE_NAME, null, values);
        if(id != -1){
            info.setId(id);
            list.add(info);
        }
    }

    public void deleteRSSSource(RSSSource info){
        db.delete(
                RSSSourcesContract.TABLE_NAME,
                RSSSourcesContract._ID + " = ?",
                new String[]{String.valueOf(info.getId())});
    }

    public void close(){
        db.close();
    }
}
