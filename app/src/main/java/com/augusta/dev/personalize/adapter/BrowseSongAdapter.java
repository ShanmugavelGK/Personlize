package com.augusta.dev.personalize.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augusta.dev.personalize.R;

/**
 * Created by shanmugavel on 29-11-2016.
 */

public class BrowseSongAdapter extends RecyclerView.Adapter<BrowseSongAdapter.MyViewHolder> {

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSongsName;
        private final ImageView ivSelected;

        public MyViewHolder(View view) {
            super(view);

            tvSongsName = (TextView) view.findViewById(R.id.tv_songs_name);
            ivSelected = (ImageView) view.findViewById(R.id.iv_select);
        }
    }
}
