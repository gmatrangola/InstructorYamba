package com.thenewcircle.instructoryamba;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class TimelineProvider extends ContentProvider {

    private static final int STATUS_DIR = 1;
    private static final int STATUS_ITEM = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(TimelineContract.AUTHORITY, TimelineContract.PATH.substring(1),
                STATUS_DIR);
        URI_MATCHER.addURI(TimelineContract.AUTHORITY, TimelineContract.PATH.substring(1)
         + "/#", STATUS_ITEM);
    }

    private static final String TAG = "Yamba." + TimelineProvider.class.getSimpleName();
    private TimelineHelper timelineHelper;

    public TimelineProvider() {
    }

    @Override
    public boolean onCreate() {
        // connect to the database
        timelineHelper = new TimelineHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert " + uri + " values: " + values);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
