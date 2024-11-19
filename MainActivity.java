package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private FloatingActionButton fabAdd;
    private NotesAdapter adapter;
    private DatabaseHelper dbHelper;
    private ArrayList<HashMap<String, String>> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi View
        rvNotes = findViewById(R.id.rvNotes);
        fabAdd = findViewById(R.id.fabAdd);

        // Inisialisasi DatabaseHelper dan mendapatkan semua notes
        dbHelper = new DatabaseHelper(this);
        notesList = dbHelper.getAllNotes(); // Pastikan Anda mendapatkan data notes dari database

        // Setup RecyclerView dengan adapter
        adapter = new NotesAdapter(this, notesList);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(adapter);

        // Listener untuk Floating Action Button (FAB) untuk menambah note baru
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update daftar notes saat activity kembali muncul
        notesList.clear();
        notesList.addAll(dbHelper.getAllNotes()); // Mengambil data terbaru dari database
        adapter.notifyDataSetChanged();
    }
}
