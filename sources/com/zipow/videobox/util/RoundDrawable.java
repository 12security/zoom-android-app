package com.zipow.videobox.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.UIUtil;

public class RoundDrawable extends InsetDrawable {
    private int mBorderColor;
    private int mBorderSize;
    private int mClientHeight;
    private int mClientWidth;
    private float mCornerRatio;
    private boolean mbCircle;

    public RoundDrawable(Drawable drawable, float f, int i) {
        this(drawable, f, i, true, 0, 0, UIUtil.dip2px(VideoBoxApplication.getInstance(), 1.0f));
    }

    public RoundDrawable(Drawable drawable, float f, int i, boolean z, int i2, int i3, int i4) {
        super(drawable, 0);
        this.mCornerRatio = 0.0f;
        this.mBorderColor = -1;
        this.mbCircle = true;
        this.mClientWidth = 0;
        this.mClientHeight = 0;
        this.mCornerRatio = f;
        this.mBorderColor = i;
        this.mbCircle = z;
        this.mClientWidth = i2;
        this.mClientHeight = i3;
        this.mBorderSize = i4;
    }

    public void setBorderSize(int i) {
        this.mBorderSize = i;
    }

    public boolean setClientSize(int i, int i2) {
        boolean z;
        if (this.mClientWidth != i) {
            this.mClientWidth = i;
            z = true;
        } else {
            z = false;
        }
        if (this.mClientHeight == i2) {
            return z;
        }
        this.mClientHeight = i2;
        return true;
    }

    public int getIntrinsicWidth() {
        int i = this.mClientWidth;
        if (i > 0) {
            int i2 = this.mClientHeight;
            if (i2 > 0) {
                int min = Math.min(i, i2);
                if (super.getIntrinsicWidth() <= super.getIntrinsicHeight()) {
                    return min;
                }
                return (super.getIntrinsicWidth() * min) / super.getIntrinsicHeight();
            }
        }
        return super.getIntrinsicWidth();
    }

    public int getIntrinsicHeight() {
        int i = this.mClientWidth;
        if (i > 0) {
            int i2 = this.mClientHeight;
            if (i2 > 0) {
                int min = Math.min(i, i2);
                if (super.getIntrinsicWidth() >= super.getIntrinsicHeight()) {
                    return min;
                }
                return (super.getIntrinsicHeight() * min) / super.getIntrinsicWidth();
            }
        }
        return super.getIntrinsicHeight();
    }

    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        Rect rect = new Rect(bounds.left, bounds.top, bounds.right, bounds.bottom);
        int i = this.mBorderSize;
        rect.inset(i, i);
        RectF rectF = new RectF(rect);
        if (this.mbCircle) {
            float min = Math.min(rectF.width(), rectF.height());
            rectF.inset((rectF.width() - min) / 2.0f, (rectF.height() - min) / 2.0f);
        }
        float width = this.mCornerRatio * ((float) rect.width());
        float height = this.mCornerRatio * ((float) rect.height());
        Path path = new Path();
        path.addRoundRect(rectF, width, height, Direction.CCW);
        canvas.save();
        canvas.clipPath(path);
        super.draw(canvas);
        canvas.restore();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(this.mBorderColor);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth((float) this.mBorderSize);
        canvas.drawPath(path, paint);
    }

    public void setBorderColor(int i) {
        this.mBorderColor = i;
    }

    public void setCornerRatio(float f) {
        this.mCornerRatio = f;
    }
}
