package com.zipow.nydus;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.view.SurfaceHolder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.safe.SafeObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VideoCapturer implements PreviewCallback {
    @NonNull
    private static final VideoCapCapability[] FALLBACK_CAPS = new VideoCapCapability[1];
    public static final float RATIO_16_9 = 1.7777778f;
    public static final float RATIO_4_3 = 1.3333334f;
    private static final String TAG = "VideoCapturer";
    @Nullable
    private static VideoCapturer instance;
    public static long sLastStopCameraTime;
    @Nullable
    private Camera mCamera;
    private int mCameraId = 0;
    /* access modifiers changed from: private */
    @NonNull
    public HashMap<Integer, VideoCapCapability[]> mCapsMap = new HashMap<>();
    private int mCurBuffSize = 0;
    private int mCurrentZoom = 0;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mDectectCapturePausedRunnable;
    @NonNull
    private Set<Integer> mFailedCameras = new HashSet();
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mIsCapturePaused = false;
    /* access modifiers changed from: private */
    public boolean mIsCapturing = false;
    /* access modifiers changed from: private */
    public boolean mIsSurfaceInvalidated = true;
    private boolean mIsZoomSupported = false;
    /* access modifiers changed from: private */
    @Nullable
    public VideoFormat mLastDataFormat = null;
    /* access modifiers changed from: private */
    public long mLastDataTimeStamp = 0;
    /* access modifiers changed from: private */
    @Nullable
    public byte[] mLastFrameData = null;
    private Listener mListener;
    @NonNull
    private Object mLockCamera = new Object();
    private int mMaxCaptureHeight = 0;
    private int mMaxZoom = 0;
    /* access modifiers changed from: private */
    public long mNativeHandle = 0;
    private float mSendRatio = 1.3333334f;
    /* access modifiers changed from: private */
    public SurfaceHolder mSurfaceHolder;
    @Nullable
    private VideoFormat mVideoFormat;
    private Parameters mZoomParameters = null;

    public interface Listener {
        void onCapturePaused();

        void onCaptureResumed();
    }

    private int imageFormat2NydusVideoType(int i) {
        if (i == 17) {
            return 12;
        }
        if (i != 20) {
            return i != 842094169 ? 0 : 2;
        }
        return 3;
    }

    private int nydusVideoType2ImageFormat(int i) {
        if (i == 12) {
            return 17;
        }
        switch (i) {
            case 2:
                return 842094169;
            case 3:
                return 20;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: private */
    public native void onFrameCaptured(long j, byte[] bArr, VideoFormat videoFormat);

    static {
        FALLBACK_CAPS[0] = new VideoCapCapability();
        VideoCapCapability[] videoCapCapabilityArr = FALLBACK_CAPS;
        videoCapCapabilityArr[0].videoType = 12;
        videoCapCapabilityArr[0].minFps = 1.0f;
        videoCapCapabilityArr[0].maxFps = 30.0f;
        videoCapCapabilityArr[0].width = 640;
        videoCapCapabilityArr[0].height = CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
    }

    public void initCameraCapabilities() {
        new Thread("VideoCapturer init thread") {
            public void run() {
                if (!VideoCapturer.this.loadCapabilitiesFromConfig()) {
                    int numberOfCameras = NydusUtil.getNumberOfCameras();
                    for (int i = 0; i < numberOfCameras; i++) {
                        VideoCapturer.this.getCameraCapability(i);
                    }
                    if (VideoCapturer.this.mCapsMap.size() == numberOfCameras) {
                        VideoCapturer.this.saveCapabilitiesToConfig();
                    }
                }
            }
        }.start();
    }

    public void handleZoom(boolean z, int i) {
        if (this.mCamera != null && isZoomSupported()) {
            int i2 = 0;
            if (z) {
                int i3 = this.mCurrentZoom;
                int i4 = this.mMaxZoom;
                if (i3 < i4) {
                    i2 = i3 + i < i4 ? i3 + i : i4;
                    this.mZoomParameters.setZoom(i2);
                    this.mCamera.setParameters(this.mZoomParameters);
                    this.mCurrentZoom = i2;
                }
            }
            int i5 = this.mCurrentZoom;
            if (i5 > 0 && i5 - i > 0) {
                i2 = i5 - i;
            }
            try {
                this.mZoomParameters.setZoom(i2);
                this.mCamera.setParameters(this.mZoomParameters);
                this.mCurrentZoom = i2;
            } catch (RuntimeException unused) {
            }
        }
    }

    public int getMaxZoom() {
        return this.mMaxZoom;
    }

    public boolean isZoomSupported() {
        return this.mIsZoomSupported && this.mIsCapturing && this.mZoomParameters != null && this.mCamera != null;
    }

    private VideoCapturer() {
    }

    @NonNull
    public static synchronized VideoCapturer getInstance() {
        VideoCapturer videoCapturer;
        synchronized (VideoCapturer.class) {
            if (instance == null) {
                instance = new VideoCapturer();
            }
            videoCapturer = instance;
        }
        return videoCapturer;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public boolean init(long j, int i, int i2, int i3, int i4, float f) {
        int numberOfCameras = NydusUtil.getNumberOfCameras();
        if (i < 0 || i >= numberOfCameras) {
            return false;
        }
        this.mNativeHandle = j;
        this.mCameraId = i;
        this.mVideoFormat = new VideoFormat();
        if (i2 != 0) {
            VideoFormat videoFormat = this.mVideoFormat;
            videoFormat.videoType = i2;
            videoFormat.width = i3;
            videoFormat.height = i4;
            videoFormat.fps = f;
        } else if (!selectDefaultVideoFormat(this.mVideoFormat, i)) {
            return true;
        }
        return true;
    }

    public void uninit() {
        this.mNativeHandle = 0;
    }

    private boolean selectDefaultVideoFormat(@NonNull VideoFormat videoFormat, int i) {
        VideoCapCapability[] cameraCapability = getCameraCapability(i);
        int i2 = 0;
        if (cameraCapability == null || cameraCapability.length == 0) {
            return false;
        }
        boolean z = false;
        for (VideoCapCapability videoCapCapability : cameraCapability) {
            if (videoCapCapability.videoType == 12) {
                z = true;
            }
        }
        int i3 = -1;
        int maxCaptureHeight = getMaxCaptureHeight();
        int i4 = 10000;
        for (int i5 = 0; i5 < cameraCapability.length; i5++) {
            if ((!z || cameraCapability[i5].videoType == 12) && cameraCapability[i5].height >= maxCaptureHeight && cameraCapability[i5].height < i4) {
                i4 = cameraCapability[i5].height;
                i3 = i5;
            }
        }
        if (i3 >= 0) {
            float f = 100.0f;
            for (int i6 = 0; i6 < cameraCapability.length; i6++) {
                if ((!z || cameraCapability[i6].videoType == 12) && cameraCapability[i6].height == i4) {
                    float abs = Math.abs((((float) cameraCapability[i6].width) / ((float) cameraCapability[i6].height)) - 1.3333334f);
                    if (abs < f) {
                        i3 = i6;
                        f = abs;
                    }
                }
            }
        }
        if (i3 >= 0) {
            i2 = i3;
        }
        videoFormat.videoType = cameraCapability[i2].videoType;
        videoFormat.width = cameraCapability[i2].width;
        videoFormat.height = cameraCapability[i2].height;
        videoFormat.fps = cameraCapability[i2].maxFps;
        return true;
    }

    @Nullable
    public VideoFormat getOutputVideoFormat() {
        if (this.mVideoFormat == null) {
            synchronized (this.mLockCamera) {
                if (this.mCamera != null) {
                    this.mVideoFormat = getOutputVideoFormat(this.mCamera);
                }
            }
        }
        return this.mVideoFormat;
    }

    @Nullable
    public VideoFormat getOutputVideoFormat(@Nullable Camera camera) {
        if (camera == null) {
            return null;
        }
        try {
            Parameters parameters = camera.getParameters();
            Size previewSize = parameters.getPreviewSize();
            VideoFormat videoFormat = new VideoFormat();
            videoFormat.videoType = imageFormat2NydusVideoType(parameters.getPreviewFormat());
            videoFormat.width = previewSize.width;
            videoFormat.height = previewSize.height;
            videoFormat.fps = ((float) parameters.getPreviewFrameRate()) / 1000.0f;
            return videoFormat;
        } catch (Exception unused) {
            return null;
        }
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.mIsSurfaceInvalidated = false;
        this.mSurfaceHolder = surfaceHolder;
        if (this.mIsCapturing && this.mIsCapturePaused) {
            new Thread("StartCameraThread") {
                public void run() {
                    VideoCapturer.this.startCapture();
                }
            }.start();
        }
    }

    public void onSurfaceInvalidated() {
        this.mIsSurfaceInvalidated = true;
        if (this.mIsCapturing) {
            onCapturePaused();
        }
    }

    public boolean isCapturePaused() {
        return this.mIsCapturePaused;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(24:31|32|33|(1:35)(1:36)|37|38|(2:40|(1:42)(6:43|44|45|46|47|(3:49|50|51)(1:52)))|57|(2:59|(1:61)(2:62|(1:64)))|65|66|(1:70)|71|72|73|74|(1:76)|77|(1:79)|80|81|82|(1:84)|(6:85|86|87|88|89|90)) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:22|23|24|25|26) */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x013c, code lost:
        r8.mCamera.release();
        r8.mCamera = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0144, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0035 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:71:0x00f4 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:85:0x0122 */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x00ff A[Catch:{ Exception -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x010d A[Catch:{ Exception -> 0x00b4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x011b A[Catch:{ Exception -> 0x0122 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:37:0x0060=Splitter:B:37:0x0060, B:24:0x0035=Splitter:B:24:0x0035} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startCapture() {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mLockCamera
            monitor-enter(r0)
            boolean r1 = r8.mIsCapturePaused     // Catch:{ all -> 0x014e }
            r2 = 1
            if (r1 == 0) goto L_0x0013
            boolean r1 = r8.mIsSurfaceInvalidated     // Catch:{ all -> 0x014e }
            if (r1 == 0) goto L_0x0013
            r8.mIsCapturing = r2     // Catch:{ all -> 0x014e }
            r8.startToDetectCapturePaused()     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r2
        L_0x0013:
            android.hardware.Camera r1 = r8.mCamera     // Catch:{ all -> 0x014e }
            if (r1 == 0) goto L_0x001d
            boolean r1 = r8.mIsCapturing     // Catch:{ all -> 0x014e }
            if (r1 == 0) goto L_0x001d
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r2
        L_0x001d:
            android.hardware.Camera r1 = r8.mCamera     // Catch:{ all -> 0x014e }
            r3 = 0
            if (r1 != 0) goto L_0x0037
            java.lang.String r1 = "camera_is_freezed"
            boolean r1 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r1, r3)     // Catch:{ all -> 0x014e }
            if (r1 == 0) goto L_0x002c
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x002c:
            int r1 = r8.mCameraId     // Catch:{ Exception -> 0x0035 }
            android.hardware.Camera r1 = android.hardware.Camera.open(r1)     // Catch:{ Exception -> 0x0035 }
            r8.mCamera = r1     // Catch:{ Exception -> 0x0035 }
            goto L_0x0037
        L_0x0035:
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x0037:
            android.hardware.Camera r1 = r8.mCamera     // Catch:{ all -> 0x014e }
            if (r1 != 0) goto L_0x003d
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x003d:
            r1 = 0
            android.hardware.Camera r4 = r8.mCamera     // Catch:{ Exception -> 0x0145 }
            android.hardware.Camera$Parameters r4 = r4.getParameters()     // Catch:{ Exception -> 0x0145 }
            r8.mZoomParameters = r4     // Catch:{ Exception -> 0x0145 }
            boolean r5 = r4.isZoomSupported()     // Catch:{ Exception -> 0x0145 }
            if (r5 == 0) goto L_0x005e
            r4.setZoom(r3)     // Catch:{ Exception -> 0x0145 }
            r8.mIsZoomSupported = r2     // Catch:{ Exception -> 0x0145 }
            int r5 = r4.getMaxZoom()     // Catch:{ Exception -> 0x0145 }
            r8.mMaxZoom = r5     // Catch:{ Exception -> 0x0145 }
            int r5 = r4.getZoom()     // Catch:{ Exception -> 0x0145 }
            r8.mCurrentZoom = r5     // Catch:{ Exception -> 0x0145 }
            goto L_0x0060
        L_0x005e:
            r8.mIsZoomSupported = r3     // Catch:{ Exception -> 0x0145 }
        L_0x0060:
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            if (r5 == 0) goto L_0x00bd
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r5 = r5.videoType     // Catch:{ all -> 0x014e }
            if (r5 == 0) goto L_0x0081
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r5 = r5.videoType     // Catch:{ all -> 0x014e }
            int r5 = r8.nydusVideoType2ImageFormat(r5)     // Catch:{ all -> 0x014e }
            r4.setPreviewFormat(r5)     // Catch:{ all -> 0x014e }
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r5 = r5.width     // Catch:{ all -> 0x014e }
            com.zipow.nydus.VideoFormat r6 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r6 = r6.height     // Catch:{ all -> 0x014e }
            r4.setPreviewSize(r5, r6)     // Catch:{ all -> 0x014e }
            goto L_0x00bd
        L_0x0081:
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            r6 = 12
            r5.videoType = r6     // Catch:{ all -> 0x014e }
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ Exception -> 0x00b4 }
            int r5 = r5.videoType     // Catch:{ Exception -> 0x00b4 }
            int r5 = r8.nydusVideoType2ImageFormat(r5)     // Catch:{ Exception -> 0x00b4 }
            r4.setPreviewFormat(r5)     // Catch:{ Exception -> 0x00b4 }
            android.hardware.Camera$Size r5 = r4.getPreviewSize()     // Catch:{ all -> 0x014e }
            if (r5 != 0) goto L_0x00a1
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.release()     // Catch:{ all -> 0x014e }
            r8.mCamera = r1     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x00a1:
            com.zipow.nydus.VideoFormat r6 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r7 = r5.width     // Catch:{ all -> 0x014e }
            r6.width = r7     // Catch:{ all -> 0x014e }
            com.zipow.nydus.VideoFormat r6 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            int r5 = r5.height     // Catch:{ all -> 0x014e }
            r6.height = r5     // Catch:{ all -> 0x014e }
            com.zipow.nydus.VideoFormat r5 = r8.mVideoFormat     // Catch:{ all -> 0x014e }
            r6 = 1106247680(0x41f00000, float:30.0)
            r5.fps = r6     // Catch:{ all -> 0x014e }
            goto L_0x00bd
        L_0x00b4:
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.release()     // Catch:{ all -> 0x014e }
            r8.mCamera = r1     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x00bd:
            java.util.List r5 = r4.getSupportedFocusModes()     // Catch:{ all -> 0x014e }
            if (r5 == 0) goto L_0x00e1
            r4.getFocusMode()     // Catch:{ all -> 0x014e }
            java.lang.String r6 = "continuous-picture"
            int r6 = r5.indexOf(r6)     // Catch:{ all -> 0x014e }
            if (r6 < 0) goto L_0x00d4
            java.lang.String r5 = "continuous-picture"
            r4.setFocusMode(r5)     // Catch:{ all -> 0x014e }
            goto L_0x00e1
        L_0x00d4:
            java.lang.String r6 = "continuous-video"
            int r5 = r5.indexOf(r6)     // Catch:{ all -> 0x014e }
            if (r5 < 0) goto L_0x00e1
            java.lang.String r5 = "continuous-video"
            r4.setFocusMode(r5)     // Catch:{ all -> 0x014e }
        L_0x00e1:
            java.lang.String r5 = r8.getPreferredCameraAntibanding()     // Catch:{ Exception -> 0x00f4 }
            java.util.List r6 = r4.getSupportedAntibanding()     // Catch:{ Exception -> 0x00f4 }
            if (r6 == 0) goto L_0x00f4
            boolean r6 = r6.contains(r5)     // Catch:{ Exception -> 0x00f4 }
            if (r6 == 0) goto L_0x00f4
            r4.setAntibanding(r5)     // Catch:{ Exception -> 0x00f4 }
        L_0x00f4:
            android.hardware.Camera r5 = r8.mCamera     // Catch:{ Exception -> 0x013c }
            r5.setParameters(r4)     // Catch:{ Exception -> 0x013c }
            byte[] r5 = r8.newCallbackBuffer(r4)     // Catch:{ all -> 0x014e }
            if (r5 == 0) goto L_0x0107
            android.hardware.Camera r6 = r8.mCamera     // Catch:{ all -> 0x014e }
            r6.addCallbackBuffer(r5)     // Catch:{ all -> 0x014e }
            int r5 = r5.length     // Catch:{ all -> 0x014e }
            r8.mCurBuffSize = r5     // Catch:{ all -> 0x014e }
        L_0x0107:
            byte[] r4 = r8.newCallbackBuffer(r4)     // Catch:{ all -> 0x014e }
            if (r4 == 0) goto L_0x0112
            android.hardware.Camera r5 = r8.mCamera     // Catch:{ all -> 0x014e }
            r5.addCallbackBuffer(r4)     // Catch:{ all -> 0x014e }
        L_0x0112:
            android.hardware.Camera r4 = r8.mCamera     // Catch:{ all -> 0x014e }
            r4.setPreviewCallbackWithBuffer(r8)     // Catch:{ all -> 0x014e }
            android.view.SurfaceHolder r4 = r8.mSurfaceHolder     // Catch:{ Exception -> 0x0122 }
            if (r4 == 0) goto L_0x0122
            android.hardware.Camera r4 = r8.mCamera     // Catch:{ Exception -> 0x0122 }
            android.view.SurfaceHolder r5 = r8.mSurfaceHolder     // Catch:{ Exception -> 0x0122 }
            r4.setPreviewDisplay(r5)     // Catch:{ Exception -> 0x0122 }
        L_0x0122:
            android.hardware.Camera r4 = r8.mCamera     // Catch:{ Exception -> 0x012e }
            r4.startPreview()     // Catch:{ Exception -> 0x012e }
            r8.mIsCapturing = r2     // Catch:{ all -> 0x014e }
            r8.startToDetectCapturePaused()     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r2
        L_0x012e:
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.setPreviewCallback(r1)     // Catch:{ all -> 0x014e }
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.release()     // Catch:{ all -> 0x014e }
            r8.mCamera = r1     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x013c:
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.release()     // Catch:{ all -> 0x014e }
            r8.mCamera = r1     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x0145:
            android.hardware.Camera r2 = r8.mCamera     // Catch:{ all -> 0x014e }
            r2.release()     // Catch:{ all -> 0x014e }
            r8.mCamera = r1     // Catch:{ all -> 0x014e }
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            return r3
        L_0x014e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x014e }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.nydus.VideoCapturer.startCapture():boolean");
    }

    @Nullable
    private String getPreferredCameraAntibanding() {
        return PreferenceUtil.readStringValue(PreferenceUtil.CAMERA_ANTIBANDING, null);
    }

    public boolean isCapturing() {
        return this.mIsCapturing;
    }

    private void startToDetectCapturePaused() {
        this.mLastDataTimeStamp = 0;
        if (this.mDectectCapturePausedRunnable == null) {
            this.mDectectCapturePausedRunnable = new Runnable() {
                public void run() {
                    if (VideoCapturer.this.mIsCapturing) {
                        long uptimeMillis = SystemClock.uptimeMillis();
                        if (!VideoCapturer.this.mIsCapturePaused && VideoCapturer.this.mLastDataTimeStamp != 0 && uptimeMillis - VideoCapturer.this.mLastDataTimeStamp > 1000 && VideoCapturer.this.mSurfaceHolder != null) {
                            if (VideoCapturer.this.mIsSurfaceInvalidated) {
                                VideoCapturer.this.onCapturePaused();
                            } else {
                                VideoCapturer.this.mLastDataTimeStamp;
                            }
                        }
                        if (!(!VideoCapturer.this.mIsCapturePaused || VideoCapturer.this.mLastDataFormat == null || VideoCapturer.this.mLastFrameData == null)) {
                            VideoCapturer videoCapturer = VideoCapturer.this;
                            videoCapturer.onFrameCaptured(videoCapturer.mNativeHandle, VideoCapturer.this.mLastFrameData, VideoCapturer.this.mLastDataFormat);
                        }
                        if (VideoCapturer.this.mDectectCapturePausedRunnable != null) {
                            VideoCapturer.this.mHandler.postDelayed(VideoCapturer.this.mDectectCapturePausedRunnable, 500);
                        }
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mDectectCapturePausedRunnable);
        this.mHandler.postDelayed(this.mDectectCapturePausedRunnable, 500);
    }

    private void stopToDetectCapturePaused() {
        Runnable runnable = this.mDectectCapturePausedRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mDectectCapturePausedRunnable = null;
        }
    }

    /* access modifiers changed from: private */
    public void onCapturePaused() {
        synchronized (this.mLockCamera) {
            stopCapture(true);
            this.mIsCapturePaused = true;
            this.mIsCapturing = true;
            if (this.mListener != null) {
                this.mListener.onCapturePaused();
            }
        }
    }

    private void onCaptureResumed() {
        this.mIsCapturePaused = false;
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onCaptureResumed();
        }
    }

    @NonNull
    public VideoSize getVideoSize() {
        VideoSize videoSize = new VideoSize();
        VideoFormat videoFormat = this.mVideoFormat;
        if (videoFormat == null || videoFormat.width == 0 || this.mVideoFormat.height == 0) {
            VideoFormat videoFormat2 = new VideoFormat();
            int i = -1;
            if (NydusUtil.getNumberOfCameras() > 0) {
                i = NydusUtil.getFrontCameraId();
                if (i < 0) {
                    i = 0;
                }
            }
            if (i < 0 || !selectDefaultVideoFormat(videoFormat2, i)) {
                videoSize.width = 640;
                videoSize.height = CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
            } else {
                videoSize.width = videoFormat2.width;
                videoSize.height = videoFormat2.height;
            }
        } else {
            videoSize.width = this.mVideoFormat.width;
            videoSize.height = this.mVideoFormat.height;
        }
        if (videoSize.width > 0 && videoSize.height > 0) {
            if (((float) (videoSize.width / videoSize.height)) > this.mSendRatio) {
                videoSize.width = Math.round(((float) videoSize.height) * this.mSendRatio);
            } else {
                videoSize.height = Math.round(((float) videoSize.width) / this.mSendRatio);
            }
        }
        return videoSize;
    }

    public boolean stopCapture() {
        return stopCapture(false);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:9|10|11|12|(1:14)|15|16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        return true;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x002e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean stopCapture(boolean r6) {
        /*
            r5 = this;
            long r0 = java.lang.System.currentTimeMillis()
            sLastStopCameraTime = r0
            r0 = 0
            r5.mIsZoomSupported = r0
            java.lang.Object r1 = r5.mLockCamera
            monitor-enter(r1)
            boolean r2 = r5.mIsCapturing     // Catch:{ all -> 0x003c }
            r3 = 1
            if (r2 != 0) goto L_0x0013
            monitor-exit(r1)     // Catch:{ all -> 0x003c }
            return r3
        L_0x0013:
            android.hardware.Camera r2 = r5.mCamera     // Catch:{ all -> 0x003c }
            if (r2 == 0) goto L_0x0033
            java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x003c }
            r2 = 0
            boolean r4 = r5.mIsCapturing     // Catch:{ Exception -> 0x002e }
            if (r4 == 0) goto L_0x0029
            android.hardware.Camera r4 = r5.mCamera     // Catch:{ Exception -> 0x002e }
            r4.setPreviewCallback(r2)     // Catch:{ Exception -> 0x002e }
            android.hardware.Camera r4 = r5.mCamera     // Catch:{ Exception -> 0x002e }
            r4.stopPreview()     // Catch:{ Exception -> 0x002e }
        L_0x0029:
            android.hardware.Camera r4 = r5.mCamera     // Catch:{ Exception -> 0x002e }
            r4.release()     // Catch:{ Exception -> 0x002e }
        L_0x002e:
            r5.mCamera = r2     // Catch:{ all -> 0x003c }
            java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x003c }
        L_0x0033:
            r5.mIsCapturing = r0     // Catch:{ all -> 0x003c }
            if (r6 != 0) goto L_0x003a
            r5.stopToDetectCapturePaused()     // Catch:{ all -> 0x003c }
        L_0x003a:
            monitor-exit(r1)     // Catch:{ all -> 0x003c }
            return r3
        L_0x003c:
            r6 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x003c }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.nydus.VideoCapturer.stopCapture(boolean):boolean");
    }

    private byte[] newCallbackBuffer(Parameters parameters) {
        Size previewSize = parameters.getPreviewSize();
        if (previewSize == null) {
            return null;
        }
        try {
            return new byte[(((previewSize.width * previewSize.height) * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())) / 8)];
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x003f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPreviewFrame(@androidx.annotation.Nullable byte[] r7, @androidx.annotation.NonNull android.hardware.Camera r8) {
        /*
            r6 = this;
            java.lang.Object r0 = r6.mLockCamera
            monitor-enter(r0)
            boolean r1 = r6.mIsCapturing     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x003e
            android.hardware.Camera r1 = r6.mCamera     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x003e
            if (r7 == 0) goto L_0x003e
            int r1 = r7.length     // Catch:{ all -> 0x0040 }
            int r2 = r6.mCurBuffSize     // Catch:{ all -> 0x0040 }
            if (r1 == r2) goto L_0x0013
            goto L_0x003e
        L_0x0013:
            boolean r1 = r6.isCapturePaused()     // Catch:{ all -> 0x0040 }
            if (r1 == 0) goto L_0x001c
            r6.onCaptureResumed()     // Catch:{ all -> 0x0040 }
        L_0x001c:
            r6.mLastFrameData = r7     // Catch:{ all -> 0x0040 }
            long r1 = android.os.SystemClock.uptimeMillis()     // Catch:{ all -> 0x0040 }
            r6.mLastDataTimeStamp = r1     // Catch:{ all -> 0x0040 }
            com.zipow.nydus.VideoFormat r1 = r6.getOutputVideoFormat()     // Catch:{ Exception -> 0x003c }
            r6.mLastDataFormat = r1     // Catch:{ Exception -> 0x003c }
            long r2 = r6.mNativeHandle     // Catch:{ Exception -> 0x003c }
            r4 = 0
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 == 0) goto L_0x003a
            long r2 = r6.mNativeHandle     // Catch:{ Exception -> 0x003c }
            r6.onFrameCaptured(r2, r7, r1)     // Catch:{ Exception -> 0x003c }
            r8.addCallbackBuffer(r7)     // Catch:{ Exception -> 0x003c }
        L_0x003a:
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return
        L_0x003c:
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return
        L_0x003e:
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return
        L_0x0040:
            r7 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.nydus.VideoCapturer.onPreviewFrame(byte[], android.hardware.Camera):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0074, code lost:
        if (r11.size() == 0) goto L_0x0076;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.zipow.nydus.VideoCapCapability[] getCameraCapability(int r20) {
        /*
            r19 = this;
            r1 = r19
            r0 = r20
            java.lang.Class<com.zipow.nydus.VideoCapCapability> r2 = com.zipow.nydus.VideoCapCapability.class
            monitor-enter(r2)
            java.util.HashMap<java.lang.Integer, com.zipow.nydus.VideoCapCapability[]> r3 = r1.mCapsMap     // Catch:{ all -> 0x013a }
            if (r3 == 0) goto L_0x0025
            java.util.HashMap<java.lang.Integer, com.zipow.nydus.VideoCapCapability[]> r3 = r1.mCapsMap     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r3 = r1.getCapsArrayFromCapsMap(r3, r0)     // Catch:{ all -> 0x013a }
            if (r3 == 0) goto L_0x0015
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r3
        L_0x0015:
            java.util.Set<java.lang.Integer> r3 = r1.mFailedCameras     // Catch:{ all -> 0x013a }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x013a }
            boolean r3 = r3.contains(r4)     // Catch:{ all -> 0x013a }
            if (r3 == 0) goto L_0x0025
            com.zipow.nydus.VideoCapCapability[] r0 = FALLBACK_CAPS     // Catch:{ all -> 0x013a }
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x0025:
            java.util.HashMap<java.lang.Integer, com.zipow.nydus.VideoCapCapability[]> r3 = r1.mCapsMap     // Catch:{ all -> 0x013a }
            if (r3 != 0) goto L_0x0030
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ all -> 0x013a }
            r3.<init>()     // Catch:{ all -> 0x013a }
            r1.mCapsMap = r3     // Catch:{ all -> 0x013a }
        L_0x0030:
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ all -> 0x013a }
            r3.<init>()     // Catch:{ all -> 0x013a }
            android.hardware.Camera r4 = android.hardware.Camera.open(r20)     // Catch:{ Exception -> 0x012d }
            if (r4 != 0) goto L_0x0048
            java.util.Set<java.lang.Integer> r3 = r1.mFailedCameras     // Catch:{ all -> 0x013a }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x013a }
            r3.add(r0)     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r0 = FALLBACK_CAPS     // Catch:{ all -> 0x013a }
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x0048:
            android.hardware.Camera$Parameters r5 = r4.getParameters()     // Catch:{ Exception -> 0x0120 }
            java.util.List r6 = r5.getSupportedPreviewFormats()     // Catch:{ Exception -> 0x0120 }
            if (r6 == 0) goto L_0x00f4
            r7 = 0
            r8 = 0
        L_0x0054:
            int r9 = r6.size()     // Catch:{ all -> 0x013a }
            if (r8 >= r9) goto L_0x00f4
            java.lang.Object r9 = r6.get(r8)     // Catch:{ all -> 0x013a }
            java.lang.Integer r9 = (java.lang.Integer) r9     // Catch:{ all -> 0x013a }
            int r9 = r9.intValue()     // Catch:{ all -> 0x013a }
            int r9 = r1.imageFormat2NydusVideoType(r9)     // Catch:{ all -> 0x013a }
            java.util.List r11 = r5.getSupportedPreviewFpsRange()     // Catch:{ Exception -> 0x006d }
            goto L_0x006e
        L_0x006d:
            r11 = 0
        L_0x006e:
            if (r11 == 0) goto L_0x0076
            int r12 = r11.size()     // Catch:{ all -> 0x013a }
            if (r12 != 0) goto L_0x0084
        L_0x0076:
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ all -> 0x013a }
            r11.<init>()     // Catch:{ all -> 0x013a }
            r12 = 2
            int[] r12 = new int[r12]     // Catch:{ all -> 0x013a }
            r12 = {1000, 30000} // fill-array     // Catch:{ all -> 0x013a }
            r11.add(r12)     // Catch:{ all -> 0x013a }
        L_0x0084:
            r12 = 0
        L_0x0085:
            int r13 = r11.size()     // Catch:{ all -> 0x013a }
            if (r12 >= r13) goto L_0x00eb
            java.lang.Object r13 = r11.get(r12)     // Catch:{ all -> 0x013a }
            int[] r13 = (int[]) r13     // Catch:{ all -> 0x013a }
            r14 = r13[r7]     // Catch:{ all -> 0x013a }
            r15 = 1
            r13 = r13[r15]     // Catch:{ all -> 0x013a }
            java.util.List r15 = r5.getSupportedPreviewSizes()     // Catch:{ Exception -> 0x009b }
            goto L_0x009c
        L_0x009b:
            r15 = 0
        L_0x009c:
            if (r15 != 0) goto L_0x00a3
            r16 = r5
            r17 = r6
            goto L_0x00e3
        L_0x00a3:
            int r10 = r15.size()     // Catch:{ all -> 0x013a }
            if (r7 >= r10) goto L_0x00df
            java.lang.Object r10 = r15.get(r7)     // Catch:{ all -> 0x013a }
            android.hardware.Camera$Size r10 = (android.hardware.Camera.Size) r10     // Catch:{ all -> 0x013a }
            if (r10 != 0) goto L_0x00b6
            r16 = r5
            r17 = r6
            goto L_0x00d8
        L_0x00b6:
            r16 = r5
            com.zipow.nydus.VideoCapCapability r5 = new com.zipow.nydus.VideoCapCapability     // Catch:{ all -> 0x013a }
            r5.<init>()     // Catch:{ all -> 0x013a }
            r5.videoType = r9     // Catch:{ all -> 0x013a }
            r17 = r6
            float r6 = (float) r14     // Catch:{ all -> 0x013a }
            r18 = 1148846080(0x447a0000, float:1000.0)
            float r6 = r6 / r18
            r5.minFps = r6     // Catch:{ all -> 0x013a }
            float r6 = (float) r13     // Catch:{ all -> 0x013a }
            float r6 = r6 / r18
            r5.maxFps = r6     // Catch:{ all -> 0x013a }
            int r6 = r10.width     // Catch:{ all -> 0x013a }
            r5.width = r6     // Catch:{ all -> 0x013a }
            int r6 = r10.height     // Catch:{ all -> 0x013a }
            r5.height = r6     // Catch:{ all -> 0x013a }
            r3.add(r5)     // Catch:{ all -> 0x013a }
        L_0x00d8:
            int r7 = r7 + 1
            r5 = r16
            r6 = r17
            goto L_0x00a3
        L_0x00df:
            r16 = r5
            r17 = r6
        L_0x00e3:
            int r12 = r12 + 1
            r5 = r16
            r6 = r17
            r7 = 0
            goto L_0x0085
        L_0x00eb:
            r16 = r5
            r17 = r6
            int r8 = r8 + 1
            r7 = 0
            goto L_0x0054
        L_0x00f4:
            r4.release()     // Catch:{ all -> 0x013a }
            int r4 = r3.size()     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r4 = new com.zipow.nydus.VideoCapCapability[r4]     // Catch:{ all -> 0x013a }
            int r5 = r3.size()     // Catch:{ all -> 0x013a }
            if (r5 <= 0) goto L_0x0112
            java.lang.Object[] r3 = r3.toArray(r4)     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r3 = (com.zipow.nydus.VideoCapCapability[]) r3     // Catch:{ all -> 0x013a }
            java.util.HashMap<java.lang.Integer, com.zipow.nydus.VideoCapCapability[]> r4 = r1.mCapsMap     // Catch:{ all -> 0x013a }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x013a }
            r4.put(r5, r3)     // Catch:{ all -> 0x013a }
        L_0x0112:
            java.util.HashMap<java.lang.Integer, com.zipow.nydus.VideoCapCapability[]> r3 = r1.mCapsMap     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r0 = r1.getCapsArrayFromCapsMap(r3, r0)     // Catch:{ all -> 0x013a }
            if (r0 != 0) goto L_0x011e
            com.zipow.nydus.VideoCapCapability[] r0 = FALLBACK_CAPS     // Catch:{ all -> 0x013a }
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x011e:
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x0120:
            java.util.Set<java.lang.Integer> r3 = r1.mFailedCameras     // Catch:{ all -> 0x013a }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x013a }
            r3.add(r0)     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r0 = FALLBACK_CAPS     // Catch:{ all -> 0x013a }
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x012d:
            java.util.Set<java.lang.Integer> r3 = r1.mFailedCameras     // Catch:{ all -> 0x013a }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x013a }
            r3.add(r0)     // Catch:{ all -> 0x013a }
            com.zipow.nydus.VideoCapCapability[] r0 = FALLBACK_CAPS     // Catch:{ all -> 0x013a }
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            return r0
        L_0x013a:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x013a }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.nydus.VideoCapturer.getCameraCapability(int):com.zipow.nydus.VideoCapCapability[]");
    }

    private VideoCapCapability[] getCapsArrayFromCapsMap(HashMap<Integer, VideoCapCapability[]> hashMap, int i) {
        VideoCapCapability[] videoCapCapabilityArr;
        try {
            videoCapCapabilityArr = (VideoCapCapability[]) this.mCapsMap.get(Integer.valueOf(i));
        } catch (Exception unused) {
            videoCapCapabilityArr = null;
        }
        if (videoCapCapabilityArr == null) {
            return null;
        }
        if (!"LENOVO".equals(Build.MANUFACTURER) || !"Lenovo K900".equals(Build.MODEL)) {
            return videoCapCapabilityArr;
        }
        ArrayList arrayList = new ArrayList();
        for (VideoCapCapability videoCapCapability : videoCapCapabilityArr) {
            if (videoCapCapability.width == 800 || videoCapCapability.height != 480) {
                arrayList.add(videoCapCapability);
            }
        }
        VideoCapCapability[] videoCapCapabilityArr2 = new VideoCapCapability[arrayList.size()];
        arrayList.toArray(videoCapCapabilityArr2);
        return videoCapCapabilityArr2;
    }

    /* access modifiers changed from: private */
    public boolean loadCapabilitiesFromConfig() {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        SafeObjectInputStream safeObjectInputStream;
        Throwable th2;
        Throwable th3;
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.CAMERA_CAPABILITIES, null);
        if (readStringValue == null || readStringValue.length() == 0) {
            return false;
        }
        try {
            try {
                byteArrayInputStream = new ByteArrayInputStream(Base64.decode(readStringValue, 8));
                try {
                    safeObjectInputStream = new SafeObjectInputStream(byteArrayInputStream);
                    try {
                        this.mCapsMap = (HashMap) safeObjectInputStream.readObject();
                        safeObjectInputStream.close();
                        byteArrayInputStream.close();
                        return true;
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        th2 = r4;
                        th3 = th5;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    throw th;
                }
            } catch (Exception unused) {
                return false;
            } catch (Throwable th7) {
                th.addSuppressed(th7);
            }
        } catch (Exception unused2) {
            return false;
        }
        throw th3;
        if (th2 != null) {
            safeObjectInputStream.close();
        } else {
            safeObjectInputStream.close();
        }
        throw th3;
        throw th;
    }

    /* access modifiers changed from: private */
    public void saveCapabilitiesToConfig() {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        Throwable th2;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            try {
                objectOutputStream.writeObject(this.mCapsMap);
                PreferenceUtil.saveStringValue(PreferenceUtil.CAMERA_CAPABILITIES, Base64.encodeToString(byteArrayOutputStream.toByteArray(), 8));
                objectOutputStream.close();
                byteArrayOutputStream.close();
                return;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                th = r3;
                th2 = th4;
            }
            throw th;
            throw th2;
            if (th != null) {
                try {
                    objectOutputStream.close();
                } catch (Throwable th5) {
                    th.addSuppressed(th5);
                }
            } else {
                objectOutputStream.close();
            }
            throw th2;
        } catch (IOException unused) {
        } catch (Throwable th6) {
            r1.addSuppressed(th6);
        }
    }

    private int getMaxCaptureHeight() {
        int i = this.mMaxCaptureHeight;
        if (i > 0) {
            return i;
        }
        this.mMaxCaptureHeight = CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
        return this.mMaxCaptureHeight;
    }
}
