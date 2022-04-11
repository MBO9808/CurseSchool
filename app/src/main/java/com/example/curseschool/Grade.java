package com.example.curseschool;

public class Grade {
    int id;
    int studentId;
    int courseId;
    int gradeNameId;
    float grade;

    public Grade(int id, int studentId, int courseId, int gradeNameId, float grade) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.gradeNameId = gradeNameId;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getGradeNameId() {
        return gradeNameId;
    }

    public void setGradeNameId(int gradeNameId) {
        this.gradeNameId = gradeNameId;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
