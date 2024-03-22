package com.hamidul.stockmaintain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    public SQLiteDatabaseHelper(Context context) {
        super(context, "my_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table stock (id INTEGER, sku TEXT, unit INTEGER, tp DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists stock");
    }

    public void ClearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from stock");
    }

    public void InsertStock (int id,String sku,int unit,double tp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("sku",sku);
        contentValues.put("unit",unit);
        contentValues.put("tp",tp);
        db.insert("stock",null,contentValues);
    }

    public Cursor getAllStock(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from stock",null);
        return cursor;
    }

    public int getStockOldUnit (String id){

        int oldUnit = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from stock where id like '"+id+"' ",null);

        while (cursor.moveToNext()){
            oldUnit = cursor.getInt(2);
        }

        return oldUnit;
    }

}
