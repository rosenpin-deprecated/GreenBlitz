package com.pin.hackaton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ehud on 8/19/14.
 */
public class SQLiteDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_STATE = "state";
    public static final String KEY_FEATURE = "feature";
    public static final String KEY_Name = "name";
    public static final String KEY_EXTRA1 = "extra1";
    public static final String KEY_EXTRA2 = "extra2";
    public static final String KEY_EXTRA3 = "extra3";


    private static final String DATABASE_NAME = "Hackathon3.0db";
    private static final String DATABASE_TABLE = "peopleTable";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;


    private static class DbHelper extends SQLiteOpenHelper {


        public DbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABlE " + DATABASE_TABLE + " (" +
                    KEY_ROWID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_STATE + " TEXT NOT NULL, " +
                    KEY_FEATURE + " TEXT NOT NULL, " +
                    KEY_Name + " TEXT NOT NULL, " +
                    KEY_EXTRA1 + " TEXT NOT NULL, " +
                    KEY_EXTRA2 + " TEXT NOT NULL, " +
                    KEY_EXTRA3 + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public SQLiteDB(Context c){
        ourContext = c;
    }

    public SQLiteDB open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String state, String feature, String name, String extra1, String extra2, String extra3){
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATE, state);
        cv.put(KEY_FEATURE, feature);
        cv.put(KEY_Name, name);
        cv.put(KEY_EXTRA1, extra1);
        cv.put(KEY_EXTRA2, extra2);
        cv.put(KEY_EXTRA3, extra3);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public String getName(long l){
        String[] columns = new String[] {KEY_ROWID, KEY_STATE, KEY_FEATURE, KEY_Name, KEY_EXTRA1, KEY_EXTRA2, KEY_EXTRA3};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if(c != null){
            c.moveToFirst();
            String name = c.getString(3);
            return  name;
        }
        return null;
    }
    public String getAllNames(){
        String[] columns = new String[] {KEY_ROWID, KEY_STATE, KEY_FEATURE, KEY_Name, KEY_EXTRA1, KEY_EXTRA2, KEY_EXTRA3};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + 0, null, null, null, null);
        if(c != null){
            c.moveToFirst();
            String name = c.getString(3);
            return name;
        }
        return null;
    }



    public String getData(){
        String[] columns = new String[] {KEY_ROWID, KEY_STATE, KEY_FEATURE, KEY_Name, KEY_EXTRA1, KEY_EXTRA2, KEY_EXTRA3};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iState = c.getColumnIndex(KEY_STATE);
        int iFeature = c.getColumnIndex(KEY_FEATURE);
        int iName = c.getColumnIndex(KEY_Name);
        int iExtra1 = c.getColumnIndex(KEY_EXTRA1);
        int iExtra2 = c.getColumnIndex(KEY_EXTRA2);
        int iExtra3 = c.getColumnIndex(KEY_EXTRA3);


        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            result = result + c.getString(iRow) + " " + c.getString(iState) + " " + c.getString(iFeature) +  " " + c.getString(iName) + " " + c.getString(iExtra1) + " " + c.getString(iExtra2) + " " + c.getString(iExtra3) + "\n";
        }

        return result;
    }

    public String getState(long l){
        String[] columns = new String[] {KEY_ROWID, KEY_STATE, KEY_FEATURE, KEY_Name, KEY_EXTRA1, KEY_EXTRA2, KEY_EXTRA3};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if(c != null){
            c.moveToFirst();
            String name = c.getString(1);
            return  name;
        }

        return null;
    }

    public String getFeature(long l){
        String[] columns = new String[] {KEY_ROWID, KEY_STATE, KEY_FEATURE, KEY_Name, KEY_EXTRA1, KEY_EXTRA2, KEY_EXTRA3};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if(c != null){
            c.moveToFirst();
            String feature = c.getString(2);
            return  feature;
        }

        return null;
    }

    public void updateEntry(long lRow, String mState, String mFeature, String mExtra1, String mExtra2, String mExtra3){

        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_STATE, mState);
        cvUpdate.put(KEY_FEATURE, mFeature);
        cvUpdate.put(KEY_Name, mFeature);
        cvUpdate.put(KEY_EXTRA1, mExtra1);
        cvUpdate.put(KEY_EXTRA2, mExtra2);
        cvUpdate.put(KEY_EXTRA3, mExtra3);
        ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + lRow, null);
    }

    public void deleteEntry(long lRow1){
        ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);

    }

}
