package com.zipow.videobox.util;

import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMSecureRandom;
import p021us.zoom.videomeetings.C4558R;

public class ZMConfUtil {
    public static ZoomQABuddy getZoomQABuddyByNodeId(long j) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        return qAComponent.getBuddyByNodeID(j);
    }

    public static ZoomQABuddy getZoomQABuddyByNodeIdOrJid(long j, String str) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            return null;
        }
        ZoomQABuddy buddyByNodeID = qAComponent.getBuddyByNodeID(j);
        if (buddyByNodeID == null && !StringUtil.isEmptyOrNull(str)) {
            buddyByNodeID = qAComponent.getBuddyByID(str);
        }
        return buddyByNodeID;
    }

    public static ZoomQABuddy getZoomQABuddyByJid(String str) {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null && !StringUtil.isEmptyOrNull(str)) {
            return qAComponent.getBuddyByID(str);
        }
        return null;
    }

    public static CmmAudioStatus getCmmAudioStatus(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById == null || userById.isH323User()) {
            return null;
        }
        return userById.getAudioStatusObj();
    }

    public static int getAudioImageResId(boolean z, boolean z2, long j, long j2) {
        if (z) {
            return ZMSecureRandom.nextDouble() > 0.5d ? getAudioOnResId(j) : getAudioOffResId(j);
        } else if (!z2) {
            return getAudioOffResId(j);
        } else {
            CmmUser userById = ConfMgr.getInstance().getUserById(j2);
            if (userById == null) {
                return getAudioOnResId(j);
            }
            CmmAudioStatus audioStatusObj = userById.getAudioStatusObj();
            if (audioStatusObj == null) {
                return getAudioOnResId(j);
            }
            if (audioStatusObj.getIsTalking()) {
                return getTalkingResId(j);
            }
            return getAudioOnResId(j);
        }
    }

    private static int getAudioOnResId(long j) {
        if (j == 1) {
            return C4558R.C4559drawable.zm_phone_unmuted;
        }
        return C4558R.C4559drawable.zm_audio_on;
    }

    private static int getAudioOffResId(long j) {
        if (j == 1) {
            return C4558R.C4559drawable.zm_phone_muted;
        }
        return C4558R.C4559drawable.zm_audio_off;
    }

    private static int getTalkingResId(long j) {
        if (j == 1) {
            return C4558R.anim.zm_talking_phone;
        }
        return C4558R.anim.zm_talking;
    }
}
