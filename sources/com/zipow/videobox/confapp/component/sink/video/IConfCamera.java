package com.zipow.videobox.confapp.component.sink.video;

import androidx.annotation.NonNull;

public interface IConfCamera {
    boolean canSwitchCamera();

    void onClickSwitchCamera();

    void refreshSwitchCameraButton();

    void switchCamera(@NonNull String str);
}
