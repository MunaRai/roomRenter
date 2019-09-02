package com.alisha.roomfinderapp.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.alisha.roomfinderapp.R;

public class SettingActivity extends AppCompatActivity {
    SwitchCompat switch_1;

    boolean stateSwitch;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        preferences = getSharedPreferences("PREFS", 0);
        stateSwitch = preferences.getBoolean("switch1", false);
        switch_1 = findViewById(R.id.notification);
        switch_1.setChecked(stateSwitch);
        switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateSwitch = !stateSwitch;
                switch_1.setChecked(stateSwitch);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1", stateSwitch);
                editor.apply();
            }
        });
    }

}
