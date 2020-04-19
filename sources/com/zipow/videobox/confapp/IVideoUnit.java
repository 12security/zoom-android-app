package com.zipow.videobox.confapp;

public interface IVideoUnit extends IRendererUnit {
    long getUser();

    void removeUser();

    void setUser(long j);
}
