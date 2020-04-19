package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.NonNull;

public class ColorSelectedImage extends ImageView {
    private int color = 0;
    private Paint mPaint;

    private void init() {
        setWillNotCacheDrawing(true);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setAntiAlias(true);
    }

    public ColorSelectedImage(Context context) {
        super(context);
        init();
    }

    public ColorSelectedImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    public void onDraw(@NonNull Canvas canvas) {
        int i = this.color;
        if (i == 0) {
            super.onDraw(canvas);
            return;
        }
        this.mPaint.setColor(i);
        int height = getHeight() / 2;
        int width = getWidth() / 2;
        int min = Math.min(height, width);
        this.mPaint.setColor(-1);
        float f = (float) width;
        float f2 = (float) height;
        canvas.drawCircle(f, f2, (float) (min - 1), this.mPaint);
        this.mPaint.setColor(this.color);
        canvas.drawCircle(f, f2, (float) (min - 3), this.mPaint);
    }
}
