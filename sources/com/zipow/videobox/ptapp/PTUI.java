package com.zipow.videobox.ptapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.FreeMeetingEndActivity;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.JoinConfActivity;
import com.zipow.videobox.JoinMeetingFailActivity;
import com.zipow.videobox.LauncherActivity;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.MinVersionForceUpdateActivity;
import com.zipow.videobox.PTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.ZMNoticeOnShowAgeGatingTask;
import com.zipow.videobox.ZMNoticeOnShowCrashReport;
import com.zipow.videobox.ZMNoticeOnWebLoginTask;
import com.zipow.videobox.ZMNoticeProtocolActionBlockedTask;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.common.ZMNoticeChooseDomainTask;
import com.zipow.videobox.common.ZMNotifyUIToLogOutTask;
import com.zipow.videobox.common.ZMShowLoginDisclaimerTask;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.confapp.WaitingRoomStatusChangeMgrOnPT;
import com.zipow.videobox.confapp.p009bo.BOStatusChangeMgrOnPT;
import com.zipow.videobox.fragment.ForceUpdateDialogFragment;
import com.zipow.videobox.fragment.JoinConfFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.kubi.KubiServiceManager;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.nos.NOSMgr;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTAppProtos.WebLaunchedToLoginParam;
import com.zipow.videobox.ptapp.p013mm.CrawlerLinkPreview;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMSessionsMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomPublicRoomSearchData;
import com.zipow.videobox.sdk.PromptProxyServerTask;
import com.zipow.videobox.sdk.PromptProxyServerTaskManager;
import com.zipow.videobox.sdk.SDKHost;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.IPBXMessageAPI;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI;
import com.zipow.videobox.sip.server.ISIPCallAPI;
import com.zipow.videobox.sip.server.ISIPLineMgrAPI;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMServiceHelper;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.IMAddrBookListView;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import p021us.zoom.androidlib.app.ForegroundTask;
import p021us.zoom.androidlib.app.ForegroundTaskManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMSecureRandom;
import p021us.zoom.thirdparty.box.BoxMgr;
import p021us.zoom.thirdparty.dropbox.ZMDropbox;
import p021us.zoom.thirdparty.googledrive.GoogleDriveMgr;
import p021us.zoom.thirdparty.login.LoginType;
import p021us.zoom.thirdparty.login.ThirdPartyLoginFactory;
import p021us.zoom.thirdparty.login.sso.SsoUtil;
import p021us.zoom.thirdparty.onedrive.OneDriveManager;
import p021us.zoom.videomeetings.C4558R;

