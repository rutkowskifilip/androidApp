package com.example.rutkowski001.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rutkowski001.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NewAlbumsActivity extends AppCompatActivity {
    File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_albums);
        ImageButton addAlbumButton = findViewById(R.id.addNewAlbum);
        ListView listView = findViewById(R.id.newAlbumsList);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);

        File dir = new File(pic, "RutkowskiFilip");


        File[] files = dir.listFiles() ;
        ArrayList<String> array = new ArrayList<>();
        Arrays.sort(files);
        for (int i=0; i< files.length;i++){
            array.add(files[i].getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                NewAlbumsActivity.this,
                R.layout.photo_list_view_element,
                R.id.tv1,
                array );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewAlbumsActivity.this, NewGalleryActivity.class);
                intent.putExtra("directory", "" + files[i]);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                AlertDialog.Builder alert = new AlertDialog.Builder(NewAlbumsActivity.this);
                alert.setTitle("DELETE DIRECTORY");
                alert.setMessage("Are you sure that you want to delete this directory?");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File dir2 = new File(dir, array.get(i));
                        for (File file : dir2.listFiles()){
                            file.delete();

                        }
                        dir2.delete();

                        array.remove(i);

                        adapter.notifyDataSetChanged();
                    }

                });

//no
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl which
                    }
                });
//
                alert.show();

                return true;
            }
        });

        addAlbumButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert = new AlertDialog.Builder(NewAlbumsActivity.this);
                alert.setTitle("NEW DIRECTORY");
                alert.setMessage("Directory name:");

                EditText input = new EditText(NewAlbumsActivity.this);
                input.setText("");
                alert.setView(input);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        File dir2 = new File(dir, String.valueOf(input.getText()));
                        dir2.mkdir();
                        array.add(String.valueOf(input.getText()));
                        Collections.sort(array);
                        adapter.notifyDataSetChanged();
                    }
                }).show();  // null to pusty click

            };
            //alert.show();

        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}