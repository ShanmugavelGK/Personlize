package com.augusta.dev.personalize.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shanmugavel on 29-11-2016.
 */

public class SongBean implements Parcelable {
    int songId;
    String songName;
    String songPath;
    boolean select;

    boolean play = false;

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public SongBean(int songId, String songName, String songPath, boolean select) {
        this.songId = songId;
        this.songName = songName;
        this.select = select;
        this.songPath = songPath;
    }

    public SongBean(int songId, String songName, String songPath) {
        this.songId = songId;
        this.songName = songName;
        this.songPath = songPath;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }


    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(songPath);
        parcel.writeString(songName);
        parcel.writeInt(songId);

    }

    public static final Parcelable.Creator<SongBean> CREATOR = new Parcelable.Creator<SongBean>() {
        public SongBean createFromParcel(Parcel in) {
            return new SongBean(in);
        }

        public SongBean[] newArray(int size) {
            return new SongBean[size];

        }
    };

    private SongBean(Parcel in) {
        songPath = in.readString();
        songName = in.readString();
        songId = in.readInt();

    }

}
