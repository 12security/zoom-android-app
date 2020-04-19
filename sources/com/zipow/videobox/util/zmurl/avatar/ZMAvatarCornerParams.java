package com.zipow.videobox.util.zmurl.avatar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ZMAvatarCornerParams {
    private boolean bCircle;
    private int borderColor;
    private int borderSize;
    private int clientHeight;
    private int clientWidth;
    private float cornerRatio;

    public ZMAvatarCornerParams(float f, int i, int i2) {
        this.cornerRatio = f;
        this.borderColor = i;
        this.borderSize = i2;
    }

    public ZMAvatarCornerParams(float f, int i, boolean z, int i2, int i3, int i4) {
        this.cornerRatio = f;
        this.borderColor = i;
        this.bCircle = z;
        this.clientWidth = i2;
        this.clientHeight = i3;
        this.borderSize = i4;
    }

    public float getCornerRatio() {
        return this.cornerRatio;
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public boolean isbCircle() {
        return this.bCircle;
    }

    public int getClientWidth() {
        return this.clientWidth;
    }

    public int getClientHeight() {
        return this.clientHeight;
    }

    public int getBorderSize() {
        return this.borderSize;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZMAvatarCornerParams zMAvatarCornerParams = (ZMAvatarCornerParams) obj;
        if (Float.compare(zMAvatarCornerParams.cornerRatio, this.cornerRatio) != 0 || this.borderColor != zMAvatarCornerParams.borderColor || this.bCircle != zMAvatarCornerParams.bCircle || this.clientWidth != zMAvatarCornerParams.clientWidth || this.clientHeight != zMAvatarCornerParams.clientHeight) {
            return false;
        }
        if (this.borderSize != zMAvatarCornerParams.borderSize) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        float f = this.cornerRatio;
        return ((((((((((f != 0.0f ? Float.floatToIntBits(f) : 0) * 31) + this.borderColor) * 31) + (this.bCircle ? 1 : 0)) * 31) + this.clientWidth) * 31) + this.clientHeight) * 31) + this.borderSize;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ZMAvatarCornerParams{cornerRatio=");
        sb.append(this.cornerRatio);
        sb.append(", borderColor=");
        sb.append(this.borderColor);
        sb.append(", bCircle=");
        sb.append(this.bCircle);
        sb.append(", clientWidth=");
        sb.append(this.clientWidth);
        sb.append(", clientHeight=");
        sb.append(this.clientHeight);
        sb.append(", borderSize=");
        sb.append(this.borderSize);
        sb.append('}');
        return sb.toString();
    }
}
