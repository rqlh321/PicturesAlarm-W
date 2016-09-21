package com.example.sic.slideshow.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.sic.slideshow.MyService;
import com.example.sic.slideshow.R;
import com.example.sic.slideshow.broadcastReceivers.TimeReceiver;
import com.example.sic.slideshow.fragments.SettingsFourthStepFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private final BroadcastReceiver kill = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopService(new Intent(ViewActivity.this, MyService.class));
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(SettingsFourthStepFragment.NOTIFICATION_ID);
            finish();
        }
    };

    public static Bitmap scaleDown(File f, float maxImageSize,
                                   boolean filter) throws FileNotFoundException {

        Bitmap realImage = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        registerReceiver(kill, new IntentFilter(TimeReceiver.DESTROY_VIEW_ACTIVITY));

        SharedPreferences mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String id = mSettings.getString(MainActivity.APP_PREFERENCES_ID, "");
        int speed = mSettings.getInt(MainActivity.APP_PREFERENCES_SPEED, 1);
        ArrayList<String> pictures = getFilesUri(id);

        AnimationDrawable animation = new AnimationDrawable();
        for (String picture : pictures) {
            try {
                Bitmap b = scaleDown(new File(picture), 640, false);
                Drawable drawable = new BitmapDrawable(getResources(), b);
                animation.addFrame(drawable, speed * 1000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageDrawable(animation);
        animation.setOneShot(false);
        animation.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(kill);
    }

    private ArrayList<String> getFilesUri(String id) {
        ArrayList<String> list = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Images.Media.BUCKET_ID + "=" + id +
                " AND " +
                MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        String sort = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
        Cursor cursor = getContentResolver().query(uri,
                projection,
                selection,
                null,
                sort);

        while (cursor.moveToNext()) {
            list.add(cursor.getString((cursor.getColumnIndex(MediaStore.Images.Media.DATA))));
        }
        cursor.close();
        return list;
    }

}

