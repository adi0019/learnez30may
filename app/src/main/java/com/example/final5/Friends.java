package com.example.final5;

public class Friends {
    private String status, image, name, uid;
    private String live;  // Add live attribute

    // Constructor with live attribute
    public Friends(String bio, String image, String name, String uid, String live) {
        this.status = bio;
        this.image = image;
        this.name = name;
        this.uid = uid;
        this.live = live;
    }

    // Default constructor
    public Friends() {
    }

    // Getter and setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and setter for image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for uid
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getter and setter for live
    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }
}
