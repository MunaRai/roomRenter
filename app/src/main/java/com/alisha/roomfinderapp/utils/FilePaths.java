package com.alisha.roomfinderapp.utils;

import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.alisha.roomfinderapp.R;


public class FilePaths {
    public static final String USER = "users";
    public static final String HOTEL = "hotels";
    public static final String CATEGORY = "categories";
    public static final String ROOM = "rooms";

    public static final String BOOKMARK = "userBookmarks";
    public static final String DEFAULT_IMAGE = "https://www.elegantthemes.com/blog/wp-content/uploads/2016/04/category-plugins-header.png";
    public static final String BOOKNOW = "hotelBookings";
    public static final String IMAGE_URI = "drawable://" + R.drawable.main;
    public static final String USER_COMMENTS = "user_comments";
    public static final String USER_REVIEWS = "user_reviews";
    public static final String USER_COMPLAINTS = "user_complaints";
    public static final String NOTIFICATIONS = "notifications";
    public static final String USERS_FCM = "users_fcm";

    @NonNull
    public static String getSaveDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Rentals/";
    }

    @NonNull
    public static String getNameFromUrl(final String url) {
        return Uri.parse(url).getLastPathSegment();
    }
}
