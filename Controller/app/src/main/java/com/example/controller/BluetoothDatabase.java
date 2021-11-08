package com.example.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BluetoothDatabase extends SQLiteOpenHelper {
    public static final String DATA_NAME = "Bluetooth.db";
    public static final String TABLE_NAME = "BLUETOOTH";
    public static final String COL_0 = "ID";
    public static final String COL_1 = "address";

    public BluetoothDatabase (Context context){
        super(context, DATA_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, ADDRESS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public boolean insertData(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, address);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1)
            return false;
        else{
            return  true;
        }
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME , null);
        return res;
    }

    public boolean updateData(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, address);
        long i =  db.update(TABLE_NAME, contentValues, "ID = 1", null);
        if(i==0) {
            boolean isInserted = insertData(address);
            if (isInserted == true){
                Toast.makeText(Data.appContext, "Address created", Toast.LENGTH_LONG).show();
                return true;}
            else{
                Toast.makeText(Data.appContext, "Address not created", Toast.LENGTH_LONG).show();
                return false;}
        }else{
            return true;
        }
    }
}
