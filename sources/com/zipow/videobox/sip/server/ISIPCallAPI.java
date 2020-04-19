package com.zipow.videobox.sip.server;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmCallPeerDataProto;
import com.zipow.videobox.ptapp.PTAppProtos.CmmCallPeerDataProto.Builder;
import com.zipow.videobox.sip.CmmCallPeerDataBean;
import p021us.zoom.androidlib.util.StringUtil;

public class ISIPCallAPI {
    private static final String TAG = "ISIPCallAPI";
    private long mNativeHandle;

    private native boolean audioDeviceChangedImpl(long j, String str, String str2);

    private native boolean callPeerWithDataImpl(long j, byte[] bArr);

    private native boolean cancelWarmTransferImpl(long j);

    private native boolean completeWarmTransferImpl(long j, String str);

    private native void dismissImpl(long j, String str);

    private native boolean enableSIPAudioImpl(long j, boolean z, boolean z2);

    private native long getADHocCallRecordingBitImpl(long j);

    private native long getActiveCallImpl(long j);

    private native long getAudioFilePlayerImpl(long j);

    private native int getCallCountImpl(long j);

    private native long getCallItemByCallIDImpl(long j, String str);

    private native long getCallItemByIndexImpl(long j, int i);

    private native long getConfigurationImpl(long j);

    private native long getHasCallingPlanBitImpl(long j);

    private native long getLBREnabledBitImpl(long j);

    private native int getMeetingStateImpl(long j);

    private native long getMessageAPIImpl(long j);

    private native long getMessageEnabledBitImpl(long j);

    private native long getPBXFeatureOptionsImpl(long j);

    private native long getReceiveCallsFromCallQueueBitImpl(long j);

    private native long getReceiveCallsFromSLGBitImpl(long j);

    private native long getRepositoryControllerImpl(long j);

    private native long getSIPLineMgrAPIImpl(long j);

    private native long getSharedLineGroupEnabledBitImpl(long j);

    private native int getUnreadVoiceMailCountImpl(long j);

    private native long getUserInCallQueueBitImpl(long j);

    private native long getUserInSLGBitImpl(long j);

    private native boolean handleCallImpl(long j, String str, int i, int i2);

    private native boolean handleRecordingImpl(long j, String str, int i);

    private native boolean hangupAllCallsImpl(long j);

    private native boolean inboundCallPushDuplicateCheckImpl(long j, String str);

    private native boolean inboundCallPushPickupImpl(long j, String str, String str2, String str3, int i, boolean z, String str4, String str5, String str6, String str7, String str8);

    private native boolean inboundCallPushReleaseImpl(long j, int i, String str, String str2, String str3, String str4, String str5);

    private native boolean initModuleForPushCallImpl(long j, String str, String str2, String str3, String str4);

    private native boolean initModuleImpl(long j, String str, String str2, String str3);

    private native boolean isCallMutedImpl(long j);

    private native boolean isEnableADHocCallRecordingImpl(long j);

    private native boolean isEnableHasCallingPlanImpl(long j);

    private native boolean isInCallQueuesImpl(long j);

    private native boolean isInSLGImpl(long j);

    private native boolean isInTalkingStatusImpl(long j);

    private native boolean isInitedImpl(long j);

    private native boolean isLBREnabledImpl(long j);

    private native boolean isMessageEnabledImpl(long j);

    private native boolean isReceiveCallsFromCallQueuesImpl(long j);

    private native boolean isReceiveCallsFromSLGImpl(long j);

    private native boolean isSharedLineGroupEnabledImpl(long j);

    private native boolean isSpeakerMutedImpl(long j);

    private native boolean isVideoTurnOffWhileJoinMeetingImpl(long j);

    private native boolean joinMeetingImpl(long j, String str, long j2, String str2, boolean z);

    private native boolean mergeCallImpl(long j, String str, String str2);

    private native boolean muteCallImpl(long j, boolean z);

    private native boolean muteSpeakerImpl(long j, boolean z);

    private native boolean notifyMeetingToTurnOnOffAudioImpl(long j, boolean z);

