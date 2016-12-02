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
import com.augusta.dev.personalize.interfaces.OnClickPlay;

import java.util.List;

/**
 * Created by shanmugavel on 29-11-2016.
 */

public class BrowseSongAdapter extends RecyclerView.Adapter<BrowseSongAdapter.MyViewHolder> {

    private List<SongBean> listBrowseSongList;
    Context mContext;
    int selectedCount = 0;

    public OnClickPlay onClickPlay;


    public void setOnClickPlay(OnClickPlay onClickPlay) {
        this.onClickPlay = onClickPlay;
    }

    public BrowseSongAdapter(List<SongBean> listBrowseSongList, Context mContext) {
        this.listBrowseSongList = listBrowseSongList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_browser_song_list, parent, false);

        return new BrowseSongAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SongBean currentItem = listBrowseSongList.get(position);
        holder.tvSongsName.setText(currentItem.getSongName());


        if (currentItem.isSelect())
            holder.ivSelected.setImageResource(R.drawable.ic_check_circle);
        else
            holder.ivSelected.setImageResource(R.drawable.ic_check_blank_circle);

        holder.tvSongsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSongs(currentItem, holder, position);
            }
        });

        holder.ivSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSongs(currentItem, holder, position);
            }
        });


        if (listBrowseSongList.get(position).isPlay())
            holder.ivPlayPause.setImageResource(R.drawable.ic_pause);
        else
            holder.ivPlayPause.setImageResource(R.drawable.ic_play);


        holder.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listBrowseSongList.get(position).isPlay()) {
                    listBrowseSongList.get(position).setPlay(false);
                } else {
                    listBrowseSongList.get(position).setPlay(true);
                }

                for (int i = 0; i < listBrowseSongList.size(); i++) {
                    if (position != i)
                        listBrowseSongList.get(i).setPlay(false);
                }
                notifyDataSetChanged();
                onClickPlay.onClickPlay(position);
            }
        });

    }

    public List<SongBean> getListBrowseSongList() {
        return listBrowseSongList;
    }

    @Override
    public int getItemCount() {
        return listBrowseSongList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSongsName;
        private final ImageView ivSelected;
        private final ImageView ivPlayPause;

        public MyViewHolder(View view) {
            super(view);

            tvSongsName = (TextView) view.findViewById(R.id.tv_songs_name);
            ivSelected = (ImageView) view.findViewById(R.id.iv_select);
            ivPlayPause = (ImageView) view.findViewById(R.id.iv_play_pause);
        }
    }

    private void selectSongs(SongBean currentItem, MyViewHolder holder, int position) {

        if (currentItem.isSelect()) {
            selectedCount--;
            holder.ivSelected.setImageResource(R.drawable.ic_check_blank_circle);
            listBrowseSongList.get(position).setSelect(false);
        } else {
            if (selectedCount >= 5) {
                Toast.makeText(mContext, "Not able to Select more then 5", Toast.LENGTH_SHORT).show();
                return;
            } else {
                selectedCount++;
                holder.ivSelected.setImageResource(R.drawable.ic_check_circle);
                listBrowseSongList.get(position).setSelect(true);
            }

        }
        notifyDataSetChanged();
    }
}
