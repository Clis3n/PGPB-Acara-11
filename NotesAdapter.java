package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final Context context;
    private final ArrayList<HashMap<String, String>> notesList;

    public NotesAdapter(Context context, ArrayList<HashMap<String, String>> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // Get the note at the current position
        HashMap<String, String> note = notesList.get(position);

        // Get data from the map
        String title = note.get("title");
        String description = note.get("description");
        String date = note.get("date");

        // Set title text
        if (title != null && !title.isEmpty()) {
            holder.tvTitle.setText(context.getString(R.string.title_text, title));
        } else {
            holder.tvTitle.setText(R.string.title_not_available);
        }

        // Set description text
        if (description != null && !description.isEmpty()) {
            holder.tvDescription.setText(context.getString(R.string.description_text, description));
        } else {
            holder.tvDescription.setText(R.string.description_not_available);
        }

        // Set date text
        if (date != null && !date.isEmpty()) {
            holder.tvDate.setText(context.getString(R.string.date_text, date));
        } else {
            holder.tvDate.setText(R.string.date_not_available);
        }

        // Set item click listener for editing the note
        holder.itemView.setOnClickListener(v -> {
            String idString = note.get("id");
            if (idString != null) {
                try {
                    // Parse the note ID
                    int id = Integer.parseInt(idString);
                    Intent intent = new Intent(context, AddEditNoteActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("title", title);
                    intent.putExtra("description", description);
                    intent.putExtra("date", date);
                    context.startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Handle error if ID is not valid
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        // Declare TextViews to hold the title, description, and date
        TextView tvTitle, tvDescription, tvDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews by finding them in the itemView
            tvTitle = itemView.findViewById(R.id.tvNoteTitle); // Ensure this matches the ID in your XML
            tvDescription = itemView.findViewById(R.id.tvNoteDesc); // Ensure this matches the ID in your XML
            tvDate = itemView.findViewById(R.id.tvNoteDate); // Ensure this matches the ID in your XML
        }
    }
}
