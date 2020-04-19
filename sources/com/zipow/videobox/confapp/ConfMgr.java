package com.zipow.videobox.confapp;

import android.content.Context;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.annotate.ZoomAnnotate;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfAppProtos.CCMessage;
import com.zipow.videobox.confapp.ConfAppProtos.ChatMessage;
import com.zipow.videobox.confapp.meeting.confhelper.ConfDataHelper;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.poll.PollingMgr;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.MonitorLogService;
import com.zipow.videobox.util.E2EMeetingExternalSessionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import p021us.zipow.mdm.ZoomMdmPolicyProvider;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;

public class ConfMgr {
    private static final String TAG = "ConfMgr";
    @Nullable
    private static ConfMgr instance;
    @Nullable
    private CmmAttentionTrackMgr mATMgr = null;
    @Nullable
    private AudioSessionMgr mAudioSessionMgr = null;
    @Nullable
    private BOMgr mBOMgr = null;
    private transient int mClientUserCount = 0;
    @Nullable
    private ConfDataHelper mConfDataHelper = null;
    @Nullable
    private CmmFeedbackMgr mFeedbackMgr = null;
    @Nullable
    private InterpretationMgr mInterpretationMgr = null;
    @Nullable
    private PollingMgr mPollingMgr = null;
    @Nullable
    private ZoomRaiseHandInWebinar mRaiseHandInWebinar = null;
    @Nullable
    private RecordMgr mRecordMgr = null;
    @Nullable
    private ShareSessionMgr mShareSessionMgr = null;
    private transient boolean mUserJoined = false;
    @Nullable
    private CmmUserList mUserList = null;
    @Nullable
    private VideoSessionMgr mVideoSessionMgr = null;
    @Nullable
    private ZoomAnnotate mZoomAnnotateMgr = null;
    @Nullable
    private ZoomMdmPolicyProvider mZoomMdmPolicyProvider = null;

    private native boolean admitAllSilentUsersIntoMeetingImpl();

    private native void agreeChinaMeetingPrivacyImpl();

    private native void agreeJoinMeetingDisclaimerImpl();

    private native void agreeStartRecordingDisclaimerImpl();

    private native boolean bindTelephoneUserImpl(long j, long j2);

    private native boolean canUmmuteMyVideoImpl();

    private native boolean canUnmuteMyselfImpl();

    private native boolean changeAttendeeNamebyJIDImpl(String str, String str2);

    private native boolean changeUserNameByIDImpl(String str, long j);

    private native boolean checkCMRPrivilegeImpl();

    private native void cleanupConfImpl();

    private native boolean confirmGDPRImpl(boolean z);

    private native boolean continueJoinAsGuestImpl();

    private native boolean disabledAttendeeUnmuteSelfImpl();

    private native void dispatchIdleMessageImpl();

    private native boolean downgradeToAttendeeImpl(String str);

    private native boolean expelAttendeeImpl(String str);

    private native long getAttentionTrackAPIImpl();

    private native long getAudioObjHandle();

    private native int getAuthInfoImpl(int i, String str, int i2, String[] strArr, String[] strArr2);

    private native long getBOManagerHandle();

    @Nullable
    private native String getBindPhoneUrlForRealNameAuthImpl();

    @Nullable
    private native byte[] getCCMessageItemAtProtoData(int i);

    @Nullable
    private native byte[] getChatMessageAtProtoData(int i);

    private native int getChatMessageCountImpl();

    private native long getChatMessageItemByIDImpl(String str);

    @Nullable
    private native String[] getChatMessagesByUserImpl(long j, boolean z);

    private native int getClientUserCountImpl(boolean z);

    private native int getClientWithoutOnHoldUserCountImpl(boolean z);

    private native int getClosedCaptionMessageCountImpl();

    private native long getConfContextHandleImpl();

    private native int getConfStatusImpl();

    private native long getConfStatusObjImpl();

    private native int getCurrentVendorImpl();

    @Nullable
    private native byte[] getE2EMeetingSecureKeyImpl();

    private native long getFeedbackAPIImpl();

    private native long getInterpretationObjImpl();

    private native int getLastNetworkErrorCodeImpl();

    private native String getMeetingTopicImpl();

    private native long getMonitorLogServiceImpl();

    private native void getPTLoginInfoImpl(String[] strArr, String[] strArr2, String[] strArr3, int[] iArr);

    private native int getPhoneBuddyCountImpl();

    private native long getPolicyProviderHandleImpl();

    private native long getPollObjHandle();

    private native int getPureCallinUserCountImpl();

    private native long getQAComponentHandle();

    private native long getRaiseHandAPIObjHandle();

    private native long getRecordMgrHandle();

    private native long getShareObjHandle();

    @Nullable
    private native String getSignUpUrlForRealNameAuthImpl();

    @Nullable
    private native String getTalkingUserNameImpl();

