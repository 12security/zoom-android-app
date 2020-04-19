package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class FavoriteMgr {
    private static final String TAG = "FavoriteMgr";
    private long mNativeHandle = 0;

    private native boolean addFavoriteImpl(long j, List<ZoomContact> list);

    private native boolean callFavoriteImpl(long j, String str);

    private native boolean getAllDomainUserImpl(long j);

    private native int getDomainUserCountImpl(long j);

    private native boolean getDomainUsersWithFilterImpl(long j, String str, List<ZoomContact> list);

    private native boolean getFavoriteByUserIDImpl(long j, String str, ZoomContact zoomContact);

    private native int getFavoriteCountImpl(long j);

    private native boolean getFavoriteListWithFilterImpl(long j, String str, List<ZoomContact> list);

    @Nullable
    private native String getLocalPicturePathImpl(long j, String str, boolean z);

    private native boolean inviteFavoriteToMeetingImpl(long j, String[] strArr, String str, long j2);

    private native boolean refreshFavoriteListImpl(long j);

    private native boolean removeFavoriteImpl(long j, String str);

    private native boolean searchDomainUserImpl(long j, String str);

    public FavoriteMgr(long j) {
        this.mNativeHandle = j;
    }

    public int getFavoriteCount() {
        return getFavoriteCountImpl(this.mNativeHandle);
    }

    public boolean refreshFavoriteList() {
        return refreshFavoriteListImpl(this.mNativeHandle);
    }

    public boolean getFavoriteListWithFilter(@Nullable String str, @Nullable List<ZoomContact> list) {
        if (str == null) {
            str = "";
        }
        if (list == null) {
            return false;
        }
        return getFavoriteListWithFilterImpl(this.mNativeHandle, str, list);
    }

    public boolean searchDomainUser(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        return searchDomainUserImpl(this.mNativeHandle, str);
    }

    public int getDomainUserCount() {
        return getDomainUserCountImpl(this.mNativeHandle);
    }

    public boolean getDomainUsersWithFilter(@Nullable String str, @Nullable List<ZoomContact> list) {
        if (str == null) {
            str = "";
        }
        if (list == null) {
            return false;
        }
        return getDomainUsersWithFilterImpl(this.mNativeHandle, str, list);
    }

    public boolean addFavorite(@Nullable List<ZoomContact> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        return addFavoriteImpl(this.mNativeHandle, list);
    }

    public boolean removeFavorite(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return removeFavoriteImpl(this.mNativeHandle, str);
    }

    public boolean getFavoriteByUserID(String str, ZoomContact zoomContact) {
        return getFavoriteByUserIDImpl(this.mNativeHandle, str, zoomContact);
    }

    @Nullable
    public String getLocalPicturePath(String str) {
        return getLocalPicturePathImpl(this.mNativeHandle, str, false);
    }

    public boolean getAllDomainUser() {
        return getAllDomainUserImpl(this.mNativeHandle);
    }
}
