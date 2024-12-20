package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageDataActivity extends AppCompatActivity {

    private ListView lvNotes;
   // private Button btnAddNote;
    private SQLiteHelper dbHelper;
    private ArrayList<Note> notesList;
    private NotesAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);

        lvNotes = findViewById(R.id.lvNotes);
        //btnAddNote = findViewById(R.id.btnAddNote);
        dbHelper = new SQLiteHelper(this);

        // Add a sample note if database is empty (for testing)
        dbHelper.addSampleNotes();

        // Load notes from the database
        notesList = new ArrayList<>();
        loadNotes();

        // Set up the adapter for the ListView
        adapter = new NotesAdapter(this, notesList);
        lvNotes.setAdapter(adapter);

        // Add new note
        /*btnAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(ManageDataActivity.this, AddOrEditNoteActivity.class);
            startActivity(intent);
        });*/
    }


    // Load notes into the list
    @SuppressLint("Range")
    private void loadNotes() {
        notesList.clear();
        Cursor cursor = dbHelper.getAllNotes();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("note_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));

                notesList.add(new Note(id, title, content, cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_NOTE_STATUS))));
            }
            cursor.close();
        }
        if (notesList.isEmpty()) {
            Toast.makeText(this, "No notes available", Toast.LENGTH_SHORT).show();
        }
    }

}
