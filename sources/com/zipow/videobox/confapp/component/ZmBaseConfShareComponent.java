package com.zipow.videobox.confapp.component;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.IAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.AttentionTrackEventSinkUI.SimpleAttentionTrackEventSinkUIListener;
import com.zipow.videobox.confapp.CmmAttentionTrackMgr;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.share.ShareView;
import com.zipow.videobox.share.ShareView.ShareViewListener;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.RCFloatView;
import com.zipow.videobox.view.video.RCFloatView.IRemoteControlButtonStatusListener;
import com.zipow.videobox.view.video.RCMouseView;
import com.zipow.videobox.view.video.ShareVideoScene;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMActivity.GlobalActivityListener;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.ZMSecureRandom;
import p021us.zoom.videomeetings.C4558R;

public abstract class ZmBaseConfShareComponent extends ZmBaseConfComponent implements IRemoteControlButtonStatusListener {
    private static final String TAG = "ZmConfShareComponent";
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mAttentionTrackTask = null;
    private long mCurSharerUserId = 0;
    @NonNull
    private GlobalActivityListener mGlobalActivityListener = new GlobalActivityListener() {
        public void onUserActivityOnUI() {
        }

        public void onUIMoveToBackground() {
            ZmBaseConfShareComponent.this.checkAttentionTrackMode();
        }

        public void onActivityMoveToFront(ZMActivity zMActivity) {
            ZmBaseConfShareComponent.this.checkAttentionTrackMode();
        }
    };
    @NonNull
    protected Handler mHandler = new Handler();
    @NonNull
    private IAttentionTrackEventSinkUIListener mIAttentionTrackEventSinkUIListener = new SimpleAttentionTrackEventSinkUIListener() {
        public void OnConfAttentionTrackStatusChanged(boolean z) {
            ZmBaseConfShareComponent.this.checkAttentionTrackMode();
        }
    };
    @Nullable
    protected RCFloatView mRCFloatView;
    @Nullable
    protected RCMouseView mRCMouseView;
    protected int mShareStatus = 0;
    @Nullable
    protected ShareView mShareView;
    protected boolean mbEditStatus = false;
    protected boolean mbPresenter = false;
    protected boolean mbReceiveShareData = false;
    protected boolean mbShareScreen = false;
    protected boolean mbShareWhiteboard = false;

    public abstract void stopShare();

    public ZmBaseConfShareComponent(@NonNull ConfActivity confActivity) {
        super(confActivity);
    }

    public void onActivityDestroy() {
        AttentionTrackEventSinkUI.getInstance().removeListener(this.mIAttentionTrackEventSinkUIListener);
        ZMActivity.removeGlobalActivityListener(this.mGlobalActivityListener);
        this.mHandler.removeCallbacksAndMessages(null);
        super.onActivityDestroy();
        this.mRCFloatView = null;
        this.mShareView = null;
        this.mRCMouseView = null;
    }

