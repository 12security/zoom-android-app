package com.zipow.videobox.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.zipow.videobox.util.GifException;
import com.zipow.videobox.util.ZMGlideRequestListener;
import com.zipow.videobox.util.ZMGlideUtil;
import p021us.zoom.androidlib.util.AndroidLifecycleUtils;
import p021us.zoom.androidlib.util.UIUtil;

@TargetApi(11)
public class ZMGifView extends ImageView {
    private static final String TAG = "ZMGifView";
    private boolean fitXY = false;
    @Nullable
    private Paint mCoverPaint = null;
    @Nullable
    private Rect mCoverRect = null;
    private float mFitXScale = 1.0f;
    private float mFitYScale = 1.0f;
    /* access modifiers changed from: private */
    public int mGifHeight;
    /* access modifiers changed from: private */
    @Nullable
    public String mGifPath;
    /* access modifiers changed from: private */
    @Nullable
    public ZMGlideRequestListener mGifRequestListener;
    /* access modifiers changed from: private */
    public int mGifWidth;
    @Nullable
    private Target mImageTarget = new SimpleTarget<Drawable>() {
        public void onResourceReady(@NonNull Drawable drawable, Transition<? super Drawable> transition) {
            int dip2px = UIUtil.dip2px(ZMGifView.this.getContext(), (float) drawable.getIntrinsicWidth());
            int dip2px2 = UIUtil.dip2px(ZMGifView.this.getContext(), (float) drawable.getIntrinsicHeight());
            if (!(dip2px == ZMGifView.this.mGifWidth && dip2px2 == ZMGifView.this.mGifHeight)) {
                ZMGifView.this.mGifHeight = dip2px2;
                ZMGifView.this.mGifWidth = dip2px;
                if (ZMGifView.this.mOnResizeListener != null) {
                    ZMGifView.this.mOnResizeListener.onResize(dip2px, dip2px2);
                }
            }
            ZMGifView.this.setImageDrawable(drawable);
            if (ZMGifView.this.mGifRequestListener != null) {
                ZMGifView.this.mGifRequestListener.onSuccess(ZMGifView.this.mGifPath);
            }
        }
    };
    private int mMaxHeight;
    private int mMaxWidth;
    /* access modifiers changed from: private */
    @Nullable
    public OnResizeListener mOnResizeListener;
    @Nullable
    private int[] mRadius;
    private int mRatio = -1;
    private float mScale = 1.0f;
    @Nullable
    private Target mTarget = new SimpleTarget<GifDrawable>() {
        public void onResourceReady(@NonNull GifDrawable gifDrawable, Transition<? super GifDrawable> transition) {
            int dip2px = UIUtil.dip2px(ZMGifView.this.getContext(), (float) gifDrawable.getIntrinsicWidth());
            int dip2px2 = UIUtil.dip2px(ZMGifView.this.getContext(), (float) gifDrawable.getIntrinsicHeight());
            if (!(dip2px == ZMGifView.this.mGifWidth && dip2px2 == ZMGifView.this.mGifHeight)) {
                ZMGifView.this.mGifHeight = dip2px2;
                ZMGifView.this.mGifWidth = dip2px;
                if (ZMGifView.this.mOnResizeListener != null) {
                    ZMGifView.this.mOnResizeListener.onResize(dip2px, dip2px2);
                }
            }
            ZMGifView.this.setImageDrawable(gifDrawable);
            if (ZMGifView.this.mGifRequestListener != null) {
                ZMGifView.this.mGifRequestListener.onSuccess(ZMGifView.this.mGifPath);
            }
            gifDrawable.start();
        }
    };

    public interface OnResizeListener {
        void onResize(int i, int i2);
    }

    public ZMGifView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMGifView(Context context) {
        super(context);
    }

    public void enableFitXY() {
        this.fitXY = true;
    }

    public void setRatio(int i) {
        this.mRatio = i;
    }

    public void setmScale(float f) {
        this.mScale = f;
    }

    public void clearRatio() {
        this.mRatio = -1;
    }

    public void setRadius(@Nullable int[] iArr) {
        if (iArr != null && iArr.length == 4) {
            this.mRadius = iArr;
        }
    }

    public void setRadius(int i) {
        setRadius(new int[]{i, i, i, i});
    }

    public void setGifResourse(String str) {
        setGifRemoteResourse(str, null);
    }

