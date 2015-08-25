package com.track.trackme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    ImageButton help;
    ImageButton add;
    ImageButton search;
    SQLiteDatabase db;
    DbHelper helper;
    Location location;
    Geocoder geo;
    LocationManager locationManager;
    LocationProvider locationProvider;
    /*
        Initialize Listeners for all the Image Buttons in the Main Activity
     */
    public void initListeners(){
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager sms = SmsManager.getDefault();
                db = helper.getReadableDatabase();
                Cursor cursor = db.query(DbHelper.TABLE_NAME,null,null,null,null,null,DbHelper.C_NAME+" DESC");
                cursor.moveToFirst();
                String address = "My current Location is \n\n"+getCurrentLocation(locationProvider.getLatitude(), locationProvider.getLongitude())+"\n\nHelp Needed";
                while (!cursor.isAfterLast()){
                    sms.sendTextMessage(cursor.getString(cursor.getColumnIndex(DbHelper.C_PHONE)),null, address, null, null);
                    Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
                    cursor.moveToNext();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPicker = new Intent(Intent.ACTION_PICK);
                contactPicker.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(contactPicker, 0);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    /*
        Get the current location to send text
     */
    public String getCurrentLocation(double latitude, double longitude){
        //location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        String address = "";
        try {
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            address+=addresses.get(0).getLocality()+"\n"+addresses.get(0).getAdminArea()+"\n"+addresses.get(0).getPostalCode()+"\n"+addresses.get(0).getCountryName();
        } catch(Exception e){
            Toast.makeText(this, "No Location found", Toast.LENGTH_SHORT).show();
        }
        return address+"\nLatitude "+latitude+" Longitude "+longitude;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        help = (ImageButton) findViewById(R.id.help_button);
        add = (ImageButton) findViewById(R.id.add_button);
        search = (ImageButton) findViewById(R.id.search_button);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        initListeners();
        helper = new DbHelper(this);
        locationProvider = new LocationProvider();

        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        locationProvider.onLocationChanged(location);

        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, locationProvider);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ((requestCode == 0) && (resultCode == RESULT_OK)) {
                Uri result = data.getData();
                String id = result.getLastPathSegment();
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?", new String[]{id}, null);
                cursor.moveToFirst();
                db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DbHelper.C_ID, id);
                values.put(DbHelper.C_NAME, cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                values.put(DbHelper.C_PHONE, cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA)));
                db.insertOrThrow(DbHelper.TABLE_NAME, null, values);
                Toast.makeText(this, "Stored", Toast.LENGTH_SHORT).show();
                db.close();
            }
        } catch(Exception e){
            Toast.makeText(this, "Enter a valid contact", Toast.LENGTH_SHORT).show();
        }
    }
}
