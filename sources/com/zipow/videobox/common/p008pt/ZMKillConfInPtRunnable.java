package com.zipow.videobox.common.p008pt;

import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;

/* renamed from: com.zipow.videobox.common.pt.ZMKillConfInPtRunnable */
public class ZMKillConfInPtRunnable implements Runnable {
    private static final String TAG = "com.zipow.videobox.common.pt.ZMKillConfInPtRunnable";
    @Nullable
    private ILeaveConfCallBack mILeaveConfCallBack;
    private boolean mIsDirectShare = false;

    public void init(@Nullable ILeaveConfCallBack iLeaveConfCallBack, boolean z) {
        this.mILeaveConfCallBack = iLeaveConfCallBack;
        this.mIsDirectShare = z;
    }

    public void run() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            instance.stopConfService();
            if (this.mIsDirectShare) {
                instance.notifyConfProcessStopped();
            }
        }
        PTApp.getInstance().dispatchIdleMessage();
        ILeaveConfCallBack iLeaveConfCallBack = this.mILeaveConfCallBack;
        if (iLeaveConfCallBack != null) {
            iLeaveConfCallBack.onLeaveComplete();
        }
    }
}
