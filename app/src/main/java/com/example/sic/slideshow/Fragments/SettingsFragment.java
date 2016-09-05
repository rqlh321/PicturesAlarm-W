package com.example.sic.slideshow.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sic.slideshow.Adapters.RecycleViewFolderListAdapter;
import com.example.sic.slideshow.MainActivity;
import com.example.sic.slideshow.MyService;
import com.example.sic.slideshow.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sic on 03.09.2016.
 */
public class SettingsFragment extends Fragment {
    TextView setDirText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        setDirText = (TextView) view.findViewById(R.id.setDirTextView);
        final Button setDirButton = (Button) view.findViewById(R.id.setDirButton);
        final Button activate = (Button) view.findViewById(R.id.startService);
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.intervalNumber);
        final TimePicker tpStart = (TimePicker) view.findViewById(R.id.startTime);
        final TimePicker tpEnd = (TimePicker) view.findViewById(R.id.endTime);

        tpStart.setIs24HourView(true);
        tpStart.setCurrentHour((new Date()).getHours());

        tpEnd.setIs24HourView(true);
        tpEnd.setCurrentHour((new Date()).getHours());

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(1);

        setDirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkReadExternalPermission()) {
                        startSelectFolderFragment();
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    startSelectFolderFragment();
                }
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(getContext(), MyService.class);
                            intent.putExtra(MyService.APP_PREFERENCES_ID, getArguments().getString(RecycleViewFolderListAdapter.FOLDER_ID))
                                    .putExtra(MyService.APP_PREFERENCES_SPEED, numberPicker.getValue())
                                    .putExtra(MyService.APP_PREFERENCES_START, tpStart.getCurrentHour() * 3600 + tpStart.getCurrentMinute() * 60)
                                    .putExtra(MyService.APP_PREFERENCES_END, tpEnd.getCurrentHour() * 3600 + tpEnd.getCurrentMinute() * 60);
                            getActivity().startService(intent);
                            Toast.makeText(getContext(), R.string.start, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), R.string.wrong_end, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), R.string.wrong_start, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.wrong_folder, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            String name = getArguments().getString(RecycleViewFolderListAdapter.FOLDER_NAME);
            setDirText.setText(name);
        }
    }

    public void startSelectFolderFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, new SelectFolderFragment())
                .addToBackStack(null)
                .commit();
    }

    private boolean checkReadExternalPermission() {
        int res = getContext().checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}
