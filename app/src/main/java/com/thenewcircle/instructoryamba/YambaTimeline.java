package com.thenewcircle.instructoryamba;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import static com.thenewcircle.instructoryamba.TimelineContract.Columns.*;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.Date;
import java.util.List;

public class YambaTimeline extends IntentService {

    private static final String TAG = "newcircle.yamba." + YambaApp.class.getSimpleName();

    public YambaTimeline() {
        super("YambaTimeline");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        YambaClient client = YambaApp.getInstance().getYambaClient();
        if(client != null) {
            try {
                final ContentResolver resolver  = getContentResolver();
                Cursor c = resolver.query(TimelineContract.CONTENT_URI,
                        TimelineContract.MAX_TIME_CREATED, null, null, null);
                final long maxTime = c.moveToFirst()?c.getLong(0): Long.MIN_VALUE;

                client.fetchFriendsTimeline(new YambaClient.TimelineProcessor() {
                    @Override
                    public boolean isRunnable() {
                        return true;
                    }

                    @Override
                    public void onStartProcessingTimeline() {

                    }

                    @Override
                    public void onEndProcessingTimeline() {

                    }

                    @Override
                    public void onTimelineStatus(long id, Date createdAt, String user, String msg, Double lat, Double lon) {
                        Log.d(TAG, msg);
                        long time = createdAt.getTime();
                        if(time > maxTime) {
                            ContentValues values = new ContentValues();
                            values.put(MESSAGE, msg);
                            values.put(TIME_CREATED, time);
                            values.put(USER, user);
                            values.put(ID, id);
                            values.put(LAT, lat);
                            values.put(LON, lon);
                            Log.d(TAG, "onTimelineStatus LAT = " + lat);
                            resolver.insert(TimelineContract.CONTENT_URI, values);
                        }

                    }
                });


            } catch (YambaClientException e) {
                Log.e(TAG, "Unable to get timeline", e);
            }
        }
    }
}
