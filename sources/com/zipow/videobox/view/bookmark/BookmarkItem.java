package com.zipow.videobox.view.bookmark;

import java.io.Serializable;

public class BookmarkItem implements Serializable {
    private static final long serialVersionUID = 1;
    private String itemName;
    private String itemUrl;

    public BookmarkItem() {
    }

    public BookmarkItem(String str, String str2) {
        this.itemName = str;
        this.itemUrl = str2;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String str) {
        this.itemName = str;
    }

    public String getItemUrl() {
        return this.itemUrl;
    }

    public void setItemUrl(String str) {
        this.itemUrl = str;
    }
}
