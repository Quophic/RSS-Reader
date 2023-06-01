package com.ncusoft.rssreader.RSS;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ncusoft.rssreader.DataBase.Contract.RSSItemsContract;
import com.ncusoft.rssreader.DataBase.Contract.RSSSourcesContract;
import com.ncusoft.rssreader.DataBase.DBHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RSSManager extends Service implements RSSManagerInterface {
    private List<RSSSource> rssSourceList;
//    private List<RSSInfo> rssInfoList;
    private SQLiteDatabase db;
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper helper = new DBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        loadAllRSSSources();

    }

    private void loadAllRSSSources(){
        rssSourceList = new ArrayList<>();
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
            RSSSource source = new RSSSource();
            source.setId(cursor.getLong(0));
            source.setTitle(cursor.getString(1));
            source.setLink(cursor.getString(2));
            byte[] bytes = cursor.getBlob(3);
            if(bytes != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                source.setImage(bitmap);
            }
            rssSourceList.add(source);
        }
        cursor.close();
    }

    public List<RSSSource> getRSSSourceList() {
        return rssSourceList;
    }



    public List<RSSSource> insertRSSSource(RSSSource source){
        ContentValues values = new ContentValues();
        values.put(RSSSourcesContract.TITLE, source.getTitle());
        values.put(RSSSourcesContract.LINK, source.getLink());
        if(source.getImage() != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            source.getImage().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            values.put(RSSSourcesContract.IMAGE, outputStream.toByteArray());
        }
        long id = db.insert(RSSSourcesContract.TABLE_NAME, null, values);
        if(id != -1){
            source.setId(id);
            rssSourceList.add(source);
        }else{
            Toast.makeText( this,"已经存在于列表中", Toast.LENGTH_SHORT).show();
        }
        return rssSourceList;
    }

    public List<RSSSource> deleteRSSSource(RSSSource source){
        int result = db.delete(
                RSSSourcesContract.TABLE_NAME,
                RSSSourcesContract._ID + " = ?",
                new String[]{String.valueOf(source.getId())});
        if(result == 1){
            rssSourceList.remove(source);
            db.delete(
                    RSSItemsContract.TABLE_NAME,
                    RSSItemsContract.SOURCE_ID + " = ?",
                    new String[]{String.valueOf(source.getId())});
        }
        return rssSourceList;
    }

    @Override
    public List<RSSItem> getRSSItemList(RSSSource source) {
        List<RSSItem> rssItemList = new ArrayList<>();
        String[] columns = {
                RSSItemsContract._ID,
                RSSItemsContract.TITLE,
                RSSItemsContract.LINK,
                RSSItemsContract.PUB_DATE,
                RSSItemsContract.STATUS
        };
        Cursor cursor = db.query(
                RSSItemsContract.TABLE_NAME,
                columns,
                RSSItemsContract.SOURCE_ID + " = ?",
                new String[]{String.valueOf(source.getId())},
                null,
                null,
                RSSItemsContract.PUB_DATE + " DESC"
        );
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            RSSItem item = new RSSItem();
            item.setId(cursor.getLong(0));
            item.setTitle(cursor.getString(1));
            item.setLink(cursor.getString(2));
            item.setPubDate(new Date(cursor.getLong(3)));
            item.setStatus(cursor.getInt(4));
            rssItemList.add(item);
        }
        cursor.close();
        return rssItemList;
    }

    @Override
    public void insertRSSItems(RSSInfo info) {
        ContentValues values = new ContentValues();
        values.put(RSSItemsContract.SOURCE_ID, info.getSource().getId());
        for(RSSItem item : info.getItems()){
            values.put(RSSItemsContract.TITLE, item.getTitle());
            values.put(RSSItemsContract.LINK, item.getLink());
            values.put(RSSItemsContract.PUB_DATE, String.valueOf(item.getPubDate().getTime()));
            values.put(RSSItemsContract.STATUS, RSSItemsContract.STATUS_NEVER);
            long result = db.insert(RSSItemsContract.TABLE_NAME, null, values);
            if(result != -1){
                item.setId(result);
            }
        }
    }

    @Override
    public void setRSSItemRead(RSSItem item) {
        ContentValues values = new ContentValues();
        values.put(RSSItemsContract.STATUS, RSSItemsContract.STATUS_READ);
        db.update(
                RSSItemsContract.TABLE_NAME,
                values,
                RSSItemsContract._ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
    public class Binder extends android.os.Binder{
        public RSSManagerInterface getDBManager(){
            return RSSManager.this;
        }
    }
}
