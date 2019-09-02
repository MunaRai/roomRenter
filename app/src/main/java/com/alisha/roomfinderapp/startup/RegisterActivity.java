package com.alisha.roomfinderapp.startup;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.alisha.roomfinderapp.utils.HelperUtilities;
import com.alisha.roomfinderapp.utils.Permissions;
import com.alisha.roomfinderapp.utils.TextDataUtils;
import com.alisha.roomfinderapp.utils.VerifyPermissions;

import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "RegisterActivity";
    private static final int VERIFY_PERMISSION_REQUEST = 1;
    public Context mContext;

    private String username, email, password, contact;
    private EditText mEmail, mName, mPassword, input_contact;
    private ProgressBar mProgressBar;

    private CircleImageView avatar_img;


    //    firebase
    private FirebaseHelper mFirebaseHelper;
    private String avatar_img_link = "";
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupToolbar();

        setupWidgets();
        init();


    }


    /**
     * when user presses submit, validate and register user to the firebase
     */
    private void init() {

        avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyPermissions verifyPermissions = new VerifyPermissions(mContext, RegisterActivity.this);
                if (verifyPermissions.checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, VERIFY_PERMISSION_REQUEST);
                } else {
                    verifyPermissions.verifyPermissionsArray(Permissions.PERMISSIONS);
                }

            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Register button click");

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                username = mName.getText().toString();
                contact = input_contact.getText().toString();

                if (validateForm()) {
//                    progressBarHandler.showProgressBar();
                    showProgressBar();
                    mFirebaseHelper.registerNewEmail(email, password, username, avatar_img_link, contact);


                } else {
                    hideProgressBar();
                }
            }
        });
    }


    /**
     * check an array of permission
     * check if user has given permissions to the app to perform certain activites like camera permissions
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionsArray(String[] permissions) {

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

    private void verifyPermissionsArray(String[] permissions) {
        Log.d(TAG, "verifyPermissionsArray: verifying permissions");

        ActivityCompat.requestPermissions(
                this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
        finish();

    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextDataUtils.isEmpty(email)) {
            mEmail.setError("Required.");
            valid = false;
        }
        if (TextDataUtils.isEmpty(contact)) {
            input_contact.setError("Required.");
            valid = false;
        }

        if (!HelperUtilities.isValidEmail(email)) {
            mEmail.setError("Not a valid email");
            valid = false;
        }


        if (TextDataUtils.isEmpty(username)) {
            mName.setError("Required.");
            valid = false;
        }

        if (!TextDataUtils.isValidPassword(password)) {
            mPassword.setError("Password should be more than 6 characters.");
            valid = false;
        }

        return valid;
    }

    private void setupWidgets() {
        mContext = RegisterActivity.this;
        view = View.inflate(mContext, R.layout.activity_register, null);
        mFirebaseHelper = new FirebaseHelper(mContext);

        avatar_img = findViewById(R.id.profileImage);
        mEmail = findViewById(R.id.input_email);
        input_contact = findViewById(R.id.input_contact);
        mName = findViewById(R.id.input_name);
        mPassword = findViewById(R.id.input_password);

        mProgressBar = findViewById(R.id.progressBar);


        hideProgressBar();
//        progressBarHandler = new ProgressBarHandler(view);
//
//        progressBarHandler.hideProgressBar();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            avatar_img_link = targetUri.toString();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                avatar_img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register an account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(view.GONE);

    }

    public void showProgressBar() {
        mProgressBar.setVisibility(view.VISIBLE);

    }

    public void onImageClick(View view) {
        VerifyPermissions verifyPermissions = new VerifyPermissions(mContext, RegisterActivity.this);
        if (verifyPermissions.checkPermissionsArray(Permissions.PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, VERIFY_PERMISSION_REQUEST);
        } else {
            verifyPermissions.verifyPermissionsArray(Permissions.PERMISSIONS);
        }
    }
}


