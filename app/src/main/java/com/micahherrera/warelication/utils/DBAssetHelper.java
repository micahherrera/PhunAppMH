package com.micahherrera.warelication.utils;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by micahherrera on 11/21/16.
 */

public class DBAssetHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "SWDB.db";
    public static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}