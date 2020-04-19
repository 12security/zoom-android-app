package com.zipow.annotate.render;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import com.zipow.annotate.AnnoToolType;
import java.util.List;

public class ISAnnotateDraw {
    public Canvas mCanvas;
    public Context m_context;

    public void cleanAll() {
    }

    public void draw(Canvas canvas) {
    }

    public void draw(Canvas canvas, Path path) {
    }

    public void setAnnoPoints(List<PointF> list, float f, float f2) {
    }

    public void setArrowData(int i, int i2, int i3, int i4, String str) {
    }

    public void setIsShareScreen(boolean z) {
    }

    public void setResources(Resources resources) {
    }

    public void setTextData(String str, int i, int i2, boolean z, boolean z2, int i3) {
    }

    public void setTextSize(int i) {
    }

    public void setToolType(AnnoToolType annoToolType) {
    }

    public void touchDown(float f, float f2) {
    }

    public void touchMove(float f, float f2) {
    }

    public void touchUp(float f, float f2) {
    }

    public void setContext(Context context) {
        this.m_context = context;
    }

    public void setCanvas(Canvas canvas) {
        this.mCanvas = canvas;
    }
}
