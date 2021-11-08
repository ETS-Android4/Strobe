package com.example.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class TemplateDatabase extends SQLiteOpenHelper {
    private static final String DATA_NAME = "Templates.db";
    private static final String TABLE_NAME = "TEMPLATES";
    private static final String COL_0 = "ID";
    private static final String COL_1 = "NAME";
    private static final String COL_2 = "BOOLEANS"; //chosen, attach, model
    private static final String COL_3 = "COLORS";
    private static final String COL_4 = "HSV";
    private static final String COL_5 = "COUNT";
    private static final String COL_6 = "DELAY";
    private static final String COL_7 = "ONTIME";
    private static final String COL_8 = "GLOBAL";
    private static final String COL_9 = "ATTACHED";

    public TemplateDatabase (Context context){
        super(context, DATA_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, BOOLEANS TEXT, COLORS INTEGER, HSV INTEGER, COUNT INTEGER, DELAY INTEGER, ONTIME INTEGER, GLOBAL INTEGER, ATTACHED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public boolean insertData(String name, String booleans, String colors, String hsv, String count, String delay, String ontime, String global, String attached){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, booleans);
        contentValues.put(COL_3, colors);
        contentValues.put(COL_4, hsv);
        contentValues.put(COL_5, count);
        contentValues.put(COL_6, delay);
        contentValues.put(COL_7, ontime);
        contentValues.put(COL_8, global);
        contentValues.put(COL_9, attached);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1)
            return false;
        else
            return  true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME , null);
        return res;
    }


    public boolean updateData(String name, String booleans, String colors, String hsv, String count, String delay, String ontime, String global, String attached){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, booleans);
        contentValues.put(COL_3, colors);
        contentValues.put(COL_4, hsv);
        contentValues.put(COL_5, count);
        contentValues.put(COL_6, delay);
        contentValues.put(COL_7, ontime);
        contentValues.put(COL_8, global);
        contentValues.put(COL_9, attached);
        long i = db.updateWithOnConflict(TABLE_NAME, contentValues, "name = ?", new String[] {name}, SQLiteDatabase.CONFLICT_ABORT);
        if(i==0) {
            boolean isInserted = insertData(name, booleans, colors, hsv, count, delay, ontime, global, attached);
            if (isInserted == true){
                Functions.ActionBar(Data.view, "Template created");
                return true;}
            else{
                Functions.ActionBar(Data.view, "Template not created");
                return false;}
        }else{
            Functions.ActionBar(Data.view, "Template updated");
            return true;
        }
    }
    public Integer deleteData (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"name = ?", new String[] {name});
    }
}