    private native void notifyNetworkStateChangedImpl(long j, int i, String str);

    private native boolean playSoundFileImpl(long j, String str, int i, int i2);

    private native boolean printPushCallLogImpl(long j, int i, String str, String str2, String str3, String str4, long j2);

    private native boolean queryUserPbxInfoImpl(long j);

    private native void registerUICallBackImpl(long j, long j2);

    private native void resumeToSuspendImpl(long j);

    private native boolean sendDTMFImpl(long j, String str, String str2);

    private native boolean startMeetingImpl(long j, String str);

    private native void suspendToResumeImpl(long j, int i, String str);

    private native boolean switchCallToCarrierImpl(long j, String str, String str2);

    private native boolean transferCallImpl(long j, String str, String str2, int i, int i2);

    private native int transferToMeetingImpl(long j, String str);

    private native void uninitModuleImpl(long j);

    private native boolean unloadSIPServiceImpl(long j);

    private native int updateReceiveCallsFromCallQueuesImpl(long j, boolean z, boolean z2);

    private native int updateReceiveCallsFromSLGImpl(long j, boolean z, boolean z2);

    private native boolean upgradeToMeetingImpl(long j, String str, long j2, String str2);

    private native void uploadExceptionMemoryLogImpl(long j, int i, String str, String str2);

    public ISIPCallAPI(long j) {
        this.mNativeHandle = j;
    }

    public void registerUICallBack(@Nullable SIPCallEventListenerUI sIPCallEventListenerUI) {
        long j = this.mNativeHandle;
        if (j != 0 && sIPCallEventListenerUI != null) {
            registerUICallBackImpl(j, sIPCallEventListenerUI.getNativeHandle());
        }
    }

