package com.nhatsangthi.chatsapp.Models;

public class GroupMessage {
    String type, message, senderId, name;
    Long timestamp;

    public GroupMessage() {
    }

    public GroupMessage(String type, String message, String senderId, String name, Long timestamp) {
        this.type = type;
        this.message = message;
        this.senderId = senderId;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
