package com.nhatsangthi.chatsapp.DTO;


import com.nhatsangthi.chatsapp.Models.User;

public class SenderUserDTO {
    User user;
    long lastMessageTime;


    public SenderUserDTO(User user, long lastMessageTime) {
        this.user = user;
        this.lastMessageTime = lastMessageTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
