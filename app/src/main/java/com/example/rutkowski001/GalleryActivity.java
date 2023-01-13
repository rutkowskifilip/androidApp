package com.example.rutkowski001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        LinearLayout linearLayout = findViewById(R.id.photoGallery);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String directoryString = (String) bundle.get("directory");
        //Log.d("files", directoryString);
        File dir = new File(directoryString);
        File[] files = dir.listFiles() ;
        //Log.d("files", String.valueOf(files));
        assert files != null;
        for (File file: files) {
            if(file.isFile()) {
                ImageView img = new ImageView(GalleryActivity.this);
                String imagepath = String.valueOf(file);
                //Log.d("files", imagepath);
                Bitmap bmp = betterImageDecode(imagepath);
                img.setImageBitmap(bmp);
                linearLayout.addView(img);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400));
                img.setOnClickListener(new View.OnClickListener(){
                    @Override
                            public void onClick(View v){
                                Log.d("file", imagepath);
                                Intent intent = new Intent(GalleryActivity.this, PhotoActivity.class);

                                intent.putExtra("imagepath", imagepath);
                                startActivity(intent);

                    }
                });
            }


        }

    }

    private Bitmap betterImageDecode(String filePath) {
        Bitmap myBitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();    //opcje przekształcania bitmapy
        options.inSampleSize = 4; // zmniejszenie jakości bitmapy 4x
        //
        myBitmap = BitmapFactory.decodeFile(filePath, options);
        return myBitmap;
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