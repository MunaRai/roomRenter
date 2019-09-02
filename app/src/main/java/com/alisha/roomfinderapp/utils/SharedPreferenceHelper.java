package com.alisha.roomfinderapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alisha.roomfinderapp.models.User;


public class SharedPreferenceHelper {
    private static final String CATEGORYKEY = "categorykey";
    private static SharedPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static final String SHARE_KEY_USERTYPE = "userType";
    private static String SHARE_USER_INFO = "userinfo";
    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";
    private static final String SHARE_KEY_CONTACT = "contact";


    public static final String ISFIRSTLAUNCH = "launchType";

    public SharedPreferenceHelper(Context context) {
        preferences = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper(context);
            preferences = context.getSharedPreferences(SHARE_USER_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void saveUserInfo(User user) {
        editor.putString(SHARE_KEY_UID, user.getUser_id());
        editor.putString(SHARE_KEY_NAME, user.getUsername());
        editor.putString(SHARE_KEY_EMAIL, user.getEmail());
        editor.putString(SHARE_KEY_AVATA, user.getAvatar_img_link());
        editor.putLong(SHARE_KEY_USERTYPE, user.getUser_type());
        editor.apply();
        Log.d("user", "saveUserInfo: " + getUserInfo().getAvatar_img_link());
    }

    public void saveUserType(int userType) {
        editor.putInt(SHARE_KEY_USERTYPE, userType);
        editor.apply();
    }

    public long getUserType() {
        return preferences.getLong(SHARE_KEY_USERTYPE, 4);
    }

    public User getUserInfo() {
        String user_id = preferences.getString(SHARE_KEY_UID, "default");
        String userName = preferences.getString(SHARE_KEY_NAME, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        long userType = preferences.getLong(SHARE_KEY_USERTYPE, UserTypes.NORMAL);
        String contact = preferences.getString(SHARE_KEY_CONTACT, "");


        return new User(user_id, userName, avatar, userType, email,contact);
    }

    public String getUID() {
        return preferences.getString(SHARE_KEY_UID, "");
    }

    public void setAvatar(String avatar_img_link) {
        editor.putString(SHARE_KEY_AVATA, avatar_img_link);
        editor.apply();

    }

    public void setIsfirstlaunch(boolean type){
        editor.putBoolean(ISFIRSTLAUNCH, type);
        editor.apply();
    }

    public boolean getIsFirstLaunch(){
        return preferences.getBoolean(ISFIRSTLAUNCH, true);
    }

}
