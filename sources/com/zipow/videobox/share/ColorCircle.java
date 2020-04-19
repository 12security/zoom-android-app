package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;

public class ColorCircle extends View {
    private static final float CENTER_RADIUS_SCALE = 0.4f;
    private float center_radius;
    private Paint mCenterPaint;
    private int[] mColors;
    private boolean mHighlightCenter;
    private IColorChangedListener mListener;
    private Paint mPaint;
    private boolean mTrackingCenter;

    public ColorCircle(Context context) {
        super(context);
        init();
    }

    public ColorCircle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /* access modifiers changed from: 0000 */
    public void init() {
        this.mColors = new int[]{SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
        SweepGradient sweepGradient = new SweepGradient(0.0f, 0.0f, this.mColors, null);
        this.mPaint = new Paint(1);
        this.mPaint.setShader(sweepGradient);
        this.mPaint.setStyle(Style.STROKE);
        this.mCenterPaint = new Paint(1);
        this.mCenterPaint.setStrokeWidth(5.0f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(@NonNull Canvas canvas) {
        float min = (float) (Math.min(getWidth(), getHeight()) / 2);
        float strokeWidth = this.center_radius + (this.mCenterPaint.getStrokeWidth() * 2.0f);
        float f = (min + strokeWidth) / 2.0f;
        canvas.translate((float) (getWidth() / 2), (float) (getHeight() / 2));
        this.mPaint.setStrokeWidth(min - strokeWidth);
        canvas.drawCircle(0.0f, 0.0f, f, this.mPaint);
        canvas.drawCircle(0.0f, 0.0f, this.center_radius, this.mCenterPaint);
        if (this.mTrackingCenter) {
            int color = this.mCenterPaint.getColor();
            this.mCenterPaint.setStyle(Style.STROKE);
            if (this.mHighlightCenter) {
                this.mCenterPaint.setAlpha(255);
            } else {
                this.mCenterPaint.setAlpha(128);
            }
            canvas.drawCircle(0.0f, 0.0f, this.center_radius + this.mCenterPaint.getStrokeWidth(), this.mCenterPaint);
            this.mCenterPaint.setStyle(Style.FILL);
            this.mCenterPaint.setColor(color);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int min = Math.min(MeasureSpec.getSize(i), MeasureSpec.getSize(i2));
        this.center_radius = (((float) min) * 0.4f) / 2.0f;
        setMeasuredDimension(min, min);
    }

    public void setColor(int i) {
        this.mCenterPaint.setColor(i);
        invalidate();
    }

    public int getColor() {
        return this.mCenterPaint.getColor();
    }

    public void setOnColorChangedListener(IColorChangedListener iColorChangedListener) {
        this.mListener = iColorChangedListener;
    }

    private int ave(int i, int i2, float f) {
        return i + Math.round(f * ((float) (i2 - i)));
    }

    private int interpColor(@NonNull int[] iArr, float f) {
        if (f <= 0.0f) {
            return iArr[0];
        }
        if (f >= 1.0f) {
            return iArr[iArr.length - 1];
        }
        float length = f * ((float) (iArr.length - 1));
        int i = (int) length;
        float f2 = length - ((float) i);
        int i2 = iArr[i];
        int i3 = iArr[i + 1];
        return Color.argb(ave(Color.alpha(i2), Color.alpha(i3), f2), ave(Color.red(i2), Color.red(i3), f2), ave(Color.green(i2), Color.green(i3), f2), ave(Color.blue(i2), Color.blue(i3), f2));
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(@androidx.annotation.NonNull android.view.MotionEvent r7) {
        /*
            r6 = this;
            float r0 = r7.getX()
            int r1 = r6.getWidth()
            int r1 = r1 / 2
            float r1 = (float) r1
            float r0 = r0 - r1
            float r1 = r7.getY()
            int r2 = r6.getHeight()
            int r2 = r2 / 2
            float r2 = (float) r2
            float r1 = r1 - r2
            float r2 = android.graphics.PointF.length(r0, r1)
            float r3 = r6.center_radius
            r4 = 0
            r5 = 1
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 > 0) goto L_0x0026
            r2 = 1
            goto L_0x0027
        L_0x0026:
            r2 = 0
        L_0x0027:
            int r7 = r7.getAction()
            switch(r7) {
                case 0: goto L_0x0048;
                case 1: goto L_0x002f;
                case 2: goto L_0x0052;
                default: goto L_0x002e;
            }
        L_0x002e:
            goto L_0x0081
        L_0x002f:
            boolean r7 = r6.mTrackingCenter
            if (r7 == 0) goto L_0x0081
            if (r2 == 0) goto L_0x0042
            com.zipow.videobox.share.IColorChangedListener r7 = r6.mListener
            if (r7 == 0) goto L_0x0042
            android.graphics.Paint r0 = r6.mCenterPaint
            int r0 = r0.getColor()
            r7.onColorPicked(r0)
        L_0x0042:
            r6.mTrackingCenter = r4
            r6.invalidate()
            goto L_0x0081
        L_0x0048:
            r6.mTrackingCenter = r2
            if (r2 == 0) goto L_0x0052
            r6.mHighlightCenter = r5
            r6.invalidate()
            goto L_0x0081
        L_0x0052:
            boolean r7 = r6.mTrackingCenter
            if (r7 == 0) goto L_0x0060
            boolean r7 = r6.mHighlightCenter
            if (r7 == r2) goto L_0x0081
            r6.mHighlightCenter = r2
            r6.invalidate()
            goto L_0x0081
        L_0x0060:
            double r1 = (double) r1
            double r3 = (double) r0
            double r0 = java.lang.Math.atan2(r1, r3)
            float r7 = (float) r0
            r0 = 1086918619(0x40c90fdb, float:6.2831855)
            float r7 = r7 / r0
            r0 = 0
            int r0 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r0 >= 0) goto L_0x0073
            r0 = 1065353216(0x3f800000, float:1.0)
            float r7 = r7 + r0
        L_0x0073:
            int[] r0 = r6.mColors
            int r7 = r6.interpColor(r0, r7)
            android.graphics.Paint r0 = r6.mCenterPaint
            r0.setColor(r7)
            r6.invalidate()
        L_0x0081:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.share.ColorCircle.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
