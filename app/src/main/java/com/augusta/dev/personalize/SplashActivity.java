package com.augusta.dev.personalize;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.augusta.dev.personalize.activity.HomeActivity;
import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Preference.getSharedPreferenceBoolean(this, "Already", false) == false) {
            Preference.setSharedPreferenceBoolean(this, "Already", true);
            Preference.setSharedPreferenceBoolean(this, Constants.ENABLE_NOTIFICATION, true);
            Preference.setSharedPreferenceBoolean(this, Constants.ENABLE_PERSONALIZE, true);
            Preference.setSharedPreferenceBoolean(this, Constants.ENABLE_ROUSE_UP, true);
            Preference.setSharedPreferenceBoolean(this, Constants.ENABLE_LOC_MODE, true);
            Preference.setSharedPreferenceInt(this, Constants.BACKGROUND_LOC_UPDATE, 5);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean resultNotification = Preference.getSharedPreferenceBoolean(SplashActivity.this, Constants.ENABLE_NOTIFICATION, false);
                if(resultNotification) {
                    CommonFunction.customNotification(SplashActivity.this);
                } else {
                    NotificationManager notificationManager = (NotificationManager) SplashActivity.this
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(Constants.NOTIFICATION_ID);
                }

                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        }).start();
    }
}
