package com.example.sic.slideshow.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sic.slideshow.Activity.MainActivity;
import com.example.sic.slideshow.MyService;
import com.example.sic.slideshow.R;

import java.util.Date;

/**
 * Created by sic on 03.09.2016.
 */
public class SettingsFourthStepFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth_step_settings, container, false);

        final SharedPreferences mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String name = mSettings.getString(MainActivity.APP_PREFERENCES_NAME, "");
        final TextView setDirText = (TextView) view.findViewById(R.id.setDirTextView);
        setDirText.setText(getResources().getString(R.string.source_folder,name));

        TextView startTimeText = (TextView) view.findViewById(R.id.start_time_text);
        startTimeText.setText(getResources().getString(R.string.start_time,MainActivity.beginSlideShow.toString()));

        TextView endTimeText = (TextView) view.findViewById(R.id.end_time_text);
        endTimeText.setText(getResources().getString(R.string.end_time,MainActivity.endSlideShow.toString()));

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.intervalNumber);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(1);

        final Button activate = (Button) view.findViewById(R.id.startService);
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                if (MainActivity.beginSlideShow.after(currentDate)) {
                    SharedPreferences mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(MainActivity.APP_PREFERENCES_SPEED, numberPicker.getValue());
                    editor.apply();
                    Intent intent = new Intent(getContext(), MyService.class);
                    getActivity().startService(intent);
                    Toast.makeText(getContext(), R.string.start, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.wrong_start, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


}
