package com.examples.libraryJSP.beans;

import java.io.InputStream;

/**
 * Created by Аяз on 21.06.2016.
 */
public class Book {
    private String id; // ISBN
    private String author;
    private String title;
    private int year;
    private boolean active = false;
    private int category_id;
    private byte[] about;

// ------ Setters ------
    public void setId(String id) {
        this.id = id;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public void setAbout(String about) {
        this.about = about.getBytes();
    }

    // ------ Getters ------
    public String getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public boolean isActive() {
        return active;
    }
    public int getCategory_id() {
        return category_id;
    }
    public byte[] getAbout() {
        return about;
    }
}
