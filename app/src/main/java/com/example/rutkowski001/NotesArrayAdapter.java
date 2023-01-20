package com.example.rutkowski001;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    @SuppressLint("ViewHolder")
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

        imagePath.setText(_list.get(position).getImagePath());
        ImageView editBtn = (ImageView) convertView.findViewById(R.id.noteEdit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxx", "klik");
                // klik w obrazek
            }
        });
        editBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view){
                Log.d("xxx", "long klik");
                return false;
            }
        });

        return convertView;
    }
}
