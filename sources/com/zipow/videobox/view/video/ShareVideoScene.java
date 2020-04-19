package com.zipow.videobox.view.video;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.nydus.VideoSize;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmShareStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.GLImage;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.ShareUnit;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ShareVideoScene extends AbsVideoScene implements OnClickListener {
    private static final long DOUBLE_SCROLL_INTERVAL_TIME = 150;
    private static final int HIDE_SHARING_TITLE = 0;
    private static final int MAX_SCALE_LEVEL_COUNT = 3;
    private static final int MAX_VELOCITY_IN_DIP = 1500;
    private static final int RC_HANDLER_MESSAGE_LONG_PRESS = 1;
    private static final int RC_HANDLER_MESSAGE_SINGLE_TAP = 2;
    private static final float SCROLL_THRESHOLD = 10.0f;
    private static final int SHARING_TITLE_TIME_OUT = 5000;
    private static final long SINGLE_TAP_INTERVAL_TIME = 200;
    private final String TAG = ShareVideoScene.class.getSimpleName();
    @NonNull
    private Handler handler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            ConfActivity confActivity = ShareVideoScene.this.getConfActivity();
            if (confActivity != null) {
                View findViewById = confActivity.findViewById(C4558R.C4560id.panelSharingTitle);
                if (findViewById != null) {
                    findViewById.setVisibility(8);
                }
            }
        }
    };
    @Nullable
    private VideoSize mActiveVideoSize;
    @Nullable
    private VideoSize mCachedShareSize;
    @Nullable
    private ShareUnit mCachedUnitShare;
    @Nullable
    private VideoUnit mCachedUnitVideo;
    @Nullable
    private VideoSize mCachedVideoSize;
    private float mContentX = 0.0f;
    private float mContentY = 0.0f;
    private MotionEvent mCurrentDownEvent;
    private float mDistance;
    @NonNull
    private Handler mFlingHandler = new Handler();
    private boolean mFlinged;
    @Nullable
    private GLImage mGLImageWaterMark;
    private boolean mHasCachedData = false;
    private boolean mInRemoteControlMode = false;
    private boolean mIsBigShareView = true;
    private boolean mIsDoubleScrollMode = false;
    private boolean mIsDoubleTap;
    private boolean mIsFitScreen = true;
    private boolean mIsMultiTouchZooming = false;
    private boolean mIsNotWaiting = false;
    private long mLastDoubleScrollTime = 0;
    private float mLastDoubleScrollX;
    private float mLastDoubleScrollY;
    private long mLastMultiTouchEndTime = 0;
    private float mLastX1;
    private float mLastX2;
    private float mLastY1;
    private float mLastY2;
    private int mMoveCount = 0;
    private MotionEvent mPreviousUpEvent;
    private float mRCMouseX;
    private float mRCMouseY;
    @NonNull
    private final Handler mRemoteControlGestureHandler = new Handler() {
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            switch (message.what) {
                case 1:
                    ShareVideoScene.this.remoteControlLongPress(data.getFloat("x"), data.getFloat("y"));
                    return;
                case 2:
                    float f = data.getFloat("x");
                    float f2 = data.getFloat("y");
                    float f3 = data.getFloat("raw_x");
                    float f4 = data.getFloat("raw_y");
                    ShareVideoScene.this.remoteControlSingleTap(f, f2);
                    ShareVideoScene.this.moveMouse(f3, f4);
                    return;
                default:
                    return;
            }
        }
    };
    private float mScaleHeight = 0.0f;
    private float mScaleWidth = 0.0f;
    private boolean mScrolled;
    @NonNull
    private final Scroller mScroller = new Scroller(VideoBoxApplication.getInstance(), new DecelerateInterpolator(1.0f));
    @Nullable
    private ShareSessionMgr mShareSession;
    @Nullable
    private VideoSize mShareSize;
    /* access modifiers changed from: private */
    public boolean mStopFling = false;
    private ImageButton[] mSwitchSceneButtons;
    private boolean mSwitchingView = false;
    @Nullable
    private VideoUnit mUnitActiveVideo;
    @Nullable
    private ShareUnit mUnitShare;
    private int mWaterMarkHeight = 0;
    private int mWaterMarkWidth = 0;
    private double mZoomVal = 0.0d;
    private boolean mbIgnoreNextScroll = false;

    public boolean isLargeShareVideoMode() {
        return false;
    }

    public boolean needShowSwitchCameraButton() {
        return false;
    }

    public ShareVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
        setCacheEnabled(true);
        this.mShareSession = ConfMgr.getInstance().getShareObj();
    }

    public boolean hasContent() {
        return this.mHasCachedData;
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        if (isNoVideoTileOnShareScreenEnabled() || ConfLocalHelper.isInVideoCompanionMode()) {
            createUnitShare();
        } else if (this.mIsBigShareView) {
            createUnitShare();
            createUnitActiveVideo();
        } else {
            createUnitActiveVideo();
            createUnitShare();
        }
        createGLImageWaterMark();
        if (isVisible()) {
            positionSwitchScenePanel();
        }
        updateMainVideoFlag();
    }

    private void createUnitActiveVideo() {
        boolean z;
        if (this.mUnitActiveVideo == null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                RendererUnitInfo createVideoUnitInfo = createVideoUnitInfo(getVideoSceneMgr().getActiveUserId() > 0);
                VideoUnit videoUnit = this.mCachedUnitVideo;
                if (videoUnit != null) {
                    this.mUnitActiveVideo = videoUnit;
                    this.mActiveVideoSize = this.mCachedVideoSize;
                    this.mCachedUnitVideo = null;
                    this.mUnitActiveVideo.updateUnitInfo(createVideoUnitInfo);
                    z = true;
                } else {
                    this.mUnitActiveVideo = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createVideoUnitInfo);
                    if (this.mUnitActiveVideo != null) {
                        z = false;
                    } else {
                        return;
                    }
                }
                this.mUnitActiveVideo.setUnitName("ActiveVideoInShareScene");
                this.mUnitActiveVideo.setVideoScene(this);
                this.mUnitActiveVideo.setBorderVisible(false);
                this.mUnitActiveVideo.setBackgroundColor(0);
                this.mUnitActiveVideo.setUserNameVisible(false);
                this.mUnitActiveVideo.setCanShowAudioOff(true);
                this.mUnitActiveVideo.setIsFloating(true);
                addUnit(this.mUnitActiveVideo);
                if (!z) {
                    this.mUnitActiveVideo.onCreate();
                }
            }
        }
    }

    private void createUnitShare() {
        if (this.mUnitShare == null) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                RendererUnitInfo createShareUnitInfo = createShareUnitInfo();
                if (createShareUnitInfo != null) {
                    boolean z = false;
                    ShareUnit shareUnit = this.mCachedUnitShare;
                    if (shareUnit != null) {
                        z = true;
                        this.mUnitShare = shareUnit;
                        this.mShareSize = this.mCachedShareSize;
                        this.mCachedUnitShare = null;
                        this.mUnitShare.updateUnitInfo(createShareUnitInfo);
                    } else {
                        this.mUnitShare = shareObj.createShareUnit(createShareUnitInfo);
                        if (this.mUnitShare == null) {
                            return;
                        }
                    }
                    this.mUnitShare.setVideoScene(this);
                    addUnit(this.mUnitShare);
                    if (!z) {
                        this.mUnitShare.onCreate();
                    }
                }
            }
        }
    }

    private void destroyUnitActiveVideo() {
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            videoUnit.removeUser();
            VideoUnit videoUnit2 = this.mUnitActiveVideo;
            if (videoUnit2 != null) {
                videoUnit2.onDestroy();
                removeUnit(this.mUnitActiveVideo);
                this.mUnitActiveVideo = null;
            }
            onUpdateUnits();
        }
    }

    public void updateAccessibilitySceneDescription() {
        if (getConfActivity() != null) {
            if (ConfShareLocalHelper.isSharingOut()) {
                getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_share));
            } else if (getConfActivity().isToolbarShowing()) {
                getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_share_toolbar_showed));
            } else {
                getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_share_toolbar_hided));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateUnits() {
        if (this.mIsFitScreen) {
            zoomToFitUnit();
        } else {
            PointF centerPixelPosOnContent = getCenterPixelPosOnContent();
            updateUnitsWithoutResetDestArea();
            if (centerPixelPosOnContent != null) {
                resetDestAreaCenter(centerPixelPosOnContent.x, centerPixelPosOnContent.y);
            } else {
                return;
            }
        }
        updateSharingTitle(getVideoSceneMgr().getShareActiveUserId());
        if (isVisible()) {
            positionSwitchScenePanel();
            updateAccessibilitySceneDescription();
        }
        updateMainVideoFlag();
    }

    private void updateUnitsWithoutResetDestArea() {
        updateUnitShare();
        updateUnitActiveVideo();
        updateGLImageWaterMark();
    }

    private void updateUnitActiveVideo() {
        if (this.mUnitActiveVideo != null) {
            this.mUnitActiveVideo.updateUnitInfo(createVideoUnitInfo(getVideoSceneMgr().getActiveUserId() > 0));
            boolean isShowUserName = isShowUserName(!this.mIsBigShareView);
            this.mUnitActiveVideo.setUserNameVisible(isShowUserName, isShowUserName);
            this.mUnitActiveVideo.onUserAudioStatus();
        }
    }

    private void updateUnitShare() {
        if (this.mUnitShare != null) {
            RendererUnitInfo createShareUnitInfo = createShareUnitInfo();
            if (createShareUnitInfo != null) {
                this.mUnitShare.updateUnitInfo(createShareUnitInfo);
            }
            AnnoDataMgr.getInstance().updateVideoGallerySize(this.mUnitShare.getRendererInfo(), getWidth() - getShareRenderWidth(), getHeight() - getShareRenderHeight());
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        this.handler.removeCallbacksAndMessages(null);
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit != null) {
            shareUnit.removeUser();
        }
        this.mUnitActiveVideo = null;
        this.mUnitShare = null;
        this.mShareSize = null;
        this.mGLImageWaterMark = null;
        if (this.mCachedUnitShare == null && this.mCachedUnitVideo == null) {
            this.mHasCachedData = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        runOnRendererInited(new Runnable() {
            public void run() {
                ShareVideoScene.this.updateContentSubscription();
            }
        });
        if (isVisible()) {
            updateSwitchScenePanel();
        }
    }

    public void updateContentSubscription() {
        if (!isPreloadStatus()) {
            checkShowVideo();
            checkShowShare();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit != null) {
            shareUnit.stopViewShareContent();
        }
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            videoUnit.removeUser();
        }
        showWaiting(false);
        updateSharingTitle(0);
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
        if (!this.mHasCachedData) {
            showWaiting(true);
        }
        updateContentSubscription();
        updateSharingTitle(getVideoSceneMgr().getShareActiveUserId());
        updateSwitchScenePanel();
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        ShareUnit shareUnit = this.mCachedUnitShare;
        if (shareUnit != null) {
            shareUnit.onGLViewSizeChanged(i, i2);
        }
        VideoUnit videoUnit = this.mCachedUnitVideo;
        if (videoUnit != null) {
            videoUnit.onGLViewSizeChanged(i, i2);
        }
        if (isVisible()) {
            if (hasGrantedUnits() && isCreated()) {
                stopAndDestroyAllGrantedUnits();
            }
            super.onGLRendererChanged(videoRenderer, i, i2);
            if (this.mIsMultiTouchZooming) {
                onMultiTouchZoomEnd();
            }
        }
    }

    public void onActiveVideoChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                ShareVideoScene.this.checkShowVideo();
            }
        });
    }

    public void onGroupUserVideoStatus(@Nullable List<Long> list) {
        super.onGroupUserVideoStatus(list);
        if (!isPreloadStatus()) {
            runOnRendererInited(new Runnable() {
                public void run() {
                    ShareVideoScene.this.checkShowVideo();
                }
            });
        }
    }

    public void onUserVideoDataSizeChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                ShareVideoScene.this.checkShowVideo();
            }
        });
    }

    public void onUserVideoQualityChanged(long j) {
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null && videoUnit.getCanShowNetworkStatus()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.isSameVideo(this.mUnitActiveVideo.getUser(), j)) {
                this.mUnitActiveVideo.onNetworkStatusChanged();
            }
        }
    }

    public void onUserAudioStatus(long j) {
        updateUserAudioStatus(j);
    }

    public void onAudioTypeChanged(long j) {
        updateUserAudioStatus(j);
    }

    private void updateUserAudioStatus(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            VideoUnit videoUnit = this.mUnitActiveVideo;
            if (videoUnit != null) {
                long user = videoUnit.getUser();
                CmmUser userById = ConfMgr.getInstance().getUserById(user);
                if (userById != null) {
                    user = userById.getNodeId();
                }
                if (user != 0 && confStatusObj.isSameUser(j, user)) {
                    this.mUnitActiveVideo.onUserAudioStatus();
                }
            }
        }
    }

    public void onUserPicReady(long j) {
        updateUserPic(j);
    }

    private void updateUserPic(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            VideoUnit videoUnit = this.mUnitActiveVideo;
            if (videoUnit != null) {
                long user = videoUnit.getUser();
                CmmUser userById = ConfMgr.getInstance().getUserById(user);
                if (userById != null) {
                    user = userById.getNodeId();
                }
                if (user != 0 && confStatusObj.isSameUser(j, user)) {
                    this.mUnitActiveVideo.updateAvatar();
                }
            }
        }
    }

    public void onShareActiveUser(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                ShareVideoScene.this.checkShowShare();
            }
        });
    }

    public void onUserCountChangesForShowHideAction() {
        if (!isPreloadStatus()) {
            updateSwitchScenePanel();
        }
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        if (!isPreloadStatus()) {
            switch (i) {
                case 0:
                case 1:
                    updateSwitchScenePanel();
                    break;
                case 2:
                    updateContentSubscription();
                    if (!this.mIsNotWaiting) {
                        showWaiting(true);
                        break;
                    }
                    break;
            }
        }
    }

    private void showWaiting(boolean z) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelWaitingShare);
            TextView textView = (TextView) findViewById.findViewById(C4558R.C4560id.txtMsgWaitingShare);
            if (z) {
                CmmUser userById = ConfMgr.getInstance().getUserById(getVideoSceneMgr().getShareActiveUserId());
                if (userById != null) {
                    String safeString = StringUtil.safeString(userById.getScreenName());
                    if (safeString.endsWith("s")) {
                        textView.setText(confActivity.getString(C4558R.string.zm_msg_waiting_share_s, new Object[]{safeString}));
                    } else {
                        textView.setText(confActivity.getString(C4558R.string.zm_msg_waiting_share, new Object[]{safeString}));
                    }
                    findViewById.setVisibility(0);
                    this.mIsNotWaiting = false;
                }
            } else {
                findViewById.setVisibility(4);
                this.mIsNotWaiting = true;
                updateGLImageWaterMark();
            }
        }
    }

    public void onShareUserReceivingStatus(long j) {
        this.mSwitchingView = false;
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            CmmShareStatus shareStatusObj = userById.getShareStatusObj();
            if (shareStatusObj != null) {
                if (shareStatusObj.getIsReceiving()) {
                    this.mHasCachedData = true;
                    showWaiting(false);
                    stopAndDestroyAllGrantedUnits();
                    ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                    if (shareObj != null) {
                        VideoSize shareDataResolution = shareObj.getShareDataResolution(j);
                        if (shareDataResolution.width > 0 && shareDataResolution.height > 0) {
                            onShareDataSizeChanged(j);
                        }
                    }
                } else if (!this.mHasCachedData) {
                    showWaiting(true);
                }
                updateSharingTitle(getVideoSceneMgr().getShareActiveUserId());
            }
        }
    }

    public void onShareUserSendingStatus(long j) {
        AbsVideoSceneMgr videoSceneMgr = getVideoSceneMgr();
        if (j == videoSceneMgr.getShareActiveUserId()) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                boolean isVideoSharingInProgress = shareObj.isVideoSharingInProgress();
                if (isVideoSharingInProgress && !videoSceneMgr.isVideoShareInProgress()) {
                    zoomToFitUnit();
                }
                videoSceneMgr.setIsVideoShareInProgress(isVideoSharingInProgress);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onGrantedUnitsDestroyed() {
        checkShowVideo();
    }

    public void onShareDataSizeChanged(long j) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            VideoSize videoSize = this.mShareSize;
            boolean z = videoSize == null || videoSize.width == 0 || this.mShareSize.height == 0;
            VideoSize shareDataResolution = shareObj.getShareDataResolution(j);
            if (shareDataResolution.width != 0 && shareDataResolution.height != 0) {
                this.mShareSize = shareDataResolution;
                AbsVideoSceneMgr videoSceneMgr = getVideoSceneMgr();
                boolean isVideoSharingInProgress = shareObj.isVideoSharingInProgress();
                if (isVideoSharingInProgress && !videoSceneMgr.isVideoShareInProgress()) {
                    this.mIsFitScreen = true;
                }
                videoSceneMgr.setIsVideoShareInProgress(isVideoSharingInProgress);
                VideoSize videoSize2 = this.mShareSize;
                if (videoSize2 != null && videoSize2.width != 0 && this.mShareSize.height != 0) {
                    if (z || this.mIsFitScreen) {
                        zoomToFitUnit();
                    } else {
                        int currentScaleLevel = getCurrentScaleLevel();
                        int scaleLevelsCount = getScaleLevelsCount();
                        if (currentScaleLevel >= scaleLevelsCount) {
                            this.mZoomVal = scaleLevelToZoomValue(scaleLevelsCount - 1);
                        }
                        this.mIsFitScreen = checkFitScreen();
                        updateUnitsWithoutResetDestArea();
                        trimContentPos();
                        if (this.mIsFitScreen) {
                            ShareUnit shareUnit = this.mUnitShare;
                            if (shareUnit != null) {
                                this.mScaleWidth = (float) shareUnit.getWidth();
                                this.mScaleHeight = (float) this.mUnitShare.getHeight();
                            }
                        } else {
                            this.mScaleWidth = (float) (this.mZoomVal * ((double) this.mShareSize.width));
                            this.mScaleHeight = (float) (this.mZoomVal * ((double) this.mShareSize.height));
                        }
                        notifyDestAreaChanged();
                    }
                }
            }
        }
    }

    public void onConfVideoSendingStatusChanged() {
        if (!isPreloadStatus()) {
            checkShowVideo();
        }
    }

    public void beforeSwitchCamera() {
        if (this.mUnitActiveVideo != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(this.mUnitActiveVideo.getUser())) {
                this.mUnitActiveVideo.stopVideo(false);
            }
        }
    }

    public void afterSwitchCamera() {
        if (this.mUnitActiveVideo != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(this.mUnitActiveVideo.getUser())) {
                this.mUnitActiveVideo.startVideo();
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkShowVideo() {
        if (isCreated() && !hasGrantedUnits()) {
            ConfMgr instance = ConfMgr.getInstance();
            VideoSessionMgr videoObj = instance.getVideoObj();
            if (videoObj != null) {
                CmmUserList userList = instance.getUserList();
                if (userList != null) {
                    long activeUserId = getVideoSceneMgr().getActiveUserId();
                    if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) == 2) {
                        CmmUser peerUser = userList.getPeerUser(false, true);
                        if (peerUser != null) {
                            activeUserId = peerUser.getNodeId();
                        }
                    }
                    if (this.mUnitActiveVideo != null) {
                        CmmConfContext confContext = instance.getConfContext();
                        if (confContext != null) {
                            boolean z = !confContext.isAudioOnlyMeeting() && !confContext.isShareOnlyMeeting();
                            boolean noOneIsSendingVideo = instance.noOneIsSendingVideo();
                            if (activeUserId > 0 && (z || !noOneIsSendingVideo)) {
                                VideoSize userVideoSize = getUserVideoSize(activeUserId);
                                if (userVideoSize.width == 0 || userVideoSize.height == 0) {
                                    userVideoSize = getMyVideoSize();
                                }
                                VideoSize videoSize = this.mActiveVideoSize;
                                if (videoSize == null || !videoSize.similarTo(userVideoSize)) {
                                    this.mActiveVideoSize = userVideoSize;
                                    this.mUnitActiveVideo.updateUnitInfo(createVideoUnitInfo(true));
                                } else {
                                    this.mActiveVideoSize = userVideoSize;
                                }
                                if (!this.mIsBigShareView) {
                                    this.mUnitActiveVideo.setIsFloating(false);
                                    this.mUnitActiveVideo.setType(1);
                                    this.mUnitActiveVideo.setBackgroundColor(0);
                                    this.mUnitActiveVideo.setBorderVisible(false);
                                } else {
                                    this.mUnitActiveVideo.setIsFloating(true);
                                    this.mUnitActiveVideo.setType(0);
                                    this.mUnitActiveVideo.setBackgroundColor(-16777216);
                                    this.mUnitActiveVideo.setBorderVisible(true);
                                }
                                this.mUnitActiveVideo.setUser(activeUserId);
                            } else if (videoObj.isVideoStarted()) {
                                CmmUser myself = userList.getMyself();
                                if (myself != null) {
                                    if (this.mUnitActiveVideo.getUser() != myself.getNodeId()) {
                                        this.mUnitActiveVideo.updateUnitInfo(createPreviewVideoUnitInfo());
                                    }
                                    if (!this.mIsBigShareView) {
                                        this.mUnitActiveVideo.setIsFloating(false);
                                        this.mUnitActiveVideo.setType(1);
                                        this.mUnitActiveVideo.setBackgroundColor(0);
                                        this.mUnitActiveVideo.setBorderVisible(false);
                                    } else {
                                        this.mUnitActiveVideo.setIsFloating(true);
                                        this.mUnitActiveVideo.setType(0);
                                        this.mUnitActiveVideo.setBackgroundColor(-16777216);
                                        this.mUnitActiveVideo.setBorderVisible(true);
                                    }
                                    this.mUnitActiveVideo.setUser(myself.getNodeId());
                                }
                            } else {
                                this.mUnitActiveVideo.stopVideo(true);
                                this.mUnitActiveVideo.removeUser();
                                this.mUnitActiveVideo.setBorderVisible(false);
                                this.mUnitActiveVideo.setBackgroundColor(0);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkShowShare() {
        if (isCreated() && ConfMgr.getInstance().getShareObj() != null && this.mUnitShare != null) {
            long shareActiveUserId = getVideoSceneMgr().getShareActiveUserId();
            if (shareActiveUserId != 0) {
                RendererUnitInfo createShareUnitInfo = createShareUnitInfo();
                if (createShareUnitInfo != null) {
                    this.mUnitShare.updateUnitInfo(createShareUnitInfo);
                }
                long user = this.mUnitShare.getUser();
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (!this.mSwitchingView && getLeft() == 0 && confStatusObj != null && !confStatusObj.isSameUser(user, shareActiveUserId) && !this.mHasCachedData) {
                    showWaiting(true);
                }
                this.mUnitShare.setUser(shareActiveUserId);
                this.mUnitShare.setBorderVisible(!this.mIsBigShareView);
                updateSharingTitle(shareActiveUserId);
            } else {
                this.mUnitShare.removeUser();
                showWaiting(false);
            }
        }
    }

    private void updateSharingTitle(long j) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSharingTitle);
            ConfShareLocalHelper.updateShareTitle(confActivity, j, findViewById);
            if (isLargeShareVideoMode()) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById.getLayoutParams();
                marginLayoutParams.bottomMargin = getHeight() - getShareRenderHeight();
                findViewById.setLayoutParams(marginLayoutParams);
                findViewById.getParent().requestLayout();
            }
            if (!isVisible() || !isStarted() || !hasContent() || confActivity.isToolbarShowing() || !this.mIsBigShareView) {
                findViewById.setVisibility(8);
            } else {
                findViewById.setVisibility(0);
            }
            this.handler.removeCallbacksAndMessages(null);
            this.handler.sendEmptyMessageDelayed(0, 5000);
        }
    }

    @NonNull
    private RendererUnitInfo createVideoUnitInfo(boolean z) {
        if (z) {
            VideoSize videoSize = this.mActiveVideoSize;
            if (videoSize != null) {
                return createActiveVideoUnitInfo(videoSize);
            }
        }
        return createPreviewVideoUnitInfo();
    }

    private void updateMainVideoFlag() {
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            videoUnit.setMainVideo(!this.mIsBigShareView);
        }
    }

    @NonNull
    private VideoSize getMyVideoSize() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            return new VideoSize(16, 9);
        }
        return videoObj.getMyVideoSize();
    }

    private VideoSize calcSmallUnitSize(@Nullable VideoSize videoSize) {
        int i;
        if (videoSize == null || (videoSize.width == 0 && videoSize.height == 0)) {
            return new VideoSize(16, 9);
        }
        ConfActivity confActivity = getConfActivity();
        int max = Math.max(UIUtil.getDisplayWidth(confActivity), UIUtil.getDisplayHeight(confActivity)) / 8;
        int min = Math.min(UIUtil.getDisplayWidth(confActivity), UIUtil.getDisplayHeight(confActivity)) / 8;
        if (videoSize.width > videoSize.height) {
            i = Math.max(max, UIUtil.dip2px(getConfActivity(), 80.0f));
        } else {
            i = Math.max(min, UIUtil.dip2px(getConfActivity(), 45.0f));
        }
        return new VideoSize(i, (videoSize.height * i) / videoSize.width);
    }

    @NonNull
    private RendererUnitInfo createPreviewVideoUnitInfo() {
        if (this.mIsBigShareView) {
            return createSmallUnitInfo(calcSmallUnitSize(getMyVideoSize()));
        }
        return createBigUnitInfo();
    }

    @NonNull
    private RendererUnitInfo createActiveVideoUnitInfo(VideoSize videoSize) {
        if (this.mIsBigShareView) {
            return createSmallUnitInfo(calcSmallUnitSize(videoSize));
        }
        return createBigUnitInfo();
    }

    private RendererUnitInfo createSmallUnitInfo(VideoSize videoSize) {
        int i = videoSize.width;
        int i2 = videoSize.height;
        ConfActivity confActivity = getConfActivity();
        int dip2px = UIUtil.dip2px(confActivity, 5.0f);
        int width = (getWidth() - dip2px) - i;
        int height = (getHeight() - i2) - dip2px;
        int toolbarHeight = confActivity.getToolbarHeight();
        if (toolbarHeight > 0) {
            height -= toolbarHeight;
        }
        if (!this.mIsBigShareView || !ZMConfComponentMgr.getInstance().isAnnotationDrawingViewVisible()) {
            return new RendererUnitInfo(getLeft() + width, getTop() + height, i, i2);
        }
        return new RendererUnitInfo(-10, -10, 1, 1);
    }

    @Nullable
    private RendererUnitInfo createShareUnitInfo() {
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null || videoSize.width == 0 || videoSize.height == 0) {
            videoSize = new VideoSize(16, 9);
        }
        if (this.mIsBigShareView) {
            return createBigShareUnitInfo(videoSize);
        }
        return createSmallShareUnitInfo(videoSize);
    }

    private RendererUnitInfo createSmallShareUnitInfo(VideoSize videoSize) {
        return createSmallUnitInfo(calcSmallUnitSize(videoSize));
    }

    @Nullable
    private RendererUnitInfo createBigShareUnitInfo(VideoSize videoSize) {
        int i;
        int i2;
        int i3;
        int i4 = videoSize.width;
        int i5 = videoSize.height;
        if (i4 == 0 || i5 == 0) {
            return null;
        }
        int shareRenderWidth = getShareRenderWidth();
        int shareRenderHeight = getShareRenderHeight();
        int shareRenderHeight2 = getShareRenderHeight();
        int shareRenderWidth2 = getShareRenderWidth();
        int i6 = 0;
        if (!this.mIsFitScreen || Math.abs(this.mZoomVal - getMinLevelZoomValue()) >= 0.01d) {
            double d = (double) i4;
            double d2 = this.mZoomVal;
            float f = (float) (d * d2);
            float f2 = (float) (((double) i5) * d2);
            if (f > ((float) shareRenderWidth)) {
                i3 = shareRenderWidth;
                i2 = 0;
            } else {
                i3 = (int) f;
                i2 = (shareRenderWidth - i3) / 2;
                if (i2 + i3 > shareRenderWidth2) {
                    i2 = shareRenderWidth2 - i3;
                    if (i2 < 0) {
                        i2 = 0;
                    }
                }
            }
            if (f2 > ((float) shareRenderHeight)) {
                i = shareRenderHeight;
            } else {
                i = (int) f2;
                int i7 = (shareRenderHeight - i) / 2;
                if (shareRenderHeight2 <= 0 || i7 + i <= shareRenderHeight2) {
                    i6 = i7;
                } else {
                    int i8 = shareRenderHeight2 - i;
                    if (i8 >= 0) {
                        i6 = i8;
                    }
                }
            }
        } else {
            int i9 = shareRenderWidth * i5;
            int i10 = shareRenderHeight * i4;
            if (i9 > i10) {
                int i11 = i10 / i5;
                int i12 = (shareRenderWidth - i11) / 2;
                if (i12 + i11 > shareRenderWidth2) {
                    int i13 = shareRenderWidth2 - i11;
                    if (i13 < 0) {
                        i3 = i11;
                        i = shareRenderHeight;
                        i2 = 0;
                    } else {
                        i2 = i13;
                        i3 = i11;
                        i = shareRenderHeight;
                    }
                } else {
                    i2 = i12;
                    i3 = i11;
                    i = shareRenderHeight;
                }
            } else {
                i = i9 / i4;
                int i14 = (shareRenderHeight - i) / 2;
                if (shareRenderHeight2 <= 0 || i14 + i <= shareRenderHeight2) {
                    i6 = i14;
                    i3 = shareRenderWidth;
                    i2 = 0;
                } else {
                    int i15 = shareRenderHeight2 - i;
                    if (i15 < 0) {
                        i3 = shareRenderWidth;
                        i2 = 0;
                    } else {
                        i6 = i15;
                        i3 = shareRenderWidth;
                        i2 = 0;
                    }
                }
            }
        }
        return new RendererUnitInfo(getLeft() + i2, getTop() + i6, i3, i);
    }

    public void onDown(MotionEvent motionEvent) {
        this.mStopFling = true;
    }

    public void onDoubleTap(@NonNull MotionEvent motionEvent) {
        if (!this.mInRemoteControlMode) {
            this.mStopFling = true;
            if (this.mIsBigShareView && this.mIsNotWaiting) {
                VideoSize videoSize = this.mShareSize;
                if (videoSize != null && videoSize.width != 0 && this.mShareSize.height != 0) {
                    int scaleLevelsCount = getScaleLevelsCount();
                    int currentScaleLevel = getCurrentScaleLevel();
                    int i = (currentScaleLevel + 1) % scaleLevelsCount;
                    if (i != currentScaleLevel) {
                        if (i == 0) {
                            zoomToFitUnit();
                        } else {
                            switchToLevel(i, motionEvent.getX(), motionEvent.getY());
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:96:0x01bf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(@androidx.annotation.NonNull android.view.MotionEvent r17) {
        /*
            r16 = this;
            r9 = r16
            r0 = r17
            boolean r1 = r9.mIsBigShareView
            r2 = 0
            if (r1 != 0) goto L_0x000a
            return r2
        L_0x000a:
            boolean r1 = r9.mIsNotWaiting
            if (r1 != 0) goto L_0x000f
            return r2
        L_0x000f:
            boolean r1 = super.onTouchEvent(r17)
            r10 = 1
            if (r1 == 0) goto L_0x0017
            return r10
        L_0x0017:
            int r1 = r17.getPointerCount()
            r3 = 0
            r4 = 2
            if (r1 != r4) goto L_0x0147
            android.os.Handler r1 = r9.mRemoteControlGestureHandler
            r1.removeCallbacksAndMessages(r3)
            float r11 = r0.getX(r2)
            float r12 = r0.getY(r2)
            float r13 = r0.getX(r10)
            float r14 = r0.getY(r10)
            int r1 = r17.getActionMasked()
            r3 = 5
            r5 = 0
            if (r1 != r3) goto L_0x0048
            r9.mLastX1 = r5
            r9.mLastY1 = r5
            r9.mLastX2 = r5
            r9.mLastY2 = r5
            r9.mScrolled = r2
            r9.mFlinged = r2
        L_0x0048:
            boolean r1 = r9.mInRemoteControlMode
            if (r1 == 0) goto L_0x00b5
            int r1 = r17.getActionMasked()
            if (r1 != r3) goto L_0x006d
            r9.mLastDoubleScrollX = r11
            r9.mLastDoubleScrollY = r12
            float r1 = r11 - r13
            float r1 = r1 * r1
            float r3 = r12 - r14
            float r3 = r3 * r3
            float r1 = r1 + r3
            double r6 = (double) r1
            double r6 = java.lang.Math.sqrt(r6)
            float r1 = (float) r6
            r9.mDistance = r1
            long r6 = java.lang.System.currentTimeMillis()
            r9.mLastDoubleScrollTime = r6
        L_0x006d:
            boolean r1 = r9.mIsMultiTouchZooming
            if (r1 != 0) goto L_0x00b7
            boolean r1 = r9.mIsDoubleScrollMode
            if (r1 != 0) goto L_0x00b7
            int r1 = r17.getActionMasked()
            if (r1 != r4) goto L_0x00b7
            float r1 = r11 - r13
            float r1 = r1 * r1
            float r3 = r12 - r14
            float r3 = r3 * r3
            float r1 = r1 + r3
            double r3 = (double) r1
            double r3 = java.lang.Math.sqrt(r3)
            float r1 = r9.mDistance
            double r6 = (double) r1
            double r3 = r3 - r6
            double r3 = java.lang.Math.abs(r3)
            com.zipow.videobox.ConfActivity r1 = r16.getConfActivity()
            int r3 = (int) r3
            float r1 = p021us.zoom.androidlib.util.UIUtil.px2dip(r1, r3)
            double r3 = (double) r1
            int r1 = r9.mMoveCount
            r6 = 20
            if (r1 > r6) goto L_0x00aa
            r7 = 4635329916471083008(0x4054000000000000, double:80.0)
            int r1 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r1 <= 0) goto L_0x00aa
            r9.mIsMultiTouchZooming = r10
            return r10
        L_0x00aa:
            int r1 = r9.mMoveCount
            if (r1 <= r6) goto L_0x00b1
            r9.mIsDoubleScrollMode = r10
            return r10
        L_0x00b1:
            int r1 = r1 + r10
            r9.mMoveCount = r1
            goto L_0x00b7
        L_0x00b5:
            r9.mIsMultiTouchZooming = r10
        L_0x00b7:
            boolean r1 = r9.mIsDoubleScrollMode
            if (r1 == 0) goto L_0x00f9
            long r0 = java.lang.System.currentTimeMillis()
            long r2 = r9.mLastDoubleScrollTime
            long r0 = r0 - r2
            r2 = 150(0x96, double:7.4E-322)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 <= 0) goto L_0x00f8
            long r0 = java.lang.System.currentTimeMillis()
            r9.mLastDoubleScrollTime = r0
            float r0 = r9.mLastDoubleScrollX
            float r0 = r11 - r0
            float r0 = java.lang.Math.abs(r0)
            float r1 = r9.mLastDoubleScrollY
            float r1 = r12 - r1
            float r1 = java.lang.Math.abs(r1)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L_0x00f4
            float r0 = r9.mLastDoubleScrollY
            float r0 = r0 - r12
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 <= 0) goto L_0x00ef
            r0 = 1065353216(0x3f800000, float:1.0)
            r9.remoteControlDoubleScroll(r5, r0)
            goto L_0x00f4
        L_0x00ef:
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r9.remoteControlDoubleScroll(r5, r0)
        L_0x00f4:
            r9.mLastDoubleScrollX = r11
            r9.mLastDoubleScrollY = r12
        L_0x00f8:
            return r10
        L_0x00f9:
            int r0 = r17.getActionMasked()
            if (r0 != r10) goto L_0x0115
            boolean r0 = r9.mIsMultiTouchZooming
            if (r0 == 0) goto L_0x010a
            r16.onMultiTouchZoomEnd()
            r16.moveMouseToRightPosition()
            return r10
        L_0x010a:
            boolean r0 = r9.mIsDoubleScrollMode
            if (r0 == 0) goto L_0x0115
            r9.mIsDoubleScrollMode = r2
            r9.mMoveCount = r2
            r9.mbIgnoreNextScroll = r10
            return r10
        L_0x0115:
            boolean r0 = r9.mIsMultiTouchZooming
            if (r0 == 0) goto L_0x013e
            float r6 = r9.mLastX1
            int r0 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
            if (r0 == 0) goto L_0x013e
            float r7 = r9.mLastY1
            int r0 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r0 == 0) goto L_0x013e
            float r8 = r9.mLastX2
            int r0 = (r8 > r5 ? 1 : (r8 == r5 ? 0 : -1))
            if (r0 == 0) goto L_0x013e
            float r15 = r9.mLastY2
            int r0 = (r15 > r5 ? 1 : (r15 == r5 ? 0 : -1))
            if (r0 == 0) goto L_0x013e
            r0 = r16
            r1 = r11
            r2 = r12
            r3 = r13
            r4 = r14
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r15
            r0.onMultiTouchZoom(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x013e:
            r9.mLastX1 = r11
            r9.mLastY1 = r12
            r9.mLastX2 = r13
            r9.mLastY2 = r14
            return r10
        L_0x0147:
            boolean r1 = r9.mIsMultiTouchZooming
            if (r1 == 0) goto L_0x0154
            r16.onMultiTouchZoomEnd()
            r16.moveMouseToRightPosition()
            r9.mMoveCount = r2
            return r10
        L_0x0154:
            boolean r1 = r9.mIsDoubleScrollMode
            if (r1 == 0) goto L_0x015f
            r9.mIsDoubleScrollMode = r2
            r9.mMoveCount = r2
            r9.mbIgnoreNextScroll = r10
            return r10
        L_0x015f:
            int r1 = r17.getPointerCount()
            if (r1 != r10) goto L_0x0274
            boolean r1 = r9.mInRemoteControlMode
            if (r1 == 0) goto L_0x0274
            int r1 = r17.getActionMasked()
            if (r1 != 0) goto L_0x01ca
            r9.mScrolled = r2
            r9.mFlinged = r2
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            if (r1 == 0) goto L_0x0194
            android.view.MotionEvent r3 = r9.mPreviousUpEvent
            if (r3 == 0) goto L_0x0194
            boolean r1 = r9.isConsideredDoubleTap(r1, r3, r0)
            if (r1 == 0) goto L_0x0194
            android.os.Handler r1 = r9.mRemoteControlGestureHandler
            r1.removeMessages(r4)
            r9.mIsDoubleTap = r10
            float r1 = r17.getX()
            float r3 = r17.getY()
            r9.remoteControlDoubleTap(r1, r3)
            goto L_0x01bb
        L_0x0194:
            android.os.Message r1 = android.os.Message.obtain()
            android.os.Bundle r3 = new android.os.Bundle
            r3.<init>()
            java.lang.String r4 = "x"
            float r5 = r17.getX()
            r3.putFloat(r4, r5)
            java.lang.String r4 = "y"
            float r5 = r17.getY()
            r3.putFloat(r4, r5)
            r1.what = r10
            r1.setData(r3)
            android.os.Handler r3 = r9.mRemoteControlGestureHandler
            r4 = 1500(0x5dc, double:7.41E-321)
            r3.sendMessageDelayed(r1, r4)
        L_0x01bb:
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            if (r1 == 0) goto L_0x01c2
            r1.recycle()
        L_0x01c2:
            android.view.MotionEvent r0 = android.view.MotionEvent.obtain(r17)
            r9.mCurrentDownEvent = r0
            goto L_0x0274
        L_0x01ca:
            int r1 = r17.getActionMasked()
            if (r1 != r4) goto L_0x0200
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            if (r1 == 0) goto L_0x0274
            float r1 = r1.getX()
            float r4 = r17.getX()
            float r1 = r1 - r4
            float r1 = java.lang.Math.abs(r1)
            r4 = 1092616192(0x41200000, float:10.0)
            int r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r1 > 0) goto L_0x01fa
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            float r1 = r1.getY()
            float r0 = r17.getY()
            float r1 = r1 - r0
            float r0 = java.lang.Math.abs(r1)
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0274
        L_0x01fa:
            android.os.Handler r0 = r9.mRemoteControlGestureHandler
            r0.removeCallbacksAndMessages(r3)
            return r2
        L_0x0200:
            int r1 = r17.getActionMasked()
            if (r1 != r10) goto L_0x0274
            android.os.Handler r1 = r9.mRemoteControlGestureHandler
            r1.removeMessages(r10)
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            if (r1 == 0) goto L_0x0265
            boolean r1 = r9.mIsDoubleTap
            if (r1 != 0) goto L_0x0265
            long r5 = r17.getEventTime()
            android.view.MotionEvent r1 = r9.mCurrentDownEvent
            long r7 = r1.getEventTime()
            long r5 = r5 - r7
            r7 = 200(0xc8, double:9.9E-322)
            int r1 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r1 >= 0) goto L_0x0265
            boolean r1 = r9.mScrolled
            if (r1 != 0) goto L_0x0265
            boolean r1 = r9.mFlinged
            if (r1 != 0) goto L_0x0265
            android.os.Message r1 = android.os.Message.obtain()
            android.os.Bundle r3 = new android.os.Bundle
            r3.<init>()
            java.lang.String r5 = "x"
            float r6 = r17.getX()
            r3.putFloat(r5, r6)
            java.lang.String r5 = "y"
            float r6 = r17.getY()
            r3.putFloat(r5, r6)
            java.lang.String r5 = "raw_x"
            float r6 = r17.getRawX()
            r3.putFloat(r5, r6)
            java.lang.String r5 = "raw_y"
            float r6 = r17.getRawY()
            r3.putFloat(r5, r6)
            r1.setData(r3)
            r1.what = r4
            android.os.Handler r3 = r9.mRemoteControlGestureHandler
            r4 = 500(0x1f4, double:2.47E-321)
            r3.sendMessageDelayed(r1, r4)
        L_0x0265:
            r9.mIsDoubleTap = r2
            android.view.MotionEvent r1 = r9.mPreviousUpEvent
            if (r1 == 0) goto L_0x026e
            r1.recycle()
        L_0x026e:
            android.view.MotionEvent r0 = android.view.MotionEvent.obtain(r17)
            r9.mPreviousUpEvent = r0
        L_0x0274:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.video.ShareVideoScene.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean isConsideredDoubleTap(@NonNull MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
        boolean z = false;
        if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > 300) {
            return false;
        }
        int x = ((int) motionEvent.getX()) - ((int) motionEvent3.getX());
        int y = ((int) motionEvent.getY()) - ((int) motionEvent3.getY());
        if (((float) ((x * x) + (y * y))) < UIUtil.px2dip(getConfActivity(), 100) * UIUtil.px2dip(getConfActivity(), 100)) {
            z = true;
        }
        return z;
    }

    private void moveMouseToRightPosition() {
        if (this.mRCMouseX != 0.0f || this.mRCMouseY != 0.0f) {
            moveMouse(shareContentToViewX(this.mRCMouseX), shareContentToViewY(this.mRCMouseY));
        }
    }

    public boolean onVideoViewSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        if (this.mInRemoteControlMode) {
            return true;
        }
        if (!isInTargetRange(motionEvent, this.mIsBigShareView ? this.mUnitActiveVideo : this.mUnitShare) || !canChangeShareViewSizeByTap()) {
            return super.onVideoViewSingleTapConfirmed(motionEvent);
        }
        changeShareViewSize(!this.mIsBigShareView);
        return true;
    }

    private void onMultiTouchZoom(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = f3 - f;
        float f10 = f4 - f2;
        float f11 = f7 - f5;
        float f12 = f8 - f6;
        double sqrt = this.mZoomVal * (Math.sqrt((double) ((f9 * f9) + (f10 * f10))) / Math.sqrt((double) ((f11 * f11) + (f12 * f12))));
        PointF unitPosToPixelPosOnContent = unitPosToPixelPosOnContent(viewXToShareUnitX(f5), viewYToShareUnitY(f6), this.mZoomVal);
        float f13 = (float) (((double) unitPosToPixelPosOnContent.x) * sqrt);
        float f14 = (float) (((double) unitPosToPixelPosOnContent.y) * sqrt);
        this.mZoomVal = sqrt;
        this.mIsFitScreen = checkFitScreen();
        updateUnitsWithoutResetDestArea();
        float viewXToShareUnitX = viewXToShareUnitX(f);
        float viewYToShareUnitY = viewYToShareUnitY(f2);
        VideoSize videoSize = this.mShareSize;
        if (videoSize != null && videoSize.width != 0) {
            this.mScaleWidth = (float) (((double) this.mShareSize.width) * sqrt);
            this.mScaleHeight = (float) (((double) this.mShareSize.height) * sqrt);
            this.mContentX = viewXToShareUnitX - f13;
            this.mContentY = viewYToShareUnitY - f14;
            trimContentPos();
            notifyDestAreaChanged();
        }
    }

    private void onMultiTouchZoomEnd() {
        this.mIsMultiTouchZooming = false;
        this.mLastMultiTouchEndTime = System.currentTimeMillis();
        if (this.mZoomVal < getMinLevelZoomValue()) {
            zoomToFitUnit();
            moveMouseToRightPosition();
        } else if (this.mZoomVal > getMaxLevelZoomValue() && this.mUnitShare != null) {
            switchToLevel(getScaleLevelsCount() - 1, (float) ((this.mUnitShare.getWidth() / 2) + this.mUnitShare.getLeft()), (float) ((this.mUnitShare.getHeight() / 2) + this.mUnitShare.getTop()));
            moveMouseToRightPosition();
        }
    }

    public boolean canDragSceneToLeft() {
        boolean z = false;
        if (this.mSwitchingView) {
            return false;
        }
        if (!this.mIsBigShareView) {
            return true;
        }
        if (this.mInRemoteControlMode || this.mUnitShare == null || !this.mHasCachedData) {
            return false;
        }
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null) {
            return true;
        }
        if (this.mContentX + ((float) (this.mZoomVal * ((double) videoSize.width))) <= ((float) this.mUnitShare.getWidth())) {
            z = true;
        }
        return z;
    }

    public boolean canDragSceneToRight() {
        boolean z = false;
        if (this.mSwitchingView) {
            return false;
        }
        if (!this.mIsBigShareView) {
            return true;
        }
        if (this.mInRemoteControlMode || this.mUnitShare == null || !this.mHasCachedData) {
            return false;
        }
        if (this.mShareSize == null) {
            return true;
        }
        if (this.mContentX >= 0.0f) {
            z = true;
        }
        return z;
    }

    private boolean checkFitScreen() {
        if (this.mZoomVal < 0.01d) {
            return true;
        }
        boolean z = false;
        if (Math.abs(this.mZoomVal - scaleLevelToZoomValue(0)) < 0.01d) {
            z = true;
        }
        return z;
    }

    private double getMaxLevelZoomValue() {
        return (double) ((VideoBoxApplication.getInstance().getResources().getDisplayMetrics().density * 160.0f) / 120.0f);
    }

    private double getMinLevelZoomValue() {
        if (this.mShareSize == null) {
            return 0.0d;
        }
        int shareRenderWidth = getShareRenderWidth();
        int shareRenderHeight = getShareRenderHeight();
        return (this.mShareSize.height * shareRenderWidth > this.mShareSize.width * shareRenderHeight ? (((double) shareRenderHeight) * ((double) this.mShareSize.width)) / ((double) this.mShareSize.height) : (double) shareRenderWidth) / ((double) this.mShareSize.width);
    }

    private int getScaleLevelsCount() {
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null || videoSize.width == 0 || this.mShareSize.height == 0) {
            return 3;
        }
        double maxLevelZoomValue = getMaxLevelZoomValue();
        float f = (float) (((double) this.mShareSize.height) * maxLevelZoomValue);
        if (((float) (((double) this.mShareSize.width) * maxLevelZoomValue)) <= ((float) getShareRenderWidth()) && f < ((float) getShareRenderHeight())) {
            return 1;
        }
        double minLevelZoomValue = ((getMinLevelZoomValue() + maxLevelZoomValue) * 2.0d) / 5.0d;
        float f2 = (float) (minLevelZoomValue * ((double) this.mShareSize.height));
        if (((float) (((double) this.mShareSize.width) * minLevelZoomValue)) > ((float) getShareRenderWidth()) || f2 >= ((float) getShareRenderHeight())) {
            return 3;
        }
        return 2;
    }

    private int getCurrentScaleLevel() {
        int scaleLevelsCount = getScaleLevelsCount();
        double[] dArr = new double[scaleLevelsCount];
        int i = 0;
        for (int i2 = 0; i2 < scaleLevelsCount; i2++) {
            dArr[i2] = scaleLevelToZoomValue(i2);
        }
        while (true) {
            int i3 = scaleLevelsCount - 1;
            if (i >= i3) {
                return i3;
            }
            double d = this.mZoomVal;
            if (d >= dArr[i] && d < dArr[i + 1]) {
                return i;
            }
            i++;
        }
    }

    private void switchToLevel(int i, float f, float f2) {
        switchToZoom(scaleLevelToZoomValue(i), f, f2);
    }

    private void switchToZoom(double d, float f, float f2) {
        double d2 = this.mZoomVal;
        this.mZoomVal = d;
        this.mIsFitScreen = checkFitScreen();
        PointF unitPosToPixelPosOnContent = unitPosToPixelPosOnContent(viewXToShareUnitX(f), viewYToShareUnitY(f2), d2);
        updateUnitsWithoutResetDestArea();
        VideoSize videoSize = this.mShareSize;
        if (videoSize != null && videoSize.width != 0) {
            float f3 = unitPosToPixelPosOnContent.x;
            float f4 = unitPosToPixelPosOnContent.y;
            this.mScaleWidth = (float) (((double) this.mShareSize.width) * this.mZoomVal);
            this.mScaleHeight = (float) (((double) this.mShareSize.height) * this.mZoomVal);
            resetDestAreaCenter(f3, f4);
        }
    }

    @Nullable
    private PointF getCenterPixelPosOnContent() {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return null;
        }
        return unitPosToPixelPosOnContent((float) (shareUnit.getWidth() / 2), (float) (this.mUnitShare.getHeight() / 2), this.mZoomVal);
    }

    private void resetDestAreaCenter(float f, float f2) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit != null) {
            this.mContentX = ((float) (shareUnit.getWidth() / 2)) - ((float) (((double) f) * this.mZoomVal));
            this.mContentY = ((float) (this.mUnitShare.getHeight() / 2)) - ((float) (((double) f2) * this.mZoomVal));
            trimContentPos();
            notifyDestAreaChanged();
        }
    }

    private PointF unitPosToPixelPosOnContent(float f, float f2, double d) {
        return new PointF((float) (((double) (f - this.mContentX)) / d), (float) (((double) (f2 - this.mContentY)) / d));
    }

    private void notifyDestAreaChanged() {
        VideoSize videoSize = this.mShareSize;
        if (videoSize != null && videoSize.width != 0 && videoSize.height != 0) {
            ShareUnit shareUnit = this.mUnitShare;
            if (shareUnit != null) {
                shareUnit.destAreaChanged((int) this.mContentX, (int) this.mContentY, (int) this.mScaleWidth, (int) this.mScaleHeight);
            }
        }
    }

    private void zoomToFitUnit() {
        if (this.mUnitShare != null) {
            this.mZoomVal = scaleLevelToZoomValue(0);
            this.mIsFitScreen = checkFitScreen();
            this.mContentX = 0.0f;
            this.mContentY = 0.0f;
            updateUnitsWithoutResetDestArea();
            this.mScaleWidth = (float) this.mUnitShare.getWidth();
            this.mScaleHeight = (float) this.mUnitShare.getHeight();
            notifyDestAreaChanged();
        }
    }

    public float viewToShareContentX(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return (float) (((double) ((f - ((float) shareUnit.getLeft())) - this.mContentX)) / this.mZoomVal);
    }

    public float viewToShareContentY(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return (float) (((double) ((f - ((float) shareUnit.getTop())) - this.mContentY)) / this.mZoomVal);
    }

    public float shareContentToViewX(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return (float) ((((double) f) * this.mZoomVal) + ((double) shareUnit.getLeft()) + ((double) this.mContentX));
    }

    public float shareContentToViewY(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return (float) ((((double) f) * this.mZoomVal) + ((double) shareUnit.getTop()) + ((double) this.mContentY));
    }

    private float viewXToShareUnitX(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return f - ((float) shareUnit.getLeft());
    }

    private float viewYToShareUnitY(float f) {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit == null) {
            return f;
        }
        return f - ((float) shareUnit.getTop());
    }

    private double scaleLevelToZoomValue(int i) {
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null || videoSize.width == 0) {
            return 1.0d;
        }
        double minLevelZoomValue = getMinLevelZoomValue();
        double maxLevelZoomValue = getMaxLevelZoomValue();
        double d = ((minLevelZoomValue + maxLevelZoomValue) * 2.0d) / 5.0d;
        int scaleLevelsCount = getScaleLevelsCount();
        if (scaleLevelsCount == 1) {
            if (minLevelZoomValue <= maxLevelZoomValue || !getVideoSceneMgr().isVideoShareInProgress()) {
                return Math.min(minLevelZoomValue, maxLevelZoomValue);
            }
            return minLevelZoomValue;
        } else if (scaleLevelsCount == 2) {
            return i != 0 ? maxLevelZoomValue : minLevelZoomValue;
        } else {
            if (scaleLevelsCount < 3) {
                return 0.0d;
            }
            switch (i) {
                case 0:
                    return minLevelZoomValue;
                case 1:
                    return d;
                default:
                    return maxLevelZoomValue;
            }
        }
    }

    public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mIsBigShareView) {
            if (this.mbIgnoreNextScroll) {
                this.mbIgnoreNextScroll = false;
                return;
            }
            this.mScrolled = true;
            this.mStopFling = true;
            if (this.mIsNotWaiting && System.currentTimeMillis() - this.mLastMultiTouchEndTime >= 300) {
                this.mContentX -= f;
                this.mContentY -= f2;
                trimContentPos();
                if (isInRemoteControlMode()) {
                    moveMouseToRightPosition();
                }
                notifyDestAreaChanged();
            }
        }
    }

    private void trimContentPos() {
        if (this.mUnitShare != null) {
            VideoSize videoSize = this.mShareSize;
            if (videoSize != null) {
                float f = (float) (this.mZoomVal * ((double) videoSize.width));
                float f2 = (float) (this.mZoomVal * ((double) this.mShareSize.height));
                if (this.mContentX > 0.0f) {
                    if (f >= ((float) this.mUnitShare.getWidth())) {
                        this.mContentX = 0.0f;
                    } else if (this.mContentX + f > ((float) this.mUnitShare.getWidth())) {
                        this.mContentX = ((float) this.mUnitShare.getWidth()) - f;
                    }
                } else if (f >= ((float) this.mUnitShare.getWidth()) && this.mContentX + f < ((float) this.mUnitShare.getWidth())) {
                    this.mContentX = ((float) this.mUnitShare.getWidth()) - f;
                } else if (f <= ((float) this.mUnitShare.getWidth())) {
                    this.mContentX = 0.0f;
                }
                if (this.mContentY > 0.0f) {
                    if (f2 >= ((float) this.mUnitShare.getHeight())) {
                        this.mContentY = 0.0f;
                    } else if (this.mContentY + f2 > ((float) this.mUnitShare.getHeight())) {
                        this.mContentY = ((float) this.mUnitShare.getHeight()) - f2;
                    }
                } else if (f2 >= ((float) this.mUnitShare.getHeight()) && this.mContentY + f2 < ((float) this.mUnitShare.getHeight())) {
                    this.mContentY = ((float) this.mUnitShare.getHeight()) - f2;
                } else if (f2 <= ((float) this.mUnitShare.getHeight())) {
                    this.mContentY = 0.0f;
                }
            }
        }
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float f3;
        if (this.mIsBigShareView && !this.mIsMultiTouchZooming && !this.mIsDoubleScrollMode) {
            this.mFlinged = true;
            if (this.mIsNotWaiting && isCreated() && System.currentTimeMillis() - this.mLastMultiTouchEndTime >= 300 && this.mUnitShare != null) {
                VideoSize videoSize = this.mShareSize;
                if (videoSize != null) {
                    int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                    if (i > 0) {
                        this.mScroller.setFinalX(0);
                    } else {
                        this.mScroller.setFinalX((int) (((float) this.mUnitShare.getWidth()) - ((float) (this.mZoomVal * ((double) videoSize.width)))));
                    }
                    int i2 = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
                    if (i2 > 0) {
                        this.mScroller.setFinalY(0);
                    } else {
                        this.mScroller.setFinalY((int) (((float) this.mUnitShare.getHeight()) - ((float) (this.mZoomVal * ((double) this.mShareSize.height)))));
                    }
                    int dip2px = UIUtil.dip2px(getConfActivity(), 1500.0f);
                    if (Math.abs(f) > Math.abs(f2)) {
                        if (i == 0) {
                            f = 0.1f;
                        }
                        float f4 = f2 / f;
                        f3 = (float) dip2px;
                        if (f <= f3) {
                            f3 = (float) (-dip2px);
                            if (f >= f3) {
                                f3 = f;
                            }
                        }
                        f2 = f4 * f3;
                    } else {
                        if (i2 == 0) {
                            f2 = 0.1f;
                        }
                        float f5 = f / f2;
                        float f6 = (float) dip2px;
                        if (f2 > f6) {
                            f2 = f6;
                        } else {
                            float f7 = (float) (-dip2px);
                            if (f2 < f7) {
                                f2 = f7;
                            }
                        }
                        f3 = f2 * f5;
                    }
                    this.mScroller.fling((int) this.mContentX, (int) this.mContentY, (int) f3, (int) f2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    this.mStopFling = false;
                    handleFling();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFling() {
        this.mFlingHandler.postDelayed(new Runnable() {
            public void run() {
                if (!ShareVideoScene.this.mStopFling && ShareVideoScene.this.updateContentPosOnFling()) {
                    ShareVideoScene.this.handleFling();
                }
            }
        }, 40);
    }

    /* access modifiers changed from: private */
    public boolean updateContentPosOnFling() {
        boolean z;
        boolean z2;
        boolean z3 = false;
        if (this.mUnitShare == null || this.mShareSize == null || !this.mScroller.computeScrollOffset()) {
            return false;
        }
        this.mContentX = (float) this.mScroller.getCurrX();
        if (this.mContentX > 0.0f) {
            this.mContentX = 0.0f;
            z = true;
        } else {
            float f = (float) (this.mZoomVal * ((double) this.mShareSize.width));
            if (this.mContentX + f < ((float) this.mUnitShare.getWidth())) {
                this.mContentX = ((float) this.mUnitShare.getWidth()) - f;
                z = true;
            } else {
                z = false;
            }
        }
        this.mContentY = (float) this.mScroller.getCurrY();
        if (this.mContentY > 0.0f) {
            this.mContentY = 0.0f;
            z2 = true;
        } else {
            float f2 = (float) (this.mZoomVal * ((double) this.mShareSize.height));
            if (this.mContentY + f2 < ((float) this.mUnitShare.getHeight())) {
                this.mContentY = ((float) this.mUnitShare.getHeight()) - f2;
                z2 = true;
            } else {
                z2 = false;
            }
        }
        notifyDestAreaChanged();
        moveMouseToRightPosition();
        if (!z && !z2) {
            z3 = true;
        }
        return z3;
    }

    private void createGLImageWaterMark() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            Bitmap createWaterMarkBitmap = ConfLocalHelper.createWaterMarkBitmap(getShareRenderWidth(), getShareRenderHeight(), C4558R.color.zm_share_text, 1.0f);
            if (createWaterMarkBitmap != null) {
                this.mGLImageWaterMark = videoObj.createGLImage(createGLImageWaterMarkUnitInfo());
                GLImage gLImage = this.mGLImageWaterMark;
                if (gLImage != null) {
                    gLImage.setVisible(false);
                    this.mGLImageWaterMark.setUnitName("mGLImageWaterMark");
                    this.mGLImageWaterMark.setVideoScene(this);
                    addUnit(this.mGLImageWaterMark);
                    this.mGLImageWaterMark.onCreate();
                    this.mGLImageWaterMark.setBackground(createWaterMarkBitmap);
                }
            }
        }
    }

    private void updateGLImageWaterMark() {
        boolean z;
        if (this.mGLImageWaterMark != null && !hasGrantedUnits()) {
            RendererUnitInfo createGLImageWaterMarkUnitInfo = createGLImageWaterMarkUnitInfo();
            if (!this.mIsBigShareView) {
                VideoUnit videoUnit = this.mUnitActiveVideo;
                if (videoUnit == null || videoUnit.getmVideoType() != 2 || !this.mUnitActiveVideo.isVideoShowing()) {
                    z = false;
                    if (this.mIsNotWaiting || !z) {
                        this.mGLImageWaterMark.setVisible(false);
                    } else {
                        this.mGLImageWaterMark.updateUnitInfo(createGLImageWaterMarkUnitInfo);
                        this.mGLImageWaterMark.setVisible(true);
                        if (!(this.mWaterMarkWidth == getShareRenderWidth() && this.mWaterMarkHeight == getShareRenderHeight())) {
                            Bitmap createWaterMarkBitmap = ConfLocalHelper.createWaterMarkBitmap(getShareRenderWidth(), getShareRenderHeight(), C4558R.color.zm_share_text, 1.0f);
                            if (createWaterMarkBitmap != null) {
                                this.mGLImageWaterMark.setBackground(createWaterMarkBitmap);
                                this.mWaterMarkWidth = getShareRenderWidth();
                                this.mWaterMarkHeight = getShareRenderHeight();
                            }
                        }
                    }
                }
            }
            z = true;
            if (this.mIsNotWaiting) {
            }
            this.mGLImageWaterMark.setVisible(false);
        }
    }

    private RendererUnitInfo createGLImageWaterMarkUnitInfo() {
        return new RendererUnitInfo(getLeft(), getTop(), getShareRenderWidth(), getShareRenderHeight());
    }

    private void updateSwitchScenePanel() {
        String str;
        if (!isVideoPaused()) {
            ConfActivity confActivity = getConfActivity();
            if (confActivity != null) {
                View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
                LinearLayout linearLayout = (LinearLayout) confActivity.findViewById(C4558R.C4560id.panelSwitchSceneButtons);
                this.mSwitchSceneButtons = new ImageButton[10];
                VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
                int sceneCount = videoSceneMgr.getSceneCount();
                int basicSceneCount = videoSceneMgr.getBasicSceneCount();
                linearLayout.removeAllViews();
                int i = 0;
                int i2 = 0;
                while (true) {
                    ImageButton[] imageButtonArr = this.mSwitchSceneButtons;
                    if (i2 >= imageButtonArr.length) {
                        break;
                    }
                    imageButtonArr[i2] = new ImageButton(confActivity);
                    this.mSwitchSceneButtons[i2].setBackgroundColor(0);
                    int i3 = basicSceneCount - 1;
                    this.mSwitchSceneButtons[i2].setImageResource(i2 == i3 ? C4558R.C4559drawable.zm_btn_switch_scene_selected : C4558R.C4559drawable.zm_btn_switch_scene_unselected);
                    this.mSwitchSceneButtons[i2].setVisibility(i2 < sceneCount ? 0 : 8);
                    this.mSwitchSceneButtons[i2].setOnClickListener(this);
                    ImageButton imageButton = this.mSwitchSceneButtons[i2];
                    if (i2 == i3) {
                        str = confActivity.getString(C4558R.string.zm_description_scene_share);
                    } else {
                        str = ((VideoSceneMgr) getVideoSceneMgr()).getAccessibliltyDescriptionSceneSwitch(i2);
                    }
                    imageButton.setContentDescription(str);
                    linearLayout.addView(this.mSwitchSceneButtons[i2], UIUtil.dip2px(confActivity, 20.0f), UIUtil.dip2px(confActivity, 40.0f));
                    i2++;
                }
                positionSwitchScenePanel();
                if (sceneCount <= 1) {
                    i = 4;
                }
                findViewById.setVisibility(i);
            }
        }
    }

    private void positionSwitchScenePanel() {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            int height = getHeight() - UIUtil.dip2px(confActivity, 45.0f);
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
            int i = 0;
            findViewById.setPadding(0, height, 0, 0);
            findViewById.getParent().requestLayout();
            if (isLargeShareVideoMode()) {
                i = 4;
            }
            findViewById.setVisibility(i);
        }
    }

    public void onClick(View view) {
        int i = 0;
        while (true) {
            ImageButton[] imageButtonArr = this.mSwitchSceneButtons;
            if (i < imageButtonArr.length) {
                if (imageButtonArr[i] == view) {
                    switchToScene(i);
                }
                i++;
            } else {
                return;
            }
        }
    }

    private void switchToScene(int i) {
        if (i != ((VideoSceneMgr) getVideoSceneMgr()).getBasicSceneCount() - 1) {
            getVideoSceneMgr().switchToScene(i);
        }
    }

    public void cacheUnits() {
        ShareUnit shareUnit = this.mUnitShare;
        if (shareUnit != null) {
            removeUnit(shareUnit);
            this.mUnitShare.updateUnitInfo(new RendererUnitInfo(-this.mUnitShare.getWidth(), this.mUnitShare.getTop(), this.mUnitShare.getWidth(), this.mUnitShare.getHeight()));
            this.mCachedUnitShare = this.mUnitShare;
            this.mCachedShareSize = this.mShareSize;
            this.mUnitShare = null;
            this.mShareSize = null;
        }
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            removeUnit(videoUnit);
            this.mUnitActiveVideo.updateUnitInfo(new RendererUnitInfo(-this.mUnitActiveVideo.getWidth(), this.mUnitActiveVideo.getTop(), this.mUnitActiveVideo.getWidth(), this.mUnitActiveVideo.getHeight()));
            this.mCachedUnitVideo = this.mUnitActiveVideo;
            this.mCachedVideoSize = this.mActiveVideoSize;
            this.mUnitActiveVideo = null;
            this.mActiveVideoSize = null;
        }
    }

    public void destroyCachedUnits() {
        ShareUnit shareUnit = this.mCachedUnitShare;
        if (shareUnit != null) {
            shareUnit.onDestroy();
            this.mCachedUnitShare = null;
            this.mCachedShareSize = null;
            this.mShareSize = null;
        }
        VideoUnit videoUnit = this.mCachedUnitVideo;
        if (videoUnit != null) {
            videoUnit.onDestroy();
            this.mCachedUnitVideo = null;
            this.mCachedVideoSize = null;
            this.mActiveVideoSize = null;
        }
        this.mHasCachedData = false;
    }

    public void onNetworkRestrictionModeChanged(boolean z) {
        if (isVisible()) {
            updateSwitchScenePanel();
        }
    }

    public void hideTitleAndSwitchScenePanel() {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSharingTitle);
            if (findViewById != null) {
                findViewById.setVisibility(8);
            }
            View findViewById2 = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
            if (findViewById2 != null) {
                findViewById2.setVisibility(8);
            }
        }
    }

    public boolean isInRemoteControlMode() {
        return this.mInRemoteControlMode;
    }

    public void setInRemoteControlMode(boolean z) {
        this.mInRemoteControlMode = z;
    }

    public boolean remoteControlSingleTap(float f, float f2) {
        float viewToShareContentX = viewToShareContentX(f);
        float viewToShareContentY = viewToShareContentY(f2);
        this.mRCMouseX = viewToShareContentX;
        this.mRCMouseY = viewToShareContentY;
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        if (shareSessionMgr != null) {
            return shareSessionMgr.remoteControlSingleTap(viewToShareContentX, viewToShareContentY);
        }
        return false;
    }

    public boolean remoteControlDoubleTap(float f, float f2) {
        float viewToShareContentX = viewToShareContentX(f);
        float viewToShareContentY = viewToShareContentY(f2);
        this.mRCMouseX = viewToShareContentX;
        this.mRCMouseY = viewToShareContentY;
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        if (shareSessionMgr != null) {
            return shareSessionMgr.remoteControlDoubleTap(viewToShareContentX, viewToShareContentY);
        }
        return false;
    }

    public boolean remoteControlLongPress(float f, float f2) {
        float viewToShareContentX = viewToShareContentX(f);
        float viewToShareContentY = viewToShareContentY(f2);
        this.mRCMouseX = viewToShareContentX;
        this.mRCMouseY = viewToShareContentY;
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        if (shareSessionMgr != null) {
            return shareSessionMgr.remoteControlLongPress(viewToShareContentX, viewToShareContentY);
        }
        return false;
    }

    public boolean remoteControlDoubleScroll(float f, float f2) {
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        if (shareSessionMgr != null) {
            return shareSessionMgr.remoteControlDoubleScroll(f, f2);
        }
        return false;
    }

    public boolean remoteControlSingleMove(float f, float f2) {
        float viewToShareContentX = viewToShareContentX(f);
        float viewToShareContentY = viewToShareContentY(f2);
        this.mRCMouseX = viewToShareContentX;
        this.mRCMouseY = viewToShareContentY;
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        if (shareSessionMgr != null) {
            return shareSessionMgr.remoteControlSingleMove(viewToShareContentX, viewToShareContentY);
        }
        return false;
    }

    public boolean remoteControlCharInput(String str) {
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        return shareSessionMgr != null && shareSessionMgr.remoteControlCharInput(str);
    }

    public boolean remoteControlKeyInput(int i) {
        ShareSessionMgr shareSessionMgr = this.mShareSession;
        return shareSessionMgr != null && shareSessionMgr.remoteControlKeyInput(i);
    }

    public int getShareRenderHeight() {
        return getHeight();
    }

    public int getShareRenderWidth() {
        return getWidth();
    }

    public boolean isNoVideoTileOnShareScreenEnabled() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.NO_VIDEO_TILE_ON_SHARE_SCREEN, false);
    }

    public boolean isBigShareView() {
        return this.mIsBigShareView;
    }

    private boolean isShowUserName(boolean z) {
        return z && !getConfActivity().isToolbarShowing() && !ConfMgr.getInstance().isCallingOut();
    }

    private boolean canChangeShareViewSizeByTap() {
        if (ConfMgr.getInstance().isViewOnlyClientOnMMR()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.getAttendeeVideoControlMode() == 2) {
                return false;
            }
        }
        return true;
    }

    public void changeShareViewSize(boolean z) {
        if (this.mIsBigShareView != z) {
            this.mSwitchingView = true;
            this.mIsBigShareView = z;
            if (!this.mIsBigShareView) {
                ZMConfComponentMgr.getInstance().switchToSmallShare();
            }
            if (isPreloadStatus()) {
                destroy(true);
            } else if (isVisible()) {
                stop();
                int width = getWidth();
                int height = getHeight();
                destroy(true);
                create(width, height);
                start();
            }
        }
    }

    /* access modifiers changed from: private */
    public void moveMouse(float f, float f2) {
        RCMouseView rCMouseView = ZMConfComponentMgr.getInstance().getRCMouseView();
        if (rCMouseView != null) {
            rCMouseView.moveMouse(f, f2);
        }
    }
}
