package com.example.roommate;

public class User implements Comparable<User> {
    private String email;
    private String roomCode;
    private String startDate;
    private String name;
    private String phone;

    public User() {}

    public User(String email, String roomCode, String startDate, String name, String phone) {
        this.email = email;
        this.roomCode = roomCode;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int compareTo(User user) {
        return this.name.compareTo(user.name);
    }
}
