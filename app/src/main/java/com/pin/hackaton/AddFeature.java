package com.pin.hackaton;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.faizmalkani.floatingactionbutton.ObservableScrollView;

import java.util.ArrayList;

/**
 * Created by tomer on 19/08/14.
 */
public class AddFeature extends Activity implements AdapterView.OnItemSelectedListener {
    FloatingActionButton mFab;
    LinearLayout lin;
    View child;
    Spinner state,feature;
    ArrayList<String> contacts;
    String name,condition,action,param1,param2,param3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        init();
    }

    void init(){
        contacts = new ArrayList<String>();
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        ObservableScrollView scrollView = (ObservableScrollView)findViewById(R.id.scroller);
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        mFab.listenToScrollView(scrollView);
        lin = (LinearLayout)findViewById(R.id.cond);
        feature = (Spinner)findViewById(R.id.spinnerConditions);
        state = (Spinner)findViewById(R.id.spinnerFeatures);
        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        child = LayoutInflater.from(this).inflate(R.layout.cond_weather, null);
        state.setOnItemSelectedListener(this);
        feature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    boolean isReady(){

        return true;
    }


    public void fabClicked(View view) {
        boolean didItWork = true;
        try {

            SQLiteDB entry = new SQLiteDB(AddFeature.this);
            entry.open();
            entry.createEntry(condition, action, name, param1, param2, param3);
            entry.close();

        } catch (Exception e) {
            didItWork = false;
            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("No");
            TextView tv = new TextView(this);
            tv.setText(error);
            d.setContentView(tv);
            d.show();
        } finally {
            if (didItWork) {
                Dialog d = new Dialog(this);
                d.setTitle("Yes");
                TextView tv = new TextView(this);
                tv.setText("Yes!!");
                d.setContentView(tv);
                d.show();
            }
        }
    }

    public void hideFab(View view) {
        mFab.hide(true);
        //getActionBar().hide();
    }

    public void showFab(View view) {
        mFab.hide(false);
        //getActionBar().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            lin.removeView(child);
                switch (position) {
                    case 0:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_mail, null);
                        lin.addView(child);
                        break;
                    case 1:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_call, null);
                        lin.addView(child);
                        Contacts();
                        break;
                    case 2:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_play_music, null);
                        lin.addView(child);
                        break;
                    case 3:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_wifi, null);
                        lin.addView(child);
                        break;
                    case 4:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_weather, null);
                        lin.addView(child);
                        break;
                    case 5:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_open_app, null);
                        lin.addView(child);
                        break;
                    case 7:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_bluetooth, null);
                        lin.addView(child);
                        break;
                    case 8:
                        child = LayoutInflater.from(AddFeature.this).inflate(R.layout.cond_volume, null);
                        lin.addView(child);
                        break;
                }
            }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void Contacts(){

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {

                    output.append("\nFirst Name:" + name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\nPhone number:" + phoneNumber);

                    }

                    phoneCursor.close();

                    String somthing = output.toString().replaceAll("   ", "");
                    contacts.add(somthing);
                    Log.d("NUMBER",contacts.get(0));
                    output.delete(0,output.length());
                }

            }
            final ListView lv = (ListView)findViewById(R.id.list);
            lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(),contacts.get(position).substring(contacts.get(position).lastIndexOf("Phone number:")+13),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
