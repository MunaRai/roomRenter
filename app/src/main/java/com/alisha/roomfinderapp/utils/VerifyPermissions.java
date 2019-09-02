package com.alisha.roomfinderapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.util.Log;

import androidx.core.app.ActivityCompat;

public class VerifyPermissions {
    private static final String TAG = "VerifyPermissions";
    public static final int VERIFY_PERMISSION_REQUEST = 1;

    private Context mContext;
    private Activity activity;

    public VerifyPermissions(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    /**
     * check an array of permission
     * check if user has given permissions to the app to perform certain activites like camera permissions
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions) {

        for (String permission : permissions) {

            if (!checkPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * check a single permisssion
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: Permission not granted for: " + permission);
            return false;
        }
        return true;
    }

    public void verifyPermissionsArray(String[] permissions) {
        Log.d(TAG, "verifyPermissionsArray: verifying permissions");

        ActivityCompat.requestPermissions(
                activity,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );


    }
}
