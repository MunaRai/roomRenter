package com.alisha.roomfinderapp.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Complaint;
import com.alisha.roomfinderapp.models.Room;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class ComplainActivity extends AppCompatActivity {

    private static final String TAG = "ComplainActivity";
    private Context mContext = ComplainActivity.this;
    private FirebaseHelper mFirebaseHelper;
    private TextView textViewPostTitle;
    private BootstrapButton btn_send;
    private EditText editTextComplainDesc;
    private Room post;

    private String setComplainFrom;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        setupFirebase();
        setupWidgets();
        getDataFromIntent();
        setupToolbar();


        sendComplaint();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Send Complaint");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendComplaint() {
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String complaintDesc = editTextComplainDesc.getText().toString();

                if (TextDataUtils.isEmpty(complaintDesc)) {
                    Toast.makeText(mContext, "Complaint field cannot be empty", Toast.LENGTH_SHORT).show();
                } else {


                    String keyId = mFirebaseHelper.getMyRef().push().getKey();
                        Complaint complaint = new Complaint(keyId,
                                post.getId(),
                                post.getName(),
                                mFirebaseHelper.getAuth().getCurrentUser().getUid(), sharedPreferenceHelper.getUserInfo().getUsername(),
                                sharedPreferenceHelper.getUserInfo().getAvatar_img_link(),
                                complaintDesc
                        );

                        mFirebaseHelper.getMyRef().child(FilePaths.USER_COMPLAINTS)
                                .child(mFirebaseHelper.getAuth().getCurrentUser().getUid())
                                .setValue(complaint, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }


            }
        });
    }

    private void setupWidgets() {
        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        editTextComplainDesc = findViewById(R.id.editTextComplainDesc);
        btn_send = findViewById(R.id.btn_send);
    }

    private void setupFirebase() {
        mFirebaseHelper = FirebaseHelper.getFirebaseInstance(mContext);
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);
    }

    private void getDataFromIntent() {
        Intent in = getIntent();

        if (in.hasExtra(mContext.getString(R.string.calling_room_complaint))) {

            post = in.getParcelableExtra(mContext.getString(R.string.calling_room_complaint));
            Log.e(TAG, "getDataFromIntent: " + post.getId());
            textViewPostTitle.setText(post.getName().toString());

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public interface OnComplainPostListener {
        void onComplete(boolean isSuccess);
    }
}