    @Nullable
    public ISIPCallConfigration getConfiguration() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return new ISIPCallConfigration(getConfigurationImpl(j));
    }

    public boolean initModule(String str, String str2, String str3) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return initModuleImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3));
    }

    public boolean initModuleForPushCall(String str, String str2, String str3, String str4) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str)) {
            return false;
        }
        return initModuleForPushCallImpl(this.mNativeHandle, str, StringUtil.safeString(str2), StringUtil.safeString(str3), StringUtil.safeString(str4));
    }

    public void uninitModule() {
        long j = this.mNativeHandle;
        if (j != 0) {
            uninitModuleImpl(j);
        }
    }

    public boolean isInited() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInitedImpl(j);
    }

    public void uploadExceptionMemoryLog(int i, String str, String str2) {
        long j = this.mNativeHandle;
        if (j != 0) {
            uploadExceptionMemoryLogImpl(j, i, StringUtil.safeString(str), StringUtil.safeString(str2));
        }
    }

    @Nullable
    public ISIPCallRepositoryController getRepositoryController() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long repositoryControllerImpl = getRepositoryControllerImpl(j);
        if (repositoryControllerImpl == 0) {
            return null;
        }
        return new ISIPCallRepositoryController(repositoryControllerImpl);
    }

    @Nullable
    public ISIPAudioFilePlayer getAudioFilePlayer() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long audioFilePlayerImpl = getAudioFilePlayerImpl(j);
        if (audioFilePlayerImpl == 0) {
            return null;
        }
        return new ISIPAudioFilePlayer(audioFilePlayerImpl);
    }

    public boolean startMeeting(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return startMeetingImpl(j, StringUtil.safeString(str));
    }

    public boolean upgradeToMeeting(String str, long j, String str2) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return upgradeToMeetingImpl(j2, StringUtil.safeString(str), j, StringUtil.safeString(str2));
    }

    public boolean joinMeeting(String str, long j, String str2, boolean z) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return joinMeetingImpl(j2, StringUtil.safeString(str), j, StringUtil.safeString(str2), z);
    }

    public boolean isVideoTurnOffWhileJoinMeeting() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isVideoTurnOffWhileJoinMeetingImpl(j);
    }

    public boolean queryUserPbxInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return queryUserPbxInfoImpl(j);
    }

    public long getPBXFeatureOptions() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPBXFeatureOptionsImpl(j);
    }

    public long getSharedLineGroupEnabledBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSharedLineGroupEnabledBitImpl(j);
    }

    public boolean isSharedLineGroupEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSharedLineGroupEnabledImpl(j);
    }

    public boolean isEnableADHocCallRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableADHocCallRecordingImpl(j);
    }

    public long getADHocCallRecordingBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getADHocCallRecordingBitImpl(j);
    }

    public long getUserInCallQueueBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUserInCallQueueBitImpl(j);
    }

    public boolean isInCallQueues() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInCallQueuesImpl(j);
    }

    public long getReceiveCallsFromCallQueueBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getReceiveCallsFromCallQueueBitImpl(j);
    }

    public boolean isReceiveCallsFromCallQueues() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isReceiveCallsFromCallQueuesImpl(j);
    }

    public int updateReceiveCallsFromCallQueues(boolean z, boolean z2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 4;
        }
        return updateReceiveCallsFromCallQueuesImpl(j, z, z2);
    }

    public long getUserInSLGBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUserInSLGBitImpl(j);
    }

    public boolean isInSLG() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInSLGImpl(j);
    }

    public long getReceiveCallsFromSLGBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getReceiveCallsFromSLGBitImpl(j);
    }

    public boolean isReceiveCallsFromSLG() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isReceiveCallsFromSLGImpl(j);
    }

    public int updateReceiveCallsFromSLG(boolean z, boolean z2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 4;
        }
        return updateReceiveCallsFromSLGImpl(j, z, z2);
    }

    public long getLBREnabledBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLBREnabledBitImpl(j);
    }

    public boolean isLBREnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLBREnabledImpl(j);
    }

    public long getMessageEnabledBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMessageEnabledBitImpl(j);
    }

    public boolean isMessageEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMessageEnabledImpl(j);
    }

    public boolean callPeerWithData(@NonNull CmmCallPeerDataBean cmmCallPeerDataBean) {
        if (this.mNativeHandle == 0) {
            return false;
        }
        Builder newBuilder = CmmCallPeerDataProto.newBuilder();
        newBuilder.setCountryCode(cmmCallPeerDataBean.getCountryCode()).setDisplayNumber(StringUtil.safeString(cmmCallPeerDataBean.getDisplayNumber())).setPeerUri(StringUtil.safeString(cmmCallPeerDataBean.getPeerUri())).setNumberType(cmmCallPeerDataBean.getNumberType()).setIsAnonymous(cmmCallPeerDataBean.isAnonymous()).setPeerName(StringUtil.safeString(cmmCallPeerDataBean.getPeerName()));
        return callPeerWithDataImpl(this.mNativeHandle, newBuilder.build().toByteArray());
    }

    public boolean printPushCallLog(int i, String str, String str2, String str3, String str4, long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        return printPushCallLogImpl(j2, i, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3), StringUtil.safeString(str4), j);
    }

    public boolean inboundCallPushPickup(String str, String str2, String str3, int i, boolean z, String str4, String str5, String str6, String str7, String str8) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return inboundCallPushPickupImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3), i, z, StringUtil.safeString(str4), StringUtil.safeString(str5), StringUtil.safeString(str6), StringUtil.safeString(str7), StringUtil.safeString(str8));
    }

    public boolean inboundCallPushRelease(int i, @NonNull String str, String str2, String str3, String str4, String str5) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return inboundCallPushReleaseImpl(j, i, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3), StringUtil.safeString(str4), StringUtil.safeString(str5));
    }

    public boolean inboundCallPushDuplicateCheck(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return inboundCallPushDuplicateCheckImpl(j, StringUtil.safeString(str));
    }

    public boolean handleCall(String str, int i, int i2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        return handleCallImpl(this.mNativeHandle, StringUtil.safeString(str), i, i2);
    }

    public boolean handleRecording(String str, int i) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return handleRecordingImpl(this.mNativeHandle, StringUtil.safeString(str), i);
        }
        return false;
    }

    public boolean transferCall(String str, String str2, int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return transferCallImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2), i, i2);
    }

    public boolean completeWarmTransfer(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return completeWarmTransferImpl(j, StringUtil.safeString(str));
    }

    public boolean cancelWarmTransfer() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return cancelWarmTransferImpl(j);
    }

    public boolean hangupAllCalls() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hangupAllCallsImpl(j);
    }

    public boolean muteCall(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return muteCallImpl(j, z);
    }

    public boolean isCallMuted() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCallMutedImpl(j);
    }

    public boolean muteSpeaker(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return muteSpeakerImpl(j, z);
    }

    public boolean isSpeakerMuted() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSpeakerMutedImpl(j);
    }

    public void notifyNetworkStateChanged(int i, String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            notifyNetworkStateChangedImpl(j, i, StringUtil.safeString(str));
        }
    }

    public void suspendToResume(boolean z, String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            boolean z2 = !z;
            suspendToResumeImpl(j, z2 ? 1 : 0, StringUtil.safeString(str));
        }
    }

    public void resumeToSuspend() {
        long j = this.mNativeHandle;
        if (j != 0) {
            resumeToSuspendImpl(j);
        }
    }

    public boolean isInTalkingStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInTalkingStatusImpl(j);
    }

    public boolean sendDTMF(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return sendDTMFImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2));
    }

    public void dismiss(String str) {
        long j = this.mNativeHandle;
        if (j != 0) {
            dismissImpl(j, StringUtil.safeString(str));
        }
    }

    public int getCallCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallCountImpl(j);
    }

    @Nullable
    public CmmSIPCallItem getCallItemByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long callItemByIndexImpl = getCallItemByIndexImpl(j, i);
        if (callItemByIndexImpl != 0) {
            return new CmmSIPCallItem(callItemByIndexImpl);
        }
        return null;
    }

    @Nullable
    public CmmSIPCallItem getCallItemByCallID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long callItemByCallIDImpl = getCallItemByCallIDImpl(j, StringUtil.safeString(str));
        if (callItemByCallIDImpl != 0) {
            return new CmmSIPCallItem(callItemByCallIDImpl);
        }
        return null;
    }

    @Nullable
    public CmmSIPCallItem getActiveCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long activeCallImpl = getActiveCallImpl(j);
        if (activeCallImpl != 0) {
            return new CmmSIPCallItem(activeCallImpl);
        }
        return null;
    }

    public boolean unloadSIPService() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return unloadSIPServiceImpl(j);
    }

    public boolean mergeCall(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return mergeCallImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2));
    }

    @Nullable
    public ISIPLineMgrAPI getSIPLineMgrAPI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long sIPLineMgrAPIImpl = getSIPLineMgrAPIImpl(j);
        if (sIPLineMgrAPIImpl == 0) {
            return null;
        }
        return new ISIPLineMgrAPI(sIPLineMgrAPIImpl);
    }

    @Nullable
    public IPBXMessageAPI getMessageAPI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long messageAPIImpl = getMessageAPIImpl(j);
        if (messageAPIImpl == 0) {
            return null;
        }
        return new IPBXMessageAPI(messageAPIImpl);
    }

    public boolean switchCallToCarrier(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return switchCallToCarrierImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2));
    }

    public boolean isEnableHasCallingPlan() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableHasCallingPlanImpl(j);
    }

    public long getHasCallingPlanBit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getHasCallingPlanBitImpl(j);
    }

    public boolean playSoundFile(String str, int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return playSoundFileImpl(j, StringUtil.safeString(str), i, i2);
    }

    public boolean audioDeviceChanged(@Nullable String str, @Nullable String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return audioDeviceChangedImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2));
    }

    public boolean notifyMeetingToTurnOnOffAudio(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return notifyMeetingToTurnOnOffAudioImpl(j, z);
    }

    public boolean enableSIPAudio(boolean z, boolean z2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return enableSIPAudioImpl(j, z, z2);
    }

    public int transferToMeeting(String str) {
        if (TextUtils.isEmpty(str)) {
            return 3;
        }
        long j = this.mNativeHandle;
        if (j == 0) {
            return 3;
        }
        return transferToMeetingImpl(j, str);
    }

    public int getMeetingState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMeetingStateImpl(j);
    }
}
