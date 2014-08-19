package com.pin.hackaton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import com.faizmalkani.floatingactionbutton.ObservableScrollView;

/**
 * Created by tomer on 19/08/14.
 */
public class AddFeature extends Activity {
    FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        init();
    }
    void init(){
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        ObservableScrollView scrollView = (ObservableScrollView)findViewById(R.id.scroller);
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        mFab.listenToScrollView(scrollView);
        final LinearLayout lin = (LinearLayout)findViewById(R.id.cond);
        for(int i = 0; i<20;i++){
            lin.addView(new TextView(getApplicationContext()));
        }
    }
}
