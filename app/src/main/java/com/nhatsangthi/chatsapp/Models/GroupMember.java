package com.nhatsangthi.chatsapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupMember implements Parcelable {
    private String id, role;

    public GroupMember() {
    }

    public GroupMember(String id, String role) {
        this.id = id;
        this.role = role;
    }

    protected GroupMember(Parcel in) {
        id = in.readString();
        role = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(role);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupMember> CREATOR = new Creator<GroupMember>() {
        @Override
        public GroupMember createFromParcel(Parcel in) {
            return new GroupMember(in);
        }

        @Override
        public GroupMember[] newArray(int size) {
            return new GroupMember[size];
        }
    };

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
