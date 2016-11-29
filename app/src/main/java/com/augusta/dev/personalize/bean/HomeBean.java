package com.augusta.dev.personalize.bean;

/**
 * Created by shanmugavel on 25-11-2016.
 */

public class HomeBean {
    String title;
    String description;
    int imageId;
    boolean isVisible;

    public HomeBean(String title, String description, int imageId, boolean isVisible) {
        this.title = title;
        this.description = description;
        this.imageId = imageId;
        this.isVisible = isVisible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
