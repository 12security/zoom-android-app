package com.zipow.videobox.confapp.component;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.component.sink.share.IShareStatusSink;
import com.zipow.videobox.confapp.component.sink.video.IVideoStatusSink;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.RCMouseView;
import com.zipow.videobox.view.video.ShareVideoScene;

public class ZMConfComponentMgr extends ZMShareConfComponentMgr {
    private static final ZMConfComponentMgr ourInstance = new ZMConfComponentMgr();

    public /* bridge */ /* synthetic */ boolean canSwitchCamera() {
        return super.canSwitchCamera();
    }

    public /* bridge */ /* synthetic */ RCMouseView getRCMouseView() {
        return super.getRCMouseView();
    }

    public /* bridge */ /* synthetic */ ShareVideoScene getShareVideoScene() {
        return super.getShareVideoScene();
    }

    public /* bridge */ /* synthetic */ int getVideoViewLocationonScrennY() {
        return super.getVideoViewLocationonScrennY();
    }

    public /* bridge */ /* synthetic */ boolean isAnnotationDrawingViewVisible() {
        return super.isAnnotationDrawingViewVisible();
    }

    public /* bridge */ /* synthetic */ void onAnnotateShutDown() {
        super.onAnnotateShutDown();
    }

    public /* bridge */ /* synthetic */ void onAnnotateStartedUp(boolean z, long j) {
        super.onAnnotateStartedUp(z, j);
    }

    public /* bridge */ /* synthetic */ void onAnnotateViewSizeChanged() {
        super.onAnnotateViewSizeChanged();
    }

    public /* bridge */ /* synthetic */ void onBeforeMyStartShare() {
        super.onBeforeMyStartShare();
    }

    public /* bridge */ /* synthetic */ void onBeforeRemoteControlEnabled(boolean z) {
        super.onBeforeRemoteControlEnabled(z);
    }

    public /* bridge */ /* synthetic */ void onCameraStatusEvent() {
        super.onCameraStatusEvent();
    }

    public /* bridge */ /* synthetic */ void onClickSwitchCamera() {
        super.onClickSwitchCamera();
    }

