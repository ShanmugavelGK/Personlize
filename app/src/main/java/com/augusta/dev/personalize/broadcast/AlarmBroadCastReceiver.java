package com.augusta.dev.personalize.broadcast;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.augusta.dev.personalize.NewAppWidget;
import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by skarthik on 11/3/2016.
 */

public class AlarmBroadCastReceiver extends BroadcastReceiver implements MediaPlayer.OnCompletionListener {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if(intent.getAction().equalsIgnoreCase("stop") == true) {

            if(mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            } else {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(intent.getStringExtra("path")));
                mediaPlayer.stop();
            }

        } else if (intent.getAction().equalsIgnoreCase("alarm") == true) {

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
                    if(Preference.getSharedPreferenceBoolean(context, Constants.ENABLE_NOTIFICATION, false)) {
                        CommonFunction.customNotification(context);
                    }
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
        } else if(intent.getAction().equalsIgnoreCase("rouse")) {

            String songs = intent.getStringExtra("songs");

            if (songs.length() != 0) {

                try {
                    JSONObject jsonObject1 = new JSONObject(songs);
                    JSONArray jsonArray = jsonObject1.getJSONArray("songList");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String song_path = jsonObject.getString(Constants.SONG_PATH);
                        strings.add(song_path);
                    }

                    PlaySongs(context);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ArrayList<String> strings = new ArrayList<>();
    int currentTrack = 0;
    public static MediaPlayer mediaPlayer;

    private void PlaySongs(Context context) {

        Uri uri = Uri.fromFile(new File(strings.get(currentTrack)));
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        removeNotify(context);
        addNotification(context, uri);
    }

    private void addNotification(Context context, Uri uri) {
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Intent intent = new Intent(context, AlarmBroadCastReceiver.class);
        intent.setAction("stop");
        intent.putExtra("path", uri.getPath());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setDeleteIntent(pendingIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFY_SONG, builder.build());
    }

    public static int NOTIFY_SONG = 12;

    public void removeNotify(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFY_SONG);
        } catch (Exception exp) {

        }
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        mediaPlayer = arg0;
        mediaPlayer.release();
        removeNotify(context);

        if (currentTrack < strings.size()) {
            currentTrack++;
            Uri uri = Uri.fromFile(new File(strings.get(currentTrack)));
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
            addNotification(context, uri);
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
                                if(Preference.getSharedPreferenceBoolean(context, Constants.ENABLE_NOTIFICATION, false)) {
                                    CommonFunction.customNotification(context);
                                }
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
