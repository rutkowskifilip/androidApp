package com.example.rutkowski001;

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

import java.io.File;
import java.util.Arrays;

public class AlbumsActivity extends AppCompatActivity {
    private ListView listView;
    private ImageButton addAlbumButton;
    File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_screen);

        addAlbumButton = findViewById(R.id.addAlbum);
        listView = findViewById(R.id.albumsList);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);

        File dir = new File(pic, "RutkowskiFilip");


        File[] files = dir.listFiles() ;// tablica plików
        String[] array = new String[files.length]; // ilość plików].{};
        Arrays.sort(files); // sortowanie plików wg nazwy
        for (int i=0; i< files.length;i++){
            array[i] = files[i].getName();
        }
        Log.d("xxx", Arrays.toString(files));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AlbumsActivity.this,       // tzw Context
                R.layout.photo_list_view_element,     // nazwa pliku xml naszego wiersza na liście
                R.id.tv1,                // id pola txt w wierszu
                array );                 // tablica przechowująca testowe dane

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumsActivity.this, GalleryActivity.class);
                intent.putExtra("directory", "" + files[i]);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("klik", "long");
                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setTitle("!!!");
                alert.setMessage("Delete directory?");
//ok
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File dir = new File(pic, array[i]);
                        for (File file : dir.listFiles()){
                            file.delete();

                        }
                        dir.delete();
                        Log.d("long", String.valueOf(array[i]));
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

                return false;
            }
        });

        addAlbumButton.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setTitle("New directory");
                alert.setMessage("Directory name");

                EditText input = new EditText(AlbumsActivity.this);
                input.setText("");
                alert.setView(input);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("klik", String.valueOf(input.getText()));
                        File dir = new File(pic, String.valueOf(input.getText()));
                        dir.mkdir();
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