    @Nullable
    private native int[] getUnreadChatMessageIndexesImpl(boolean z);

    @Nullable
    private native String[] getUnreadChatMessagesByUserImpl(long j, boolean z);

    private native long getUserByIdImpl(long j);

    private native long getUserByQAAttendeeJIDImpl(String str);

    private native long getUserListHandle();

    @Nullable
    private native String getVerifiedPhoneNumberImpl();

    private native long getVideoObjHandle();

    private native int getVideoUserCountImpl();

    private native int getViewOnlyTelephonyUserCountImpl();

    private native int getViewOnlyUserCountImpl();

    @Nullable
    private native String getWaitingRoomLayoutDescriptionImpl();

    @Nullable
    private native String getWaitingRoomLayoutImagePathImpl();

    @Nullable
    private native String getWaitingRoomLayoutTitleImpl();

    private native int getWaitingRoomLayoutTypeImpl();

    @Nullable
    private native String getWebDomainImpl(boolean z);

    private native long getWebinarChatAPIObjHandle();

    private native boolean handleConfCmdImpl(int i);

    private native boolean handleE2EMeetingExternalSessionKeyReadyImpl(int[] iArr, E2EMeetingExternalSessionKey[] e2EMeetingExternalSessionKeyArr, boolean z);

    private native boolean handleUserCmdImpl(int i, long j);

    private native boolean inviteRoomSystemByCalloutImpl(InviteRoomDeviceInfo inviteRoomDeviceInfo);

    private native boolean isAllowAskQuestionAnonymouslyImpl();

    private native boolean isAllowAttendeeAnswerQuestionImpl();

    private native boolean isAllowAttendeeChatImpl();

    private native boolean isAllowAttendeeUpvoteQuestionImpl();

    private native boolean isAllowAttendeeViewAllQuestionImpl();

    private native boolean isDriveModeSettingOnImpl();

    private native boolean isJoinWithOutAudioImpl();

    private native boolean isNoVideoMeetingImpl();

    private native boolean isPlayChimeOnImpl();

    private native boolean isPublicGmailUserImpl();

    private native boolean isPutOnHoldOnEntryLockedImpl();

    private native boolean isPutOnHoldOnEntryOnImpl();

    private native boolean isShareLockedImpl();

    private native boolean isShowClockEnableImpl();

    private native boolean isUserOriginalorAltHostImpl(String str);

    private native boolean isViewOnlyClientOnMMRImpl();

    private native boolean isViewOnlyMeetingImpl();

    private native boolean isWaitingRoomLayoutReadyImpl();

    private native void logUICommandImpl(String str, String str2, String str3);

    private native boolean loginToJoinMeetingForGuestImpl();

    private native boolean loginToJoinMeetingForRealNameAuthImpl();

    private native boolean loginToJoinMeetingImpl();

    private native boolean loginWhenInWaitingRoomImpl();

    private native boolean mmrMonitorLogImpl(String str, String str2);

    private native void nativeInit();

    private native boolean needPreviewVideoWhenStartMeetingImpl();

    private native boolean noOneIsSendingVideoImpl();

    private native boolean notifyConfLeaveReasonImpl(String str, boolean z, boolean z2);

    private native boolean notifyPTLoginToClaimHostImpl();

    private native boolean notifyPTStartLoginImpl(String str);

    private native void onUserConfirmOptionalVanityURLsImpl(String str);

    private native void onUserConfirmRealNameAuthImpl(String str, String str2, String str3);

    private native void onUserConfirmToJoinImpl(boolean z, String str);

    private native void onUserConfirmVideoPrivacyImpl(boolean z);

    private native void onUserInputConfNumberImpl(boolean z, String str);

    private native void onUserInputPasswordImpl(String str, String str2, boolean z);

    private native void onUserRegisterWebinarImpl(String str, String str2, boolean z);

    private native boolean promotePanelistImpl(String str);

    private native boolean requestRealNameAuthSMSImpl(String str, String str2);

    private native boolean sendChatMessageToImpl(long j, String str, boolean z, boolean z2, long j2);

    private native boolean sendChatToSilentModeUsersImpl(String str);

    private native boolean sendEmojiReactionImpl(String str);

    private native boolean sendEmojiReactionTypeImpl(int i, int i2);

    private native boolean sendParingCodeImpl(String str);

    private native boolean sendXmppChatToAllPanelistsImpl(String str);

    private native boolean sendXmppChatToIndividualImpl(String str, String str2, boolean z);

    private native void setAndroidNetworkTypeImpl(int i, int i2);

    private native boolean setChatMessageAsReadedImpl(String str);

    private native void setConnectAudioDialogShowStatusImpl(boolean z);

    private native void setLanguageIDImpl(String str);

    private native boolean setMeetingTopicImpl(String str);

    private native void setPlayChimeOnOffImpl(boolean z);

    private native void setPutOnHoldOnEntryImpl(boolean z);

