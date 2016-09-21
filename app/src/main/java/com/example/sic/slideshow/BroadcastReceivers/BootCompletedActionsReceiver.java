package com.example.sic.slideshow.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.sic.slideshow.activity.MainActivity;
import com.example.sic.slideshow.activity.ViewActivity;
import com.example.sic.slideshow.MyService;

/**
 * Created by sic on 06.09.2016.
 */
public class BootCompletedActionsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSettings = context.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String id = mSettings.getString(MainActivity.APP_PREFERENCES_ID, "");
        if (!id.equals("")) {
            Intent service = new Intent(context, MyService.class);
            context.startService(service);
            Intent intentForView = new Intent(context, ViewActivity.class);
            intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentForView);
        }
    }
}
