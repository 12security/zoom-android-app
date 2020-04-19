package com.zipow.videobox.share;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.display.VirtualDisplay;
import android.hardware.display.VirtualDisplay.Callback;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.annotate.ShareScreenAnnoToolbar;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.DesktopModeReceiver;
import com.zipow.videobox.util.DesktopModeReceiver.DesktopModeListener;
import com.zipow.videobox.util.IShareCustomScreenHandler;
import java.nio.ByteBuffer;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

@TargetApi(21)
public class ScreenShareMgr implements com.zipow.annotate.ShareScreenAnnoToolbar.Listener, DesktopModeListener {
    private static final String TAG = "ScreenShareMgr";
    @Nullable
    private static ScreenShareMgr instance;
    private final int MIN_CAPTURE_RESOTION = 540;
    @Nullable
    private DesktopModeReceiver mDesktopReceiver;
    private int mDisplayHeight;
    private int mDisplayWidth;
    /* access modifiers changed from: private */
    @Nullable
    public Handler mHandler;
    private ImageAvailableListener mImageAvailableListener;
    /* access modifiers changed from: private */
    @Nullable
    public ImageReader mImageReader;
    /* access modifiers changed from: private */
    @Nullable
    public ImageReader mImageReaderRotated;
    Intent mIntent;
    boolean mIsCustomShare;
    boolean mIsShared = false;
    boolean mIsSharing;
    @Nullable
    Listener mListener;
    private int mLogTimes = 0;
    @Nullable
    private MediaProjection mMediaProjection;
    @Nullable
    private MediaProjectionManager mProjectionManager;
    boolean mReloadingVirtualDisplay = false;
    @Nullable
    private BroadcastReceiver mScreenBroadcastReceiver;
    private int mScreenDensity;
    @Nullable
    private ShareScreenAnnoToolbar mShareToolbar;
    @Nullable
    private VirtualDisplay mVirtualDisplay;
    private VirtualDisplayCallback mVirtualDisplayCallback;
    @Nullable
    private WakeLock mWakeLock;

    private class ImageAvailableListener implements OnImageAvailableListener {
        private ImageAvailableListener() {
        }

        public void onImageAvailable(@NonNull ImageReader imageReader) {
            Image acquireLatestImage;
            try {
                acquireLatestImage = imageReader.acquireLatestImage();
                if (acquireLatestImage == null) {
                    if (acquireLatestImage != null) {
                        acquireLatestImage.close();
                    }
                    return;
                } else if (ScreenShareMgr.this.screenRotated(acquireLatestImage.getWidth(), acquireLatestImage.getHeight())) {
                    ScreenShareMgr.this.reloadVirtualDisplay();
                    if (acquireLatestImage != null) {
                        acquireLatestImage.close();
                    }
                    return;
                } else {
                    Plane[] planes = acquireLatestImage.getPlanes();
                    if (planes[0].getBuffer() == null) {
                        if (acquireLatestImage != null) {
                            acquireLatestImage.close();
                        }
                        return;
                    }
                    ByteBuffer byteBuffer = (ByteBuffer) planes[0].getBuffer().rewind();
                    ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                    if (shareObj != null) {
                        shareObj.setCaptureFrame(acquireLatestImage.getWidth(), acquireLatestImage.getHeight(), planes[0].getRowStride(), byteBuffer);
                    }
                    if (acquireLatestImage != null) {
                        acquireLatestImage.close();
                    }
                    return;
                }
            } catch (Exception unused) {
            } catch (Throwable th) {
                r0.addSuppressed(th);
            }
            throw th;
        }
    }

    public interface Listener {
        void onAnnoStatusChanged();

        void onClickStopScreenShare();
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private ScreenBroadcastReceiver() {
        }

        public void onReceive(Context context, @NonNull Intent intent) {
            if (StringUtil.isSameString(intent.getAction(), "android.intent.action.SCREEN_OFF")) {
                ScreenShareMgr.this.onClickStopShare();
            }
        }
    }

    private class VirtualDisplayCallback extends Callback {
        public void onPaused() {
        }

        public void onResumed() {
        }

        private VirtualDisplayCallback() {
        }

        public void onStopped() {
            if (ScreenShareMgr.this.mReloadingVirtualDisplay) {
                ScreenShareMgr screenShareMgr = ScreenShareMgr.this;
                screenShareMgr.mReloadingVirtualDisplay = false;
                screenShareMgr.createVirtualDisplay();
            }
        }
    }

    class WorkThread extends Thread {
        WorkThread() {
        }

