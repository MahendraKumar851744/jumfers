package com.androidai.jumfers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId("67c8a7d7-6f04-49c2-8d44-2405fb19a1af");



        Log.d("APPLICATION",getSharedPreferences("BASE_APP",MODE_PRIVATE).getString("token",""));

        SharedPreferences preferences = getSharedPreferences("notifications", Context.MODE_PRIVATE);
        String title = preferences.getString("title", "");
        String message = preferences.getString("message", "");
        Log.d("NOTIFICATION_CHANNEL",message);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,new Home_Fragment())
                        .commit();
                return true;
            case R.id.notifications:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,new Frag_Notifications())
                        .commit();
                return true;
            case R.id.history:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment,new Frag_History())
                        .commit();
                return true;
            case R.id.progress:
                return true;
        }
        return false;
    }




}