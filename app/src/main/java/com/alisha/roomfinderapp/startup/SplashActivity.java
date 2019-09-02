package com.alisha.roomfinderapp.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.home.HomeActivity;
import com.alisha.roomfinderapp.utils.SharedPreferenceHelper;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private Context mContext = SplashActivity.this;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);
        loadScreen();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.roomNotifications));
        initImageLoader();
    }


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void loadScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!sharedPreferenceHelper.getIsFirstLaunch()) {
                    sharedPreferenceHelper.setIsfirstlaunch(false);
                    Intent in = new Intent(mContext, WelcomeActivityV2.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null){

                        Intent in = new Intent(mContext, LoginActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }else {
                        Intent in = new Intent(mContext, HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }
                }

            }
        }, 2000);
    }


}
