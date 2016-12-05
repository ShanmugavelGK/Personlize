package com.augusta.dev.personalize.DBmodel;

import com.augusta.dev.personalize.bean.SongBean;

import java.util.ArrayList;

/**
 * Created by shanmugavel on 02-12-2016.
 */

public class DBRouseListModel {


    int rouseId;
    String rouseName;
    String rouseTime;
    ArrayList<SongBean> listSongBean;

    public DBRouseListModel(int rouseId, String rouseName, String rouseTime, ArrayList<SongBean> listSongBean) {
        this.rouseId = rouseId;
        this.rouseName = rouseName;
        this.rouseTime = rouseTime;
        this.listSongBean = listSongBean;
    }

    public int getRouseId() {
        return rouseId;
    }

    public void setRouseId(int rouseId) {
        this.rouseId = rouseId;
    }

    public String getRouseName() {
        return rouseName;
    }

    public void setRouseName(String rouseName) {
        this.rouseName = rouseName;
    }

    public String getRouseTime() {
        return rouseTime;
    }

    public void setRouseTime(String rouseTime) {
        this.rouseTime = rouseTime;
    }

    public ArrayList<SongBean> getListSongBean() {
        return listSongBean;
    }

    public void setListSongBean(ArrayList<SongBean> listSongBean) {
        this.listSongBean = listSongBean;
    }
}
