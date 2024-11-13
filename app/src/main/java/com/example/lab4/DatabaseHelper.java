// DatabaseHelper.java
package com.example.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table if exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id, name, content FROM notes", null);
    }

    public boolean addNote(String name, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("content", content);

        long result = db.insert("notes", null, values);
        db.close();
        return result != -1;
    }

    public boolean updateNote(String oldName, String newName, String newContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("content", newContent);

        int rowsUpdated = db.update("notes", values, "name=?", new String[]{oldName});
        db.close();
        return rowsUpdated > 0;
    }

    public boolean deleteNoteByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("notes", "name=?", new String[]{name});
        db.close();
        return rowsDeleted > 0;
    }

    public Cursor getNoteNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT name FROM notes", null);
    }
}