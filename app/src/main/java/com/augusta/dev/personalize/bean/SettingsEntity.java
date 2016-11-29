package com.augusta.dev.personalize.bean;

/**
 * Created by skarthik on 11/2/2016.
 */

public class SettingsEntity
{
    String time="";
    String mode="";
    Double latitude=0.0;
    Double longitude=0.0;
    String address="";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public SettingsEntity(String address, String mode, Double latitude, Double longitude) {
        this.address = address;

        this.latitude = latitude;
        this.longitude = longitude;

        this.mode = mode;
    }

    public SettingsEntity(String time, String mode) {
        this.time = time;
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
