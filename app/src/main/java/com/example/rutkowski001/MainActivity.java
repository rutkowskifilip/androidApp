package com.example.rutkowski001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amitshekhar.DebugDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private LinearLayout cameraLayout;
    private LinearLayout albumsLayout;
    private LinearLayout collageLayout;
    private LinearLayout networkLayout;
    private LinearLayout newAlbumsLayout;
    private LinearLayout notes;
    File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
        checkPermission(Manifest.permission.CAMERA, 100);
        Log.d("xxx", DebugDB.getAddressLog());
        //
        File dir = new File(pic, "RutkowskiFilip");
        if(dir.exists() == false) {
            dir.mkdir();

            File dir1 = new File(dir, "miejsca");
            File dir2 = new File(dir, "ludzie");
            File dir3 = new File(dir, "rzeczy");
            dir1.mkdir();
            dir2.mkdir();
            dir3.mkdir();

        }
        cameraLayout = findViewById(R.id.camera);
        albumsLayout = findViewById(R.id.albums);
        collageLayout = findViewById(R.id.collage);
        networkLayout = findViewById(R.id.network);
        notes = findViewById(R.id.notes);
        newAlbumsLayout = findViewById(R.id.newAlbums);
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Wybierz źródło zdjęcia:");
//nie może mieć setMessage!!!
                String[] options = {"Aparat","Galeria"};
                alert.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("which", String.valueOf(which));
                        if(which == 0){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//jeśli jest dostępny aparat
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(intent, 200); // 200 - stała wartość, która później posłuży do identyfikacji tej akcji
                            }
                        }
                    }
                });
//
                alert.show();
//                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
//                startActivity(intent);
            }
        });
        albumsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
                startActivity(intent);
            }
        });
        collageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CollageActivity.class);
                startActivity(intent);
            }
        });
        networkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  NetworkActivity.class);
                startActivity(intent);
            }
        });
        newAlbumsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  NewAlbumsActivity.class);
                startActivity(intent);
            }
        });
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  NotesActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //tak
                } else {
                    //nie
                }
                break;
            case 101 :

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("W którym folderze zapisać zdjęcię?");

                File dir = new File(pic, "RutkowskiFilip");
                File[] files = dir.listFiles() ;// tablica plików
                String[] options = new String[files.length]; // ilość plików].{};
                Arrays.sort(files); // sortowanie plików wg nazwy
                for (int i=0; i< files.length;i++){
                    options[i] = files[i].getName();
                }

                alert.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("which", String.valueOf(which));
                        Bundle extras = data.getExtras();
                        Bitmap b = (Bitmap) extras.get("data");
//                        ImageView iv = null;
//                        iv.setImageBitmap(b);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
                        byte[] byteArray = stream.toByteArray();
                        FileOutputStream fs = null;
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String d = df.format(new Date());
                        try {
                            fs = new FileOutputStream(dir+"/"+files[which].getName()+"/"+d+".jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fs.write(byteArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
//
                alert.show();


            }
        }
    }
}