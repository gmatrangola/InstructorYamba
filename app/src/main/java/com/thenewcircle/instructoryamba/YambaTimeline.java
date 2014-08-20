package com.thenewcircle.instructoryamba;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

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
                for(YambaClient.Status status : posts) {
                    Log.d(TAG, status.getMessage());
                }
            } catch (YambaClientException e) {
                Log.e(TAG, "Unable to get timeline", e);
            }
        }
    }
}
