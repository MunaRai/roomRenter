package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {
    public String keyId;
    public String userId;
    public String message;
    public String senderId;
    public String date_added;

    public Notification() {
    }

    protected Notification(Parcel in) {
        keyId = in.readString();
        userId = in.readString();
        message = in.readString();
        senderId = in.readString();
        date_added = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public Notification(String keyId, String userId, String message, String senderId, String date_added) {
        this.keyId = keyId;
        this.userId = userId;
        this.message = message;
        this.senderId = senderId;
        this.date_added = date_added;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(keyId);
        parcel.writeString(userId);
        parcel.writeString(message);
        parcel.writeString(senderId);
        parcel.writeString(date_added);
    }
}
