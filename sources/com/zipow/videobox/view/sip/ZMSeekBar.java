package com.zipow.videobox.view.sip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import java.math.BigDecimal;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZMSeekBar extends View {

    /* renamed from: dx */
    float f350dx;
    private boolean isThumbOnDragging;
    private float mDelta;
    private float mLeft;
    private float mMax;
    private float mMin;
    private Paint mPaint;
    private float mProgress;
    private OnProgressChangedListener mProgressListener;
    private float mRight;
    private int mSecondTrackColor;
    private float mThumbCenterX;
    private int mThumbColor;
    private int mThumbRadius;
    private int mThumbRadiusOnDragging;
    private int mTrackColor;
    private float mTrackLength;
    private int mTrackSize;

    public interface OnProgressChangedListener {
        void getProgressOnActionUp(ZMSeekBar zMSeekBar, int i, float f);

        void onProgressChanged(ZMSeekBar zMSeekBar, int i, float f);

        void onProgressChangedWhenMoving(ZMSeekBar zMSeekBar, int i, float f);
    }

    public ZMSeekBar(@NonNull Context context) {
        this(context, null);
    }

    public ZMSeekBar(@NonNull Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZMSeekBar(@NonNull Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMin = 0.0f;
        this.mMax = 100.0f;
        this.mProgress = this.mMin;
        int dip2px = UIUtil.dip2px(context, 2.0f);
        this.mTrackSize = dip2px;
        this.mThumbRadius = dip2px * 2;
        this.mThumbRadiusOnDragging = this.mThumbRadius * 2;
        this.mTrackColor = ContextCompat.getColor(context, C4558R.color.zm_btn_background_blue);
        this.mSecondTrackColor = ContextCompat.getColor(context, C4558R.color.zm_ui_kit_color_gray_747487);
        this.mThumbColor = this.mTrackColor;
        setEnabled(isEnabled());
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setTextAlign(Align.CENTER);
        initConfigByPriority();
    }

    private void initConfigByPriority() {
        if (this.mMin == this.mMax) {
            this.mMin = 0.0f;
            this.mMax = 100.0f;
        }
        float f = this.mMin;
        float f2 = this.mMax;
        if (f > f2) {
            this.mMax = f;
            this.mMin = f2;
        }
        float f3 = this.mProgress;
        float f4 = this.mMin;
        if (f3 < f4) {
            this.mProgress = f4;
        }
        float f5 = this.mProgress;
        float f6 = this.mMax;
        if (f5 > f6) {
            this.mProgress = f6;
        }
        this.mDelta = this.mMax - this.mMin;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(resolveSize(UIUtil.dip2px(getContext(), 180.0f), i), this.mThumbRadiusOnDragging * 2);
        this.mLeft = (float) (getPaddingLeft() + this.mThumbRadiusOnDragging);
        this.mRight = (float) ((getMeasuredWidth() - getPaddingRight()) - this.mThumbRadiusOnDragging);
        this.mTrackLength = this.mRight - this.mLeft;
    }

    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float paddingLeft = (float) getPaddingLeft();
        float measuredWidth = (float) (getMeasuredWidth() - getPaddingRight());
        int paddingTop = getPaddingTop();
        int i = this.mThumbRadiusOnDragging;
        float f = (float) (paddingTop + i);
        float f2 = paddingLeft + ((float) i);
        float f3 = measuredWidth - ((float) i);
        if (!this.isThumbOnDragging) {
            this.mThumbCenterX = ((this.mTrackLength / this.mDelta) * (this.mProgress - this.mMin)) + f2;
        }
        this.mPaint.setStrokeWidth((float) this.mTrackSize);
        this.mPaint.setColor(this.mTrackColor);
        Canvas canvas2 = canvas;
        float f4 = f;
        float f5 = f;
        canvas2.drawLine(f2, f4, this.mThumbCenterX, f5, this.mPaint);
        this.mPaint.setColor(this.mSecondTrackColor);
        canvas2.drawLine(this.mThumbCenterX, f4, f3, f5, this.mPaint);
        this.mPaint.setColor(this.mThumbColor);
        canvas.drawCircle(this.mThumbCenterX, f, (float) (this.isThumbOnDragging ? this.mThumbRadiusOnDragging : this.mThumbRadius), this.mPaint);
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
                performClick();
                getParent().requestDisallowInterceptTouchEvent(true);
                this.isThumbOnDragging = isThumbTouched(motionEvent);
                if (this.isThumbOnDragging) {
                    invalidate();
                }
                this.f350dx = this.mThumbCenterX - motionEvent.getX();
                break;
            case 1:
            case 3:
                getParent().requestDisallowInterceptTouchEvent(false);
                if (this.isThumbOnDragging) {
                    this.isThumbOnDragging = false;
                    invalidate();
                }
                OnProgressChangedListener onProgressChangedListener = this.mProgressListener;
                if (onProgressChangedListener != null) {
                    onProgressChangedListener.getProgressOnActionUp(this, getProgress(), getProgressFloat());
                    break;
                }
                break;
            case 2:
                if (this.isThumbOnDragging) {
                    this.mThumbCenterX = motionEvent.getX() + this.f350dx;
                    float f = this.mThumbCenterX;
                    float f2 = this.mLeft;
                    if (f < f2) {
                        this.mThumbCenterX = f2;
                    }
                    float f3 = this.mThumbCenterX;
                    float f4 = this.mRight;
                    if (f3 > f4) {
                        this.mThumbCenterX = f4;
                    }
                    this.mProgress = calculateProgress();
                    invalidate();
                    OnProgressChangedListener onProgressChangedListener2 = this.mProgressListener;
                    if (onProgressChangedListener2 != null) {
                        onProgressChangedListener2.onProgressChangedWhenMoving(this, getProgress(), getProgressFloat());
                        break;
                    }
                }
                break;
        }
        if (this.isThumbOnDragging || super.onTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    private boolean isThumbTouched(@NonNull MotionEvent motionEvent) {
        boolean z = false;
        if (!isEnabled()) {
            return false;
        }
        float f = this.mLeft + ((this.mTrackLength / this.mDelta) * (this.mProgress - this.mMin));
        float measuredHeight = ((float) getMeasuredHeight()) / 2.0f;
        if (((motionEvent.getX() - f) * (motionEvent.getX() - f)) + ((motionEvent.getY() - measuredHeight) * (motionEvent.getY() - measuredHeight)) <= (this.mLeft + ((float) UIUtil.dip2px(getContext(), 8.0f))) * (this.mLeft + ((float) UIUtil.dip2px(getContext(), 8.0f)))) {
            z = true;
        }
        return z;
    }

    private float formatFloat(float f) {
        return BigDecimal.valueOf((double) f).setScale(1, 4).floatValue();
    }

    private float calculateProgress() {
        return (((this.mThumbCenterX - this.mLeft) * this.mDelta) / this.mTrackLength) + this.mMin;
    }

    public float getMin() {
        return this.mMin;
    }

    public float getMax() {
        return this.mMax;
    }

    public float getmMax() {
        return this.mMax;
    }

    public void setmMax(float f) {
        this.mMax = f;
        this.mDelta = f - this.mMin;
    }

    public void setProgress(float f) {
        this.mProgress = f;
        postInvalidate();
    }

    public int getProgress() {
        return Math.round(processProgress());
    }

    public float getProgressFloat() {
        return formatFloat(processProgress());
    }

    private float processProgress() {
        return this.mProgress;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mProgressListener = onProgressChangedListener;
    }

    public OnProgressChangedListener getOnProgressChangedListener() {
        return this.mProgressListener;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("save_instance", super.onSaveInstanceState());
        bundle.putFloat(NotificationCompat.CATEGORY_PROGRESS, this.mProgress);
        return bundle;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mProgress = bundle.getFloat(NotificationCompat.CATEGORY_PROGRESS);
            super.onRestoreInstanceState(bundle.getParcelable("save_instance"));
            setProgress(this.mProgress);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }
}
