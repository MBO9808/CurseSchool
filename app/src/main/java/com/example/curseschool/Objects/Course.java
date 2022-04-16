package com.example.curseschool.Objects;

import java.util.Date;
import java.util.ArrayList;

public class Course {
    int id;
    String courseName;
    int teacherId;
    int languageId;
    int courseAdvancementId;
    int maxStudents;
    Date startDate;
    Date endDate;
    int classRoomId;
    Date paymentDate;
    Float payment;
    Date creationDate;
    boolean archival;
    Date signDate;
    ArrayList<Integer> studentsList;
    ArrayList<CourseDate> courseDatesList;

    public Course(int id, String courseName, int teacherId, int languageId, int courseAdvancementId, int maxStudents, Date startDate, Date endDate, int classRoomId, Date paymentDate, Float payment, Date creationDate, boolean archival, Date signDate, ArrayList<Integer> studentsList, ArrayList<CourseDate> courseDatesList) {
        this.id = id;
        this.courseName = courseName;
        this.teacherId = teacherId;
        this.languageId = languageId;
        this.courseAdvancementId = courseAdvancementId;
        this.maxStudents = maxStudents;
        this.startDate = startDate;
        this.endDate = endDate;
        this.classRoomId = classRoomId;
        this.paymentDate = paymentDate;
        this.payment = payment;
        this.creationDate = creationDate;
        this.archival = archival;
        this.signDate = signDate;
        this.studentsList = studentsList;
        this.courseDatesList = courseDatesList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getCourseAdvancementId() {
        return courseAdvancementId;
    }

    public void setCourseAdvancementId(int courseAdvancementId) {
        this.courseAdvancementId = courseAdvancementId;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isArchival() {
        return archival;
    }

    public void setArchival(boolean archival) {
        this.archival = archival;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public ArrayList<Integer> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<Integer> studentsList) {
        this.studentsList = studentsList;
    }

    public ArrayList<CourseDate> getCourseDatesList() {
        return courseDatesList;
    }

    public void setCourseDatesList(ArrayList<CourseDate> courseDatesList) {
        this.courseDatesList = courseDatesList;
    }
}
