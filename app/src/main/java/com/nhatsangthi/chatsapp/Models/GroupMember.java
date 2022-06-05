package com.nhatsangthi.chatsapp.Models;

public class GroupMember {
    private String id, role;

    public GroupMember() {
    }

    public GroupMember(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
