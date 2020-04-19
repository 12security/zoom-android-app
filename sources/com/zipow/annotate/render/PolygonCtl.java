package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoToolType;
import java.util.List;

public class PolygonCtl extends ISAnnotateDraw {
    @NonNull
    private Paint mPaint = new Paint();
    @NonNull
    RectF mRectF = new RectF();
    @NonNull
    private Path m_path = new Path();
    private AnnoToolType m_type = AnnoToolType.ANNO_TOOL_TYPE_NONE;

    public PolygonCtl(float f, int i, int i2) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(i);
        this.mPaint.setAlpha(i2);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(f);
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null) {
            canvas.drawPath(this.m_path, this.mPaint);
            if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_QM == this.m_type) {
                this.mPaint.setStyle(Style.FILL);
                canvas.drawOval(this.mRectF, this.mPaint);
            }
        }
    }

    public void setToolType(AnnoToolType annoToolType) {
        this.m_type = annoToolType;
        if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_ARROW == this.m_type || AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_STAR == this.m_type || AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_HEART == this.m_type) {
            this.mPaint.setStyle(Style.FILL);
        }
    }

    public void setAnnoPoints(@NonNull List<PointF> list, float f, float f2) {
        if (f > 0.0f && f2 > 0.0f) {
            float strokeWidth = this.mPaint.getStrokeWidth() / 2.0f;
            RectF rectF = this.mRectF;
            rectF.left = f - strokeWidth;
            rectF.top = f2 - strokeWidth;
            float f3 = strokeWidth * 2.0f;
            rectF.right = rectF.left + f3;
            RectF rectF2 = this.mRectF;
            rectF2.bottom = rectF2.top + f3;
        }
        if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_HEART == this.m_type || AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_QM == this.m_type) {
            int size = (((list.size() - 1) / 3) * 3) + 1;
            this.m_path.moveTo(((PointF) list.get(0)).x, ((PointF) list.get(0)).y);
            for (int i = 1; i < size; i += 3) {
                Path path = this.m_path;
                float f4 = ((PointF) list.get(i)).x;
                float f5 = ((PointF) list.get(i)).y;
                int i2 = i + 1;
                float f6 = ((PointF) list.get(i2)).x;
                float f7 = ((PointF) list.get(i2)).y;
                int i3 = i + 2;
                path.cubicTo(f4, f5, f6, f7, ((PointF) list.get(i3)).x, ((PointF) list.get(i3)).y);
            }
            return;
        }
        for (int i4 = 0; i4 < list.size(); i4++) {
            float f8 = ((PointF) list.get(i4)).x;
            float f9 = ((PointF) list.get(i4)).y;
            if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_STAMP_X == this.m_type) {
                if (i4 % 2 != 1) {
                    this.m_path.moveTo(f8, f9);
                } else {
                    this.m_path.lineTo(f8, f9);
                }
            } else if (i4 == 0) {
                this.m_path.moveTo(f8, f9);
            } else {
                this.m_path.lineTo(f8, f9);
            }
        }
    }
}
