package com.zipow.videobox.confapp.component;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoCapturer;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.common.conf.MyBandwidthLimitInfo;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IVideoFECCCmdListener;
import com.zipow.videobox.confapp.SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.component.sink.video.IConfCamera;
import com.zipow.videobox.dialog.ZMSpotlightVideoDialog;
import com.zipow.videobox.fragment.PListFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMConfCameraUtils;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.CompanionModeView;
import com.zipow.videobox.view.FeccMessageButtonTip;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.VideoTip;
import com.zipow.videobox.view.ZMFeccView;
import com.zipow.videobox.view.ZMFeccView.FeccListener;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.NormalVideoScene;
import com.zipow.videobox.view.video.VideoSceneMgr;
import java.util.HashMap;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.DeviceInfoUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.ViewPressEffectHelper;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

class ZmConfVideoComponent extends ZmBaseConfVideoComponent implements FeccListener, IVideoFECCCmdListener, IConfCamera {
    /* access modifiers changed from: private */
    public ZMAlertDialog cannotStartVideoDlg;
    @Nullable
    private ImageView mBandwidthLimitIcon;
    @Nullable
    private View mBtnSwitchCamera;
    @Nullable
    private CompanionModeView mCompanionModeView;
    private boolean mIsVideoStarted = false;
    /* access modifiers changed from: private */
    public final HashMap<Long, ZMAlertDialog> mMapFeccDialogs = new HashMap<>();
    private int mMyVideoRotation = 0;
    @Nullable
    private ZMFeccView mPanelFecc;
    /* access modifiers changed from: private */
    public ZMAlertDialog videoLimitDlg;

    public ZmConfVideoComponent(@NonNull ConfActivity confActivity) {
        super(confActivity);
    }

    public void onClickSwitchCamera() {
        if (this.mContext != null && this.mBtnSwitchCamera != null) {
            int numberOfCameras = ZMConfCameraUtils.getNumberOfCameras();
            if (numberOfCameras == 2) {
                if (switchToNextCamera() && AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
                    if (ZMConfCameraUtils.getCameraFace() == 1) {
                        if (!AccessibilityUtil.getIsAccessibilityFocused(this.mBtnSwitchCamera)) {
                            AccessibilityUtil.announceForAccessibilityCompat(this.mBtnSwitchCamera, C4558R.string.zm_accessibility_selected_front_camera_23059);
                        }
                        this.mBtnSwitchCamera.setContentDescription(this.mContext.getString(C4558R.string.zm_accessibility_current_front_camera_23059));
                    } else {
                        if (!AccessibilityUtil.getIsAccessibilityFocused(this.mBtnSwitchCamera)) {
                            AccessibilityUtil.announceForAccessibilityCompat(this.mBtnSwitchCamera, C4558R.string.zm_accessibility_selected_back_camera_23059);
                        }
                        this.mBtnSwitchCamera.setContentDescription(this.mContext.getString(C4558R.string.zm_accessibility_current_back_camera_23059));
                    }
                }
                this.mContext.hideToolbarDefaultDelayed();
            } else if (numberOfCameras > 2) {
                VideoTip.show(this.mContext.getSupportFragmentManager(), C4558R.C4560id.btnSwitchCamera, 1, false);
            }
        }
    }

    public boolean canSwitchCamera() {
        return this.mbSendingVideo && ZMConfCameraUtils.getNumberOfCameras() >= 2 && ConfMgr.getInstance().isConfConnected() && !ConfMgr.getInstance().isCallingOut();
    }

