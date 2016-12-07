package com.augusta.dev.personalize.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.augusta.dev.personalize.DBmodel.DBRouseListModel;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.activity.RouseBrowseActivity;
import com.augusta.dev.personalize.interfaces.OnItemLongClickListener;
import com.augusta.dev.personalize.interfaces.OnRemoveItem;

import java.util.List;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class RouseUpAdapter extends RecyclerView.Adapter<RouseUpAdapter.MyViewHolder> {

    //private List<RouseBean> rouseList;
    Context mContext;
    List<DBRouseListModel> mDBRouseListModel;

    OnItemLongClickListener onItemLongClickListener;
    public OnRemoveItem onRemoveItem;

    public void setOnRemoveItem(OnRemoveItem onRemoveItem) {
        this.onRemoveItem = onRemoveItem;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public RouseUpAdapter(List<DBRouseListModel> mDBRouseListModel, Context mContext) {
        this.mDBRouseListModel = mDBRouseListModel;
        this.mContext = mContext;
    }

    public void setData(List<DBRouseListModel> mDBRouseListModel) {
        this.mDBRouseListModel = mDBRouseListModel;
        notifyDataSetChanged();
    }

    public List<DBRouseListModel> getmDBRouseListModel() {
        return mDBRouseListModel;
    }
/*public RouseUpAdapter(List<RouseBean> rouseList, Context mContext) {
        this.rouseList = rouseList;
        this.mContext = mContext;
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_rouse_up_list, parent, false);

        return new RouseUpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DBRouseListModel currentItem = mDBRouseListModel.get(position);
        holder.tvTitle.setText(currentItem.getRouseName());
        holder.tvSongList.setText(currentItem.getStrSongList());
        holder.tvTime.setText(currentItem.getRouseTime());

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(mContext, RouseBrowseActivity.class);
                mContext.startActivity(intent);
            }
        });
        holder.viewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.dia_title);
                builder.setMessage(R.string.dia_content);
                builder.setPositiveButton(R.string.dia_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onItemLongClickListener.onLongClick(position);
                        mDBRouseListModel.remove(position);
                    }
                });
                builder.setNegativeButton(R.string.dia_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDBRouseListModel.size();
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
