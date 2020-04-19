package com.zipow.annotate.render;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import com.zipow.annotate.AnnoToolType;
import java.io.Serializable;
import java.util.List;

public class AnnoDrawObj implements Serializable {
    public int alpha;
    public List<PointF> annoPoints;
    public int color;
    public float endX;
    public float endY;
    public int fontSize;
    public boolean isBold;
    public boolean isItalic;
    @Nullable
    public Path path;
    public float startX;
    public float startY;
    @Nullable
    public String text;
    public int toolType;
    public int width;

    public AnnoDrawObj() {
        this.toolType = AnnoToolType.ANNO_TOOL_TYPE_NONE.ordinal();
        this.width = 4;
        this.color = SupportMenu.CATEGORY_MASK;
        this.alpha = 255;
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.endX = 0.0f;
        this.endY = 0.0f;
        this.text = "";
        this.isBold = false;
        this.isItalic = false;
        this.fontSize = 10;
    }

    public AnnoDrawObj(boolean z, boolean z2, int i) {
        this.isBold = z;
        this.isItalic = z2;
        this.fontSize = i;
    }

    public void transition(int i, int i2) {
        if (this.annoPoints != null) {
            for (int i3 = 0; i3 < this.annoPoints.size(); i3++) {
                PointF pointF = (PointF) this.annoPoints.get(i3);
                float f = (float) i;
                pointF.x *= f;
                pointF.y = (pointF.y * f) - ((float) i2);
            }
        }
        this.width *= i;
        float f2 = (float) i;
        this.startX *= f2;
        float f3 = (float) i2;
        this.startY = (this.startY * f2) - f3;
        this.endX *= f2;
        this.endY = (this.endY * f2) - f3;
    }
}
