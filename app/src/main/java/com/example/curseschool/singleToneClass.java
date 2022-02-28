package com.example.curseschool;

public class singleToneClass {
    String s;
    private static final singleToneClass ourInstance = new singleToneClass();
    public static singleToneClass getInstance() {
        return ourInstance;
    }
    private singleToneClass() {
    }
    public void setData(String s) {
        this.s = s;
    }
    public String getData() {
        return s;
    }
}
