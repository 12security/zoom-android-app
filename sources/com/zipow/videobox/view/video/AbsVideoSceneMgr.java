package com.zipow.videobox.view.video;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.AccessibilityUtil;

public abstract class AbsVideoSceneMgr {
    private static final String TAG = "AbsVideoSceneMgr";
    private long mActiveSpeakerId = 0;
    private long mActiveUserId = 0;
    @Nullable
    private ConfActivity mConfActivity;
    private boolean mIsVideoShareInProgress = false;
    @Nullable
    private VideoUnit mKeyVideoUnit;
    private long mLockedUserId = 0;
    protected VideoRenderer mRenderer;
    @NonNull
    protected List<AbsVideoScene> mSceneList = new ArrayList();
    private long mShareActiveUserId = 0;
    private VideoAccessibilityHelper mVideoAccessibilityHelper;
    @NonNull
    private final VideoBoxApplication mVideoBoxApplication;
    private View mVideoView;
    private boolean mbDestroyed = true;
    private boolean mbNetworkRestrictionMode = false;

    private class VideoAccessibilityHelper extends ExploreByTouchHelper {
        /* access modifiers changed from: protected */
        public boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            return false;
        }

        public VideoAccessibilityHelper(@NonNull View view) {
            super(view);
        }

        private int getViewIndexAt(float f, float f2) {
            AbsVideoScene activeScene = AbsVideoSceneMgr.this.getActiveScene();
            if (activeScene != null) {
                return activeScene.getAccessbilityViewIndexAt(f, f2);
            }
            return -1;
        }

        @NonNull
        private Rect getBoundsForIndex(int i) {
            AbsVideoScene activeScene = AbsVideoSceneMgr.this.getActiveScene();
            if (activeScene != null) {
                return activeScene.getBoundsForAccessbilityViewIndex(i);
            }
            return new Rect();
        }

        @NonNull
        private CharSequence getDescriptionForIndex(int i) {
            AbsVideoScene activeScene = AbsVideoSceneMgr.this.getActiveScene();
            return activeScene != null ? activeScene.getAccessibilityDescriptionForIndex(i) : "";
        }

        /* access modifiers changed from: protected */
        public int getVirtualViewAt(float f, float f2) {
            int viewIndexAt = getViewIndexAt(f, f2);
            if (viewIndexAt >= 0) {
                return viewIndexAt;
            }
            return Integer.MIN_VALUE;
        }

        /* access modifiers changed from: protected */
        public void getVisibleVirtualViews(List<Integer> list) {
            if (AbsVideoSceneMgr.this.getActiveScene() != null) {
                AbsVideoSceneMgr.this.getActiveScene().getAccessibilityVisibleVirtualViews(list);
            }
        }

        /* access modifiers changed from: protected */
        public void onPopulateEventForVirtualView(int i, @NonNull AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.setContentDescription(getDescriptionForIndex(i));
        }

