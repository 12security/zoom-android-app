package com.zipow.videobox.confapp;

import androidx.annotation.NonNull;

public class RendererUnitInfo {
    public int height;
    public int left;
    public int top;
    public int width;

    public RendererUnitInfo(int i, int i2, int i3, int i4) {
        this.left = i;
        this.top = i2;
        this.width = i3;
        this.height = i4;
    }

    @NonNull
    public String toString() {
        return String.format("[%d, %d, %d, %d]", new Object[]{Integer.valueOf(this.left), Integer.valueOf(this.top), Integer.valueOf(this.width), Integer.valueOf(this.height)});
    }
}
