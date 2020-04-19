package com.zipow.videobox.confapp;

public interface IConfToolbar {
    void disableToolbarAutoHide();

    int getToolbarHeight();

    int getTopBarHeight();

    boolean hasTipPointToToolbar();

    void hideToolbarDefaultDelayed();

    void hideToolbarDelayed(long j);

    boolean isToolbarShowing();

    void refreshToolbar();

    void showToolbar(boolean z, boolean z2);

    void updateSystemStatusBar();

    void updateTitleBar();
}
