package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list;
    FloatingActionButton add;
    List<Model> notesList;
    ArrayAdapter<Model> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        list = findViewById(R.id.list);
        add = findViewById(R.id.addNote);

        // Initialize the notes list and adapter

        notesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        list.setAdapter(adapter);

        // Load existing data from the database

        Database database = new Database(this);
        notesList.addAll(database.getData());
        adapter.notifyDataSetChanged();

        //The add button will change the view to the Note editing page
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Note.class),1);
                }
            });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("ID", -1);
            String title = data.getStringExtra("title");


            if (id != -1 && title != null) {
                // Add the new note to the list and update the adapter
                Model newNote = new Model(id,title,"");

                //Debugging
                System.out.println("this is for main activity: "+ id);

                notesList.add(newNote);
                adapter.notifyDataSetChanged();
            }
        }
    }
}