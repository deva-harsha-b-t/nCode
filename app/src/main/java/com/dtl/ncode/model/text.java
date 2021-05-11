package com.dtl.ncode.model;

public class text {
    private String title;
    private String description;

    public text() {
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

    public text(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
