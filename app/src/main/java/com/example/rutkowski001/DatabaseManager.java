package com.example.rutkowski001;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'title' TEXT, 'description' TEXT, 'color' TEXT, 'imagePath' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public boolean insert(String title, String description, String color, String imagePath){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("color", color);
        contentValues.put("imagePath", imagePath);
        db.insertOrThrow("notes", null, contentValues); // gdy insert się nie powiedzie, będzie błąd
        db.close();
        return true;
    }
    public int delete(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("notes",
                "_id = ? ",
                new String[]{id});
    }
    @SuppressLint("Range")
    public ArrayList<Note> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();
        @SuppressLint("Recycle") Cursor result = db.rawQuery("SELECT * FROM notes" , null);
        while(result.moveToNext()){
            notes.add( new Note(
                    result.getString(result.getColumnIndex("_id")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("description")),
                    result.getString(result.getColumnIndex("color")),
                    result.getString(result.getColumnIndex("imagePath"))


            ));

        }
        return notes;
    }

}
