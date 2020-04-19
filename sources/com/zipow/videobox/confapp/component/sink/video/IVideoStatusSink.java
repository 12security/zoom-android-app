package com.zipow.videobox.confapp.component.sink.video;

import android.view.MotionEvent;
import androidx.annotation.NonNull;

public interface IVideoStatusSink {
    void onCameraStatusEvent();

    void onMyVideoStatusChanged();

    void onVideoEnableOrDisable();

    void onVideoMute();

    void onVideoViewSingleTapConfirmed(MotionEvent motionEvent);

    boolean onVideoViewTouchEvent(@NonNull MotionEvent motionEvent);
}