    public void onActivityCreate(Bundle bundle) {
        if (this.mContext != null) {
            this.mShareView = (ShareView) this.mContext.findViewById(C4558R.C4560id.sharingView);
            ShareView shareView = this.mShareView;
            if (shareView != null) {
                shareView.setShareListener(new ShareViewListener() {
                    public void onStopEdit() {
                        ZmBaseConfShareComponent.this.mbEditStatus = false;
                        ZMConfComponentMgr.getInstance().onShareEdit(false);
                    }

                    public void onStartEdit() {
                        ZmBaseConfShareComponent.this.onAnnotationStarted();
                        ZmBaseConfShareComponent.this.mbEditStatus = true;
                        ZMConfComponentMgr.getInstance().onShareEdit(true);
                        if (ZmBaseConfShareComponent.this.isInShareVideoScene()) {
                            ((ShareVideoScene) ZmBaseConfShareComponent.this.mAbsVideoSceneMgr.getActiveScene()).hideTitleAndSwitchScenePanel();
                        }
                    }

                    public void onShareError() {
                        ZmBaseConfShareComponent.this.stopShare();
                    }
                });
                this.mRCFloatView = (RCFloatView) this.mContext.findViewById(C4558R.C4560id.rc_float_view);
                RCFloatView rCFloatView = this.mRCFloatView;
                if (rCFloatView != null) {
                    rCFloatView.setRemoteControlButtonStatusListener(this);
                }
                this.mRCMouseView = (RCMouseView) this.mContext.findViewById(C4558R.C4560id.rc_mouse);
                AttentionTrackEventSinkUI.getInstance().addListener(this.mIAttentionTrackEventSinkUIListener);
                ZMActivity.addGlobalActivityListener(this.mGlobalActivityListener);
            }
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (ConfLocalHelper.isInRemoteControlMode(this.mAbsVideoSceneMgr)) {
            RCFloatView rCFloatView = this.mRCFloatView;
            if (rCFloatView != null && rCFloatView.isControlling()) {
                ShareVideoScene shareVideoScene = getShareVideoScene();
                if (shareVideoScene != null) {
                    switch (i) {
                        case 66:
                            shareVideoScene.remoteControlKeyInput(1);
                            break;
                        case 67:
                            shareVideoScene.remoteControlKeyInput(0);
                            break;
                    }
                    return true;
                }
            }
        }
        if (ConfShareLocalHelper.isSharingOut()) {
            ShareView shareView = this.mShareView;
            return shareView != null && shareView.onKeyDown(i, keyEvent);
        }
    }

    public boolean handleRequestPermissionResult(int i, @NonNull String str, int i2) {
        ShareView shareView = this.mShareView;
        return shareView != null && shareView.handleRequestPermissionResult(i, str, i2);
    }

    public void onEnabledRC(boolean z) {
        if (this.mRCMouseView != null) {
            ZMConfComponentMgr.getInstance().onBeforeRemoteControlEnabled(z);
            boolean z2 = true;
            if (z) {
                ShareView shareView = this.mShareView;
                if (shareView != null) {
                    shareView.closeAnnotateView();
                }
                this.mRCMouseView.showRCMouse(true);
            } else {
                this.mRCMouseView.showRCMouse(false);
                z2 = false;
            }
            ShareVideoScene shareVideoScene = getShareVideoScene();
            if (shareVideoScene != null) {
                shareVideoScene.setInRemoteControlMode(z2);
            }
        }
    }

    @Nullable
    public ShareVideoScene getShareVideoScene() {
        if (this.mAbsVideoSceneMgr == null) {
            return null;
        }
        AbsVideoScene activeScene = this.mAbsVideoSceneMgr.getActiveScene();
        if (!(activeScene instanceof ShareVideoScene)) {
            return null;
        }
        return (ShareVideoScene) activeScene;
    }

    public void pauseShareView() {
        ShareView shareView = this.mShareView;
        if (shareView != null) {
            shareView.pause();
        }
    }

    public void onSwitchToOrOutShare(boolean z) {
        RCFloatView rCFloatView = this.mRCFloatView;
        if (rCFloatView != null) {
            if (!z) {
                rCFloatView.showRCFloatView(false, false);
            } else if (ConfLocalHelper.checkRemoteControlPrivilege()) {
                this.mRCFloatView.showRCFloatView(true, false);
            }
        }
    }

    @Nullable
    public RCMouseView getRCMouseView() {
        return this.mRCMouseView;
    }

    public boolean isMbEditStatus() {
        return this.mbEditStatus;
    }

    public void onLayoutChange(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        RCFloatView rCFloatView = this.mRCFloatView;
        if (rCFloatView != null) {
            rCFloatView.onConfLayoutChanged(i, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public void showAnnotateViewWhenSceneChanged() {
        showAnnotateViewWhenSceneChanged(isInShareVideoScene());
    }

    public void handleSilentModeChangedForOtherScreenSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        long activeUserID = shareObj != null ? shareObj.getActiveUserID() : 0;
        if (activeUserID > 0) {
            if (!handleActiveUserForScreenShare(activeUserID)) {
                onShareActiveUser(activeUserID);
            }
            onShareUserReceivingStatus(activeUserID);
        }
    }

    /* access modifiers changed from: protected */
    public boolean checkConfSupportOrEnableAnnotate() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null || this.mShareView == null) {
            return false;
        }
        boolean senderSupportAnnotation = this.mbShareWhiteboard ? true : shareObj.senderSupportAnnotation(this.mCurSharerUserId);
        this.mShareView.setAnnotationEnable(senderSupportAnnotation);
        return senderSupportAnnotation;
    }

    /* access modifiers changed from: protected */
    public void onMyShareStarted() {
        if (this.mContext != null && this.mShareView != null) {
            this.mCurSharerUserId = 0;
            ConfMgr.getInstance().getConfDataHelper().setIsVideoOnBeforeShare(false);
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.isVideoStarted()) {
                ZMConfComponentMgr.getInstance().sinkInMuteVideo(true);
                ConfMgr.getInstance().getConfDataHelper().setIsVideoOnBeforeShare(true);
            }
            setShareCaptureObject();
            if (ScreenShareMgr.getInstance().isSharing()) {
                ScreenShareMgr.getInstance().startShare();
            } else {
                ZMConfComponentMgr.getInstance().onBeforeMyStartShare();
                this.mShareView.start();
                this.mbPresenter = true;
            }
            ZMConfComponentMgr.getInstance().onMyShareStatueChanged(true);
            this.mbReceiveShareData = true;
            checkShareViewIsCanVisible();
        }
    }

