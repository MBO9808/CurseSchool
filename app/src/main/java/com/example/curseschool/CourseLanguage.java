package com.example.curseschool;

public class CourseLanguage {

    private int id;
    private String language;

    public CourseLanguage(int id, String language) {
        this.id = id;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
