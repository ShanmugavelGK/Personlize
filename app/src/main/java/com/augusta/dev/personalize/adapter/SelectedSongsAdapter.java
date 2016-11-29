package com.augusta.dev.personalize.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.bean.SelectedSongsBean;

import java.util.List;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class SelectedSongsAdapter extends RecyclerView.Adapter<SelectedSongsAdapter.MyViewHolder> {

    private List<SelectedSongsBean> selectedSongsList;
    Context mContext;

    public SelectedSongsAdapter(List<SelectedSongsBean> selectedSongsList, Context mContext) {
        this.selectedSongsList = selectedSongsList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_rouse_selected_songs_list, parent, false);

        return new SelectedSongsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectedSongsBean currentItem = selectedSongsList.get(position);
        holder.tvSongsName.setText(currentItem.getSongName());

        if (currentItem.isSelected()) {
            holder.ivIsSelected.setImageResource(R.drawable.ic_check_circle);
        } else {
            holder.ivIsSelected.setImageResource(R.drawable.ic_check_blank_circle);
        }
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedSongsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSongsName;
        private final ImageView ivIsSelected;
        private final ImageView ivDelete;

        public MyViewHolder(View view) {
            super(view);

            tvSongsName = (TextView) view.findViewById(R.id.tv_songs_name);
            ivIsSelected = (ImageView) view.findViewById(R.id.iv_is_selected);
            ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

        }
    }
}
