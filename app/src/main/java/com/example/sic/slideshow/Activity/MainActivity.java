package com.example.sic.slideshow.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sic.slideshow.fragments.SelectFolderFragment;
import com.example.sic.slideshow.fragments.SettingsFirstStepFragment;
import com.example.sic.slideshow.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2909;
    public static final String APP_PREFERENCES = "mySettings";
    public static final String APP_PREFERENCES_ID = "id";
    static final public String APP_PREFERENCES_NAME = "name";
    public static final String APP_PREFERENCES_START = "startTime";
    public static final String APP_PREFERENCES_END = "endTime";
    public static final String APP_PREFERENCES_SPEED = "speed";
    public static final String APP_PREFERENCES_ENABELE = "enable";
    public static Date beginSlideShow;
    public static Date endSlideShow;
    boolean permissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null || savedInstanceState.isEmpty()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .add(R.id.container, new SettingsFirstStepFragment())
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    permissionGranted = true;
                } else {
                    Log.e("Permission", "Denied");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionGranted) {
            permissionGranted = false;
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.container, new SelectFolderFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}