package com.example.rutkowski001.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rutkowski001.R;
import com.example.rutkowski001.classes.Note;

import java.util.ArrayList;

public class DrawerArrayAdapter extends ArrayAdapter{
    private ArrayList<String> _list;
    private Context _context;
    private int _resource;
    public DrawerArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this._list = objects;
        this._context =context;
        this._resource = resource;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int[] icons = {R.drawable.baseline_cloud_upload_white_48, R.drawable.baseline_share_white_48,R.drawable.baseline_crop_white_48};

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.drawer_list_view_element, null);
        TextView text = (TextView) convertView.findViewById(R.id.drawerText);
        text.setText(_list.get(position));
        ImageView image = (ImageView) convertView.findViewById(R.id.drawerIcon);
        image.setImageResource(icons[position]);

        return convertView;
    }
}
