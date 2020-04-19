package com.zipow.videobox.share;

import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import androidx.annotation.Nullable;

public abstract class ZMDrawObject {
    public static final BlurMaskFilter BLURMASKFILTER = new BlurMaskFilter(5.0f, Blur.SOLID);
    public static final int DFT_LINE_WIDTH = 2;
    public static final EmbossMaskFilter EMBOSSMASKFILTER = new EmbossMaskFilter(direction, 0.4f, 6.0f, 3.5f);
    public static final float blur = 3.5f;
    public static final float[] direction = {1.0f, 1.0f, 1.0f};
    public static final float light = 0.4f;
    public static final float specular = 6.0f;
    protected int mAlpha;
    @Nullable
    protected Paint mPaint = null;
    protected int paintColor;

    public abstract void draw(Canvas canvas);

    public void setPaint(@Nullable Paint paint) {
        if (paint != null) {
            this.mPaint = new Paint();
            this.mPaint.set(paint);
        }
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
        this.mPaint.setAlpha(this.mAlpha);
    }

    @Nullable
    public Paint getPaint() {
        return this.mPaint;
    }

    public void ClearPaint() {
        this.mPaint = null;
    }

    public int getPaintColor() {
        return this.paintColor;
    }

    public void setPaintColor(int i) {
        this.paintColor = i;
    }
}
