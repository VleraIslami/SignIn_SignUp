package com.example.signin_signup;

import android.content.Context;
import android.content.Intent;
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

import androidx.core.content.ContextCompat;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {

    private Context context;
    private List<Note> notesList;
    private SQLiteHelper dbHelper;
    private AlertDialog alertDialog;  // Declare dialog reference

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

        // Initialize all views
        TextView tvTitle = convertView.findViewById(R.id.tvNoteTitle);
        TextView tvContent = convertView.findViewById(R.id.tvNoteContent);
        EditText etTitle = convertView.findViewById(R.id.etNoteTitle);
        EditText etContent = convertView.findViewById(R.id.etNoteContent);
        Button btnDelete = convertView.findViewById(R.id.btnDeleteNote);
        Button btnEdit = convertView.findViewById(R.id.btnEditNote);
        Button btnPreview = convertView.findViewById(R.id.btnPreviewNote);

        // Initialize the status buttons for DONE and ON PROGRESS
        Button btnDone = convertView.findViewById(R.id.btnDone);
        Button btnOnProgress = convertView.findViewById(R.id.btnOnProgress);

        // Display title and content in TextViews
        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());

        // Set EditText fields to be initially hidden
        etTitle.setVisibility(View.GONE);
        etContent.setVisibility(View.GONE);



        // Set the status buttons based on the current status
        if ("DONE".equals(note.getStatus())) {
            btnOnProgress.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        } else {
            btnDone.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            btnOnProgress.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
        }


        // Done button logic
        btnDone.setOnClickListener(v -> {
            // Change the background color of "DONE" button to green
            btnDone.setBackgroundColor(context.getResources().getColor(R.color.green));

            // Change the background color of "ON PROGRESS" button to grey
            btnOnProgress.setBackgroundColor(context.getResources().getColor(R.color.gray));

            // Update the note's status to "DONE"
            note.setStatus("DONE");
            dbHelper.updateNoteStatus(note); // Update the note status in the database

            notifyDataSetChanged(); // Refresh the list
        });

// On Progress button logic
        btnOnProgress.setOnClickListener(v -> {
            // Change the background color of "ON PROGRESS" button to orange (or your desired color)
            btnOnProgress.setBackgroundColor(context.getResources().getColor(R.color.orange));

            // Change the background color of "DONE" button to grey
            btnDone.setBackgroundColor(context.getResources().getColor(R.color.gray));

            // Update the note's status to "ON PROGRESS"
            note.setStatus("ON PROGRESS");
            dbHelper.updateNoteStatus(note); // Update the note status in the database

            notifyDataSetChanged(); // Refresh the list
        });


        // Delete button logic
        btnDelete.setOnClickListener(v -> {
            alertDialog = new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        dbHelper.deleteNote(note.getId());
                        notesList.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .create();
            alertDialog.show();
        });

        // Edit button logic
        btnEdit.setOnClickListener(v -> {
            if (btnEdit.getText().toString().equals("Edit")) {

                tvTitle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                etTitle.setVisibility(View.VISIBLE);
                etContent.setVisibility(View.VISIBLE);

                etTitle.setText(note.getTitle());
                etContent.setText(note.getContent());
                btnEdit.setText("Save");
            } else {
                String updatedTitle = etTitle.getText().toString();
                String updatedContent = etContent.getText().toString();

                if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                    Toast.makeText(context, "Title or content cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                note.setTitle(updatedTitle);
                note.setContent(updatedContent);
                dbHelper.updateNote(note);

                tvTitle.setText(updatedTitle);
                tvContent.setText(updatedContent);

                etTitle.setVisibility(View.GONE);
                etContent.setVisibility(View.GONE);
                tvTitle.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);

                btnEdit.setText("Edit");
            }
        });

        // Preview button logic
        btnPreview.setOnClickListener(v -> {
            Intent previewIntent = new Intent(context, AddOrEditNoteActivity.class);
            previewIntent.putExtra("noteId", note.getId());
            context.startActivity(previewIntent);
        });

        return convertView;
    }

}
