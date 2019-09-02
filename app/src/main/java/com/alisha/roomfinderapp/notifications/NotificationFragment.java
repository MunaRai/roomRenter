package com.alisha.roomfinderapp.notifications;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Notification;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationFragment extends Fragment {
    private static final String TAG = "NotificationFragment";
    private Context mContext;
    private View view;

    private List<Notification> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private NotificationRecyclerAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private SwipeRefreshLayout refresh;
    private String  post_id,user_id;
    private boolean fromNotificationBundle = false;

    public NotificationFragment() {
        super();
        setArguments(new Bundle());
    }
    private void getArgs() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            fromNotificationBundle = true;
            post_id = bundle.getString(getString(R.string.data_post_id));
            user_id = bundle.getString(getString(R.string.data_user_id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mContext = getContext();

        firebaseHelper = new FirebaseHelper(mContext);

//        getArgs();
        setupAdapter();
        loadNotifications();


        return view;
    }

    private void loadNotifications() {
        refresh.setRefreshing(true);

        if (!fromNotificationBundle){
            firebaseHelper.getMyRef().child(FilePaths.NOTIFICATIONS).child(firebaseHelper.getUser_id()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot.toString());
                    for (DataSnapshot ds :
                            dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: "+ds.getValue());


                        Map<String, Object> objectMap = (HashMap<String, Object>) ds.getValue();

                        for (DataSnapshot dss :
                                ds.getChildren()) {
                            Map<String, Object> notificationMap = (HashMap<String, Object>) dss.getValue();
                            Notification notification = new Notification();
                            notification.setKeyId(notificationMap.get("keyId").toString());//room id
                            notification.setUserId(notificationMap.get("userId").toString());
                            notification.setMessage(notificationMap.get("message").toString());
                            notification.setSenderId(notificationMap.get("senderId").toString());
                            notification.setDate_added(notificationMap.get("date_added").toString());//room id

                            mList.add(notification);
                        }
                        refresh.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }

                    refresh.setRefreshing(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    refresh.setRefreshing(false);
                }
            });
        }

//        firebaseHelper.getMyRef().child(FilePaths.ROOM)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        mList.clear();
//                        for (DataSnapshot ds :
//                                dataSnapshot.getChildren()) {
//
//                            Room post= ds.getValue(Room.class);
//
//                            mList.add(post);
//                        }
//                        adapter.notifyDataSetChanged();
//                        refresh.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
//                        refresh.setRefreshing(false);
//                    }
//                });
    }

    private void setupAdapter() {

        mList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        refresh = view.findViewById(R.id.refresh);

        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new NotificationRecyclerAdapter(mContext, mList);

        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNotifications();
            }
        });
    }


}
