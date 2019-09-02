package com.alisha.roomfinderapp.rooms.normal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.CommentRatingMerge;
import com.alisha.roomfinderapp.models.Notification;
import com.alisha.roomfinderapp.models.ReviewRatingMerge;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.rooms.RoomAddActivity;
import com.alisha.roomfinderapp.utils.CommentsRatingRecyclerAdapter;
import com.alisha.roomfinderapp.utils.ComplainActivity;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.alisha.roomfinderapp.utils.SharedPreferenceHelper;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.checkOut.helper.KhaltiCheckOut;
import khalti.widget.KhaltiButton;


public class RoomDetailActivity extends AppCompatActivity {
    private static final String TAG = "RoomDetailActivity";
    private Intent in;
    private Context mContext = RoomDetailActivity.this;
    private Room post;
    private TextView roomName, location, price, contact_no, owner_name, no_of_rooms, description, bookmark;
    private ImageView house_pic;
    private Button call_now;

    private FirebaseHelper mFirebaseHelper;
    private Button lin_directions;
    private AlertDialog.Builder commentBuilder;

    private CircleImageView imageUser;
    private SmileRating smile_rating;
    private TextInputEditText inputReview;
    private Button btn_send, btn_review_send;
    private RecyclerView recyclerView;
    private float userRated = 0;
    private List<CommentRatingMerge> mCommentList;
    private LinearLayoutManager manager;
    private CommentsRatingRecyclerAdapter adapter;
    private SharedPreferenceHelper sharedPreferenceHelper;

    private Button mButton_book;

    private ESewaConfiguration eSewaConfiguration;
    private static final int REQUEST_CODE_PAYMENT=100;

    public static final String[] POSTS = {
            "Reviews",
            "Comments"
    };
    private ViewPager viewPager;
    private int page=0;
    private TextView tv_reviews,tv_comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        mFirebaseHelper = new FirebaseHelper(mContext);
        sharedPreferenceHelper = new SharedPreferenceHelper(mContext);


        setupWidgets();
        getIncomingIntent();

        setupToolbar();
//        setupAdapter();
//        setupCommentsList();
////        tempData();
//
        initSendComment();
        initSendReview();

        setupPaymentSystem();

        setupViewPagerForCommentsOrReview();

    }

    private void setupViewPagerForCommentsOrReview() {
        tv_reviews = findViewById(R.id.tv_reviews);
        tv_comments = findViewById(R.id.tv_comments);


        CommentsFragment commentsFragment = new CommentsFragment();
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.data_post_id), post.getId());
        bundle.putString(getString(R.string.data_post_name), post.getName());
        commentsFragment.setArguments(bundle);
        reviewFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, reviewFragment).commit();

        tv_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, reviewFragment).commit();
            }
        });

        tv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, commentsFragment).commit();
            }
        });

    }

    private void setupPaymentSystem() {
        mButton_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               ESewaPayment eSewaPayment = new ESewaPayment("20",
//        "cycle", "102","https://roomrental13-36686.firebaseio.com/rest/saving-data/fireblog");
//
//               Intent intent = new Intent(RoomDetailActivity.this, ESewaPaymentActivity.class);
//               intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
//
//               intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
//               startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.payment_layout, null);
                KhaltiButton khaltiButton = alertLayout.findViewById(R.id.khalti_button);
                Button esewa = alertLayout.findViewById(R.id.esewa_button);


                final Config config = new Config("test_public_key_67697b5bb1194422a5c3c51c963b92ad", post.getId(), post.getName(), "", 1000L, new OnCheckOutListener() {

                    @Override
                    public void onSuccess(HashMap<String, Object> data) {
                        Log.i("Payment confirmed", data + "");
                        Log.i("RoomFinderApp", " data" + data.toString());
                        Toast.makeText(RoomDetailActivity.this, "Payment Sucess", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String action, String message) {
                        Log.i("RoomFinderApp", action + " message: " + message);
                        Toast.makeText(RoomDetailActivity.this, "Payment Failed Try Again!", Toast.LENGTH_LONG).show();
                    }
                });
                //   khalti.setCheckOutConfig(config);

                khaltiButton.setCheckOutConfig(config);
                AlertDialog.Builder alert = new AlertDialog.Builder(RoomDetailActivity.this);
                alert.setTitle("Payment with");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                khaltiButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(RoomDetailActivity.this, config);
                        khaltiCheckOut.show();
                    }
                });


                esewa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eSewaConfiguration = new ESewaConfiguration()
                                .clientId("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R")
                                .secretKey("BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==")
                                .environment(ESewaConfiguration.ENVIRONMENT_TEST);
                        ESewaPayment eSewaPayment = new ESewaPayment("20",
                                "cycle", "025", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

                        Intent intent = new Intent(RoomDetailActivity.this, ESewaPaymentActivity.class);
                        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);

                        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
                        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


