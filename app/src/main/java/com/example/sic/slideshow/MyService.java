package com.example.sic.slideshow;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.util.Date;

public class MyService extends Service {
    public static final String APP_PREFERENCES = "mySettings";
    public static final String APP_PREFERENCES_URL = "url";
    public static final String APP_PREFERENCES_START = "startTime";
    public static final String APP_PREFERENCES_END = "endTime";
    public static final String APP_PREFERENCES_SPEED = "speed";
    public SharedPreferences mSettings;


    int endTime;
    int startTime;
    String url;
    int speed;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();
        if (intent != null) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(APP_PREFERENCES_URL, intent.getStringExtra("url"));
            editor.putInt(APP_PREFERENCES_START, intent.getIntExtra("timeToStart", 0));
            editor.putInt(APP_PREFERENCES_END, intent.getIntExtra("timeToStop", 0));
            editor.putInt(APP_PREFERENCES_SPEED, intent.getIntExtra("speed", 1));
            editor.apply();
        }
        url = mSettings.getString(APP_PREFERENCES_URL, "");
        startTime = mSettings.getInt(APP_PREFERENCES_START, 0);
        endTime = mSettings.getInt(APP_PREFERENCES_END, 0);
        speed = mSettings.getInt(APP_PREFERENCES_SPEED, 1);

        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        IntentFilter charging = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        IntentFilter reboot = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);

        registerReceiver(justShow, charging);
        registerReceiver(checkTime, mTime);
        registerReceiver(justShow, reboot);

        return super.onStartCommand(intent, flags, startId);
    }

    private BroadcastReceiver checkTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            int currentTime = ((new Date()).getHours() * 3600 + (new Date()).getMinutes() * 60);
            if (currentTime == startTime) {
                Intent intentForView = new Intent(MyService.this, ViewActivity.class);
                intentForView.putExtra("url", url)
                        .putExtra("speed", speed);
                intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentForView);
            }
            if (currentTime == endTime) {
                sendBroadcast(new Intent("kill"));
            }
        }
    };

    private BroadcastReceiver justShow = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            Intent intentForView = new Intent(MyService.this, ViewActivity.class);
            intentForView.putExtra("url", url)
                    .putExtra("speed", speed);
            intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForView);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(checkTime);
        unregisterReceiver(justShow);
    }
}
