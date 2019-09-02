package com.alisha.roomfinderapp.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Complaint;
import com.alisha.roomfinderapp.rooms.normal.RoomDetailActivity;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ComplaintsRecyclerAdapter extends RecyclerView.Adapter<ComplaintsRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Complaint> mList;

    public ComplaintsRecyclerAdapter(Context context, List<Complaint> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_complaint, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintsRecyclerAdapter.MyViewHolder holder, int position) {
        Complaint post = mList.get(position);
        initImageLoader();
        UniversalImageLoader.setImage(post.getUser_pic(), holder.user_image, null, "");
        holder.username.setText(post.getUsername());
        holder.title.setText(post.getPost_name());
        holder.user_complaint.setText(post.getDescription());

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
        CircleImageView user_image;
        TextView title,username,user_complaint;

        public MyViewHolder(View view) {
            super(view);
            user_image = view.findViewById(R.id.user_image);
            title = view.findViewById(R.id.title);
            username = view.findViewById(R.id.username);
            user_complaint = view.findViewById(R.id.user_complaint);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
//                        Intent intent = new Intent(mContext, RoomDetailActivity.class);
//                        intent.putExtra(mContext.getString(R.string.data_post_id),
//                                mList.get(getAdapterPosition()).getPost_id());
//
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
