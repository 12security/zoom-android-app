package com.zipow.videobox.confapp.component.sink.common;

import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.component.ZMConfEnumViewMode;

public interface IConfUISink {
    boolean dispatchModeViewSwitch();

    boolean handleRequestPermissionResult(int i, @NonNull String str, int i2);

    void onConfReady();

    void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode);
}
