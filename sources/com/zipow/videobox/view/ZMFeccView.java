package com.zipow.videobox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import p021us.zoom.videomeetings.C4558R;

public class ZMFeccView extends LinearLayout implements IFeccListener, OnClickListener, OnTouchListener {
    private ImageView mClose;
    private Handler mHandler;
    private FeccListener mListener;
    private ZMPieView mPieView;
    private ImageView mSwitch;
    private ZMFeccEventTimeRunnable mTimeRunnable;
    private ImageView mZoomIn;
    private ImageView mZoomOut;

    public interface FeccListener extends IFeccListener {
        void onFeccClose();

        void onFeccSwitchCam();
    }

    @SuppressLint({"NewApi"})
    public ZMFeccView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public ZMFeccView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public ZMFeccView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        inflateLayout();
        this.mPieView = (ZMPieView) findViewById(C4558R.C4560id.pieView);
        this.mSwitch = (ImageView) findViewById(C4558R.C4560id.btnSwitch);
        this.mClose = (ImageView) findViewById(C4558R.C4560id.btnClose);
        this.mZoomIn = (ImageView) findViewById(C4558R.C4560id.btnZoomIn);
        this.mZoomOut = (ImageView) findViewById(C4558R.C4560id.btnZoomOut);
        this.mPieView.setListener(this);
        this.mSwitch.setOnClickListener(this);
        this.mClose.setOnClickListener(this);
        this.mZoomIn.setOnTouchListener(this);
        this.mZoomOut.setOnTouchListener(this);
        this.mHandler = new Handler();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_fecc_view, this);
    }

    public void setListener(FeccListener feccListener) {
        this.mListener = feccListener;
    }

    public void showPieView(boolean z) {
        if (z) {
            this.mPieView.setVisibility(0);
            this.mZoomIn.setVisibility(0);
            this.mZoomOut.setVisibility(0);
            return;
        }
        this.mPieView.setVisibility(4);
        this.mZoomIn.setVisibility(4);
        this.mZoomOut.setVisibility(4);
    }

    public void onClick(View view) {
        if (view == this.mSwitch) {
            onClickSwitchCam();
        } else if (view == this.mClose) {
            onClickClose();
        }
    }

    private void onClickClose() {
        FeccListener feccListener = this.mListener;
        if (feccListener != null) {
            feccListener.onFeccClose();
        }
    }

    private void onClickSwitchCam() {
        FeccListener feccListener = this.mListener;
        if (feccListener != null) {
            feccListener.onFeccSwitchCam();
        }
    }

    public void onFeccClick(int i, int i2) {
        FeccListener feccListener = this.mListener;
        if (feccListener != null) {
            feccListener.onFeccClick(i, i2);
        }
    }

    private void updateEvent(int i) {
        ZMFeccEventTimeRunnable zMFeccEventTimeRunnable = this.mTimeRunnable;
        if (zMFeccEventTimeRunnable != null) {
            zMFeccEventTimeRunnable.updateEvent(i);
        }
    }

    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
        int i;
        ImageView imageView = this.mZoomIn;
        if (view == imageView) {
            i = 5;
        } else {
            imageView = this.mZoomOut;
            if (view == imageView) {
                i = 6;
            } else {
                imageView = null;
                i = 0;
            }
        }
        updateEvent(i);
        int action = motionEvent.getAction();
        if (action == 0) {
            if (imageView != null) {
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    drawable.setState(new int[]{16842910, 16842908, 16842919});
                    imageView.invalidate();
                }
            }
            FeccListener feccListener = this.mListener;
            if (!(feccListener == null || i == 0)) {
                feccListener.onFeccClick(1, i);
            }
            if (this.mTimeRunnable == null) {
                this.mTimeRunnable = new ZMFeccEventTimeRunnable();
            }
            this.mTimeRunnable.initial(i, this.mHandler, this.mListener);
            this.mHandler.postDelayed(this.mTimeRunnable, 300);
        } else if (action == 1) {
            if (imageView != null) {
                Drawable drawable2 = imageView.getDrawable();
                if (drawable2 != null) {
                    drawable2.setState(new int[0]);
                    imageView.invalidate();
                }
            }
            ZMFeccEventTimeRunnable zMFeccEventTimeRunnable = this.mTimeRunnable;
            if (zMFeccEventTimeRunnable != null) {
                this.mHandler.removeCallbacks(zMFeccEventTimeRunnable);
            }
            FeccListener feccListener2 = this.mListener;
            if (feccListener2 != null) {
                feccListener2.onFeccClick(3, i);
            }
            if (imageView != null) {
                imageView.playSoundEffect(0);
            }
            updateEvent(0);
        }
        return true;
    }
}
