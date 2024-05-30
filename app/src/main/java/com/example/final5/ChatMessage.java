package com.example.final5;

/* loaded from: classes3.dex */
public class ChatMessage {
    private boolean fromUser;
    private String message;

    public ChatMessage(String message, boolean fromUser) {
        this.message = message;
        this.fromUser = fromUser;
    }

    // Getter for message
    public String getMessage() {
        return this.message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for fromUser
    public boolean isFromUser() {
        return this.fromUser;
    }

    // Setter for fromUser
    public void setFromUser(boolean fromUser) {
        this.fromUser = fromUser;
    }
}
