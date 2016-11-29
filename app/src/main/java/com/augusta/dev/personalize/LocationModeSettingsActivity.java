package com.augusta.dev.personalize;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.augusta.dev.personalize.adapter.SettingsAdapter;
import com.augusta.dev.personalize.bean.SettingsEntity;
import com.augusta.dev.personalize.broadcast.AlarmBroadCastReceiver;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LocationModeSettingsActivity extends AppCompatActivity {

    RecyclerView rcv_location_mode;
    SettingsAdapter mAdapter;
    List<SettingsEntity> entities=new ArrayList<>();
    FloatingActionButton fab_add_location_mode;
    private int INTENT_LOCATION_MODE=130;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_mode_settings);

        rcv_location_mode = (RecyclerView) findViewById(R.id.rcv_location_mode);
        fab_add_location_mode = (FloatingActionButton) findViewById(R.id.fab_add_location_mode);

        fab_add_location_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationModeSettingsActivity.this, AddLocationModeActivity.class);
                startActivityForResult(intent, INTENT_LOCATION_MODE);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_location_mode.setLayoutManager(mLayoutManager);
        rcv_location_mode.setItemAnimator(new DefaultItemAnimator());

        prepareMovieData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        entities = new ArrayList<>();
        prepareMovieData();
    }

    private void prepareMovieData() {

        String str_settings_json = Preference.getSharedPreferenceString(LocationModeSettingsActivity.this, Constants.LOCATION_MODES, "");

        try {
            JSONArray jsonArray = new JSONArray(str_settings_json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                entities.add(new SettingsEntity(jsonObject.getString(Constants.ADDRESS),
                        jsonObject.getString(Constants.MODE),
                        jsonObject.getDouble(Constants.LATITUDE),
                        jsonObject.getDouble(Constants.LONGITUDE)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new SettingsAdapter(entities);
        mAdapter.setLocationMode(true);
        mAdapter.setActivity(LocationModeSettingsActivity.this);
        mAdapter.setOnClick(new SettingsAdapter.OnClick() {
            @Override
            public void onClick(int position) {
            }

            @Override
            public void onDeleteClick(int position) {
                alertForDelete(position);
            }
        });
        rcv_location_mode.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void alertForDelete(final int position) {
        new AlertDialog.Builder(LocationModeSettingsActivity.this)
                .setTitle(R.string.app_name)
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        DeleteClick(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void DeleteClick(int pos) {
        String str_json_settings = Preference.getSharedPreferenceString(LocationModeSettingsActivity.this, Constants.LOCATION_MODES, "");
        if(str_json_settings.length() != 0) {
            try {
                JSONArray jsonArray = new JSONArray(str_json_settings);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    jsonArray.remove(pos);
                    entities.remove(pos);
                }

                Preference.setSharedPreferenceString(LocationModeSettingsActivity.this, Constants.LOCATION_MODES, jsonArray.toString());
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
