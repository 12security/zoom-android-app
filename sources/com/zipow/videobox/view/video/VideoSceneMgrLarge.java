package com.zipow.videobox.view.video;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;

public class VideoSceneMgrLarge extends AbsVideoSceneMgr {
    private LargeVideoScene mLargeVideoScene = new LargeVideoScene(this);

    public void attendeeVideoControlChange(long j) {
    }

    public void attendeeVideoLayoutChange(long j) {
    }

    public void attendeeVideoLayoutFlagChange(long j) {
    }

    public void scrollHorizontal(boolean z) {
    }

    public VideoSceneMgrLarge(@NonNull VideoBoxApplication videoBoxApplication) {
        super(videoBoxApplication);
        this.mLargeVideoScene.setVisible(true);
        this.mSceneList.add(this.mLargeVideoScene);
    }

    public boolean isViewingSharing() {
        return getShareActiveUserId() > 0;
    }

    public AbsVideoScene getActiveScene() {
        return this.mLargeVideoScene;
    }

    public void onScroll(MotionEvent motionEvent, @NonNull MotionEvent motionEvent2, float f, float f2) {
        this.mLargeVideoScene.onScroll(motionEvent, motionEvent2, f, f2);
    }

    public void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.mLargeVideoScene.onFling(motionEvent, motionEvent2, f, f2);
    }

    public void onDown(MotionEvent motionEvent) {
        this.mLargeVideoScene.onDown(motionEvent);
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        return this.mLargeVideoScene.onTouchEvent(motionEvent);
    }

    public void onDoubleTap(@NonNull MotionEvent motionEvent) {
        this.mLargeVideoScene.onDoubleTap(motionEvent);
    }

    public boolean onVideoViewSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        return this.mLargeVideoScene.onVideoViewSingleTapConfirmed(motionEvent);
    }

    public boolean onNetworkRestrictionModeChanged(boolean z) {
        boolean onNetworkRestrictionModeChanged = super.onNetworkRestrictionModeChanged(z);
        if (onNetworkRestrictionModeChanged) {
            this.mLargeVideoScene.onNetworkRestrictionModeChanged(z);
        }
        return onNetworkRestrictionModeChanged;
    }
}
