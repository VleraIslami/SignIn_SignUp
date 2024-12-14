package com.example.signin_signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        // Set initial view mode (TextViews)
        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());

        // Set up the delete button
        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteNote(note.getId());  // Delete the note from the database
            notesList.remove(position);  // Remove the note from the list
            notifyDataSetChanged();  // Notify the adapter to refresh the list
        });

        // Set up the edit button
        View finalConvertView = convertView;
        btnEdit.setOnClickListener(v -> {
            // If in view mode, switch to edit mode
            if (btnEdit.getText().toString().equals("Edit")) {
                // Convert TextViews to EditTexts
                tvTitle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);

                // Create EditText views and set the initial values
                EditText etTitle = new EditText(context);
                etTitle.setText(note.getTitle());
                ViewGroup titleContainer = finalConvertView.findViewById(R.id.note_title_container);
                titleContainer.removeAllViews();  // Remove any existing views
                titleContainer.addView(etTitle);  // Add EditText to the title container

                EditText etContent = new EditText(context);
                etContent.setText(note.getContent());
                ViewGroup contentContainer = finalConvertView.findViewById(R.id.note_content_container);
                contentContainer.removeAllViews();  // Remove any existing views
                contentContainer.addView(etContent);  // Add EditText to the content container

                btnEdit.setText("Save");  // Change button text to Save

                // Handle saving the updated note when the button is clicked again
                btnEdit.setOnClickListener(v1 -> {
                    String updatedTitle = etTitle.getText().toString();
                    String updatedContent = etContent.getText().toString();

                    // Update the note in the database
                    note.setTitle(updatedTitle);
                    note.setContent(updatedContent);
                    dbHelper.updateNote(note);

                    // Update the view to show the updated note
                    tvTitle.setText(updatedTitle);
                    tvContent.setText(updatedContent);

                    // Hide EditText fields and show TextView fields again
                    etTitle.setVisibility(View.GONE);
                    etContent.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    tvContent.setVisibility(View.VISIBLE);

                    btnEdit.setText("Edit");  // Change button text back to Edit
                });
            }
        });

        return convertView;
    }
}
