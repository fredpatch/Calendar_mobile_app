package com.example.customcalendarviewwithsevent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_EVENT_TABLE = "create table "+DBstructure.EVENT_TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            +DBstructure.EVENT+" TEXT, "+DBstructure.TIME+" TEXT, "+DBstructure.DATE+" TEXT, "+DBstructure.MONTH+" TEXT, "
            +DBstructure.YEAR+" TEXT, "+DBstructure.Notify+" TEXT)";

    private static final String DROP_EVENT_TABLE= "DROP TABLE IF EXISTS "+DBstructure.EVENT_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBstructure.DB_name, null, DBstructure.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EVENT_TABLE);
        onCreate(db);

    }
    public void SaveEvent(String event,String time,String date,String month,String year,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBstructure.EVENT,event);
        contentValues.put(DBstructure.TIME,time);
        contentValues.put(DBstructure.DATE,date);
        contentValues.put(DBstructure.MONTH,month);
        contentValues.put(DBstructure.YEAR,year);
        database.insert(DBstructure.EVENT_TABLE_NAME,null,contentValues);

    }

    public Cursor ReadEvents(String date,SQLiteDatabase database){
        String [] Projections = {DBstructure.EVENT,DBstructure.TIME,DBstructure.DATE,DBstructure.MONTH,DBstructure.YEAR};
        String Selection = DBstructure.DATE +"=?";
        String [] SelectionArgs = {date};

        return database.query(DBstructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }
    public Cursor ReadIDEvents(String date,String event, String time,SQLiteDatabase database){
        String [] Projections = {DBstructure.ID,DBstructure.Notify,DBstructure.TIME};
        String Selection = DBstructure.DATE +"=? and "+DBstructure.EVENT+"=? and "+DBstructure.TIME+"=?";
        String [] SelectionArgs = {date,event,time};

        return database.query(DBstructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public Cursor ReadEventsperMonth(String month,String year,SQLiteDatabase database){
        String [] Projections = {DBstructure.EVENT,DBstructure.TIME,DBstructure.DATE,DBstructure.MONTH,DBstructure.YEAR};
        String Selection = DBstructure.MONTH +"=? and "+DBstructure.YEAR+"=?";
        String [] SelectionArgs = {month,year};
        return database.query(DBstructure.EVENT_TABLE_NAME,Projections,Selection,SelectionArgs,null,null,null);
    }

    public void deleteEvent(String event,String date,String time,SQLiteDatabase database){
        String selection = DBstructure.EVENT+"=? and "+DBstructure.DATE+"=? and "+DBstructure.TIME+"=?";
        String[] selectionArg = {event,date,time};
        database.delete(DBstructure.EVENT_TABLE_NAME,selection,selectionArg);
    }

    public void updateEvent(String date,String event, String time,String notify,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBstructure.Notify,notify);
        String Selection = DBstructure.DATE +"=? and "+DBstructure.EVENT+"=? and "+DBstructure.TIME+"=?";
        String [] SelectionArgs = {date,event,time};
        database.update(DBstructure.EVENT_TABLE_NAME,contentValues,Selection,SelectionArgs);
    }
}
