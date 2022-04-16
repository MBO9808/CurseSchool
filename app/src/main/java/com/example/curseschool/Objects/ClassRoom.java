package com.example.curseschool.Objects;

public class ClassRoom {

    private int id;
    private int classRoom;

    public ClassRoom(int id, int classRoom) {
        this.id = id;
        this.classRoom = classRoom;
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
}
