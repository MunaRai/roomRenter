package com.alisha.roomfinderapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.utils.SettingActivity;

public class MoreFragment extends Fragment {
    private TextView mTextView;
    public MoreFragment(){

    }
    //public MoreFragment(){

    //}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ImageButton imageButton = view.findViewById(R.id.setting_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), SettingActivity.class);
                in.putExtra("some", "somedata");
                startActivity(in);

            }
        });
        /*TextView mTextView = view.findViewById(R.id.user_detail);
        mTextView.setOnClickListener({
                Intent in = new Intent(getActivity(),Userprofile.class);
         in.putExtra("some","somedata");
        startActivity(in);
        });*/
        // }
        return view;
    }
   //TextView mtextview = view.findViewById(R.id.user_detail);
}
