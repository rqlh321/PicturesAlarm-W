package com.example.sic.slideshow;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FilenameFilter;

public class ViewActivity extends AppCompatActivity {
    File[] files;
    int counter = 0;
    ImageView imageView;
    Show show;
    long delay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        imageView= (ImageView) findViewById(R.id.image);
        String url = getIntent().getStringExtra("url");
        delay = getIntent().getIntExtra("speed", 1);

        File mainFile = new File(url);
        FilenameFilter fFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                    return (s.contains(".jpg"));
            }
        };
        files = mainFile.listFiles(fFilter);

        imageView.setImageBitmap(BitmapFactory.decodeFile(files[counter].getPath()));//показать 1ю картинку
        counter++;

        new Show().execute();
    }

    public class Show extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(delay*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imageView.setImageBitmap(BitmapFactory.decodeFile(files[counter].getPath()));
            counter++;
            if(counter==files.length) counter=0;
            show =new Show();
            show.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        show.cancel(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        show.cancel(true);
        finish();
    }
}

