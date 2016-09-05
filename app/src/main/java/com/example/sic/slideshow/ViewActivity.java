package com.example.sic.slideshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    private final BroadcastReceiver kill = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBackPressed();
        }
    };
    ArrayList<String> pictures;
    int counter = 0;
    ImageView imageView;
    Show show;
    long speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        registerReceiver(kill, new IntentFilter("kill"));

        imageView = (ImageView) findViewById(R.id.image);
        String id = getIntent().getStringExtra(MyService.APP_PREFERENCES_ID);
        speed = getIntent().getIntExtra(MyService.APP_PREFERENCES_SPEED, 1);
        pictures = getFilesUri(id);
        imageView.setImageBitmap(BitmapFactory.decodeFile(pictures.get(counter)));//показать 1ю картинку
        counter++;

        new Show().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        show.cancel(true);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(kill);
    }

    private ArrayList<String> getFilesUri(String id) {
        ArrayList<String> list = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};

        String sort = MediaStore.Files.FileColumns.DATE_ADDED + " DESC";
        Cursor cursor = getContentResolver().query(uri,
                projection,
                MediaStore.Images.Media.BUCKET_ID + "=?" + "AND(" + selection + ")",
                new String[]{id},
                sort);

        while (cursor.moveToNext()) {
            list.add(cursor.getString((cursor.getColumnIndex(MediaStore.Images.Media.DATA))));
        }
        cursor.close();
        return list;
    }

    public class Show extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(speed * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imageView.setImageBitmap(BitmapFactory.decodeFile(pictures.get(counter)));
            counter++;
            if (counter == pictures.size()) counter = 0;
            show = new Show();
            show.execute();
        }
    }
}

