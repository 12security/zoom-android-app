package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;

public interface IRendererUnit {

    public static class PicInfo {
        int bmpHeight = 0;
        int bmpWidth = 0;
        long dataHandle = 0;
        int intExtra = 0;
        String strExtra = "";

        PicInfo(long j, int i, int i2) {
            this.dataHandle = j;
            this.bmpWidth = i;
            this.bmpHeight = i2;
        }

        PicInfo(long j, int i, int i2, int i3) {
            this.dataHandle = j;
            this.bmpWidth = i;
            this.bmpHeight = i2;
            this.intExtra = i3;
        }

        PicInfo(long j, int i, int i2, String str) {
            this.dataHandle = j;
            this.bmpWidth = i;
            this.bmpHeight = i2;
            this.strExtra = str;
        }
    }

    void clearRenderer();

    int getBottom();

    int getHeight();

    int getLeft();

    long getRendererInfo();

    int getRight();

    int getTop();

    @Nullable
    String getUnitName();

    int getWidth();

    boolean isPaused();

    void onCreate();

    void onDestroy();

    void onGLViewSizeChanged(int i, int i2);

    void onIdle();

    void pause();

    void resume();

    void setUnitName(String str);

    void updateUnitInfo(RendererUnitInfo rendererUnitInfo);
}
