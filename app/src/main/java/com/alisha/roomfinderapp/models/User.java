package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    public String user_id;
    public String username;
    public String avatar_img_link;
    public long user_type;
    public String email;
    public String contact;

    public User() {
    }

    public User(Parcel in) {
        user_id = in.readString();
        username = in.readString();
        avatar_img_link = in.readString();
        user_type = in.readLong();
        email = in.readString();
        contact = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar_img_link() {
        return avatar_img_link;
    }

    public void setAvatar_img_link(String avatar_img_link) {
        this.avatar_img_link = avatar_img_link;
    }

    public long getUser_type() {
        return user_type;
    }

    public void setUser_type(long user_type) {
        this.user_type = user_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public User(String user_id, String username, String avatar_img_link, long user_type, String email, String contact) {
        this.user_id = user_id;
        this.username = username;
        this.avatar_img_link = avatar_img_link;
        this.user_type = user_type;
        this.email = email;
        this.contact = contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user_id);
        parcel.writeString(username);
        parcel.writeString(avatar_img_link);
        parcel.writeLong(user_type);
        parcel.writeString(email);
        parcel.writeString(contact);
    }
}
