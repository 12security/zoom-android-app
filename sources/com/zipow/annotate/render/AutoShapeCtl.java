package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoToolType;
import p021us.zoom.androidlib.util.UIUtil;

public class AutoShapeCtl extends ISAnnotateDraw {
    private float m_endx = 0.0f;
    private float m_endy = 0.0f;
    private int m_fontSize = 10;
    @NonNull
    private Paint m_paint = new Paint();
    private float m_startx = 0.0f;
    private float m_starty = 0.0f;
    private String m_title = "";
    @NonNull
    private Paint m_titlePaint = new Paint();
    private AnnoToolType m_toolType = AnnoToolType.ANNO_TOOL_TYPE_NONE;

    public AutoShapeCtl(float f, int i, int i2) {
        this.m_paint.setAntiAlias(true);
        this.m_paint.setDither(true);
        this.m_paint.setColor(i);
        Paint paint = this.m_paint;
        if (i2 <= 0) {
            i2 = 255;
        }
        paint.setAlpha(i2);
        this.m_paint.setStyle(Style.STROKE);
        Paint paint2 = this.m_paint;
        if (f <= 0.0f) {
            f = 4.0f;
        }
        paint2.setStrokeWidth(f);
    }

    public void setToolType(AnnoToolType annoToolType) {
        this.m_toolType = annoToolType;
        if (AnnoToolType.ANNO_TOOL_TYPE_AUTO_RECTANGLE == this.m_toolType || AnnoToolType.ANNO_TOOL_TYPE_AUTO_ELLIPSE == this.m_toolType || AnnoToolType.ANNO_TOOL_TYPE_AUTO_LINE == this.m_toolType) {
            this.m_paint.setStyle(Style.STROKE);
        } else {
            this.m_paint.setStyle(Style.FILL);
        }
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null) {
            switch (this.m_toolType) {
                case ANNO_TOOL_TYPE_AUTO_LINE:
                    canvas.drawLine(this.m_startx, this.m_starty, this.m_endx, this.m_endy, this.m_paint);
                    break;
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE:
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE_SEMI_FILL:
                case ANNO_TOOL_TYPE_AUTO_ELLIPSE_FILL:
                    RectF rectF = new RectF();
                    rectF.left = this.m_startx;
                    rectF.top = this.m_starty;
                    rectF.right = this.m_endx;
                    rectF.bottom = this.m_endy;
                    canvas.drawOval(rectF, this.m_paint);
                    break;
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE:
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE_SEMI_FILL:
                case ANNO_TOOL_TYPE_AUTO_RECTANGLE_FILL:
                    canvas.drawRect(this.m_startx, this.m_starty, this.m_endx, this.m_endy, this.m_paint);
                    break;
                case ANNO_TOOL_TYPE_AUTO_ROUNDEDRECTANGLE:
                case ANNO_TOOL_TYPE_AUTO_ROUNDEDRECTANGLE_SEMI_FILL:
                case ANNO_TOOL_TYPE_AUTO_ROUNDEDRECTANGLE_FILL:
                case ANNO_TOOL_TYPE_AUTO_ANNOTATOR_NAME:
                    canvas.drawRoundRect(new RectF(this.m_startx, this.m_starty, this.m_endx, this.m_endy), (float) UIUtil.dip2px(this.m_context, 4.0f), (float) UIUtil.dip2px(this.m_context, 4.0f), this.m_paint);
                    break;
            }
            String str = this.m_title;
            if (str != null && !str.isEmpty()) {
                initTextPaint();
                Rect rect = new Rect();
                Paint paint = this.m_titlePaint;
                String str2 = this.m_title;
                paint.getTextBounds(str2, 0, str2.length(), rect);
                float f = this.m_startx;
                float width = f + (((this.m_endx - f) - ((float) rect.width())) / 2.0f);
                float f2 = this.m_startx;
                if (width < f2) {
                    width = ((float) UIUtil.dip2px(this.m_context, 5.0f)) + f2;
                }
                FontMetrics fontMetrics = this.m_titlePaint.getFontMetrics();
                float f3 = fontMetrics.top;
                float f4 = fontMetrics.bottom;
                float f5 = this.m_endy;
                float f6 = (float) ((int) (((f5 - ((f5 - this.m_starty) / 2.0f)) - (f3 / 2.0f)) - (f4 / 2.0f)));
                this.m_title = TextUtils.ellipsize(this.m_title, new TextPaint(this.m_titlePaint), (this.m_endx - this.m_startx) - ((float) UIUtil.dip2px(this.m_context, 10.0f)), TruncateAt.END).toString();
                canvas.drawText(this.m_title, width, f6, this.m_titlePaint);
            }
        }
    }

    private void initTextPaint() {
        this.m_titlePaint.setColor(-1);
        this.m_titlePaint.setTextSize((float) this.m_fontSize);
        this.m_titlePaint.setAntiAlias(true);
        this.m_titlePaint.setDither(true);
        this.m_titlePaint.setAlpha(255);
        this.m_titlePaint.setStyle(Style.FILL);
        this.m_titlePaint.setStrokeJoin(Join.MITER);
        this.m_titlePaint.setStrokeWidth(4.0f);
    }

    public void touchDown(float f, float f2) {
        this.m_startx = f;
        this.m_starty = f2;
        this.m_endx = f;
        this.m_endy = f2;
    }

    public void touchMove(float f, float f2) {
        this.m_endx = f;
        this.m_endy = f2;
    }

    public void touchUp(float f, float f2) {
        this.m_endx = f;
        this.m_endy = f2;
    }

    public void setTextData(String str, int i, int i2, boolean z, boolean z2, int i3) {
        this.m_title = str;
        this.m_fontSize = i3;
    }
}
