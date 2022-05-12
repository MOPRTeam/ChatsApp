package com.nhatsangthi.chatsapp.DTO;


public class ChatDTO {

    private String chatID;
    private long lastMsgTime;

    public ChatDTO() {
    }

    public ChatDTO(String chatID, long lastMsgTime) {
        this.chatID = chatID;
        this.lastMsgTime = lastMsgTime;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
