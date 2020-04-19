package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.BuddyInviteFragment;
import com.zipow.videobox.fragment.JoinConfFragment;
import com.zipow.videobox.fragment.NewVersionDialog;
import com.zipow.videobox.fragment.NewVersionDialog.RequestPermissionListener;
import com.zipow.videobox.fragment.SettingFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.fragment.SystemNotificationFragment;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomAccount;
import com.zipow.videobox.ptapp.PTBuddyHelper;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IConfInvitationListener;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.IPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.IPBXMessageEventSinkUI.SimpleIPBXMessageEventSinkUIListener;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.ISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.UpgradeUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.IMBuddyItem;
import com.zipow.videobox.view.IMView;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.thirdparty.login.facebook.AuthToken;
import p021us.zoom.thirdparty.login.facebook.FBSessionStore;
import p021us.zoom.thirdparty.login.facebook.FacebookError;
import p021us.zoom.thirdparty.login.facebook.ServiceListener;
import p021us.zoom.videomeetings.C4558R;

public class IMActivity extends ZMActivity implements IPTUIListener, IIMListener, IConfInvitationListener, IPhoneABListener {
    public static final String ACTION_LOGIN_AS_HOST;
    public static final String ACTION_PBX_SHOW_UNREAD_MESSAGE;
    public static final String ACTION_SHOW_AND_UPGRADE;
    public static final String ACTION_SHOW_JOIN_BY_NO;
    public static final String ACTION_SHOW_LOGIN_TO_USE;
    public static final String ACTION_SHOW_SIP_CALL_DIALPAD;
    public static final String ACTION_SHOW_SIP_CALL_HISTORY;
    public static final String ACTION_SHOW_UNREAD_MESSAGE;
    public static final String ACTION_SHOW_UNREAD_MESSAGE_MM;
    private static final String ARG_CLEAR_OTHER_ACTIVITIES = "clearOtherActivities";
    public static final String ARG_JOIN_MEETING_URL;
    public static final String ARG_NEW_VERSIONS = "ARG_NEW_VERSIONS";
    public static final String ARG_PBX_MESSAGE_PROTO = "ARG_PBX_MESSAGE_PROTO";
    public static final String ARG_PBX_MESSAGE_SESSION_ID = "ARG_PBX_MESSAGE_SESSION_ID";
    public static final String ARG_SIP_PHONE_NUMBER = "ARG_SIP_PHONE_NUMBER";
    public static final String ARG_USE_PASSWD = "ARG_USE_PASSWD";
    public static final int REQUEST_AB_DETAILS = 106;
    public static final int REQUEST_CHAT = 100;
    public static final int REQUEST_HISTORYINFO = 105;
    public static final int REQUEST_INVITE_BUDDY = 102;
    public static final int REQUEST_MEETINGINFO = 104;
    public static final int REQUEST_MIC = 108;
    public static final int REQUEST_SCHEDULE = 103;
    public static final int REQUEST_SETTINGS = 101;
    public static final int REQUEST_STORAGE_NEW_VERSION = 107;
    private static final String TAG = "IMActivity";
    private static boolean gbShowRateRoomDialog = false;
    private static boolean gbShowStartCallFailedNeedUpdate = false;
    /* access modifiers changed from: private */
    @Nullable
    public AuthToken mFacebookAuthToken;
    /* access modifiers changed from: private */
    public IMView mIMView;
    @Nullable
    private Boolean mIsAddressBookEnalbed = null;
    private ZMKeyboardDetector mKeyboardDetector;
    private SimpleISIPLineMgrEventSinkListener mLineListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            if (CmmSIPLineManager.getInstance().isMineLine(str)) {
                IMActivity.this.onSipCallEvent();
            }
        }
    };
    private IPBXMessageEventSinkUIListener mMessageEventSinkUIListener = new SimpleIPBXMessageEventSinkUIListener() {
        public void OnTotalUnreadCountChanged() {
            super.OnTotalUnreadCountChanged();
            IMActivity.this.onPBXMessageUnreadCountChanged();
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mNewMsgRunnable;
    @NonNull
    private INotificationSettingUIListener mNotificationSettingUIListener = new SimpleNotificationSettingUIListener() {
        public void OnUnreadBadgeSettingUpdated() {
            IMActivity.this.onUnreadBadgeSettingUpdated();
        }
    };
    @NonNull
    private ISIPCallRepositoryEventSinkListener mRepositoryEventSinkListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnTotalUnreadVoiceMailCountChanged(int i) {
            super.OnTotalUnreadVoiceMailCountChanged(i);
            IMActivity.this.onSipVoiceMailsCountChanged();
        }

        public void OnMissedCallHistoryChanged(int i) {
            super.OnMissedCallHistoryChanged(i);
            IMActivity.this.onSipCallHistoryCountChanged();
        }
    };
    @NonNull
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnCallStatusUpdate(String str, int i) {
            IMActivity.this.onSipCallEvent();
        }

        public void OnSIPCallServiceStarted() {
            IMActivity.this.onSipCallEvent();
        }

        public void OnPBXUserStatusChange(int i) {
            IMActivity.this.onSipCallEvent();
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            IMActivity.this.onSipCallEvent();
        }
    };
    @NonNull
    private SimpleNetworkStatusListener mSipNetworkListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            IMActivity.this.onSipCallEvent();
        }
    };
    private ZMTipLayer mTipLayer;
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    class ExtendTokenServiceListener implements ServiceListener {
        ExtendTokenServiceListener() {
        }

        public void onComplete(Bundle bundle) {
            IMActivity iMActivity = IMActivity.this;
            iMActivity.onFBTokenExtended(iMActivity.mFacebookAuthToken != null ? IMActivity.this.mFacebookAuthToken.token : null);
        }

        public void onFacebookError(@NonNull FacebookError facebookError) {
            LoginActivity.show(IMActivity.this, true);
        }

        public void onError(@NonNull Error error) {
            LoginActivity.show(IMActivity.this, true);
        }
    }

    private void handleOnWebLogout(long j) {
    }

    private void sinkIMLogout(long j) {
    }

    public void onCallAccepted(InvitationItem invitationItem) {
    }

    public void onCallDeclined(InvitationItem invitationItem) {
    }

    public void onConfInvitation(InvitationItem invitationItem) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(IMActivity.class.getName());
        sb.append(".action.SHOW_UNREAD_MESSAGE");
        ACTION_SHOW_UNREAD_MESSAGE = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(IMActivity.class.getName());
        sb2.append(".action.SHOW_UNREAD_MESSAGE_MM");
        ACTION_SHOW_UNREAD_MESSAGE_MM = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(IMActivity.class.getName());
        sb3.append(".action.ACTION_SHOW_JOIN_BY_NO");
        ACTION_SHOW_JOIN_BY_NO = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(IMActivity.class.getName());
        sb4.append(".action.ACTION_SHOW_LOGIN_TO_USE");
        ACTION_SHOW_LOGIN_TO_USE = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(IMActivity.class.getName());
        sb5.append(".action.ACTION_LOGIN_AS_HOST");
        ACTION_LOGIN_AS_HOST = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(IMActivity.class.getName());
        sb6.append(".action.ACTION_SHOW_AND_UPGRADE");
        ACTION_SHOW_AND_UPGRADE = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(IMActivity.class.getName());
        sb7.append(".action.ACTION_SHOW_SIP_CALL_DIALPAD");
        ACTION_SHOW_SIP_CALL_DIALPAD = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(IMActivity.class.getName());
        sb8.append(".action.ACTION_SHOW_SIP_CALL_HISTORY");
        ACTION_SHOW_SIP_CALL_HISTORY = sb8.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(IMActivity.class.getName());
        sb9.append(".action.PBX_SHOW_UNREAD_MESSAGE");
        ACTION_PBX_SHOW_UNREAD_MESSAGE = sb9.toString();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(IMActivity.class.getName());
        sb10.append(".arg.join.meeting.url");
        ARG_JOIN_MEETING_URL = sb10.toString();
    }

    public static void show(Context context) {
        show(context, false, null, null);
    }

    public static void showWithPasswd(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_USE_PASSWD, true);
        show(context, false, null, bundle);
    }

    public static void show(Context context, boolean z) {
        show(context, z, null, null);
    }

    public static void show(Context context, boolean z, String str) {
        show(context, z, str, null);
    }

    public static void show(Context context, boolean z, @Nullable String str, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, IMActivity.class);
        intent.addFlags(131072);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        if (str != null) {
            intent.setAction(str);
        }
        intent.putExtra(ARG_CLEAR_OTHER_ACTIVITIES, z);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void showJoinByNumber(Context context) {
        Intent intent = new Intent(context, IMActivity.class);
        intent.setAction(ACTION_SHOW_JOIN_BY_NO);
        intent.addFlags(131072);
        Context frontActivity = ZMActivity.getFrontActivity();
        if (frontActivity == null) {
            intent.addFlags(268435456);
        } else {
            context = frontActivity;
        }
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void showLoginToUse(Context context) {
        Intent intent = new Intent(context, IMActivity.class);
        intent.setAction(ACTION_SHOW_LOGIN_TO_USE);
        intent.addFlags(131072);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void showStartCallFailedNeedUpdateOnResume() {
        gbShowStartCallFailedNeedUpdate = true;
    }

    public static void showRateRoomDialogOnResume() {
        gbShowRateRoomDialog = true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        disableFinishActivityByGesture(true);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            showLauncherActivity();
            finish();
            return;
        }
        if (UIMgr.isLargeMode(this) && !UIMgr.isDualPaneSupportedInPortraitMode(this)) {
            setRequestedOrientation(0);
        } else if (UIUtil.isTablet(this) || UIUtil.isTV(this)) {
            setRequestedOrientation(4);
        } else if (PTApp.getInstance().hasMessenger()) {
            setRequestedOrientation(1);
        }
        setContentView(C4558R.layout.zm_im_main_screen);
        this.mIMView = (IMView) findViewById(C4558R.C4560id.imView);
        this.mTipLayer = (ZMTipLayer) findViewById(C4558R.C4560id.tipLayer);
        this.mKeyboardDetector = (ZMKeyboardDetector) findViewById(C4558R.C4560id.keyboardDetector);
        this.mKeyboardDetector.setKeyboardListener(this.mIMView);
        initTipLayer();
        PTUI.getInstance().addPTUIListener(this);
        PTUI.getInstance().addIMListener(this);
        PTUI.getInstance().addConfInvitationListener(this);
        PTUI.getInstance().addPhoneABListener(this);
        PTApp.getInstance().setLanguageIdAsSystemConfiguration();
        checkFingerPrint();
    }

    private void checkFingerPrint() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getBoolean(ARG_USE_PASSWD, false) && PTApp.getInstance().getPTLoginType() == 100 && ZmPtUtils.isSupportFingerprintAndDisableFingerprintWithUserInfo(this)) {
                DialogUtils.showAlertDialog((ZMActivity) this, C4558R.string.zm_title_confirm_sign_in_fingerprint_22438, C4558R.string.zm_btn_ok, C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FingerprintOption readFromPreference = FingerprintOption.readFromPreference();
                        if (readFromPreference != null && !readFromPreference.ismEnableFingerprint()) {
                            readFromPreference.setmEnableFingerprint(true);
                            readFromPreference.savePreference();
                        }
                    }
                });
            }
        }
    }

    private void showLauncherActivity() {
        Intent intent = getIntent();
        String action = intent != null ? intent.getAction() : null;
        if (intent != null) {
            LauncherActivity.showLauncherActivity(this, action, intent.getExtras());
        }
        finish();
    }

    private void initTipLayer() {
        ZMTipLayer zMTipLayer = this.mTipLayer;
        if (zMTipLayer != null) {
            zMTipLayer.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return IMActivity.this.closeTips();
                }
            });
        }
    }

    public boolean isKeyboardOpen() {
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector == null) {
            return false;
        }
        return zMKeyboardDetector.isKeyboardOpen();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void showUnreadMessageThirdPartyIM() {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
            if (buddyHelper != null) {
                int buddyItemCount = buddyHelper.getBuddyItemCount();
                String str = null;
                for (int i = 0; i < buddyItemCount; i++) {
                    String buddyItemJid = buddyHelper.getBuddyItemJid(i);
                    IMSession sessionBySessionName = iMHelper.getSessionBySessionName(buddyItemJid);
                    if (sessionBySessionName != null && sessionBySessionName.getUnreadMessageCount() > 0) {
                        if (str != null) {
                            this.mIMView.showBuddyList();
                            return;
                        }
                        str = buddyItemJid;
                    }
                }
                if (str != null) {
                    BuddyItem buddyItemByJid = buddyHelper.getBuddyItemByJid(str);
                    if (buddyItemByJid != null) {
                        showChatUI(new IMBuddyItem().parseFromProtoItem(buddyItemByJid));
                    }
                }
            }
        }
    }

    private void showUnreadMessageMM() {
        int i;
        int i2;
        NotificationMgr.removeMessageNotificationMM(this);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i3 = 0;
        if (zoomMessenger != null) {
            i2 = zoomMessenger.getTotalUnreadMessageCount();
            i = zoomMessenger.getUnreadRequestCount();
            Intent intent = getIntent();
            if (intent != null) {
                String stringExtra = intent.getStringExtra(IntegrationActivity.ARG_UNREAD_MESSAGE_SESSION);
                if (!StringUtil.isEmptyOrNull(stringExtra)) {
                    if (stringExtra.equals(zoomMessenger.getContactRequestsSessionID()) || intent.getBooleanExtra(IntegrationActivity.ARG_ADD_CONTACT, false)) {
                        showSystemNotification();
                    } else {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(stringExtra);
                        if (sessionById != null) {
                            if (sessionById.isGroup()) {
                                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                                if (sessionGroup != null) {
                                    String groupID = sessionGroup.getGroupID();
                                    if (!StringUtil.isEmptyOrNull(groupID)) {
                                        startGroupChat(groupID);
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                                if (sessionBuddy != null) {
                                    startOneToOneChat(sessionBuddy);
                                } else {
                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            i2 = 0;
            i = 0;
        }
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            i3 = iMHelper.getUnreadMsgCount();
        }
        if (i == 0 && i2 == 0 && i3 > 0) {
            showUnreadMessageThirdPartyIM();
        }
        if (i == 0 && i2 > 0 && i3 == 0) {
            this.mIMView.showChatsList();
        } else if (i > 0 && i2 == 0 && i3 == 0) {
            showSystemNotification();
        } else {
            this.mIMView.showChatsList();
        }
    }

    private void showSIPCallDialpad(String str) {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.showSIPDialpad(str);
        }
    }

    private void showSIPHistory() {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.postDelayed(new Runnable() {
                public void run() {
                    if (IMActivity.this.mIMView != null) {
                        IMActivity.this.mIMView.showSIPHistory();
                    }
                }
            }, 100);
        }
    }

    private void showPBXUnreadMessage(@Nullable String str, @Nullable String str2) {
        if (!TextUtils.isEmpty(str) && CmmSIPMessageManager.getInstance().isMessageEnabled()) {
            if (!TextUtils.isEmpty(str2)) {
                CmmSIPMessageManager.getInstance().handlePushMessage(str2);
            }
            PBXSMSActivity.showAsSession(this, str);
        }
    }

    private void showSystemNotification() {
        SystemNotificationFragment.showAsActivity(this, 0);
    }

    private void startOneToOneChat(ZoomBuddy zoomBuddy) {
        MMChatActivity.showAsOneToOneChat(this, zoomBuddy);
    }

    private void startGroupChat(String str) {
        MMChatActivity.showAsGroupChat(this, str);
    }

    private void showJoinByNo() {
        if (UIMgr.isLargeMode(this)) {
            JoinConfFragment.showJoinByNumber(getSupportFragmentManager(), null, null);
        } else {
            JoinConfActivity.showJoinByNumber(this, null, null);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (isFinishing() && !PTApp.getInstance().isPhoneNumberRegistered()) {
            NotificationMgr.removeMessageNotificationMM(this);
        }
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.onActivityDestroy();
        }
        if (isFinishing()) {
            PTUI.getInstance().removePTUIListener(this);
            PTUI.getInstance().removeIMListener(this);
            PTUI.getInstance().removeConfInvitationListener(this);
            PTUI.getInstance().removePhoneABListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        NotificationSettingUI.getInstance().addListener(this.mNotificationSettingUIListener);
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineListener);
        CmmSIPCallManager.getInstance().addNetworkListener(this.mSipNetworkListener);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mRepositoryEventSinkListener);
        CmmSIPMessageManager.getInstance().addListener(this.mMessageEventSinkUIListener);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        NotificationSettingUI.getInstance().removeListener(this.mNotificationSettingUIListener);
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineListener);
        CmmSIPCallManager.getInstance().removeNetworkListener(this.mSipNetworkListener);
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mRepositoryEventSinkListener);
        CmmSIPMessageManager.getInstance().removeListener(this.mMessageEventSinkUIListener);
        super.onStop();
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0115  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0148  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0155  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x016f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onResume() {
        /*
            r5 = this;
            com.zipow.videobox.util.NotificationMgr.removeLoginExpiredNotificaiton(r5)
            r5.finishSubActivities()
            super.onResume()
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isNeedToReturnToMeetingOnResume()
            r1 = 0
            if (r0 == 0) goto L_0x0028
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            r0.setNeedToReturnToMeetingOnResume(r1)
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.hasActiveCall()
            if (r0 == 0) goto L_0x0028
            com.zipow.videobox.util.ConfLocalHelper.returnToConf(r5)
        L_0x0028:
            com.zipow.videobox.view.IMView r0 = r5.mIMView
            if (r0 != 0) goto L_0x002d
            return
        L_0x002d:
            us.zoom.thirdparty.login.facebook.AuthToken r0 = r5.mFacebookAuthToken
            if (r0 != 0) goto L_0x0039
            java.lang.String r0 = "facebook-session"
            us.zoom.thirdparty.login.facebook.AuthToken r0 = p021us.zoom.thirdparty.login.facebook.FBSessionStore.getSession(r5, r0)
            r5.mFacebookAuthToken = r0
        L_0x0039:
            r5.checkPendingCall()
            android.content.Intent r0 = r5.getIntent()
            if (r0 != 0) goto L_0x0043
            return
        L_0x0043:
            java.lang.String r2 = ACTION_SHOW_UNREAD_MESSAGE
            java.lang.String r3 = r0.getAction()
            boolean r2 = r2.equals(r3)
            r3 = 1
            if (r2 == 0) goto L_0x0055
            r5.showUnreadMessageThirdPartyIM()
            goto L_0x0103
        L_0x0055:
            java.lang.String r2 = ACTION_SHOW_UNREAD_MESSAGE_MM
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0066
            r5.showUnreadMessageMM()
            goto L_0x0103
        L_0x0066:
            java.lang.String r2 = ACTION_SHOW_JOIN_BY_NO
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0077
            r5.showJoinByNo()
            goto L_0x0103
        L_0x0077:
            java.lang.String r2 = ACTION_SHOW_LOGIN_TO_USE
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0089
            r5.loginToUse()
            r2 = 0
            goto L_0x0104
        L_0x0089:
            java.lang.String r2 = ACTION_LOGIN_AS_HOST
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00b2
            java.lang.String r2 = ARG_JOIN_MEETING_URL
            java.lang.String r2 = r0.getStringExtra(r2)
            r4 = 100
            if (r2 == 0) goto L_0x00a6
            int r4 = com.zipow.videobox.util.ZmPtUtils.parseVendorFromUrl(r2)
            com.zipow.videobox.util.ZmPtUtils.switchToVendor(r4)
        L_0x00a6:
            r5.loginToUse(r4)
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            r2.setNeedToReturnToMeetingOnResume(r3)
            r2 = 0
            goto L_0x0104
        L_0x00b2:
            java.lang.String r2 = ACTION_SHOW_AND_UPGRADE
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00c2
            com.zipow.videobox.util.UpgradeUtil.upgrade(r5)
            goto L_0x0103
        L_0x00c2:
            java.lang.String r2 = ACTION_SHOW_SIP_CALL_DIALPAD
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00d8
            java.lang.String r2 = "ARG_SIP_PHONE_NUMBER"
            java.lang.String r2 = r0.getStringExtra(r2)
            r5.showSIPCallDialpad(r2)
            goto L_0x0103
        L_0x00d8:
            java.lang.String r2 = ACTION_SHOW_SIP_CALL_HISTORY
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x00e8
            r5.showSIPHistory()
            goto L_0x0103
        L_0x00e8:
            java.lang.String r2 = ACTION_PBX_SHOW_UNREAD_MESSAGE
            java.lang.String r4 = r0.getAction()
            boolean r2 = r2.equals(r4)
            if (r2 == 0) goto L_0x0103
            java.lang.String r2 = "ARG_PBX_MESSAGE_SESSION_ID"
            java.lang.String r2 = r0.getStringExtra(r2)
            java.lang.String r4 = "ARG_PBX_MESSAGE_PROTO"
            java.lang.String r4 = r0.getStringExtra(r4)
            r5.showPBXUnreadMessage(r2, r4)
        L_0x0103:
            r2 = 1
        L_0x0104:
            if (r2 == 0) goto L_0x0109
            r5.checkAutoLogin()
        L_0x0109:
            r2 = 0
            r0.setAction(r2)
            java.lang.String r2 = "clearOtherActivities"
            boolean r2 = r0.getBooleanExtra(r2, r1)
            if (r2 == 0) goto L_0x0134
            int r2 = p021us.zoom.androidlib.app.ZMActivity.getInProcessActivityCountInStack()
            if (r2 <= 0) goto L_0x012f
            int r2 = r2 - r3
        L_0x011c:
            if (r2 < 0) goto L_0x012f
            us.zoom.androidlib.app.ZMActivity r3 = p021us.zoom.androidlib.app.ZMActivity.getInProcessActivityInStackAt(r2)
            boolean r4 = r3 instanceof com.zipow.videobox.IMActivity
            if (r4 == 0) goto L_0x0127
            goto L_0x012c
        L_0x0127:
            if (r3 == 0) goto L_0x012c
            r3.finish()
        L_0x012c:
            int r2 = r2 + -1
            goto L_0x011c
        L_0x012f:
            java.lang.String r2 = "clearOtherActivities"
            r0.putExtra(r2, r1)
        L_0x0134:
            r5.setIntent(r0)
            boolean r0 = gbShowStartCallFailedNeedUpdate
            if (r0 == 0) goto L_0x0144
            androidx.fragment.app.FragmentManager r0 = r5.getSupportFragmentManager()
            com.zipow.videobox.fragment.ForceUpdateDialogFragment.show(r0)
            gbShowStartCallFailedNeedUpdate = r1
        L_0x0144:
            boolean r0 = gbShowRateRoomDialog
            if (r0 == 0) goto L_0x0151
            androidx.fragment.app.FragmentManager r0 = r5.getSupportFragmentManager()
            com.zipow.videobox.fragment.RateZoomDialogFragment.show(r0)
            gbShowRateRoomDialog = r1
        L_0x0151:
            com.zipow.videobox.ptapp.ZoomMessengerUI$IZoomMessengerUIListener r0 = r5.mZoomMessengerUIListener
            if (r0 != 0) goto L_0x015c
            com.zipow.videobox.IMActivity$10 r0 = new com.zipow.videobox.IMActivity$10
            r0.<init>()
            r5.mZoomMessengerUIListener = r0
        L_0x015c:
            com.zipow.videobox.ptapp.ZoomMessengerUI r0 = com.zipow.videobox.ptapp.ZoomMessengerUI.getInstance()
            com.zipow.videobox.ptapp.ZoomMessengerUI$IZoomMessengerUIListener r2 = r5.mZoomMessengerUIListener
            r0.addListener(r2)
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r0 = r0.isWebSignedOn()
            if (r0 == 0) goto L_0x019e
            java.lang.Boolean r0 = r5.mIsAddressBookEnalbed
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r2 = r2.isPhoneNumberRegistered()
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            r5.mIsAddressBookEnalbed = r2
            if (r0 == 0) goto L_0x019e
            java.lang.Boolean r2 = r5.mIsAddressBookEnalbed
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x019e
            java.lang.String r0 = "fte_chats_list_fte"
            java.lang.String r0 = com.zipow.videobox.util.PreferenceUtil.getPreferenceName(r0)
            boolean r0 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r0, r1)
            if (r0 == 0) goto L_0x019e
            java.lang.Boolean r0 = r5.mIsAddressBookEnalbed
            boolean r0 = r0.booleanValue()
            r5.onAddressBookEnabled(r0)
        L_0x019e:
            com.zipow.videobox.ptapp.PTUI r0 = com.zipow.videobox.ptapp.PTUI.getInstance()
            r0.checkStartKubiService()
            com.zipow.videobox.view.IMView r0 = r5.mIMView
            r0.onResume()
            r5.checkNewVersionReadyBeforeShowIM()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.IMActivity.onResume():void");
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        if (bundle != null) {
            bundle.putBoolean("needToReturnToMeetingOnResume", PTApp.getInstance().isNeedToReturnToMeetingOnResume());
        }
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        if (bundle != null) {
            PTApp.getInstance().setNeedToReturnToMeetingOnResume(bundle.getBoolean("needToReturnToMeetingOnResume"));
        }
        super.onRestoreInstanceState(bundle);
    }

    private void checkNewVersionReadyBeforeShowIM() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.getBoolean(ARG_NEW_VERSIONS, false)) {
                handleOnNewVersionReady();
                extras.putBoolean(ARG_NEW_VERSIONS, false);
                intent.putExtras(extras);
            }
        }
    }

    private void checkAutoLogin() {
        if (!PTApp.getInstance().hasZoomMessenger()) {
            checkAutoLogin(true);
        }
    }

    public void onSipCallEvent() {
        if (isActive()) {
            this.mIMView.onSipCallEvent();
        }
    }

    public void onUnreadBadgeSettingUpdated() {
        if (isActive()) {
            this.mIMView.onUnreadBadgeSettingUpdated();
        }
    }

    public void onSipVoiceMailsCountChanged() {
        if (isActive()) {
            this.mIMView.onSipVoiceMailsCountChanged();
        }
    }

    public void onSipCallHistoryCountChanged() {
        if (isActive()) {
            this.mIMView.onSipCallHistoryCountChanged();
        }
    }

    public void onPBXMessageUnreadCountChanged() {
        if (isActive()) {
            this.mIMView.onPBXMessageUnreadCountChanged();
        }
    }

    public void onIndicateZoomMessengerBuddyListUpdated() {
        if (isActive()) {
            this.mIMView.onIndicateZoomMessengerBuddyListUpdated();
        }
    }

    public boolean onZoomMessengerNotifySubscribeRequest() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (!(zoomMessenger == null || zoomMessenger.imChatGetOption() == 2 || !isActive())) {
            this.mIMView.onZoomMessengerNotifySubscribeRequest();
        }
        return false;
    }

    private void checkAutoLogin(boolean z) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (!PTApp.getInstance().isWebSignedOn() && !PTApp.getInstance().isAuthenticating() && (iMHelper == null || (!iMHelper.isIMSignedOn() && !iMHelper.isIMLoggingIn()))) {
            autoLogin();
        } else if (z && iMHelper != null && PTApp.getInstance().isCurrentLoginTypeSupportIM() && !iMHelper.isIMSignedOn() && !iMHelper.isIMLoggingIn()) {
            PTUI.getInstance().reconnectIM();
        }
        if (isActive()) {
            this.mIMView.updateUI();
        }
    }

    public void onPause() {
        super.onPause();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    private void switchAccount() {
        PTApp.getInstance().logout(1);
        showLoginUI(false);
    }

    public void loginToUse() {
        switchAccount();
    }

    public void loginToUse(int i) {
        PTApp.getInstance().logout(1, i != 1);
        LoginUtil.showLoginUI(this, false, i);
    }

    public void autoLoginByDeviceAfterLogout() {
        checkAutoLogin();
    }

    private void autoLogin() {
        boolean z;
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            if (!PTApp.getInstance().isWebSignedOn()) {
                finishAndShowWelcome();
            }
        } else if (!PTApp.getInstance().isTokenExpired()) {
            int pTLoginType = PTApp.getInstance().getPTLoginType();
            if (pTLoginType == 2) {
                if (!PTApp.getInstance().autoSignin()) {
                    showLoginUIForTokenExpired(true);
                }
            } else if (pTLoginType == 0) {
                AuthToken authToken = this.mFacebookAuthToken;
                if (authToken == null || !authToken.isSessionValid()) {
                    showLoginUIForTokenExpired(true);
                } else {
                    if (this.mFacebookAuthToken.shouldExtendAccessToken()) {
                        try {
                            z = !this.mFacebookAuthToken.extendAccessToken(getApplicationContext(), new ExtendTokenServiceListener());
                        } catch (Exception unused) {
                            z = false;
                        }
                    } else {
                        z = true;
                    }
                    if (z && !PTApp.getInstance().autoSignin()) {
                        showLoginUIForTokenExpired(true);
                    }
                }
            } else if (ZmLoginHelper.isUseZoomLogin(pTLoginType)) {
                ZoomAccount savedZoomAccount = PTApp.getInstance().getSavedZoomAccount();
                if (savedZoomAccount == null || StringUtil.isEmptyOrNull(savedZoomAccount.getUserName())) {
                    finishAndShowWelcome();
                } else if (!PTApp.getInstance().autoSignin()) {
                    showLoginUIForTokenExpired(true);
                }
            } else if (pTLoginType == 101) {
                if (!PTApp.getInstance().autoSignin()) {
                    showLoginUIForTokenExpired(true);
                }
            } else if (pTLoginType == 98) {
                String[] strArr = new String[1];
                PTApp.getInstance().getSavedRingCentralPhoneNumberAndExt(strArr, new String[1]);
                if (strArr[0] == null || StringUtil.isEmptyOrNull(strArr[0])) {
                    finishAndShowWelcome();
                } else if (!PTApp.getInstance().autoSignin()) {
                    showLoginUIForTokenExpired(true);
                }
            } else {
                finishAndShowWelcome();
            }
        }
    }

    private void finishAndShowWelcome() {
        WelcomeActivity.show(this, false, false);
        finish();
    }

    private void showLoginUIForTokenExpired(final boolean z) {
        PTApp.getInstance().setTokenExpired(true);
        getNonNullEventTaskManagerOrThrowException().push("showLoginUIForTokenExpired", new EventAction() {
            public void run(IUIElement iUIElement) {
                LoginUtil.showLoginUI(IMActivity.this, z, 100);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onFBTokenExtended(String str) {
        AuthToken authToken = this.mFacebookAuthToken;
        if (authToken != null) {
            FBSessionStore.save(FBSessionStore.FACEBOOK_KEY, authToken, this);
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            long currentTimeMillis = (this.mFacebookAuthToken.expires - System.currentTimeMillis()) / 1000;
            PTApp instance = PTApp.getInstance();
            instance.loginXmppServer(str);
            instance.loginWithFacebook(str, currentTimeMillis);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.onConfigurationChanged(configuration);
        }
    }

    private void finishSubActivities() {
        finishActivity(100);
        finishActivity(101);
        finishActivity(102);
        finishActivity(103);
        finishActivity(104);
        finishActivity(105);
        finishActivity(106);
    }

    public void onBackPressed() {
        if (!closeTips() && !this.mIMView.onBackPressed()) {
            try {
                moveTaskToBack(true);
            } catch (Exception unused) {
            }
        }
    }

    public boolean onSearchRequested() {
        return this.mIMView.onSearchRequested();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 103 && i2 == -1 && intent != null) {
            onScheduleSuccess((ScheduledMeetingItem) intent.getSerializableExtra("meetingItem"));
        }
    }

    public void onScheduleSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        this.mIMView.onScheduleSuccess(scheduledMeetingItem);
    }

    public void onAddressBookEnabled(boolean z) {
        this.mIMView.onAddressBookEnabled(z);
    }

    /* access modifiers changed from: private */
    public boolean closeTips() {
        return this.mIMView.closeTips();
    }

    public void onClickJoinConf() {
        JoinConfActivity.showJoinByNumber(this, null, null);
    }

    public void showChatUI(IMBuddyItem iMBuddyItem) {
        this.mIMView.showChatUI(iMBuddyItem);
    }

    public void showLoginUI(boolean z) {
        LoginUtil.showLoginUI(this, z, 100);
    }

    public void showBuddyInviteUI(@NonNull IMBuddyItem iMBuddyItem) {
        if (UIMgr.isLargeMode(this)) {
            BuddyInviteFragment.showDialog(getSupportFragmentManager(), iMBuddyItem.userId);
        } else {
            BuddyInviteActivity.show(this, 102, iMBuddyItem.userId);
        }
    }

    public void onPTAppEvent(int i, long j) {
        switch (i) {
            case 0:
                sinkWebLogin(j);
                return;
            case 1:
                sinkWebLogout(j);
                return;
            case 8:
                sinkIMLogin(j);
                return;
            case 9:
                sinkMyInfoReady(j);
                return;
            case 12:
                sinkMyPictureReady(j);
                return;
            case 14:
                sinkIMLogout(j);
                return;
            case 21:
                sinkOnIMReconnecting();
                return;
            case 22:
                sinkCallStatusChanged(j);
                return;
            case 23:
                sinkCallPlistChanged();
                return;
            case 25:
                onNewVersionReady(j);
                return;
            case 37:
                sinkGoogleWebAccessFail();
                return;
            default:
                return;
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
        if (isActive() && PTApp.getInstance().isWebSignedOn()) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                this.mIMView.onIMLocalStatusChanged(iMHelper.getIMLocalStatus());
            }
        }
        if (z) {
            checkAutoLogin(false);
        }
    }

    private void sinkMyInfoReady(long j) {
        if (isActive()) {
            this.mIMView.onMyInfoReady();
        }
    }

    private void sinkMyPictureReady(long j) {
        if (isActive()) {
            this.mIMView.onMyPictureReady();
        }
    }

    private void sinkWebLogout(long j) {
        if (isActive()) {
            handleOnWebLogout(j);
        }
    }

    private void sinkIMLogin(long j) {
        if (isActive()) {
            this.mIMView.onIMLogin(j);
        }
    }

    private void sinkWebLogin(long j) {
        if (j == 1006 || j == 1003 || j == 1037 || j == 1038) {
            PTApp.getInstance().setRencentJid("");
            finishAllActivities();
            LoginUtil.showLoginUI(VideoBoxApplication.getGlobalContext(), false, 100);
        } else if (isActive()) {
            this.mIMView.onWebLogin(j);
            checkPendingCall();
        }
    }

    private void sinkCallStatusChanged(long j) {
        if (isActive()) {
            this.mIMView.onCallStatusChanged(j);
        }
    }

    private void sinkCallPlistChanged() {
        if (isActive()) {
            this.mIMView.onCallPlistChanged();
        }
    }

    private void sinkGoogleWebAccessFail() {
        if (isActive()) {
            this.mIMView.onGoogleWebAccessFail();
        }
    }

    public void onIMLocalStatusChanged(int i) {
        if (isActive()) {
            this.mIMView.onIMLocalStatusChanged(i);
        }
    }

    private void sinkOnIMReconnecting() {
        if (isActive()) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                this.mIMView.onIMLocalStatusChanged(iMHelper.getIMLocalStatus());
            }
        }
    }

    public void onIMReceived(@NonNull IMMessage iMMessage) {
        if (isActive()) {
            this.mIMView.onIMReceived(iMMessage);
        }
        if (iMMessage.getMessageType() == 0) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                String jIDMyself = iMHelper.getJIDMyself();
                if (jIDMyself == null || !jIDMyself.equals(iMMessage.getFromScreenName())) {
                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                    if (!(frontActivity instanceof IMChatActivity) || !frontActivity.isActive()) {
                        NotificationMgr.showMessageNotificationMM(this, true);
                    }
                } else {
                    iMHelper.setIMMessageUnread(iMMessage, false);
                }
            }
        }
    }

    public void onIMBuddyPresence(BuddyItem buddyItem) {
        if (isActive()) {
            this.mIMView.onIMBuddyPresence(buddyItem);
        }
    }

    public void onIMBuddyPic(BuddyItem buddyItem) {
        if (isActive()) {
            this.mIMView.onIMBuddyPic(buddyItem);
        }
    }

    public void onIMBuddySort() {
        if (isActive()) {
            this.mIMView.onIMBuddySort();
        }
    }

    public void onSubscriptionRequest() {
        if (isActive()) {
            this.mIMView.onSubscriptionRequest();
        }
    }

    public void onSubscriptionUpdate() {
        if (isActive()) {
            this.mIMView.onSubscriptionUpdate();
        }
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        if (i != 3) {
            return;
        }
        if (j == 1104) {
            getNonNullEventTaskManagerOrThrowException().push("onPhoneBindByOther", new EventAction("onPhoneBindByOther") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((IMActivity) iUIElement).onPhoneBindByOther();
                }
            });
        } else if (j == 1102) {
            getNonNullEventTaskManagerOrThrowException().push("onPhoneNotExist", new EventAction("onPhoneNotExist") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((IMActivity) iUIElement).onPhoneNotExist();
                }
            });
        }
    }

    private void finishAllActivities() {
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        LoginActivity.show(VideoBoxApplication.getGlobalContext(), false, true);
    }

    /* access modifiers changed from: private */
    public void onPhoneBindByOther() {
        this.mIMView.reloadView();
        showPhoneBindByOtherDialog();
    }

    /* access modifiers changed from: private */
    public void onPhoneNotExist() {
        this.mIMView.reloadView();
    }

    private void showPhoneBindByOtherDialog() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_phone_bind_by_other).show(getSupportFragmentManager(), "BindByOtherMessageDialog");
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerMessageReceived() {
        if (this.mNewMsgRunnable == null) {
            this.mNewMsgRunnable = new Runnable() {
                public void run() {
                    if (!IMActivity.this.isDestroyed() && IMActivity.this.isActive()) {
                        IMActivity.this.mIMView.onZoomMessengerMessageReceived();
                    }
                    IMActivity.this.mNewMsgRunnable = null;
                }
            };
            this.mIMView.postDelayed(this.mNewMsgRunnable, 1000);
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerNotifySubscribeRequestUpdated(String str) {
        if (isActive()) {
            this.mIMView.onZoomMessengerChatSessionListUpdate();
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerNotifyChatSessionUnreadUpdate(String str) {
        if (isActive()) {
            this.mIMView.onZoomMessengerNotifyChatSessionUnreadUpdate(str);
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerNotifyChatSessionUnreadUpdate() {
        if (isActive()) {
            this.mIMView.onZoomMessengerNotifyChatSessionUnreadUpdate();
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerChatSessionListUpdate() {
        if (isActive()) {
            this.mIMView.onZoomMessengerChatSessionListUpdate();
        }
    }

    /* access modifiers changed from: private */
    public void onZoomMessengerIndicateRevokeMessageResult(String str, String str2, String str3, String str4, boolean z) {
        if (isActive()) {
            this.mIMView.onZoomMessengerIndicateRevokeMessageResult(str, str2, str3, str4, z);
        }
    }

    public void onMMSessionDeleted(@NonNull String str) {
        int i;
        int i2;
        this.mIMView.onMMSessionDeleted(str);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int i3 = 0;
        if (zoomMessenger != null) {
            i = zoomMessenger.getTotalUnreadMessageCount();
            i2 = zoomMessenger.getUnreadRequestCount();
        } else {
            i2 = 0;
            i = 0;
        }
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            i3 = iMHelper.getUnreadMsgCount();
        }
        if (i + i3 + i2 == 0) {
            NotificationMgr.removeMessageNotificationMM(this);
        }
        NotificationMgr.removeMessageNotificationMM(this, str);
    }

    private void onNewVersionReady(long j) {
        SettingFragment.saveNewVersionReadyTime();
        if (isActive()) {
            handleOnNewVersionReady();
        }
    }

    private void handleOnNewVersionReady() {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.onNewVersionReady();
        }
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        String latestVersionReleaseNote = PTApp.getInstance().getLatestVersionReleaseNote();
        NewVersionDialog newVersionDialog = (NewVersionDialog) getSupportFragmentManager().findFragmentByTag(NewVersionDialog.class.getName());
        if (newVersionDialog != null) {
            newVersionDialog.setArguments(latestVersionString, latestVersionReleaseNote);
            return;
        }
        NewVersionDialog lastInstance = NewVersionDialog.getLastInstance();
        if (lastInstance != null) {
            lastInstance.setArguments(latestVersionString, latestVersionReleaseNote);
            return;
        }
        if (System.currentTimeMillis() - PTApp.getInstance().getLastUpdateNotesDisplayTime() >= 43200000) {
            PTApp.getInstance().setLastUpdateNotesDisplayTime(System.currentTimeMillis());
            NewVersionDialog.newInstance(latestVersionString, latestVersionReleaseNote, new RequestPermissionListener() {
                public void requestPermission() {
                    IMActivity.this.zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 107);
                }
            }).show(getSupportFragmentManager(), NewVersionDialog.class.getName());
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 107) {
            if (checkStoragePermission()) {
                UpgradeUtil.upgrade(this);
            }
        } else if (i == 108) {
            checkPendingCall();
        } else {
            super.onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    private boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void updateSettingsNoteBubble() {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.updateSettingsNoteBubble();
        }
    }

    public boolean isPhoneTabSelected() {
        return this.mIMView.isPhoneTabSelected();
    }

    private void checkPendingCall() {
        if (PTApp.getInstance().isWebSignedOn()) {
            final String pendingNumber = CmmSIPCallManager.getInstance().getPendingNumber();
            if (!StringUtil.isEmptyOrNull(pendingNumber)) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    CmmSIPCallManager.getInstance().setPendingNumber(null);
                    showSIPCallDialpad("");
                    if (NetworkUtil.hasDataNetwork(this)) {
                        this.mIMView.postDelayed(new Runnable() {
                            public void run() {
                                if (CmmSIPCallManager.getInstance().isSipCallEnabled()) {
                                    CmmSIPCallManager.getInstance().callPeer(pendingNumber);
                                } else {
                                    DialogUtils.showAlertDialog((ZMActivity) IMActivity.this, C4558R.string.zm_sip_calling_not_support_114834, C4558R.string.zm_btn_ok);
                                }
                            }
                        }, 200);
                    }
                } else {
                    zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 108);
                }
            }
        }
    }

    public boolean onCreatePanelMenu(int i, @NonNull Menu menu) {
        return super.onCreatePanelMenu(i, menu);
    }

    public void enterSelectMode() {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.enterSelectMode();
        }
    }

    public void exitSelectMode() {
        IMView iMView = this.mIMView;
        if (iMView != null) {
            iMView.exitSelectMode();
        }
    }
}
