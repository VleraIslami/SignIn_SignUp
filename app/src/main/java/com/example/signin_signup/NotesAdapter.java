package com.example.signin_signup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {

    private Context context;
    private List<Note> notesList;
    private SQLiteHelper dbHelper;

    public NotesAdapter(Context context, List<Note> notesList) {
        super(context, R.layout.list_item_note, notesList);
        this.context = context;
        this.notesList = notesList;
        this.dbHelper = new SQLiteHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false);
        }

        final Note note = notesList.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvNoteTitle);
        TextView tvContent = convertView.findViewById(R.id.tvNoteContent);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteNote);
        Button btnEdit = convertView.findViewById(R.id.btnEditNote);  // Find the Edit button

        // Get references to the EditText fields in the layout
        EditText etTitle = convertView.findViewById(R.id.etNoteTitle);
        EditText etContent = convertView.findViewById(R.id.etNoteContent);

        // Reset visibility for every item
        if (btnEdit.getText().toString().equals("Save")) {
            tvTitle.setVisibility(View.GONE);
            tvContent.setVisibility(View.GONE);
            etTitle.setVisibility(View.VISIBLE);
            etContent.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            etTitle.setVisibility(View.GONE);
            etContent.setVisibility(View.GONE);
        }

        // Set the current note data to display in TextViews and EditTexts
        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());

        // Set up the delete button with a confirmation dialog
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        dbHelper.deleteNote(note.getId());  // Delete the note from the database
                        notesList.remove(position);  // Remove the note from the list
                        notifyDataSetChanged();  // Notify the adapter to refresh the list
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .create().show();
        });

        // Set up the edit button
        btnEdit.setOnClickListener(v -> {
            // Log the current visibility before any changes
            Log.d("NotesAdapter", "Before Edit - etTitle visibility: " + etTitle.getVisibility());
            Log.d("NotesAdapter", "Before Edit - tvTitle visibility: " + tvTitle.getVisibility());

            // If in view mode, switch to edit mode
            if (btnEdit.getText().toString().equals("Edit")) {
                // Switch visibility to show EditText fields
                tvTitle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                etTitle.setVisibility(View.VISIBLE);
                etContent.setVisibility(View.VISIBLE);

                // Log the visibility after making changes
                Log.d("NotesAdapter", "After Edit - etTitle visibility: " + etTitle.getVisibility());
                Log.d("NotesAdapter", "After Edit - tvTitle visibility: " + tvTitle.getVisibility());

                // Set the EditText fields with the current note data
                etTitle.setText(note.getTitle());
                etContent.setText(note.getContent());

                btnEdit.setText("Save");  // Change button text to Save
            } else {
                // When Save is clicked, save the updated note
                String updatedTitle = etTitle.getText().toString();
                String updatedContent = etContent.getText().toString();

                // Check for empty fields
                if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                    Toast.makeText(context, "Title or content cannot be empty", Toast.LENGTH_SHORT).show();
                    return;  // Prevent saving if fields are empty
                }

                // Update the note in the database
                note.setTitle(updatedTitle);
                note.setContent(updatedContent);
                dbHelper.updateNote(note);

                // Update the view to show the updated note
                tvTitle.setText(updatedTitle);
                tvContent.setText(updatedContent);

                // Log the visibility after saving
                Log.d("NotesAdapter", "After Save - etTitle visibility: " + etTitle.getVisibility());
                Log.d("NotesAdapter", "After Save - tvTitle visibility: " + tvTitle.getVisibility());

                // Hide EditText fields and show TextView fields again
                etTitle.setVisibility(View.GONE);
                etContent.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);

                btnEdit.setText("Edit");  // Change button text back to Edit
            }
        });


        return convertView;
    }
}
