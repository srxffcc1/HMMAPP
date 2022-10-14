package com.healthy.library.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CacheDao {
    private static volatile CacheDao cacheDao;

    private HMMDBHelper helper;
    private SQLiteDatabase database;

    private CacheDao(Context context){
        helper = new HMMDBHelper(context.getApplicationContext());
        database = helper.getWritableDatabase();
    }
    public static CacheDao getInstance(Context context) {
        if (cacheDao == null) {
            synchronized (CacheDao.class) {
                if (cacheDao == null) {
                    cacheDao = new CacheDao(context);
                }
            }
        }
        return cacheDao;
    }
    //查
    public HMMHttpCache queryResponse(String urlKey) {
        HMMHttpCache hmmHttpCache=null;
        Cursor cursor=database.query(HMMDBHelper.CACHE_TABLE,new String[]{"url","response","ts"},"url=?",new String[]{urlKey},null,null,null);
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()) {
                hmmHttpCache = new HMMHttpCache(cursor.getString(cursor.getColumnIndex("url"))
                        , cursor.getString(cursor.getColumnIndex("response"))
                        , cursor.getString(cursor.getColumnIndex("ts")));
            }
        }
        return hmmHttpCache;
    }
    //增
    public void insertResponse(String url, String response, String ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("url",url);
        contentValues.put("response",response);
        contentValues.put("ts",ts);
        database.delete(HMMDBHelper.CACHE_TABLE,"url=?",new String[]{url});
        database.insert(HMMDBHelper.CACHE_TABLE,null,contentValues);
    }
    //改
    public void updateResponse(String url, String response, String ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("url",url);
        contentValues.put("response",response);
        contentValues.put("ts",ts);
        database.update(HMMDBHelper.CACHE_TABLE,contentValues,"url=?",new String[]{url});
    }
    //删
    public void deleteResponse(String url) {
        database.delete(HMMDBHelper.CACHE_TABLE,"url=?",new String[]{url});
    }
}