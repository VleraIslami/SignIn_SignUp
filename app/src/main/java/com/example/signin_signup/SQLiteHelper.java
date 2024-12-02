package com.example.signin_signup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_data.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";  // Table name for users
    public static final String TABLE_NOTES = "notes";  // Table name for notes
    public static final String COLUMN_USER_ID = "user_id";  // Column for user ID
    public static final String COLUMN_USER_NAME = "name";  // Column for user name
    public static final String COLUMN_USER_EMAIL = "email";  // Column for user email
    public static final String COLUMN_USER_PASSWORD = "password";  // Column for user password

    public static final String COLUMN_NOTE_ID = "note_id";  // Column for note ID
    public static final String COLUMN_NOTE_TITLE = "title";  // Column for note title
    public static final String COLUMN_NOTE_CONTENT = "content";  // Column for note content

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

        // Create 'notes' table
        String createNotesTableQuery = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_CONTENT + " TEXT)";
        db.execSQL(createNotesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Add the missing methods

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean isRegistered = cursor.getCount() > 0;
        cursor.close();
        return isRegistered;
    }

    public boolean isUserValid(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM notes WHERE note_id = ?";
        db.execSQL(query, new Object[]{noteId});
    }

    public boolean insertUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM notes"; // Replace with your actual table and columns if needed
        return db.rawQuery(query, null); // Return the result as a Cursor
    }

    public void addSampleNotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", "Sample Note");
        values.put("content", "This is a sample note.");
        db.insert("notes", null, values);
        db.close();
    }

    // Add any other helper methods if needed
}
