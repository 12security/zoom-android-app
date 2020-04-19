package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class ZoomContact implements Serializable {
    private static final long serialVersionUID = 1;
    private String mEmail;
    private int mFavBuddyState = 0;
    private String mFirstName;
    private String mLastName;
    private String mPicUrl;
    private String mUserID;

    public ZoomContact() {
    }

    public ZoomContact(String str, String str2, String str3, String str4, String str5, int i) {
        this.mUserID = str;
        this.mEmail = str2;
        this.mFirstName = str3;
        this.mLastName = str4;
        this.mPicUrl = str5;
        this.mFavBuddyState = i;
    }

    public String getUserID() {
        return this.mUserID;
    }

    public void setUserID(String str) {
        this.mUserID = str;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public void setEmail(String str) {
        this.mEmail = str;
    }

    public String getFirstName() {
        return this.mFirstName;
    }

    public void setFirstName(String str) {
        this.mFirstName = str;
    }

    public String getLastName() {
        return this.mLastName;
    }

    public void setLastName(String str) {
        this.mLastName = str;
    }

    public String getPicUrl() {
        return this.mPicUrl;
    }

    public void setPicUrl(String str) {
        this.mPicUrl = str;
    }

    public int getFavBuddyState() {
        return this.mFavBuddyState;
    }

    public void setFavBuddyState(int i) {
        this.mFavBuddyState = i;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[userID=");
        sb.append(getUserID());
        sb.append(", email=");
        sb.append(getEmail());
        sb.append(", firstName=");
        sb.append(getFirstName());
        sb.append(", lastName=");
        sb.append(getLastName());
        sb.append(", picUrl=");
        sb.append(getPicUrl());
        sb.append(", favBuddyState=");
        sb.append(getFavBuddyState());
        sb.append("]");
        return sb.toString();
    }
}
