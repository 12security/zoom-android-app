package com.zipow.videobox.view.roundedview;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.ImageView.ScaleType;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.HashSet;

public class RoundedDrawable extends Drawable {
    public static final int DEFAULT_BORDER_COLOR = -16777216;
    public static final String TAG = "RoundedDrawable";
    @NonNull
    private final Bitmap mBitmap;
    private final int mBitmapHeight;
    @NonNull
    private final Paint mBitmapPaint;
    private final RectF mBitmapRect = new RectF();
    private final int mBitmapWidth;
    @NonNull
    private ColorStateList mBorderColor = ColorStateList.valueOf(-16777216);
    @NonNull
    private final Paint mBorderPaint;
    private final RectF mBorderRect = new RectF();
    private float mBorderWidth = 0.0f;
    private final RectF mBounds = new RectF();
    private float mCornerRadius = 0.0f;
    private final boolean[] mCornersRounded = {true, true, true, true};
    private final RectF mDrawableRect = new RectF();
    private boolean mOval = false;
    private boolean mRebuildShader = true;
    @NonNull
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private final Matrix mShaderMatrix = new Matrix();
    private final RectF mSquareCornersRect = new RectF();
    private TileMode mTileModeX = TileMode.CLAMP;
    private TileMode mTileModeY = TileMode.CLAMP;

