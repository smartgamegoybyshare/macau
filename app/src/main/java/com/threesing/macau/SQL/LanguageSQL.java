package com.threesing.macau.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LanguageSQL extends SQLiteOpenHelper {

    private String table_name = "newLanguage"; //資料表名
    private static String db_name = "LanguageSQL.db";    //資料庫名
    private static int VERSION = 2;

    public LanguageSQL(Context context) {
        super(context, db_name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //ok
        String DATABASE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + table_name + "(" +
                "id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL," +
                "flag" + " INTEGER" + ")";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        db.execSQL("DROP TABLE IF EXISTS " + table_name); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打開數據庫後首先被執行
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public void insert(int flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("flag", flag);
        db.insert(table_name, null, cv);
    }

    public int getflag() {
        int getflag = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            do {
                getflag = cursor.getInt(cursor.getColumnIndex("flag"));
            } while (cursor.moveToNext());
        }
        return getflag;
    }

    public int getCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        return cursor.getCount();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name);
    }

    public int getID() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
            } while (cursor.moveToNext());
        }
        return id;
    }

    public void update(int id, int flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("flag", flag);

        db.update(table_name, cv, "id=" + id, null);
    }
}