//    private void setupAdapter() {
//        mCommentList = new ArrayList<>();
//        recyclerView = findViewById(R.id.recyclerView);
//        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);
//
//        recyclerView.setNestedScrollingEnabled(false);
//
//        recyclerView.setLayoutManager(manager);
//
//        adapter = new CommentsRatingRecyclerAdapter(mContext, mCommentList, new CommentsRatingRecyclerAdapter.OnCommentRemoveListener() {
//            @Override
//            public void onCommentRemove(CommentRatingMerge commentUserMerge) {
//                popupForDelete(commentUserMerge);
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//
//
//    }

    private void setupWidgets() {
//        title = findViewById(R.id.title);
//        title.setText(post.getDesc());

        house_pic = findViewById(R.id.house_pic);
        roomName = findViewById(R.id.roomName);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        no_of_rooms = findViewById(R.id.no_of_rooms);
        price = findViewById(R.id.price);
        call_now = findViewById(R.id.call_now);
        lin_directions = findViewById(R.id.directions);
        contact_no = findViewById(R.id.contact_no);
        owner_name = findViewById(R.id.owner_name);

        commentBuilder = new AlertDialog.Builder(mContext);
        commentBuilder.setTitle("Delete comment?");

        imageUser = findViewById(R.id.imageUser);
        smile_rating = findViewById(R.id.smile_rating);

        inputReview = findViewById(R.id.inputReview);
        btn_send = findViewById(R.id.btn_send);
        btn_review_send = findViewById(R.id.btn_send_review);

        recyclerView = findViewById(R.id.recyclerView);

        mButton_book=findViewById(R.id.btn_room_book);

        smile_rating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                userRated = level;
            }
        });

    }



    private void popupForDeleteItem() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        builder.setTitle("Delete room item: " + post.getName() + " ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                mFirebaseHelper.getmStorageReference().child(FilePaths.ROOM + "/room" + post.getId())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("l", "onSuccess: File deleted succesfully.");
                        mFirebaseHelper.getMyRef().child(FilePaths.ROOM)
                                .child(post.getId())
                                .removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        Toast.makeText(mContext, "Success!", Toast.LENGTH_SHORT).show();

                                        mFirebaseHelper.getMyRef().child(FilePaths.USER_COMMENTS)
                                                .child(post.getId())
                                                .removeValue();
                                        mFirebaseHelper.getMyRef().child(FilePaths.USER_COMPLAINTS)
                                                .child(post.getId())
                                                .removeValue();
                                        finish();
                                    }
                                });

                        dialog.cancel();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(mContext, "Error occured", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_details_menu, menu);
