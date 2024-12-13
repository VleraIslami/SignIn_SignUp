package com.example.signin_signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());

        // Set up the delete button
        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteNote(note.getId());  // Delete the note from the database
            notesList.remove(position);  // Remove the note from the list
            notifyDataSetChanged();  // Notify the adapter to refresh the list
        });

        return convertView;
    }
}
