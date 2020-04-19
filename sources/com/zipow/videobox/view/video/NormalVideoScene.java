package com.zipow.videobox.view.video;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoSize;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.GLImage;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class NormalVideoScene extends AbsVideoScene implements OnClickListener, IHeadsetConnectionListener {
    private static final int INDEX_ACCESSIBILITY_VIEW_ACTIVEVIDEO = 0;
    private static final int INDEX_ACCESSIBILITY_VIEW_PREVIEW = 1;
    private static final String KEY_UPDATE_CHECK_SHOW_ACTIVE_VIDEO = "checkShowActiveVideo";
    private static final String KEY_UPDATE_CONTENT_SUBSCRIPTION = "updateContentSubscription";
    private static final String KEY_UPDATE_UNITS = "updateUnits";
    public static final int MAX_SWITCH_SCENE_BUTTONS = 10;
    private static final String TAG = "NormalVideoScene";
    /* access modifiers changed from: private */
    public VideoSize mActiveVideoSize;
    private boolean mCanStartPreview = true;
    @NonNull
    private HashMap<String, String> mFlagRecords = new HashMap<>();
    @Nullable
    private GLImage mGLImageWaterMark;
    /* access modifiers changed from: private */
    public boolean mIsExchangedMode = false;
    private ImageButton[] mSwitchSceneButtons;
    @Nullable
    private VideoUnit mUnitActiveVideo;
    @Nullable
    private VideoUnit mUnitPreview;
    /* access modifiers changed from: private */
    public int mUserCount = 1;
    private int mWaterMarkHeight = 0;
    private int mWaterMarkWidth = 0;

    private void createSwitchCameraButton(VideoSessionMgr videoSessionMgr) {
    }

    public void onBluetoothScoAudioStatus(boolean z) {
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
    }

    public NormalVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
    }

    public long getPreviewRenderInfo() {
        VideoUnit myVideoUnit = getMyVideoUnit();
        if (myVideoUnit != null) {
            return myVideoUnit.getRendererInfo();
        }
        return 0;
    }

    public void onActiveVideoChanged(long j) {
        if (!isVideoPaused()) {
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null) {
                userById.getScreenName();
            }
            runOnRendererInited(new Runnable() {
                public void run() {
                    NormalVideoScene.this.checkShowActiveVideo();
                }
            });
        }
    }

    public void onConfReady() {
        checkUpdateVideoUnitsForViewOnlyMeeting();
        updateAccessibilitySceneDescription();
        getVideoSceneMgr().announceAccessibilityAtActiveScene();
    }

    private void checkUpdateVideoUnitsForViewOnlyMeeting() {
        if (ConfLocalHelper.isViewOnlyButNotSpeakAttendee()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.isPreviewing()) {
                VideoUnit videoUnit = this.mUnitActiveVideo;
                if (videoUnit != null) {
                    videoObj.stopPreviewDevice(videoUnit.getRendererInfo());
                }
            }
            removePreviewUnit();
        }
    }

    private void removePreviewUnit() {
        VideoUnit videoUnit = this.mUnitPreview;
        if (videoUnit != null) {
            videoUnit.removeUser();
            this.mUnitPreview.onDestroy();
            removeUnit(this.mUnitPreview);
            this.mUnitPreview = null;
            onUpdateUnits();
        }
    }

    public void onGroupUserVideoStatus(@Nullable List<Long> list) {
        final ArrayList arrayList;
        super.onGroupUserVideoStatus(list);
        if (!isPreloadStatus()) {
            if (list == null) {
                arrayList = new ArrayList();
            } else {
                arrayList = new ArrayList(list);
            }
            runOnRendererInited(new Runnable() {
                public void run() {
                    NormalVideoScene.this.checkUpdateGroupUserVideo(arrayList);
                }
            });
        }
    }

    public void onUserVideoDataSizeChanged(final long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                NormalVideoScene.this.checkUpdateUserVideo(j);
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
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || !confStatusObj.isMyself(j)) {
            updateUserAudioStatus(j);
        } else {
            updateUserAudioStatus(j);
        }
    }

    public void onAudioTypeChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            updateUserAudioStatus(j);
        }
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
            VideoUnit videoUnit2 = this.mUnitPreview;
            if (!(videoUnit2 == null || videoUnit2.getUser() == 0 || !confStatusObj.isSameUser(j, this.mUnitPreview.getUser()))) {
                this.mUnitPreview.onUserAudioStatus();
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
            VideoUnit videoUnit2 = this.mUnitPreview;
            if (!(videoUnit2 == null || videoUnit2.getUser() == 0 || !confStatusObj.isSameUser(j, this.mUnitPreview.getUser()))) {
                this.mUnitPreview.updateAvatar();
            }
        }
    }

    public void onAutoStartVideo() {
        runOnRendererInited(new Runnable() {
            public void run() {
                NormalVideoScene.this.showMyVideo();
            }
        });
        updateSwitchScenePanel();
    }

    public void onConfOne2One() {
        runOnRendererInited(new Runnable() {
            public void run() {
                NormalVideoScene.this.startOne2One();
            }
        });
    }

    public void onMyVideoStatusChanged() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && !videoObj.isPreviewing()) {
            if (this.mUserCount == 1) {
                this.mActiveVideoSize = getMyVideoSize();
            }
            showMyVideo();
        }
    }

    public void onUserCountChangesForShowHideAction() {
        if (!isPreloadStatus()) {
            updateSwitchScenePanel();
        }
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        if (!isPreloadStatus()) {
            this.mUserCount = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false);
            switch (i) {
                case 0:
                    if (this.mUserCount >= 2 && !this.mFlagRecords.containsKey(KEY_UPDATE_UNITS)) {
                        this.mFlagRecords.put(KEY_UPDATE_UNITS, KEY_UPDATE_UNITS);
                        updateUnits();
                    } else if (this.mUserCount < 2) {
                        this.mFlagRecords.remove(KEY_UPDATE_UNITS);
                    }
                    updateSwitchScenePanel();
                    break;
                case 1:
                    if (this.mUserCount < 2) {
                        if (this.mIsExchangedMode) {
                            exchangeVideoUnits();
                        }
                        this.mActiveVideoSize = getMyVideoSize();
                        updateContentSubscription();
                        this.mFlagRecords.remove(KEY_UPDATE_UNITS);
                    } else if (!this.mFlagRecords.containsKey(KEY_UPDATE_CONTENT_SUBSCRIPTION)) {
                        this.mFlagRecords.put(KEY_UPDATE_CONTENT_SUBSCRIPTION, KEY_UPDATE_CONTENT_SUBSCRIPTION);
                        updateContentSubscription();
                    }
                    updateSwitchScenePanel();
                    break;
                case 2:
                    updateContentSubscription();
                    break;
            }
        }
    }

    public void onLaunchConfParamReady() {
        updateContentSubscription();
    }

    public void onConfVideoSendingStatusChanged() {
        if (ConfMgr.getInstance().noOneIsSendingVideo() && this.mIsExchangedMode) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                if (!(!confContext.isAudioOnlyMeeting() && !confContext.isShareOnlyMeeting())) {
                    exchangeVideoUnits();
                    return;
                }
            } else {
                return;
            }
        }
        updateContentSubscription();
    }

    public void beforeSwitchCamera() {
        VideoUnit myVideoUnit = getMyVideoUnit();
        if (myVideoUnit != null) {
            myVideoUnit.stopVideo(false);
        }
    }

    public void afterSwitchCamera() {
        VideoUnit myVideoUnit = getMyVideoUnit();
        if (myVideoUnit != null) {
            myVideoUnit.startVideo();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mUserCount = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false);
        if (this.mUserCount < 1) {
            this.mUserCount = 1;
        }
        runOnRendererInited(new Runnable() {
            public void run() {
                if (NormalVideoScene.this.mUserCount < 2) {
                    if (NormalVideoScene.this.mIsExchangedMode) {
                        NormalVideoScene.this.exchangeVideoUnits();
                    }
                    NormalVideoScene normalVideoScene = NormalVideoScene.this;
                    normalVideoScene.mActiveVideoSize = normalVideoScene.getMyVideoSize();
                }
                NormalVideoScene.this.updateContentSubscription();
            }
        });
        if (isVisible()) {
            updateSwitchScenePanel();
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            updateUserAudioStatus(myself.getNodeId());
        }
        HeadsetUtil.getInstance().addListener(this);
    }

    public void updateContentSubscription() {
        if (!isPreloadStatus()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj == null || !videoObj.isPreviewing()) {
                showMyVideo();
                checkShowActiveVideo();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
        updateContentSubscription();
        updateSwitchScenePanel();
        removeFadeView();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (ConfMgr.getInstance().getVideoObj() != null) {
            VideoUnit videoUnit = this.mUnitActiveVideo;
            if (videoUnit != null && hasUnit(videoUnit)) {
                this.mUnitActiveVideo.removeUser();
            }
            VideoUnit videoUnit2 = this.mUnitPreview;
            if (videoUnit2 != null && hasUnit(videoUnit2)) {
                this.mUnitPreview.removeUser();
            }
            HeadsetUtil.getInstance().removeListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            if (!this.mIsExchangedMode) {
                createActiveVideoUnit(videoObj);
                if (!ConfMgr.getInstance().isViewOnlyMeeting()) {
                    createPreviewVideoUnit(videoObj);
                }
            } else {
                createPreviewVideoUnit(videoObj);
                createActiveVideoUnit(videoObj);
            }
            createSwitchCameraButton(videoObj);
            if (isVisible()) {
                positionSwitchScenePanel();
                updateAccessibilitySceneDescription();
                getVideoSceneMgr().announceAccessibilityAtActiveScene();
            }
            createGLImageWaterMark();
            updateMainVideoFlag();
        }
    }

    public void updateAccessibilitySceneDescription() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_connecting));
            return;
        }
        if (getConfActivity() != null) {
            if (getConfActivity().isToolbarShowing()) {
                getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_normal_toolbar_showed));
            } else {
                getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_normal_toolbar_hided));
            }
        }
    }

    @NonNull
    public Rect getBoundsForAccessbilityViewIndex(int i) {
        switch (i) {
            case 0:
                VideoUnit videoUnit = this.mUnitActiveVideo;
                if (videoUnit != null) {
                    return new Rect(videoUnit.getLeft(), this.mUnitActiveVideo.getTop(), this.mUnitActiveVideo.getRight(), this.mUnitActiveVideo.getBottom());
                }
                break;
            case 1:
                VideoUnit videoUnit2 = this.mUnitPreview;
                if (videoUnit2 != null) {
                    return new Rect(videoUnit2.getLeft(), this.mUnitPreview.getTop(), this.mUnitPreview.getRight(), this.mUnitPreview.getBottom());
                }
                break;
        }
        return new Rect();
    }

    public int getAccessbilityViewIndexAt(float f, float f2) {
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null && videoUnit.isPointInUnit(f, f2)) {
            return 0;
        }
        VideoUnit videoUnit2 = this.mUnitPreview;
        return (videoUnit2 == null || !videoUnit2.isPointInUnit(f, f2)) ? -1 : 1;
    }

    @NonNull
    public CharSequence getAccessibilityDescriptionForIndex(int i) {
        StringBuilder sb = new StringBuilder();
        switch (i) {
            case 0:
                VideoUnit videoUnit = this.mUnitActiveVideo;
                if (videoUnit != null) {
                    sb.append(videoUnit.getAccessibilityDescription());
                    break;
                }
                break;
            case 1:
                if (this.mUnitPreview != null) {
                    CmmUser userById = ConfMgr.getInstance().getUserById(this.mUnitPreview.getUser());
                    if (userById != null) {
                        sb.append(userById.getScreenName());
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                        sb.append(this.mUnitPreview.getMeetingReactionAccTxt());
                        break;
                    }
                }
                break;
        }
        return sb.toString();
    }

    public void getAccessibilityVisibleVirtualViews(@NonNull List<Integer> list) {
        if (this.mUnitActiveVideo != null) {
            list.add(Integer.valueOf(0));
        }
        if (this.mUnitPreview != null) {
            list.add(Integer.valueOf(1));
        }
    }

    private void createPreviewVideoUnit(@NonNull VideoSessionMgr videoSessionMgr) {
        boolean z = false;
        this.mUnitPreview = videoSessionMgr.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createPreviewVideoUnitInfo());
        VideoUnit videoUnit = this.mUnitPreview;
        if (videoUnit != null) {
            videoUnit.setUnitName("MyPreview");
            this.mUnitPreview.setVideoScene(this);
            this.mUnitPreview.setUserNameVisible(isShowUserName(this.mIsExchangedMode), false);
            this.mUnitPreview.setBorderVisible(false);
            this.mUnitPreview.setBackgroundColor((this.mUserCount <= 1 || this.mIsExchangedMode) ? 0 : -16777216);
            VideoUnit videoUnit2 = this.mUnitPreview;
            videoUnit2.setCanShowAudioOff(!this.mIsExchangedMode || videoUnit2.isUserNameVisible());
            VideoUnit videoUnit3 = this.mUnitPreview;
            if (this.mIsExchangedMode && isShowWaterMarkIfCan()) {
                z = true;
            }
            videoUnit3.setCanShowWaterMark(z);
            videoSessionMgr.setAspectMode(this.mUnitPreview.getRendererInfo(), 2);
            addUnit(this.mUnitPreview);
            this.mUnitPreview.onCreate();
        }
    }

    private void createActiveVideoUnit(@NonNull VideoSessionMgr videoSessionMgr) {
        boolean z = true;
        this.mUnitActiveVideo = videoSessionMgr.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createActiveVideoUnitInfo(getVideoSceneMgr().getActiveUserId() > 0));
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            videoUnit.setUnitName("ActiveVideo");
            this.mUnitActiveVideo.setVideoScene(this);
            boolean isShowUserName = isShowUserName(!this.mIsExchangedMode);
            this.mUnitActiveVideo.setUserNameVisible(isShowUserName, isShowUserName && this.mUserCount > 1);
            this.mUnitActiveVideo.setBorderVisible(false);
            this.mUnitActiveVideo.setBackgroundColor(this.mIsExchangedMode ? -16777216 : 0);
            VideoUnit videoUnit2 = this.mUnitActiveVideo;
            videoUnit2.setCanShowAudioOff(this.mIsExchangedMode || videoUnit2.isUserNameVisible());
            VideoUnit videoUnit3 = this.mUnitActiveVideo;
            if (this.mIsExchangedMode || !isShowWaterMarkIfCan()) {
                z = false;
            }
            videoUnit3.setCanShowWaterMark(z);
            videoSessionMgr.setAspectMode(this.mUnitActiveVideo.getRendererInfo(), 2);
            addUnit(this.mUnitActiveVideo);
            this.mUnitActiveVideo.onCreate();
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateUnits() {
        boolean z = false;
        if (this.mUnitActiveVideo != null) {
            this.mUnitActiveVideo.updateUnitInfo(createActiveVideoUnitInfo(getVideoSceneMgr().getActiveUserId() > 0));
            this.mUnitActiveVideo.setCanShowWaterMark(!this.mIsExchangedMode && isShowWaterMarkIfCan());
            boolean isShowUserName = isShowUserName(!this.mIsExchangedMode);
            this.mUnitActiveVideo.setUserNameVisible(isShowUserName, isShowUserName(isShowUserName) && this.mUserCount > 1);
            VideoUnit videoUnit = this.mUnitActiveVideo;
            videoUnit.setCanShowAudioOff(this.mIsExchangedMode || videoUnit.isUserNameVisible());
        }
        if (this.mUnitPreview != null) {
            this.mUnitPreview.updateUnitInfo(createPreviewVideoUnitInfo());
            this.mUnitPreview.setCanShowWaterMark(this.mIsExchangedMode && isShowWaterMarkIfCan());
            this.mUnitPreview.setUserNameVisible(isShowUserName(this.mIsExchangedMode), false);
            VideoUnit videoUnit2 = this.mUnitPreview;
            if (!this.mIsExchangedMode || videoUnit2.isUserNameVisible()) {
                z = true;
            }
            videoUnit2.setCanShowAudioOff(z);
        }
        if (isVisible()) {
            positionSwitchScenePanel();
            updateAccessibilitySceneDescription();
        }
        updateGLImageWaterMark();
        updateMainVideoFlag();
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        this.mUnitActiveVideo = null;
        this.mUnitPreview = null;
        this.mGLImageWaterMark = null;
        this.mWaterMarkWidth = 0;
        this.mWaterMarkHeight = 0;
    }

    public boolean onVideoViewSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        if (!ConfMgr.getInstance().isConfConnected() || ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) < 2) {
            return false;
        }
        if (!isInTargetRange(motionEvent, this.mIsExchangedMode ? this.mUnitActiveVideo : this.mUnitPreview)) {
            return false;
        }
        exchangeVideoUnits();
        return true;
    }

    private boolean isOtherSharing() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        boolean z = false;
        if (shareObj == null) {
            return false;
        }
        if (shareObj.getShareStatus() == 3) {
            z = true;
        }
        return z;
    }

    public void onDoubleTap(MotionEvent motionEvent) {
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && videoObj.isManualMode() && videoSceneMgr.unPinVideo() && !ConfMgr.getInstance().isViewOnlyMeeting()) {
            if (isOtherSharing()) {
                pauseVideo();
            }
            showUnPinVideoAnimation();
            Toast makeText = Toast.makeText(getConfActivity(), C4558R.string.zm_msg_doubletap_leave_pinvideo, 0);
            makeText.setGravity(17, 0, 0);
            makeText.show();
        }
    }

    private void showUnPinVideoAnimation() {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            final ImageView imageView = (ImageView) confActivity.findViewById(C4558R.C4560id.fadeview);
            final ImageView imageView2 = (ImageView) confActivity.findViewById(C4558R.C4560id.fadeview1);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 1, 0.0f, 1, 0.5f);
            ScaleAnimation scaleAnimation2 = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 1, 1.0f, 1, 0.5f);
            scaleAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(8);
                    imageView2.setVisibility(8);
                }
            });
            scaleAnimation.setDuration(1000);
            scaleAnimation2.setDuration(1000);
            scaleAnimation.setRepeatCount(1);
            scaleAnimation2.setRepeatCount(1);
            scaleAnimation.setRepeatMode(2);
            scaleAnimation2.setRepeatMode(2);
            imageView.setVisibility(0);
            imageView2.setVisibility(0);
            imageView.startAnimation(scaleAnimation);
            imageView2.startAnimation(scaleAnimation2);
        }
    }

    public void onMyVideoRotationChanged(int i) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && videoObj.isPreviewing()) {
            VideoUnit videoUnit = this.mUnitActiveVideo;
            if (videoUnit != null) {
                videoObj.rotateDevice(i, videoUnit.getRendererInfo());
            }
        }
        long activeUserId = getVideoSceneMgr().getActiveUserId();
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                if (confStatusObj.isSameUser(activeUserId, myself.getNodeId())) {
                    this.mActiveVideoSize = ConfMgr.getInstance().getVideoObj().getMyVideoSize();
                }
                updateUnits();
            }
        }
    }

    public void onNetworkRestrictionModeChanged(boolean z) {
        if (isVisible()) {
            updateSwitchScenePanel();
            VideoUnit activeVideoUnit = getActiveVideoUnit();
            if (activeVideoUnit != null && activeVideoUnit.getType() == 1) {
                activeVideoUnit.setNetworkRestrictionMode(z, true);
            }
        }
    }

    private void updateMainVideoFlag() {
        VideoUnit videoUnit = this.mUnitActiveVideo;
        if (videoUnit != null) {
            videoUnit.setMainVideo(!this.mIsExchangedMode);
        }
        VideoUnit videoUnit2 = this.mUnitPreview;
        if (videoUnit2 != null) {
            videoUnit2.setMainVideo(this.mIsExchangedMode);
        }
    }

    /* access modifiers changed from: private */
    public void exchangeVideoUnits() {
        this.mUserCount = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false);
        setExchangedMode(!this.mIsExchangedMode);
    }

    /* access modifiers changed from: private */
    public void showMyVideo() {
        boolean z;
        if (isCreated()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && !ConfMgr.getInstance().isViewOnlyMeeting()) {
                boolean z2 = true;
                if (ConfMgr.getInstance().isConfConnected()) {
                    VideoUnit myVideoUnit = getMyVideoUnit();
                    if (myVideoUnit != null) {
                        if (myVideoUnit == this.mUnitActiveVideo) {
                            this.mUnitActiveVideo.updateUnitInfo(createBigPreviewVideoUnitInfo());
                            z = !this.mIsExchangedMode;
                            VideoUnit videoUnit = this.mUnitPreview;
                            if (videoUnit != null) {
                                videoUnit.stopVideo(true);
                                this.mUnitPreview.removeUser();
                                this.mUnitPreview.setBorderVisible(false);
                                this.mUnitPreview.setBackgroundColor(0);
                            }
                        } else {
                            myVideoUnit.updateUnitInfo(this.mIsExchangedMode ? createBigPreviewVideoUnitInfo() : createPreviewVideoUnitInfo());
                            z = this.mIsExchangedMode;
                        }
                        ConfMgr instance = ConfMgr.getInstance();
                        CmmUserList userList = instance.getUserList();
                        if (userList != null) {
                            CmmUser myself = userList.getMyself();
                            if (myself != null) {
                                CmmConfContext confContext = instance.getConfContext();
                                if (confContext != null) {
                                    boolean z3 = !confContext.isAudioOnlyMeeting() && !confContext.isShareOnlyMeeting();
                                    boolean noOneIsSendingVideo = instance.noOneIsSendingVideo();
                                    videoObj.setAspectMode(myVideoUnit.getRendererInfo(), 2);
                                    if (z3 || !noOneIsSendingVideo || this.mUserCount < 2) {
                                        myVideoUnit.setType(z ? 1 : 0);
                                        myVideoUnit.setUser(myself.getNodeId());
                                        myVideoUnit.setBorderVisible(myVideoUnit == this.mUnitPreview && !this.mIsExchangedMode);
                                        myVideoUnit.setBackgroundColor((myVideoUnit != this.mUnitPreview || this.mIsExchangedMode) ? 0 : -16777216);
                                        myVideoUnit.setCanShowWaterMark(((myVideoUnit == this.mUnitPreview && this.mIsExchangedMode) || (myVideoUnit == this.mUnitActiveVideo && !this.mIsExchangedMode)) && isShowWaterMarkIfCan());
                                        myVideoUnit.setUserNameVisible(((myVideoUnit == this.mUnitPreview && this.mIsExchangedMode) || (myVideoUnit == this.mUnitActiveVideo && !this.mIsExchangedMode)) && !getConfActivity().isToolbarShowing() && !ConfMgr.getInstance().isCallingOut(), false);
                                        if (!myVideoUnit.isUserNameVisible() && (myVideoUnit != this.mUnitPreview || this.mIsExchangedMode)) {
                                            z2 = false;
                                        }
                                        myVideoUnit.setCanShowAudioOff(z2);
                                    } else {
                                        myVideoUnit.stopVideo(true);
                                        myVideoUnit.removeUser();
                                        myVideoUnit.setBorderVisible(false);
                                        myVideoUnit.setBackgroundColor(0);
                                    }
                                }
                            }
                        }
                    }
                } else if (this.mUnitActiveVideo != null && !videoObj.isPreviewing()) {
                    this.mUnitActiveVideo.setCanShowWaterMark(false);
                    this.mUnitActiveVideo.setUserNameVisible(false, false);
                    if (!this.mSceneMgr.getmVideoBoxApplication().isSDKMode()) {
                        if (ConfUI.getInstance().isLaunchConfParamReady() && ConfMgr.getInstance().needPreviewVideoWhenStartMeeting() && ConfMgr.getInstance().getConfStatus() != 0 && ConfMgr.getInstance().getConfStatus() != 14) {
                            this.mUnitActiveVideo.startPreview(videoObj.getDefaultCameraToUse());
                        }
                    } else if (this.mCanStartPreview && ConfUI.getInstance().isLaunchConfParamReady()) {
                        CmmConfContext confContext2 = ConfMgr.getInstance().getConfContext();
                        if (confContext2 != null) {
                            boolean z4 = ConfMgr.getInstance().needPreviewVideoWhenStartMeeting() && confContext2.getLaunchReason() == 1;
                            if (confContext2.isAudioOnlyMeeting() || confContext2.isShareOnlyMeeting() || confContext2.isDirectShareClient() || !confContext2.isVideoOn()) {
                                z2 = false;
                            }
                            if (z4 || z2) {
                                this.mUnitActiveVideo.startPreview(videoObj.getDefaultCameraToUse());
                            }
                        } else {
                            return;
                        }
                    }
                    this.mUnitActiveVideo.updateUnitInfo(createBigPreviewVideoUnitInfo());
                }
            }
        }
    }

    @Nullable
    private VideoUnit getMyVideoUnit() {
        return ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) >= 2 ? this.mUnitPreview : this.mUnitActiveVideo;
    }

    @Nullable
    private VideoUnit getActiveVideoUnit() {
        if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) >= 2) {
            return this.mUnitActiveVideo;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void checkShowActiveVideo() {
        long j;
        if (isCreated()) {
            CmmUserList userList = ConfMgr.getInstance().getUserList();
            if (userList != null) {
                boolean z = true;
                int clientWithoutOnHoldUserCount = ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true);
                if (!ConfMgr.getInstance().isViewOnlyMeeting()) {
                    j = getVideoSceneMgr().getActiveUserId();
                    if (clientWithoutOnHoldUserCount != 1) {
                        if (clientWithoutOnHoldUserCount == 2) {
                            CmmUser peerUser = userList.getPeerUser(false, true);
                            if (peerUser != null) {
                                j = peerUser.getNodeId();
                            }
                        }
                    } else {
                        return;
                    }
                } else if (getVideoSceneMgr().getActiveUserId() != 0) {
                    j = 1;
                } else {
                    return;
                }
                boolean isNetworkRestrictionMode = getVideoSceneMgr().isNetworkRestrictionMode();
                if (j > 0) {
                    int i = -16777216;
                    if (this.mUnitActiveVideo != null) {
                        VideoSize userVideoSize = getUserVideoSize(j);
                        VideoSize videoSize = this.mActiveVideoSize;
                        if (videoSize == null || !videoSize.similarTo(userVideoSize)) {
                            this.mActiveVideoSize = userVideoSize;
                            this.mUnitActiveVideo.updateUnitInfo(createActiveVideoUnitInfo(true));
                        } else {
                            this.mActiveVideoSize = userVideoSize;
                        }
                        if (!this.mIsExchangedMode) {
                            this.mUnitActiveVideo.setNetworkRestrictionMode(isNetworkRestrictionMode, false);
                            this.mUnitActiveVideo.setType(1);
                            this.mUnitActiveVideo.setIsFloating(false);
                        } else {
                            this.mUnitActiveVideo.setType(0);
                            this.mUnitActiveVideo.setIsFloating(true);
                        }
                        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                        if (clientWithoutOnHoldUserCount >= 2 && !this.mFlagRecords.containsKey(KEY_UPDATE_CHECK_SHOW_ACTIVE_VIDEO)) {
                            this.mUnitActiveVideo.setUser(j);
                            this.mFlagRecords.put(KEY_UPDATE_CHECK_SHOW_ACTIVE_VIDEO, KEY_UPDATE_CHECK_SHOW_ACTIVE_VIDEO);
                        } else if (videoObj == null || !videoObj.isManualMode()) {
                            this.mUnitActiveVideo.setUser(1);
                        } else {
                            this.mUnitActiveVideo.setUser(videoObj.getSelectedUser());
                        }
                        if (clientWithoutOnHoldUserCount < 2) {
                            this.mFlagRecords.remove(KEY_UPDATE_CHECK_SHOW_ACTIVE_VIDEO);
                        }
                        this.mUnitActiveVideo.setBorderVisible(this.mIsExchangedMode);
                        this.mUnitActiveVideo.setBackgroundColor(this.mIsExchangedMode ? -16777216 : 0);
                        this.mUnitActiveVideo.setCanShowWaterMark(!this.mIsExchangedMode && isShowWaterMarkIfCan());
                        boolean isShowUserName = isShowUserName(!this.mIsExchangedMode);
                        this.mUnitActiveVideo.setUserNameVisible(isShowUserName, isShowUserName && this.mUserCount > 1);
                        VideoUnit videoUnit = this.mUnitActiveVideo;
                        videoUnit.setCanShowAudioOff(this.mIsExchangedMode || videoUnit.isUserNameVisible());
                    }
                    if (this.mUnitPreview != null) {
                        if (ConfMgr.getInstance().getVideoObj() != null) {
                            VideoUnit myVideoUnit = getMyVideoUnit();
                            VideoUnit videoUnit2 = this.mUnitPreview;
                            if (myVideoUnit == videoUnit2) {
                                ConfMgr instance = ConfMgr.getInstance();
                                CmmConfContext confContext = instance.getConfContext();
                                if (confContext != null) {
                                    boolean z2 = !confContext.isAudioOnlyMeeting() && !confContext.isShareOnlyMeeting();
                                    boolean noOneIsSendingVideo = instance.noOneIsSendingVideo();
                                    if (myVideoUnit != null && !z2 && noOneIsSendingVideo && clientWithoutOnHoldUserCount >= 2) {
                                        myVideoUnit.stopVideo(true);
                                        myVideoUnit.removeUser();
                                        myVideoUnit.setBorderVisible(false);
                                        myVideoUnit.setBackgroundColor(0);
                                    } else if (myVideoUnit != null) {
                                        CmmUser myself = userList.getMyself();
                                        if (myself != null) {
                                            if (!this.mIsExchangedMode) {
                                                myVideoUnit.setType(0);
                                            } else {
                                                myVideoUnit.setType(1);
                                            }
                                            myVideoUnit.setUser(myself.getNodeId());
                                            myVideoUnit.setBorderVisible(myVideoUnit == this.mUnitPreview && !this.mIsExchangedMode);
                                            if (myVideoUnit != this.mUnitPreview || this.mIsExchangedMode) {
                                                i = 0;
                                            }
                                            myVideoUnit.setBackgroundColor(i);
                                            myVideoUnit.setCanShowWaterMark(this.mIsExchangedMode && isShowWaterMarkIfCan());
                                            myVideoUnit.setUserNameVisible(isShowUserName(this.mIsExchangedMode));
                                            if (this.mIsExchangedMode && !myVideoUnit.isUserNameVisible()) {
                                                z = false;
                                            }
                                            myVideoUnit.setCanShowAudioOff(z);
                                        } else {
                                            return;
                                        }
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                videoUnit2.stopVideo(true);
                                this.mUnitPreview.removeUser();
                                this.mUnitPreview.setBorderVisible(false);
                                this.mUnitPreview.setBackgroundColor(0);
                            }
                        } else {
                            return;
                        }
                    }
                    updateGLImageWaterMark();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void startOne2One() {
        if (isCreated()) {
            ConfMgr instance = ConfMgr.getInstance();
            if (instance.getVideoObj() != null) {
                CmmUserList userList = instance.getUserList();
                if (userList != null && this.mUnitActiveVideo != null && this.mUnitPreview != null) {
                    int i = 0;
                    boolean z = true;
                    if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false) > 1) {
                        CmmUser peerUser = userList.getPeerUser(false, true);
                        if (peerUser != null) {
                            long nodeId = peerUser.getNodeId();
                            getVideoSceneMgr().setActiveUserId(nodeId);
                            VideoSize userVideoSize = getUserVideoSize(nodeId);
                            VideoSize videoSize = this.mActiveVideoSize;
                            if (videoSize == null || !videoSize.similarTo(userVideoSize)) {
                                this.mActiveVideoSize = userVideoSize;
                                this.mUnitActiveVideo.updateUnitInfo(createActiveVideoUnitInfo(true));
                            } else {
                                this.mActiveVideoSize = userVideoSize;
                            }
                            CmmConfContext confContext = instance.getConfContext();
                            if (confContext != null) {
                                boolean z2 = !confContext.isAudioOnlyMeeting() && !confContext.isShareOnlyMeeting();
                                boolean noOneIsSendingVideo = instance.noOneIsSendingVideo();
                                this.mUnitActiveVideo.setType(1);
                                this.mUnitActiveVideo.setUser(nodeId);
                                this.mUnitActiveVideo.setBorderVisible(this.mIsExchangedMode && (z2 || !noOneIsSendingVideo));
                                this.mUnitActiveVideo.setBackgroundColor(this.mIsExchangedMode ? -16777216 : 0);
                                CmmUser myself = userList.getMyself();
                                if (myself != null) {
                                    this.mUnitPreview.setType(0);
                                    this.mUnitPreview.setUser(myself.getNodeId());
                                    VideoUnit videoUnit = this.mUnitPreview;
                                    if (this.mIsExchangedMode || (!z2 && noOneIsSendingVideo)) {
                                        z = false;
                                    }
                                    videoUnit.setBorderVisible(z);
                                    VideoUnit videoUnit2 = this.mUnitPreview;
                                    if (!this.mIsExchangedMode) {
                                        i = -16777216;
                                    }
                                    videoUnit2.setBackgroundColor(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkUpdateUserVideo(long j) {
        long j2;
        if (isCreated() && this.mUnitActiveVideo != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                j2 = 1;
            } else {
                j2 = getVideoSceneMgr().getActiveUserId();
                CmmUser userById = ConfMgr.getInstance().getUserById(j2);
                if (userById != null) {
                    j2 = userById.getNodeId();
                }
            }
            if (ConfLocalHelper.isViewOnlyButNotSpeakAttendee() || (confStatusObj != null && confStatusObj.isSameUser(j, j2))) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    if (j2 > 0) {
                        VideoSize userVideoSize = getUserVideoSize(j2);
                        VideoSize videoSize = this.mActiveVideoSize;
                        if (videoSize == null || !videoSize.similarTo(userVideoSize)) {
                            this.mActiveVideoSize = userVideoSize;
                            this.mUnitActiveVideo.updateUnitInfo(createActiveVideoUnitInfo(true));
                        } else {
                            this.mActiveVideoSize = userVideoSize;
                        }
                        boolean z = false;
                        if (!this.mIsExchangedMode) {
                            this.mUnitActiveVideo.setType(1);
                        } else {
                            this.mUnitActiveVideo.setType(0);
                        }
                        if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) <= 2) {
                            this.mUnitActiveVideo.setUser(j2);
                        } else if (videoObj.isManualMode()) {
                            this.mUnitActiveVideo.setUser(videoObj.getSelectedUser());
                        } else {
                            this.mUnitActiveVideo.setUser(1);
                        }
                        this.mUnitActiveVideo.setCanShowWaterMark(!this.mIsExchangedMode && isShowWaterMarkIfCan());
                        boolean isShowUserName = isShowUserName(!this.mIsExchangedMode);
                        this.mUnitActiveVideo.setUserNameVisible(isShowUserName, ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false) >= 2 && isShowUserName);
                        VideoUnit videoUnit = this.mUnitActiveVideo;
                        if (this.mIsExchangedMode || videoUnit.isUserNameVisible()) {
                            z = true;
                        }
                        videoUnit.setCanShowAudioOff(z);
                    }
                } else {
                    return;
                }
            }
            updateSwitchScenePanel();
            updateGLImageWaterMark();
        }
    }

    /* access modifiers changed from: private */
    public void checkUpdateGroupUserVideo(@Nullable List<Long> list) {
        long j;
        if (isCreated() && this.mUnitActiveVideo != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                j = 1;
            } else {
                j = getVideoSceneMgr().getActiveUserId();
                CmmUser userById = ConfMgr.getInstance().getUserById(j);
                if (userById != null) {
                    j = userById.getNodeId();
                }
            }
            if (j > 0) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (ConfLocalHelper.isViewOnlyButNotSpeakAttendee()) {
                    if (videoObj != null) {
                        updateActiveUserVideo(videoObj, j);
                    } else {
                        return;
                    }
                } else if (!CollectionsUtil.isCollectionEmpty(list)) {
                    if (videoObj != null && confStatusObj != null) {
                        Iterator it = list.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (confStatusObj.isSameUser(((Long) it.next()).longValue(), j)) {
                                    updateActiveUserVideo(videoObj, j);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
            updateSwitchScenePanel();
            updateGLImageWaterMark();
        }
    }

    private void updateActiveUserVideo(@NonNull VideoSessionMgr videoSessionMgr, long j) {
        if (j > 0) {
            VideoSize userVideoSize = getUserVideoSize(j);
            VideoSize videoSize = this.mActiveVideoSize;
            if (videoSize == null || !videoSize.similarTo(userVideoSize)) {
                this.mActiveVideoSize = userVideoSize;
                RendererUnitInfo createActiveVideoUnitInfo = createActiveVideoUnitInfo(true);
                VideoUnit videoUnit = this.mUnitActiveVideo;
                if (videoUnit != null) {
                    videoUnit.updateUnitInfo(createActiveVideoUnitInfo);
                }
            } else {
                this.mActiveVideoSize = userVideoSize;
            }
            VideoUnit videoUnit2 = this.mUnitActiveVideo;
            boolean z = false;
            if (videoUnit2 != null) {
                if (!this.mIsExchangedMode) {
                    videoUnit2.setType(1);
                } else {
                    videoUnit2.setType(0);
                }
            }
            if (ConfMgr.getInstance().getClientWithoutOnHoldUserCount(true) <= 2) {
                this.mUnitActiveVideo.setUser(j);
            } else if (videoSessionMgr.isManualMode()) {
                this.mUnitActiveVideo.setUser(videoSessionMgr.getSelectedUser());
            } else {
                this.mUnitActiveVideo.setUser(1);
            }
            this.mUnitActiveVideo.setCanShowWaterMark(!this.mIsExchangedMode && isShowWaterMarkIfCan());
            boolean isShowUserName = isShowUserName(!this.mIsExchangedMode);
            this.mUnitActiveVideo.setUserNameVisible(isShowUserName, ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false) >= 2 && isShowUserName);
            VideoUnit videoUnit3 = this.mUnitActiveVideo;
            if (this.mIsExchangedMode || videoUnit3.isUserNameVisible()) {
                z = true;
            }
            videoUnit3.setCanShowAudioOff(z);
        }
    }

    @NonNull
    private RendererUnitInfo createActiveVideoUnitInfo(boolean z) {
        RendererUnitInfo rendererUnitInfo;
        if (!this.mIsExchangedMode || !z || !ConfMgr.getInstance().isConfConnected()) {
            if (!ConfMgr.getInstance().isConfConnected() || !z) {
                rendererUnitInfo = createBigPreviewVideoUnitInfo();
            } else {
                rendererUnitInfo = createBigVideoUnitInfo();
            }
            return rendererUnitInfo;
        }
        VideoSize videoSize = null;
        VideoSize videoSize2 = this.mActiveVideoSize;
        if (videoSize2 != null) {
            videoSize = getPreviewSize(videoSize2);
        }
        return createSmallVideoUnitInfo(videoSize);
    }

    @NonNull
    private RendererUnitInfo createBigVideoUnitInfo() {
        return createBigUnitInfo();
    }

    @NonNull
    private RendererUnitInfo createBigPreviewVideoUnitInfo() {
        return createBigUnitInfo();
    }

    /* access modifiers changed from: private */
    @NonNull
    public VideoSize getMyVideoSize() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            return new VideoSize(100, 100);
        }
        return (videoObj.isVideoStarted() || videoObj.isPreviewing()) ? videoObj.getMyVideoSize() : new VideoSize(100, 100);
    }

    private VideoSize getPreviewSize() {
        VideoSize myVideoSize = getMyVideoSize();
        if (myVideoSize.width == 0 && myVideoSize.height == 0) {
            myVideoSize = UIMgr.isLargeMode(getConfActivity()) ? new VideoSize(16, 9) : new VideoSize(4, 3);
        }
        return getPreviewSize(myVideoSize);
    }

    private VideoSize getPreviewSize(@Nullable VideoSize videoSize) {
        int i;
        if (videoSize == null || (videoSize.width == 0 && videoSize.height == 0)) {
            return new VideoSize(4, 3);
        }
        int max = Math.max(UIUtil.getDisplayWidth(getConfActivity()), UIUtil.getDisplayHeight(getConfActivity())) / 8;
        int min = Math.min(UIUtil.getDisplayWidth(getConfActivity()), UIUtil.getDisplayHeight(getConfActivity())) / 8;
        if (videoSize.width > videoSize.height) {
            i = Math.max(max, UIUtil.dip2px(getConfActivity(), 80.0f));
        } else {
            i = Math.max(min, UIUtil.dip2px(getConfActivity(), 45.0f));
        }
        return new VideoSize(i, (videoSize.height * i) / videoSize.width);
    }

    @NonNull
    private RendererUnitInfo createPreviewVideoUnitInfo() {
        if (!this.mIsExchangedMode) {
            return createSmallVideoUnitInfo(getPreviewSize());
        }
        return createBigVideoUnitInfo();
    }

    @NonNull
    private RendererUnitInfo createSmallVideoUnitInfo(@Nullable VideoSize videoSize) {
        if (videoSize == null || videoSize.width == 0 || videoSize.height == 0) {
            videoSize = getPreviewSize(new VideoSize(16, 9));
        }
        int i = videoSize.width;
        int i2 = videoSize.height;
        int dip2px = UIUtil.dip2px(getConfActivity(), 5.0f);
        int width = (getWidth() - dip2px) - i;
        int height = (getHeight() - i2) - dip2px;
        int toolbarHeight = getConfActivity().getToolbarHeight();
        if (toolbarHeight > 0) {
            height -= toolbarHeight;
        }
        return new RendererUnitInfo(getLeft() + width, getTop() + height, i, i2);
    }

    private void removeFadeView() {
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            ((ImageView) confActivity.findViewById(C4558R.C4560id.fadeview)).setVisibility(8);
        }
    }

    private void updateSwitchScenePanel() {
        String str;
        if (!isVideoPaused()) {
            ConfActivity confActivity = getConfActivity();
            if (confActivity != null) {
                View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
                LinearLayout linearLayout = (LinearLayout) confActivity.findViewById(C4558R.C4560id.panelSwitchSceneButtons);
                if (ConfMgr.getInstance().isCallingOut()) {
                    findViewById.setVisibility(8);
                    return;
                }
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
                        str = getConfActivity().getString(C4558R.string.zm_description_scene_normal);
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
            if (findViewById.getPaddingTop() != height) {
                findViewById.setPadding(0, height, 0, 0);
                findViewById.getParent().requestLayout();
            }
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

    public boolean isExchangedMode() {
        return this.mIsExchangedMode;
    }

    public void setExchangedMode(boolean z) {
        if (this.mIsExchangedMode != z) {
            this.mIsExchangedMode = z;
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

    private boolean isShowWaterMarkIfCan() {
        return this.mSceneMgr.getmVideoBoxApplication().isSDKMode() || getConfActivity().isToolbarShowing();
    }

    private boolean isShowUserName(boolean z) {
        return z && !getConfActivity().isToolbarShowing() && !ConfMgr.getInstance().isCallingOut();
    }

    private RendererUnitInfo createGLImageWaterMarkUnitInfo() {
        return new RendererUnitInfo(getLeft(), getTop(), getWidth(), getHeight());
    }

    private void createGLImageWaterMark() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && confContext.isSupportConfidentialWaterMarker()) {
                this.mGLImageWaterMark = videoObj.createGLImage(createGLImageWaterMarkUnitInfo());
                GLImage gLImage = this.mGLImageWaterMark;
                if (gLImage != null) {
                    gLImage.setUnitName("mGLImageWaterMark");
                    this.mGLImageWaterMark.setVideoScene(this);
                    addUnit(this.mGLImageWaterMark);
                    this.mGLImageWaterMark.onCreate();
                    this.mGLImageWaterMark.setVisible(false);
                }
            }
        }
    }

    private void updateGLImageWaterMark() {
        if (this.mGLImageWaterMark == null) {
            createGLImageWaterMark();
        }
        if (this.mGLImageWaterMark != null) {
            RendererUnitInfo createGLImageWaterMarkUnitInfo = createGLImageWaterMarkUnitInfo();
            VideoUnit videoUnit = this.mIsExchangedMode ? this.mUnitPreview : this.mUnitActiveVideo;
            if (videoUnit == null || videoUnit.getmVideoType() != 2 || !videoUnit.isVideoShowing()) {
                this.mGLImageWaterMark.setVisible(false);
                this.mWaterMarkWidth = 0;
                this.mWaterMarkHeight = 0;
            } else {
                this.mGLImageWaterMark.updateUnitInfo(createGLImageWaterMarkUnitInfo);
                this.mGLImageWaterMark.setVisible(true);
                if (!(this.mWaterMarkWidth == getWidth() && this.mWaterMarkHeight == getHeight())) {
                    Bitmap createWaterMarkBitmap = ConfLocalHelper.createWaterMarkBitmap(getWidth(), getHeight(), C4558R.color.zm_video_text, 1.0f);
                    if (createWaterMarkBitmap != null) {
                        this.mGLImageWaterMark.setBackground(createWaterMarkBitmap);
                        this.mWaterMarkWidth = getWidth();
                        this.mWaterMarkHeight = getHeight();
                    }
                }
            }
        }
    }

    public void setCanStartPreview(boolean z) {
        this.mCanStartPreview = z;
    }

    public boolean isZoomScene() {
        return (isExchangedMode() || ConfMgr.getInstance().getClientWithoutOnHoldUserCount(false) == 1) && !ConfMgr.getInstance().isViewOnlyMeeting();
    }
}
