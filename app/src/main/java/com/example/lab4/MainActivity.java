package com.example.lab4;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.main_activity_title);
            toolbar.setTitleTextColor(getResources().getColor(R.color.green));
        }

        dbHelper = new DatabaseHelper(this);

        // Initialize ListView and load notes
        ListView notesListView = findViewById(R.id.notesListView);
        loadNotes(notesListView);

        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            int nameIndex = cursor.getColumnIndex("name");
            int contentIndex = cursor.getColumnIndex("content");

            if (nameIndex >= 0 && contentIndex >= 0) {
                String noteName = cursor.getString(nameIndex);
                String noteContent = cursor.getString(contentIndex);

                // Pass the note's name and content to the AddNoteActivity for editing
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra("noteName", noteName);
                intent.putExtra("noteContent", noteContent);
                startActivity(intent);
            }
        });
    }

    private void loadNotes(ListView notesListView) {
        Cursor cursor = dbHelper.getAllNotes();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"name", "content"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        notesListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView notesListView = findViewById(R.id.notesListView);
        loadNotes(notesListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_note) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (itemId == R.id.delete_note) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}