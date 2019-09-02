package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.alisha.roomfinderapp.utils.TextDataUtils;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Date;


public class Comment implements Parcelable {
    public String comment_id;
    public String comment_desc;
    public String date_created;

    public String user_id;
    public String username;

    public Comment() {
    }

    public Comment(String comment_id, String comment_desc, String user_id, String username) {
        this.comment_id = comment_id;
        this.comment_desc = comment_desc;
        this.user_id = user_id;
        this.username = username;
        this.date_created = DateTimeUtils.formatDate(new Date());;
    }

    protected Comment(Parcel in) {
        comment_id = in.readString();
        comment_desc = in.readString();
        date_created = in.readString();
        user_id = in.readString();
        username = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getComment_id() {

        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_desc() {
        return comment_desc;
    }

    public void setComment_desc(String comment_desc) {
        this.comment_desc = comment_desc;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment_id);
        dest.writeString(comment_desc);
        dest.writeString(date_created);
        dest.writeString(user_id);
        dest.writeString(username);
    }
}
