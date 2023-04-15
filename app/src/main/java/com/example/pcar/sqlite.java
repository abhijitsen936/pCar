package com.example.pcar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cars.db";
    private static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "_id";
    static final String COLUMN_CARMODEL = "carModel";
    static final String COLUMN_CARMAKE = "carMake";

    public sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CARMAKE + " TEXT,"
                + COLUMN_CARMODEL + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("drop table if exists users");
       onCreate(db);
    }

    public Cursor getinfo(){

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users", null);
        return cursor;

    }

    public  boolean delete_data(String carMake){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor =db.rawQuery("select * from users where carMake=?", new String[]{carMake});
        if(cursor.getCount()>0){
            long r=db.delete("users", "carMake=?", new String[]{carMake});
            if(r==-1) return false;
            else
                return true;
        }else
            return false;


    }


}
