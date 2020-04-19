package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoToolType;
import java.util.List;

public class AutoArrow extends ISAnnotateDraw {
    @NonNull
    private PointF endPos = new PointF(0.0f, 0.0f);
    private AnnoToolType m_annoToolType;
    @NonNull
    private Paint m_paint = new Paint();
    private float m_width = 4.0f;
    @NonNull
    private PointF startPos = new PointF(0.0f, 0.0f);

    public AutoArrow(float f, int i, int i2) {
        this.m_width = f;
        this.m_paint.setAntiAlias(true);
        this.m_paint.setDither(true);
        this.m_paint.setColor(i);
        this.m_paint.setAlpha(i2);
        this.m_paint.setStyle(Style.FILL);
        this.m_paint.setStrokeJoin(Join.MITER);
        this.m_paint.setStrokeWidth(2.0f);
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null) {
            Path path = new Path();
            switch (this.m_annoToolType) {
                case ANNO_TOOL_TYPE_AUTO_DOUBLE_ARROW:
                    drawdoubleArrow(path);
                    break;
                case ANNO_TOOL_TYPE_AUTO_ARROW1:
                    drawArrow1(path);
                    break;
                case ANNO_TOOL_TYPE_AUTO_ARROW2:
                    drawArrow2(path);
                    break;
            }
            canvas.drawPath(path, this.m_paint);
        }
    }

    public void drawArrow1(@NonNull Path path) {
        float f = this.m_width;
        float f2 = 3.0f * f;
        float max = Math.max(10.5f, 6.0f * f);
        float f3 = this.m_width;
        float max2 = Math.max(14.0f, 9.0f * f3);
        float max3 = Math.max(11.5f, this.m_width * 6.3f);
        PointF pointF = new PointF(this.startPos.x, this.startPos.y);
        PointF pointF2 = new PointF(this.endPos.x, this.endPos.y);
        float afpDistance = afpDistance(pointF, pointF2);
        if (afpDistance < f2) {
            pointF2 = afpAtpoint(new PointF(pointF.x, pointF.y), new PointF(pointF2.x, pointF2.y), f2 - afpDistance);
        }
        afpDistance(pointF, pointF2);
        PointF afpNormalize = afpNormalize(afpPerp(afpSub(pointF2, pointF)));
        PointF afpMult = afpMult(afpNormalize, f / 2.0f);
        PointF afpAdd = afpAdd(pointF, afpMult);
        PointF afpSub = afpSub(pointF, afpMult);
        PointF afpAtpoint = afpAtpoint(pointF, pointF2, 0.0f - max);
        PointF afpMult2 = afpMult(afpNormalize, f3 / 2.0f);
        PointF afpAdd2 = afpAdd(afpAtpoint, afpMult2);
        PointF afpSub2 = afpSub(afpAtpoint, afpMult2);
        PointF afpAtpoint2 = afpAtpoint(pointF, pointF2, 0.0f - max2);
        PointF afpMult3 = afpMult(afpNormalize, max3 / 2.0f);
        PointF afpAdd3 = afpAdd(afpAtpoint2, afpMult3);
        PointF afpSub3 = afpSub(afpAtpoint2, afpMult3);
        path.moveTo(afpAdd.x, afpAdd.y);
        path.lineTo(afpSub.x, afpSub.y);
        path.lineTo(afpSub2.x, afpSub2.y);
        path.lineTo(afpSub3.x, afpSub3.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(afpAdd3.x, afpAdd3.y);
        path.lineTo(afpAdd2.x, afpAdd2.y);
        path.close();
    }

    public void drawArrow2(@NonNull Path path) {
        float f = this.m_width;
        float f2 = 6.0f * f;
        float f3 = 40.0f * f;
        float f4 = 9.33333f * f;
        float f5 = 4.0f * f;
        float f6 = 10.3333f * f;
        float f7 = 9.3333f * f;
        PointF pointF = new PointF(this.startPos.x, this.startPos.y);
        PointF pointF2 = new PointF(this.endPos.x, this.endPos.y);
        float afpDistance = afpDistance(pointF, pointF2);
        if (afpDistance < f2) {
            pointF2 = afpAtpoint(pointF, pointF2, f2 - afpDistance);
        }
        float afpDistance2 = afpDistance(pointF, pointF2);
        if (afpDistance2 < f3) {
            float f8 = ((0.7f * afpDistance2) / f3) + 0.3f;
            f4 *= f8;
            f5 *= f8;
            f6 *= f8;
            f7 *= f8;
        }
        PointF afpNormalize = afpNormalize(afpPerp(afpSub(pointF2, pointF)));
        PointF afpMult = afpMult(afpNormalize, f / 2.0f);
        PointF afpAdd = afpAdd(pointF, afpMult);
        PointF afpSub = afpSub(pointF, afpMult);
        PointF afpAtpoint = afpAtpoint(pointF, pointF2, 0.0f - f4);
        PointF afpMult2 = afpMult(afpNormalize, f5 / 2.0f);
        PointF afpAdd2 = afpAdd(afpAtpoint, afpMult2);
        PointF afpSub2 = afpSub(afpAtpoint, afpMult2);
        PointF afpAtpoint2 = afpAtpoint(pointF, pointF2, 0.0f - f6);
        PointF afpMult3 = afpMult(afpNormalize, f7 / 2.0f);
        PointF afpAdd3 = afpAdd(afpAtpoint2, afpMult3);
        PointF afpSub3 = afpSub(afpAtpoint2, afpMult3);
        path.lineTo(afpAdd.x, afpAdd.y);
        path.lineTo(afpSub.x, afpSub.y);
        path.lineTo(afpSub2.x, afpSub2.y);
        path.lineTo(afpSub3.x, afpSub3.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(afpAdd3.x, afpAdd3.y);
        path.lineTo(afpAdd2.x, afpAdd2.y);
        path.lineTo(afpAdd.x, afpAdd.y);
        path.close();
    }

    public void drawdoubleArrow(@NonNull Path path) {
        float f = this.m_width;
        float f2 = 3.0f * f;
        float max = Math.max(10.5f, 6.0f * f);
        float f3 = this.m_width;
        float max2 = Math.max(14.0f, 9.0f * f3);
        float max3 = Math.max(11.5f, this.m_width * 6.3f);
        PointF pointF = new PointF(this.startPos.x, this.startPos.y);
        PointF pointF2 = new PointF(this.endPos.x, this.endPos.y);
        float afpDistance = afpDistance(new PointF(pointF.x, pointF.y), new PointF(pointF2.x, pointF2.y));
        if (afpDistance < f2) {
            pointF2 = afpAtpoint(new PointF(pointF.x, pointF.y), new PointF(pointF2.x, pointF2.y), f2 - afpDistance);
        }
        afpDistance(pointF, pointF2);
        PointF afpNormalize = afpNormalize(afpPerp(afpSub(pointF2, pointF)));
        float f4 = 0.0f - max;
        PointF afpAtpoint = afpAtpoint(pointF2, pointF, f4);
        PointF afpMult = afpMult(afpNormalize, f / 2.0f);
        PointF afpAdd = afpAdd(afpAtpoint, afpMult);
        PointF afpSub = afpSub(afpAtpoint, afpMult);
        float f5 = 0.0f - max2;
        PointF afpAtpoint2 = afpAtpoint(pointF2, pointF, f5);
        float f6 = max3 / 2.0f;
        PointF afpMult2 = afpMult(afpNormalize, f6);
        PointF afpAdd2 = afpAdd(afpAtpoint2, afpMult2);
        PointF afpSub2 = afpSub(afpAtpoint2, afpMult2);
        PointF afpAtpoint3 = afpAtpoint(pointF, pointF2, f4);
        PointF afpMult3 = afpMult(afpNormalize, f3 / 2.0f);
        PointF afpAdd3 = afpAdd(afpAtpoint3, afpMult3);
        PointF afpSub3 = afpSub(afpAtpoint3, afpMult3);
        PointF afpAtpoint4 = afpAtpoint(pointF, pointF2, f5);
        PointF afpMult4 = afpMult(afpNormalize, f6);
        PointF afpAdd4 = afpAdd(afpAtpoint4, afpMult4);
        PointF afpSub4 = afpSub(afpAtpoint4, afpMult4);
        path.moveTo(pointF.x, pointF.y);
        path.lineTo(afpAdd2.x, afpAdd2.y);
        path.lineTo(afpAdd.x, afpAdd.y);
        path.lineTo(afpAdd3.x, afpAdd3.y);
        path.lineTo(afpAdd4.x, afpAdd4.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(afpSub4.x, afpSub4.y);
        path.lineTo(afpSub3.x, afpSub3.y);
        path.lineTo(afpSub.x, afpSub.y);
        path.lineTo(afpSub2.x, afpSub2.y);
        path.close();
    }

    public void touchDown(float f, float f2) {
        PointF pointF = this.startPos;
        pointF.x = f;
        pointF.y = f2;
        PointF pointF2 = this.endPos;
        pointF2.x = f;
        pointF2.y = f2;
    }

    public void touchMove(float f, float f2) {
        PointF pointF = this.endPos;
        pointF.x = f;
        pointF.y = f2;
    }

    public void touchUp(float f, float f2) {
        PointF pointF = this.endPos;
        pointF.x = f;
        pointF.y = f2;
    }

    public void setAnnoPoints(@NonNull List<PointF> list, float f, float f2) {
        for (int i = 0; i < list.size(); i++) {
            PointF pointF = (PointF) list.get(i);
            if (i == 0) {
                touchDown(pointF.x, pointF.y);
            } else {
                touchMove(pointF.x, pointF.y);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public float afpDistance(@NonNull PointF pointF, @NonNull PointF pointF2) {
        return afpLength(afpSub(pointF, pointF2));
    }

    /* access modifiers changed from: 0000 */
    public float afpLength(@NonNull PointF pointF) {
        return (float) Math.sqrt((double) afpLengthSQ(pointF));
    }

    /* access modifiers changed from: 0000 */
    public float afpLengthSQ(@NonNull PointF pointF) {
        return (pointF.x * pointF.x) + (pointF.y * pointF.y);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpSub(@NonNull PointF pointF, @NonNull PointF pointF2) {
        return new PointF(pointF.x - pointF2.x, pointF.y - pointF2.y);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpAtpoint(@NonNull PointF pointF, @NonNull PointF pointF2, float f) {
        if (f == 0.0f) {
            return pointF2;
        }
        float afpDistance = afpDistance(pointF, pointF2);
        float f2 = afpDistance + f;
        return new PointF(((pointF2.x * f2) - (pointF.x * f)) / afpDistance, ((f2 * pointF2.y) - (f * pointF.y)) / afpDistance);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpNormalize(@NonNull PointF pointF) {
        return afpMult(pointF, 1.0f / afpLength(pointF));
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpMult(@NonNull PointF pointF, float f) {
        return new PointF(pointF.x * f, pointF.y * f);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpPerp(@NonNull PointF pointF) {
        return new PointF(-pointF.y, pointF.x);
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public PointF afpAdd(@NonNull PointF pointF, @NonNull PointF pointF2) {
        return new PointF(pointF.x + pointF2.x, pointF.y + pointF2.y);
    }

    public void setToolType(AnnoToolType annoToolType) {
        this.m_annoToolType = annoToolType;
    }
}
