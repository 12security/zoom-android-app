package com.zipow.videobox.share;

public interface IShareServer {
    void endShare();

    boolean isShared();

    void onRepaint();

    void pauseShare();

    void resumeShare();

    void setCacheView(IShareView iShareView);

    void startShare(boolean z) throws ShareException;
}