    public void setGifRemoteResourse(String str, ZMGlideRequestListener zMGlideRequestListener, @Nullable OnResizeListener onResizeListener) {
        if (onResizeListener != null) {
            this.mOnResizeListener = onResizeListener;
        }
        setGifRemoteResourse(str, zMGlideRequestListener);
    }

    public void setGifRemoteResourse(String str, ZMGlideRequestListener zMGlideRequestListener) {
        setTag(null);
        if (!TextUtils.isEmpty(str)) {
            if (!TextUtils.equals(this.mGifPath, str)) {
                clear();
            }
            this.mGifPath = str;
            this.mGifRequestListener = zMGlideRequestListener;
            if (getLayerType() != 1) {
                setLayerType(1, null);
            }
            loadAndShowImage();
        }
    }

    private void loadAndShowImage() {
        C35783 r0 = new RequestListener<GifDrawable>() {
            public boolean onResourceReady(GifDrawable gifDrawable, Object obj, Target<GifDrawable> target, DataSource dataSource, boolean z) {
                return false;
            }

            public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<GifDrawable> target, boolean z) {
                ZMGifView.this.post(new Runnable() {
                    public void run() {
                        ZMGifView.this.retryAfterLoadGifFailed();
                    }
                });
                return false;
            }
        };
        if (getContext() != null && this.mTarget != null && !TextUtils.isEmpty(this.mGifPath)) {
            if (this.mGifPath.startsWith("content:")) {
                ZMGlideUtil.loadGif(getContext(), this.mTarget, Uri.parse(this.mGifPath), (RequestListener) r0);
            } else {
                ZMGlideUtil.loadGif(getContext(), this.mTarget, this.mGifPath, (RequestListener) r0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void retryAfterLoadGifFailed() {
        if (getContext() != null && this.mImageTarget != null && !TextUtils.isEmpty(this.mGifPath)) {
            C35804 r0 = new RequestListener<Drawable>() {
                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                    return false;
                }

                public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                    if (ZMGifView.this.mGifRequestListener != null) {
                        ZMGifView.this.mGifRequestListener.onError(ZMGifView.this.mGifPath, new GifException("RequestListener.onLoadFailed", glideException));
                    }
                    return false;
                }
            };
            if (this.mGifPath.startsWith("content:")) {
                ZMGlideUtil.load(getContext(), this.mImageTarget, Uri.parse(this.mGifPath), (RequestListener) r0);
            } else {
                ZMGlideUtil.load(getContext(), this.mImageTarget, this.mGifPath, (RequestListener) r0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        if (this.mGifWidth == 0 || this.mGifHeight == 0) {
            super.onMeasure(i, i2);
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        if (MeasureSpec.getMode(i) == 1073741824) {
            int size = MeasureSpec.getSize(i);
            float f = ((float) ((size - paddingLeft) - paddingRight)) / (((float) this.mGifWidth) + 0.0f);
            if (MeasureSpec.getMode(i2) == 1073741824) {
                i3 = MeasureSpec.getSize(i2);
                float f2 = ((float) ((i3 - paddingLeft) - paddingRight)) / (((float) this.mGifHeight) + 0.0f);
                if (this.fitXY) {
                    this.mFitXScale = f;
                    this.mFitYScale = f2;
                } else {
                    if (f <= f2) {
                        f2 = f;
                    }
                    this.mScale = f2;
                }
            } else {
                this.mScale = f;
                i3 = ((int) (this.mScale * ((float) this.mGifHeight))) + paddingTop + paddingBottom;
            }
            setMeasuredDimension(size, i3);
            return;
        }
        int maxWidth = getMaxWidth();
        int maxHeight = getMaxHeight();
        if (maxWidth == 0) {
            maxWidth = UIUtil.getDisplayWidth(getContext());
        }
        if (maxHeight == 0) {
            maxHeight = UIUtil.getDisplayHeight(getContext());
        }
        int i4 = (maxWidth - paddingLeft) - paddingRight;
        int i5 = (maxHeight - paddingTop) - paddingBottom;
        if (this.mGifWidth >= i4 || this.mGifHeight >= i5) {
            float f3 = ((float) i4) / (((float) this.mGifWidth) + 0.0f);
            float f4 = ((float) i5) / (((float) this.mGifHeight) + 0.0f);
            if (f3 > f4) {
                f3 = f4;
            }
            this.mScale = f3;
        }
        float f5 = (float) this.mGifWidth;
        float f6 = this.mScale;
        setMeasuredDimension(((int) (f5 * f6)) + paddingLeft + paddingRight, ((int) (((float) this.mGifHeight) * f6)) + paddingTop + paddingBottom);
    }

    public void setMaxHeight(int i) {
        super.setMaxHeight(i);
        this.mMaxHeight = i;
    }

    public void setMaxWidth(int i) {
        super.setMaxWidth(i);
        this.mMaxWidth = i;
    }

    public int getMaxHeight() {
        if (VERSION.SDK_INT >= 16) {
            return super.getMaxHeight();
        }
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        if (VERSION.SDK_INT >= 16) {
            return super.getMaxWidth();
        }
        return this.mMaxWidth;
    }

    @Nullable
    public Path getShapePath(int i, int i2) {
        if (i == 0 || i2 == 0 || this.mRadius == null) {
            return null;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        Path path = new Path();
        float f = (float) paddingTop;
        path.moveTo((float) (this.mRadius[0] + paddingLeft), f);
        path.lineTo((float) ((i - this.mRadius[1]) - paddingRight), f);
        int[] iArr = this.mRadius;
        if (iArr[1] != 0) {
            path.arcTo(getArcOval((i - (iArr[1] * 2)) - paddingRight, paddingTop, iArr[1]), 270.0f, 90.0f);
        }
        path.lineTo((float) (i - paddingRight), (float) ((i2 - this.mRadius[2]) - paddingBottom));
        int[] iArr2 = this.mRadius;
        if (iArr2[2] != 0) {
            path.arcTo(getArcOval((i - (iArr2[2] * 2)) - paddingRight, (i2 - (iArr2[2] * 2)) - paddingBottom, iArr2[2]), 0.0f, 90.0f);
        }
        path.lineTo((float) (this.mRadius[3] + paddingLeft), (float) (i2 - paddingBottom));
        int[] iArr3 = this.mRadius;
        if (iArr3[3] != 0) {
            path.arcTo(getArcOval(paddingLeft, (i2 - (iArr3[3] * 2)) - paddingBottom, iArr3[3]), 90.0f, 90.0f);
        }
        path.lineTo((float) paddingLeft, (float) (this.mRadius[0] + paddingTop));
        int[] iArr4 = this.mRadius;
        if (iArr4[0] != 0) {
            path.arcTo(getArcOval(paddingLeft, paddingTop, iArr4[0]), 180.0f, 90.0f);
        }
        return path;
    }

    @NonNull
    private RectF getArcOval(int i, int i2, int i3) {
        RectF rectF = new RectF();
        rectF.left = (float) i;
        rectF.top = (float) i2;
        int i4 = i3 * 2;
        rectF.right = (float) (i + i4);
        rectF.bottom = (float) (i2 + i4);
        return rectF;
    }

    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredHeight == 0 || measuredWidth == 0) {
            measuredWidth = canvas.getWidth();
            measuredHeight = canvas.getHeight();
        }
        Path shapePath = getShapePath(measuredWidth, measuredHeight);
        if (shapePath != null) {
            canvas.save();
            if (!(VERSION.SDK_INT <= 17 && isHardwareAccelerated())) {
                canvas.clipPath(shapePath);
            }
        }
        super.onDraw(canvas);
        if (shapePath != null) {
            int i = this.mRatio;
            if (i >= 0 && i < 100) {
                int i2 = (measuredHeight * (100 - i)) / 100;
                Rect rect = this.mCoverRect;
                if (rect == null) {
                    this.mCoverRect = new Rect(0, 0, measuredWidth, i2);
                } else {
                    rect.right = measuredWidth;
                    rect.bottom = i2;
                }
                if (this.mCoverPaint == null) {
                    this.mCoverPaint = new Paint();
                    this.mCoverPaint.setColor(Integer.MIN_VALUE);
                }
                canvas.drawRect(this.mCoverRect, this.mCoverPaint);
            }
            canvas.restore();
        }
    }

    private void clear() {
        if (AndroidLifecycleUtils.canLoadImage(getContext()) && this.mTarget != null) {
            ZMGlideUtil.clear(getContext(), this.mTarget);
        }
        this.mGifRequestListener = null;
        this.mGifPath = null;
        this.mGifWidth = 0;
        this.mGifHeight = 0;
    }
}
