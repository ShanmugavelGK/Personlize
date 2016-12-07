package com.augusta.dev.personalize.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.augusta.dev.personalize.DBmodel.DBRouseListModel;
import com.augusta.dev.personalize.bean.SongBean;
import com.augusta.dev.personalize.utliz.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanmugavel on 02-12-2016.
 */

public class DBOperation {
    DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public DBOperation(Context mContext) {
        databaseHelper = new DatabaseHelper(mContext);
    }

    public void readDB() {
        db = databaseHelper.getReadableDatabase();
    }

    public void writeDB() {
        db = databaseHelper.getWritableDatabase();
    }

    public void closeDB() {
        databaseHelper.close();
    }

    public void insertRouseList(String name, String time, String list) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ROUSE_NAME, name);
        contentValue.put(DatabaseHelper.ROUSE_TIME, time);
        contentValue.put(DatabaseHelper.ROUSE_SONG_LIST, list);
        db.insert(DatabaseHelper.TABLE_ROUSE_LIST, null, contentValue);
    }

    public void deleteRouseList(int _id) {
        db.delete(DatabaseHelper.TABLE_ROUSE_LIST, DatabaseHelper.ROUSE_ID + "=" + _id, null);
    }

    public void deleteAllRouseList() {
        db.delete(DatabaseHelper.TABLE_ROUSE_LIST, null, null);
    }

    public List<DBRouseListModel> getAllRouseList() {
        List<DBRouseListModel> mDBRouseListModel = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ROUSE_LIST;
        Cursor cursor = db.rawQuery(selectQuery, null);
        DBRouseListModel model;
        if (cursor.moveToFirst()) {
            do {
                model = new DBRouseListModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getList(cursor.getString(3)));
                mDBRouseListModel.add(model);
            } while (cursor.moveToNext());
        }
        return mDBRouseListModel;
    }

    public int getLastId() {
        String countQuery = "SELECT  _id FROM " + DatabaseHelper.TABLE_ROUSE_LIST + " ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(cursor.getColumnIndex("_id"));
        cursor.close();
        return count;
    }

    private ArrayList<SongBean> getList(String rouseList) {
        ArrayList<SongBean> songBeanList = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(rouseList);
            JSONArray jsonArray = jsonObject.getJSONArray("songList");
            SongBean model;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                model = new SongBean(obj.getInt(Constants.SONG_ID), obj.getString(Constants.SONG_NAME), obj.getString(Constants.SONG_PATH));
                songBeanList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return songBeanList;
    }

    public int getRouseListCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_ROUSE_LIST;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
