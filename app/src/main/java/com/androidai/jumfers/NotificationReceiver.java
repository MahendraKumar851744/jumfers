package com.androidai.jumfers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("message")) {
            String title = bundle.getString("title");
            String message = bundle.getString("message");
            SharedPreferences preferences = context.getSharedPreferences("notifications", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("title", title);
            editor.putString("message", message);
            editor.apply();
            Log.d("NOTIFICATION_CHANNEL","recieved");
        }
    }
}