package com.alisha.roomfinderapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.home.HomeActivity;
import com.alisha.roomfinderapp.models.CommentRatingMerge;
import com.alisha.roomfinderapp.models.ReviewRatingMerge;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.models.User;
import com.alisha.roomfinderapp.rooms.normal.RoomDetailActivity;
import com.alisha.roomfinderapp.startup.LoginActivity;
import com.alisha.roomfinderapp.startup.RegisterActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.alisha.roomfinderapp.utils.SharedPreferenceHelper.getInstance;


public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static FirebaseHelper instance;

    private final StorageReference mStorageReference;
    private String user_id;
    private Context mContext;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private int mPhotoUploadProgress = 0;

    public static FirebaseHelper getFirebaseInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);
        }

        return instance;
    }

    public FirebaseHelper(Context mContext) {
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        //inititalize storage

        mStorageReference = FirebaseStorage.getInstance().getReference();
//        initialize the firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        if (mAuth.getCurrentUser() != null) {
            this.user_id = mAuth.getCurrentUser().getUid();
            Log.e(TAG, "FirebaseHelper: " + this.user_id);
        }

        Log.e(TAG, "FirebaseHelper: " + myRef.toString());
    }

    public StorageReference getmStorageReference() {
        return mStorageReference;
    }


    public DatabaseReference getMyRef() {
        return myRef;
    }

    public void setUserID(String userID) {
        this.user_id = userID;
    }


    public void signOut() {
        mAuth.signOut();


        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }


    public FirebaseAuth getAuth() {
        return mAuth;
    }

    /**
     * sets the selected user by admin to staff
     *
     * @param selected_user_id - user_id of the user selected by the admin in profile activity
     */
    public void setUserAsNormal(String selected_user_id) {
        myRef.child("users")
                .child(selected_user_id)
                .child("user_type")
                .setValue(UserTypes.NORMAL);
    }


    public String getUser_id() {
        return user_id;
    }

    public void registerNewEmail(final String email, String password, final String username,
                                 final String avatar_img, final String contact) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            ((RegisterActivity) mContext).hideProgressBar();

                        } else if (task.isSuccessful()) {

                            Toast.makeText(mContext, "Sucessfully registered user. Welcome " + username, Toast.LENGTH_SHORT).show();
                            ((RegisterActivity) mContext).finish();
                            mContext.startActivity(new Intent(mContext, HomeActivity.class));
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            FirebaseUser current_user = mAuth.getCurrentUser();
                            assert current_user != null;
                            current_user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");

                                            }
                                        }
                                    });

                            User user = new User(mAuth.getCurrentUser().getUid(),
                                    username,
                                    avatar_img,
                                    UserTypes.NORMAL,
                                    email,
                                    contact
                            );
                            SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(mContext);

                            sharedPreferenceHelper.saveUserInfo(user);

                            addUserDetails(sharedPreferenceHelper.getUserInfo());
                            ((RegisterActivity) mContext).hideProgressBar();

                        }

                    }
                });
    }

    private void addUserDetails(User user) {
        myRef.child("users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);

        if (user.getAvatar_img_link().isEmpty()) {

            Intent in = new Intent(mContext, HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ((RegisterActivity) mContext).finish();
            mContext.startActivity(in);
        } else {
            FilePaths filePaths = new FilePaths();
            final StorageReference storageReference = mStorageReference
                    .child("users" + "/" + SharedPreferenceHelper.getInstance(mContext).getUID() + "/avatar");
            InputStream is = null;
            try {
                is = mContext.getContentResolver().openInputStream(Uri.parse(user.getAvatar_img_link()));

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                byte[] data = ImageManager.getBytesFromBitmap(bitmap, 100);
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(mContext, "Exp:" + exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                    }


                });
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();
                    }
                })
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUrl = task.getResult();


                                    assert downloadUrl != null;
                                    SharedPreferenceHelper sharedPreferenceHelper = getInstance(mContext);
                                    sharedPreferenceHelper.setAvatar(downloadUrl.toString());
                                    Toast.makeText(mContext, "Upload success", Toast.LENGTH_SHORT).show();
                                    myRef.child("users").child(mAuth.getCurrentUser().getUid())
                                            .child(mContext.getString(R.string.avatar_link))
                                            .setValue(downloadUrl.toString());


                                    Intent in = new Intent(mContext, HomeActivity.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    ((RegisterActivity) mContext).finish();
                                    mContext.startActivity(in);
                                } else {
                                    // Handle failures
                                    // ...
                                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } catch (FileNotFoundException e) {
                //            e.printStackTrace();
                Toast.makeText(mContext, "File not found.", Toast.LENGTH_SHORT).show();
            } finally {

                try {
                    assert is != null;
                    is.close();
                } catch (IOException e) {
                    //                e.printStackTrace();
                    Toast.makeText(mContext, "Exception while closing file", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    public void getCommentRatingList(String postId, final RoomDetailActivity.CommentRatingListener commentRatingListener) {
        myRef.child(FilePaths.USER_COMMENTS)
                .child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<CommentRatingMerge> commentList = new ArrayList<>();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            CommentRatingMerge comment = ds.getValue(CommentRatingMerge.class);
                            commentList.add(comment);
                        }

                        commentRatingListener.onLoaded(commentList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public void getReviewRatingMerge(String postId, final RoomDetailActivity.ReviewRatingListener commentRatingListener) {
        myRef.child(FilePaths.USER_REVIEWS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<ReviewRatingMerge> commentList = new ArrayList<>();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {

                            ReviewRatingMerge comment = ds.getValue(ReviewRatingMerge.class);

                            if (comment.getPost_id().equals(postId))
                                commentList.add(comment);
                        }

                        commentRatingListener.onLoaded(commentList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void checkIfRatingExists(final Room post, final RoomDetailActivity.CheckRatingExistListener checkRatingExistListener) {
        myRef.child(FilePaths.USER_COMMENTS)
                .child(post.getId())
                .orderByChild("user_id")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            CommentRatingMerge comment = ds.getValue(CommentRatingMerge.class);
                            if (comment.getUser_id().equals(user_id)) {

                                checkRatingExistListener.onResult(true, post.getId());
                            } else {
                                checkRatingExistListener.onResult(false, "");
                            }
                        }
                        if (!dataSnapshot.exists())
                            checkRatingExistListener.onResult(false, "");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getUserRating(String postId, final RoomDetailActivity.GetUserRatingListener getUserRatingListener) {
        myRef.child(FilePaths.USER_COMMENTS)
                .child(postId)
                .orderByChild("user_id")
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds :
                                    dataSnapshot.getChildren()) {

                                CommentRatingMerge comment = ds.getValue(CommentRatingMerge.class);
                                getUserRatingListener.onResult((int) comment.getUserRating());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