        /* access modifiers changed from: protected */
        public void onPopulateNodeForVirtualView(int i, @NonNull AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setContentDescription(getDescriptionForIndex(i));
            Rect boundsForIndex = getBoundsForIndex(i);
            if (boundsForIndex.isEmpty()) {
                boundsForIndex.left = 1;
                boundsForIndex.right = 2;
                boundsForIndex.top = 1;
                boundsForIndex.bottom = 2;
            }
            accessibilityNodeInfoCompat.setBoundsInParent(boundsForIndex);
        }
    }

    public abstract void attendeeVideoControlChange(long j);

    public abstract void attendeeVideoLayoutChange(long j);

    public abstract void attendeeVideoLayoutFlagChange(long j);

    /* access modifiers changed from: protected */
    public void beforeNotifyScenesActiveVideoChanged(long j) {
    }

    /* access modifiers changed from: protected */
    public void beforeNotifyScenesGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void beforeNotifyScenesShareActiveUser(long j) {
    }

    @Nullable
    public abstract AbsVideoScene getActiveScene();

    public int getSceneCount() {
        return 1;
    }

    public boolean isInDefaultScene() {
        return true;
    }

    public boolean isInDriveModeScence() {
        return false;
    }

    public boolean isInNormalVideoScene() {
        return false;
    }

    public boolean isInShareVideoScene() {
        return false;
    }

    public boolean isViewingSharing() {
        return false;
    }

    public void onConfLeaving() {
    }

    public void onDoubleTap(MotionEvent motionEvent) {
    }

    public void onDown(MotionEvent motionEvent) {
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    public boolean onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    public abstract void scrollHorizontal(boolean z);

    public void switchToScene(int i) {
    }

    public AbsVideoSceneMgr(@NonNull VideoBoxApplication videoBoxApplication) {
        this.mVideoBoxApplication = videoBoxApplication;
    }

    @NonNull
    public VideoBoxApplication getmVideoBoxApplication() {
        return this.mVideoBoxApplication;
    }

    public long getActiveUserId() {
        return this.mActiveUserId;
    }

    public void setActiveUserId(long j) {
        this.mActiveUserId = j;
    }

    public long getActiveSpeakerId() {
        return this.mActiveSpeakerId;
    }

    public long getShareActiveUserId() {
        return this.mShareActiveUserId;
    }

    public void setShareActiveUserId(long j) {
        this.mShareActiveUserId = j;
    }

    public boolean isVideoShareInProgress() {
        return this.mIsVideoShareInProgress;
    }

    public void setIsVideoShareInProgress(boolean z) {
        this.mIsVideoShareInProgress = z;
    }

    public void setLockedUserId(long j) {
        this.mLockedUserId = j;
        ConfUI.getInstance().setLockedUserId(this.mLockedUserId);
    }

    public long getLockedUserId() {
        return this.mLockedUserId;
    }

    public VideoRenderer getVideoRenderer() {
        return this.mRenderer;
    }

    @Nullable
    public ConfActivity getConfActivity() {
        return this.mConfActivity;
    }

    public boolean isDestroyed() {
        return this.mbDestroyed;
    }

    public void setVideoView(View view) {
        this.mVideoView = view;
    }

    public void onConfActivityCreated(@Nullable ConfActivity confActivity) {
        this.mConfActivity = confActivity;
        if (confActivity != null) {
            this.mbNetworkRestrictionMode = confActivity.isNetworkRestrictionMode();
            View view = this.mVideoView;
            if (view != null) {
                this.mVideoAccessibilityHelper = new VideoAccessibilityHelper(view);
                ViewCompat.setAccessibilityDelegate(this.mVideoView, this.mVideoAccessibilityHelper);
            }
        }
    }

    public void onConfActivityResume() {
        this.mLockedUserId = ConfUI.getInstance().getLockedUserId();
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.start();
            }
        }
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        long j = 0;
        if (shareObj != null) {
            j = shareObj.getActiveUserID();
        }
        long activeVideo = ConfUI.getInstance().getActiveVideo();
        long activeSpeaker = ConfUI.getInstance().getActiveSpeaker();
        if (j != this.mShareActiveUserId) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && !confStatusObj.isMyself(j)) {
                onShareActiveUser(j);
            }
        }
        if (activeVideo != this.mActiveUserId) {
            onActiveVideoChanged(activeVideo);
        }
        if (activeSpeaker != this.mActiveSpeakerId) {
            onUserActiveVideoForDeck(activeSpeaker);
        }
    }

    public void onConfActivityPause() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.stop();
            }
        }
    }

    public void onConfUIRelayout(ConfActivity confActivity) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.onConfUIRelayout(confActivity);
            }
        }
    }

    public void updateVisibleScenes() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.updateUnits();
            }
        }
    }

    public void onGLRendererCreated(VideoRenderer videoRenderer) {
        this.mRenderer = videoRenderer;
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        this.mRenderer = videoRenderer;
        this.mbDestroyed = false;
        VideoUnit videoUnit = this.mKeyVideoUnit;
        if (videoUnit == null) {
            createKeyVideoUnit();
        } else {
            videoUnit.onGLViewSizeChanged(i, i2);
        }
        beforeNotifyScenesGLRendererChanged(videoRenderer, i, i2);
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() || absVideoScene.isCachedEnabled() || absVideoScene.hasUnits() || absVideoScene.isPreloadStatus()) {
                absVideoScene.onGLRendererChanged(videoRenderer, i, i2);
            }
        }
    }

    public boolean stopPreviewDevice() {
        return stopPreviewDevice(getPreviewRenderInfo());
    }

    public boolean stopPreviewDevice(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null || !videoObj.isPreviewing()) {
            return false;
        }
        return videoObj.stopPreviewDevice(j);
    }

    public void onGLRendererNeedDestroy() {
        MeetingReactionMgr.getInstance().unregisterAllUnits();
        destroyKeyVideoUnit();
        for (int i = 0; i < this.mSceneList.size(); i++) {
            AbsVideoScene absVideoScene = (AbsVideoScene) this.mSceneList.get(i);
            if (absVideoScene.isVisible() || absVideoScene.hasUnits()) {
                absVideoScene.destroy();
            }
            if (absVideoScene.isCachedEnabled()) {
                absVideoScene.destroyCachedUnits();
            }
        }
        this.mbDestroyed = true;
    }

    public void onIdle() {
        for (int i = 0; i < this.mSceneList.size(); i++) {
            AbsVideoScene absVideoScene = (AbsVideoScene) this.mSceneList.get(i);
            if (absVideoScene.isVisible()) {
                absVideoScene.onIdle();
            }
        }
    }

    public void onActiveVideoChanged(long j) {
        if (getActiveUserId() != j) {
            setActiveUserId(j);
            onActiveVideoChangedRefreshUIDirectly(j);
        }
    }

    public void onActiveVideoChangedRefreshUIDirectly(long j) {
        beforeNotifyScenesActiveVideoChanged(j);
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onActiveVideoChanged(j);
            }
        }
    }

    public void onUserActiveVideoForDeck(long j) {
        this.mActiveSpeakerId = j;
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserActiveVideoForDeck(j);
            }
        }
        if (ConfMgr.getInstance().noOneIsSendingVideo()) {
            onActiveVideoChanged(j);
        }
    }

    public void onUserTalkingVideo(long j) {
        onUserActiveVideoForDeck(j);
    }

    public void onShareActiveUser(long j) {
        setShareActiveUserId(j);
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            setIsVideoShareInProgress(shareObj.isVideoSharingInProgress());
        }
        beforeNotifyScenesShareActiveUser(j);
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onShareActiveUser(j);
            }
        }
    }

    public void onShareUserReceivingStatus(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onShareUserReceivingStatus(j);
            }
        }
    }

    public void onShareUserSendingStatus(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onShareUserSendingStatus(j);
            }
        }
    }

    public void onShareDataSizeChanged(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onShareDataSizeChanged(j);
            }
        }
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if ((absVideoScene.isVisible() || absVideoScene.isPreloadEnabled()) && absVideoScene.isStarted()) {
                absVideoScene.onGroupUserEvent(i, list);
            }
        }
    }

    public void onUserCountChangesForShowHideAction() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.onUserCountChangesForShowHideAction();
            }
        }
    }

    public boolean onHoverEvent(@NonNull MotionEvent motionEvent) {
        VideoAccessibilityHelper videoAccessibilityHelper = this.mVideoAccessibilityHelper;
        return videoAccessibilityHelper != null && videoAccessibilityHelper.dispatchHoverEvent(motionEvent);
    }

    public void onGroupUserVideoStatus(List<Long> list) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if ((absVideoScene.isVisible() || absVideoScene.isPreloadEnabled()) && absVideoScene.isStarted()) {
                absVideoScene.onGroupUserVideoStatus(list);
            }
        }
    }

    public void onUserVideoDataSizeChanged(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserVideoDataSizeChanged(j);
            }
        }
    }

    public void onUserVideoQualityChanged(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserVideoQualityChanged(j);
            }
        }
    }

    public void onUserAudioStatus(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserAudioStatus(j);
            }
        }
    }

    public void onAudioTypeChanged(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onAudioTypeChanged(j);
            }
        }
    }

    public void onUserActiveAudio(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserActiveAudio(j);
            }
        }
    }

    public void onUserPicReady(long j) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onUserPicReady(j);
            }
        }
    }

    public void onConfReady() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onConfReady();
            }
        }
    }

    public void onAutoStartVideo() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onAutoStartVideo();
            }
        }
    }

    public void onConfOne2One() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onConfOne2One();
            }
        }
    }

    public void beforeSwitchCamera() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.beforeSwitchCamera();
            }
        }
    }

    public void afterSwitchCamera() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.afterSwitchCamera();
            }
        }
    }

    public void stopAllScenes() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.stop();
            }
        }
    }

    public void startActiveScene() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible()) {
                absVideoScene.start();
            }
        }
    }

    public void onMyVideoStatusChanged() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onMyVideoStatusChanged();
            }
        }
    }

    public void onMyVideoRotationChanged(int i) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onMyVideoRotationChanged(i);
            }
        }
    }

    public void onLaunchConfParamReady() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onLaunchConfParamReady();
            }
        }
    }

    public void onConfVideoSendingStatusChanged() {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onConfVideoSendingStatusChanged();
            }
        }
    }

    public long getPreviewRenderInfo() {
        AbsVideoScene activeScene = getActiveScene();
        if (activeScene != null) {
            return activeScene.getPreviewRenderInfo();
        }
        return 0;
    }

    public void setNetworkRestrictionMode(boolean z) {
        this.mbNetworkRestrictionMode = z;
    }

    public boolean isNetworkRestrictionMode() {
        return this.mbNetworkRestrictionMode;
    }

    public boolean onNetworkRestrictionModeChanged(boolean z) {
        if (this.mbNetworkRestrictionMode == z) {
            return false;
        }
        this.mbNetworkRestrictionMode = z;
        return true;
    }

    public void onConfSilentModeChanged() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null ? confContext.inSilentMode() : false) {
            for (AbsVideoScene absVideoScene : this.mSceneList) {
                if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                    absVideoScene.stop();
                }
            }
            return;
        }
        for (AbsVideoScene absVideoScene2 : this.mSceneList) {
            if (absVideoScene2.isVisible()) {
                absVideoScene2.start();
            }
        }
    }

    public void onHostChanged(long j, boolean z) {
        for (AbsVideoScene absVideoScene : this.mSceneList) {
            if (absVideoScene.isVisible() && absVideoScene.isStarted()) {
                absVideoScene.onHostChanged(j, z);
            }
        }
    }

    private void createKeyVideoUnit() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            this.mKeyVideoUnit = videoObj.createVideoUnit(this.mVideoBoxApplication, true, new RendererUnitInfo(0, 0, 1, 1));
        }
    }

    private void destroyKeyVideoUnit() {
        if (this.mKeyVideoUnit != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                videoObj.destroyVideoUnit(this.mKeyVideoUnit);
                this.mKeyVideoUnit = null;
            }
        }
    }

    public void onAccessibilityRootViewUpdated() {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getConfActivity())) {
            VideoAccessibilityHelper videoAccessibilityHelper = this.mVideoAccessibilityHelper;
            if (videoAccessibilityHelper != null) {
                videoAccessibilityHelper.invalidateRoot();
            }
        }
    }

    public void onAccessibilityViewUpdated(int i) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getConfActivity())) {
            VideoAccessibilityHelper videoAccessibilityHelper = this.mVideoAccessibilityHelper;
            if (videoAccessibilityHelper != null) {
                videoAccessibilityHelper.invalidateVirtualView(i);
            }
        }
    }

    public void announceAccessibilityAtView(int i) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getConfActivity())) {
            VideoAccessibilityHelper videoAccessibilityHelper = this.mVideoAccessibilityHelper;
            if (videoAccessibilityHelper != null && videoAccessibilityHelper.getFocusedVirtualView() == i) {
                this.mVideoAccessibilityHelper.sendEventForVirtualView(i, 16384);
            }
        }
    }

    public void announceAccessibilityAtActiveScene() {
        if (this.mVideoView != null && getConfActivity() != null && AccessibilityUtil.isSpokenFeedbackEnabled(getConfActivity())) {
            try {
                this.mVideoView.sendAccessibilityEvent(8);
            } catch (Exception unused) {
            }
        }
    }

    public void updateAccessibilityDescriptionForActiveScece(String str) {
        if (this.mVideoView != null && getConfActivity() != null) {
            this.mVideoView.setContentDescription(str);
        }
    }

    public int getClientWithoutOnHoldIncludeAttendeeUserCount() {
        return ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false);
    }

    public int getClientWithoutOnHoldIncludeAttendeeUserCount(boolean z) {
        int clientWithoutOnHoldUserCount = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false);
        if (ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView()) {
            return clientWithoutOnHoldUserCount;
        }
        if (z) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null || !hasVideo(myself)) {
                return clientWithoutOnHoldUserCount;
            }
            if (clientWithoutOnHoldUserCount > 1) {
                clientWithoutOnHoldUserCount--;
            }
            return clientWithoutOnHoldUserCount;
        }
        if (clientWithoutOnHoldUserCount > 1) {
            clientWithoutOnHoldUserCount--;
        }
        return clientWithoutOnHoldUserCount;
    }

    public int getTotalVideoCount() {
        if (ConfLocalHelper.isHideNoVideoUsers()) {
            return getVideoUserCount();
        }
        return getClientWithoutOnHoldIncludeAttendeeUserCount(false);
    }

    public boolean isMeetSwitchToGalleryView() {
        boolean z = true;
        if (ConfMgr.getInstance().isViewOnlyClientOnMMR()) {
            if (getVideoUserCount() <= 0) {
                z = false;
            }
            return z;
        }
        if (getTotalVideoCount() < 2) {
            z = false;
        }
        return z;
    }

    public boolean shouldSwitchActiveSpeakerView() {
        if (!(this instanceof VideoSceneMgr) || isMeetSwitchToGalleryView()) {
            return false;
        }
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this;
        if (!(videoSceneMgr.getActiveScene() instanceof DriverModeVideoScene)) {
            videoSceneMgr.switchToDefaultScene();
        }
        return true;
    }

    private int getVideoUserCount() {
        ConfMgr instance = ConfMgr.getInstance();
        int videoUserCount = instance.getVideoUserCount();
        CmmUser myself = instance.getMyself();
        if (instance.getConfDataHelper().ismIsShowMyVideoInGalleryView() || myself == null || !hasVideo(myself)) {
            return videoUserCount;
        }
        if (videoUserCount > 1) {
            videoUserCount--;
        }
        return videoUserCount;
    }

    public boolean hasVideo(@Nullable CmmUser cmmUser) {
        boolean z = false;
        if (cmmUser == null || cmmUser.isMMRUser() || cmmUser.isPureCallInUser() || cmmUser.inSilentMode()) {
            return false;
        }
        CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
        if (videoStatusObj != null && videoStatusObj.getIsSending()) {
            z = true;
        }
        return z;
    }
}
