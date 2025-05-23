package com.example.signin_signup;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 2;  // Increased version for upgrade

    public static final String TABLE_USERS = "users";  // Table name for users
    public static final String TABLE_NOTES = "notes";  // Table name for notes
    public static final String COLUMN_USER_ID = "user_id";  // Column for user ID
    public static final String COLUMN_USER_NAME = "name";  // Column for user name
    public static final String COLUMN_USER_EMAIL = "email";  // Column for user email
    public static final String COLUMN_USER_PASSWORD = "password";  // Column for user password

    public static final String COLUMN_NOTE_ID = "note_id";  // Column for note ID
    public static final String COLUMN_NOTE_TITLE = "title";  // Column for note title
    public static final String COLUMN_NOTE_CONTENT = "content";  // Column for note content
    public static final String COLUMN_NOTE_STATUS = "status";  // Column for note status

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create 'users' table
        String createUserTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createUserTableQuery);

        // Create 'notes' table with the 'status' column
        String createNotesTableQuery = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_CONTENT + " TEXT, " +
                COLUMN_NOTE_STATUS + " TEXT DEFAULT 'ON PROGRESS')";
        db.execSQL(createNotesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Upgrade the 'notes' table by adding the 'status' column
            db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_NOTE_STATUS + " TEXT DEFAULT 'ON PROGRESS'");
        }
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        return isRegistered;
    }

    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    // Delete a note
    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + COLUMN_NOTE_ID + " = ?";
        db.execSQL(query, new Object[]{noteId});
        db.close();
    }

    // Insert a new user (for registration purposes)
    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Get all notes
    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES;
        return db.rawQuery(query, null);
    }

    // Get a single note by its ID
    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_NOTE_ID, COLUMN_NOTE_TITLE, COLUMN_NOTE_CONTENT, COLUMN_NOTE_STATUS},
                COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            @SuppressLint("Range") Note note = new Note(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_STATUS))  // Add 'status'
            );
            cursor.close();
            db.close();
            return note;
        } else {
            db.close();
            return null;
        }
    }

    // Add a new note to the database
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_STATUS, note.getStatus());  // Add 'status'

        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    // Update an existing note
    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_STATUS, note.getStatus());  // Update 'status'

        db.update(TABLE_NOTES, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    // Update the status of a note
    public void updateNoteStatus(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_STATUS, note.getStatus()); // Update the status column

        db.update(TABLE_NOTES, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    // Add sample notes (for testing purposes)
    public void addSampleNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, "Sample Note");
        values.put(COLUMN_NOTE_CONTENT, "This is a sample note.");
        values.put(COLUMN_NOTE_STATUS, "ON PROGRESS");  // Add default status
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }
}
