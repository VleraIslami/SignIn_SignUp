package com.example.signin_signup;

public class Note {
    private int id;
    private String title;
    private String content;

    // Constructor and Getters/Setters
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

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

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", title='" + title + '\'' + ", content='" + content + '\'' + '}';
    }
}
