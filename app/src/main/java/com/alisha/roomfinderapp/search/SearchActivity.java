package com.alisha.roomfinderapp.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<String> mList;
    private FirebaseHelper mFirebaseHelper;

    private FragmentManager fragmentManager;
    private FirebaseHelper firebaseHelper;

    private List<Room> rooms;
    private boolean isSearchReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        firebaseHelper = new FirebaseHelper(getApplicationContext());


        rooms= new ArrayList<>();
        firebaseHelper.getMyRef().child(FilePaths.ROOM).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    Room room = ds.getValue(Room.class);
                    rooms.add(room);
                }
                isSearchReady = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        SearchView searchView = findViewById(R.id.fa_search);
//
//        final List<String> searchedLocations = new ArrayList<>();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (isSearchReady){
//                    searchedLocations.clear();
//                    for (Room room :
//                            rooms) {
//                        if (room.getLocation().contains(newText)) {
//                            searchedLocations.add(room.getLocation());
//
//                            Toast.makeText(SearchActivity.this, "Found", Toast.LENGTH_SHORT).show();
//                        }
//                        //you can implement recycler view or list view here to show these results
//                        //use adapter.notifydatasetchanged()
//                        }
//                }
//                return false;
//            }
//        });

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, new High2LowRoomFragment())
                .commit();
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
//        menu.findItem(R.id.edit_post_action).setVisible(false);
//        menu.findItem(R.id.delete_post_action).setVisible(false);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_price_high:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new High2LowRoomFragment())
                        .commit();

                return true;
            case R.id.action_price_low:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new Low2HighRoomFragment())
                        .commit();
                return true;
            case R.id.action_price_location:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new LocationRoomFragment())
                        .commit();
                return true;
            case R.id.action_popular:
                fragmentManager.beginTransaction()
                        .replace(R.id.main_frame, new PopularRoomFragment())
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
