package com.example.roommate.dday;

public class Dday implements Comparable<Dday> {
    private String email;
    private String name;
    private String roomCode;
    private String title;
    private String date;

    public Dday() {}

    public Dday(String email, String name, String roomCode, String title, String date) {
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.title = title;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Dday dday) {
        return this.date.compareTo(dday.date);
    }
}
