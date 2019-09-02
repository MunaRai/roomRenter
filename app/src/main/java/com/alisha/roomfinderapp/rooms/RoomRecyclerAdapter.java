package com.alisha.roomfinderapp.rooms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.roomfinderapp.R;
import com.alisha.roomfinderapp.models.Room;
import com.alisha.roomfinderapp.rooms.normal.RoomDetailActivity;
import com.alisha.roomfinderapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Room> mList;

    public RoomRecyclerAdapter(Context context, List<Room> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_room_vert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomRecyclerAdapter.MyViewHolder holder, int position) {
        Room post = mList.get(position);
        initImageLoader();
        UniversalImageLoader.setImage(post.getImage(), holder.image, null, "");
        holder.title.setText(post.getName());

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
        ImageView image;
        TextView title;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, RoomDetailActivity.class);
                        intent.putExtra(mContext.getString(R.string.calling_room_detail),
                                mList.get(getAdapterPosition()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
