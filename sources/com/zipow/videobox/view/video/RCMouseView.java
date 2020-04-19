package com.zipow.videobox.view.video;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.ConfActivityNormal;

public class RCMouseView extends ImageView {
    @Nullable
    private AbsVideoSceneMgr mAbsVideoSceneMgr;
    private ConfActivityNormal mActivity;
    private float mClickXRelativeToRCFloatView;
    private float mClickYRelativeToRCFloatView;
    private Handler mHandler;
    private long mPrevMouseDragTime;

    @RequiresApi(api = 21)
    public RCMouseView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public RCMouseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public RCMouseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public RCMouseView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (ConfActivityNormal) context;
        this.mHandler = new Handler();
    }

    public void moveMouse(float f, float f2) {
        View view = (View) getParent();
        if (view != null) {
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.leftMargin = ((int) f) - iArr[0];
            layoutParams.topMargin = ((int) f2) - iArr[1];
            setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    public void moveRCMouse() {
        ConfActivityNormal confActivityNormal = this.mActivity;
        if (confActivityNormal != null) {
            this.mAbsVideoSceneMgr = confActivityNormal.getVideoSceneMgr();
            AbsVideoSceneMgr absVideoSceneMgr = this.mAbsVideoSceneMgr;
            if (absVideoSceneMgr != null) {
                AbsVideoScene activeScene = absVideoSceneMgr.getActiveScene();
                if (activeScene instanceof ShareVideoScene) {
                    ShareVideoScene shareVideoScene = (ShareVideoScene) activeScene;
                    if (shareVideoScene.isStarted() && shareVideoScene.isInRemoteControlMode()) {
                        shareVideoScene.remoteControlSingleMove((float) getLeft(), (float) getTop());
                    }
                }
            }
        }
    }

    public void showRCMouse(boolean z) {
        if (z) {
            int width = this.mActivity.getVideoSceneMgr().getActiveScene().getWidth();
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.topMargin = this.mActivity.getVideoSceneMgr().getActiveScene().getHeight() / 2;
            layoutParams.leftMargin = width / 2;
            setLayoutParams(layoutParams);
            setVisibility(0);
            return;
        }
        setVisibility(8);
    }

    private boolean correctPostion(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE, Integer.MIN_VALUE);
        measure(makeMeasureSpec, makeMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        View view = (View) getParent();
        if (view == null) {
            return false;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (i < 0) {
            i = 0;
        }
        if (i2 < 0) {
            i2 = 0;
        }
        if (i + measuredWidth > width) {
            i = width - measuredWidth;
        }
        if (i2 + measuredHeight > height) {
            i2 = height - measuredHeight;
        }
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams == null) {
            return false;
        }
        if (layoutParams.topMargin == i2 && layoutParams.leftMargin == i) {
            return false;
        }
        layoutParams.topMargin = i2;
        layoutParams.leftMargin = i;
        setLayoutParams(layoutParams);
        return true;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            int[] iArr = new int[2];
            getLocationOnScreen(iArr);
            this.mClickXRelativeToRCFloatView = motionEvent.getRawX() - ((float) iArr[0]);
            this.mClickYRelativeToRCFloatView = motionEvent.getRawY() - ((float) iArr[1]);
        } else if (motionEvent.getActionMasked() == 2) {
            boolean correctPostion = correctPostion((int) (motionEvent.getRawX() - this.mClickXRelativeToRCFloatView), (int) (motionEvent.getRawY() - this.mClickYRelativeToRCFloatView));
            long currentTimeMillis = System.currentTimeMillis();
            if (correctPostion && currentTimeMillis - this.mPrevMouseDragTime > 200) {
                this.mPrevMouseDragTime = currentTimeMillis;
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        RCMouseView.this.moveRCMouse();
                    }
                }, 200);
            }
        } else if (motionEvent.getActionMasked() == 1) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    RCMouseView.this.moveRCMouse();
                }
            }, 200);
        }
        return true;
    }
}
