package com.alisha.roomfinderapp.admin;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Complaint;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsActivity extends AppCompatActivity {
    private Context mContext;


    private List<Complaint> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ComplaintsRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        mContext = getApplicationContext();

        mFirebaseHelper = new FirebaseHelper(getApplicationContext());
        setupAdapter();
        loadComplaintsData();
    }


    private void loadComplaintsData() {
        refresh.setRefreshing(true);

        mFirebaseHelper.getMyRef().child(FilePaths.USER_COMPLAINTS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mList.clear();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {

                            Complaint post= ds.getValue(Complaint.class);
                            mList.add(post);
                        }
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }
                });
    }

    private void setupAdapter() {

        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.refresh);

        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new ComplaintsRecyclerAdapter(mContext, mList);

        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadComplaintsData();
            }
        });
    }

}
