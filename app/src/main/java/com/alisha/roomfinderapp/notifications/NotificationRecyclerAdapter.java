package com.alisha.roomfinderapp.notifications;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Notification;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.models.User;
import com.alisha.roomfinderapp.rooms.normal.RoomDetailActivity;
import com.alisha.roomfinderapp.utils.FilePaths;
import com.alisha.roomfinderapp.utils.FirebaseHelper;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.github.thunder413.datetimeutils.DateTimeStyle;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "NotificationRecyclerAda";
    private final FirebaseHelper firebaseHelper;
    private Context mContext;
    private List<Notification> mList;

    public NotificationRecyclerAdapter(Context context, List<Notification> datas) {
        mContext = context;
        mList = datas;
        firebaseHelper = new FirebaseHelper(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_list_item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationRecyclerAdapter.MyViewHolder holder, int position) {
        final Notification post = mList.get(position);
        initImageLoader();
        holder.userCommentDesc.setText(post.getMessage());
        //for user details
        firebaseHelper.getMyRef().child(FilePaths.USER).orderByChild("user_id").equalTo(post.getSenderId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    final User user = ds.getValue(User.class);
                    assert user != null;
                    holder.username.setText(user.getUsername());
                    UniversalImageLoader.setImage(user.getAvatar_img_link(), holder.profileImage, null, "");

                    holder.call_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + user.getContact()));
                            mContext.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseHelper.getMyRef().child(FilePaths.ROOM).orderByChild("id").equalTo(post.getKeyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    final Room room = ds.getValue(Room.class);
                    assert room != null;

                    holder.roomName.setText(room.getName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, RoomDetailActivity.class);
                            intent.putExtra(mContext.getString(R.string.calling_room_detail),
                                    room);

                            mContext.startActivity(intent);

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        holder.userCommentDate.setText(
                DateTimeUtils.formatWithStyle(post.getDate_added(), DateTimeStyle.FULL));

//        UniversalImageLoader.setImage(post.getImage(), holder.image, null, "");
//        holder.title.setText(post.getName());

    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final Button call_user;

        CircleImageView profileImage;
        TextView username, userCommentDesc, userCommentDate, roomName;

        public MyViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.profileImage);
            username = view.findViewById(R.id.username);
            userCommentDesc = view.findViewById(R.id.userCommentDesc);
            userCommentDate = view.findViewById(R.id.userCommentDate);
            call_user = view.findViewById(R.id.call_user);
            roomName = view.findViewById(R.id.roomName);


        }
    }
}
