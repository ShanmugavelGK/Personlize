package com.augusta.dev.personalize.bean;

/**
 * Created by shanmugavel on 28-11-2016.
 */

public class SelectedSongsBean {
    String songName;
    boolean isSelected;

    public SelectedSongsBean(String songName, boolean isSelected) {
        this.songName = songName;
        this.isSelected = isSelected;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
