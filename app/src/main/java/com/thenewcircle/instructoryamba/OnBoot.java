package com.thenewcircle.instructoryamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by geoff on 8/20/14.
 */
public class OnBoot extends BroadcastReceiver {
    private static final String TAG = "newcircle.yamba." + OnBoot.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

    }
}
