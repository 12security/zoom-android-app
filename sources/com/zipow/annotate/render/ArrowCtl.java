package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class ArrowCtl extends ISAnnotateDraw {
    @NonNull
    private Paint mPaint = new Paint();
    boolean m_bShareScreen = false;
    @NonNull
    private Path m_path = new Path();
    private String m_title = "";
    private int m_titlePosX = 0;
    private int m_titlePosY = 0;
    private int m_titleWidth = 0;
    @NonNull
    Paint titlePaint = new Paint();

    public void touchDown(float f, float f2) {
    }

    public void touchMove(float f, float f2) {
    }

    public void touchUp(float f, float f2) {
    }

    public ArrowCtl(float f, int i, int i2) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(i);
        this.mPaint.setAlpha(i2);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeJoin(Join.MITER);
        this.mPaint.setStrokeWidth(f);
        if ((((((Color.red(i) * 66) + (Color.green(i) * 129)) + (Color.green(i) * 25)) + 128) >> 8) + 16 > 128) {
            this.titlePaint.setColor(-16777216);
        } else {
            this.titlePaint.setColor(-1);
        }
        this.titlePaint.setTextSize(10.0f);
        this.titlePaint.setAntiAlias(true);
        this.titlePaint.setDither(true);
        this.titlePaint.setAlpha(i2);
        this.titlePaint.setStyle(Style.FILL);
        this.titlePaint.setStrokeJoin(Join.MITER);
        this.titlePaint.setStrokeWidth(f);
    }

    public void draw(@Nullable Canvas canvas) {
        if (canvas != null) {
            canvas.drawPath(this.m_path, this.mPaint);
            this.m_title = TextUtils.ellipsize(this.m_title, new TextPaint(this.titlePaint), (float) this.m_titleWidth, TruncateAt.END).toString();
            canvas.drawText(this.m_title, (float) this.m_titlePosX, (float) this.m_titlePosY, this.titlePaint);
        }
    }

    public void setAnnoPoints(@NonNull List<PointF> list, float f, float f2) {
        for (int i = 0; i < list.size(); i++) {
            float f3 = ((PointF) list.get(i)).x;
            float f4 = ((PointF) list.get(i)).y;
            if (i == 0) {
                this.m_path.moveTo(f3, f4);
            } else {
                this.m_path.lineTo(f3, f4);
            }
        }
    }

    public void setArrowData(int i, int i2, int i3, int i4, String str) {
        this.m_title = str;
        int i5 = i3 - i;
        this.m_titleWidth = i5;
        this.m_titlePosX = i + (i5 / 2);
        Rect rect = new Rect();
        Paint paint = this.titlePaint;
        String str2 = this.m_title;
        paint.getTextBounds(str2, 0, str2.length(), rect);
        int i6 = i4 - ((i4 - i2) / 2);
        if (this.m_bShareScreen) {
            this.m_titlePosY = i6 + rect.height();
        } else {
            this.m_titlePosY = i6 + (rect.height() / 2);
        }
    }

    public void setTextSize(int i) {
        this.titlePaint.setTextSize((float) i);
        this.titlePaint.setTextAlign(Align.CENTER);
    }

    public void setIsShareScreen(boolean z) {
        this.m_bShareScreen = z;
    }
}
