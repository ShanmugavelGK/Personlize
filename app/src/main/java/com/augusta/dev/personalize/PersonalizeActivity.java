package com.augusta.dev.personalize;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.augusta.dev.personalize.adapter.ModeAdapter;
import com.augusta.dev.personalize.bean.ModeChildBean;
import com.augusta.dev.personalize.bean.ModeParentBean;
import com.augusta.dev.personalize.broadcast.AlarmBroadCastReceiver;
import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PersonalizeActivity extends AppCompatActivity {

    public static Activity mPersonalizeActivity;
    private ExpandableListView elvModeList;
    public ModeAdapter mModeAdapter;
    private ArrayList<ModeParentBean> mModeType;
    private ArrayList<ArrayList<ModeChildBean>> mModeItems;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalize);
        findViewById();
        mPersonalizeActivity = this;
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ONLISTUPDATE));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        alarmManager  = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmBroadCastReceiver.class);
        intent.setAction("mode_location");

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 100, intent, 0);
        int EVERY_TWO_MINUTES = 2 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                EVERY_TWO_MINUTES, alarmIntent);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dataPopulate();
            setAdapter();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!Preference.getSharedPreferenceBoolean(this, Constants.IS_FIRST, false)) {
            initDataPopulate();
        } else {
            dataPopulate();
        }

        setAdapter();
    }

    private void dataPopulate() {
        String modeTypes = Preference.getSharedPreferenceString(this, Constants.MODES, "");
        try {
            JSONArray modesArray = new JSONArray(modeTypes);
            mModeType = new ArrayList<>();

            mModeItems = new ArrayList<>();
            ArrayList<ModeChildBean> subItem;
            ModeChildBean modeChildBean;

            for (int i = 0; i < modesArray.length(); i++) {
                JSONObject obj = modesArray.getJSONObject(i);
                ModeParentBean parentBean;

                parentBean = new ModeParentBean(obj.getString(Constants.MODE_TYPE), obj.getBoolean(Constants.IS_SELECT));
                mModeType.add(parentBean);

                int alarm = obj.getInt(Constants.ALARM);
                int call = obj.getInt(Constants.CALL);
                int music = obj.getInt(Constants.MUSIC);

                subItem = new ArrayList<>();
                modeChildBean = new ModeChildBean(call, music, alarm);
                subItem.add(modeChildBean);
                mModeItems.add(i, subItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initDataPopulate() {
        mModeType = new ArrayList<>();
        ModeParentBean parentBean;

        parentBean = new ModeParentBean("Normal", true);
        mModeType.add(parentBean);
        parentBean = new ModeParentBean("Silent", false);
        mModeType.add(parentBean);
        parentBean = new ModeParentBean("Office", false);
        mModeType.add(parentBean);
        parentBean = new ModeParentBean("Meeting", false);
        mModeType.add(parentBean);
        parentBean = new ModeParentBean("Travel", false);
        mModeType.add(parentBean);

        mModeItems = new ArrayList<>();
        ArrayList<ModeChildBean> subItem;
        ModeChildBean modeChildBean;

        subItem = new ArrayList<>();
        modeChildBean = new ModeChildBean(2, 2, 2);
        subItem.add(modeChildBean);
        mModeItems.add(0, subItem);


        subItem = new ArrayList<>();
        modeChildBean = new ModeChildBean(0, 0, 0);
        subItem.add(modeChildBean);
        mModeItems.add(1, subItem);

        subItem = new ArrayList<>();
        modeChildBean = new ModeChildBean(0, 0, 0);
        subItem.add(modeChildBean);
        mModeItems.add(2, subItem);

        subItem = new ArrayList<>();
        modeChildBean = new ModeChildBean(0, 0, 0);
        subItem.add(modeChildBean);
        mModeItems.add(3, subItem);

        subItem = new ArrayList<>();
        modeChildBean = new ModeChildBean(0, 0, 0);
        subItem.add(modeChildBean);
        mModeItems.add(4, subItem);

        CommonFunction.generateModesType(this, mModeType, mModeItems);
        Preference.setSharedPreferenceBoolean(this, Constants.IS_FIRST, true);
    }

    private void setAdapter() {
        mModeAdapter = new ModeAdapter(this, mModeType, mModeItems);
        elvModeList.setAdapter(mModeAdapter);
    }

    private void findViewById() {
        elvModeList = (ExpandableListView) findViewById(R.id.elv_mode);
    }
}
