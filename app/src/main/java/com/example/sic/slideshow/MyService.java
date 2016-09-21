package com.example.sic.slideshow;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.sic.slideshow.broadcastReceivers.BootCompletedActionsReceiver;
import com.example.sic.slideshow.broadcastReceivers.ChargingActionsReceiver;
import com.example.sic.slideshow.broadcastReceivers.TimeReceiver;

public class MyService extends Service {
    private TimeReceiver timeAction = new TimeReceiver();
    private BootCompletedActionsReceiver rebootAction = new BootCompletedActionsReceiver();
    private ChargingActionsReceiver chargingAction = new ChargingActionsReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(timeAction, new IntentFilter(Intent.ACTION_TIME_TICK));
        registerReceiver(chargingAction, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(rebootAction, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeAction);
        unregisterReceiver(chargingAction);
        unregisterReceiver(rebootAction);
    }

}