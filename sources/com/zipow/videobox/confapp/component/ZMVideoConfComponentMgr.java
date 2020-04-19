package com.zipow.videobox.confapp.component;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.component.sink.video.IVideoSink;
import com.zipow.videobox.confapp.component.sink.video.IVideoStatusSink;
import com.zipow.videobox.confapp.component.sink.video.IVideoUIInteractionSink;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;

class ZMVideoConfComponentMgr extends ZMAudioConfComponentMgr implements IVideoSink, IVideoUIInteractionSink, IVideoStatusSink {
    private static final String TAG = "ZMVideoConfComponentMgr";
    /* access modifiers changed from: private */
    @Nullable
    public IVideoStatusSink mVideoStatusSink;

    ZMVideoConfComponentMgr() {
    }

    public int getVideoViewLocationonScrennY() {
        if (this.mZmConfVideoComponent != null) {
            return this.mZmConfVideoComponent.getVideoViewLocationonScrennY();
        }
        return 0;
    }

    public void setmVideoStatusSink(@Nullable IVideoStatusSink iVideoStatusSink) {
        this.mVideoStatusSink = iVideoStatusSink;
    }

    public void sinkAutoStartVideo(final long j) {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater(new EventAction("onAutoStartVideo") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkAutoStartVideo(j);
                    }
                }
            });
        }
    }

    public void sinkCompanionModeChanged() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("sinkCompanionModeChanged", new EventAction() {
                public void run(IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mVideoStatusSink != null) {
                        ZMVideoConfComponentMgr.this.mVideoStatusSink.onVideoEnableOrDisable();
                    }
                }
            });
        }
    }

    public void sinkBandwidthLimitStatusChanged() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("bandwidthLimitStatusChanged", new EventAction() {
                public void run(IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkBandwidthLimitStatusChanged();
                    }
                }
            });
        }
    }

    public void sinkReceiveVideoPrivilegeChanged() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("sinkReceiveVideoPrivilegeChanged", new EventAction() {
                public void run(IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkReceiveVideoPrivilegeChanged();
                    }
                }
            });
        }
    }

    public void sinkSendVideoPrivilegeChanged() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("sinkSendVideoPrivilegeChanged", new EventAction() {
                public void run(IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkSendVideoPrivilegeChanged();
                    }
                }
            });
        }
    }

    public void sinkConfVideoSendingStatusChanged() {
        if (this.mContext != null && this.mContext.isActive() && this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkConfVideoSendingStatusChanged();
        }
    }

    public void sinkVideoLeaderShipModeOnOff() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push("sinkVideoLeaderShipModeOnOff", new EventAction("sinkVideoLeaderShipModeOnOff") {
                public void run(IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkVideoLeaderShipModeOnOff();
                    }
                }
            });
        }
    }

    public void sinkLeaderShipModeChanged() {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().push("sinkLeaderShipModeChanged", new EventAction("sinkLeaderShipModeChanged") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkLeaderShipModeChanged();
                    }
                }
            });
        }
    }

    public void sinkUserVideoStatus(long j) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkUserVideoStatus(j);
        }
    }

    public void sinkUserActiveVideo(final long j) {
        if (this.mContext != null) {
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById == null || !userById.inSilentMode()) {
                if (this.mAbsVideoSceneMgr != null) {
                    this.mAbsVideoSceneMgr.setActiveUserId(j);
                }
                this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("onUserActiveVideo", new EventAction("onUserActiveVideo") {
                    public void run(@NonNull IUIElement iUIElement) {
                        if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                            ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkUserActiveVideo(j);
                        }
                    }
                });
            }
        }
    }

    public void sinkUserActiveVideoForDeck(final long j) {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("onUserActiveVideoForDeck", new EventAction("onUserActiveVideoForDeck") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkUserActiveVideoForDeck(j);
                    }
                }
            });
        }
    }

    public void sinkUserVideoDataSizeChanged(long j) {
        if (!(this.mContext == null || !this.mContext.isActive() || this.mZmConfVideoComponent == null)) {
            this.mZmConfVideoComponent.sinkUserVideoDataSizeChanged(j);
        }
    }

    public void sinkUserTalkingVideo(final long j) {
        if (this.mContext != null) {
            this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("onUserTalkingVideo", new EventAction("onUserTalkingVideo") {
                public void run(@NonNull IUIElement iUIElement) {
                    if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                        ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkUserTalkingVideo(j);
                    }
                }
            });
        }
    }

    public void sinkUserVideoQualityChanged(long j) {
        if (!(this.mContext == null || !this.mContext.isActive() || this.mZmConfVideoComponent == null)) {
            this.mZmConfVideoComponent.sinkUserVideoQualityChanged(j);
        }
    }

    public void sinkUserVideoMutedByHost(final long j) {
        if (this.mContext != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.isVideoStarted()) {
                if (!this.mContext.isActive()) {
                    videoObj.stopMyVideo(0);
                }
                this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("sinkUserVideoMuteByHost", new EventAction("sinkUserVideoMuteByHost") {
                    public void run(@NonNull IUIElement iUIElement) {
                        if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                            ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkUserVideoMutedByHost(j);
                        }
                    }
                });
            }
        }
    }

    public void sinkUserVideoRequestUnmuteByHost(final long j) {
        if (this.mContext != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && !videoObj.isVideoStarted()) {
                this.mContext.getNonNullEventTaskManagerOrThrowException().pushLater("sinkUserVideoRequestUnmuteByHost", new EventAction("sinkUserVideoRequestUnmuteByHost") {
                    public void run(@NonNull IUIElement iUIElement) {
                        if (ZMVideoConfComponentMgr.this.mZmConfVideoComponent != null) {
                            ZMVideoConfComponentMgr.this.mZmConfVideoComponent.sinkUserVideoRequestUnmuteByHost(j);
                        }
                    }
                });
            }
        }
    }

    public void sinkUserVideoParticipantUnmuteLater(long j) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkUserVideoParticipantUnmuteLater(j);
        }
    }

    public void sinkInControlCameraTypeChanged(long j) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInControlCameraTypeChanged(j);
        }
    }

    public boolean sinkInDeviceStatusChanged(int i, int i2) {
        if (this.mZmConfVideoComponent != null) {
            return this.mZmConfVideoComponent.sinkInDeviceStatusChanged(i, i2);
        }
        return false;
    }

    public void sinkMyShareStatueChanged(boolean z) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkMyShareStatueChanged(z);
        }
    }

    public void sinkInOrientationChanged() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInOrientationChanged();
        }
    }

    public void sinkInResumeVideo() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInResumeVideo();
        }
    }

    public void sinkInStopVideo() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.pauseShareView();
        }
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInStopVideo();
        }
    }

    public void sinkInMuteVideo(boolean z) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInMuteVideo(z);
        }
    }

    public void sinkInClickBtnVideo() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInClickBtnVideo();
        }
    }

    public void sinkInFeccUserApproved(long j) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInFeccUserApproved(j);
        }
    }

    public void sinkInRefreshFeccUI() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkInRefreshFeccUI();
        }
    }

    public void refreshSwitchCameraButton() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.refreshSwitchCameraButton();
        }
    }

    public void switchCamera(@NonNull String str) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.switchCamera(str);
        }
    }

    public boolean canSwitchCamera() {
        if (this.mZmConfVideoComponent != null) {
            return this.mZmConfVideoComponent.canSwitchCamera();
        }
        return false;
    }

    public void onClickSwitchCamera() {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.onClickSwitchCamera();
        }
    }

    public boolean onVideoViewTouchEvent(@NonNull MotionEvent motionEvent) {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            return iVideoStatusSink.onVideoViewTouchEvent(motionEvent);
        }
        return false;
    }

    public void onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            iVideoStatusSink.onVideoViewSingleTapConfirmed(motionEvent);
        }
    }

    public void onVideoMute() {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            iVideoStatusSink.onVideoMute();
        }
    }

    public void onVideoEnableOrDisable() {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            iVideoStatusSink.onVideoEnableOrDisable();
        }
    }

    public void onCameraStatusEvent() {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            iVideoStatusSink.onCameraStatusEvent();
        }
    }

    public void onMyVideoStatusChanged() {
        IVideoStatusSink iVideoStatusSink = this.mVideoStatusSink;
        if (iVideoStatusSink != null) {
            iVideoStatusSink.onMyVideoStatusChanged();
        }
    }
}