public final class PTUI {
    private static final String TAG = "PTUI";
    private static final long XMPP_AUTO_SIGNIN_CHECK_TIME = 30;
    private static final long XMPP_AUTO_SIGNIN_THRESHOLD_GCM = 7200000;
    private static final long XMPP_AUTO_SIGNIN_THRESHOLD_NO_WIFI = 2700000;
    private static final long XMPP_AUTO_SIGNIN_THRESHOLD_WIFI = 1200000;
    private static final int XMPP_AUTO_SIGNIN_TIME_INTERVAL_UNIT = 1200000;
    @Nullable
    private static PTUI instance;
    @NonNull
    private ListenerList mCalendarAuthListenerList = new ListenerList();
    @NonNull
    private ListenerList mCallMeListenerList = new ListenerList();
    @NonNull
    private ListenerList mConfFailLisenerList = new ListenerList();
    @NonNull
    private ListenerList mConfInvitationListenerList = new ListenerList();
    @NonNull
    private ListenerList mFavoriteListenerList = new ListenerList();
    private int mFreeMeetingTimes = -1;
    @NonNull
    private ListenerList mGDPRListenerList = new ListenerList();
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mHasDataNetwork = false;
    @NonNull
    private ListenerList mIAuthInternationalHandlerList = new ListenerList();
    @NonNull
    private ListenerList mIAuthSsoHandlerList = new ListenerList();
    /* access modifiers changed from: private */
    @NonNull
    public IConfProcessListener mIConfProcessListener = new IConfProcessListener() {
        public void onConfProcessStarted() {
        }

        public void onConfProcessStopped() {
            PTUI.this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    PTUI.this.processGiftFreeMeeting();
                    VideoBoxApplication instance = VideoBoxApplication.getInstance();
                    if (instance != null) {
                        instance.removeConfProcessListener(PTUI.this.mIConfProcessListener);
                    }
                }
            }, 200);
        }
    };
    @NonNull
    private ListenerList mIMListenerList = new ListenerList();
    @NonNull
    private ListenerList mInviteByCallOutListenerList = new ListenerList();
    @NonNull
    private ListenerList mJoinConfMeetingStatusListener = new ListenerList();
    @Nullable
    private String mLatestMeetingId = "";
    private long mLatestMeetingNumber = 0;
    private int mLeaveReason = -1;
    @NonNull
    private ListenerList mMeetingMgrListenerList = new ListenerList();
    private boolean mNeedGDPRConfirm = false;
    private boolean mNeedLoginDisclaimerConfirm = false;
    @Nullable
    private BroadcastReceiver mNetworkStateReceiver;
    @NonNull
    private ListenerList mNotifyZAKRefreshListenerList = new ListenerList();
    @NonNull
    private ListenerList mPTListenerList = new ListenerList();
    @NonNull
    private ListenerList mPhoneABListenerList = new ListenerList();
    @NonNull
    private ListenerList mPresentToRoomStatusListener = new ListenerList();
    private String mPrivacyUrl;
    @NonNull
    private ListenerList mProfileListenerList = new ListenerList();
    @NonNull
    private ListenerList mRoomCallListenerList = new ListenerList();
    @Nullable
    private Runnable mRunnableDispatchCallStatusIdle = null;
    private Runnable mRunnableShowForceUpdateDialog;
    @NonNull
    private ListenerList mSDKAuthListenerList = new ListenerList();
    private String mTosUrl;
    @Nullable
    private String mUpgradeUrl = null;
    private int mXMPPAutoLoginRandomTimeInterval;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mXmppAutoSignInDoubleCheckRunnable = new Runnable() {
        public void run() {
            if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && !zoomMessenger.isConnectionGood() && !zoomMessenger.isStreamConflict()) {
                    zoomMessenger.trySignon();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public long mXmppAutoSignInLastCheckTime = 0;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mXmppAutoSignInRunnable = new Runnable() {
        public void run() {
            if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
                long currentTimeMillis = System.currentTimeMillis() - PTUI.this.mXmppAutoSignInLastCheckTime;
                long access$800 = PTUI.this.getXmppAutoSignInThreshold();
                if (currentTimeMillis <= 0 || currentTimeMillis >= access$800) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger == null) {
                        PTUI.this.stopXmppAutoSignInTimer();
                    } else if (zoomMessenger.isConnectionGood()) {
                        PTUI.this.mXmppAutoSignInLastCheckTime = System.currentTimeMillis();
                    } else if (!zoomMessenger.isStreamConflict()) {
                        zoomMessenger.trySignon();
                        PTUI.this.mXmppAutoSignInLastCheckTime = System.currentTimeMillis();
                        VideoBoxApplication.getInstance().runOnMainThreadDelayed(PTUI.this.mXmppAutoSignInDoubleCheckRunnable, 60000);
                    }
                }
            }
        }
    };
    @Nullable
    private ScheduledExecutorService mXmppAutoSignInScheduler = null;
    @Nullable
    private ScheduledFuture<?> mXmppAutoSignInTask = null;

    public interface IAuthInternationalHandlerListener extends IListener {
        void onFacebookAuthReturn(String str, long j, long j2, String str2);

        void onGoogleAuthReturn(String str, String str2, long j, String str3);
    }

    public interface IAuthSsoHandlerListener extends IListener {
        void onQuerySSOVanityURL(String str, int i, String str2);

        void onSSOLoginTokenReturn(String str);

        void onSSOLoginTokenReturnKMS(String str, String str2, String str3);
    }

    public interface ICalendarAuthListener extends IListener {
        void onCalendarAuthResult(int i);
    }

    public interface ICallMeListener extends IListener {
        void onCallMeStatusChanged(int i);
    }

    public interface IConfFailListener extends IListener {
        void onConfFail(int i, int i2);
    }

    public interface IConfInvitationListener extends IListener {
        void onCallAccepted(InvitationItem invitationItem);

        void onCallDeclined(InvitationItem invitationItem);

        void onConfInvitation(InvitationItem invitationItem);
    }

    public interface IFavoriteListener extends IListener {
        void onFavAvatarReady(String str);

        void onFavoriteEvent(int i, long j);

        void onFinishSearchDomainUser(String str, int i, int i2, List<ZoomContact> list);
    }

    public interface IGDPRListener extends IListener {
        void NotifyUIToLogOut();

        void OnShowPrivacyDialog(String str, String str2);
    }

    public interface IIMListener extends IListener {
        void onIMBuddyPic(BuddyItem buddyItem);

        void onIMBuddyPresence(BuddyItem buddyItem);

        void onIMBuddySort();

        void onIMLocalStatusChanged(int i);

        void onIMReceived(IMMessage iMMessage);

        void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo);

        void onSubscriptionRequest();

        void onSubscriptionUpdate();
    }

    public interface IInviteByCallOutListener extends IListener {
        void onCallOutStatusChanged(int i);
    }

    public interface IJoinConfMeetingStatusListener extends IListener {
        void onJoinConfMeetingStatus(boolean z, boolean z2);
    }

    public interface IMeetingMgrListener extends IListener {
        void onDeleteMeetingResult(int i);

        void onListCalendarEventsResult(int i);

        void onListMeetingResult(int i);

        void onPMIEvent(int i, int i2, MeetingInfoProto meetingInfoProto);

        void onScheduleMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str);

        void onStartFailBeforeLaunch(int i);

        void onUpdateMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str);
    }

    public interface INotifyZAKListener extends IListener {
        void notifyZAKRefreshFailed(int i);
    }

    public interface IPTCommonEventListener extends IListener {
        void onPTCommonEvent(int i, byte[] bArr);
    }

    public interface IPTUIListener extends IListener {
        void onDataNetworkStatusChanged(boolean z);

        void onPTAppCustomEvent(int i, long j);

        void onPTAppEvent(int i, long j);
    }

    public interface IPhoneABListener extends IListener {
        void onPhoneABEvent(int i, long j, Object obj);
    }

    public interface IPresentToRoomStatusListener extends IListener {
        void presentToRoomStatusUpdate(int i);
    }

    public interface IProfileListener extends IListener {
        void OnProfileFieldUpdated(String str, int i, int i2, String str2);
    }

    public interface IRoomCallListener extends IListener {
        void onRoomCallEvent(int i, long j, boolean z);
    }

    public interface ISDKAuthListener extends IListener {
        void onSDKAuth(int i);
    }

    public static class SimpleMeetingMgrListener implements IMeetingMgrListener {
        public void onDeleteMeetingResult(int i) {
        }

        public void onListCalendarEventsResult(int i) {
        }

        public void onListMeetingResult(int i) {
        }

        public void onPMIEvent(int i, int i2, MeetingInfoProto meetingInfoProto) {
        }

        public void onScheduleMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
        }

        public void onStartFailBeforeLaunch(int i) {
        }

        public void onUpdateMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
        }
    }

    public static class SimplePTUIListener implements IPTUIListener {
        public void onDataNetworkStatusChanged(boolean z) {
        }

        public void onPTAppCustomEvent(int i, long j) {
        }

        public void onPTAppEvent(int i, long j) {
        }
    }

    public static class SimpleProfileListener implements IProfileListener {
        public void OnProfileFieldUpdated(String str, int i, int i2, String str2) {
        }
    }

    private native void nativeInit();

    private void sinkIPCLoginToClaimHostImpl(int i) {
    }

    private PTUI() {
    }

    public void initialize() {
        nativeInit();
        startListenNetworkState();
    }

    @NonNull
    public static synchronized PTUI getInstance() {
        PTUI ptui;
        synchronized (PTUI.class) {
            if (instance == null) {
                instance = new PTUI();
            }
            ptui = instance;
        }
        return ptui;
    }

    public void addPTUIListener(@Nullable IPTUIListener iPTUIListener) {
        if (iPTUIListener != null) {
            IListener[] all = this.mPTListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iPTUIListener) {
                    removePTUIListener((IPTUIListener) all[i]);
                }
            }
            this.mPTListenerList.add(iPTUIListener);
        }
    }

    public void removePTUIListener(IPTUIListener iPTUIListener) {
        this.mPTListenerList.remove(iPTUIListener);
    }

    public void addIMListener(@Nullable IIMListener iIMListener) {
        if (iIMListener != null) {
            IListener[] all = this.mIMListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iIMListener) {
                    removeIMListener((IIMListener) all[i]);
                }
            }
            this.mIMListenerList.add(iIMListener);
        }
    }

    public void removeIMListener(IIMListener iIMListener) {
        this.mIMListenerList.remove(iIMListener);
    }

    public void addConfInvitationListener(@Nullable IConfInvitationListener iConfInvitationListener) {
        if (iConfInvitationListener != null) {
            IListener[] all = this.mConfInvitationListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iConfInvitationListener) {
                    removeConfInvitationListener((IConfInvitationListener) all[i]);
                }
            }
            this.mConfInvitationListenerList.add(iConfInvitationListener);
        }
    }

    public void removeConfInvitationListener(IConfInvitationListener iConfInvitationListener) {
        this.mConfInvitationListenerList.remove(iConfInvitationListener);
    }

    public void addMeetingMgrListener(@Nullable IMeetingMgrListener iMeetingMgrListener) {
        if (iMeetingMgrListener != null) {
            IListener[] all = this.mMeetingMgrListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iMeetingMgrListener) {
                    removeMeetingMgrListener((IMeetingMgrListener) all[i]);
                }
            }
            this.mMeetingMgrListenerList.add(iMeetingMgrListener);
        }
    }

    public void removeMeetingMgrListener(IMeetingMgrListener iMeetingMgrListener) {
        this.mMeetingMgrListenerList.remove(iMeetingMgrListener);
    }

    public void addFavoriteListener(@Nullable IFavoriteListener iFavoriteListener) {
        if (iFavoriteListener != null) {
            IListener[] all = this.mFavoriteListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iFavoriteListener) {
                    removeFavoriteListener((IFavoriteListener) all[i]);
                }
            }
            this.mFavoriteListenerList.add(iFavoriteListener);
        }
    }

    public void removeFavoriteListener(IFavoriteListener iFavoriteListener) {
        this.mFavoriteListenerList.remove(iFavoriteListener);
    }

    public void addPhoneABListener(@Nullable IPhoneABListener iPhoneABListener) {
        if (iPhoneABListener != null) {
            IListener[] all = this.mPhoneABListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iPhoneABListener) {
                    removePhoneABListener((IPhoneABListener) all[i]);
                }
            }
            this.mPhoneABListenerList.add(iPhoneABListener);
        }
    }

    public void removePhoneABListener(IPhoneABListener iPhoneABListener) {
        this.mPhoneABListenerList.remove(iPhoneABListener);
    }

    public void addConfFailListener(@Nullable IConfFailListener iConfFailListener) {
        if (iConfFailListener != null) {
            this.mConfFailLisenerList.add(iConfFailListener);
        }
    }

    public void removeConfFailListener(IConfFailListener iConfFailListener) {
        this.mConfFailLisenerList.remove(iConfFailListener);
    }

    public void addInviteByCallOutListener(@Nullable IInviteByCallOutListener iInviteByCallOutListener) {
        if (iInviteByCallOutListener != null) {
            this.mInviteByCallOutListenerList.add(iInviteByCallOutListener);
        }
    }

    public void removeInviteByCallOutListener(IInviteByCallOutListener iInviteByCallOutListener) {
        this.mInviteByCallOutListenerList.remove(iInviteByCallOutListener);
    }

    public void addCallMeListener(@Nullable ICallMeListener iCallMeListener) {
        if (iCallMeListener != null) {
            this.mCallMeListenerList.add(iCallMeListener);
        }
    }

    public void removeCallMeListener(ICallMeListener iCallMeListener) {
        this.mCallMeListenerList.remove(iCallMeListener);
    }

    public void addSDKAuthListener(@Nullable ISDKAuthListener iSDKAuthListener) {
        if (iSDKAuthListener != null) {
            this.mSDKAuthListenerList.add(iSDKAuthListener);
        }
    }

    public void removeSDKAuthListener(ISDKAuthListener iSDKAuthListener) {
        this.mSDKAuthListenerList.remove(iSDKAuthListener);
    }

    public void addRoomCallListener(@Nullable IRoomCallListener iRoomCallListener) {
        if (iRoomCallListener != null) {
            this.mRoomCallListenerList.add(iRoomCallListener);
        }
    }

    public void removeRoomCallListener(IRoomCallListener iRoomCallListener) {
        this.mRoomCallListenerList.remove(iRoomCallListener);
    }

    public void addJoinConfMeetingStatusListener(@Nullable IJoinConfMeetingStatusListener iJoinConfMeetingStatusListener) {
        if (iJoinConfMeetingStatusListener != null) {
            this.mJoinConfMeetingStatusListener.add(iJoinConfMeetingStatusListener);
        }
    }

    public void removeJoinConfMeetingStatusListener(IJoinConfMeetingStatusListener iJoinConfMeetingStatusListener) {
        this.mJoinConfMeetingStatusListener.remove(iJoinConfMeetingStatusListener);
    }

    public void addPresentToRoomStatusListener(@Nullable IPresentToRoomStatusListener iPresentToRoomStatusListener) {
        if (iPresentToRoomStatusListener != null) {
            this.mPresentToRoomStatusListener.add(iPresentToRoomStatusListener);
        }
    }

    public void removePresentToRoomStatusListener(IPresentToRoomStatusListener iPresentToRoomStatusListener) {
        this.mPresentToRoomStatusListener.remove(iPresentToRoomStatusListener);
    }

    public void addProfileListener(IProfileListener iProfileListener) {
        this.mProfileListenerList.add(iProfileListener);
    }

    public void removeProfileListener(IProfileListener iProfileListener) {
        this.mProfileListenerList.remove(iProfileListener);
    }

    public void addGDPRListener(IGDPRListener iGDPRListener) {
        this.mGDPRListenerList.add(iGDPRListener);
    }

    public void removeGDPRListener(IGDPRListener iGDPRListener) {
        this.mGDPRListenerList.remove(iGDPRListener);
    }

    public void addAuthInternationalHandler(@Nullable IAuthInternationalHandlerListener iAuthInternationalHandlerListener) {
        if (iAuthInternationalHandlerListener != null) {
            IListener[] all = this.mIAuthInternationalHandlerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iAuthInternationalHandlerListener) {
                    removeAuthInternationalHandler((IAuthInternationalHandlerListener) all[i]);
                }
            }
            this.mIAuthInternationalHandlerList.add(iAuthInternationalHandlerListener);
        }
    }

    public void removeAuthInternationalHandler(IAuthInternationalHandlerListener iAuthInternationalHandlerListener) {
        this.mIAuthInternationalHandlerList.remove(iAuthInternationalHandlerListener);
    }

    public void addAuthSsoHandler(@Nullable IAuthSsoHandlerListener iAuthSsoHandlerListener) {
        if (iAuthSsoHandlerListener != null) {
            IListener[] all = this.mIAuthSsoHandlerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iAuthSsoHandlerListener) {
                    removeAuthSsoHandler((IAuthSsoHandlerListener) all[i]);
                }
            }
            this.mIAuthSsoHandlerList.add(iAuthSsoHandlerListener);
        }
    }

    public void removeAuthSsoHandler(IAuthSsoHandlerListener iAuthSsoHandlerListener) {
        this.mIAuthSsoHandlerList.remove(iAuthSsoHandlerListener);
    }

    public void addINotifyZAKListener(INotifyZAKListener iNotifyZAKListener) {
        this.mNotifyZAKRefreshListenerList.add(iNotifyZAKListener);
    }

    public void removeINotifyZAKListener(INotifyZAKListener iNotifyZAKListener) {
        this.mNotifyZAKRefreshListenerList.remove(iNotifyZAKListener);
    }

    public void addCalendarAuthListener(ICalendarAuthListener iCalendarAuthListener) {
        this.mCalendarAuthListenerList.add(iCalendarAuthListener);
    }

    public void removeCalendarAuthListener(ICalendarAuthListener iCalendarAuthListener) {
        this.mCalendarAuthListenerList.remove(iCalendarAuthListener);
    }

    /* access modifiers changed from: protected */
    public void onGoogleAuthReturn(String str, String str2, long j, String str3) {
        try {
            onGoogleAuthReturnImpl(str, str2, j, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onGoogleAuthReturnImpl(String str, String str2, long j, String str3) {
        IListener[] all = this.mIAuthInternationalHandlerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IAuthInternationalHandlerListener) all[i]).onGoogleAuthReturn(str, str2, j, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFacebookAuthReturn(String str, long j, long j2, String str2) {
        try {
            onFacebookAuthReturnImpl(str, j, j2, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onFacebookAuthReturnImpl(String str, long j, long j2, String str2) {
        IListener[] all = this.mIAuthInternationalHandlerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IAuthInternationalHandlerListener) all[i]).onFacebookAuthReturn(str, j, j2, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSSOLoginTokenReturn(String str) {
        try {
            onSSOLoginTokenReturnImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onSSOLoginTokenReturnImpl(String str) {
        IListener[] all = this.mIAuthSsoHandlerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAuthSsoHandlerListener) iListener).onSSOLoginTokenReturn(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSSOLoginTokenReturnKMS(String str, String str2, String str3) {
        try {
            onSSOLoginTokenReturnKMSImpl(str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onSSOLoginTokenReturnKMSImpl(String str, String str2, String str3) {
        IListener[] all = this.mIAuthSsoHandlerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAuthSsoHandlerListener) iListener).onSSOLoginTokenReturnKMS(str, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyUIToLogOut() {
        try {
            NotifyUIToLogOutImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void NotifyUIToLogOutImpl() {
        IListener[] all = this.mGDPRListenerList.getAll();
        if (all == null || all.length < 1) {
            ForegroundTaskManager.getInstance().runInForeground(new ZMNotifyUIToLogOutTask(ZMNotifyUIToLogOutTask.class.getName()));
            return;
        }
        for (IListener iListener : all) {
            ((IGDPRListener) iListener).NotifyUIToLogOut();
        }
    }

    /* access modifiers changed from: protected */
    public void OnShowPrivacyDialog(String str, String str2) {
        try {
            OnShowPrivacyDialogImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* access modifiers changed from: protected */
    public void onOpenLoginPanel(int i, @NonNull String str) {
        try {
            onOpenLoginPanelImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnShowPrivacyDialogImpl(String str, String str2) {
        this.mNeedGDPRConfirm = true;
        this.mPrivacyUrl = str;
        this.mTosUrl = str2;
        IListener[] all = this.mGDPRListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IGDPRListener) iListener).OnShowPrivacyDialog(str, str2);
            }
        }
    }

    private void onOpenLoginPanelImpl(int i, @NonNull String str) {
        switch (i) {
            case 28:
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                if (frontActivity != null) {
                    PTApp.getInstance().setNeedToReturnToMeetingOnResume(true);
                    ThirdPartyLoginFactory.build(LoginType.Sso, ThirdPartyLoginFactory.buildSsoBundle(SsoUtil.formatUrl(str, PTApp.getInstance().getZMCID()))).login(frontActivity, ZMUtils.getDefaultBrowserPkgName(frontActivity));
                    break;
                } else {
                    return;
                }
            case 29:
                this.mHandler.post(new Runnable() {
                    public void run() {
                        LoginUtil.launchLogin(false, true);
                    }
                });
                break;
        }
    }

    /* access modifiers changed from: protected */
    public void OnShowLoginDisclaimerDialog(CustomizeInfo customizeInfo) {
        try {
            OnShowLoginDisclaimerDialogImpl(customizeInfo);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnShowLoginDisclaimerDialogImpl(CustomizeInfo customizeInfo) {
        this.mNeedLoginDisclaimerConfirm = true;
        ForegroundTaskManager.getInstance().runInForeground(new ZMShowLoginDisclaimerTask(ZMShowLoginDisclaimerTask.class.getName(), customizeInfo));
    }

    public boolean NeedLoginDisclaimerConfirm() {
        return this.mNeedLoginDisclaimerConfirm;
    }

    public void ClearLoginDisclaimerConfirmFlag() {
        this.mNeedLoginDisclaimerConfirm = false;
    }

    /* access modifiers changed from: protected */
    public void onNeedForceUpgrade(String str) {
        try {
            onNeedForceUpgradeImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onNeedForceUpgradeImpl(final String str) {
        if (this.mRunnableShowForceUpdateDialog == null) {
            this.mRunnableShowForceUpdateDialog = new Runnable() {
                public void run() {
                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    if (frontActivity != null && frontActivity.isActive()) {
                        MinVersionForceUpdateActivity.showMinVersionForceUpdate(frontActivity, str, true);
                    }
                }
            };
        }
        this.mHandler.postDelayed(this.mRunnableShowForceUpdateDialog, 200);
    }

    public boolean NeedGDPRConfirm() {
        return this.mNeedGDPRConfirm;
    }

    public void ClearGDPRConfirmFlag() {
        this.mNeedGDPRConfirm = false;
    }

    public String getGDPRPrivacyUrl() {
        return this.mPrivacyUrl;
    }

    public String getGDPRTosUrl() {
        return this.mTosUrl;
    }

    /* access modifiers changed from: protected */
    public void dispatchPTAppEvent(int i, long j) {
        try {
            dispatchPTAppEventImpl(i, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void dispatchPTAppEventImpl(int i, long j) {
        switch (i) {
            case 0:
                onWebLogin(j);
                break;
            case 1:
                onWebLogout(j);
                break;
            case 8:
                onIMLogin(j);
                break;
            case 14:
                onIMLogout(j);
                break;
            case 21:
                onIMReconnecting();
                break;
            case 22:
                if (((int) j) != 0) {
                    Runnable runnable = this.mRunnableDispatchCallStatusIdle;
                    if (runnable != null) {
                        this.mHandler.removeCallbacks(runnable);
                    }
                } else if (VideoBoxApplication.getInstance().getConfProcessId() > 0 || VideoBoxApplication.getInstance().getConfService() != null) {
                    dispatchCallStatusIdleAfterConfServiceDisconnected();
                    return;
                }
                onCallStatusChanged(j);
                break;
            case 37:
                onGoogleWebAccessFail();
                break;
            case 39:
                onPTEventLogoutWithBrowser((int) j);
                break;
            case 53:
                WaitingRoomStatusChangeMgrOnPT.getInstance().handleStatusChangeStart();
                break;
            case 68:
                ZMPTIMeetingMgr.getInstance().setmIsPullingCalendarIntegrationConfig(false);
                break;
        }
        IListener[] all = this.mPTListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPTUIListener) iListener).onPTAppEvent(i, j);
            }
        }
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService != null) {
            try {
                confService.sinkPTAppEvent(i, j);
            } catch (RemoteException unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchCallStatusIdleAfterConfServiceDisconnected() {
        if (this.mRunnableDispatchCallStatusIdle == null) {
            this.mRunnableDispatchCallStatusIdle = new Runnable() {
                public void run() {
                    if (VideoBoxApplication.getInstance().getConfProcessId() > 0 || VideoBoxApplication.getInstance().getConfService() != null) {
                        PTUI.this.dispatchCallStatusIdleAfterConfServiceDisconnected();
                    } else {
                        PTUI.this.dispatchPTAppEvent(22, 0);
                    }
                }
            };
        }
        this.mHandler.postDelayed(this.mRunnableDispatchCallStatusIdle, 100);
    }

    private void onIMLogin(long j) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            iMHelper.setIMLocalStatus(j == 0 ? 4 : 5);
        }
        if (j == 0) {
            PTApp.getInstance().setTokenExpired(false);
            if (iMHelper != null) {
                PTApp.getInstance().setRencentJid(iMHelper.getJIDMyself());
            }
            ZMServiceHelper.doServiceActionInFront(PTService.ACTION_START_FOREGROUND, PTService.class);
        }
    }

    private void onWebLogin(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        PTApp.getInstance().setWebSignedOn(i == 0);
        if (i == 0) {
            if (!PTApp.getInstance().hasMessenger() || VideoBoxApplication.getNonNullInstance().isSDKMode()) {
                enableSendFileActivity(false);
            } else {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.setMsgUI(ZoomMessengerUI.getInstance());
                    PreferenceUtil.saveIntValue(PreferenceUtil.IM_GIPHY_OPTION, zoomMessenger.getGiphyOption());
                    ZoomPublicRoomSearchData publicRoomSearchData = zoomMessenger.getPublicRoomSearchData();
                    if (publicRoomSearchData != null) {
                        publicRoomSearchData.setCallback(ZoomPublicRoomSearchUI.getInstance());
                    }
                    ThreadDataProvider threadDataProvider = zoomMessenger.getThreadDataProvider();
                    if (threadDataProvider != null) {
                        threadDataProvider.setMsgUI(ThreadDataUI.getInstance());
                    }
                }
                MMPrivateStickerMgr zoomPrivateStickerMgr = PTApp.getInstance().getZoomPrivateStickerMgr();
                if (zoomPrivateStickerMgr != null) {
                    zoomPrivateStickerMgr.registerUICallBack(PrivateStickerUICallBack.getInstance());
                }
                NOSMgr.getInstance().register();
                ZMBuddySyncInstance.getInsatance().refreshAllBuddy();
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    notificationSettingMgr.registerUICallBack(NotificationSettingUI.getInstance());
                }
                CrawlerLinkPreview linkCrawler = PTApp.getInstance().getLinkCrawler();
                if (linkCrawler != null) {
                    linkCrawler.RegisterUICallback(CrawlerLinkPreviewUI.getInstance());
                }
                IMCallbackUI.getInstance().registerCallback();
                ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
                if (zoomMessageTemplate != null) {
                    zoomMessageTemplate.registerCommonAppUICallback(ZoomMessageTemplateUI.getInstance());
                }
                enableSendFileActivity(!PTApp.getInstance().isFileTransferDisabled());
            }
            if (!VideoBoxApplication.getNonNullInstance().isSDKMode() && PTApp.getInstance().isSipPhoneEnabled()) {
                if (!CmmSIPCallManager.getInstance().isSipInited()) {
                    ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
                    if (sipCallAPI != null) {
                        sipCallAPI.registerUICallBack(SIPCallEventListenerUI.getInstance());
                        ISIPLineMgrAPI sIPLineMgrAPI = sipCallAPI.getSIPLineMgrAPI();
                        if (sIPLineMgrAPI != null) {
                            sIPLineMgrAPI.setLineEventSink(ISIPLineMgrEventSinkUI.getInstance());
                        }
                        IPBXMessageAPI messageAPI = sipCallAPI.getMessageAPI();
                        if (messageAPI != null && !messageAPI.isInited()) {
                            messageAPI.initialize(IPBXMessageEventSinkUI.getInstance());
                        }
                    }
                }
                CmmSIPCallManager.getInstance().onZoomLoginFinished();
            }
            PTApp.getInstance().setTokenExpired(false);
            if (PTApp.getInstance().needDoWebStart()) {
                ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask("launchCallForWebStart") {
                    public boolean isOtherProcessSupported() {
                        return false;
                    }

                    public boolean isValidActivity(String str) {
                        return IMActivity.class.getName().equals(str);
                    }

                    public void run(@Nullable ZMActivity zMActivity) {
                        if (zMActivity != null && ConfActivity.launchCallForWebStart(zMActivity) == 8) {
                            ForceUpdateDialogFragment.show(zMActivity.getSupportFragmentManager());
                        }
                    }
                });
            }
            ZMServiceHelper.doServiceActionInFront(PTService.ACTION_START_FOREGROUND, PTService.class);
            checkStartKubiService();
            PreferenceUtil.saveBooleanValue(PreferenceUtil.ACCOUNT_LOGIN, true);
        } else if (j == 407) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null && frontActivity.isActive()) {
                MinVersionForceUpdateActivity.showMinVersionForceUpdate(frontActivity, "", false);
            }
        }
        if (PTApp.getInstance().isNeedCheckSwitchCall()) {
            IPCHelper.getInstance().dispacthPtLoginResultEventToConf();
            PTApp.getInstance().setNeedCheckSwitchCall(false);
        }
    }

    public void checkStartKubiService() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null && settingHelper.getIsKubiDeviceEnabled()) {
            KubiServiceManager instance2 = KubiServiceManager.getInstance(VideoBoxApplication.getInstance());
            instance2.startKubiService();
            instance2.connectKubiService(false);
        }
    }

    private void onWebLogout(long j) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.ACCOUNT_LOGIN, false);
        PTApp.getInstance().setWebSignedOn(false);
        ZoomMessengerUI.getInstance().resetStatus();
        NOSMgr.getInstance().unregister();
        PTApp.getInstance().setRencentJid("");
        PTApp.getInstance().setRencentZoomJid("");
        PTApp.getInstance().nos_ClearDeviceToken();
        stopXmppAutoSignInTimer();
        ZMServiceHelper.doServiceActionInFront(PTService.ACTION_STOP_FOREGROUND, PTService.class);
        NotificationMgr.removeAllMessageNotificationMM(VideoBoxApplication.getGlobalContext());
        CmmSIPCallManager.getInstance().onZoomLogoutFinished();
        ZoomMessengerUI.getInstance().resetStatus();
        if (!VideoBoxApplication.getInstance().isSDKMode() && PTApp.getInstance().ismNeedSwitchVendor()) {
            ZoomProductHelper zoomProductHelper = PTApp.getInstance().getZoomProductHelper();
            if (zoomProductHelper != null && zoomProductHelper.getCurrentVendor() == 1) {
                zoomProductHelper.vendorSwitchTo(0);
            }
        }
        PreferenceUtil.removeValue(PreferenceUtil.FIRST_OPEN_CONTACTS);
        ZMBuddySyncInstance.getInsatance().clearAllBuddies();
        ZMSessionsMgr.getInstance().clear();
        enableSendFileActivity(false);
        clearCloudStorageInfo();
        stopKubiService();
    }

    private void stopKubiService() {
        KubiServiceManager.getInstance(VideoBoxApplication.getInstance()).stopKubiService();
    }

    private void clearCloudStorageInfo() {
        GoogleDriveMgr.release();
        BoxMgr.release();
        ZMDropbox.release();
        OneDriveManager.release();
    }

    private void onIMLogout(long j) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            iMHelper.setIMLocalStatus(0);
        }
    }

    private void onIMReconnecting() {
        if (PTApp.getInstance().getIMHelper() != null && !(ZMActivity.getFrontActivity() instanceof LoginActivity)) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            boolean z = (frontActivity != null && frontActivity.isActive()) || PTApp.getInstance().hasActiveCall();
            if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance()) && z) {
                reconnectIM();
            }
        }
    }

    private void onGoogleWebAccessFail() {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            iMHelper.setIMLocalStatus(5);
        }
    }

    public void onPTEventLogoutWithBrowser(final int i) {
        if (!VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            new Thread("ClearCookie") {
                public void run() {
                    int i = i;
                    if (i == 0) {
                        ThirdPartyLoginFactory.build(LoginType.Facebook, ThirdPartyLoginFactory.buildEmptyFacebookBundle()).logout(VideoBoxApplication.getNonNullInstance());
                    } else if (i == 2) {
                        ThirdPartyLoginFactory.build(LoginType.Google, ThirdPartyLoginFactory.buildEmptyGoogleBundle()).logout(VideoBoxApplication.getNonNullInstance());
                    } else if (i == 101) {
                        ThirdPartyLoginFactory.build(LoginType.Sso, ThirdPartyLoginFactory.buildEmptySsoBundle()).logout(VideoBoxApplication.getNonNullInstance());
                    }
                }
            }.start();
        }
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.LOCAL_AVATAR, "");
        if (readStringValue != null && readStringValue.length() > 0) {
            PreferenceUtil.saveStringValue(PreferenceUtil.LOCAL_AVATAR, "");
        }
    }

    private void startListenNetworkState() {
        this.mHasDataNetwork = NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance());
        if (this.mNetworkStateReceiver == null) {
            this.mNetworkStateReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @Nullable Intent intent) {
                    if (intent != null) {
                        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                            PTUI.this.onNetworkState(intent);
                        }
                    }
                }
            };
            VideoBoxApplication.getInstance().registerReceiver(this.mNetworkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    private void stopListenNetworkState() {
        if (this.mNetworkStateReceiver != null) {
            try {
                VideoBoxApplication.getNonNullInstance().unregisterReceiver(this.mNetworkStateReceiver);
            } catch (Exception unused) {
            }
            this.mNetworkStateReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void onNetworkState(Intent intent) {
        boolean hasDataNetwork = NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance());
        if (this.mHasDataNetwork != hasDataNetwork) {
            if (hasDataNetwork && PTApp.getInstance().isWebSignedOn()) {
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                boolean z = (frontActivity != null && frontActivity.isActive()) || PTApp.getInstance().hasActiveCall();
                if (iMHelper != null && PTApp.getInstance().isCurrentLoginTypeSupportIM() && !iMHelper.isIMSignedOn() && !iMHelper.isIMLoggingIn() && z) {
                    reconnectIM();
                }
            }
            this.mHasDataNetwork = hasDataNetwork;
            IListener[] all = this.mPTListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IPTUIListener) iListener).onDataNetworkStatusChanged(hasDataNetwork);
                }
            }
            IConfService confService = VideoBoxApplication.getInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkDataNetworkStatusChanged(hasDataNetwork);
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public void reconnectIM() {
        PTApp instance2 = PTApp.getInstance();
        instance2.resetForReconnecting();
        if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            int pTLoginType = instance2.getPTLoginType();
            if (pTLoginType == 0) {
                sinkIMLocalStatusChanged(1);
                instance2.loginFacebookWithLocalToken(true);
            } else if (pTLoginType == 2) {
                sinkIMLocalStatusChanged(1);
                instance2.retryLoginGoogle();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkOnQueryIPLocation(int i, byte[] bArr) {
        try {
            sinkOnQueryIPLocationImpl(i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkOnQueryIPLocationImpl(int i, @Nullable byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            try {
                IPLocationInfo parseFrom = IPLocationInfo.parseFrom(bArr);
                IListener[] all = this.mIMListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IIMListener) iListener).onQueryIPLocation(i, parseFrom);
                    }
                }
                NOSMgr.getInstance().onQueryIPLocation(parseFrom);
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkQuerySSOVanityURL(String str, int i, String str2) {
        try {
            sinkQuerySSOVanityURLImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkQuerySSOVanityURLImpl(String str, int i, String str2) {
        IListener[] all = this.mIAuthSsoHandlerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAuthSsoHandlerListener) iListener).onQuerySSOVanityURL(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMReceived(@NonNull byte[] bArr) {
        try {
            sinkIMReceivedImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMReceivedImpl(@NonNull byte[] bArr) {
        try {
            IMMessage parseFrom = IMMessage.parseFrom(bArr);
            if (parseFrom.getMessageType() == 1) {
                IMHelper iMHelper = PTApp.getInstance().getIMHelper();
                if (iMHelper != null) {
                    String fromScreenName = parseFrom.getFromScreenName();
                    if (fromScreenName != null) {
                        if (fromScreenName.equals(iMHelper.getJIDMyself())) {
                            iMHelper.setIMMessageUnread(parseFrom, false);
                        } else {
                            InvitationItem currentCall = IncomingCallManager.getInstance().getCurrentCall();
                            if (currentCall == null || parseFrom.getMessage().indexOf(String.valueOf(currentCall.getMeetingNumber())) > 0) {
                                iMHelper.setIMMessageUnread(parseFrom, false);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMReceived(parseFrom);
                }
            }
            if ((all == null || all.length == 0) && parseFrom.getMessageType() == 0) {
                IMHelper iMHelper2 = PTApp.getInstance().getIMHelper();
                if (iMHelper2 != null) {
                    String jIDMyself = iMHelper2.getJIDMyself();
                    if (jIDMyself == null || !jIDMyself.equals(parseFrom.getFromScreenName())) {
                        NotificationMgr.showMessageNotificationMM(VideoBoxApplication.getInstance(), true);
                    } else {
                        iMHelper2.setIMMessageUnread(parseFrom, false);
                        return;
                    }
                } else {
                    return;
                }
            }
            IConfService confService = VideoBoxApplication.getInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkIMReceived(bArr);
                } catch (RemoteException unused) {
                }
            }
        } catch (InvalidProtocolBufferException unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMBuddyPresence(@NonNull byte[] bArr) {
        try {
            sinkIMBuddyPresenceImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMBuddyPresenceImpl(@NonNull byte[] bArr) {
        try {
            BuddyItem parseFrom = BuddyItem.parseFrom(bArr);
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMBuddyPresence(parseFrom);
                }
            }
            IConfService confService = VideoBoxApplication.getInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkIMBuddyPresence(bArr);
                } catch (RemoteException unused) {
                }
            }
        } catch (InvalidProtocolBufferException unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMBuddyPic(@NonNull byte[] bArr) {
        try {
            sinkIMBuddyPicImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMBuddyPicImpl(@NonNull byte[] bArr) {
        try {
            BuddyItem parseFrom = BuddyItem.parseFrom(bArr);
            IListener[] all = this.mIMListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMListener) iListener).onIMBuddyPic(parseFrom);
                }
            }
            IConfService confService = VideoBoxApplication.getInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkIMBuddyPic(bArr);
                } catch (RemoteException unused) {
                }
            }
        } catch (InvalidProtocolBufferException unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMBuddySort() {
        try {
            sinkIMBuddySortImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMBuddySortImpl() {
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onIMBuddySort();
            }
        }
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService != null) {
            try {
                confService.sinkIMBuddySort();
            } catch (RemoteException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMLocalStatusChanged(int i) {
        try {
            sinkIMLocalStatusChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMLocalStatusChangedImpl(int i) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            iMHelper.setIMLocalStatus(i);
        }
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onIMLocalStatusChanged(i);
            }
        }
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService != null) {
            try {
                confService.sinkIMLocalStatusChanged(i);
            } catch (RemoteException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMCallDeclined(@NonNull byte[] bArr) {
        try {
            sinkIMCallDeclinedImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMCallDeclinedImpl(@NonNull byte[] bArr) {
        try {
            InvitationItem parseFrom = InvitationItem.parseFrom(bArr);
            long meetingNumber = parseFrom.getMeetingNumber();
            IListener[] all = this.mConfInvitationListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IConfInvitationListener) iListener).onCallDeclined(parseFrom);
                }
            }
            if (PTApp.getInstance().getActiveMeetingNo() == meetingNumber) {
                PTApp.getInstance().tryEndCallForDeclined();
            }
            IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkPTCommonEvent(2, ZmPtUtils.enhanceInvitationItem(parseFrom).toByteArray());
                } catch (RemoteException unused) {
                }
            }
            IncomingCallManager.getInstance().onDeclineEventFromPTEvent(parseFrom);
        } catch (InvalidProtocolBufferException unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIMCallAccepted(@NonNull byte[] bArr) {
        try {
            sinkIMCallAcceptedImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIMCallAcceptedImpl(@NonNull byte[] bArr) {
        try {
            InvitationItem parseFrom = InvitationItem.parseFrom(bArr);
            parseFrom.getMeetingNumber();
            IListener[] all = this.mConfInvitationListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IConfInvitationListener) iListener).onCallAccepted(parseFrom);
                }
            }
            IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
            if (confService != null) {
                try {
                    confService.sinkPTCommonEvent(1, ZmPtUtils.enhanceInvitationItem(parseFrom).toByteArray());
                } catch (RemoteException unused) {
                }
            }
            IncomingCallManager.getInstance().onAcceptEventFromPTEvent(parseFrom);
        } catch (InvalidProtocolBufferException unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void sinkConfInvitation(@NonNull byte[] bArr) {
        try {
            sinkConfInvitationImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkConfInvitationImpl(@NonNull byte[] bArr) {
        try {
            InvitationItem parseFrom = InvitationItem.parseFrom(bArr);
            if (PTApp.getInstance().getActiveMeetingNo() != parseFrom.getMeetingNumber()) {
                onConfInvitation(parseFrom);
                IListener[] all = this.mConfInvitationListenerList.getAll();
                if (all != null) {
                    for (IListener iListener : all) {
                        ((IConfInvitationListener) iListener).onConfInvitation(parseFrom);
                    }
                }
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    private void onConfInvitation(@NonNull InvitationItem invitationItem) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            String senderJID = invitationItem.getSenderJID();
            if (senderJID != null && !senderJID.equals(iMHelper.getJIDMyself())) {
                IncomingCallManager.getInstance().onConfInvitation(invitationItem);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIPCWebStartNoLogin() {
        try {
            sinkIPCWebStartNoLoginImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* JADX WARNING: type inference failed for: r0v2, types: [com.zipow.videobox.VideoBoxApplication] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sinkIPCWebStartNoLoginImpl() {
        /*
            r2 = this;
            us.zoom.androidlib.app.ZMActivity r0 = p021us.zoom.androidlib.app.ZMActivity.getFrontActivity()
            boolean r1 = r0 instanceof com.zipow.videobox.IMActivity
            if (r1 == 0) goto L_0x0014
            boolean r1 = r0.isActive()
            if (r1 == 0) goto L_0x0014
            com.zipow.videobox.IMActivity r0 = (com.zipow.videobox.IMActivity) r0
            r0.loginToUse()
            goto L_0x001e
        L_0x0014:
            if (r0 == 0) goto L_0x0017
            goto L_0x001b
        L_0x0017:
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
        L_0x001b:
            com.zipow.videobox.IMActivity.showLoginToUse(r0)
        L_0x001e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.PTUI.sinkIPCWebStartNoLoginImpl():void");
    }

    /* access modifiers changed from: protected */
    public void sinkIPCWebJoinNoConfNo() {
        try {
            sinkIPCWebJoinNoConfNoImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIPCWebJoinNoConfNoImpl() {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity != null) {
            if (UIMgr.isLargeMode(frontActivity)) {
                JoinConfFragment.showJoinByNumber(frontActivity.getSupportFragmentManager(), null, null);
            } else {
                JoinConfActivity.showJoinByNumber(frontActivity, null, null);
            }
        } else if (PTApp.getInstance().getIMHelper() != null) {
            IMActivity.showJoinByNumber(VideoBoxApplication.getInstance());
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIPCWebStartNeedForceUpdate() {
        try {
            sinkIPCWebStartNeedForceUpdateImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIPCWebStartNeedForceUpdateImpl() {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (!(frontActivity instanceof IMActivity) || !frontActivity.isActive()) {
            IMActivity.showStartCallFailedNeedUpdateOnResume();
            if (frontActivity != null) {
                IMActivity.show(frontActivity);
                return;
            }
            Intent intent = new Intent(VideoBoxApplication.getInstance(), IMActivity.class);
            intent.addFlags(268566528);
            ActivityStartHelper.startActivityForeground(VideoBoxApplication.getInstance(), intent);
            return;
        }
        ForceUpdateDialogFragment.show(frontActivity.getSupportFragmentManager());
    }

    /* access modifiers changed from: protected */
    public void sinkIPCLoginToClaimHost(int i) {
        try {
            sinkIPCLoginToClaimHostImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIPCConfirmConfLeave(String str, boolean z, int i) {
        try {
            sinkIPCConfirmConfLeaveImpl(str, z, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* access modifiers changed from: private */
    public void processGiftFreeMeeting() {
        PTApp.getInstance();
        int i = this.mLeaveReason;
        if (i == 1 || i == 2 || i == 38) {
            int i2 = this.mFreeMeetingTimes;
            if (i2 < 3 && i2 > 0) {
                FreeMeetingEndActivity.show(VideoBoxApplication.getNonNullInstance(), this.mFreeMeetingTimes, this.mUpgradeUrl);
            }
        }
    }

    private void sinkIPCConfirmConfLeaveImpl(@Nullable String str, boolean z, int i) {
        if (str != null) {
            try {
                VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
                if (instance2 != null) {
                    int parseInt = Integer.parseInt(str);
                    boolean z2 = true;
                    if (!(parseInt == 1 || parseInt == 2)) {
                        if (parseInt != 38) {
                            z2 = false;
                        }
                    }
                    if (z2) {
                        PTApp instance3 = PTApp.getInstance();
                        int freeMeetingGiftTime = instance3.getFreeMeetingGiftTime();
                        if (freeMeetingGiftTime < 3 && freeMeetingGiftTime > 0) {
                            this.mFreeMeetingTimes = freeMeetingGiftTime;
                            this.mLeaveReason = parseInt;
                            this.mUpgradeUrl = instance3.getGiftUpgradeUrl();
                            if (!StringUtil.isEmptyOrNull(this.mUpgradeUrl)) {
                                if (instance2.isConfProcessRunning()) {
                                    instance2.addConfProcessListener(this.mIConfProcessListener);
                                    this.mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            VideoBoxApplication instance = VideoBoxApplication.getInstance();
                                            if (instance != null) {
                                                instance.removeConfProcessListener(PTUI.this.mIConfProcessListener);
                                            }
                                        }
                                    }, 10000);
                                } else {
                                    processGiftFreeMeeting();
                                }
                            }
                        }
                    }
                }
                for (IListener iListener : this.mConfFailLisenerList.getAll()) {
                    ((IConfFailListener) iListener).onConfFail(this.mLeaveReason, i);
                }
            } catch (NumberFormatException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkIPCConfCallOutStatusChanged(int i) {
        try {
            sinkIPCConfCallOutStatusChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkIPCConfCallOutStatusChangedImpl(int i) {
        IListener[] all = this.mInviteByCallOutListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInviteByCallOutListener) iListener).onCallOutStatusChanged(i);
            }
        }
    }

    public void dispatchCallMeStatusChanged(int i) {
        IListener[] all = this.mCallMeListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ICallMeListener) iListener).onCallMeStatusChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkSubscriptionRequest() {
        try {
            sinkSubscriptionRequestImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkSubscriptionRequestImpl() {
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onSubscriptionRequest();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkSubscriptionUpdate() {
        try {
            sinkSubscriptionUpdateImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkSubscriptionUpdateImpl() {
        IListener[] all = this.mIMListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMListener) iListener).onSubscriptionUpdate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkScheduleMeetingResult(int i, byte[] bArr, String str) {
        try {
            sinkScheduleMeetingResultImpl(i, bArr, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkScheduleMeetingResultImpl(int i, @Nullable byte[] bArr, String str) {
        MeetingInfoProto meetingInfoProto;
        if (bArr == null || bArr.length <= 0) {
            meetingInfoProto = null;
        } else {
            try {
                meetingInfoProto = MeetingInfoProto.parseFrom(bArr);
            } catch (InvalidProtocolBufferException unused) {
                return;
            }
        }
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onScheduleMeetingResult(i, meetingInfoProto, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkListMeetingResult(int i) {
        try {
            sinkListMeetingResultImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkListMeetingResultImpl(int i) {
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        ZMPTIMeetingMgr.getInstance().setmIsPullingCloudMeetings(false);
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onListMeetingResult(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkListCalendarEventsResult(int i) {
        try {
            sinkListCalendarEventsResultImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkListCalendarEventsResultImpl(int i) {
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onListCalendarEventsResult(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:0|(2:2|3)|4|5|(4:7|(2:9|10)|13|16)(1:15)) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0023, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        java.lang.Thread.getDefaultUncaughtExceptionHandler().uncaughtException(java.lang.Thread.currentThread(), r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x000d */
    /* JADX WARNING: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0015 A[Catch:{ Throwable -> 0x0023 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void presentToRoomStatusUpdate(int r5) {
        /*
            r4 = this;
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            com.zipow.videobox.IConfService r0 = r0.getConfService()
            if (r0 == 0) goto L_0x000d
            r0.sinkPTPresentToRoomEvent(r5)     // Catch:{ RemoteException -> 0x000d }
        L_0x000d:
            us.zoom.androidlib.util.ListenerList r0 = r4.mPresentToRoomStatusListener     // Catch:{ Throwable -> 0x0023 }
            us.zoom.androidlib.util.IListener[] r0 = r0.getAll()     // Catch:{ Throwable -> 0x0023 }
            if (r0 == 0) goto L_0x002f
            int r1 = r0.length     // Catch:{ Throwable -> 0x0023 }
            r2 = 0
        L_0x0017:
            if (r2 >= r1) goto L_0x002f
            r3 = r0[r2]     // Catch:{ Throwable -> 0x0023 }
            com.zipow.videobox.ptapp.PTUI$IPresentToRoomStatusListener r3 = (com.zipow.videobox.ptapp.PTUI.IPresentToRoomStatusListener) r3     // Catch:{ Throwable -> 0x0023 }
            r3.presentToRoomStatusUpdate(r5)     // Catch:{ Throwable -> 0x0023 }
            int r2 = r2 + 1
            goto L_0x0017
        L_0x0023:
            r5 = move-exception
            java.lang.Thread$UncaughtExceptionHandler r0 = java.lang.Thread.getDefaultUncaughtExceptionHandler()
            java.lang.Thread r1 = java.lang.Thread.currentThread()
            r0.uncaughtException(r1, r5)
        L_0x002f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.PTUI.presentToRoomStatusUpdate(int):void");
    }

    /* access modifiers changed from: protected */
    public void sinkUpdateMeetingResult(int i, byte[] bArr, String str) {
        try {
            sinkUpdateMeetingResultImpl(i, bArr, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkUpdateMeetingResultImpl(int i, @Nullable byte[] bArr, String str) {
        MeetingInfoProto meetingInfoProto;
        if (bArr == null || bArr.length <= 0) {
            meetingInfoProto = null;
        } else {
            try {
                meetingInfoProto = MeetingInfoProto.parseFrom(bArr);
            } catch (InvalidProtocolBufferException unused) {
                return;
            }
        }
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onUpdateMeetingResult(i, meetingInfoProto, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkDeleteMeetingResult(int i) {
        try {
            sinkDeleteMeetingResultImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkDeleteMeetingResultImpl(int i) {
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onDeleteMeetingResult(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkStartFailBeforeLaunch(int i) {
        try {
            sinkStartFailBeforeLaunchImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkStartFailBeforeLaunchImpl(int i) {
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity != null && frontActivity.isActive()) {
            SimpleMessageDialog.newInstance(frontActivity.getString(C4558R.string.zm_msg_cannot_start_meeting, new Object[]{Integer.valueOf(i)})).show(frontActivity.getSupportFragmentManager(), SimpleMessageDialog.class.getName());
        }
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onStartFailBeforeLaunch(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkPMIEvent(int i, int i2, byte[] bArr) {
        try {
            sinkPMIEventImpl(i, i2, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkPMIEventImpl(int i, int i2, @Nullable byte[] bArr) {
        MeetingInfoProto meetingInfoProto;
        if (bArr == null || bArr.length <= 0) {
            meetingInfoProto = null;
        } else {
            try {
                meetingInfoProto = MeetingInfoProto.parseFrom(bArr);
            } catch (InvalidProtocolBufferException unused) {
                return;
            }
        }
        IListener[] all = this.mMeetingMgrListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IMeetingMgrListener) iListener).onPMIEvent(i, i2, meetingInfoProto);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkSearchDomainUser(String str, int i, int i2, List<ZoomContact> list) {
        try {
            sinkSearchDomainUserImpl(str, i, i2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkSearchDomainUserImpl(String str, int i, int i2, List<ZoomContact> list) {
        IListener[] all = this.mFavoriteListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IFavoriteListener) iListener).onFinishSearchDomainUser(str, i, i2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkFavoriteEvent(int i, long j) {
        try {
            sinkFavoriteEventImpl(i, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkFavoriteEventImpl(int i, long j) {
        IListener[] all = this.mFavoriteListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IFavoriteListener) iListener).onFavoriteEvent(i, j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkFavAvatarReady(String str) {
        try {
            sinkFavAvatarReadyImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkFavAvatarReadyImpl(String str) {
        IListener[] all = this.mFavoriteListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IFavoriteListener) iListener).onFavAvatarReady(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkPhoneABEvent(int i, long j, Object obj) {
        try {
            sinkPhoneABEventImpl(i, j, obj);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkPhoneABEventImpl(int i, long j, Object obj) {
        if (i == 1 && j == 0) {
            ABContactsHelper.setMatchPhoneNumbersCalled(false);
            ABContactsHelper.setAddrBookEnabledDone(false);
        }
        if (!(i == 0 && j == 0)) {
            if (i == 1) {
                IMAddrBookListView.clearCaches();
                boolean isDirectCallAvailable = PTApp.getInstance().isDirectCallAvailable();
            } else if (i == 3) {
                IMAddrBookListView.clearCaches();
                int i2 = (int) j;
                if (!(i2 == 0 || i2 == 1102 || i2 == 1104)) {
                    ABContactsHelper.setLastMatchPhoneNumbersTime(0);
                }
            }
        }
        IListener[] all = this.mPhoneABListenerList.getAll();
        ZMBuddySyncInstance.getInsatance().onPhoneABEvent(i, j, obj);
        if (all != null) {
            for (IListener iListener : all) {
                ((IPhoneABListener) iListener).onPhoneABEvent(i, j, obj);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean promptToInputUserNamePasswordForProxyServer(final String str, final int i, String str2) {
        this.mHandler.post(new Runnable() {
            public void run() {
                PTUI.this.promptToInputUserNamePasswordForProxyServerImpl(str, i);
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public void promptToInputUserNamePasswordForProxyServerImpl(final String str, final int i) {
        if (VideoBoxApplication.getNonNullInstance().isSDKMode()) {
            PromptProxyServerTaskManager.getInstance().run(new PromptProxyServerTask("promptToInputUserNamePasswordForProxyServer") {
                public void run() {
                    IntegrationActivity.promptToInputUserNamePasswordForProxyServer(VideoBoxApplication.getNonNullInstance(), str, i);
                }
            });
            return;
        }
        ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask("promptToInputUserNamePasswordForProxyServer") {
            public boolean isMultipleInstancesAllowed() {
                return false;
            }

            public void run(ZMActivity zMActivity) {
                IntegrationActivity.promptToInputUserNamePasswordForProxyServer(VideoBoxApplication.getNonNullInstance(), str, i);
            }

            public boolean isValidActivity(String str) {
                if (LauncherActivity.class.getName().equals(str)) {
                    return false;
                }
                return super.isValidActivity(str);
            }

            public boolean hasAnotherProcessAtFront() {
                IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
                if (confService != null) {
                    try {
                        return confService.isConfAppAtFront();
                    } catch (RemoteException unused) {
                    }
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void sinkSDKOnAuth(int i) {
        try {
            sinkSDKOnAuthImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkSDKOnAuthImpl(int i) {
        IListener[] all = this.mSDKAuthListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISDKAuthListener) iListener).onSDKAuth(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void NotifyZAKRefreshFailed(int i) {
        try {
            notifyZAKRefreshFailedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyZAKRefreshFailedImpl(int i) {
        IListener[] all = this.mNotifyZAKRefreshListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotifyZAKListener) iListener).notifyZAKRefreshFailed(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sinkRoomCallEvent(int i, long j, boolean z) {
        try {
            sinkRoomCallEventImpl(i, j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkRoomCallEventImpl(int i, long j, boolean z) {
        IListener[] all = this.mRoomCallListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IRoomCallListener) iListener).onRoomCallEvent(i, j, z);
            }
        }
        if (!PTApp.getInstance().isNeedFilterCallRoomEventCallbackInMeeting()) {
            IPCHelper.getInstance().sendRoomSystemCallStatus(i, j, z);
        }
    }

    /* access modifiers changed from: protected */
    public void sinkOnProfileFieldUpdated(String str, int i, int i2, String str2) {
        try {
            sinkOnProfileFieldUpdatedImpl(str, i, i2, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkOnProfileFieldUpdatedImpl(String str, int i, int i2, String str2) {
        IListener[] all = this.mProfileListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IProfileListener) iListener).OnProfileFieldUpdated(str, i, i2, str2);
            }
        }
    }

    private boolean VTLS_NotifyCertItemVerifyFailed(VerifyCertEvent verifyCertEvent) {
        try {
            VTLS_NotifyCertItemVerifyFailedImpl(verifyCertEvent);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        return true;
    }

    private void VTLS_NotifyCertItemVerifyFailedImpl(final VerifyCertEvent verifyCertEvent) {
        this.mHandler.post(new Runnable() {
            public void run() {
                PTUI.this.promptVerifyCertFailureConfirmation(verifyCertEvent);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void sinkCalendarAuthResult(int i) {
        try {
            sinkCalendarAuthResultImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void sinkCalendarAuthResultImpl(int i) {
        IListener[] all = this.mCalendarAuthListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ICalendarAuthListener) iListener).onCalendarAuthResult(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onShowSignToJoinOption(String str, boolean z, String str2, String str3) {
        try {
            onShowSignToJoinOptionImpl(str, z, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onShowSignToJoinOptionImpl(String str, boolean z, String str2, String str3) {
        ZMNoticeChooseDomainTask zMNoticeChooseDomainTask = new ZMNoticeChooseDomainTask(ZMNoticeChooseDomainTask.class.getName(), str, z, str2, str3);
        ForegroundTaskManager.getInstance().runInForeground(zMNoticeChooseDomainTask);
    }

    /* access modifiers changed from: protected */
    public void onShowLoginDialog(String str) {
        try {
            onShowLoginDialogImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onShowLoginDialogImpl(String str) {
        LoginUtil.launchLogin(str, true);
    }

    /* access modifiers changed from: protected */
    public void onAppProtocolActionBlocked(int i, long j) {
        try {
            onAppProtocolActionBlockedImpl(i, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onAppProtocolActionBlockedImpl(int i, long j) {
        ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeProtocolActionBlockedTask(ZMNoticeProtocolActionBlockedTask.class.getName(), LoginUtil.getFmtRestrictedLoginDomain()));
    }

    /* access modifiers changed from: protected */
    public void onWebLaunchedToLogin(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            try {
                WebLaunchedToLoginParam parseFrom = WebLaunchedToLoginParam.parseFrom(bArr);
                if (parseFrom != null && !ZmLoginHelper.isMeetingProcessRun()) {
                    if (PTApp.getInstance().isWebSignedOn()) {
                        ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeOnWebLoginTask(ZMNoticeOnWebLoginTask.class.getName(), parseFrom));
                    } else {
                        ZmLoginHelper.goLoginActivity(parseFrom);
                    }
                }
            } catch (InvalidProtocolBufferException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onShowAgeGatingDialog() {
        ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeOnShowAgeGatingTask(ZMNoticeOnShowAgeGatingTask.class.getName()));
    }

    /* access modifiers changed from: protected */
    public void onShowCrashReport() {
        ForegroundTaskManager.getInstance().runInForeground(new ZMNoticeOnShowCrashReport(ZMNoticeOnShowCrashReport.class.getName()));
    }

    /* access modifiers changed from: private */
    public void promptVerifyCertFailureConfirmation(final VerifyCertEvent verifyCertEvent) {
        ForegroundTaskManager.getInstance().runInForeground(new ForegroundTask("promptVerifyCertFailureConfirmation") {
            public void run(ZMActivity zMActivity) {
                IntegrationActivity.promptVerifyCertFailureConfirmation(VideoBoxApplication.getInstance(), verifyCertEvent);
            }

            public boolean isValidActivity(String str) {
                if (LauncherActivity.class.getName().equals(str)) {
                    return false;
                }
                return super.isValidActivity(str);
            }

            public boolean hasAnotherProcessAtFront() {
                IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
                if (confService != null) {
                    try {
                        return confService.isConfAppAtFront();
                    } catch (RemoteException unused) {
                    }
                }
                return false;
            }
        });
    }

    public String getLatestMeetingId() {
        return this.mLatestMeetingId;
    }

    public long getLatestMeetingNumber() {
        return this.mLatestMeetingNumber;
    }

    private void onCallStatusChanged(long j) {
        int i = (int) j;
        if (i == 2) {
            this.mLatestMeetingId = PTApp.getInstance().getActiveCallId();
            this.mLatestMeetingNumber = PTApp.getInstance().getActiveMeetingNo();
            BOStatusChangeMgrOnPT.getInstance().handleStatusChangeCompeleted();
            WaitingRoomStatusChangeMgrOnPT.getInstance().handleStatusChangeComplete();
        }
        broadcastCallStatus(i);
    }

    private void broadcastCallStatus(int i) {
        VideoBoxApplication nonNullInstance = VideoBoxApplication.getNonNullInstance();
        switch (i) {
            case 1:
                SDKHost.broadcastMeetingStatus(nonNullInstance, 1);
                return;
            case 2:
                SDKHost.broadcastMeetingStatus(nonNullInstance, 2);
                return;
            default:
                SDKHost.broadcastMeetingStatus(nonNullInstance, 0);
                return;
        }
    }

    public void onJoinConfMeetingStatus(boolean z, boolean z2) {
        IListener[] all = this.mJoinConfMeetingStatusListener.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IJoinConfMeetingStatusListener) iListener).onJoinConfMeetingStatus(z, z2);
            }
        }
    }

    /* access modifiers changed from: private */
    public long getXmppAutoSignInThreshold() {
        if (this.mXMPPAutoLoginRandomTimeInterval == 0) {
            this.mXMPPAutoLoginRandomTimeInterval = ZMSecureRandom.nextInt(XMPP_AUTO_SIGNIN_TIME_INTERVAL_UNIT);
        }
        if (NOSMgr.getInstance().isGCMRegistered()) {
            return ((long) this.mXMPPAutoLoginRandomTimeInterval) + XMPP_AUTO_SIGNIN_THRESHOLD_GCM;
        }
        int dataNetworkType = NetworkUtil.getDataNetworkType(VideoBoxApplication.getInstance());
        if (dataNetworkType == 1 || dataNetworkType == 4) {
            return ((long) this.mXMPPAutoLoginRandomTimeInterval) + XMPP_AUTO_SIGNIN_THRESHOLD_WIFI;
        }
        return ((long) this.mXMPPAutoLoginRandomTimeInterval) + XMPP_AUTO_SIGNIN_THRESHOLD_NO_WIFI;
    }

    public void startXmppAutoSignInTimer() {
        if (this.mXmppAutoSignInTask == null) {
            this.mXmppAutoSignInLastCheckTime = System.currentTimeMillis();
            this.mXmppAutoSignInScheduler = Executors.newSingleThreadScheduledExecutor();
            this.mXmppAutoSignInTask = this.mXmppAutoSignInScheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    VideoBoxApplication.getNonNullInstance().runOnMainThread(PTUI.this.mXmppAutoSignInRunnable);
                }
            }, XMPP_AUTO_SIGNIN_CHECK_TIME, XMPP_AUTO_SIGNIN_CHECK_TIME, TimeUnit.SECONDS);
        }
    }

    /* access modifiers changed from: private */
    public void stopXmppAutoSignInTimer() {
        ScheduledFuture<?> scheduledFuture = this.mXmppAutoSignInTask;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            this.mXmppAutoSignInTask = null;
            ScheduledExecutorService scheduledExecutorService = this.mXmppAutoSignInScheduler;
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdown();
                this.mXmppAutoSignInScheduler = null;
            }
        }
    }

    public static void enableSendFileActivity(boolean z) {
        try {
            VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
            instance2.getPackageManager().setComponentEnabledSetting(new ComponentName(instance2, "us.zoom.videomeetings.SendFileActivity"), z ? 1 : 2, 1);
        } catch (Exception unused) {
        }
    }

    public void notifyLeaveAndPerformAction(int i, int i2, int i3) {
        if (i == LeaveConfAction.SHOW_JOIN_ERROR.ordinal()) {
            JoinMeetingFailActivity.showJoinFailedMessage(VideoBoxApplication.getNonNullInstance(), JoinMeetingFailActivity.class.getName(), i2, i3);
        } else if (i == LeaveConfAction.BEFORE_CONF_KILL_HIMSELF_PROCESS.ordinal()) {
            VideoBoxApplication.getNonNullInstance().stopConfProcessDirect();
        } else if (i == LeaveConfAction.LOG_FORCE_SIGN_IN.ordinal()) {
            ZoomLogEventTracking.eventTrackForceSignIn();
        } else if (i == LeaveConfAction.LOG_MEETING.ordinal()) {
            ZoomLogEventTracking.eventTrackHostEndMeeting(i2);
        }
    }
}
