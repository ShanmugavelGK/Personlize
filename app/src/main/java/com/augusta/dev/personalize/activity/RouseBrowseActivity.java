package com.augusta.dev.personalize.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.augusta.dev.personalize.AddNewSettingActivity;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.adapter.SelectedSongsAdapter;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.broadcast.AlarmBroadCastReceiver;
import com.augusta.dev.personalize.dbhelper.DBOperation;
import com.augusta.dev.personalize.interfaces.OnRemoveItem;
import com.augusta.dev.personalize.utliz.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.augusta.dev.personalize.utliz.Constants.ONE_DAY;

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
    DBOperation dbOperation;

    AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rouse_browse);

        alarmManager= (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        findViewById();
        initToolBar();

        bindRecyclerView();
        bindData();

        dbOperation = new DBOperation(this);
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
                JSONObject jsonObject = new JSONObject();

                if (strName.length() >= 3) {
                    if (len != 0) {
                        jsonObject = saveRouseList(strName, etSelectTime.getText().toString(), selectedSongsList);
                        intent.putExtra("isData", true);
                    } else
                        intent.putExtra("isData", false);
                } else {
                    Toast.makeText(this, "Name must be at least 3 Character.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                setResult(100, intent);
                Intent intent1 = new Intent(RouseBrowseActivity.this, AlarmBroadCastReceiver.class);
                intent1.setAction("rouse");
                intent1.putExtra("songs", jsonObject.toString());
                dbOperation.readDB();
                int id = dbOperation.getLastId();
                dbOperation.closeDB();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                SimpleDateFormat format_full = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat format_hrs = new SimpleDateFormat("HH");
                SimpleDateFormat format_mins = new SimpleDateFormat("mm");
                int hour_of_day = 0;
                int minute = 0;
                try {
                    hour_of_day = Integer.parseInt(format_hrs.format(format_full.parse(etSelectTime.getText().toString())));
                    minute = Integer.parseInt(format_mins.format(format_full.parse(etSelectTime.getText().toString())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour_of_day);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                alarmIntent = PendingIntent.getBroadcast(RouseBrowseActivity.this, id, intent1, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        ONE_DAY, alarmIntent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private JSONObject saveRouseList(String strName, String timing, List<SongBean> selectedSongsList) {

        dbOperation.writeDB();
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < selectedSongsList.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put(Constants.SONG_ID, selectedSongsList.get(i).getSongId());
                obj.put(Constants.SONG_NAME, selectedSongsList.get(i).getSongName());
                obj.put(Constants.SONG_PATH, selectedSongsList.get(i).getSongPath());
                jsonArray.put(i, obj);
            }
            jsonObject.put("songList", jsonArray);
            dbOperation.insertRouseList(strName, timing, jsonObject.toString());
            dbOperation.closeDB();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
