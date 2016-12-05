package com.augusta.dev.personalize;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

public class AppSettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat notification;
    SwitchCompat personalize;
    SwitchCompat location_mode;
    SwitchCompat rouse_up;

    TextView back_loc_time_min;

    Button minus;
    Button plus;
    //Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        //setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
        listener();
        UpdateData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void UpdateData() {

        notification.setChecked(Preference.getSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_NOTIFICATION, false));
        personalize.setChecked(Preference.getSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_PERSONALIZE, false));
        location_mode.setChecked(Preference.getSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_LOC_MODE, false));
        rouse_up.setChecked(Preference.getSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_ROUSE_UP, false));

        back_loc_time_min.setText("" + Preference.getSharedPreferenceInt(AppSettingsActivity.this, Constants.BACKGROUND_LOC_UPDATE, 0));
    }

    private void listener() {
        notification.setOnCheckedChangeListener(this);
        personalize.setOnCheckedChangeListener(this);
        location_mode.setOnCheckedChangeListener(this);
        rouse_up.setOnCheckedChangeListener(this);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConvertStringtoInt(back_loc_time_min.getText().toString()) == 1) {
                    Toast.makeText(AppSettingsActivity.this, "Sorry. Minimum value is reached", Toast.LENGTH_SHORT).show();
                } else {
                    back_loc_time_min.setText((ConvertStringtoInt(back_loc_time_min.getText().toString()) - 1) + "");
                    Preference.setSharedPreferenceInt(AppSettingsActivity.this,
                            Constants.BACKGROUND_LOC_UPDATE,
                            Integer.parseInt(back_loc_time_min.getText().toString()));
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ConvertStringtoInt(back_loc_time_min.getText().toString()) == 60) {
                    Toast.makeText(AppSettingsActivity.this, "Sorry. Maximum value is reached", Toast.LENGTH_SHORT).show();
                } else {
                    back_loc_time_min.setText((ConvertStringtoInt(back_loc_time_min.getText().toString()) + 1) + "");
                    Preference.setSharedPreferenceInt(AppSettingsActivity.this,
                            Constants.BACKGROUND_LOC_UPDATE,
                            Integer.parseInt(back_loc_time_min.getText().toString()));
                }
            }
        });
    }

    private void init() {
        notification = (SwitchCompat) findViewById(R.id.notification);
        personalize = (SwitchCompat) findViewById(R.id.personalize);
        location_mode = (SwitchCompat) findViewById(R.id.location_mode);
        rouse_up = (SwitchCompat) findViewById(R.id.rouse_up);

        back_loc_time_min = (TextView) findViewById(R.id.back_loc_time_min);

        minus = (Button) findViewById(R.id.minus);
        plus = (Button) findViewById(R.id.plus);
    }

    private int ConvertStringtoInt(String str) {

        int value = Integer.parseInt(str);
        return value;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if(compoundButton.getId() == R.id.notification) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_NOTIFICATION, b);
            if(b) {
                CommonFunction.customNotification(this);
            } else {
                NotificationManager notificationManager = (NotificationManager) this
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(Constants.NOTIFICATION_ID);
            }

        } else if(compoundButton.getId() == R.id.personalize) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_PERSONALIZE, b);

        } else if(compoundButton.getId() == R.id.location_mode) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_LOC_MODE, b);

        } else if(compoundButton.getId() == R.id.rouse_up) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_ROUSE_UP, b);
        }
    }
}