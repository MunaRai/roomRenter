package com.alisha.roomfinderapp.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //prepare to send to fragment
        Bundle notificationBundle= new Bundle();
        FirebaseHelper firebaseHelper = new FirebaseHelper(getApplicationContext());
        //get data from notification
        Intent in = getIntent();
        if (in.hasExtra(getString(R.string.calling_notification))){
            String post_id = in.getStringExtra(getString(R.string.data_post_id));
            String user_id = in.getStringExtra(getString(R.string.data_user_id));

            notificationBundle.putString(getString(R.string.data_post_id), post_id);
            notificationBundle.putString(getString(R.string.data_user_id), user_id);

        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        NotificationFragment notificationFragment = new NotificationFragment();
        notificationFragment.setArguments(notificationBundle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, notificationFragment)
                .commit();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
