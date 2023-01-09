package com.example.qrscanner.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.qrscanner.Model.ListItem;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME="myTable";
    public static final String DATABASE_NAME="qrDb.db";

    public static final String COL_ID="id";
    public static final String COL_CODE="code";
    public static final String COL_TYPE="type";



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table " + TABLE_NAME
                + "("
                + COL_ID + " integer primary key autoincrement,"
                + COL_CODE + " text ,"
                + COL_TYPE + " text"
                + ")";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " +  TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String code, String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CODE,code);
        values.put(COL_TYPE, type);
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1)
            return false;
        else
            return true;
    }

    public ArrayList<ListItem>getAllInformation(){
        ArrayList<ListItem> arrayList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from "+TABLE_NAME,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String code = cursor.getString(1);
                String type = cursor.getString(2);
                ListItem listItem = new ListItem(id,code,type);
                arrayList.add(listItem);
            }
        }
        cursor.close();
        return arrayList;
    }

    public void deleteRow(int value){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME+ "= WHERE "+COL_ID+"='"+value+"'");
    }
}
