package com.example.roommate.Calendar;

public class Schedule implements Comparable<Schedule> {
    private String email;
    private String name;
    private String roomCode;
    private String title;
    private String date;
    private String time;
    private String memo;

    public Schedule() {}

    public Schedule(String email, String name, String roomCode, String title, String date, String time, String memo) {
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.title = title;
        this.date = date;
        this.time = time;
        this.memo = memo;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public int compareTo(Schedule schedule) {
        return this.time.compareTo(schedule.time);
    }
}
