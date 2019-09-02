package com.alisha.roomfinderapp.rooms.normal;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.alisha.roomfinderapp.models.ReviewRatingMerge;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.alisha.roomfinderapp.utils.ReviewRatingRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class ReviewFragment extends Fragment {

    private Context mContext;
    private View view;

    private List<Room> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ReviewRatingRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;
    private List<ReviewRatingMerge> mCommentList;
    private String post_id, post_name;


    public ReviewFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.snippet_layout_comments, container, false);
        mContext = getContext();

        initArgs();
        mFirebaseHelper = new FirebaseHelper(mContext);

        setupAdapter();
        setupCommentsList();


        return view;
    }

    private void initArgs() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            post_id = bundle.getString(getContext().getString(R.string.data_post_id));
            post_name = bundle.getString(getContext().getString(R.string.data_post_name));
        }
    }


    private void setupCommentsList() {

        mFirebaseHelper.getReviewRatingMerge(post_id, new RoomDetailActivity.ReviewRatingListener() {
            @Override
            public void onLoaded(List<ReviewRatingMerge> mComments) {
                mCommentList.clear();
                mCommentList.addAll(mComments);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void setupAdapter() {
        mCommentList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, true);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(manager);

        adapter = new ReviewRatingRecyclerAdapter(getContext(), mCommentList, new ReviewRatingRecyclerAdapter.OnCommentRemoveListener() {
            @Override
            public void onCommentRemove(ReviewRatingMerge commentUserMerge) {
                popupForDelete(commentUserMerge);
            }
        });

        recyclerView.setAdapter(adapter);


    }

    private void popupForDelete(final ReviewRatingMerge commentUserMerge) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        builder.setTitle("Delete comment  " + commentUserMerge.getComment_desc() + " ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                mFirebaseHelper.getMyRef().child(FilePaths.USER_REVIEWS)
                        .child(commentUserMerge.getComment_id())
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                mCommentList.remove(commentUserMerge);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(mContext, "Deleted artSales: " + post_name, Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.cancel();
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


}
