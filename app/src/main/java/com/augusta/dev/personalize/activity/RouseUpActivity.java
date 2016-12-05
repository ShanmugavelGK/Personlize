package com.augusta.dev.personalize.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.augusta.dev.personalize.DBmodel.DBRouseListModel;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.RouseUpAdapter;
import com.augusta.dev.personalize.bean.RouseBean;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.dbhelper.DBOperation;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RouseUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RouseUpAdapter mAdapter;
    private List<RouseBean> rouseUpList = new ArrayList<>();
    private RecyclerView rcvListRouseUp;
    private FloatingActionButton fabAddRouseUp;
    private DBOperation dbOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_up);
        dbOperation = new DBOperation(this);

        findViewById();
        initToolBar();


        fabAddRouseUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(RouseUpActivity.this, RouseBrowseActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindRecyclerView();
        bindData();
    }

    private void bindData() {

        dbOperation.readDB();
        Log.e("Count1", "" + dbOperation.getRouseListCount());
        List<DBRouseListModel> mDBRouseListModel = dbOperation.getAllRouseList();
        dbOperation.closeDB();

        RouseBean mode;
        for (int i = 0; i < mDBRouseListModel.size(); i++) {
            mode = new RouseBean(mDBRouseListModel.get(i).getRouseName(), mDBRouseListModel.get(i).getRouseTime(), getSongNameList(mDBRouseListModel.get(i).getListSongBean()));
            rouseUpList.add(mode);
        }
        mAdapter.notifyDataSetChanged();
    }

    private String getSongNameList(ArrayList<SongBean> s) {
        StringBuilder songList = new StringBuilder();
        for (int i = 0; i < s.size(); i++) {
            songList.append(s.get(i).getSongName());
            songList.append(", ");
        }
        return songList.toString();
    }


    private void bindRecyclerView() {
        rouseUpList = new ArrayList<>();
        mAdapter = new RouseUpAdapter(rouseUpList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListRouseUp.setLayoutManager(mLayoutManager);
        rcvListRouseUp.setItemAnimator(new DefaultItemAnimator());
        rcvListRouseUp.setAdapter(mAdapter);
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListRouseUp = (RecyclerView) findViewById(R.id.rcv_list_rouse);

        fabAddRouseUp = (FloatingActionButton) findViewById(R.id.fab_add_rouse_up);
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
