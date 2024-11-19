package com.example.notesapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_DATE + " TEXT)";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        // Create tables again
        onCreate(db);
    }

    // Method untuk menambahkan note
    public boolean insertNote(String title, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DATE, date);

        long result = db.insert(TABLE_NOTES, null, values);
        db.close();
        return result != -1;
    }

    // Method untuk memperbarui note
    public boolean updateNote(int noteId, String title, String description, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_DATE, date);

        int result = db.update(TABLE_NOTES, values, KEY_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
        return result > 0;
    }

    // Method untuk mendapatkan semua notes
    public ArrayList<HashMap<String, String>> getAllNotes() {
        ArrayList<HashMap<String, String>> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);

            // Pastikan cursor tidak kosong
            if (cursor != null && cursor.moveToFirst()) {
                int idColumnIndex = cursor.getColumnIndex(KEY_ID);
                int titleColumnIndex = cursor.getColumnIndex(KEY_TITLE);
                int descriptionColumnIndex = cursor.getColumnIndex(KEY_DESCRIPTION);
                int dateColumnIndex = cursor.getColumnIndex(KEY_DATE);

                // Pastikan kolom yang dibutuhkan ada
                if (idColumnIndex != -1 && titleColumnIndex != -1 && descriptionColumnIndex != -1 && dateColumnIndex != -1) {
                    do {
                        HashMap<String, String> note = new HashMap<>();
                        note.put("id", cursor.getString(idColumnIndex));
                        note.put("title", cursor.getString(titleColumnIndex));
                        note.put("description", cursor.getString(descriptionColumnIndex));
                        note.put("date", cursor.getString(dateColumnIndex));
                        notesList.add(note);
                    } while (cursor.moveToNext());
                } else {
                    throw new SQLException("Satu atau lebih kolom tidak ada di dalam database");
                }
            }
        } catch (SQLException e) {
            // Menangani error jika ada masalah pada query atau cursor
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return notesList;
    }
}
