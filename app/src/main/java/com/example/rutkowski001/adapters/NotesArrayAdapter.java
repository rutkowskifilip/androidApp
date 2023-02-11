package com.example.rutkowski001.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.rutkowski001.classes.DatabaseManager;
import com.example.rutkowski001.classes.Note;
import com.example.rutkowski001.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NotesArrayAdapter extends ArrayAdapter {
    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;
    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this._list = objects;
        this._context = context;
        this._resource = resource;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.note_list_view_element, null);
// convertView = inflater.inflate(_resource, null);


        TextView noteId = (TextView) convertView.findViewById(R.id.noteId);
        noteId.setText(_list.get(position).getId());
        TextView title = (TextView) convertView.findViewById(R.id.noteTitle);

        title.setText(_list.get(position).getTitle());
        title.setTextColor(Integer.parseInt(_list.get(position).getColor()));
        TextView desc = (TextView) convertView.findViewById(R.id.noteDescription);

        desc.setText(_list.get(position).getDescription());
        TextView imagePath = (TextView) convertView.findViewById(R.id.noteImagePath);
        String[] imagepath = _list.get(position).getImagePath().split("/");

        imagePath.setText("/"+imagepath[6]+"/"+ imagepath[7]);
        notifyDataSetChanged();
        ImageView editBtn = (ImageView) convertView.findViewById(R.id.noteEdit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxx", "klik");
                // klik w obrazek
            }
        });
        DatabaseManager db = new DatabaseManager (
                _context,
                "NotesRutkowskiFilip.db",
                null,
                3
        );
        editBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("NOTE");

                String[] options = {"Edit", "Delete", "Sort by title", "Sort by color"};
                alert.setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // wyswietl opcje[whic
                        if(which == 0){
                            View editView = View.inflate(_context, R.layout.note_input, null);
                            EditText titleInput = (EditText) editView.findViewById(R.id.noteTitle);
                            EditText descInput = (EditText) editView.findViewById(R.id.noteDescription);
                            titleInput.setText(_list.get(position).getTitle());
                            descInput.setText(_list.get(position).getTitle());
                            titleInput.setTextColor(Integer.parseInt(_list.get(position).getColor()));
                            String [] colors = {"#ff0000", "#00ff00","#0000ff"};
                            LinearLayout colorPicker = (LinearLayout) editView.findViewById(R.id.colorPicker);
//            Log.d("xxx", String.valueOf(colorPicker));
                            for(String color: colors){
                                Button b = new Button(_context);

                                b.setLayoutParams(new LinearLayout.LayoutParams(100,100));
                                b.setBackgroundColor(Color.parseColor(color));
                                b.setOnClickListener(y -> {

                                    titleInput.setTextColor(Color.parseColor(color));
                                });
                                colorPicker.addView(b);
                            }
                            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                            alert.setTitle("NOTE");
                            alert.setMessage("Photo note:\n" + _list.get(position).getImagePath());
                            alert.setView(editView);



                            alert.setNeutralButton("Save", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    String title = titleInput.getText().toString();
                                    String desc = descInput.getText().toString();
                                    int color = titleInput.getCurrentTextColor();
                                    db.update(_list.get(position).getId(), title, desc, String.valueOf(color), _list.get(position).getImagePath());
                                    _list.set(position, new Note(_list.get(position).getId(), title, desc, String.valueOf(color), _list.get(position).getImagePath()));
                                    notifyDataSetChanged();
                                }
                            });
                            alert.setNegativeButton("Cancel", null);
                            alert.show();

                        }
                        else if(which == 1){
                            db.delete((String) noteId.getText());
                            _list.remove(position);
                            notifyDataSetChanged();
                        }else if(which == 2){
                            Collections.sort(_list, new Comparator<Note>() {
                                @Override
                                public int compare(Note a, Note b) {
                                    return a.getTitle().compareTo(b.getTitle());
                                }
                            });
                            notifyDataSetChanged();
                        }else if(which == 3){
                            Collections.sort(_list, new Comparator<Note>() {
                                @Override
                                public int compare(Note a, Note b) {
                                    return a.getColor().compareTo(b.getColor());
                                }
                            });
                            notifyDataSetChanged();
                        }
                        Log.d("xxx", String.valueOf(which));

                    }
                });

                alert.show();
                return true;
            }

        });

        return convertView;
    }
}
