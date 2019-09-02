package com.alisha.roomfinderapp.rooms.normal;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.recommendations.RecommendationActivity;
import com.alisha.roomfinderapp.rooms.RoomRecyclerAdapter;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RoomFragment extends Fragment {

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
        view = inflater.inflate(R.layout.fragment_rooms, container, false);
        mContext = getContext();

        mFirebaseHelper = new FirebaseHelper(mContext);

        Button btn_recommendations = view.findViewById(R.id.btn_recommendations);
        btn_recommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RecommendationActivity.class));
            }
        });
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
//                            Map<String, Object> roomMap = (HashMap<String, Object>) ds.getValue();
//                            Room post = new Room();
//                            post.setId(roomMap.get("id").toString());//room id
//                            post.setName(roomMap.get("name").toString());//room id
//                            post.setDesc(roomMap.get("desc").toString());//room id
//                            post.setLocation(roomMap.get("location").toString());//room id
//                            post.setImage(roomMap.get("image").toString());//room id
//                            post.setCategory_name(roomMap.get("category_name").toString());//room id
//                            post.setContact_no(Long.parseLong(roomMap.get("contact_no").toString()));//room id
//                            post.setPrice(roomMap.get("price").toString());//room id
//                            post.setPerUnits(roomMap.get("perUnits").toString());//room id
//                            post.setOwner_name(roomMap.get("owner_name").toString());//room id
//                            post.setOwner_id(roomMap.get("owner_id").toString());//room id
//                            post.setNo_of_rooms(Long.parseLong(roomMap.get("no_of_rooms").toString()));//room id
//                            post.setFullFlat(Boolean.parseBoolean(roomMap.get("isFullFlat").toString()));//room id
//                            post.setOwnerRules(roomMap.get("ownerRules").toString());//room id
//                            post.setDistrict(roomMap.get("district").toString());//room id
//                            post.setExactLocation(roomMap.get("exactLocation").toString());//room id
//                            post.setDate_added(roomMap.get("date_added").toString());//room id
//                            post.setUpdated_at(roomMap.get("updated_at").toString());//room id
//                            post.setDeleted_at(roomMap.get("deleted_at").toString());//room id
//                            post.setLongitutde(Long.parseLong(roomMap.get("longitutde").toString()));//room id
//                            post.setLattitude(Long.parseLong(roomMap.get("lattitude").toString()));//room id
//                            post.setRating(Integer.parseInt(roomMap.get("rating").toString()));//room id
//                            post.setViewCount(Integer.parseInt(roomMap.get("viewCount").toString()));//room id
//                            post.setOnlinePaymentType(roomMap.get("onlinePaymentType").toString());//room id
//                            post.setServices(roomMap.get("services").toString());//room id
//

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
        recyclerView = view.findViewById(R.id.recyclerView);
        refresh = view.findViewById(R.id.refresh);

        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);

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
