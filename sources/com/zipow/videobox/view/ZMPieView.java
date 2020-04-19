package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import p021us.zoom.videomeetings.C4558R;

public class ZMPieView extends RelativeLayout {
    private static final String TAG = "ZMPieView";
    private ImageView mBackground;
    private float mBgRadius = 0.0f;
    private float mBgX = 0.0f;
    private float mBgY = 0.0f;
    private ImageView mDown;
    private int mEvent = 0;
    private Handler mHandler;
    private ImageView mLeft;
    private IFeccListener mListener;
    private ImageView mRight;
    private ZMFeccEventTimeRunnable mTimeRunnable;
    private ImageView mUp;

    public ZMPieView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public ZMPieView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public ZMPieView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        inflateLayout();
        this.mBackground = (ImageView) findViewById(C4558R.C4560id.imgCircle);
        this.mLeft = (ImageView) findViewById(C4558R.C4560id.imgFocusLeft);
        this.mRight = (ImageView) findViewById(C4558R.C4560id.imgFocusRight);
        this.mUp = (ImageView) findViewById(C4558R.C4560id.imgFocusUp);
        this.mDown = (ImageView) findViewById(C4558R.C4560id.imgFocusDown);
        this.mHandler = new Handler();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_pie_view, this);
    }

    public void setListener(IFeccListener iFeccListener) {
        this.mListener = iFeccListener;
    }

    private int getAngleByCoordinat(float f, float f2, float f3, float f4) {
        int round = Math.round((float) ((Math.atan2((double) (f4 - f2), (double) (f3 - f)) / 3.141592653589793d) * 180.0d));
        return round < 0 ? round + 360 : round;
    }

    private float getRadius(float f, float f2, float f3, float f4) {
        float abs = Math.abs(f3 - f);
        float abs2 = Math.abs(f4 - f2);
        return (float) Math.sqrt((double) ((abs * abs) + (abs2 * abs2)));
    }

    private void refreshUI() {
        int i = this.mEvent;
        if (i == 0 || i == -1) {
            this.mLeft.setVisibility(8);
            this.mRight.setVisibility(8);
            this.mUp.setVisibility(8);
            this.mDown.setVisibility(8);
        } else if (i == 1) {
            this.mLeft.setVisibility(8);
            this.mRight.setVisibility(8);
            this.mUp.setVisibility(8);
            this.mDown.setVisibility(0);
        } else if (i == 2) {
            this.mLeft.setVisibility(8);
            this.mRight.setVisibility(8);
            this.mUp.setVisibility(0);
            this.mDown.setVisibility(8);
        } else if (i == 3) {
            this.mLeft.setVisibility(0);
            this.mRight.setVisibility(8);
            this.mUp.setVisibility(8);
            this.mDown.setVisibility(8);
        } else if (i == 4) {
            this.mLeft.setVisibility(8);
            this.mRight.setVisibility(0);
            this.mUp.setVisibility(8);
            this.mDown.setVisibility(8);
        }
    }

    private int getEvent(float f, float f2) {
        if (getRadius(this.mBgX, this.mBgY, f, f2) > this.mBgRadius) {
            return -1;
        }
        int angleByCoordinat = getAngleByCoordinat(this.mBgX, this.mBgY, f, f2);
        int i = (angleByCoordinat < 45 || angleByCoordinat >= 135) ? (angleByCoordinat < 135 || angleByCoordinat >= 225) ? (angleByCoordinat < 225 || angleByCoordinat >= 315) ? (angleByCoordinat >= 315 || angleByCoordinat < 45) ? 4 : 0 : 2 : 3 : 1;
        return i;
    }

    private void updateEvent(int i) {
        this.mEvent = i;
        ZMFeccEventTimeRunnable zMFeccEventTimeRunnable = this.mTimeRunnable;
        if (zMFeccEventTimeRunnable != null) {
            zMFeccEventTimeRunnable.updateEvent(this.mEvent);
        }
    }

    private boolean handleTouchEvent(int i, float f, float f2) {
        updateEvent(getEvent(f, f2));
        int i2 = this.mEvent;
        if (i2 == -1) {
            ZMFeccEventTimeRunnable zMFeccEventTimeRunnable = this.mTimeRunnable;
            if (zMFeccEventTimeRunnable != null) {
                this.mHandler.removeCallbacks(zMFeccEventTimeRunnable);
            }
            IFeccListener iFeccListener = this.mListener;
            if (iFeccListener != null) {
                iFeccListener.onFeccClick(3, this.mEvent);
            }
            return false;
        }
        boolean z = true;
        if (i == 0) {
            IFeccListener iFeccListener2 = this.mListener;
            if (iFeccListener2 != null) {
                iFeccListener2.onFeccClick(1, i2);
            }
            if (this.mTimeRunnable == null) {
                this.mTimeRunnable = new ZMFeccEventTimeRunnable();
            }
            this.mTimeRunnable.initial(this.mEvent, this.mHandler, this.mListener);
            this.mHandler.postDelayed(this.mTimeRunnable, 300);
        } else if (i == 1) {
            ZMFeccEventTimeRunnable zMFeccEventTimeRunnable2 = this.mTimeRunnable;
            if (zMFeccEventTimeRunnable2 != null) {
                this.mHandler.removeCallbacks(zMFeccEventTimeRunnable2);
            }
            IFeccListener iFeccListener3 = this.mListener;
            if (iFeccListener3 != null) {
                iFeccListener3.onFeccClick(3, this.mEvent);
            }
            playSoundEffect(0);
            updateEvent(0);
        } else if (i != 2) {
            z = false;
        }
        return z;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        if (this.mBgX <= 0.0f || this.mBgY <= 0.0f) {
            int[] iArr = new int[2];
            this.mBackground.getLocationOnScreen(iArr);
            this.mBgX = (float) (iArr[0] + (this.mBackground.getWidth() / 2));
            this.mBgY = (float) (iArr[1] + (this.mBackground.getHeight() / 2));
            this.mBgRadius = (float) (this.mBackground.getWidth() / 2);
        }
        if (!handleTouchEvent(motionEvent.getAction(), motionEvent.getRawX(), motionEvent.getRawY())) {
            return super.onTouchEvent(motionEvent);
        }
        refreshUI();
        return true;
    }
}
