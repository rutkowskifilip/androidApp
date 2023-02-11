package com.example.rutkowski001.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.rutkowski001.R;
import com.example.rutkowski001.classes.ImageData;

import java.util.ArrayList;

public class CollageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_screen);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayList<ImageData> list = new ArrayList<>();
        ImageView first = findViewById(R.id.firstCollage);
        ImageView second = findViewById(R.id.secondCollage);
        ImageView third = findViewById(R.id.thirdCollage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("xxx","szerokość ekranu " + size.x);
        Log.d("xxx","wysokość ekranu " +size.y);
        int width = size.x;
        int height = size.y;
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(new ImageData(0,0, width,(height-100)/2));
                list.add(new ImageData(0,(height-100)/2,width/2,(height-100)/2));
                list.add(new ImageData(width/2,(height-100)/2,width/2,(height-100)/2));
                Intent intent = new Intent(CollageActivity.this, CollageMakerActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);

            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(new ImageData(0,0,width/3,(height-100)));
                list.add(new ImageData(width/3,0,width/3,(height-100)));
                list.add(new ImageData(width/3*2,0,width/3,(height-100)));
                Intent intent = new Intent(CollageActivity.this, CollageMakerActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(new ImageData(0,0,width/2,(height-100)/2));
                list.add(new ImageData(0,(height-100)/2,width/2,(height-100)/2));
                list.add(new ImageData(width/2,0,width/2,(height-100)));
                Intent intent = new Intent(CollageActivity.this, CollageMakerActivity.class);
                intent.putExtra("list", list);
                startActivity(intent);

            }
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