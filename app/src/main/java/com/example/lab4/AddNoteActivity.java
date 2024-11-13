package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {
    private EditText txtName, txtContent;
    private DatabaseHelper dbHelper;
    private String oldNoteName; // To store the old note's name for updating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_note_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        txtName = findViewById(R.id.txtName);
        txtContent = findViewById(R.id.txtContent);
        Button btnSave = findViewById(R.id.btnSave);

        // Add hints for input fields from string resources
        txtName.setHint(R.string.note_name_hint);
        txtContent.setHint(R.string.note_content_hint);

        // Check if the activity was opened for editing an existing note
        Intent intent = getIntent();
        if (intent.hasExtra("noteName") && intent.hasExtra("noteContent")) {
            oldNoteName = intent.getStringExtra("noteName");
            String noteContent = intent.getStringExtra("noteContent");

            // Populate the EditText fields with the existing note's information
            txtName.setText(oldNoteName);
            txtContent.setText(noteContent);
        }

        // Set up the button to save the note (or update if editing)
        btnSave.setOnClickListener(v -> {
            String name = txtName.getText().toString();
            String content = txtContent.getText().toString();
            if (name.isEmpty() || content.isEmpty()) {
                Toast.makeText(AddNoteActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
            } else {
                if (oldNoteName == null) {
                    // Add a new note if there is no old name
                    if (dbHelper.addNote(name, content)) {
                        Toast.makeText(AddNoteActivity.this, R.string.save_note_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddNoteActivity.this, R.string.save_note_failure, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Update the existing note
                    if (dbHelper.updateNote(oldNoteName, name, content)) {
                        Toast.makeText(AddNoteActivity.this, R.string.save_note_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddNoteActivity.this, R.string.save_note_failure, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}