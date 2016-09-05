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
    public static final String APP_PREFERENCES_ID = "id";
    public static final String APP_PREFERENCES_START = "startTime";
    public static final String APP_PREFERENCES_END = "endTime";
    public static final String APP_PREFERENCES_SPEED = "speed";
    public SharedPreferences mSettings;


    int endTime;
    int startTime;
    String id;
    int speed;
    private BroadcastReceiver checkTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            int currentTime = ((new Date()).getHours() * 3600 + (new Date()).getMinutes() * 60);
            if (currentTime == startTime) {
                Intent intentForView = new Intent(MyService.this, ViewActivity.class);
                intentForView.putExtra(APP_PREFERENCES_ID, id)
                        .putExtra(APP_PREFERENCES_SPEED, speed)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentForView);
            }
            if (currentTime == endTime) {
                sendBroadcast(new Intent("kill"));
            }
        }
    };
    private BroadcastReceiver checkCharging = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            Intent intentForView = new Intent(MyService.this, ViewActivity.class);
            intentForView.putExtra(APP_PREFERENCES_ID, id)
                    .putExtra(APP_PREFERENCES_SPEED, speed)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForView);
        }
    };
    private BroadcastReceiver checkBoot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            Intent intentForView = new Intent(MyService.this, ViewActivity.class);
            intentForView.putExtra(APP_PREFERENCES_ID, id)
                    .putExtra(APP_PREFERENCES_SPEED, speed);
            intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentForView);
        }
    };

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
            editor.putString(APP_PREFERENCES_ID, intent.getStringExtra(APP_PREFERENCES_ID));
            editor.putInt(APP_PREFERENCES_START, intent.getIntExtra(APP_PREFERENCES_START, 0));
            editor.putInt(APP_PREFERENCES_END, intent.getIntExtra(APP_PREFERENCES_END, 0));
            editor.putInt(APP_PREFERENCES_SPEED, intent.getIntExtra(APP_PREFERENCES_SPEED, 1));
            editor.apply();
        }
        id = mSettings.getString(APP_PREFERENCES_ID, "");
        startTime = mSettings.getInt(APP_PREFERENCES_START, 0);
        endTime = mSettings.getInt(APP_PREFERENCES_END, 0);
        speed = mSettings.getInt(APP_PREFERENCES_SPEED, 1);

        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        IntentFilter charging = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        IntentFilter reboot = new IntentFilter(Intent.ACTION_REBOOT);

        registerReceiver(checkCharging, charging);
        registerReceiver(checkTime, mTime);
        registerReceiver(checkBoot, reboot);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(checkTime);
        unregisterReceiver(checkBoot);
        unregisterReceiver(checkCharging);
    }
}