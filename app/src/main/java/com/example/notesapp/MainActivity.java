package com.example.notesapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model selectedNote = notesList.get(position);

                // Start Note activity with the data of the selected note
                Intent intent = new Intent(MainActivity.this, Note.class);
                intent.putExtra("ID", selectedNote.getId());
                intent.putExtra("title", selectedNote.getTitle());
                intent.putExtra("text", selectedNote.getText());
                startActivityForResult(intent, 1);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Model note = notesList.get(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete note from database
                                boolean isDeleted = database.deleteData(note.getId());
                                if (isDeleted) {
                                    // Remove the note from the list and update the adapter
                                    notesList.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int id = data.getIntExtra("ID", -1);
            String title = data.getStringExtra("title");
            String text = data.getStringExtra("text");


            if (id != -1 && title != null) {
                // Check if it's an existing note or a new one
                boolean updated = false;
                for (int i = 0; i < notesList.size(); i++) {
                    Model note = notesList.get(i);
                    if (note.getId() == id) {
                        note.setTitle(title);
                        note.setText(text);
                        updated = true;
                        break;
                    }
                }

                if (!updated) {
                    // If it's a new note, add it to the list
                    Model newNote = new Model(id, title, text);
                    notesList.add(newNote);
                }

                adapter.notifyDataSetChanged();
        }
    }

    }
}