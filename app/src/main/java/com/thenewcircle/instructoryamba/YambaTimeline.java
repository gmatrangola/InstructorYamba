package com.thenewcircle.instructoryamba;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import static com.thenewcircle.instructoryamba.TimelineContract.Columns.*;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

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
                List<YambaClient.Status> posts = client.getTimeline(100);
                final ContentResolver resolver  = getContentResolver();


                for(YambaClient.Status status : posts) {
                    Log.d(TAG, status.getMessage());
                    ContentValues values = new ContentValues();
                    values.put(MESSAGE, status.getMessage());
                    values.put(TIME_CREATED, status.getCreatedAt().getTime());
                    values.put(USER, status.getUser());
                    resolver.insert(TimelineContract.CONTENT_URI, values);
                }
            } catch (YambaClientException e) {
                Log.e(TAG, "Unable to get timeline", e);
            }
        }
    }
}
