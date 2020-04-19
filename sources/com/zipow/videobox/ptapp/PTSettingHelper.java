package com.zipow.videobox.ptapp;

import android.content.Context;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;
import com.zipow.videobox.util.ZmPtUtils;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class PTSettingHelper {
    private static final String TAG = "PTSettingHelper";
    private long mNativeHandle = 0;

    private native boolean alwaysMuteMicWhenJoinVoIPImpl(long j);

    private native boolean alwaysUseVoIPWhenJoinMeetingImpl(long j);

    private native boolean getShowIMMessageReminderImpl(long j);

    public static boolean getShowOfflineBuddies() {
        return true;
    }

    private native boolean isDriveModeSettingOnImpl(long j);

    private native Object isOriginalSoundChangableImpl(long j);

    private native boolean neverConfirmVideoPrivacyWhenJoinMeetingImpl(long j);

    private native void setAlwaysMuteMicWhenJoinVoIPImpl(long j, boolean z);

    private native void setAlwaysUseVoIPWhenJoinMeetingImpl(long j, boolean z);

    private native boolean setDriveModeImpl(long j, boolean z);

    private native boolean setHideNoVideoUserInWallViewImpl(long j, boolean z);

    private native void setNeverConfirmVideoPrivacyWhenJoinMeetingImpl(long j, boolean z);

    private native void setNeverStartVideoWhenJoinMeetingImpl(long j, boolean z);

    private native void setOriginalSoundChangableImpl(long j, boolean z);

    public static int getShowChatMessageReminder() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return 0;
        }
        boolean z = !settingHelper.getShowIMMessageReminder();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i = 2;
        if (zoomMessenger == null) {
            if (!z) {
                i = 5;
            }
            return i;
        }
        int blockAll_Get = zoomMessenger.blockAll_Get();
        if (blockAll_Get != 0 && blockAll_Get != 1) {
            return blockAll_Get;
        }
        if (!z) {
            i = 5;
        }
        return i;
    }

    public static String getAutoCallPhoneNumber(@Nullable Context context, String str) {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, null);
        CountryCodeItem defaultAutoCallCountryCode = ZmPtUtils.getDefaultAutoCallCountryCode(context);
        return (StringUtil.isEmptyOrNull(readStringValue) || defaultAutoCallCountryCode == null || StringUtil.isEmptyOrNull(defaultAutoCallCountryCode.countryCode)) ? str : PhoneNumberUtil.formatDisplayNumber(readStringValue, defaultAutoCallCountryCode.countryCode);
    }

    public static boolean saveShowChatMessageReminder(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.blockAll_Set(i);
    }

    public PTSettingHelper(long j) {
        this.mNativeHandle = j;
    }

    public boolean getShowIMMessageReminder() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return getShowIMMessageReminderImpl(j);
    }

    public boolean setDriveMode(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return setDriveModeImpl(j, z);
    }

    public boolean isDriveModeSettingOn() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDriveModeSettingOnImpl(j);
    }

    public void setAlwaysUseVoIPWhenJoinMeeting(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setAlwaysUseVoIPWhenJoinMeetingImpl(j, z);
        }
    }

    public boolean alwaysUseVoIPWhenJoinMeeting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return alwaysUseVoIPWhenJoinMeetingImpl(j);
    }

    public static void saveShowOfflineBuddies(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SHOW_OFFLINE_USER, z);
    }

    public static boolean getPlayAlertSound() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.PLAY_ALERT_SOUND, true);
    }

    public static void savePlayAlertSound(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.PLAY_ALERT_SOUND, z);
    }

    public static boolean getPlayAlertVibrate() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.PLAY_ALERT_VIBRATE, true);
    }

    public static void savePlayAlertVibrate(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.PLAY_ALERT_VIBRATE, z);
    }

    public void setNeverStartVideoWhenJoinMeeting(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setNeverStartVideoWhenJoinMeetingImpl(j, z);
        }
    }

    public static void SetAlwaysMuteMicWhenJoinVoIP(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.setAlwaysMuteMicWhenJoinVoIP(z);
        }
    }

    private void setAlwaysMuteMicWhenJoinVoIP(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setAlwaysMuteMicWhenJoinVoIPImpl(j, z);
        }
    }

    public static boolean AlwaysMuteMicWhenJoinVoIP() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return false;
        }
        return settingHelper.alwaysMuteMicWhenJoinVoIP();
    }

    private boolean alwaysMuteMicWhenJoinVoIP() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return alwaysMuteMicWhenJoinVoIPImpl(j);
    }

    public static void SetNeverConfirmVideoPrivacyWhenJoinMeeting(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.setNeverConfirmVideoPrivacyWhenJoinMeeting(z);
        }
    }

    public static boolean NeverConfirmVideoPrivacyWhenJoinMeeting() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return false;
        }
        return settingHelper.neverConfirmVideoPrivacyWhenJoinMeeting();
    }

    private void setNeverConfirmVideoPrivacyWhenJoinMeeting(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setNeverConfirmVideoPrivacyWhenJoinMeetingImpl(j, z);
        }
    }

    private boolean neverConfirmVideoPrivacyWhenJoinMeeting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return neverConfirmVideoPrivacyWhenJoinMeetingImpl(j);
    }

    public static void SetHideNoVideoUserInWallView(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.setHideNoVideoUserInWallView(z);
        }
    }

    private void setHideNoVideoUserInWallView(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setHideNoVideoUserInWallViewImpl(j, z);
        }
    }

    public boolean getIsKubiDeviceEnabled() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.ENABLE_KUBI_DEVICE, false);
    }

    public void saveIsKubiDeviceEnabled(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.ENABLE_KUBI_DEVICE, z);
    }

    public static boolean isImLlinkPreviewDescription() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.IM_LINK_PREVIEW_DESCRIPTION, true);
    }

    public static void saveImLlinkPreviewDescription(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_LINK_PREVIEW_DESCRIPTION, z);
    }

    public static boolean isImNotificationMessagePreview() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.IM_NOTIFICATION_MESSAGE_PREVIEW, true);
    }

    public static void saveImImNotificationMessagePreview(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_NOTIFICATION_MESSAGE_PREVIEW, z);
    }

    public static boolean canSetAutoCallMyPhone() {
        if (!PTApp.getInstance().isWebSignedOn()) {
            return false;
        }
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return false;
        }
        CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
        if (callinCountryCodes == null) {
            return false;
        }
        return !CollectionsUtil.isListEmpty(callinCountryCodes.getCallinCountryCodesList());
    }

    public static void saveAutoConnectAudio(int i) {
        PreferenceUtil.saveIntValue(PreferenceUtil.CONF_AUTO_CONNECT_AUDIO, i);
    }

    public static int getAutoConnectAudio() {
        return PreferenceUtil.readIntValue(PreferenceUtil.CONF_AUTO_CONNECT_AUDIO, 0);
    }

    public static void SetOriginalSoundChangable(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.setOriginalSoundChangable(z);
        }
    }

    private void setOriginalSoundChangable(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setOriginalSoundChangableImpl(j, z);
        }
    }

    @Nullable
    public static BooleanQueryResult IsOriginalSoundChangable() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return null;
        }
        return settingHelper.isOriginalSoundChangable();
    }

    @Nullable
    private BooleanQueryResult isOriginalSoundChangable() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        Object isOriginalSoundChangableImpl = isOriginalSoundChangableImpl(j);
        if (isOriginalSoundChangableImpl instanceof BooleanQueryResult) {
            return (BooleanQueryResult) isOriginalSoundChangableImpl;
        }
        return new BooleanQueryResult(false, false, false, false);
    }
}
