// DeleteNoteActivity.java
package com.example.lab4;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {
    private Spinner spinnerNotes;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        // Enable the up button in the action bar for back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        spinnerNotes = findViewById(R.id.spinnerNotes);
        Button btnDelete = findViewById(R.id.btnDelete);

        loadNoteNames();

        btnDelete.setOnClickListener(v -> {
            String name = (String) spinnerNotes.getSelectedItem();
            if (name != null && dbHelper.deleteNoteByName(name)) {
                Toast.makeText(DeleteNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                loadNoteNames(); // Reload the note names after deletion
                Log.d("DeleteNoteActivity", "Note deleted with name: " + name);
                finish();
            } else {
                Toast.makeText(DeleteNoteActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNoteNames() {
        Cursor cursor = dbHelper.getNoteNames();
        ArrayList<String> noteNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex("name");
            if (nameIndex != -1) {
                noteNames.add(cursor.getString(nameIndex));
            }
        }
        cursor.close();

        // Use a custom ArrayAdapter with green text
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noteNames) {
            @Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.view.View view = super.getView(position, convertView, parent);
                android.widget.TextView textView = (android.widget.TextView) view;
                textView.setTextColor(getResources().getColor(R.color.green)); // Set the text color to green
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotes.setAdapter(adapter);
    }
}