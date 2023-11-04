package com.example.roommate.Album;

public class Photo {
    private String email;
    private String name;
    private String roomCode;
    private String imageUrl;
    private String comment;

    public Photo() {}

    public Photo(String email, String name, String roomCode, String imageUrl, String comment) {
        this.email = email;
        this.name = name;
        this.roomCode = roomCode;
        this.imageUrl = imageUrl;
        this.comment = comment;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
