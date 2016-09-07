package com.example.sic.slideshow.Activity;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.sic.slideshow.BroadcastReceivers.TimeReceiver;
import com.example.sic.slideshow.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    private final BroadcastReceiver kill = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    public static Bitmap decodeAndResizeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 370;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
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
            Bitmap b = decodeAndResizeFile(new File(picture));
            Drawable drawable = new BitmapDrawable(getResources(), b);
            animation.addFrame(drawable, speed * 1000);
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
