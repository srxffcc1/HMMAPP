package com.healthy.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.healthy.library.LibApplication;

public class HMMDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = LibApplication.getAppContext().getCacheDir()+"/hmm.db";
    private static final int DB_VERSION = 1;
    static final String CACHE_TABLE = "cache";

    public HMMDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + CACHE_TABLE +
                " (url text, response text,ts text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}