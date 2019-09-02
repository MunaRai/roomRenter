package com.alisha.roomfinderapp.search;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.List;


public class LocationRoomFragment extends Fragment {

    private Context mContext;
    private View view;

    private List<Room> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RoomRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;
    private ArrayAdapter<CharSequence> districtsAdapter;
    private Spinner districtsSpinner;
    private String item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location_filter, container, false);
        mContext = getContext();

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupWidgets();
        setupAdapter();
        loadHousesData();


        return view;
    }

    private void setupWidgets() {
        districtsSpinner = view.findViewById(R.id.districtsSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        districtsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.districts_array, android.R.layout.simple_spinner_item);
        districtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtsSpinner.setAdapter(districtsAdapter);

        item = districtsAdapter.getItem(0).toString();

        districtsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long position) {
                item = adapterView.getItemAtPosition((int) position).toString();
                loadHousesData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                            if (post.getDistrict().equals(item)){

                                mList.add(post);
                            }
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
