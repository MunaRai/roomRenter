package com.alisha.roomfinderapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.ReviewRatingMerge;
import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.alisha.roomfinderapp.utils.SharedPreferenceHelper.getInstance;

public class ReviewRatingRecyclerAdapter extends RecyclerView.Adapter<ReviewRatingRecyclerAdapter.ViewHolder> {


    private final FirebaseHelper mFirebaseHelper;
    private final SharedPreferenceHelper sharedPreferences;

    private Context mContext;

    private List<ReviewRatingMerge> mCommentsList;
    private OnCommentRemoveListener onCommentRemoveListener;

    public ReviewRatingRecyclerAdapter(Context mContext, List<ReviewRatingMerge> mCommentsList,

                                       OnCommentRemoveListener listener) {

        this.mContext = mContext;
        this.mCommentsList = mCommentsList;

        onCommentRemoveListener = listener;

        sharedPreferences = getInstance(mContext);
        mFirebaseHelper = new FirebaseHelper(mContext);

    }

    @NonNull
    @Override
    public ReviewRatingRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item_comment_review, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewRatingRecyclerAdapter.ViewHolder holder, final int position) {


        final ReviewRatingMerge comment = mCommentsList.get(holder.getAdapterPosition());
        UniversalImageLoader.setImage(comment.getAvatar_img_link(),
                holder.mUserProfilePhoto, null, "");

        holder.mUsername.setText(comment.getUsername());
        holder.mComment.setText(comment.getComment_desc());

//        int dateDiff = DateTimeUtils.getDateDiff(new Date(),
//                comment.getDate_created(), DateTimeUnits.DAYS);

        String dateDiff = DateTimeUtils.getTimeAgo(mContext, DateTimeUtils.formatDate(comment.getDate_created()),
                DateTimeStyle.AGO_SHORT_STRING);
        holder.mUserCommentDate.setText(dateDiff + " days ago");

        if (!comment.getUser_id().equals(mFirebaseHelper.getAuth().getCurrentUser().getUid())) {
            holder.mUserCommentDelete.setVisibility(View.GONE);
        } else {
            holder.mUserCommentDelete.setVisibility(View.VISIBLE);
            holder.mUserCommentDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onCommentRemoveListener.onCommentRemove(comment);
                }
            });

        }
        holder.userRating.setRating(comment.getUserRating());
    }


    @Override
    public int getItemCount() {
        if (mCommentsList != null) {
            return mCommentsList.size();
        }
        return 0;
    }

    public interface OnCommentRemoveListener {
        void onCommentRemove(ReviewRatingMerge commentUserMerge);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsername, mComment, mUserCommentDate, mUserCommentDelete;
        private CircleImageView mUserProfilePhoto;
        private RatingBar userRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.username);
            mComment = itemView.findViewById(R.id.userCommentDesc);
            mUserCommentDate = itemView.findViewById(R.id.userCommentDate);

            mUserProfilePhoto = itemView.findViewById(R.id.profileImage);

            mUserCommentDelete = itemView.findViewById(R.id.userDeleteComment);
            userRating = itemView.findViewById(R.id.userRating);

        }

    }
}
