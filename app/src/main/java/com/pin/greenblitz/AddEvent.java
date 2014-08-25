package com.pin.greenblitz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;

/**
 * Created by tomer on 21/08/14.
 */
public class AddEvent extends Activity {
    EditText name,date,location,imageLink,link;
    TextView nam,dat,loc;
    ImageView img;
    ParseObject parseObject;
    String pathName;
    FloatingActionButton confirm;
    String ADMIN;
    private static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
        init();

    }
    void init(){
        nam = (TextView)findViewById(R.id.text);
        dat = (TextView)findViewById(R.id.time);
        loc = (TextView)findViewById(R.id.loc);
        img = (ImageView)findViewById(R.id.img);
        Parse.initialize(this, "8gFySBMbwBzsYRbry37yza9mi7c7Cc84lOu5gx4Q", "tlj9lzzGjtvoihOKBmrf6mWQh4OJ5vaRe8knEAre");
        final ParseObject parseObject = new ParseObject("Event");
        location = (EditText)findViewById(R.id.location);
        link = (EditText)findViewById(R.id.link);
        name = (EditText)findViewById(R.id.name);
        date = (EditText)findViewById(R.id.date);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nam.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                nam.setText(s.toString());
            }
        });
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dat.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                dat.setText(s.toString());
            }
        });
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loc.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                loc.setText(s.toString());
            }
        });

        imageLink = (EditText)findViewById(R.id.image);
        imageLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ShowImage(s.toString(),img);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ShowImage(s.toString(),img);
            }
        });
        confirm = new FloatingActionButton.Builder(AddEvent.this,800)
                .withButtonColor(Color.GREEN)
                .withDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept))
                .withGravity(Gravity.BOTTOM|Gravity.RIGHT)
                .withMargins(0,0,16,16)
                .create();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReady())
                {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Admin");
                    query.getInBackground("g9iWVS9lyr", new GetCallback<ParseObject>() {


                        @Override
                        public void done(ParseObject parseObjecta, com.parse.ParseException e) {
                            if (e == null) {
                                ADMIN = parseObjecta.getString("ADMINPASS");
                                final EditText edit = new EditText(getApplicationContext());
                                edit.setHint("PASSWORD");
                                edit.setTextColor(getResources().getColor(R.color.Black));
                                Dialog passowrd = new AlertDialog.Builder(AddEvent.this)
                                        .setTitle("Please enter admin password")
                                        .setView(edit)
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                if (edit.getText().toString().equals(ADMIN)) {
                                                    parseObject.put("event", name.getText().toString());
                                                    parseObject.put("location", location.getText().toString());
                                                    parseObject.put("date", date.getText().toString());
                                                    parseObject.put("ImageLink",imageLink.getText().toString());
                                                    parseObject.put("Link",link.getText().toString());
                                                    parseObject.saveInBackground();
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"WRONG PASSWORD",Toast.LENGTH_LONG).show();
                                                    edit.setError("Wrong Passowrd");
                                                }
                                            }
                                        })
                                        .show();
                                // object will be your game score
                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                                // something went wrong
                            }
                        }
                    });

                }
                else{
                    Dialog error = new AlertDialog.Builder(AddEvent.this)
                            .setTitle("Error adding event")
                            .setMessage("Please fix errors before uploading event")
                            .show();
                }
            }
        });
    }

    public void pick(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri _uri = data.getData();

            //User had pick an image.
            Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();

            //Link to the image
            final String imageFilePath = cursor.getString(0);
            pathName = imageFilePath;
            Drawable d = Drawable.createFromPath(pathName);
            ImageView img = (ImageView)findViewById(R.id.image);
            img.setImageDrawable(d);
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ShowImage(String Link,ImageView img) {
        new DownloadImageTask(img)
                .execute(Link);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                if (result.hasAlpha()) {
                    imageLink.setError("Image contains alpha!");
                }
            }
            catch (NullPointerException e){
                imageLink.setError("Not an image!");
            }
                Drawable d = new BitmapDrawable(getResources(), result);
                bmImage.setImageDrawable(d);
                bmImage.getLayoutParams().width = 300;

        }
    }

    boolean isReady(){
        name.setError(null);
        location.setError(null);
        date.setError(null);
        link.setError(null);
        imageLink.setError(null);
        if(name.getText().toString().isEmpty()){
            name.setError("REQUIRED FIELD",getResources().getDrawable(android.R.drawable.stat_notify_error));
            return false;
        }
        if(date.getText().toString().isEmpty()){
            date.setError("REQUIRED FIELD");
            return false;
        }
        if(location.getText().toString().isEmpty()){
            location.setError("REQUIRED FIELD");
            return false;
        }
        if(imageLink.getText().toString().isEmpty()){
            imageLink.setError("REQUIRED FIELD");
            return false;
        }
        else if(imageLink.getError()!=null){
            return false;
        }
        if(link.getText().toString().isEmpty()){
            link.setError("REQUIRED FIELD");
            return false;
        }


        else {
            return true;
        }
    }
}
