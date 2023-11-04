package com.example.roommate.Memo;

public class Memo {
    private String email;
    private String name;
    private String roomCode;
    private String content;
    private String date;
    private String time;

    public Memo() {}

    public Memo(String email, String name, String roomCode, String content, String date, String time) {
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.content = content;
        this.date = date;
        this.time = time;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
