package com.augusta.dev.personalize.broadcast;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.augusta.dev.personalize.NewAppWidget;
import com.augusta.dev.personalize.PersonalizeActivity;
import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by skarthik on 11/3/2016.
 */

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (intent.getAction().equalsIgnoreCase("alarm") == true) {

            String mode = intent.getStringExtra("mode");

            String modes = Preference.getSharedPreferenceString(context, Constants.MODES, "");

            if (modes.length() != 0) {

                try {
                    JSONArray jsonArray = new JSONArray(modes);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String str_json_mode = jsonObject.getString(Constants.MODE_TYPE);

                        jsonObject.put(Constants.IS_SELECT, false);

                        if (str_json_mode.toUpperCase().equalsIgnoreCase(mode.toUpperCase()) == true) {

                            int call = jsonObject.getInt(Constants.CALL);
                            int music = jsonObject.getInt(Constants.MUSIC);
                            int alarm = jsonObject.getInt(Constants.ALARM);

                            jsonObject.put(Constants.IS_SELECT, true);

                            NewAppWidget.updateVolume(context, AudioManager.STREAM_SYSTEM, call);
                            NewAppWidget.updateVolume(context, AudioManager.STREAM_MUSIC, music);
                            NewAppWidget.updateVolume(context, AudioManager.STREAM_ALARM, alarm);

                            Toast.makeText(context, "Successfully changed to " + mode + " time " + (new Date()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    Preference.setSharedPreferenceString(context, Constants.MODES, jsonArray.toString());
                    PersonalizeActivity.customNotification(context);
                    updateWidgetManager(context);
                    context.sendBroadcast(new Intent(Constants.ONLISTUPDATE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (intent.getAction().equalsIgnoreCase("mode_location")) {
            Log.d("mode_location", "mode_location started." + new Date());
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.d("mode_location", "mode_location received."+ new Date());
                LocationUpdate(location);
            }
        }
    }

    private void LocationUpdate(Location location) {
        String sLocationModes = Preference.getSharedPreferenceString(context, Constants.LOCATION_MODES, "");

        if (sLocationModes.length() != 0) {

            try {
                JSONArray jsonArray = new JSONArray(sLocationModes);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String mode = jsonObject.getString(Constants.MODE);
                    Double lat1 = jsonObject.getDouble(Constants.LATITUDE);
                    Double long1 = jsonObject.getDouble(Constants.LONGITUDE);

                    if (CommonFunction.distance(lat1, long1, location.getLatitude(), location.getLongitude()) < 5) {

                        String modes = Preference.getSharedPreferenceString(context, Constants.MODES, "");

                        if (modes.length() != 0) {

                            try {
                                JSONArray jsonArray1 = new JSONArray(modes);
                                for (int j = 0; j < jsonArray1.length(); j++) {

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    String str_json_mode = jsonObject1.getString(Constants.MODE_TYPE);
                                    boolean mode_is_select = jsonObject1.getBoolean(Constants.IS_SELECT);

                                    jsonObject1.put(Constants.IS_SELECT, false);

                                    if (str_json_mode.toUpperCase().equalsIgnoreCase(mode.toUpperCase())) {

                                        int call = jsonObject1.getInt(Constants.CALL);
                                        int music = jsonObject1.getInt(Constants.MUSIC);
                                        int alarm = jsonObject1.getInt(Constants.ALARM);

                                        jsonObject1.put(Constants.IS_SELECT, true);

                                        if(!mode_is_select) {
                                            NewAppWidget.updateVolume(context, AudioManager.STREAM_SYSTEM, call);
                                            NewAppWidget.updateVolume(context, AudioManager.STREAM_MUSIC, music);
                                            NewAppWidget.updateVolume(context, AudioManager.STREAM_ALARM, alarm);
                                            Log.d("mode_location", "mode_location changed."+ new Date());
                                        }
                                    }
                                }

                                Preference.setSharedPreferenceString(context, Constants.MODES, jsonArray1.toString());
                                PersonalizeActivity.customNotification(context);
                                updateWidgetManager(context);
                                context.sendBroadcast(new Intent(Constants.ONLISTUPDATE));
                                Log.d("mode_location", "mode_location completed."+ new Date());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    LocationManager locationManager;

    private void updateWidgetManager(Context context) {
        Intent intent = new Intent(context, NewAppWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}
