package com.example.sic.slideshow.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sic.slideshow.Activity.MainActivity;
import com.example.sic.slideshow.R;

/**
 * Created by sic on 03.09.2016.
 */
public class SettingsFirstStepFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_step_settings, container, false);
        final Button setDirButton = (Button) view.findViewById(R.id.setDirButton);

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
        return view;
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
