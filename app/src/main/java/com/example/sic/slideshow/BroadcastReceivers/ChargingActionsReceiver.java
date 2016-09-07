package com.example.sic.slideshow.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.sic.slideshow.Activity.ViewActivity;

/**
 * Created by sic on 06.09.2016.
 */
public class ChargingActionsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentForView = new Intent(context, ViewActivity.class);
        intentForView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentForView);
    }
}
