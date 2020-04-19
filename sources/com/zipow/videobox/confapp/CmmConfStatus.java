package com.zipow.videobox.confapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.common.conf.MyBandwidthLimitInfo;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmConfStatus {
    private static final String TAG = "CmmConfStatus";
    private long mNativeHandle = 0;

    private native boolean canIAdmitOthersWhenNoHostImpl(long j);

    private native void changeAttendeeChatPriviledgeImpl(long j, int i);

    private native int getAttendeeChatPriviledgeImpl(long j);

    private native int getAttendeeVideoControlModeImpl(long j);

    private native int getAttendeeVideoLayoutFlagImpl(long j);

    private native int getAttendeeVideoLayoutModeImpl(long j);

    private native int getCallMeStatusImpl(long j);

    @Nullable
    private native String getLiveChannelUrlImpl(long j, int i);

    private native int getLiveChannelsCountImpl(long j);

    @Nullable
    private native String getLiveChannelsNameImpl(long j, int i);

    private native int getLiveTranscriptionStatusImpl(long j);

    private native long getMeetingElapsedTimeInSecsImpl(long j);

    private native boolean getMyBandwidthLimitInfoImpl(long j, MyBandwidthLimitInfo myBandwidthLimitInfo);

    @Nullable
    private native String getMyCallOutNumberImpl(long j);

    private native boolean getShowBandwidthLimitAgainImpl(long j);

    private native boolean hangUpImpl(long j);

    private native boolean hasHostinMeetingImpl(long j);

    private native boolean isAllowMessageAndFeedbackNotifyImpl(long j);

    private native boolean isAllowParticipantRenameImpl(long j);

    private native boolean isAllowRaiseHandImpl(long j);

    private native boolean isBOModeratorImpl(long j);

    private native boolean isBandwidthLimitEnabledImpl(long j);

    private native boolean isCMRInConnectingImpl(long j);

    private native boolean isCallOutInProgressImpl(long j);

    private native boolean isChatDisabledByInfoBarrierImpl(long j);

    private native boolean isConfLockedImpl(long j);

    private native boolean isDialInImpl(long j);

    private native boolean isHostImpl(long j);

    private native boolean isHostViewingShareInWebinarImpl(long j);

    private native boolean isInPracticeSessionImpl(long j);

    private native boolean isLiveChannelsOnImpl(long j, int i);

    private native boolean isLiveConnectingImpl(long j);

    private native boolean isLiveOnImpl(long j);

    private native boolean isMMRUserImpl(long j, long j2);

    private native boolean isMasterConfHostImpl(long j, long j2);

    private native boolean isMyselfImpl(long j, long j2);

    private native boolean isPresentImpl(long j);

    private native boolean isRemoteAdminExistingImpl(long j);

    private native boolean isSameUserImpl(long j, long j2, long j3);

    private native boolean isShareDisabledByInfoBarrierImpl(long j);

    private native boolean isStartVideoDisabledIml(long j);

    private native boolean isVerifyingMyGuestRoleImpl(long j);

    private native boolean setAttendeeVideoControlModeImpl(long j, int i);

    private native void setLangcodeImpl(long j, String str);

    private native boolean setLiveLayoutModeImpl(long j, boolean z);

    private native boolean setShowBandwidthLimitAgainImpl(long j, boolean z);

    private native boolean startCallOutImpl(long j, String str, String str2);

    private native boolean stopLiveImpl(long j);

    public CmmConfStatus(long j) {
        this.mNativeHandle = j;
    }

    public boolean isHost() {
        return isHostImpl(this.mNativeHandle);
    }

    public boolean isBOModerator() {
        return isBOModeratorImpl(this.mNativeHandle);
    }

    public boolean isMasterConfHost(long j) {
        return isMasterConfHostImpl(this.mNativeHandle, j);
    }

    public boolean isPresent() {
        return isPresentImpl(this.mNativeHandle);
    }

    public boolean isMyself(long j) {
        return isMyselfImpl(this.mNativeHandle, j);
    }

    public boolean isSameUser(long j, long j2) {
        return isSameUserImpl(this.mNativeHandle, j, j2);
    }

    public boolean isMMRUser(long j) {
        return isMMRUserImpl(this.mNativeHandle, j);
    }

    public boolean isConfLocked() {
        return isConfLockedImpl(this.mNativeHandle);
    }

    public boolean hasHostinMeeting() {
        return hasHostinMeetingImpl(this.mNativeHandle);
    }

    public boolean isCallOutInProgress() {
        return isCallOutInProgressImpl(this.mNativeHandle);
    }

    public int getCallMeStatus() {
        return getCallMeStatusImpl(this.mNativeHandle);
    }

    public int getLiveTranscriptionStatus() {
        return getLiveTranscriptionStatusImpl(this.mNativeHandle);
    }

    public boolean startCallOut(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return startCallOutImpl(this.mNativeHandle, str, "");
    }

    public boolean hangUp() {
        return hangUpImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyCallOutNumber() {
        return getMyCallOutNumberImpl(this.mNativeHandle);
    }

    public boolean isDialIn() {
        return isDialInImpl(this.mNativeHandle);
    }

    public boolean isCMRInConnecting() {
        return isCMRInConnectingImpl(this.mNativeHandle);
    }

    public void setLangcode(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            setLangcodeImpl(this.mNativeHandle, str);
        }
    }

    public boolean isStartVideoDisabled() {
        return isStartVideoDisabledIml(this.mNativeHandle);
    }

    public boolean isAllowRaiseHand() {
        return isAllowRaiseHandImpl(this.mNativeHandle);
    }

    public boolean isInPracticeSession() {
        return isInPracticeSessionImpl(this.mNativeHandle);
    }

    public boolean isLiveOn() {
        return isLiveOnImpl(this.mNativeHandle);
    }

    public boolean isLiveConnecting() {
        return isLiveConnectingImpl(this.mNativeHandle);
    }

    public boolean stopLive() {
        return stopLiveImpl(this.mNativeHandle);
    }

    public boolean setLiveLayoutMode(boolean z) {
        return setLiveLayoutModeImpl(this.mNativeHandle, z);
    }

    public int getLiveChannelsCount() {
        return getLiveChannelsCountImpl(this.mNativeHandle);
    }

    public boolean isLiveChannelsOn(int i) {
        return isLiveChannelsOnImpl(this.mNativeHandle, i);
    }

    public boolean canIAdmitOthersWhenNoHost() {
        return canIAdmitOthersWhenNoHostImpl(this.mNativeHandle);
    }

    @Nullable
    public String getLiveChannelsName(int i) {
        return getLiveChannelsNameImpl(this.mNativeHandle, i);
    }

    @Nullable
    public String getLiveChannelUrL(int i) {
        return getLiveChannelUrlImpl(this.mNativeHandle, i);
    }

    public void changeAttendeeChatPriviledge(int i) {
        changeAttendeeChatPriviledgeImpl(this.mNativeHandle, i);
    }

    public int getAttendeeChatPriviledge() {
        return getAttendeeChatPriviledgeImpl(this.mNativeHandle);
    }

    public long getMeetingElapsedTimeInSecs() {
        return getMeetingElapsedTimeInSecsImpl(this.mNativeHandle);
    }

    public boolean setAttendeeVideoControlMode(int i) {
        return setAttendeeVideoControlModeImpl(this.mNativeHandle, i);
    }

    public int getAttendeeVideoControlMode() {
        return getAttendeeVideoControlModeImpl(this.mNativeHandle);
    }

    public int getAttendeeVideoLayoutMode() {
        return getAttendeeVideoLayoutModeImpl(this.mNativeHandle);
    }

    public int getAttendeeVideoLayoutFlag() {
        return getAttendeeVideoLayoutFlagImpl(this.mNativeHandle);
    }

    public boolean isAllowParticipantRename() {
        return isAllowParticipantRenameImpl(this.mNativeHandle);
    }

    public boolean isAllowMessageAndFeedbackNotify() {
        return isAllowMessageAndFeedbackNotifyImpl(this.mNativeHandle);
    }

    public boolean isVerifyingMyGuestRole() {
        return isVerifyingMyGuestRoleImpl(this.mNativeHandle);
    }

    public boolean isBandwidthLimitEnabled() {
        return isBandwidthLimitEnabledImpl(this.mNativeHandle);
    }

    @NonNull
    public MyBandwidthLimitInfo getMyBandwidthLimitInfo() {
        MyBandwidthLimitInfo myBandwidthLimitInfo = new MyBandwidthLimitInfo();
        getMyBandwidthLimitInfoImpl(this.mNativeHandle, myBandwidthLimitInfo);
        return myBandwidthLimitInfo;
    }

    public void setShowBandwidthLimitAgain(boolean z) {
        setShowBandwidthLimitAgainImpl(this.mNativeHandle, z);
    }

    public boolean getShowBandwidthLimitAgain() {
        return getShowBandwidthLimitAgainImpl(this.mNativeHandle);
    }

    public boolean isHostViewingShareInWebinar() {
        return isHostViewingShareInWebinarImpl(this.mNativeHandle);
    }

    public boolean isRemoteAdminExisting() {
        return isRemoteAdminExistingImpl(this.mNativeHandle);
    }

    public boolean isShareDisabledByInfoBarrier() {
        return isShareDisabledByInfoBarrierImpl(this.mNativeHandle);
    }

    public boolean isChatDisabledByInfoBarrier() {
        return isChatDisabledByInfoBarrierImpl(this.mNativeHandle);
    }
}
