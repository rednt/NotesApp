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

        Intent intent = getIntent();
        if(intent.hasExtra("ID")){
            int id = intent.getIntExtra("ID", -1);
            String titleExist = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            title.setText(titleExist);
            mainText.setText(text);

        }

        //Button listener for save

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model data;
                try {
                    data = new Model();
                    data.setTitle(title.getText().toString());
                    data.setText(mainText.getText().toString());

                    Database database = new Database(Note.this);
                    boolean success = database.addData(data);

                    if (success) {
                        int id = data.getId();

                        // Pass the new note back to MainActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("ID", id);
                        resultIntent.putExtra("title", data.getTitle());
                        resultIntent.putExtra("text", data.getText());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(Note.this, "Failed to save note", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(Note.this, "Error occurred", Toast.LENGTH_LONG).show();
                }


            }
     });


    }
}