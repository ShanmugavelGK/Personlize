package com.augusta.dev.personalize.application;

import android.app.Application;

import com.augusta.dev.personalize.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shanmugavel on 25-11-2016.
 */

public class FontsConfigApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
