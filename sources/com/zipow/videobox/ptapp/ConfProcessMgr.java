package com.zipow.videobox.ptapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ConfService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.IPCHelper;

public class ConfProcessMgr {
    private static final String TAG = "ConfProcessMgr";
    @Nullable
    private static ConfProcessMgr instance;
    private int mCurrentConfProcessId = 0;
    private int mLastError = 0;

    private ConfProcessMgr() {
    }

    @NonNull
    public static synchronized ConfProcessMgr getInstance() {
        ConfProcessMgr confProcessMgr;
        synchronized (ConfProcessMgr.class) {
            if (instance == null) {
                instance = new ConfProcessMgr();
            }
            confProcessMgr = instance;
        }
        return confProcessMgr;
    }

    public synchronized int createConfProcess(String str) {
        int confProcessId;
        boolean z = !VideoBoxApplication.getInstance().isConfUIPreloaded();
        this.mCurrentConfProcessId = 0;
        Bundle bundle = new Bundle();
        bundle.putString(ConfService.ARG_COMMAND_LINE, str);
        this.mLastError = VideoBoxApplication.getInstance().startConfService(bundle);
        confProcessId = VideoBoxApplication.getInstance().getConfProcessId();
        this.mCurrentConfProcessId = confProcessId;
        if (this.mLastError == 0 && z) {
            ConfActivity.startConfUI(VideoBoxApplication.getInstance());
        }
        if (confProcessId <= 0) {
            IPCHelper.getInstance().sendBOStatusChangeComplete();
        }
        return confProcessId;
    }

    public boolean isConfProcessRunning() {
        if (VideoBoxApplication.getInstance().getConfService() != null) {
            return true;
        }
        return VideoBoxApplication.getInstance().isConfProcessRunning();
    }

    public boolean terminateConfProcess(int i) {
        if (i == VideoBoxApplication.getInstance().getConfProcessId()) {
            VideoBoxApplication.getInstance().killConfProcess();
        }
        return true;
    }

    public synchronized int getCurrentConfProcessId() {
        return this.mCurrentConfProcessId;
    }

    public int getLastError() {
        return this.mLastError;
    }
}
