package com.zipow.videobox.utils.meeting;

import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;

public class ZmAudioUtils {
    public static boolean isShowOriginalSoundOption() {
        if (ConfMgr.getInstance().isViewOnlyMeeting()) {
            return false;
        }
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj == null || audioObj.getAudioSessionType() != 0) {
            return false;
        }
        BooleanQueryResult isOriginalSoundChangable = audioObj.isOriginalSoundChangable();
        if (isOriginalSoundChangable == null || !isOriginalSoundChangable.isSuccess() || !isOriginalSoundChangable.getResult()) {
            return false;
        }
        BooleanQueryResult isMicKeepOriInputEnabled = audioObj.isMicKeepOriInputEnabled();
        if (isMicKeepOriInputEnabled == null || !isMicKeepOriInputEnabled.isSuccess()) {
            return true;
        }
        return !isMicKeepOriInputEnabled.isMandatory();
    }

    public static boolean isEnableOriginalSound() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        boolean z = false;
        if (audioObj == null) {
            return false;
        }
        BooleanQueryResult isMicKeepOriInputEnabled = audioObj.isMicKeepOriInputEnabled();
        if (isMicKeepOriInputEnabled != null && isMicKeepOriInputEnabled.isSuccess() && isMicKeepOriInputEnabled.getResult()) {
            z = true;
        }
        return z;
    }
}
