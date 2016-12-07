package com.augusta.dev.personalize.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.augusta.dev.personalize.DBmodel.DBRouseListModel;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.RouseUpAdapter;
import com.augusta.dev.personalize.dbhelper.DBOperation;
import com.augusta.dev.personalize.interfaces.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RouseUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RouseUpAdapter mAdapter;
    //private List<RouseBean> rouseUpList = new ArrayList<>();
    private RecyclerView rcvListRouseUp;
    private FloatingActionButton fabAddRouseUp;
    private DBOperation dbOperation;

    List<DBRouseListModel> mDBRouseListModel = new ArrayList<>();
    private TextView tvNoData;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_up);
        dbOperation = new DBOperation(this);

        findViewById();
        initToolBar();

        bindRecyclerView();
        bindData();

        tvInfo.setVisibility(View.GONE);

        fabAddRouseUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(RouseUpActivity.this, RouseBrowseActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void deleteData(int position) {
        int id = mAdapter.getmDBRouseListModel().get(position).getRouseId();
        mDBRouseListModel = mAdapter.getmDBRouseListModel();
        dbOperation.deleteRouseList(id);
        if (dbOperation.getRouseListCount() <= 0) {
            tvNoData.setVisibility(View.VISIBLE);
            rcvListRouseUp.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            rcvListRouseUp.setVisibility(View.VISIBLE);
        }
    }

    private void bindData() {
        if (dbOperation.getRouseListCount() <= 0) {
            tvNoData.setVisibility(View.VISIBLE);
            rcvListRouseUp.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            rcvListRouseUp.setVisibility(View.VISIBLE);
            mDBRouseListModel = dbOperation.getAllRouseList();
            mAdapter = new RouseUpAdapter(mDBRouseListModel, this);
            longPress();
            rcvListRouseUp.setAdapter(mAdapter);
        }
    }

    private void longPress() {
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onLongClick(int position) {
                deleteData(position);
                mAdapter.notifyDataSetChanged();

            }
        });
    }


    private void bindRecyclerView() {
        mAdapter = new RouseUpAdapter(mDBRouseListModel, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListRouseUp.setLayoutManager(mLayoutManager);
        rcvListRouseUp.setItemAnimator(new DefaultItemAnimator());
        longPress();
        rcvListRouseUp.setAdapter(mAdapter);
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rcvListRouseUp = (RecyclerView) findViewById(R.id.rcv_list_rouse);
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        fabAddRouseUp = (FloatingActionButton) findViewById(R.id.fab_add_rouse_up);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_rouse_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            boolean isData = data.getExtras().getBoolean("isData");
            if (isData) {
                mDBRouseListModel.clear();
                mDBRouseListModel = dbOperation.getAllRouseList();
            }
            bindData();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rouse_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_show_info:
                View v1 = findViewById(R.id.tv_info);
                expand(v1, 50);
                collapse(v1, 250);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void expand(final View v, final int dur) {
        v.measure(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)) * dur);
        v.startAnimation(a);
    }

    public static void collapse(final View v, final int dur) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)) * dur);
        v.startAnimation(a);
    }

}
