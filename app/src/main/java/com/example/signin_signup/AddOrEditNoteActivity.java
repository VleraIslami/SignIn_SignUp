package com.example.signin_signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddOrEditNoteActivity extends AppCompatActivity {

    private EditText edtTitle, edtContent;
    private Button btnSave, btnBack, btnManageNotes;
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
        btnManageNotes = findViewById(R.id.btnManageNotes);  // Initialize Manage Notes button
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
            // Navigate back to the previous activity
            finish(); // Close the current activity and return to the previous one in the stack
        });

        // Set onClickListener for Manage Notes button
        btnManageNotes.setOnClickListener(v -> {
            // Log to verify if this part is being reached
            Log.d("AddOrEditNoteActivity", "Manage Notes button clicked");

            // Create an Intent to navigate to ManageNotesActivity
            Intent manageNotesIntent = new Intent(AddOrEditNoteActivity.this, ManageNotesActivity.class);

            // Optional: Use flags to ensure the activity starts cleanly
            manageNotesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Start the ManageNotesActivity
            startActivity(manageNotesIntent);
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
