package com.pin.greenblitz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import SwipeDismiss.SwipeDismissListViewTouchListener;
import de.timroes.android.listview.EnhancedListView;


public class Main extends Activity {
    FloatingActionButton mFab;
    List<String> actions,time,location,imagelink,link;
    SwipeDismissListViewTouchListener touchListener;
    boolean editing;
    private EnhancedListAdapter mAdapter;
    EnhancedListView lv;
    ParseObject parseObject;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void init(){
        lv = (EnhancedListView)findViewById(R.id.list);
        mAdapter = new EnhancedListAdapter(actions,time,location,imagelink);

        lv.setAdapter(mAdapter);

        // Set the callback that handles dismisses.
     /*   lv.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {
                parseObject = new ParseObject("Event");
                final String item = (String) mAdapter.getItem(position);
                final String date = (String) mAdapter.getItem(position);
                final String loc = (String) mAdapter.getItem(position);
                parseObject.remove(item);
                parseObject.remove(date);
                parseObject.remove(loc);
                parseObject.saveInBackground();
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        mAdapter.insert(position, item,date,loc);
                    }
                };
            }
        });*/

        lv.setSwipingLayout(R.id.swiping_layout);
      /*  touchListener =
                new SwipeDismissListViewTouchListener(
                        lv,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    lv.delete(position);
                                }
                            }
                        });*/

        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
      //  lv.setOnScrollListener(touchListener.makeScrollListener());
        if(mFab == null) {
            mFab = new FloatingActionButton.Builder(this,800)
                    .withDrawable(getResources().getDrawable(R.drawable.ic_content_new))
                    .withButtonColor(getResources().getColor(R.color.Red))
                    .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                    .create();
            mFab.setText("HELLO WORLD");
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), AddEvent.class));
                }
            });
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Dialog dialog = new AlertDialog.Builder(Main.this)
                        .setView(LayoutInflater.from(Main.this).inflate(R.layout.custom_dialog,null))
                        .show();
                TextView title = (TextView)dialog.findViewById(R.id.title);
                ImageView image = (ImageView)dialog.findViewById(R.id.image);
                TextView time = (TextView)dialog.findViewById(R.id.time);
                TextView location = (TextView)dialog.findViewById(R.id.location);
                Button openLink = (Button)dialog.findViewById(R.id.link);
                title.setText(mAdapter.getItem(position));
                time.setText(mAdapter.getTime(position));
                location.setText(mAdapter.getLocation(position));
                try {
                image.setImageDrawable(mAdapter.getImage(position));
                    Display display = getWindowManager().getDefaultDisplay();
                    int width = display.getWidth();
                    //int width = dialog.getWindow().getDecorView().getWidth();  // deprecated
                    image.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
                    image.getLayoutParams().height = width;

                  }
                 catch (Exception e){
                Toast.makeText(getApplicationContext(),"STILL LOADING...",Toast.LENGTH_LONG).show();
                  }
                openLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLink(link.get(position));

                    }
                });
            }
        });
        mFab.listenTo(lv);
    }

    void openLink(String url){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),"You don't have a browser",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                initlist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.normal, menu);
        if(Build.VERSION.SDK_INT-5 > Build.VERSION_CODES.KITKAT_WATCH) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override public boolean onQueryTextChange(String query) {
                    if(query.length()!=0){
                        List<String> Actions,Time,Location,ImageLink,Link;
                        Link = new ArrayList<String>();
                        Actions = new ArrayList<String>();
                        Time = new ArrayList<String>();
                        Location = new ArrayList<String>();
                        ImageLink = new ArrayList<String>();
                        List<Integer> pos = new ArrayList<Integer>();
                            for(int i = 0;i<actions.size();i++){
                                for(int x = 0;x<actions.get(i).length();x++) {
                                    if (query.contains(actions.get(i).substring(x).toLowerCase())) {
                                        pos.add(i);
                                    }
                                }
                            }
                        for(int i = 0;i<pos.size();i++){
                            Link.add(link.get(i));
                            Actions.add(actions.get(i));
                            Time.add(time.get(i));
                            Location.add(location.get(i));
                            ImageLink.add(imagelink.get(i));
                        }
                        lv.setAdapter(new EnhancedListAdapter(Actions,Time,Location,ImageLink));
                    }
                    else{
                        lv.setAdapter(mAdapter);
                    }
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    void initlist() {
        link = new ArrayList<String>();
        handler = new Handler();
        actions = new ArrayList<String>();
        time = new ArrayList<String>();
        location = new ArrayList<String>();
        imagelink = new ArrayList<String>();
        final ProgressDialog pb = new ProgressDialog(Main.this);
        pb.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading));
        pb.setCancelable(false);
        pb.show();
        for(int i = 0;i<actions.size();){
            actions.remove(i);
        }
        for(int i = 0;i<time.size();){
            actions.remove(i);
        }
        for(int i = 0;i<location.size();){
            actions.remove(i);
        }
        for(int i = 0;i<imagelink.size();){
            actions.remove(i);
        }
        for(int i = 0;i<link.size();){
            link.remove(i);
        }
        if(isNetworkAvailable()) {

            Parse.initialize(this, "8gFySBMbwBzsYRbry37yza9mi7c7Cc84lOu5gx4Q", "tlj9lzzGjtvoihOKBmrf6mWQh4OJ5vaRe8knEAre");

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                    for (int i = 0; i < parseObjects.size(); i++) {
                        actions.add(parseObjects.get(i).getString("event"));
                        time.add(parseObjects.get(i).getString("date"));
                        location.add(parseObjects.get(i).getString("location"));
                        imagelink.add(parseObjects.get(i).getString("ImageLink"));
                        link.add(parseObjects.get(i).getString("Link"));
                    }
                    init();
                    pb.hide();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            pb.hide();
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initlist();
    }


    public void ShowImage(String Link,ImageView img){
        new DownloadImageTask(img)
                .execute(Link);

    }

    private class EnhancedListAdapter extends BaseAdapter {

        public EnhancedListAdapter (List<String> Actions,List<String> Time,List<String> Location,List<String> ImageLink){
            this.mItems = Actions;
            this.mItems2 = Time;
            this.mItems3 = Location;
            this.mItems4  = ImageLink;
            this.drawables = new ArrayList<Drawable>();
        }

        private List<String> mItems;
        private List<String> mItems2;
        private List<String> mItems3;
        private List<String> mItems4;
        public List<Drawable> drawables;



        public void remove(int position) {
        }

        public void insert(int position, String item,String date,String locat) {
            mItems.add(position, item);
            mItems2.add(position, item);
            mItems3.add(position, item);
            notifyDataSetChanged();
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public String getItem(int position) {
            return mItems.get(position);
        }
        public String getTime(int position) {
            return time.get(position);
        }
        public String getLocation(int position) {
            return location.get(position);
        }
        public Drawable getImage(int position){return drawables.get(position);}
        public int getPos(int position) {

            return (position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                // Clicking the delete icon, will read the position of the item stored in
                // the tag and delete it from the list. So we don't need to generate a new
                // onClickListener every time the content of this view changes.


                holder = new ViewHolder();
                assert convertView != null;
                holder.mTextView = (TextView) convertView.findViewById(R.id.text);
                holder.mTextView2 = (TextView) convertView.findViewById(R.id.time);
                holder.mTextView3 = (TextView) convertView.findViewById(R.id.location);
                holder.mTextView4 = (ImageView)convertView.findViewById(R.id.image);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.position = position;
            holder.mTextView.setText(mItems.get(position));
            holder.mTextView2.setText(mItems2.get(position));
            holder.mTextView3.setText(mItems3.get(position));
            ShowImage(mItems4.get(position), holder.mTextView4);

            return convertView;
        }



        private class ViewHolder {
            TextView mTextView;
            TextView mTextView2;
            TextView mTextView3;
            ImageView mTextView4;
            int position;
        }

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
            Drawable d = new BitmapDrawable(getResources(),result);
            bmImage.setImageDrawable(d);
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();
            bmImage.getLayoutParams().width = width/3;
            bmImage.getLayoutParams().height = width/3;
            mAdapter.drawables.add(d);
            if(result.hasAlpha()){
                Toast.makeText(getApplicationContext(),"HAS ALPHA",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
