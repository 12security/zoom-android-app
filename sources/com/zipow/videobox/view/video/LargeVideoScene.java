package com.zipow.videobox.view.video;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoSize;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmShareStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.GLButton;
import com.zipow.videobox.confapp.GLButton.OnClickListener;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.ShareUnit;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.util.ConfShareLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class LargeVideoScene extends AbsVideoScene implements OnClickListener {
    private static final int GALLERY_UNIT_MARGIN_DIP = 20;
    private static int MAX_GALLERY_ITEMS_COUNT = 4;
    private static final int MAX_SCALE_LEVEL_COUNT = 3;
    private static final int MAX_VELOCITY_IN_DIP = 1500;
    private final String TAG = LargeVideoScene.class.getSimpleName();
    private VideoSize mActiveVideoSize;
    private float mContentX = 0.0f;
    private float mContentY = 0.0f;
    private int mCountUsers = 1;
    @Nullable
    private Drawable mDrawableGalleryCollapse = null;
    @Nullable
    private Drawable mDrawableGalleryExpand = null;
    @NonNull
    private Handler mFlingHandler = new Handler();
    private GLButton mGLBtnCloseGallery;
    private GLButton mGLBtnExpandGallery;
    private GLButton mGLBtnExpandVideo;
    private GLButton mGLBtnSwitchCamera;
    private int mGalleryScrollPos = 0;
    private boolean mHasCachedData = false;
    private boolean mIsExchangedMode = false;
    private boolean mIsFitScreen = true;
    private boolean mIsMultiTouchZooming = false;
    private boolean mIsNotWaiting_UnitShare = false;
    private boolean mIsScrollingGallery = false;
    private boolean mIsVideoExpand = true;
    private boolean mIsVideoGalleryExpand = false;
    private float mLastX1;
    private float mLastX2;
    private float mLastY1;
    private float mLastY2;
    @NonNull
    private ArrayList<VideoUnit> mListGalleryUnits = new ArrayList<>();
    private float mScaleHeight = 0.0f;
    private float mScaleWidth = 0.0f;
    @Nullable
    private Scroller mScroller = new Scroller(VideoBoxApplication.getInstance(), new DecelerateInterpolator(1.0f));
    @Nullable
    private VideoSize mShareSize;
    /* access modifiers changed from: private */
    public boolean mStopFling_UnitShare = false;
    private VideoUnit mUnitBigVideo;
    private ShareUnit mUnitShare;
    private VideoUnit mUnitSmallSingleVideo;
    private double mZoomVal = 0.0d;

    public boolean needShowSwitchCameraButton() {
        return false;
    }

    public void onNetworkRestrictionModeChanged(boolean z) {
    }

    public LargeVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
        if (!UIUtil.isLargeScreen(VideoBoxApplication.getInstance()) && MAX_GALLERY_ITEMS_COUNT > 3) {
            MAX_GALLERY_ITEMS_COUNT = 3;
        }
    }

    public long getPreviewRenderInfo() {
        VideoUnit videoUnit = this.mUnitBigVideo;
        if (videoUnit != null) {
            return videoUnit.getRendererInfo();
        }
        return 0;
    }

    public boolean hasContent() {
        return this.mHasCachedData;
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        createUnitShare();
        createUnitBigVideo();
        createSmallSingleVideoUnit();
        createGalleryUnits();
        createExpandVideoButton();
        createExpandGalleryButton();
        createCloseGalleryButton();
        createSwitchCameraButton();
    }

    private void createUnitBigVideo() {
        if (this.mUnitBigVideo == null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.mUnitBigVideo = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createBigVideoUnitInfo(getVideoSceneMgr().getActiveUserId() > 0));
                VideoUnit videoUnit = this.mUnitBigVideo;
                if (videoUnit != null) {
                    videoUnit.setUnitName("BigVideo");
                    this.mUnitBigVideo.setVideoScene(this);
                    this.mUnitBigVideo.setBorderVisible(false);
                    this.mUnitBigVideo.setBackgroundColor(0);
                    this.mUnitBigVideo.setUserNameVisible(false);
                    this.mUnitBigVideo.setCanShowAudioOff(true);
                    this.mUnitBigVideo.setCanShowWaterMark(true);
                    addUnit(this.mUnitBigVideo);
                    this.mUnitBigVideo.onCreate();
                }
            }
        }
    }

    private void createSmallSingleVideoUnit() {
        if (this.mUnitSmallSingleVideo == null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.mUnitSmallSingleVideo = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createSmallSingleVideoUnitInfo());
                VideoUnit videoUnit = this.mUnitSmallSingleVideo;
                if (videoUnit != null) {
                    videoUnit.setUnitName("SmallSingleVideo");
                    this.mUnitSmallSingleVideo.setVideoScene(this);
                    this.mUnitSmallSingleVideo.setBorderVisible(false);
                    this.mUnitSmallSingleVideo.setBackgroundColor(0);
                    this.mUnitSmallSingleVideo.setUserNameVisible(true);
                    this.mUnitSmallSingleVideo.setCanShowAudioOff(true);
                    addUnit(this.mUnitSmallSingleVideo);
                    this.mUnitSmallSingleVideo.onCreate();
                }
            }
        }
    }

    @NonNull
    private RendererUnitInfo createSmallSingleVideoUnitInfo() {
        VideoSize videoSize;
        if (getVideoSceneMgr().isViewingSharing()) {
            VideoSize videoSize2 = this.mActiveVideoSize;
            if (videoSize2 == null || videoSize2.width == 0 || this.mActiveVideoSize.height == 0) {
                this.mActiveVideoSize = new VideoSize(16, 9);
            }
            videoSize = calcSmallSingleVideoUnitSize(this.mActiveVideoSize);
        } else {
            videoSize = calcSmallSingleVideoUnitSize(getMyVideoSize());
        }
        return createSmallSingleVideoUnitInfo(videoSize);
    }

    /* access modifiers changed from: private */
    public void createUnitShare() {
        if (this.mUnitShare == null) {
            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
            if (shareObj != null) {
                RendererUnitInfo createShareUnitInfo = createShareUnitInfo();
                if (createShareUnitInfo != null) {
                    this.mUnitShare = shareObj.createShareUnit(createShareUnitInfo);
                    ShareUnit shareUnit = this.mUnitShare;
                    if (shareUnit != null) {
                        shareUnit.setVideoScene(this);
                        addUnit(this.mUnitShare);
                        this.mUnitShare.onCreate();
                    }
                }
            }
        }
    }

    private void createGalleryUnits() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            for (int i = 0; i <= MAX_GALLERY_ITEMS_COUNT; i++) {
                VideoUnit createVideoUnit = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createGalleryUnitInfo(i));
                if (createVideoUnit != null) {
                    this.mListGalleryUnits.add(createVideoUnit);
                    createVideoUnit.setUnitName("GalleryUnit");
                    createVideoUnit.setVideoScene(this);
                    createVideoUnit.setBorderVisible(false);
                    createVideoUnit.setBackgroundColor(0);
                    createVideoUnit.setUserNameVisible(true);
                    createVideoUnit.setCanShowAudioOff(true);
                    addUnit(createVideoUnit);
                    createVideoUnit.onCreate();
                }
            }
        }
    }

    private void updateGalleryUnits() {
        if (ConfMgr.getInstance().getVideoObj() != null) {
            for (int i = 0; i <= MAX_GALLERY_ITEMS_COUNT; i++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
                RendererUnitInfo createGalleryUnitInfo = createGalleryUnitInfo(i);
                if (videoUnit != null) {
                    videoUnit.updateUnitInfo(createGalleryUnitInfo);
                }
            }
        }
    }

    private RendererUnitInfo createGalleryUnitInfo(int i) {
        int i2;
        int width = getWidth();
        int galleryUnitMargin = getGalleryUnitMargin();
        int galleryUnitWidth = getGalleryUnitWidth();
        int i3 = (galleryUnitWidth * 9) / 16;
        int i4 = this.mCountUsers;
        if (i4 <= MAX_GALLERY_ITEMS_COUNT) {
            i2 = ((((width - (i4 * (galleryUnitWidth + galleryUnitMargin))) - galleryUnitMargin) / 2) + galleryUnitMargin) - this.mGalleryScrollPos;
        } else {
            int i5 = this.mGalleryScrollPos;
            if (i5 < 0) {
                i2 = (-i5) + galleryUnitMargin;
            } else {
                i2 = galleryUnitMargin - (i5 % (galleryUnitWidth + galleryUnitMargin));
            }
        }
        int i6 = i2 + ((galleryUnitMargin + galleryUnitWidth) * i);
        int height = (getHeight() - i3) - UIUtil.dip2px(getConfActivity(), 22.0f);
        if (i == MAX_GALLERY_ITEMS_COUNT && Math.abs(getWidth() - i6) < 3) {
            i6 = getWidth();
        }
        return new RendererUnitInfo(i6, height, galleryUnitWidth, i3);
    }

    private int getGalleryUnitMargin() {
        return UIUtil.dip2px(getConfActivity(), 20.0f);
    }

    private int getGalleryUnitWidth() {
        int width = getWidth();
        int galleryUnitMargin = getGalleryUnitMargin();
        int i = MAX_GALLERY_ITEMS_COUNT;
        return (width - (galleryUnitMargin * (i + 1))) / i;
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
        updateUnitBigVideo();
        updateUnitSmallSingleVideo();
        updateExpandVideoButton();
        updateExpandGalleryButton();
        updateCloseGalleryButton();
        updateSwitchCameraButton();
        updateGalleryUnits();
    }

    private void updateUnitsWithoutResetDestArea() {
        updateUnitShare();
    }

    private void updateUnitBigVideo() {
        if (this.mUnitBigVideo != null) {
            long lockedUserId = getVideoSceneMgr().getLockedUserId();
            if (lockedUserId <= 0) {
                lockedUserId = getVideoSceneMgr().getActiveUserId();
            }
            this.mUnitBigVideo.updateUnitInfo(createBigVideoUnitInfo(lockedUserId > 0));
        }
    }

    private void updateUnitSmallSingleVideo() {
        if (this.mUnitSmallSingleVideo != null) {
            this.mUnitSmallSingleVideo.updateUnitInfo(createSmallSingleVideoUnitInfo());
        }
    }

    private void updateUnitShare() {
        if (this.mUnitShare != null) {
            RendererUnitInfo createShareUnitInfo = createShareUnitInfo();
            if (createShareUnitInfo != null) {
                this.mUnitShare.updateUnitInfo(createShareUnitInfo);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        this.mUnitBigVideo = null;
        this.mUnitSmallSingleVideo = null;
        this.mUnitShare = null;
        this.mHasCachedData = false;
        this.mListGalleryUnits.clear();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mCountUsers = this.mSceneMgr.getTotalVideoCount();
        if (this.mCountUsers < 1) {
            this.mCountUsers = 1;
        }
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeVideoScene.this.updateContentSubscription();
            }
        });
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
            shareUnit.removeUser();
        }
        VideoUnit videoUnit = this.mUnitBigVideo;
        if (videoUnit != null) {
            videoUnit.removeUser();
        }
        this.mShareSize = null;
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
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        if (hasGrantedUnits() && isCreated()) {
            stopAndDestroyAllGrantedUnits();
        }
        super.onGLRendererChanged(videoRenderer, i, i2);
        if (this.mIsMultiTouchZooming) {
            onMultiTouchZoomEnd();
        }
    }

    public void onActiveVideoChanged(long j) {
        if (getVideoSceneMgr().getLockedUserId() == 0 || getVideoSceneMgr().isViewingSharing()) {
            runOnRendererInited(new Runnable() {
                public void run() {
                    LargeVideoScene.this.checkShowVideo();
                }
            });
        }
    }

    public void onUserActiveVideoForDeck(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeVideoScene.this.checkShowVideo();
            }
        });
    }

    public void onGroupUserVideoStatus(List<Long> list) {
        super.onGroupUserVideoStatus(list);
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeVideoScene.this.checkShowVideo();
            }
        });
    }

    public void onUserVideoDataSizeChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeVideoScene.this.checkShowVideo();
            }
        });
    }

    public void onUserVideoQualityChanged(long j) {
        VideoUnit videoUnit = this.mUnitBigVideo;
        if (videoUnit != null && videoUnit.getCanShowNetworkStatus()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.isSameVideo(this.mUnitBigVideo.getUser(), j)) {
                this.mUnitBigVideo.onNetworkStatusChanged();
            }
        }
    }

    public void onGroupUserEvent(int i, @Nullable List<ConfUserInfoEvent> list) {
        this.mIsExchangedMode = false;
        if (i == 1) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && list != null) {
                long lockedUserId = getVideoSceneMgr().getLockedUserId();
                for (ConfUserInfoEvent userId : list) {
                    if (confStatusObj.isSameUser(userId.getUserId(), lockedUserId)) {
                        getVideoSceneMgr().setLockedUserId(0);
                    }
                }
            } else {
                return;
            }
        } else if (i == 2) {
            updateContentSubscription();
        }
        this.mCountUsers = this.mSceneMgr.getTotalVideoCount();
        if (this.mCountUsers <= 2) {
            this.mIsVideoGalleryExpand = false;
        }
        if (this.mCountUsers <= MAX_GALLERY_ITEMS_COUNT) {
            this.mGalleryScrollPos = 0;
        }
        int galleryUnitMargin = getGalleryUnitMargin();
        int galleryUnitWidth = getGalleryUnitWidth();
        int i2 = this.mGalleryScrollPos;
        int i3 = galleryUnitMargin + galleryUnitWidth;
        int i4 = MAX_GALLERY_ITEMS_COUNT;
        int i5 = i2 + (i3 * i4);
        int i6 = this.mCountUsers;
        if (i5 > i6 * i3) {
            this.mGalleryScrollPos = (i6 * i3) - (i3 * i4);
        }
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeVideoScene.this.checkShowVideo();
            }
        });
        if (this.mIsVideoGalleryExpand) {
            onScrollGalleryEnd();
        }
    }

    public void onMyVideoStatusChanged() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            showSwitchCameraButton(videoObj.isVideoStarted());
            checkShowVideo();
        }
    }

    private void showSwitchCameraButton(boolean z) {
        if (!z) {
            GLButton gLButton = this.mGLBtnSwitchCamera;
            if (gLButton != null) {
                gLButton.setVisible(false);
                return;
            }
            return;
        }
        if (this.mGLBtnSwitchCamera == null) {
            createSwitchCameraButton();
        }
        GLButton gLButton2 = this.mGLBtnSwitchCamera;
        if (gLButton2 != null) {
            gLButton2.setVisible(true);
        }
    }

    public void onUserAudioStatus(long j) {
        updateUserAudioStatus(j);
    }

    public void onAudioTypeChanged(long j) {
        updateUserAudioStatus(j);
    }

    private void updateUserAudioStatus(long j) {
        long j2;
        long j3;
        if (ConfMgr.getInstance().getVideoObj() != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                VideoUnit videoUnit = this.mUnitBigVideo;
                if (videoUnit != null) {
                    j2 = videoUnit.getUser();
                    CmmUser userById = ConfMgr.getInstance().getUserById(j2);
                    if (userById != null) {
                        j2 = userById.getNodeId();
                    }
                } else {
                    j2 = 0;
                }
                VideoUnit videoUnit2 = this.mUnitSmallSingleVideo;
                if (videoUnit2 != null) {
                    j3 = videoUnit2.getUser();
                    CmmUser userById2 = ConfMgr.getInstance().getUserById(j3);
                    if (userById2 != null) {
                        j3 = userById2.getNodeId();
                    }
                } else {
                    j3 = 0;
                }
                if (j2 != 0 && confStatusObj.isSameUser(j, j2)) {
                    this.mUnitBigVideo.onUserAudioStatus();
                } else if (j3 == 0 || !confStatusObj.isSameUser(j, j3)) {
                    int i = 0;
                    while (true) {
                        if (i >= this.mListGalleryUnits.size()) {
                            break;
                        }
                        VideoUnit videoUnit3 = (VideoUnit) this.mListGalleryUnits.get(i);
                        if (videoUnit3 != null && videoUnit3.getUser() != 0 && confStatusObj.isSameUser(j, videoUnit3.getUser())) {
                            videoUnit3.onUserAudioStatus();
                            break;
                        }
                        i++;
                    }
                } else {
                    this.mUnitSmallSingleVideo.onUserAudioStatus();
                }
            }
        }
    }

    public void onShareActiveUser(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i != 0) {
            getVideoSceneMgr().setLockedUserId(0);
            this.mIsExchangedMode = false;
        }
        if (i == 0 && this.mCountUsers <= 2) {
            this.mIsVideoGalleryExpand = false;
        }
        if (this.mUnitShare != null || i == 0) {
            runOnRendererInited(new Runnable() {
                public void run() {
                    LargeVideoScene.this.checkShowShare();
                    LargeVideoScene.this.checkShowVideo();
                }
            });
        } else {
            runOnRendererInited(new Runnable() {
                public void run() {
                    LargeVideoScene.this.createUnitShare();
                    LargeVideoScene.this.checkShowShare();
                    LargeVideoScene.this.checkShowVideo();
                }
            });
        }
        updateSharingTitle(0);
        if (i == 0) {
            showWaiting(false);
        }
    }

    private void showWaiting(boolean z) {
        ConfActivity confActivity = getConfActivity();
        View findViewById = confActivity.findViewById(C4558R.C4560id.panelWaitingShare);
        TextView textView = (TextView) findViewById.findViewById(C4558R.C4560id.txtMsgWaitingShare);
        if (z) {
            CmmUser userById = ConfMgr.getInstance().getUserById(getVideoSceneMgr().getShareActiveUserId());
            if (userById != null) {
                String screenName = userById.getScreenName();
                if (screenName.endsWith("s")) {
                    textView.setText(confActivity.getString(C4558R.string.zm_msg_waiting_share_s, new Object[]{screenName}));
                } else {
                    textView.setText(confActivity.getString(C4558R.string.zm_msg_waiting_share, new Object[]{screenName}));
                }
                findViewById.setVisibility(0);
                this.mIsNotWaiting_UnitShare = false;
            }
        } else {
            findViewById.setVisibility(4);
            this.mIsNotWaiting_UnitShare = true;
        }
    }

    public void onShareUserReceivingStatus(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById != null) {
            CmmShareStatus shareStatusObj = userById.getShareStatusObj();
            if (shareStatusObj != null) {
                if (shareStatusObj.getIsReceiving()) {
                    this.mHasCachedData = true;
                    showWaiting(false);
                    stopAndDestroyAllGrantedUnits();
                } else if (!this.mHasCachedData) {
                    showWaiting(true);
                }
                updateSharingTitle(getVideoSceneMgr().getShareActiveUserId());
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
            this.mShareSize = shareObj.getShareDataResolution(j);
            VideoSize videoSize2 = this.mShareSize;
            if (videoSize2 != null && videoSize2.width != 0 && this.mShareSize.height != 0) {
                showWaiting(false);
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
                        this.mScaleWidth = (float) this.mUnitShare.getWidth();
                        this.mScaleHeight = (float) this.mUnitShare.getHeight();
                    } else {
                        this.mScaleWidth = (float) (this.mZoomVal * ((double) this.mShareSize.width));
                        this.mScaleHeight = (float) (this.mZoomVal * ((double) this.mShareSize.height));
                    }
                    notifyDestAreaChanged();
                }
            }
        }
    }

    public void onLaunchConfParamReady() {
        updateContentSubscription();
    }

    public void beforeSwitchCamera() {
        if (this.mUnitBigVideo != null && ConfMgr.getInstance().getConfStatusObj() != null) {
            this.mUnitBigVideo.stopVideo(false);
        }
    }

    public void afterSwitchCamera() {
        if (this.mUnitBigVideo != null && ConfMgr.getInstance().getConfStatusObj() != null) {
            this.mUnitBigVideo.startVideo();
        }
    }

    /* access modifiers changed from: private */
    public void checkShowVideo() {
        long j;
        System.currentTimeMillis();
        if (isCreated() && !hasGrantedUnits()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                CmmUserList userList = ConfMgr.getInstance().getUserList();
                if (userList != null) {
                    boolean z = false;
                    if (ConfMgr.getInstance().isConfConnected()) {
                        long lockedUserId = getVideoSceneMgr().getLockedUserId();
                        if (lockedUserId <= 0) {
                            lockedUserId = getVideoSceneMgr().getActiveUserId();
                        }
                        if (this.mCountUsers == 2) {
                            if (!this.mIsExchangedMode) {
                                CmmUser peerUser = userList.getPeerUser(false, true);
                                if (peerUser != null) {
                                    lockedUserId = peerUser.getNodeId();
                                }
                            } else {
                                lockedUserId = userList.getMyself().getNodeId();
                            }
                        }
                        if (getVideoSceneMgr().isViewingSharing()) {
                            this.mUnitBigVideo.stopVideo(true);
                            this.mUnitBigVideo.removeUser();
                            this.mUnitBigVideo.setBorderVisible(false);
                            this.mUnitBigVideo.setBackgroundColor(0);
                            this.mUnitBigVideo.setUserNameVisible(false, false);
                            this.mUnitBigVideo.setCanShowAudioOff(false);
                        } else if (lockedUserId > 0) {
                            VideoSize userVideoSize = getUserVideoSize(lockedUserId);
                            if (userVideoSize.width == 0 || userVideoSize.height == 0) {
                                userVideoSize = getMyVideoSize();
                            }
                            if (this.mActiveVideoSize == null || !userVideoSize.similarTo(new VideoSize(this.mUnitBigVideo.getWidth(), this.mUnitBigVideo.getHeight()))) {
                                this.mActiveVideoSize = userVideoSize;
                                this.mUnitBigVideo.updateUnitInfo(createBigVideoUnitInfo(true));
                            } else {
                                this.mActiveVideoSize = userVideoSize;
                            }
                            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                            if (confStatusObj != null) {
                                boolean isMyself = confStatusObj.isMyself(lockedUserId);
                                this.mUnitBigVideo.setType(1);
                                this.mUnitBigVideo.setUser(lockedUserId);
                                this.mUnitBigVideo.setBorderVisible(false);
                                this.mUnitBigVideo.setBackgroundColor(-16777216);
                                this.mUnitBigVideo.setUserNameVisible(true, !isMyself);
                                this.mUnitBigVideo.setCanShowAudioOff(true);
                            } else {
                                return;
                            }
                        } else if (videoObj.isVideoStarted()) {
                            CmmUser myself = userList.getMyself();
                            if (myself != null) {
                                if (this.mUnitBigVideo.getUser() != myself.getNodeId()) {
                                    this.mUnitBigVideo.updateUnitInfo(createPreviewVideoUnitInfo());
                                }
                                this.mUnitBigVideo.setType(1);
                                this.mUnitBigVideo.setUser(myself.getNodeId());
                                this.mUnitBigVideo.setBorderVisible(false);
                                this.mUnitBigVideo.setBackgroundColor(-16777216);
                                this.mUnitBigVideo.setUserNameVisible(true, false);
                                this.mUnitBigVideo.setCanShowAudioOff(true);
                            } else {
                                return;
                            }
                        }
                        checkUpdateGalleryUnitsVideo();
                        if (!this.mIsVideoExpand || this.mIsVideoGalleryExpand || this.mCountUsers == 1) {
                            this.mUnitSmallSingleVideo.stopVideo(true);
                            this.mUnitSmallSingleVideo.removeUser();
                            this.mUnitSmallSingleVideo.setBorderVisible(false);
                            this.mUnitSmallSingleVideo.setBackgroundColor(0);
                            this.mUnitSmallSingleVideo.setUserNameVisible(false);
                            this.mUnitSmallSingleVideo.setCanShowAudioOff(false);
                            updateExpandVideoButton();
                            this.mGLBtnExpandVideo.setVisible(this.mCountUsers > 1 && !this.mIsVideoExpand && !this.mIsVideoGalleryExpand);
                            updateExpandGalleryButton();
                            this.mGLBtnExpandGallery.setVisible(this.mCountUsers > 1 && this.mIsVideoExpand);
                            updateCloseGalleryButton();
                            GLButton gLButton = this.mGLBtnCloseGallery;
                            if (this.mCountUsers > 1 && this.mIsVideoExpand) {
                                z = true;
                            }
                            gLButton.setVisible(z);
                        } else {
                            if (!getVideoSceneMgr().isViewingSharing() || lockedUserId <= 0) {
                                if (!this.mIsExchangedMode) {
                                    CmmUser myself2 = userList.getMyself();
                                    if (myself2 != null) {
                                        if (this.mUnitSmallSingleVideo.getUser() != myself2.getNodeId()) {
                                            this.mUnitSmallSingleVideo.updateUnitInfo(createSmallSingleVideoUnitInfo(calcSmallSingleVideoUnitSize(getMyVideoSize())));
                                        }
                                        j = myself2.getNodeId();
                                    } else {
                                        return;
                                    }
                                } else {
                                    CmmUser peerUser2 = userList.getPeerUser(false, true);
                                    j = peerUser2 != null ? peerUser2.getNodeId() : 0;
                                    this.mUnitSmallSingleVideo.updateUnitInfo(createSmallSingleVideoUnitInfo(calcSmallSingleVideoUnitSize(getUserVideoSize(j))));
                                }
                                this.mUnitSmallSingleVideo.setType(0);
                                this.mUnitSmallSingleVideo.setUser(j);
                                this.mUnitSmallSingleVideo.setBorderVisible(true);
                                this.mUnitSmallSingleVideo.setBackgroundColor(-16777216);
                                this.mUnitSmallSingleVideo.setUserNameVisible(true);
                                this.mUnitSmallSingleVideo.setCanShowAudioOff(true);
                                updateExpandGalleryButton();
                                updateCloseGalleryButton();
                                updateExpandVideoButton();
                                this.mGLBtnExpandGallery.setVisible(this.mCountUsers > 2);
                                this.mGLBtnCloseGallery.setVisible(true);
                                this.mGLBtnExpandVideo.setVisible(false);
                            } else {
                                VideoSize userVideoSize2 = getUserVideoSize(lockedUserId);
                                if (userVideoSize2.width == 0 || userVideoSize2.height == 0) {
                                    userVideoSize2 = getMyVideoSize();
                                }
                                VideoSize videoSize = this.mActiveVideoSize;
                                if (videoSize == null || !videoSize.similarTo(userVideoSize2)) {
                                    this.mActiveVideoSize = userVideoSize2;
                                    this.mUnitSmallSingleVideo.updateUnitInfo(createSmallSingleVideoUnitInfo(calcSmallSingleVideoUnitSize(this.mActiveVideoSize)));
                                } else {
                                    this.mActiveVideoSize = userVideoSize2;
                                }
                                this.mUnitSmallSingleVideo.setType(0);
                                this.mUnitSmallSingleVideo.setUser(lockedUserId);
                                this.mUnitSmallSingleVideo.setBorderVisible(true);
                                this.mUnitSmallSingleVideo.setBackgroundColor(-16777216);
                                this.mUnitSmallSingleVideo.setUserNameVisible(true);
                                this.mUnitSmallSingleVideo.setCanShowAudioOff(true);
                                updateExpandGalleryButton();
                                updateCloseGalleryButton();
                                updateExpandVideoButton();
                                this.mGLBtnExpandGallery.setVisible(true);
                                this.mGLBtnCloseGallery.setVisible(true);
                                this.mGLBtnExpandVideo.setVisible(false);
                            }
                            updateSwitchCameraButton();
                        }
                    } else if (this.mUnitBigVideo != null && !videoObj.isPreviewing()) {
                        if (ConfUI.getInstance().isLaunchConfParamReady() && ConfMgr.getInstance().needPreviewVideoWhenStartMeeting() && ConfMgr.getInstance().getConfStatus() != 0 && ConfMgr.getInstance().getConfStatus() != 14) {
                            this.mUnitBigVideo.startPreview(videoObj.getDefaultCameraToUse());
                        }
                        this.mUnitBigVideo.updateUnitInfo(createBigVideoUnitInfo(false));
                    }
                }
            }
        }
    }

    private void checkUpdateGalleryUnitsVideo() {
        long j;
        boolean z;
        long j2;
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            int galleryUnitWidth = getGalleryUnitWidth();
            int galleryUnitMargin = getGalleryUnitMargin();
            long lockedUserId = getVideoSceneMgr().getLockedUserId();
            for (int i = 0; i <= MAX_GALLERY_ITEMS_COUNT; i++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
                RendererUnitInfo createGalleryUnitInfo = createGalleryUnitInfo(i);
                videoUnit.updateUnitInfo(createGalleryUnitInfo);
                if (!this.mIsVideoGalleryExpand || i >= this.mCountUsers) {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    videoUnit.setBorderVisible(false);
                    videoUnit.setBackgroundColor(0);
                } else {
                    int i2 = this.mGalleryScrollPos;
                    if (i2 >= 0) {
                        j = getGalleryUnitUserId((i2 / (galleryUnitWidth + galleryUnitMargin)) + i);
                    } else {
                        j = getGalleryUnitUserId(i);
                    }
                    if (j == 0) {
                        videoUnit.stopVideo(true);
                        videoUnit.removeUser();
                        videoUnit.setBorderVisible(false);
                        videoUnit.setBackgroundColor(0);
                    } else {
                        if (createGalleryUnitInfo.left < ((-createGalleryUnitInfo.width) * 2) / 3 || createGalleryUnitInfo.left + createGalleryUnitInfo.width > getWidth() + ((createGalleryUnitInfo.width * 2) / 3)) {
                            videoUnit.removeUser();
                            z = true;
                        } else {
                            if (this.mIsScrollingGallery) {
                                videoUnit.pause();
                            } else {
                                videoUnit.resume();
                            }
                            videoUnit.setType(0);
                            videoUnit.setUser(j);
                            long activeSpeakerId = getVideoSceneMgr().getActiveSpeakerId();
                            CmmUser userById = ConfMgr.getInstance().getUserById(activeSpeakerId);
                            if (userById != null) {
                                activeSpeakerId = userById.getNodeId();
                                j2 = 0;
                            } else {
                                j2 = 0;
                            }
                            if (lockedUserId != j2 || !confStatusObj.isSameUser(j, activeSpeakerId)) {
                                z = true;
                                if (j == lockedUserId) {
                                    videoUnit.setBorderType(2);
                                } else {
                                    videoUnit.setBorderType(0);
                                }
                            } else {
                                z = true;
                                videoUnit.setBorderType(1);
                            }
                        }
                        videoUnit.setBorderVisible(z);
                        videoUnit.setBackgroundColor(-16777216);
                    }
                }
            }
        }
    }

    private long getGalleryUnitUserId(int i) {
        if (i == 0) {
            return ConfMgr.getInstance().getMyself().getNodeId();
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        CmmUserList userList = ConfMgr.getInstance().getUserList();
        if (confStatusObj == null || userList == null) {
            return 0;
        }
        int i2 = 1;
        for (int i3 = 0; i3 < userList.getUserCount(); i3++) {
            CmmUser userAt = userList.getUserAt(i3);
            if (!userAt.isMMRUser() && !confStatusObj.isMyself(userAt.getNodeId())) {
                if (i2 == i) {
                    return userAt.getNodeId();
                }
                i2++;
            }
        }
        return 0;
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
                if (getLeft() == 0 && confStatusObj != null && !confStatusObj.isSameUser(user, shareActiveUserId)) {
                    this.mHasCachedData = false;
                    showWaiting(true);
                }
                this.mUnitShare.setUser(shareActiveUserId);
                updateSharingTitle(shareActiveUserId);
            } else {
                this.mUnitShare.removeUser();
                this.mUnitShare.clearRenderer();
            }
        }
    }

    private void updateSharingTitle(long j) {
        View findViewById = getConfActivity().findViewById(C4558R.C4560id.panelSharingTitle);
        ConfShareLocalHelper.updateShareTitle(getConfActivity(), j, findViewById);
        if (!isStarted() || !hasContent() || j <= 0) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
        }
    }

    @NonNull
    private RendererUnitInfo createBigVideoUnitInfo(boolean z) {
        if (z) {
            VideoSize videoSize = this.mActiveVideoSize;
            if (videoSize != null) {
                return createBigVideoUnitInfoForVideoSize(videoSize);
            }
        }
        return createPreviewVideoUnitInfo();
    }

    @NonNull
    private VideoSize getMyVideoSize() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            return new VideoSize(16, 9);
        }
        return videoObj.getMyVideoSize();
    }

    private VideoSize calcBigVideoUnitSize(VideoSize videoSize) {
        float f;
        float f2;
        float f3;
        float f4;
        int width = getWidth();
        int height = getHeight();
        int i = videoSize.width;
        int i2 = videoSize.height;
        if (i > i2) {
            f2 = (float) i;
            f = (float) i2;
        } else {
            f2 = (float) i2;
            f = (float) i;
        }
        float f5 = f2 / f;
        if (i > i2) {
            f4 = (float) width;
            f3 = (float) height;
        } else {
            f4 = (float) height;
            f3 = (float) width;
        }
        if (((double) Math.abs(f5 - (f4 / f3))) >= 0.3d) {
            if (videoSize.width * height > videoSize.height * width) {
                height = (videoSize.height * width) / videoSize.width;
            } else {
                width = (videoSize.width * height) / videoSize.height;
            }
        }
        return new VideoSize(width, height);
    }

    private VideoSize calcSmallSingleVideoUnitSize(VideoSize videoSize) {
        int galleryUnitWidth = getGalleryUnitWidth();
        int i = (videoSize.height * galleryUnitWidth) / videoSize.width;
        int i2 = (galleryUnitWidth * 9) / 16;
        if (i > i2) {
            galleryUnitWidth = (videoSize.width * i2) / videoSize.height;
            i = i2;
        }
        return new VideoSize(galleryUnitWidth, i);
    }

    @NonNull
    private RendererUnitInfo createPreviewVideoUnitInfo() {
        return createBigVideoUnitInfoForUnitSize(calcBigVideoUnitSize(getMyVideoSize()));
    }

    private RendererUnitInfo createBigVideoUnitInfoForVideoSize(@NonNull VideoSize videoSize) {
        return createBigVideoUnitInfoForUnitSize(calcBigVideoUnitSize(videoSize));
    }

    private RendererUnitInfo createBigVideoUnitInfoForUnitSize(VideoSize videoSize) {
        return new RendererUnitInfo(getLeft() + ((getWidth() - videoSize.width) / 2), getTop() + ((getHeight() - videoSize.height) / 2), videoSize.width, videoSize.height);
    }

    private RendererUnitInfo createSmallSingleVideoUnitInfo(VideoSize videoSize) {
        return new RendererUnitInfo(getLeft() + ((getWidth() - videoSize.width) - UIUtil.dip2px(getConfActivity(), 22.0f)), getTop() + ((getHeight() - videoSize.height) - UIUtil.dip2px(getConfActivity(), 22.0f)), videoSize.width, videoSize.height);
    }

    @Nullable
    private RendererUnitInfo createShareUnitInfo() {
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null || videoSize.width == 0 || videoSize.height == 0) {
            videoSize = new VideoSize(16, 9);
        }
        return createShareUnitInfo(videoSize);
    }

    private RendererUnitInfo createShareUnitInfo(VideoSize videoSize) {
        int i;
        int i2;
        int i3;
        int i4 = videoSize.width;
        int i5 = videoSize.height;
        if (i4 == 0 || i5 == 0) {
            return null;
        }
        int width = getWidth();
        int height = getHeight();
        int i6 = 0;
        if (!this.mIsFitScreen || Math.abs(this.mZoomVal - getMinLevelZoomValue()) >= 0.01d) {
            double d = (double) i4;
            double d2 = this.mZoomVal;
            float f = (float) (d * d2);
            float f2 = (float) (((double) i5) * d2);
            if (f > ((float) getWidth())) {
                i3 = getWidth();
                i2 = 0;
            } else {
                i3 = (int) f;
                i2 = (width - i3) / 2;
            }
            if (f2 > ((float) getHeight())) {
                i = getHeight();
            } else {
                i = (int) f2;
                i6 = (height - i) / 2;
            }
        } else {
            int i7 = width * i5;
            int i8 = height * i4;
            if (i7 > i8) {
                int i9 = i8 / i5;
                i2 = (width - i9) / 2;
                i3 = i9;
                i = height;
            } else {
                i = i7 / i4;
                i6 = (height - i) / 2;
                i3 = width;
                i2 = 0;
            }
        }
        return new RendererUnitInfo(getLeft() + i2, getTop() + i6, i3, i);
    }

    public void onDown(MotionEvent motionEvent) {
        this.mStopFling_UnitShare = true;
    }

    public void onDoubleTap(@NonNull MotionEvent motionEvent) {
        this.mStopFling_UnitShare = true;
        if (this.mIsNotWaiting_UnitShare) {
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

    public boolean onVideoViewSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        if (this.mIsVideoGalleryExpand && !getVideoSceneMgr().isViewingSharing()) {
            for (int i = MAX_GALLERY_ITEMS_COUNT; i >= 0; i--) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
                if (videoUnit.isBorderVisible() && ((float) videoUnit.getLeft()) <= motionEvent.getX() && ((float) videoUnit.getRight()) > motionEvent.getX() && ((float) videoUnit.getTop()) <= motionEvent.getY() && ((float) videoUnit.getBottom()) > motionEvent.getY()) {
                    return onClickGalleryUnit(videoUnit);
                }
            }
        } else if (this.mIsVideoExpand && !getVideoSceneMgr().isViewingSharing() && this.mCountUsers == 2) {
            VideoUnit videoUnit2 = this.mUnitSmallSingleVideo;
            if (videoUnit2 != null && ((float) videoUnit2.getLeft()) <= motionEvent.getX() && ((float) this.mUnitSmallSingleVideo.getRight()) > motionEvent.getX() && ((float) this.mUnitSmallSingleVideo.getTop()) <= motionEvent.getY() && ((float) this.mUnitSmallSingleVideo.getBottom()) > motionEvent.getY()) {
                return onClickSmallSingleVideoUnit();
            }
        }
        return false;
    }

    private boolean onClickSmallSingleVideoUnit() {
        this.mIsExchangedMode = !this.mIsExchangedMode;
        this.mUnitBigVideo.stopVideo(true);
        this.mUnitBigVideo.removeUser();
        this.mUnitSmallSingleVideo.stopVideo(true);
        this.mUnitSmallSingleVideo.removeUser();
        checkShowVideo();
        return true;
    }

    private boolean onClickGalleryUnit(VideoUnit videoUnit) {
        if (getVideoSceneMgr().getLockedUserId() == videoUnit.getUser()) {
            getVideoSceneMgr().setLockedUserId(0);
        } else {
            getVideoSceneMgr().setLockedUserId(videoUnit.getUser());
        }
        checkShowVideo();
        return true;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (!this.mIsScrollingGallery || motionEvent.getPointerCount() != 1) {
            if (motionEvent.getPointerCount() != 2 || !getVideoSceneMgr().isViewingSharing()) {
                if (this.mIsMultiTouchZooming) {
                    onMultiTouchZoomEnd();
                    return true;
                }
            } else if (motionEvent.getActionMasked() != 1 || !this.mIsMultiTouchZooming) {
                float x = motionEvent.getX(0);
                float y = motionEvent.getY(0);
                float x2 = motionEvent.getX(1);
                float y2 = motionEvent.getY(1);
                if (this.mIsMultiTouchZooming) {
                    onMultiTouchZoom(x, y, x2, y2, this.mLastX1, this.mLastY1, this.mLastX2, this.mLastY2);
                }
                this.mIsMultiTouchZooming = true;
                this.mLastX1 = x;
                this.mLastY1 = y;
                this.mLastX2 = x2;
                this.mLastY2 = y2;
                return false;
            } else {
                onMultiTouchZoomEnd();
                return true;
            }
        } else if (motionEvent.getActionMasked() == 1) {
            onScrollGalleryEnd();
        }
        return false;
    }

    private void onScrollGalleryEnd() {
        VideoUnit videoUnit;
        int galleryUnitMargin = getGalleryUnitMargin();
        this.mIsScrollingGallery = false;
        VideoUnit videoUnit2 = (VideoUnit) this.mListGalleryUnits.get(MAX_GALLERY_ITEMS_COUNT);
        VideoUnit videoUnit3 = (VideoUnit) this.mListGalleryUnits.get(0);
        int i = MAX_GALLERY_ITEMS_COUNT;
        while (true) {
            if (i < 0) {
                videoUnit = null;
                break;
            }
            videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
            if (videoUnit.isBorderVisible()) {
                break;
            }
            i--;
        }
        int width = videoUnit3.getWidth();
        if (videoUnit != null) {
            int right = videoUnit.getRight();
            int i2 = MAX_GALLERY_ITEMS_COUNT;
            if (right < (galleryUnitMargin + width) * i2) {
                scrollGalleryToUnitAt(this.mCountUsers - i2, galleryUnitMargin, width);
                checkUpdateGalleryUnitsVideo();
                updateExpandGalleryButton();
                updateCloseGalleryButton();
                this.mGLBtnExpandGallery.setVisible(true);
                this.mGLBtnCloseGallery.setVisible(true);
                updateSwitchCameraButton();
            }
        }
        if (videoUnit3.getLeft() > galleryUnitMargin) {
            scrollGalleryToUnitAt(0, galleryUnitMargin, width);
        } else if (galleryUnitMargin - videoUnit3.getLeft() > (width * 3) / 4) {
            videoUnit3.stopVideo(true);
            videoUnit3.removeUser();
            scrollGalleryToUnitAt(((videoUnit3.getRight() + galleryUnitMargin) + this.mGalleryScrollPos) / (galleryUnitMargin + width), galleryUnitMargin, width);
            ArrayList<VideoUnit> arrayList = this.mListGalleryUnits;
            arrayList.add(arrayList.remove(0));
        } else {
            videoUnit2.stopVideo(true);
            videoUnit2.removeUser();
            scrollGalleryToUnitAt((videoUnit3.getLeft() + this.mGalleryScrollPos) / (galleryUnitMargin + width), galleryUnitMargin, width);
        }
        checkUpdateGalleryUnitsVideo();
        updateExpandGalleryButton();
        updateCloseGalleryButton();
        this.mGLBtnExpandGallery.setVisible(true);
        this.mGLBtnCloseGallery.setVisible(true);
        updateSwitchCameraButton();
    }

    private void scrollGalleryToUnitAt(int i, int i2, int i3) {
        this.mGalleryScrollPos = i * (i2 + i3);
    }

    private void onMultiTouchZoom(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.mIsMultiTouchZooming = true;
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
        if (this.mZoomVal < getMinLevelZoomValue()) {
            zoomToFitUnit();
        } else if (this.mZoomVal > getMaxLevelZoomValue() && this.mUnitShare != null) {
            switchToLevel(getScaleLevelsCount() - 1, (float) ((this.mUnitShare.getWidth() / 2) + this.mUnitShare.getLeft()), (float) ((this.mUnitShare.getHeight() / 2) + this.mUnitShare.getTop()));
        }
    }

    public boolean canDragSceneToLeft() {
        boolean z = false;
        if (this.mUnitShare != null && this.mHasCachedData) {
            VideoSize videoSize = this.mShareSize;
            if (videoSize != null) {
                if (this.mContentX + ((float) (this.mZoomVal * ((double) videoSize.width))) <= ((float) this.mUnitShare.getWidth())) {
                    z = true;
                }
                return z;
            }
        }
        return false;
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
        int width = getWidth();
        int height = getHeight();
        return (this.mShareSize.height * width > this.mShareSize.width * height ? (((double) height) * ((double) this.mShareSize.width)) / ((double) this.mShareSize.height) : (double) width) / ((double) this.mShareSize.width);
    }

    private int getScaleLevelsCount() {
        VideoSize videoSize = this.mShareSize;
        if (videoSize == null || videoSize.width == 0 || this.mShareSize.height == 0) {
            return 3;
        }
        double maxLevelZoomValue = getMaxLevelZoomValue();
        float f = (float) (((double) this.mShareSize.height) * maxLevelZoomValue);
        if (((float) (((double) this.mShareSize.width) * maxLevelZoomValue)) <= ((float) getWidth()) && f < ((float) getHeight())) {
            return 1;
        }
        double minLevelZoomValue = ((getMinLevelZoomValue() + maxLevelZoomValue) * 2.0d) / 5.0d;
        float f2 = (float) (minLevelZoomValue * ((double) this.mShareSize.height));
        if (((float) (((double) this.mShareSize.width) * minLevelZoomValue)) > ((float) getWidth()) || f2 >= ((float) getHeight())) {
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
            return Math.min(minLevelZoomValue, maxLevelZoomValue);
        }
        if (scaleLevelsCount == 2) {
            return i != 0 ? maxLevelZoomValue : minLevelZoomValue;
        }
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

    public void onScroll(MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
        if (isOnGallery(motionEvent2)) {
            onScrollGallery((int) f);
        } else if (getVideoSceneMgr().isViewingSharing()) {
            onScrollShareUnit(f, f2);
        }
    }

    private void onScrollShareUnit(float f, float f2) {
        this.mStopFling_UnitShare = true;
        if (this.mIsNotWaiting_UnitShare) {
            this.mContentX -= f;
            this.mContentY -= f2;
            trimContentPos();
            notifyDestAreaChanged();
        }
    }

    private void onScrollGallery(int i) {
        if (this.mCountUsers <= MAX_GALLERY_ITEMS_COUNT) {
            if (this.mIsScrollingGallery) {
                onScrollGalleryEnd();
            }
            return;
        }
        int i2 = this.mGalleryScrollPos;
        this.mGalleryScrollPos = i + i2;
        if (this.mGalleryScrollPos < 0) {
            this.mGalleryScrollPos = 0;
        }
        int galleryUnitMargin = (this.mCountUsers - MAX_GALLERY_ITEMS_COUNT) * (getGalleryUnitMargin() + getGalleryUnitWidth());
        if (this.mGalleryScrollPos > galleryUnitMargin) {
            this.mGalleryScrollPos = galleryUnitMargin;
        }
        int i3 = this.mGalleryScrollPos;
        if (i2 != i3) {
            int i4 = i3 - i2;
            int i5 = 0;
            for (int i6 = 0; i6 <= MAX_GALLERY_ITEMS_COUNT; i6++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i6);
                if (videoUnit.getRight() - i4 <= 0) {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    i5++;
                } else if (this.mGalleryScrollPos > 0 && videoUnit.getLeft() - i4 > getWidth()) {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    i5++;
                }
            }
            if (i4 > 0) {
                for (int i7 = 0; i7 < i5; i7++) {
                    ArrayList<VideoUnit> arrayList = this.mListGalleryUnits;
                    arrayList.add(arrayList.remove(0));
                }
            } else {
                for (int i8 = 0; i8 < i5; i8++) {
                    ArrayList<VideoUnit> arrayList2 = this.mListGalleryUnits;
                    arrayList2.add(0, arrayList2.remove(MAX_GALLERY_ITEMS_COUNT));
                }
            }
            checkUpdateGalleryUnitsVideo();
            updateSwitchCameraButton();
            this.mGLBtnExpandGallery.setVisible(false);
            this.mGLBtnCloseGallery.setVisible(false);
            this.mIsScrollingGallery = true;
        }
    }

    private boolean isOnGallery(@NonNull MotionEvent motionEvent) {
        boolean z = false;
        if (!this.mIsVideoGalleryExpand) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(0);
        VideoUnit videoUnit2 = null;
        int i = MAX_GALLERY_ITEMS_COUNT;
        while (true) {
            if (i < 0) {
                break;
            }
            VideoUnit videoUnit3 = (VideoUnit) this.mListGalleryUnits.get(i);
            if (videoUnit3.isBorderVisible()) {
                videoUnit2 = videoUnit3;
                break;
            }
            i--;
        }
        if (videoUnit2 == null) {
            videoUnit2 = videoUnit;
        }
        if (x >= ((float) videoUnit.getLeft()) && x < ((float) videoUnit2.getRight()) && y >= ((float) videoUnit.getTop()) && y < ((float) videoUnit.getBottom())) {
            z = true;
        }
        return z;
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
        if (this.mIsNotWaiting_UnitShare && isCreated() && this.mUnitShare != null) {
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
                this.mStopFling_UnitShare = false;
                handleFling();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFling() {
        this.mFlingHandler.postDelayed(new Runnable() {
            public void run() {
                if (!LargeVideoScene.this.mStopFling_UnitShare && LargeVideoScene.this.updateContentPosOnFling()) {
                    LargeVideoScene.this.handleFling();
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
        if (!z && !z2) {
            z3 = true;
        }
        return z3;
    }

    private void createExpandVideoButton() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            Drawable drawable = getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_expand_video);
            this.mGLBtnExpandVideo = videoObj.createGLButton(createExpandVideoButtonUnitInfo(drawable));
            GLButton gLButton = this.mGLBtnExpandVideo;
            if (gLButton != null) {
                gLButton.setUnitName("ExpandVideo");
                this.mGLBtnExpandVideo.setVideoScene(this);
                addUnit(this.mGLBtnExpandVideo);
                this.mGLBtnExpandVideo.onCreate();
                this.mGLBtnExpandVideo.setVisible(false);
                this.mGLBtnExpandVideo.setBackground(drawable);
                this.mGLBtnExpandVideo.setOnClickListener(this);
            }
        }
    }

    private void updateExpandVideoButton() {
        if (this.mGLBtnExpandVideo != null) {
            this.mGLBtnExpandVideo.updateUnitInfo(createExpandVideoButtonUnitInfo(getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_expand_video)));
        }
    }

    private RendererUnitInfo createExpandVideoButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        if (drawable == null || !UIUtil.isXLargeScreen(getConfActivity())) {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        } else {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        }
        int dip2px = UIUtil.dip2px(getConfActivity(), 12.0f);
        return new RendererUnitInfo(((getLeft() + getWidth()) - i) - dip2px, ((getTop() + getHeight()) - i2) - dip2px, i, i2);
    }

    private void createExpandGalleryButton() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            Drawable drawable = getConfActivity().getResources().getDrawable(this.mIsVideoGalleryExpand ? C4558R.C4559drawable.zm_btn_gallery_collapse : C4558R.C4559drawable.zm_btn_gallery_expand);
            this.mGLBtnExpandGallery = videoObj.createGLButton(createExpandGalleryButtonUnitInfo(drawable));
            GLButton gLButton = this.mGLBtnExpandGallery;
            if (gLButton != null) {
                gLButton.setUnitName("ExpandGallery");
                this.mGLBtnExpandGallery.setVideoScene(this);
                addUnit(this.mGLBtnExpandGallery);
                this.mGLBtnExpandGallery.onCreate();
                this.mGLBtnExpandGallery.setVisible(false);
                this.mGLBtnExpandGallery.setBackground(drawable);
                this.mGLBtnExpandGallery.setOnClickListener(this);
            }
        }
    }

    private void updateExpandGalleryButton() {
        Drawable drawable;
        if (this.mGLBtnExpandGallery != null) {
            if (this.mIsVideoGalleryExpand) {
                if (this.mDrawableGalleryCollapse == null) {
                    this.mDrawableGalleryCollapse = getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_gallery_collapse);
                }
                drawable = this.mDrawableGalleryCollapse;
            } else {
                if (this.mDrawableGalleryExpand == null) {
                    this.mDrawableGalleryExpand = getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_gallery_expand);
                }
                drawable = this.mDrawableGalleryExpand;
            }
            RendererUnitInfo createExpandGalleryButtonUnitInfo = createExpandGalleryButtonUnitInfo(drawable);
            this.mGLBtnExpandGallery.setBackground(drawable);
            this.mGLBtnExpandGallery.updateUnitInfo(createExpandGalleryButtonUnitInfo);
        }
    }

    private RendererUnitInfo createExpandGalleryButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        int i3;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        } else {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        }
        int i4 = 0;
        if (!this.mIsVideoExpand) {
            i3 = 0;
            i4 = Integer.MAX_VALUE;
        } else if (this.mIsVideoGalleryExpand) {
            VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(0);
            if (videoUnit != null) {
                i4 = videoUnit.getLeft() - (i / 2);
                i3 = videoUnit.getTop() - (i2 / 2);
            } else {
                i3 = 0;
            }
        } else {
            VideoUnit videoUnit2 = this.mUnitSmallSingleVideo;
            if (videoUnit2 != null) {
                i4 = videoUnit2.getLeft() - (i / 2);
                i3 = this.mUnitSmallSingleVideo.getTop() - (i2 / 2);
            } else {
                i3 = 0;
            }
        }
        return new RendererUnitInfo(i4, i3, i, i2);
    }

    private void createCloseGalleryButton() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            Drawable drawable = getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_gallery_close);
            this.mGLBtnCloseGallery = videoObj.createGLButton(createCloseGalleryButtonUnitInfo(drawable));
            GLButton gLButton = this.mGLBtnCloseGallery;
            if (gLButton != null) {
                gLButton.setUnitName("CloseGallery");
                this.mGLBtnCloseGallery.setVideoScene(this);
                addUnit(this.mGLBtnCloseGallery);
                this.mGLBtnCloseGallery.onCreate();
                this.mGLBtnCloseGallery.setVisible(false);
                this.mGLBtnCloseGallery.setBackground(drawable);
                this.mGLBtnCloseGallery.setOnClickListener(this);
            }
        }
    }

    private void updateCloseGalleryButton() {
        if (this.mGLBtnCloseGallery != null) {
            this.mGLBtnCloseGallery.updateUnitInfo(createCloseGalleryButtonUnitInfo(getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_gallery_close)));
        }
    }

    private RendererUnitInfo createCloseGalleryButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        int i3;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        } else {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        }
        int i4 = 0;
        if (!this.mIsVideoExpand) {
            i3 = Integer.MAX_VALUE;
        } else if (this.mIsVideoGalleryExpand) {
            VideoUnit lastVisibleGalleryUnit = getLastVisibleGalleryUnit();
            if (lastVisibleGalleryUnit != null) {
                int left = (lastVisibleGalleryUnit.getLeft() + lastVisibleGalleryUnit.getWidth()) - (i / 2);
                i4 = lastVisibleGalleryUnit.getTop() - (i2 / 2);
                i3 = left;
            } else {
                i3 = 0;
            }
        } else {
            i3 = (this.mUnitSmallSingleVideo.getLeft() + this.mUnitSmallSingleVideo.getWidth()) - (i / 2);
            i4 = this.mUnitSmallSingleVideo.getTop() - (i2 / 2);
        }
        return new RendererUnitInfo(i3, i4, i, i2);
    }

    private VideoUnit getLastVisibleGalleryUnit() {
        int i = this.mCountUsers;
        int i2 = MAX_GALLERY_ITEMS_COUNT;
        if (i <= i2) {
            return (VideoUnit) this.mListGalleryUnits.get(i - 1);
        }
        VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i2 - 1);
        if (videoUnit.getLeft() < getWidth()) {
            return videoUnit;
        }
        return (VideoUnit) this.mListGalleryUnits.get(MAX_GALLERY_ITEMS_COUNT - 2);
    }

    private void createSwitchCameraButton() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (getConfActivity() != null && ZMConfComponentMgr.getInstance().canSwitchCamera()) {
            Drawable drawable = getConfActivity().getResources().getDrawable(C4558R.C4559drawable.zm_btn_switch_camera);
            this.mGLBtnSwitchCamera = videoObj.createGLButton(createSwitchCameraButtonUnitInfo(null, drawable));
            GLButton gLButton = this.mGLBtnSwitchCamera;
            if (gLButton != null) {
                gLButton.setUnitName("SwitchCamera");
                this.mGLBtnSwitchCamera.setVideoScene(this);
                addUnit(this.mGLBtnSwitchCamera);
                this.mGLBtnSwitchCamera.onCreate();
                this.mGLBtnSwitchCamera.setBackground(drawable);
                this.mGLBtnSwitchCamera.setOnClickListener(this);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0086  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateSwitchCameraButton() {
        /*
            r6 = this;
            com.zipow.videobox.confapp.GLButton r0 = r6.mGLBtnSwitchCamera
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            com.zipow.videobox.ConfActivity r0 = r6.getConfActivity()
            if (r0 != 0) goto L_0x000c
            return
        L_0x000c:
            com.zipow.videobox.confapp.component.ZMConfComponentMgr r0 = com.zipow.videobox.confapp.component.ZMConfComponentMgr.getInstance()
            boolean r0 = r0.canSwitchCamera()
            if (r0 == 0) goto L_0x008b
            r0 = 0
            com.zipow.videobox.view.video.AbsVideoSceneMgr r1 = r6.getVideoSceneMgr()
            long r1 = r1.getLockedUserId()
            r3 = 0
            r5 = 0
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 != 0) goto L_0x0033
            com.zipow.videobox.confapp.VideoUnit r1 = r6.mUnitBigVideo
            if (r1 == 0) goto L_0x0033
            boolean r1 = r1.isMySelf()
            if (r1 == 0) goto L_0x0033
            com.zipow.videobox.confapp.VideoUnit r0 = r6.mUnitBigVideo
            goto L_0x0051
        L_0x0033:
            com.zipow.videobox.confapp.VideoUnit r1 = r6.mUnitSmallSingleVideo
            if (r1 == 0) goto L_0x0040
            boolean r1 = r1.isMySelf()
            if (r1 == 0) goto L_0x0040
            com.zipow.videobox.confapp.VideoUnit r0 = r6.mUnitSmallSingleVideo
            goto L_0x0051
        L_0x0040:
            java.util.ArrayList<com.zipow.videobox.confapp.VideoUnit> r1 = r6.mListGalleryUnits
            java.lang.Object r1 = r1.get(r5)
            com.zipow.videobox.confapp.VideoUnit r1 = (com.zipow.videobox.confapp.VideoUnit) r1
            if (r1 == 0) goto L_0x0051
            boolean r2 = r1.isMySelf()
            if (r2 == 0) goto L_0x0051
            r0 = r1
        L_0x0051:
            if (r0 == 0) goto L_0x0086
            com.zipow.videobox.confapp.GLButton r1 = r6.mGLBtnSwitchCamera
            r2 = 1
            r1.setVisible(r2)
            com.zipow.videobox.confapp.RendererUnitInfo r1 = new com.zipow.videobox.confapp.RendererUnitInfo
            int r2 = r0.getLeft()
            int r3 = r0.getTop()
            int r4 = r0.getWidth()
            int r0 = r0.getHeight()
            r1.<init>(r2, r3, r4, r0)
            com.zipow.videobox.ConfActivity r0 = r6.getConfActivity()
            android.content.res.Resources r0 = r0.getResources()
            int r2 = p021us.zoom.videomeetings.C4558R.C4559drawable.zm_btn_switch_camera
            android.graphics.drawable.Drawable r0 = r0.getDrawable(r2)
            com.zipow.videobox.confapp.RendererUnitInfo r0 = r6.createSwitchCameraButtonUnitInfo(r1, r0)
            com.zipow.videobox.confapp.GLButton r1 = r6.mGLBtnSwitchCamera
            r1.updateUnitInfo(r0)
            goto L_0x008b
        L_0x0086:
            com.zipow.videobox.confapp.GLButton r0 = r6.mGLBtnSwitchCamera
            r0.setVisible(r5)
        L_0x008b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.video.LargeVideoScene.updateSwitchCameraButton():void");
    }

    @NonNull
    private RendererUnitInfo createSwitchCameraButtonUnitInfo(@Nullable RendererUnitInfo rendererUnitInfo, @Nullable Drawable drawable) {
        int i;
        int i2;
        int i3;
        int i4;
        if (drawable == null) {
            GLButton gLButton = this.mGLBtnSwitchCamera;
            if (gLButton != null) {
                drawable = gLButton.getBackgroundDrawable();
            }
        }
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        } else {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        }
        int dip2px = UIUtil.dip2px(getConfActivity(), 12.0f);
        if (rendererUnitInfo == null) {
            i4 = 0;
            i3 = Integer.MAX_VALUE;
        } else {
            i3 = ((rendererUnitInfo.left + rendererUnitInfo.width) - i) - dip2px;
            i4 = dip2px + rendererUnitInfo.top;
            if (getVideoSceneMgr().getLockedUserId() == 0) {
                VideoUnit videoUnit = this.mUnitBigVideo;
                if (videoUnit != null && videoUnit.isMySelf()) {
                    i4 += getConfActivity().getToolbarHeight();
                }
            }
        }
        return new RendererUnitInfo(i3, i4, i, i2);
    }

    public void onClick(GLButton gLButton) {
        if (gLButton == this.mGLBtnCloseGallery) {
            this.mIsVideoGalleryExpand = false;
            this.mIsVideoExpand = false;
            this.mGalleryScrollPos = 0;
            checkShowVideo();
        } else if (gLButton == this.mGLBtnExpandGallery) {
            this.mIsVideoGalleryExpand = !this.mIsVideoGalleryExpand;
            this.mGalleryScrollPos = 0;
            checkShowVideo();
        } else if (gLButton == this.mGLBtnExpandVideo) {
            this.mIsVideoGalleryExpand = false;
            this.mIsVideoExpand = true;
            this.mGalleryScrollPos = 0;
            checkShowVideo();
        } else if (gLButton == this.mGLBtnSwitchCamera) {
            ZMConfComponentMgr.getInstance().onClickSwitchCamera();
        }
    }
}