    /* access modifiers changed from: protected */
    public void onMyShareStopped() {
        this.mbShareWhiteboard = false;
        this.mbShareScreen = false;
        this.mbEditStatus = false;
        ShareView shareView = this.mShareView;
        if (shareView != null) {
            this.mbPresenter = false;
            shareView.stop();
            changeShareViewVisibility();
        }
        ZMConfComponentMgr.getInstance().onMyShareStatueChanged(false);
        if (ConfMgr.getInstance().getConfDataHelper().getIsVideoOnBeforeShare()) {
            ZMConfComponentMgr.getInstance().sinkInMuteVideo(false);
        }
        if (ScreenShareMgr.getInstance().isSharing()) {
            ScreenShareMgr.getInstance().stopShare();
        }
        this.mCurSharerUserId = 0;
        this.mbReceiveShareData = false;
    }

    private void onOtherStartShare(long j) {
        ZMConfComponentMgr.getInstance().onOtherShareStatueChanged(true, j);
    }

    private void onOtherStopShare() {
        ZMConfComponentMgr.getInstance().onOtherShareStatueChanged(false, 0);
        this.mCurSharerUserId = 0;
        this.mbEditStatus = false;
        this.mbReceiveShareData = false;
        ShareView shareView = this.mShareView;
        if (shareView != null) {
            shareView.stop();
            this.mShareView.setVisibility(8);
        }
    }

    private void showAnnotateViewWhenSceneChanged(boolean z) {
        if (isInShareVideoScene()) {
            checkShareViewIsCanVisible();
        } else if (!z) {
            changeShareViewVisibility();
        }
    }

    public void onUserGetRemoteControlPrivilege(long j) {
        if (this.mRCFloatView != null) {
            boolean checkRemoteControlPrivilege = ConfLocalHelper.checkRemoteControlPrivilege();
            ConfMgr instance = ConfMgr.getInstance();
            if (instance.getMyself() != null) {
                long nodeId = instance.getMyself().getNodeId();
                CmmConfStatus confStatusObj = instance.getConfStatusObj();
                if (confStatusObj != null) {
                    boolean isSameUser = confStatusObj.isSameUser(nodeId, j);
                    if (!checkRemoteControlPrivilege || !isSameUser) {
                        this.mRCFloatView.showRCFloatView(false, false);
                    } else {
                        this.mRCFloatView.showRCFloatView(true, true);
                    }
                }
            }
        }
    }

