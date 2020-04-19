package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;

public class CmmSIPAPI {
    private static final String TAG = "CmmSIPAPI";

    public static boolean notifyMeetingToTurnOnOffAudio(boolean z) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.notifyMeetingToTurnOnOffAudio(z);
    }

    public static boolean enableSIPAudio(boolean z, boolean z2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.enableSIPAudio(z, z2);
    }

    @Nullable
    public static ISIPCallConfigration getSipCallConfigration() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getConfiguration();
    }

    public static boolean isToggleAudioForUnHoldPromptReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isToggleAudioForUnHoldPromptReaded();
    }

    public static void setToggleAudioForUnHoldPromptAsReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration != null) {
            sipCallConfigration.setToggleAudioForUnHoldPromptAsReaded(true);
        }
    }

    public static boolean isAudioTransferToMeetingPromptReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isAudioTransferToMeetingPromptReaded();
    }

    public static void setAudioTransferToMeetingPromptAsReaded() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration != null) {
            sipCallConfigration.setAudioTransferToMeetingPromptAsReaded(true);
        }
    }

    public static boolean isFirstTimeForSLAHold() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration == null) {
            return false;
        }
        return sipCallConfigration.isFirstTimeForSLAHold();
    }

    public static void setFirstTimeForSLAHold() {
        ISIPCallConfigration sipCallConfigration = getSipCallConfigration();
        if (sipCallConfigration != null) {
            sipCallConfigration.setFirstTimeForSLAHold(false);
        }
    }

    public static int getMeetingState() {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return 0;
        }
        return sipCallAPI.getMeetingState();
    }
}
