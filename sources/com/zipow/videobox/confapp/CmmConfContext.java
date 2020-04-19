package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfAppProtos.VanityURLInfoList;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.RealNameAuthCountryCodes;
import com.zipow.videobox.ptapp.MeetingInfoProtos.UserPhoneInfoList;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.ParamsList;

public class CmmConfContext {
    private static final String TAG = "CmmConfContext";
    private long mNativeHandle = 0;

    private native boolean amIGuestImpl(long j);

    private native boolean canActAsCCEditorImpl(long j);

    private native boolean canCopyChatContentImpl(long j);

    private native boolean canUpgradeThisFreeMeetingImpl(long j);

    @Nullable
    private native String get1On1BuddyLocalPicImpl(long j);

    @Nullable
    private native String get1On1BuddyPhoneNumberImpl(long j);

    @Nullable
    private native String get1On1BuddyScreeNameImpl(long j);

    @Nullable
    private native String getAppContextStringImpl(long j);

    private native int getBOJoinReasonCodeImpl(long j);

    @Nullable
    private native String getBONameImpl(long j);

    private native String getChinaMeetingPrivacyUrlImpl(long j);

    @Nullable
    private native String getClosedCaptionGatewayURLImpl(long j);

    private native long getConfNumberImpl(long j);

    private native long getConfOptionImpl(long j);

    @Nullable
    private native String getConfidentialWaterMarkerImpl(long j);

    private native int getDisableRecvVideoReasonImpl(long j);

    private native int getDisableSendVideoReasonImpl(long j);

    private native int getGiftMeetingCountImpl(long j);

    @Nullable
    private native String getGiftUpgradeUrlImpl(long j);

    @Nullable
    private native String getH323ConfInfoImpl(long j);

    @Nullable
    private native String getH323PasswordImpl(long j);

    private native CustomizeInfo getJoinMeetingDisclaimerImpl(long j);

    private native int getLaunchReasonImpl(long j);

    @Nullable
    private native String getMeetingIdImpl(long j);

    @Nullable
    private native byte[] getMeetingItemProtoData(long j);

    @Nullable
    private native String getMeetingPasswordImpl(long j);

    @Nullable
    private native byte[] getMultiVanityURLsImpl(long j);

    @Nullable
    private native String getMyBigPicLocalImpl(long j);

    @Nullable
    private native String getMyCredentialImpl(long j);

    @Nullable
    private native String getMyEmailImpl(long j);

    private native int getMyRoleImpl(long j);

    @Nullable
    private native String getMyScreenNameImpl(long j);

    @Nullable
    private native String getMyUserIdImpl(long j);

    private native boolean getOldMuteMyselfFlagImpl(long j, long j2);

    private native boolean getOriginalHostImpl(long j);

    private native int getParticipantLimitImpl(long j);

    @Nullable
    private native String getPhoneCallInNumberImpl(long j);

    @Nullable
    private native String getPrivacyUrlImpl(long j);

    @Nullable
    private native String getRawMeetingPasswordImpl(long j);

    @Nullable
    private native byte[] getRealNameAuthCountryCodesImpl(long j);

    @Nullable
    private native String getRealNameAuthPrivacyURLImpl(long j);

    @Nullable
    private native String getRecordingManagementURLImpl(long j);

    private native CustomizeInfo getRecordingReminderCustomizeInfoImpl(long j);

    private native CustomizeInfo getStartRecordingDisclaimerImpl(long j);

    @Nullable
    private native String getTeleConfURLImpl(long j);

    @Nullable
    private native String getToSUrlImpl(long j);

    @Nullable
    private native String getTollFreeCallInNumberImpl(long j);

    private native long getUserOption2Impl(long j);

    @Nullable
    private native byte[] getUserPhoneInfosImpl(long j);

    @Nullable
    private native String getVanityMeetingIDImpl(long j);

    private native boolean hasZoomIMImpl(long j);

