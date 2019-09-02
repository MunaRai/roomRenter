package com.alisha.roomfinderapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserFCM{
    public String user_id;
    public String token;
    public String username;

    public UserFCM() {
    }

    public UserFCM(String user_id, String token, String username) {
        this.user_id = user_id;
        this.token = token;
        this.username = username;
    }
}