    private native void setShowClockInMeetingImpl(boolean z);

    private native void setWifiSignalQualityImpl(int i);

    private native boolean tryUpgradeThisFreeMeetingImpl();

    private native boolean unbindTelephoneUserImpl(long j);

    private native void updateChattedAttendeesImpl();

    private native boolean upgradeAccountImpl();

    private native boolean validateConfNumberImpl(String str);

    private native boolean verifyHostKeyImpl(String str);

    public boolean isFacebookImEnabled() {
        return false;
    }

    public boolean isGoogleImEnabled() {
        return false;
    }

    private ConfMgr() {
    }

    @NonNull
    public static synchronized ConfMgr getInstance() {
        ConfMgr confMgr;
        synchronized (ConfMgr.class) {
            if (instance == null) {
                instance = new ConfMgr();
            }
            confMgr = instance;
        }
        return confMgr;
    }

    public void initialize() {
        nativeInit();
    }

    @Nullable
    public static Context getApplicationContext() {
        return VideoBoxApplication.getInstance();
    }

    public void dispatchIdleMessage() {
        dispatchIdleMessageImpl();
    }

    public void onUserInputConfNumber(boolean z, String str) {
        if (isInitialForMainboard()) {
            onUserInputConfNumberImpl(z, str);
        }
    }

    public void onUserConfirmToJoin(boolean z) {
        onUserConfirmToJoin(z, null);
    }

    public void onUserConfirmToJoin(boolean z, @Nullable String str) {
        if (str == null) {
            str = "";
        }
        if (isInitialForMainboard()) {
            onUserConfirmToJoinImpl(z, str);
        }
    }