    public void switchCamera(@NonNull String str) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            ConfMgr.getInstance().getConfDataHelper().setIsVideoOnBeforeShare(false);
            AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
            if (absVideoSceneMgr != null) {
                absVideoSceneMgr.beforeSwitchCamera();
            }
            if (!ConfMgr.getInstance().canUnmuteMyVideo()) {
                alertCameraDisabledByHost();
            } else if (!videoObj.switchCamera(str) && !videoObj.isVideoStarted()) {
                alertStartCameraFailed();
            }
            if (absVideoSceneMgr != null) {
                absVideoSceneMgr.afterSwitchCamera();
            }
            this.mMyVideoRotation = ZMConfCameraUtils.getCurrentMyVideoRotation(this.mContext);
            rotateMyVideo(this.mMyVideoRotation);
        }
    }

    public void refreshSwitchCameraButton() {
        if (this.mContext != null) {
            int i = 0;
            boolean z = canSwitchCamera() && !this.mContext.getConfParams().isSwitchCameraButtonDisabled();
            View view = this.mBtnSwitchCamera;
            if (view != null) {
                if (!z) {
                    i = 8;
                }
                view.setVisibility(i);
                if (AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
                    this.mBtnSwitchCamera.setContentDescription(this.mContext.getString(ZMConfCameraUtils.getCameraFace() == 1 ? C4558R.string.zm_accessibility_current_front_camera_23059 : C4558R.string.zm_accessibility_current_back_camera_23059));
                }
            }
        }
    }

    public void onConfReady() {
        sinkBandwidthLimitStatusChanged();
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        if ("android.permission.CAMERA".equals(str) && i2 == 0) {
            if (i == 1016) {
                toggleVideoStatus();
                return true;
            } else if (i == 1015) {
                startMyVideo();
                return true;
            }
        }
        return false;
    }

    public void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode) {
        if (zMConfEnumViewMode == ZMConfEnumViewMode.CONF_VIEW) {
            if (this.mCompanionModeView != null) {
                if (!ConfLocalHelper.isInVideoCompanionMode() || ConfShareLocalHelper.isOtherScreenSharing()) {
                    this.mCompanionModeView.setVisibility(8);
                } else {
                    this.mCompanionModeView.setVisibility(0);
                    this.mCompanionModeView.updateData();
                }
            }
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.SILENT_VIEW) {
            if (this.askStartVideoDlg != null && this.askStartVideoDlg.isShowing()) {
                this.askStartVideoDlg.dismiss();
                this.askStartVideoDlg = null;
            }
            ZMAlertDialog zMAlertDialog = this.cannotStartVideoDlg;
            if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                this.cannotStartVideoDlg.dismiss();
                this.cannotStartVideoDlg = null;
            }
            ZMAlertDialog zMAlertDialog2 = this.videoLimitDlg;
            if (zMAlertDialog2 != null && zMAlertDialog2.isShowing()) {
                this.videoLimitDlg.dismiss();
                this.videoLimitDlg = null;
            }
        }
    }

    public void onActivityCreate(Bundle bundle) {
        if (this.mContext != null) {
            super.onActivityCreate(bundle);
            this.mCompanionModeView = (CompanionModeView) this.mContext.findViewById(C4558R.C4560id.companionModeView);
            this.mBandwidthLimitIcon = (ImageView) this.mContext.findViewById(C4558R.C4560id.imgBandwidthLimit);
            this.mBtnSwitchCamera = this.mContext.findViewById(C4558R.C4560id.btnSwitchCamera);
            View view = this.mBtnSwitchCamera;
            if (view != null) {
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        ZmConfVideoComponent.this.onClickSwitchCamera();
                    }
                });
                ViewPressEffectHelper.attach(this.mBtnSwitchCamera);
                this.mBtnSwitchCamera.setContentDescription(this.mContext.getString(ZMConfCameraUtils.getCameraFace() == 1 ? C4558R.string.zm_accessibility_current_front_camera_23059 : C4558R.string.zm_accessibility_current_back_camera_23059));
            }
            this.mPanelFecc = (ZMFeccView) this.mContext.findViewById(C4558R.C4560id.panelFecc);
            this.mPanelFecc.setListener(this);
            this.mPanelFecc.setVisibility(8);
            ConfUI.getInstance().addVideoFECCCmdListener(this);
            this.mIsVideoStarted = ConfMgr.getInstance().getConfDataHelper().ismIsVideoStarted();
        }
    }

    public void onActivityDestroy() {
        ConfUI.getInstance().removeVideoFECCCmdListener(this);
        this.mPanelFecc = null;
        this.mBtnSwitchCamera = null;
        this.mBandwidthLimitIcon = null;
        this.mCompanionModeView = null;
        super.onActivityDestroy();
    }

    public void onActivityResume() {
        updateVideoStatus();
        this.mIsVideoStarted = ConfMgr.getInstance().getConfDataHelper().ismIsVideoStarted();
        this.mMyVideoRotation = ZMConfCameraUtils.getCurrentMyVideoRotation(this.mContext);
        rotateMyVideo(this.mMyVideoRotation);
    }

    public void sinkBeforeMyStartShare() {
        if (this.mVideoView != null) {
            this.mVideoView.setVisibility(8);
        }
    }

    public void sinkMyShareStatueChanged(boolean z) {
        if (this.mVideoView != null && !z) {
            this.mVideoView.setVisibility(0);
            if (this.mRenderer != null) {
                this.mRenderer.resumeRenderer();
            }
        }
    }

    public void pauseRenderer() {
        if (this.mRenderer != null) {
            this.mRenderer.pauseRenderer();
        }
    }

    public void sinkInResumeVideo() {
        if (this.mContext != null && this.mVideoView != null) {
            this.mVideoView.onResume();
            if (this.mRenderer != null) {
                this.mRenderer.resumeRenderer();
            }
            this.mContext.getWindow().getDecorView().postDelayed(new Runnable() {
                public void run() {
                    if (ZmConfVideoComponent.this.mVideoView != null && ZmConfVideoComponent.this.mContext != null && ZmConfVideoComponent.this.mContext.isActive() && !ConfUI.getInstance().isLeaveComplete() && ZmConfVideoComponent.this.mAbsVideoSceneMgr != null) {
                        if (ZmConfVideoComponent.this.mVideoView.getVisibility() == 0) {
                            ZmConfVideoComponent.this.checkStartVideo();
                            ZmConfVideoComponent.this.mAbsVideoSceneMgr.onConfActivityResume();
                        }
                        if (ZmConfVideoComponent.this.mAbsVideoSceneMgr.isInDriveModeScence()) {
                            boolean readBooleanValue = PreferenceUtil.readBooleanValue(PreferenceUtil.DRIVE_MODE_ENABLED, true);
                            if (!readBooleanValue && (ZmConfVideoComponent.this.mAbsVideoSceneMgr instanceof VideoSceneMgr)) {
                                ((VideoSceneMgr) ZmConfVideoComponent.this.mAbsVideoSceneMgr).switchToDefaultScene();
                            }
                            UIMgr.setGlobalDriverModeEnabled(readBooleanValue);
                        }
                    }
                }
            }, 300);
            if (this.mSvPreview != null) {
                SurfaceHolder holder = this.mSvPreview.getHolder();
                if (this.mbHasSurface) {
                    onVideoCaptureSurfaceReady(holder);
                } else {
                    holder.addCallback(this);
                }
            }
        }
    }

    public void sinkInStopVideo() {
        stopVideo();
    }

    public void sinkInMuteVideo(boolean z) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            ConfMgr.getInstance().getConfDataHelper().setIsVideoOnBeforeShare(false);
            if (z) {
                if (videoObj.isVideoStarted()) {
                    this.mIsVideoStarted = !videoObj.stopMyVideo(0);
                } else {
                    this.mIsVideoStarted = false;
                }
            } else if (!ConfMgr.getInstance().canUnmuteMyVideo()) {
                alertCameraDisabledByHost();
            } else {
                PreferenceUtil.removeValue(PreferenceUtil.CAMERA_IS_FREEZED);
                this.mIsVideoStarted = videoObj.startMyVideo(0);
                if (!this.mIsVideoStarted && !VideoCapturer.getInstance().isCapturing()) {
                    alertStartCameraFailed();
                }
            }
            ConfMgr.getInstance().getConfDataHelper().setmIsVideoStarted(this.mIsVideoStarted);
            refreshSwitchCameraButton();
            ZMConfComponentMgr.getInstance().onVideoMute();
        }
    }

    public void sinkInClickBtnVideo() {
        if (this.mContext != null) {
            if (VERSION.SDK_INT < 23 || this.mContext.zm_checkSelfPermission("android.permission.CAMERA") == 0) {
                toggleVideoStatus();
            } else {
                this.mContext.requestPermission("android.permission.CAMERA", 1016, 0);
            }
        }
    }

    public boolean sinkInDeviceStatusChanged(int i, final int i2) {
        if (this.mContext == null || i != 3) {
            return false;
        }
        this.mContext.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onCameraStatusEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ZmConfVideoComponent.this.handleOnCameraStatusEvent(i2);
            }
        });
        return true;
    }

    public void sinkOtherShareStatueChanged(boolean z) {
        if (this.mCompanionModeView != null) {
            if (z) {
                if (ConfLocalHelper.isInVideoCompanionMode()) {
                    this.mCompanionModeView.setVisibility(8);
                }
            } else if (ConfLocalHelper.isInVideoCompanionMode()) {
                this.mCompanionModeView.setVisibility(0);
                this.mCompanionModeView.updateData();
            } else {
                this.mCompanionModeView.setVisibility(8);
            }
        }
    }

    public void sinkInRefreshFeccUI() {
        refreshFeccUI();
    }

    public void sinkInOrientationChanged() {
        checkRotation();
    }

    public void sinkInFeccUserApproved(long j) {
        onFeccUserApproved(j);
    }

    public void onVideoFECCCmd(final int i, final SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO ssb_mc_data_block_fecc_talk_right_info) {
        if (this.mContext != null) {
            if (i == 13) {
                if (UIMgr.isLargeMode(this.mContext)) {
                    PListFragment.dismiss(this.mContext.getSupportFragmentManager());
                } else {
                    this.mContext.finishActivity(1001);
                }
            }
            if (i == 20) {
                if (this.mContext.isActive() && switchToNextCamera() && AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
                    AccessibilityUtil.announceNoInterruptForAccessibilityCompat(this.mPanelFecc, ZMConfCameraUtils.getCameraFace() == 1 ? C4558R.string.zm_accessibility_selected_front_camera_23059 : C4558R.string.zm_accessibility_selected_back_camera_23059);
                }
                return;
            }
            EventTaskManager eventTaskManager = this.mContext.getEventTaskManager();
            if (eventTaskManager != null) {
                eventTaskManager.push(new EventAction("onVideoFECCCmdImpl") {
                    public void run(IUIElement iUIElement) {
                        ZmConfVideoComponent.this.handleOnVideoFECCCmd(i, ssb_mc_data_block_fecc_talk_right_info);
                    }
                });
            }
        }
    }

    public void onFeccClick(int i, int i2) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            long controllCameraUserId = getControllCameraUserId();
            if (controllCameraUserId != 0 && ConfMgr.getInstance().getUserById(controllCameraUserId) != null) {
                int i3 = 15;
                if (i != 1) {
                    if (i == 2) {
                        i3 = 16;
                    } else if (i == 3) {
                        i3 = 17;
                    }
                }
                int i4 = 128;
                if (i2 != 3) {
                    if (i2 == 4) {
                        i4 = 192;
                    } else if (i2 == 1) {
                        i4 = 32;
                    } else if (i2 == 2) {
                        i4 = 48;
                    } else if (i2 == 5) {
                        i4 = 12;
                    } else if (i2 == 6) {
                        i4 = 8;
                    }
                }
                videoObj.handleFECCCmd(i3, controllCameraUserId, i4);
            }
        }
    }

    public void onFeccSwitchCam() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            long controllCameraUserId = getControllCameraUserId();
            if (controllCameraUserId != 0 && ConfMgr.getInstance().getUserById(controllCameraUserId) != null) {
                videoObj.handleFECCCmd(20, controllCameraUserId, 0);
            }
        }
    }

    public void onFeccClose() {
        if (this.mContext != null) {
            ZMFeccView zMFeccView = this.mPanelFecc;
            if (zMFeccView != null) {
                zMFeccView.setVisibility(8);
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    long controllCameraUserId = getControllCameraUserId();
                    if (controllCameraUserId != 0 && ConfMgr.getInstance().getUserById(controllCameraUserId) != null) {
                        videoObj.handleFECCCmd(14, controllCameraUserId);
                        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
                        if (videoSceneMgr == null) {
                            ZMUtils.printFunctionCallStack("Please note : Exception happens");
                        } else {
                            videoSceneMgr.unPinVideo();
                        }
                    }
                }
            }
        }
    }

    public void onFeccUserApproved(long j) {
        if (j != 0) {
            VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
            if (videoSceneMgr == null) {
                ZMUtils.printFunctionCallStack("Please note : Exception happens");
                return;
            }
            videoSceneMgr.switchToNormalScene();
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && !videoObj.isSelectedUser(j)) {
                videoSceneMgr.pinVideo(j);
            }
        }
    }

    public void refreshFeccUI() {
        if (this.mPanelFecc != null) {
            long controllCameraUserId = getControllCameraUserId();
            if (controllCameraUserId == 0) {
                this.mPanelFecc.setVisibility(8);
                return;
            }
            CmmUser userById = ConfMgr.getInstance().getUserById(controllCameraUserId);
            if (userById != null) {
                if (shouldShowFeccUI(controllCameraUserId)) {
                    boolean canControlUserCamera = canControlUserCamera(controllCameraUserId);
                    CmmVideoStatus videoStatusObj = userById.getVideoStatusObj();
                    boolean z = false;
                    boolean isSending = videoStatusObj != null ? videoStatusObj.getIsSending() : false;
                    this.mPanelFecc.setVisibility(0);
                    ZMFeccView zMFeccView = this.mPanelFecc;
                    if (canControlUserCamera && isSending) {
                        z = true;
                    }
                    zMFeccView.showPieView(z);
                } else {
                    this.mPanelFecc.setVisibility(8);
                }
            }
        }
    }

    public int getVideoViewLocationonScrennY() {
        if (this.mVideoView == null) {
            return 0;
        }
        int[] iArr = new int[2];
        this.mVideoView.getLocationOnScreen(iArr);
        return iArr[1];
    }

    public void alertStartCameraFailed() {
        if (!ConfLocalHelper.hasDisableSendVideoReason(4) && this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(IUIElement iUIElement) {
                    ZmConfVideoComponent.this.handleStartCameraFailed();
                }
            });
        }
    }

    public void sinkLeaderShipModeChanged() {
        if (this.mContext != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (videoObj.isLeadShipMode()) {
                    showTipMutedForLeaderShipModeStarted();
                } else {
                    NormalMessageTip.show(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_UNMUTED_FOR_LEADERSHIP_MODE_STOPPED.name(), (String) null, this.mContext.getString(C4558R.string.zm_msg_unmuted_for_leadership_mode_stopped), 3000);
                }
            }
        }
    }

    public void sinkVideoLeaderShipModeOnOff() {
        if (this.mContext != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself != null) {
                    if (videoObj.isLeaderofLeadMode(myself.getNodeId())) {
                        if (ConfLocalHelper.isAudioUnMuted()) {
                            NormalMessageTip.show(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_UNMUTED_FOR_LEADERSHIP_MODE_ON.name(), (String) null, this.mContext.getString(C4558R.string.zm_msg_unmuted_for_leadership_on_98431), (long) ZMConfiguration.DURATION_NORMAL_TIP);
                        } else {
                            ZMSpotlightVideoDialog.dismiss(this.mContext.getSupportFragmentManager());
                            ZMSpotlightVideoDialog.showSpotlightVideoDialog(this.mContext);
                        }
                    } else if (videoObj.isLeadShipMode()) {
                        NormalMessageTip.dismiss(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_UNMUTED_FOR_LEADERSHIP_MODE_ON.name());
                        ZMSpotlightVideoDialog.dismiss(this.mContext.getSupportFragmentManager());
                    } else {
                        NormalMessageTip.dismiss(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_UNMUTED_FOR_LEADERSHIP_MODE_ON.name());
                        ZMSpotlightVideoDialog.dismiss(this.mContext.getSupportFragmentManager());
                    }
                }
            }
        }
    }

    public void sinkConfVideoSendingStatusChanged() {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onConfVideoSendingStatusChanged();
        }
    }

    public void sinkSendVideoPrivilegeChanged() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            ZMConfComponentMgr.getInstance().onVideoEnableOrDisable();
            boolean hasDisableSendVideoReason = ConfLocalHelper.hasDisableSendVideoReason(0);
            if (!videoObj.isVideoStarted() && videoObj.needTurnOnVideoWhenCanResend() && hasDisableSendVideoReason) {
                ZMConfComponentMgr.getInstance().sinkInMuteVideo(false);
            } else if (videoObj.isVideoStarted() && !hasDisableSendVideoReason) {
                ZMConfComponentMgr.getInstance().sinkInMuteVideo(true);
            }
        }
    }

    public void sinkReceiveVideoPrivilegeChanged() {
        if (this.mAbsVideoSceneMgr != null && ConfLocalHelper.hasDisableRecvVideoReason(0)) {
            AbsVideoScene activeScene = this.mAbsVideoSceneMgr.getActiveScene();
            if (activeScene != null) {
                activeScene.reStart();
            }
        }
    }

    public void sinkAutoStartVideo(long j) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            if (confContext.isVideoOn() && !checkNeedMuteVideoByDefault()) {
                long currentTimeMillis = System.currentTimeMillis();
                if (!DeviceInfoUtil.isInPopUpCameraWhiteList() || currentTimeMillis - VideoCapturer.sLastStopCameraTime >= 600) {
                    startMyVideo();
                } else {
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ZmConfVideoComponent.this.startMyVideo();
                        }
                    }, (VideoCapturer.sLastStopCameraTime + 600) - currentTimeMillis);
                }
            } else if (this.mAbsVideoSceneMgr != null) {
                this.mAbsVideoSceneMgr.stopPreviewDevice();
            }
            if (this.mAbsVideoSceneMgr != null) {
                this.mAbsVideoSceneMgr.onAutoStartVideo();
            }
        }
    }

    public void sinkBandwidthLimitStatusChanged() {
        if (this.mBandwidthLimitIcon != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                if (confStatusObj.isBandwidthLimitEnabled()) {
                    this.mBandwidthLimitIcon.setVisibility(0);
                    MyBandwidthLimitInfo myBandwidthLimitInfo = confStatusObj.getMyBandwidthLimitInfo();
                    if (confStatusObj.getShowBandwidthLimitAgain()) {
                        if (myBandwidthLimitInfo.isDisableSendVideo() && myBandwidthLimitInfo.isDisableReceiveVideo()) {
                            showVideoLimitDialog(C4558R.string.zm_alert_bandwidth_send_receive_video_disabled_title_82445, C4558R.string.zm_alert_bandwidth_send_receive_video_disabled_msg_82445);
                            confStatusObj.setShowBandwidthLimitAgain(false);
                        } else if (myBandwidthLimitInfo.isDisableSendVideo()) {
                            showVideoLimitDialog(C4558R.string.zm_alert_bandwidth_send_video_disabled_title_82445, C4558R.string.zm_alert_bandwidth_send_video_disabled_msg_82445);
                            confStatusObj.setShowBandwidthLimitAgain(false);
                        } else if (myBandwidthLimitInfo.isDisableReceiveVideo()) {
                            showVideoLimitDialog(C4558R.string.zm_alert_bandwidth_receive_video_disabled_title_82445, C4558R.string.zm_alert_bandwidth_receive_video_disabled_msg_82445);
                            confStatusObj.setShowBandwidthLimitAgain(false);
                        }
                    }
                } else {
                    this.mBandwidthLimitIcon.setVisibility(8);
                }
            }
        }
    }

    private void showVideoLimitDialog(final int i, final int i2) {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("disableVideo", new EventAction("disableVideo") {
                public void run(IUIElement iUIElement) {
                    if (!ConfLocalHelper.isInSilentMode() && !ConfLocalHelper.isDirectShareClient()) {
                        if (ZmConfVideoComponent.this.videoLimitDlg == null) {
                            ZmConfVideoComponent.this.videoLimitDlg = new Builder(ZmConfVideoComponent.this.mContext).setMessage(i2).setTitle(i).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).create();
                            ZmConfVideoComponent.this.videoLimitDlg.show();
                        } else if (!ZmConfVideoComponent.this.videoLimitDlg.isShowing()) {
                            ZmConfVideoComponent.this.videoLimitDlg.show();
                        }
                    }
                }
            });
        }
    }

    private boolean checkNeedMuteVideoByDefault() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        int i = confContext.getAppContextParams().getInt("drivingMode", -1);
        if (!(this.mAbsVideoSceneMgr instanceof VideoSceneMgr)) {
            return false;
        }
        boolean z = true;
        if (i != 1 && (i != -1 || !ConfLocalHelper.getEnabledDrivingMode() || ConfMgr.getInstance().isCallingOut())) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void startMyVideo() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setDefaultDevice(videoObj.getDefaultCameraToUse());
            if (!ConfMgr.getInstance().canUnmuteMyVideo()) {
                alertCameraDisabledByHost();
            } else {
                this.mIsVideoStarted = videoObj.startMyVideo(0);
                if (!this.mIsVideoStarted && !VideoCapturer.getInstance().isCapturing()) {
                    alertStartCameraFailed();
                }
                ConfMgr.getInstance().getConfDataHelper().setmIsVideoStarted(this.mIsVideoStarted);
            }
            if (videoObj.isPreviewing()) {
                videoObj.stopPreviewDevice(0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleOnCameraStatusEvent(int i) {
        if (i == 2) {
            sinkInMuteVideo(true);
        }
        ZMConfComponentMgr.getInstance().onCameraStatusEvent();
        if (this.mContext != null) {
            VideoTip.updateIfExists(this.mContext.getSupportFragmentManager());
        }
    }

    /* access modifiers changed from: private */
    public void handleStartCameraFailed() {
        if (this.mContext != null) {
            if (VERSION.SDK_INT < 23 || this.mContext.zm_checkSelfPermission("android.permission.CAMERA") == 0) {
                DialogUtils.showAlertDialog((ZMActivity) this.mContext, C4558R.string.zm_alert_start_camera_failed_title, C4558R.string.zm_alert_start_camera_failed_msg, C4558R.string.zm_btn_ok);
            } else {
                this.mContext.requestPermission("android.permission.CAMERA", 1015, 500);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkStartVideo() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && this.mIsVideoStarted && !videoObj.isVideoStarted() && !confContext.inSilentMode()) {
                this.mIsVideoStarted = videoObj.startMyVideo(0);
                if (!this.mIsVideoStarted) {
                    alertStartCameraFailed();
                }
                ConfMgr.getInstance().getConfDataHelper().setmIsVideoStarted(this.mIsVideoStarted);
            }
        }
    }

    private void toggleVideoStatus() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            if (ConfLocalHelper.isInVideoCompanionMode() && this.mCompanionModeView != null && videoObj.leaveVideoCompanionMode()) {
                this.mCompanionModeView.setVisibility(8);
                ZMConfComponentMgr.getInstance().onVideoEnableOrDisable();
            }
            if (videoObj.isVideoStarted()) {
                sinkInMuteVideo(true);
                ZMConfEventTracking.logToggleVideo(true);
            } else if (ConfLocalHelper.hasDisableSendVideoReason(4)) {
                showCannotStartVideoDueToBandwidthDialog();
            } else {
                sinkInMuteVideo(false);
                ZMConfEventTracking.logToggleVideo(false);
            }
        }
    }

    private void showCannotStartVideoDueToBandwidthDialog() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("cannotStartVideo", new EventAction("cannotStartVideo") {
                public void run(IUIElement iUIElement) {
                    if (!ConfLocalHelper.isInSilentMode() && !ConfLocalHelper.isDirectShareClient()) {
                        if (ZmConfVideoComponent.this.cannotStartVideoDlg == null) {
                            ZmConfVideoComponent.this.cannotStartVideoDlg = new Builder(ZmConfVideoComponent.this.mContext).setMessage(C4558R.string.zm_alert_bandwidth_cannot_start_video_msg_82445).setTitle(C4558R.string.zm_alert_bandwidth_cannot_start_video_title_82445).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) null).create();
                            ZmConfVideoComponent.this.cannotStartVideoDlg.show();
                        } else if (!ZmConfVideoComponent.this.cannotStartVideoDlg.isShowing()) {
                            ZmConfVideoComponent.this.cannotStartVideoDlg.show();
                        }
                    }
                }
            });
        }
    }

    private void showTipMutedForLeaderShipModeStarted() {
        if (this.mContext != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                CmmUser userById = ConfMgr.getInstance().getUserById(videoObj.getActiveUserID());
                if (userById != null) {
                    NormalMessageTip.show(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_MUTED_FOR_LEADERSHIP_MODE_STARTED.name(), (String) null, this.mContext.getString(C4558R.string.zm_msg_muted_for_leadership_mode_started, new Object[]{userById.getScreenName()}), 3000);
                }
            }
        }
    }

    private long getControllCameraUserId() {
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (userList == null) {
            return 0;
        }
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            return 0;
        }
        int userCount = userList.getUserCount();
        for (int i = 0; i < userCount; i++) {
            CmmUser userAt = userList.getUserAt(i);
            if (userAt != null) {
                long nodeId = userAt.getNodeId();
                if (videoObj.canControlltheCam(nodeId) && videoObj.isCamInControl(nodeId)) {
                    return nodeId;
                }
            }
        }
        return 0;
    }

    private boolean shouldShowFeccUI(long j) {
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
        boolean z = false;
        if (videoSceneMgr == null) {
            ZMUtils.printFunctionCallStack("Please note : Exception happens");
            return false;
        } else if (!(videoSceneMgr.getActiveScene() instanceof NormalVideoScene) || ConfMgr.getInstance().getUserById(j) == null) {
            return false;
        } else {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj == null || !videoObj.isManualMode() || !videoObj.isSelectedUser(j)) {
                return false;
            }
            boolean canControlUserCamera = canControlUserCamera(j);
            boolean canSwitchUserCamera = canSwitchUserCamera(j);
            if (canControlUserCamera || canSwitchUserCamera) {
                z = true;
            }
            return z;
        }
    }

    private boolean canControlUserCamera(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        boolean z = false;
        if (videoObj == null) {
            return false;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById == null) {
            return false;
        }
        CmmVideoStatus videoStatusObj = userById.getVideoStatusObj();
        if (videoStatusObj == null) {
            return false;
        }
        int camFecc = videoStatusObj.getCamFecc();
        boolean canControlltheCam = videoObj.canControlltheCam(j);
        if (camFecc > 0 && canControlltheCam) {
            z = true;
        }
        return z;
    }

    private boolean canSwitchUserCamera(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        boolean z = false;
        if (videoObj == null) {
            return false;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null && userById.supportSwitchCam() && videoObj.canControlltheCam(j)) {
            z = true;
        }
        return z;
    }

    private void onFeccRequest(final long j) {
        if (this.mContext != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isKubiEnabled()) {
                approveCameraControl(true, j);
            } else if (!this.mMapFeccDialogs.containsKey(Long.valueOf(j))) {
                CmmUser userById = ConfMgr.getInstance().getUserById(j);
                if (userById != null) {
                    ZMAlertDialog create = new Builder(this.mContext).setTitle((CharSequence) this.mContext.getString(C4558R.string.zm_fecc_msg_request, new Object[]{userById.getScreenName()})).setPositiveButton(C4558R.string.zm_fecc_btn_approve, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ZmConfVideoComponent.this.approveCameraControl(true, j);
                        }
                    }).setNegativeButton(C4558R.string.zm_fecc_btn_decline, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ZmConfVideoComponent.this.approveCameraControl(false, j);
                        }
                    }).create();
                    create.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialogInterface) {
                            ZmConfVideoComponent.this.mMapFeccDialogs.remove(Long.valueOf(j));
                        }
                    });
                    create.setCancelable(false);
                    this.mMapFeccDialogs.put(Long.valueOf(j), create);
                    create.show();
                }
            }
        }
    }

    private void onFeccApprove(@NonNull CmmUser cmmUser, long j) {
        if (this.mContext != null) {
            if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) <= 2) {
                VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
                if (videoSceneMgr == null) {
                    ZMUtils.printFunctionCallStack("Please note : Exception happens");
                    return;
                }
                videoSceneMgr.pinVideo(j);
            } else if (canControlUserCamera(j) || canSwitchUserCamera(j)) {
                FeccMessageButtonTip.show(this.mContext.getSupportFragmentManager(), "fecc_approve", j, this.mContext.getString(C4558R.string.zm_fecc_msg_approve, new Object[]{cmmUser.getScreenName()}), this.mContext.getString(C4558R.string.zm_fecc_msg_start_control), 5000);
            }
            refreshFeccUI();
        }
    }

    private void onFeccDecline(CmmUser cmmUser) {
        if (this.mContext != null) {
            NormalMessageTip.show(this.mContext.getSupportFragmentManager(), "fecc_decline", (String) null, this.mContext.getString(C4558R.string.zm_fecc_msg_decline, new Object[]{cmmUser.getScreenName()}), 3000);
            VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
            if (videoSceneMgr == null) {
                ZMUtils.printFunctionCallStack("Please note : Exception happens");
                return;
            }
            videoSceneMgr.unPinVideo();
            this.mHandler.post(new Runnable() {
                public void run() {
                    ZmConfVideoComponent.this.refreshFeccUI();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleOnVideoFECCCmd(int i, @Nullable SSB_MC_DATA_BLOCK_FECC_TALK_RIGHT_INFO ssb_mc_data_block_fecc_talk_right_info) {
        if (this.mContext != null) {
            if (i == 13 || i == 12) {
                PListFragment.dismiss(this.mContext.getSupportFragmentManager());
            }
            if (ssb_mc_data_block_fecc_talk_right_info != null) {
                long j = ssb_mc_data_block_fecc_talk_right_info.executive;
                if (i == 11) {
                    onFeccRequest(j);
                } else {
                    CmmUser userById = ConfMgr.getInstance().getUserById(j);
                    if (userById != null) {
                        if (i == 14) {
                            NormalMessageTip.show(this.mContext.getSupportFragmentManager(), "fecc_giveup", (String) null, this.mContext.getString(C4558R.string.zm_fecc_msg_giveup, new Object[]{userById.getScreenName()}), 3000);
                        } else if (i == 13) {
                            onFeccApprove(userById, j);
                        } else if (i == 12) {
                            onFeccDecline(userById);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void approveCameraControl(boolean z, long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.handleFECCCmd(z ? 13 : 12, j);
        }
    }

    public boolean switchToNextCamera() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null || videoObj.getNumberOfCameras() <= 1) {
            return false;
        }
        ConfMgr.getInstance().getConfDataHelper().setIsVideoOnBeforeShare(false);
        AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
        if (absVideoSceneMgr != null) {
            absVideoSceneMgr.beforeSwitchCamera();
        }
        if (!videoObj.switchToNextCam() && !videoObj.isVideoStarted()) {
            alertStartCameraFailedUsingToast();
        }
        if (absVideoSceneMgr != null) {
            absVideoSceneMgr.afterSwitchCamera();
        }
        this.mMyVideoRotation = ZMConfCameraUtils.getCurrentMyVideoRotation(this.mContext);
        return rotateMyVideo(this.mMyVideoRotation);
    }

    public void checkRotation() {
        int currentMyVideoRotation = ZMConfCameraUtils.getCurrentMyVideoRotation(this.mContext);
        if (this.mMyVideoRotation != currentMyVideoRotation) {
            this.mMyVideoRotation = currentMyVideoRotation;
            rotateMyVideo(currentMyVideoRotation);
            if (ScreenShareMgr.getInstance().isSharing()) {
                ScreenShareMgr.getInstance().onOrientationChanged();
            }
        }
    }

    public void onMyVideoStatusChanged() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && (videoObj.isPreviewing() || videoObj.isVideoStarted())) {
            checkRotation();
        }
        ZMConfComponentMgr.getInstance().onMyVideoStatusChanged();
    }

    public void muteVideo(boolean z) {
        sinkInMuteVideo(z);
    }

    private void alertStartCameraFailedUsingToast() {
        if (this.mContext != null) {
            Toast.makeText(this.mContext.getApplicationContext(), C4558R.string.zm_alert_start_camera_failed_title, 1).show();
        }
    }

    public void alertCameraDisabledByHost() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                public void run(IUIElement iUIElement) {
                    DialogUtils.showAlertDialog((ZMActivity) ZmConfVideoComponent.this.mContext, C4558R.string.zm_msg_video_cannot_start_video_for_host_has_stopped_it, C4558R.string.zm_btn_ok);
                }
            });
        }
    }

    private void updateVideoStatus() {
        if (ConfMgr.getInstance().isConfConnected()) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
                if (videoStatusObj != null) {
                    this.mbSendingVideo = videoStatusObj.getIsSending();
                }
            }
        }
    }

    private boolean rotateMyVideo(int i) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        int i2 = 0;
        if (videoObj == null) {
            return false;
        }
        if (i != 0) {
            if (i == 90) {
                i2 = 1;
            } else if (i == 180) {
                i2 = 2;
            } else if (i == 270) {
                i2 = 3;
            }
        }
        boolean rotateDevice = videoObj.rotateDevice(i2, 0);
        AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
        if (absVideoSceneMgr != null) {
            absVideoSceneMgr.onMyVideoRotationChanged(i2);
        }
        return rotateDevice;
    }
}
