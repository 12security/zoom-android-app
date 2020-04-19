package com.zipow.videobox.util;

import com.zipow.videobox.util.ZMPolicyDataHelper.IntQueryResult;

public class ZMSettingHelper {
    public static int getMeetingReactionSkinToneType() {
        IntQueryResult queryIntPolicy = ZMPolicyDataHelper.getInstance().queryIntPolicy(145);
        if (queryIntPolicy.isSuccess()) {
            return queryIntPolicy.getResult();
        }
        return 0;
    }

    public static void setMeetingReactionSkinToneType(int i) {
        ZMPolicyDataHelper.getInstance().setIntValue(145, i);
    }
}
