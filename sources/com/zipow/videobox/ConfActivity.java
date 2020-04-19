package com.zipow.videobox;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.accessibility.AccessibilityManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.INewIncomingCallEventListener;
import com.zipow.videobox.confapp.ConfUI.IPtLoginResultEventListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.confapp.IConfToolbar;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.component.ZMConfEnumViewMode;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.confapp.meeting.confhelper.BOComponent;
import com.zipow.videobox.confapp.meeting.confhelper.IAssembleConfComponent;
import com.zipow.videobox.confapp.meeting.confhelper.KubiComponent;
import com.zipow.videobox.confapp.meeting.confhelper.PollComponent;
import com.zipow.videobox.confapp.p009bo.BOLeaveFragment;
import com.zipow.videobox.confapp.p009bo.BOMessageTip;
import com.zipow.videobox.confapp.p009bo.BOUtil;
import com.zipow.videobox.confapp.param.ZMConfIntentParam;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.dialog.AlertFreeMeetingDialog;
import com.zipow.videobox.dialog.CmrFullStorageDialog;
import com.zipow.videobox.dialog.DialogActionCallBack;
import com.zipow.videobox.dialog.LeaveAlertDialog;
import com.zipow.videobox.dialog.NamePasswordDialog;
import com.zipow.videobox.dialog.PayerReminderDialog;
import com.zipow.videobox.dialog.PermissionUnableAccessDialog;
import com.zipow.videobox.dialog.SelectPersonalLinkToJoinDialog;
import com.zipow.videobox.dialog.SwitchToJoinFromRoomDialog;
import com.zipow.videobox.dialog.SwitchToJoinMeetingDialog;
import com.zipow.videobox.dialog.UpgradeFreeMeetingDialog;
import com.zipow.videobox.dialog.UpgradeFreeMeetingErrorDialog;
import com.zipow.videobox.dialog.ZMCDPRConfirmDialog;
import com.zipow.videobox.dialog.ZMGDPRConfirmDialog;
import com.zipow.videobox.dialog.ZMNetErrorDialog;
import com.zipow.videobox.dialog.ZMPreviewVideoDialog;
import com.zipow.videobox.dialog.ZMRealNameAuthDialog;
import com.zipow.videobox.dialog.ZMRealNameConfirmDialog;
import com.zipow.videobox.dialog.conf.CloseOtherMeetingDialog;
import com.zipow.videobox.dialog.conf.NewIncomingCallDialog;
import com.zipow.videobox.dialog.conf.PermissionPromptDialog;
import com.zipow.videobox.dialog.conf.SwitchCallDialog;
import com.zipow.videobox.dialog.conf.SwitchStartMeetingDialog;
import com.zipow.videobox.dialog.conf.WebinarNeedRegisterDialog;
import com.zipow.videobox.fragment.InviteViaDialogFragment;
import com.zipow.videobox.fragment.MeetingRunningInfoFragment;
import com.zipow.videobox.fragment.PrivacyDisclaimerFragment;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.fragment.WebinarRegisterDialog;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.AudioTip;
import com.zipow.videobox.view.ChatTip;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.InterpretationTip;
import com.zipow.videobox.view.MessageTip;
import com.zipow.videobox.view.MoreTip;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.RaiseHandTip;
import com.zipow.videobox.view.ShareTip;
import com.zipow.videobox.view.ToastTip;
import com.zipow.videobox.view.VideoTip;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import java.util.ArrayList;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class ConfActivity extends ZMActivity implements DialogActionCallBack, IConfToolbar, IAssembleConfComponent, INewIncomingCallEventListener {
    private static final int DIALOG_REQUEST_GDPR_CANNOT_JOIN_MEETING = 1;
    private static final int DIALOG_REQUEST_GDPR_JOIN_MEETING = 0;
    private static final String TAG = "ConfActivity";
    private static final long TIMEOUT_NOTIFY_WIFI_SIGNAL = 10000;
    @Nullable
    private static AudioManager g_audioManager;
    /* access modifiers changed from: private */
    @NonNull
    public static Handler g_handler = new Handler();
    /* access modifiers changed from: private */
    public static int g_lastNotifiedVolume = -1;
    /* access modifiers changed from: private */
    public static int g_lastVolume = -1;
    @Nullable
    private static BroadcastReceiver g_networkStateReceiver;
    private static int g_notifyUpdateWaitCountDown = 20;
    private static Runnable g_runnableNotifyVolumeChanged;
    @Nullable
    private static Runnable g_taskNotifyWifiSignal = null;
    @Nullable
    private static BroadcastReceiver g_volumeChangeReceiver;
    protected final ConfParams mConfParams = new ConfParams();
    @NonNull
    private IConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public boolean onConfStatusChanged2(int i, long j) {
            return ConfActivity.this._onConfStatusChanged2(i, j);
        }

        public boolean onConfStatusChanged(int i) {
            return ConfActivity.this._onConfStatusChanged(i);
        }

        public void onPTAskToLeave(int i) {
            ConfActivity.this._onPTAskToLeave(i);
        }

        public void onJoinConfConfirmMeetingInfo(boolean z, boolean z2, boolean z3) {
            ConfActivity.this._onJoinConfConfirmMeetingInfo(z, z2, z3);
        }

        public void onJoinConfConfirmPasswordValidateResult(boolean z, boolean z2) {
            ConfActivity.this._onJoinConfConfirmPasswordValidateResult(z, z2);
        }

        public void onJoinConfVerifyMeetingInfo(int i) {
            ConfActivity.this._onJoinConfVerifyMeetingInfo(i);
        }

        public void onJoinConfConfirmMeetingStatus(boolean z, boolean z2) {
            ConfActivity.this._onJoinConfConfirmMeetingStatus(z, z2);
        }

        public boolean onJoinConf_ConfirmMultiVanityURLs() {
            return ConfActivity.this._onJoinConf_ConfirmMultiVanityURLs();
        }

        public void onCallTimeOut() {
            ConfActivity.this._onCallTimeOut();
        }

        public void onDeviceStatusChanged(int i, int i2) {
            ConfActivity.this._onDeviceStatusChanged(i, i2);
        }

        public void onWebinarNeedRegister(boolean z) {
            ConfActivity.this._onWebinarNeedRegister(z);
        }

        public void onUpgradeThisFreeMeeting(int i) {
            ConfActivity.this._onUpgradeThisFreeMeeting(i);
        }

        public void onCheckCMRPrivilege(int i, boolean z) {
            ConfActivity.this._onCheckCMRPrivilege(i, z);
        }
    };
    private ZMAlertDialog mGuestJoinLoginTip;
    @NonNull
    private Runnable mHandleRequestPermissionsRunnable = new Runnable() {
        public void run() {
            if (!ConfActivity.this.mPendingRequestPermissions.isEmpty()) {
                if (PreferenceUtil.readBooleanValue(PreferenceUtil.PERMISSION_PROMT_FOR_MEETING, false) || VERSION.SDK_INT < 23) {
                    ConfActivity.this.doRequestPermission();
                } else {
                    PermissionPromptDialog.showDialog(ConfActivity.this.getSupportFragmentManager());
                    PreferenceUtil.saveBooleanValue(PreferenceUtil.PERMISSION_PROMT_FOR_MEETING, true);
                }
            }
        }
    };
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mInPendingRequestPermission = false;
    private OrientationEventListener mOrientationListener;
    @NonNull
    private ArrayList<Integer> mPendingRequestPermissionCodes = new ArrayList<>();
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<String> mPendingRequestPermissions = new ArrayList<>();
    @Nullable
    private ZMPreviewVideoDialog mPreviewVideoDialog;
    @NonNull
    private IPtLoginResultEventListener mPtLoginResultEventListener = new IPtLoginResultEventListener() {
        public void onPtLoginResultEvent(boolean z, String str, String str2) {
            if (z) {
                ConfActivity.this.switchCall(str, str2);
            } else {
                ConfActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onPtLoginResultEvent") {
                    public void run(@NonNull IUIElement iUIElement) {
                        ConfLocalHelper.leaveCallForErrorCode(ConfActivity.this, 23);
                    }
                });
            }
        }
    };
    private long mRequestPermissionTime = 0;
    @Nullable
    private RetainedFragment mRetainedFragment;
    @Nullable
    protected AbsVideoSceneMgr mVideoSceneMgr;
    private boolean mbNeedSaveUrlParams = false;

    public static class RetainedFragment extends ZMFragment {
        /* access modifiers changed from: private */
        public boolean mHasPopupNameDialog = false;
        private boolean mHasPopupStartingRecord = false;
        /* access modifiers changed from: private */
        public boolean mIsAbleToJoin = false;
        @Nullable
        private AbsVideoSceneMgr mVideoSeceneMgr = null;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveVideoSceneMgr(AbsVideoSceneMgr absVideoSceneMgr) {
            this.mVideoSeceneMgr = absVideoSceneMgr;
        }

        @Nullable
        public AbsVideoSceneMgr restoreVideoSceneMgr() {
            return this.mVideoSeceneMgr;
        }

        public void setHasPopupStartingRecord(boolean z) {
            this.mHasPopupStartingRecord = z;
        }

        public boolean hasPopupStartingRecord() {
            return this.mHasPopupStartingRecord;
        }

        public boolean ismIsAbleToJoin() {
            return this.mIsAbleToJoin;
        }
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoControlChanged(long j) {
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoLayoutChanged(long j) {
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoLayoutFlagChanged(long j) {
    }

    public void disableToolbarAutoHide() {
    }

    public void enterHostKeyToClaimHost() {
    }

    public int getToolbarHeight() {
        return 0;
    }

    public int getTopBarHeight() {
        return 0;
    }

    @Nullable
    public AbsVideoSceneMgr getVideoSceneMgr() {
        return null;
    }

    @Nullable
    public BOComponent getmBOComponent() {
        return null;
    }

    @Nullable
    public KubiComponent getmKubiComponent() {
        return null;
    }

    @Nullable
    public PollComponent getmPollComponent() {
        return null;
    }

    @Nullable
    public ZMTipLayer getmZMTipLayer() {
        return null;
    }

    public void hiddenMainVideoAudioStatus() {
    }

    public void hideToolbarDefaultDelayed() {
    }

    public void hideToolbarDelayed(long j) {
    }

    public boolean isBottombarShowing() {
        return false;
    }

    public boolean isToolbarShowing() {
        return false;
    }

    public void onClickBtnAudio() {
    }

    public void onDraggingVideoScene() {
    }

    public void onDropVideoScene(boolean z) {
    }

    public void onHostAskUnmute() {
    }

    public void onPListTipClosed() {
    }

    public void onVideoSceneChanged(AbsVideoScene absVideoScene, AbsVideoScene absVideoScene2) {
    }

    public void refreshMainVideoAudioStatus(int i, int i2, int i3, String str) {
    }

    public void refreshToolbar() {
    }

    public void refreshUnreadChatCount() {
    }

    public void showAttendeeList() {
    }

    public void showPList() {
    }

    public void showTipMicEchoDetected() {
    }

    public void showToolbar(boolean z, boolean z2) {
    }

    public void switchViewTo(ZMConfEnumViewMode zMConfEnumViewMode) {
    }

    public void updateSystemStatusBar() {
    }

    public void updateTitleBar() {
    }

    /* access modifiers changed from: private */
    public static void runOnConfProcessReady(@NonNull final Runnable runnable, final long j) {
        if (VideoBoxApplication.getInstance().isConfProcessReady()) {
            runnable.run();
        } else if (j > 0) {
            g_handler.postDelayed(new Runnable() {
                public void run() {
                    ConfActivity.runOnConfProcessReady(runnable, j - 20);
                }
            }, 20);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
    }

    public static void joinById(@Nullable final Context context, final long j, String str, String str2, String str3, boolean z, boolean z2) {
        long j2 = j;
        if (context != null && ((j2 > 0 || !StringUtil.isEmptyOrNull(str2)) && !StringUtil.isEmptyOrNull(str))) {
            VideoBoxApplication.getInstance().clearConfAppContext();
            VideoBoxApplication.getInstance().setConfUIPreloaded(true);
            PTApp instance = PTApp.getInstance();
            if (instance.joinFromIconTray(str, str2, j, str3, z, z2)) {
                ZMConfEventTracking.logJoinMeeting(instance.isWebSignedOn(), !z, !z2);
            }
            final String str4 = str;
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_JOIN_BY_ID);
                    intent.putExtra("confno", j);
                    intent.putExtra("screenName", str4);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002c  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean joinByUrl(@androidx.annotation.NonNull android.content.Context r8, java.lang.String r9) {
        /*
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            r1.setUrlAction(r9)
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r1 = r1.isWebSignedOn()
            r2 = 0
            if (r1 == 0) goto L_0x0025
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTUserProfile r1 = r1.getCurrentUserProfile()
            if (r1 == 0) goto L_0x0025
            java.lang.String r1 = r1.getUserName()
            goto L_0x0026
        L_0x0025:
            r1 = r2
        L_0x0026:
            com.zipow.videobox.ptapp.PTAppProtos$UrlActionData r3 = r0.parseURLActionData(r9)
            if (r3 == 0) goto L_0x0031
            java.lang.String r4 = r3.getConfno()
            goto L_0x0032
        L_0x0031:
            r4 = r2
        L_0x0032:
            if (r3 == 0) goto L_0x0038
            java.lang.String r2 = r3.getConfid()
        L_0x0038:
            boolean r3 = r0.hasActiveCall()
            r5 = 1
            if (r3 == 0) goto L_0x0081
            long r6 = r0.getActiveMeetingNo()
            java.lang.String r3 = java.lang.String.valueOf(r6)
            java.lang.String r0 = r0.getActiveCallId()
            if (r0 != 0) goto L_0x004f
            java.lang.String r0 = ""
        L_0x004f:
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x007d
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x005c
            goto L_0x007d
        L_0x005c:
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class r2 = com.zipow.videobox.util.ConfLocalHelper.getConfActivityImplClass(r8)
            r0.<init>(r8, r2)
            r2 = 131072(0x20000, float:1.83671E-40)
            r0.addFlags(r2)
            java.lang.String r2 = com.zipow.videobox.confapp.param.ZMConfIntentParam.ACTION_SWITCH_CALL
            r0.setAction(r2)
            java.lang.String r2 = "urlAction"
            r0.putExtra(r2, r9)
            java.lang.String r9 = "screenName"
            r0.putExtra(r9, r1)
            com.zipow.videobox.util.ActivityStartHelper.startActivityForeground(r8, r0)
            return r5
        L_0x007d:
            com.zipow.videobox.util.ConfLocalHelper.returnToConf(r8)
            return r5
        L_0x0081:
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r4)
            if (r0 == 0) goto L_0x0094
            com.zipow.videobox.mainboard.Mainboard r0 = com.zipow.videobox.mainboard.Mainboard.getMainboard()
            r0.notifyUrlAction(r9)
            boolean r8 = com.zipow.videobox.util.UIMgr.isLargeMode(r8)
            r8 = r8 ^ r5
            return r8
        L_0x0094:
            boolean r8 = joinByUrl(r8, r9, r1)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ConfActivity.joinByUrl(android.content.Context, java.lang.String):boolean");
    }

    public static boolean joinByUrl(@Nullable final Context context, final String str, final String str2) {
        if (context == null || StringUtil.isEmptyOrNull(str)) {
            return true;
        }
        VideoBoxApplication.getInstance().clearConfAppContext();
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        Mainboard.getMainboard().notifyUrlAction(str);
        runOnConfProcessReady(new Runnable() {
            public void run() {
                Context context = context;
                Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                Context context2 = context;
                if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                    intent.setFlags(268435456);
                }
                intent.setAction(ZMConfIntentParam.ACTION_JOIN_BY_URL);
                intent.putExtra("urlAction", str);
                intent.putExtra("screenName", str2);
                ActivityStartHelper.startActivityForeground(context, intent);
            }
        }, 2000);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean webStart(@androidx.annotation.NonNull final android.content.Context r8, final java.lang.String r9) {
        /*
            com.zipow.videobox.ptapp.PTApp r0 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            boolean r1 = r1.isWebSignedOn()
            r2 = 0
            if (r1 == 0) goto L_0x001e
            com.zipow.videobox.ptapp.PTApp r1 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.PTUserProfile r1 = r1.getCurrentUserProfile()
            if (r1 == 0) goto L_0x001e
            java.lang.String r1 = r1.getUserName()
            goto L_0x001f
        L_0x001e:
            r1 = r2
        L_0x001f:
            com.zipow.videobox.ptapp.PTAppProtos$UrlActionData r3 = r0.parseURLActionData(r9)
            if (r3 == 0) goto L_0x002a
            java.lang.String r4 = r3.getConfno()
            goto L_0x002b
        L_0x002a:
            r4 = r2
        L_0x002b:
            if (r3 == 0) goto L_0x0031
            java.lang.String r2 = r3.getConfid()
        L_0x0031:
            boolean r3 = r0.hasActiveCall()
            r5 = 1
            if (r3 == 0) goto L_0x007f
            long r6 = r0.getActiveMeetingNo()
            java.lang.String r3 = java.lang.String.valueOf(r6)
            java.lang.String r0 = r0.getActiveCallId()
            if (r0 != 0) goto L_0x0048
            java.lang.String r0 = ""
        L_0x0048:
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x007b
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x0055
            goto L_0x007b
        L_0x0055:
            android.content.Intent r0 = new android.content.Intent
            java.lang.Class r2 = com.zipow.videobox.util.ConfLocalHelper.getConfActivityImplClass(r8)
            r0.<init>(r8, r2)
            r2 = 131072(0x20000, float:1.83671E-40)
            r0.addFlags(r2)
            java.lang.String r2 = com.zipow.videobox.confapp.param.ZMConfIntentParam.ACTION_SWITCH_CALL
            r0.setAction(r2)
            java.lang.String r2 = "urlAction"
            r0.putExtra(r2, r9)
            java.lang.String r9 = "screenName"
            r0.putExtra(r9, r1)
            java.lang.String r9 = "isStart"
            r0.putExtra(r9, r5)
            com.zipow.videobox.util.ActivityStartHelper.startActivityForeground(r8, r0)
            goto L_0x0097
        L_0x007b:
            com.zipow.videobox.util.ConfLocalHelper.returnToConf(r8)
            return r5
        L_0x007f:
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r0.setConfUIPreloaded(r5)
            com.zipow.videobox.mainboard.Mainboard r0 = com.zipow.videobox.mainboard.Mainboard.getMainboard()
            r0.notifyUrlAction(r9)
            com.zipow.videobox.ConfActivity$6 r0 = new com.zipow.videobox.ConfActivity$6
            r0.<init>(r8, r9, r1)
            r8 = 2000(0x7d0, double:9.88E-321)
            runOnConfProcessReady(r0, r8)
        L_0x0097:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ConfActivity.webStart(android.content.Context, java.lang.String):boolean");
    }

    public static int startConference(Context context) {
        return startConference(context, 3);
    }

    public static int startConference(Context context, int i) {
        return startGroupCall(context, null, i);
    }

    public static int startGroupCall(@Nullable final Context context, String str, int i) {
        int i2;
        if (context == null) {
            return 1;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        if (TextUtils.isEmpty(str)) {
            i2 = PTApp.getInstance().startGroupVideoCall(null, null, context.getString(C4558R.string.zm_msg_invitation_message_template), 0, i);
        } else {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return 1;
            }
            i2 = zoomMessenger.startMeeting(str, null, 0, i);
        }
        if (i2 == 0) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return i2;
    }

    public static int inviteToVideoCall(@Nullable final Context context, String str, int i) {
        if (context == null) {
            return 1;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 1;
        }
        int startMeeting = zoomMessenger.startMeeting(null, str, 0, i);
        if (startMeeting == 0) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
            CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
            if (callHistoryMgr != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("zoomMeeting");
                sb.append(System.currentTimeMillis());
                callHistoryMgr.insertZoomMeetingHistory(sb.toString(), 2, str, i == 0, true);
            }
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return startMeeting;
    }

    public static int startMeetingCallFromZoomMessenger(@Nullable final Context context, String str, String str2, long j, int i) {
        if (context == null) {
            return 1;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return 1;
        }
        int startMeeting = zoomMessenger.startMeeting(str, str2, j, i);
        if (startMeeting == 0) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return startMeeting;
    }

    public static int callABContact(@Nullable final Context context, int i, @Nullable IMAddrBookItem iMAddrBookItem) {
        if (context == null || iMAddrBookItem == null) {
            return 1;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper == null) {
            return 1;
        }
        ArrayList arrayList = new ArrayList();
        int phoneNumberCount = iMAddrBookItem.getPhoneNumberCount();
        for (int i2 = 0; i2 < phoneNumberCount; i2++) {
            arrayList.add(iMAddrBookItem.getNormalizedPhoneNumber(i2));
        }
        int callABContact = aBContactsHelper.callABContact(i, arrayList, iMAddrBookItem.getScreenName(), context.getString(C4558R.string.zm_msg_invitation_message_template));
        if (callABContact == 0) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return callABContact;
    }

    public static void startConfUI(@NonNull Context context) {
        Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
        if (!(context instanceof ZMActivity) || !((ZMActivity) context).isActive()) {
            intent.setFlags(268435456);
        }
        intent.addFlags(131072);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void acceptCall(@Nullable final Context context, @Nullable InvitationItem invitationItem, boolean z) {
        if (context != null && invitationItem != null) {
            VideoBoxApplication.getInstance().setConfUIPreloaded(true);
            if (PTApp.getInstance().acceptVideoCall(invitationItem, context.getResources().getString(C4558R.string.zm_msg_accept_call), z) == 0) {
                runOnConfProcessReady(new Runnable() {
                    public void run() {
                        Context context = context;
                        boolean z = false;
                        if (!(context instanceof ZMActivity) || !((ZMActivity) context).isActive() || (context instanceof CallingActivity)) {
                            ZMActivity frontActivity = ZMActivity.getFrontActivity();
                            if (frontActivity == 0 || (frontActivity instanceof CallingActivity) || !frontActivity.isActive()) {
                                z = true;
                                context = context;
                            } else {
                                context = frontActivity;
                            }
                        }
                        Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                        if (z) {
                            intent.setFlags(268435456);
                        }
                        intent.setAction(ZMConfIntentParam.ACTION_ACCEPT_CALL);
                        ActivityStartHelper.startActivityForeground(context, intent);
                    }
                }, 2000);
            } else {
                VideoBoxApplication.getInstance().setConfUIPreloaded(false);
            }
        }
    }

    public static void handleOnPTAskToLeaveForUpdate() {
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                pTService.showNeedUpdate();
            } catch (RemoteException unused) {
            }
            ConfMgr.getInstance().leaveConference();
            return;
        }
        int i = g_notifyUpdateWaitCountDown;
        g_notifyUpdateWaitCountDown = i - 1;
        if (i > 0) {
            g_handler.postDelayed(new Runnable() {
                public void run() {
                    ConfActivity.handleOnPTAskToLeaveForUpdate();
                }
            }, 100);
        }
    }

    public static int launchCallForWebStart(@Nullable final Context context) {
        if (context == null) {
            return 1;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        int launchCallForWebStart = PTApp.getInstance().launchCallForWebStart();
        if (launchCallForWebStart == 0) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    Context context = context;
                    Intent intent = new Intent(context, ConfLocalHelper.getConfActivityImplClass(context));
                    Context context2 = context;
                    if (!(context2 instanceof ZMActivity) || !((ZMActivity) context2).isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(context, intent);
                }
            }, 2000);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return launchCallForWebStart;
    }

    public static void onNewIncomingCall(@NonNull ZMActivity zMActivity, InvitationItem invitationItem) {
        Intent intent = new Intent(zMActivity, ConfLocalHelper.getConfActivityImplClass(zMActivity));
        intent.addFlags(131072);
        intent.setAction(ZMConfIntentParam.ACTION_NEW_INCOMING_CALL);
        intent.putExtra("invitation", invitationItem);
        ActivityStartHelper.startActivityForeground(zMActivity, intent);
    }

    public static boolean startMeeting(@Nullable final ZMActivity zMActivity, long j, String str) {
        if (zMActivity == null) {
            return false;
        }
        if (PTApp.getInstance().hasActiveCall()) {
            long activeMeetingNo = PTApp.getInstance().getActiveMeetingNo();
            String activeCallId = PTApp.getInstance().getActiveCallId();
            if (activeMeetingNo == j || (activeCallId != null && activeCallId.equals(str))) {
                ConfLocalHelper.returnToConf(zMActivity);
                return false;
            }
            SwitchStartMeetingDialog.newSwitchStartMeetingDialog(j, str).show(zMActivity.getSupportFragmentManager(), SwitchStartMeetingDialog.class.getName());
            return false;
        }
        VideoBoxApplication.getInstance().setConfUIPreloaded(true);
        boolean startMeeting = PTApp.getInstance().startMeeting(j);
        if (startMeeting) {
            runOnConfProcessReady(new Runnable() {
                public void run() {
                    ZMActivity zMActivity = zMActivity;
                    Intent intent = new Intent(zMActivity, ConfLocalHelper.getConfActivityImplClass(zMActivity));
                    if (!zMActivity.isActive()) {
                        intent.setFlags(268435456);
                    }
                    intent.setAction(ZMConfIntentParam.ACTION_START_CONFERENCE);
                    ActivityStartHelper.startActivityForeground(zMActivity, intent);
                }
            }, 2000);
        } else {
            VideoBoxApplication.getInstance().setConfUIPreloaded(false);
        }
        return startMeeting;
    }

    public static void joinFromRoom(@Nullable ZMActivity zMActivity, long j, String str, String str2, String str3) {
        if (zMActivity != null) {
            if (PTApp.getInstance().hasActiveCall()) {
                long activeMeetingNo = PTApp.getInstance().getActiveMeetingNo();
                String activeCallId = PTApp.getInstance().getActiveCallId();
                if (activeMeetingNo == j || (activeCallId != null && activeCallId.equals(str))) {
                    ConfLocalHelper.returnToConf(zMActivity);
                } else {
                    SwitchToJoinFromRoomDialog.newSwitchToJoinFromRoomDialog(j, str, str2, str3).show(zMActivity.getSupportFragmentManager(), SwitchToJoinFromRoomDialog.class.getName());
                }
            } else {
                PTApp.getInstance().joinMeetingBySpecialMode(0, j, str2, str3);
            }
        }
    }

    public static void checkExistingCallAndJoinMeeting(@Nullable ZMActivity zMActivity, long j, String str, String str2, String str3) {
        if (zMActivity != null) {
            if (PTApp.getInstance().hasActiveCall()) {
                long activeMeetingNo = PTApp.getInstance().getActiveMeetingNo();
                String activeCallId = PTApp.getInstance().getActiveCallId();
                if (activeMeetingNo == j || (activeCallId != null && activeCallId.equals(str))) {
                    ConfLocalHelper.returnToConf(zMActivity);
                } else {
                    SwitchToJoinMeetingDialog.newSwitchToJoinMeetingDialog(j, str2, str3).show(zMActivity.getSupportFragmentManager(), SwitchToJoinMeetingDialog.class.getName());
                }
            } else {
                ZmPtUtils.joinMeeting(zMActivity, j, str2, str3);
            }
        }
    }

    public boolean isImmersedModeEnabled() {
        return UIUtil.isImmersedModeSupported();
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        disableFinishActivityByGesture(true);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish(true);
            VideoBoxApplication instance = VideoBoxApplication.getInstance();
            if (instance != null) {
                instance.stopConfService();
            } else {
                Process.killProcess(Process.myPid());
            }
            return;
        }
        initRetainedFragment();
        ConfUI.getInstance().addListener(this.mConfUIListener);
        ConfUI.getInstance().addWaitPtLoginResultListener(this.mPtLoginResultEventListener);
        ConfUI.getInstance().addINewIncomingCallEventListener(this);
        startNotifyWifiSignal();
        startListenNetworkState(this);
        startListenVolumeChange(this);
        if (bundle == null) {
            doIntent(getIntent());
        }
        if (ConfMgr.getInstance().isConfConnected()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                this.mConfParams.parseFromParamsList(confContext.getAppContextParams());
                if (this.mConfParams.isMbNoDrivingMode()) {
                    UIMgr.setDriverModeEnabled(false);
                }
                ConfUI.getInstance().setIsMeetingEndMessageDisabled(this.mConfParams.isMbNoMeetingEndMsg());
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (VideoBoxApplication.getInstance() != null) {
            ConfUI.getInstance().removeListener(this.mConfUIListener);
            ConfUI.getInstance().removeWaitPtLoginResultListener(this.mPtLoginResultEventListener);
            ConfUI.getInstance().removeINewIncomingCallEventListener(this);
            if (ConfUI.getInstance().isLeavingConference()) {
                stopNotifyWifiSignal();
                stopListenNetworkState(this);
                stopListenVolumeChange(this);
            }
        }
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        if (this.mRetainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
        }
    }

    @Nullable
    public RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }

    public boolean isCallingOut() {
        return ConfMgr.getInstance().isCallingOut();
    }

    @NonNull
    public ConfParams getConfParams() {
        return this.mConfParams;
    }

    public boolean isShowJoinLeaveTip() {
        BooleanQueryResult queryBooleanPolicy = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip);
        if (!queryBooleanPolicy.isSuccess()) {
            return false;
        }
        return queryBooleanPolicy.getResult();
    }

    public boolean isArrowAcceleratorDisabled() {
        return ResourcesUtil.getBoolean((Context) this, C4558R.bool.zm_config_no_arrow_accelerator, false);
    }

    private void doIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ZMConfIntentParam.ACTION_JOIN_BY_ID.equals(action)) {
                joinById(intent.getLongExtra("confno", 0), intent.getStringExtra("screenName"));
            } else if (ZMConfIntentParam.ACTION_JOIN_BY_URL.equals(action)) {
                joinByUrl(intent.getStringExtra("urlAction"), intent.getStringExtra("screenName"));
            } else if (ZMConfIntentParam.ACTION_SWITCH_CALL.equals(action)) {
                alertSwitchCall(intent.getStringExtra("urlAction"), intent.getStringExtra("screenName"), intent.getBooleanExtra("isStart", false));
            } else if (ZMConfIntentParam.ACTION_NEW_INCOMING_CALL.equals(action)) {
                InvitationItem invitationItem = (InvitationItem) intent.getSerializableExtra("invitation");
                if (invitationItem != null) {
                    alertNewIncomingCall(invitationItem);
                }
            } else if (ZMConfIntentParam.ACTION_PT_ASK_TO_LEAVE.equals(action)) {
                handleOnPTAskToLeave(intent.getIntExtra(ZMConfIntentParam.ARG_LEAVE_REASON, 0));
            }
        }
    }

    private void joinById(long j, String str) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(ConfService.ARG_COMMAND_TYPE, 1);
        bundle.putLong("confno", j);
        bundle.putString("screenName", str);
        instance.startConfService(bundle);
    }

    private void joinByUrl(String str, String str2) {
        Uri parse = Uri.parse(str);
        if (parse != null) {
            this.mConfParams.parseFromUri(parse);
            if (this.mConfParams.isMbNoDrivingMode()) {
                UIMgr.setDriverModeEnabled(false);
            }
            ConfUI.getInstance().setIsMeetingEndMessageDisabled(this.mConfParams.isMbNoMeetingEndMsg());
            if (ConfMgr.getInstance().isConfConnected()) {
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null) {
                    ParamsList appContextParams = confContext.getAppContextParams();
                    this.mConfParams.saveParamList(appContextParams);
                    confContext.setAppContextParams(appContextParams);
                    this.mbNeedSaveUrlParams = false;
                    return;
                }
                return;
            }
            this.mbNeedSaveUrlParams = true;
        }
    }

    private void alertSwitchCall(String str, String str2, boolean z) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final String str3 = str2;
        final String str4 = str;
        final boolean z2 = z;
        C190215 r1 = new EventAction("alertSwitchCall") {
            public void run(@NonNull IUIElement iUIElement) {
                SwitchCallDialog switchCallDialog = new SwitchCallDialog();
                Bundle bundle = new Bundle();
                bundle.putString("screenName", str3);
                bundle.putString("urlAction", str4);
                bundle.putBoolean("isStart", z2);
                switchCallDialog.setArguments(bundle);
                switchCallDialog.show(((ConfActivity) iUIElement).getSupportFragmentManager(), SwitchCallDialog.class.getName());
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r1);
    }

    public void switchCall(String str, String str2) {
        ConfLocalHelper.leaveCall(this);
        JoinByURLActivity.switchCallTo(getApplicationContext(), str, str2);
    }

    private void alertNewIncomingCall(final InvitationItem invitationItem) {
        long meetingNumber = invitationItem.getMeetingNumber();
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null || confContext.getConfNumber() != meetingNumber) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("alertNewIncomingCall") {
                public void run(@Nullable IUIElement iUIElement) {
                    ConfActivity confActivity = (ConfActivity) iUIElement;
                    if (iUIElement != null) {
                        NewIncomingCallDialog.showDialog(confActivity, invitationItem);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean _onConfStatusChanged(int i) {
        if (i == 12) {
            sinkConfReady((long) i);
        }
        return true;
    }

    private void sinkConfReady(final long j) {
        getEventTaskManager().push(new EventAction("onConfReady") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfReady(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfReady(long j) {
        if (!isCallingOut()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if ((confContext == null || !confContext.inSilentMode()) && confContext != null) {
                ParamsList appContextParams = confContext.getAppContextParams();
                if (this.mbNeedSaveUrlParams) {
                    this.mbNeedSaveUrlParams = false;
                    this.mConfParams.saveParamList(appContextParams);
                    confContext.setAppContextParams(appContextParams);
                } else {
                    this.mConfParams.parseFromParamsList(appContextParams);
                }
                ConfUI.getInstance().setIsMeetingEndMessageDisabled(this.mConfParams.isMbNoMeetingEndMsg());
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean _onConfStatusChanged2(int i, long j) {
        if (i != 105) {
            switch (i) {
                case 1:
                    sinkConfLeaveComplete(j);
                    break;
                case 2:
                    sinkConfFail(j);
                    break;
                default:
                    switch (i) {
                        case 68:
                            sinkConfNoHost(j);
                            break;
                        case 69:
                            sinkConfCloseOtherMeeting();
                            break;
                        case 70:
                            if (j != 7) {
                                if (j == 8) {
                                    ZMConfComponentMgr.getInstance().sinkConfKmsKeyNotReady();
                                    break;
                                }
                            } else {
                                ConfMgr.getInstance().checkCMRPrivilege();
                                break;
                            }
                            break;
                        default:
                            switch (i) {
                                case 73:
                                    sinkConfPlayerReminder(j);
                                    break;
                                case 74:
                                    sinkConfFirstTimeFreeGift(j);
                                    break;
                                case 75:
                                    sinkConfThirdTimeFreeGift(j);
                                    break;
                                case 76:
                                    sinkConfNeedAdminPayRemind(j);
                                    break;
                                case 77:
                                    sinkConfMeetingUpgraded();
                                    break;
                                default:
                                    switch (i) {
                                        case 131:
                                            sinkAttendeeVideoLayoutChanged(j);
                                            break;
                                        case 132:
                                            sinkAttendeeVideoLayoutFlagChanged(j);
                                            break;
                                        case 133:
                                            sinkAttendeeVideoControlChanged(j);
                                            break;
                                    }
                            }
                    }
            }
        } else {
            sinkCallOutStatusChanged(j);
        }
        return true;
    }

    private void sinkAttendeeVideoControlChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkAttendeeVideoControlChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).attendeeVideoControlChanged(j);
            }
        });
    }

    private void sinkAttendeeVideoLayoutChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkAttendeeVideoLayoutChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).attendeeVideoLayoutChanged(j);
            }
        });
    }

    private void sinkAttendeeVideoLayoutFlagChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkAttendeeVideoLayoutFlagChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).attendeeVideoLayoutFlagChanged(j);
            }
        });
    }

    public boolean isInDriveMode() {
        AbsVideoSceneMgr absVideoSceneMgr = this.mVideoSceneMgr;
        return absVideoSceneMgr != null && absVideoSceneMgr.isInDriveModeScence();
    }

    private void sinkConfLeaveComplete(long j) {
        if (onConfLeaveComplete(j)) {
            ConfMgr.getInstance().cleanupConf();
            VideoBoxApplication.getInstance().stopConfService();
        }
    }

    /* access modifiers changed from: protected */
    public boolean onConfLeaveComplete(long j) {
        finishSubActivities();
        finish(true);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        finishSubActivities();
        ZMConfComponentMgr.getInstance().onActivityResume();
        initOrientationListener();
        checkNetworkRestrictionMode();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        destroyOrientationListener();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(@NonNull Intent intent) {
        doIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1000 && i2 == -1 && intent != null) {
            onSentInvitationDone(intent);
        }
    }

    public void onBackPressed() {
        if (MeetingRunningInfoFragment.dismiss(getSupportFragmentManager()) || isCallingOut()) {
            return;
        }
        if (ConfShareLocalHelper.isSharingOut()) {
            moveTaskToBack(true);
            return;
        }
        if (ConfMgr.getInstance().isConfConnected()) {
            onClickBtnBack();
        } else {
            onClickLeave();
        }
    }

    public void onNewIncomingCallCanceled(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onNewIncomingCallCanceled") {
            public void run(@NonNull IUIElement iUIElement) {
                NewIncomingCallDialog.closeDialog(ConfActivity.this, j);
            }
        });
    }

    private void initOrientationListener() {
        this.mOrientationListener = new OrientationEventListener(this, 3) {
            private int mRotation = -1;

            public void onOrientationChanged(int i) {
                int orientation = ConfLocalHelper.getOrientation(i);
                if (orientation != this.mRotation && orientation != -1) {
                    this.mRotation = orientation;
                    ConfActivity.this.onOrientationChanged(orientation);
                }
            }
        };
        if (this.mOrientationListener.canDetectOrientation()) {
            this.mOrientationListener.enable();
        }
    }

    private void destroyOrientationListener() {
        OrientationEventListener orientationEventListener = this.mOrientationListener;
        if (orientationEventListener != null && orientationEventListener.canDetectOrientation()) {
            this.mOrientationListener.disable();
        }
    }

    /* access modifiers changed from: private */
    public void onOrientationChanged(int i) {
        ZMConfComponentMgr.getInstance().sinkInOrientationChanged();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ZMConfComponentMgr.getInstance().sinkInOrientationChanged();
    }

    public void onSentInvitationDone(@NonNull Intent intent) {
        final int intExtra = intent.getIntExtra("invitations_count", 0);
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).showTipInvitationsSent(intExtra);
            }
        });
    }

    public boolean canSwitchAudioSource() {
        boolean z = false;
        if (!ConfMgr.getInstance().isConfConnected() || isCallingOut()) {
            return false;
        }
        int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
        boolean z2 = selectedPlayerStreamType == 0 || (selectedPlayerStreamType < 0 && ConfUI.getInstance().isCallOffHook());
        boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(this);
        boolean z3 = HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn();
        if (z2 && ((isFeatureTelephonySupported || z3) && (ConfLocalHelper.getMyAudioType() == 0 || ConfUI.getInstance().isCallOffHook()))) {
            z = true;
        }
        return z;
    }

    public void confirmWebinarRegisterInfo(String str, String str2, boolean z) {
        ConfMgr instance = ConfMgr.getInstance();
        if (!StringUtil.isEmptyOrNull(str)) {
            PreferenceUtil.saveStringValue(PreferenceUtil.SCREEN_NAME, str);
        }
        if (!StringUtil.isEmptyOrNull(str2)) {
            PreferenceUtil.saveStringValue("email", str2);
        }
        instance.onUserRegisterWebinar(str, str2, z);
    }

    public void muteAudio(boolean z) {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                long confOption = confContext.getConfOption();
                if (z) {
                    audioObj.setMutebySelfFlag(true);
                    if (!audioObj.stopAudio()) {
                        audioObj.setMutebySelfFlag(confContext.getOldMuteMyselfFlag(confOption));
                    }
                } else if (ConfMgr.getInstance().canUnmuteMyself()) {
                    audioObj.setMutebySelfFlag(false);
                    if (!audioObj.startAudio()) {
                        audioObj.setMutebySelfFlag(confContext.getOldMuteMyselfFlag(confOption));
                    }
                } else {
                    showTipCannotUnmuteForSharingAudioStarted();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void hostAskUnmute() {
        ConfLocalHelper.hostAskUnmute();
    }

    public void finish(boolean z) {
        if (z) {
            ConfUI.getInstance().setIsLeavingConference(true);
        }
        if (this.mConfParams.isMbCloseOnLeaveMeeting()) {
            moveTaskToBack(true);
        }
        finishSubActivities();
        super.finish();
    }

    public void finishSubActivities() {
        finishActivity(1000);
        finishActivity(1007);
        finishActivity(1004);
        finishActivity(1010);
        finishActivity(1013);
        finishActivity(1014);
        finishActivity(ZMConfRequestConstant.REQUEST_DOCUMENT_BUSINESS_PICKER);
        finishActivity(1019);
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof ConfActivityNormal) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
    }

    public boolean dismissTempTips() {
        boolean dismiss = ChatTip.dismiss(getSupportFragmentManager());
        if (MessageTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (BOMessageTip.dismissAll(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (ToastTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (AudioTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (VideoTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (InviteViaDialogFragment.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (ShareTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        AbsVideoSceneMgr videoSceneMgr = getVideoSceneMgr();
        if (confContext != null && !confContext.isWebinar() && videoSceneMgr != null && videoSceneMgr.isInDriveModeScence() && RaiseHandTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (InterpretationTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        if (MoreTip.dismiss(getSupportFragmentManager())) {
            dismiss = true;
        }
        for (TipMessageType name : TipMessageType.values()) {
            if (NormalMessageTip.dismiss(getSupportFragmentManager(), name.name())) {
                dismiss = true;
            }
        }
        return dismiss;
    }

    public boolean isNetworkRestrictionMode() {
        switch (NetworkUtil.getDataNetworkType(this)) {
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public void onClickBtnBack() {
        if (!PTAppDelegation.getInstance().isWebSignedOn() || VideoBoxApplication.getInstance().isSDKMode()) {
            moveTaskToBack(true);
        } else if (!ConfLocalHelper.isDirectShareClient()) {
            IMActivity.show(this, true);
        }
    }

    @SuppressLint({"NewApi"})
    public void onClickLeave() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
        boolean z = accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
        if (BOUtil.isInBOMeeting() || (BOUtil.isInBOController() && BOUtil.getBOControlStatus() == 2)) {
            showLeaveBODialog();
            return;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (!z || myself == null || myself.isHost() || ConfLocalHelper.getMyAudioType() == 1) {
            new LeaveAlertDialog().show(getSupportFragmentManager(), LeaveAlertDialog.class.getName());
            return;
        }
        int confStatus = ConfMgr.getInstance().getConfStatus();
        if (confStatus == 8 || confStatus == 9) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(8), true);
        } else {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
        }
        ConfLocalHelper.leaveCall(this);
    }

    public boolean isWebinarAttendeeRaiseHand(String str) {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            return raiseHandAPIObj.getRaisedHandStatus(str);
        }
        return false;
    }

    private void sinkConfFail(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onConfFail") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfFail(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfFail(long j) {
        if (VideoBoxApplication.getInstance().isSDKMode()) {
            if (j == 10) {
                ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(4), true);
                ConfLocalHelper.leaveCall(this);
                return;
            } else if (j == 23) {
                ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(26), true);
                ConfLocalHelper.leaveCall(this);
                return;
            }
        }
        if (this.mConfParams.isMbNoMeetingErrorMsg()) {
            int i = (int) j;
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(ConfLocalHelper.errorCodeToLeaveReason(i)), true, i == 1);
            ConfLocalHelper.leaveCall(this);
        } else if (j == 16) {
            MeetingEndMessageActivity.showTokenExpiredMessage(this);
            ConfLocalHelper.leaveCall(this);
        } else if (j == 62) {
            ZMRealNameConfirmDialog.showDialog(this);
        } else if (j == 66) {
            ConfLocalHelper.leaveCallWithDialog(this, getString(C4558R.string.zm_unable_to_join_meeting_title_93170), getString(C4558R.string.zm_unable_to_join_meeting_msg_93170));
        } else {
            int i2 = (int) j;
            if (23 != i2) {
                ConfLocalHelper.leaveCallForErrorCode(this, i2);
            } else if (!PTAppDelegation.getInstance().isAuthenticating()) {
                ConfLocalHelper.leaveCallForErrorCode(this, i2);
            } else {
                PTAppDelegation.getInstance().setNeedCheckSwitchCall(true);
            }
        }
    }

    private void sinkConfFirstTimeFreeGift(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfFirstTimeFreeGift", new EventAction("sinkConfFirstTimeFreeGift") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfFirstTimeFreeGift(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfFirstTimeFreeGift(long j) {
        AlertFreeMeetingDialog.showDialog(getSupportFragmentManager(), false);
    }

    private void sinkConfThirdTimeFreeGift(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfThirdTimeFreeGift", new EventAction("sinkConfThirdTimeFreeGift") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfThirdTimeFreeGift(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfThirdTimeFreeGift(long j) {
        if (j == 1) {
            UpgradeFreeMeetingDialog.showDialog(getSupportFragmentManager());
        } else {
            showPlayerReminderDialog(false);
        }
    }

    private void sinkConfNeedAdminPayRemind(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfNeedAdminPayRemind", new EventAction("sinkConfNeedAdminPayRemind") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfNeedAdminPayRemind(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfNeedAdminPayRemind(long j) {
        AlertFreeMeetingDialog.showDialog(getSupportFragmentManager(), true);
    }

    private void sinkConfPlayerReminder(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfPlayerReminder", new EventAction("sinkConfPlayerReminder") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfPlayerReminder(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfPlayerReminder(long j) {
        boolean z = j == 1;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (!z || confContext == null || !confContext.canUpgradeThisFreeMeeting()) {
            showPlayerReminderDialog(z);
        } else {
            AlertFreeMeetingDialog.showDialog(getSupportFragmentManager(), false);
        }
    }

    private void sinkConfNoHost(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfNoHost", new EventAction("sinkConfNoHost") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfNoHost(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfNoHost(long j) {
        showConfNoHostDialog(j);
    }

    private void showPlayerReminderDialog(boolean z) {
        PayerReminderDialog.newPlayerReminderDialog(z).show(getSupportFragmentManager(), PayerReminderDialog.class.getSimpleName());
    }

    private void showConfNoHostDialog(long j) {
        SimpleMessageDialog.newInstance(getString(C4558R.string.zm_msg_conf_no_host, new Object[]{Long.valueOf(j)})).show(getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
    }

    private void sinkConfCloseOtherMeeting() {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfCloseOtherMeeting", new EventAction("sinkConfCloseOtherMeeting") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfCloseOtherMeeting();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfCloseOtherMeeting() {
        CloseOtherMeetingDialog.show(getSupportFragmentManager());
    }

    private void sinkConfMeetingUpgraded() {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfMeetingUpgraded", new EventAction("sinkConfMeetingUpgraded") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).onConfMeetingUpgrade();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfMeetingUpgrade() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            AlertFreeMeetingDialog.dismiss(supportFragmentManager);
            UpgradeFreeMeetingDialog.dismiss(supportFragmentManager);
            UpgradeFreeMeetingErrorDialog.dismiss(supportFragmentManager);
        }
    }

    private void sinkCallOutStatusChanged(final long j) {
        if (ConfMgr.getInstance().getConfDataHelper().ismIsNeedHandleCallOutStatusChangedInMeeting()) {
            getNonNullEventTaskManagerOrThrowException().push("sinkCallOutStatusChanged", new EventAction("sinkCallOutStatusChanged") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((ConfActivity) iUIElement).onCallOutStatusChanged(j);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onCallOutStatusChanged(long j) {
        ConfLocalHelper.handleCallOutStatusChanged(this, j);
    }

    private void sinkCmrStorageFull() {
        getNonNullEventTaskManagerOrThrowException().push("sinkCmrStorageFull", new EventAction("sinkCmrStorageFull") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).showCmrStorageFull();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showCmrStorageFull() {
        new CmrFullStorageDialog().show(getSupportFragmentManager(), CmrFullStorageDialog.class.getName());
    }

    /* access modifiers changed from: private */
    public void _onPTAskToLeave(int i) {
        if (i == 0 || i == 8) {
            finish(true);
        } else if (i != 41) {
            if (isActive()) {
                handleOnPTAskToLeave(i);
            } else {
                ConfUI.getInstance().clearPTAskToLeaveReason();
                Intent intent = new Intent(this, getClass());
                intent.setAction(ZMConfIntentParam.ACTION_PT_ASK_TO_LEAVE);
                intent.addFlags(131072);
                intent.putExtra(ZMConfIntentParam.ARG_LEAVE_REASON, i);
                ActivityStartHelper.startActivityForeground(this, intent);
            }
        } else if (!ConfLocalHelper.isGe2NotCallingOut()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                IntegrationActivity.showCallNotAnsweredMessage(this, confContext.get1On1BuddyScreeName());
            }
            finish(true);
        }
    }

    private void handleOnPTAskToLeave(final int i) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("handleOnPTAskToLeave") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleOnPTAskToLeaveImpl(i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleOnPTAskToLeaveImpl(int i) {
        if (i == 1 || i == 3) {
            onClickLeave();
            return;
        }
        switch (i) {
            case 5:
                ConfLocalHelper.leaveCallForErrorCode(this, 1);
                return;
            case 6:
                ConfLocalHelper.leaveCallForErrorCode(this, 50);
                return;
            default:
                ConfLocalHelper.leaveCallForErrorCode(this, -1);
                return;
        }
    }

    /* access modifiers changed from: private */
    public void _onJoinConfConfirmMeetingInfo(boolean z, boolean z2, boolean z3) {
        final boolean z4 = z;
        final boolean z5 = z2;
        final boolean z6 = z3;
        C192535 r2 = new EventAction("handleJoinConfConfirmMeetingInfo") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleJoinConfConfirmMeetingInfo(z4, z5, z6);
            }
        };
        getNonNullEventTaskManagerOrThrowException().push("handleJoinConfConfirmMeetingInfo", r2);
    }

    /* access modifiers changed from: private */
    public void handleJoinConfConfirmMeetingInfo(boolean z, boolean z2, boolean z3) {
        if (z) {
            if (z2) {
                ZMRealNameAuthDialog.dismiss(this, true);
                NamePasswordDialog namePasswordDialog = new NamePasswordDialog();
                Bundle bundle = new Bundle();
                if (z3) {
                    bundle.putSerializable(NamePasswordDialog.ARG_SHOW_SCREEN_NAME, Boolean.valueOf(false));
                } else {
                    bundle.putSerializable("screenName", PreferenceUtil.readStringValue(PreferenceUtil.SCREEN_NAME, ""));
                    RetainedFragment retainedFragment = getRetainedFragment();
                    if (retainedFragment != null) {
                        retainedFragment.mHasPopupNameDialog = true;
                    }
                }
                namePasswordDialog.setArguments(bundle);
                namePasswordDialog.show(getSupportFragmentManager(), NamePasswordDialog.class.getName());
            } else if (!z3) {
                AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
                String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.SCREEN_NAME, "");
                if (accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
                    if (StringUtil.isEmptyOrNull(readStringValue)) {
                        readStringValue = Mainboard.getDeviceDefaultName();
                    }
                    ConfLocalHelper.confirmNamePassword(this, "", readStringValue);
                }
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && !confContext.isVideoOn()) {
                AbsVideoSceneMgr videoSceneMgr = getVideoSceneMgr();
                if (videoSceneMgr != null) {
                    videoSceneMgr.stopPreviewDevice();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void _onJoinConfConfirmPasswordValidateResult(final boolean z, final boolean z2) {
        getNonNullEventTaskManagerOrThrowException().push("handleJoinConfConfirmPasswordValidateResult", new EventAction("handleJoinConfConfirmPasswordValidateResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleJoinConfConfirmPasswordValidateResult(z, z2);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleJoinConfConfirmPasswordValidateResult(boolean z, boolean z2) {
        if (!z) {
            ZMRealNameAuthDialog.dismiss(this, true);
            NamePasswordDialog namePasswordDialog = new NamePasswordDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(NamePasswordDialog.ARG_SHOW_SCREEN_NAME, Boolean.valueOf(false));
            bundle.putSerializable(NamePasswordDialog.ARG_INCORRECT_PSW, Boolean.valueOf(true));
            namePasswordDialog.setArguments(bundle);
            namePasswordDialog.show(getSupportFragmentManager(), NamePasswordDialog.class.getName());
        } else if (z2) {
            switchViewToWaitingJoinView();
        }
    }

    /* access modifiers changed from: private */
    public void _onJoinConfVerifyMeetingInfo(final int i) {
        getNonNullEventTaskManagerOrThrowException().push("handleJoinConfVerifyMeetingInfo", new EventAction("handleJoinConfVerifyMeetingInfo") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleJoinConfVerifyMeetingInfo(i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleJoinConfVerifyMeetingInfo(int i) {
        ZMRealNameAuthDialog.show(this);
    }

    /* access modifiers changed from: private */
    public void _onJoinConfConfirmMeetingStatus(final boolean z, final boolean z2) {
        getNonNullEventTaskManagerOrThrowException().push("handleJoinConfConfirmMeetingStatus", new EventAction("handleJoinConfConfirmMeetingStatus") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleJoinConfConfirmMeetingStatus(z, z2);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean _onJoinConf_ConfirmMultiVanityURLs() {
        getNonNullEventTaskManagerOrThrowException().push("_onJoinConf_ConfirmMultiVanityURLs", new EventAction("_onJoinConf_ConfirmMultiVanityURLs") {
            public void run(@NonNull IUIElement iUIElement) {
                SelectPersonalLinkToJoinDialog.show(ConfActivity.this.getSupportFragmentManager());
            }
        });
        return true;
    }

    /* access modifiers changed from: private */
    public void handleJoinConfConfirmMeetingStatus(boolean z, boolean z2) {
        if (z) {
            RetainedFragment retainedFragment = getRetainedFragment();
            if (retainedFragment != null) {
                retainedFragment.mIsAbleToJoin = true;
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null) {
                    if (!confContext.needUserConfirmToJoinOrStartMeeting()) {
                        if (!isCallingOut()) {
                            switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                        }
                    } else if (confContext.needPromptJoinMeetingDisclaimer()) {
                        CustomizeInfo joinMeetingDisclaimer = confContext.getJoinMeetingDisclaimer();
                        if (joinMeetingDisclaimer == null || joinMeetingDisclaimer.isEmpty()) {
                            ZMNetErrorDialog.showDialog(this, 2);
                        } else {
                            joinMeetingDisclaimer.setType(2);
                            PrivacyDisclaimerFragment.showDialog(this, joinMeetingDisclaimer);
                        }
                    } else if (confContext.needPromptLoginWhenJoin()) {
                        showPromptLogin();
                    } else {
                        String myScreenName = confContext.getMyScreenName();
                        boolean needConfirmGDPR = confContext.needConfirmGDPR();
                        String toSUrl = confContext.getToSUrl();
                        String privacyUrl = confContext.getPrivacyUrl();
                        if (StringUtil.isEmptyOrNull(myScreenName)) {
                            RetainedFragment retainedFragment2 = getRetainedFragment();
                            if (!retainedFragment2.mHasPopupNameDialog) {
                                retainedFragment2.mHasPopupNameDialog = true;
                                ZMRealNameAuthDialog.dismiss(this, true);
                                NamePasswordDialog namePasswordDialog = new NamePasswordDialog();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("screenName", PreferenceUtil.readStringValue(PreferenceUtil.SCREEN_NAME, ""));
                                bundle.putSerializable(NamePasswordDialog.ARG_SHOW_PSW, Boolean.valueOf(false));
                                namePasswordDialog.setArguments(bundle);
                                namePasswordDialog.show(getSupportFragmentManager(), NamePasswordDialog.class.getName());
                            } else {
                                return;
                            }
                        } else if (needConfirmGDPR && !StringUtil.isEmptyOrNull(toSUrl) && !StringUtil.isEmptyOrNull(privacyUrl)) {
                            ZMGDPRConfirmDialog findFragment = ZMGDPRConfirmDialog.findFragment(getSupportFragmentManager());
                            if (findFragment != null) {
                                findFragment.dismiss();
                            }
                            ZMGDPRConfirmDialog.showDialog(this, 0, 2, toSUrl, privacyUrl);
                        } else if (confContext.needPromptChinaMeetingPrivacy()) {
                            ZMCDPRConfirmDialog.showDialog(this);
                        } else if (confContext.needPromptGuestParticipantLoginWhenJoin()) {
                            ZMAlertDialog zMAlertDialog = this.mGuestJoinLoginTip;
                            if (zMAlertDialog == null) {
                                this.mGuestJoinLoginTip = new Builder(this).setVerticalOptionStyle(true).setMessage(C4558R.string.zm_alert_join_tip_87408).setTitle(C4558R.string.zm_alert_join_the_meeting_title_87408).setCancelable(false).setPositiveButton(C4558R.string.zm_alert_sign_in_to_join_title_87408, (OnClickListener) new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.LOG_FORCE_SIGN_IN.ordinal(), 47);
                                        ConfMgr.getInstance().loginToJoinMeetingForGuest();
                                    }
                                }).setNeutralButton(C4558R.string.zm_btn_join_as_guest_87408, (OnClickListener) new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ConfMgr.getInstance().continueJoinAsGuest();
                                    }
                                }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                                        ConfLocalHelper.leaveCall(ConfActivity.this);
                                    }
                                }).create();
                                this.mGuestJoinLoginTip.show();
                            } else if (!zMAlertDialog.isShowing()) {
                                this.mGuestJoinLoginTip.show();
                            }
                        } else if (confContext.needConfirmVideoPrivacyWhenJoinMeeting()) {
                            this.mPreviewVideoDialog = ZMPreviewVideoDialog.show(this);
                        }
                    }
                    notifyNetworkType();
                }
            }
        }
    }

    private void showPromptLogin() {
        String str;
        String str2;
        String str3;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (confContext.isConfUserLogin()) {
                str3 = getString(C4558R.string.zm_switch_account_to_join_title_129757);
                str2 = getString(C4558R.string.zm_switch_account_to_join_message_129757);
                str = getString(C4558R.string.zm_switch_account_129757);
            } else {
                str3 = getString(C4558R.string.zm_require_sign_to_join_title_129757);
                str2 = getString(C4558R.string.zm_require_sign_to_join_message_129757);
                str = getString(C4558R.string.zm_sign_to_join_129757);
            }
            ZMAlertDialog create = new Builder(this).setTitle((CharSequence) str3).setMessage(str2).setPositiveButton(str, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfMgr.getInstance().loginToJoinMeeting();
                }
            }).setNegativeButton(C4558R.string.zm_btn_leave_meeting, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfLocalHelper.leaveCall(ConfActivity.this);
                }
            }).create();
            create.setCancelable(true);
            create.setCanceledOnTouchOutside(false);
            create.show();
        }
    }

    public void showPreviewVideoDialog() {
        ZMPreviewVideoDialog zMPreviewVideoDialog = this.mPreviewVideoDialog;
        if (zMPreviewVideoDialog != null) {
            zMPreviewVideoDialog.startPreview();
        }
    }

    /* access modifiers changed from: private */
    public void _onCallTimeOut() {
        if (!ConfLocalHelper.isGe2NotCallingOut()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                IntegrationActivity.showCallNotAnsweredMessage(this, confContext.get1On1BuddyScreeName());
            }
            ConfLocalHelper.endCall(this);
        }
    }

    /* access modifiers changed from: private */
    public void _onDeviceStatusChanged(int i, int i2) {
        if (i != 1) {
            ZMConfComponentMgr.getInstance().sinkInDeviceStatusChanged(i, i2);
        } else if (i2 == 10) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("onMicFeedbackDetected") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((ConfActivity) iUIElement).handleOnMicEchoDetected();
                }
            });
        } else if (i2 == 2) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("onMicDeviceErrorFound") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((ConfActivity) iUIElement).handleOnMicDeviceError();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnMicDeviceError() {
        if (VERSION.SDK_INT >= 23 && zm_checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            requestPermission("android.permission.RECORD_AUDIO", 1015, 500);
        }
    }

    /* access modifiers changed from: private */
    public void handleOnMicEchoDetected() {
        showTipMicEchoDetected();
    }

    /* access modifiers changed from: private */
    public void _onWebinarNeedRegister(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onWebinarNeedRegister") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleOnWebinarNeedRegister(z);
            }
        });
    }

    /* access modifiers changed from: private */
    public void _onUpgradeThisFreeMeeting(final int i) {
        if (i != 0) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("_onUpgradeThisFreeMeeting") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((ConfActivity) iUIElement).handleOnUpgradeThisFreeMeeting(i);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void _onCheckCMRPrivilege(int i, boolean z) {
        if (i != 0) {
            showCheckCMRPrivilegeErrorMessage();
            return;
        }
        if (z) {
            ConfLocalHelper.startCMR();
        } else {
            sinkCmrStorageFull();
        }
    }

    private void showCheckCMRPrivilegeErrorMessage() {
        ZMAlertDialog create = new Builder(this).setTitle(C4558R.string.zm_record_msg_start_cmr_error_5537).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCancelable(true);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    /* access modifiers changed from: private */
    public void handleOnWebinarNeedRegister(boolean z) {
        if (z) {
            showWebinarNeedRegisterMessage();
        } else {
            showWebinarRegisterDialog();
        }
    }

    /* access modifiers changed from: private */
    public void handleOnUpgradeThisFreeMeeting(int i) {
        if (i != 0) {
            UpgradeFreeMeetingErrorDialog.showDialog(getSupportFragmentManager(), i);
        }
    }

    private void showWebinarNeedRegisterMessage() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            new WebinarNeedRegisterDialog().show(supportFragmentManager, WebinarNeedRegisterDialog.class.getName());
        }
    }

    private void showWebinarRegisterDialog() {
        boolean z;
        String str;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            String str2 = null;
            if (confContext != null) {
                str2 = confContext.getMyScreenName();
                str = confContext.getMyEmail();
                z = confContext.isWebinar();
            } else {
                str = null;
                z = false;
            }
            if (StringUtil.isEmptyOrNull(str2)) {
                str2 = PreferenceUtil.readStringValue(PreferenceUtil.SCREEN_NAME, "");
            }
            if (StringUtil.isEmptyOrNull(str)) {
                str = PreferenceUtil.readStringValue("email", "");
            }
            WebinarRegisterDialog.show(supportFragmentManager, str2, str, z ? C4558R.string.zm_msg_webinar_need_register : C4558R.string.zm_msg_meeting_need_register);
        }
    }

    /* access modifiers changed from: private */
    public static void onNetworkState(Intent intent) {
        notifyNetworkType();
        ZMActivity frontActivity = ZMActivity.getFrontActivity();
        if ((frontActivity instanceof ConfActivityNormal) && frontActivity.isActive()) {
            ((ConfActivity) frontActivity).checkNetworkRestrictionMode();
        }
    }

    private static void notifyNetworkType() {
        ConfUI.getInstance().notifyNetworkType();
    }

    /* access modifiers changed from: private */
    public static void notifyWifiSignal() {
        ConfUI.getInstance().notifyWifiSignal();
    }

    private static void startNotifyWifiSignal() {
        if (g_taskNotifyWifiSignal == null) {
            g_taskNotifyWifiSignal = new Runnable() {
                public void run() {
                    ConfActivity.notifyWifiSignal();
                    ConfActivity.g_handler.postDelayed(this, ConfActivity.TIMEOUT_NOTIFY_WIFI_SIGNAL);
                }
            };
            g_handler.postDelayed(g_taskNotifyWifiSignal, TIMEOUT_NOTIFY_WIFI_SIGNAL);
        }
    }

    private static void stopNotifyWifiSignal() {
        Runnable runnable = g_taskNotifyWifiSignal;
        if (runnable != null) {
            g_handler.removeCallbacks(runnable);
            g_taskNotifyWifiSignal = null;
        }
    }

    private void checkNetworkRestrictionMode() {
        if (ConfMgr.getInstance().isConfConnected()) {
            boolean isNetworkRestrictionMode = isNetworkRestrictionMode();
            AbsVideoSceneMgr videoSceneMgr = getVideoSceneMgr();
            if (videoSceneMgr != null) {
                videoSceneMgr.onNetworkRestrictionModeChanged(isNetworkRestrictionMode);
            }
        }
    }

    private static void startListenNetworkState(@NonNull Context context) {
        if (g_networkStateReceiver == null) {
            g_networkStateReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @Nullable Intent intent) {
                    if (intent != null && "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                        ConfActivity.onNetworkState(intent);
                    }
                }
            };
            context.getApplicationContext().registerReceiver(g_networkStateReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    private static void stopListenNetworkState(@NonNull Context context) {
        if (g_networkStateReceiver != null) {
            context.getApplicationContext().unregisterReceiver(g_networkStateReceiver);
            g_networkStateReceiver = null;
        }
    }

    private static void startListenVolumeChange(@NonNull Context context) {
        if (g_volumeChangeReceiver == null) {
            g_volumeChangeReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @Nullable Intent intent) {
                    if (intent != null && "android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
                        ConfActivity.onVolumeChanged(intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1), intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1));
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
            context.getApplicationContext().registerReceiver(g_volumeChangeReceiver, intentFilter);
        }
    }

    private static void stopListenVolumeChange(@NonNull Context context) {
        if (g_volumeChangeReceiver != null) {
            context.getApplicationContext().unregisterReceiver(g_volumeChangeReceiver);
            g_volumeChangeReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public static void onVolumeChanged(final int i, int i2) {
        if (i == 3 && "Amazon".equals(Build.MANUFACTURER) && !VoiceEngineCompat.isFeatureTelephonySupported(VideoBoxApplication.getInstance())) {
            if (g_audioManager == null) {
                g_audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
            }
            AudioManager audioManager = g_audioManager;
            if (audioManager != null) {
                g_audioManager.setStreamVolume(0, (int) (((float) g_audioManager.getStreamMaxVolume(0)) * (((float) i2) / ((float) audioManager.getStreamMaxVolume(i)))), 0);
            } else {
                return;
            }
        }
        if (VoiceEnginContext.getSelectedPlayerStreamType() == i && i2 != g_lastVolume) {
            g_lastVolume = i2;
            if (g_runnableNotifyVolumeChanged == null) {
                g_runnableNotifyVolumeChanged = new Runnable() {
                    public void run() {
                        ConfActivityNormal.notifyVolumeChanged(ConfActivity.g_lastNotifiedVolume < ConfActivity.g_lastVolume, ConfActivity.g_lastVolume, i);
                    }
                };
            }
            g_handler.removeCallbacks(g_runnableNotifyVolumeChanged);
            g_handler.postDelayed(g_runnableNotifyVolumeChanged, 1000);
        }
    }

    public static void notifyVolumeChanged(boolean z, int i, int i2) {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            g_lastNotifiedVolume = i;
            if (g_audioManager == null) {
                g_audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
            }
            AudioManager audioManager = g_audioManager;
            if (audioManager != null) {
                audioObj.notifyVolumeChanged(z, Math.round((((float) i) * 100.0f) / ((float) audioManager.getStreamMaxVolume(i2))));
            }
        }
    }

    private void showTipCannotUnmuteForSharingAudioStarted() {
        NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_CANNOT_UNMUTE_FOR_SHARING_AUDIO_STARTED.name(), (String) null, getString(C4558R.string.zm_msg_cannot_unmute_for_host_muted_all_141384), 3000);
    }

    /* access modifiers changed from: private */
    public void showTipInvitationsSent(int i) {
        NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_INVITATIONS_SENT.name(), null, getResources().getQuantityString(C4558R.plurals.zm_msg_invitations_sent, i, new Object[]{Integer.valueOf(i)}), C4558R.C4559drawable.zm_ic_tick, 0, 0, 3000);
    }

    /* access modifiers changed from: protected */
    public void switchViewToWaitingJoinView() {
        if (hasCustomJBHActivity()) {
            showCustomJBHActivity();
        } else {
            switchViewTo(ZMConfEnumViewMode.WAITING_JOIN_VIEW);
        }
    }

    public void performDialogAction(int i, int i2, @Nullable Bundle bundle) {
        if (i == 0) {
            if (i2 == -1) {
                ConfMgr.getInstance().confirmGDPR(true);
            } else if (i2 == -2 && bundle != null) {
                final String string = bundle.getString(ZMGDPRConfirmDialog.ARGS_TERMS_URL);
                final String string2 = bundle.getString(ZMGDPRConfirmDialog.ARGS_PRIVACY_URL);
                if (!StringUtil.isEmptyOrNull(string) && !StringUtil.isEmptyOrNull(string2)) {
                    getNonNullEventTaskManagerOrThrowException().push(new EventAction("alertNewIncomingCall") {
                        public void run(@Nullable IUIElement iUIElement) {
                            ConfActivity confActivity = (ConfActivity) iUIElement;
                            if (iUIElement != null) {
                                ZMGDPRConfirmDialog.showDialog(confActivity, 1, 3, string, string2);
                            }
                        }
                    });
                }
            }
        } else if (i == 1) {
            if (i2 == -1) {
                ConfMgr.getInstance().confirmGDPR(true);
            } else if (i2 == -2) {
                ConfMgr.getInstance().confirmGDPR(false);
            }
        }
    }

    public boolean hasTipPointToToolbar() {
        if (!ChatTip.isShown(getSupportFragmentManager()) && !BOMessageTip.isShown(getSupportFragmentManager()) && !AudioTip.isShown(getSupportFragmentManager()) && !VideoTip.isShown(getSupportFragmentManager()) && !NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_WAITING_TO_INVITE.name()) && !NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_RECONNECT_AUDIO.name()) && !NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_UNABLE_TO_SHARE.name()) && !NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_MIC_ECHO_DETECTED.name()) && !NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name()) && !ShareTip.isShown(getSupportFragmentManager()) && !InterpretationTip.isShown(getSupportFragmentManager())) {
            return MoreTip.isShown(getSupportFragmentManager());
        }
        return true;
    }

    private void showLeaveBODialog() {
        BOLeaveFragment.showAsDialog(getSupportFragmentManager(), BOUtil.isInBOController(), BOUtil.isInBOMeeting(), BOLeaveFragment.class.getSimpleName());
    }

    public void doRequestPermission() {
        boolean z;
        int size = this.mPendingRequestPermissions.size();
        if (size > 0) {
            int intValue = ((Integer) this.mPendingRequestPermissionCodes.get(0)).intValue();
            int i = 1;
            while (true) {
                if (i >= size) {
                    z = true;
                    break;
                } else if (intValue != ((Integer) this.mPendingRequestPermissionCodes.get(i)).intValue()) {
                    z = false;
                    break;
                } else {
                    i++;
                }
            }
            if (z) {
                String[] strArr = (String[]) this.mPendingRequestPermissions.toArray(new String[size]);
                if (strArr.length > 0) {
                    this.mRequestPermissionTime = System.currentTimeMillis();
                    zm_requestPermissions(strArr, intValue);
                }
                this.mPendingRequestPermissions.clear();
                this.mPendingRequestPermissionCodes.clear();
            } else {
                String str = (String) this.mPendingRequestPermissions.get(0);
                this.mRequestPermissionTime = System.currentTimeMillis();
                zm_requestPermissions(new String[]{str}, intValue);
                this.mPendingRequestPermissions.remove(0);
                this.mPendingRequestPermissionCodes.remove(0);
            }
            this.mInPendingRequestPermission = false;
        }
    }

    /* access modifiers changed from: protected */
    public void requestPendingPermission() {
        requestPermission("", 0, 0);
    }

    public void requestPermission(String str, int i, long j) {
        if (!StringUtil.isEmptyOrNull(str) && !this.mPendingRequestPermissions.contains(str)) {
            this.mPendingRequestPermissions.add(str);
            this.mPendingRequestPermissionCodes.add(Integer.valueOf(i));
            if (!this.mInPendingRequestPermission) {
                this.mInPendingRequestPermission = true;
                this.mHandler.removeCallbacks(this.mHandleRequestPermissionsRunnable);
                this.mHandler.postDelayed(this.mHandleRequestPermissionsRunnable, j);
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final long currentTimeMillis = System.currentTimeMillis() - this.mRequestPermissionTime;
        this.mRequestPermissionTime = 0;
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C194856 r4 = new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivity) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2, currentTimeMillis);
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r4);
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr, long j) {
        if (strArr != null && iArr != null && j <= 100) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (iArr[i2] != 0) {
                    PermissionUnableAccessDialog.showDialog(getSupportFragmentManager(), strArr[i2]);
                }
            }
        }
    }

    private boolean hasCustomJBHActivity() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPackageName());
        sb.append(ZMConfIntentParam.ACTION_MEETING_JBH);
        return AndroidAppUtil.hasActivityForIntent(this, new Intent(sb.toString()));
    }

    private void showCustomJBHActivity() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(getPackageName());
                sb.append(ZMConfIntentParam.ACTION_MEETING_JBH);
                Intent intent = new Intent(sb.toString());
                intent.putExtra(AndroidAppUtil.EXTRA_TOPIC, meetingItem.getTopic());
                intent.putExtra(AndroidAppUtil.EXTRA_MEETING_ID, meetingItem.getMeetingNumber());
                intent.putExtra(AndroidAppUtil.EXTRA_IS_REPEAT, meetingItem.getType() == MeetingType.REPEAT);
                intent.putExtra(AndroidAppUtil.EXTRA_DATE, TimeFormatUtil.formatDate(this, meetingItem.getStartTime() * 1000, false));
                intent.putExtra(AndroidAppUtil.EXTRA_TIME, TimeFormatUtil.formatTime(this, meetingItem.getStartTime() * 1000));
                try {
                    startActivityForResult(intent, 1019);
                } catch (Exception unused) {
                }
            }
        }
    }
}
