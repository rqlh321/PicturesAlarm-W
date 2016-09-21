package com.example.sic.slideshow.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sic.slideshow.activity.MainActivity;
import com.example.sic.slideshow.R;

import java.util.Date;

/**
 * Created by sic on 03.09.2016.
 */
public class SettingsSecondStepFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_step_settings, container, false);

        final SharedPreferences mSettings = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        final TimePicker tpStart = (TimePicker) view.findViewById(R.id.startTime);
        tpStart.setIs24HourView(true);

        Button next = (Button) view.findViewById(R.id.nextSetting);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date start = new Date();
                start.setHours(tpStart.getCurrentHour());
                start.setMinutes(tpStart.getCurrentMinute());
                MainActivity.beginSlideShow=start;
                Date currentDate = new Date();
                if (start.after(currentDate)) {
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(MainActivity.APP_PREFERENCES_START, tpStart.getCurrentHour() * 3600 + tpStart.getCurrentMinute() * 60);
                    editor.apply();

                    SettingsThirdStepFragment settingsFragment = new SettingsThirdStepFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.container, settingsFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getContext(), R.string.wrong_start, Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }
}
