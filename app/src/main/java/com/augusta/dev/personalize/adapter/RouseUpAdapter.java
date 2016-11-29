package com.augusta.dev.personalize.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.activity.RouseBrowseActivity;
import com.augusta.dev.personalize.bean.RouseBean;

import java.util.List;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class RouseUpAdapter extends RecyclerView.Adapter<RouseUpAdapter.MyViewHolder> {

    private List<RouseBean> rouseList;
    Context mContext;

    public RouseUpAdapter(List<RouseBean> rouseList, Context mContext) {
        this.rouseList = rouseList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_rouse_up_list, parent, false);

        return new RouseUpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RouseBean currentItem = rouseList.get(position);
        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvSongList.setText(currentItem.getSongList());
        holder.tvTime.setText(currentItem.getTiming());

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(mContext, RouseBrowseActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rouseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvSongList;
        private final TextView tvTime;
        private final RelativeLayout viewItem;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_rouse_title);
            tvSongList = (TextView) view.findViewById(R.id.tv_rouse_songlist);
            tvTime = (TextView) view.findViewById(R.id.tv_rouse_timing);
            viewItem = (RelativeLayout) view.findViewById(R.id.view_item);
        }
    }
}
