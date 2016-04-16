package com.example.sic.slideshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class OpenDir extends Activity {
    ListView listDir;
    TextView textPath;
    Context context;
    int selectIdList = -1;
    String path = "//";
    Button choose;

    ArrayList<String> arrayDir = new ArrayList<>();
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_dir);

        choose = (Button) findViewById(R.id.go);
        listDir = (ListView) findViewById(R.id.list_dir);
        textPath = (TextView) findViewById(R.id.textPath);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayDir);
        listDir.setAdapter(adapter);

        updateListDir();

        listDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIdList = (int) id;
                File file = new File(path + "/" + arrayDir.get(position));
                if (position == 0) {
                    String parents = (new File(path)).getParent();
                    if (parents != null) {
                        path = parents + "/";
                        updateListDir();
                    }
                }
                if (file.isDirectory()) {
                    updateListDir();
                }
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File mainFile = new File(path);
                FilenameFilter fFilter = new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        return (s.contains(".jpg"));
                    }
                };
                File[] files = mainFile.listFiles(fFilter);
                if ((files != null) && (files.length > 0)) {
                    Intent intent = new Intent();
                    intent.putExtra("url", path);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(OpenDir.this, R.string.wrong_folder_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateListDir() {
        if (selectIdList >0) {
            path = path + arrayDir.get(selectIdList) + "/";
        }

        selectIdList = -1;
        arrayDir.clear();
        arrayDir.add("...");
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File aFile : files) {
                if((aFile.isDirectory())||(aFile.getName().contains(".jpg")))
                    arrayDir.add(aFile.getName());
            }
        }
        adapter.notifyDataSetChanged();
        textPath.setText(path);
    }

}