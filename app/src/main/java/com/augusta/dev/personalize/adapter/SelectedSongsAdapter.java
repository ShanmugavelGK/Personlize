package com.augusta.dev.personalize.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.interfaces.OnRemoveItem;

import java.util.List;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class SelectedSongsAdapter extends RecyclerView.Adapter<SelectedSongsAdapter.MyViewHolder> {

    private List<SongBean> selectedSongsList;
    Context mContext;

    public OnRemoveItem onRemoveItem;

    public void setOnRemoveItem(OnRemoveItem onRemoveItem) {
        this.onRemoveItem = onRemoveItem;
    }

    public SelectedSongsAdapter(List<SongBean> selectedSongsList, Context mContext) {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SongBean currentItem = selectedSongsList.get(position);
        holder.tvSongsName.setText(currentItem.getSongName());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSongsList.remove(position);  // selectedSongsList.get(position).
                onRemoveItem.onRemoveItem(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedSongsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSongsName;
        private final ImageView ivDelete;

        public MyViewHolder(View view) {
            super(view);

            tvSongsName = (TextView) view.findViewById(R.id.tv_songs_name);
            ivDelete = (ImageView) view.findViewById(R.id.iv_delete);

        }
    }
}