    public /* bridge */ /* synthetic */ void onLayoutChange(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        super.onLayoutChange(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public /* bridge */ /* synthetic */ void onMyShareStatueChanged(boolean z) {
        super.onMyShareStatueChanged(z);
    }

    public /* bridge */ /* synthetic */ void onMyVideoStatusChanged() {
        super.onMyVideoStatusChanged();
    }

    public /* bridge */ /* synthetic */ void onOtherShareStatueChanged(boolean z, long j) {
        super.onOtherShareStatueChanged(z, j);
    }

    public /* bridge */ /* synthetic */ void onShareEdit(boolean z) {
        super.onShareEdit(z);
    }

    public /* bridge */ /* synthetic */ void onSwitchToOrOutShare(boolean z) {
        super.onSwitchToOrOutShare(z);
    }

    public /* bridge */ /* synthetic */ void onToolbarVisibilityChanged(boolean z) {
        super.onToolbarVisibilityChanged(z);
    }

    public /* bridge */ /* synthetic */ void onUserGetRemoteControlPrivilege(long j) {
        super.onUserGetRemoteControlPrivilege(j);
    }

    public /* bridge */ /* synthetic */ void onVideoEnableOrDisable() {
        super.onVideoEnableOrDisable();
    }

    public /* bridge */ /* synthetic */ void onVideoMute() {
        super.onVideoMute();
    }

    public /* bridge */ /* synthetic */ void onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        super.onVideoViewSingleTapConfirmed(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onVideoViewTouchEvent(@NonNull MotionEvent motionEvent) {
        return super.onVideoViewTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void onWBPageChanged(int i, int i2, int i3, int i4) {
        super.onWBPageChanged(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void refreshAudioSharing(boolean z) {
        super.refreshAudioSharing(z);
    }

    public /* bridge */ /* synthetic */ void refreshSwitchCameraButton() {
        super.refreshSwitchCameraButton();
    }

    public /* bridge */ /* synthetic */ void remoteControlStarted(long j) {
        super.remoteControlStarted(j);
    }

    public /* bridge */ /* synthetic */ void selectShareType(@NonNull ShareOptionType shareOptionType) {
        super.selectShareType(shareOptionType);
    }

    public /* bridge */ /* synthetic */ void setPaddingForTranslucentStatus(int i, int i2, int i3, int i4) {
        super.setPaddingForTranslucentStatus(i, i2, i3, i4);
    }

    public /* bridge */ /* synthetic */ void setmIShareStatusSink(@Nullable IShareStatusSink iShareStatusSink) {
        super.setmIShareStatusSink(iShareStatusSink);
    }

    public /* bridge */ /* synthetic */ void setmVideoStatusSink(@Nullable IVideoStatusSink iVideoStatusSink) {
        super.setmVideoStatusSink(iVideoStatusSink);
    }

    public /* bridge */ /* synthetic */ void shareByPathExtension(@Nullable String str) {
        super.shareByPathExtension(str);
    }

    public /* bridge */ /* synthetic */ void showAnnotateViewWhenSceneChanged() {
        super.showAnnotateViewWhenSceneChanged();
    }

    public /* bridge */ /* synthetic */ void showShareChoice() {
        super.showShareChoice();
    }

    public /* bridge */ /* synthetic */ void showShareTip() {
        super.showShareTip();
    }

    public /* bridge */ /* synthetic */ void sinkAutoStartVideo(long j) {
        super.sinkAutoStartVideo(j);
    }

    public /* bridge */ /* synthetic */ void sinkBandwidthLimitStatusChanged() {
        super.sinkBandwidthLimitStatusChanged();
    }

    public /* bridge */ /* synthetic */ void sinkCompanionModeChanged() {
        super.sinkCompanionModeChanged();
    }

    public /* bridge */ /* synthetic */ void sinkConfConnecting() {
        super.sinkConfConnecting();
    }

    public /* bridge */ /* synthetic */ void sinkConfVideoSendingStatusChanged() {
        super.sinkConfVideoSendingStatusChanged();
    }

    public /* bridge */ /* synthetic */ void sinkInClickBtnVideo() {
        super.sinkInClickBtnVideo();
    }

    public /* bridge */ /* synthetic */ void sinkInControlCameraTypeChanged(long j) {
        super.sinkInControlCameraTypeChanged(j);
    }

    public /* bridge */ /* synthetic */ boolean sinkInDeviceStatusChanged(int i, int i2) {
        return super.sinkInDeviceStatusChanged(i, i2);
    }

    public /* bridge */ /* synthetic */ void sinkInFeccUserApproved(long j) {
        super.sinkInFeccUserApproved(j);
    }

    public /* bridge */ /* synthetic */ void sinkInMuteVideo(boolean z) {
        super.sinkInMuteVideo(z);
    }

    public /* bridge */ /* synthetic */ void sinkInOrientationChanged() {
        super.sinkInOrientationChanged();
    }

    public /* bridge */ /* synthetic */ void sinkInRefreshFeccUI() {
        super.sinkInRefreshFeccUI();
    }

    public /* bridge */ /* synthetic */ void sinkInResumeVideo() {
        super.sinkInResumeVideo();
    }

    public /* bridge */ /* synthetic */ void sinkInStopVideo() {
        super.sinkInStopVideo();
    }

    public /* bridge */ /* synthetic */ void sinkLeaderShipModeChanged() {
        super.sinkLeaderShipModeChanged();
    }

    public /* bridge */ /* synthetic */ void sinkMyShareStatueChanged(boolean z) {
        super.sinkMyShareStatueChanged(z);
    }

    public /* bridge */ /* synthetic */ void sinkReceiveVideoPrivilegeChanged() {
        super.sinkReceiveVideoPrivilegeChanged();
    }

    public /* bridge */ /* synthetic */ void sinkSendVideoPrivilegeChanged() {
        super.sinkSendVideoPrivilegeChanged();
    }

    public /* bridge */ /* synthetic */ void sinkShareActiveUser(long j) {
        super.sinkShareActiveUser(j);
    }

    public /* bridge */ /* synthetic */ void sinkShareDataSizeChanged(long j) {
        super.sinkShareDataSizeChanged(j);
    }

    public /* bridge */ /* synthetic */ void sinkShareUserReceivingStatus(long j) {
        super.sinkShareUserReceivingStatus(j);
    }

    public /* bridge */ /* synthetic */ void sinkShareUserSendingStatus(long j) {
        super.sinkShareUserSendingStatus(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserActiveVideo(long j) {
        super.sinkUserActiveVideo(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserActiveVideoForDeck(long j) {
        super.sinkUserActiveVideoForDeck(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserTalkingVideo(long j) {
        super.sinkUserTalkingVideo(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoDataSizeChanged(long j) {
        super.sinkUserVideoDataSizeChanged(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoMutedByHost(long j) {
        super.sinkUserVideoMutedByHost(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoParticipantUnmuteLater(long j) {
        super.sinkUserVideoParticipantUnmuteLater(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoQualityChanged(long j) {
        super.sinkUserVideoQualityChanged(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoRequestUnmuteByHost(long j) {
        super.sinkUserVideoRequestUnmuteByHost(j);
    }

    public /* bridge */ /* synthetic */ void sinkUserVideoStatus(long j) {
        super.sinkUserVideoStatus(j);
    }

    public /* bridge */ /* synthetic */ void sinkVideoLeaderShipModeOnOff() {
        super.sinkVideoLeaderShipModeOnOff();
    }

    public /* bridge */ /* synthetic */ void startShareImage(Uri uri, boolean z) {
        super.startShareImage(uri, z);
    }

    public /* bridge */ /* synthetic */ void startSharePDF(Uri uri, boolean z) {
        super.startSharePDF(uri, z);
    }

    public /* bridge */ /* synthetic */ void startShareScreen(@Nullable Intent intent) {
        super.startShareScreen(intent);
    }

    public /* bridge */ /* synthetic */ void startShareWebview(@Nullable String str) {
        super.startShareWebview(str);
    }

    public /* bridge */ /* synthetic */ void stopShare() {
        super.stopShare();
    }

    public /* bridge */ /* synthetic */ void switchCamera(@NonNull String str) {
        super.switchCamera(str);
    }

    public /* bridge */ /* synthetic */ void switchToSmallShare() {
        super.switchToSmallShare();
    }

    private ZMConfComponentMgr() {
    }

    public static ZMConfComponentMgr getInstance() {
        return ourInstance;
    }

    public void initVideoSceneMgr(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.initVideoSceneMgr(absVideoSceneMgr);
        }
    }

    public void handleCmdConfSilentModeChanged() {
        if (!ConfShareLocalHelper.isOtherScreenSharing()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            long activeUserID = videoObj != null ? videoObj.getActiveUserID() : 0;
            if (activeUserID > 0 && this.mZmConfVideoComponent != null) {
                this.mZmConfVideoComponent.sinkUserActiveVideo(activeUserID);
            }
        } else if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.handleSilentModeChangedForOtherScreenSharing();
        }
    }
}
