package com.zipow.videobox.view.video;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoCapturer;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.NormalMessageTip;
import java.util.List;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class VideoSceneMgr extends AbsVideoSceneMgr {
    private static final String TAG = "VideoSceneMgr";
    private boolean isInitLastHostVideoLayout;
    @Nullable
    private AbsVideoScene mActiveScene;
    private AbsVideoScene mComingScene;
    @Nullable
    private DriverModeVideoScene mDriverModeScene = null;
    @NonNull
    private GalleryVideoScene mGalleryScene1;
    @NonNull
    private GalleryVideoScene mGalleryScene2;
    private boolean mIsDraggingToLeft = false;
    private boolean mIsDraggingToRight = false;
    private boolean mIsDriverModeShownAsDefaultScene = false;
    private boolean mIsSwitchingScene = false;
    private int mLastHostVideoLayout = -1;
    @Nullable
    private AbsVideoScene mLastSceneBeforeShare = null;
    private int mMicMutedPreDriverMode = -1;
    private int mMicMutedPreDrivingModeParam = -1;
    @NonNull
    private final NormalVideoScene mNormalScene;
    private float mOldDist = 1.0f;
    private int mScaleStep = 0;
    @NonNull
    private final ShareVideoScene mShareScene;
    private int mVideoMutedPreDriverMode = -1;
    private int mVideoMutedPreDrivingModeParam = -1;
    private int mVideoUserCount = 0;

    public VideoSceneMgr(@NonNull VideoBoxApplication videoBoxApplication) {
        super(videoBoxApplication);
        if (UIMgr.isDriverModeEnabled()) {
            this.mDriverModeScene = new DriverModeVideoScene(this);
            this.mSceneList.add(this.mDriverModeScene);
        }
        this.mNormalScene = new NormalVideoScene(this);
        this.mNormalScene.setVisible(true);
        this.mSceneList.add(this.mNormalScene);
        if (isShowLargeShareVideoScene()) {
            this.mShareScene = new LargeShareVideoScene(this);
        } else {
            this.mShareScene = new ShareVideoScene(this);
        }
        this.mSceneList.add(this.mShareScene);
        this.mGalleryScene1 = new GalleryVideoScene(this);
        this.mSceneList.add(this.mGalleryScene1);
        this.mGalleryScene2 = new GalleryVideoScene(this);
        this.mSceneList.add(this.mGalleryScene2);
        this.mActiveScene = this.mNormalScene;
        this.mVideoUserCount = getTotalVideoCount();
    }

    public boolean isInShareVideoScene() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        return absVideoScene != null && (absVideoScene instanceof ShareVideoScene);
    }

    public boolean isInNormalVideoScene() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        return absVideoScene != null && (absVideoScene instanceof NormalVideoScene);
    }

    public boolean isInDriveModeScence() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        return absVideoScene != null && (absVideoScene instanceof DriverModeVideoScene);
    }

    @Nullable
    public AbsVideoScene getActiveScene() {
        return this.mActiveScene;
    }

    public boolean isViewingSharing() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        ShareVideoScene shareVideoScene = this.mShareScene;
        return absVideoScene == shareVideoScene && shareVideoScene.hasContent();
    }

    /* access modifiers changed from: protected */
    public void beforeNotifyScenesGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null && absVideoScene.isPreloadStatus()) {
            this.mActiveScene.setLocation(0, 0);
            this.mActiveScene.resumeVideo();
        }
    }

    /* access modifiers changed from: protected */
    public void beforeNotifyScenesShareActiveUser(long j) {
        if (j > 0) {
            if (this.mActiveScene != null && !this.mShareScene.isVisible()) {
                if (this.mComingScene != null) {
                    this.mActiveScene.setLocation(0, 0);
                    destroyComingScene();
                }
                AbsVideoScene absVideoScene = this.mActiveScene;
                this.mLastSceneBeforeShare = absVideoScene;
                DriverModeVideoScene driverModeVideoScene = this.mDriverModeScene;
                if (absVideoScene != driverModeVideoScene || driverModeVideoScene == null) {
                    if (this.mShareScene.isCachedEnabled()) {
                        this.mShareScene.destroyCachedUnits();
                    }
                    switchToShareScene();
                }
            }
        } else if (this.mActiveScene == this.mShareScene) {
            if (this.mComingScene != null) {
                destroyComingScene();
            }
            this.mShareScene.setCacheEnabled(false);
            if (!(this.mLastSceneBeforeShare instanceof GalleryVideoScene) || !isMeetSwitchToGalleryView()) {
                switchToNormalScene();
            } else {
                switchToGalleryScene(0);
            }
            this.mLastSceneBeforeShare = null;
            this.mShareScene.setCacheEnabled(true);
        } else {
            this.mLastSceneBeforeShare = null;
        }
    }

    private void destroyComingScene() {
        this.mComingScene.setVisible(false);
        this.mComingScene.stop();
        this.mComingScene.destroy();
        this.mComingScene = null;
    }

    public void onDoubleTap(MotionEvent motionEvent) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null) {
            absVideoScene.onDoubleTap(motionEvent);
        }
    }

    public void scrollHorizontal(boolean z) {
        if (!ConfMgr.getInstance().isCallingOut() && getSceneCount() > 1 && !this.mIsSwitchingScene) {
            boolean isDriverModeEnabled = UIMgr.isDriverModeEnabled();
            AbsVideoScene absVideoScene = this.mActiveScene;
            int i = (absVideoScene == this.mNormalScene || absVideoScene == this.mShareScene) ? (isDriverModeEnabled ? 1 : 0) + false : absVideoScene instanceof GalleryVideoScene ? ((GalleryVideoScene) absVideoScene).getPageIndex() + getBasicSceneCount() : 0;
            int i2 = (z ? -1 : 1) + i;
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 <= getSceneCount() - 1) {
                switchToScene(i2);
            }
        }
    }

    public boolean isInDefaultScene() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        return absVideoScene == this.mNormalScene || absVideoScene == this.mShareScene;
    }

    public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if ((confContext == null || !confContext.isInVideoCompanionMode()) && !ConfMgr.getInstance().isCallingOut()) {
            AbsVideoScene absVideoScene = this.mActiveScene;
            if (absVideoScene != null) {
                absVideoScene.onScroll(motionEvent, motionEvent2, f, f2);
            }
            boolean z = true;
            if (getSceneCount() > 1 || canScroll(f)) {
                int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i != 0 && !this.mIsSwitchingScene) {
                    AbsVideoScene absVideoScene2 = this.mComingScene;
                    if (absVideoScene2 == null) {
                        AbsVideoScene absVideoScene3 = this.mActiveScene;
                        DriverModeVideoScene driverModeVideoScene = this.mDriverModeScene;
                        if (absVideoScene3 != driverModeVideoScene || driverModeVideoScene == null) {
                            AbsVideoScene absVideoScene4 = this.mActiveScene;
                            if (absVideoScene4 != this.mNormalScene) {
                                ShareVideoScene shareVideoScene = this.mShareScene;
                                if (absVideoScene4 != shareVideoScene) {
                                    GalleryVideoScene galleryVideoScene = this.mGalleryScene1;
                                    if (absVideoScene4 != galleryVideoScene) {
                                        GalleryVideoScene galleryVideoScene2 = this.mGalleryScene2;
                                        if (absVideoScene4 == galleryVideoScene2) {
                                            if (f < 0.0f) {
                                                if (!galleryVideoScene2.hasPrevPage()) {
                                                    this.mComingScene = getShareActiveUserId() > 0 ? this.mShareScene : this.mNormalScene;
                                                } else {
                                                    GalleryVideoScene galleryVideoScene3 = this.mGalleryScene1;
                                                    this.mComingScene = galleryVideoScene3;
                                                    galleryVideoScene3.setPageIndex(this.mGalleryScene2.getPageIndex() - 1);
                                                }
                                                this.mComingScene.setVisible(true);
                                                this.mComingScene.setLocation((int) (((float) (-this.mRenderer.getWidth())) - f), 0);
                                            } else if (galleryVideoScene2.hasNextPage()) {
                                                this.mGalleryScene1.setPageIndex(this.mGalleryScene2.getPageIndex() + 1);
                                                this.mComingScene = this.mGalleryScene1;
                                                this.mComingScene.setVisible(true);
                                                this.mComingScene.setLocation((int) (((float) this.mRenderer.getWidth()) - f), 0);
                                            }
                                        }
                                    } else if (f < 0.0f) {
                                        if (!galleryVideoScene.hasPrevPage()) {
                                            this.mComingScene = getShareActiveUserId() > 0 ? this.mShareScene : this.mNormalScene;
                                        } else {
                                            GalleryVideoScene galleryVideoScene4 = this.mGalleryScene2;
                                            this.mComingScene = galleryVideoScene4;
                                            galleryVideoScene4.checkGalleryUnits(this.mRenderer.getWidth(), this.mRenderer.getHeight());
                                            this.mGalleryScene2.setPageIndex(this.mGalleryScene1.getPageIndex() - 1);
                                        }
                                        this.mComingScene.setVisible(true);
                                        this.mComingScene.setLocation((int) (((float) (-this.mRenderer.getWidth())) - f), 0);
                                    } else if (galleryVideoScene.hasNextPage()) {
                                        this.mGalleryScene2.checkGalleryUnits(this.mRenderer.getWidth(), this.mRenderer.getHeight());
                                        this.mGalleryScene2.setPageIndex(this.mGalleryScene1.getPageIndex() + 1);
                                        this.mComingScene = this.mGalleryScene2;
                                        this.mComingScene.setVisible(true);
                                        this.mComingScene.setLocation((int) (((float) this.mRenderer.getWidth()) - f), 0);
                                    }
                                } else if (i > 0 && shareVideoScene.canDragSceneToLeft() && !isGalleryVideoModeDisabled()) {
                                    this.mGalleryScene1.setPageIndex(0);
                                    this.mComingScene = this.mGalleryScene1;
                                    this.mComingScene.setVisible(true);
                                    this.mComingScene.setLocation((int) (((float) this.mRenderer.getWidth()) - f), 0);
                                } else if (f < 0.0f && UIMgr.isDriverModeEnabled() && this.mShareScene.canDragSceneToRight()) {
                                    this.mComingScene = this.mDriverModeScene;
                                    AbsVideoScene absVideoScene5 = this.mComingScene;
                                    if (absVideoScene5 != null) {
                                        absVideoScene5.setVisible(true);
                                        this.mComingScene.setLocation((int) (((float) (-this.mRenderer.getWidth())) - f), 0);
                                    }
                                }
                            } else if (i > 0 && getTotalVideoCount() >= 2 && !isGalleryVideoModeDisabled()) {
                                this.mGalleryScene1.setPageIndex(0);
                                this.mComingScene = this.mGalleryScene1;
                                this.mComingScene.setVisible(true);
                                this.mComingScene.setLocation((int) (((float) this.mRenderer.getWidth()) - f), 0);
                            } else if (f < 0.0f && UIMgr.isDriverModeEnabled()) {
                                this.mComingScene = this.mDriverModeScene;
                                AbsVideoScene absVideoScene6 = this.mComingScene;
                                if (absVideoScene6 != null) {
                                    absVideoScene6.setVisible(true);
                                    this.mComingScene.setLocation((int) (((float) (-this.mRenderer.getWidth())) - f), 0);
                                }
                            }
                        } else if (i > 0) {
                            this.mComingScene = getShareActiveUserId() > 0 ? this.mShareScene : this.mNormalScene;
                            this.mComingScene.setVisible(true);
                            this.mComingScene.setLocation((int) (((float) this.mRenderer.getWidth()) - f), 0);
                        }
                        if (this.mComingScene != null) {
                            this.mIsDraggingToLeft = i > 0;
                            if (f >= 0.0f) {
                                z = false;
                            }
                            this.mIsDraggingToRight = z;
                            AbsVideoScene absVideoScene7 = this.mComingScene;
                            if (absVideoScene7 instanceof GalleryVideoScene) {
                                ((GalleryVideoScene) absVideoScene7).updateContentSubscription();
                            }
                            this.mComingScene.create(this.mRenderer.getWidth(), this.mRenderer.getHeight(), false);
                            this.mComingScene.pauseVideo();
                            this.mComingScene.start();
                        }
                    } else if (this.mIsDraggingToLeft) {
                        int i2 = (int) f;
                        if (absVideoScene2.getLeft() - i2 < 0) {
                            this.mComingScene.setLocation(0, 0);
                        } else {
                            this.mComingScene.move(-i2, 0);
                        }
                    } else if (this.mIsDraggingToRight) {
                        int i3 = (int) f;
                        if (absVideoScene2.getLeft() - i3 > 0) {
                            this.mComingScene.setLocation(0, 0);
                        } else {
                            this.mComingScene.move(-i3, 0);
                        }
                    }
                    if (this.mComingScene != null) {
                        AbsVideoScene absVideoScene8 = this.mActiveScene;
                        if (absVideoScene8 != null) {
                            absVideoScene8.pauseVideo();
                            if (this.mIsDraggingToLeft) {
                                int i4 = (int) f;
                                if ((this.mActiveScene.getLeft() - i4) + this.mActiveScene.getWidth() < 0) {
                                    AbsVideoScene absVideoScene9 = this.mActiveScene;
                                    absVideoScene9.setLocation(-absVideoScene9.getWidth(), 0);
                                } else {
                                    this.mActiveScene.move(-i4, 0);
                                }
                            } else if (this.mIsDraggingToRight) {
                                int i5 = (int) f;
                                if (this.mActiveScene.getLeft() - i5 > this.mRenderer.getWidth()) {
                                    this.mActiveScene.setLocation(this.mRenderer.getWidth(), 0);
                                } else {
                                    this.mActiveScene.move(-i5, 0);
                                }
                            }
                        }
                    }
                    AbsVideoScene absVideoScene10 = this.mComingScene;
                    if (absVideoScene10 != null) {
                        absVideoScene10.onDraggingIn();
                        onDraggingVideoScene();
                    }
                }
            }
        }
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null) {
            absVideoScene.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    public void onDown(MotionEvent motionEvent) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null) {
            absVideoScene.onDown(motionEvent);
        }
    }

    private void switchScreen() {
        this.mIsSwitchingScene = true;
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null && absVideoScene.isVisible()) {
            unPinVideo();
            if (this.mActiveScene.isCachedEnabled()) {
                this.mActiveScene.cacheUnits();
            }
            this.mActiveScene.setVisible(false);
            this.mActiveScene.stop();
            absVideoScene.destroy();
            this.mActiveScene = null;
        }
        this.mComingScene.setLocation(0, 0);
        this.mActiveScene = this.mComingScene;
        this.mComingScene = null;
        this.mIsSwitchingScene = false;
        onVideoSceneChanged(absVideoScene, this.mActiveScene);
        onDropVideoScene(true);
        this.mActiveScene.resumeVideo();
        onSceneChanged();
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if ((this.mActiveScene instanceof NormalVideoScene) && this.mNormalScene.isZoomScene() && motionEvent.getPointerCount() > 1 && VideoCapturer.getInstance().isZoomSupported() && VideoCapturer.getInstance().getMaxZoom() > 0) {
            checkZoomInOrOut(motionEvent);
            if (motionEvent.getActionMasked() == 2) {
                return true;
            }
        }
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null && absVideoScene.onTouchEvent(motionEvent)) {
            return true;
        }
        if (motionEvent.getActionMasked() == 1) {
            AbsVideoScene absVideoScene2 = this.mComingScene;
            if (absVideoScene2 != null && !this.mIsSwitchingScene) {
                if ((this.mIsDraggingToLeft && absVideoScene2.getLeft() < (this.mRenderer.getWidth() * 2) / 3) || (this.mIsDraggingToRight && this.mComingScene.getRight() > this.mRenderer.getWidth() / 3)) {
                    switchScreen();
                } else if (this.mComingScene.isVisible()) {
                    this.mIsSwitchingScene = true;
                    if (this.mComingScene.isCachedEnabled()) {
                        this.mComingScene.cacheUnits();
                    }
                    if (this.mActiveScene != null) {
                        this.mComingScene.setLocation(Integer.MIN_VALUE, 0);
                        this.mActiveScene.setLocation(0, 0);
                        this.mActiveScene.resumeVideo();
                    }
                    this.mComingScene.setVisible(false);
                    this.mComingScene.stop();
                    this.mComingScene.destroy();
                    this.mComingScene = null;
                    this.mIsSwitchingScene = false;
                    onDropVideoScene(false);
                }
                return true;
            }
        }
        if (motionEvent.getActionMasked() == 1) {
            onDropVideoScene(false);
        }
        return false;
    }

    private void checkZoomInOrOut(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 2) {
            float fingerSpacing = getFingerSpacing(motionEvent);
            float f = this.mOldDist;
            if (fingerSpacing > f) {
                VideoCapturer.getInstance().handleZoom(true, getScale(fingerSpacing - this.mOldDist));
            } else if (fingerSpacing < f) {
                VideoCapturer.getInstance().handleZoom(false, getScale(this.mOldDist - fingerSpacing));
            }
            this.mOldDist = fingerSpacing;
        } else if (action == 5) {
            this.mOldDist = getFingerSpacing(motionEvent);
        }
    }

    private int getScale(float f) {
        if (this.mScaleStep == 0) {
            VideoBoxApplication videoBoxApplication = getmVideoBoxApplication();
            this.mScaleStep = ((int) Math.sqrt((double) ((UIUtil.getDisplayWidth(videoBoxApplication) * UIUtil.getDisplayWidth(videoBoxApplication)) + (UIUtil.getDisplayHeight(videoBoxApplication) * UIUtil.getDisplayHeight(videoBoxApplication))))) / VideoCapturer.getInstance().getMaxZoom();
        }
        return Math.round(f / ((float) this.mScaleStep));
    }

    private float getFingerSpacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void onSwitchOutDriverMode() {
        this.mMicMutedPreDrivingModeParam = -1;
        this.mVideoMutedPreDrivingModeParam = -1;
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            ConfMgr instance = ConfMgr.getInstance();
            if (this.mIsDriverModeShownAsDefaultScene) {
                this.mMicMutedPreDriverMode = -1;
                this.mIsDriverModeShownAsDefaultScene = false;
            }
            if (!instance.canUnmuteMyself() || this.mMicMutedPreDriverMode != 0) {
                CmmUser myself = instance.getMyself();
                if (myself != null) {
                    CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                    if (!(audioStatusObj == null || audioStatusObj.getAudiotype() == 2 || !audioStatusObj.getIsMuted())) {
                        NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_MUTED.name(), (String) null, confActivity.getResources().getString(C4558R.string.zm_msg_driving_mode_message_muted), 3000);
                    }
                }
            } else {
                confActivity.muteAudio(false);
                NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_UNMUTED.name(), (String) null, confActivity.getResources().getString(C4558R.string.zm_msg_driving_mode_message_unmuted), 3000);
            }
            if (this.mVideoMutedPreDriverMode == 0) {
                ZMConfComponentMgr.getInstance().sinkInMuteVideo(false);
            }
            CmmConfContext confContext = instance.getConfContext();
            if (confContext != null) {
                ParamsList appContextParams = confContext.getAppContextParams();
                appContextParams.remove("micMutedPreDrivingMode");
                appContextParams.remove("videoMutedPreDrivingMode");
                confContext.setAppContextParams(appContextParams);
            }
        }
    }

    private void onSwitchToDriverMode() {
        boolean z;
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            ConfMgr instance = ConfMgr.getInstance();
            CmmUser myself = instance.getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
                    if (videoStatusObj != null) {
                        boolean isMuted = audioStatusObj.getIsMuted();
                        int i = 1;
                        if (!isMuted) {
                            confActivity.muteAudio(true);
                        }
                        VideoSessionMgr videoObj = instance.getVideoObj();
                        if (videoObj != null) {
                            z = !videoObj.isVideoStarted();
                            if (!z) {
                                ZMConfComponentMgr.getInstance().sinkInMuteVideo(true);
                            }
                        } else {
                            z = false;
                        }
                        int i2 = this.mMicMutedPreDrivingModeParam;
                        if (i2 >= 0) {
                            this.mMicMutedPreDriverMode = i2;
                        } else if (audioStatusObj.getAudiotype() == 2) {
                            this.mMicMutedPreDriverMode = -1;
                        } else {
                            this.mMicMutedPreDriverMode = isMuted ? 1 : 0;
                        }
                        int i3 = this.mVideoMutedPreDrivingModeParam;
                        if (i3 >= 0) {
                            this.mVideoMutedPreDriverMode = i3;
                        } else if (videoStatusObj.getIsSource()) {
                            if (!z) {
                                i = 0;
                            }
                            this.mVideoMutedPreDriverMode = i;
                        } else {
                            this.mVideoMutedPreDriverMode = -1;
                        }
                        CmmConfContext confContext = instance.getConfContext();
                        if (confContext != null) {
                            ParamsList appContextParams = confContext.getAppContextParams();
                            appContextParams.putInt("micMutedPreDrivingMode", this.mMicMutedPreDriverMode);
                            appContextParams.putInt("videoMutedPreDrivingMode", this.mVideoMutedPreDriverMode);
                            confContext.setAppContextParams(appContextParams);
                        }
                    }
                }
            }
        }
    }

    public boolean onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene != null) {
            return absVideoScene.onVideoViewSingleTapConfirmed(motionEvent);
        }
        return false;
    }

    public void showPinModeInNormalScene() {
        if (this.mNormalScene.isExchangedMode()) {
            this.mNormalScene.setExchangedMode(false);
        }
        switchToScene((AbsVideoScene) this.mNormalScene);
    }

    public void switchToNormalScene() {
        switchToScene((AbsVideoScene) this.mNormalScene);
    }

    private void switchToShareScene() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (!(absVideoScene == null || absVideoScene == this.mShareScene)) {
            absVideoScene.pauseVideo();
            this.mActiveScene.grantUnitsTo(this.mShareScene);
        }
        switchToScene((AbsVideoScene) this.mShareScene);
    }

    public void switchToDriverModeScene() {
        if (canSwitchScene(this.mDriverModeScene)) {
            DriverModeVideoScene driverModeVideoScene = this.mDriverModeScene;
            if (driverModeVideoScene != null) {
                boolean z = true;
                if (this.mVideoMutedPreDrivingModeParam == 1) {
                    z = false;
                }
                driverModeVideoScene.setIsVideoOnPrevDrivingMode(z);
            }
            switchToScene((AbsVideoScene) this.mDriverModeScene);
        }
    }

    public void restoreDriverModeSceneOnFailoverSuccess() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            ParamsList appContextParams = confContext.getAppContextParams();
            this.mMicMutedPreDrivingModeParam = appContextParams.getInt("micMutedPreDrivingMode", -1);
            this.mVideoMutedPreDrivingModeParam = appContextParams.getInt("videoMutedPreDrivingMode", -1);
            switchToDriverModeScene();
        }
    }

    public void switchToDriverModeSceneAsDefaultScene() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && canSwitchScene(this.mDriverModeScene)) {
            this.mMicMutedPreDrivingModeParam = 0;
            this.mVideoMutedPreDrivingModeParam = confContext.isVideoOn() ^ true ? 1 : 0;
            this.mIsDriverModeShownAsDefaultScene = true;
            switchToScene((AbsVideoScene) this.mDriverModeScene);
        }
    }

    public void switchToDefaultScene() {
        switchToScene(getShareActiveUserId() > 0 ? this.mShareScene : this.mNormalScene);
    }

    public boolean onNetworkRestrictionModeChanged(boolean z) {
        boolean onNetworkRestrictionModeChanged = super.onNetworkRestrictionModeChanged(z);
        if (onNetworkRestrictionModeChanged) {
            AbsVideoScene absVideoScene = this.mActiveScene;
            if (absVideoScene != null) {
                absVideoScene.onNetworkRestrictionModeChanged(z);
            }
        }
        return onNetworkRestrictionModeChanged;
    }

    private String getAccessibliityDescriptionDefaultSceneSwitch() {
        if (getShareActiveUserId() > 0) {
            return getmVideoBoxApplication().getString(C4558R.string.zm_description_btn_switch_share_scene);
        }
        return getmVideoBoxApplication().getString(C4558R.string.zm_description_btn_switch_normal_scene);
    }

    @NonNull
    public String getAccessibliltyDescriptionSceneSwitch(int i) {
        if (UIMgr.isDriverModeEnabled()) {
            if (i == 0) {
                return getmVideoBoxApplication().getString(C4558R.string.zm_description_btn_switch_driving_scene);
            }
            if (i == 1) {
                return getAccessibliityDescriptionDefaultSceneSwitch();
            }
        } else if (i == 0) {
            return getAccessibliityDescriptionDefaultSceneSwitch();
        }
        return getmVideoBoxApplication().getString(C4558R.string.zm_description_btn_switch_gallery_scene);
    }

    public void switchToScene(int i) {
        if (UIMgr.isDriverModeEnabled()) {
            if (i == 0) {
                switchToDriverModeScene();
                return;
            } else if (i == 1) {
                switchToDefaultScene();
                return;
            }
        } else if (i == 0) {
            switchToDefaultScene();
            return;
        }
        GalleryVideoScene galleryVideoScene = this.mGalleryScene1;
        if (galleryVideoScene.isVisible()) {
            galleryVideoScene = this.mGalleryScene2;
        }
        if (!galleryVideoScene.isVisible() && canSwitchScene(galleryVideoScene)) {
            galleryVideoScene.setPageIndex(i - getBasicSceneCount());
            switchToScene((AbsVideoScene) galleryVideoScene);
        }
    }

    public void switchToGalleryScene(int i) {
        GalleryVideoScene galleryVideoScene = this.mGalleryScene1;
        if (!galleryVideoScene.isVisible()) {
            galleryVideoScene.setPageIndex(i);
            switchToScene((AbsVideoScene) galleryVideoScene);
        }
    }

    public void switchToScene(@Nullable AbsVideoScene absVideoScene) {
        if (canSwitchScene(absVideoScene)) {
            AbsVideoScene absVideoScene2 = this.mActiveScene;
            if (absVideoScene2 != null && absVideoScene2 != absVideoScene && absVideoScene != null && !this.mIsSwitchingScene) {
                this.mIsSwitchingScene = true;
                if (absVideoScene2.isCachedEnabled()) {
                    this.mActiveScene.cacheUnits();
                }
                AbsVideoScene absVideoScene3 = this.mActiveScene;
                absVideoScene3.setVisible(false);
                absVideoScene.setVisible(true);
                this.mActiveScene = null;
                absVideoScene3.pauseVideo();
                absVideoScene3.stop();
                absVideoScene3.destroy();
                if (absVideoScene instanceof GalleryVideoScene) {
                    ((GalleryVideoScene) absVideoScene).checkGalleryUnits(this.mRenderer.getWidth(), this.mRenderer.getHeight());
                }
                absVideoScene.create(this.mRenderer.getWidth(), this.mRenderer.getHeight());
                absVideoScene.setLocation(0, 0);
                absVideoScene.start();
                this.mActiveScene = absVideoScene;
                this.mIsSwitchingScene = false;
                onVideoSceneChanged(absVideoScene3, this.mActiveScene);
                this.mActiveScene.resumeVideo();
            }
        }
    }

    public void onConfReady() {
        super.onConfReady();
        this.mNormalScene.setPreloadEnabled(true);
        this.mGalleryScene1.preload();
        this.mGalleryScene2.preload();
        DriverModeVideoScene driverModeVideoScene = this.mDriverModeScene;
        if (driverModeVideoScene != null) {
            driverModeVideoScene.setPreloadEnabled(true);
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null || confContext.getAppContextParams().getInt("drivingMode", -1) != 1) {
                this.mDriverModeScene.preload();
            }
        }
    }

    public void onConfLeaving() {
        this.mNormalScene.setCanStartPreview(false);
    }

    public void onHostChanged(long j, boolean z) {
        super.onHostChanged(j, z);
        if (z) {
            onSceneChanged();
        }
    }

    public void onShareActiveUser(long j) {
        super.onShareActiveUser(j);
        checkCurrentScene();
    }

    public void onGroupUserVideoStatus(List<Long> list) {
        super.onGroupUserVideoStatus(list);
        checkCurrentScene();
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        if (!this.isInitLastHostVideoLayout) {
            initLastHostVideoLayout();
        }
        super.onGroupUserEvent(i, list);
        if (!shouldSwitchActiveSpeakerView()) {
            checkCurrentScene();
            if (this.isInitLastHostVideoLayout && this.mLastHostVideoLayout != -1) {
                restoreHostVideoLayout();
            }
            int i2 = this.mVideoUserCount;
            this.mVideoUserCount = getTotalVideoCount();
            VideoLayoutHelper instance = VideoLayoutHelper.getInstance();
            if (instance != null && instance.isSwitchVideoLayoutAccordingToUserCountEnabled() && !ConfLocalHelper.isViewOnlyButNotSupportMMR()) {
                int switchVideoLayoutUserCountThreshold = instance.getSwitchVideoLayoutUserCountThreshold();
                if (i2 >= switchVideoLayoutUserCountThreshold || this.mVideoUserCount < switchVideoLayoutUserCountThreshold) {
                    if (i2 >= switchVideoLayoutUserCountThreshold && this.mVideoUserCount < switchVideoLayoutUserCountThreshold && (this.mActiveScene instanceof GalleryVideoScene)) {
                        switchToNormalScene();
                    }
                } else if (this.mActiveScene instanceof NormalVideoScene) {
                    switchToScene((AbsVideoScene) this.mGalleryScene1);
                }
            }
        }
    }

    private void onVideoSceneChanged(AbsVideoScene absVideoScene, AbsVideoScene absVideoScene2) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            confActivity.onVideoSceneChanged(absVideoScene, absVideoScene2);
            if (absVideoScene != absVideoScene2) {
                ZMConfEventTracking.logSwitchModeViewInMeeting(absVideoScene2);
            }
            DriverModeVideoScene driverModeVideoScene = this.mDriverModeScene;
            boolean z = true;
            boolean z2 = absVideoScene == driverModeVideoScene && driverModeVideoScene != null;
            DriverModeVideoScene driverModeVideoScene2 = this.mDriverModeScene;
            if (absVideoScene2 != driverModeVideoScene2 || driverModeVideoScene2 == null) {
                z = false;
            }
            if (z) {
                onSwitchToDriverMode();
            } else if (z2) {
                onSwitchOutDriverMode();
            }
            onAccessibilityRootViewUpdated();
            announceAccessibilityAtActiveScene();
            onSceneChanged();
        }
    }

    private void onDraggingVideoScene() {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            confActivity.onDraggingVideoScene();
        }
    }

    private void onDropVideoScene(boolean z) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            confActivity.onDropVideoScene(z);
        }
    }

    public int getSceneCount() {
        GalleryVideoScene galleryVideoScene;
        int i = 1;
        if (!ConfMgr.getInstance().isConfConnected() || ConfMgr.getInstance().isViewOnlyMeeting()) {
            return 1;
        }
        if (isGalleryVideoModeDisabled()) {
            return getBasicSceneCount();
        }
        int totalVideoCount = getTotalVideoCount();
        if (totalVideoCount < 2 && getShareActiveUserId() == 0 && !(this.mActiveScene instanceof GalleryVideoScene)) {
            return getBasicSceneCount();
        }
        int basicSceneCount = getBasicSceneCount();
        AbsVideoScene absVideoScene = this.mActiveScene;
        if (absVideoScene instanceof GalleryVideoScene) {
            galleryVideoScene = (GalleryVideoScene) absVideoScene;
        } else {
            galleryVideoScene = this.mGalleryScene1;
            galleryVideoScene.updateLayoutInfo();
        }
        int unitsCount = galleryVideoScene.getUnitsCount();
        if (unitsCount == 0) {
            galleryVideoScene.updateLayoutInfo();
            unitsCount = galleryVideoScene.getUnitsCount();
        }
        if (unitsCount == 0) {
            return basicSceneCount;
        }
        int i2 = totalVideoCount / unitsCount;
        if (totalVideoCount % unitsCount <= 0) {
            i = 0;
        }
        return i2 + i + basicSceneCount;
    }

    public int getBasicSceneCount() {
        return UIMgr.isDriverModeEnabled() ? 2 : 1;
    }

    public boolean canBePinVideo() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && !videoObj.isLeadShipMode()) {
            return true;
        }
        return false;
    }

    public boolean pinVideo(long j) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null || j == 0 || !canBePinVideo()) {
            return false;
        }
        videoObj.setManualMode(true, j);
        return true;
    }

    public boolean unPinVideo() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && videoObj.isManualMode()) {
            long selectedUser = videoObj.getSelectedUser();
            if (selectedUser != 0) {
                videoObj.setManualMode(false, selectedUser);
                return true;
            }
        }
        return false;
    }

    public void attendeeVideoControlChange(long j) {
        checkCurrentScene();
    }

    public void attendeeVideoLayoutChange(long j) {
        checkCurrentScene();
    }

    public void attendeeVideoLayoutFlagChange(long j) {
        checkCurrentScene();
    }

    private boolean canSwitchScene(AbsVideoScene absVideoScene) {
        if (!ConfMgr.getInstance().isViewOnlyClientOnMMR() || (isActiveOrShare(absVideoScene) && !isMeetSwitchToGalleryView())) {
            return true;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            int attendeeVideoControlMode = confStatusObj.getAttendeeVideoControlMode();
            if (attendeeVideoControlMode == 0) {
                return isActiveOrShare(absVideoScene);
            }
            if (attendeeVideoControlMode == 2) {
                int attendeeVideoLayoutMode = confStatusObj.getAttendeeVideoLayoutMode();
                if (attendeeVideoLayoutMode == 0) {
                    return isActiveOrShare(absVideoScene);
                }
                if (attendeeVideoLayoutMode == 1) {
                    return isGalleryOrShare(absVideoScene);
                }
            } else if (attendeeVideoControlMode == 1) {
                return isGalleryOrShare(absVideoScene);
            }
        }
        return true;
    }

    private boolean checkCurrentScene() {
        if (!ConfLocalHelper.isInVideoCompanionMode() && ConfMgr.getInstance().isViewOnlyClientOnMMR()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                int attendeeVideoControlMode = confStatusObj.getAttendeeVideoControlMode();
                if (attendeeVideoControlMode == 0 && !isActive()) {
                    switchToDefaultScene();
                    return true;
                } else if (attendeeVideoControlMode == 2) {
                    int attendeeVideoLayoutMode = confStatusObj.getAttendeeVideoLayoutMode();
                    if (isShare()) {
                        AbsVideoScene absVideoScene = this.mActiveScene;
                        if (absVideoScene != null) {
                            ((ShareVideoScene) absVideoScene).changeShareViewSize(confStatusObj.isHostViewingShareInWebinar());
                        }
                    }
                    if (attendeeVideoLayoutMode == 0 && !isActive()) {
                        switchToDefaultScene();
                        return true;
                    } else if (attendeeVideoLayoutMode == 1 && isMeetSwitchToGalleryView() && !isGallery()) {
                        this.mGalleryScene1.setPageIndex(0);
                        switchToScene((AbsVideoScene) this.mGalleryScene1);
                        return true;
                    }
                } else if (attendeeVideoControlMode == 1 && isMeetSwitchToGalleryView() && !isGalleryOrShare()) {
                    this.mGalleryScene1.setPageIndex(0);
                    switchToScene((AbsVideoScene) this.mGalleryScene1);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isActive() {
        return this.mActiveScene == this.mNormalScene;
    }

    private boolean isGallery() {
        AbsVideoScene absVideoScene = this.mActiveScene;
        return absVideoScene == this.mGalleryScene1 || absVideoScene == this.mGalleryScene2;
    }

    private boolean isShare() {
        return this.mActiveScene == this.mShareScene && getShareActiveUserId() > 0;
    }

    private boolean isGalleryOrShare() {
        return isGallery() || isShare();
    }

    private boolean isActiveOrShare() {
        return isActive() || isShare();
    }

    private boolean isGalleryOrShare(AbsVideoScene absVideoScene) {
        return absVideoScene == this.mGalleryScene1 || absVideoScene == this.mGalleryScene2 || (absVideoScene == this.mShareScene && getShareActiveUserId() > 0);
    }

    private boolean isActiveOrShare(AbsVideoScene absVideoScene) {
        return absVideoScene == this.mNormalScene || (absVideoScene == this.mShareScene && getShareActiveUserId() > 0);
    }

    private void onSceneChanged() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && myself.isHost()) {
            AbsVideoScene absVideoScene = this.mActiveScene;
            if (absVideoScene == this.mNormalScene || absVideoScene == this.mDriverModeScene) {
                ConfLocalHelper.setAttendeeVideoLayout(0);
            } else if (absVideoScene == this.mGalleryScene1 || absVideoScene == this.mGalleryScene2) {
                ConfLocalHelper.setAttendeeVideoLayout(1);
            }
        }
    }

    private void initLastHostVideoLayout() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            this.isInitLastHostVideoLayout = true;
            if (myself.isHost()) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    this.mLastHostVideoLayout = confStatusObj.getAttendeeVideoLayoutMode();
                }
            }
        }
    }

    private void restoreHostVideoLayout() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && myself.isHost()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                int attendeeVideoLayoutMode = confStatusObj.getAttendeeVideoLayoutMode();
                int i = this.mLastHostVideoLayout;
                if (attendeeVideoLayoutMode != i) {
                    if (i == 0 && !isActiveOrShare()) {
                        switchToDefaultScene();
                    } else if (this.mLastHostVideoLayout == 1 && !isGalleryOrShare()) {
                        this.mGalleryScene1.setPageIndex(0);
                        switchToScene((AbsVideoScene) this.mGalleryScene1);
                    }
                }
                this.mLastHostVideoLayout = -1;
            }
        }
    }

    private boolean canScroll(float f) {
        AbsVideoScene absVideoScene = this.mActiveScene;
        boolean z = false;
        if (!(absVideoScene instanceof GalleryVideoScene)) {
            return false;
        }
        GalleryVideoScene galleryVideoScene = (GalleryVideoScene) absVideoScene;
        if ((galleryVideoScene.hasPrevPage() && f < 0.0f) || (galleryVideoScene.hasNextPage() && f > 0.0f)) {
            z = true;
        }
        return z;
    }

    private boolean isGalleryVideoModeDisabled() {
        if (getShareActiveUserId() <= 0 || !isShowLargeShareVideoScene()) {
            return PreferenceUtil.readBooleanValue(PreferenceUtil.NO_GALLERY_VIDEOS_VIEW, false);
        }
        return true;
    }

    private boolean isShowLargeShareVideoScene() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.LARGE_SHARE_VIDEO_SCENE_MODE, false);
    }
}
