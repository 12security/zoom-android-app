package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZMSessionsMgr */
public class ZMSessionsMgr {
    @NonNull
    private static ZMSessionsMgr instance = new ZMSessionsMgr();
    private boolean mIsAllOfflineMsgReady = false;
    private boolean mIsUnreadyCountReady = false;
    @NonNull
    private Set<String> mOfflineMsgReadySessions = new HashSet();
    @Nullable
    private List<String> mUnreadCountReadySessions = null;

    private ZMSessionsMgr() {
    }

    @NonNull
    public static ZMSessionsMgr getInstance() {
        return instance;
    }

    public void onChatSessionUnreadCountReady(List<String> list) {
        this.mIsUnreadyCountReady = true;
        if (CollectionsUtil.isCollectionEmpty(list)) {
            this.mIsAllOfflineMsgReady = true;
        }
        this.mUnreadCountReadySessions = list;
        if (this.mUnreadCountReadySessions == null) {
            this.mUnreadCountReadySessions = new ArrayList();
        }
    }

    public void Indicate_SessionOfflineMessageFinished(String str) {
        this.mOfflineMsgReadySessions.add(str);
    }

    public void Indicate_LoginOfflineMessageFinished() {
        this.mIsAllOfflineMsgReady = true;
    }

    public void clear() {
        this.mIsUnreadyCountReady = false;
        this.mIsAllOfflineMsgReady = false;
        this.mOfflineMsgReadySessions.clear();
        this.mUnreadCountReadySessions = null;
    }

    public boolean isSessionOfflineMessageFinished(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (this.mIsAllOfflineMsgReady) {
            return true;
        }
        List<String> list = this.mUnreadCountReadySessions;
        if (list == null || list.contains(str)) {
            return this.mOfflineMsgReadySessions.contains(str);
        }
        return true;
    }

    public boolean isSessionUnreadCountReady() {
        return this.mIsUnreadyCountReady;
    }
}
