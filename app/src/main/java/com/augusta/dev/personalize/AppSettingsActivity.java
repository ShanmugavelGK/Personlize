package com.augusta.dev.personalize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);

        init();
        listener();
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
                            (ConvertStringtoInt(back_loc_time_min.getText().toString()) - 1));
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
                            (ConvertStringtoInt(back_loc_time_min.getText().toString()) + 1));
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

        } else if(compoundButton.getId() == R.id.personalize) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_PERSONALIZE, b);

        } else if(compoundButton.getId() == R.id.location_mode) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_LOC_MODE, b);

        } else if(compoundButton.getId() == R.id.rouse_up) {
            Preference.setSharedPreferenceBoolean(AppSettingsActivity.this, Constants.ENABLE_ROUSE_UP, b);
        }
    }
}