package com.augusta.dev.personalize.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.SelectedSongsAdapter;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.interfaces.OnRemoveItem;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RouseBrowseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView etSelectTime;
    private RecyclerView rcvListSelection;
    private SelectedSongsAdapter mAdapter;
    private List<SongBean> selectedSongsList = new ArrayList<>();
    private ImageView ivSongsBrowse;
    private TimePickerDialog mTimePicker;
    private ImageView ivTimeChange;
    private TextView tvNoSongs;

    private static final DateFormat sdf = new SimpleDateFormat("hh:mm a");
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_browse);

        findViewById();
        initToolBar();

        bindRecyclerView();
        bindData();

        Calendar cal = Calendar.getInstance();
        etSelectTime.setText("" + sdf.format(cal.getTime()));


        ivTimeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }

        });
        ivSongsBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouseBrowseActivity.this, BrowseSongActivity.class);
                startActivityForResult(intent, 2);// Activity is started with requestCode 2
            }
        });

        mAdapter.setOnRemoveItem(new OnRemoveItem() {
            @Override
            public void onRemoveItem(int position) {
                bindData();
            }
        });

    }

    private void bindData() {
        if (selectedSongsList.size() == 0) {
            tvNoSongs.setVisibility(View.VISIBLE);
            rcvListSelection.setVisibility(View.GONE);
        } else {
            tvNoSongs.setVisibility(View.GONE);
            rcvListSelection.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }

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
        tvNoSongs = (TextView) findViewById(R.id.tv_no_songs);
        rcvListSelection = (RecyclerView) findViewById(R.id.rcv_list_rouse_selection);
        ivSongsBrowse = (ImageView) findViewById(R.id.iv_songs_browse);
        ivTimeChange = (ImageView) findViewById(R.id.iv_time_change);
        etName = (EditText) findViewById(R.id.et_name);
        etName.setSelection(etName.getText().length());
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            Bundle bundle = data.getExtras();
            ArrayList<SongBean> selectedSongList = bundle.getParcelableArrayList("selected_song_list");
            if (selectedSongList != null) {
                SongBean mode;
                for (int i = 0; i < selectedSongList.size(); i++) {
                    mode = new SongBean(selectedSongList.get(i).getSongId(), selectedSongList.get(i).getSongName(), selectedSongList.get(i).getSongPath());
                    selectedSongsList.add(mode);
                }
            }
            bindData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selected_song, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_song_selected:
                String strName = etName.getText().toString();
                int len = selectedSongsList.size();
                Intent intent = new Intent(RouseBrowseActivity.this, RouseUpActivity.class);

                if (strName.length() >= 3) {
                    if (len != 0) {
                        dataSetPreference(strName, etSelectTime.getText().toString(), selectedSongsList);
                        intent.putExtra("isData", true);
                    } else
                        intent.putExtra("isData", false);
                } else {
                    Toast.makeText(this, "Name must be at least 3 Character.", Toast.LENGTH_SHORT).show();
                }

                setResult(100, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dataSetPreference(String strName, String timing, List<SongBean> selectedSongsList) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rouseName", strName);
            jsonObject.put("rouseTime", timing);
            JSONArray jsonArray = null;
            for (int i = 0; i < selectedSongsList.size(); i++) {
                jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                obj.put(Constants.SONG_ID, selectedSongsList.get(i).getSongId());
                obj.put(Constants.SONG_NAME, selectedSongsList.get(i).getSongName());
                obj.put(Constants.SONG_PATH, selectedSongsList.get(i).getSongPath());
                jsonArray.put(i, obj);
            }
            jsonObject.put("songList", jsonArray);
            Preference.setSharedPreferenceString(this, Constants.SONG_SELECT_LIST, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
