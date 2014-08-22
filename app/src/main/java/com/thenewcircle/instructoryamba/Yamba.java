package com.thenewcircle.instructoryamba;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

import static com.thenewcircle.instructoryamba.TimelineContract.Columns.*;

/**
 * Implementation of App Widget functionality.
 */
public class Yamba extends AppWidgetProvider {

    private static final String TAG = "newcircle.yamba" + Yamba.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        Log.d(TAG, "onUpdate");
        Intent refreshIntent = new Intent(context, YambaTimeline.class);
        context.startService(refreshIntent);
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(TimelineContract.CONTENT_URI, null, null, null,
                TIME_CREATED + " desc");
        if(cursor.moveToFirst()) {
            String message = cursor.getString(cursor.getColumnIndex(MESSAGE));
            String user = cursor.getString(cursor.getColumnIndex(USER));
            long time = cursor.getLong(cursor.getColumnIndex(TIME_CREATED));
            String friendly = DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), 0).toString();
            for (int i=0; i<N; i++) {
                updateAppWidget(context, message, user, friendly, appWidgetManager, appWidgetIds[i]);
            }
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, String message, String user, String friendly, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yamba);
        views.setTextViewText(R.id.textViewMessage, message);
        views.setTextViewText(R.id.textViewUser, user);
        views.setTextViewText(R.id.textViewTime, friendly);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


