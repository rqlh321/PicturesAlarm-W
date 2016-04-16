package com.example.sic.slideshow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText setDirText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDirText = (EditText) findViewById(R.id.setDirTextView);
        final Button setDirButton = (Button) findViewById(R.id.setDirButton);
        final Button startService = (Button) findViewById(R.id.startService);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.intervalNumber);
        final TimePicker tpStart = (TimePicker) findViewById(R.id.startTime);
        final TimePicker tpEnd = (TimePicker) findViewById(R.id.endTime);

        assert tpStart != null;
        tpStart.setIs24HourView(true);
        tpStart.setCurrentHour((new Date()).getHours());

        assert tpEnd != null;
        tpEnd.setIs24HourView(true);
        tpEnd.setCurrentHour((new Date()).getHours());

        assert numberPicker != null;
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(1);

        assert setDirButton != null;
        setDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OpenDir.class);
                startActivityForResult(intent, 1);
            }
        });

        assert startService != null;
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!setDirText.getText().toString().isEmpty()) {
                        Date start = new Date();
                        start.setHours(tpStart.getCurrentHour());
                        start.setMinutes(tpStart.getCurrentMinute());
                        Date end = new Date();
                        end.setHours(tpEnd.getCurrentHour());
                        end.setMinutes(tpEnd.getCurrentMinute());
                        Date currentDate = new Date();
                        if (start.after(currentDate)) {
                            if (end.after(start)) {
                                Intent intent = new Intent(MainActivity.this, MyService.class);
                                intent.putExtra("url", setDirText.getText().toString())
                                        .putExtra("speed", numberPicker.getValue())
                                        .putExtra("timeToStart", tpStart.getCurrentHour() * 3600 + tpStart.getCurrentMinute() * 60)
                                        .putExtra("timeToStop", tpEnd.getCurrentHour() * 3600 + tpEnd.getCurrentMinute() * 60);
                                startService(intent);
                                Toast.makeText(MainActivity.this, R.string.start, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, R.string.wrong_end, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.wrong_start, Toast.LENGTH_SHORT).show();
                        }
                } else {
                    Toast.makeText(MainActivity.this, R.string.wrong_folder, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String url = data.getStringExtra("url");
        setDirText.setText(url);
    }
}