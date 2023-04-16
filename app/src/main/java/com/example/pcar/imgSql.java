package com.example.pcar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class imgSql extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "images.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "images";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String DATA = "data";

    public imgSql(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                DATA + " BLOB" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
