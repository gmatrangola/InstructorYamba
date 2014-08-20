package com.thenewcircle.instructoryamba;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by geoff on 8/19/14.
 */
public class YambaApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "newcircle.yamba." + YambaApp.class.getSimpleName();
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String INTERVAL = "interval";

    private YambaClient yambaClient;
    private static YambaApp instance;
    private PendingIntent refreshPendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        instance = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        setupTimer();
    }

    private void setupTimer() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Long interval = Long.valueOf(prefs.getString(INTERVAL, "0"));

        Log.d(TAG, "Setting up alarm with interval " + interval);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(refreshPendingIntent != null) {
            alarmManager.cancel(refreshPendingIntent);
            refreshPendingIntent = null;
        }

        if(interval > 0 ) {
            Intent refreshIntent = new Intent(this, YambaTimeline.class);
            refreshPendingIntent = PendingIntent.getService(this, 0, refreshIntent, 0);
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
                    interval, refreshPendingIntent);
        }
    }

    public YambaClient getYambaClient() {
        if(yambaClient == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String username = prefs.getString(USERNAME, null);
            String password = prefs.getString(PASSWORD, null);
            if(username != null && username.length() != 0 &&
                password != null && password.length() != 0) {
                yambaClient = new YambaClient(username, password);
            }
        }
        return yambaClient;
    }

    public static YambaApp getInstance() {
        return instance;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged " + key);
        if(key.equals(USERNAME) || key.equals(PASSWORD)) {
            yambaClient = null;
        }
        else if(key.equals(INTERVAL)) {
            setupTimer();
        }
    }
}
