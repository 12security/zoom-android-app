package com.zipow.videobox.share;

import android.graphics.Bitmap;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;

public class ShareServerImpl implements IShareServer {
    private static final int INTERVAL_TIME = 500;
    private static final String TAG = "ShareServerImpl";
    /* access modifiers changed from: private */
    public boolean bAutoCapture = true;
    private IShareView cacheView;
    /* access modifiers changed from: private */
    public transient boolean doRunning = false;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mShareServerRunnable;

    public ShareServerImpl(Handler handler) {
        this.mHandler = handler;
        this.mShareServerRunnable = createShareServerRunnable();
    }

    private boolean translateBitmap(Bitmap bitmap) {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.setCaptureFrame(bitmap);
    }

    @NonNull
    private Runnable createShareServerRunnable() {
        return new Runnable() {
            public void run() {
                ShareServerImpl.this.doShareFrame();
                if (ShareServerImpl.this.doRunning && ShareServerImpl.this.mHandler != null && ShareServerImpl.this.bAutoCapture && ShareServerImpl.this.mShareServerRunnable != null) {
                    ShareServerImpl.this.mHandler.postDelayed(ShareServerImpl.this.mShareServerRunnable, 500);
                }
            }
        };
    }

    public void startShare(boolean z) {
        this.bAutoCapture = z;
        startShareServer();
    }

    private void startShareServer() {
        this.doRunning = true;
        if (this.mShareServerRunnable == null) {
            this.mShareServerRunnable = createShareServerRunnable();
        }
        this.mHandler.post(this.mShareServerRunnable);
    }

    public void pauseShare() {
        this.doRunning = false;
    }

    public void endShare() {
        Runnable runnable = this.mShareServerRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        this.doRunning = false;
        this.mShareServerRunnable = null;
    }

    public void setCacheView(IShareView iShareView) {
        this.cacheView = iShareView;
    }

    /* access modifiers changed from: private */
    public boolean doShareFrame() {
        IShareView iShareView = this.cacheView;
        Bitmap cacheDrawingView = iShareView != null ? iShareView.getCacheDrawingView() : null;
        if (cacheDrawingView != null) {
            return translateBitmap(cacheDrawingView);
        }
        return false;
    }

    public boolean isShared() {
        return this.doRunning;
    }

    public void resumeShare() {
        startShareServer();
    }

    public void onRepaint() {
        if (this.doRunning) {
            if (this.mShareServerRunnable == null) {
                this.mShareServerRunnable = createShareServerRunnable();
            }
            this.mHandler.removeCallbacks(this.mShareServerRunnable);
            this.mHandler.post(this.mShareServerRunnable);
        }
    }
}
