package com.example.curseschool.Objects;

public class ClassRoom {

    private int id;
    private int classRoom;
    private boolean archival;

    public ClassRoom(int id, int classRoom, boolean archival) {
        this.id = id;
        this.classRoom = classRoom;
        this.archival = archival;
    }

    public int getId() {
        return id;
    }

    public int getClassRoom() {
        return classRoom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClassRoom(int classRoom) {
        this.classRoom = classRoom;
    }

    public boolean isArchival() {
        return archival;
    }

    public void setArchival(boolean archival) {
        this.archival = archival;
    }
}
