package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Note extends AppCompatActivity {

    Button save;
    EditText mainText, title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        save = findViewById(R.id.btnSave);
        mainText = findViewById(R.id.mainText);
        title = findViewById(R.id.inputTitle);

        //Check for existing note

        int noteId = getIntent().getIntExtra("ID", -1);
        if (noteId != -1) {
            // Fill the fields with the existing note's data
            String noteTitle = getIntent().getStringExtra("title");
            String noteText = getIntent().getStringExtra("text");

            title.setText(noteTitle);
            mainText.setText(noteText);
        }

        //Button listener for save

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String noteTitle = title.getText().toString();
                String noteText = mainText.getText().toString();

                Database database = new Database(Note.this);
                boolean success;
                Model data = new Model();
                // Check if we are editing an existing note
                int noteId = getIntent().getIntExtra("ID", -1);
                if (noteId != -1) {
                    // Update the existing note
                    success = database.updateData(noteId, noteTitle, noteText);
                    data.setId(noteId);
                    data.setTitle(noteTitle);
                    data.setText(noteText);
                } else {
                    // Create a new note
                    data.setTitle(noteTitle);
                    data.setText(noteText);

                    success = database.addData(data);
                    noteId = data.getId();  // Get the ID of the newly created note
                }

                if (success) {
                    // Pass the updated/new note back to MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ID", noteId);
                    resultIntent.putExtra("title", data.getTitle());
                    resultIntent.putExtra("text", data.getText());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(Note.this, "Failed to save note", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}