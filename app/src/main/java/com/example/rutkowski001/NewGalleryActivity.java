package com.example.rutkowski001;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.rutkowski001.adapters.TestAdapter;

import java.io.File;
import java.util.ArrayList;

public class NewGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gallery);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);
        ListView listView = findViewById(R.id.newPhotosList);
        Bundle bundle = getIntent().getExtras();
        String directoryString = (String) bundle.get("directory");

        File dir = new File(directoryString);
        File[] files = dir.listFiles() ;
        ArrayList<String> list = new ArrayList();
        for (File file:files
             ) {

            list.add(String.valueOf(file));
        }

        TestAdapter adapter = new TestAdapter (
                NewGalleryActivity.this,
                R.layout.new_photo_list_view_element,
                list
        );
        listView.setAdapter(adapter);
    }
}