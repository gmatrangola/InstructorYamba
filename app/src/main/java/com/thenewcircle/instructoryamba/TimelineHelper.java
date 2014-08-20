package com.thenewcircle.instructoryamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.thenewcircle.instructoryamba.TimelineContract.Columns.*;

/**
 * Created by geoff on 5/21/14.
 */
public class TimelineHelper extends SQLiteOpenHelper {
    public static final String TABLE = "timeline";
    public static final int VERSION = 1;
    private static final String TAG = "Yamba." + TimelineHelper.class.getSimpleName();

    public TimelineHelper(Context context) {
        super(context, TABLE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INT PRIMARY KEY, " +
         " %s INT, %s TEXT, %s TEXT, %s INT, %s INT);",
         TABLE, ID, TIME_CREATED,USER, MESSAGE, LAT, LON);
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Sorry no upgrade
    }
}
