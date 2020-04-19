package com.zipow.videobox.view.video;

import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class VideoRenderer implements Renderer {
    private static final String TAG = "VideoRenderer";
    /* access modifiers changed from: private */
    public float mFps = 0.0f;
    private long mGLThreadId = 0;
    /* access modifiers changed from: private */
    public ZPGLSurfaceView mGLView;
    @NonNull
    private Handler mHandler = new Handler();
    private int mHeight = 0;
    /* access modifiers changed from: private */
    public volatile boolean mIsStarted = false;
    @Nullable
    private Thread mScheduleThread;
    private int mWidth = 0;
    private boolean mbIntialized = false;
    /* access modifiers changed from: private */
    public volatile boolean mbPaused = false;

    private native void glRun(int i);

    public abstract void onDrawFrame(GL10 gl10, VideoRenderer videoRenderer);

    /* access modifiers changed from: protected */
    public void onGLSurfaceChanged(int i, int i2) {
    }

    /* access modifiers changed from: protected */
    public void onGLSurfaceCreated() {
    }

    public VideoRenderer(ZPGLSurfaceView zPGLSurfaceView) {
        this.mGLView = zPGLSurfaceView;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void initialize() {
        this.mbIntialized = true;
    }

    public boolean isInitialized() {
        return this.mbIntialized;
    }

    public void startRenderer(float f) {
        this.mFps = f;
        if (!this.mIsStarted) {
            this.mIsStarted = true;
            this.mbPaused = false;
            this.mGLView.setRenderMode(0);
            this.mScheduleThread = new Thread("ScheduleRendererThread") {
                public void run() {
                    do {
                        try {
                            if (!VideoRenderer.this.mbPaused) {
                                VideoRenderer.this.mGLView.requestRender();
                            }
                            sleep((long) (1000.0f / VideoRenderer.this.mFps));
                        } catch (Exception unused) {
                        }
                    } while (VideoRenderer.this.mIsStarted);
                }
            };
            this.mScheduleThread.start();
        }
    }

    public boolean isRunning() {
        return this.mIsStarted;
    }

    public void pauseRenderer() {
        this.mbPaused = true;
    }

    public void resumeRenderer() {
        this.mbPaused = false;
    }

    public void stopRenderer() {
        this.mIsStarted = false;
        this.mbPaused = true;
        this.mbIntialized = false;
        Thread thread = this.mScheduleThread;
        if (thread != null && thread.isAlive()) {
            this.mScheduleThread.interrupt();
            this.mScheduleThread = null;
        }
    }

    public void beforeGLContextDestroyed() {
        this.mGLThreadId = Thread.currentThread().getId();
        onSurfaceNeedDestroy();
        this.mGLThreadId = 0;
    }

    public void onDrawFrame(GL10 gl10) {
        onDrawFrame(gl10, this);
        glRun(0);
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.mGLThreadId = Thread.currentThread().getId();
        this.mWidth = i;
        this.mHeight = i2;
        onGLSurfaceChanged(i, i2);
        glRun(0);
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.mGLThreadId = Thread.currentThread().getId();
        onGLSurfaceCreated();
        glRun(0);
    }

    /* access modifiers changed from: protected */
    public void onSurfaceNeedDestroy() {
        glRun(0);
    }
}
