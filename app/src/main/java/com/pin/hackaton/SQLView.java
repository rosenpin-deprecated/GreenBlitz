package com.pin.hackaton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by ehud on 8/19/14.
 */
public class SQLView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlview);
        TextView tv = (TextView) findViewById(R.id.tvSQlinfo);

        SQLiteDB info = new SQLiteDB(this);
        info.open();
        String data = info.getData();
        info.close();
        tv.setText(data);
    }
}
