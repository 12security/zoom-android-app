package com.zipow.videobox.view;

public interface RoomSystemCallViewListener {
    void onCancel(boolean z);

    void onConnected(boolean z);

    void onConnecting(boolean z);

    void onFailed(boolean z);
}
