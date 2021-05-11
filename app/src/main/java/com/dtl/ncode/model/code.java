package com.dtl.ncode.model;

public class code {
    private String Code;
    private String CoverURL;
    private String Title;
    private String CodeURl;

    public code() {
    }

    public code(String code, String coverURL, String title, String codeURl) {
        Code = code;
        CoverURL = coverURL;
        Title = title;
        CodeURl = codeURl;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCoverURL() {
        return CoverURL;
    }

    public void setCoverURL(String coverURL) {
        CoverURL = coverURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCodeURL() {
        return CodeURl;
    }

    public void setCodeURL(String codeURL) {
        CodeURl = codeURL;
    }
}
