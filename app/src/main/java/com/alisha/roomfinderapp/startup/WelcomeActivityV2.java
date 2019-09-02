package com.alisha.roomfinderapp.startup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.home.HomeActivity;
import com.alisha.roomfinderapp.models.ReviewRatingMerge;
import com.alisha.roomfinderapp.utils.AutomateData;
import com.alisha.roomfinderapp.utils.CookieThumperSample;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import su.levenetc.android.textsurface.TextSurface;

public class WelcomeActivityV2 extends AppCompatActivity {
    private static final String TAG = "WelcomeActivityV2";

    private TextSurface textSurface;
    private Button btn_continue;
    private FirebaseHelper firebaseHelper;
    private List<String> roomIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_v2);


        textSurface = findViewById(R.id.text_surface);
        btn_continue = findViewById(R.id.btn_continue);

        btn_continue.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_continue.setClickable(true);
                btn_continue.setText("Go");
            }
        }, 5000);


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUI(FirebaseAuth.getInstance().getCurrentUser());

            }
        });

        textSurface.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        }, 1000);

        initFirstLauchTempData();

    }

    private void initFirstLauchTempData() {
        firebaseHelper = new FirebaseHelper(getApplicationContext());
        automateData();

    }

    private void automateData() {
        roomIds = new ArrayList<>();
//        for (int i = 0; i < AutomateData.room_names.length; i++) {
//            final Room post = new Room();
//            String keyId = firebaseHelper.getMyRef().child(FilePaths.ROOM).push().getKey();
//            roomIds.add(keyId);
//            post.setCategory_name("Room");
//            post.setId(keyId);
//            post.setName(AutomateData.room_names[i]);
//            post.setDesc(AutomateData.description[i]);
//            post.setLocation(AutomateData.locations[i]);
//            post.setImage(AutomateData.images[i]);
//            long randomPrice = new Random().nextInt(9000) + 1000;
//            post.setPrice(String.valueOf(randomPrice));
//            post.setPerUnits("per month");
//            post.setNo_of_rooms(AutomateData.no_rooms);
//            post.setOwner_name(AutomateData.names[i]);
//            post.setOwner_id(AutomateData.users_id[i]);
//            post.setContact_no(980823929);
//            post.setOwnerRules("Landlord-tenant law governs the rental of commercial and residential property. It is composed primarily of state statutes and common law. A number of states have based their statutory law on either the Uniform Residential Landlord And Tenant Act (URLTA) or the Model Residential Landlord-Tenant Code. Further, federal statutory law may be relevant during times of national/regional emergencies and in preventing forms of discrimination.\n" +
//                    "\n" +
//                    "The Four Basic Types of Landlord-Tenant Relationships\n" +
//                    "The basis of the legal relationship between a landlord and tenant is grounded in both contract and property law. The tenant has a property interest in the land (historically, a non-freehold estate) for a given period of time before the property interest transfers back to the landlord. See State Property Statues. While these four relationship types are generally true, they are subject to state statutes, as well as the actual lease agreed upon by the landlord and the tenant. \n" +
//                    "\n" +
//                    "The length of the tenancy is typically classified in 1 of 4 categories:\n" +
//                    "\n" +
//                    "Term of Years Tenancy\n" +
//                    "The relationship lasts for a fixed period which is agreed upon in advance by both the landlord and tenant. When the period ends, so do the tenant's possessory rights/\n" +
//                    "In this relationship, the tenant has the right to possess the land, to restrict others (including the landlord from entering the land, and to sublease or assign the property). \n" +
//                    "Periodic Tenancy\n" +
//                    "The relationship is automatically renewed unless the landlord gives advance notice of termination\n" +
//                    "In this relationship, the tenant has the right to possess the land, to restrict others (including the landlord from entering the land, and to sublease or assign the property). \n" +
//                    "Tenancy at Will\n" +
//                    "There is no fixed ending period. The relationship continues for as long as the tenant and landlord desire.\n" +
//                    "Tenancy at Sufferance\n" +
//                    "The tenant continues to inhabit the property after the lease expires.");
//            post.setDistrict(AutomateData.districts[i]);
//            post.setExactLocation(AutomateData.locations[i]);
//            post.setDate_added(date);
//            post.setUpdated_at(date);
//            post.setDeleted_at(null);
//            post.setLattitude((long) Double.parseDouble(AutomateData.latitudes[i]));
//            post.setLongitutde((long) Double.parseDouble(AutomateData.longitudes[i]));
//            post.setViewCount(0);
//
//            post.setOnlinePaymentType(i % 2 == 0 ? "Cash" : "Online");
//            post.setServices(getString(R.string.sample_services2));
//
//            assert keyId != null;
//
//            firebaseHelper.getMyRef().child(FilePaths.ROOM).child(keyId).setValue(post);
//
//
//        }

//
//        firebaseHelper.getMyRef().child(FilePaths.ROOM).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int i = 0;
//                for (DataSnapshot ds :
//                        dataSnapshot.getChildren()) {
//                    Room room = ds.getValue(Room.class);
//                    setupRoomReviews(room.getId(), i);
//
//                    i++;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void setupRoomReviews(String roomId, int i) {

        String keyId = firebaseHelper.getMyRef().child(FilePaths.USER_REVIEWS).push().getKey();

        final ReviewRatingMerge reviewRatingMerge = new ReviewRatingMerge();
        reviewRatingMerge.setAvatar_img_link(AutomateData.images[i]);
        reviewRatingMerge.setComment_desc(AutomateData.reviews[i]);
        reviewRatingMerge.setPost_id(roomId);
        reviewRatingMerge.setUser_id(AutomateData.users_id[i]);
        reviewRatingMerge.setComment_id(keyId);
        reviewRatingMerge.setDate_created(DateTimeUtils.formatDate(new Date()));
        reviewRatingMerge.setUsername(AutomateData.names[i]);
        reviewRatingMerge.setUserRating(AutomateData.getRandomNumberInRange(1, 5));
        firebaseHelper.getMyRef().child(FilePaths.USER_REVIEWS).child(keyId).setValue(reviewRatingMerge);


    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void show() {
        textSurface.reset();

        CookieThumperSample.play(textSurface, getAssets());
//        CookieThumperSample.play(textSurface, getAssets());
    }

}
