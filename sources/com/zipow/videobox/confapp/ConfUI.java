package com.zipow.videobox.confapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.box.androidsdk.content.models.BoxUser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfAppProtos.CCMessage;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p009bo.BOObject;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.kubi.KubiServiceManager;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.NotificationMgr;
import java.util.Locale;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.DeviceInfoUtil;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.videomeetings.C4558R;

public final class ConfUI implements IHeadsetConnectionListener {
    private static final String TAG = "ConfUI";
    @Nullable
    private static ConfUI instance;
    private long mActiveSpeaker = 0;
    private long mActiveVideo = 0;
    private long mConfFailedReason = 0;
    private int mCurAudioSourceType = 0;
    @NonNull
    private Handler mHandler = new Handler();
    @NonNull
    ListenerList mIConfInnerEventListeners = new ListenerList();
    @NonNull
    private ListenerList mIEmojiReactionListeners = new ListenerList();
    @NonNull
    ListenerList mIRealNameAuthSMSListeners = new ListenerList();
    private boolean mIsAudioMutedByCallOffHook = false;
    private boolean mIsAudioMutedByPauseAudio = false;
    private boolean mIsAudioReady = false;
    private boolean mIsAudioStoppedByCallOffHook = false;
    private boolean mIsAudioStoppedByPauseAudio = false;
    private boolean mIsCallOffHook = false;
    private boolean mIsLaunchConfParamReady = false;
    private boolean mIsLeaveComplete = false;
    private boolean mIsLeavingConference = false;
    private boolean mIsMeetingEndMessageDisabled = false;
    private boolean mIsMeetingInfoReady = false;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mLockedUserId = 0;
    @NonNull
    private ListenerList mNewIncomingCallEventListener = new ListenerList();
    private int mPTAskToLeave = -1;
    private PhoneStateListener mPhoneStateListener;
    private int mPreferAudioType = -1;
    @NonNull
    private ListenerList mRoomCallListenerList = new ListenerList();
    private int mSwitchableAudioSourceType = 0;
    @NonNull
    private ListenerList mVideoFECCCmdListenerList = new ListenerList();
    @NonNull
    private ListenerList mWaitPtLoginResultListenerList = new ListenerList();
    private boolean mbBOStatusChanging = false;
    private boolean mbCMRNotificationDisplayed = false;
    private boolean mbEndForCallDeclined = false;
    private boolean mbHasCMR = false;
    private boolean mbHasOneStartVideo = false;
    private boolean mbOriginalHost = false;
    private boolean mbRingerModeChangedByZoom = false;
    @Nullable
    private BroadcastReceiver ringerModeReceiver = null;

    public interface IConfInnerEventListener extends IListener {
        void onInnerEventAction(int i, int i2);
    }

    public interface IConfUIListener extends IListener {
        void onAnnotateShutDown();

        void onAnnotateStartedUp(boolean z, long j);

        void onAudioSourceTypeChanged(int i);

        void onCallTimeOut();

        boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3);

        void onCheckCMRPrivilege(int i, boolean z);

        void onClosedCaptionMessageReceived(String str, String str2, long j);

        boolean onConfStatusChanged(int i);

        boolean onConfStatusChanged2(int i, long j);

        void onDeviceStatusChanged(int i, int i2);

        void onHostBindTelNotification(long j, long j2, boolean z);

        void onJoinConfConfirmMeetingInfo(boolean z, boolean z2, boolean z3);

        void onJoinConfConfirmMeetingStatus(boolean z, boolean z2);

        void onJoinConfConfirmPasswordValidateResult(boolean z, boolean z2);

        void onJoinConfVerifyMeetingInfo(int i);

        boolean onJoinConf_ConfirmMultiVanityURLs();

        void onLaunchConfParamReady();

        void onLeavingSilentModeStatusChanged(long j, boolean z);

        void onLiveTranscriptionClosedCaptionMessageReceived(CCMessage cCMessage, int i);

        void onPTAskToLeave(int i);

        boolean onPTInvitationSent(String str);

        void onPTInviteRoomSystemResult(boolean z, String str, String str2, String str3, int i, int i2);

        void onRealtimeClosedCaptionMessageReceived(String str);

        void onUpgradeThisFreeMeeting(int i);

        boolean onUserEvent(int i, long j, int i2);

        boolean onUserStatusChanged(int i, long j, int i2);

        void onVerifyMyGuestRoleResult(boolean z, boolean z2);

        void onWBPageChanged(int i, int i2, int i3, int i4);

