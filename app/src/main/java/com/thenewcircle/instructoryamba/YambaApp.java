package com.thenewcircle.instructoryamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by geoff on 8/19/14.
 */
public class YambaApp extends Application {

    private static final String TAG = "newcircle.yamba." + YambaApp.class.getSimpleName();

    private YambaClient yambaClient;
    private static YambaApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        instance = this;
    }

    public YambaClient getYambaClient() {
        if(yambaClient == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String username = prefs.getString("username", null);
            String password = prefs.getString("password", null);
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


}
