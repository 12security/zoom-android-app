package com.zipow.videobox.view.photopicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.OverScroller;
import android.widget.Scroller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchImageView extends ImageView {
    private static final String DEBUG = "DEBUG";
    private static final float SUPER_MAX_MULTIPLIER = 1.25f;
    private static final float SUPER_MIN_MULTIPLIER = 0.75f;
    /* access modifiers changed from: private */
    public Context context;
    @Nullable
    private ZoomVariables delayedZoomVariables;
    /* access modifiers changed from: private */
    @Nullable
    public OnDoubleTapListener doubleTapListener = null;
    /* access modifiers changed from: private */
    public Fling fling;
    private boolean imageRenderedAtLeastOnce;
    /* access modifiers changed from: private */
    @Nullable

    /* renamed from: m */
    public float[] f347m;
    /* access modifiers changed from: private */
    public GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public ScaleGestureDetector mScaleDetector;
    private ScaleType mScaleType;
    private float matchViewHeight;
    private float matchViewWidth;
    /* access modifiers changed from: private */
    public Matrix matrix;
    /* access modifiers changed from: private */
    public float maxScale;
    /* access modifiers changed from: private */
    public float minScale;
    /* access modifiers changed from: private */
    public float normalizedScale;
    private boolean onDrawReady;
    private float prevMatchViewHeight;
    private float prevMatchViewWidth;
    private Matrix prevMatrix;
    private int prevViewHeight;
    private int prevViewWidth;
    /* access modifiers changed from: private */
    public State state;
    private float superMaxScale;
    private float superMinScale;
    /* access modifiers changed from: private */
    @Nullable
    public OnTouchImageViewListener touchImageViewListener = null;
    /* access modifiers changed from: private */
    @Nullable
    public OnTouchListener userTouchListener = null;
    /* access modifiers changed from: private */
    public int viewHeight;
    /* access modifiers changed from: private */
    public int viewWidth;

    /* renamed from: com.zipow.videobox.view.photopicker.TouchImageView$1 */
    static /* synthetic */ class C39791 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            /*
                android.widget.ImageView$ScaleType[] r0 = android.widget.ImageView.ScaleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$widget$ImageView$ScaleType = r0
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0014 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x001f }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_CROP     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x002a }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.CENTER_INSIDE     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0035 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_CENTER     // Catch:{ NoSuchFieldError -> 0x0035 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0035 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0035 }
            L_0x0035:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0040 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_XY     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.photopicker.TouchImageView.C39791.<clinit>():void");
        }
    }

    @TargetApi(9)
    private class CompatScroller {
        boolean isPreGingerbread = false;
        OverScroller overScroller;
        Scroller scroller;

        public CompatScroller(Context context) {
            this.overScroller = new OverScroller(context);
        }

        public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            if (this.isPreGingerbread) {
                this.scroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
            } else {
                this.overScroller.fling(i, i2, i3, i4, i5, i6, i7, i8);
            }
        }

        public void forceFinished(boolean z) {
            if (this.isPreGingerbread) {
                this.scroller.forceFinished(z);
            } else {
                this.overScroller.forceFinished(z);
            }
        }

        public boolean isFinished() {
            if (this.isPreGingerbread) {
                return this.scroller.isFinished();
            }
            return this.overScroller.isFinished();
        }

        public boolean computeScrollOffset() {
            if (this.isPreGingerbread) {
                return this.scroller.computeScrollOffset();
            }
            this.overScroller.computeScrollOffset();
            return this.overScroller.computeScrollOffset();
        }

        public int getCurrX() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrX();
            }
            return this.overScroller.getCurrX();
        }

        public int getCurrY() {
            if (this.isPreGingerbread) {
                return this.scroller.getCurrY();
            }
            return this.overScroller.getCurrY();
        }
    }

    private class DoubleTapZoom implements Runnable {
        private static final float ZOOM_TIME = 500.0f;
        private float bitmapX;
        private float bitmapY;
        private PointF endTouch;
        @NonNull
        private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        private long startTime;
        private PointF startTouch;
        private float startZoom;
        private boolean stretchImageToSuper;
        private float targetZoom;

        DoubleTapZoom(float f, float f2, float f3, boolean z) {
            TouchImageView.this.setState(State.ANIMATE_ZOOM);
            this.startTime = System.currentTimeMillis();
            this.startZoom = TouchImageView.this.normalizedScale;
            this.targetZoom = f;
            this.stretchImageToSuper = z;
            PointF access$2300 = TouchImageView.this.transformCoordTouchToBitmap(f2, f3, false);
            this.bitmapX = access$2300.x;
            this.bitmapY = access$2300.y;
            this.startTouch = TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, this.bitmapY);
            this.endTouch = new PointF((float) (TouchImageView.this.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2));
        }

        public void run() {
            float interpolate = interpolate();
            TouchImageView.this.scaleImage(calculateDeltaScale(interpolate), this.bitmapX, this.bitmapY, this.stretchImageToSuper);
            translateImageToCenterTouchPosition(interpolate);
            TouchImageView.this.fixScaleTrans();
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.setImageMatrix(touchImageView.matrix);
            if (TouchImageView.this.touchImageViewListener != null) {
                TouchImageView.this.touchImageViewListener.onMove();
            }
            if (interpolate < 1.0f) {
                TouchImageView.this.compatPostOnAnimation(this);
            } else {
                TouchImageView.this.setState(State.NONE);
            }
        }

        private void translateImageToCenterTouchPosition(float f) {
            float f2 = this.startTouch.x + ((this.endTouch.x - this.startTouch.x) * f);
            float f3 = this.startTouch.y + (f * (this.endTouch.y - this.startTouch.y));
            PointF access$2400 = TouchImageView.this.transformCoordBitmapToTouch(this.bitmapX, this.bitmapY);
            TouchImageView.this.matrix.postTranslate(f2 - access$2400.x, f3 - access$2400.y);
        }

        private float interpolate() {
            return this.interpolator.getInterpolation(Math.min(1.0f, ((float) (System.currentTimeMillis() - this.startTime)) / ZOOM_TIME));
        }

        private double calculateDeltaScale(float f) {
            float f2 = this.startZoom;
            return ((double) (f2 + (f * (this.targetZoom - f2)))) / ((double) TouchImageView.this.normalizedScale);
        }
    }

    private class Fling implements Runnable {
        int currX;
        int currY;
        @Nullable
        CompatScroller scroller;

        Fling(int i, int i2) {
            int i3;
            int i4;
            int i5;
            int i6;
            TouchImageView.this.setState(State.FLING);
            this.scroller = new CompatScroller(TouchImageView.this.context);
            TouchImageView.this.matrix.getValues(TouchImageView.this.f347m);
            int i7 = (int) TouchImageView.this.f347m[2];
            int i8 = (int) TouchImageView.this.f347m[5];
            if (TouchImageView.this.getImageWidth() > ((float) TouchImageView.this.viewWidth)) {
                i4 = TouchImageView.this.viewWidth - ((int) TouchImageView.this.getImageWidth());
                i3 = 0;
            } else {
                i4 = i7;
                i3 = i4;
            }
            if (TouchImageView.this.getImageHeight() > ((float) TouchImageView.this.viewHeight)) {
                i6 = TouchImageView.this.viewHeight - ((int) TouchImageView.this.getImageHeight());
                i5 = 0;
            } else {
                i6 = i8;
                i5 = i6;
            }
            this.scroller.fling(i7, i8, i, i2, i4, i3, i6, i5);
            this.currX = i7;
            this.currY = i8;
        }

        public void cancelFling() {
            if (this.scroller != null) {
                TouchImageView.this.setState(State.NONE);
                this.scroller.forceFinished(true);
            }
        }

        public void run() {
            if (TouchImageView.this.touchImageViewListener != null) {
                TouchImageView.this.touchImageViewListener.onMove();
            }
            if (this.scroller.isFinished()) {
                this.scroller = null;
                return;
            }
            if (this.scroller.computeScrollOffset()) {
                int currX2 = this.scroller.getCurrX();
                int currY2 = this.scroller.getCurrY();
                int i = currX2 - this.currX;
                int i2 = currY2 - this.currY;
                this.currX = currX2;
                this.currY = currY2;
                TouchImageView.this.matrix.postTranslate((float) i, (float) i2);
                TouchImageView.this.fixTrans();
                TouchImageView touchImageView = TouchImageView.this;
                touchImageView.setImageMatrix(touchImageView.matrix);
                TouchImageView.this.compatPostOnAnimation(this);
            }
        }
    }

    private class GestureListener extends SimpleOnGestureListener {
        private GestureListener() {
        }

        /* synthetic */ GestureListener(TouchImageView touchImageView, C39791 r2) {
            this();
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (TouchImageView.this.doubleTapListener != null) {
                return TouchImageView.this.doubleTapListener.onSingleTapConfirmed(motionEvent);
            }
            return TouchImageView.this.performClick();
        }

        public void onLongPress(MotionEvent motionEvent) {
            TouchImageView.this.performLongClick();
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (TouchImageView.this.fling != null) {
                TouchImageView.this.fling.cancelFling();
            }
            TouchImageView touchImageView = TouchImageView.this;
            touchImageView.fling = new Fling((int) f, (int) f2);
            TouchImageView touchImageView2 = TouchImageView.this;
            touchImageView2.compatPostOnAnimation(touchImageView2.fling);
            return super.onFling(motionEvent, motionEvent2, f, f2);
        }

        public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
            boolean onDoubleTap = TouchImageView.this.doubleTapListener != null ? TouchImageView.this.doubleTapListener.onDoubleTap(motionEvent) : false;
            if (TouchImageView.this.state != State.NONE) {
                return onDoubleTap;
            }
            DoubleTapZoom doubleTapZoom = new DoubleTapZoom(TouchImageView.this.normalizedScale == TouchImageView.this.minScale ? TouchImageView.this.maxScale : TouchImageView.this.minScale, motionEvent.getX(), motionEvent.getY(), false);
            TouchImageView.this.compatPostOnAnimation(doubleTapZoom);
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            if (TouchImageView.this.doubleTapListener != null) {
                return TouchImageView.this.doubleTapListener.onDoubleTapEvent(motionEvent);
            }
            return false;
        }
    }

    public interface OnTouchImageViewListener {
        void onMove();
    }

    private class PrivateOnTouchListener implements OnTouchListener {
        @NonNull
        private PointF last;

        private PrivateOnTouchListener() {
            this.last = new PointF();
        }

        /* synthetic */ PrivateOnTouchListener(TouchImageView touchImageView, C39791 r2) {
            this();
        }

        public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
            TouchImageView.this.mScaleDetector.onTouchEvent(motionEvent);
            TouchImageView.this.mGestureDetector.onTouchEvent(motionEvent);
            PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
            if (TouchImageView.this.state == State.NONE || TouchImageView.this.state == State.DRAG || TouchImageView.this.state == State.FLING) {
                int action = motionEvent.getAction();
                if (action != 6) {
                    switch (action) {
                        case 0:
                            this.last.set(pointF);
                            if (TouchImageView.this.fling != null) {
                                TouchImageView.this.fling.cancelFling();
                            }
                            TouchImageView.this.setState(State.DRAG);
                            break;
                        case 1:
                            break;
                        case 2:
                            if (TouchImageView.this.state == State.DRAG) {
                                float f = pointF.x - this.last.x;
                                float f2 = pointF.y - this.last.y;
                                TouchImageView touchImageView = TouchImageView.this;
                                float access$1500 = touchImageView.getFixDragTrans(f, (float) touchImageView.viewWidth, TouchImageView.this.getImageWidth());
                                TouchImageView touchImageView2 = TouchImageView.this;
                                TouchImageView.this.matrix.postTranslate(access$1500, touchImageView2.getFixDragTrans(f2, (float) touchImageView2.viewHeight, TouchImageView.this.getImageHeight()));
                                TouchImageView.this.fixTrans();
                                this.last.set(pointF.x, pointF.y);
                                break;
                            }
                            break;
                    }
                }
                TouchImageView.this.setState(State.NONE);
            }
            TouchImageView touchImageView3 = TouchImageView.this;
            touchImageView3.setImageMatrix(touchImageView3.matrix);
            if (TouchImageView.this.userTouchListener != null) {
                TouchImageView.this.userTouchListener.onTouch(view, motionEvent);
            }
            if (TouchImageView.this.touchImageViewListener != null) {
                TouchImageView.this.touchImageViewListener.onMove();
            }
            return true;
        }
    }

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        /* synthetic */ ScaleListener(TouchImageView touchImageView, C39791 r2) {
            this();
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.setState(State.ZOOM);
            return true;
        }

        public boolean onScale(@NonNull ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.scaleImage((double) scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), true);
            if (TouchImageView.this.touchImageViewListener != null) {
                TouchImageView.this.touchImageViewListener.onMove();
            }
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            float f;
            super.onScaleEnd(scaleGestureDetector);
            TouchImageView.this.setState(State.NONE);
            float access$700 = TouchImageView.this.normalizedScale;
            boolean z = true;
            if (TouchImageView.this.normalizedScale > TouchImageView.this.maxScale) {
                f = TouchImageView.this.maxScale;
            } else if (TouchImageView.this.normalizedScale < TouchImageView.this.minScale) {
                f = TouchImageView.this.minScale;
            } else {
                z = false;
                f = access$700;
            }
            if (z) {
                TouchImageView touchImageView = TouchImageView.this;
                DoubleTapZoom doubleTapZoom = new DoubleTapZoom(f, (float) (touchImageView.viewWidth / 2), (float) (TouchImageView.this.viewHeight / 2), true);
                TouchImageView.this.compatPostOnAnimation(doubleTapZoom);
            }
        }
    }

    private enum State {
        NONE,
        DRAG,
        ZOOM,
        FLING,
        ANIMATE_ZOOM
    }

    private class ZoomVariables {
        public float focusX;
        public float focusY;
        public float scale;
        public ScaleType scaleType;

        public ZoomVariables(float f, float f2, float f3, ScaleType scaleType2) {
            this.scale = f;
            this.focusX = f2;
            this.focusY = f3;
            this.scaleType = scaleType2;
        }
    }

    /* access modifiers changed from: private */
    public float getFixDragTrans(float f, float f2, float f3) {
        if (f3 <= f2) {
            return 0.0f;
        }
        return f;
    }

    private float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    public TouchImageView(Context context2) {
        super(context2);
        sharedConstructing(context2);
    }

    public TouchImageView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        sharedConstructing(context2);
    }

    public TouchImageView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        sharedConstructing(context2);
    }

    private void sharedConstructing(Context context2) {
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener(this, null));
        this.mGestureDetector = new GestureDetector(context2, new GestureListener(this, null));
        this.matrix = new Matrix();
        this.prevMatrix = new Matrix();
        this.f347m = new float[9];
        this.normalizedScale = 1.0f;
        if (this.mScaleType == null) {
            this.mScaleType = ScaleType.FIT_CENTER;
        }
        this.minScale = 1.0f;
        this.maxScale = 3.0f;
        this.superMinScale = this.minScale * SUPER_MIN_MULTIPLIER;
        this.superMaxScale = this.maxScale * SUPER_MAX_MULTIPLIER;
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setState(State.NONE);
        this.onDrawReady = false;
        super.setOnTouchListener(new PrivateOnTouchListener(this, null));
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.userTouchListener = onTouchListener;
    }

    public void setOnTouchImageViewListener(OnTouchImageViewListener onTouchImageViewListener) {
        this.touchImageViewListener = onTouchImageViewListener;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.doubleTapListener = onDoubleTapListener;
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        savePreviousImageValues();
        fitImageToView();
    }

    public void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.FIT_START || scaleType == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        } else if (scaleType == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);
        } else {
            this.mScaleType = scaleType;
            if (this.onDrawReady) {
                setZoom(this);
            }
        }
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public boolean isZoomed() {
        return this.normalizedScale != 1.0f;
    }

    @NonNull
    public RectF getZoomedRect() {
        if (this.mScaleType != ScaleType.FIT_XY) {
            PointF transformCoordTouchToBitmap = transformCoordTouchToBitmap(0.0f, 0.0f, true);
            PointF transformCoordTouchToBitmap2 = transformCoordTouchToBitmap((float) this.viewWidth, (float) this.viewHeight, true);
            float intrinsicWidth = (float) getDrawable().getIntrinsicWidth();
            float intrinsicHeight = (float) getDrawable().getIntrinsicHeight();
            return new RectF(transformCoordTouchToBitmap.x / intrinsicWidth, transformCoordTouchToBitmap.y / intrinsicHeight, transformCoordTouchToBitmap2.x / intrinsicWidth, transformCoordTouchToBitmap2.y / intrinsicHeight);
        }
        throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
    }

    private void savePreviousImageValues() {
        Matrix matrix2 = this.matrix;
        if (matrix2 != null && this.viewHeight != 0 && this.viewWidth != 0) {
            matrix2.getValues(this.f347m);
            this.prevMatrix.setValues(this.f347m);
            this.prevMatchViewHeight = this.matchViewHeight;
            this.prevMatchViewWidth = this.matchViewWidth;
            this.prevViewHeight = this.viewHeight;
            this.prevViewWidth = this.viewWidth;
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", this.normalizedScale);
        bundle.putFloat("matchViewHeight", this.matchViewHeight);
        bundle.putFloat("matchViewWidth", this.matchViewWidth);
        bundle.putInt("viewWidth", this.viewWidth);
        bundle.putInt("viewHeight", this.viewHeight);
        this.matrix.getValues(this.f347m);
        bundle.putFloatArray("matrix", this.f347m);
        bundle.putBoolean("imageRendered", this.imageRenderedAtLeastOnce);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.normalizedScale = bundle.getFloat("saveScale");
            this.f347m = bundle.getFloatArray("matrix");
            this.prevMatrix.setValues(this.f347m);
            this.prevMatchViewHeight = bundle.getFloat("matchViewHeight");
            this.prevMatchViewWidth = bundle.getFloat("matchViewWidth");
            this.prevViewHeight = bundle.getInt("viewHeight");
            this.prevViewWidth = bundle.getInt("viewWidth");
            this.imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.onDrawReady = true;
        this.imageRenderedAtLeastOnce = true;
        ZoomVariables zoomVariables = this.delayedZoomVariables;
        if (zoomVariables != null) {
            setZoom(zoomVariables.scale, this.delayedZoomVariables.focusX, this.delayedZoomVariables.focusY, this.delayedZoomVariables.scaleType);
            this.delayedZoomVariables = null;
        }
        super.onDraw(canvas);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        savePreviousImageValues();
    }

    public float getMaxZoom() {
        return this.maxScale;
    }

    public void setMaxZoom(float f) {
        this.maxScale = f;
        this.superMaxScale = this.maxScale * SUPER_MAX_MULTIPLIER;
    }

    public float getMinZoom() {
        return this.minScale;
    }

    public float getCurrentZoom() {
        return this.normalizedScale;
    }

    public void setMinZoom(float f) {
        this.minScale = f;
        this.superMinScale = this.minScale * SUPER_MIN_MULTIPLIER;
    }

    public void resetZoom() {
        this.normalizedScale = 1.0f;
        fitImageToView();
    }

    public void setZoom(float f) {
        setZoom(f, 0.5f, 0.5f);
    }

    public void setZoom(float f, float f2, float f3) {
        setZoom(f, f2, f3, this.mScaleType);
    }

    public void setZoom(float f, float f2, float f3, ScaleType scaleType) {
        if (!this.onDrawReady) {
            ZoomVariables zoomVariables = new ZoomVariables(f, f2, f3, scaleType);
            this.delayedZoomVariables = zoomVariables;
            return;
        }
        if (scaleType != this.mScaleType) {
            setScaleType(scaleType);
        }
        resetZoom();
        scaleImage((double) f, (float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
        this.matrix.getValues(this.f347m);
        this.f347m[2] = -((f2 * getImageWidth()) - (((float) this.viewWidth) * 0.5f));
        this.f347m[5] = -((f3 * getImageHeight()) - (((float) this.viewHeight) * 0.5f));
        this.matrix.setValues(this.f347m);
        fixTrans();
        setImageMatrix(this.matrix);
    }

    public void setZoom(@NonNull TouchImageView touchImageView) {
        PointF scrollPosition = touchImageView.getScrollPosition();
        setZoom(touchImageView.getCurrentZoom(), scrollPosition.x, scrollPosition.y, touchImageView.getScaleType());
    }

    @Nullable
    public PointF getScrollPosition() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        PointF transformCoordTouchToBitmap = transformCoordTouchToBitmap((float) (this.viewWidth / 2), (float) (this.viewHeight / 2), true);
        transformCoordTouchToBitmap.x /= (float) intrinsicWidth;
        transformCoordTouchToBitmap.y /= (float) intrinsicHeight;
        return transformCoordTouchToBitmap;
    }

    public void setScrollPosition(float f, float f2) {
        setZoom(this.normalizedScale, f, f2);
    }

    /* access modifiers changed from: private */
    public void fixTrans() {
        this.matrix.getValues(this.f347m);
        float[] fArr = this.f347m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, getImageWidth());
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, getImageHeight());
        if (fixTrans != 0.0f || fixTrans2 != 0.0f) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
    }

    /* access modifiers changed from: private */
    public void fixScaleTrans() {
        fixTrans();
        this.matrix.getValues(this.f347m);
        float imageWidth = getImageWidth();
        int i = this.viewWidth;
        if (imageWidth < ((float) i)) {
            this.f347m[2] = (((float) i) - getImageWidth()) / 2.0f;
        }
        float imageHeight = getImageHeight();
        int i2 = this.viewHeight;
        if (imageHeight < ((float) i2)) {
            this.f347m[5] = (((float) i2) - getImageHeight()) / 2.0f;
        }
        this.matrix.setValues(this.f347m);
    }

    /* access modifiers changed from: private */
    public float getImageWidth() {
        return this.matchViewWidth * this.normalizedScale;
    }

    /* access modifiers changed from: private */
    public float getImageHeight() {
        return this.matchViewHeight * this.normalizedScale;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        int size2 = MeasureSpec.getSize(i2);
        int mode2 = MeasureSpec.getMode(i2);
        this.viewWidth = setViewSize(mode, size, intrinsicWidth);
        this.viewHeight = setViewSize(mode2, size2, intrinsicHeight);
        setMeasuredDimension(this.viewWidth, this.viewHeight);
        fitImageToView();
    }

    private void fitImageToView() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0 && this.matrix != null && this.prevMatrix != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            float f = (float) intrinsicWidth;
            float f2 = ((float) this.viewWidth) / f;
            float f3 = (float) intrinsicHeight;
            float f4 = ((float) this.viewHeight) / f3;
            switch (C39791.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
                case 1:
                    f2 = 1.0f;
                    f4 = 1.0f;
                    break;
                case 2:
                    f2 = Math.max(f2, f4);
                    f4 = f2;
                    break;
                case 3:
                    f2 = Math.min(1.0f, Math.min(f2, f4));
                    f4 = f2;
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
            }
            f2 = Math.min(f2, f4);
            f4 = f2;
            int i = this.viewWidth;
            float f5 = ((float) i) - (f2 * f);
            int i2 = this.viewHeight;
            float f6 = ((float) i2) - (f4 * f3);
            this.matchViewWidth = ((float) i) - f5;
            this.matchViewHeight = ((float) i2) - f6;
            if (isZoomed() || this.imageRenderedAtLeastOnce) {
                if (this.prevMatchViewWidth == 0.0f || this.prevMatchViewHeight == 0.0f) {
                    savePreviousImageValues();
                }
                this.prevMatrix.getValues(this.f347m);
                float[] fArr = this.f347m;
                float f7 = this.matchViewWidth / f;
                float f8 = this.normalizedScale;
                fArr[0] = f7 * f8;
                fArr[4] = (this.matchViewHeight / f3) * f8;
                float f9 = fArr[2];
                float f10 = fArr[5];
                translateMatrixAfterRotate(2, f9, this.prevMatchViewWidth * f8, getImageWidth(), this.prevViewWidth, this.viewWidth, intrinsicWidth);
                translateMatrixAfterRotate(5, f10, this.prevMatchViewHeight * this.normalizedScale, getImageHeight(), this.prevViewHeight, this.viewHeight, intrinsicHeight);
                this.matrix.setValues(this.f347m);
            } else {
                this.matrix.setScale(f2, f4);
                this.matrix.postTranslate(f5 / 2.0f, f6 / 2.0f);
                this.normalizedScale = 1.0f;
            }
            fixTrans();
            setImageMatrix(this.matrix);
        }
    }

    private int setViewSize(int i, int i2, int i3) {
        if (i != Integer.MIN_VALUE) {
            return i != 0 ? i2 : i3;
        }
        return Math.min(i3, i2);
    }

    private void translateMatrixAfterRotate(int i, float f, float f2, float f3, int i2, int i3, int i4) {
        float f4 = (float) i3;
        if (f3 < f4) {
            float[] fArr = this.f347m;
            fArr[i] = (f4 - (((float) i4) * fArr[0])) * 0.5f;
        } else if (f > 0.0f) {
            this.f347m[i] = -((f3 - f4) * 0.5f);
        } else {
            this.f347m[i] = -((((Math.abs(f) + (((float) i2) * 0.5f)) / f2) * f3) - (f4 * 0.5f));
        }
    }

    /* access modifiers changed from: private */
    public void setState(State state2) {
        this.state = state2;
    }

    public boolean canScrollHorizontallyFroyo(int i) {
        return canScrollHorizontally(i);
    }

    public boolean canScrollHorizontally(int i) {
        this.matrix.getValues(this.f347m);
        float f = this.f347m[2];
        if (getImageWidth() < ((float) this.viewWidth)) {
            return false;
        }
        if (f >= -1.0f && i < 0) {
            return false;
        }
        if (Math.abs(f) + ((float) this.viewWidth) + 1.0f < getImageWidth() || i <= 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void scaleImage(double d, float f, float f2, boolean z) {
        float f3;
        float f4;
        if (z) {
            f3 = this.superMinScale;
            f4 = this.superMaxScale;
        } else {
            f3 = this.minScale;
            f4 = this.maxScale;
        }
        float f5 = this.normalizedScale;
        this.normalizedScale = (float) (((double) f5) * d);
        float f6 = this.normalizedScale;
        if (f6 > f4) {
            this.normalizedScale = f4;
            d = (double) (f4 / f5);
        } else if (f6 < f3) {
            this.normalizedScale = f3;
            d = (double) (f3 / f5);
        }
        float f7 = (float) d;
        this.matrix.postScale(f7, f7, f, f2);
        fixScaleTrans();
    }

    /* access modifiers changed from: private */
    public PointF transformCoordTouchToBitmap(float f, float f2, boolean z) {
        this.matrix.getValues(this.f347m);
        float intrinsicWidth = (float) getDrawable().getIntrinsicWidth();
        float intrinsicHeight = (float) getDrawable().getIntrinsicHeight();
        float[] fArr = this.f347m;
        float f3 = fArr[2];
        float imageWidth = ((f - f3) * intrinsicWidth) / getImageWidth();
        float imageHeight = ((f2 - fArr[5]) * intrinsicHeight) / getImageHeight();
        if (z) {
            imageWidth = Math.min(Math.max(imageWidth, 0.0f), intrinsicWidth);
            imageHeight = Math.min(Math.max(imageHeight, 0.0f), intrinsicHeight);
        }
        return new PointF(imageWidth, imageHeight);
    }

    /* access modifiers changed from: private */
    public PointF transformCoordBitmapToTouch(float f, float f2) {
        this.matrix.getValues(this.f347m);
        return new PointF(this.f347m[2] + (getImageWidth() * (f / ((float) getDrawable().getIntrinsicWidth()))), this.f347m[5] + (getImageHeight() * (f2 / ((float) getDrawable().getIntrinsicHeight()))));
    }

    /* access modifiers changed from: private */
    @TargetApi(16)
    public void compatPostOnAnimation(Runnable runnable) {
        if (VERSION.SDK_INT >= 16) {
            postOnAnimation(runnable);
        } else {
            postDelayed(runnable, 16);
        }
    }

    private void printMatrixInfo() {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        String str = DEBUG;
        StringBuilder sb = new StringBuilder();
        sb.append("Scale: ");
        sb.append(fArr[0]);
        sb.append(" TransX: ");
        sb.append(fArr[2]);
        sb.append(" TransY: ");
        sb.append(fArr[5]);
        Log.d(str, sb.toString());
    }
}
