package com.nhatsangthi.chatsapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupLastMessage implements Parcelable {
    private String lastMsg, senderUid;
    private Long lastMsgTime;

    public GroupLastMessage() {
    }

    public GroupLastMessage(String lastMsg, String senderUid, Long lastMsgTime) {
        this.lastMsg = lastMsg;
        this.senderUid = senderUid;
        this.lastMsgTime = lastMsgTime;
    }

    protected GroupLastMessage(Parcel in) {
        lastMsg = in.readString();
        senderUid = in.readString();
        if (in.readByte() == 0) {
            lastMsgTime = null;
        } else {
            lastMsgTime = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lastMsg);
        dest.writeString(senderUid);
        if (lastMsgTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(lastMsgTime);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupLastMessage> CREATOR = new Creator<GroupLastMessage>() {
        @Override
        public GroupLastMessage createFromParcel(Parcel in) {
            return new GroupLastMessage(in);
        }

        @Override
        public GroupLastMessage[] newArray(int size) {
            return new GroupLastMessage[size];
        }
    };

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public Long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
