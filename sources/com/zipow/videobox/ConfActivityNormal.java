package com.zipow.videobox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ConfActivity.RetainedFragment;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CCMessage;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfInnerEventListener;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.IRoomSystemCallEventListener;
import com.zipow.videobox.confapp.InterpretationSinkUI;
import com.zipow.videobox.confapp.InterpretationSinkUI.IInterpretationSinkUIListener;
import com.zipow.videobox.confapp.InviteRoomDeviceInfo;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.component.ZMConfEnumViewMode;
import com.zipow.videobox.confapp.component.sink.share.IShareStatusSink;
import com.zipow.videobox.confapp.component.sink.video.IVideoStatusSink;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.confapp.meeting.confhelper.BOComponent;
import com.zipow.videobox.confapp.meeting.confhelper.ConfDataHelper;
import com.zipow.videobox.confapp.meeting.confhelper.KubiComponent;
import com.zipow.videobox.confapp.meeting.confhelper.LiveStreamComponent;
import com.zipow.videobox.confapp.meeting.confhelper.PollComponent;
import com.zipow.videobox.confapp.p009bo.BOUtil;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.dialog.ConfAllowTalkDialog;
import com.zipow.videobox.dialog.RecordingReminderDialog;
import com.zipow.videobox.dialog.ZMRealNameAuthDialog;
import com.zipow.videobox.dialog.ZMSpotlightVideoDialog;
import com.zipow.videobox.fragment.ConfChatFragment;
import com.zipow.videobox.fragment.HostKeyEnterDialog;
import com.zipow.videobox.fragment.HostKeyErrorDialog;
import com.zipow.videobox.fragment.InviteFragment;
import com.zipow.videobox.fragment.MeetingRunningInfoFragment;
import com.zipow.videobox.fragment.PListFragment;
import com.zipow.videobox.fragment.QAWebinarAttendeeListFragment;
import com.zipow.videobox.fragment.RecordControlDialog;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTUI.IPTCommonEventListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.RoomDevice;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.delegate.PTUIDelegation;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.util.AppStateMonitor;
import com.zipow.videobox.util.AppStateMonitor.IAppStateListener;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.MeetingInvitationUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMConfCameraUtils;
import com.zipow.videobox.view.AudioClip;
import com.zipow.videobox.view.AudioTip;
import com.zipow.videobox.view.CallConnectingView;
import com.zipow.videobox.view.ChatTip;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import com.zipow.videobox.view.ConfChatItem;
import com.zipow.videobox.view.ConfInterpretationLanguage;
import com.zipow.videobox.view.ConfInterpretationSwitch;
import com.zipow.videobox.view.ConfToolbar;
import com.zipow.videobox.view.ConfToolbar.Listener;
import com.zipow.videobox.view.ConfToolsPanel;
import com.zipow.videobox.view.InterpretationTip;
import com.zipow.videobox.view.MoreTip;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.OnSilentView;
import com.zipow.videobox.view.RaiseHandTip;
import com.zipow.videobox.view.ShareTip;
import com.zipow.videobox.view.ToastTip;
import com.zipow.videobox.view.WaitingJoinView;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.DriverModeVideoScene;
import com.zipow.videobox.view.video.GalleryVideoScene;
import com.zipow.videobox.view.video.MeetingReactionMgr;
import com.zipow.videobox.view.video.NormalVideoScene;
import com.zipow.videobox.view.video.ShareVideoScene;
import com.zipow.videobox.view.video.VideoSceneMgr;
import com.zipow.videobox.view.video.VideoSceneMgrLarge;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.androidlib.widget.CaptionView;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class ConfActivityNormal extends ConfAccessibilityActivity implements IConfUIListener, Listener, OnClickListener, ConfToolsPanel.Listener, IAppStateListener, IConfInnerEventListener {
    private static final String PREF_CLOSED_CAPTION_CONTENT = "closed_caption_content";
    private static final String TAG = "ConfActivityNormal";
    private static final int TIMER_REFRESH_DURATION = 1000;
    /* access modifiers changed from: private */
    @NonNull
    public static Handler g_handler = new Handler();
    /* access modifiers changed from: private */
    @Nullable
    public static Runnable g_hideToolbarRunnable;
    @NonNull
    private Runnable mAttendeeRaiseLowerHandRunnalbe = new Runnable() {
        public void run() {
            ConfActivityNormal.this.updateRaiseHandStatus();
        }
    };
    private BOComponent mBOComponent;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Resources resources = context.getResources();
            if (resources != null) {
                Configuration configuration = resources.getConfiguration();
                if (configuration != null) {
                    ScreenShareMgr.getInstance().onConfigurationChanged(configuration);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public View mBroadcastingView;
    private Button mBtnBack;
    private Button mBtnBroadcast;
    private Button mBtnLeave;
    private View mBtnSwitchToShare;
    private CallConnectingView mCallConnectingView;
    /* access modifiers changed from: private */
    public int mCallType;
    /* access modifiers changed from: private */
    @Nullable
    public String mClosedCaptionContent;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mClosedCaptionTimeoutRunnable = new Runnable() {
        public void run() {
            if (!ConfActivityNormal.this.isDestroyed()) {
                ConfActivityNormal.this.mClosedCaptionView.setVisibility(8);
                ConfActivityNormal.this.mClosedCaptionContent = null;
            }
        }
    };
    /* access modifiers changed from: private */
    public CaptionView mClosedCaptionView;
    private View mConfView;
    @Nullable
    private AudioClip mDuduVoiceClip;
    /* access modifiers changed from: private */
    public ZMAlertDialog mGuestJoinTip;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private ImageView mImgAudioSource;
    private ImageView mImgE2EMeetingView;
    private ImageView mImgRecordControlArrow;
    private ImageView mImgRecording;
    private ConfInterpretationLanguage mInterpretationLanguage;
    private IInterpretationSinkUIListener mInterpretationSinkUIListener = new IInterpretationSinkUIListener() {
        public void OnInterpretationStart() {
            MoreTip.dismiss(ConfActivityNormal.this.getSupportFragmentManager());
            ConfActivityNormal.this.refreshInterpretation();
        }

        public void OnInterpreterListChanged() {
            ConfActivityNormal.this.refreshInterpretation();
        }

        public void OnInterpretationStop() {
            ConfActivityNormal.this.refreshInterpretation();
            InterpretationTip.clearShowState();
            InterpretationTip.dismiss(ConfActivityNormal.this.getSupportFragmentManager());
            MoreTip.dismiss(ConfActivityNormal.this.getSupportFragmentManager());
        }

        public void OnInterpreterInfoChanged(long j, int i) {
            ConfActivityNormal.this.refreshInterpretation();
        }

        public void OnParticipantActiveLanChanged(long j) {
            ConfActivityNormal.this.refreshInterpretation();
        }

        public void OnParticipantActiveLanInvalid() {
            ConfActivityNormal.this.refreshInterpretation();
        }
    };
    private ConfInterpretationSwitch mInterpretationSwitch;
    private ZMAlertDialog mJoinWaitingListDlg;
    private KubiComponent mKubiComponent;
    private LiveStreamComponent mLiveStreamComponent;
    /* access modifiers changed from: private */
    public OnSilentView mOnSilentView;
    @NonNull
    private Handler mOptimizeUserEventHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                case 1:
                case 2:
                    List list = (List) ConfActivityNormal.this.mUserInfoEventCaches.get(message.what);
                    if (!CollectionsUtil.isCollectionEmpty(list)) {
                        ConfActivityNormal.this.onBatchUserEvent(message.what, list);
                        return;
                    }
                    return;
                default:
                    super.handleMessage(message);
                    return;
            }
        }
    };
    private IPTCommonEventListener mPTCommonEventListener;
    private IPTUIListener mPTUIListener;
    private View mPanelAudioConnectStatus;
    private View mPanelConnecting;
    /* access modifiers changed from: private */
    public View mPanelCurUserRecording;
    /* access modifiers changed from: private */
    public View mPanelRecording;
    private View mPanelRejoinMsg;
    /* access modifiers changed from: private */
    public View mPanelStartingRecord;
    private View mPanelSwitchSceneButtons;
    /* access modifiers changed from: private */
    public ConfToolsPanel mPanelTools;
    private PollComponent mPollComponent;
    private View mPracticeModeView;
    private ProgressBar mProgressBarBroadcasting;
    private View mProgressStartingRecord;
    @Nullable
    private IZoomQAUIListener mQAUIListener;
    private View mQaView;
    /* access modifiers changed from: private */
    public RecordingReminderDialog mReminderRecordingTip;
    /* access modifiers changed from: private */
    public RoomDevice mRoomDevice = null;
    private IRoomSystemCallEventListener mRoomSystemCallEventListener = new IRoomSystemCallEventListener() {
        public void onRoomSystemCallEvent(int i, long j, boolean z) {
            if (i == 8 && j >= 100 && ConfActivityNormal.this.mRoomDevice != null) {
                ConfActivityNormal confActivityNormal = ConfActivityNormal.this;
                confActivityNormal.handleCallRoomFail(confActivityNormal.mRoomDevice, ConfActivityNormal.this.mCallType);
            }
        }
    };
    @NonNull
    private IShareStatusSink mShareStatusSink = new IShareStatusSink() {
        public void onBeforeMyStartShare() {
            ConfActivityNormal.this.disableToolbarAutoHide();
            ConfActivityNormal.this.showToolbar(true, false);
            ConfActivityNormal.this.showTopToolbar(false);
            if (!ConfActivityNormal.this.mConfParams.isBottomBarDisabled()) {
                ConfActivityNormal.this.mPanelTools.showToolbar(true, false);
            }
        }

        public void onMyShareStatueChanged(boolean z) {
            if (!z) {
                ConfActivityNormal.this.showTopToolbar(true);
                if (!ConfActivityNormal.this.mConfParams.isBottomBarDisabled()) {
                    ConfActivityNormal.this.mPanelTools.showToolbar(true, false);
                }
                ConfActivityNormal.this.refreshToolbar();
                ZMConfComponentMgr.getInstance().sinkMyShareStatueChanged(false);
                ConfActivityNormal.this.mVideoSceneMgr.startActiveScene();
                ConfActivityNormal.this.mVideoSceneMgr.shouldSwitchActiveSpeakerView();
            } else if (ConfActivityNormal.this.isActive()) {
                ConfActivityNormal.this.refreshToolbar();
            }
        }

        public void onOtherShareStatueChanged(boolean z, long j) {
            if (z) {
                ConfActivityNormal.this.mVideoSceneMgr.onShareActiveUser(j);
                ConfActivityNormal.this.refreshToolbar();
                ConfActivityNormal.this.hideToolbarDelayed(5000);
            }
        }

        public void onShareEdit(boolean z) {
            if (z) {
                ConfActivityNormal.this.showTopToolbar(false);
                ConfActivityNormal.this.mPanelTools.showToolbar(false, false);
            } else if (!ConfActivityNormal.this.mConfParams.isBottomBarDisabled() && !ConfActivityNormal.this.isInDriveMode()) {
                ConfActivityNormal.this.mPanelTools.showToolbar(true, false);
            }
        }

        public void onBeforeRemoteControlEnabled(boolean z) {
            ConfActivityNormal.this.showToolbar(!z, true);
        }
    };
    /* access modifiers changed from: private */
    public ZMAlertDialog mSignIntoJoinTip;
    @NonNull
    private Runnable mTimerRunnable = new Runnable() {
        public void run() {
            ConfActivityNormal.this.mTxtTimer.setVisibility(ConfMgr.getInstance().isShowClockEnable() ? 0 : 8);
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                ConfActivityNormal.this.mTxtTimer.setText(TimeUtil.formateDuration(confStatusObj.getMeetingElapsedTimeInSecs()));
            }
            ConfActivityNormal.this.mHandler.postDelayed(this, 1000);
        }
    };
    /* access modifiers changed from: private */
    public ZMTipLayer mTipLayer;
    /* access modifiers changed from: private */
    public ConfToolbar mToolbar;
    private transient int mToolbarHeight = 0;
    private View mTopbar;
    private transient int mTopbarHeight = 0;
    private TextView mTxtAudioConnectStatus;
    private TextView mTxtCountdown;
    private TextView mTxtMeetingNumber;
    private TextView mTxtPassword;
    private TextView mTxtQAOpenNumber;
    private TextView mTxtRecordStatus;
    private TextView mTxtStartingRecrod;
    /* access modifiers changed from: private */
    public TextView mTxtTimer;
    @NonNull
    private Runnable mUnmuteMyselfRunnable = new Runnable() {
        public void run() {
            ConfLocalHelper.hostAskUnmute();
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public SparseArray<List<ConfUserInfoEvent>> mUserInfoEventCaches = new SparseArray<>();
    /* access modifiers changed from: private */
    @Nullable
    public ProgressDialog mVerifyHostKeyWaitingDialog = null;
    private View mVerifyingMeetingIDView;
    @NonNull
    private IVideoStatusSink mVideoStatusSink = new IVideoStatusSink() {
        public boolean onVideoViewTouchEvent(@NonNull MotionEvent motionEvent) {
            if (ConfActivityNormal.this.isToolbarShowing()) {
                if (motionEvent.getAction() == 0) {
                    if (ConfActivityNormal.g_hideToolbarRunnable != null) {
                        ConfActivityNormal.g_handler.removeCallbacks(ConfActivityNormal.g_hideToolbarRunnable);
                    }
                } else if (motionEvent.getAction() == 1) {
                    ConfActivityNormal.this.hideToolbarDelayed(5000);
                }
            }
            return false;
        }

        public void onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
            ConfActivityNormal.this.switchToolbar();
        }

        public void onVideoMute() {
            ConfActivityNormal.this.mToolbar.setVideoMuted(!ConfMgr.getInstance().getConfDataHelper().ismIsVideoStarted());
            ConfActivityNormal.this.hideToolbarDelayed(5000);
        }

        public void onVideoEnableOrDisable() {
            ConfActivityNormal.this.mToolbar.refreshBtnVideo();
        }

        public void onCameraStatusEvent() {
            ConfActivityNormal.this.refreshToolbar();
        }

        public void onMyVideoStatusChanged() {
            ConfActivityNormal.this.refreshToolbar();
        }
    };
    private View mViewAudioWatermark;
    private WaitingJoinView mWaitingJoinView;

    private boolean isSensorOrientationEnabled() {
        return true;
    }

    /* access modifiers changed from: private */
    public void onCallAccepted(InvitationItem invitationItem) {
    }

    /* access modifiers changed from: private */
    public void onConfReconnect(long j) {
    }

    private void showCMRNotification() {
    }

    private void sinkUserJoinRing() {
    }

    public void onCallTimeOut() {
    }

    public void onCheckCMRPrivilege(int i, boolean z) {
    }

    public void onDeviceStatusChanged(int i, int i2) {
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

    public void onLeavingSilentModeStatusChanged(long j, boolean z) {
    }

    public void onPTAskToLeave(int i) {
    }

    public void onUpgradeThisFreeMeeting(int i) {
    }

    public void onWebinarNeedRegister(boolean z) {
    }

    private int getLayout() {
        if (UIMgr.isLargeMode(this)) {
            return C4558R.layout.zm_conf_main_screen_large;
        }
        return C4558R.layout.zm_conf_main_screen;
    }

    public BOComponent getmBOComponent() {
        return this.mBOComponent;
    }

    public PollComponent getmPollComponent() {
        return this.mPollComponent;
    }

    public KubiComponent getmKubiComponent() {
        return this.mKubiComponent;
    }

    public ZMTipLayer getmZMTipLayer() {
        return this.mTipLayer;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x02fb  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(@androidx.annotation.Nullable android.os.Bundle r8) {
        /*
            r7 = this;
            super.onCreate(r8)
            r0 = 11
            com.zipow.videobox.util.NotificationMgr.removeNotification(r7, r0)
            boolean r0 = r7.isImmersedModeEnabled()
            r1 = 1
            if (r0 == 0) goto L_0x0013
            p021us.zoom.androidlib.util.UIUtil.setTranslucentStatus(r7, r1)
            goto L_0x002a
        L_0x0013:
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 21
            if (r0 < r2) goto L_0x002a
            android.view.Window r0 = r7.getWindow()
            android.content.res.Resources r2 = r7.getResources()
            int r3 = p021us.zoom.videomeetings.C4558R.color.zm_black
            int r2 = r2.getColor(r3)
            r0.setStatusBarColor(r2)
        L_0x002a:
            com.zipow.videobox.mainboard.Mainboard r0 = com.zipow.videobox.mainboard.Mainboard.getMainboard()
            if (r0 == 0) goto L_0x0328
            boolean r0 = r0.isInitialized()
            if (r0 != 0) goto L_0x0038
            goto L_0x0328
        L_0x0038:
            com.zipow.videobox.confapp.component.ZMConfComponentMgr r0 = com.zipow.videobox.confapp.component.ZMConfComponentMgr.getInstance()
            r0.registerAllComponents(r7)
            com.zipow.videobox.confapp.component.ZMConfComponentMgr r0 = com.zipow.videobox.confapp.component.ZMConfComponentMgr.getInstance()
            com.zipow.videobox.confapp.component.sink.share.IShareStatusSink r2 = r7.mShareStatusSink
            r0.setmIShareStatusSink(r2)
            com.zipow.videobox.confapp.component.ZMConfComponentMgr r0 = com.zipow.videobox.confapp.component.ZMConfComponentMgr.getInstance()
            com.zipow.videobox.confapp.component.sink.video.IVideoStatusSink r2 = r7.mVideoStatusSink
            r0.setmVideoStatusSink(r2)
            r7.initUIOrientation()
            android.view.Window r0 = r7.getWindow()
            r2 = 6291584(0x600080, float:8.816387E-39)
            r0.addFlags(r2)
            r7.updateSystemStatusBar()
            int r0 = r7.getLayout()
            r7.setContentView(r0)
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.confView
            android.view.View r0 = r7.findViewById(r0)
            r7.mConfView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.waitingJoinView
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.WaitingJoinView r0 = (com.zipow.videobox.view.WaitingJoinView) r0
            r7.mWaitingJoinView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.verifyingMeetingId
            android.view.View r0 = r7.findViewById(r0)
            r7.mVerifyingMeetingIDView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.tipLayer
            android.view.View r0 = r7.findViewById(r0)
            us.zoom.androidlib.widget.ZMTipLayer r0 = (p021us.zoom.androidlib.widget.ZMTipLayer) r0
            r7.mTipLayer = r0
            android.view.View r0 = r7.mConfView
            int r2 = p021us.zoom.videomeetings.C4558R.C4560id.panelConnecting
            android.view.View r0 = r0.findViewById(r2)
            r7.mPanelConnecting = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.confToolbar
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.ConfToolbar r0 = (com.zipow.videobox.view.ConfToolbar) r0
            r7.mToolbar = r0
            java.lang.String r0 = "large_share_video_scene_mode"
            r2 = 0
            boolean r0 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r0, r2)
            if (r0 == 0) goto L_0x00b8
            com.zipow.videobox.view.ConfToolbar r0 = r7.mToolbar
            android.content.res.Resources r3 = r7.getResources()
            int r4 = p021us.zoom.videomeetings.C4558R.color.zm_sdk_conf_toolbar_bg
            int r3 = r3.getColor(r4)
            r0.setBackgroundColor(r3)
        L_0x00b8:
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelTop
            android.view.View r0 = r7.findViewById(r0)
            r7.mTopbar = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelTools
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.ConfToolsPanel r0 = (com.zipow.videobox.view.ConfToolsPanel) r0
            r7.mPanelTools = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.languageInterpretationPanel
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.ConfInterpretationSwitch r0 = (com.zipow.videobox.view.ConfInterpretationSwitch) r0
            r7.mInterpretationSwitch = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.showInterpretationLanguage
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.ConfInterpretationLanguage r0 = (com.zipow.videobox.view.ConfInterpretationLanguage) r0
            r7.mInterpretationLanguage = r0
            android.view.View r0 = r7.mConfView
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtMeetingNumber
            android.view.View r0 = r0.findViewById(r3)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtMeetingNumber = r0
            android.view.View r0 = r7.mConfView
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtTimer
            android.view.View r0 = r0.findViewById(r3)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtTimer = r0
            android.view.View r0 = r7.mConfView
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtCountdown
            android.view.View r0 = r0.findViewById(r3)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtCountdown = r0
            android.view.View r0 = r7.mConfView
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtPassword
            android.view.View r0 = r0.findViewById(r3)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtPassword = r0
            android.view.View r0 = r7.mConfView
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.btnBack
            android.view.View r0 = r0.findViewById(r3)
            android.widget.Button r0 = (android.widget.Button) r0
            r7.mBtnBack = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.imgAudioWatermark
            android.view.View r0 = r7.findViewById(r0)
            r7.mViewAudioWatermark = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelRecording
            android.view.View r0 = r7.findViewById(r0)
            r7.mPanelRecording = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelCurUserRecording
            android.view.View r0 = r7.findViewById(r0)
            r7.mPanelCurUserRecording = r0
            android.view.View r0 = r7.mPanelCurUserRecording
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.imgRecording
            android.view.View r0 = r0.findViewById(r3)
            android.widget.ImageView r0 = (android.widget.ImageView) r0
            r7.mImgRecording = r0
            android.view.View r0 = r7.mPanelCurUserRecording
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.progressStartingRecord
            android.view.View r0 = r0.findViewById(r3)
            r7.mProgressStartingRecord = r0
            android.view.View r0 = r7.mPanelCurUserRecording
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.imgRecordControlArrow
            android.view.View r0 = r0.findViewById(r3)
            android.widget.ImageView r0 = (android.widget.ImageView) r0
            r7.mImgRecordControlArrow = r0
            android.view.View r0 = r7.mPanelCurUserRecording
            int r3 = p021us.zoom.videomeetings.C4558R.C4560id.txtRecordStatus
            android.view.View r0 = r0.findViewById(r3)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtRecordStatus = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelStartingRecord
            android.view.View r0 = r7.findViewById(r0)
            r7.mPanelStartingRecord = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.txtStartingRecord
            android.view.View r0 = r7.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtStartingRecrod = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.callconnectingView
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.CallConnectingView r0 = (com.zipow.videobox.view.CallConnectingView) r0
            r7.mCallConnectingView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.onHoldView
            android.view.View r0 = r7.findViewById(r0)
            com.zipow.videobox.view.OnSilentView r0 = (com.zipow.videobox.view.OnSilentView) r0
            r7.mOnSilentView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.imgAudioSource
            android.view.View r0 = r7.findViewById(r0)
            android.widget.ImageView r0 = (android.widget.ImageView) r0
            r7.mImgAudioSource = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.btnSwitchToShare
            android.view.View r0 = r7.findViewById(r0)
            r7.mBtnSwitchToShare = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.rlQa
            android.view.View r0 = r7.findViewById(r0)
            r7.mQaView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.txtQAOpenNumber
            android.view.View r0 = r7.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtQAOpenNumber = r0
            com.zipow.videobox.confapp.meeting.confhelper.PollComponent r0 = new com.zipow.videobox.confapp.meeting.confhelper.PollComponent
            r0.<init>(r7)
            r7.mPollComponent = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelRejoinMsg
            android.view.View r0 = r7.findViewById(r0)
            r7.mPanelRejoinMsg = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.praticeModeView
            android.view.View r0 = r7.findViewById(r0)
            r7.mPracticeModeView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.broadcastingView
            android.view.View r0 = r7.findViewById(r0)
            r7.mBroadcastingView = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.btnBroadcast
            android.view.View r0 = r7.findViewById(r0)
            android.widget.Button r0 = (android.widget.Button) r0
            r7.mBtnBroadcast = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.progressBarBroadcasting
            android.view.View r0 = r7.findViewById(r0)
            android.widget.ProgressBar r0 = (android.widget.ProgressBar) r0
            r7.mProgressBarBroadcasting = r0
            com.zipow.videobox.confapp.meeting.confhelper.BOComponent r0 = new com.zipow.videobox.confapp.meeting.confhelper.BOComponent
            r0.<init>(r7)
            r7.mBOComponent = r0
            com.zipow.videobox.confapp.meeting.confhelper.KubiComponent r0 = new com.zipow.videobox.confapp.meeting.confhelper.KubiComponent
            r0.<init>(r7)
            r7.mKubiComponent = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.txtClosedCaption
            android.view.View r0 = r7.findViewById(r0)
            us.zoom.androidlib.widget.CaptionView r0 = (p021us.zoom.androidlib.widget.CaptionView) r0
            r7.mClosedCaptionView = r0
            us.zoom.androidlib.widget.CaptionView r0 = r7.mClosedCaptionView
            r3 = 8
            r0.setVisibility(r3)
            android.view.View r0 = r7.mPanelStartingRecord
            r0.setVisibility(r3)
            com.zipow.videobox.confapp.component.ZMConfComponentMgr r0 = com.zipow.videobox.confapp.component.ZMConfComponentMgr.getInstance()
            r0.onActivityCreate(r8)
            com.zipow.videobox.confapp.meeting.confhelper.LiveStreamComponent r0 = new com.zipow.videobox.confapp.meeting.confhelper.LiveStreamComponent
            android.view.View r3 = r7.mConfView
            r0.<init>(r7, r3)
            r7.mLiveStreamComponent = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.imgE2EIcon
            android.view.View r0 = r7.findViewById(r0)
            android.widget.ImageView r0 = (android.widget.ImageView) r0
            r7.mImgE2EMeetingView = r0
            r7.setPaddingsForTranslucentStatus()
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.panelAudioShare
            android.view.View r0 = r7.findViewById(r0)
            r7.mPanelAudioConnectStatus = r0
            int r0 = p021us.zoom.videomeetings.C4558R.C4560id.txtAudioShareInfo
            android.view.View r0 = r7.findViewById(r0)
            android.widget.TextView r0 = (android.widget.TextView) r0
            r7.mTxtAudioConnectStatus = r0
            if (r8 == 0) goto L_0x023a
            java.lang.String r0 = "closed_caption_content"
            java.lang.String r8 = r8.getString(r0)
            r7.mClosedCaptionContent = r8
        L_0x023a:
            r7.initTipLayer()
            r7.initPanelTools()
            r7.initPanelWaitingShare()
            r7.initPanelSharingTitle()
            r7.initPanelSwitchScene()
            r7.initPTListener()
            r7.initUIStatus()
            r7.initVideoSceneMgr()
            com.zipow.videobox.confapp.VideoUnit.initDefaultResources()
            com.zipow.videobox.confapp.ConfUI r8 = com.zipow.videobox.confapp.ConfUI.getInstance()
            r8.addListener(r7)
            com.zipow.videobox.confapp.ConfUI r8 = com.zipow.videobox.confapp.ConfUI.getInstance()
            r8.addIConfInnerEventListener(r7)
            com.zipow.videobox.confapp.ConfUI r8 = com.zipow.videobox.confapp.ConfUI.getInstance()
            com.zipow.videobox.confapp.ConfUI$IRoomSystemCallEventListener r0 = r7.mRoomSystemCallEventListener
            r8.addRoomCallStatusListener(r0)
            com.zipow.videobox.confapp.ConfUI r8 = com.zipow.videobox.confapp.ConfUI.getInstance()
            com.zipow.videobox.view.video.MeetingReactionMgr r0 = com.zipow.videobox.view.video.MeetingReactionMgr.getInstance()
            r8.addIEmojiReactionListener(r0)
            com.zipow.videobox.util.AppStateMonitor r8 = com.zipow.videobox.util.AppStateMonitor.getInstance()
            r8.addListener(r7)
            r7.registerQAListener()
            com.zipow.videobox.confapp.InterpretationSinkUI r8 = com.zipow.videobox.confapp.InterpretationSinkUI.getInstance()
            com.zipow.videobox.confapp.InterpretationSinkUI$IInterpretationSinkUIListener r0 = r7.mInterpretationSinkUIListener
            r8.addListener(r0)
            android.widget.Button r8 = r7.mBtnBack
            if (r8 == 0) goto L_0x0291
            r8.setOnClickListener(r7)
        L_0x0291:
            android.widget.ImageView r8 = r7.mImgAudioSource
            if (r8 == 0) goto L_0x029d
            p021us.zoom.androidlib.util.ViewPressEffectHelper.attach(r8)
            android.widget.ImageView r8 = r7.mImgAudioSource
            r8.setOnClickListener(r7)
        L_0x029d:
            android.view.View r8 = r7.mBtnSwitchToShare
            if (r8 == 0) goto L_0x02a4
            r8.setOnClickListener(r7)
        L_0x02a4:
            android.view.View r8 = r7.mQaView
            if (r8 == 0) goto L_0x02b1
            int r8 = p021us.zoom.videomeetings.C4558R.C4560id.btnQA
            android.view.View r8 = r7.findViewById(r8)
            r8.setOnClickListener(r7)
        L_0x02b1:
            android.widget.TextView r8 = r7.mTxtMeetingNumber
            if (r8 == 0) goto L_0x02bd
            com.zipow.videobox.ConfActivityNormal$8 r0 = new com.zipow.videobox.ConfActivityNormal$8
            r0.<init>()
            r8.setOnLongClickListener(r0)
        L_0x02bd:
            com.zipow.videobox.VideoBoxApplication r8 = com.zipow.videobox.VideoBoxApplication.getInstance()
            r8.setConfUIPreloaded(r2)
            com.zipow.videobox.confapp.ConfMgr r8 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            boolean r0 = r8.isConfConnected()
            if (r0 == 0) goto L_0x0307
            com.zipow.videobox.view.video.AbsVideoSceneMgr r0 = r7.mVideoSceneMgr
            if (r0 == 0) goto L_0x02d7
            com.zipow.videobox.view.video.AbsVideoSceneMgr r0 = r7.mVideoSceneMgr
            r0.onConfReady()
        L_0x02d7:
            boolean r0 = r7.checkStartDrivingModeOnConfReady()
            if (r0 != 0) goto L_0x0307
            com.zipow.videobox.confapp.CmmUser r0 = r8.getMyself()
            if (r0 == 0) goto L_0x02f8
            com.zipow.videobox.confapp.ConfAppProtos$CmmAudioStatus r0 = r0.getAudioStatusObj()
            if (r0 == 0) goto L_0x02f8
            r3 = 2
            long r5 = r0.getAudiotype()
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 != 0) goto L_0x02f8
            r7.showToolbar(r1, r2)
            r0 = 1
            goto L_0x02f9
        L_0x02f8:
            r0 = 0
        L_0x02f9:
            if (r0 != 0) goto L_0x0307
            int[] r8 = r8.getUnreadChatMessageIndexes()
            if (r8 == 0) goto L_0x0307
            int r8 = r8.length
            if (r8 <= 0) goto L_0x0307
            r7.showToolbar(r1, r2)
        L_0x0307:
            android.view.View r8 = r7.mConfView
            com.zipow.videobox.ConfActivityNormal$9 r0 = new com.zipow.videobox.ConfActivityNormal$9
            r0.<init>()
            r8.addOnLayoutChangeListener(r0)
            android.content.IntentFilter r8 = new android.content.IntentFilter
            r8.<init>()
            java.lang.String r0 = "android.intent.action.LOCALE_CHANGED"
            r8.addAction(r0)
            android.content.BroadcastReceiver r0 = r7.mBroadcastReceiver
            r7.registerReceiver(r0, r8)
            com.zipow.videobox.BitmapCacheMgr r8 = com.zipow.videobox.BitmapCacheMgr.getInstance()
            r8.init()
            return
        L_0x0328:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ConfActivityNormal.onCreate(android.os.Bundle):void");
    }

    public AbsVideoSceneMgr getVideoSceneMgr() {
        return this.mVideoSceneMgr;
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoControlChanged(long j) {
        super.attendeeVideoControlChanged(j);
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.attendeeVideoControlChange(j);
        }
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoLayoutChanged(long j) {
        super.attendeeVideoLayoutChanged(j);
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.attendeeVideoLayoutChange(j);
        }
    }

    /* access modifiers changed from: protected */
    public void attendeeVideoLayoutFlagChanged(long j) {
        super.attendeeVideoLayoutChanged(j);
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.attendeeVideoLayoutFlagChange(j);
        }
    }

    private void setPaddingsForTranslucentStatus() {
        if (isImmersedModeEnabled() && this.mConfView != null) {
            int i = 0;
            if (UIUtil.isPortraitMode(this)) {
                i = UIUtil.getStatusBarHeight(this);
            }
            int dip2px = UIUtil.dip2px(this, 3.0f);
            int dip2px2 = i + UIUtil.dip2px(this, 5.0f);
            int dip2px3 = UIUtil.dip2px(this, 5.0f);
            this.mConfView.findViewById(C4558R.C4560id.titleBar).setPadding(dip2px, dip2px2, dip2px, dip2px3);
            this.mPanelConnecting.setPadding(dip2px, dip2px2, dip2px, dip2px3);
            this.mVerifyingMeetingIDView.findViewById(C4558R.C4560id.panelConnecting).setPadding(dip2px, dip2px2, dip2px, dip2px3);
            OnSilentView onSilentView = this.mOnSilentView;
            if (onSilentView != null) {
                onSilentView.setTitlePadding(dip2px, dip2px2, dip2px, dip2px3);
            }
            WaitingJoinView waitingJoinView = this.mWaitingJoinView;
            if (waitingJoinView != null) {
                waitingJoinView.setTitlePadding(dip2px, dip2px2, dip2px, dip2px3);
            }
            ZMConfComponentMgr.getInstance().setPaddingForTranslucentStatus(dip2px, dip2px2, dip2px, dip2px3);
        }
    }

    private void initUIOrientation() {
        if (UIMgr.isLargeMode(this)) {
            setRequestedOrientation(0);
        }
    }

    private void initTipLayer() {
        this.mTipLayer.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
                boolean access$1500 = ConfActivityNormal.this.isInToolbarRect(motionEvent.getX(), motionEvent.getY());
                boolean dismissTempTips = ConfActivityNormal.this.dismissTempTips();
                boolean access$1600 = ConfActivityNormal.this.hideToolbarMenus();
                if (access$1500) {
                    return access$1600;
                }
                return dismissTempTips || access$1600;
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isInToolbarRect(float f, float f2) {
        boolean z = false;
        if (!isToolbarShowing()) {
            return false;
        }
        int top = this.mPanelTools.getTop();
        int bottom = this.mPanelTools.getBottom();
        int left = this.mPanelTools.getLeft();
        int right = this.mPanelTools.getRight();
        if (f >= ((float) left) && f <= ((float) right) && f2 >= ((float) top) && f2 <= ((float) bottom)) {
            z = true;
        }
        return z;
    }

    private void initToolbar() {
        this.mToolbar.setListener(this);
        this.mToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            int lastToolbarTop = 0;
            int lastToolbarWidth = 0;

            public void onGlobalLayout() {
                int top = ConfActivityNormal.this.mToolbar.getTop();
                int width = ConfActivityNormal.this.mToolbar.getWidth();
                if (top != this.lastToolbarTop || width != this.lastToolbarWidth) {
                    this.lastToolbarTop = top;
                    this.lastToolbarWidth = width;
                    ConfActivityNormal.this.mTipLayer.requestLayout();
                }
            }
        });
    }

    private void initPanelWaitingShare() {
        View findViewById = findViewById(C4558R.C4560id.panelWaitingShare);
        findViewById.setVisibility(8);
        findViewById.setOnClickListener(this);
    }

    private void initPanelSharingTitle() {
        View findViewById = findViewById(C4558R.C4560id.panelSharingTitle);
        if (findViewById != null) {
            findViewById.setVisibility(8);
        }
    }

    private void initPanelTools() {
        if (!UIMgr.isLargeMode(this)) {
            View findViewById = this.mPanelTools.findViewById(C4558R.C4560id.panelTop);
            showTopToolbar(false);
            findViewById.setOnClickListener(this);
            this.mBtnLeave = (Button) findViewById.findViewById(C4558R.C4560id.btnLeave);
            this.mBtnLeave.setOnClickListener(this);
        }
        this.mPanelTools.setListener(this);
        initToolbar();
    }

    private void initPanelSwitchScene() {
        if (!UIMgr.isLargeMode(this)) {
            findViewById(C4558R.C4560id.panelSwitchScene).setVisibility(8);
            this.mPanelSwitchSceneButtons = findViewById(C4558R.C4560id.panelSwitchSceneButtons);
            this.mPanelSwitchSceneButtons.setVisibility(0);
        }
    }

    private void initPTListener() {
        this.mPTUIListener = new SimplePTUIListener() {
            public void onPTAppEvent(int i, long j) {
                if (i != 8) {
                    if (i != 14) {
                        switch (i) {
                            case 0:
                                break;
                            case 1:
                                break;
                            default:
                                return;
                        }
                    }
                    ConfActivityNormal.this.onIMLogout();
                    return;
                }
                ConfActivityNormal.this.onIMLogin(j);
            }

            public void onPTAppCustomEvent(int i, long j) {
                if (i == 1) {
                    ConfLocalHelper.handleCallError(ConfActivityNormal.this, j);
                }
            }
        };
        this.mPTCommonEventListener = new IPTCommonEventListener() {
            public void onPTCommonEvent(int i, byte[] bArr) {
                switch (i) {
                    case 1:
                    case 2:
                        ConfActivityNormal.this.sinkCallInvitationStatus(i, bArr);
                        return;
                    default:
                        return;
                }
            }
        };
        PTUIDelegation.getInstance().addPTUIListener(this.mPTUIListener);
        PTUIDelegation.getInstance().addPTCommonEventListener(this.mPTCommonEventListener);
    }

    /* access modifiers changed from: private */
    public void sinkCallInvitationStatus(final int i, byte[] bArr) {
        try {
            final InvitationItem parseFrom = InvitationItem.parseFrom(bArr);
            getNonNullEventTaskManagerOrThrowException().pushLater("sinkCallInvitationStatus", new EventAction() {
                public void run(IUIElement iUIElement) {
                    switch (i) {
                        case 1:
                            ConfActivityNormal.this.onCallAccepted(parseFrom);
                            return;
                        case 2:
                            ConfActivityNormal.this.onCallDeclined(parseFrom);
                            return;
                        default:
                            return;
                    }
                }
            });
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void onCallDeclined(InvitationItem invitationItem) {
        String fromUserScreenName = invitationItem.getFromUserScreenName();
        if (!StringUtil.isEmptyOrNull(fromUserScreenName)) {
            ToastTip.showNow(getSupportFragmentManager(), getString(C4558R.string.zm_msg_xxx_did_not_answer_93541, new Object[]{fromUserScreenName}), 3000, isInDriveMode());
        }
    }

    /* access modifiers changed from: private */
    public void onIMLogout() {
        refreshBtnBack();
    }

    /* access modifiers changed from: private */
    public void onIMLogin(long j) {
        refreshBtnBack();
    }

    private void uninitPTListener() {
        if (this.mPTUIListener != null) {
            PTUIDelegation.getInstance().removePTUIListener(this.mPTUIListener);
        }
        if (this.mPTCommonEventListener != null) {
            PTUIDelegation.getInstance().removePTCommonEventListener(this.mPTCommonEventListener);
        }
    }

    private void initVideoSceneMgr() {
        RetainedFragment retainedFragment = getRetainedFragment();
        if (retainedFragment != null) {
            this.mVideoSceneMgr = retainedFragment.restoreVideoSceneMgr();
        }
        if (retainedFragment != null && this.mVideoSceneMgr == null) {
            if (UIMgr.isLargeMode(this)) {
                this.mVideoSceneMgr = new VideoSceneMgrLarge(VideoBoxApplication.getInstance());
            } else {
                this.mVideoSceneMgr = new VideoSceneMgr(VideoBoxApplication.getInstance());
            }
            retainedFragment.saveVideoSceneMgr(this.mVideoSceneMgr);
        }
        if (this.mVideoSceneMgr != null) {
            ZMConfComponentMgr.getInstance().initVideoSceneMgr(this.mVideoSceneMgr);
        }
        ZMConfComponentMgr.getInstance().setVideoSceneMgr(this.mVideoSceneMgr);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 82) {
            return super.onKeyUp(i, keyEvent);
        }
        switchToolbar();
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        BitmapCacheMgr.getInstance().uninit();
        unregisterReceiver(this.mBroadcastReceiver);
        if (VideoBoxApplication.getInstance() != null) {
            ZMConfComponentMgr.getInstance().setmVideoStatusSink(null);
            ZMConfComponentMgr.getInstance().setmIShareStatusSink(null);
            ZMConfComponentMgr.getInstance().unRegisterAllComponents();
            this.mHandler.removeCallbacks(this.mAttendeeRaiseLowerHandRunnalbe);
            this.mHandler.removeCallbacks(this.mClosedCaptionTimeoutRunnable);
            this.mHandler.removeCallbacks(this.mUnmuteMyselfRunnable);
            ConfUI.getInstance().removeIConfInnerEventListener(this);
            ConfUI.getInstance().removeListener(this);
            ConfUI.getInstance().removeRoomCallStatusListener(this.mRoomSystemCallEventListener);
            ConfUI.getInstance().removeIEmojiReactionListener(MeetingReactionMgr.getInstance());
            InterpretationSinkUI.getInstance().removeListener(this.mInterpretationSinkUIListener);
            this.mOptimizeUserEventHandler.removeCallbacksAndMessages(null);
            this.mUserInfoEventCaches.clear();
            uninitPTListener();
            BOComponent bOComponent = this.mBOComponent;
            if (bOComponent != null) {
                bOComponent.onDestroy();
            }
            PollComponent pollComponent = this.mPollComponent;
            if (pollComponent != null) {
                pollComponent.onDestroy();
            }
            unregisterQAListener();
            KubiComponent kubiComponent = this.mKubiComponent;
            if (kubiComponent != null) {
                kubiComponent.onDestroy();
            }
            if (ConfUI.getInstance().isLeavingConference()) {
                VideoBoxApplication.getInstance().setConfUIPreloaded(false);
                VideoBoxApplication.getInstance().clearConfAppContext();
            }
            stopPlayDuduVoice();
            Runnable runnable = g_hideToolbarRunnable;
            if (runnable != null) {
                g_handler.removeCallbacks(runnable);
            }
            AppStateMonitor.getInstance().removeListener(this);
            dismissVerifyHostKeyDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (!StringUtil.isEmptyOrNull(this.mClosedCaptionContent)) {
            bundle.putString(PREF_CLOSED_CAPTION_CONTENT, this.mClosedCaptionContent);
        }
        ZMConfComponentMgr.getInstance().onSaveInstanceState(bundle);
    }

    public void onAppActivated() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setMobileAppActiveStatus(true);
        }
    }

    public void onAppInactivated() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setMobileAppActiveStatus(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (!UiModeUtil.isInDesktopMode(this) && !isInMultiWindowMode()) {
            ZMConfComponentMgr.getInstance().sinkInStopVideo();
        }
        ZMConfComponentMgr.getInstance().onActivityPause();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        checkShowTimer();
        if (this.mVideoSceneMgr == null || !this.mVideoSceneMgr.isInDriveModeScence()) {
            UIMgr.setGlobalDriverModeEnabled(PreferenceUtil.readBooleanValue(PreferenceUtil.DRIVE_MODE_ENABLED, true));
        }
        if (UiModeUtil.isInDesktopMode(this) || isInMultiWindowMode()) {
            ZMConfComponentMgr.getInstance().sinkInResumeVideo();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (UiModeUtil.isInDesktopMode(this) || isInMultiWindowMode()) {
            ZMConfComponentMgr.getInstance().sinkInStopVideo();
        }
        this.mHandler.removeCallbacks(this.mTimerRunnable);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        initUIStatus(true);
        refreshToolbar();
        refreshBtnBack();
        refreshPanelRecording();
        refreshAudioWatermark();
        BOUtil.checkBOStatus();
        BOComponent bOComponent = this.mBOComponent;
        if (bOComponent != null) {
            bOComponent.updateBOButton();
        }
        checkClosedCaption();
        this.mLiveStreamComponent.onLiveStreamStatusChange();
        int pTAskToLeaveReason = ConfUI.getInstance().getPTAskToLeaveReason();
        if (pTAskToLeaveReason >= 0) {
            onPTAskToLeave(pTAskToLeaveReason);
            ConfUI.getInstance().clearPTAskToLeaveReason();
        }
        updateQAButton();
        PollComponent pollComponent = this.mPollComponent;
        if (pollComponent != null) {
            pollComponent.updatePollButton();
        }
        ZMConfComponentMgr.getInstance().sinkInResumeVideo();
        if (BOUtil.isLaunchReasonByBO()) {
            overridePendingTransition(0, 0);
        }
        updatePracticeModeView();
        onAttendeeLeft();
    }

    private void registerQAListener() {
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onRefreshQAUI() {
                    ConfActivityNormal.this.updateQAButton();
                }

                public void onReceiveQuestion(String str) {
                    ConfActivityNormal.this.updateQAButton();
                    ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                    if (qAComponent == null) {
                        return;
                    }
                    if (qAComponent.isWebinarHost() || qAComponent.isWebinarPanelist()) {
                        ConfActivityNormal.this.showToolbar(true, false);
                        ConfActivityNormal.this.disableToolbarAutoHide();
                    }
                }

                public void onReceiveAnswer(String str) {
                    ConfActivityNormal.this.updateQAButton();
                    if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                        ConfActivityNormal.this.updateAttendeeQAButton();
                        ConfActivityNormal.this.showToolbar(true, false);
                        ConfActivityNormal.this.disableToolbarAutoHide();
                    }
                }

                public void onQuestionMarkedAsAnswered(String str) {
                    ConfActivityNormal.this.updateQAButton();
                }

                public void onWebinarAttendeeRaisedHand(long j) {
                    if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                        ConfActivityNormal.this.updateAttendeeRaiseHandButton();
                    }
                    ConfActivityNormal.this.handleAttendeeRaiseLowerHand(j);
                }

                public void onWebinarAttendeeLowerHand(long j) {
                    if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                        ConfActivityNormal.this.updateAttendeeRaiseHandButton();
                    }
                    ConfActivityNormal.this.handleAttendeeRaiseLowerHand(j);
                }

                public void onUserRemoved(String str) {
                    if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                        ConfActivityNormal.this.updateAttendeeRaiseHandButton();
                    }
                    ConfActivityNormal.this.handleAttendeeLeft();
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
    }

    private void unregisterQAListener() {
        if (this.mQAUIListener != null) {
            ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        }
    }

    /* access modifiers changed from: private */
    public void updateQAButton() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null) {
            View view = this.mQaView;
            if (view != null) {
                view.setVisibility(8);
            }
            return;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (confContext.isQANDAOFF() || (!qAComponent.isWebinarHost() && !qAComponent.isWebinarPanelist())) {
                this.mTxtQAOpenNumber.setVisibility(8);
            } else {
                int openQuestionCount = qAComponent.getOpenQuestionCount();
                if (openQuestionCount <= 0) {
                    this.mTxtQAOpenNumber.setVisibility(8);
                } else {
                    this.mTxtQAOpenNumber.setVisibility(0);
                    this.mTxtQAOpenNumber.setText(openQuestionCount < 100 ? String.valueOf(openQuestionCount) : "99+");
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateAttendeeQAButton() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (confContext != null && qAComponent != null && this.mToolbar != null) {
            if (!confContext.isQANDAOFF()) {
                this.mToolbar.setQANoteMsgButton(qAComponent.getAnsweredQuestionCount());
            } else {
                this.mToolbar.setQANoteMsgButton(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateAttendeeRaiseHandButton() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent != null && qAComponent.getMyJID() != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj == null || confStatusObj.isAllowRaiseHand()) {
                if (isWebinarAttendeeRaiseHand(qAComponent.getMyJID())) {
                    this.mToolbar.showLowerHand();
                } else {
                    this.mToolbar.showRaiseHand();
                }
            }
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateSystemStatusBar();
    }

    @SuppressLint({"InlinedApi"})
    public void onAudioSourceTypeChanged(int i) {
        if (isActive()) {
            if (this.mVideoSceneMgr != null) {
                this.mVideoSceneMgr.updateVisibleScenes();
            }
            refreshAudioSourceBtn();
            AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
            if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                ImageView imageView = this.mImgAudioSource;
                if (imageView != null) {
                    imageView.sendAccessibilityEvent(32768);
                }
            }
            CallConnectingView callConnectingView = this.mCallConnectingView;
            if (callConnectingView != null && callConnectingView.getVisibility() == 0) {
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null) {
                    this.mCallConnectingView.updateUIForCallType(ConfLocalHelper.getZoomConfType(confContext));
                }
            }
        }
    }

    private void refreshBtnBack() {
        boolean isWebSignedOn = PTAppDelegation.getInstance().isWebSignedOn();
        Button button = this.mBtnBack;
        if (button != null) {
            button.setVisibility(isWebSignedOn ? 0 : 8);
        }
    }

    public boolean isToolbarShowing() {
        return this.mPanelTools.isVisible();
    }

    public int getToolbarHeight() {
        if (isToolbarShowing()) {
            return this.mToolbarHeight;
        }
        return 0;
    }

    public int getTopBarHeight() {
        if (isTopbarShowing()) {
            return this.mTopbarHeight;
        }
        return 0;
    }

    public void showToolbar(boolean z, boolean z2) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (!ConfMgr.getInstance().isConfConnected() || isInDriveMode() || ConfLocalHelper.isDirectShareClient()) {
            z = false;
            z2 = false;
        } else {
            if (ConfShareLocalHelper.isSharingOut() && !ScreenShareMgr.getInstance().isSharing()) {
                z = false;
                z2 = false;
            }
            if (confContext != null && confContext.isAlwaysShowMeetingToolbar()) {
                z = true;
            }
        }
        refreshBtnBack();
        if (confContext != null) {
            this.mPanelTools.setConfNumber(confContext.getConfNumber());
        }
        if ((!ConfShareLocalHelper.isSharingOut() || z || ScreenShareMgr.getInstance().isSharing()) && !ZMConfComponentMgr.getInstance().isMbEditStatus()) {
            this.mPanelTools.showToolbar(z, z2);
            if (this.mConfParams.isTitleBarDisabled()) {
                showTitlebar(false);
            }
            if (this.mConfParams.isBottomBarDisabled() || ZMConfComponentMgr.getInstance().isMbEditStatus()) {
                this.mPanelTools.showToolbar(false, false);
            }
        }
    }

    public void disableToolbarAutoHide() {
        Runnable runnable = g_hideToolbarRunnable;
        if (runnable != null) {
            g_handler.removeCallbacks(runnable);
        }
    }

    public void hideToolbarDefaultDelayed() {
        hideToolbarDelayed(5000);
    }

    public void hideToolbarDelayed(final long j) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
        if ((accessibilityManager == null || !accessibilityManager.isEnabled()) && !ConfShareLocalHelper.isSharingOut() && !canShowBroadcastButton()) {
            Runnable runnable = g_hideToolbarRunnable;
            if (runnable != null) {
                g_handler.removeCallbacks(runnable);
            }
            g_hideToolbarRunnable = new Runnable() {
                public void run() {
                    if (!ConfActivityNormal.this.isActive()) {
                        ConfActivityNormal.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("hideToolbarDelayed") {
                            public void run(@NonNull IUIElement iUIElement) {
                                ((ConfActivityNormal) iUIElement).hideToolbarDelayed(j);
                            }
                        });
                    } else if (!ConfActivityNormal.this.hasTipPointToToolbar()) {
                        ConfActivityNormal.this.showToolbar(false, true);
                        ConfActivityNormal.g_handler.removeCallbacks(ConfActivityNormal.g_hideToolbarRunnable);
                        ConfActivityNormal.g_hideToolbarRunnable = null;
                    }
                }
            };
            g_handler.postDelayed(g_hideToolbarRunnable, j);
        }
    }

    public void updateSystemStatusBar() {
        if (UIUtil.isLandscapeMode(getApplicationContext())) {
            getWindow().setFlags(1024, 1024);
        } else {
            getWindow().setFlags(-1025, 1024);
        }
        setPaddingsForTranslucentStatus();
    }

    public void updateTitleBar() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (this.mConfParams.isMeetingIdTextDisabled()) {
                this.mTxtMeetingNumber.setVisibility(8);
            } else {
                this.mTxtMeetingNumber.setVisibility(0);
                if (BOUtil.isInBOMeeting()) {
                    this.mTxtMeetingNumber.setText(BOUtil.getMyBOMeetingName(2));
                } else if (StringUtil.isEmptyOrNull(this.mConfParams.getCustomMeetingId())) {
                    this.mTxtMeetingNumber.setText(getResources().getString(UIUtil.isLargeScreen(this) ? C4558R.string.zm_title_conf_long : C4558R.string.zm_title_conf, new Object[]{StringUtil.formatConfNumber(confContext.getConfNumber())}));
                    this.mTxtMeetingNumber.setContentDescription(getString(C4558R.string.zm_title_conf_long, new Object[]{String.valueOf(confContext.getConfNumber())}));
                } else {
                    this.mTxtMeetingNumber.setText(this.mConfParams.getCustomMeetingId());
                    this.mTxtMeetingNumber.setContentDescription(getString(C4558R.string.zm_title_conf_long, new Object[]{this.mConfParams.getCustomMeetingId()}));
                }
            }
            if (this.mImgE2EMeetingView != null) {
                if (confContext.isE2EMeeting()) {
                    this.mImgE2EMeetingView.setVisibility(0);
                } else {
                    this.mImgE2EMeetingView.setVisibility(8);
                }
            }
            String rawMeetingPassword = confContext.getRawMeetingPassword();
            if (StringUtil.isEmptyOrNull(rawMeetingPassword) || BOUtil.isInBOMeeting() || this.mConfParams.isPasswordTextDisabled()) {
                this.mTxtPassword.setVisibility(8);
                return;
            }
            this.mTxtPassword.setText(getResources().getString(C4558R.string.zm_lbl_password_xxx, new Object[]{rawMeetingPassword}));
            this.mTxtPassword.setVisibility(0);
        }
    }

    private boolean isTopbarShowing() {
        return this.mPanelTools.isVisible() && this.mTopbar.getVisibility() == 0;
    }

    public boolean isBottombarShowing() {
        return this.mPanelTools.isVisible() && this.mToolbar.getVisibility() == 0;
    }

    public void refreshUnreadChatCount() {
        ConfMgr instance = ConfMgr.getInstance();
        if (this.mToolbar != null) {
            int[] unreadChatMessageIndexes = instance.getUnreadChatMessageIndexes();
            this.mToolbar.setChatsButton(unreadChatMessageIndexes != null ? unreadChatMessageIndexes.length : 0);
        }
        PListFragment pListFragment = PListFragment.getPListFragment(getSupportFragmentManager());
        if (pListFragment != null) {
            pListFragment.refresh(false);
        }
    }

    private void startPlayDuduVoice() {
        if (this.mDuduVoiceClip == null) {
            this.mDuduVoiceClip = new AudioClip(C4558R.raw.zm_dudu, VoiceEnginContext.getSelectedPlayerStreamType());
            this.mDuduVoiceClip.startPlay();
            ConfUI.getInstance().checkOpenLoudSpeaker();
        }
    }

    private void stopPlayDuduVoice() {
        AudioClip audioClip = this.mDuduVoiceClip;
        if (audioClip != null) {
            audioClip.stopPlay();
            this.mDuduVoiceClip = null;
        }
    }

    private void initUIStatus() {
        initUIStatus(false);
    }

    private void initUIStatus(boolean z) {
        NotificationMgr.showConfNotification();
        if (ConfMgr.getInstance().isConfConnected()) {
            this.mPanelConnecting.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mOnSilentView.setVisibility(8);
            this.mWaitingJoinView.setVisibility(8);
            this.mPanelRejoinMsg.setVisibility(8);
            if (isCallingOut()) {
                switchViewTo(ZMConfEnumViewMode.CALL_CONNECTING_VIEW);
                return;
            } else if (!ZMConfComponentMgr.getInstance().dispatchModeViewSwitch()) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                    if (audioStatusObj != null) {
                        this.mToolbar.setAudioMuted(audioStatusObj.getIsMuted());
                    }
                    this.mToolbar.setVideoMuted(true ^ ConfMgr.getInstance().getConfDataHelper().ismIsVideoStarted());
                    ZMConfComponentMgr.getInstance().refreshSwitchCameraButton();
                    switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            this.mPanelConnecting.setVisibility(0);
            ConfUI instance = ConfUI.getInstance();
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                boolean isDirectStart = confContext.isDirectStart();
                int launchReason = confContext.getLaunchReason();
                TextView textView = (TextView) this.mPanelRejoinMsg.findViewById(C4558R.C4560id.txtRejoinMsgTitle);
                TextView textView2 = (TextView) this.mPanelRejoinMsg.findViewById(C4558R.C4560id.txtRejoinMsgMessage);
                if (launchReason == 7) {
                    this.mPanelRejoinMsg.setVisibility(0);
                    this.mPanelConnecting.setVisibility(8);
                    View findViewById = this.mVerifyingMeetingIDView.findViewById(C4558R.C4560id.panelConnecting);
                    if (findViewById != null) {
                        findViewById.setVisibility(8);
                    }
                    textView.setText(C4558R.string.zm_webinar_msg_host_change_you_to_panelist);
                    textView2.setText(C4558R.string.zm_webinar_msg_connecting_as_panelist);
                } else if (launchReason == 8) {
                    this.mPanelRejoinMsg.setVisibility(0);
                    this.mPanelConnecting.setVisibility(8);
                    View findViewById2 = this.mVerifyingMeetingIDView.findViewById(C4558R.C4560id.panelConnecting);
                    if (findViewById2 != null) {
                        findViewById2.setVisibility(8);
                    }
                    textView.setText(C4558R.string.zm_webinar_msg_host_change_you_to_attendee);
                    textView2.setText(C4558R.string.zm_webinar_msg_connecting_as_attendee);
                } else {
                    BOComponent bOComponent = this.mBOComponent;
                    if (bOComponent != null) {
                        bOComponent.processLaunchConfReason(confContext, launchReason);
                    }
                    this.mPanelRejoinMsg.setVisibility(8);
                }
                if (isDirectStart || !(launchReason == 2 || launchReason == 4 || launchReason == 3)) {
                    if (instance.isLaunchConfParamReady()) {
                        int confStatus = ConfMgr.getInstance().getConfStatus();
                        if (confContext.isCall() && launchReason == 1) {
                            this.mPanelConnecting.setVisibility(8);
                            switchViewTo(ZMConfEnumViewMode.CALL_CONNECTING_VIEW);
                        } else if (confStatus == 8 || confStatus == 9) {
                            switchViewTo(ZMConfEnumViewMode.WAITING_JOIN_VIEW);
                        } else if (launchReason == 1) {
                            if (confContext.getConfNumber() <= 0) {
                                switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                            } else {
                                switchViewTo(ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW);
                            }
                        } else if (ConfUI.getInstance().isMeetingInfoReady()) {
                            switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                        } else {
                            switchViewTo(ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW);
                        }
                    } else {
                        switchViewTo(ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW);
                    }
                } else if (instance.isLaunchConfParamReady()) {
                    this.mCallConnectingView.setVisibility(8);
                    int confStatus2 = ConfMgr.getInstance().getConfStatus();
                    if (!confContext.isCall() && (confStatus2 == 3 || confStatus2 == 4 || confStatus2 == 5)) {
                        switchViewTo(ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW);
                    } else if (confStatus2 == 8 || confStatus2 == 9) {
                        switchViewToWaitingJoinView();
                    } else {
                        switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                    }
                } else {
                    switchViewTo(ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW);
                }
            } else {
                return;
            }
        }
        if (!z) {
            showToolbar(false, false);
        }
    }

    public void switchViewTo(ZMConfEnumViewMode zMConfEnumViewMode) {
        if (zMConfEnumViewMode == ZMConfEnumViewMode.CONF_VIEW) {
            this.mOnSilentView.updateData();
            ZMAlertDialog zMAlertDialog = this.mSignIntoJoinTip;
            if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                this.mSignIntoJoinTip.dismiss();
            }
            ZMAlertDialog zMAlertDialog2 = this.mGuestJoinTip;
            if (zMAlertDialog2 != null && zMAlertDialog2.isShowing()) {
                this.mGuestJoinTip.dismiss();
            }
            if (isSensorOrientationEnabled() && ConfMgr.getInstance().isConfConnected()) {
                setRequestedOrientation(4);
            }
            this.mVerifyingMeetingIDView.setVisibility(8);
            this.mWaitingJoinView.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mOnSilentView.setVisibility(8);
            getWindow().getDecorView().setBackgroundColor(-16777216);
            this.mToolbar.refreshBtnVideo();
            refreshInterpretation();
            this.mConfView.setVisibility(0);
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.VERIFYING_MEETING_ID_VIEW) {
            this.mConfView.setVisibility(8);
            this.mWaitingJoinView.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mOnSilentView.setVisibility(8);
            this.mVerifyingMeetingIDView.setVisibility(0);
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.WAITING_JOIN_VIEW) {
            this.mWaitingJoinView.setCustomMeetingId(this.mConfParams.getCustomMeetingId());
            this.mWaitingJoinView.updateData();
            if (isSensorOrientationEnabled()) {
                setRequestedOrientation(4);
            }
            this.mConfView.setVisibility(8);
            this.mVerifyingMeetingIDView.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mOnSilentView.setVisibility(8);
            this.mWaitingJoinView.setVisibility(0);
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.CALL_CONNECTING_VIEW) {
            if (UIUtil.getDisplayMinWidthInDip(this) < 500.0f && isSensorOrientationEnabled()) {
                setRequestedOrientation(1);
            }
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                int zoomConfType = ConfLocalHelper.getZoomConfType(confContext);
                if (zoomConfType == 1 || zoomConfType == 3) {
                    this.mConfView.setVisibility(0);
                } else {
                    this.mConfView.setVisibility(8);
                }
                this.mVerifyingMeetingIDView.setVisibility(8);
                this.mWaitingJoinView.setVisibility(8);
                this.mOnSilentView.setVisibility(8);
                startPlayDuduVoice();
                this.mCallConnectingView.updateUIForCallType(zoomConfType);
                this.mCallConnectingView.setVisibility(0);
            } else {
                return;
            }
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.SILENT_VIEW) {
            RecordingReminderDialog recordingReminderDialog = this.mReminderRecordingTip;
            if (recordingReminderDialog != null && recordingReminderDialog.isShowing()) {
                this.mReminderRecordingTip.dismiss();
            }
            if (isSensorOrientationEnabled()) {
                setRequestedOrientation(4);
            }
            this.mWaitingJoinView.setVisibility(8);
            this.mConfView.setVisibility(8);
            this.mVerifyingMeetingIDView.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mTipLayer.dismissAllTips();
            this.mOnSilentView.updateData();
            CmmConfContext confContext2 = ConfMgr.getInstance().getConfContext();
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confContext2 != null && confContext2.needRemindLoginWhenInWaitingRoom() && confStatusObj != null && !confStatusObj.isVerifyingMyGuestRole()) {
                if (this.mSignIntoJoinTip == null) {
                    this.mSignIntoJoinTip = new Builder(this).setMessage(C4558R.string.zm_alert_sign_in_to_join_content_87408).setTitle(C4558R.string.zm_alert_sign_in_to_join_title_87408).setCancelable(false).setNegativeButton(C4558R.string.zm_btn_not_now_87408, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).setPositiveButton(C4558R.string.zm_btn_login, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.LOG_FORCE_SIGN_IN.ordinal(), 47);
                            ConfActivityNormal.this.mOnSilentView.hideWaitingRoomSignInBtn();
                            ConfMgr.getInstance().loginWhenInWaitingRoom();
                        }
                    }).create();
                }
                if (!this.mSignIntoJoinTip.isShowing()) {
                    this.mSignIntoJoinTip.show();
                }
            }
            this.mOnSilentView.setVisibility(0);
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.PRESENT_ROOM_LAYER) {
            this.mWaitingJoinView.setVisibility(8);
            this.mConfView.setVisibility(8);
            this.mVerifyingMeetingIDView.setVisibility(8);
            this.mCallConnectingView.setVisibility(8);
            this.mOnSilentView.setVisibility(8);
        }
        ZMConfComponentMgr.getInstance().onModeViewChanged(zMConfEnumViewMode);
    }

    /* access modifiers changed from: private */
    public void showCopyLink() {
        getNonNullEventTaskManagerOrThrowException().pushLater("showCopyLink", new EventAction("showCopyLink") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).refreshCopyLink();
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshCopyLink() {
        if (MeetingInvitationUtil.copyInviteURL(this)) {
            ToastTip.showNow(getSupportFragmentManager(), getString(C4558R.string.zm_lbl_turn_on_auto_copy_meeting_link_topic_64735), 4000, isInDriveMode());
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
                AccessibilityUtil.announceForAccessibilityCompat(getWindow().getDecorView(), C4558R.string.zm_lbl_turn_on_auto_copy_meeting_link_topic_64735);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshInterpretation() {
        getNonNullEventTaskManagerOrThrowException().pushLater("onInterpretationChange", new EventAction("onInterpretationChange") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onInterpretationChange();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onInterpretationChange() {
        if (isActive()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if ((confContext != null && confContext.inSilentMode()) || ConfLocalHelper.isDirectShareClient()) {
                return;
            }
            if (!ConfLocalHelper.isUseAudioVOIP()) {
                this.mInterpretationSwitch.setVisibility(8);
                this.mInterpretationLanguage.setVisibility(8);
                InterpretationTip.dismiss(getSupportFragmentManager());
                return;
            }
            this.mInterpretationSwitch.refresh();
            this.mInterpretationLanguage.refresh();
            if (InterpretationTip.canShowTip(getSupportFragmentManager())) {
                showToolbar(true, false);
                InterpretationTip.show(getSupportFragmentManager(), C4558R.C4560id.btnMore);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConfReady(long j) {
        ConfMgr.getInstance().initInterpretation();
        if (!isCallingOut()) {
            BOComponent bOComponent = this.mBOComponent;
            if (bOComponent != null) {
                bOComponent.hideBOStatusChangeUI();
            }
            if (!ZMConfComponentMgr.getInstance().dispatchModeViewSwitch()) {
                switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                this.mPanelConnecting.setVisibility(8);
                this.mPanelRejoinMsg.setVisibility(8);
                refreshToolbar();
                if (this.mVideoSceneMgr != null) {
                    this.mVideoSceneMgr.onConfReady();
                }
                if (!showConfReadyTips(checkStartDrivingModeOnConfReady())) {
                    hideToolbarDelayed(5000);
                }
                this.mTxtCountdown.setVisibility(8);
                checkShowTimer();
                ZMConfComponentMgr.getInstance().onConfReady();
                if (isShowCopyTips()) {
                    showCopyLink();
                }
            }
        }
    }

    private boolean isShowCopyTips() {
        boolean z = ZMPolicyUIHelper.getAutoCopyLink() && ConfMgr.getInstance().getConfContext() != null && ConfMgr.getInstance().getConfContext().getOrginalHost();
        boolean z2 = ConfMgr.getInstance().getBOMgr() != null && ConfMgr.getInstance().getBOMgr().isInBOMeeting();
        boolean z3 = ConfMgr.getInstance().getConfContext() != null && ConfMgr.getInstance().getConfContext().getLaunchReason() == 11;
        if (!z || z2 || z3) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void onConfReadyCmd(long j) {
        ZMRealNameAuthDialog.dismiss(this, true);
        BOComponent bOComponent = this.mBOComponent;
        if (bOComponent != null) {
            bOComponent.boCheckShowNewAttendeeWaitUnassignedDialog();
        }
        updatePracticeModeView();
        updateSilentModeView();
    }

    private boolean checkStartDrivingModeOnConfReady() {
        ConfMgr instance = ConfMgr.getInstance();
        if (isCallingOut()) {
            return false;
        }
        CmmConfContext confContext = instance.getConfContext();
        if (confContext == null || instance.isViewOnlyMeeting() || this.mConfParams.isDriverModeDisabled()) {
            return false;
        }
        int i = confContext.getAppContextParams().getInt("drivingMode", -1);
        if (this.mVideoSceneMgr instanceof VideoSceneMgr) {
            if (i == 1) {
                ((VideoSceneMgr) this.mVideoSceneMgr).restoreDriverModeSceneOnFailoverSuccess();
                return true;
            } else if (i == -1 && ConfLocalHelper.getEnabledDrivingMode()) {
                ((VideoSceneMgr) this.mVideoSceneMgr).switchToDriverModeSceneAsDefaultScene();
                return true;
            }
        }
        return false;
    }

    public void refreshToolbar() {
        ConfMgr instance = ConfMgr.getInstance();
        if (isCallingOut() || !instance.isConfConnected()) {
            showToolbar(false, false);
        }
        CmmUser myself = instance.getMyself();
        boolean isViewOnlyMeeting = instance.isViewOnlyMeeting();
        if (isViewOnlyMeeting) {
            ConfLocalHelper.refreshViewOnlyToolbar(this.mToolbar, myself);
        } else {
            int i = 63;
            int[] unreadChatMessageIndexes = instance.getUnreadChatMessageIndexes();
            if (unreadChatMessageIndexes != null) {
                this.mToolbar.setChatsButton(unreadChatMessageIndexes.length);
            }
            if (ZMConfCameraUtils.getNumberOfCameras() <= 0 || this.mConfParams.isVideoButtonDisabled()) {
                i = 62;
            }
            VideoSessionMgr videoObj = instance.getVideoObj();
            boolean z = true;
            this.mToolbar.setVideoMuted(videoObj == null || !videoObj.isVideoStarted());
            if (myself != null) {
                boolean isHost = myself.isHost();
                boolean isCoHost = myself.isCoHost();
                ConfToolbar confToolbar = this.mToolbar;
                if (!isHost && !isCoHost) {
                    z = false;
                }
                confToolbar.setHostRole(z);
                if (isHost) {
                    this.mBtnLeave.setText(C4558R.string.zm_btn_end_meeting);
                } else {
                    this.mBtnLeave.setText(C4558R.string.zm_btn_leave_meeting);
                }
            }
            if (!MoreTip.hasItemsToShow() || this.mConfParams.isMoreButtonDisabled()) {
                i &= -33;
            }
            if (!ConfLocalHelper.isNeedShowBtnShare(this.mConfParams)) {
                i &= -5;
            }
            if (this.mConfParams.isPlistButtonDisabled()) {
                i &= -9;
            }
            if (this.mConfParams.isAudioButtonDisabled()) {
                i &= -3;
            }
            this.mToolbar.setButtons(i);
        }
        if (myself != null && this.mToolbar.hasEnableBUtton(2)) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                boolean isMuted = audioStatusObj.getIsMuted();
                if (!isMuted) {
                    ConfAllowTalkDialog.dismiss(getSupportFragmentManager());
                    ZMSpotlightVideoDialog.dismiss(getSupportFragmentManager());
                }
                this.mToolbar.setAudioMuted(isMuted);
                this.mToolbar.setAudioType(audioStatusObj.getAudiotype());
            }
        }
        if (this.mConfParams.isLeaveButtonDisabled()) {
            this.mBtnLeave.setVisibility(8);
        } else {
            this.mBtnLeave.setVisibility(0);
        }
        if (this.mTxtMeetingNumber != null) {
            updateTitleBar();
        }
        if (isViewOnlyMeeting) {
            updateAttendeeRaiseHandButton();
            View view = this.mQaView;
            if (view != null) {
                view.setVisibility(8);
            }
            refreshAudioSourceBtn();
        } else {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (ConfMgr.getInstance().isConfConnected()) {
                if (confContext != null && confContext.isWebinar() && !confContext.isQANDAOFF()) {
                    View view2 = this.mQaView;
                    if (view2 != null) {
                        view2.setVisibility(0);
                    }
                } else if (BOUtil.isInBOMeeting()) {
                    View view3 = this.mQaView;
                    if (view3 != null) {
                        view3.setVisibility(8);
                        this.mTxtQAOpenNumber.setVisibility(8);
                    }
                } else {
                    View view4 = this.mQaView;
                    if (view4 != null) {
                        view4.setVisibility(8);
                        this.mTxtQAOpenNumber.setVisibility(8);
                    }
                }
                refreshAudioSourceBtn();
            }
            ZMConfComponentMgr.getInstance().refreshSwitchCameraButton();
        }
        BOComponent bOComponent = this.mBOComponent;
        if (bOComponent != null) {
            bOComponent.updateBOButton();
        }
        KubiComponent kubiComponent = this.mKubiComponent;
        if (kubiComponent != null) {
            kubiComponent.updateKubiButton();
        }
        updateSwitchToShareButton();
        refreshAudioWatermark();
    }

    private void refreshAudioSourceBtn() {
        if (this.mImgAudioSource != null) {
            if (!canSwitchAudioSource() || this.mConfParams.isSwitchAudioSourceButtonDisabled()) {
                this.mImgAudioSource.setVisibility(8);
            } else {
                this.mImgAudioSource.setVisibility(0);
            }
            int currentAudioSourceType = ConfUI.getInstance().getCurrentAudioSourceType();
            int i = C4558R.C4559drawable.zm_ic_speaker_off;
            String string = getString(C4558R.string.zm_description_btn_audio_source_speaker_phone);
            switch (currentAudioSourceType) {
                case 0:
                    i = C4558R.C4559drawable.zm_ic_speaker_on;
                    string = getString(C4558R.string.zm_description_btn_audio_source_speaker_phone);
                    break;
                case 1:
                    i = C4558R.C4559drawable.zm_ic_speaker_off;
                    string = getString(C4558R.string.zm_description_btn_audio_source_ear_phone);
                    break;
                case 2:
                    i = C4558R.C4559drawable.zm_ic_current_headset;
                    string = getString(C4558R.string.zm_description_btn_audio_source_wired);
                    break;
                case 3:
                    i = C4558R.C4559drawable.zm_ic_current_bluetooth;
                    string = getString(C4558R.string.zm_description_btn_audio_source_bluetooth);
                    break;
            }
            ImageView imageView = this.mImgAudioSource;
            if (imageView != null) {
                imageView.setImageResource(i);
                this.mImgAudioSource.setContentDescription(string);
            }
        }
    }

    private void refreshAudioWatermark() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            this.mViewAudioWatermark.setVisibility((!confContext.isAudioWatermarkEnabled() || isInDriveMode()) ? 8 : 0);
        }
    }

    /* access modifiers changed from: private */
    public void refreshPanelRecording() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                boolean theMeetingisBeingRecording = recordMgr.theMeetingisBeingRecording();
                boolean isRecordingInProgress = recordMgr.isRecordingInProgress();
                boolean isInDriveMode = isInDriveMode();
                int i = 8;
                if (isRecordingInProgress) {
                    this.mPanelRecording.setVisibility(8);
                    this.mPanelCurUserRecording.setVisibility(!isInDriveMode ? 0 : 8);
                    if (confStatusObj.isCMRInConnecting()) {
                        this.mPanelCurUserRecording.setOnClickListener(null);
                        this.mImgRecording.setVisibility(8);
                        this.mImgRecordControlArrow.setVisibility(8);
                        this.mProgressStartingRecord.setVisibility(0);
                        this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_preparing);
                    } else {
                        this.mPanelCurUserRecording.setOnClickListener(this);
                        if (recordMgr.isCMRPaused()) {
                            this.mImgRecording.setVisibility(8);
                            this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_paused);
                        } else {
                            this.mImgRecording.setVisibility(0);
                            this.mTxtRecordStatus.setText(C4558R.string.zm_record_status_recording);
                        }
                        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                        if (confContext == null || !confContext.isAutoCMRForbidManualStop() || recordMgr.isCMRPaused()) {
                            this.mImgRecordControlArrow.setVisibility(0);
                        } else {
                            this.mImgRecordControlArrow.setVisibility(8);
                        }
                        this.mProgressStartingRecord.setVisibility(8);
                    }
                } else {
                    this.mPanelCurUserRecording.setVisibility(8);
                    View view = this.mPanelRecording;
                    if (theMeetingisBeingRecording && !isInDriveMode) {
                        i = 0;
                    }
                    view.setVisibility(i);
                    AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService("accessibility");
                    if (accessibilityManager != null && accessibilityManager.isEnabled()) {
                        this.mPanelRecording.setFocusable(true);
                    }
                }
                if (!theMeetingisBeingRecording) {
                    RetainedFragment retainedFragment = getRetainedFragment();
                    if (retainedFragment != null) {
                        retainedFragment.setHasPopupStartingRecord(false);
                    }
                }
                this.mPanelStartingRecord.setVisibility(4);
                MoreTip.updateRecordIfExists(getSupportFragmentManager(), isRecordingInProgress, isInDriveMode);
            }
        }
    }

    private void startRecordingAnimation() {
        String str;
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList == null) {
            refreshPanelRecording();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= userList.getUserCount()) {
                str = null;
                break;
            }
            CmmUser userAt = userList.getUserAt(i);
            if (userAt != null && userAt.isRecording()) {
                str = userAt.getScreenName();
                break;
            }
            i++;
        }
        if (StringUtil.isEmptyOrNull(str)) {
            refreshPanelRecording();
            return;
        }
        if (!ConfMgr.getInstance().isViewOnlyMeeting() || ConfLocalHelper.isViewOnlyButSpeakAttendee()) {
            this.mTxtStartingRecrod.setText(getString(C4558R.string.zm_lbl_starting_record, new Object[]{str}));
            this.mPanelStartingRecord.setVisibility(0);
        } else {
            this.mPanelStartingRecord.setVisibility(4);
        }
        RetainedFragment retainedFragment = getRetainedFragment();
        if (retainedFragment != null) {
            if (retainedFragment.hasPopupStartingRecord()) {
                refreshPanelRecording();
                return;
            }
            retainedFragment.setHasPopupStartingRecord(true);
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (ConfActivityNormal.this.isActive()) {
                    ConfActivityNormal.this.mPanelStartingRecord.setVisibility(4);
                    ConfActivityNormal.this.refreshPanelRecording();
                    if (ConfActivityNormal.this.mPanelRecording.getVisibility() == 0 || ConfActivityNormal.this.mPanelCurUserRecording.getVisibility() == 0) {
                        ConfActivityNormal.this.hideToolbarDelayed(0);
                    }
                }
            }
        }, 3000);
    }

    private void sinkConfReady(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onConfReady") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfReady(j);
            }
        });
    }

    private void sinkConfReadyCmd(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkConfReadyCmd") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfReadyCmd(j);
            }
        });
    }

    private void sinkConfLeaving(long j) {
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onConfLeaving();
        }
    }

    private void sinkUserCountChangesForShowHideAction() {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("sinkUserCountChangesForShowHideAction") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onUserCountChangesForShowHideAction();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onUserCountChangesForShowHideAction() {
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onUserCountChangesForShowHideAction();
        }
    }

    private void sinkConfRecordStatus() {
        if (isActive() && !isInDriveMode() && !ConfLocalHelper.isDirectShareClient()) {
            RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
            if (recordMgr != null) {
                if (!recordMgr.theMeetingisBeingRecording()) {
                    refreshPanelRecording();
                } else {
                    startRecordingAnimation();
                }
            }
        }
    }

    private void sinkAudioSharingStatusChanged() {
        getNonNullEventTaskManagerOrThrowException().push("sinkAudioSharingStatusChanged", new EventAction("sinkAudioSharingStatusChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onAudioSharingStatusChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onAudioSharingStatusChanged() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            if (shareObj.presenterIsSharingAudio()) {
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_MUTED_FOR_SHARING_AUDIO_STARTED.name(), (String) null, getString(C4558R.string.zm_msg_muted_for_sharing_audio_started), 3000);
            } else {
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_UNMUTED_FOR_SHARING_AUDIO_STOPPED.name(), (String) null, getString(C4558R.string.zm_msg_unmuted_for_sharing_audio_stopped), 3000);
            }
        }
    }

    private void sinkConfMeetingUpgraded(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkConfMeetingUpgraded", new EventAction("sinkConfMeetingUpgraded") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfMeetingUpgraded(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfMeetingUpgraded(long j) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.canUpgradeThisFreeMeeting()) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_upgrade_free_meeting_success_15609, C4558R.string.zm_msg_host_paid_title).show(getSupportFragmentManager(), "SimpleMessageDialog.msg_conf_free_meeting_start_reminder");
        } else if (j == 1) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_conf_paid_meeting_start_reminder, C4558R.string.zm_msg_host_paid_title).show(getSupportFragmentManager(), "SimpleMessageDialog.msg_conf_paid_meeting_start_reminder");
        } else {
            SimpleMessageDialog.newInstance(C4558R.string.zm_msg_conf_host_paid_reminder, C4558R.string.zm_msg_host_paid_title).show(getSupportFragmentManager(), "SimpleMessageDialog.msg_conf_host_paid_reminder");
        }
    }

    private void sinkConfAllowRaiseHandStatusChanged() {
        if (isActive()) {
            refreshToolbar();
            handleAttendeeRaiseLowerHand(0);
        }
    }

    private void sinkCmdAutoShowAudioSelectionDlg() {
        if (!ConfLocalHelper.tryAutoConnectAudio(this)) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isAutoShowJoinAudioDialogEnabled()) {
                getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                    public void run(IUIElement iUIElement) {
                        ((ConfActivityNormal) iUIElement).handleCmdAudioShowAudioSelectionDlg();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleCmdAudioShowAudioSelectionDlg() {
        g_handler.postDelayed(new Runnable() {
            public void run() {
                if (ConfActivityNormal.this.isActive()) {
                    CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                    if (confContext != null) {
                        MeetingInfoProto meetingItem = confContext.getMeetingItem();
                        if (meetingItem != null) {
                            if (meetingItem.getIsSelfTelephonyOn()) {
                                ConfActivityNormal.this.showSelfTelephoneInfo(meetingItem.getOtherTeleConfInfo());
                            } else if (!ConfMgr.getInstance().getConfDataHelper().ismIsAutoCalledOrCanceledCall()) {
                                int i = 0;
                                if (ConfActivityNormal.this.isInDriveMode()) {
                                    ConfActivityNormal.this.showAudioBottomSheet(0);
                                } else {
                                    ConfActivityNormal.this.showToolbar(true, false);
                                    ConfActivityNormal.this.disableToolbarAutoHide();
                                    ConfActivityNormal confActivityNormal = ConfActivityNormal.this;
                                    if (confActivityNormal.isBottombarShowing()) {
                                        i = C4558R.C4560id.btnAudio;
                                    }
                                    confActivityNormal.showAudioBottomSheet(i);
                                }
                            }
                        }
                    }
                }
            }
        }, 500);
    }

    private void sinkConfSilentModeChanged(boolean z) {
        if (z) {
            finishSubActivities();
            if (ScreenShareMgr.getInstance().isSharing()) {
                ConfLocalHelper.returnToConf(this);
            }
        }
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleCmdConfSilentModeChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleCmdConfSilentModeChanged() {
        boolean isInSilentMode = ConfLocalHelper.isInSilentMode();
        boolean isDirectShareClient = ConfLocalHelper.isDirectShareClient();
        if (isInSilentMode) {
            dismissTempTips();
            BOComponent bOComponent = this.mBOComponent;
            if (bOComponent != null) {
                bOComponent.clearAllBOUI();
            }
            switchViewTo(ZMConfEnumViewMode.SILENT_VIEW);
        } else if (isDirectShareClient) {
            dismissTempTips();
            switchViewTo(ZMConfEnumViewMode.PRESENT_ROOM_LAYER);
        } else if (!isCallingOut() && ConfMgr.getInstance().isConfConnected()) {
            switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
            ZMConfComponentMgr.getInstance().handleCmdConfSilentModeChanged();
        }
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onConfSilentModeChanged();
        }
        BOComponent bOComponent2 = this.mBOComponent;
        if (bOComponent2 != null) {
            bOComponent2.boCheckShowNewAttendeeWaitUnassignedDialog();
        }
    }

    private void sinkCMRStartTimeOut() {
        getNonNullEventTaskManagerOrThrowException().push("handleCMRStartTimeOut", new EventAction("handleCMRStartTimeOut") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleCMRStartTimeOut();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleCMRStartTimeOut() {
        showCMRTimeOutMessage();
    }

    private void showCMRTimeOutMessage() {
        ZMAlertDialog create = new Builder(this).setTitle(C4558R.string.zm_record_msg_start_cmr_timeout).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
        create.setCancelable(true);
        create.setCanceledOnTouchOutside(false);
        create.show();
    }

    /* access modifiers changed from: protected */
    public boolean onConfLeaveComplete(long j) {
        return super.onConfLeaveComplete(j) && this.mVideoSceneMgr != null && this.mVideoSceneMgr.isDestroyed();
    }

    private void sinkAudioReady() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            AudioManager audioManager = (AudioManager) getSystemService("audio");
            int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
            if (selectedPlayerStreamType >= 0 && audioManager != null) {
                notifyVolumeChanged(false, audioManager.getStreamVolume(selectedPlayerStreamType), selectedPlayerStreamType);
                audioObj.notifyHeadsetStatusChanged(HeadsetUtil.getInstance().isBluetoothHeadsetOn(), HeadsetUtil.getInstance().isWiredHeadsetOn());
                audioObj.notifyChipAECEnabled(VoiceEngineCompat.isChipAECSupported(this));
                audioObj.notifyIsTablet(VoiceEngineCompat.isTablet(this));
            } else {
                return;
            }
        }
        if (checkNeedMuteAudioByDefault() && audioObj != null) {
            audioObj.stopAudio();
        }
    }

    private boolean checkNeedMuteAudioByDefault() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        int i = confContext.getAppContextParams().getInt("drivingMode", -1);
        if (!(this.mVideoSceneMgr instanceof VideoSceneMgr)) {
            return false;
        }
        boolean z = true;
        if (i != 1 && (i != -1 || !ConfLocalHelper.getEnabledDrivingMode())) {
            z = false;
        }
        return z;
    }

    private boolean showConfReadyTips(boolean z) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null || ConfMgr.getInstance().getUserList() == null) {
            return false;
        }
        boolean z2 = true;
        if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) < 2 && !isCallingOut() && confContext.getOrginalHost()) {
            showToolbar(true, false);
            String str = confContext.get1On1BuddyScreeName();
            boolean isInstantMeeting = confContext.isInstantMeeting();
            if (!StringUtil.isEmptyOrNull(str)) {
                ToastTip.showNow(getSupportFragmentManager(), getString(C4558R.string.zm_msg_conf_waiting_to_join, new Object[]{str}), 0, isInDriveMode());
            } else if (isInstantMeeting && !this.mConfParams.isInviteDisabled() && !z) {
                showTipWaitingToInvite();
            }
            return z2;
        }
        z2 = false;
        return z2;
    }

    private void sinkUserAudioStatus(long j, final int i) {
        if (isActive()) {
            onUserAudioStatus(j);
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            EventTaskManager eventTaskManager = getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.pushLater("sinkUserAudioStatus", new EventAction("sinkUserAudioStatus") {
                    public void run(IUIElement iUIElement) {
                        ConfActivity confActivity = (ConfActivity) iUIElement;
                        if (confActivity != null) {
                            ConfLocalHelper.showTipForUserAction(confActivity, i);
                        }
                    }
                });
            }
        }
    }

    private void onUserAudioStatus(long j) {
        ConfMgr instance = ConfMgr.getInstance();
        if (instance.getUserById(j) != null) {
            CmmConfStatus confStatusObj = instance.getConfStatusObj();
            if (confStatusObj != null) {
                if (confStatusObj.isMyself(j)) {
                    if (isActive()) {
                        refreshToolbar();
                    }
                    AudioTip.updateIfExists(getSupportFragmentManager());
                }
                this.mVideoSceneMgr.onUserAudioStatus(j);
            }
        }
    }

    public void refreshMainVideoAudioStatus() {
        if (isToolbarShowing()) {
            this.mPanelAudioConnectStatus.setVisibility(8);
        }
    }

    private void refreshMainVideoAudioStatus(AbsVideoScene absVideoScene, AbsVideoScene absVideoScene2) {
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
        if (videoSceneMgr != null) {
            AbsVideoScene activeScene = videoSceneMgr.getActiveScene();
            if (activeScene != null && !(activeScene instanceof NormalVideoScene)) {
                this.mPanelAudioConnectStatus.setVisibility(8);
            }
        }
    }

    public void refreshMainVideoAudioStatus(int i, int i2, int i3, String str) {
        super.refreshMainVideoAudioStatus(i, i2, i3, str);
        hiddenMainVideoAudioStatus();
        if (!isToolbarShowing()) {
            VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
            if (videoSceneMgr != null) {
                AbsVideoScene activeScene = videoSceneMgr.getActiveScene();
                if (activeScene != null && (activeScene instanceof NormalVideoScene) && !ConfShareLocalHelper.isOtherPureAudioSharing()) {
                    this.mPanelAudioConnectStatus.setVisibility(0);
                    String ellipseString = StringUtil.ellipseString(str, 32);
                    if (i2 == 1) {
                        this.mTxtAudioConnectStatus.setText(getResources().getString(C4558R.string.zm_lbl_someone_is_connecting_audio_and_not_hear_123338, new Object[]{ellipseString}));
                    } else if (i2 == 2) {
                        this.mTxtAudioConnectStatus.setText(getResources().getString(C4558R.string.zm_lbl_someone_connected_audio_123338, new Object[]{ellipseString}));
                    } else if (i2 == 3) {
                        this.mTxtAudioConnectStatus.setText(getResources().getString(C4558R.string.zm_lbl_someone_did_not_connect_audio_123338, new Object[]{ellipseString}));
                    }
                    Drawable drawable = getResources().getDrawable(i3);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    this.mTxtAudioConnectStatus.setCompoundDrawables(null, null, drawable, null);
                    if (AccessibilityUtil.isSpokenFeedbackEnabled(this) && i2 != i) {
                        AccessibilityUtil.announceForAccessibilityCompat(this.mPanelAudioConnectStatus, this.mTxtAudioConnectStatus.getText());
                    }
                }
            }
        }
    }

    public void hiddenMainVideoAudioStatus() {
        super.hiddenMainVideoAudioStatus();
        if (!ConfShareLocalHelper.isOtherPureAudioSharing()) {
            this.mPanelAudioConnectStatus.setVisibility(8);
        }
    }

    private void sinkUserActiveAudio(long j) {
        if (isActive()) {
            onUserActiveAudio(j);
        }
    }

    private void onUserActiveAudio(long j) {
        this.mVideoSceneMgr.onUserActiveAudio(j);
    }

    private void sinkUserRaiseLowerHand(long j, boolean z) {
        final long j2 = j;
        final boolean z2 = z;
        C198033 r2 = new EventAction("onUserRaiseLowerHand") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onUserRaiseLowerHand(j2, z2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("onUserRaiseLowerHand", r2);
    }

    private void sinkRosterAttributeChangedForAll() {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkRosterAttributeChangedForAll", new EventAction("sinkRosterAttributeChangedForAll") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).updateRaiseHandStatus();
            }
        });
    }

    private int getAllRaiseHandCount() {
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList == null) {
            return 0;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if ((confStatusObj == null || !confStatusObj.isAllowRaiseHand()) && !ConfLocalHelper.isWebinar()) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < userList.getUserCount(); i2++) {
            CmmUser userAt = userList.getUserAt(i2);
            if (userAt != null && userAt.getRaiseHandState()) {
                i++;
            }
        }
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj == null) {
            return i;
        }
        return i + raiseHandAPIObj.getRaisedHandCount();
    }

    @Nullable
    private String getFirstRaiseHandName() {
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList == null) {
            return null;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return null;
        }
        for (int i = 0; i < userList.getUserCount(); i++) {
            CmmUser userAt = userList.getUserAt(i);
            if (userAt != null && userAt.getRaiseHandState() && !confStatusObj.isMyself(userAt.getNodeId())) {
                return userAt.getScreenName();
            }
        }
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj == null) {
            return null;
        }
        List raisedHandAttendees = raiseHandAPIObj.getRaisedHandAttendees();
        if (raisedHandAttendees != null) {
            ZoomQABuddy zoomQABuddy = (ZoomQABuddy) raisedHandAttendees.get(0);
            if (zoomQABuddy != null) {
                return zoomQABuddy.getName();
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void onUserRaiseLowerHand(long j, boolean z) {
        updateRaiseHandStatus();
    }

    /* access modifiers changed from: private */
    public void updateRaiseHandStatus() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            if (myself.isHostCoHost()) {
                int allRaiseHandCount = getAllRaiseHandCount();
                if (allRaiseHandCount == 0) {
                    dismissRaiseHandTip();
                    return;
                }
                String firstRaiseHandName = getFirstRaiseHandName();
                if (allRaiseHandCount != 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(");
                    sb.append(String.valueOf(allRaiseHandCount));
                    sb.append(")");
                    firstRaiseHandName = sb.toString();
                }
                showRaiseHandTip(firstRaiseHandName);
            } else if (!myself.getRaiseHandState()) {
                dismissRaiseHandTip();
            } else if (!RaiseHandTip.isShown(getSupportFragmentManager())) {
                showRaiseHandTip("");
            }
        }
    }

    private void dismissRaiseHandTip() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null && RaiseHandTip.isShown(supportFragmentManager)) {
            RaiseHandTip.dismiss(supportFragmentManager);
        }
    }

    /* access modifiers changed from: private */
    public void handleAttendeeLeft() {
        getNonNullEventTaskManagerOrThrowException().pushLater("onAttendeeLeft", new EventAction("onAttendeeLeft") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onAttendeeLeft();
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleAttendeeRaiseLowerHand(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("onAttendeeRaiseLowerHand", new EventAction("onAttendeeRaiseLowerHand") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onAttendeeRaiseLowerHand(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onAttendeeLeft() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && (myself.isHost() || myself.isCoHost())) {
            this.mHandler.removeCallbacks(this.mAttendeeRaiseLowerHandRunnalbe);
            this.mHandler.postDelayed(this.mAttendeeRaiseLowerHandRunnalbe, 400);
        }
    }

    /* access modifiers changed from: private */
    public void onAttendeeRaiseLowerHand(long j) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && (myself.isHost() || myself.isCoHost())) {
            this.mHandler.removeCallbacks(this.mAttendeeRaiseLowerHandRunnalbe);
            Handler handler = this.mHandler;
            Runnable runnable = this.mAttendeeRaiseLowerHandRunnalbe;
            long j2 = 0;
            if (j != 0) {
                j2 = 1000;
            }
            handler.postDelayed(runnable, j2);
        }
    }

    private void sinkBOModeratorChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().push("sinkBOModeratorChanged", new EventAction("sinkBOModeratorChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleBOModeratorChanged(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleBOModeratorChanged(long j) {
        if (MoreTip.hasItemsToShow()) {
            MoreTip.updateIfExists(getSupportFragmentManager());
        } else {
            MoreTip.dismiss(getSupportFragmentManager());
        }
        refreshToolbar();
    }

    private void showRaiseHandTip(String str) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            if (RaiseHandTip.isShown(supportFragmentManager)) {
                RaiseHandTip.dismiss(supportFragmentManager);
            }
            RaiseHandTip.showTip(supportFragmentManager, str, 0);
        }
    }

    private void sinkConfOne2One(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("onConfOne2One", new EventAction("onConfOne2One") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfOne2One(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfOne2One(long j) {
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onConfOne2One();
        }
    }

    private void sinkHostChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("onHostChange", new EventAction("onHostChange") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onHostChange(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onHostChange(long j) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null && ConfMgr.getInstance().isConfConnected()) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            boolean z = true;
            boolean z2 = myself != null && myself.isHost();
            boolean z3 = myself != null && myself.isCoHost();
            if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) >= 2 && z2) {
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_YOU_ARE_HOST.name(), (String) null, getString(C4558R.string.zm_msg_meeting_youarehost), 3000);
            }
            if (z2) {
                if (MoreTip.isShown(supportFragmentManager)) {
                    MoreTip.dismiss(supportFragmentManager);
                }
                this.mBtnLeave.setText(C4558R.string.zm_btn_end_meeting);
            } else {
                this.mBtnLeave.setText(C4558R.string.zm_btn_leave_meeting);
            }
            BOComponent bOComponent = this.mBOComponent;
            if (bOComponent != null) {
                bOComponent.updateBOButton();
            }
            ConfToolbar confToolbar = this.mToolbar;
            if (!z2 && !z3) {
                z = false;
            }
            confToolbar.setHostRole(z);
            updateRaiseHandStatus();
            NormalMessageTip.dismiss(supportFragmentManager, TipMessageType.TIP_LOGIN_AS_HOST.name());
            if (this.mVideoSceneMgr != null) {
                this.mVideoSceneMgr.onHostChanged(j, z2);
            }
            refreshPanelRecording();
            updatePracticeModeView();
            LiveStreamComponent liveStreamComponent = this.mLiveStreamComponent;
            if (liveStreamComponent != null) {
                liveStreamComponent.onHostCohostChanged(this);
            }
            checkShowRemoteAdminTip(false);
        }
    }

    /* access modifiers changed from: private */
    public void checkShowRemoteAdminTip(boolean z) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        CmmUser myself = ConfMgr.getInstance().getMyself();
        boolean z2 = true;
        boolean z3 = myself != null && myself.isHost();
        if (confStatusObj == null || !confStatusObj.isRemoteAdminExisting()) {
            z2 = false;
        }
        if (z3) {
            if (z2) {
                if (!NormalMessageTip.isTipShown(TipMessageType.TIP_REMOTE_ADMIN_JOINED.name())) {
                    NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_REMOTE_ADMIN_JOINED.name(), (String) null, getString(C4558R.string.zm_msg_meeting_remote_admin_join_113385), 3000);
                }
            } else if (z && !NormalMessageTip.isTipShown(TipMessageType.TIP_REMOTE_ADMIN_LEFT.name())) {
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_REMOTE_ADMIN_LEFT.name(), (String) null, getString(C4558R.string.zm_msg_meeting_remote_admin_leave_113385), 3000);
            }
        }
    }

    private void sinkCoHostChanged(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("onCoHostChange", new EventAction("onHostChange") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onCoHostChange(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onCoHostChange(long j) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null && ConfMgr.getInstance().isConfConnected() && !ConfLocalHelper.isDirectShareClient()) {
            if (!ConfLocalHelper.isMySelf(j) || !ConfLocalHelper.isCoHost()) {
                CmmUser userById = ConfMgr.getInstance().getUserById(j);
                if (userById != null && userById.isCoHost()) {
                    NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_YOU_ARE_HOST.name(), (String) null, getString(C4558R.string.zm_msg_meeting_xxx_are_cohost, new Object[]{userById.getScreenName()}), 3000);
                }
            } else {
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_YOU_ARE_HOST.name(), (String) null, getString(C4558R.string.zm_msg_meeting_you_are_cohost), 3000);
                handleAttendeeRaiseLowerHand(0);
            }
            updateRaiseHandStatus();
            if (ConfLocalHelper.isMySelf(j) && MoreTip.isShown(supportFragmentManager)) {
                MoreTip.dismiss(supportFragmentManager);
            }
            this.mToolbar.setHostRole(ConfLocalHelper.isHostCoHost());
            refreshPanelRecording();
            updatePracticeModeView();
            LiveStreamComponent liveStreamComponent = this.mLiveStreamComponent;
            if (liveStreamComponent != null) {
                liveStreamComponent.onHostCohostChanged(this);
            }
            ShareTip.dismiss(supportFragmentManager);
        }
    }

    private void sinkAudioTypeChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && !ConfLocalHelper.isDirectShareClient()) {
            if (confStatusObj.isMyself(j)) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                    if (audioStatusObj != null) {
                        long audiotype = audioStatusObj.getAudiotype();
                        if (isActive()) {
                            refreshToolbar();
                        }
                        if (isActive()) {
                            if (!isCallingOut()) {
                                if (audiotype == 0) {
                                    NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_AUDIO_TYPE_CHANGED.name(), (String) null, getString(C4558R.string.zm_msg_audio_changed_to_voip), 3000);
                                } else if (audiotype == 1) {
                                    NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_AUDIO_TYPE_CHANGED.name(), (String) null, getString(C4558R.string.zm_msg_audio_changed_to_phone), 3000);
                                }
                            }
                            if (audiotype != 2) {
                                AudioTip.dismiss(getSupportFragmentManager());
                                hideToolbarDelayed(5000);
                            }
                        }
                        refreshInterpretation();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (isActive() && this.mVideoSceneMgr != null) {
                this.mVideoSceneMgr.onAudioTypeChanged(j);
            }
        }
    }

    private void sinkUserPicReady(long j) {
        if (isActive() && this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onUserPicReady(j);
        }
    }

    private void sinkConfVerifyHostKeyStatus(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction("onVerifyHostKeyStatus") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfVerifyHostKeyStatus(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onConfVerifyHostKeyStatus(long j) {
        dismissVerifyHostKeyDialog();
        if (j != 0) {
            HostKeyErrorDialog.show(getSupportFragmentManager());
        }
    }

    private void sinkNotSupportAnnotationJoined() {
        getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction("onNotSupportAnnotationJoined") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onNotSupportAnnotationJoined();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onNotSupportAnnotationJoined() {
        new Builder(this).setCancelable(true).setMessage(C4558R.string.zm_alert_non_annotation_joined).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    public void onInnerEventAction(int i, int i2) {
        boolean z = true;
        switch (i) {
            case 0:
                ConfDataHelper confDataHelper = ConfMgr.getInstance().getConfDataHelper();
                if (i2 <= 0) {
                    z = false;
                }
                confDataHelper.setmIsShowMyVideoInGalleryView(z);
                sinkUserCountChangesForShowHideAction();
                return;
            case 1:
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    if (i2 <= 0) {
                        z = false;
                    }
                    videoObj.setHideNoVideoUserInWallView(z);
                }
                sinkUserCountChangesForShowHideAction();
                return;
            case 2:
                ZMConfComponentMgr.getInstance().sinkPreemptionAudio(i2);
                return;
            default:
                return;
        }
    }

    public boolean onConfStatusChanged(int i) {
        if (!(i == 0 || i == 10)) {
            if (i != 21) {
                switch (i) {
                    case 3:
                    case 4:
                    case 6:
                        break;
                    case 5:
                        ZMConfComponentMgr.getInstance().sinkConfConnecting();
                        break;
                    default:
                        switch (i) {
                            case 13:
                                sinkConfReady((long) i);
                                break;
                            case 14:
                                sinkConfLeaving((long) i);
                                break;
                        }
                }
            } else {
                sinkConfReconnect((long) i);
            }
        }
        return true;
    }

    public boolean onConfStatusChanged2(int i, long j) {
        processSpokenAccessibilityForConfCmd(ZMConfComponentMgr.getInstance().getFocusView(), i, j);
        boolean z = false;
        switch (i) {
            case 2:
                BOComponent bOComponent = this.mBOComponent;
                if (bOComponent != null) {
                    bOComponent.hideBOStatusChangeUI();
                    break;
                }
                break;
            case 5:
                sinkAudioReady();
                break;
            case 7:
                ZMConfComponentMgr.getInstance().sinkAutoStartVideo(j);
                break;
            case 8:
                sinkConfReadyCmd(j);
                break;
            case 9:
                sinkConfOne2One(j);
                break;
            case 17:
                ZMConfComponentMgr.getInstance().sinkConfVideoSendingStatusChanged();
                break;
            case 19:
                sinkAudioSharingStatusChanged();
                break;
            case 20:
                ZMConfComponentMgr.getInstance().sinkLeaderShipModeChanged();
                break;
            case 21:
                ZMConfComponentMgr.getInstance().sinkVideoLeaderShipModeOnOff();
                break;
            case 24:
                sinkNotSupportAnnotationJoined();
                break;
            case 27:
                if (j == 1) {
                    z = true;
                }
                sinkConfPracticeSessionStatusChanged(z);
                break;
            case 31:
                sinkConfAllowRaiseHandStatusChanged();
                break;
            case 35:
                sinkCmdAutoShowAudioSelectionDlg();
                break;
            case 39:
                if (j == 1) {
                    z = true;
                }
                sinkConfSilentModeChanged(z);
                break;
            case 45:
                sinkCMRStartTimeOut();
                break;
            case 47:
            case 49:
                LiveStreamComponent liveStreamComponent = this.mLiveStreamComponent;
                if (liveStreamComponent != null) {
                    liveStreamComponent.onConfStatusChanged2(i, j);
                    break;
                }
                break;
            case 77:
                sinkConfMeetingUpgraded(j);
                break;
            case 79:
                sinkConfRecordStatus();
                break;
            case 80:
                sinkReminderRecording();
                break;
            case 81:
                sinkUserJoinRing();
                break;
            case 103:
                sinkRosterAttributeChangedForAll();
                break;
            case 107:
                sinkConfVerifyHostKeyStatus(j);
                break;
            case 136:
                sinkWaitingRoomDataReady();
                break;
            case 149:
                ZMConfComponentMgr.getInstance().sinkBandwidthLimitStatusChanged();
                break;
            case 150:
                ZMConfComponentMgr.getInstance().sinkSendVideoPrivilegeChanged();
                break;
            case 151:
                ZMConfComponentMgr.getInstance().sinkReceiveVideoPrivilegeChanged();
                break;
            case 152:
                ZMConfComponentMgr.getInstance().sinkSendVideoPrivilegeChanged();
                ZMConfComponentMgr.getInstance().sinkReceiveVideoPrivilegeChanged();
                break;
            case 153:
                ZMConfComponentMgr.getInstance().sinkCompanionModeChanged();
                break;
            case 159:
                sinkRemoteAdminExistStatusChanged();
                break;
            case 161:
                updateBarrierChange(j);
                break;
        }
        return true;
    }

    private void updateBarrierChange(long j) {
        if ((j & ((long) ConfParams.InfoBarrierFieldShare)) == ((long) ConfParams.InfoBarrierFieldShare) && ConfMgr.getInstance().getShareObj() != null && ConfMgr.getInstance().getShareObj().getShareStatus() == 2) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isShareDisabledByInfoBarrier()) {
                if (ScreenShareMgr.getInstance().isSharing() && !ConfLocalHelper.isDirectShareClient()) {
                    ConfLocalHelper.returnConf(this);
                }
                ZMConfComponentMgr.getInstance().stopShare();
                NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_UNABLE_TO_SHARE.name(), (String) null, getString(C4558R.string.zm_unable_to_share_in_meeting_msg_93170), 3000);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
                    AccessibilityUtil.announceForAccessibilityCompat(getWindow().getDecorView(), (CharSequence) getString(C4558R.string.zm_unable_to_share_in_meeting_msg_93170));
                }
            }
        }
    }

    public boolean onUserStatusChanged(int i, long j, int i2) {
        processSpokenAccessibilityForUserCmd(ZMConfComponentMgr.getInstance().getFocusView(), i, j, i2);
        switch (i) {
            case 1:
                sinkHostChanged(j);
                break;
            case 4:
                ZMConfComponentMgr.getInstance().sinkUserVideoStatus(j);
                break;
            case 6:
                ZMConfComponentMgr.getInstance().sinkUserActiveVideoForDeck(j);
                break;
            case 9:
            case 70:
                sinkUserAudioStatus(j, i2);
                break;
            case 10:
                ZMConfComponentMgr.getInstance().sinkUserActiveVideo(j);
                break;
            case 12:
                sinkUserActiveAudio(j);
                break;
            case 14:
            case 15:
                sinkUserPicReady(j);
                break;
            case 16:
                ZMConfComponentMgr.getInstance().sinkUserVideoQualityChanged(j);
                break;
            case 17:
                ZMConfComponentMgr.getInstance().sinkUserVideoDataSizeChanged(j);
                break;
            case 18:
                ZMConfComponentMgr.getInstance().sinkUserTalkingVideo(j);
                break;
            case 21:
                sinkAudioTypeChanged(j);
                break;
            case 23:
                ZMConfComponentMgr.getInstance().sinkUserVideoMutedByHost(j);
                break;
            case 24:
                ZMConfComponentMgr.getInstance().sinkUserVideoRequestUnmuteByHost(j);
                break;
            case 25:
                sinkBOModeratorChanged(j);
                break;
            case 28:
            case 29:
                sinkViewOnlyTalkChange(i, j);
                break;
            case 36:
                sinkUserRaiseLowerHand(j, true);
                break;
            case 37:
                sinkUserRaiseLowerHand(j, false);
                break;
            case 44:
                sinkCoHostChanged(j);
                break;
            case 51:
                sinkHostAskUnmute(j);
                break;
            case 52:
                ZMConfComponentMgr.getInstance().sinkShareActiveUser(j);
                break;
            case 53:
                ZMConfComponentMgr.getInstance().sinkShareUserReceivingStatus(j);
                break;
            case 54:
                ZMConfComponentMgr.getInstance().sinkShareUserSendingStatus(j);
                break;
            case 55:
                ZMConfComponentMgr.getInstance().sinkShareDataSizeChanged(j);
                break;
            case 56:
                ZMConfComponentMgr.getInstance().onUserGetRemoteControlPrivilege(j);
                break;
            case 57:
                ZMConfComponentMgr.getInstance().remoteControlStarted(j);
                break;
            case 64:
                ZMConfComponentMgr.getInstance().sinkUserVideoParticipantUnmuteLater(j);
                break;
            case 65:
                ZMConfComponentMgr.getInstance().sinkInControlCameraTypeChanged(j);
                break;
        }
        return true;
    }

    private void sinkWaitingRoomDataReady() {
        getNonNullEventTaskManagerOrThrowException().pushLater("updateSilentStatus", new EventAction("updateSilentStatus") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).updateSilentModeView();
            }
        });
    }

    private void sinkReminderRecording() {
        getNonNullEventTaskManagerOrThrowException().pushLater("reminderRecordingTip", new EventAction("reminderRecordingTip") {
            public void run(IUIElement iUIElement) {
                if (!ConfLocalHelper.isInSilentMode() && !ConfLocalHelper.isDirectShareClient()) {
                    if (ConfActivityNormal.this.mReminderRecordingTip == null) {
                        ConfActivityNormal.this.mReminderRecordingTip = RecordingReminderDialog.newRecordingReminderDialog();
                        ConfActivityNormal.this.mReminderRecordingTip.show(ConfActivityNormal.this.getSupportFragmentManager(), RecordingReminderDialog.class.getName());
                    } else if (!ConfActivityNormal.this.mReminderRecordingTip.isShowing()) {
                        ConfActivityNormal.this.mReminderRecordingTip.show(ConfActivityNormal.this.getSupportFragmentManager(), RecordingReminderDialog.class.getName());
                    }
                }
            }
        });
    }

    private void sinkRemoteAdminExistStatusChanged() {
        getNonNullEventTaskManagerOrThrowException().pushLater("remoteAdminExistStatusChanged", new EventAction() {
            public void run(IUIElement iUIElement) {
                ConfActivityNormal.this.checkShowRemoteAdminTip(true);
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateSilentModeView() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.inSilentMode()) {
            OnSilentView onSilentView = this.mOnSilentView;
            if (onSilentView != null) {
                onSilentView.updateData();
            }
        }
    }

    private void sinkViewOnlyTalkChange(int i, long j) {
        final int i2 = i;
        final long j2 = j;
        C199648 r2 = new EventAction("sinkViewOnlyTalkChange") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onViewOnlyTalkChange(i2, j2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkViewOnlyTalkChange", r2);
    }

    /* access modifiers changed from: private */
    public void onViewOnlyTalkChange(int i, long j) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isSameUser(myself.getNodeId(), j)) {
                if (i == 28) {
                    ConfAllowTalkDialog.dismiss(getSupportFragmentManager());
                    ConfAllowTalkDialog.showConfAllowTalkDialog(this);
                } else {
                    ConfAllowTalkDialog.dismiss(getSupportFragmentManager());
                }
                refreshToolbar();
                refreshPanelRecording();
            }
        }
    }

    private void sinkHostAskUnmute(final long j) {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkHostAskUnmute", new EventAction("sinkHostAskUnmute") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onHostAskUnmute(j);
            }
        });
    }

    private void sinkOnPTInviteRoomSystemResult(boolean z, String str, String str2, String str3, int i, int i2) {
        RoomDevice roomDevice = new RoomDevice(str, str2, str3, i, i2);
        this.mRoomDevice = roomDevice;
        this.mCallType = i;
        handleCallRoomFail(this.mRoomDevice, this.mCallType);
    }

    /* access modifiers changed from: private */
    public void handleCallRoomFail(final RoomDevice roomDevice, final int i) {
        getNonNullEventTaskManagerOrThrowException().pushLater("handleCallRoomFail", new EventAction("handleCallRoomFail") {
            public void run(IUIElement iUIElement) {
                ToastTip.dismiss(ConfActivityNormal.this.getSupportFragmentManager());
                new Builder((ConfActivityNormal) iUIElement).setCancelable(false).setTitle(C4558R.string.zm_sip_callout_failed_27110).setMessage(C4558R.string.zm_msg_call_back_103311).setPositiveButton(C4558R.string.zm_lbl_context_menu_call_back, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConfActivityNormal.this.sinkOnPTInvitationSent(roomDevice.getDisplayName());
                        IPCHelper.getInstance().callOutRoomSystem(roomDevice.getAddress(), i);
                    }
                }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create().show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void sinkOnPTInvitationSent(final String str) {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkOnPTInvitationSent", new EventAction("sinkOnPTInvitationSent") {
            public void run(IUIElement iUIElement) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    ToastTip.showNow(ConfActivityNormal.this.getSupportFragmentManager(), ConfActivityNormal.this.getString(C4558R.string.zm_msg_conf_waiting_to_join, new Object[]{str}), 0, ConfActivityNormal.this.isInDriveMode());
                }
            }
        });
    }

    private void sinkOnVerifyMyGuestRoleResult(final boolean z, final boolean z2) {
        getNonNullEventTaskManagerOrThrowException().pushLater("sinkOnVerifyMyGuestRoleResult", new EventAction("sinkOnVerifyMyGuestRoleResult") {
            public void run(IUIElement iUIElement) {
                ConfActivityNormal.this.mOnSilentView.updateData();
                if (z) {
                    if (ConfActivityNormal.this.mSignIntoJoinTip != null && ConfActivityNormal.this.mSignIntoJoinTip.isShowing()) {
                        ConfActivityNormal.this.mSignIntoJoinTip.dismiss();
                    }
                    if (z2) {
                        if (ConfActivityNormal.this.mGuestJoinTip == null) {
                            ConfActivityNormal.this.mGuestJoinTip = new Builder(ConfActivityNormal.this).setMessage(C4558R.string.zm_alert_wait_content_87408).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ConfMgr.getInstance().loginWhenInWaitingRoom();
                                }
                            }).create();
                        }
                        if (!ConfActivityNormal.this.mGuestJoinTip.isShowing()) {
                            ConfActivityNormal.this.mGuestJoinTip.show();
                            return;
                        }
                        return;
                    }
                    return;
                }
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confContext != null && confContext.needRemindLoginWhenInWaitingRoom() && confStatusObj != null && !confStatusObj.isVerifyingMyGuestRole() && ConfActivityNormal.this.mSignIntoJoinTip != null && !ConfActivityNormal.this.mSignIntoJoinTip.isShowing()) {
                    ConfActivityNormal.this.mSignIntoJoinTip.show();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onHostAskUnmute(long j) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isSameUser(myself.getNodeId(), j)) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null && audioStatusObj.getIsMuted()) {
                    ConfAllowTalkDialog.dismiss(getSupportFragmentManager());
                    ConfAllowTalkDialog.showConfAllowTalkDialog(this);
                    refreshToolbar();
                    refreshPanelRecording();
                }
            }
        }
    }

    public boolean onUserEvent(int i, long j, int i2) {
        List list = (List) this.mUserInfoEventCaches.get(i);
        if (list == null) {
            list = new ArrayList();
            this.mUserInfoEventCaches.put(i, list);
        }
        list.add(new ConfUserInfoEvent(j, i2));
        this.mOptimizeUserEventHandler.removeMessages(i);
        this.mOptimizeUserEventHandler.sendEmptyMessageDelayed(i, (long) ZMConfiguration.CONF_FRENQUENCE_EVENT_DELAY);
        return true;
    }

    public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        String str5;
        int i;
        refreshToolbar();
        if (ConfLocalHelper.isInSilentMode()) {
            OnSilentView onSilentView = this.mOnSilentView;
            if (onSilentView != null) {
                onSilentView.refreshMessage();
                return true;
            }
        }
        if (isInDriveMode() || ConfLocalHelper.isDirectShareClient() || ConfLocalHelper.shouldExcludeMsgSender(this, j)) {
            return true;
        }
        boolean z = false;
        if (!isActive()) {
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
                String str6 = str;
                AccessibilityUtil.announceForAccessibilityCompat(ZMConfComponentMgr.getInstance().getFocusView(), getConfChatAccessibilityDescription(this, ConfChatItem.getConfChatItemFromMsgID(str, false)), true);
            }
            return true;
        }
        String str7 = str;
        if (str4.length() > 128) {
            StringBuilder sb = new StringBuilder();
            sb.append(str4.substring(0, 128));
            sb.append("...");
            str5 = sb.toString();
        } else {
            str5 = str4;
        }
        if (this.mToolbar.getVisibility() == 0) {
            if (ConfMgr.getInstance().isConfConnected() && ConfLocalHelper.isWebinar()) {
                ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                if (qAComponent != null) {
                    z = qAComponent.isWebinarAttendee();
                }
            }
            i = z ? C4558R.C4560id.btnChats : C4558R.C4560id.btnPList;
        } else {
            i = 0;
        }
        ChatTip.show(getSupportFragmentManager(), null, str2, str5, j, j2, i, str);
        return true;
    }

    public void onLaunchConfParamReady() {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onLaunchConfParamReady") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleOnLaunchConfParamReady();
            }
        });
    }

    public void onPTInviteRoomSystemResult(boolean z, String str, String str2, String str3, int i, int i2) {
        if (!z) {
            ConfMgr instance = ConfMgr.getInstance();
            InviteRoomDeviceInfo inviteRoomDeviceInfo = new InviteRoomDeviceInfo(str, str2, str3, i, i2);
            instance.inviteRoomSystemByCallout(inviteRoomDeviceInfo);
            sinkOnPTInviteRoomSystemResult(false, str, str2, str3, i, i2);
        }
    }

    public boolean onPTInvitationSent(String str) {
        sinkOnPTInvitationSent(str);
        return true;
    }

    public void onVerifyMyGuestRoleResult(boolean z, boolean z2) {
        sinkOnVerifyMyGuestRoleResult(z, z2);
    }

    public void onHostBindTelNotification(long j, long j2, boolean z) {
        String str;
        String str2;
        CmmUser cmmUser = new CmmUser(j);
        CmmUser cmmUser2 = new CmmUser(j2);
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(cmmUser2.getNodeId())) {
            String str3 = "";
            String str4 = "";
            if (z && cmmUser.isHost()) {
                str2 = TipMessageType.TIP_AUDIO_MERGED_BY_HOST.name();
                str = getString(C4558R.string.zm_msg_audio_merged_by_host_116180);
            } else if (z && cmmUser.isCoHost()) {
                String name = TipMessageType.TIP_AUDIO_MERGED_BY_COHOST.name();
                str2 = name;
                str = getString(C4558R.string.zm_msg_audio_merged_by_cohost_116180, new Object[]{cmmUser.getScreenName()});
            } else if (!z && cmmUser.isHost()) {
                str2 = TipMessageType.TIP_AUDIO_SEPARATED_BY_HOST.name();
                str = getString(C4558R.string.zm_msg_audio_separate_by_host_116180);
            } else if (z || !cmmUser.isCoHost()) {
                str2 = str3;
                str = str4;
            } else {
                String name2 = TipMessageType.TIP_AUDIO_SEPARATED_BY_COHOST.name();
                str2 = name2;
                str = getString(C4558R.string.zm_msg_audio_separate_by_cohost_116180, new Object[]{cmmUser.getScreenName()});
            }
            if (!"".equals(str) && !NormalMessageTip.isTipShown(str2)) {
                NormalMessageTip.show(getSupportFragmentManager(), str2, (String) null, str, 3000);
            }
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        requestPendingPermission();
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        ZMConfComponentMgr.getInstance().onAnnotateStartedUp(z, j);
    }

    public void onAnnotateShutDown() {
        ZMConfComponentMgr.getInstance().onAnnotateShutDown();
    }

    public void onWBPageChanged(int i, int i2, int i3, int i4) {
        ZMConfComponentMgr.getInstance().onWBPageChanged(i, i2, i3, i4);
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.RECORD_AUDIO".equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        if (i == 1016) {
                            toggleAudioStatus();
                        } else if (i == 1015) {
                            AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
                            if (audioObj != null) {
                                muteAudio(audioObj.isMuteOnEntryOn());
                            }
                        } else if (i == 1021) {
                            hostAskUnmute();
                        }
                    }
                } else if (!"android.permission.CAMERA".equals(strArr[i2])) {
                    KubiComponent kubiComponent = this.mKubiComponent;
                    if (kubiComponent == null || !kubiComponent.handleRequestPermissionResult(i, strArr[i2], iArr[i2])) {
                        ZMConfComponentMgr.getInstance().handleRequestPermissionResult(i, strArr[i2], iArr[i2]);
                    }
                } else if (iArr[i2] == 0 && !ZMConfComponentMgr.getInstance().handleRequestPermissionResult(i, strArr[i2], iArr[i2]) && i == 1017) {
                    showPreviewVideoDialog();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleOnLaunchConfParamReady() {
        initUIStatus();
        if (this.mVideoSceneMgr != null) {
            this.mVideoSceneMgr.onLaunchConfParamReady();
        }
    }

    /* access modifiers changed from: private */
    public void onBatchUserEvent(int i, @Nullable List<ConfUserInfoEvent> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            if (list.size() >= ZMConfiguration.CONF_CACHE_USER_NUMBERS) {
                ArrayList arrayList = new ArrayList(list.subList(0, ZMConfiguration.CONF_CACHE_USER_NUMBERS));
                onGroupUserEvent(i, arrayList);
                list.removeAll(arrayList);
                this.mOptimizeUserEventHandler.removeMessages(i);
                this.mOptimizeUserEventHandler.sendEmptyMessageDelayed(i, (long) ZMConfiguration.CONF_FRENQUENCE_EVENT_DELAY);
            } else {
                onGroupUserEvent(i, list);
                list.clear();
            }
        }
    }

    private void onGroupUserEvent(final int i, @NonNull List<ConfUserInfoEvent> list) {
        if (i == 0 && ConfMgr.getInstance().getClientUserCount() >= 2) {
            stopPlayDuduVoice();
        }
        final ArrayList arrayList = new ArrayList(list);
        checkAccessibilityForUserEvents(i, arrayList);
        getNonNullEventTaskManagerOrThrowException().pushLater("onUserEvent", new EventAction("onUserEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handleOnUserEvent(i, arrayList);
            }
        });
    }

    private void checkAccessibilityForUserEvents(int i, @NonNull List<ConfUserInfoEvent> list) {
        if (!isActive() && AccessibilityUtil.isSpokenFeedbackEnabled(this) && i == 0 && ConfLocalHelper.canControlWaitingRoom()) {
            CmmUser cmmUser = null;
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CmmUser userById = ConfMgr.getInstance().getUserById(((ConfUserInfoEvent) it.next()).getUserId());
                if (userById != null && userById.inSilentMode()) {
                    cmmUser = userById;
                    break;
                }
            }
            if (cmmUser != null) {
                AccessibilityUtil.announceForAccessibilityCompat(this.mConfView, (CharSequence) getString(C4558R.string.zm_accessibility_someone_enter_enter_waiting_room_149486, new Object[]{StringUtil.safeString(cmmUser.getScreenName())}));
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleOnUserEvent(int i, @Nullable List<ConfUserInfoEvent> list) {
        refreshToolbar();
        hideToolbarDelayed(5000);
        boolean z = false;
        CmmUser cmmUser = null;
        switch (i) {
            case 0:
                if (list != null && !list.isEmpty()) {
                    ToastTip.dismiss(getSupportFragmentManager());
                    boolean z2 = false;
                    for (ConfUserInfoEvent confUserInfoEvent : list) {
                        CmmUser userById = ConfMgr.getInstance().getUserById(confUserInfoEvent.getUserId());
                        if (userById != null) {
                            if (confUserInfoEvent.getFlag() == 0 && !userById.isFailoverUser() && !userById.isViewOnlyUserCanTalk()) {
                                cmmUser = userById;
                            }
                            if (userById.inSilentMode()) {
                                z2 = true;
                            }
                        }
                    }
                    if (cmmUser != null && ConfLocalHelper.needShowJoinLeaveTip()) {
                        String screenName = cmmUser.getScreenName();
                        if (!StringUtil.isEmptyOrNull(screenName)) {
                            ToastTip.showNow(getSupportFragmentManager(), getString(C4558R.string.zm_msg_user_joined_41162, new Object[]{screenName}), 3000, isInDriveMode());
                        }
                    }
                    if (z2 && ConfLocalHelper.canControlWaitingRoom()) {
                        showUserJoinWaitingListDialog();
                    }
                }
                CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                if (confContext != null && confContext.inSilentMode()) {
                    switchViewTo(ZMConfEnumViewMode.SILENT_VIEW);
                } else if (confContext == null || !confContext.isDirectShareClient()) {
                    switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
                } else {
                    switchViewTo(ZMConfEnumViewMode.PRESENT_ROOM_LAYER);
                }
                this.mPanelConnecting.setVisibility(8);
                if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) == 2) {
                    checkStartDrivingModeOnConfReady();
                    break;
                }
                break;
            case 1:
                handleAttendeeLeft();
                CmmUserList userList = ConfMgr.getInstance().getUserList();
                if (userList != null && list != null) {
                    boolean z3 = false;
                    for (ConfUserInfoEvent userId : list) {
                        CmmUser leftUserById = userList.getLeftUserById(userId.getUserId());
                        if (leftUserById != null && !leftUserById.isFailoverUser()) {
                            if (!leftUserById.isViewOnlyUserCanTalk()) {
                                cmmUser = leftUserById;
                            }
                            if (leftUserById.inSilentMode()) {
                                z3 = true;
                            }
                        }
                    }
                    ZMConfComponentMgr.getInstance().sinkInRefreshFeccUI();
                    if (cmmUser != null && ConfLocalHelper.needShowJoinLeaveTip()) {
                        String screenName2 = cmmUser.getScreenName();
                        if (!StringUtil.isEmptyOrNull(screenName2)) {
                            ToastTip.showNow(getSupportFragmentManager(), getString(C4558R.string.zm_msg_user_left_41162, new Object[]{screenName2}), 3000, isInDriveMode());
                        }
                    }
                    if (z3 && ConfLocalHelper.canControlWaitingRoom()) {
                        updateJoinWaitingList();
                        break;
                    }
                } else {
                    return;
                }
            case 2:
                if (list != null && !list.isEmpty()) {
                    for (ConfUserInfoEvent userId2 : list) {
                        CmmUser userById2 = ConfMgr.getInstance().getUserById(userId2.getUserId());
                        if (userById2 != null && !userById2.inSilentMode()) {
                            z = true;
                        }
                    }
                    if (z && ConfLocalHelper.canControlWaitingRoom()) {
                        updateJoinWaitingList();
                        break;
                    }
                }
        }
        this.mVideoSceneMgr.onGroupUserEvent(i, list);
        BOComponent bOComponent = this.mBOComponent;
        if (bOComponent != null) {
            bOComponent.onUserEventForBO(i, list);
        }
    }

    private void sinkConfReconnect(final long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onConfReconnect") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).onConfReconnect(j);
            }
        });
    }

    public void muteAudio(boolean z) {
        super.muteAudio(z);
        hideToolbarDelayed(5000);
    }

    public void onClickBtnAudio() {
        if (ConfMgr.getInstance().isConfConnected()) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    if (VERSION.SDK_INT < 23 || audioStatusObj.getAudiotype() != 0 || zm_checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                        toggleAudioStatus();
                    } else {
                        requestPermission("android.permission.RECORD_AUDIO", 1016, 0);
                    }
                }
            }
        }
    }

    public void onHostAskUnmute() {
        CmmAudioStatus mySelfAudioStatus = ConfLocalHelper.getMySelfAudioStatus();
        if (mySelfAudioStatus != null) {
            if (VERSION.SDK_INT < 23 || mySelfAudioStatus.getAudiotype() != 0 || zm_checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                hostAskUnmute();
            } else {
                requestPermission("android.permission.RECORD_AUDIO", 1021, 0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void hostAskUnmute() {
        CmmAudioStatus mySelfAudioStatus = ConfLocalHelper.getMySelfAudioStatus();
        if (mySelfAudioStatus != null) {
            if (mySelfAudioStatus.getAudiotype() != 2) {
                super.hostAskUnmute();
                return;
            }
            showAudioOptions();
            this.mHandler.removeCallbacks(this.mUnmuteMyselfRunnable);
            this.mHandler.postDelayed(this.mUnmuteMyselfRunnable, 200);
        }
    }

    private void toggleAudioStatus() {
        CmmAudioStatus mySelfAudioStatus = ConfLocalHelper.getMySelfAudioStatus();
        if (mySelfAudioStatus != null) {
            if (mySelfAudioStatus.getAudiotype() != 2) {
                boolean isMuted = mySelfAudioStatus.getIsMuted();
                muteAudio(!isMuted);
                ZMConfEventTracking.logToggleAudio(!isMuted);
                return;
            }
            showAudioOptions();
        }
    }

    private void showAudioOptions() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                if (meetingItem.getIsSelfTelephonyOn()) {
                    showSelfTelephoneInfo(meetingItem.getOtherTeleConfInfo());
                } else {
                    if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                        ConfLocalHelper.connectVoIP();
                    } else {
                        showAudioBottomSheet(isBottombarShowing() ? C4558R.C4560id.btnAudio : 0);
                    }
                    disableToolbarAutoHide();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void showAudioBottomSheet(int i) {
        if (ConfLocalHelper.isNoneAudioTypeSupport()) {
            ZMAlertDialog create = new Builder(this).setTitle((CharSequence) getString(C4558R.string.zm_no_audio_type_support_129757)).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
            create.setCancelable(true);
            create.setCanceledOnTouchOutside(false);
            create.show();
            return;
        }
        AudioTip.show(getSupportFragmentManager(), i);
    }

    /* access modifiers changed from: private */
    public void showSelfTelephoneInfo(@Nullable String str) {
        if (str != null) {
            new Builder(this).setTitle(C4558R.string.zm_title_audio_conference).setMessage(str).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    public void onClickParticipants() {
        showPList();
    }

    public void onClickChats() {
        if (ConfMgr.getInstance().isConfConnected()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isWebinar()) {
                if (UIMgr.isLargeMode(this)) {
                    ConfChatFragment.showAsFragment(getSupportFragmentManager(), 0);
                    return;
                }
                ConfChatFragment.showAsActivity((ZMActivity) this, 0, (ConfChatAttendeeItem) null);
                hideToolbarDelayed(5000);
            }
        }
    }

    public void onClickMore() {
        if (InterpretationTip.isShown(getSupportFragmentManager())) {
            InterpretationTip.dismiss(getSupportFragmentManager());
        }
        MoreTip.show(getSupportFragmentManager(), isBottombarShowing() ? C4558R.C4560id.btnMore : 0);
    }

    private void onClickSwitchToShare() {
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
        if (videoSceneMgr != null && !videoSceneMgr.isViewingSharing()) {
            videoSceneMgr.switchToDefaultScene();
        }
    }

    private void onSwitchToOrOutDriverMode(boolean z) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (z) {
                showToolbar(false, false);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (ConfActivityNormal.this.isActive()) {
                            ConfActivityNormal.this.dismissTempTips();
                        }
                    }
                });
            } else {
                if (confContext.isAlwaysShowMeetingToolbar()) {
                    showToolbar(true, false);
                }
                updateRaiseHandStatus();
            }
            ParamsList appContextParams = confContext.getAppContextParams();
            appContextParams.putInt("drivingMode", z ? 1 : 0);
            confContext.setAppContextParams(appContextParams);
            refreshPanelRecording();
            refreshAudioWatermark();
            ZMConfComponentMgr.getInstance().refreshAudioSharing(false);
            LiveStreamComponent liveStreamComponent = this.mLiveStreamComponent;
            if (liveStreamComponent != null) {
                liveStreamComponent.onLiveStreamStatusChange();
            }
        }
    }

    public void showPList() {
        if (UIMgr.isLargeMode(this)) {
            PListFragment.show(getSupportFragmentManager(), C4558R.C4560id.btnPList);
            Runnable runnable = g_hideToolbarRunnable;
            if (runnable != null) {
                g_handler.removeCallbacks(runnable);
                return;
            }
            return;
        }
        PListActivity.show(this, 1001);
        hideToolbarDelayed(5000);
    }

    public void showAttendeeList() {
        QAWebinarAttendeeListFragment.showAsActivity(this, 0);
        hideToolbarDelayed(5000);
    }

    private void onClickBtnAudioSource() {
        ConfLocalHelper.switchAudio(this);
    }

    private void onClickBtnQA() {
        ConfLocalHelper.showQA(this);
    }

    private void onClickPanelCurUserRecording() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && (!confContext.isAutoCMRForbidManualStop() || recordMgr.isCMRPaused())) {
                RecordControlDialog.show(getSupportFragmentManager());
                showCMRNotification();
            }
        }
    }

    @SuppressLint({"Range"})
    public void onToolbarVisibilityChanged(boolean z) {
        ZMConfComponentMgr.getInstance().onToolbarVisibilityChanged(z);
        if (z) {
            if (this.mToolbar.getVisibility() != 0) {
                this.mToolbarHeight = 0;
            } else {
                int height = this.mToolbar.getHeight();
                if (height == 0) {
                    this.mToolbar.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE));
                    height = this.mToolbar.getMeasuredHeight();
                }
                this.mToolbarHeight = height;
            }
            if (this.mTopbar.getVisibility() != 0) {
                this.mTopbarHeight = 0;
            } else {
                int height2 = this.mTopbar.getHeight();
                if (height2 == 0) {
                    this.mTopbar.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE));
                    height2 = this.mTopbar.getMeasuredHeight();
                }
                this.mTopbarHeight = height2;
            }
        } else {
            this.mToolbarHeight = 0;
            this.mTopbarHeight = 0;
        }
        if (this.mVideoSceneMgr != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfActivityNormal.this.mVideoSceneMgr.onConfUIRelayout(ConfActivityNormal.this);
                }
            });
        }
        if (!UIMgr.isLargeMode(this)) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            this.mPanelSwitchSceneButtons.setVisibility((z || (confContext != null && confContext.isInVideoCompanionMode())) ? 4 : 0);
        }
        if (!(this.mVideoSceneMgr == null || this.mVideoSceneMgr.getActiveScene() == null)) {
            this.mVideoSceneMgr.getActiveScene().updateAccessibilitySceneDescription();
            this.mVideoSceneMgr.announceAccessibilityAtActiveScene();
        }
        updateSwitchToShareButton();
        ZMConfComponentMgr.getInstance().refreshAudioSharing(false);
        this.mToolbar.refreshBtnVideo();
        refreshMainVideoAudioStatus();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.panelWaitingShare || id == C4558R.C4560id.panelSharingTitle || id == C4558R.C4560id.panelTop) {
            switchToolbar();
        } else if (id == C4558R.C4560id.btnLeave) {
            onClickLeave();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.imgAudioSource) {
            onClickBtnAudioSource();
        } else if (id == C4558R.C4560id.btnQA) {
            onClickBtnQA();
        } else if (id == C4558R.C4560id.panelCurUserRecording) {
            onClickPanelCurUserRecording();
        } else if (id == C4558R.C4560id.btnSwitchToShare) {
            onClickSwitchToShare();
        } else if (id == C4558R.C4560id.btnBroadcast) {
            onClickBroadcast();
        }
    }

    private void updateSwitchToShareButton() {
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
        if (videoSceneMgr != null) {
            AbsVideoScene activeScene = videoSceneMgr.getActiveScene();
            if (activeScene != null) {
                if (!(activeScene instanceof NormalVideoScene) || !ConfShareLocalHelper.isOtherScreenSharing() || !isToolbarShowing()) {
                    this.mBtnSwitchToShare.setVisibility(8);
                } else {
                    this.mBtnSwitchToShare.setVisibility(0);
                }
            }
        }
    }

    public void onVideoSceneChanged(AbsVideoScene absVideoScene, AbsVideoScene absVideoScene2) {
        refreshToolbar();
        refreshPanelRecording();
        refreshAudioWatermark();
        ZMConfComponentMgr.getInstance().sinkInRefreshFeccUI();
        checkClosedCaption();
        boolean z = absVideoScene instanceof DriverModeVideoScene;
        if (absVideoScene2 instanceof DriverModeVideoScene) {
            onSwitchToOrOutDriverMode(true);
        } else if (z) {
            onSwitchToOrOutDriverMode(false);
        } else {
            ZMConfComponentMgr.getInstance().refreshAudioSharing(false);
        }
        if (absVideoScene2 instanceof ShareVideoScene) {
            ZMConfComponentMgr.getInstance().onSwitchToOrOutShare(true);
        } else if (absVideoScene instanceof ShareVideoScene) {
            ZMConfComponentMgr.getInstance().onSwitchToOrOutShare(false);
        }
        updateLayoutMode(absVideoScene, absVideoScene2);
        ZMConfComponentMgr.getInstance().showAnnotateViewWhenSceneChanged();
        refreshMainVideoAudioStatus(absVideoScene, absVideoScene2);
    }

    public void onDraggingVideoScene() {
        this.mPanelRecording.setVisibility(8);
        this.mPanelCurUserRecording.setVisibility(8);
        this.mViewAudioWatermark.setVisibility(8);
        if (isToolbarShowing()) {
            showToolbar(false, false);
        }
    }

    public void onDropVideoScene(boolean z) {
        refreshPanelRecording();
        refreshAudioWatermark();
    }

    /* access modifiers changed from: private */
    public void switchToolbar() {
        if (isInDriveMode()) {
            showToolbar(false, false);
        } else if (ConfLocalHelper.isDirectShareClient()) {
            showToolbar(false, false);
        } else {
            if (!ConfMgr.getInstance().isConfConnected() || isCallingOut()) {
                showToolbar(false, false);
                disableToolbarAutoHide();
            } else {
                boolean z = !isToolbarShowing();
                showToolbar(z, true);
                if (z) {
                    refreshToolbar();
                    hideToolbarDelayed(5000);
                    this.mToolbar.focusFirstVisibleButton();
                }
            }
        }
    }

    private void focusConfToolbarButton(int i) {
        this.mToolbar.focus(i);
    }

    private void showTitlebar(boolean z) {
        View findViewById = this.mPanelTools.findViewById(C4558R.C4560id.titleBar);
        if (findViewById != null) {
            findViewById.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: private */
    public void showTopToolbar(boolean z) {
        this.mPanelTools.setVisibilityForTopToolbar(z ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public boolean hideToolbarMenus() {
        boolean hide = PListFragment.hide(getSupportFragmentManager());
        if (InviteFragment.hide(getSupportFragmentManager())) {
            UIUtil.closeSoftKeyboard(this, getWindow().getDecorView());
            hide = true;
        }
        if (MeetingRunningInfoFragment.dismiss(getSupportFragmentManager())) {
            return true;
        }
        return hide;
    }

    public void onPListTipClosed() {
        hideToolbarDelayed(5000);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (!ZMConfComponentMgr.getInstance().onActivityResult(i, i2, intent)) {
            PollComponent pollComponent = this.mPollComponent;
            if (pollComponent == null || !pollComponent.onActivityResult(i, i2, intent)) {
                KubiComponent kubiComponent = this.mKubiComponent;
                if (kubiComponent != null && !kubiComponent.onActivityResult(i, i2, intent)) {
                }
            }
        }
    }

    private void showTipWaitingToInvite() {
        showToolbar(true, false);
        NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_WAITING_TO_INVITE.name(), getString(C4558R.string.zm_msg_conf_waiting_to_invite_title), getString(C4558R.string.zm_msg_conf_waiting_to_invite), C4558R.C4560id.btnPList, UIMgr.isLargeMode(this) ? 1 : 3);
    }

    public void showTipMicEchoDetected() {
        showToolbar(true, false);
        NormalMessageTip.show(getSupportFragmentManager(), TipMessageType.TIP_MIC_ECHO_DETECTED.name(), (String) null, getString(C4558R.string.zm_msg_voip_disconnected_for_echo_detected), isBottombarShowing() ? C4558R.C4560id.btnAudio : 0, UIMgr.isLargeMode(this) ? 1 : 3);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (ZMConfComponentMgr.getInstance().onKeyDown(i, keyEvent)) {
            return true;
        }
        if (isToolbarShowing()) {
            hideToolbarDelayed(5000);
        }
        if (i != 4) {
            switch (i) {
                case 19:
                case 20:
                    if (isArrowAcceleratorDisabled()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    if (NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_WAITING_TO_INVITE.name())) {
                        NormalMessageTip.dismiss(getSupportFragmentManager(), TipMessageType.TIP_WAITING_TO_INVITE.name());
                        this.mToolbar.focus(8);
                    } else if (NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_MIC_ECHO_DETECTED.name())) {
                        NormalMessageTip.dismiss(getSupportFragmentManager(), TipMessageType.TIP_MIC_ECHO_DETECTED.name());
                        this.mToolbar.focus(2);
                    } else if (NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_RECONNECT_AUDIO.name())) {
                        NormalMessageTip.dismiss(getSupportFragmentManager(), TipMessageType.TIP_RECONNECT_AUDIO.name());
                        this.mToolbar.focus(2);
                    }
                    if (isToolbarShowing()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    switchToolbar();
                    return true;
                case 21:
                    if (isArrowAcceleratorDisabled()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    if (isToolbarShowing()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    this.mVideoSceneMgr.scrollHorizontal(true);
                    return true;
                case 22:
                    if (isArrowAcceleratorDisabled()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    if (isToolbarShowing()) {
                        return super.onKeyDown(i, keyEvent);
                    }
                    this.mVideoSceneMgr.scrollHorizontal(false);
                    return true;
                default:
                    return super.onKeyDown(i, keyEvent);
            }
        } else if (hasTipPointToToolbar()) {
            dismissTempTips();
            return true;
        } else if (!isToolbarShowing()) {
            return super.onKeyDown(i, keyEvent);
        } else {
            switchToolbar();
            return true;
        }
    }

    public void onClickAttendeeRaiseHand() {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null && raiseHandAPIObj.raiseHand()) {
            updateAttendeeRaiseHandButton();
            focusConfToolbarButton(64);
        }
    }

    public void onClickAttendeeLowerHand() {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null && raiseHandAPIObj.lowerHand("")) {
            updateAttendeeRaiseHandButton();
            focusConfToolbarButton(64);
        }
    }

    public void onClickQA() {
        ConfLocalHelper.showQA(this);
    }

    public boolean dismissTempTips() {
        if (NormalMessageTip.hasTip(getSupportFragmentManager(), TipMessageType.TIP_BO_JOIN_BREAKOUT_SESSION.name())) {
            hideToolbarDelayed(5000);
        }
        return super.dismissTempTips();
    }

    private void updatePracticeModeView() {
        if (this.mPracticeModeView != null && this.mBtnBroadcast != null) {
            if (canShowBroadcastButton()) {
                this.mPracticeModeView.setVisibility(0);
                this.mBtnBroadcast.setOnClickListener(this);
                showToolbar(true, false);
                disableToolbarAutoHide();
                return;
            }
            this.mPracticeModeView.setVisibility(8);
        }
    }

    private void onClickBroadcast() {
        this.mBtnBroadcast.setVisibility(8);
        this.mProgressBarBroadcasting.setVisibility(0);
        ConfMgr.getInstance().handleConfCmd(112);
    }

    private boolean canShowBroadcastButton() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = false;
        if (confContext == null || !confContext.isWebinar()) {
            return false;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null || (!myself.isHost() && !myself.isCoHost())) {
            return false;
        }
        if (confContext.isPracticeSessionFeatureOn() && confStatusObj.isInPracticeSession()) {
            z = true;
        }
        return z;
    }

    private void sinkConfPracticeSessionStatusChanged(final boolean z) {
        getNonNullEventTaskManagerOrThrowException().push("handlerConfPracticeSessionStatusChanged", new EventAction("handlerConfPracticeSessionStatusChanged") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ConfActivityNormal) iUIElement).handlerConfPracticeSessionStatusChanged(z);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handlerConfPracticeSessionStatusChanged(boolean z) {
        if (!z) {
            View view = this.mPracticeModeView;
            if (view != null && this.mBroadcastingView != null && view.getVisibility() == 0) {
                showToolbar(true, false);
                hideToolbarDelayed(5000);
                this.mPracticeModeView.setVisibility(8);
                this.mBroadcastingView.setVisibility(0);
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        ConfActivityNormal.this.mBroadcastingView.setVisibility(8);
                    }
                }, 5000);
            }
        } else if (this.mBtnBroadcast != null && this.mProgressBarBroadcasting != null && this.mPracticeModeView.getVisibility() == 0) {
            showToolbar(true, false);
            this.mBtnBroadcast.setVisibility(0);
            this.mProgressBarBroadcasting.setVisibility(8);
        }
    }

    public void enterHostKeyToClaimHost() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.hasHostinMeeting()) {
            HostKeyEnterDialog.show(getSupportFragmentManager(), ConfLocalHelper.getMeetingHostName(), HostKeyEnterDialog.class.getName());
        }
    }

    public void onBOCountdown(String str) {
        if (this.mTxtCountdown.getVisibility() != 0) {
            this.mTxtCountdown.setVisibility(0);
        }
        if (this.mTxtTimer.getVisibility() != 8) {
            this.mHandler.removeCallbacks(this.mTimerRunnable);
            this.mHandler.post(new Runnable() {
                public void run() {
                    ConfActivityNormal.this.mTxtTimer.setVisibility(8);
                }
            });
        }
        this.mTxtCountdown.setText(getString(C4558R.string.zm_bo_countdown, new Object[]{str}));
    }

    public void showVerifyHostKeyDialog() {
        if (this.mVerifyHostKeyWaitingDialog == null) {
            this.mVerifyHostKeyWaitingDialog = new ProgressDialog(this);
            this.mVerifyHostKeyWaitingDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    ConfActivityNormal.this.mVerifyHostKeyWaitingDialog = null;
                }
            });
            this.mVerifyHostKeyWaitingDialog.requestWindowFeature(1);
            this.mVerifyHostKeyWaitingDialog.setMessage(getString(C4558R.string.zm_msg_verifying_hostkey));
            this.mVerifyHostKeyWaitingDialog.setCanceledOnTouchOutside(false);
            this.mVerifyHostKeyWaitingDialog.setCancelable(true);
            this.mVerifyHostKeyWaitingDialog.show();
        }
    }

    private void dismissVerifyHostKeyDialog() {
        ProgressDialog progressDialog = this.mVerifyHostKeyWaitingDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mVerifyHostKeyWaitingDialog = null;
        }
    }

    public void onClosedCaptionMessageReceived(String str, String str2, long j) {
        onRealtimeClosedCaptionMessageReceived(str2);
    }

    public void onRealtimeClosedCaptionMessageReceived(String str) {
        if (isNeedShowClosedCaption()) {
            onClosedCaptionMessageReceived(str, true);
        }
    }

    public void onLiveTranscriptionClosedCaptionMessageReceived(CCMessage cCMessage, int i) {
        if (isNeedShowClosedCaption()) {
            if (i == 1 || i == 2) {
                onClosedCaptionMessageReceived(cCMessage.getContent(), false);
            }
        }
    }

    public void onClosedCaptionMessageReceived(final String str, final boolean z) {
        this.mClosedCaptionContent = str;
        getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction() {
            public void run(IUIElement iUIElement) {
                ConfActivityNormal.this.updateClosedCaption(str, z);
                if (!StringUtil.isEmptyOrNull(str)) {
                    ConfActivityNormal.this.mHandler.removeCallbacks(ConfActivityNormal.this.mClosedCaptionTimeoutRunnable);
                    ConfActivityNormal.this.mHandler.postDelayed(ConfActivityNormal.this.mClosedCaptionTimeoutRunnable, (long) ZMConfiguration.CONF_CLOSE_CAPTION_DURATION);
                }
            }
        });
    }

    private boolean isNeedShowClosedCaption() {
        if (!PreferenceUtil.readBooleanValue(PreferenceUtil.CLOSED_CAPTION_ENABLED, true)) {
            return false;
        }
        return !isInDriveMode();
    }

    private void checkClosedCaption() {
        if (StringUtil.isEmptyOrNull(this.mClosedCaptionContent)) {
            this.mClosedCaptionView.setVisibility(8);
        } else {
            updateClosedCaption(this.mClosedCaptionContent, false);
        }
    }

    private void checkShowTimer() {
        this.mHandler.removeCallbacks(this.mTimerRunnable);
        if (PreferenceUtil.readBooleanValue(PreferenceUtil.SHOW_TIMER_ENABLED, false)) {
            ConfMgr.getInstance().setShowClockInMeeting(true);
            this.mTxtTimer.setVisibility(0);
            this.mHandler.post(this.mTimerRunnable);
            return;
        }
        ConfMgr.getInstance().setShowClockInMeeting(false);
        this.mTxtTimer.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void updateClosedCaption(String str, boolean z) {
        if (this.mClosedCaptionView != null) {
            if (StringUtil.isEmptyOrNull(str)) {
                this.mClosedCaptionView.setVisibility(8);
                this.mClosedCaptionContent = null;
                this.mHandler.removeCallbacks(this.mClosedCaptionTimeoutRunnable);
                return;
            }
            this.mClosedCaptionView.setText(str);
            this.mClosedCaptionView.setVisibility(0);
            if (z) {
                this.mClosedCaptionView.setContentDescription(str);
                if (OsUtil.isAtLeastJB()) {
                    this.mClosedCaptionView.sendAccessibilityEvent(16384);
                }
            }
        }
    }

    private void showUserJoinWaitingListDialog() {
        String str;
        ZMAlertDialog zMAlertDialog = this.mJoinWaitingListDlg;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mJoinWaitingListDlg.dismiss();
        }
        List clientOnHoldUserList = ConfMgr.getInstance().getClientOnHoldUserList();
        int size = clientOnHoldUserList.size();
        if (size == 1) {
            str = getString(C4558R.string.zm_msg_xxx_join_meeting_in_waiting_list, new Object[]{((CmmUser) clientOnHoldUserList.get(0)).getScreenName()});
        } else if (size > 1) {
            str = getResources().getQuantityString(C4558R.plurals.zm_msg_join_meeting_in_waiting_list, size, new Object[]{Integer.valueOf(size)});
        } else {
            return;
        }
        Builder negativeButton = new Builder(this).setTitle((CharSequence) str).setNegativeButton(C4558R.string.zm_btn_see_waiting_list, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivityNormal.this.showPList();
            }
        });
        if (size == 1) {
            final long nodeId = ((CmmUser) clientOnHoldUserList.get(0)).getNodeId();
            negativeButton.setPositiveButton(C4558R.string.zm_btn_admit, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfMgr.getInstance().handleUserCmd(43, nodeId);
                }
            });
        }
        this.mJoinWaitingListDlg = negativeButton.create();
        this.mJoinWaitingListDlg.show();
    }

    private void updateJoinWaitingList() {
        ZMAlertDialog zMAlertDialog = this.mJoinWaitingListDlg;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            showUserJoinWaitingListDialog();
        }
    }

    private void updateLayoutMode(AbsVideoScene absVideoScene, @Nullable AbsVideoScene absVideoScene2) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (absVideoScene2 != null && confStatusObj != null && confStatusObj.isLiveOn()) {
            if (absVideoScene2 instanceof GalleryVideoScene) {
                confStatusObj.setLiveLayoutMode(false);
            } else {
                confStatusObj.setLiveLayoutMode(true);
            }
        }
    }
}
