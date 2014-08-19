package com.pin.hackaton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import SwipeDismiss.SwipeDismissListViewTouchListener;
import de.timroes.android.listview.EnhancedListView;


public class Main extends Activity {
    FloatingActionButton mFab;
    List<String> actions;
    stuff Stuff = new stuff();
    private EnhancedListAdapter mAdapter;
    EnhancedListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initlist();
        init();
    }

    void init(){
        lv = (EnhancedListView)findViewById(R.id.list);
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
        mFab.listenTo(lv);
        mAdapter = new EnhancedListAdapter();

        lv.setAdapter(mAdapter);

        // Set the callback that handles dismisses.
        lv.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                final String item = (String) mAdapter.getItem(position);
                return new EnhancedListView.Undoable() {
                    @Override
                    public void undo() {
                        mAdapter.insert(position, item);
                    }
                };
            }
        });

        lv.setSwipingLayout(R.id.swiping_layout);
        SwipeDismissListViewTouchListener touchListener =
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
                        });
        lv.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }

    void initlist(){
        actions = new ArrayList<String>();
        for(int i = 0;i<Stuff.list.length;i++){
            actions.add(Stuff.list[i]);
            actions.add(Stuff.list[i]);
            actions.add(Stuff.list[i]);
        }
    }

    public void fabClicked(View view) {
        startActivity(new Intent(getApplicationContext(), AddFeature.class));
    }

    public void hideFab(View view) {
        mFab.hide(true);
        //getActionBar().hide();
    }

    public void showFab(View view) {
        mFab.hide(false);
        //getActionBar().show();
    }

    class stuff{
        String list[]={"ehud","ido","tomer","shit","more stuff","moer","hello","sda","","","asdg"};
    }
    private class EnhancedListAdapter extends BaseAdapter {

        private List<String> mItems = actions;

        public void remove(int position) {
        }

        public void insert(int position, String item) {
            mItems.add(position, item);
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
        public Object getItem(int position) {
            return mItems.get(position);
        }
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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.position = position;
            holder.mTextView.setText(mItems.get(position));

            return convertView;
        }

        private class ViewHolder {
            TextView mTextView;
            int position;
        }

    }
}