//        menu.findItem(R.id.edit_post_action).setVisible(false);
//        menu.findItem(R.id.delete_post_action).setVisible(false);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!post.getOwner_id().equals(mFirebaseHelper.getAuth().getCurrentUser().getUid())) {
            menu.findItem(R.id.edit_post_action).setVisible(false);
            menu.findItem(R.id.delete_post_action).setVisible(false);
        }
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());

        if (sharedPreferenceHelper.getUserType() == 0) {
            menu.findItem(R.id.delete_post_action).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.complain_action:
                Intent intent = new Intent(this, ComplainActivity.class);
                intent.putExtra(mContext.getString(R.string.calling_room_complaint), post);
                startActivity(intent);

                return true;
            case R.id.edit_post_action:
                Intent editIntent = new Intent(this, RoomAddActivity.class);
                editIntent.putExtra(mContext.getString(R.string.calling_room_edit), post);
                startActivity(editIntent);

                return true;
            case R.id.delete_post_action:
                popupForDeleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getIncomingIntent() {
        in = getIntent();

        //for edit calls from another activity
        if (in.hasExtra(mContext.getString(R.string.calling_room_detail))) {
            post = in.getParcelableExtra(mContext.getString(R.string.calling_room_detail));
            UniversalImageLoader.setImage(post.getImage(), house_pic, null, "");
            roomName.setText(post.getName());
            location.setText(post.getLocation());
            description.setText(post.getDesc());
            no_of_rooms.setText(post.getNo_of_rooms() + "");
            location.setText(post.getLocation());
            price.setText(post.getPrice() + "");
            contact_no.setText(post.getContact_no() + "");
            owner_name.setText(post.getOwner_name());

            call_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + post.getContact_no()));
                    startActivity(intent);
                }
            });

            lin_directions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "google.navigation:q=%.8f,%.8f",
                            Double.parseDouble(String.valueOf(post.getLattitude())), Double.parseDouble(String.valueOf(post.getLongitutde())));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    mContext.startActivity(intent);
                }
            });
            //increase view count by 1
            mFirebaseHelper.getMyRef().child(FilePaths.ROOM).child(post.getId()).child("viewCount").setValue(post.getViewCount()+1);

        }

        if (in.hasExtra(getString(R.string.data_post_id))) {
            mFirebaseHelper.getMyRef().child(FilePaths.ROOM).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds :
                            dataSnapshot.getChildren()) {
                        Room room = ds.getValue(Room.class);
                        if (room.getId().equals(in.getStringExtra(getString(R.string.data_post_id)))) {
                            post = room;
                        }
                    }
                    mFirebaseHelper.getMyRef().child(FilePaths.ROOM).child(post.getId()).child("viewCount").setValue(post.getViewCount()+1);
                    UniversalImageLoader.setImage(post.getImage(), house_pic, null, "");
                    roomName.setText(post.getName());
                    location.setText(post.getLocation());
                    description.setText(post.getDesc());
                    no_of_rooms.setText(post.getNo_of_rooms() + "");
                    location.setText(post.getLocation());
                    price.setText(post.getPrice() + "");
                    contact_no.setText(post.getContact_no() + "");
                    owner_name.setText(post.getOwner_name());

                    call_now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + post.getContact_no()));
                            startActivity(intent);
                        }
                    });

                    lin_directions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uri = String.format(Locale.ENGLISH, "google.navigation:q=%.8f,%.8f",
                                    Double.parseDouble(String.valueOf(post.getLattitude())), Double.parseDouble(String.valueOf(post.getLongitutde())));
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            mContext.startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //increase view count by 1


        }






    }

    private void initSendComment() {

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String textComment = inputReview.getText().toString();


                final String keyId;


                keyId = mFirebaseHelper.getMyRef()
                        .child(FilePaths.USER_COMMENTS)
                        .child(post.getId()).push().getKey();
                final CommentRatingMerge comment = new CommentRatingMerge(
                        keyId,
                        post.getId(),
                        textComment,
                        mFirebaseHelper.getAuth().getCurrentUser().getUid(),
                        sharedPreferenceHelper.getUserInfo().getUsername(),
                        sharedPreferenceHelper.getUserInfo().getAvatar_img_link(),
                        userRated
                );
                Toast.makeText(mContext, "sending your comment..", Toast.LENGTH_SHORT).show();
                mFirebaseHelper.getMyRef().child(FilePaths.USER_COMMENTS)
                        .child(post.getId())
                        .child(keyId)
                        .setValue(comment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Toast.makeText(mContext, "Comment added", Toast.LENGTH_SHORT).show();

                                sendNotificationToOwner(textComment);
                            }
                        });


                inputReview.setText("");


            }
        });
    }


    private void initSendReview() {

        btn_review_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String textComment = inputReview.getText().toString();

                inputReview.setText("");

                final String keyId;


                keyId = mFirebaseHelper.getMyRef()
                        .child(FilePaths.USER_REVIEWS)
                        .push().getKey();
                final ReviewRatingMerge comment = new ReviewRatingMerge(
                        keyId,
                        post.getId(),
                        textComment,
                        mFirebaseHelper.getAuth().getCurrentUser().getUid(),
                        sharedPreferenceHelper.getUserInfo().getUsername(),
                        sharedPreferenceHelper.getUserInfo().getAvatar_img_link(),
                        userRated
                );
                Toast.makeText(mContext, "sending your comment..", Toast.LENGTH_SHORT).show();
                mFirebaseHelper.getMyRef().child(FilePaths.USER_REVIEWS)
                        .child(keyId)
                        .setValue(comment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Toast.makeText(mContext, "Comment added", Toast.LENGTH_SHORT).show();

//                                sendNotificationToOwner(textComment);
                            }
                        });


                inputReview.setText("");


            }
        });
    }


    private void sendNotificationToOwner(String textComment) {
        Notification notification = new Notification();

        notification.setKeyId(post.getId());
        notification.setMessage(String.format("%s commented: %s", sharedPreferenceHelper.getUserInfo().getUsername(),
                textComment));
        notification.setUserId(post.getOwner_id());
        notification.setSenderId(mFirebaseHelper.getAuth().getCurrentUser().getUid());
        notification.setDate_added(DateTimeUtils.formatDate(new Date()));
        //insert to notification table only if other users send comment
        if (!mFirebaseHelper.getAuth().getCurrentUser().getUid().equals(post.getOwner_id())){
            String keyId = mFirebaseHelper.getMyRef().child(FilePaths.NOTIFICATIONS)
                    .child(post.getOwner_id()).push().getKey();
            mFirebaseHelper.getMyRef().child(FilePaths.NOTIFICATIONS)
                    .child(post.getOwner_id())
                    .child(post.getId())
                    .child(keyId)
                    .setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(mContext, "Message sent to owner", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    //payments module


    public interface CommentRatingListener {
        void onLoaded(List<CommentRatingMerge> mCommentList);
    }
    public interface ReviewRatingListener {
        void onLoaded(List<ReviewRatingMerge> mCommentList);
    }

    public interface CheckRatingExistListener {
        void onResult(Boolean ratingForUserExist, String postId);
    }

    public interface GetUserRatingListener {
        void onResult(int userRating);
    }
}
