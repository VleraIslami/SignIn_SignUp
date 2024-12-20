package com.example.signin_signup;

public class Note {
    private int id;
    private String title;
    private String content;
    private String status;  // New field to store the status

    // Constructor with the status field added
    public Note(int id, String title, String content, String string) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = "ON PROGRESS";   // Initialize the status
    }

    // Getter and setter methods for id, title, and content
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and setter methods for the status field
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", title='" + title + '\'' + ", content='" + content + '\'' + ", status='" + status + '\'' + '}';
    }
}
