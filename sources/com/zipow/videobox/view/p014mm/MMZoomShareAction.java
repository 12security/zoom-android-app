package com.zipow.videobox.view.p014mm;

import android.content.Context;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomShareAction;
import java.io.Serializable;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMZoomShareAction */
public class MMZoomShareAction implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean mIsGroup;
    private boolean mIsMUC;
    private boolean mIsToMe;
    private long shareTime;
    private String sharee;
    private String webFileID;

    @Nullable
    public static MMZoomShareAction createWithZoomShareAction(@Nullable ZoomShareAction zoomShareAction) {
        if (zoomShareAction == null) {
            return null;
        }
        MMZoomShareAction mMZoomShareAction = new MMZoomShareAction();
        mMZoomShareAction.setSharee(zoomShareAction.getSharee());
        mMZoomShareAction.setShareTime(zoomShareAction.getShareTime());
        mMZoomShareAction.setWebFileID(zoomShareAction.getWebFileID());
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(zoomShareAction.getSharee());
        if (groupById != null) {
            mMZoomShareAction.setIsGroup(true);
            mMZoomShareAction.setIsMUC(!groupById.isRoom());
        }
        return mMZoomShareAction;
    }

    public long getShareTime() {
        return this.shareTime;
    }

    public void setShareTime(long j) {
        this.shareTime = j;
    }

    public String getSharee() {
        return this.sharee;
    }

    @Nullable
    public String getShareeName(@Nullable Context context) {
        if (StringUtil.isEmptyOrNull(this.sharee) || context == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.sharee);
        if (buddyWithJID != null) {
            return buddyWithJID.getScreenName();
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.sharee);
        if (groupById != null) {
            return groupById.getGroupDisplayName(context);
        }
        return null;
    }

    public boolean isBlockedByIB(@Nullable Context context) {
        if (StringUtil.isEmptyOrNull(this.sharee) || context == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.sharee);
        if (buddyWithJID != null) {
            return buddyWithJID.isIMBlockedByIB();
        }
        return false;
    }

    public boolean isBuddy(@Nullable Context context) {
        if (StringUtil.isEmptyOrNull(this.sharee) || context == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.getBuddyWithJID(this.sharee) == null) {
            return false;
        }
        return true;
    }

    public boolean isGroupAdmin(@Nullable Context context) {
        boolean z = false;
        if (StringUtil.isEmptyOrNull(this.sharee) || context == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ZoomGroup groupById = zoomMessenger.getGroupById(this.sharee);
        if (groupById == null) {
            return false;
        }
        boolean z2 = groupById.isForceE2EGroup() || zoomMessenger.e2eGetMyOption() == 2;
        if (groupById.isGroupOperatorable() && !z2) {
            z = true;
        }
        return z;
    }

    public void setSharee(String str) {
        this.sharee = str;
    }

    public String getWebFileID() {
        return this.webFileID;
    }

    public void setWebFileID(String str) {
        this.webFileID = str;
    }

    public boolean isGroup() {
        return this.mIsGroup;
    }

    public void setIsGroup(boolean z) {
        this.mIsGroup = z;
    }

    public boolean isMUC() {
        return this.mIsMUC;
    }

    public void setIsMUC(boolean z) {
        this.mIsMUC = z;
    }

    public boolean isToMe() {
        return this.mIsToMe;
    }

    public void setIsToMe(boolean z) {
        this.mIsToMe = z;
    }
}
