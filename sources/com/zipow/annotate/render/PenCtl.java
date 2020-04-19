package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PenCtl extends ISAnnotateDraw {
    private boolean mBHighlighter;
    @NonNull
    private Paint mPaint = new Paint();

    /* renamed from: mX */
    private float f306mX;

    /* renamed from: mY */
    private float f307mY;
    @NonNull
    private Path m_path = new Path();

    public PenCtl(float f, int i, int i2) {
        boolean z = false;
        this.mBHighlighter = false;
        this.f306mX = 0.0f;
        this.f307mY = 0.0f;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(f);
        this.mPaint.setColor(i);
        this.mPaint.setAlpha(i2);
        if (i2 < 255) {
            z = true;
        }
        this.mBHighlighter = z;
    }

    public void cleanAll() {
        this.m_path.reset();
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null && this.mBHighlighter) {
            canvas.drawPath(this.m_path, this.mPaint);
        }
    }

    public void touchDown(float f, float f2) {
        if (this.mBHighlighter) {
            this.m_path.moveTo(f, f2);
            this.m_path.lineTo(f, f2);
        }
        this.f306mX = f;
        this.f307mY = f2;
    }

    public void touchMove(float f, float f2) {
        if (this.mBHighlighter) {
            this.m_path.quadTo(this.f306mX, this.f307mY, f, f2);
        } else if (this.mCanvas != null) {
            this.mCanvas.drawLine(this.f306mX, this.f307mY, f, f2, this.mPaint);
        }
        this.f306mX = f;
        this.f307mY = f2;
    }

    public void touchUp(float f, float f2) {
        if (this.mBHighlighter) {
            this.m_path.lineTo(this.f306mX, this.f307mY);
        } else if (this.mCanvas != null) {
            this.mCanvas.drawLine(this.f306mX, this.f307mY, f, f2, this.mPaint);
        }
        this.f306mX = 0.0f;
        this.f307mY = 0.0f;
    }
}
