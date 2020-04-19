package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.UiModeUtil;
import p021us.zoom.androidlib.widget.ZMViewPager.Page;

/* renamed from: us.zoom.androidlib.widget.TouchImageView */
public class TouchImageView extends View implements Page, OnTouchListener, OnGestureListener, OnDoubleTapListener {
    private static final int MAX_SCALE_LEVEL_COUNT = 3;
    private static final int MAX_VELOCITY_IN_DIP = 1500;
    private static final int MEM_BITMAP_THRESHOLD = 4096;
    private static final String TAG = "TouchImageView";
    private Size mContentSize = new Size();
    private float mContentX = 0.0f;
    private float mContentY = 0.0f;
    private Rect mDestRect = new Rect();
    private Drawable mDrawable = null;
    private Handler mFlingHandler = new Handler();
    private GestureDetector mGestureDetector;
    private Handler mHandler = new Handler();
    private boolean mHasInitData = false;
    private boolean mHasLayout = false;
    private boolean mIsFitScreen = true;
    private boolean mIsMultiTouchZooming = false;
    private float mLastX1;
    private float mLastX2;
    private float mLastY1;
    private float mLastY2;
    private Bitmap mMemBitmap = null;
    private Canvas mMemCanvas = null;
    private OnSingleTapConfirmedListener mOnSingleTapConfirmedListener;
    private OnTouchListener mOnTouchListener;
    /* access modifiers changed from: private */
    public OnViewPortChangedListener mOnViewPortChangedListener;
    private Rect mRectContent = new Rect();
    private Runnable mRunnableNotifyViewPortChange = new Runnable() {
        public void run() {
            if (TouchImageView.this.mOnViewPortChangedListener != null) {
                TouchImageView.this.mOnViewPortChangedListener.onViewPortChanged();
            }
        }
    };
    private float mScaleHeight = 0.0f;
    private float mScaleWidth = 0.0f;
    private Scroller mScroller;
    private Rect mSrcRect = new Rect();
    /* access modifiers changed from: private */
    public boolean mStopFling = false;
    private double mZoomVal = 0.0d;

    /* renamed from: us.zoom.androidlib.widget.TouchImageView$OnSingleTapConfirmedListener */
    public interface OnSingleTapConfirmedListener {
        void onSingleTapConfirmed();
    }

    /* renamed from: us.zoom.androidlib.widget.TouchImageView$OnViewPortChangedListener */
    public interface OnViewPortChangedListener {
        void onViewPortChanged();
    }

    /* renamed from: us.zoom.androidlib.widget.TouchImageView$Size */
    static class Size {
        int height = 0;
        int width = 0;

