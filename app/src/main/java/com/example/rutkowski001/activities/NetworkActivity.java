package com.example.rutkowski001.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bikomobile.multipart.Multipart;
import com.example.rutkowski001.R;
import com.example.rutkowski001.adapters.DrawerArrayAdapter;
import com.example.rutkowski001.classes.DatabaseManager;
import com.example.rutkowski001.classes.Networking;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    String ip;
    File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
    File selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_screen);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);
        pDialog = new ProgressDialog(NetworkActivity.this);
        pDialog.setMessage("kurwa");
        pDialog.setCancelable(false); // nie da się zamknąć klikając w ekran
//        pDialog.show();
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.openDrawer(GravityCompat.START);
        EditText ipEditText = findViewById(R.id.ipEditText);
        Button saveIp = findViewById(R.id.saveIp);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NetworkActivity.this);

        if (preferences.getString("ip", null) != null){
            ip = preferences.getString("ip", null);
            ipEditText.setText(ip);
        }

        saveIp.setOnClickListener(v->{
            ip = String.valueOf(ipEditText.getText());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("ip", ip);
            if(editor.commit()){
                AlertDialog.Builder alert = new AlertDialog.Builder(NetworkActivity.this);
                alert.setTitle("IP SAVED");
                alert.setMessage("IP: "+ ip + " saved successfully");
                alert.setPositiveButton("OK", null);
                alert.show();
            };
        });
        Log.d("ipString", "ip Address: "+ip);
        Networking net = new Networking (
                NetworkActivity.this // activity z galerią zdjęć
        );
        Boolean conn = net.checkConnection();
        if(!conn){
            AlertDialog.Builder alert = new AlertDialog.Builder(NetworkActivity.this);
            alert.setTitle("CONNECTION ERROR");
            alert.setMessage("Check your network connection status");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NetworkActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
            });
            alert.show();

        }

        ListView listView = findViewById(R.id.drawerListView);
        ArrayList<String> list = new ArrayList<>();
        list.add("upload");
        list.add("share");
        list.add("crop");
        DrawerArrayAdapter adapter = new DrawerArrayAdapter (
                NetworkActivity.this,
                R.layout.drawer_list_view_element,
                list
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           if(i == 0){
               Log.d("klik", list.get(i) +" "+ selectedImage);
               Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(selectedImage));
               Multipart multipart = new Multipart(NetworkActivity.this);
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
               byte[] byteArray = stream.toByteArray();
               multipart.addFile("image/jpeg", "file", "plik.jpg", byteArray);
               multipart.launchRequest("http://"+ip+"/api/upload",
                       response -> {
                           Log.d("uploadCheck", "success");
                       },
                       error -> {
                           Log.d("uploadCheck", error.getMessage());
                       });
           }
            }});
        ArrayList<File> photos = new ArrayList<>();
//        File dir = new File(pic, "RutkowskiFilip");
//        File[] dirs = dir.listFiles();
//        Log.d("photos", "dirs: "+ String.valueOf(dirs.length));
//        for(File file:dirs){
//            File[] files = file.listFiles();
//            Log.d("photos", "files: "+ String.valueOf(files.length));
//            for(File photo : files){
//                photos.add(photo);
//            }
//        }

        LinearLayout networkScreen = findViewById(R.id.networkScreen);
        for(File file: pic.listFiles()){
            ImageView iv = new ImageView(NetworkActivity.this);
            iv.setImageURI(Uri.fromFile(file));
            iv.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            networkScreen.addView(iv);
            iv.setOnClickListener(v->{
                selectedImage = file;
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
            });
        }
        Log.d("photos", String.valueOf(photos.size()));



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