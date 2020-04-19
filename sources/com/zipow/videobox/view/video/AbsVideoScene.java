package com.zipow.videobox.view.video;

import android.graphics.Rect;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoSize;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.GLButton;
import com.zipow.videobox.confapp.GLImage;
import com.zipow.videobox.confapp.IRendererUnit;
import com.zipow.videobox.confapp.IVideoUnit;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.VideoUnit;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbsVideoScene {
    static final int SWITCH_SCENE_PANEL_BOTTOM_MARGIN = 45;
    private static final String TAG = "AbsVideoScene";
    private boolean mCacheEnabled = false;
    @NonNull
    private LinkedList<Runnable> mCachedTasks = new LinkedList<>();
    private int mHeight = 0;
    private int mLeft = 0;
    @NonNull
    protected final AbsVideoSceneMgr mSceneMgr;
    private int mTop = 0;
    @NonNull
    private List<IRendererUnit> mUnitsGranted = new ArrayList();
    @NonNull
    private List<IRendererUnit> mVideoUnits = new ArrayList();
    private int mWidth = 0;
    private boolean mbCreated = false;
    private boolean mbPreloadEnabled = false;
    private boolean mbStarted = false;
    private boolean mbVideoPaused = false;
    private boolean mbVisible = false;

    public void afterSwitchCamera() {
    }

    public void beforeSwitchCamera() {
    }

    public void cacheUnits() {
    }

    public void destroyCachedUnits() {
    }

    public int getAccessbilityViewIndexAt(float f, float f2) {
        return -1;
    }

    @NonNull
    public CharSequence getAccessibilityDescriptionForIndex(int i) {
        return "";
    }

    public void getAccessibilityVisibleVirtualViews(List<Integer> list) {
    }

    public long getPreviewRenderInfo() {
        return 0;
    }

    public void onActiveVideoChanged(long j) {
    }

    public void onAudioTypeChanged(long j) {
    }

    public void onAutoStartVideo() {
    }

    public void onConfOne2One() {
    }

    public void onConfReady() {
    }

    public void onConfVideoSendingStatusChanged() {
    }

    /* access modifiers changed from: protected */
    public abstract void onCreateUnits();

    /* access modifiers changed from: protected */
    public abstract void onDestroyUnits();

    public void onDoubleTap(MotionEvent motionEvent) {
    }

    public void onDown(MotionEvent motionEvent) {
    }

    /* access modifiers changed from: protected */
    public void onDraggingIn() {
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    /* access modifiers changed from: protected */
    public void onGrantedUnitsDestroyed() {
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
    }

    public void onHostChanged(long j, boolean z) {
    }

    public void onLaunchConfParamReady() {
    }

    public void onMyVideoRotationChanged(int i) {
    }

    public void onMyVideoStatusChanged() {
    }

    /* access modifiers changed from: protected */
    public void onNetworkRestrictionModeChanged(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void onPauseVideo() {
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
    }

    public void onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
    }

    public void onShareActiveUser(long j) {
    }

    public void onShareDataSizeChanged(long j) {
    }

    public void onShareUserReceivingStatus(long j) {
    }

    public void onShareUserSendingStatus(long j) {
    }

    /* access modifiers changed from: protected */
    public abstract void onStart();

    /* access modifiers changed from: protected */
    public abstract void onStop();

    /* access modifiers changed from: protected */
    public abstract void onUpdateUnits();

    public void onUserActiveAudio(long j) {
    }

    public void onUserActiveVideoForDeck(long j) {
    }

    public void onUserAudioStatus(long j) {
    }

    public void onUserCountChangesForShowHideAction() {
    }

    public void onUserPicReady(long j) {
    }

    public void onUserVideoDataSizeChanged(long j) {
    }

    public void onUserVideoQualityChanged(long j) {
    }

    public boolean onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    public void updateAccessibilitySceneDescription() {
    }

    public void updateContentSubscription() {
    }

    public AbsVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        this.mSceneMgr = absVideoSceneMgr;
    }

    @NonNull
    public AbsVideoSceneMgr getVideoSceneMgr() {
        return this.mSceneMgr;
    }

    public boolean isVisible() {
        return this.mbVisible;
    }

    public void setVisible(boolean z) {
        this.mbVisible = z;
    }

    public boolean isCreated() {
        return this.mbCreated;
    }

    public boolean isStarted() {
        return this.mbStarted;
    }

    public void setPreloadEnabled(boolean z) {
        this.mbPreloadEnabled = z;
    }

    public boolean isPreloadEnabled() {
        return this.mbPreloadEnabled;
    }

    public void setCacheEnabled(boolean z) {
        this.mCacheEnabled = z;
    }

    public boolean isCachedEnabled() {
        return this.mCacheEnabled;
    }

    public boolean hasUnits() {
        return this.mVideoUnits.size() > 0;
    }

    public void addUnit(IRendererUnit iRendererUnit) {
        this.mVideoUnits.add(iRendererUnit);
    }

    public void removeUnit(IRendererUnit iRendererUnit) {
        this.mVideoUnits.remove(iRendererUnit);
    }

    public boolean hasUnit(IRendererUnit iRendererUnit) {
        return this.mVideoUnits.indexOf(iRendererUnit) >= 0;
    }

    public void start() {
        if (!this.mbStarted) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext == null || !confContext.inSilentMode()) {
                this.mbStarted = true;
                onStart();
            }
        }
    }

    public void stop() {
        if (this.mbStarted) {
            this.mbStarted = false;
            onStop();
        }
    }

    public void reStart() {
        stop();
        start();
    }

    public void pauseVideo() {
        this.mbVideoPaused = true;
        for (IRendererUnit iRendererUnit : this.mVideoUnits) {
            if (iRendererUnit != null) {
                iRendererUnit.pause();
            }
        }
        onPauseVideo();
    }

    public void resumeVideo() {
        this.mbVideoPaused = false;
        for (IRendererUnit iRendererUnit : this.mVideoUnits) {
            if (iRendererUnit != null) {
                iRendererUnit.resume();
            }
        }
        onResumeVideo();
    }

    public boolean isVideoPaused() {
        return this.mbVideoPaused;
    }

    public void onConfUIRelayout(ConfActivity confActivity) {
        updateUnits();
    }

    public void updateUnits() {
        onUpdateUnits();
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        if (this.mWidth == 0 && this.mHeight == 0) {
            doCachedTasks();
            create(i, i2);
            return;
        }
        for (IRendererUnit iRendererUnit : this.mVideoUnits) {
            if (iRendererUnit != null) {
                iRendererUnit.onGLViewSizeChanged(i, i2);
            }
        }
        update(i, i2);
    }

    public void create(int i, int i2) {
        create(i, i2, true);
    }

    public void create(int i, int i2, boolean z) {
        if (!this.mbCreated) {
            if (z) {
                this.mLeft = 0;
                this.mTop = 0;
            }
            this.mWidth = i;
            this.mHeight = i2;
            if (this.mWidth > 0 && this.mHeight > 0) {
                onCreateUnits();
                this.mbCreated = true;
                if (isStarted()) {
                    onStart();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void update(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        if (this.mWidth > 0 && this.mHeight > 0) {
            onUpdateUnits();
        }
    }

    public void destroy() {
        destroy(false);
    }

    public void destroy(boolean z) {
        if (this.mbCreated) {
            if (hasGrantedUnits()) {
                stopAndDestroyAllGrantedUnits();
            }
            if (this.mVideoUnits.size() > 0) {
                for (IRendererUnit iRendererUnit : this.mVideoUnits) {
                    if (iRendererUnit != null) {
                        iRendererUnit.onDestroy();
                    }
                }
                this.mVideoUnits.clear();
            }
            onDestroyUnits();
            this.mbCreated = false;
            this.mbStarted = false;
            this.mLeft = 0;
            this.mTop = 0;
            this.mWidth = 0;
            this.mHeight = 0;
            if (!z && isPreloadEnabled()) {
                ConfActivity confActivity = getConfActivity();
                if (confActivity != null && confActivity.isActive()) {
                    preload();
                }
            }
        }
    }

    public void onIdle() {
        for (int i = 0; i < this.mVideoUnits.size(); i++) {
            IRendererUnit iRendererUnit = (IRendererUnit) this.mVideoUnits.get(i);
            if (iRendererUnit != null) {
                iRendererUnit.onIdle();
            }
        }
    }

    public void onGroupUserVideoStatus(@Nullable List<Long> list) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null && list != null) {
            for (Long longValue : list) {
                long longValue2 = longValue.longValue();
                for (IRendererUnit iRendererUnit : this.mVideoUnits) {
                    if (iRendererUnit instanceof VideoUnit) {
                        VideoUnit videoUnit = (VideoUnit) iRendererUnit;
                        if (videoObj.isSameVideo(videoUnit.getUser(), longValue2)) {
                            videoUnit.onUserVideoStatus();
                        }
                    }
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isVideoPaused()) {
            return false;
        }
        for (IRendererUnit iRendererUnit : this.mVideoUnits) {
            if (iRendererUnit instanceof GLButton) {
                if (((GLButton) iRendererUnit).onTouchEvent(motionEvent)) {
                    return true;
                }
            } else if ((iRendererUnit instanceof GLImage) && ((GLImage) iRendererUnit).onTouchEvent(motionEvent)) {
                return true;
            }
        }
        return false;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getRight() {
        return this.mLeft + this.mWidth;
    }

    public int getBottom() {
        return this.mTop + this.mHeight;
    }

    public void move(int i, int i2) {
        this.mLeft += i;
        this.mTop += i2;
        if (isCreated() && this.mWidth > 0 && this.mHeight > 0) {
            onUpdateUnits();
        }
    }

    public void setLocation(int i, int i2) {
        this.mLeft = i;
        this.mTop = i2;
        if (isCreated() && this.mWidth > 0 && this.mHeight > 0) {
            onUpdateUnits();
        }
    }

    @Nullable
    public ConfActivity getConfActivity() {
        return this.mSceneMgr.getConfActivity();
    }

    public VideoRenderer getVideoRenderer() {
        return this.mSceneMgr.getVideoRenderer();
    }

    public void runOnRendererInited(@Nullable Runnable runnable) {
        if (runnable != null) {
            VideoRenderer videoRenderer = getVideoSceneMgr().getVideoRenderer();
            if (videoRenderer != null) {
                if (videoRenderer.isInitialized()) {
                    runnable.run();
                } else {
                    cacheTask(runnable);
                }
            }
        }
    }

    private void cacheTask(Runnable runnable) {
        this.mCachedTasks.add(runnable);
    }

    private void doCachedTasks() {
        Iterator it = this.mCachedTasks.iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
        this.mCachedTasks.clear();
    }

    @NonNull
    public VideoSize getUserVideoSize(long j) {
        VideoSize videoSize = new VideoSize(100, 100);
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        if (userById == null) {
            return videoSize;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return videoSize;
        }
        CmmVideoStatus videoStatusObj = userById.getVideoStatusObj();
        if (videoStatusObj == null) {
            return videoSize;
        }
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return videoSize;
        }
        if (confStatusObj.isSameUser(j, myself.getNodeId())) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj == null) {
                return videoSize;
            }
            videoSize = (videoObj.isVideoStarted() || videoObj.isPreviewing()) ? videoObj.getMyVideoSize() : new VideoSize(100, 100);
        } else if (!videoStatusObj.getIsSending()) {
            return videoSize;
        } else {
            CmmVideoStatus videoStatusObj2 = userById.getVideoStatusObj();
            boolean z = videoStatusObj2 != null && videoStatusObj2.getIsSending();
            if (userById.isPureCallInUser() || (userById.isH323User() && !z)) {
                videoSize.height = 100;
                videoSize.width = 100;
            } else {
                long resolution = videoStatusObj.getResolution();
                videoSize.height = ((int) (-65536 & resolution)) >> 16;
                videoSize.width = (int) (resolution & 65535);
            }
        }
        if (videoSize.width <= 0 || videoSize.height <= 0) {
            videoSize.width = 16;
            videoSize.height = 9;
        }
        return videoSize;
    }

    public void grantUnitsTo(@NonNull AbsVideoScene absVideoScene) {
        for (IRendererUnit iRendererUnit : this.mVideoUnits) {
            if (iRendererUnit != null) {
                absVideoScene.mUnitsGranted.add(iRendererUnit);
            }
        }
        this.mVideoUnits.clear();
    }

    public boolean hasGrantedUnits() {
        return this.mUnitsGranted.size() > 0;
    }

    public void stopAndDestroyAllGrantedUnits() {
        for (IRendererUnit iRendererUnit : this.mUnitsGranted) {
            if (iRendererUnit instanceof IVideoUnit) {
                ((IVideoUnit) iRendererUnit).removeUser();
            }
        }
        for (IRendererUnit iRendererUnit2 : this.mUnitsGranted) {
            if (iRendererUnit2 != null) {
                iRendererUnit2.onDestroy();
            }
        }
        this.mUnitsGranted.clear();
        onGrantedUnitsDestroyed();
    }

    public void preload() {
        if (!hasUnits()) {
            setPreloadEnabled(true);
            runOnRendererInited(new Runnable() {
                public void run() {
                    AbsVideoScene.this.setLocation(Integer.MIN_VALUE, 0);
                    AbsVideoScene absVideoScene = AbsVideoScene.this;
                    absVideoScene.create(absVideoScene.getVideoRenderer().getWidth(), AbsVideoScene.this.getVideoRenderer().getHeight(), false);
                    AbsVideoScene.this.pauseVideo();
                    AbsVideoScene.this.start();
                }
            });
        }
    }

    public boolean isPreloadStatus() {
        return isPreloadEnabled() && this.mbVideoPaused;
    }

    /* access modifiers changed from: protected */
    public boolean isInTargetRange(@NonNull MotionEvent motionEvent, @Nullable IVideoUnit iVideoUnit) {
        if (iVideoUnit == null || iVideoUnit.getUser() <= 0) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x <= ((float) iVideoUnit.getLeft()) || x >= ((float) (iVideoUnit.getLeft() + iVideoUnit.getWidth())) || y <= ((float) iVideoUnit.getTop()) || y >= ((float) (iVideoUnit.getTop() + iVideoUnit.getHeight()))) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public RendererUnitInfo createBigUnitInfo() {
        return new RendererUnitInfo(getLeft(), getTop(), getWidth(), getHeight());
    }

    @NonNull
    public Rect getBoundsForAccessbilityViewIndex(int i) {
        return new Rect();
    }
}