        public void run() {
            Looper.prepare();
            ScreenShareMgr.this.mHandler = new Handler();
            ScreenShareMgr.this.createVirtualDisplay();
            if (!ScreenShareMgr.this.mIsCustomShare) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                intent.addFlags(268435456);
                try {
                    VideoBoxApplication.getInstance().startActivity(intent);
                } catch (Exception unused) {
                }
            } else {
                String string = ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), C4558R.string.zm_config_share_custom_screen_handler);
                if (!StringUtil.isEmptyOrNull(string)) {
                    try {
                        ((IShareCustomScreenHandler) Class.forName(string).newInstance()).onStartedShareCustomScreen(VideoBoxApplication.getInstance());
                    } catch (Exception unused2) {
                    }
                }
            }
            Looper.loop();
            if (ScreenShareMgr.this.mImageReader != null) {
                ScreenShareMgr.this.mImageReader.close();
                ScreenShareMgr.this.mImageReader = null;
            }
            if (ScreenShareMgr.this.mImageReaderRotated != null) {
                ScreenShareMgr.this.mImageReaderRotated.close();
                ScreenShareMgr.this.mImageReaderRotated = null;
            }
        }
    }

    @NonNull
    public static synchronized ScreenShareMgr getInstance() {
        ScreenShareMgr screenShareMgr;
        synchronized (ScreenShareMgr.class) {
            if (instance == null) {
                instance = new ScreenShareMgr();
            }
            screenShareMgr = instance;
        }
        return screenShareMgr;
    }

    private ScreenShareMgr() {
    }

    public void initialize(Listener listener) {
        this.mListener = listener;
    }

    public void unInitialize() {
        this.mListener = null;
    }

    @SuppressLint({"InlinedApi"})
    public void prepare(Intent intent) {
        this.mIsSharing = true;
        this.mIntent = intent;
        this.mShareToolbar = new ShareScreenAnnoToolbar(this);
        this.mImageAvailableListener = new ImageAvailableListener();
        this.mVirtualDisplayCallback = new VirtualDisplayCallback();
        this.mProjectionManager = (MediaProjectionManager) VideoBoxApplication.getInstance().getSystemService("media_projection");
    }

    public boolean isSharing() {
        return this.mIsSharing;
    }

    public void setIsCustomShare(boolean z) {
        this.mIsCustomShare = z;
    }

    private void adjustDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) VideoBoxApplication.getInstance().getSystemService("window");
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
            this.mScreenDensity = displayMetrics.densityDpi;
            if (Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) / 2 < 540 || displayMetrics.density < 2.0f) {
                AnnoDataMgr.getInstance().setIsHDPI(false);
                this.mDisplayWidth = displayMetrics.widthPixels;
                this.mDisplayHeight = displayMetrics.heightPixels;
            } else {
                AnnoDataMgr.getInstance().setIsHDPI(true);
                this.mDisplayWidth = displayMetrics.widthPixels / 2;
                this.mDisplayHeight = displayMetrics.heightPixels / 2;
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean screenRotated(int i, int i2) {
        adjustDisplayMetrics();
        return (i == this.mDisplayWidth && i2 == this.mDisplayHeight) ? false : true;
    }

    public void startShare() {
        MediaProjectionManager mediaProjectionManager = this.mProjectionManager;
        if (mediaProjectionManager != null && this.mMediaProjection == null && this.mIsSharing) {
            this.mMediaProjection = mediaProjectionManager.getMediaProjection(-1, this.mIntent);
            if (this.mMediaProjection != null) {
                this.mIsShared = true;
                if (this.mDesktopReceiver == null) {
                    this.mDesktopReceiver = new DesktopModeReceiver();
                }
                this.mDesktopReceiver.setListener(this);
                this.mDesktopReceiver.registerReceiver(VideoBoxApplication.getInstance());
                new WorkThread().start();
                updateScreenOff();
            }
        }
    }

    public void onOrientationChanged() {
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.updateLayoutparameter();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (getInstance().isSharing()) {
            ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
            if (shareScreenAnnoToolbar != null) {
                shareScreenAnnoToolbar.onConfigurationChanged(configuration);
            }
        }
    }

    private void createImageReader() {
        adjustDisplayMetrics();
        ImageReader imageReader = this.mImageReader;
        if (imageReader == null) {
            this.mImageReader = ImageReader.newInstance(this.mDisplayWidth, this.mDisplayHeight, 1, 1);
            this.mImageReader.setOnImageAvailableListener(this.mImageAvailableListener, this.mHandler);
            return;
        }
        int width = imageReader.getWidth();
        int i = this.mDisplayWidth;
        if (width != i && this.mImageReaderRotated == null) {
            this.mImageReaderRotated = ImageReader.newInstance(i, this.mDisplayHeight, 1, 1);
            this.mImageReaderRotated.setOnImageAvailableListener(this.mImageAvailableListener, this.mHandler);
        }
    }

    /* access modifiers changed from: private */
    @SuppressLint({"InlinedApi"})
    public void createVirtualDisplay() {
        if (this.mMediaProjection != null) {
            createImageReader();
            try {
                if (this.mImageReader.getWidth() == this.mDisplayWidth) {
                    this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("ScreenSharing", this.mDisplayWidth, this.mDisplayHeight, this.mScreenDensity, 8, this.mImageReader.getSurface(), this.mVirtualDisplayCallback, this.mHandler);
                } else {
                    this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("ScreenSharing", this.mDisplayWidth, this.mDisplayHeight, this.mScreenDensity, 8, this.mImageReaderRotated.getSurface(), this.mVirtualDisplayCallback, this.mHandler);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void reloadVirtualDisplay() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            this.mReloadingVirtualDisplay = true;
            virtualDisplay.release();
            this.mVirtualDisplay = null;
        }
    }

    public void stopShare() {
        this.mIsSharing = false;
        this.mLogTimes = 0;
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            virtualDisplay.release();
            this.mVirtualDisplay = null;
        }
        MediaProjection mediaProjection = this.mMediaProjection;
        if (mediaProjection != null) {
            mediaProjection.stop();
            this.mMediaProjection = null;
        }
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.destroy();
            this.mShareToolbar = null;
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.getLooper().quitSafely();
            this.mHandler = null;
        }
        try {
            if (this.mWakeLock != null) {
                this.mWakeLock.release();
                this.mWakeLock = null;
            }
        } catch (Exception unused) {
        }
        if (this.mScreenBroadcastReceiver != null) {
            VideoBoxApplication.getInstance().unregisterReceiver(this.mScreenBroadcastReceiver);
            this.mScreenBroadcastReceiver = null;
        }
        DesktopModeReceiver desktopModeReceiver = this.mDesktopReceiver;
        if (desktopModeReceiver != null) {
            desktopModeReceiver.unregisterReceiver(VideoBoxApplication.getInstance());
            this.mDesktopReceiver = null;
        }
        this.mProjectionManager = null;
    }

    public void onClickStopShare() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onClickStopScreenShare();
            return;
        }
        stopShareSession();
        if (isSharing()) {
            stopShare();
        }
        Intent intent = new Intent(VideoBoxApplication.getGlobalContext(), IntegrationActivity.class);
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
        intent.setFlags(268435456);
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            ActivityStartHelper.startActivityForeground(globalContext, intent);
        }
    }

    public void onAnnoStatusChanged() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onAnnoStatusChanged();
        }
    }

    public boolean stopShareSession() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.stopShare();
    }

    public void setAnnoToolbarVisible(boolean z) {
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.setAnnoToolbarVisible(z);
        }
    }

    public void onDesktopModeChange(boolean z) {
        boolean z2;
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            z2 = shareScreenAnnoToolbar.isAnnotationStart();
            this.mShareToolbar.destroy();
            this.mShareToolbar = null;
        } else {
            z2 = false;
        }
        this.mShareToolbar = new ShareScreenAnnoToolbar(this);
        if (this.mIsShared) {
            this.mShareToolbar.showToolbar();
            if (z2) {
                this.mShareToolbar.setAnnoToolbarVisible(true);
            } else {
                this.mShareToolbar.setAnnoToolbarVisible(false);
            }
        }
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.onAnnotateStartedUp(z, j);
        }
    }

    public void onAnnotateShutDown() {
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.onAnnotateShutDown();
        }
    }

    @SuppressLint({"InvalidWakeLockTag"})
    private void updateScreenOff() {
        ShareScreenAnnoToolbar shareScreenAnnoToolbar = this.mShareToolbar;
        if (shareScreenAnnoToolbar != null) {
            shareScreenAnnoToolbar.showToolbar();
        }
        try {
            if (this.mWakeLock == null) {
                PowerManager powerManager = (PowerManager) VideoBoxApplication.getNonNullInstance().getSystemService("power");
                if (powerManager != null) {
                    this.mWakeLock = powerManager.newWakeLock(536870922, "ZoomScreenShare");
                    this.mWakeLock.acquire();
                }
            }
        } catch (Exception unused) {
        }
        if (this.mScreenBroadcastReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mScreenBroadcastReceiver = new ScreenBroadcastReceiver();
            VideoBoxApplication.getNonNullInstance().registerReceiver(this.mScreenBroadcastReceiver, intentFilter);
        }
    }
}