    /* renamed from: com.zipow.videobox.view.roundedview.RoundedDrawable$1 */
    static /* synthetic */ class C39801 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
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
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_END     // Catch:{ NoSuchFieldError -> 0x0040 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0040 }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0040 }
            L_0x0040:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x004b }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_START     // Catch:{ NoSuchFieldError -> 0x004b }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x004b }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x004b }
            L_0x004b:
                int[] r0 = $SwitchMap$android$widget$ImageView$ScaleType     // Catch:{ NoSuchFieldError -> 0x0056 }
                android.widget.ImageView$ScaleType r1 = android.widget.ImageView.ScaleType.FIT_XY     // Catch:{ NoSuchFieldError -> 0x0056 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0056 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0056 }
            L_0x0056:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.roundedview.RoundedDrawable.C39801.<clinit>():void");
        }
    }

    public int getOpacity() {
        return -3;
    }

    public RoundedDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
        this.mBitmapRect.set(0.0f, 0.0f, (float) this.mBitmapWidth, (float) this.mBitmapHeight);
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setStyle(Style.FILL);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(this.mBorderColor.getColorForState(getState(), -16777216));
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
    }

    @Nullable
    public static RoundedDrawable fromBitmap(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundedDrawable(bitmap);
        }
        return null;
    }

    @Nullable
    public static Drawable fromDrawable(@Nullable Drawable drawable) {
        if (drawable == null || (drawable instanceof RoundedDrawable)) {
            return drawable;
        }
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            int numberOfLayers = layerDrawable.getNumberOfLayers();
            for (int i = 0; i < numberOfLayers; i++) {
                layerDrawable.setDrawableByLayerId(layerDrawable.getId(i), fromDrawable(layerDrawable.getDrawable(i)));
            }
            return layerDrawable;
        }
        Bitmap drawableToBitmap = drawableToBitmap(drawable);
        if (drawableToBitmap != null) {
            return new RoundedDrawable(drawableToBitmap);
        }
        return drawable;
    }

    @Nullable
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            bitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 2), Math.max(drawable.getIntrinsicHeight(), 2), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Throwable th) {
            th.printStackTrace();
            Log.w(TAG, "Failed to create bitmap from drawable!");
            bitmap = null;
        }
        return bitmap;
    }

    @NonNull
    public Bitmap getSourceBitmap() {
        return this.mBitmap;
    }

    public boolean isStateful() {
        return this.mBorderColor.isStateful();
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        int colorForState = this.mBorderColor.getColorForState(iArr, 0);
        if (this.mBorderPaint.getColor() == colorForState) {
            return super.onStateChange(iArr);
        }
        this.mBorderPaint.setColor(colorForState);
        return true;
    }

    private void updateShaderMatrix() {
        float f;
        float f2;
        float f3;
        switch (C39801.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
            case 1:
                this.mBorderRect.set(this.mBounds);
                RectF rectF = this.mBorderRect;
                float f4 = this.mBorderWidth;
                rectF.inset(f4 / 2.0f, f4 / 2.0f);
                this.mShaderMatrix.reset();
                this.mShaderMatrix.setTranslate((float) ((int) (((this.mBorderRect.width() - ((float) this.mBitmapWidth)) * 0.5f) + 0.5f)), (float) ((int) (((this.mBorderRect.height() - ((float) this.mBitmapHeight)) * 0.5f) + 0.5f)));
                break;
            case 2:
                this.mBorderRect.set(this.mBounds);
                RectF rectF2 = this.mBorderRect;
                float f5 = this.mBorderWidth;
                rectF2.inset(f5 / 2.0f, f5 / 2.0f);
                this.mShaderMatrix.reset();
                float f6 = 0.0f;
                if (((float) this.mBitmapWidth) * this.mBorderRect.height() > this.mBorderRect.width() * ((float) this.mBitmapHeight)) {
                    f2 = this.mBorderRect.height() / ((float) this.mBitmapHeight);
                    f = (this.mBorderRect.width() - (((float) this.mBitmapWidth) * f2)) * 0.5f;
                } else {
                    f2 = this.mBorderRect.width() / ((float) this.mBitmapWidth);
                    f6 = (this.mBorderRect.height() - (((float) this.mBitmapHeight) * f2)) * 0.5f;
                    f = 0.0f;
                }
                this.mShaderMatrix.setScale(f2, f2);
                Matrix matrix = this.mShaderMatrix;
                float f7 = (float) ((int) (f + 0.5f));
                float f8 = this.mBorderWidth;
                matrix.postTranslate(f7 + (f8 / 2.0f), ((float) ((int) (f6 + 0.5f))) + (f8 / 2.0f));
                break;
            case 3:
                this.mShaderMatrix.reset();
                if (((float) this.mBitmapWidth) > this.mBounds.width() || ((float) this.mBitmapHeight) > this.mBounds.height()) {
                    f3 = Math.min(this.mBounds.width() / ((float) this.mBitmapWidth), this.mBounds.height() / ((float) this.mBitmapHeight));
                } else {
                    f3 = 1.0f;
                }
                float width = (float) ((int) (((this.mBounds.width() - (((float) this.mBitmapWidth) * f3)) * 0.5f) + 0.5f));
                float height = (float) ((int) (((this.mBounds.height() - (((float) this.mBitmapHeight) * f3)) * 0.5f) + 0.5f));
                this.mShaderMatrix.setScale(f3, f3);
                this.mShaderMatrix.postTranslate(width, height);
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF3 = this.mBorderRect;
                float f9 = this.mBorderWidth;
                rectF3.inset(f9 / 2.0f, f9 / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
                break;
            case 5:
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.END);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF4 = this.mBorderRect;
                float f10 = this.mBorderWidth;
                rectF4.inset(f10 / 2.0f, f10 / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
                break;
            case 6:
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.START);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF5 = this.mBorderRect;
                float f11 = this.mBorderWidth;
                rectF5.inset(f11 / 2.0f, f11 / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
                break;
            case 7:
                this.mBorderRect.set(this.mBounds);
                RectF rectF6 = this.mBorderRect;
                float f12 = this.mBorderWidth;
                rectF6.inset(f12 / 2.0f, f12 / 2.0f);
                this.mShaderMatrix.reset();
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
                break;
            default:
                this.mBorderRect.set(this.mBitmapRect);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.CENTER);
                this.mShaderMatrix.mapRect(this.mBorderRect);
                RectF rectF7 = this.mBorderRect;
                float f13 = this.mBorderWidth;
                rectF7.inset(f13 / 2.0f, f13 / 2.0f);
                this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
                break;
        }
        this.mDrawableRect.set(this.mBorderRect);
        this.mRebuildShader = true;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(@NonNull Rect rect) {
        super.onBoundsChange(rect);
        this.mBounds.set(rect);
        updateShaderMatrix();
    }

    public void draw(@NonNull Canvas canvas) {
        if (this.mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(this.mBitmap, this.mTileModeX, this.mTileModeY);
            if (this.mTileModeX == TileMode.CLAMP && this.mTileModeY == TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(this.mShaderMatrix);
            }
            this.mBitmapPaint.setShader(bitmapShader);
            this.mRebuildShader = false;
        }
        if (this.mOval) {
            if (this.mBorderWidth > 0.0f) {
                canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
                canvas.drawOval(this.mBorderRect, this.mBorderPaint);
                return;
            }
            canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
        } else if (any(this.mCornersRounded)) {
            float f = this.mCornerRadius;
            if (this.mBorderWidth > 0.0f) {
                canvas.drawRoundRect(this.mDrawableRect, f, f, this.mBitmapPaint);
                canvas.drawRoundRect(this.mBorderRect, f, f, this.mBorderPaint);
                redrawBitmapForSquareCorners(canvas);
                redrawBorderForSquareCorners(canvas);
                return;
            }
            canvas.drawRoundRect(this.mDrawableRect, f, f, this.mBitmapPaint);
            redrawBitmapForSquareCorners(canvas);
        } else {
            canvas.drawRect(this.mDrawableRect, this.mBitmapPaint);
            if (this.mBorderWidth > 0.0f) {
                canvas.drawRect(this.mBorderRect, this.mBorderPaint);
            }
        }
    }

    private void redrawBitmapForSquareCorners(@NonNull Canvas canvas) {
        if (!all(this.mCornersRounded) && this.mCornerRadius != 0.0f) {
            float f = this.mDrawableRect.left;
            float f2 = this.mDrawableRect.top;
            float width = this.mDrawableRect.width() + f;
            float height = this.mDrawableRect.height() + f2;
            float f3 = this.mCornerRadius;
            if (!this.mCornersRounded[0]) {
                this.mSquareCornersRect.set(f, f2, f + f3, f2 + f3);
                canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
            }
            if (!this.mCornersRounded[1]) {
                this.mSquareCornersRect.set(width - f3, f2, width, f3);
                canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
            }
            if (!this.mCornersRounded[2]) {
                this.mSquareCornersRect.set(width - f3, height - f3, width, height);
                canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
            }
            if (!this.mCornersRounded[3]) {
                this.mSquareCornersRect.set(f, height - f3, f3 + f, height);
                canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
            }
        }
    }

    private void redrawBorderForSquareCorners(@NonNull Canvas canvas) {
        if (!all(this.mCornersRounded) && this.mCornerRadius != 0.0f) {
            float f = this.mDrawableRect.left;
            float f2 = this.mDrawableRect.top;
            float width = f + this.mDrawableRect.width();
            float height = f2 + this.mDrawableRect.height();
            float f3 = this.mCornerRadius;
            float f4 = this.mBorderWidth / 2.0f;
            if (!this.mCornersRounded[0]) {
                canvas.drawLine(f - f4, f2, f + f3, f2, this.mBorderPaint);
                canvas.drawLine(f, f2 - f4, f, f2 + f3, this.mBorderPaint);
            }
            if (!this.mCornersRounded[1]) {
                Canvas canvas2 = canvas;
                float f5 = width;
                canvas2.drawLine((width - f3) - f4, f2, f5, f2, this.mBorderPaint);
                canvas2.drawLine(width, f2 - f4, f5, f2 + f3, this.mBorderPaint);
            }
            if (!this.mCornersRounded[2]) {
                Canvas canvas3 = canvas;
                float f6 = height;
                canvas3.drawLine((width - f3) - f4, height, width + f4, f6, this.mBorderPaint);
                canvas3.drawLine(width, height - f3, width, f6, this.mBorderPaint);
            }
            if (!this.mCornersRounded[3]) {
                canvas.drawLine(f - f4, height, f + f3, height, this.mBorderPaint);
                canvas.drawLine(f, height - f3, f, height, this.mBorderPaint);
            }
        }
    }

    public int getAlpha() {
        return this.mBitmapPaint.getAlpha();
    }

    public void setAlpha(int i) {
        this.mBitmapPaint.setAlpha(i);
        invalidateSelf();
    }

    public ColorFilter getColorFilter() {
        return this.mBitmapPaint.getColorFilter();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mBitmapPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setDither(boolean z) {
        this.mBitmapPaint.setDither(z);
        invalidateSelf();
    }

    public void setFilterBitmap(boolean z) {
        this.mBitmapPaint.setFilterBitmap(z);
        invalidateSelf();
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public float getCornerRadius(int i) {
        if (this.mCornersRounded[i]) {
            return this.mCornerRadius;
        }
        return 0.0f;
    }

    @NonNull
    public RoundedDrawable setCornerRadius(float f) {
        setCornerRadius(f, f, f, f);
        return this;
    }

    @NonNull
    public RoundedDrawable setCornerRadius(int i, float f) {
        int i2 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i2 != 0) {
            float f2 = this.mCornerRadius;
            if (!(f2 == 0.0f || f2 == f)) {
                throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
            }
        }
        if (i2 == 0) {
            if (only(i, this.mCornersRounded)) {
                this.mCornerRadius = 0.0f;
            }
            this.mCornersRounded[i] = false;
        } else {
            if (this.mCornerRadius == 0.0f) {
                this.mCornerRadius = f;
            }
            this.mCornersRounded[i] = true;
        }
        return this;
    }

    @NonNull
    public RoundedDrawable setCornerRadius(float f, float f2, float f3, float f4) {
        HashSet hashSet = new HashSet(4);
        hashSet.add(Float.valueOf(f));
        hashSet.add(Float.valueOf(f2));
        hashSet.add(Float.valueOf(f3));
        hashSet.add(Float.valueOf(f4));
        hashSet.remove(Float.valueOf(0.0f));
        if (hashSet.size() <= 1) {
            if (!hashSet.isEmpty()) {
                float floatValue = ((Float) hashSet.iterator().next()).floatValue();
                if (Float.isInfinite(floatValue) || Float.isNaN(floatValue) || floatValue < 0.0f) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Invalid radius value: ");
                    sb.append(floatValue);
                    throw new IllegalArgumentException(sb.toString());
                }
                this.mCornerRadius = floatValue;
            } else {
                this.mCornerRadius = 0.0f;
            }
            boolean z = false;
            this.mCornersRounded[0] = f > 0.0f;
            this.mCornersRounded[1] = f2 > 0.0f;
            this.mCornersRounded[2] = f3 > 0.0f;
            boolean[] zArr = this.mCornersRounded;
            if (f4 > 0.0f) {
                z = true;
            }
            zArr[3] = z;
            return this;
        }
        throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    @NonNull
    public RoundedDrawable setBorderWidth(float f) {
        this.mBorderWidth = f;
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
        return this;
    }

    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    @NonNull
    public RoundedDrawable setBorderColor(@ColorInt int i) {
        return setBorderColor(ColorStateList.valueOf(i));
    }

    @NonNull
    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    @NonNull
    public RoundedDrawable setBorderColor(@Nullable ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(0);
        }
        this.mBorderColor = colorStateList;
        this.mBorderPaint.setColor(this.mBorderColor.getColorForState(getState(), -16777216));
        return this;
    }

    public boolean isOval() {
        return this.mOval;
    }

    @NonNull
    public RoundedDrawable setOval(boolean z) {
        this.mOval = z;
        return this;
    }

    @NonNull
    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    @NonNull
    public RoundedDrawable setScaleType(@Nullable ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_CENTER;
        }
        if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    public TileMode getTileModeX() {
        return this.mTileModeX;
    }

    @NonNull
    public RoundedDrawable setTileModeX(TileMode tileMode) {
        if (this.mTileModeX != tileMode) {
            this.mTileModeX = tileMode;
            this.mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    public TileMode getTileModeY() {
        return this.mTileModeY;
    }

    @NonNull
    public RoundedDrawable setTileModeY(TileMode tileMode) {
        if (this.mTileModeY != tileMode) {
            this.mTileModeY = tileMode;
            this.mRebuildShader = true;
            invalidateSelf();
        }
        return this;
    }

    private static boolean only(int i, boolean[] zArr) {
        int length = zArr.length;
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i2 >= length) {
                return true;
            }
            boolean z2 = zArr[i2];
            if (i2 != i) {
                z = false;
            }
            if (z2 != z) {
                return false;
            }
            i2++;
        }
    }

    private static boolean any(boolean[] zArr) {
        for (boolean z : zArr) {
            if (z) {
                return true;
            }
        }
        return false;
    }

    private static boolean all(boolean[] zArr) {
        for (boolean z : zArr) {
            if (z) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public Bitmap toBitmap() {
        return drawableToBitmap(this);
    }
}
