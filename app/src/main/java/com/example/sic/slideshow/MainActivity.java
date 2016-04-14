package com.example.sic.slideshow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Formatter;

public class MainActivity extends AppCompatActivity {
    EditText setDirText;
    Button setDirButton;
    Button startService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDirText= (EditText) findViewById(R.id.setDirTextView);
        setDirButton= (Button) findViewById(R.id.setDirButton);
        startService= (Button) findViewById(R.id.startService);

        final TimePicker tpStart= (TimePicker) findViewById(R.id.startTime);
        final TimePicker tpEnd= (TimePicker) findViewById(R.id.endTime);
        tpStart.setIs24HourView(true);
        tpStart.setCurrentHour((new Date()).getHours());
        tpEnd.setIs24HourView(true);
        tpEnd.setCurrentHour((new Date()).getHours());

        NumberPicker numberPicker= (NumberPicker) findViewById(R.id.intervalNumber);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(1);

        setDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,OpenDir.class);
                startActivityForResult(intent, 1);
            }
        });

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setDirText.getText().toString().isEmpty()){
                    Date start=new Date();
                    start.setHours(tpStart.getCurrentHour());
                    start.setMinutes(tpStart.getCurrentMinute());
                    Date end=new Date();
                    end.setHours(tpEnd.getCurrentHour());
                    end.setMinutes(tpEnd.getCurrentMinute());
                    Date currentDate=new Date();
                    if(start.after(currentDate)){
                        if(end.after(start)){
                            Toast.makeText(MainActivity.this, R.string.start_service, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,R.string.wrong_end, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this,R.string.wrong_start, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, R.string.wrong_folder, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null) {return;}
        String url = data.getStringExtra("url");
        setDirText.setText(url);
    }
}
