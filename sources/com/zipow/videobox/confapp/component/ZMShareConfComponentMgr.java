package com.zipow.videobox.confapp.component;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.component.sink.share.IShareSink;
import com.zipow.videobox.confapp.component.sink.share.IShareStatusSink;
import com.zipow.videobox.confapp.component.sink.share.IShareUIInteractionSink;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.view.video.RCMouseView;
import com.zipow.videobox.view.video.ShareVideoScene;

class ZMShareConfComponentMgr extends ZMVideoConfComponentMgr implements IShareSink, IShareUIInteractionSink, IShareStatusSink {
    private static final String TAG = "ZMShareConfComponentMgr";
    @Nullable
    private IShareStatusSink mIShareStatusSink;

    ZMShareConfComponentMgr() {
    }

    public void setmIShareStatusSink(@Nullable IShareStatusSink iShareStatusSink) {
        this.mIShareStatusSink = iShareStatusSink;
    }

    public void sinkConfConnecting() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.sinkConfConnecting();
        }
    }

    public void sinkShareActiveUser(long j) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.sinkShareActiveUser(j);
        }
    }

    public void sinkShareUserReceivingStatus(long j) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.sinkShareUserReceivingStatus(j);
        }
    }

    public void sinkShareUserSendingStatus(long j) {
        if (ConfMgr.getInstance().getShareObj().getShareStatus() == 2) {
            if (this.mAbsVideoSceneMgr != null) {
                this.mAbsVideoSceneMgr.stopAllScenes();
            }
            if (this.mZmConfVideoComponent != null) {
                this.mZmConfVideoComponent.pauseRenderer();
            }
        } else if (!(this.mContext == null || !this.mContext.isActive() || this.mAbsVideoSceneMgr == null)) {
            this.mAbsVideoSceneMgr.onShareUserSendingStatus(j);
        }
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.sinkShareUserSendingStatus(j);
        }
    }

    public void sinkShareDataSizeChanged(long j) {
        if (this.mContext != null && this.mContext.isActive() && this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.sinkShareDataSizeChanged(j);
        }
    }

    public void onUserGetRemoteControlPrivilege(long j) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onUserGetRemoteControlPrivilege(j);
        }
    }

    public void remoteControlStarted(long j) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.remoteControlStarted(j);
        }
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onAnnotateStartedUp(z, j);
        }
    }

    public void onAnnotateShutDown() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onAnnotateShutDown();
        }
    }

    public void onWBPageChanged(int i, int i2, int i3, int i4) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onWBPageChanged(i, i2, i3, i4);
        }
    }

    public void onAnnotateViewSizeChanged() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onAnnotateViewSizeChanged();
        }
    }

    public void setPaddingForTranslucentStatus(int i, int i2, int i3, int i4) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.setPaddingForTranslucentStatus(i, i2, i3, i4);
        }
    }

    public void onLayoutChange(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onLayoutChange(i, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public void refreshAudioSharing(boolean z) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.refreshAudioSharing(z);
        }
    }

    public void showAnnotateViewWhenSceneChanged() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.showAnnotateViewWhenSceneChanged();
        }
    }

    public void onToolbarVisibilityChanged(boolean z) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onToolbarVisibilityChanged(z);
        }
    }

    public void onSwitchToOrOutShare(boolean z) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.onSwitchToOrOutShare(z);
        }
    }

    public void switchToSmallShare() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.changeShareViewVisibility();
            this.mZmConfShareComponent.beforeShrinkShareViewSize();
        }
    }

    public RCMouseView getRCMouseView() {
        if (this.mZmConfShareComponent != null) {
            return this.mZmConfShareComponent.getRCMouseView();
        }
        return null;
    }

    public boolean isAnnotationDrawingViewVisible() {
        if (this.mZmConfShareComponent != null) {
            return this.mZmConfShareComponent.isAnnotationDrawingViewVisible();
        }
        return false;
    }

    public void showShareTip() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.mbMarkedAsGrabShare = true;
            this.mZmConfShareComponent.showShareTip();
        }
    }

    public void startShareImage(Uri uri, boolean z) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.startShareImage(uri, z);
        }
    }

    public void startSharePDF(Uri uri, boolean z) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.startSharePDF(uri, z);
        }
    }

    public void showShareChoice() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.showShareChoice();
        }
    }

    public void shareByPathExtension(@Nullable String str) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.shareByPathExtension(str);
        }
    }

    public void startShareWebview(@Nullable String str) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.startShareWebview(str);
        }
    }

    public void startShareScreen(@Nullable Intent intent) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.startShareScreen(intent);
        }
    }

    public void selectShareType(@NonNull ShareOptionType shareOptionType) {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.selectShareType(shareOptionType);
        }
    }

    public void stopShare() {
        if (this.mZmConfShareComponent != null) {
            this.mZmConfShareComponent.stopShare();
        }
    }

    public ShareVideoScene getShareVideoScene() {
        if (this.mZmConfShareComponent != null) {
            return this.mZmConfShareComponent.getShareVideoScene();
        }
        return null;
    }

    public void onBeforeMyStartShare() {
        IShareStatusSink iShareStatusSink = this.mIShareStatusSink;
        if (iShareStatusSink != null) {
            iShareStatusSink.onBeforeMyStartShare();
        }
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkBeforeMyStartShare();
        }
    }

    public void onMyShareStatueChanged(boolean z) {
        IShareStatusSink iShareStatusSink = this.mIShareStatusSink;
        if (iShareStatusSink != null) {
            iShareStatusSink.onMyShareStatueChanged(z);
        }
    }

    public void onOtherShareStatueChanged(boolean z, long j) {
        if (this.mZmConfVideoComponent != null) {
            this.mZmConfVideoComponent.sinkOtherShareStatueChanged(z);
        }
        IShareStatusSink iShareStatusSink = this.mIShareStatusSink;
        if (iShareStatusSink != null) {
            iShareStatusSink.onOtherShareStatueChanged(z, j);
        }
    }

    public void onShareEdit(boolean z) {
        IShareStatusSink iShareStatusSink = this.mIShareStatusSink;
        if (iShareStatusSink != null) {
            iShareStatusSink.onShareEdit(z);
        }
    }

    public void onBeforeRemoteControlEnabled(boolean z) {
        IShareStatusSink iShareStatusSink = this.mIShareStatusSink;
        if (iShareStatusSink != null) {
            iShareStatusSink.onBeforeRemoteControlEnabled(z);
        }
    }
}