    private native boolean inSilentModeImpl(long j);

    private native boolean isAllowAttendeeAnswerQuestionChangableImpl(long j);

    private native boolean isAllowAttendeeUpvoteQuestionChangableImpl(long j);

    private native boolean isAllowAttendeeViewAllQuestionChangableImpl(long j);

    private native boolean isAllowParticipantRenameEnabledImpl(long j);

    private native boolean isAllowUserRejoinAfterRemoveImpl(long j);

    private native boolean isAnnoationOffImpl(long j);

    private native boolean isAnonymousQAChangableImpl(long j);

    private native boolean isAudioOnlyMeetingImpl(long j);

    private native boolean isAudioWatermarkEnabledImpl(long j);

    private native boolean isAuthLocalRecordDisabledImpl(long j);

    private native boolean isAutoCMRForbidManualStopImpl(long j);

    private native boolean isAutoShowJoinAudioDialogEnabledImpl(long j);

    private native boolean isBAASecurityMeetingImpl(long j);

    private native boolean isBindTelephoneUserEnableImpl(long j);

    private native boolean isCMRStorageFullImpl(long j);

    private native boolean isCallImpl(long j);

    private native boolean isChatOffImpl(long j);

    private native boolean isClosedCaptionOnImpl(long j);

    private native boolean isConfUserLoginImpl(long j);

    private native boolean isDirectShareClientImpl(long j);

    private native boolean isDirectStartImpl(long j);

    private native boolean isE2EMeetingImpl(long j);

    private native boolean isEmojiReactionEnabledImpl(long j);

    private native boolean isExternalMeetingImpl(long j);

    private native boolean isFeedbackEnableImpl(long j);

    private native boolean isHighlightGuestFeatureEnabledImpl(long j);

    private native boolean isInVideoCompanionModeImpl(long j);

    private native boolean isInstantMeetingImpl(long j);

    private native boolean isInternalMeetingtImpl(long j);

    private native boolean isKubiEnabledImpl(long j);

    private native boolean isLiveTranscriptionFeatureOnImpl(long j);

    private native boolean isLocalRecordDisabledImpl(long j);

    private native boolean isMMRSupportViewOnlyClientImpl(long j);

    private native boolean isMMRSupportWaitingRoomMsgImpl(long j);

    private native boolean isMeetingSupportCameraControlImpl(long j);

    private native boolean isMeetingSupportSilentModeImpl(long j);

    private native boolean isMessageAndFeedbackNotifyEnabledImpl(long j);

    private native boolean isOneOnOneImpl(long j);

    private native boolean isPSTNPassWordProtectionOnImpl(long j);

    private native boolean isPTLoginImpl(long j);

    private native boolean isPhoneCallImpl(long j);

    private native boolean isPracticeSessionFeatureOnImpl(long j);

    private native boolean isPrivateChatOFFImpl(long j);

    private native boolean isQANDAOFFImpl(long j);

    private native boolean isRecordDisabledImpl(long j);

    private native boolean isScreenShareDisabledImpl(long j);

    private native boolean isShareBoxComOFFImpl(long j);

    private native boolean isShareDesktopDisabledImpl(long j);

    private native boolean isShareDropBoxOFFImpl(long j);

    private native boolean isShareGoogleDriveOFFImpl(long j);

    private native boolean isShareOneDriveOFFImpl(long j);

    private native boolean isShareOnlyMeetingImpl(long j);

    private native boolean isShareSettingTypeLockedImpl(long j);

    private native boolean isStartVideoDisabledIml(long j);

    private native boolean isSupportConfidentialWaterMarkerImpl(long j);

    private native boolean isTspEnabledImpl(long j);

    private native boolean isUnbindTelephoneUserEnableImpl(long j);

    private native boolean isVideoOnImpl(long j);

    private native boolean isWBFeatureOFFImpl(long j);

    private native boolean isWebinarImpl(long j);

