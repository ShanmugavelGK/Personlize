package com.augusta.dev.personalize.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.RouseUpAdapter;
import com.augusta.dev.personalize.bean.RouseBean;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RouseUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RouseUpAdapter mAdapter;
    private List<RouseBean> rouseUpList = new ArrayList<>();
    private RecyclerView rcvListRouseUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_up);

        findViewById();
        initToolBar();
        bindRecyclerView();
        bindData();

    }

    private void bindData() {
        RouseBean mode;
        mode = new RouseBean("Wake up", "10:10 AM", "AR Songs");
        rouseUpList.add(mode);
        mode = new RouseBean("Wake up", "11:10 AM", "Ae Dil Hai Mushkil - Title Track  Pritam , Arijit Singh Ae Dil Hai Mushkil");
        rouseUpList.add(mode);

        mAdapter.notifyDataSetChanged();
    }

    private void bindRecyclerView() {
        mAdapter = new RouseUpAdapter(rouseUpList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListRouseUp.setLayoutManager(mLayoutManager);
        rcvListRouseUp.setItemAnimator(new DefaultItemAnimator());
        rcvListRouseUp.setAdapter(mAdapter);
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListRouseUp = (RecyclerView) findViewById(R.id.rcv_list_rouse);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_rouse_up);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
