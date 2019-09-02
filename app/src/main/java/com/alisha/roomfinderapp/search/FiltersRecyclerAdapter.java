package com.alisha.roomfinderapp.search;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.alisha.roomfinderapp.R;

import java.util.List;


public class FiltersRecyclerAdapter extends RecyclerView.Adapter<FiltersRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> mList;
    private OnFilterSearch onFilterSearch;

    public FiltersRecyclerAdapter(Context context, List<String> datas, OnFilterSearch onFilterSearch) {
        mContext = context;
        mList = datas;
        this.onFilterSearch = onFilterSearch;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_list_item_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersRecyclerAdapter.MyViewHolder holder, int position) {
        holder.filter_text.setText(mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button filter_text;


        public MyViewHolder(View view) {
            super(view);
            filter_text = view.findViewById(R.id.filter_text);


            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onFilterSearch.onFilterClick(position);
                        filter_text.setTextColor(mContext.getColor(R.color.accent_500));
                    }
                }
            });
        }
    }

    public interface OnFilterSearch {
        void onFilterClick(int position);
    }
}

