package com.dtl.ncode.model;

import com.google.firebase.Timestamp;

public class image {
    private String imageTitle;
    private String imageUrl;
    private String id;
    private Timestamp timestamp;

    public image() {
    }

    public image(String imageTitle, String imageUrl, String id, Timestamp timestamp) {
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