        void onWebinarNeedRegister(boolean z);
    }

    public interface IEmojiReactionListener extends IListener {
        void onEmojiReactionReceived(long j, int i, int i2);

        void onEmojiReactionReceived(long j, String str);
    }

    public interface INewIncomingCallEventListener extends IListener {
        void onNewIncomingCallCanceled(long j);
    }

    public interface IPtLoginResultEventListener extends IListener {
        void onPtLoginResultEvent(boolean z, String str, String str2);
    }

    public interface IRealNameAuthEventListener extends IListener {
        void onRequestRealNameAuthSMS(int i);

        void onVerifyRealNameAuthResult(int i, int i2);
    }

    public interface IRoomSystemCallEventListener extends IListener {
        void onRoomSystemCallEvent(int i, long j, boolean z);
    }

    public interface IVideoFECCCmdListener extends IListener {
        void onVideoFECCCmd(int i, SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO ssb_mc_data_block_fecc_talk_right_info);
    }

    public static abstract class SimpleConfUIListener implements IConfUIListener {
        public void onAnnotateShutDown() {
        }

        public void onAnnotateStartedUp(boolean z, long j) {
        }

        public void onAudioSourceTypeChanged(int i) {
        }

        public void onCallTimeOut() {
        }

        public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
            return false;
        }

        public void onCheckCMRPrivilege(int i, boolean z) {
        }

        public void onClosedCaptionMessageReceived(String str, String str2, long j) {
        }

        public boolean onConfStatusChanged(int i) {
            return false;
        }

        public boolean onConfStatusChanged2(int i, long j) {
            return false;
        }

        public void onDeviceStatusChanged(int i, int i2) {
        }

        public void onHostBindTelNotification(long j, long j2, boolean z) {
        }

        public void onJoinConfConfirmMeetingInfo(boolean z, boolean z2, boolean z3) {
        }

        public void onJoinConfConfirmMeetingStatus(boolean z, boolean z2) {
        }

        public void onJoinConfConfirmPasswordValidateResult(boolean z, boolean z2) {
        }

        public void onJoinConfVerifyMeetingInfo(int i) {
        }

        public boolean onJoinConf_ConfirmMultiVanityURLs() {
            return false;
        }

        public void onLaunchConfParamReady() {
        }

        public void onLeavingSilentModeStatusChanged(long j, boolean z) {
        }

        public void onLiveTranscriptionClosedCaptionMessageReceived(CCMessage cCMessage, int i) {
        }

        public void onPTAskToLeave(int i) {
        }

        public boolean onPTInvitationSent(String str) {
            return true;
        }

        public void onPTInviteRoomSystemResult(boolean z, String str, String str2, String str3, int i, int i2) {
        }

        public void onRealtimeClosedCaptionMessageReceived(String str) {
        }

        public void onUpgradeThisFreeMeeting(int i) {
        }

        public boolean onUserEvent(int i, long j, int i2) {
            return false;
        }

        public boolean onUserStatusChanged(int i, long j, int i2) {
            return false;
        }

        public void onVerifyMyGuestRoleResult(boolean z, boolean z2) {
        }

        public void onWBPageChanged(int i, int i2, int i3, int i4) {
        }

        public void onWebinarNeedRegister(boolean z) {
        }
    }

    private native void nativeInit();

    private ConfUI() {
    }

    public void initialize() {
        nativeInit();
        startTelephoneMonitoring();
        HeadsetUtil.getInstance().addListener(this);
    }

    @NonNull
    public static synchronized ConfUI getInstance() {
        ConfUI confUI;
        synchronized (ConfUI.class) {
            if (instance == null) {
                instance = new ConfUI();
            }
            confUI = instance;
        }
        return confUI;
    }

    public boolean isLaunchConfParamReady() {
        return this.mIsLaunchConfParamReady;
    }

    public boolean isMeetingInfoReady() {
        return this.mIsMeetingInfoReady;
    }

    public long getActiveVideo() {
        return this.mActiveVideo;
    }

    public long getActiveSpeaker() {
        return this.mActiveSpeaker;
    }

    public void setLockedUserId(long j) {
        this.mLockedUserId = j;
    }

    public long getLockedUserId() {
        return this.mLockedUserId;
    }

    public boolean isLeaveComplete() {
        return this.mIsLeaveComplete;
    }

    public void setCMRNotifiationDisplayed(boolean z) {
        this.mbCMRNotificationDisplayed = z;
    }

    public boolean isCMRNotificationDisplayed() {
        return this.mbCMRNotificationDisplayed;
    }

    public void addListener(@Nullable IConfUIListener iConfUIListener) {
        if (iConfUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            boolean z = all.length == 0 && this.mConfFailedReason != 0;
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iConfUIListener) {
                    removeListener((IConfUIListener) all[i]);
                }
            }
            this.mListenerList.add(iConfUIListener);
            if (z) {
                iConfUIListener.onConfStatusChanged2(2, this.mConfFailedReason);
            }
        }
    }

    public void removeListener(IConfUIListener iConfUIListener) {
        this.mListenerList.remove(iConfUIListener);
    }

    public void addVideoFECCCmdListener(IVideoFECCCmdListener iVideoFECCCmdListener) {
        this.mVideoFECCCmdListenerList.add(iVideoFECCCmdListener);
    }

    public void removeVideoFECCCmdListener(IVideoFECCCmdListener iVideoFECCCmdListener) {
        this.mVideoFECCCmdListenerList.remove(iVideoFECCCmdListener);
    }

    public void addRoomCallStatusListener(@Nullable IRoomSystemCallEventListener iRoomSystemCallEventListener) {
        if (iRoomSystemCallEventListener != null) {
            this.mRoomCallListenerList.add(iRoomSystemCallEventListener);
        }
    }

    public void removeRoomCallStatusListener(IRoomSystemCallEventListener iRoomSystemCallEventListener) {
        this.mRoomCallListenerList.remove(iRoomSystemCallEventListener);
    }

    public void addINewIncomingCallEventListener(@Nullable INewIncomingCallEventListener iNewIncomingCallEventListener) {
        if (iNewIncomingCallEventListener != null) {
            this.mNewIncomingCallEventListener.add(iNewIncomingCallEventListener);
        }
    }

    public void removeINewIncomingCallEventListener(INewIncomingCallEventListener iNewIncomingCallEventListener) {
        this.mNewIncomingCallEventListener.remove(iNewIncomingCallEventListener);
    }

    public void addIRealNameAuthEventListener(@Nullable IRealNameAuthEventListener iRealNameAuthEventListener) {
        if (iRealNameAuthEventListener != null) {
            this.mIRealNameAuthSMSListeners.add(iRealNameAuthEventListener);
        }
    }

    public void removeIRealNameAuthEventListener(IRealNameAuthEventListener iRealNameAuthEventListener) {
        this.mIRealNameAuthSMSListeners.remove(iRealNameAuthEventListener);
    }

    public void addIConfInnerEventListener(@Nullable IConfInnerEventListener iConfInnerEventListener) {
        if (iConfInnerEventListener != null) {
            this.mIConfInnerEventListeners.add(iConfInnerEventListener);
        }
    }

    public void removeIConfInnerEventListener(IConfInnerEventListener iConfInnerEventListener) {
        this.mIConfInnerEventListeners.remove(iConfInnerEventListener);
    }

    public void addIEmojiReactionListener(@NonNull IEmojiReactionListener iEmojiReactionListener) {
        this.mIEmojiReactionListeners.add(iEmojiReactionListener);
    }

    public void removeIEmojiReactionListener(@NonNull IEmojiReactionListener iEmojiReactionListener) {
        this.mIEmojiReactionListeners.remove(iEmojiReactionListener);
    }

    public void addWaitPtLoginResultListener(@Nullable IPtLoginResultEventListener iPtLoginResultEventListener) {
        if (iPtLoginResultEventListener != null) {
            this.mWaitPtLoginResultListenerList.add(iPtLoginResultEventListener);
        }
    }

    public void removeWaitPtLoginResultListener(@Nullable IPtLoginResultEventListener iPtLoginResultEventListener) {
        this.mWaitPtLoginResultListenerList.remove(iPtLoginResultEventListener);
    }

    /* access modifiers changed from: protected */
    public boolean onConfStatusChanged(int i) {
        try {
            return onConfStatusChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onConfStatusChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onConfStatusChanged(i);
            }
        }
        if (!this.mbBOStatusChanging && i == 10) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ConfUI.this.boStatusChangeComplete();
                }
            }, 2000);
        }
        if (i == 12) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                Locale localDefault = CompatUtils.getLocalDefault();
                String language = localDefault.getLanguage();
                String country = localDefault.getCountry();
                StringBuilder sb = new StringBuilder();
                sb.append(language);
                sb.append("-");
                sb.append(country);
                confStatusObj.setLangcode(sb.toString());
            }
            NotificationMgr.showConfNotification();
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null) {
                qAComponent.setZoomQAUI(ZoomQAUI.getInstance());
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                this.mbOriginalHost = confContext.getOrginalHost();
            }
            boStatusChangeComplete();
            reportOSArchInfo();
        } else if (i == 21) {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
            if (all == null || all.length == 0) {
                VideoBoxApplication.getInstance().killConfProcess();
            }
        } else if (i == 18) {
            boStatusChangeStart(true);
        } else if (i == 19) {
            boStatusChangeStart(false);
        }
        return true;
    }

    private void boStatusChangeStart(boolean z) {
        int i;
        this.mbBOStatusChanging = true;
        if (z) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                i = confContext.getBOJoinReason();
                IPCHelper.getInstance().sendBOStatusChangeStart(z, i, getBoMeetingName());
            }
        }
        i = 0;
        IPCHelper.getInstance().sendBOStatusChangeStart(z, i, getBoMeetingName());
    }

    @NonNull
    private String getBoMeetingName() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        if (bOMgr == null) {
            return "";
        }
        BOObject myBOObject = bOMgr.getMyBOObject(1);
        if (myBOObject == null) {
            return "";
        }
        return myBOObject.getMeetingName();
    }

    /* access modifiers changed from: private */
    public void boStatusChangeComplete() {
        this.mbBOStatusChanging = false;
        IPCHelper.getInstance().sendBOStatusChangeComplete();
    }

    public boolean inSilentMode() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            return confContext.inSilentMode();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onConfStatusChanged2(int i, long j) {
        try {
            return onConfStatusChanged2Impl(i, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onConfStatusChanged2Impl(int i, long j) {
        if (!(!inSilentMode() || i == 8 || i == 39 || i == 104 || i == 136)) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                    break;
                default:
                    return false;
            }
        }
        if (i == 1) {
            this.mIsLeaveComplete = true;
        }
        if (i == 141) {
            ConfMgr.getInstance().getConfDataHelper().setmEnableAdmitAll(j != 1);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onConfStatusChanged2(i, j);
            }
        }
        if (i == 105) {
            IPCHelper.getInstance().sendCallOutStatus((int) j);
        }
        if (i == 1) {
            onConfLeaveComplete(j);
        } else if (i == 5) {
            this.mIsAudioReady = true;
            checkOpenLoudSpeaker();
        } else if (i == 2) {
            this.mConfFailedReason = j;
            boStatusChangeComplete();
        } else if (i == 17) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && !this.mbHasOneStartVideo && !ConfMgr.getInstance().noOneIsSendingVideo()) {
                this.mbHasOneStartVideo = true;
                if (confContext.isAudioOnlyMeeting()) {
                    this.mbHasOneStartVideo = true;
                    AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                    if (audioObj != null) {
                        audioObj.setPreferedLoudSpeakerStatus(-1);
                        checkOpenLoudSpeaker();
                    }
                }
            }
        } else if (i == 79) {
            RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (!(recordMgr == null || confStatusObj == null || ((!recordMgr.isCMRInProgress() || confStatusObj.isCMRInConnecting()) && !recordMgr.recordingMeetingOnCloud()))) {
                this.mbHasCMR = true;
            }
        } else if (i == 45) {
            ConfLocalHelper.stopRecord(true);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x007e  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x015a  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0174  */
    /* JADX WARNING: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onConfLeaveComplete(long r10) {
        /*
            r9 = this;
            com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper r0 = new com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper
            r0.<init>()
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r2 = "PREFERENCE_PROVIDER_DEFAULT_SP_NAME"
            com.zipow.videobox.VideoBoxApplication r3 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r3 = com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper.getGoodQualityMeetingTimesKey(r3)
            r4 = 0
            int r1 = p021us.zoom.androidlib.app.preference.ZMPreferencesStoreUtils.getInt(r1, r2, r3, r4, r4)
            r2 = 3
            r3 = 1
            if (r1 >= r2) goto L_0x006b
            r5 = 2
            int r5 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r5 == 0) goto L_0x0028
            r5 = 0
            int r5 = (r10 > r5 ? 1 : (r10 == r5 ? 0 : -1))
            if (r5 != 0) goto L_0x005a
        L_0x0028:
            int r5 = r0.getMeetingScore()
            r6 = 90
            if (r5 <= r6) goto L_0x005a
            long r5 = r0.getMeetingElapsedMinute()
            r7 = 10
            int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r0 < 0) goto L_0x006b
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r5 = "PREFERENCE_PROVIDER_DEFAULT_SP_NAME"
            com.zipow.videobox.VideoBoxApplication r6 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r6 = com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper.getGoodQualityMeetingTimesKey(r6)
            int r1 = r1 + r3
            p021us.zoom.androidlib.app.preference.ZMPreferencesStoreUtils.putInt(r0, r5, r6, r1, r4)
            if (r1 != r2) goto L_0x006b
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r0 = p021us.zoom.androidlib.util.AndroidAppUtil.hasGooglePlayStoreApp(r0)
            if (r0 == 0) goto L_0x006b
            r0 = 1
            goto L_0x006c
        L_0x005a:
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r1 = "PREFERENCE_PROVIDER_DEFAULT_SP_NAME"
            com.zipow.videobox.VideoBoxApplication r5 = com.zipow.videobox.VideoBoxApplication.getInstance()
            java.lang.String r5 = com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper.getGoodQualityMeetingTimesKey(r5)
            p021us.zoom.androidlib.app.preference.ZMPreferencesStoreUtils.putInt(r0, r1, r5, r4, r4)
        L_0x006b:
            r0 = 0
        L_0x006c:
            int r10 = (int) r10
            r11 = 0
            switch(r10) {
                case 1: goto L_0x013b;
                case 2: goto L_0x00f0;
                case 3: goto L_0x013b;
                case 4: goto L_0x00a6;
                case 5: goto L_0x0071;
                case 6: goto L_0x013b;
                case 7: goto L_0x007e;
                default: goto L_0x0071;
            }
        L_0x0071:
            boolean r10 = r9.mbEndForCallDeclined
            if (r10 != 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showLeavingMessage(r10, r11)
            goto L_0x0158
        L_0x007e:
            boolean r1 = r9.mIsMeetingEndMessageDisabled
            if (r1 != 0) goto L_0x0093
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.confapp.ConfMgr r1 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            int r1 = r1.getLastNetworkErrorCode()
            com.zipow.videobox.MeetingEndMessageActivity.showMeetingEndedMessage(r11, r10, r1)
            goto L_0x0158
        L_0x0093:
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r10 = r10.isSDKMode()
            if (r10 == 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showLeavingMessage(r10, r11)
            goto L_0x0158
        L_0x00a6:
            boolean r1 = r9.mIsMeetingEndMessageDisabled
            if (r1 != 0) goto L_0x00de
            com.zipow.videobox.confapp.ConfMgr r11 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r11 = r11.getConfContext()
            if (r11 == 0) goto L_0x00d2
            boolean r1 = r11.getOrginalHost()
            if (r1 == 0) goto L_0x00d2
            int r1 = r11.getGiftMeetingCount()
            java.lang.String r11 = r11.getUpgradeUrl()
            if (r1 <= 0) goto L_0x00d2
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
            if (r2 != 0) goto L_0x00d2
            com.zipow.videobox.VideoBoxApplication r2 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showFreeMeetingTimeOutForOriginalHost(r2, r1, r11)
            goto L_0x00d3
        L_0x00d2:
            r3 = 0
        L_0x00d3:
            if (r3 != 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showMeetingEndedMessage(r11, r10)
            goto L_0x0158
        L_0x00de:
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r10 = r10.isSDKMode()
            if (r10 == 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showLeavingMessage(r10, r11)
            goto L_0x0158
        L_0x00f0:
            boolean r1 = r9.mIsMeetingEndMessageDisabled
            if (r1 != 0) goto L_0x0129
            com.zipow.videobox.confapp.ConfMgr r11 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r11 = r11.getConfContext()
            if (r11 == 0) goto L_0x011e
            boolean r1 = r11.getOrginalHost()
            if (r1 == 0) goto L_0x011e
            int r1 = r11.getGiftMeetingCount()
            java.lang.String r11 = r11.getUpgradeUrl()
            if (r1 <= 0) goto L_0x011e
            if (r1 >= r2) goto L_0x011e
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r11)
            if (r2 != 0) goto L_0x011e
            com.zipow.videobox.VideoBoxApplication r2 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.FreeMeetingEndActivity.show(r2, r1, r11)
            goto L_0x011f
        L_0x011e:
            r3 = 0
        L_0x011f:
            if (r3 != 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showMeetingEndedMessage(r11, r10)
            goto L_0x0158
        L_0x0129:
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r10 = r10.isSDKMode()
            if (r10 == 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showLeavingMessage(r10, r11)
            goto L_0x0158
        L_0x013b:
            boolean r1 = r9.mIsMeetingEndMessageDisabled
            if (r1 != 0) goto L_0x0147
            com.zipow.videobox.VideoBoxApplication r11 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showMeetingEndedMessage(r11, r10)
            goto L_0x0158
        L_0x0147:
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            boolean r10 = r10.isSDKMode()
            if (r10 == 0) goto L_0x0158
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getInstance()
            com.zipow.videobox.MeetingEndMessageActivity.showLeavingMessage(r10, r11)
        L_0x0158:
            if (r0 == 0) goto L_0x0169
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            com.zipow.videobox.IPTService r10 = r10.getPTService()
            if (r10 == 0) goto L_0x0169
            r10.showRateZoomDialog()     // Catch:{ Exception -> 0x0168 }
            goto L_0x0169
        L_0x0168:
        L_0x0169:
            r9.stopListenPhoneState()
            us.zoom.androidlib.util.ListenerList r10 = r9.mListenerList
            int r10 = r10.size()
            if (r10 != 0) goto L_0x0182
            com.zipow.videobox.confapp.ConfMgr r10 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            r10.cleanupConf()
            com.zipow.videobox.VideoBoxApplication r10 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            r10.stopConfService()
        L_0x0182:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.ConfUI.onConfLeaveComplete(long):void");
    }

    public boolean isDisplayAsHost(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return false;
        }
        return confStatusObj.isMasterConfHost(j);
    }

    public boolean isDisplayAsCohost(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        boolean z = false;
        if (confStatusObj == null || j == 0 || confStatusObj.isMasterConfHost(j)) {
            return false;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById == null) {
            return false;
        }
        if (userById.isCoHost() || userById.isBOModerator()) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean onUserStatusChanged(int i, long j, int i2) {
        try {
            return onUserStatusChangedImpl(i, j, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onUserStatusChangedImpl(int i, long j, int i2) {
        if (inSilentMode()) {
            return false;
        }
        if (i == 4) {
            onUserVideoStatus(j);
        } else if (i == 6) {
            if (ConfMgr.getInstance().noOneIsSendingVideo()) {
                this.mActiveVideo = j;
            }
            this.mActiveSpeaker = j;
        } else if (i == 10) {
            this.mActiveVideo = j;
        } else if (i == 21) {
            onUserAudioTypeChanged(j);
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onUserStatusChanged(i, j, i2);
            }
        }
        return true;
    }

    private void onUserAudioTypeChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
            if (audioObj != null && confStatusObj.isMyself(j)) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                    if (audioStatusObj != null) {
                        long audiotype = audioStatusObj.getAudiotype();
                        if (audiotype == 1) {
                            if (this.mIsAudioMutedByCallOffHook && audioStatusObj.getIsMuted()) {
                                audioObj.startAudio();
                                this.mIsAudioMutedByCallOffHook = false;
                            }
                            IPTService pTService = VideoBoxApplication.getNonNullInstance().getPTService();
                            if (pTService != null) {
                                try {
                                    pTService.disablePhoneAudio();
                                } catch (Exception unused) {
                                }
                            }
                        }
                        if (audiotype == 0) {
                            checkOpenLoudSpeaker();
                        } else {
                            UIUtil.stopProximityScreenOffWakeLock();
                        }
                    }
                }
            }
        }
    }

    private void onUserVideoStatus(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
                if (videoStatusObj != null && videoStatusObj.getIsSending()) {
                    VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                    if (videoObj != null) {
                        videoObj.onMyVideoStarted();
                        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                        if (confContext != null && confContext.isKubiEnabled()) {
                            videoObj.turnKubiDeviceOnOFF(true);
                            KubiServiceManager instance2 = KubiServiceManager.getInstance(VideoBoxApplication.getInstance());
                            if (instance2 != null) {
                                instance2.connectKubiService(true);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onUserEvent(int i, long j, int i2) {
        try {
            return onUserEventImpl(i, j, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onUserEventImpl(int i, long j, int i2) {
        if (inSilentMode()) {
            return false;
        }
        ConfMgr.getInstance().onUserEvent(i, j, i2);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onUserEvent(i, j, i2);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onClosedCaptionMessageReceived(String str, String str2, long j) {
        try {
            return onClosedCaptionMessageReceivedImpl(str, str2, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onClosedCaptionMessageReceivedImpl(String str, String str2, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onClosedCaptionMessageReceived(str, str2, j);
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onRealtimeClosedCaptionMessageReceived(String str) {
        try {
            return onRealtimeClosedCaptionMessageReceivedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onRealtimeClosedCaptionMessageReceivedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onRealtimeClosedCaptionMessageReceived(str);
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean onLiveTranscriptionClosedCaptionMessageReceived(byte[] bArr, int i) {
        try {
            return onLiveTranscriptionClosedCaptionMessageReceivedImpl(CCMessage.parseFrom(bArr), i);
        } catch (InvalidProtocolBufferException unused) {
            return false;
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onLiveTranscriptionClosedCaptionMessageReceivedImpl(CCMessage cCMessage, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onLiveTranscriptionClosedCaptionMessageReceived(cCMessage, i);
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean notifyChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        try {
            return notifyChatMessageReceivedImpl(str, j, str2, j2, str3, str4, j3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean notifyChatMessageReceivedImpl(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IConfUIListener) all[i]).onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onPTAskToLeave(int i) {
        try {
            onPTAskToLeaveImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onPTAskToLeaveImpl(int i) {
        ConfMgr instance2 = ConfMgr.getInstance();
        if (i != 41 || !ConfLocalHelper.isGe2NotCallingOut()) {
            this.mPTAskToLeave = i;
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IConfUIListener) iListener).onPTAskToLeave(i);
                }
            }
            if (i == 0) {
                instance2.notifyConfLeaveReason(String.valueOf(0), true);
                instance2.leaveConference();
            } else if (i == 4) {
                ConfActivity.handleOnPTAskToLeaveForUpdate();
            } else if (i == 8) {
                instance2.notifyConfLeaveReason(String.valueOf(i), true);
                instance2.leaveConference();
            } else if (i == 41) {
                onCallDeclined(instance2);
            }
        }
    }

    private void onCallDeclined(@NonNull ConfMgr confMgr) {
        this.mbEndForCallDeclined = true;
        confMgr.endConference();
    }

    public int getPTAskToLeaveReason() {
        return this.mPTAskToLeave;
    }

    public void clearPTAskToLeaveReason() {
        this.mPTAskToLeave = -1;
    }

    public void setIsLeavingConference(boolean z) {
        this.mIsLeavingConference = true;
    }

    public boolean isLeavingConference() {
        return this.mIsLeavingConference;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_ConfirmMeetingInfo(boolean z, boolean z2, boolean z3) {
        try {
            return joinConf_ConfirmMeetingInfoImpl(z, z2, z3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_ConfirmMeetingInfoImpl(boolean z, boolean z2, boolean z3) {
        this.mIsMeetingInfoReady = true;
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onJoinConfConfirmMeetingInfo(z, z2, z3);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_ConfirmPasswordValidateResult(boolean z, boolean z2) {
        try {
            return joinConf_ConfirmPasswordValidateResultImpl(z, z2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_ConfirmPasswordValidateResultImpl(boolean z, boolean z2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onJoinConfConfirmPasswordValidateResult(z, z2);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_ConfirmMeetingStatus(boolean z, boolean z2) {
        try {
            return joinConf_ConfirmMeetingStatusImpl(z, z2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_ConfirmMeetingStatusImpl(boolean z, boolean z2) {
        this.mIsMeetingInfoReady = true;
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onJoinConfConfirmMeetingStatus(z, z2);
            }
        }
        IPCHelper.getInstance().sendJoinConfConfirmMeetingStatus(z, z2);
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_VerifyMeetingInfo(int i) {
        try {
            return joinConf_VerifyMeetingInfoImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_VerifyMeetingInfoImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onJoinConfVerifyMeetingInfo(i);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_VerifyMeetingInfoResult(int i, int i2) {
        try {
            return joinConf_VerifyMeetingInfoResultImpl(i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_VerifyMeetingInfoResultImpl(int i, int i2) {
        IListener[] all = this.mIRealNameAuthSMSListeners.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IRealNameAuthEventListener) iListener).onVerifyRealNameAuthResult(i, i2);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean joinConf_ConfirmMultiVanityURLs() {
        try {
            return joinConf_ConfirmMultiVanityURLsImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean joinConf_ConfirmMultiVanityURLsImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onJoinConf_ConfirmMultiVanityURLs();
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void notifyCallTimeout() {
        try {
            notifyCallTimeoutImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyCallTimeoutImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onCallTimeOut();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLaunchConfParamReady() {
        try {
            onLaunchConfParamReadyImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onLaunchConfParamReadyImpl() {
        this.mIsLaunchConfParamReady = true;
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onLaunchConfParamReady();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean onDeviceStatusChanged(int i, int i2) {
        try {
            return onDeviceStatusChangedImpl(i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return false;
        }
    }

    private boolean onDeviceStatusChangedImpl(int i, int i2) {
        if (inSilentMode()) {
            return false;
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onDeviceStatusChanged(i, i2);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onWebinarNeedRegister(boolean z) {
        try {
            onWebinarNeedRegisterImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onWebinarNeedRegisterImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onWebinarNeedRegister(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpgradeThisFreeMeeting(int i) {
        try {
            onUpgradeThisFreeMeetingImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUpgradeThisFreeMeetingImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onUpgradeThisFreeMeeting(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCheckCMRPrivilege(int i, boolean z) {
        try {
            onCheckCMRPrivilegeImpl(i, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onCheckCMRPrivilegeImpl(int i, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onCheckCMRPrivilege(i, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onVideoFECCCmd(int i, long j, long j2, long j3, long j4, int i2) {
        SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO ssb_mc_data_block_fecc_talk_right_info = new SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO();
        ssb_mc_data_block_fecc_talk_right_info.commander = j;
        ssb_mc_data_block_fecc_talk_right_info.executive = j2;
        ssb_mc_data_block_fecc_talk_right_info.receiver = j3;
        ssb_mc_data_block_fecc_talk_right_info.camera_index = j4;
        ssb_mc_data_block_fecc_talk_right_info.reason = i2;
        try {
            onVideoFECCCmdImpl(i, ssb_mc_data_block_fecc_talk_right_info);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onVideoFECCCmdImpl(int i, SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO ssb_mc_data_block_fecc_talk_right_info) {
        IListener[] all = this.mVideoFECCCmdListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IVideoFECCCmdListener) iListener).onVideoFECCCmd(i, ssb_mc_data_block_fecc_talk_right_info);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRequestRealNameAuthSMS(int i) {
        try {
            onRequestRealNameAuthSMSImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onRequestRealNameAuthSMSImpl(int i) {
        IListener[] all = this.mIRealNameAuthSMSListeners.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IRealNameAuthEventListener) iListener).onRequestRealNameAuthSMS(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onEmojiReactionReceived(long j, String str) {
        try {
            onEmojiReactionReceivedImpl(j, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onEmojiReactionReceivedImpl(long j, String str) {
        IListener[] all = this.mIEmojiReactionListeners.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IEmojiReactionListener) iListener).onEmojiReactionReceived(j, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLeavingSilentModeStatusChanged(long j, boolean z) {
        try {
            onLeavingSilentModeStatusChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onLeavingSilentModeStatusChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onLeavingSilentModeStatusChanged(j, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onEmojiReactionReceived(long j, int i, int i2) {
        try {
            onEmojiReactionReceivedImpl(j, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onEmojiReactionReceivedImpl(long j, int i, int i2) {
        IListener[] all = this.mIEmojiReactionListeners.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IEmojiReactionListener) iListener).onEmojiReactionReceived(j, i, i2);
            }
        }
    }

    public void handleConfInnerEvent(int i, int i2) {
        IListener[] all = this.mIConfInnerEventListeners.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfInnerEventListener) iListener).onInnerEventAction(i, i2);
            }
        }
    }

    public void onRoomSystemCallStatus(int i, long j, boolean z) {
        IListener[] all = this.mRoomCallListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IRoomSystemCallEventListener) iListener).onRoomSystemCallEvent(i, j, z);
            }
        }
    }

    public void onNewIncomingCallCanceled(long j) {
        IListener[] all = this.mNewIncomingCallEventListener.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INewIncomingCallEventListener) iListener).onNewIncomingCallCanceled(j);
            }
        }
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        if (!this.mIsCallOffHook) {
            ConfMgr instance2 = ConfMgr.getInstance();
            if (instance2.isConfConnected()) {
                AudioSessionMgr audioObj = instance2.getAudioObj();
                if (audioObj != null) {
                    if (z || z2) {
                        audioObj.setPreferedLoudSpeakerStatus(-1);
                    }
                    checkOpenLoudSpeaker();
                    audioObj.notifyHeadsetStatusChanged(z2, z);
                }
            }
        }
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        if (!z) {
            checkOpenLoudSpeaker();
        }
        updateAudioSourceType();
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            audioObj.notifyBluetoothScoAudioStatus(z, this.mIsCallOffHook);
        }
    }

    public void onPtLoginResultEvent(boolean z, String str, String str2) {
        IListener[] all = this.mWaitPtLoginResultListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPtLoginResultEventListener) iListener).onPtLoginResultEvent(z, str, str2);
            }
        }
    }

    private void startTelephoneMonitoring() {
        if (!OsUtil.isAtLeastP() || !DeviceInfoUtil.isInRingerModeWhiteList()) {
            startToListenPhoneState();
        } else {
            startToListenPhoneStateForRingerMode();
        }
    }

    /* access modifiers changed from: private */
    public void ringerModeVibrateIfNormal() {
        if (!HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            try {
                AudioManager audioManager = getAudioManager();
                if (audioManager != null && audioManager.getRingerMode() == 2) {
                    this.mbRingerModeChangedByZoom = true;
                    audioManager.setRingerMode(1);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void ringerModeRevertIfNecessary() {
        if (this.mbRingerModeChangedByZoom) {
            try {
                AudioManager audioManager = getAudioManager();
                if (audioManager != null && audioManager.getRingerMode() == 1) {
                    audioManager.setRingerMode(2);
                    this.mbRingerModeChangedByZoom = false;
                }
            } catch (Exception unused) {
            }
        }
    }

    @Nullable
    private AudioManager getAudioManager() {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            return (AudioManager) globalContext.getSystemService("audio");
        }
        return null;
    }

    public void startToListenPhoneState() {
        TelephonyManager telephonyManager = (TelephonyManager) VideoBoxApplication.getInstance().getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager != null) {
            this.mIsCallOffHook = telephonyManager.getCallState() == 2;
            this.mPhoneStateListener = new PhoneStateListener() {
                public void onDataConnectionStateChanged(int i, int i2) {
                }

                public void onCallStateChanged(int i, String str) {
                    super.onCallStateChanged(i, str);
                    switch (i) {
                        case 0:
                            ConfUI.this.onPhoneCallIdle();
                            return;
                        case 1:
                        case 2:
                            ConfUI.this.onPhoneCallOffHook();
                            return;
                        default:
                            return;
                    }
                }
            };
            try {
                telephonyManager.listen(this.mPhoneStateListener, 96);
            } catch (Exception unused) {
            }
        }
    }

    private void startToListenPhoneStateForRingerMode() {
        TelephonyManager telephonyManager = (TelephonyManager) VideoBoxApplication.getInstance().getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager != null) {
            this.mIsCallOffHook = telephonyManager.getCallState() == 2;
            this.mPhoneStateListener = new PhoneStateListener() {
                public void onDataConnectionStateChanged(int i, int i2) {
                }

                public void onCallStateChanged(int i, String str) {
                    super.onCallStateChanged(i, str);
                    switch (i) {
                        case 0:
                            ConfUI.this.ringerModeRevertIfNecessary();
                            ConfUI.this.onPhoneCallIdle();
                            return;
                        case 1:
                            ConfUI.this.ringerModeVibrateIfNormal();
                            return;
                        case 2:
                            ConfUI.this.ringerModeRevertIfNecessary();
                            ConfUI.this.onPhoneCallOffHook();
                            return;
                        default:
                            return;
                    }
                }
            };
            try {
                telephonyManager.listen(this.mPhoneStateListener, 96);
            } catch (Exception unused) {
            }
        }
    }

    private void stopListenPhoneState() {
        if (this.mPhoneStateListener != null) {
            TelephonyManager telephonyManager = (TelephonyManager) VideoBoxApplication.getInstance().getSystemService(BoxUser.FIELD_PHONE);
            if (telephonyManager != null) {
                try {
                    telephonyManager.listen(this.mPhoneStateListener, 0);
                } catch (Exception unused) {
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onPhoneCallOffHook() {
        this.mIsCallOffHook = true;
        if (!this.mIsAudioStoppedByPauseAudio) {
            ConfMgr instance2 = ConfMgr.getInstance();
            if (instance2.isConfConnected() && this.mIsAudioReady) {
                AudioSessionMgr audioObj = instance2.getAudioObj();
                if (audioObj != null) {
                    if (this.mIsAudioReady) {
                        CmmUser myself = instance2.getMyself();
                        if (myself != null) {
                            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                            if (audioStatusObj != null) {
                                if (audioStatusObj.getAudiotype() == 0 && !audioStatusObj.getIsMuted()) {
                                    ConfLocalHelper.muteAudioByMe(audioObj);
                                    this.mIsAudioMutedByCallOffHook = true;
                                    Toast.makeText(VideoBoxApplication.getInstance(), C4558R.string.zm_msg_audio_stopped_by_call_offhook, 1).show();
                                }
                                audioObj.setLoudSpeakerStatus(false);
                                audioObj.stopPlayout();
                                this.mIsAudioStoppedByCallOffHook = true;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    updateAudioSourceType();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onPhoneCallIdle() {
        this.mIsCallOffHook = false;
        if (this.mIsAudioStoppedByCallOffHook) {
            ConfMgr instance2 = ConfMgr.getInstance();
            if (instance2.isConfConnected() && this.mIsAudioReady) {
                AudioSessionMgr audioObj = instance2.getAudioObj();
                if (audioObj != null) {
                    CmmUser myself = instance2.getMyself();
                    if (myself != null) {
                        audioObj.startPlayout();
                        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                        if (audioStatusObj != null) {
                            if (audioStatusObj.getAudiotype() == 0 && this.mIsAudioMutedByCallOffHook && audioStatusObj.getIsMuted()) {
                                audioObj.startAudio();
                            }
                            this.mIsAudioStoppedByCallOffHook = false;
                            this.mIsAudioMutedByCallOffHook = false;
                            this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    ConfUI.this.checkOpenLoudSpeaker();
                                }
                            }, 2000);
                        }
                    }
                }
            }
        }
    }

    public void pauseAudio() {
        if (!this.mIsAudioStoppedByCallOffHook) {
            if (this.mIsAudioReady) {
                ConfMgr instance2 = ConfMgr.getInstance();
                AudioSessionMgr audioObj = instance2.getAudioObj();
                if (audioObj != null) {
                    CmmUser myself = instance2.getMyself();
                    if (myself != null) {
                        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                        if (audioStatusObj != null) {
                            if (audioStatusObj.getAudiotype() == 0 && !audioStatusObj.getIsMuted()) {
                                audioObj.stopAudio();
                                this.mIsAudioMutedByPauseAudio = true;
                            }
                            audioObj.setLoudSpeakerStatus(false);
                            audioObj.stopPlayout();
                            this.mIsAudioStoppedByPauseAudio = true;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            updateAudioSourceType();
        }
    }

    public void resumeAudio() {
        if (this.mIsAudioStoppedByPauseAudio && this.mIsAudioReady) {
            ConfMgr instance2 = ConfMgr.getInstance();
            AudioSessionMgr audioObj = instance2.getAudioObj();
            if (audioObj != null) {
                CmmUser myself = instance2.getMyself();
                if (myself != null) {
                    audioObj.startPlayout();
                    CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                    if (audioStatusObj != null) {
                        if (audioStatusObj.getAudiotype() == 0 && this.mIsAudioMutedByPauseAudio && audioStatusObj.getIsMuted()) {
                            audioObj.startAudio();
                        }
                        this.mIsAudioStoppedByPauseAudio = false;
                        this.mIsAudioMutedByPauseAudio = false;
                        this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                ConfUI.this.checkOpenLoudSpeaker();
                            }
                        }, 2000);
                    }
                }
            }
        }
    }

    public void checkOpenLoudSpeaker() {
        ConfMgr instance2 = ConfMgr.getInstance();
        AudioSessionMgr audioObj = instance2.getAudioObj();
        if (audioObj != null) {
            boolean z = false;
            if (this.mIsCallOffHook) {
                if (this.mIsAudioReady && !this.mIsAudioStoppedByCallOffHook) {
                    audioObj.setLoudSpeakerStatus(false);
                    CmmUser myself = instance2.getMyself();
                    if (myself != null) {
                        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                        if (audioStatusObj != null && audioStatusObj.getAudiotype() == 0 && !audioStatusObj.getIsMuted()) {
                            audioObj.stopAudio();
                            this.mIsAudioMutedByCallOffHook = true;
                        }
                    }
                    audioObj.stopPlayout();
                    this.mIsAudioStoppedByCallOffHook = true;
                }
            } else if (audioObj.getPreferedLoudSpeakerStatus() == 1) {
                audioObj.stopBluetoothHeadset();
                audioObj.setLoudSpeakerStatus(true);
            } else {
                if (HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !audioObj.ismIsUseA2dpMode()) {
                    int i = this.mPreferAudioType;
                    if (i == 3 || i == -1 || !HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                        audioObj.startBluetoothHeadset();
                        updateAudioSourceType();
                        return;
                    }
                }
                audioObj.stopBluetoothHeadset();
                if (audioObj.getPreferedLoudSpeakerStatus() == 0) {
                    audioObj.setLoudSpeakerStatus(false);
                } else {
                    if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !HeadsetUtil.getInstance().isWiredHeadsetOn() && ConfMgr.getInstance().isConfConnected()) {
                        z = true;
                    }
                    audioObj.setLoudSpeakerStatus(z);
                }
            }
        }
        updateAudioSourceType();
    }

    public void changeAudioOutput(int i) {
        ConfMgr instance2 = ConfMgr.getInstance();
        this.mPreferAudioType = i;
        AudioSessionMgr audioObj = instance2.getAudioObj();
        if (audioObj != null) {
            boolean z = false;
            if (this.mIsCallOffHook) {
                if (this.mIsAudioReady && !this.mIsAudioStoppedByCallOffHook) {
                    audioObj.setLoudSpeakerStatus(false);
                    CmmUser myself = instance2.getMyself();
                    if (myself != null) {
                        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                        if (audioStatusObj != null && audioStatusObj.getAudiotype() == 0 && !audioStatusObj.getIsMuted()) {
                            audioObj.stopAudio();
                            this.mIsAudioMutedByCallOffHook = true;
                        }
                    }
                    audioObj.stopPlayout();
                    this.mIsAudioStoppedByCallOffHook = true;
                }
            } else if (audioObj.getPreferedLoudSpeakerStatus() == 1) {
                audioObj.stopBluetoothHeadset();
                audioObj.setLoudSpeakerStatus(true);
            } else if (i != 3 || !HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
                audioObj.stopBluetoothHeadset();
                if (i == 2) {
                    audioObj.startWiredHeadset();
                }
                if (audioObj.getPreferedLoudSpeakerStatus() == 0) {
                    audioObj.setLoudSpeakerStatus(false);
                } else {
                    if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                        z = true;
                    }
                    audioObj.setLoudSpeakerStatus(z);
                }
            } else {
                audioObj.startBluetoothHeadset();
                updateAudioSourceType();
                return;
            }
        }
        updateAudioSourceType();
    }

    private void updateAudioSourceType() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            boolean z = VoiceEnginContext.getSelectedPlayerStreamType() == 3;
            boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(VideoBoxApplication.getInstance());
            HeadsetUtil instance2 = HeadsetUtil.getInstance();
            boolean z2 = instance2.isBluetoothHeadsetOn() || instance2.isWiredHeadsetOn();
            int i = this.mCurAudioSourceType;
            if (z || (!isFeatureTelephonySupported && !z2)) {
                this.mCurAudioSourceType = 0;
                this.mSwitchableAudioSourceType = -1;
            } else if (!audioObj.getLoudSpeakerStatus() || (instance2.isBluetoothScoAudioOn() && VoiceEngineCompat.isBluetoothScoSupported())) {
                if (instance2.isBluetoothScoAudioOn() && VoiceEngineCompat.isBluetoothScoSupported()) {
                    this.mCurAudioSourceType = 3;
                } else if (instance2.isBluetoothHeadsetOn() && (audioObj.ismIsUseA2dpMode() || audioObj.isStarttingSco())) {
                    this.mCurAudioSourceType = 3;
                } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                    this.mCurAudioSourceType = 2;
                } else {
                    this.mCurAudioSourceType = 1;
                }
                this.mSwitchableAudioSourceType = 0;
            } else {
                this.mCurAudioSourceType = 0;
                if (instance2.isBluetoothHeadsetOn()) {
                    this.mSwitchableAudioSourceType = 0;
                } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                    this.mSwitchableAudioSourceType = 2;
                } else {
                    this.mSwitchableAudioSourceType = 1;
                }
            }
            if (i != this.mCurAudioSourceType) {
                IListener[] all = this.mListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IConfUIListener) iListener).onAudioSourceTypeChanged(this.mCurAudioSourceType);
                    }
                }
            }
        }
    }

    public int getCurrentAudioSourceType() {
        return this.mCurAudioSourceType;
    }

    public int getSwitchableAudioSourceType() {
        return this.mSwitchableAudioSourceType;
    }

    public void notifyNetworkType() {
        int i;
        switch (NetworkUtil.getDataNetworkType(VideoBoxApplication.getInstance())) {
            case 0:
            case 5:
                i = 0;
                break;
            case 1:
                i = 2;
                break;
            case 2:
            case 3:
                i = 3;
                break;
            case 4:
                i = 1;
                break;
            default:
                i = 0;
                break;
        }
        ConfMgr.getInstance().setAndroidNetworkType(i, 0);
    }

    public void notifyWifiSignal() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            try {
                WifiManager wifiManager = (WifiManager) instance2.getApplicationContext().getSystemService("wifi");
                if (wifiManager != null) {
                    WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                    if (connectionInfo != null) {
                        ConfMgr.getInstance().setWifiSignalQuality(WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 101));
                    }
                }
            } catch (Throwable th) {
                ZMLog.m289w(TAG, th, "notifyWifiSignal failure", new Object[0]);
            }
        }
    }

    public void setIsMeetingEndMessageDisabled(boolean z) {
        this.mIsMeetingEndMessageDisabled = z;
    }

    public boolean isCallOffHook() {
        return this.mIsCallOffHook;
    }

    private void reportOSArchInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("android,");
        sb.append(VERSION.RELEASE);
        sb.append(PreferencesConstants.COOKIE_DELIMITER);
        sb.append(HardwareUtil.getPreferredCpuABI());
        ConfMgr.getInstance().mmrMonitorLog("os_arch_info", sb.toString());
    }

    public void tryTurnOnAudioSession() {
        ConfMgr instance2 = ConfMgr.getInstance();
        if (instance2.isConfConnected() && this.mIsAudioReady) {
            AudioSessionMgr audioObj = instance2.getAudioObj();
            if (audioObj != null) {
                audioObj.turnOnOffAudioSession(true);
            }
        }
    }

    public boolean tryRetrieveMicrophone() {
        ConfMgr instance2 = ConfMgr.getInstance();
        if (!instance2.isConfConnected() || !this.mIsAudioReady) {
            return false;
        }
        AudioSessionMgr audioObj = instance2.getAudioObj();
        if (audioObj == null) {
            return false;
        }
        CmmUser myself = instance2.getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        if (audioStatusObj.getAudiotype() != 0) {
            audioObj.unSelectMicrophone();
            audioObj.selectDefaultMicrophone(true);
            return true;
        }
        boolean isMuted = audioStatusObj.getIsMuted();
        if (!isMuted) {
            audioObj.stopAudio();
        }
        audioObj.unSelectMicrophone();
        if (!audioObj.selectDefaultMicrophone(true)) {
            return false;
        }
        if (!isMuted) {
            return audioObj.startAudio();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void OnPTInviteRoomSystemResult(boolean z, String str, String str2, String str3, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            int length = all.length;
            for (int i3 = 0; i3 < length; i3++) {
                ((IConfUIListener) all[i3]).onPTInviteRoomSystemResult(z, str, str2, str3, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean OnPTInvitationSent(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onPTInvitationSent(str);
            }
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onVerifyMyGuestRoleResult(boolean z, boolean z2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onVerifyMyGuestRoleResult(z, z2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onHostBindTelNotification(long j, long j2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IConfUIListener) all[i]).onHostBindTelNotification(j, j2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAnnotateStartedUp(boolean z, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onAnnotateStartedUp(z, j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAnnotateShutDown() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onAnnotateShutDown();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWBPageChanged(int i, int i2, int i3, int i4) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null && all.length > 0) {
            for (IListener iListener : all) {
                ((IConfUIListener) iListener).onWBPageChanged(i, i2, i3, i4);
            }
        }
    }
}
