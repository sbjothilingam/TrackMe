package com.track.trackme;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by Suresh Babu on 7/21/2015.
 */
public class ContactActivity extends Activity {
    SQLiteDatabase db;
    Cursor cursor;
    DbHelper helper;
    ListView list_view;
    ContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        helper = new DbHelper(this);
        db = helper.getReadableDatabase();
        list_view = (ListView) findViewById(R.id.contact_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = db.query(DbHelper.TABLE_NAME,null,null,null,null,null,DbHelper.C_NAME+" DESC");
        startManagingCursor(cursor);
        cursor.moveToFirst();
        adapter = new ContactAdapter(this, cursor);
        list_view.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
