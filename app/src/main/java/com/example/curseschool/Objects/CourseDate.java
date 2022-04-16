package com.example.curseschool.Objects;

import java.sql.Time;
import java.util.Date;

public class CourseDate {
    int id;
    int courseId;
    Date courseDate;
    Time courseTimeStart;
    Time courseTimeEnd;

    public CourseDate(int id, int courseId, Date courseDate, Time courseTimeStart, Time courseTimeEnd) {
        this.id = id;
        this.courseId = courseId;
        this.courseDate = courseDate;
        this.courseTimeStart = courseTimeStart;
        this.courseTimeEnd = courseTimeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public Time getCourseTimeStart() {
        return courseTimeStart;
    }

    public void setCourseTimeStart(Time courseTimeStart) {
        this.courseTimeStart = courseTimeStart;
    }

    public Time getCourseTimeEnd() {
        return courseTimeEnd;
    }

    public void setCourseTimeEnd(Time courseTimeEnd) {
        this.courseTimeEnd = courseTimeEnd;
    }
}
