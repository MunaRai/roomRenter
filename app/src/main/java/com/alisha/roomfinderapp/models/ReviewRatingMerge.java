package com.alisha.roomfinderapp.models;


import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Date;

public class ReviewRatingMerge {
    public String comment_id;
    public String post_id;
    public String comment_desc;
    public String date_created;

    public String user_id;
    public String username;
    public String avatar_img_link;
    public float userRating;

    public ReviewRatingMerge() {
    }

    public ReviewRatingMerge(String comment_id,
                             String post_id,
                             String comment_desc,
                             String user_id,
                             String username, String avatar_img_link, float userRating) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.comment_desc = comment_desc;
        this.date_created = DateTimeUtils.formatDate(new Date());
        this.user_id = user_id;
        this.username = username;
        this.avatar_img_link = avatar_img_link;
        this.userRating = userRating;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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

    public String getAvatar_img_link() {
        return avatar_img_link;
    }

    public void setAvatar_img_link(String avatar_img_link) {
        this.avatar_img_link = avatar_img_link;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }
}