    public boolean handleConfCmd(int i) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return handleConfCmdImpl(i);
    }

    public boolean handleUserCmd(int i, long j) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return handleUserCmdImpl(i, j);
    }

    public int getConfStatus() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getConfStatusImpl();
    }

    public boolean isConfConnected() {
        int confStatus = getConfStatus();
        return confStatus == 12 || confStatus == 13;
    }

    @Nullable
    public VideoSessionMgr getVideoObj() {
        VideoSessionMgr videoSessionMgr = this.mVideoSessionMgr;
        if (videoSessionMgr != null) {
            return videoSessionMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long videoObjHandle = getVideoObjHandle();
        if (videoObjHandle == 0) {
            return null;
        }
        this.mVideoSessionMgr = new VideoSessionMgr(videoObjHandle);
        return this.mVideoSessionMgr;
    }

    @Nullable
    public AudioSessionMgr getAudioObj() {
        AudioSessionMgr audioSessionMgr = this.mAudioSessionMgr;
        if (audioSessionMgr != null) {
            return audioSessionMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long audioObjHandle = getAudioObjHandle();
        if (audioObjHandle == 0) {
            return null;
        }
        this.mAudioSessionMgr = new AudioSessionMgr(audioObjHandle);
        return this.mAudioSessionMgr;
    }

    @Nullable
    public ShareSessionMgr getShareObj() {
        ShareSessionMgr shareSessionMgr = this.mShareSessionMgr;
        if (shareSessionMgr != null) {
            return shareSessionMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long shareObjHandle = getShareObjHandle();
        if (shareObjHandle == 0) {
            return null;
        }
        this.mShareSessionMgr = new ShareSessionMgr(shareObjHandle);
        this.mShareSessionMgr.setShareEventSink(ZoomShareUI.getInstance().getNativeHandle());
        return this.mShareSessionMgr;
    }

    @Nullable
    public ZoomAnnotate getZoomAnnotateMgr() {
        ZoomAnnotate zoomAnnotate = this.mZoomAnnotateMgr;
        if (zoomAnnotate != null) {
            return zoomAnnotate;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long shareObjHandle = getShareObjHandle();
        if (shareObjHandle == 0) {
            return null;
        }
        this.mZoomAnnotateMgr = new ZoomAnnotate(shareObjHandle);
        return this.mZoomAnnotateMgr;
    }

    @NonNull
    public ConfDataHelper getConfDataHelper() {
        ConfDataHelper confDataHelper = this.mConfDataHelper;
        if (confDataHelper != null) {
            return confDataHelper;
        }
        this.mConfDataHelper = new ConfDataHelper();
        return this.mConfDataHelper;
    }

    @Nullable
    public CmmUserList getUserList() {
        CmmUserList cmmUserList = this.mUserList;
        if (cmmUserList != null) {
            return cmmUserList;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long userListHandle = getUserListHandle();
        if (userListHandle == 0) {
            return null;
        }
        this.mUserList = new CmmUserList(userListHandle);
        return this.mUserList;
    }

    @Nullable
    public RecordMgr getRecordMgr() {
        RecordMgr recordMgr = this.mRecordMgr;
        if (recordMgr != null) {
            return recordMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long recordMgrHandle = getRecordMgrHandle();
        if (recordMgrHandle == 0) {
            return null;
        }
        this.mRecordMgr = new RecordMgr(recordMgrHandle);
        return this.mRecordMgr;
    }

    @Nullable
    public ZoomQAComponent getQAComponent() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long qAComponentHandle = getQAComponentHandle();
        if (qAComponentHandle == 0) {
            return null;
        }
        return new ZoomQAComponent(qAComponentHandle);
    }

    @Nullable
    public ZoomChatInWebinar getChatInWebinar() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long webinarChatAPIObjHandle = getWebinarChatAPIObjHandle();
        if (webinarChatAPIObjHandle == 0) {
            return null;
        }
        return new ZoomChatInWebinar(webinarChatAPIObjHandle);
    }

    @Nullable
    public PollingMgr getPollObj() {
        PollingMgr pollingMgr = this.mPollingMgr;
        if (pollingMgr != null) {
            return pollingMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long pollObjHandle = getPollObjHandle();
        if (pollObjHandle == 0) {
            return null;
        }
        this.mPollingMgr = new PollingMgr(pollObjHandle);
        return this.mPollingMgr;
    }

    @Nullable
    public ZoomRaiseHandInWebinar getRaiseHandAPIObj() {
        ZoomRaiseHandInWebinar zoomRaiseHandInWebinar = this.mRaiseHandInWebinar;
        if (zoomRaiseHandInWebinar != null) {
            return zoomRaiseHandInWebinar;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long raiseHandAPIObjHandle = getRaiseHandAPIObjHandle();
        if (raiseHandAPIObjHandle == 0) {
            return null;
        }
        this.mRaiseHandInWebinar = new ZoomRaiseHandInWebinar(raiseHandAPIObjHandle);
        return this.mRaiseHandInWebinar;
    }

    @Nullable
    public BOMgr getBOMgr() {
        BOMgr bOMgr = this.mBOMgr;
        if (bOMgr != null) {
            return bOMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long bOManagerHandle = getBOManagerHandle();
        if (bOManagerHandle == 0) {
            return null;
        }
        this.mBOMgr = new BOMgr(bOManagerHandle);
        return this.mBOMgr;
    }

    @Nullable
    public CmmFeedbackMgr getFeedbackMgr() {
        CmmFeedbackMgr cmmFeedbackMgr = this.mFeedbackMgr;
        if (cmmFeedbackMgr != null) {
            return cmmFeedbackMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long feedbackAPIImpl = getFeedbackAPIImpl();
        if (feedbackAPIImpl == 0) {
            return null;
        }
        this.mFeedbackMgr = new CmmFeedbackMgr(feedbackAPIImpl);
        return this.mFeedbackMgr;
    }

    @Nullable
    public CmmUser getMyself() {
        CmmUserList userList = getUserList();
        if (userList == null) {
            return null;
        }
        return userList.getMyself();
    }

    @Nullable
    public CmmUser getUserById(long j) {
        if (!isInitialForMainboard()) {
            return null;
        }
        long userByIdImpl = getUserByIdImpl(j);
        if (userByIdImpl == 0) {
            return null;
        }
        return new CmmUser(userByIdImpl);
    }

    @Nullable
    public CmmUser getUserByQAAttendeeJID(String str) {
        if (!isInitialForMainboard()) {
            return null;
        }
        long userByQAAttendeeJIDImpl = getUserByQAAttendeeJIDImpl(str);
        if (userByQAAttendeeJIDImpl == 0) {
            return null;
        }
        return new CmmUser(userByQAAttendeeJIDImpl);
    }

    @Nullable
    public CmmConfStatus getConfStatusObj() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long confStatusObjImpl = getConfStatusObjImpl();
        if (confStatusObjImpl == 0) {
            return null;
        }
        return new CmmConfStatus(confStatusObjImpl);
    }

    @Nullable
    public CmmConfContext getConfContext() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long confContextHandleImpl = getConfContextHandleImpl();
        if (confContextHandleImpl == 0) {
            return null;
        }
        return new CmmConfContext(confContextHandleImpl);
    }

    public int getClientUserCount() {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            if (!isInitialForMainboard()) {
                return 0;
            }
            this.mClientUserCount = getClientUserCountImpl(true);
        }
        return this.mClientUserCount;
    }

    public int getViewOnlyTelephonyUserCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getViewOnlyTelephonyUserCountImpl();
    }

    public int getChatMessageCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getChatMessageCountImpl();
    }

    @Nullable
    public ChatMessage getChatMessageAt(int i) {
        if (!isInitialForMainboard()) {
            return null;
        }
        try {
            ChatMessage parseFrom = ChatMessage.parseFrom(getChatMessageAtProtoData(i));
            if (!StringUtil.isEmptyOrNull(parseFrom.getId())) {
                return parseFrom;
            }
            return null;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public int[] getUnreadChatMessageIndexes() {
        if (!isInitialForMainboard()) {
            return null;
        }
        CmmConfContext confContext = getConfContext();
        if (confContext == null || !confContext.inSilentMode()) {
            return getUnreadChatMessageIndexesImpl(true);
        }
        return getUnreadChatMessageIndexesImpl(false);
    }

    public int getUnreadCount() {
        int[] unreadChatMessageIndexes = getUnreadChatMessageIndexes();
        if (unreadChatMessageIndexes == null) {
            return 0;
        }
        return unreadChatMessageIndexes.length;
    }

    @Nullable
    public String[] getChatMessagesByUser(long j, boolean z) {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getChatMessagesByUserImpl(j, z);
    }

    @Nullable
    public String[] getUnreadChatMessagesByUser(long j, boolean z) {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getUnreadChatMessagesByUserImpl(j, z);
    }

    public boolean setChatMessageAsReaded(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return setChatMessageAsReadedImpl(str);
    }

    public boolean sendChatMessageTo(long j, String str, boolean z, boolean z2, long j2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendChatMessageToImpl(j, str, z, z2, j2);
    }

    public boolean validateConfNumber(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return validateConfNumberImpl(str);
    }

    public void leaveConference() {
        leaveConference(false);
    }

    public void endConference() {
        leaveConference(true);
    }

    private void leaveConference(boolean z) {
        if (isConfConnected()) {
            AudioSessionMgr audioObj = getAudioObj();
            if (audioObj != null) {
                if (audioObj.isBluetoothHeadsetStarted()) {
                    audioObj.stopBluetoothHeadset();
                }
                audioObj.setLoudSpeakerStatus(false, true);
            }
        }
        if (z) {
            handleConfCmd(53);
        } else {
            handleConfCmd(0);
        }
    }

    public boolean notifyConfLeaveReason(String str, boolean z) {
        return notifyConfLeaveReason(str, z, false);
    }

    public boolean notifyConfLeaveReason(String str, boolean z, boolean z2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return notifyConfLeaveReasonImpl(str, z, z2);
    }

    public void onUserInputPassword(String str, String str2, boolean z) {
        if (isInitialForMainboard()) {
            onUserInputPasswordImpl(str, str2, z);
        }
    }

    public void onUserRegisterWebinar(@Nullable String str, @Nullable String str2, boolean z) {
        if (isInitialForMainboard()) {
            if (str == null) {
                str = "";
            }
            if (str2 == null) {
                str2 = "";
            }
            onUserRegisterWebinarImpl(str, str2, z);
        }
    }

    public boolean notifyPTStartLogin(@Nullable String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        return notifyPTStartLoginImpl(str);
    }

    public void setAndroidNetworkType(int i, int i2) {
        if (isInitialForMainboard()) {
            setAndroidNetworkTypeImpl(i, i2);
        }
    }

    public boolean updateAccount() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return upgradeAccountImpl();
    }

    public boolean notifyPTLoginToClaimHost() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return notifyPTLoginToClaimHostImpl();
    }

    public boolean canUnmuteMyself() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return canUnmuteMyselfImpl();
    }

    public boolean isCallingOut() {
        if (this.mUserJoined) {
            return false;
        }
        CmmConfContext confContext = getConfContext();
        if (confContext == null) {
            return false;
        }
        boolean isConfConnected = isConfConnected();
        boolean isCall = confContext.isCall();
        int launchReason = confContext.getLaunchReason();
        int clientUserCount = getClientUserCount();
        if (!isCall || launchReason != 1 || (isConfConnected && clientUserCount >= 2)) {
            return false;
        }
        return true;
    }

    private void setUserJoined() {
        this.mUserJoined = true;
    }

    /* access modifiers changed from: protected */
    public void onUserEvent(int i, long j, int i2) {
        this.mClientUserCount = getInstance().getClientUserCount();
        if (i == 0 && this.mClientUserCount >= 2) {
            setUserJoined();
        }
    }

    @Nullable
    public String getVerifiedPhoneNumber() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getVerifiedPhoneNumberImpl();
    }

    public int getPhoneBuddyCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getPhoneBuddyCountImpl();
    }

    public boolean noOneIsSendingVideo() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return noOneIsSendingVideoImpl();
    }

    public boolean isNoVideoMeeting() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isNoVideoMeetingImpl();
    }

    public void getPTLoginInfo(String[] strArr, String[] strArr2, String[] strArr3, int[] iArr) {
        if (isInitialForMainboard()) {
            getPTLoginInfoImpl(strArr, strArr2, strArr3, iArr);
        }
    }

    public int getPTLoginType() {
        int[] iArr = {102};
        getPTLoginInfo(new String[1], new String[1], new String[1], iArr);
        return iArr[0];
    }

    public void cleanupConf() {
        synchronized (ShareSessionMgr.SHARE_SESSION_LOCK) {
            if (isInitialForMainboard()) {
                if (this.mShareSessionMgr != null) {
                    this.mShareSessionMgr.setConfCleaned(true);
                }
                cleanupConfImpl();
            }
        }
    }

    @Nullable
    public String getTalkingUserName() {
        return getTalkingUserNameImpl();
    }

    public boolean isDriveModeSettingOn() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isDriveModeSettingOnImpl();
    }

    public void logUICommand(String str, String str2, String str3) {
        if (isInitialForMainboard()) {
            logUICommandImpl(str, str2, str3);
        }
    }

    public void setWifiSignalQuality(int i) {
        if (isInitialForMainboard()) {
            setWifiSignalQualityImpl(i);
        }
    }

    public boolean isViewOnlyMeeting() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isViewOnlyMeetingImpl();
    }

    public int getViewOnlyUserCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getViewOnlyUserCountImpl();
    }

    public boolean isShareLocked() {
        return isShareLockedImpl();
    }

    public boolean disabledAttendeeUnmuteSelf() {
        return disabledAttendeeUnmuteSelfImpl();
    }

    public int getCurrentVendor() {
        return getCurrentVendorImpl();
    }

    public int getLastNetworkErrorCode() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getLastNetworkErrorCodeImpl();
    }

    public boolean sendParingCode(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendParingCodeImpl(str);
    }

    public boolean changeUserNameByID(@Nullable String str, long j) {
        if (isInitialForMainboard() && str != null) {
            return changeUserNameByIDImpl(str, j);
        }
        return false;
    }

    public void setPlayChimeOnOff(boolean z) {
        if (isInitialForMainboard()) {
            setPlayChimeOnOffImpl(z);
        }
    }

    public boolean isPlayChimeOn() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPlayChimeOnImpl();
    }

    public int getAuthInfo(int i, String str, int i2, @Nullable String[] strArr, @Nullable String[] strArr2) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str) || i2 <= 0 || strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0) {
            return 0;
        }
        return getAuthInfoImpl(i, str, i2, strArr, strArr2);
    }

    public void setLanguageID(String str) {
        if (isInitialForMainboard()) {
            setLanguageIDImpl(str);
        }
    }

    public void setConnectAudioDialogShowStatus(boolean z) {
        if (isInitialForMainboard()) {
            setConnectAudioDialogShowStatusImpl(z);
        }
    }

    public void setLanguageIdAsSystemConfiguration() {
        Locale localDefault = CompatUtils.getLocalDefault();
        StringBuilder sb = new StringBuilder();
        sb.append(localDefault.getLanguage());
        sb.append("-");
        sb.append(localDefault.getCountry());
        setLanguageID(sb.toString());
    }

    public boolean promotePanelist(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return promotePanelistImpl(str);
        }
        return false;
    }

    public boolean downgradeToAttendee(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return downgradeToAttendeeImpl(str);
        }
        return false;
    }

    public boolean isUserOriginalorAltHost(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return isUserOriginalorAltHostImpl(str);
        }
        return false;
    }

    public boolean isPublicGmailUser() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPublicGmailUserImpl();
    }

    public boolean canUnmuteMyVideo() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return canUmmuteMyVideoImpl();
    }

    public boolean expelAttendee(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return expelAttendeeImpl(str);
    }

    public void setPutOnHoldOnEntry(boolean z) {
        if (isInitialForMainboard()) {
            setPutOnHoldOnEntryImpl(z);
        }
    }

    public boolean isPutOnHoldOnEntryOn() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPutOnHoldOnEntryOnImpl();
    }

    public boolean isPutOnHoldOnEntryLocked() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPutOnHoldOnEntryLockedImpl();
    }

    @Nullable
    public ConfChatMessage getChatMessageItemByID(String str) {
        if (!isInitialForMainboard()) {
            return null;
        }
        long chatMessageItemByIDImpl = getChatMessageItemByIDImpl(str);
        if (chatMessageItemByIDImpl == 0) {
            return null;
        }
        return new ConfChatMessage(chatMessageItemByIDImpl);
    }

    public boolean sendXmppChatToAllPanelists(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendXmppChatToAllPanelistsImpl(str);
    }

    public boolean sendXmppChatToIndividual(String str, String str2, boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendXmppChatToIndividualImpl(str, str2, z);
    }

    public boolean isAllowAttendeeChat() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAllowAttendeeChatImpl();
    }

    public void updateChattedAttendees() {
        if (isInitialForMainboard()) {
            updateChattedAttendeesImpl();
        }
    }

    public boolean verifyHostKey(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return verifyHostKeyImpl(str);
        }
        return false;
    }

    public boolean mmrMonitorLog(String str, @Nullable String str2) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str) && str2 != null) {
            return mmrMonitorLogImpl(str, str2);
        }
        return false;
    }

    public boolean isAllowAskQuestionAnonymously() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAllowAskQuestionAnonymouslyImpl();
    }

    public boolean needPreviewVideoWhenStartMeeting() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return needPreviewVideoWhenStartMeetingImpl();
    }

    @Nullable
    public CmmAttentionTrackMgr getAttentionTrackAPI() {
        CmmAttentionTrackMgr cmmAttentionTrackMgr = this.mATMgr;
        if (cmmAttentionTrackMgr != null) {
            return cmmAttentionTrackMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long attentionTrackAPIImpl = getAttentionTrackAPIImpl();
        if (attentionTrackAPIImpl == 0) {
            return null;
        }
        this.mATMgr = new CmmAttentionTrackMgr(attentionTrackAPIImpl);
        this.mATMgr.setEventSink(AttentionTrackEventSinkUI.getInstance());
        return this.mATMgr;
    }

    public int getClientWithoutOnHoldUserCount(boolean z) {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getClientWithoutOnHoldUserCountImpl(z);
    }

    private boolean isInBOMeeting() {
        BOMgr bOMgr = getInstance().getBOMgr();
        if (bOMgr != null) {
            return bOMgr.isInBOMeeting();
        }
        return false;
    }

    public boolean isUserOnHold(@Nullable CmmUser cmmUser) {
        return cmmUser != null && !cmmUser.isMMRUser() && (isInBOMeeting() || !cmmUser.isInBOMeeting()) && cmmUser.inSilentMode();
    }

    @NonNull
    public List<CmmUser> getClientOnHoldUserList() {
        ArrayList arrayList = new ArrayList();
        CmmUserList userList = getUserList();
        if (userList == null) {
            return arrayList;
        }
        int userCount = userList.getUserCount();
        for (int i = 0; i < userCount; i++) {
            CmmUser userAt = userList.getUserAt(i);
            if (isUserOnHold(userAt)) {
                arrayList.add(userAt);
            }
        }
        return arrayList;
    }

    public void setShowClockInMeeting(boolean z) {
        if (isInitialForMainboard()) {
            setShowClockInMeetingImpl(z);
        }
    }

    public boolean isShowClockEnable() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isShowClockEnableImpl();
    }

    public void tryUpgradeThisFreeMeeting() {
        if (isInitialForMainboard()) {
            tryUpgradeThisFreeMeetingImpl();
        }
    }

    public void checkCMRPrivilege() {
        if (isInitialForMainboard()) {
            checkCMRPrivilegeImpl();
        }
    }

    public boolean isViewOnlyClientOnMMR() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isViewOnlyClientOnMMRImpl();
    }

    public int getVideoUserCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getVideoUserCountImpl();
    }

    public boolean handleE2EMeetingExternalSessionKeyReady(@Nullable Map<String, E2EMeetingExternalSessionKey> map, boolean z) {
        if (!isInitialForMainboard() || map == null || map.size() <= 0) {
            return false;
        }
        String[] strArr = (String[]) map.keySet().toArray(new String[map.size()]);
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            iArr[i] = Integer.parseInt(strArr[i]);
        }
        return handleE2EMeetingExternalSessionKeyReadyImpl(iArr, (E2EMeetingExternalSessionKey[]) map.values().toArray(new E2EMeetingExternalSessionKey[map.size()]), z);
    }

    @Nullable
    public byte[] getE2EMeetingSecureKey() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getE2EMeetingSecureKeyImpl();
    }

    public int getWaitingRoomLayoutType() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getWaitingRoomLayoutTypeImpl();
    }

    @Nullable
    public String getWaitingRoomLayoutTitle() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getWaitingRoomLayoutTitleImpl();
    }

    @Nullable
    public String getWaitingRoomLayoutDescription() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getWaitingRoomLayoutDescriptionImpl();
    }

    @Nullable
    public String getWaitingRoomLayoutImagePath() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getWaitingRoomLayoutImagePathImpl();
    }

    public boolean isWaitingRoomLayoutReady() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isWaitingRoomLayoutReadyImpl();
    }

    public boolean confirmGDPR(boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return confirmGDPRImpl(z);
    }

    public boolean isAllowAttendeeViewAllQuestion() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAllowAttendeeViewAllQuestionImpl();
    }

    public boolean isAllowAttendeeUpvoteQuestion() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAllowAttendeeUpvoteQuestionImpl();
    }

    public boolean isAllowAttendeeAnswerQuestion() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAllowAttendeeAnswerQuestionImpl();
    }

    public boolean isJoinWithOutAudio() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isJoinWithOutAudioImpl();
    }

    public int getPureCallinUserCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getPureCallinUserCountImpl();
    }

    @Nullable
    public MonitorLogService getMonitorLogService() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long monitorLogServiceImpl = getMonitorLogServiceImpl();
        if (monitorLogServiceImpl == 0) {
            return null;
        }
        return new MonitorLogService(monitorLogServiceImpl);
    }

    private boolean isInitialForMainboard() {
        Mainboard mainboard = Mainboard.getMainboard();
        return mainboard != null && mainboard.isInitialized();
    }

    @Nullable
    public String getWebDomain(boolean z) {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getWebDomainImpl(z);
    }

    public boolean admitAllSilentUsersIntoMeeting() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return admitAllSilentUsersIntoMeetingImpl();
    }

    public boolean inviteRoomSystemByCallout(InviteRoomDeviceInfo inviteRoomDeviceInfo) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return inviteRoomSystemByCalloutImpl(inviteRoomDeviceInfo);
    }

    public void initInterpretation() {
        getInterpretationObj();
    }

    @Nullable
    public ZoomMdmPolicyProvider getZoomMdmPolicyProvider() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = this.mZoomMdmPolicyProvider;
        if (zoomMdmPolicyProvider != null) {
            return zoomMdmPolicyProvider;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long policyProviderHandleImpl = getPolicyProviderHandleImpl();
        if (policyProviderHandleImpl == 0) {
            return null;
        }
        this.mZoomMdmPolicyProvider = new ZoomMdmPolicyProvider(policyProviderHandleImpl);
        return this.mZoomMdmPolicyProvider;
    }

    public boolean loginWhenInWaitingRoom() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return loginWhenInWaitingRoomImpl();
    }

    public boolean continueJoinAsGuest() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return continueJoinAsGuestImpl();
    }

    public boolean loginToJoinMeetingForGuest() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return loginToJoinMeetingForGuestImpl();
    }

    public boolean bindTelephoneUser(long j, long j2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return bindTelephoneUserImpl(j, j2);
    }

    public boolean unbindTelephoneUser(long j) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return unbindTelephoneUserImpl(j);
    }

    public boolean sendChatToSilentModeUsers(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendChatToSilentModeUsersImpl(str);
    }

    @Nullable
    public String getSignUpUrlForRealNameAuth() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getSignUpUrlForRealNameAuthImpl();
    }

    @Nullable
    public String getBindPhoneUrlForRealNameAuth() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getBindPhoneUrlForRealNameAuthImpl();
    }

    @Nullable
    public InterpretationMgr getInterpretationObj() {
        InterpretationMgr interpretationMgr = this.mInterpretationMgr;
        if (interpretationMgr != null) {
            return interpretationMgr;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long interpretationObjImpl = getInterpretationObjImpl();
        if (interpretationObjImpl == 0) {
            return null;
        }
        this.mInterpretationMgr = new InterpretationMgr(interpretationObjImpl);
        this.mInterpretationMgr.setEventSink(InterpretationSinkUI.getInstance());
        return this.mInterpretationMgr;
    }

    public void onUserConfirmVideoPrivacy(boolean z) {
        onUserConfirmVideoPrivacyImpl(z);
    }

    public int getClosedCaptionMessageCount() {
        return getClosedCaptionMessageCountImpl();
    }

    @Nullable
    public CCMessage getCCMessageAt(int i) {
        if (!isInitialForMainboard()) {
            return null;
        }
        try {
            CCMessage parseFrom = CCMessage.parseFrom(getCCMessageItemAtProtoData(i));
            if (!StringUtil.isEmptyOrNull(parseFrom.getId())) {
                return parseFrom;
            }
            return null;
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean requestRealNameAuthSMS(String str, String str2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return requestRealNameAuthSMSImpl(str, str2);
    }

    public void onUserConfirmRealNameAuth(String str, String str2, String str3) {
        if (isInitialForMainboard()) {
            onUserConfirmRealNameAuthImpl(str, str2, str3);
        }
    }

    public boolean loginToJoinMeetingForRealNameAuth() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return loginToJoinMeetingForRealNameAuthImpl();
    }

    public boolean setMeetingTopic(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return setMeetingTopicImpl(str);
    }

    @Nullable
    public String getMeetingTopic() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getMeetingTopicImpl();
    }

    public void onUserConfirmOptionalVanityURLs(String str) {
        onUserConfirmOptionalVanityURLsImpl(str);
    }

    public boolean changeAttendeeNamebyJID(String str, String str2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return changeAttendeeNamebyJIDImpl(str, str2);
    }

    public boolean sendEmojiReaction(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendEmojiReactionImpl(str);
    }

    public boolean sendEmojiReaction(int i, int i2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendEmojiReactionTypeImpl(i, i2);
    }

    public boolean loginToJoinMeeting() {
        return loginToJoinMeetingImpl();
    }

    public void agreeChinaMeetingPrivacy() {
        if (isInitialForMainboard()) {
            agreeChinaMeetingPrivacyImpl();
        }
    }

    public void agreeJoinMeetingDisclaimer() {
        if (isInitialForMainboard()) {
            agreeJoinMeetingDisclaimerImpl();
        }
    }

    public void agreeStartRecordingDisclaimer() {
        if (isInitialForMainboard()) {
            agreeStartRecordingDisclaimerImpl();
        }
    }
}
