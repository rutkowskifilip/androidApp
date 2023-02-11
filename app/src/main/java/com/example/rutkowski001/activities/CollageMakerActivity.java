package com.example.rutkowski001.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.rutkowski001.R;
import com.example.rutkowski001.classes.ImageData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageMakerActivity extends Activity {
    private ImageView selectedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collage_maker);
        ImageButton rotate = findViewById(R.id.rotatePhoto);
        ImageButton flip = findViewById(R.id.flipPhoto);
        ImageButton save = findViewById(R.id.saveCollage);
        ArrayList<ImageData> list = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("list");
        //Log.d("lista", String.valueOf(list.get(1)));
        FrameLayout layout = findViewById(R.id.collageFrameLayout);
        for(ImageData i : list){
            //Log.d("xxx", String.valueOf(i.getX())+i.getY());
            ImageView iv = new ImageView(CollageMakerActivity.this);
            iv.setX(i.getX());
            iv.setY(i.getY());
//            iv.setBackgroundColor(Color.rgb(255,255,255));
            iv.setLayoutParams(new FrameLayout.LayoutParams(i.getW(), i.getH()));
            iv.setImageResource(R.drawable.baseline_photo_camera_white_48);
            iv.setBackgroundColor(Color.rgb(33,33,33));
            iv.setPadding(100,100,100,100);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedImageView = iv;
                }
            });
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CollageMakerActivity.this);
                    alert.setTitle("Select photo source:");
                    selectedImageView = iv;
                    String[] options = {"Camera","Gallery"};
                    alert.setItems(options, new DialogInterface.OnClickListener() {
                        @SuppressLint("QueryPermissionsNeeded")
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("which", String.valueOf(which));
                            if(which == 0){
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, 200);
                                }

                            }else{
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, 100);
                            }
                        }


                    });
//
                    alert.show();



                    return true;
                }
            });
            layout.addView(iv);
        }
        Matrix matrix = new Matrix();

        rotate.setOnClickListener(v->{
            Log.d("klik", "rotate");
            matrix.postRotate(-90);


            Bitmap oryginal = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
            Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

            selectedImageView.setImageBitmap(rotated);
            matrix.reset();
        });
        flip.setOnClickListener(v->{
            Log.d("klik", "flip");
            matrix.postScale(-1.0f, 1.0f);
            Bitmap oryginal = ((BitmapDrawable) selectedImageView.getDrawable()).getBitmap();
            Bitmap rotated = Bitmap.createBitmap(oryginal, 0, 0, oryginal.getWidth(), oryginal.getHeight(), matrix, true);

            selectedImageView.setImageBitmap(rotated);
            matrix.reset();
        });
        save.setOnClickListener(v->{
            layout.setDrawingCacheEnabled(true);
            Bitmap b = layout.getDrawingCache(true);
            File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
            File dir = new File(pic, "RutkowskiFilip");
            File collages = new File(dir, "collages");
            if(!collages.exists()) {
                collages.mkdir();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
            byte[] byteArray = stream.toByteArray();
            FileOutputStream fs = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String d = df.format(new Date());
            try {
                fs = new FileOutputStream(collages+"/"+d+".jpg");
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
            Intent intent = new Intent(CollageMakerActivity.this ,MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(pic, "RutkowskiFilip");
        String[] options = new String[dir.listFiles().length];
        for(int i=0; i< dir.listFiles().length; i++){
            options[i] = dir.listFiles()[i].getName();
        }

        if(requestCode == 200){

            if(resultCode == RESULT_OK){

                try {

                    Bitmap b = (Bitmap) data.getExtras().get("data");
                    selectedImageView.setImageBitmap(b);
                    selectedImageView.setPadding(0, 0, 0, 0);
                    selectedImageView.setBackgroundColor(Color.rgb(255,255,255));
                    selectedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }catch(Exception x){

                }
            }
        }else if(requestCode == 100){
            if(resultCode == RESULT_OK){
                Uri imgData = data.getData();
                try {
                    InputStream stream = getContentResolver().openInputStream(imgData);
                    Bitmap b = BitmapFactory.decodeStream(stream);
                    selectedImageView.setImageBitmap(b);
                    selectedImageView.setPadding(0, 0, 0, 0);
                    selectedImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }catch(Exception x){

                }

            }
        }

    }


}