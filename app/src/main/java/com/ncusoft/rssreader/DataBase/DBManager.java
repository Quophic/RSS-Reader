package com.ncusoft.rssreader.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
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
                TableContract._ID,
                TableContract.TITLE,
                TableContract.LINK,
                TableContract.IMAGE
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

    public List<SubscribedRSSInfo> getAllSubscribedRSS() {
        return list;
    }

    public void add(SubscribedRSSInfo info){
        ContentValues values = new ContentValues();
        values.put(TableContract.TITLE, info.getTitle());
        values.put(TableContract.LINK, info.getLink());
        if(info.getImage() != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            info.getImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            values.put(TableContract.IMAGE, outputStream.toByteArray());
        }
        long id = db.insert(TableContract.TABLE_NAME, null, values);
        if(id != -1){
            info.setId(id);
            list.add(info);
        }
    }

    public void delete(SubscribedRSSInfo info){
        db.delete(
                TableContract.TABLE_NAME,
                TableContract._ID + " = ?",
                new String[]{String.valueOf(info.getId())});
    }
    public void close(){
        db.close();
    }
}
