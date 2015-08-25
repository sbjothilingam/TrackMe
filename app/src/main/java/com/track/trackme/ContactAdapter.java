package com.track.trackme;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Suresh Babu on 7/21/2015.
 */
public class ContactAdapter extends SimpleCursorAdapter {
    final static String[] from = {DbHelper.C_NAME, DbHelper.C_PHONE};
    final static int[] to = {R.id.contact_name, R.id.contact_phone};

    public ContactAdapter(Context context, Cursor c) {
        super(context, R.layout.contact_row, c, from, to);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView name = (TextView) view.findViewById(R.id.contact_name);
        TextView phone = (TextView) view.findViewById(R.id.contact_phone);
        name.setText(cursor.getString(cursor.getColumnIndex(DbHelper.C_NAME)));
        phone.setText(cursor.getString(cursor.getColumnIndex(DbHelper.C_PHONE)));
    }
}
