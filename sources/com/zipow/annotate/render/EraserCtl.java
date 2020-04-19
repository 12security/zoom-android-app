package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

public class EraserCtl extends ISAnnotateDraw {
    private Paint mPaint = new Paint();
    private Path m_path = new Path();
    private List<PointF> pointList = new ArrayList();

    public void touchDown(float f, float f2) {
    }

    public EraserCtl(float f, int i, int i2) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(12.0f);
        this.mPaint.setColor(Color.argb(255, 150, 255, 255));
        this.mPaint.setAlpha(200);
    }

    public void draw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawPath(this.m_path, this.mPaint);
        }
    }

    public void touchMove(float f, float f2) {
        if (this.mCanvas != null) {
            this.pointList.add(new PointF(f, f2));
            if (this.pointList.size() > 5) {
                this.pointList.remove(0);
            }
            this.m_path.reset();
            for (int i = 0; i < this.pointList.size(); i++) {
                float f3 = ((PointF) this.pointList.get(i)).x;
                float f4 = ((PointF) this.pointList.get(i)).y;
                if (i == 0) {
                    this.m_path.moveTo(f3, f4);
                } else {
                    this.m_path.lineTo(f3, f4);
                }
            }
        }
    }

    public void touchUp(float f, float f2) {
        this.pointList.clear();
        this.m_path.reset();
    }
}
