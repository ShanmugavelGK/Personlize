package com.augusta.dev.personalize.bean;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class RouseBean {
    String title;
    String timing;
    String songList;

    public RouseBean(String title, String timing, String songList) {
        this.title = title;
        this.timing = timing;
        this.songList = songList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getSongList() {
        return songList;
    }

    public void setSongList(String songList) {
        this.songList = songList;
    }
}
