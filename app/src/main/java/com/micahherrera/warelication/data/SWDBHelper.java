package com.micahherrera.warelication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.micahherrera.warelication.utils.DBAssetHelper;

import java.util.List;

/**
 * Created by micahherrera on 11/21/16.
 */

public class SWDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = DBAssetHelper.DATABASE_NAME;
    private static final int DATABASE_VERSION = DBAssetHelper.DATABASE_VERSION;

    public static final String TABLE_NAME = "event_table";
    public static final String ID="id";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String TIMESTAMP = "timestamp";
    public static final String IMAGE = "image";
    public static final String DATE = "date";
    public static final String LOCATIONLINE1 = "locationline1";
    public static final String LOCATIONLINE2 = "locationline2";
    public static final String PHONE = "phone";


    private static SWDBHelper instance;

    private SWDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SWDBHelper getInstance(Context context){
        if(instance==null){
            instance = new SWDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static final String SQL_CREATE_ENTRIES_EVENTS = "CREATE TABLE " +
            TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY," +
            DESCRIPTION + " TEXT," +
            TITLE + " TEXT," +
            TIMESTAMP + " TEXT," +
            IMAGE + " TEXT," +
            DATE + " TEXT," +
            LOCATIONLINE1 + " TEXT," +
            LOCATIONLINE2 + " TEXT," +
            PHONE + " TEXT" + ")";

    public Cursor getEvents(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM %s",
                TABLE_NAME);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void saveEvents(List<Event> eventList){
        SQLiteDatabase db = getWritableDatabase();

        for (Event event:eventList){
            ContentValues values = new ContentValues();
            values.put(ID, event.getId());
            values.put(DESCRIPTION, event.getDescription());
            values.put(TITLE, event.getTitle());
            values.put(TIMESTAMP, event.getTimestamp());
            values.put(IMAGE, event.getImage());
            values.put(DATE, event.getDate());
            values.put(LOCATIONLINE1, event.getLocationline1());
            values.put(LOCATIONLINE2, event.getLocationline2());
            values.put(PHONE, event.getPhone());

            db.insert(TABLE_NAME, null, values);
        }
    }

    public void clearEvents(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public Cursor getEvent(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE "+ID+ "="+id, null);
        return cursor;
    }
}