        Size() {
        }
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    public TouchImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public TouchImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public TouchImageView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator(1.0f));
        if (context instanceof ZMActivity) {
            ((ZMActivity) context).disableFinishActivityByGesture(true);
        }
        super.setOnTouchListener(this);
        if (!isInEditMode()) {
            this.mGestureDetector = new GestureDetector(context, this);
            this.mGestureDetector.setOnDoubleTapListener(this);
            this.mGestureDetector.setIsLongpressEnabled(false);
        }
    }

    public void setOnSingleTapConfirmedListener(OnSingleTapConfirmedListener onSingleTapConfirmedListener) {
        this.mOnSingleTapConfirmedListener = onSingleTapConfirmedListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    public void setOnViewPortChangedListener(OnViewPortChangedListener onViewPortChangedListener) {
        this.mOnViewPortChangedListener = onViewPortChangedListener;
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 8) {
            handleScroll(0.0f, (-motionEvent.getAxisValue(9)) * ((float) UIUtil.dip2px(getContext(), 5.0f)));
        }
        return false;
    }

    public void setImageResource(int i) {
        Context context = getContext();
        if (context != null) {
            if (i == 0) {
                this.mDrawable = null;
                invalidate();
                return;
            }
            this.mDrawable = context.getResources().getDrawable(i);
            if (this.mDrawable != null) {
                initData();
            }
            invalidate();
        }
    }

    public void setImageDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        Bitmap bitmap = this.mMemBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mMemBitmap = null;
            this.mMemCanvas = null;
        }
        if (this.mDrawable != null) {
            initData();
        }
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            this.mDrawable = null;
            invalidate();
            return;
        }
        this.mDrawable = new BitmapDrawable(getResources(), bitmap);
        Bitmap bitmap2 = this.mMemBitmap;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.mMemBitmap = null;
            this.mMemCanvas = null;
        }
        if (this.mDrawable != null) {
            initData();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Bitmap bitmap = this.mMemBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mMemBitmap = null;
            this.mMemCanvas = null;
        }
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public boolean hasImage() {
        return this.mDrawable != null;
    }

    public boolean canScrollHorizontal(int i, int i2, int i3) {
        return canScrollHorizontal(i);
    }

    public boolean canScrollHorizontal(int i) {
        Size size = this.mContentSize;
        boolean z = false;
        if (size == null || this.mRectContent == null || this.mIsFitScreen) {
            return false;
        }
        float f = (float) (this.mZoomVal * ((double) size.width));
        float f2 = this.mContentX + ((float) i);
        if (f2 > 0.0f) {
            if (f >= ((float) this.mRectContent.width())) {
                f2 = 0.0f;
            } else if (f2 + f > ((float) this.mRectContent.width())) {
                f2 = ((float) this.mRectContent.width()) - f;
            }
        } else if (f >= ((float) this.mRectContent.width()) && f2 + f < ((float) this.mRectContent.width())) {
            f2 = ((float) this.mRectContent.width()) - f;
        } else if (f <= ((float) this.mRectContent.width())) {
            f2 = 0.0f;
        }
        if (((int) (f2 * 100.0f)) != ((int) (this.mContentX * 100.0f))) {
            z = true;
        }
        return z;
    }

    public boolean canScrollVertical(int i) {
        Size size = this.mContentSize;
        boolean z = false;
        if (size == null || this.mRectContent == null || this.mIsFitScreen) {
            return false;
        }
        float f = (float) (this.mZoomVal * ((double) size.height));
        float f2 = this.mContentY + ((float) i);
        if (f2 > 0.0f) {
            if (f >= ((float) this.mRectContent.height())) {
                f2 = 0.0f;
            } else if (f2 + f > ((float) this.mRectContent.height())) {
                f2 = ((float) this.mRectContent.height()) - f;
            }
        } else if (f >= ((float) this.mRectContent.height()) && f2 + f < ((float) this.mRectContent.height())) {
            f2 = ((float) this.mRectContent.height()) - f;
        } else if (f <= ((float) this.mRectContent.height())) {
            f2 = 0.0f;
        }
        if (((int) (f2 * 100.0f)) != ((int) (this.mContentY * 100.0f))) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mHasLayout = true;
        if (!this.mHasInitData) {
            initData();
        } else if (this.mIsFitScreen) {
            zoomToFitUnit();
        } else {
            PointF centerPixelPosOnContent = getCenterPixelPosOnContent();
            updateRectContentWithoutResetDestArea();
            if (centerPixelPosOnContent != null) {
                resetDestAreaCenter(centerPixelPosOnContent.x, centerPixelPosOnContent.y);
            }
        }
    }

    private void initData() {
        if (this.mHasLayout) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                this.mContentSize.width = drawable.getIntrinsicWidth();
                this.mContentSize.height = drawable.getIntrinsicHeight();
                this.mRectContent = createRectContent(this.mContentSize);
            }
            this.mHasInitData = true;
            zoomToFitUnit();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            canvas.save();
            try {
                canvas.clipRect(this.mRectContent);
                int i = (int) (((float) this.mRectContent.left) + this.mContentX);
                int i2 = (int) (((float) this.mRectContent.top) + this.mContentY);
                int i3 = (int) (((float) i) + this.mScaleWidth);
                int i4 = (int) (((float) i2) + this.mScaleHeight);
                boolean z = false;
                if ((drawable instanceof BitmapDrawable) && (drawable.getIntrinsicWidth() > 4096 || drawable.getIntrinsicHeight() > 4096)) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    if (bitmap != null) {
                        Bitmap memBitmap = getMemBitmap(bitmap.getConfig());
                        if (!(memBitmap == null || this.mMemCanvas == null)) {
                            this.mSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                            this.mDestRect.set(i, i2, i3, i4);
                            this.mMemCanvas.drawColor(-16777216);
                            this.mMemCanvas.drawBitmap(bitmap, this.mSrcRect, this.mDestRect, null);
                            canvas.drawBitmap(memBitmap, 0.0f, 0.0f, null);
                            z = true;
                        }
                    }
                }
                if (!z) {
                    drawable.setBounds(i, i2, i3, i4);
                    drawable.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvas.restore();
        }
    }

    private Bitmap getMemBitmap(Config config) {
        Bitmap bitmap = this.mMemBitmap;
        if (bitmap != null) {
            if (bitmap.getWidth() == getWidth() && this.mMemBitmap.getHeight() == getHeight()) {
                return this.mMemBitmap;
            }
            this.mMemBitmap.recycle();
            this.mMemBitmap = null;
            this.mMemCanvas = null;
        }
        try {
            this.mMemBitmap = Bitmap.createBitmap(getWidth(), getHeight(), config);
        } catch (OutOfMemoryError unused) {
        }
        Bitmap bitmap2 = this.mMemBitmap;
        if (bitmap2 != null) {
            this.mMemCanvas = new Canvas(bitmap2);
        }
        return this.mMemBitmap;
    }

    public boolean onDown(MotionEvent motionEvent) {
        this.mStopFling = true;
        return true;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        OnSingleTapConfirmedListener onSingleTapConfirmedListener = this.mOnSingleTapConfirmedListener;
        if (onSingleTapConfirmedListener != null) {
            onSingleTapConfirmedListener.onSingleTapConfirmed();
        }
        return false;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        this.mStopFling = true;
        Size size = this.mContentSize;
        if (size == null || size.width == 0 || this.mContentSize.height == 0) {
            return true;
        }
        int scaleLevelsCount = getScaleLevelsCount();
        int currentScaleLevel = getCurrentScaleLevel();
        int i = (currentScaleLevel + 1) % scaleLevelsCount;
        if (i == currentScaleLevel) {
            return true;
        }
        if (i == 0) {
            zoomToFitUnit();
        } else {
            switchToLevel(i, motionEvent.getX(), motionEvent.getY());
        }
        return true;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        OnTouchListener onTouchListener = this.mOnTouchListener;
        if (onTouchListener != null && onTouchListener.onTouch(view, motionEvent)) {
            return true;
        }
        if (motionEvent.getPointerCount() == 2) {
            if (motionEvent.getActionMasked() != 1 || !this.mIsMultiTouchZooming) {
                float x = motionEvent.getX(0);
                float y = motionEvent.getY(0);
                float x2 = motionEvent.getX(1);
                float y2 = motionEvent.getY(1);
                if (this.mIsMultiTouchZooming) {
                    onMultiTouchZoom(x, y, x2, y2, this.mLastX1, this.mLastY1, this.mLastX2, this.mLastY2);
                }
                this.mIsMultiTouchZooming = true;
                this.mLastX1 = x;
                this.mLastY1 = y;
                this.mLastX2 = x2;
                this.mLastY2 = y2;
                return true;
            }
            onMultiTouchZoomEnd();
            return true;
        } else if (this.mIsMultiTouchZooming) {
            onMultiTouchZoomEnd();
            return true;
        } else {
            if (motionEvent.getActionMasked() == 1) {
                notifyViewPortChange(0);
            }
            return this.mGestureDetector.onTouchEvent(motionEvent);
        }
    }

    private void notifyViewPortChange(int i) {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mRunnableNotifyViewPortChange);
            this.mHandler.postDelayed(this.mRunnableNotifyViewPortChange, (long) i);
        }
    }

    private void onMultiTouchZoom(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.mIsMultiTouchZooming = true;
        float f9 = f3 - f;
        float f10 = f4 - f2;
        float f11 = f7 - f5;
        float f12 = f8 - f6;
        double sqrt = this.mZoomVal * (Math.sqrt((double) ((f9 * f9) + (f10 * f10))) / Math.sqrt((double) ((f11 * f11) + (f12 * f12))));
        PointF unitPosToPixelPosOnContent = unitPosToPixelPosOnContent(viewXToShareUnitX(f5), viewYToShareUnitY(f6), this.mZoomVal);
        float f13 = (float) (((double) unitPosToPixelPosOnContent.x) * sqrt);
        float f14 = (float) (((double) unitPosToPixelPosOnContent.y) * sqrt);
        this.mZoomVal = sqrt;
        this.mIsFitScreen = checkFitScreen();
        updateRectContentWithoutResetDestArea();
        float viewXToShareUnitX = viewXToShareUnitX(f);
        float viewYToShareUnitY = viewYToShareUnitY(f2);
        Size size = this.mContentSize;
        if (size != null && size.width != 0) {
            this.mScaleWidth = (float) (((double) this.mContentSize.width) * sqrt);
            this.mScaleHeight = (float) (((double) this.mContentSize.height) * sqrt);
            this.mContentX = viewXToShareUnitX - f13;
            this.mContentY = viewYToShareUnitY - f14;
            trimContentPos();
            repaint();
            notifyViewPortChange(0);
        }
    }

    private void onMultiTouchZoomEnd() {
        this.mIsMultiTouchZooming = false;
        if (this.mZoomVal < getMinLevelZoomValue()) {
            zoomToFitUnit();
        } else if (this.mZoomVal > getMaxLevelZoomValue()) {
            switchToLevel(getScaleLevelsCount() - 1, (float) ((this.mRectContent.width() / 2) + this.mRectContent.left), (float) ((this.mRectContent.height() / 2) + this.mRectContent.top));
        }
        notifyViewPortChange(0);
    }

    private boolean checkFitScreen() {
        if (this.mZoomVal < 0.01d) {
            return true;
        }
        boolean z = false;
        if (Math.abs(this.mZoomVal - scaleLevelToZoomValue(0)) < 0.01d) {
            z = true;
        }
        return z;
    }

    private double getMaxLevelZoomValue() {
        Context context = getContext();
        if (context == null) {
            return 1.0d;
        }
        return (double) (UiModeUtil.isInDesktopMode(getContext()) ? (float) (((getFitLevelZoomValue(false) * 5.0d) / 2.0d) - getMinLevelZoomValue()) : (context.getResources().getDisplayMetrics().density * 160.0f) / 120.0f);
    }

    private double getMinLevelZoomValue() {
        return getFitLevelZoomValue(true);
    }

    private double getFitLevelZoomValue(boolean z) {
        if (this.mContentSize == null) {
            return 0.0d;
        }
        int width = getWidth();
        int height = getHeight();
        boolean z2 = this.mContentSize.height * width > this.mContentSize.width * height;
        return (((!z2 || !z) && (z2 || z)) ? (double) width : (((double) height) * ((double) this.mContentSize.width)) / ((double) this.mContentSize.height)) / ((double) this.mContentSize.width);
    }

    private int getScaleLevelsCount() {
        Size size = this.mContentSize;
        if (size == null || size.width == 0 || this.mContentSize.height == 0) {
            return 3;
        }
        double maxLevelZoomValue = getMaxLevelZoomValue();
        float f = (float) (((double) this.mContentSize.height) * maxLevelZoomValue);
        if (((float) (((double) this.mContentSize.width) * maxLevelZoomValue)) <= ((float) getWidth()) && f < ((float) getHeight())) {
            return 1;
        }
        double minLevelZoomValue = ((getMinLevelZoomValue() + maxLevelZoomValue) * 2.0d) / 5.0d;
        float f2 = (float) (minLevelZoomValue * ((double) this.mContentSize.height));
        if (((float) (((double) this.mContentSize.width) * minLevelZoomValue)) > ((float) getWidth()) || f2 >= ((float) getHeight())) {
            return 3;
        }
        return 2;
    }

    private int getCurrentScaleLevel() {
        int scaleLevelsCount = getScaleLevelsCount();
        double[] dArr = new double[scaleLevelsCount];
        int i = 0;
        for (int i2 = 0; i2 < scaleLevelsCount; i2++) {
            dArr[i2] = scaleLevelToZoomValue(i2);
        }
        while (true) {
            int i3 = scaleLevelsCount - 1;
            if (i >= i3) {
                return i3;
            }
            double d = this.mZoomVal;
            if (d >= dArr[i] && d < dArr[i + 1]) {
                return i;
            }
            i++;
        }
    }

    private void switchToLevel(int i, float f, float f2) {
        switchToZoom(scaleLevelToZoomValue(i), f, f2);
    }

    private void switchToZoom(double d, float f, float f2) {
        double d2 = this.mZoomVal;
        this.mZoomVal = d;
        this.mIsFitScreen = checkFitScreen();
        PointF unitPosToPixelPosOnContent = unitPosToPixelPosOnContent(viewXToShareUnitX(f), viewYToShareUnitY(f2), d2);
        updateRectContentWithoutResetDestArea();
        Size size = this.mContentSize;
        if (size != null && size.width != 0) {
            float f3 = unitPosToPixelPosOnContent.x;
            float f4 = unitPosToPixelPosOnContent.y;
            this.mScaleWidth = (float) (((double) this.mContentSize.width) * this.mZoomVal);
            this.mScaleHeight = (float) (((double) this.mContentSize.height) * this.mZoomVal);
            resetDestAreaCenter(f3, f4);
            notifyViewPortChange(500);
        }
    }

    private PointF getCenterPixelPosOnContent() {
        Rect rect = this.mRectContent;
        if (rect == null) {
            return null;
        }
        return unitPosToPixelPosOnContent((float) (rect.width() / 2), (float) (this.mRectContent.height() / 2), this.mZoomVal);
    }

    private void resetDestAreaCenter(float f, float f2) {
        Rect rect = this.mRectContent;
        if (rect != null) {
            this.mContentX = ((float) (rect.width() / 2)) - ((float) (((double) f) * this.mZoomVal));
            this.mContentY = ((float) (this.mRectContent.height() / 2)) - ((float) (((double) f2) * this.mZoomVal));
            trimContentPos();
            repaint();
        }
    }

    private PointF unitPosToPixelPosOnContent(float f, float f2, double d) {
        return new PointF((float) (((double) (f - this.mContentX)) / d), (float) (((double) (f2 - this.mContentY)) / d));
    }

    private void repaint() {
        invalidate();
    }

    private void zoomToFitUnit() {
        this.mZoomVal = scaleLevelToZoomValue(0);
        this.mIsFitScreen = checkFitScreen();
        this.mContentX = 0.0f;
        this.mContentY = 0.0f;
        updateRectContentWithoutResetDestArea();
        Rect rect = this.mRectContent;
        if (rect != null) {
            this.mScaleWidth = (float) rect.width();
            this.mScaleHeight = (float) this.mRectContent.height();
            repaint();
            notifyViewPortChange(0);
        }
    }

    private float viewXToShareUnitX(float f) {
        Rect rect = this.mRectContent;
        if (rect == null) {
            return f;
        }
        return f - ((float) rect.left);
    }

    private float viewYToShareUnitY(float f) {
        Rect rect = this.mRectContent;
        if (rect == null) {
            return f;
        }
        return f - ((float) rect.top);
    }

    private double scaleLevelToZoomValue(int i) {
        Size size = this.mContentSize;
        if (size == null || size.width == 0) {
            return 1.0d;
        }
        double minLevelZoomValue = getMinLevelZoomValue();
        double maxLevelZoomValue = getMaxLevelZoomValue();
        double d = ((minLevelZoomValue + maxLevelZoomValue) * 2.0d) / 5.0d;
        int scaleLevelsCount = getScaleLevelsCount();
        if (scaleLevelsCount == 1) {
            return Math.min(minLevelZoomValue, maxLevelZoomValue);
        }
        if (scaleLevelsCount == 2) {
            return i != 0 ? maxLevelZoomValue : minLevelZoomValue;
        }
        if (scaleLevelsCount < 3) {
            return 0.0d;
        }
        switch (i) {
            case 0:
                return minLevelZoomValue;
            case 1:
                return d;
            default:
                return maxLevelZoomValue;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.mStopFling = true;
        this.mContentX -= f;
        this.mContentY -= f2;
        trimContentPos();
        repaint();
        notifyViewPortChange(500);
        return true;
    }

    private void handleScroll(float f, float f2) {
        this.mStopFling = true;
        this.mContentX -= f;
        this.mContentY -= f2;
        trimContentPos();
        repaint();
        notifyViewPortChange(500);
    }

    private void trimContentPos() {
        Size size = this.mContentSize;
        if (size != null && this.mRectContent != null) {
            float f = (float) (this.mZoomVal * ((double) size.width));
            float f2 = (float) (this.mZoomVal * ((double) this.mContentSize.height));
            if (this.mContentX > 0.0f) {
                if (f >= ((float) this.mRectContent.width())) {
                    this.mContentX = 0.0f;
                } else if (this.mContentX + f > ((float) this.mRectContent.width())) {
                    this.mContentX = ((float) this.mRectContent.width()) - f;
                }
            } else if (f >= ((float) this.mRectContent.width()) && this.mContentX + f < ((float) this.mRectContent.width())) {
                this.mContentX = ((float) this.mRectContent.width()) - f;
            } else if (f <= ((float) this.mRectContent.width())) {
                this.mContentX = 0.0f;
            }
            if (this.mContentY > 0.0f) {
                if (f2 >= ((float) this.mRectContent.height())) {
                    this.mContentY = 0.0f;
                } else if (this.mContentY + f2 > ((float) this.mRectContent.height())) {
                    this.mContentY = ((float) this.mRectContent.height()) - f2;
                }
            } else if (f2 >= ((float) this.mRectContent.height()) && this.mContentY + f2 < ((float) this.mRectContent.height())) {
                this.mContentY = ((float) this.mRectContent.height()) - f2;
            } else if (f2 <= ((float) this.mRectContent.height())) {
                this.mContentY = 0.0f;
            }
        }
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        float f3;
        if (this.mRectContent != null) {
            Size size = this.mContentSize;
            if (size != null) {
                int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i > 0) {
                    this.mScroller.setFinalX(0);
                } else {
                    this.mScroller.setFinalX((int) (((float) this.mRectContent.width()) - ((float) (this.mZoomVal * ((double) size.width)))));
                }
                int i2 = (f2 > 0.0f ? 1 : (f2 == 0.0f ? 0 : -1));
                if (i2 > 0) {
                    this.mScroller.setFinalY(0);
                } else {
                    this.mScroller.setFinalY((int) (((float) this.mRectContent.height()) - ((float) (this.mZoomVal * ((double) this.mContentSize.height)))));
                }
                int dip2px = UIUtil.dip2px(getContext(), 1500.0f);
                if (Math.abs(f) > Math.abs(f2)) {
                    if (i == 0) {
                        f = 0.1f;
                    }
                    float f4 = f2 / f;
                    f3 = (float) dip2px;
                    if (f <= f3) {
                        f3 = (float) (-dip2px);
                        if (f >= f3) {
                            f3 = f;
                        }
                    }
                    f2 = f4 * f3;
                } else {
                    if (i2 == 0) {
                        f2 = 0.1f;
                    }
                    float f5 = f / f2;
                    float f6 = (float) dip2px;
                    if (f2 > f6) {
                        f2 = f6;
                    } else {
                        float f7 = (float) (-dip2px);
                        if (f2 < f7) {
                            f2 = f7;
                        }
                    }
                    f3 = f2 * f5;
                }
                this.mScroller.fling((int) this.mContentX, (int) this.mContentY, (int) f3, (int) f2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
                this.mStopFling = false;
                handleFling();
                return true;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void handleFling() {
        this.mFlingHandler.postDelayed(new Runnable() {
            public void run() {
                if (!TouchImageView.this.mStopFling && TouchImageView.this.updateContentPosOnFling()) {
                    TouchImageView.this.handleFling();
                }
            }
        }, 40);
    }

    /* access modifiers changed from: private */
    public boolean updateContentPosOnFling() {
        boolean z;
        boolean z2;
        if (this.mRectContent == null || this.mContentSize == null || !this.mScroller.computeScrollOffset()) {
            return false;
        }
        this.mContentX = (float) this.mScroller.getCurrX();
        if (this.mContentX > 0.0f) {
            this.mContentX = 0.0f;
            z = true;
        } else {
            float f = (float) (this.mZoomVal * ((double) this.mContentSize.width));
            if (this.mContentX + f < ((float) this.mRectContent.width())) {
                this.mContentX = ((float) this.mRectContent.width()) - f;
                z = true;
            } else {
                z = false;
            }
        }
        this.mContentY = (float) this.mScroller.getCurrY();
        if (this.mContentY > 0.0f) {
            this.mContentY = 0.0f;
            z2 = true;
        } else {
            float f2 = (float) (this.mZoomVal * ((double) this.mContentSize.height));
            if (this.mContentY + f2 < ((float) this.mRectContent.height())) {
                this.mContentY = ((float) this.mRectContent.height()) - f2;
                z2 = true;
            } else {
                z2 = false;
            }
        }
        repaint();
        boolean z3 = z || z2;
        if (z3) {
            notifyViewPortChange(0);
        }
        return !z3;
    }

    private void updateRectContentWithoutResetDestArea() {
        this.mRectContent = createRectContent(this.mContentSize);
    }

    private Rect createRectContent(Size size) {
        int i;
        int i2;
        int i3;
        int i4 = size.width;
        int i5 = size.height;
        if (i4 == 0 || i5 == 0) {
            return null;
        }
        int width = getWidth();
        int height = getHeight();
        int i6 = 0;
        if (!this.mIsFitScreen || Math.abs(this.mZoomVal - getMinLevelZoomValue()) >= 0.01d) {
            double d = (double) i4;
            double d2 = this.mZoomVal;
            float f = (float) (d * d2);
            float f2 = (float) (((double) i5) * d2);
            if (f > ((float) getWidth())) {
                i3 = getWidth();
                i2 = 0;
            } else {
                i3 = (int) f;
                i2 = (width - i3) / 2;
            }
            if (f2 > ((float) getHeight())) {
                i = getHeight();
            } else {
                i = (int) f2;
                i6 = (height - i) / 2;
            }
        } else {
            int i7 = width * i5;
            int i8 = height * i4;
            if (i7 > i8) {
                int i9 = i8 / i5;
                i2 = (width - i9) / 2;
                i3 = i9;
                i = height;
            } else {
                i = i7 / i4;
                i6 = (height - i) / 2;
                i3 = width;
                i2 = 0;
            }
        }
        return new Rect(i2, i6, i3 + i2, i + i6);
    }
}
