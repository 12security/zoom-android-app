package com.zipow.videobox.confapp;

import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;

public class CustomizeInfo implements Serializable {
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_RECORD = 4;
    public static final int TYPE_START_OR_JOIN_MEETING = 2;
    public static final int TYPE_START_RECORD = 3;
    public String description;
    public String language;
    public String linkText;
    public String linkUrl;
    public String title;
    public int type;

    public CustomizeInfo(String str, String str2, String str3, String str4, String str5) {
        this.language = str;
        this.title = str2;
        this.description = str3;
        this.linkUrl = str4;
        this.linkText = str5;
    }

    public CustomizeInfo() {
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public String getLinkText() {
        return this.linkText;
    }

    public boolean isEmpty() {
        return StringUtil.isEmptyOrNull(this.language) && StringUtil.isEmptyOrNull(this.title) && StringUtil.isEmptyOrNull(this.description) && StringUtil.isEmptyOrNull(this.linkUrl) && StringUtil.isEmptyOrNull(this.linkText);
    }
}
