package com.androidai.jumfers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class CustomNotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Log.d("NOTIFICATION_CHANNEL",packageName);

        Bundle extras = sbn.getNotification().extras;

        if (packageName.equals("com.onesignal")) {
            String title = extras.getString("android.title");
            String message = extras.getString("android.text");

            // Store the notification in your local database or preferred storage mechanism
            // Example: You can use SQLite, SharedPreferences, or any other solution of your choice

            // Here's an example of storing the notification in SharedPreferences
            SharedPreferences preferences = getSharedPreferences("notifications", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("title", title);
            editor.putString("message", message);
            editor.apply();
            Log.d("NOTIFICATION_CHANNEL","recieved");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // This method is called when a notification is removed from the notification shade.
    }
}
