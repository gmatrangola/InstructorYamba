package com.thenewcircle.instructoryamba;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class PostMessageService extends IntentService {

    private static final String TAG = "newcircle.yamba." + PostMessageService.class.getSimpleName();
    public static final int NOTIFICATION_PASSWORD_ERROR = 100;
    private static final int NOTIFICATION_MESSAGE_POSTED = 101;

    public PostMessageService() {
        super("PostMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent ");
        // long start = System.currentTimeMillis();
        String message = intent.getStringExtra("message");
        Log.d(TAG, "sending Message " + message);

        YambaClient client = YambaApp.getInstance().getYambaClient();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if(client == null) {
            Log.w(TAG, "No username or password set");
            Notification.Builder notification = new Notification.Builder(this);
            notification.setSmallIcon(R.drawable.ic_launcher);
            notification.setContentTitle("Yamba Error");
            notification.setContentText("Username or Password not set");
            notificationManager.notify(NOTIFICATION_PASSWORD_ERROR, notification.getNotification());
        }
        else {
            notificationManager.cancel(NOTIFICATION_PASSWORD_ERROR);
            try {
                Notification.Builder notification = new Notification.Builder(this);
                notification.setSmallIcon(R.drawable.ic_launcher);
                notification.setContentTitle("Yamba");
                notification.setContentText("Posted: " + message);
                Intent statusIntent = new Intent(this, TimelineActivity.class);
                statusIntent.putExtra("fromNotification", true);
                Double lat = null;
                Double lon = null;
                if(intent.hasExtra("lat")) {
                    lat = intent.getDoubleExtra("lat", 0);
                    lon = intent.getDoubleExtra("lon", 0);
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, statusIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                notification.setContentIntent(pendingIntent);
                notificationManager.notify(NOTIFICATION_MESSAGE_POSTED, notification.getNotification());
                if(lat != null) {
                    client.postStatus(message, lat, lon);
                }
                else {
                    client.postStatus(message);
                }
            } catch (YambaClientException e) {
                Log.e(TAG, "Error posting: " + message, e);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }
}