    private native boolean needConfirmGDPRImpl(long j);

    private native boolean needConfirmVideoPrivacyWhenJoinMeetingImpl(long j);

    private native boolean needPromptChinaMeetingPrivacyImpl(long j);

    private native boolean needPromptGuestParticipantLoginWhenJoinImpl(long j);

    private native boolean needPromptJoinMeetingDisclaimerImpl(long j);

    private native boolean needPromptLoginWhenJoinImpl(long j);

    private native boolean needPromptStartRecordingDisclaimerImpl(long j);

    private native boolean needRemindLoginWhenInWaitingRoomImpl(long j);

    private native boolean needShowPresenterNameToWaterMarkmpl(long j);

    private native boolean needUserConfirmToJoinOrStartMeetingImpl(long j);

    private native boolean notSupportTelephonyImpl(long j);

    private native boolean notSupportVoIPImpl(long j);

    private native int querySessionNetworkStatusImpl(long j, int i, boolean z);

    private native void setAppContextStringImpl(long j, String str);

    private native boolean supportPutUserinWaitingListUponEntryFeatureImpl(long j);

    public CmmConfContext(long j) {
        this.mNativeHandle = j;
    }

    public int getLaunchReason() {
        return getLaunchReasonImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyUserId() {
        return getMyUserIdImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyScreenName() {
        return getMyScreenNameImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMeetingId() {
        return getMeetingIdImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMeetingPassword() {
        return getMeetingPasswordImpl(this.mNativeHandle);
    }

    @Nullable
    public String getRawMeetingPassword() {
        return getRawMeetingPasswordImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyEmail() {
        return getMyEmailImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyBigPicLocal() {
        return getMyBigPicLocalImpl(this.mNativeHandle);
    }

    public long getConfNumber() {
        return getConfNumberImpl(this.mNativeHandle);
    }

    public boolean getOrginalHost() {
        return getOriginalHostImpl(this.mNativeHandle);
    }

    @Nullable
    public String get1On1BuddyScreeName() {
        return get1On1BuddyScreeNameImpl(this.mNativeHandle);
    }

    @Nullable
    public String get1On1BuddyLocalPic() {
        return get1On1BuddyLocalPicImpl(this.mNativeHandle);
    }

    public boolean isOneOnOne() {
        return isOneOnOneImpl(this.mNativeHandle);
    }

    public boolean isE2EMeeting() {
        return isE2EMeetingImpl(this.mNativeHandle);
    }

    public boolean isVideoOn() {
        return isVideoOnImpl(this.mNativeHandle);
    }

    public boolean isChatOff() {
        return isChatOffImpl(this.mNativeHandle);
    }

    public boolean isRecordDisabled() {
        return isRecordDisabledImpl(this.mNativeHandle);
    }

    public boolean isPrivateChatOFF() {
        return isPrivateChatOFFImpl(this.mNativeHandle);
    }

    public boolean isQANDAOFF() {
        return isQANDAOFFImpl(this.mNativeHandle);
    }

    public boolean isDirectStart() {
        return isDirectStartImpl(this.mNativeHandle);
    }

    @Nullable
    public MeetingInfoProto getMeetingItem() {
        byte[] meetingItemProtoData = getMeetingItemProtoData(this.mNativeHandle);
        MeetingInfoProto meetingInfoProto = null;
        if (meetingItemProtoData == null || meetingItemProtoData.length == 0) {
            return null;
        }
        try {
            meetingInfoProto = MeetingInfoProto.parseFrom(meetingItemProtoData);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingInfoProto;
    }

    public boolean isShareOnlyMeeting() {
        return isShareOnlyMeetingImpl(this.mNativeHandle);
    }

    public boolean isAudioOnlyMeeting() {
        return isAudioOnlyMeetingImpl(this.mNativeHandle);
    }

    public boolean isCall() {
        return isCallImpl(this.mNativeHandle);
    }

    public boolean isPhoneCall() {
        return isPhoneCallImpl(this.mNativeHandle);
    }

    @Nullable
    public String getPhoneCallInNumber() {
        return getPhoneCallInNumberImpl(this.mNativeHandle);
    }

    @Nullable
    public String getTollFreeCallInNumber() {
        return getTollFreeCallInNumberImpl(this.mNativeHandle);
    }

    @Nullable
    public String getH323ConfInfo() {
        return getH323ConfInfoImpl(this.mNativeHandle);
    }

    @Nullable
    public String getH323Password() {
        return getH323PasswordImpl(this.mNativeHandle);
    }

    @Nullable
    public String get1On1BuddyPhoneNumber() {
        return get1On1BuddyPhoneNumberImpl(this.mNativeHandle);
    }

    private void setAppContextString(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        setAppContextStringImpl(this.mNativeHandle, str);
    }

    @Nullable
    private String getAppContextString() {
        return getAppContextStringImpl(this.mNativeHandle);
    }

    public void setAppContextParams(@Nullable ParamsList paramsList) {
        setAppContextString(paramsList != null ? paramsList.serializeToString() : null);
    }

    public ParamsList getAppContextParams() {
        return ParamsList.parseFromString(getAppContextString());
    }

    @Nullable
    public String getTeleConfURL() {
        return getTeleConfURLImpl(this.mNativeHandle);
    }

    public boolean isWebinar() {
        return isWebinarImpl(this.mNativeHandle);
    }

    public int getParticipantLimit() {
        return getParticipantLimitImpl(this.mNativeHandle);
    }

    public boolean isMeetingSupportSilentMode() {
        return isMeetingSupportSilentModeImpl(this.mNativeHandle);
    }

    public boolean inSilentMode() {
        return inSilentModeImpl(this.mNativeHandle);
    }

    public boolean hasZoomIM() {
        return hasZoomIMImpl(this.mNativeHandle);
    }

    public boolean isInstantMeeting() {
        return isInstantMeetingImpl(this.mNativeHandle);
    }

    public boolean isLocalRecordDisabled() {
        return isLocalRecordDisabledImpl(this.mNativeHandle);
    }

    public boolean isScreenShareDisabled() {
        return isScreenShareDisabledImpl(this.mNativeHandle);
    }

    public boolean isShareDesktopDisabled() {
        return isShareDesktopDisabledImpl(this.mNativeHandle);
    }

    public boolean isShareBoxComOFF() {
        return isShareBoxComOFFImpl(this.mNativeHandle);
    }

    public boolean isShareGoogleDriveOFF() {
        return isShareGoogleDriveOFFImpl(this.mNativeHandle);
    }

    public boolean isShareDropBoxOFF() {
        return isShareDropBoxOFFImpl(this.mNativeHandle);
    }

    public boolean isShareOneDriveOFF() {
        return isShareOneDriveOFFImpl(this.mNativeHandle);
    }

    public boolean isBindTelephoneUserEnable() {
        return isBindTelephoneUserEnableImpl(this.mNativeHandle);
    }

    public boolean isUnbindTelephoneUserEnable() {
        return isUnbindTelephoneUserEnableImpl(this.mNativeHandle);
    }

    public boolean isDirectShareClient() {
        return isDirectShareClientImpl(this.mNativeHandle);
    }

    public boolean isBAASecurityMeeting() {
        return isBAASecurityMeetingImpl(this.mNativeHandle);
    }

    public boolean isStartVideoDisabled() {
        return isStartVideoDisabledIml(this.mNativeHandle);
    }

    public boolean isKubiEnabled() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        boolean isSDKMode = instance != null ? instance.isSDKMode() : false;
        if ((isKubiEnabledImpl(this.mNativeHandle) || isSDKMode) && getIsKubiDeviceEnabledInLocalSettings()) {
            return true;
        }
        return false;
    }

    public boolean isMeetingSupportCameraControl() {
        return isMeetingSupportCameraControlImpl(this.mNativeHandle);
    }

    private boolean getIsKubiDeviceEnabledInLocalSettings() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.ENABLE_KUBI_DEVICE, false);
    }

    public boolean isAlwaysShowMeetingToolbar() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.ALWAYS_SHOW_MEETING_TOOLBAR, false);
    }

    public boolean isPracticeSessionFeatureOn() {
        return isPracticeSessionFeatureOnImpl(this.mNativeHandle);
    }

    @Nullable
    public String getVanityMeetingID() {
        return getVanityMeetingIDImpl(this.mNativeHandle);
    }

    public boolean isSupportConfidentialWaterMarker() {
        return isSupportConfidentialWaterMarkerImpl(this.mNativeHandle);
    }

    @Nullable
    public String getConfidentialWaterMarker() {
        return getConfidentialWaterMarkerImpl(this.mNativeHandle);
    }

    public boolean isPSTNPassWordProtectionOn() {
        return isPSTNPassWordProtectionOnImpl(this.mNativeHandle);
    }

    public int getBOJoinReason() {
        return getBOJoinReasonCodeImpl(this.mNativeHandle);
    }

    @Nullable
    public String getBOName() {
        return getBONameImpl(this.mNativeHandle);
    }

    public boolean isAnnoationOff() {
        return isAnnoationOffImpl(this.mNativeHandle);
    }

    public long getUserOption2() {
        return getUserOption2Impl(this.mNativeHandle);
    }

    public boolean isTspEnabled() {
        return isTspEnabledImpl(this.mNativeHandle);
    }

    public boolean notSupportTelephony() {
        return notSupportTelephonyImpl(this.mNativeHandle);
    }

    public boolean notSupportVoIP() {
        return notSupportVoIPImpl(this.mNativeHandle);
    }

    public boolean needPromptLoginWhenJoin() {
        return needPromptLoginWhenJoinImpl(this.mNativeHandle);
    }

    public boolean isConfUserLogin() {
        return isConfUserLoginImpl(this.mNativeHandle);
    }

    public boolean supportPutUserinWaitingListUponEntryFeature() {
        return supportPutUserinWaitingListUponEntryFeatureImpl(this.mNativeHandle);
    }

    public boolean isFeedbackEnable() {
        return isFeedbackEnableImpl(this.mNativeHandle);
    }

    public boolean canUpgradeThisFreeMeeting() {
        return canUpgradeThisFreeMeetingImpl(this.mNativeHandle);
    }

    public boolean isCMRStorageFull() {
        return isCMRStorageFullImpl(this.mNativeHandle);
    }

    @Nullable
    public String getRecordingManagementURL() {
        return getRecordingManagementURLImpl(this.mNativeHandle);
    }

    public int getMyRole() {
        return getMyRoleImpl(this.mNativeHandle);
    }

    public boolean canActAsCCEditor() {
        return canActAsCCEditorImpl(this.mNativeHandle);
    }

    public boolean isClosedCaptionOn() {
        return isClosedCaptionOnImpl(this.mNativeHandle);
    }

    public boolean isLiveTranscriptionFeatureOn() {
        return isLiveTranscriptionFeatureOnImpl(this.mNativeHandle);
    }

    @Nullable
    public String getClosedCaptionGatewayURL() {
        return getClosedCaptionGatewayURLImpl(this.mNativeHandle);
    }

    public boolean isMMRSupportViewOnlyClient() {
        return isMMRSupportViewOnlyClientImpl(this.mNativeHandle);
    }

    public long getConfOption() {
        return getConfOptionImpl(this.mNativeHandle);
    }

    public boolean getOldMuteMyselfFlag(long j) {
        return getOldMuteMyselfFlagImpl(this.mNativeHandle, j);
    }

    public boolean isAnonymousQAChangable() {
        return isAnonymousQAChangableImpl(this.mNativeHandle);
    }

    public boolean isHighlightGuestFeatureEnabled() {
        return isHighlightGuestFeatureEnabledImpl(this.mNativeHandle);
    }

    public boolean amIGuest() {
        return amIGuestImpl(this.mNativeHandle);
    }

    public boolean isExternalMeeting() {
        return isExternalMeetingImpl(this.mNativeHandle);
    }

    public boolean isInternalMeeting() {
        return isInternalMeetingtImpl(this.mNativeHandle);
    }

    public boolean needShowPresenterNameToWaterMark() {
        return needShowPresenterNameToWaterMarkmpl(this.mNativeHandle);
    }

    public int querySessionNetworkStatus(int i, boolean z) {
        return querySessionNetworkStatusImpl(this.mNativeHandle, i, z);
    }

    public boolean isAutoCMRForbidManualStop() {
        return isAutoCMRForbidManualStopImpl(this.mNativeHandle);
    }

    public boolean needUserConfirmToJoinOrStartMeeting() {
        return needUserConfirmToJoinOrStartMeetingImpl(this.mNativeHandle);
    }

    public boolean needConfirmGDPR() {
        return needConfirmGDPRImpl(this.mNativeHandle);
    }

    public boolean needPromptChinaMeetingPrivacy() {
        return needPromptChinaMeetingPrivacyImpl(this.mNativeHandle);
    }

    public String getChinaMeetingPrivacyUrl() {
        return getChinaMeetingPrivacyUrlImpl(this.mNativeHandle);
    }

    public boolean isEmojiReactionEnabled() {
        return isEmojiReactionEnabledImpl(this.mNativeHandle);
    }

    @Nullable
    public String getPrivacyUrl() {
        return getPrivacyUrlImpl(this.mNativeHandle);
    }

    @Nullable
    public String getToSUrl() {
        return getToSUrlImpl(this.mNativeHandle);
    }

    public boolean isAllowAttendeeViewAllQuestionChangable() {
        return isAllowAttendeeViewAllQuestionChangableImpl(this.mNativeHandle);
    }

    public boolean isAllowAttendeeUpvoteQuestionChangable() {
        return isAllowAttendeeUpvoteQuestionChangableImpl(this.mNativeHandle);
    }

    public boolean isAllowAttendeeAnswerQuestionChangable() {
        return isAllowAttendeeAnswerQuestionChangableImpl(this.mNativeHandle);
    }

    public boolean isPTLogin() {
        return isPTLoginImpl(this.mNativeHandle);
    }

    @Nullable
    public String getUpgradeUrl() {
        return getGiftUpgradeUrlImpl(this.mNativeHandle);
    }

    public int getGiftMeetingCount() {
        return getGiftMeetingCountImpl(this.mNativeHandle);
    }

    public boolean isAllowUserRejoinAfterRemove() {
        return isAllowUserRejoinAfterRemoveImpl(this.mNativeHandle);
    }

    public boolean isAudioWatermarkEnabled() {
        return isAudioWatermarkEnabledImpl(this.mNativeHandle);
    }

    public boolean isAutoShowJoinAudioDialogEnabled() {
        return isAutoShowJoinAudioDialogEnabledImpl(this.mNativeHandle);
    }

    public boolean isAllowParticipantRenameEnabled() {
        return isAllowParticipantRenameEnabledImpl(this.mNativeHandle);
    }

    public boolean isMMRSupportWaitingRoomMsg() {
        return isMMRSupportWaitingRoomMsgImpl(this.mNativeHandle);
    }

    public boolean canCopyChatContent() {
        return canCopyChatContentImpl(this.mNativeHandle);
    }

    public boolean isMessageAndFeedbackNotifyEnabled() {
        return isMessageAndFeedbackNotifyEnabledImpl(this.mNativeHandle);
    }

    public boolean needPromptJoinMeetingDisclaimer() {
        return needPromptJoinMeetingDisclaimerImpl(this.mNativeHandle);
    }

    @Nullable
    public CustomizeInfo getJoinMeetingDisclaimer() {
        return getJoinMeetingDisclaimerImpl(this.mNativeHandle);
    }

    public boolean needPromptStartRecordingDisclaimer() {
        return needPromptStartRecordingDisclaimerImpl(this.mNativeHandle);
    }

    @Nullable
    public CustomizeInfo getStartRecordingDisclaimer() {
        return getStartRecordingDisclaimerImpl(this.mNativeHandle);
    }

    public boolean needConfirmVideoPrivacyWhenJoinMeeting() {
        return needConfirmVideoPrivacyWhenJoinMeetingImpl(this.mNativeHandle);
    }

    public boolean needRemindLoginWhenInWaitingRoom() {
        return needRemindLoginWhenInWaitingRoomImpl(this.mNativeHandle);
    }

    public boolean needPromptGuestParticipantLoginWhenJoin() {
        return needPromptGuestParticipantLoginWhenJoinImpl(this.mNativeHandle);
    }

    public boolean isWBFeatureOFF() {
        return isWBFeatureOFFImpl(this.mNativeHandle);
    }

    public int getDisableSendVideoReason() {
        return getDisableSendVideoReasonImpl(this.mNativeHandle);
    }

    public int getDisableRecvVideoReason() {
        return getDisableRecvVideoReasonImpl(this.mNativeHandle);
    }

    public boolean isShareSettingTypeLocked() {
        return isShareSettingTypeLockedImpl(this.mNativeHandle);
    }

    public boolean isInVideoCompanionMode() {
        return isInVideoCompanionModeImpl(this.mNativeHandle);
    }

    @Nullable
    public UserPhoneInfoList getUserPhoneInfos() {
        byte[] userPhoneInfosImpl = getUserPhoneInfosImpl(this.mNativeHandle);
        UserPhoneInfoList userPhoneInfoList = null;
        if (userPhoneInfosImpl == null || userPhoneInfosImpl.length == 0) {
            return null;
        }
        try {
            userPhoneInfoList = UserPhoneInfoList.parseFrom(userPhoneInfosImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return userPhoneInfoList;
    }

    @Nullable
    public RealNameAuthCountryCodes getRealNameAuthCountryCodes() {
        byte[] realNameAuthCountryCodesImpl = getRealNameAuthCountryCodesImpl(this.mNativeHandle);
        RealNameAuthCountryCodes realNameAuthCountryCodes = null;
        if (realNameAuthCountryCodesImpl == null || realNameAuthCountryCodesImpl.length == 0) {
            return null;
        }
        try {
            realNameAuthCountryCodes = RealNameAuthCountryCodes.parseFrom(realNameAuthCountryCodesImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return realNameAuthCountryCodes;
    }

    @Nullable
    public String getRealNameAuthPrivacyURL() {
        return getRealNameAuthPrivacyURLImpl(this.mNativeHandle);
    }

    @Nullable
    public VanityURLInfoList getMultiVanityURLs() {
        byte[] multiVanityURLsImpl = getMultiVanityURLsImpl(this.mNativeHandle);
        VanityURLInfoList vanityURLInfoList = null;
        if (multiVanityURLsImpl == null || multiVanityURLsImpl.length == 0) {
            return null;
        }
        try {
            vanityURLInfoList = VanityURLInfoList.parseFrom(multiVanityURLsImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return vanityURLInfoList;
    }

    public CustomizeInfo getRecordingReminderCustomizeInfo() {
        return getRecordingReminderCustomizeInfoImpl(this.mNativeHandle);
    }

    public boolean isAuthLocalRecordDisabled() {
        return isAuthLocalRecordDisabledImpl(this.mNativeHandle);
    }
}
