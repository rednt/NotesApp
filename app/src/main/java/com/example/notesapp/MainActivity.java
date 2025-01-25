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
    Database database;

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

        database = new Database(this);
        notesList.addAll(database.getData());
        adapter.notifyDataSetChanged();

        //The add button will change the view to the Note editing page
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, Note.class),1);
                }
            });

        //Clicking on an item opens its related note
        list.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected note
            Model selectedNote = notesList.get(position);

            // Pass note details to the Note activity
            Intent intent = new Intent(MainActivity.this, Note.class);
            intent.putExtra("ID", selectedNote.getId());
            intent.putExtra("title", selectedNote.getTitle());
            intent.putExtra("text", selectedNote.getText());
            startActivity(intent);
        });

        }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh the notes list only when the activity is resumed
        Database database = new Database(this);
        List<Model> updatedNotes = database.getData();

        // Check if the list has changed and update it
        if (updatedNotes.size() != notesList.size()) {
            notesList.clear();
            notesList.addAll(updatedNotes);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("ID", -1);
            String title = data.getStringExtra("title");
            String text = data.getStringExtra("text");


            if (id != -1 && title != null) {
                // Add the new note to the list and update the adapter
                Model newNote = new Model(id,title,text);



                notesList.add(newNote);
                adapter.notifyDataSetChanged();
            }
        }
    }
}