package com.themescreen.flashcolor.stylescreen.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelperClass extends SQLiteOpenHelper {
    private static final String dbname = "caller_contect_theam";
    private static final int version = 2;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteHelperClass(Context context) {
        super(context, dbname, (SQLiteDatabase.CursorFactory) null, 2);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE contect_theme (_id INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER TEXT,THEMEVALUE TEXT,PRICE REAL )");
        sQLiteDatabase.execSQL("CREATE TABLE calling_icon (_id INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER TEXT,RECEVIEICON INTEGER,REJECTICON INTEGER )");
    }

    public void insertdata(String str, String str2, SQLiteDatabase sQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NUMBER", str);
        contentValues.put("THEMEVALUE", str2);
        sQLiteDatabase.insert("contect_theme", null, contentValues);
    }

    public void inserticondata(String str, int i, int i2, SQLiteDatabase sQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NUMBER", str);
        contentValues.put("RECEVIEICON", Integer.valueOf(i));
        contentValues.put("REJECTICON", Integer.valueOf(i2));
        sQLiteDatabase.insert("calling_icon", null, contentValues);
    }

    public void deletedata(String str, SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.delete("contect_theme", "NUMBER = ?", new String[]{str});
    }

    public void deletecallingicon(String str, SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.delete("calling_icon", "NUMBER = ?", new String[]{str});
    }
}
