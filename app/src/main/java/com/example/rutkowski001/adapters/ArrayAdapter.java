package com.example.rutkowski001.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.rutkowski001.classes.DatabaseManager;
import com.example.rutkowski001.R;

import java.io.File;
import java.util.ArrayList;

public class ArrayAdapter extends android.widget.ArrayAdapter {

    public ArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this._list= objects;
        this._context = context;
        this._resource = resource;
    }
    private ArrayList<String> _list;
    private Context _context;
    private int _resource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);
        ImageView image =  (ImageView)  convertView.findViewById(R.id.photoList);
        image.setImageURI(Uri.parse(_list.get(position)));


        ImageButton delete = (ImageButton) convertView.findViewById(R.id.deletePhotoList);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("DELETE PHOTO");
                alert.setMessage("Delete photo " + _list.get(position) + "?");

                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(_list.get(position));
                        file.delete();
                        _list.remove(position);
                        notifyDataSetChanged();
                    }

                });

                alert.setNeutralButton("Cancel", null);

                alert.show();

                }

        });
        ImageButton edit = (ImageButton) convertView.findViewById(R.id.editPhotoList);

        View finalConvertView = convertView;

        edit.setOnClickListener(v -> {
            View editView = View.inflate(_context, R.layout.note_input, null);

            String [] colors = {"#ff0000", "#00ff00","#0000ff", "#ffff00", "#ff00ff", "#00ffff", "#f0f0f0", "#ffffff"};
            LinearLayout colorPicker = (LinearLayout) editView.findViewById(R.id.colorPicker);
//            Log.d("xxx", String.valueOf(colorPicker));
            for(String color: colors){
                Button b = new Button(_context);
                b.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                b.setBackgroundColor(Color.parseColor(color));
                b.setOnClickListener(y -> {
                    EditText title = (EditText) editView.findViewById(R.id.noteTitle);
                    title.setTextColor(Color.parseColor(color));
                });
                colorPicker.addView(b);
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("NOTE");
            alert.setMessage("Note to photo:\n" + _list.get(position));
            alert.setView(editView);



            alert.setNeutralButton("Save", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseManager db = new DatabaseManager (
                            _context, // activity z galerią zdjęć
                            "NotesRutkowskiFilip.db", // nazwa bazy
                            null,
                            3 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
                    );
                    EditText titleInput = (EditText) editView.findViewById(R.id.noteTitle);
                    String title = titleInput.getText().toString();
                    EditText descInput = (EditText) editView.findViewById(R.id.noteDescription);
                    String desc = titleInput.getText().toString();
                    int color = titleInput.getCurrentTextColor();

                    db.insert(title, desc, String.valueOf(color),_list.get(position));
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();


        });
        ImageButton info = (ImageButton) convertView.findViewById(R.id.infoPhotoList);
        info.setOnClickListener( v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Info");
            alert.setMessage("Photo info\n" + _list.get(position));



            alert.setNeutralButton("OK", null);

            alert.show();


        });
        return convertView;
    }

    @Override
    public int getCount() {
        return _list.size();
    }

    }
