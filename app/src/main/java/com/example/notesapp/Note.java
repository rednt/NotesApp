package com.example.notesapp;

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



        Model data = new Model();

        //Button listener for save

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setId(1);
                data.setTitle(title.getText().toString());
                data.setText(mainText.getText().toString());
                Toast.makeText(Note.this,
                        data.toString(),
                        Toast.LENGTH_SHORT).show();

                Database database = new Database(Note.this);
            }
        });


    }
}