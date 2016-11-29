package com.augusta.dev.personalize.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.HomeAdapter;
import com.augusta.dev.personalize.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rcvListHome;
    private List<HomeBean> homeList = new ArrayList<>();
    private HomeAdapter mAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById();
        initToolBar();

        mAdapter = new HomeAdapter(homeList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListHome.setLayoutManager(mLayoutManager);
        rcvListHome.setItemAnimator(new DefaultItemAnimator());
        rcvListHome.setAdapter(mAdapter);

        prepareData();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_personalize);
    }

    private void prepareData() {
        HomeBean model;

        model = new HomeBean("Personalize", "Personalize the your Mobile or Tablet", R.drawable.ic_home_personalize, true);
        homeList.add(model);

        model = new HomeBean("Rouse Up", "Set the Wake up tone as a Favorite Ring tones", R.drawable.ic_home_rouse_up, true);
        homeList.add(model);

        model = new HomeBean("Settings", "Can be custom the app setting", R.drawable.ic_home_setting, true);
        homeList.add(model);
        mAdapter.notifyDataSetChanged();
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListHome = (RecyclerView) findViewById(R.id.rcv_list_home);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
