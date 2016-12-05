package com.augusta.dev.personalize.utliz;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.augusta.dev.personalize.R;
import com.augusta.dev.personalize.bean.ModeChildBean;
import com.augusta.dev.personalize.bean.ModeParentBean;
import com.augusta.dev.personalize.broadcast.PendingBroadCastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shanmugavel on 10/28/2016.
 */

public class CommonFunction {

    static int[] drawableSelect = new int[]{R.drawable.ic_notify_normal_select, R.drawable.ic_notify_silent_select, R.drawable.ic_notify_office_select, R.drawable.ic_notify_meeting_select, R.drawable.ic_notify_travel_select};
    static int[] drawableUnSelect = new int[]{R.drawable.ic_notify_normal_unselect, R.drawable.ic_notify_silent_unselect, R.drawable.ic_notify_office_unselect, R.drawable.ic_notify_meeting_unselect, R.drawable.ic_notify_travel_unselect};

    static int[] drawableSelectWidget = new int[]{R.drawable.ic_notify_normal_select_png, R.drawable.ic_notify_silent_select_png, R.drawable.ic_notify_office_select_png, R.drawable.ic_notify_meeting_select_png, R.drawable.ic_notify_travel_select_png};
    static int[] drawableUnSelectWidget = new int[]{R.drawable.ic_notify_normal_unselect_png, R.drawable.ic_notify_silent_unselect_png, R.drawable.ic_notify_office_unselect_png, R.drawable.ic_notify_meeting_unselect_png, R.drawable.ic_notify_travel_unselect_png};

    public static void generateModesType(Activity mActivity, ArrayList<ModeParentBean> mModeType, ArrayList<ArrayList<ModeChildBean>> mModeItems) {
        JSONArray modesArray = new JSONArray();
        for (int i = 0; i < mModeType.size(); i++) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put(Constants.MODE_TYPE, mModeType.get(i).getModeType());
                jsonObj.put(Constants.IS_SELECT, mModeType.get(i).isSelected());

                jsonObj.put(Constants.ALARM, mModeItems.get(i).get(0).getAlarmValue());
                jsonObj.put(Constants.CALL, mModeItems.get(i).get(0).getCallValue());
                jsonObj.put(Constants.MUSIC, mModeItems.get(i).get(0).getMusicValue());

                Log.e("jsonObj", jsonObj.toString());

                modesArray.put(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Preference.setSharedPreferenceString(mActivity, Constants.MODES, modesArray.toString());
    }

    public static void setVectorRemoteView(RemoteViews remoteViews, int resourceId, int position, boolean isSelect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isSelect)
                remoteViews.setTextViewCompoundDrawables(resourceId, 0, drawableSelect[position], 0, 0);
            else
                remoteViews.setTextViewCompoundDrawables(resourceId, 0, drawableUnSelect[position], 0, 0);

        } else {
            if (isSelect)
                remoteViews.setTextViewCompoundDrawables(resourceId, 0, drawableSelectWidget[position], 0, 0);
            else
                remoteViews.setTextViewCompoundDrawables(resourceId, 0, drawableUnSelectWidget[position], 0, 0);
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static void customNotification(Context mActivity) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(),
                R.layout.custom_notification);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(mActivity, 0, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        updateAppWidget(mActivity, remoteViews);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(mActivity)
                .setSmallIcon(R.drawable.ic_p_24)
                .setTicker("Notification")
                .setAutoCancel(false)
                .setContentIntent(pIntent)
                .setContent(remoteViews);

        remoteViews.setOnClickPendingIntent(R.id.normal, getPendingSelfIntent(mActivity, "normal"));
        remoteViews.setOnClickPendingIntent(R.id.silent, getPendingSelfIntent(mActivity, "silent"));
        remoteViews.setOnClickPendingIntent(R.id.office, getPendingSelfIntent(mActivity, "office"));
        remoteViews.setOnClickPendingIntent(R.id.meeting, getPendingSelfIntent(mActivity, "meeting"));
        remoteViews.setOnClickPendingIntent(R.id.travel, getPendingSelfIntent(mActivity, "travel"));

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) mActivity.getSystemService(Activity.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        notificationmanager.notify(Constants.NOTIFICATION_ID, notification);
    }

    static int[] resourceId = new int[]{R.id.normal, R.id.silent, R.id.office, R.id.meeting, R.id.travel};

    private static void updateAppWidget(Context mActivity, RemoteViews remoteViews) {

        try {

            String sJsonArray = Preference.getSharedPreferenceString(mActivity, Constants.MODES, "");

            if (!sJsonArray.equalsIgnoreCase("")) {

                JSONArray jsonArray = new JSONArray(sJsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {

                    CommonFunction.setVectorRemoteView(remoteViews, resourceId[i], i, false);
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getBoolean(Constants.IS_SELECT))
                        CommonFunction.setVectorRemoteView(remoteViews, resourceId[i], i, true);
                }
            }
        } catch (Exception exp) {
            Toast.makeText(mActivity, "Error " + exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action) {

        Intent intent = new Intent(context, PendingBroadCastReceiver.class);
        intent.setAction(action);

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
