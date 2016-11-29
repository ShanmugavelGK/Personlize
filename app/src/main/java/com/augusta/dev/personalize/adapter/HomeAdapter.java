package com.augusta.dev.personalize.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augusta.dev.personalize.AppSettingsActivity;
import com.augusta.dev.personalize.PersonalizeActivity;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.activity.RouseUpActivity;
import com.augusta.dev.personalize.bean.HomeBean;

import java.util.List;

/**
 * Created by shanmugavel on 25-11-2016.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    //http://stacktips.com/tutorials/android/android-recyclerview-example
    private List<HomeBean> homeList;
    Context mContext;

    public HomeAdapter(List<HomeBean> homeList, Context mContext) {
        this.homeList = homeList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_home_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        HomeBean currentItem = homeList.get(position);
        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvDescription.setText(currentItem.getDescription());
        holder.imLogo.setImageResource(currentItem.getImageId());
        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(mContext, PersonalizeActivity.class);
                    mContext.startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(mContext, RouseUpActivity.class);
                    mContext.startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(mContext, AppSettingsActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final ImageView imLogo;
        private final LinearLayout viewItem;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_home_title);
            tvDescription = (TextView) view.findViewById(R.id.tv_home_description);
            imLogo = (ImageView) view.findViewById(R.id.iv_home_logo);
            viewItem = (LinearLayout) view.findViewById(R.id.view_item);
        }
    }
}
