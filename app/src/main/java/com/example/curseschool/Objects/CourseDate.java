package com.example.curseschool.Objects;

import java.sql.Time;
import java.util.Date;

public class CourseDate {
    int id;
    int courseId;
    Date courseDate;
    Time courseTimeStart;
    Time courseTimeEnd;
    int classRoomId;

    public int getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }

    public CourseDate(int id, int courseId, Date courseDate, Time courseTimeStart, Time courseTimeEnd, int classRoomId) {
        this.id = id;
        this.courseId = courseId;
        this.courseDate = courseDate;
        this.courseTimeStart = courseTimeStart;
        this.courseTimeEnd = courseTimeEnd;
        this.classRoomId = classRoomId;
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
