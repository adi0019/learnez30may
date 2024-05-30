package com.example.final5;

import java.util.List;

/* loaded from: classes3.dex */
public class chatingmsg {
    private String message;
    private String profileImageUrl;
    private String sender;
    private long timestamp;

    public chatingmsg() {
    }

    public chatingmsg(String message, String sender, long timestamp) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public chatingmsg(List<chatingmsg> messages) {
    }

    public String getMessage() {
        return this.message;
    }

    public String getSender() {
        return this.sender;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}