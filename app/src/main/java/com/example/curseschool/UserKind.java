package com.example.curseschool;

public enum UserKind {
    admin, teacher, student;


    public String getName(UserKind userKind) {
        if (userKind.equals(UserKind.admin)) {
            return String.valueOf(R.string.admin);
        } else if (userKind.equals(UserKind.teacher)) {
            return String.valueOf(R.string.teacher);
        } else if (userKind.equals(UserKind.student)) {
            return String.valueOf(R.string.student);
        } else {
            return "";
        }
    }
}
