package com.thenewcircle.instructoryamba;

import android.net.Uri;

/**
 * Created by geoff on 5/21/14.
 */
public class TimelineContract {

    public static final String AUTHORITY = "com.thenewcircle.yamba.provider";
    public static final String PATH = "/timeline";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + PATH);

    public static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.thenewcircle.yamba.timeline.status";
    public static final String SINGLE_RECORDS_MIME_TYPE = "vnd.android.cursor.itemvnd.thenewcircle.yamba.timeline.status";

    public static class Columns {
        // TODO put constants for column names here
        public static final String ID = "_id";
        public static final String MESSAGE = "message";
        public static final String TIME_CREATED = "time_created";
        public static final String USER = "user";
        public static final String LAT = "lat";
        public static final String LON = "lon";
        private Columns() {

        }
    }

    public static final String[] MAX_TIME_CREATED =  { "MAX (" + Columns.TIME_CREATED + ")"};

    public static final String DEFAULT_SORT_ORDER = Columns.TIME_CREATED + " DESC";
    private TimelineContract() {}
}
