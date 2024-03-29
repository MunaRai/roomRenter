package com.alisha.roomfinderapp.search;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.rooms.RoomRecyclerAdapter;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class High2LowRoomFragment extends Fragment {

    private Context mContext;
    private View view;

    private List<Room> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RoomRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter_high_low, container, false);
        mContext = getContext();

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupAdapter();
        loadHousesData();


        return view;
    }

    private void loadHousesData() {
        refresh.setRefreshing(true);

        mFirebaseHelper.getMyRef().child(FilePaths.ROOM)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mList.clear();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {

                            Room post= ds.getValue(Room.class);

                            mList.add(post);
                        }
                        filterListByHigh2Low();
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

    private void filterListByHigh2Low() {
        Collections.sort(mList, new Comparator<Room>() {
            @Override
            public int compare(Room u1, Room u2) {
                int p1 = Integer.parseInt(u2.getPrice());
                int p2 = Integer.parseInt(u1.getPrice());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return Integer.compare(p1,p2);
                }
                return u2.getPrice().compareTo(u1.getPrice());
            }
        });
    }

    private void setupAdapter() {

        mList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        refresh = view.findViewById(R.id.refresh);

        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new RoomRecyclerAdapter(mContext, mList);

        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHousesData();
            }
        });
    }


}
