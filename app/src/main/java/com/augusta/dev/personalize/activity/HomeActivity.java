package com.augusta.dev.personalize.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.HomeAdapter;
import com.augusta.dev.personalize.bean.HomeBean;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

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
        setAdapter();
    }

    private void setAdapter() {

        prepareData();
        mAdapter = new HomeAdapter(homeList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListHome.setLayoutManager(mLayoutManager);
        rcvListHome.setItemAnimator(new DefaultItemAnimator());
        rcvListHome.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeList = new ArrayList<>();
        setAdapter();
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_personalize);
    }

    private void prepareData() {
        HomeBean model;

        if(Preference.getSharedPreferenceBoolean(this, Constants.ENABLE_PERSONALIZE, false)) {
            model = new HomeBean(1, "Personalize", "Personalize the your Mobile or Tablet", R.drawable.ic_home_personalize, true);
            homeList.add(model);
        }

        if(Preference.getSharedPreferenceBoolean(this, Constants.ENABLE_ROUSE_UP, false)) {
            model = new HomeBean(2, "Rouse Up", "Set the Wake up tone as a Favorite Ring tones", R.drawable.ic_home_rouse_up, true);
            homeList.add(model);
        }

        if(Preference.getSharedPreferenceBoolean(this, Constants.ENABLE_LOC_MODE, false)) {
            model = new HomeBean(3, "Location Mode", "Personalize the volume based on location", R.drawable.ic_home_rouse_up, true);
            homeList.add(model);
        }

        model = new HomeBean(4, "Settings", "Can be custom the app setting", R.drawable.ic_home_setting, true);
        homeList.add(model);
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
