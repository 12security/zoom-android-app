package com.zipow.videobox.view.video;

import android.content.Context;
import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.EGLContextFactory;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.stabilility.JavaCrashHandler;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class VideoView extends ZPGLSurfaceView implements OnGestureListener, OnDoubleTapListener {
    private static final boolean DEBUG = false;
    @NonNull
    private static String TAG = "VideoView";
    private GestureDetector mGestureDetector;
    private Listener mListener;

    private static class ConfigChooser implements EGLConfigChooser {
        private static int EGL_OPENGL_ES2_BIT = 4;
        @NonNull
        private static int[] s_configAttribs2 = {12324, 4, 12323, 4, 12322, 4, 12352, EGL_OPENGL_ES2_BIT, 12344};
        protected int mAlphaSize;
        protected int mBlueSize;
        protected int mDepthSize;
        protected int mGreenSize;
        protected int mRedSize;
        protected int mStencilSize;
        @NonNull
        private int[] mValue = new int[1];

        public ConfigChooser(int i, int i2, int i3, int i4, int i5, int i6) {
            this.mRedSize = i;
            this.mGreenSize = i2;
            this.mBlueSize = i3;
            this.mAlphaSize = i4;
            this.mDepthSize = i5;
            this.mStencilSize = i6;
        }

        @Nullable
        public EGLConfig chooseConfig(@NonNull EGL10 egl10, EGLDisplay eGLDisplay) {
            int[] iArr = new int[1];
            egl10.eglChooseConfig(eGLDisplay, s_configAttribs2, null, 0, iArr);
            int i = iArr[0];
            if (i > 0) {
                EGLConfig[] eGLConfigArr = new EGLConfig[i];
                egl10.eglChooseConfig(eGLDisplay, s_configAttribs2, eGLConfigArr, i, iArr);
                return chooseConfig(egl10, eGLDisplay, eGLConfigArr);
            }
            handleChooseConfigError(egl10, eGLDisplay);
            throw new IllegalArgumentException("No configs match configSpec");
        }

        @Nullable
        public EGLConfig chooseConfig(@NonNull EGL10 egl10, EGLDisplay eGLDisplay, @NonNull EGLConfig[] eGLConfigArr) {
            for (EGLConfig eGLConfig : eGLConfigArr) {
                EGL10 egl102 = egl10;
                EGLDisplay eGLDisplay2 = eGLDisplay;
                EGLConfig eGLConfig2 = eGLConfig;
                int findConfigAttrib = findConfigAttrib(egl102, eGLDisplay2, eGLConfig2, 12325, 0);
                int findConfigAttrib2 = findConfigAttrib(egl102, eGLDisplay2, eGLConfig2, 12326, 0);
                if (findConfigAttrib >= this.mDepthSize && findConfigAttrib2 >= this.mStencilSize) {
                    EGL10 egl103 = egl10;
                    EGLDisplay eGLDisplay3 = eGLDisplay;
                    EGLConfig eGLConfig3 = eGLConfig;
                    int findConfigAttrib3 = findConfigAttrib(egl103, eGLDisplay3, eGLConfig3, 12324, 0);
                    int findConfigAttrib4 = findConfigAttrib(egl103, eGLDisplay3, eGLConfig3, 12323, 0);
                    int findConfigAttrib5 = findConfigAttrib(egl103, eGLDisplay3, eGLConfig3, 12322, 0);
                    int findConfigAttrib6 = findConfigAttrib(egl103, eGLDisplay3, eGLConfig3, 12321, 0);
                    if (findConfigAttrib3 == this.mRedSize && findConfigAttrib4 == this.mGreenSize && findConfigAttrib5 == this.mBlueSize && findConfigAttrib6 == this.mAlphaSize) {
                        return eGLConfig;
                    }
                }
            }
            return null;
        }

        private int findConfigAttrib(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int i2) {
            return egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, this.mValue) ? this.mValue[0] : i2;
        }

        private void printConfigs(@NonNull EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr, StringBuilder sb) {
            int length = eGLConfigArr.length;
            if (sb != null) {
                sb.append(String.format("%d configurations in total\n", new Object[]{Integer.valueOf(length)}));
            }
            for (int i = 0; i < length; i++) {
                if (sb != null) {
                    sb.append(String.format("Configuration %d:\n", new Object[]{Integer.valueOf(i)}));
                }
                printConfig(egl10, eGLDisplay, eGLConfigArr[i], sb);
            }
        }

        private void printConfig(@NonNull EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig, StringBuilder sb) {
            StringBuilder sb2 = sb;
            int[] iArr = {12320, 12321, 12322, 12323, 12324, 12325, 12326, 12327, 12328, 12329, 12330, 12331, 12332, 12333, 12334, 12335, 12336, 12337, 12338, 12339, 12340, 12343, 12342, 12341, 12345, 12346, 12347, 12348, 12349, 12350, 12351, 12352, 12354};
            String[] strArr = {"EGL_BUFFER_SIZE", "EGL_ALPHA_SIZE", "EGL_BLUE_SIZE", "EGL_GREEN_SIZE", "EGL_RED_SIZE", "EGL_DEPTH_SIZE", "EGL_STENCIL_SIZE", "EGL_CONFIG_CAVEAT", "EGL_CONFIG_ID", "EGL_LEVEL", "EGL_MAX_PBUFFER_HEIGHT", "EGL_MAX_PBUFFER_PIXELS", "EGL_MAX_PBUFFER_WIDTH", "EGL_NATIVE_RENDERABLE", "EGL_NATIVE_VISUAL_ID", "EGL_NATIVE_VISUAL_TYPE", "EGL_PRESERVED_RESOURCES", "EGL_SAMPLES", "EGL_SAMPLE_BUFFERS", "EGL_SURFACE_TYPE", "EGL_TRANSPARENT_TYPE", "EGL_TRANSPARENT_RED_VALUE", "EGL_TRANSPARENT_GREEN_VALUE", "EGL_TRANSPARENT_BLUE_VALUE", "EGL_BIND_TO_TEXTURE_RGB", "EGL_BIND_TO_TEXTURE_RGBA", "EGL_MIN_SWAP_INTERVAL", "EGL_MAX_SWAP_INTERVAL", "EGL_LUMINANCE_SIZE", "EGL_ALPHA_MASK_SIZE", "EGL_COLOR_BUFFER_TYPE", "EGL_RENDERABLE_TYPE", "EGL_CONFORMANT"};
            int[] iArr2 = new int[1];
            for (int i = 0; i < iArr.length; i++) {
                int i2 = iArr[i];
                String str = strArr[i];
                if (!egl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i2, iArr2)) {
                    do {
                    } while (egl10.eglGetError() != 12288);
                } else if (sb2 != null) {
                    sb2.append(String.format("  %s: %d\n", new Object[]{str, Integer.valueOf(iArr2[0])}));
                }
            }
        }

        private void handleChooseConfigError(EGL10 egl10, EGLDisplay eGLDisplay) {
            StringBuilder sb = new StringBuilder();
            sb.append("EGL configuration\n");
            int[] iArr = new int[1];
            egl10.eglChooseConfig(eGLDisplay, null, null, 0, iArr);
            if (iArr[0] <= 0) {
                sb.append("No EGL configurations found!");
            } else {
                EGLConfig[] eGLConfigArr = new EGLConfig[iArr[0]];
                egl10.eglChooseConfig(eGLDisplay, null, eGLConfigArr, iArr[0], iArr);
                printConfigs(egl10, eGLDisplay, eGLConfigArr, sb);
            }
            JavaCrashHandler.setExtraInfo(sb.toString());
        }
    }

    private static class ContextFactory implements EGLContextFactory {
        private static int EGL_CONTEXT_CLIENT_VERSION = 12440;
        private VideoView mVideoView;

        public ContextFactory(VideoView videoView) {
            this.mVideoView = videoView;
        }

        public EGLContext createContext(@NonNull EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
            VideoView.checkEglError("Before eglCreateContext", egl10);
            EGLContext eglCreateContext = egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, new int[]{EGL_CONTEXT_CLIENT_VERSION, 2, 12344});
            VideoView.checkEglError("After eglCreateContext", egl10);
            return eglCreateContext;
        }

        public void destroyContext(@NonNull EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
            VideoView videoView = this.mVideoView;
            if (videoView != null) {
                videoView.beforeGLContextDestroyed();
            }
            egl10.eglDestroyContext(eGLDisplay, eGLContext);
        }
    }

    public interface Listener {
        void beforeGLContextDestroyed();

        void beforeSurfaceDestroyed();

        void onVideoViewDetachedFromWindow();

        void onVideoViewDoubleTap(MotionEvent motionEvent);

        void onVideoViewDown(MotionEvent motionEvent);

        void onVideoViewFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        boolean onVideoViewHoverEvent(MotionEvent motionEvent);

        void onVideoViewScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onVideoViewSingleTapConfirmed(MotionEvent motionEvent);

        boolean onVideoViewTouchEvent(MotionEvent motionEvent);
    }

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return true;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return true;
    }

    public VideoView(Context context) {
        super(context);
        init(context, false, 0, 0);
    }

    public VideoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, false, 0, 0);
    }

    public VideoView(Context context, boolean z, int i, int i2) {
        super(context);
        init(context, z, i, i2);
    }

    private void init(Context context, boolean z, int i, int i2) {
        if (z) {
            getHolder().setFormat(-3);
        }
        setEGLContextFactory(new ContextFactory(this));
        setEGLConfigChooser(z ? new ConfigChooser(8, 8, 8, 8, i, i2) : new ConfigChooser(5, 6, 5, 0, i, i2));
        if (!isInEditMode()) {
            Context context2 = context;
            this.mGestureDetector = new GestureDetector(context, this);
            this.mGestureDetector.setOnDoubleTapListener(this);
            this.mGestureDetector.setIsLongpressEnabled(false);
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.beforeSurfaceDestroyed();
        }
        super.surfaceDestroyed(surfaceHolder);
    }

    public boolean dispatchHoverEvent(MotionEvent motionEvent) {
        Listener listener = this.mListener;
        if (listener == null || !listener.onVideoViewHoverEvent(motionEvent)) {
            return super.dispatchHoverEvent(motionEvent);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Listener listener = this.mListener;
        if (listener == null || !listener.onVideoViewTouchEvent(motionEvent)) {
            return this.mGestureDetector.onTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void beforeGLContextDestroyed() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.beforeGLContextDestroyed();
        }
    }

    public boolean onDown(MotionEvent motionEvent) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewDown(motionEvent);
        }
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewFling(motionEvent, motionEvent2, f, f2);
        }
        return true;
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewScroll(motionEvent, motionEvent2, f, f2);
        }
        return true;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewDoubleTap(motionEvent);
        }
        return true;
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewSingleTapConfirmed(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onVideoViewDetachedFromWindow();
        }
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: private */
    public static void checkEglError(String str, EGL10 egl10) {
        do {
        } while (egl10.eglGetError() != 12288);
    }
}