    public void remoteControlStarted(long j) {
        if (this.mRCFloatView != null) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null && shareObj.getShareStatus() == 3) {
                ConfMgr instance = ConfMgr.getInstance();
                CmmUser myself = instance.getMyself();
                if (myself != null) {
                    long nodeId = myself.getNodeId();
                    CmmConfStatus confStatusObj = instance.getConfStatusObj();
                    if (confStatusObj != null) {
                        if (confStatusObj.isSameUser(nodeId, j)) {
                            shareObj.startRemoteControl();
                            this.mRCFloatView.enableRC(true);
                        } else {
                            this.mRCFloatView.enableRC(false);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onAnnotationStarted() {
        if (this.mRCFloatView != null && ConfLocalHelper.isInRemoteControlMode(this.mAbsVideoSceneMgr)) {
            this.mRCFloatView.enableRC(false);
        }
    }

    public void sinkShareActiveUser(final long j) {
        if (this.mContext != null) {
            checkAttentionTrackMode();
            if (!handleActiveUserForScreenShare(j)) {
                this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("onShareActiveUser", new EventAction("onShareActiveUser") {
                    public void run(@NonNull IUIElement iUIElement) {
                        ZmBaseConfShareComponent.this.onShareActiveUser(j);
                    }
                });
            }
        }
    }

    public void sinkShareUserReceivingStatus(long j) {
        if (this.mContext != null && this.mContext.isActive()) {
            onShareUserReceivingStatus(j);
        }
    }

    public void sinkShareUserSendingStatus(long j) {
        checkShareViewIsCanVisible();
    }

    public void sinkShareDataSizeChanged(long j) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onShareDataSizeChanged(j);
        }
    }

    private void onShareUserReceivingStatus(long j) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onShareUserReceivingStatus(j);
        }
        this.mCurSharerUserId = j;
        this.mbReceiveShareData = true;
        showAnnotateViewWhenSceneChanged(true);
    }

