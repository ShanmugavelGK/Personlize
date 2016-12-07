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
    String strSongList;


    ArrayList<SongBean> listSongBean;

    public DBRouseListModel(int rouseId, String rouseName, String rouseTime, String strSongList, ArrayList<SongBean> listSongBean) {
        this.rouseId = rouseId;
        this.rouseName = rouseName;
        this.rouseTime = rouseTime;
        this.strSongList = strSongList;
        this.listSongBean = listSongBean;
    }

    /*public DBRouseListModel(String strSongList, String rouseTime, String rouseName) {
        this.strSongList = strSongList;
        this.rouseTime = rouseTime;
        this.rouseName = rouseName;
    }*/

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

    public String getStrSongList() {
        return strSongList;
    }

    public void setStrSongList(String strSongList) {
        this.strSongList = strSongList;
    }
}
