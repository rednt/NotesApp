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

        long insert = db.insert(NOTES_TABLE, null, cv); // Inserts the data into the database
        model.setId((int) insert); // Set the generated ID back to the model
        db.close(); // Close the database connection to avoid leaks

        //Debugging
        System.out.println("id inserted is: " + insert);
        if (insert != -1) {
            System.out.println("Data Inserted Successfully");
        } else {
            System.out.println("Data Insertion Failed");
        }

        return insert != -1;
        }


    public List<Model> getData(){

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_TITLE + ", " + COLUMN_MAIN_TEXT + " FROM " + NOTES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        List<Model> returnList = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);

                Model newData = new Model(id, title, text);
                returnList.add(newData);

            }while(cursor.moveToNext());

        }

        //Closing the cursor and db

        cursor.close();
        db.close();
        return returnList;
    }

    //Updating records
    public boolean updateData(int id, String title, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_MAIN_TEXT, text);

        // Update the record where the ID matches
        int result = db.update(NOTES_TABLE, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        //Debugging
        System.out.println("id updated is: " + id);
        if (result != -1) {
            System.out.println("Data Updated Successfully");
        } else {
            System.out.println("Data Update Failed");
        }

        return result > 0; // Return true if at least one row was updated

    }
}
