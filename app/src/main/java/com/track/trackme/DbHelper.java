package com.track.trackme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Suresh Babu on 7/21/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "contacts.db";
    static final int DB_VERSION = 1;
    static final String TABLE_NAME = "contacts";
    static final String C_ID = BaseColumns._ID;
    static final String C_NAME = "name";
    static final String C_PHONE = "phone";
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "create table "+TABLE_NAME+" ("+C_ID+" int primary key, "+C_NAME+" text, "+C_PHONE+" text);";
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String command = "drop table if exists "+TABLE_NAME;
        db.execSQL(command);
    }
}
