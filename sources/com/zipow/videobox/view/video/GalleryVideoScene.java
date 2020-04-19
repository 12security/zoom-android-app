package com.zipow.videobox.view.video;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.GLButton;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.List;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class GalleryVideoScene extends AbsVideoScene implements OnClickListener, GLButton.OnClickListener {
    private static final int UNITS_GAP = 2;
    @NonNull
    private String TAG = GalleryVideoScene.class.getSimpleName();
    private int mCountVideoUsers = 0;
    @Nullable
    private GLButton mGLBtnSwitchCamera;
    private int mLastHeight = 0;
    private int mLastWidth = 0;
    @NonNull
    private LayoutInfo mLayoutInfo = new LayoutInfo();
    private int mPageIndex = 0;
    private ImageButton[] mSwitchSceneButtons;
    @Nullable
    private VideoUnit[] mUnits = null;

    static class LayoutInfo {
        int cols;
        int rows;
        int unitHeight;
        int unitWidth;

        public LayoutInfo() {
        }

        public LayoutInfo(int i, int i2, int i3, int i4) {
            this.cols = i;
            this.rows = i2;
            this.unitWidth = i3;
            this.unitHeight = i4;
        }

        public LayoutInfo(LayoutInfo layoutInfo) {
            this.cols = layoutInfo.cols;
            this.rows = layoutInfo.rows;
            this.unitWidth = layoutInfo.unitWidth;
            this.unitHeight = layoutInfo.unitHeight;
        }

        public boolean isValid() {
            return this.rows > 0 && this.cols > 0 && this.unitWidth > 0 && this.unitHeight > 0;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof LayoutInfo)) {
                return false;
            }
            LayoutInfo layoutInfo = (LayoutInfo) obj;
            if (this.cols == layoutInfo.cols && this.rows == layoutInfo.rows && this.unitWidth == layoutInfo.unitWidth && this.unitHeight == layoutInfo.unitHeight) {
                z = true;
            }
            return z;
        }
    }

    public void onMyVideoStatusChanged() {
    }

    public GalleryVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
    }

    public void updateLayoutInfo() {
        updateLayoutInfo(this.mLayoutInfo);
    }

    private int getCapacity() {
        int galleryViewCapacity = VideoLayoutHelper.getInstance().getGalleryViewCapacity();
        if (galleryViewCapacity <= 0) {
            return 4;
        }
        return galleryViewCapacity;
    }

    private void updateLayoutInfo(@Nullable LayoutInfo layoutInfo) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        LayoutInfo layoutInfo2 = layoutInfo;
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null && layoutInfo2 != null) {
            this.mCountVideoUsers = this.mSceneMgr.getTotalVideoCount();
            if (this.mCountVideoUsers != 0) {
                int dip2px = UIUtil.dip2px(confActivity, 2.0f);
                int width = getWidth();
                int height = getHeight() - UIUtil.dip2px(confActivity, 45.0f);
                int capacity = getCapacity();
                VideoSize videoSize = new VideoSize();
                int min = Math.min(this.mCountVideoUsers, capacity);
                int i8 = 0;
                int i9 = 0;
                int i10 = 0;
                int i11 = 0;
                int i12 = 1;
                while (i12 <= capacity) {
                    int i13 = i8;
                    int i14 = i9;
                    int i15 = i10;
                    int i16 = i11;
                    int i17 = 1;
                    while (i17 <= capacity) {
                        int i18 = i17 * i12;
                        if (i18 < min) {
                            i2 = i17;
                            i = i12;
                            i4 = width;
                            i3 = height;
                            i5 = min;
                            i7 = i16;
                            i6 = i15;
                        } else if (i18 > capacity) {
                            i2 = i17;
                            i = i12;
                            i4 = width;
                            i3 = height;
                            i5 = min;
                            i7 = i16;
                            i6 = i15;
                        } else if (((double) (((float) width) / ((float) height))) < 1.6d || i17 <= i12) {
                            VideoSize videoSize2 = new VideoSize();
                            i5 = min;
                            int i19 = i18;
                            int i20 = width;
                            i4 = width;
                            i7 = i16;
                            int i21 = height;
                            i3 = height;
                            i6 = i15;
                            i2 = i17;
                            i = i12;
                            calVideoSize(i17, i12, i20, i21, dip2px, dip2px, videoSize2);
                            int i22 = this.mCountVideoUsers;
                            if (i22 > capacity) {
                                i22 = i19;
                            }
                            VideoSize videoSize3 = videoSize2;
                            i15 = videoSize3.width * videoSize3.height * i22;
                            if (i6 < i15 || (i6 == i15 && i19 < i7)) {
                                videoSize.width = videoSize3.width;
                                videoSize.height = videoSize3.height;
                                i16 = i19;
                                i14 = i2;
                                i13 = i;
                                i17 = i2 + 1;
                                min = i5;
                                width = i4;
                                height = i3;
                                i12 = i;
                            }
                        } else {
                            i2 = i17;
                            i = i12;
                            i4 = width;
                            i3 = height;
                            i5 = min;
                            i7 = i16;
                            i6 = i15;
                        }
                        i16 = i7;
                        i15 = i6;
                        i17 = i2 + 1;
                        min = i5;
                        width = i4;
                        height = i3;
                        i12 = i;
                    }
                    int i23 = min;
                    i12++;
                    i11 = i16;
                    i10 = i15;
                    i8 = i13;
                    i9 = i14;
                    width = width;
                    height = height;
                }
                layoutInfo2.cols = i8;
                layoutInfo2.rows = i9;
                layoutInfo2.unitWidth = videoSize.width;
                layoutInfo2.unitHeight = videoSize.height;
            }
        }
    }

    private void calVideoSize(int i, int i2, int i3, int i4, int i5, int i6, VideoSize videoSize) {
        boolean z = false;
        videoSize.width = 0;
        videoSize.height = 0;
        if (i2 != 0 && i != 0) {
            int i7 = i3 - ((i2 + 1) * i5);
            int i8 = i4 - ((i + 1) * i6);
            if (i7 * i * 9 >= i8 * i2 * 16) {
                z = true;
            }
            if (z) {
                videoSize.height = i8 / i;
                int i9 = videoSize.height / 9;
                videoSize.height = i9 * 9;
                videoSize.width = i9 * 16;
            } else {
                videoSize.width = i7 / i2;
                int i10 = videoSize.width / 16;
                videoSize.width = i10 * 16;
                videoSize.height = i10 * 9;
            }
        }
    }

    public int getUnitsCount() {
        return this.mLayoutInfo.cols * this.mLayoutInfo.rows;
    }

    public void setPageIndex(int i) {
        this.mPageIndex = i;
        if (this.mUnits != null) {
            int visibleUnitCount = getVisibleUnitCount();
            for (int i2 = 0; i2 < visibleUnitCount; i2++) {
                VideoUnit[] videoUnitArr = this.mUnits;
                if (i2 >= videoUnitArr.length) {
                    break;
                }
                if (videoUnitArr[i2] != null) {
                    videoUnitArr[i2].setBorderVisible(true);
                }
            }
            while (true) {
                VideoUnit[] videoUnitArr2 = this.mUnits;
                if (visibleUnitCount < videoUnitArr2.length) {
                    if (videoUnitArr2[visibleUnitCount] != null) {
                        if (videoUnitArr2[visibleUnitCount].getUser() != 0) {
                            this.mUnits[visibleUnitCount].removeUser();
                            this.mUnits[visibleUnitCount].setBorderType(0);
                            this.mUnits[visibleUnitCount].clearRenderer();
                        }
                        this.mUnits[visibleUnitCount].setBorderVisible(false);
                        this.mUnits[visibleUnitCount].setBackgroundColor(0);
                    }
                    visibleUnitCount++;
                } else {
                    return;
                }
            }
        }
    }

    public int getPageIndex() {
        return this.mPageIndex;
    }

    public boolean hasPrevPage() {
        return this.mPageIndex > 0;
    }

    public boolean hasNextPage() {
        return (this.mPageIndex + 1) * getUnitsCount() < this.mSceneMgr.getTotalVideoCount();
    }

    public int getVisibleUnitCount() {
        int totalVideoCount = this.mSceneMgr.getTotalVideoCount();
        int i = 0;
        if (totalVideoCount == 0) {
            return 0;
        }
        int unitsCount = getUnitsCount();
        if (unitsCount == 0) {
            updateLayoutInfo();
            unitsCount = getUnitsCount();
        }
        if (unitsCount == 0) {
            return 0;
        }
        int i2 = totalVideoCount / unitsCount;
        int i3 = totalVideoCount % unitsCount;
        if (i3 > 0) {
            i = 1;
        }
        if (this.mPageIndex != (i2 + i) - 1 || i3 == 0) {
            i3 = unitsCount;
        }
        return i3;
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null || this.mUnits != null) {
            return;
        }
        if (!this.mLayoutInfo.isValid()) {
            updateLayoutInfo();
            if (isCreated()) {
                onCreateUnits();
            }
            return;
        }
        int unitsCount = getUnitsCount();
        this.mUnits = new VideoUnit[unitsCount];
        for (int i = 0; i < unitsCount; i++) {
            RendererUnitInfo createVideoUnitInfo = createVideoUnitInfo(i);
            if (createVideoUnitInfo != null) {
                VideoUnit createVideoUnit = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createVideoUnitInfo);
                this.mUnits[i] = createVideoUnit;
                if (createVideoUnit != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("GalleryUnit");
                    sb.append(i);
                    createVideoUnit.setUnitName(sb.toString());
                    createVideoUnit.setVideoScene(this);
                    createVideoUnit.setCanShowAudioOff(true);
                    createVideoUnit.setBorderVisible(false);
                    createVideoUnit.setBackgroundColor(-16777216);
                    addUnit(createVideoUnit);
                    createVideoUnit.onCreate();
                }
            }
        }
        if (isVisible()) {
            positionSwitchScenePanel();
        }
    }

    public void updateAccessibilitySceneDescription() {
        if (getConfActivity() == null) {
            return;
        }
        if (getConfActivity().isToolbarShowing()) {
            getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_gallery_video_toolbar_showed));
        } else {
            getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(getConfActivity().getString(C4558R.string.zm_description_scene_gallery_video_toolbar_hided));
        }
    }

    @NonNull
    public Rect getBoundsForAccessbilityViewIndex(int i) {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr == null || i >= videoUnitArr.length) {
            return new Rect();
        }
        VideoUnit videoUnit = videoUnitArr[i];
        if (videoUnit == null) {
            return new Rect();
        }
        return new Rect(videoUnit.getLeft(), videoUnit.getTop(), videoUnit.getRight(), videoUnit.getBottom());
    }

    public int getAccessbilityViewIndexAt(float f, float f2) {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr == null) {
            return -1;
        }
        int length = videoUnitArr.length;
        if (length <= 0) {
            return -1;
        }
        for (int i = 0; i < length; i++) {
            if (this.mUnits[i].isPointInUnit(f, f2)) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    public CharSequence getAccessibilityDescriptionForIndex(int i) {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr == null) {
            return "";
        }
        int length = videoUnitArr.length;
        if (length <= 0) {
            return "";
        }
        if (i >= length) {
            return "";
        }
        new StringBuilder();
        VideoUnit videoUnit = this.mUnits[i];
        if (videoUnit == null) {
            return "";
        }
        return videoUnit.getAccessibilityDescription();
    }

    public void getAccessibilityVisibleVirtualViews(@NonNull List<Integer> list) {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr != null && videoUnitArr.length > 0) {
            for (int i = 0; i < this.mUnits.length; i++) {
                list.add(Integer.valueOf(i));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateUnits() {
        if (this.mLastWidth == 0 || this.mLastHeight == 0 || (getWidth() == this.mLastWidth && getHeight() == this.mLastHeight)) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.mLastWidth = getWidth();
                this.mLastHeight = getHeight();
                int unitsCount = getUnitsCount();
                VideoUnit[] videoUnitArr = this.mUnits;
                if (videoUnitArr != null) {
                    if (videoUnitArr.length < unitsCount) {
                        VideoUnit[] videoUnitArr2 = new VideoUnit[unitsCount];
                        for (int i = 0; i < unitsCount; i++) {
                            VideoUnit[] videoUnitArr3 = this.mUnits;
                            if (i < videoUnitArr3.length) {
                                videoUnitArr2[i] = videoUnitArr3[i];
                            } else {
                                RendererUnitInfo createVideoUnitInfo = createVideoUnitInfo(i);
                                if (createVideoUnitInfo != null) {
                                    VideoUnit createVideoUnit = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createVideoUnitInfo);
                                    videoUnitArr2[i] = createVideoUnit;
                                    if (createVideoUnit != null) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("GalleryUnit");
                                        sb.append(i);
                                        createVideoUnit.setUnitName(sb.toString());
                                        createVideoUnit.setVideoScene(this);
                                        createVideoUnit.setCanShowAudioOff(true);
                                        createVideoUnit.setBorderVisible(false);
                                        createVideoUnit.setBackgroundColor(-16777216);
                                        addUnit(createVideoUnit);
                                        createVideoUnit.onCreate();
                                    }
                                }
                            }
                        }
                        this.mUnits = videoUnitArr2;
                    } else if (videoUnitArr.length > unitsCount) {
                        VideoUnit[] videoUnitArr4 = new VideoUnit[unitsCount];
                        int i2 = 0;
                        while (true) {
                            VideoUnit[] videoUnitArr5 = this.mUnits;
                            if (i2 >= videoUnitArr5.length) {
                                break;
                            }
                            if (i2 < unitsCount) {
                                videoUnitArr4[i2] = videoUnitArr5[i2];
                            } else if (videoUnitArr5[i2] != null) {
                                videoUnitArr5[i2].removeUser();
                                this.mUnits[i2].onDestroy();
                                removeUnit(this.mUnits[i2]);
                            }
                            i2++;
                        }
                        this.mUnits = videoUnitArr4;
                    }
                }
                if (this.mUnits == null) {
                    this.mUnits = new VideoUnit[unitsCount];
                    return;
                }
                for (int i3 = 0; i3 < this.mUnits.length; i3++) {
                    RendererUnitInfo createVideoUnitInfo2 = createVideoUnitInfo(i3);
                    if (createVideoUnitInfo2 != null) {
                        VideoUnit[] videoUnitArr6 = this.mUnits;
                        if (videoUnitArr6[i3] == null) {
                            VideoUnit createVideoUnit2 = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createVideoUnitInfo2);
                            this.mUnits[i3] = createVideoUnit2;
                            if (createVideoUnit2 != null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("GalleryUnit");
                                sb2.append(i3);
                                createVideoUnit2.setUnitName(sb2.toString());
                                createVideoUnit2.setVideoScene(this);
                                createVideoUnit2.setCanShowAudioOff(true);
                                createVideoUnit2.setBorderVisible(false);
                                createVideoUnit2.setBackgroundColor(-16777216);
                                addUnit(createVideoUnit2);
                                createVideoUnit2.onCreate();
                            }
                        } else {
                            videoUnitArr6[i3].updateUnitInfo(createVideoUnitInfo2);
                        }
                    }
                }
                updateSwitchCameraButton();
                if (isVisible()) {
                    updateSwitchScenePanel();
                    updateAccessibilitySceneDescription();
                }
                return;
            }
            return;
        }
        this.mLastWidth = getWidth();
        this.mLastHeight = getHeight();
        updateContentSubscription();
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        if (this.mUnits != null) {
            int i = 0;
            while (true) {
                VideoUnit[] videoUnitArr = this.mUnits;
                if (i >= videoUnitArr.length) {
                    break;
                }
                videoUnitArr[i] = null;
                i++;
            }
        }
        this.mUnits = null;
        this.mGLBtnSwitchCamera = null;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        runOnRendererInited(new Runnable() {
            public void run() {
                GalleryVideoScene.this.updateContentSubscription();
            }
        });
        if (isVisible()) {
            updateSwitchScenePanel();
        }
    }

    public void updateContentSubscription() {
        checkShowVideo();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        if (this.mUnits != null) {
            int i = 0;
            while (true) {
                VideoUnit[] videoUnitArr = this.mUnits;
                if (i < videoUnitArr.length) {
                    if (videoUnitArr[i] != null) {
                        videoUnitArr[i].removeUser();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void onGroupUserVideoStatus(@Nullable List<Long> list) {
        super.onGroupUserVideoStatus(list);
        runOnRendererInited(new Runnable() {
            public void run() {
                GalleryVideoScene.this.updateContentSubscription();
            }
        });
    }

    public void onUserVideoDataSizeChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                GalleryVideoScene.this.updateContentSubscription();
            }
        });
    }

    public void onUserPicReady(long j) {
        updateUserPic(j);
    }

    private void updateUserPic(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && this.mUnits != null) {
            int i = 0;
            while (true) {
                VideoUnit[] videoUnitArr = this.mUnits;
                if (i < videoUnitArr.length) {
                    if (!(videoUnitArr[i] == null || videoUnitArr[i].getUser() == 0 || !confStatusObj.isSameUser(j, this.mUnits[i].getUser()))) {
                        this.mUnits[i].updateAvatar();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void onActiveVideoChanged(long j) {
        if (getVideoSceneMgr().getLockedUserId() == 0) {
            runOnRendererInited(new Runnable() {
                public void run() {
                    GalleryVideoScene.this.updateContentSubscription();
                }
            });
        }
    }

    public void onUserActiveVideoForDeck(long j) {
        if (getVideoSceneMgr().getLockedUserId() == 0) {
            runOnRendererInited(new Runnable() {
                public void run() {
                    GalleryVideoScene.this.updateContentSubscription();
                }
            });
        }
    }

    public void onUserCountChangesForShowHideAction() {
        if (this.mSceneMgr.isMeetSwitchToGalleryView()) {
            updateContentSubscription();
        } else if (!VideoLayoutHelper.getInstance().isSwitchVideoLayoutAccordingToUserCountEnabled() && isVisible()) {
            ((VideoSceneMgr) getVideoSceneMgr()).switchToDefaultScene();
        }
        updateSwitchScenePanel();
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        switch (i) {
            case 0:
            case 1:
                if (this.mSceneMgr.isMeetSwitchToGalleryView()) {
                    updateContentSubscription();
                } else if (!VideoLayoutHelper.getInstance().isSwitchVideoLayoutAccordingToUserCountEnabled() && isVisible()) {
                    ((VideoSceneMgr) getVideoSceneMgr()).switchToDefaultScene();
                }
                updateSwitchScenePanel();
                return;
            case 2:
                updateContentSubscription();
                return;
            default:
                return;
        }
    }

    public void onDoubleTap(@NonNull MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (this.mUnits != null) {
            final ConfActivity confActivity = getConfActivity();
            if (confActivity != null) {
                int i = 0;
                while (true) {
                    VideoUnit[] videoUnitArr = this.mUnits;
                    if (i >= videoUnitArr.length) {
                        return;
                    }
                    if (videoUnitArr[i] == null || videoUnitArr[i].getUser() == 0 || x <= ((float) this.mUnits[i].getLeft()) || x >= ((float) (this.mUnits[i].getLeft() + this.mUnits[i].getWidth())) || y <= ((float) this.mUnits[i].getTop()) || y >= ((float) (this.mUnits[i].getTop() + this.mUnits[i].getHeight()))) {
                        i++;
                    } else {
                        long user = this.mUnits[i].getUser();
                        final VideoSceneMgr videoSceneMgr = (VideoSceneMgr) getVideoSceneMgr();
                        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                        if (videoObj != null && !videoObj.isSelectedUser(user)) {
                            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                            if ((confStatusObj == null || !confStatusObj.isMyself(user)) && videoSceneMgr.pinVideo(user) && !ConfMgr.getInstance().isViewOnlyMeeting()) {
                                View findViewById = confActivity.findViewById(C4558R.C4560id.confView);
                                ImageView imageView = (ImageView) confActivity.findViewById(C4558R.C4560id.fadeview);
                                ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, (x - ((float) findViewById.getLeft())) / ((float) findViewById.getWidth()), 1, (y - ((float) findViewById.getTop())) / ((float) findViewById.getHeight()));
                                scaleAnimation.setAnimationListener(new AnimationListener() {
                                    public void onAnimationRepeat(Animation animation) {
                                    }

                                    public void onAnimationStart(Animation animation) {
                                    }

                                    public void onAnimationEnd(Animation animation) {
                                        videoSceneMgr.showPinModeInNormalScene();
                                        Toast makeText = Toast.makeText(confActivity, C4558R.string.zm_msg_doubletap_enter_pinvideo, 0);
                                        makeText.setGravity(17, 0, 0);
                                        makeText.show();
                                    }
                                });
                                scaleAnimation.setDuration(200);
                                imageView.setVisibility(0);
                                imageView.startAnimation(scaleAnimation);
                            }
                            return;
                        }
                        return;
                    }
                }
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
        if (confStatusObj != null && this.mUnits != null) {
            int i = 0;
            while (true) {
                VideoUnit[] videoUnitArr = this.mUnits;
                if (i < videoUnitArr.length) {
                    if (!(videoUnitArr[i] == null || videoUnitArr[i].getUser() == 0 || !confStatusObj.isSameUser(j, this.mUnits[i].getUser()))) {
                        this.mUnits[i].onUserAudioStatus();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void beforeSwitchCamera() {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr != null && videoUnitArr.length != 0 && videoUnitArr[0] != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(this.mUnits[0].getUser())) {
                this.mUnits[0].stopVideo(false);
            }
        }
    }

    public void afterSwitchCamera() {
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr != null && videoUnitArr.length != 0 && videoUnitArr[0] != null) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.isMyself(this.mUnits[0].getUser())) {
                this.mUnits[0].startVideo();
            }
        }
    }

    private void checkShowVideo() {
        int i;
        long j;
        long j2;
        boolean z;
        CmmUser cmmUser;
        if (isCreated()) {
            int i2 = this.mLayoutInfo.rows * this.mLayoutInfo.cols;
            if (i2 != 0) {
                int totalVideoCount = this.mSceneMgr.getTotalVideoCount();
                if (totalVideoCount == 0) {
                    ((VideoSceneMgr) getVideoSceneMgr()).switchToDefaultScene();
                    return;
                }
                int i3 = ((totalVideoCount + i2) - 1) / i2;
                if (this.mPageIndex >= i3) {
                    this.mPageIndex = i3 - 1;
                    updateSwitchScenePanel();
                }
                ConfMgr instance = ConfMgr.getInstance();
                CmmUserList userList = instance.getUserList();
                if (userList != null) {
                    CmmConfStatus confStatusObj = instance.getConfStatusObj();
                    if (confStatusObj != null) {
                        long lockedUserId = getVideoSceneMgr().getLockedUserId();
                        VideoSessionMgr videoObj = instance.getVideoObj();
                        long activeDeckUserID = videoObj != null ? videoObj.getActiveDeckUserID(false) : 0;
                        LayoutInfo layoutInfo = new LayoutInfo(this.mLayoutInfo);
                        updateLayoutInfo(this.mLayoutInfo);
                        if (layoutInfo.equals(this.mLayoutInfo)) {
                            VideoUnit[] videoUnitArr = this.mUnits;
                            if (videoUnitArr != null && videoUnitArr.length == getUnitsCount()) {
                                updateUnits();
                                if (this.mUnits != null) {
                                    boolean z2 = !ConfLocalHelper.isHideNoVideoUsers();
                                    int unitsCount = getUnitsCount();
                                    BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
                                    boolean isInBOMeeting = bOMgr != null ? bOMgr.isInBOMeeting() : false;
                                    boolean z3 = !ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView();
                                    int i4 = 0;
                                    while (true) {
                                        VideoUnit[] videoUnitArr2 = this.mUnits;
                                        if (i4 >= videoUnitArr2.length) {
                                            break;
                                        }
                                        if (videoUnitArr2[i4] == null) {
                                            i = i4;
                                            j = activeDeckUserID;
                                        } else {
                                            int i5 = this.mPageIndex;
                                            if (i5 >= 0) {
                                                if (z2) {
                                                    i = i4;
                                                    j2 = activeDeckUserID;
                                                    z = false;
                                                    cmmUser = getUserAt(userList, confStatusObj, unitsCount, i5, i, isInBOMeeting, z3);
                                                } else {
                                                    i = i4;
                                                    j2 = activeDeckUserID;
                                                    z = false;
                                                    cmmUser = getHasVideoUserAt(userList, confStatusObj, unitsCount, i5, i, isInBOMeeting, z3);
                                                }
                                                if (cmmUser == null) {
                                                    j = j2;
                                                    if (this.mUnits[i].getUser() != 0) {
                                                        this.mUnits[i].removeUser();
                                                        this.mUnits[i].setBorderType(z);
                                                        this.mUnits[i].clearRenderer();
                                                    }
                                                    this.mUnits[i].setBorderVisible(z);
                                                    this.mUnits[i].setBackgroundColor(z ? 1 : 0);
                                                } else if (!isPreloadStatus()) {
                                                    long nodeId = cmmUser.getNodeId();
                                                    this.mUnits[i].setType(z);
                                                    this.mUnits[i].setUser(nodeId);
                                                    this.mUnits[i].setBackgroundColor(-16777216);
                                                    if (lockedUserId == 0) {
                                                        j = j2;
                                                        if (confStatusObj.isSameUser(nodeId, j)) {
                                                            this.mUnits[i].setBorderType(1);
                                                            this.mUnits[i].setBorderVisible(true);
                                                        }
                                                    } else {
                                                        j = j2;
                                                    }
                                                    if (nodeId == lockedUserId) {
                                                        this.mUnits[i].setBorderType(2);
                                                    } else {
                                                        this.mUnits[i].setBorderType(z);
                                                    }
                                                    this.mUnits[i].setBorderVisible(true);
                                                } else {
                                                    j = j2;
                                                    if (this.mUnits[i].getUser() != 0) {
                                                        this.mUnits[i].removeUser();
                                                    }
                                                    this.mUnits[i].setBorderType(z);
                                                    this.mUnits[i].clearRenderer();
                                                    this.mUnits[i].setBorderVisible(true);
                                                    this.mUnits[i].setBackgroundColor(-16777216);
                                                }
                                            } else if (videoUnitArr2[i4].getUser() != 0) {
                                                this.mUnits[i4].removeUser();
                                                this.mUnits[i4].clearRenderer();
                                                i = i4;
                                                j = activeDeckUserID;
                                            } else {
                                                i = i4;
                                                j = activeDeckUserID;
                                            }
                                        }
                                        activeDeckUserID = j;
                                        i4 = i + 1;
                                    }
                                    if (!isPreloadStatus()) {
                                        updateSwitchScenePanel();
                                    }
                                    return;
                                }
                                return;
                            }
                        }
                        updateUnits();
                        updateContentSubscription();
                    }
                }
            }
        }
    }

    private CmmUser getHasVideoUserAt(CmmUserList cmmUserList, @NonNull CmmConfStatus cmmConfStatus, int i, int i2, int i3, boolean z, boolean z2) {
        CmmUser myself = cmmUserList.getMyself();
        boolean hasVideo = this.mSceneMgr.hasVideo(myself);
        if (i2 == 0 && i3 == 0 && hasVideo && !z2) {
            return myself;
        }
        int i4 = (!hasVideo || z2) ? 0 : 1;
        int userCount = cmmUserList.getUserCount();
        int i5 = i4;
        int i6 = 0;
        for (int i7 = 0; i7 < userCount; i7++) {
            CmmUser userAt = cmmUserList.getUserAt(i7);
            if (userAt != null && !cmmConfStatus.isMyself(userAt.getNodeId()) && this.mSceneMgr.hasVideo(userAt) && (z || !userAt.isInBOMeeting())) {
                if (i6 == i2 && i5 == i3) {
                    return userAt;
                }
                i5++;
                if (i5 == i) {
                    i6++;
                    i5 = 0;
                }
            }
        }
        return null;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=int, for r14v0, types: [int, boolean] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.zipow.videobox.confapp.CmmUser getUserAt(@androidx.annotation.NonNull com.zipow.videobox.confapp.CmmUserList r8, @androidx.annotation.NonNull com.zipow.videobox.confapp.CmmConfStatus r9, int r10, int r11, int r12, boolean r13, int r14) {
        /*
            r7 = this;
            if (r11 != 0) goto L_0x000b
            if (r12 != 0) goto L_0x000b
            if (r14 != 0) goto L_0x000b
            com.zipow.videobox.confapp.CmmUser r8 = r8.getMyself()
            return r8
        L_0x000b:
            r14 = r14 ^ 1
            int r0 = r8.getUserCount()
            r1 = 0
            r3 = r14
            r14 = 0
            r2 = 0
        L_0x0015:
            if (r14 >= r0) goto L_0x004c
            com.zipow.videobox.confapp.CmmUser r4 = r8.getUserAt(r14)
            if (r4 == 0) goto L_0x0049
            boolean r5 = r4.isMMRUser()
            if (r5 != 0) goto L_0x0049
            long r5 = r4.getNodeId()
            boolean r5 = r9.isMyself(r5)
            if (r5 != 0) goto L_0x0049
            boolean r5 = r4.inSilentMode()
            if (r5 == 0) goto L_0x0034
            goto L_0x0049
        L_0x0034:
            if (r13 != 0) goto L_0x003d
            boolean r5 = r4.isInBOMeeting()
            if (r5 == 0) goto L_0x003d
            goto L_0x0049
        L_0x003d:
            if (r2 != r11) goto L_0x0042
            if (r3 != r12) goto L_0x0042
            return r4
        L_0x0042:
            int r3 = r3 + 1
            if (r3 != r10) goto L_0x0049
            int r2 = r2 + 1
            r3 = 0
        L_0x0049:
            int r14 = r14 + 1
            goto L_0x0015
        L_0x004c:
            r8 = 0
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.video.GalleryVideoScene.getUserAt(com.zipow.videobox.confapp.CmmUserList, com.zipow.videobox.confapp.CmmConfStatus, int, int, int, boolean, boolean):com.zipow.videobox.confapp.CmmUser");
    }

    private RendererUnitInfo createVideoUnitInfo(int i) {
        double d;
        if (!this.mLayoutInfo.isValid()) {
            return null;
        }
        int i2 = this.mLayoutInfo.rows;
        int i3 = this.mLayoutInfo.cols;
        int dip2px = UIUtil.dip2px(getConfActivity(), 2.0f);
        double d2 = (double) this.mLayoutInfo.unitWidth;
        double d3 = (double) this.mLayoutInfo.unitHeight;
        double width = ((((double) getWidth()) - ((((double) i3) * d2) + ((double) ((i3 - 1) * dip2px)))) * 1.0d) / 2.0d;
        int i4 = i / i3;
        int i5 = i % i3;
        double d4 = width + (((double) i5) * d2) + ((double) (i5 * dip2px));
        double height = (((((double) (getHeight() - UIUtil.dip2px(getConfActivity(), 45.0f))) - ((((double) i2) * d3) + ((double) ((i2 - 1) * dip2px)))) * 1.0d) / 2.0d) + (((double) i4) * d3) + ((double) (i4 * dip2px));
        VideoSize videoSize = new VideoSize(16, 9);
        int i6 = dip2px;
        if (((double) videoSize.height) * d2 > ((double) videoSize.width) * d3) {
            d = ((((double) videoSize.width) * d3) * 1.0d) / ((double) videoSize.height);
            d4 += ((d2 - d) * 1.0d) / 2.0d;
        } else {
            double d5 = ((((double) videoSize.height) * d2) * 1.0d) / ((double) videoSize.width);
            height += ((d3 - d5) * 1.0d) / 2.0d;
            d3 = d5;
            d = d2;
        }
        int i7 = this.mLayoutInfo.rows * this.mLayoutInfo.cols;
        boolean z = ((this.mCountVideoUsers + i7) + -1) / i7 == getPageIndex() + 1;
        boolean z2 = i / this.mLayoutInfo.cols == this.mLayoutInfo.rows + -1;
        if (z && z2) {
            int i8 = this.mCountVideoUsers % this.mLayoutInfo.cols;
            if (i8 != 0) {
                d4 = (d4 - width) + ((((double) getWidth()) - ((((double) i8) * d2) + ((double) ((i8 - 1) * i6)))) / 2.0d);
            }
        }
        return new RendererUnitInfo(Math.round((float) (((double) getLeft()) + d4)), Math.round((float) (((double) getTop()) + height)), Math.round((float) d), Math.round((float) d3));
    }

    private String getLogTag() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.TAG);
        sb.append("[");
        sb.append(this.mPageIndex);
        sb.append("]");
        return sb.toString();
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
                    this.mSwitchSceneButtons[i2].setImageResource(i2 == getPageIndex() + basicSceneCount ? C4558R.C4559drawable.zm_btn_switch_scene_selected : C4558R.C4559drawable.zm_btn_switch_scene_unselected);
                    this.mSwitchSceneButtons[i2].setVisibility(i2 < sceneCount ? 0 : 8);
                    this.mSwitchSceneButtons[i2].setOnClickListener(this);
                    ImageButton imageButton = this.mSwitchSceneButtons[i2];
                    if (i2 == getPageIndex() + basicSceneCount) {
                        str = getConfActivity().getString(C4558R.string.zm_description_scene_gallery_video);
                    } else {
                        str = ((VideoSceneMgr) getVideoSceneMgr()).getAccessibliltyDescriptionSceneSwitch(i2);
                    }
                    imageButton.setContentDescription(str);
                    linearLayout.addView(this.mSwitchSceneButtons[i2], UIUtil.dip2px(confActivity, 20.0f), UIUtil.dip2px(confActivity, 40.0f));
                    i2++;
                }
                positionSwitchScenePanel();
                if (sceneCount <= 0) {
                    i = 4;
                }
                findViewById.setVisibility(i);
            }
        }
    }

    private void positionSwitchScenePanel() {
        int height = getHeight() - UIUtil.dip2px(getConfActivity(), 45.0f);
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
            findViewById.setPadding(0, height, 0, 0);
            findViewById.getParent().requestLayout();
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
        if (i != getPageIndex() + ((VideoSceneMgr) getVideoSceneMgr()).getBasicSceneCount()) {
            getVideoSceneMgr().switchToScene(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
        updateContentSubscription();
        updateSwitchScenePanel();
    }

    private void updateSwitchCameraButton() {
        if (this.mGLBtnSwitchCamera != null && ConfMgr.getInstance().getVideoObj() != null) {
            RendererUnitInfo createSwitchCameraButtonUnitInfo = createSwitchCameraButtonUnitInfo();
            if (createSwitchCameraButtonUnitInfo != null) {
                this.mGLBtnSwitchCamera.updateUnitInfo(createSwitchCameraButtonUnitInfo);
            }
        }
    }

    @Nullable
    private RendererUnitInfo createSwitchCameraButtonUnitInfo() {
        return createSwitchCameraButtonUnitInfo(null);
    }

    private RendererUnitInfo createSwitchCameraButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        VideoUnit[] videoUnitArr = this.mUnits;
        if (videoUnitArr == null || videoUnitArr.length == 0 || videoUnitArr[0] == null) {
            return null;
        }
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
        int dip2px = UIUtil.dip2px(getConfActivity(), 2.0f);
        return new RendererUnitInfo(((this.mUnits[0].getLeft() + this.mUnits[0].getWidth()) - i) - dip2px, this.mUnits[0].getTop() + dip2px, i, i2);
    }

    public void onClick(GLButton gLButton) {
        ZMConfComponentMgr.getInstance().onClickSwitchCamera();
    }

    public void checkGalleryUnits(int i, int i2) {
        if (getUnitsCount() == 0) {
            stop();
            destroy();
            create(i, i2);
            setLocation(0, 0);
            start();
        }
    }
}
