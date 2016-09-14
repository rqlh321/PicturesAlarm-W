package com.example.sic.slideshow;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.sic.slideshow.Activity.MainActivity;
import com.example.sic.slideshow.BroadcastReceivers.BootCompletedActionsReceiver;
import com.example.sic.slideshow.BroadcastReceivers.ChargingActionsReceiver;
import com.example.sic.slideshow.BroadcastReceivers.TimeReceiver;

public class MyService extends Service {
    private final int NOTIFICATION_ID = 0;
    NotificationManager mNotifyMgr;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title");

        Intent resultIntent = new Intent(this, MyService.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, notification);
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotifyMgr.cancel(NOTIFICATION_ID);
    }

}