    /* access modifiers changed from: private */
    public void onShareActiveUser(long j) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            int shareStatus = shareObj.getShareStatus();
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                boolean isMyself = confStatusObj.isMyself(j);
                if (shareStatus == 2) {
                    if (isMyself) {
                        onMyShareStarted();
                    } else if (this.mAbsVideoSceneMgr != null) {
                        this.mAbsVideoSceneMgr.onShareActiveUser(j);
                    }
                } else if (shareStatus == 3) {
                    if (!isMyself) {
                        if (this.mShareStatus == 2) {
                            if (ConfLocalHelper.isDirectShareClient()) {
                                PTAppDelegation.getInstance().stopPresentToRoom(false);
                                return;
                            }
                            onMyShareStopped();
                        }
                        onOtherStartShare(j);
                    }
                } else if (shareStatus != 1) {
                    if (shareStatus == 0) {
                        if (this.mShareStatus == 2) {
                            if (ConfLocalHelper.isDirectShareClient()) {
                                PTAppDelegation.getInstance().stopPresentToRoom(false);
                                return;
                            }
                            onMyShareStopped();
                        }
                        if (!isMyself) {
                            onOtherStopShare();
                        }
                        if (this.mRCFloatView != null) {
                            if (ConfLocalHelper.isInRemoteControlMode(this.mAbsVideoSceneMgr)) {
                                this.mRCFloatView.enableRC(false);
                            }
                            this.mRCFloatView.showRCFloatView(false, false);
                        }
                        if (this.mAbsVideoSceneMgr != null) {
                            this.mAbsVideoSceneMgr.onShareActiveUser(j);
                        }
                    } else if (this.mAbsVideoSceneMgr != null) {
                        this.mAbsVideoSceneMgr.onShareActiveUser(j);
                    }
                }
                this.mShareStatus = shareStatus;
                if (this.mRCFloatView != null) {
                    if (ConfLocalHelper.checkRemoteControlPrivilege()) {
                        this.mRCFloatView.showRCFloatView(true, false);
                    } else {
                        this.mRCFloatView.showRCFloatView(false, false);
                    }
                }
            }
        }
    }

    private boolean handleActiveUserForScreenShare(long j) {
        if (this.mContext == null || !ScreenShareMgr.getInstance().isSharing()) {
            return false;
        }
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        int shareStatus = shareObj.getShareStatus();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return false;
        }
        boolean isMyself = confStatusObj.isMyself(j);
        if (shareStatus == 2) {
            if (isMyself) {
                onMyShareStarted();
                this.mShareStatus = shareStatus;
                return true;
            }
        } else if (shareStatus == 3) {
            if (!isMyself && this.mShareStatus == 2) {
                ConfLocalHelper.returnToConf(this.mContext);
            }
        } else if (shareStatus == 0 && this.mShareStatus == 2) {
            ConfLocalHelper.returnToConf(this.mContext);
        }
        return false;
    }

    private void checkShareViewIsCanVisible() {
        checkConfSupportOrEnableAnnotate();
        changeShareViewVisibility();
    }

    public void changeShareViewVisibility() {
        if (this.mShareView != null) {
            if (!this.mbReceiveShareData || this.mbShareScreen || (!this.mbPresenter && (!isInShareVideoScene() || !isInBigShareMode()))) {
                this.mShareView.setVisibility(8);
            } else {
                this.mShareView.setVisibility(0);
                this.mShareView.setDrawingCacheEnabled(true);
                checkConfSupportOrEnableAnnotate();
            }
        }
    }

    private boolean isInBigShareMode() {
        if (this.mAbsVideoSceneMgr == null) {
            return false;
        }
        AbsVideoScene activeScene = this.mAbsVideoSceneMgr.getActiveScene();
        if (activeScene instanceof ShareVideoScene) {
            return ((ShareVideoScene) activeScene).isBigShareView();
        }
        return false;
    }

    private boolean setShareCaptureObject() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.setCaptureObject(this.mbShareWhiteboard);
    }

    /* access modifiers changed from: private */
    public void checkAttentionTrackMode() {
        CmmAttentionTrackMgr attentionTrackAPI = ConfMgr.getInstance().getAttentionTrackAPI();
        if (attentionTrackAPI != null) {
            ZMActivity frontActivity = ZMActivity.getFrontActivity();
            if (frontActivity != null) {
                ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                if (shareObj != null) {
                    if (shareObj.getShareStatus() == 2 || !attentionTrackAPI.isConfAttentionTrackEnabled() || frontActivity.isActive()) {
                        Runnable runnable = this.mAttentionTrackTask;
                        if (runnable != null) {
                            this.mHandler.removeCallbacks(runnable);
                            this.mAttentionTrackTask = null;
                        }
                        if (attentionTrackAPI.isWebAttentionTrackEnabled()) {
                            attentionTrackAPI.changeMyAttentionStatus(true);
                        }
                        return;
                    }
                    if (this.mAttentionTrackTask == null) {
                        this.mAttentionTrackTask = new Runnable() {
                            public void run() {
                                CmmAttentionTrackMgr attentionTrackAPI = ConfMgr.getInstance().getAttentionTrackAPI();
                                if (attentionTrackAPI != null) {
                                    ZMActivity frontActivity = ZMActivity.getFrontActivity();
                                    if (frontActivity != null && attentionTrackAPI.isConfAttentionTrackEnabled()) {
                                        attentionTrackAPI.changeMyAttentionStatus(frontActivity.isActive());
                                        ZmBaseConfShareComponent.this.mAttentionTrackTask = null;
                                    }
                                }
                            }
                        };
                        this.mHandler.postDelayed(this.mAttentionTrackTask, (long) ((ZMSecureRandom.nextInt(16) + 25) * 1000));
                    }
                }
            }
        }
    }
}
