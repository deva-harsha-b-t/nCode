package com.dtl.ncode.model;

public class link {
    private String titleForLink;
    private String LinkURL;

    public link(String titleForLink, String linkURL) {
        this.titleForLink = titleForLink;
        LinkURL = linkURL;
    }

    public link() {
    }

    public String getTitleForLink() {
        return titleForLink;
    }

    public void setTitleForLink(String titleForLink) {
        this.titleForLink = titleForLink;
    }

    public String getLinkURL() {
        return LinkURL;
    }

    public void setLinkURL(String linkURL) {
        LinkURL = linkURL;
    }
}
