package com.zipow.videobox.view.video;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.share.ShareView;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class LargeShareVideoScene extends ShareVideoScene {
    private static final int GALLERY_ACTION_BAR_DIP = 20;
    private static final int GALLERY_ITEMS_PHONE_LANDSCAPE_COUNT = 4;
    private static final int GALLERY_ITEMS_PORTRAIT_COUNT = 3;
    private static final int GALLERY_ITEMS_TABLET_LANDSCAPE_COUNT = 6;
    private static final int GALLERY_UNIT_MARGIN_DIP = 3;
    private static final int GALLERY_lIST_MARGIN_PORTRAIT_DIP = 4;
    private static final int GALLERY_lIST_MARGIN_lANDSCAPE_DIP = 2;
    private static final int MAX_GALLERY_ITEMS_PHONE_COUNT = 4;
    private static final int MAX_GALLERY_ITEMS_TABLET_COUNT = 6;
    @NonNull
    OnClickListener ExpandGalleryViewlistener = new OnClickListener() {
        public void onClick(View view) {
            LargeShareVideoScene largeShareVideoScene = LargeShareVideoScene.this;
            largeShareVideoScene.showOrHideGalleryVideoUnits(!largeShareVideoScene.mIsGalleryVideoViewExpand);
        }
    };
    private final String TAG = LargeShareVideoScene.class.getSimpleName();
    private int mCountUsers = 1;
    private int mGalleryScrollPosX = 0;
    private int mGalleryScrollPosY = 0;
    /* access modifiers changed from: private */
    public boolean mIsGalleryVideoViewExpand = true;
    private boolean mIsScrollingGallery = false;
    @NonNull
    private ArrayList<VideoUnit> mListGalleryUnits = new ArrayList<>();

    public boolean canDragSceneToLeft() {
        return false;
    }

    public boolean isLargeShareVideoMode() {
        return true;
    }

    public boolean isNoVideoTileOnShareScreenEnabled() {
        return true;
    }

    public LargeShareVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        if (this.mIsGalleryVideoViewExpand) {
            createGalleryUnits();
        }
        updateShareToolbarBtnPosition();
        super.onCreateUnits();
    }

    private void createGalleryUnits() {
        if (this.mListGalleryUnits.size() <= 0) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                for (int i = 0; i <= getMaxGalleryItemsCount(); i++) {
                    VideoUnit createVideoUnit = videoObj.createVideoUnit(this.mSceneMgr.getmVideoBoxApplication(), false, createGalleryUnitInfo(i));
                    if (createVideoUnit != null) {
                        createVideoUnit.setUnitName("GalleryUnit");
                        createVideoUnit.setVideoScene(this);
                        createVideoUnit.setBorderVisible(false);
                        createVideoUnit.setBackgroundColor(0);
                        createVideoUnit.setUserNameVisible(true);
                        createVideoUnit.setCanShowAudioOff(true);
                        addUnit(createVideoUnit);
                        createVideoUnit.onCreate();
                        this.mListGalleryUnits.add(createVideoUnit);
                    }
                }
            }
        }
    }

    @NonNull
    private RendererUnitInfo createGalleryUnitInfo(int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int width = getWidth();
        int height = getHeight();
        int galleryUnitMargin = getGalleryUnitMargin();
        if (height > width) {
            i4 = getGalleryUnitPortraitWidth();
            i2 = (i4 * 9) / 16;
            int i8 = this.mCountUsers;
            if (i8 <= 3) {
                i7 = (((width - (i8 * (i4 + galleryUnitMargin))) - galleryUnitMargin) / 2) + galleryUnitMargin;
                this.mGalleryScrollPosX = 0;
            } else {
                int i9 = this.mGalleryScrollPosX;
                if (i9 < 0) {
                    i7 = (-i9) + galleryUnitMargin;
                } else {
                    i7 = galleryUnitMargin - (i9 % (i4 + galleryUnitMargin));
                }
            }
            i5 = i7 + ((galleryUnitMargin + i4) * i);
            i3 = (getHeight() - i2) - getGalleryListPortraitMargin();
            if (i == 3 && Math.abs(getWidth() - i5) < 3) {
                i5 = getWidth();
            }
        } else {
            i2 = getGalleryUnitLandscapeHeight();
            int i10 = (i2 * 16) / 9;
            if (this.mCountUsers <= getGalleryItemsLandscapeCount()) {
                i6 = (((height - (this.mCountUsers * (i2 + galleryUnitMargin))) - galleryUnitMargin) / 2) + galleryUnitMargin;
                this.mGalleryScrollPosY = 0;
            } else {
                int i11 = this.mGalleryScrollPosY;
                if (i11 < 0) {
                    i6 = (-i11) + galleryUnitMargin;
                } else {
                    i6 = galleryUnitMargin - (i11 % (i2 + galleryUnitMargin));
                }
            }
            i3 = ((galleryUnitMargin + i2) * i) + i6;
            int width2 = (getWidth() - i10) - getGalleryListLandscapeMargin();
            if (i != getGalleryItemsLandscapeCount() || Math.abs(getHeight() - i3) >= 3) {
                int i12 = width2;
                i4 = i10;
                i5 = i12;
            } else {
                i3 = getHeight();
                int i13 = width2;
                i4 = i10;
                i5 = i13;
            }
        }
        return new RendererUnitInfo(getLeft() + i5, getTop() + i3, i4, i2);
    }

    private void updateGalleryUnits() {
        if (this.mListGalleryUnits.size() != 0) {
            for (int i = 0; i <= getGalleryItemsCount(); i++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
                RendererUnitInfo createGalleryUnitInfo = createGalleryUnitInfo(i);
                if (videoUnit != null) {
                    videoUnit.updateUnitInfo(createGalleryUnitInfo);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateUnits() {
        checkUpdateGalleryUnitsVideo();
        updateGalleryViewExpandButton();
        super.onUpdateUnits();
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        this.mListGalleryUnits.clear();
        super.onDestroyUnits();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        if (!ConfMgr.getInstance().isViewOnlyMeeting()) {
            this.mCountUsers = this.mSceneMgr.getTotalVideoCount();
        } else {
            this.mCountUsers = 1;
        }
        if (this.mCountUsers < 1) {
            this.mCountUsers = 1;
        }
        runOnRendererInited(new Runnable() {
            public void run() {
                if (!LargeShareVideoScene.this.isPreloadStatus()) {
                    LargeShareVideoScene.this.checkUpdateGalleryUnitsVideo();
                }
            }
        });
        super.onStart();
    }

    public void updateContentSubscription() {
        if (!isPreloadStatus()) {
            checkUpdateGalleryUnitsVideo();
        }
        super.updateContentSubscription();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        for (int i = 0; i < this.mListGalleryUnits.size(); i++) {
            VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
            if (videoUnit != null) {
                videoUnit.removeUser();
            }
        }
        updateGalleryViewExpandButton();
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
        if (!isPreloadStatus()) {
            checkUpdateGalleryUnitsVideo();
        }
        super.onResumeVideo();
    }

    public void onUserAudioStatus(final long j) {
        super.onUserAudioStatus(j);
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeShareVideoScene.this.updateUserAudioStatus(j);
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateUserAudioStatus(long j) {
        if (this.mListGalleryUnits.size() != 0) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                for (int i = 0; i < getMaxGalleryItemsCount(); i++) {
                    VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
                    if (!(videoUnit == null || videoUnit.getUser() == 0 || !confStatusObj.isSameUser(j, videoUnit.getUser()))) {
                        videoUnit.onUserAudioStatus();
                    }
                }
            }
        }
    }

    public void onGroupUserVideoStatus(@Nullable List<Long> list) {
        super.onGroupUserVideoStatus(list);
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeShareVideoScene.this.checkUpdateGalleryUnitsVideo();
            }
        });
        super.onGroupUserVideoStatus(list);
    }

    public void onUserVideoDataSizeChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeShareVideoScene.this.checkUpdateGalleryUnitsVideo();
            }
        });
        super.onUserVideoDataSizeChanged(j);
    }

    public void onActiveVideoChanged(long j) {
        runOnRendererInited(new Runnable() {
            public void run() {
                LargeShareVideoScene.this.checkUpdateGalleryUnitsVideo();
            }
        });
        super.onActiveVideoChanged(j);
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        if (!isPreloadStatus()) {
            if (!ConfMgr.getInstance().isViewOnlyMeeting()) {
                this.mCountUsers = this.mSceneMgr.getTotalVideoCount();
            } else {
                this.mCountUsers = 1;
            }
            if (this.mCountUsers <= 3) {
                this.mGalleryScrollPosX = 0;
            }
            if (this.mCountUsers <= getGalleryItemsLandscapeCount()) {
                this.mGalleryScrollPosY = 0;
            }
            int galleryUnitMargin = getGalleryUnitMargin();
            if (getHeight() > getWidth()) {
                if (this.mCountUsers >= 3) {
                    int galleryUnitPortraitWidth = galleryUnitMargin + getGalleryUnitPortraitWidth();
                    int i2 = galleryUnitPortraitWidth * 3;
                    int i3 = this.mGalleryScrollPosX + i2;
                    int i4 = this.mCountUsers;
                    if (i3 > i4 * galleryUnitPortraitWidth) {
                        this.mGalleryScrollPosX = (i4 * galleryUnitPortraitWidth) - i2;
                    }
                    onScrollGalleryEnd();
                } else {
                    checkUpdateGalleryUnitsVideo();
                }
                this.mGalleryScrollPosY = 0;
            } else {
                if (this.mCountUsers >= getGalleryItemsLandscapeCount()) {
                    int galleryUnitLandscapeHeight = galleryUnitMargin + getGalleryUnitLandscapeHeight();
                    int galleryItemsLandscapeCount = this.mGalleryScrollPosY + (getGalleryItemsLandscapeCount() * galleryUnitLandscapeHeight);
                    int i5 = this.mCountUsers;
                    if (galleryItemsLandscapeCount > i5 * galleryUnitLandscapeHeight) {
                        this.mGalleryScrollPosY = (i5 * galleryUnitLandscapeHeight) - (galleryUnitLandscapeHeight * getGalleryItemsLandscapeCount());
                    }
                    onScrollGalleryEnd();
                } else {
                    checkUpdateGalleryUnitsVideo();
                }
                this.mGalleryScrollPosX = 0;
            }
            super.onGroupUserEvent(i, list);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00db  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void checkUpdateGalleryUnitsVideo() {
        /*
            r19 = this;
            r0 = r19
            java.util.ArrayList<com.zipow.videobox.confapp.VideoUnit> r1 = r0.mListGalleryUnits
            int r1 = r1.size()
            if (r1 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.confapp.ConfMgr r1 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfStatus r1 = r1.getConfStatusObj()
            if (r1 != 0) goto L_0x0016
            return
        L_0x0016:
            int r2 = r19.getGalleryUnitPortraitWidth()
            int r3 = r19.getGalleryUnitLandscapeHeight()
            int r4 = r19.getGalleryUnitMargin()
            com.zipow.videobox.view.video.AbsVideoSceneMgr r5 = r19.getVideoSceneMgr()
            long r5 = r5.getLockedUserId()
            r7 = 0
            r8 = 0
        L_0x002c:
            int r9 = r19.getMaxGalleryItemsCount()
            if (r8 > r9) goto L_0x0163
            java.util.ArrayList<com.zipow.videobox.confapp.VideoUnit> r9 = r0.mListGalleryUnits
            java.lang.Object r9 = r9.get(r8)
            com.zipow.videobox.confapp.VideoUnit r9 = (com.zipow.videobox.confapp.VideoUnit) r9
            com.zipow.videobox.confapp.RendererUnitInfo r10 = r0.createGalleryUnitInfo(r8)
            r9.updateUnitInfo(r10)
            int r11 = r19.getHeight()
            int r12 = r19.getWidth()
            if (r11 <= r12) goto L_0x005d
            int r11 = r0.mGalleryScrollPosX
            if (r11 < 0) goto L_0x0058
            int r12 = r2 + r4
            int r11 = r11 / r12
            int r11 = r11 + r8
            long r11 = r0.getGalleryUnitUserId(r11)
            goto L_0x006e
        L_0x0058:
            long r11 = r0.getGalleryUnitUserId(r8)
            goto L_0x006e
        L_0x005d:
            int r11 = r0.mGalleryScrollPosY
            if (r11 < 0) goto L_0x006a
            int r12 = r3 + r4
            int r11 = r11 / r12
            int r11 = r11 + r8
            long r11 = r0.getGalleryUnitUserId(r11)
            goto L_0x006e
        L_0x006a:
            long r11 = r0.getGalleryUnitUserId(r8)
        L_0x006e:
            boolean r13 = r19.isVideoPaused()
            r15 = 0
            r14 = 1
            if (r13 != 0) goto L_0x0138
            int r13 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1))
            if (r13 != 0) goto L_0x0089
            r9.stopVideo(r14)
            r9.removeUser()
            r9.setBorderVisible(r7)
            r9.setBackgroundColor(r7)
            goto L_0x015f
        L_0x0089:
            int r13 = r19.getHeight()
            int r14 = r19.getWidth()
            r15 = 2
            if (r13 <= r14) goto L_0x00b3
            int r13 = r10.left
            int r14 = r10.width
            int r14 = -r14
            int r14 = r14 * 2
            int r14 = r14 / 3
            if (r13 < r14) goto L_0x00b1
            int r13 = r10.left
            int r14 = r10.width
            int r13 = r13 + r14
            int r14 = r19.getWidth()
            int r10 = r10.width
            int r10 = r10 * 2
            int r10 = r10 / 3
            int r14 = r14 + r10
            if (r13 <= r14) goto L_0x00d1
        L_0x00b1:
            r10 = 1
            goto L_0x00d4
        L_0x00b3:
            int r13 = r10.top
            int r14 = r10.height
            int r14 = -r14
            int r14 = r14 * 2
            int r14 = r14 / 3
            if (r13 < r14) goto L_0x00d3
            int r13 = r10.top
            int r14 = r10.height
            int r13 = r13 + r14
            int r14 = r19.getHeight()
            int r10 = r10.height
            int r10 = r10 * 2
            int r10 = r10 / 3
            int r14 = r14 + r10
            if (r13 <= r14) goto L_0x00d1
            goto L_0x00d3
        L_0x00d1:
            r10 = 0
            goto L_0x00d4
        L_0x00d3:
            r10 = 1
        L_0x00d4:
            if (r10 == 0) goto L_0x00db
            r9.removeUser()
            r10 = 1
            goto L_0x012f
        L_0x00db:
            boolean r10 = r0.mIsScrollingGallery
            if (r10 == 0) goto L_0x00e3
            r9.pause()
            goto L_0x00e6
        L_0x00e3:
            r9.resume()
        L_0x00e6:
            r9.setType(r7)
            r9.setUser(r11)
            com.zipow.videobox.view.video.AbsVideoSceneMgr r10 = r19.getVideoSceneMgr()
            long r13 = r10.getActiveSpeakerId()
            com.zipow.videobox.confapp.ConfMgr r10 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmUser r10 = r10.getUserById(r13)
            if (r10 == 0) goto L_0x0102
            long r13 = r10.getNodeId()
        L_0x0102:
            com.zipow.videobox.confapp.ConfMgr r10 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            boolean r10 = r10.isViewOnlyMeeting()
            if (r10 != 0) goto L_0x012b
            r17 = 0
            int r10 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r10 != 0) goto L_0x011d
            boolean r10 = r1.isSameUser(r11, r13)
            if (r10 == 0) goto L_0x011d
            r10 = 1
            r9.setBorderType(r10)
            goto L_0x012f
        L_0x011d:
            int r10 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r10 != 0) goto L_0x0126
            r9.setBorderType(r15)
            r10 = 1
            goto L_0x012f
        L_0x0126:
            r9.setBorderType(r7)
            r10 = 1
            goto L_0x012f
        L_0x012b:
            r9.setBorderType(r7)
            r10 = 1
        L_0x012f:
            r9.setBorderVisible(r10)
            r10 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r9.setBackgroundColor(r10)
            goto L_0x015f
        L_0x0138:
            r10 = 1
            long r13 = r9.getUser()
            r15 = 0
            int r13 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r13 == 0) goto L_0x0146
            r9.removeUser()
        L_0x0146:
            r9.clearRenderer()
            r9.setBorderVisible(r10)
            int r10 = (r11 > r15 ? 1 : (r11 == r15 ? 0 : -1))
            if (r10 != 0) goto L_0x0157
            r9.setBorderVisible(r7)
            r9.setBackgroundColor(r7)
            goto L_0x015f
        L_0x0157:
            r10 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r9.setBackgroundColor(r10)
            r9.setBorderType(r7)
        L_0x015f:
            int r8 = r8 + 1
            goto L_0x002c
        L_0x0163:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.video.LargeShareVideoScene.checkUpdateGalleryUnitsVideo():void");
    }

    private long getGalleryUnitUserId(int i) {
        if (ConfMgr.getInstance().isViewOnlyMeeting()) {
            return i == 0 ? 1 : 0;
        }
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

    public void onDoubleTap(@NonNull MotionEvent motionEvent) {
        if (!isOnGallery(motionEvent)) {
            super.onDoubleTap(motionEvent);
        }
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (this.mIsScrollingGallery && motionEvent.getPointerCount() == 1 && motionEvent.getActionMasked() == 1) {
            onScrollGalleryEnd();
        }
        return false;
    }

    public boolean canDragSceneToRight() {
        return !this.mIsScrollingGallery && super.canDragSceneToRight();
    }

    public void onScroll(MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
        if (!isOnGallery(motionEvent2)) {
            super.onScroll(motionEvent, motionEvent2, f, f2);
        } else if (getHeight() > getWidth()) {
            onScrollGalleryX((int) f);
        } else {
            onScrollGalleryY((int) f2);
        }
    }

    public void onFling(@NonNull MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!isOnGallery(motionEvent)) {
            super.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    private void onScrollGalleryX(int i) {
        if (this.mCountUsers <= 3) {
            if (this.mIsScrollingGallery) {
                onScrollGalleryEnd();
            }
            return;
        }
        int i2 = this.mGalleryScrollPosX;
        this.mGalleryScrollPosX = i + i2;
        if (this.mGalleryScrollPosX < 0) {
            this.mGalleryScrollPosX = 0;
        }
        int galleryUnitMargin = (this.mCountUsers - 3) * (getGalleryUnitMargin() + getGalleryUnitPortraitWidth());
        if (this.mGalleryScrollPosX > galleryUnitMargin) {
            this.mGalleryScrollPosX = galleryUnitMargin;
        }
        int i3 = this.mGalleryScrollPosX;
        if (i2 != i3) {
            int i4 = i3 - i2;
            int i5 = 0;
            for (int i6 = 0; i6 <= 3; i6++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i6);
                if (videoUnit.getRight() - i4 <= 0) {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    i5++;
                } else if (this.mGalleryScrollPosX > 0 && videoUnit.getLeft() - i4 > getWidth()) {
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
                    arrayList2.add(0, arrayList2.remove(3));
                }
            }
            checkUpdateGalleryUnitsVideo();
            this.mIsScrollingGallery = true;
        }
    }

    private void onScrollGalleryY(int i) {
        if (this.mCountUsers <= getGalleryItemsLandscapeCount()) {
            if (this.mIsScrollingGallery) {
                onScrollGalleryEnd();
            }
            return;
        }
        int i2 = this.mGalleryScrollPosY;
        this.mGalleryScrollPosY = i + i2;
        if (this.mGalleryScrollPosY < 0) {
            this.mGalleryScrollPosY = 0;
        }
        int galleryItemsLandscapeCount = (this.mCountUsers - getGalleryItemsLandscapeCount()) * (getGalleryUnitMargin() + getGalleryUnitLandscapeHeight());
        if (this.mGalleryScrollPosY > galleryItemsLandscapeCount) {
            this.mGalleryScrollPosY = galleryItemsLandscapeCount;
        }
        int i3 = this.mGalleryScrollPosY;
        if (i2 != i3) {
            int i4 = i3 - i2;
            int i5 = 0;
            for (int i6 = 0; i6 <= getGalleryItemsLandscapeCount(); i6++) {
                VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i6);
                if (videoUnit.getBottom() - i4 <= 0) {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    i5++;
                } else if (this.mGalleryScrollPosY > 0 && videoUnit.getTop() - i4 > getHeight()) {
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
                    arrayList2.add(0, arrayList2.remove(getGalleryItemsLandscapeCount()));
                }
            }
            checkUpdateGalleryUnitsVideo();
            this.mIsScrollingGallery = true;
        }
    }

    private boolean isOnGallery(@NonNull MotionEvent motionEvent) {
        boolean z = false;
        if (this.mListGalleryUnits.size() == 0) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(0);
        if (videoUnit == null) {
            return false;
        }
        if (getHeight() > getWidth()) {
            if (y >= ((float) videoUnit.getTop()) && y < ((float) videoUnit.getBottom())) {
                z = true;
            }
            return z;
        }
        if (x >= ((float) videoUnit.getLeft()) && x < ((float) videoUnit.getRight())) {
            z = true;
        }
        return z;
    }

    private void onScrollGalleryEnd() {
        if (this.mListGalleryUnits.size() != 0) {
            int galleryUnitMargin = getGalleryUnitMargin();
            this.mIsScrollingGallery = false;
            VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(getGalleryItemsCount());
            VideoUnit videoUnit2 = (VideoUnit) this.mListGalleryUnits.get(0);
            VideoUnit videoUnit3 = null;
            int galleryItemsCount = getGalleryItemsCount();
            while (true) {
                if (galleryItemsCount < 0) {
                    break;
                }
                VideoUnit videoUnit4 = (VideoUnit) this.mListGalleryUnits.get(galleryItemsCount);
                if (videoUnit4.isBorderVisible()) {
                    videoUnit3 = videoUnit4;
                    break;
                }
                galleryItemsCount--;
            }
            if (getHeight() > getWidth()) {
                int width = videoUnit2.getWidth();
                if (videoUnit3 != null && videoUnit3.getRight() < (galleryUnitMargin + width) * 3) {
                    scrollGalleryToUnitAtX(this.mCountUsers - 3, galleryUnitMargin, width);
                } else if (videoUnit2.getLeft() > galleryUnitMargin) {
                    scrollGalleryToUnitAtX(0, galleryUnitMargin, width);
                } else if (galleryUnitMargin - videoUnit2.getLeft() > (width * 3) / 4) {
                    videoUnit2.stopVideo(true);
                    videoUnit2.removeUser();
                    scrollGalleryToUnitAtX(((videoUnit2.getRight() + galleryUnitMargin) + this.mGalleryScrollPosX) / (galleryUnitMargin + width), galleryUnitMargin, width);
                    ArrayList<VideoUnit> arrayList = this.mListGalleryUnits;
                    arrayList.add(arrayList.remove(0));
                } else {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    scrollGalleryToUnitAtX((videoUnit2.getLeft() + this.mGalleryScrollPosX) / (galleryUnitMargin + width), galleryUnitMargin, width);
                }
            } else {
                int height = videoUnit2.getHeight();
                if (videoUnit3 != null && videoUnit3.getBottom() < getGalleryItemsLandscapeCount() * (galleryUnitMargin + height)) {
                    scrollGalleryToUnitAtY(this.mCountUsers - getGalleryItemsLandscapeCount(), galleryUnitMargin, height);
                } else if (videoUnit2.getTop() > galleryUnitMargin) {
                    scrollGalleryToUnitAtY(0, galleryUnitMargin, height);
                } else if (galleryUnitMargin - videoUnit2.getTop() > (height * 3) / 4) {
                    videoUnit2.stopVideo(true);
                    videoUnit2.removeUser();
                    scrollGalleryToUnitAtY(((videoUnit2.getBottom() + galleryUnitMargin) + this.mGalleryScrollPosY) / (galleryUnitMargin + height), galleryUnitMargin, height);
                    ArrayList<VideoUnit> arrayList2 = this.mListGalleryUnits;
                    arrayList2.add(arrayList2.remove(0));
                } else {
                    videoUnit.stopVideo(true);
                    videoUnit.removeUser();
                    scrollGalleryToUnitAtY((videoUnit2.getTop() + this.mGalleryScrollPosY) / (galleryUnitMargin + height), galleryUnitMargin, height);
                }
            }
            checkUpdateGalleryUnitsVideo();
        }
    }

    private void scrollGalleryToUnitAtX(int i, int i2, int i3) {
        this.mGalleryScrollPosX = i * (i2 + i3);
    }

    private void scrollGalleryToUnitAtY(int i, int i2, int i3) {
        this.mGalleryScrollPosY = i * (i2 + i3);
    }

    public int getShareRenderHeight() {
        int height = getHeight();
        if (getHeight() <= getWidth()) {
            return height;
        }
        if (this.mIsGalleryVideoViewExpand) {
            return ((height - getGalleryActionBarDip()) - ((getGalleryUnitPortraitWidth() * 9) / 16)) - (getGalleryListPortraitMargin() * 2);
        }
        return height - getGalleryActionBarDip();
    }

    public int getShareRenderWidth() {
        int width = getWidth();
        if (getHeight() >= getWidth()) {
            return width;
        }
        if (this.mIsGalleryVideoViewExpand) {
            return ((width - getGalleryActionBarDip()) - ((getGalleryUnitLandscapeHeight() * 16) / 9)) - (getGalleryListLandscapeMargin() * 2);
        }
        return width - getGalleryActionBarDip();
    }

    private int getGalleryActionBarDip() {
        return UIUtil.dip2px(getConfActivity(), 20.0f);
    }

    private int getGalleryUnitMargin() {
        return UIUtil.dip2px(getConfActivity(), 3.0f);
    }

    private int getGalleryListLandscapeMargin() {
        return UIUtil.dip2px(getConfActivity(), 2.0f);
    }

    private int getGalleryListPortraitMargin() {
        return UIUtil.dip2px(getConfActivity(), 4.0f);
    }

    private int getGalleryUnitPortraitWidth() {
        return (getWidth() - (getGalleryUnitMargin() * 4)) / 3;
    }

    private int getGalleryUnitLandscapeHeight() {
        return (getHeight() - (getGalleryUnitMargin() * (getGalleryItemsLandscapeCount() + 1))) / getGalleryItemsLandscapeCount();
    }

    private int getGalleryItemsCount() {
        if (getHeight() > getWidth()) {
            return 3;
        }
        return getGalleryItemsLandscapeCount();
    }

    private int getGalleryItemsLandscapeCount() {
        return UIUtil.isTabletOrTV(getConfActivity()) ? 6 : 4;
    }

    private int getMaxGalleryItemsCount() {
        return UIUtil.isTabletOrTV(getConfActivity()) ? 6 : 4;
    }

    public void onShareUserReceivingStatus(long j) {
        super.onShareUserReceivingStatus(j);
        updateGalleryViewExpandButton();
    }

    private void updateGalleryViewExpandButton() {
        View findViewById = getConfActivity().findViewById(C4558R.C4560id.panelShareGalleryExpandPortView);
        ImageView imageView = (ImageView) getConfActivity().findViewById(C4558R.C4560id.galleryViewExpandArrowImgPort);
        View findViewById2 = getConfActivity().findViewById(C4558R.C4560id.panelShareGalleryExpandLandView);
        ImageView imageView2 = (ImageView) getConfActivity().findViewById(C4558R.C4560id.galleryViewExpandArrowImgLand);
        if (!isVisible() || !isStarted() || !hasContent()) {
            findViewById.setOnClickListener(null);
            findViewById2.setOnClickListener(null);
            findViewById2.setVisibility(8);
            findViewById.setVisibility(8);
        } else if (getHeight() > getWidth()) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) findViewById.getLayoutParams();
            marginLayoutParams.topMargin = getTop() + getShareRenderHeight();
            findViewById.setLayoutParams(marginLayoutParams);
            findViewById.getParent().requestLayout();
            if (this.mIsGalleryVideoViewExpand) {
                imageView.setImageResource(C4558R.C4559drawable.zm_arrow_down_normal);
            } else {
                imageView.setImageResource(C4558R.C4559drawable.zm_arrow_up_normal);
            }
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(this.ExpandGalleryViewlistener);
            findViewById2.setVisibility(8);
        } else {
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) findViewById2.getLayoutParams();
            marginLayoutParams2.leftMargin = getLeft() + getShareRenderWidth();
            findViewById2.setLayoutParams(marginLayoutParams2);
            findViewById2.getParent().requestLayout();
            if (this.mIsGalleryVideoViewExpand) {
                imageView2.setImageResource(C4558R.C4559drawable.zm_arrow_right_normal);
            } else {
                imageView2.setImageResource(C4558R.C4559drawable.zm_arrow_left_normal);
            }
            findViewById2.setVisibility(0);
            findViewById2.setOnClickListener(this.ExpandGalleryViewlistener);
            findViewById.setVisibility(8);
        }
    }

    private void updateShareToolbarBtnPosition() {
        ShareView shareView = (ShareView) getConfActivity().findViewById(C4558R.C4560id.sharingView);
        if (shareView != null) {
            shareView.setToolbarBtnPosition(5, (getShareRenderHeight() - getHeight()) - 70);
        }
    }

    /* access modifiers changed from: private */
    public void showOrHideGalleryVideoUnits(boolean z) {
        if (this.mIsGalleryVideoViewExpand != z) {
            this.mIsGalleryVideoViewExpand = z;
            updateGalleryViewExpandButton();
            if (!this.mIsGalleryVideoViewExpand) {
                destroyGalleryVideoUnits();
            } else {
                createGalleryUnits();
            }
            onUpdateUnits();
        }
    }

    private void destroyGalleryVideoUnits() {
        for (int i = 0; i < this.mListGalleryUnits.size(); i++) {
            VideoUnit videoUnit = (VideoUnit) this.mListGalleryUnits.get(i);
            if (videoUnit != null) {
                videoUnit.removeUser();
                videoUnit.onDestroy();
                removeUnit(videoUnit);
            }
        }
        this.mListGalleryUnits.clear();
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        super.onGLRendererChanged(videoRenderer, i, i2);
        updateShareToolbarBtnPosition();
    }
}
