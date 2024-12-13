package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ManageNotesActivity extends AppCompatActivity {

    private ListView notesListView;
    private NotesAdapter notesAdapter;
    private SQLiteHelper dbHelper;
    private List<Note> notesList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notes);

        dbHelper = new SQLiteHelper(this);
        notesListView = findViewById(R.id.notesListView);
        notesList = new ArrayList<>();

        // Load all notes
        loadNotes();

        notesAdapter = new NotesAdapter(this, notesList);
        notesListView.setAdapter(notesAdapter);

        // Set up item click listener for editing a note
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            Note selectedNote = notesList.get(position);
            Intent intent = new Intent(ManageNotesActivity.this, AddOrEditNoteActivity.class);
            intent.putExtra("noteId", selectedNote.getId());
            startActivityForResult(intent, 1);  // Use startActivityForResult to capture result
        });

        // Set up button to add new note

    }

    private void loadNotes() {
        notesList.clear();
        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllNotes();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") Note note = new Note(
                            cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE_ID)),
                            cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE_TITLE)),
                            cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE_CONTENT))
                    );
                    notesList.add(note);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();  // Make sure the cursor is closed properly
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the note was added or edited, reload the notes list
        if (resultCode == RESULT_OK) {
            loadNotes();  // Reload the notes from the database
            notesAdapter.notifyDataSetChanged();  // Refresh the ListView
        }
    }
}
