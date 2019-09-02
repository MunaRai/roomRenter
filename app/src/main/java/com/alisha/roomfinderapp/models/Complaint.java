package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Date;

public class Complaint implements Parcelable {

    public String id;
    public String post_id;
    public String post_name;
    public String user_id;
    public String username;
    public String user_pic;
    public String description;
    public String date_created;

    public Complaint() {
    }

    protected Complaint(Parcel in) {
        id = in.readString();
        post_id = in.readString();
        post_name = in.readString();
        user_id = in.readString();
        username = in.readString();
        user_pic = in.readString();
        description = in.readString();
        date_created = in.readString();
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_name() {
        return post_name;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

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

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public Complaint(String id, String post_id, String post_name, String user_id, String username, String user_pic, String description) {
        this.id = id;
        this.post_id = post_id;
        this.post_name = post_name;
        this.user_id = user_id;
        this.username = username;
        this.user_pic = user_pic;
        this.description = description;
        this.date_created = DateTimeUtils.formatDate(new Date());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(post_id);
        parcel.writeString(post_name);
        parcel.writeString(user_id);
        parcel.writeString(username);
        parcel.writeString(user_pic);
        parcel.writeString(description);
        parcel.writeString(date_created);
    }
}
