package com.augusta.dev.personalize.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augusta.dev.personalize.AddNewSettingActivity;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.RouseUpAdapter;
import com.augusta.dev.personalize.adapter.SelectedSongsAdapter;
import com.augusta.dev.personalize.bean.RouseBean;
import com.augusta.dev.personalize.bean.SelectedSongsBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RouseBrowseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView etSelectTime;
    private RecyclerView rcvListSelection;
    private SelectedSongsAdapter mAdapter;
    private List<SelectedSongsBean> selectedSongsList = new ArrayList<>();
    private ImageView ivSongsBrowse;
    private TimePickerDialog mTimePicker;
    private ImageView ivTimeChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_browse);

        findViewById();
        initToolBar();

        bindRecyclerView();
        bindData();

        ivTimeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }

        });
        ivSongsBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RouseBrowseActivity.this, BrowseSongActivity.class);
                startActivity(i);
            }
        });
    }

    private void bindData() {
        SelectedSongsBean mode;
        mode = new SelectedSongsBean("AR Songs", true);
        selectedSongsList.add(mode);
        mode = new SelectedSongsBean("Ae Dil Hai Mushkil - Title Track  Pritam , Arijit Singh Ae Dil Hai Mushkil", true);
        selectedSongsList.add(mode);
        mode = new SelectedSongsBean("Ae Dil Hai Mushkil - Title Track  Pritam , Arijit Singh Ae Dil Hai Mushkil", false);
        selectedSongsList.add(mode);
        mode = new SelectedSongsBean("Ae Dil Hai Mushkil - Title Track  Pritam , Arijit Singh Ae Dil Hai Mushkil", true);
        selectedSongsList.add(mode);
        mAdapter.notifyDataSetChanged();
    }

    private void bindRecyclerView() {
        mAdapter = new SelectedSongsAdapter(selectedSongsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcvListSelection.setLayoutManager(mLayoutManager);
        rcvListSelection.setItemAnimator(new DefaultItemAnimator());
        rcvListSelection.setAdapter(mAdapter);
    }

    private void findViewById() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etSelectTime = (TextView) findViewById(R.id.et_select_time);
        rcvListSelection = (RecyclerView) findViewById(R.id.rcv_list_rouse_selection);
        ivSongsBrowse = (ImageView) findViewById(R.id.iv_songs_browse);
        ivTimeChange = (ImageView) findViewById(R.id.iv_time_change);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.str_set_song_time);
    }

    private void showTime() {
        int mHour, mMinute;

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        mTimePicker = new TimePickerDialog(this, R.style.MyDialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String ampm = " AM";

                if (selectedHour > 12) {

                    selectedHour = selectedHour - 12;
                    ampm = " PM";
                } else if (selectedHour == 0) {
                    selectedHour = 12;
                }

                String sSelectedHour = "", sSelectedMinute = "";
                if (selectedHour <= 9) {
                    sSelectedHour = "0" + selectedHour;
                } else {
                    sSelectedHour = selectedHour + "";
                }

                if (selectedMinute <= 9) {
                    sSelectedMinute = "0" + selectedMinute;
                } else {
                    sSelectedMinute = selectedMinute + "";
                }

                etSelectTime.setText(sSelectedHour + ":" + sSelectedMinute + ampm);
            }
        }, mHour, mMinute, false);
        mTimePicker.show();
    }
}
