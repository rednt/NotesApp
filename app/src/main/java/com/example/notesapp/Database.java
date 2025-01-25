package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "Notes_Table";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_MAIN_TEXT = "main_text";
    public static final String COLUMN_ID = "ID";

    public Database(@Nullable Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_MAIN_TEXT + " TEXT); ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addData(Model model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COLUMN_TITLE, model.getTitle());
        cv.put(COLUMN_MAIN_TEXT, model.getText());

        long insertId = db.insert(NOTES_TABLE, null, cv);

        if (insertId != -1) {
            model.setId((int) insertId); // Set the generated ID back to the model

            //Debugging
            System.out.println("id inserted is: "+ insertId);

            return true;
        } else {
            return false;
        }
    }

    public List<Model> getData(){

        String query = "SELECT " + COLUMN_TITLE + " FROM " + NOTES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        List<Model> returnList = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{

                String Title = cursor.getString(0);
                Model newData = new Model();
                newData.setTitle(Title);
                newData.setText(" ");
                returnList.add(newData);

            }while(cursor.moveToNext());

        }else{

            //Empty nothing added
        }

        //Closing the cursor and db

        cursor.close();
        db.close();
        return returnList;
    }
}
