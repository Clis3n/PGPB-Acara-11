package com.example.notesapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate;
    private Button btnAdd, btnUpdate;
    private DatabaseHelper dbHelper;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        // Inisialisasi View
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Cek apakah sedang mengedit note yang sudah ada
        if (getIntent() != null && getIntent().hasExtra("id")) {
            noteId = getIntent().getIntExtra("id", -1);
            etTitle.setText(getIntent().getStringExtra("title"));
            etDescription.setText(getIntent().getStringExtra("description"));
            etDate.setText(getIntent().getStringExtra("date"));
            btnAdd.setVisibility(View.GONE); // Sembunyikan tombol Add
        } else {
            btnUpdate.setVisibility(View.GONE); // Sembunyikan tombol Update
        }

        // Listener untuk membuka DatePickerDialog
        etDate.setOnClickListener(v -> {
            // Dapatkan tanggal saat ini
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Tampilkan DatePickerDialog
            new DatePickerDialog(AddEditNoteActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Format tanggal menjadi YYYY-MM-DD
                String date = selectedYear + "-" +
                        String.format("%02d", (selectedMonth + 1)) + "-" +
                        String.format("%02d", selectedDay);
                etDate.setText(date); // Set hasil ke EditText
            }, year, month, day).show();
        });

        // Listener untuk menambahkan note
        btnAdd.setOnClickListener(v -> addNote());

        // Listener untuk memperbarui note
        btnUpdate.setOnClickListener(v -> updateNote());
    }

    private void addNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (validateInputs(title, description, date)) {
            boolean isInserted = dbHelper.insertNote(title, description, date);
            if (isInserted) {
                Toast.makeText(this, "Catatan Ditambahkan", Toast.LENGTH_SHORT).show();
                finish(); // Tutup activity setelah menambahkan note
            } else {
                Toast.makeText(this, "Gagal Menambahkan Catatan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNote() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (validateInputs(title, description, date)) {
            boolean isUpdated = dbHelper.updateNote(noteId, title, description, date);
            if (isUpdated) {
                Toast.makeText(this, "Catatan Diperbarui", Toast.LENGTH_SHORT).show();
                finish(); // Tutup activity setelah memperbarui note
            } else {
                Toast.makeText(this, "Gagal Memperbarui Catatan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String title, String description, String date) {
        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
