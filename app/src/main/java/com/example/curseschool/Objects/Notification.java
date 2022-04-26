package com.example.curseschool.Objects;


import java.util.Date;

public class Notification {
    int id;
    int userId;
    String description;
    Date creationDate;
    boolean archival;

    public Notification(int id, int userId, String description, Date creationDate, boolean archival) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.creationDate = creationDate;
        this.archival = archival;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
