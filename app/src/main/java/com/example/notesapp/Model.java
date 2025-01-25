package com.example.notesapp;

public class Model {
    private int id;
    private String title;
    private String text;

    public Model(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Model() {
    }

    @Override
    public String toString() {
        return title;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
