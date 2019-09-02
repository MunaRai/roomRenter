package com.alisha.roomfinderapp.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alisha.roomfinderapp.models.UserFCM;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString(Constants.FIREBASE_TOKEN, s).apply();

        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child(FilePaths.USERS_FCM)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new UserFCM(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            token, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
        }
    }
}
