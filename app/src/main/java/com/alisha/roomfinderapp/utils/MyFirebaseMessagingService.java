package com.alisha.roomfinderapp.utils;

/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.home.HomeActivity;
import com.alisha.roomfinderapp.models.UserFCM;
import com.alisha.roomfinderapp.notifications.NotificationActivity;
import com.alisha.roomfinderapp.rooms.normal.RoomDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final int BROADCAST_NOTIFICATION_ID = 1;

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";
    private static final String ADMIN_CHANNEL_ID ="admin_channel";
    private static final String POST_ID = "post_id";
    private NotificationManager notificationManager;


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child(FilePaths.USERS_FCM)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())

                    .setValue(new UserFCM(FirebaseAuth.getInstance().getCurrentUser().getUid(), token,
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
        }
    }
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        String notificationBody = "";
        String notificationTitle = "";
        String post_id = "";
        String user_id = "";
        String date_created  = "";


        String dataType = remoteMessage.getData().get(getString(    R.string.data_type));
        if (dataType.equals(getString(R.string.direct_message))) {
            Log.d(TAG, "onMessageReceived: new incoming message.");
            post_id = remoteMessage.getData().get(getString(R.string.data_post_id));
            user_id = remoteMessage.getData().get(getString(R.string.data_user_id));
            date_created = remoteMessage.getData().get(getString(R.string.data_date_created));
            notificationTitle = remoteMessage.getData().get(getString(R.string.data_title));
            notificationBody = remoteMessage.getData().get(getString(R.string.data_body));
//            String message = remoteMessage.getData().get(getString(R.string.data_message));
//            String messageId = remoteMessage.getData().get(getString(R.string.data_message_id));
//            sendMessageNotification(title, message, messageId);
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user_id)) {
                sendMessageNotification(notificationTitle, notificationBody, post_id, user_id, date_created, "123");
            }
        }else if(dataType.equals("room_message")){
            post_id = remoteMessage.getData().get(getString(R.string.data_post_id));
            date_created = remoteMessage.getData().get(getString(R.string.data_date_created));
            notificationTitle = remoteMessage.getData().get(getString(R.string.data_title));
            notificationBody = remoteMessage.getData().get(getString(R.string.data_body));
            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user_id)) {
                sendRoomNotification(notificationTitle, notificationBody, post_id, date_created, "123");
            }
        }
    }

    private void sendRoomNotification(String notificationTitle, String notificationBody, String post_id, String date_created, String s) {
        //You should use an actual ID instead
        int notificationId = new Random().nextInt(60000);



        Intent likeIntent = new Intent(this, HomeActivity.class);

        likeIntent.putExtra(getString(R.string.data_post_id),post_id);
        final PendingIntent likePendingIntent = PendingIntent.getActivity(this,
                notificationId+1,likeIntent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                        .setLargeIcon(bitmap)
                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(remoteMessage.getData().get("title"))
//                        .setStyle(new NotificationCompat.BigPictureStyle()
//                                .setSummaryText(remoteMessage.getData().get("message"))
//                                .bigPicture(bitmap))/*Notification with Image*/
//                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_home,
                                getString(R.string.notification_details),likePendingIntent)
                        .setContentIntent(likePendingIntent);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setContentInfo("Info");

        notificationManager.notify(notificationId, notificationBuilder.build());

    }


    /**
     * Build a push notification for a chat message
     * @param title
     * @param message
     *
     * @param s
     */
    private void sendMessageNotification(String title, String message, String post_id, String user_id,
                                         String date_created, String s) {
        Log.d(TAG, "sendChatmessageNotification: building a chatmessage notification");

        //You should use an actual ID instead
        int notificationId = new Random().nextInt(60000);



        Intent likeIntent = new Intent(this, NotificationActivity.class);
        likeIntent.putExtra(getString(R.string.calling_notification),true);
        likeIntent.putExtra(getString(R.string.data_post_id),post_id);
        likeIntent.putExtra(getString(R.string.data_user_id),user_id);
        final PendingIntent likePendingIntent = PendingIntent.getActivity(this,
                notificationId+1,likeIntent,PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }

            NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
//                        .setLargeIcon(bitmap)
                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(remoteMessage.getData().get("title"))
//                        .setStyle(new NotificationCompat.BigPictureStyle()
//                                .setSummaryText(remoteMessage.getData().get("message"))
//                                .bigPicture(bitmap))/*Notification with Image*/
//                        .setContentText(remoteMessage.getData().get("message"))
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        .addAction(R.drawable.ic_home,
                                getString(R.string.notification_details),likePendingIntent)
                        .setContentIntent(likePendingIntent);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("Info");

        notificationManager.notify(notificationId, notificationBuilder.build());

    }


    private int buildNotificationId(String id) {
        Log.d(TAG, "buildNotificationId: building a notification id.");

        int notificationId = 0;
        for (int i = 0; i < 9; i++) {
            notificationId = notificationId + id.charAt(0);
        }
        Log.d(TAG, "buildNotificationId: id: " + id);
        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
        return notificationId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

}