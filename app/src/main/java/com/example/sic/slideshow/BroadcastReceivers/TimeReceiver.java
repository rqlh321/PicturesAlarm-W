package com.example.sic.slideshow.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.sic.slideshow.Activity.MainActivity;
import com.example.sic.slideshow.Activity.ViewActivity;

import java.util.Date;

/**
 * Created by sic on 06.09.2016.
 */
public class TimeReceiver extends BroadcastReceiver {
 public static final String DESTROY_VIEW_ACTIVITY="kill";
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int startTime = mSettings.getInt(MainActivity.APP_PREFERENCES_START, 0);
        int endTime = mSettings.getInt(MainActivity.APP_PREFERENCES_END, 0);
        int currentTime = ((new Date()).getHours() * 3600 + (new Date()).getMinutes() * 60);
        if (currentTime == startTime) {
            Intent intentForView = new Intent(context, ViewActivity.class);
            intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentForView);
        }
        if (currentTime == endTime) {
            context.sendBroadcast(new Intent(DESTROY_VIEW_ACTIVITY));
        }
    }
}
