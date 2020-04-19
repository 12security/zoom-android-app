package com.zipow.videobox.share;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ZMLine extends ZMDrawObject {
    private Path mPath;
    private int mWidth = 2;

    /* renamed from: x1 */
    private int f320x1;

    /* renamed from: x2 */
    private int f321x2;

    /* renamed from: y1 */
    private int f322y1;

    /* renamed from: y2 */
    private int f323y2;

    public int getX1() {
        return this.f320x1;
    }

    public void setX1(int i) {
        this.f320x1 = i;
    }

    public int getY1() {
        return this.f322y1;
    }

    public void setY1(int i) {
        this.f322y1 = i;
    }

    public int getX2() {
        return this.f321x2;
    }

    public void setX2(int i) {
        this.f321x2 = i;
    }

    public int getY2() {
        return this.f323y2;
    }

    public void setY2(int i) {
        this.f323y2 = i;
    }

    public void setPath(@NonNull Path path) {
        this.mPath = new Path();
        this.mPath.set(path);
    }

    @NonNull
    private Paint getLinePaint() {
        if (this.mPaint != null) {
            return this.mPaint;
        }
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeJoin(Join.ROUND);
        paint.setStrokeCap(Cap.ROUND);
        paint.setColor(-16777216 | this.paintColor);
        paint.setStrokeWidth((float) this.mWidth);
        paint.setAlpha(this.mAlpha);
        return paint;
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null) {
            Paint linePaint = getLinePaint();
            linePaint.setAlpha(this.mAlpha);
            Path path = this.mPath;
            if (path != null) {
                canvas.drawPath(path, linePaint);
            } else if (this.f320x1 != this.f321x2 || this.f322y1 != this.f323y2) {
                canvas.drawLine((float) this.f320x1, (float) this.f322y1, (float) this.f321x2, (float) this.f323y2, linePaint);
            }
        }
    }
}
