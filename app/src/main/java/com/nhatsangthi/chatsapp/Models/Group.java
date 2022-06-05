package com.nhatsangthi.chatsapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Group implements Parcelable {
    private String id, adminId, adminName, createdAt, image, name;
    private List<GroupMember> members;
    private GroupLastMessage groupLastMessage;
    private boolean isAdmin;

    public Group() {
    }

    public Group(String id, String adminId, String adminName, String createdAt, String image, String name, boolean isAdmin) {
        this.id = id;
        this.adminId = adminId;
        this.adminName = adminName;
        this.createdAt = createdAt;
        this.image = image;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    protected Group(Parcel in) {
        id = in.readString();
        adminId = in.readString();
        adminName = in.readString();
        createdAt = in.readString();
        image = in.readString();
        name = in.readString();
        isAdmin = in.readByte() != 0;
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    public GroupLastMessage getGroupLastMessage() {
        return groupLastMessage;
    }

    public void setGroupLastMessage(GroupLastMessage groupLastMessage) {
        this.groupLastMessage = groupLastMessage;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        isAdmin = isAdmin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(adminId);
        parcel.writeString(adminName);
        parcel.writeString(createdAt);
        parcel.writeString(image);
        parcel.writeString(name);
        parcel.writeByte((byte) (isAdmin ? 1 : 0));
    }
}
