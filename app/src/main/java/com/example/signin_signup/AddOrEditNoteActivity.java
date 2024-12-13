package com.example.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddOrEditNoteActivity extends AppCompatActivity {

    private EditText edtTitle, edtContent;
    private Button btnSave, btnBack;
    private SQLiteHelper dbHelper;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new SQLiteHelper(this);

        // Check if we're editing an existing note
        Intent intent = getIntent();
        if (intent.hasExtra("noteId")) {
            // Editing existing note
            int noteId = intent.getIntExtra("noteId", -1);
            note = dbHelper.getNoteById(noteId);
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
        } else {
            // New note
            note = new Note(-1, "", "");
        }

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();

            if (note.getId() == -1) {
                // Adding new note
                note = new Note(0, title, content);
                dbHelper.addNote(note);

                // Show confirmation alert after saving the note
                showAlert("Note saved successfully!");
            } else {
                // Updating existing note
                note.setTitle(title);
                note.setContent(content);
                dbHelper.updateNote(note);
                showAlert("Note updated successfully!");
            }
        });

        // Set onClickListener for Back button
        btnBack.setOnClickListener(v -> {
            // Navigate back to ActivityHome
            Intent homeIntent = new Intent(AddOrEditNoteActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();  // Close current activity so the user can't go back here
        });
    }

    // Method to show alert
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    // Do nothing here, just close the dialog and keep the activity open
                });
        builder.create().show();
    }
}
