package com.zipow.videobox.confapp;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.NydusUtil;
import com.zipow.nydus.VideoCapturer;
import com.zipow.nydus.VideoCapturer.Listener;
import com.zipow.nydus.VideoSize;
import com.zipow.videobox.VideoBoxApplication;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class VideoSessionMgr {
    private static final int CAMERA_FIRST = 0;
    private static final int CAMERA_SECOND = 1;
    private static final String TAG = "VideoSessionMgr";
    private boolean mIsPreviewing = false;
    private long mNativeHandle = 0;
    /* access modifiers changed from: private */
    public int mRotation = 0;
    private int mRotationBeforeVideoPaused = 0;
    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private boolean mbHasSetDefaultDevice = false;

    private native long addPicImpl(long j, long j2, int i, int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private native void clearRendererImpl(long j, long j2);

    private native long createRendererInfo(long j, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private native boolean destroyRenderer(long j, long j2);

    private native boolean destroyRendererInfo(long j, long j2);

    private native boolean drawFrameImpl(long j, long j2);

    private native boolean enable180pImpl(long j, boolean z);

    private native long getActiveDeckUserIDImpl(long j, boolean z);

    private native long getActiveUserIDImpl(long j);

    @Nullable
    private native List<MediaDevice> getCamListImpl(long j);

    @Nullable
    private native String getDefaultDeviceImpl(long j);

    private native int getMeetingScoreImpl(long j);

    private native int getNumberOfCamerasImpl(long j);

    private native long getSelectedUserImpl(long j);

    private native int getVideoTypeByIDImpl(long j, long j2);

    private native void glViewSizeChangedImpl(long j, long j2, int i, int i2);

    private native boolean handleFECCCmdImpl(long j, int i, long j2, int i2);

    private native boolean hideNoVideoUserInWallViewImpl(long j);

    private native boolean iCanControlltheCamImpl(long j, long j2);

    private native boolean isCamInControlImpl(long j, long j2);

    private native boolean isControlMyCamImpl(long j, long j2);

    private native boolean isLeadShipModeImpl(long j);

    private native boolean isLeaderofLeadModeImpl(long j, long j2);

    private native boolean isManualModeImpl(long j);

    private native boolean isSameVideoImpl(long j, long j2, long j3);

    private native boolean isSelectedUserImpl(long j, long j2);

    private native boolean isSendingVideoImpl(long j);

    private native boolean leaveVideoCompanionModeImpl(long j);

    private native long movePic2Impl(long j, long j2, int i, int i2, int i3, int i4, int i5);

    private native long movePicImpl(long j, long j2, int i, long j3, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private native boolean needTurnOnVideoWhenCanResendImpl(long j);

    private native boolean neverConfirmVideoPrivacyWhenJoinMeetingImpl(long j);

    private native boolean prepareRenderer(long j, long j2);

    private native boolean querySubStatusImpl(long j, long j2);

    private native boolean removePicImpl(long j, long j2, int i);

    private native boolean rotateDeviceImpl(long j, int i, long j2);

    private native boolean setAspectModeImpl(long j, long j2, int i);

    private native void setDefaultDeviceImpl(long j, String str, boolean z);

    private native void setHideNoVideoUserInWallViewImpl(long j, boolean z);

    private native boolean setLeadShipModeImpl(long j, boolean z, long j2);

    private native boolean setManualModeImpl(long j, boolean z, long j2);

    private native void setMobileAppActiveStatusImpl(long j, boolean z);

    private native void setNeverConfirmVideoPrivacyWhenJoinMeetingImpl(long j, boolean z);

    private native void setRendererBackgroudColorImpl(long j, long j2, int i);

    private native boolean showActiveVideoImpl(long j, long j2, long j3, int i);

    private native boolean showAttendeeVideoImpl(long j, long j2, long j3, int i, boolean z);

    private native boolean startMyVideoImpl(long j, long j2);

    private native boolean startPreviewDeviceImpl(long j, long j2, String str);

    private native boolean stopMyVideoImpl(long j, long j2);

    private native boolean stopPreviewDeviceImpl(long j, long j2);

    private native boolean stopShowVideoImpl(long j, long j2, boolean z);

    private native boolean switchToNextCamImpl(long j);

    private native void turnKubiDeviceOnOFFImpl(long j, boolean z);

    private native boolean updateRendererInfo(long j, long j2, int i, int i2, int i3, int i4, int i5, int i6);

    public VideoSessionMgr(long j) {
        this.mNativeHandle = j;
        VideoCapturer.getInstance().setListener(new Listener() {
            public void onCapturePaused() {
            }

            public void onCaptureResumed() {
                VideoSessionMgr videoSessionMgr = VideoSessionMgr.this;
                videoSessionMgr.rotateDevice(videoSessionMgr.mRotation, 0);
            }
        });
    }

    public boolean isPreviewing() {
        return this.mIsPreviewing;
    }

    public boolean isVideoStarted() {
        return isSendingVideo();
    }

    public boolean startPreviewDevice(long j, @Nullable String str) {
        if (this.mNativeHandle == 0 || j == 0 || str == null || this.mIsPreviewing) {
            return false;
        }
        setDefaultDevice(str);
        this.mIsPreviewing = startPreviewDeviceImpl(this.mNativeHandle, j, String.valueOf(str));
        if (this.mIsPreviewing) {
            rotateDevice(this.mRotation, j);
        }
        return this.mIsPreviewing;
    }

    private static boolean isInternalCameraId(@Nullable String str) {
        int i;
        boolean z = false;
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        try {
            i = Integer.valueOf(str).intValue();
        } catch (NumberFormatException unused) {
            i = -1;
        }
        if (i > -1) {
            z = true;
        }
        return z;
    }

    @Nullable
    public String getDefaultCameraToUse() {
        int i;
        String str;
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            i = confContext.getLaunchReason();
        } else {
            i = 0;
        }
        if (i == 6 || i == 5 || i == 10 || i == 11) {
            str = getDefaultDevice();
            StringUtil.isEmptyOrNull(str);
        } else {
            str = getDefaultDevice();
            if (isInternalCameraId(str)) {
                str = null;
            }
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            List camList = getCamList();
            if (camList != null && camList.size() > 0) {
                for (int i2 = 0; i2 < camList.size(); i2++) {
                    MediaDevice mediaDevice = (MediaDevice) camList.get(i2);
                    if (mediaDevice != null && str.equals(mediaDevice.getDeviceId())) {
                        return str;
                    }
                }
            }
            str = null;
        }
        if (NydusUtil.getNumberOfCameras() > 0) {
            int frontCameraId = NydusUtil.getFrontCameraId();
            if (frontCameraId < 0) {
                frontCameraId = 0;
            }
            str = String.valueOf(frontCameraId);
        } else {
            List camList2 = getCamList();
            if (camList2 != null && camList2.size() > 0) {
                MediaDevice mediaDevice2 = (MediaDevice) camList2.get(0);
                if (mediaDevice2 != null) {
                    str = mediaDevice2.getDeviceId();
                }
            }
        }
        return str;
    }

    public boolean stopPreviewDevice(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return false;
        }
        if (!this.mIsPreviewing) {
            return stopShowVideo(j);
        }
        boolean stopPreviewDeviceImpl = stopPreviewDeviceImpl(j2, j);
        this.mIsPreviewing = false;
        return stopPreviewDeviceImpl;
    }

    public boolean startMyVideo(long j) {
        if (this.mNativeHandle == 0 || getNumberOfCameras() < 1) {
            return false;
        }
        if (isVideoStarted()) {
            return true;
        }
        if (!this.mbHasSetDefaultDevice) {
            String defaultCameraToUse = getDefaultCameraToUse();
            if (!StringUtil.isEmptyOrNull(defaultCameraToUse)) {
                setDefaultDevice(defaultCameraToUse);
            }
        }
        boolean startMyVideoImpl = startMyVideoImpl(this.mNativeHandle, j);
        if (startMyVideoImpl) {
            rotateDevice(this.mRotation, 0);
        }
        return startMyVideoImpl;
    }

    public boolean stopMyVideo(long j) {
        if (this.mNativeHandle != 0 && isVideoStarted()) {
            return stopMyVideoImpl(this.mNativeHandle, j);
        }
        return false;
    }

    public boolean showAttendeeVideo(long j, long j2, int i, boolean z) {
        long j3 = this.mNativeHandle;
        if (j3 == 0 || j == 0) {
            return false;
        }
        return showAttendeeVideoImpl(j3, j, j2, i, z);
    }

    public boolean stopShowVideo(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0 || j == 0) {
            return false;
        }
        return stopShowVideoImpl(j2, j, false);
    }

    public void setGLViewSize(int i, int i2) {
        this.mViewWidth = i;
        this.mViewHeight = i2;
    }

    @Nullable
    public VideoUnit createVideoUnit(@NonNull VideoBoxApplication videoBoxApplication, boolean z, @NonNull RendererUnitInfo rendererUnitInfo) {
        RendererUnitInfo rendererUnitInfo2 = rendererUnitInfo;
        long createRendererInfo = createRendererInfo(this.mNativeHandle, z, 0, this.mViewWidth, this.mViewHeight, rendererUnitInfo2.left, rendererUnitInfo2.top, rendererUnitInfo2.width, rendererUnitInfo2.height);
        if (createRendererInfo != 0) {
            if (prepareRenderer(this.mNativeHandle, createRendererInfo)) {
                VideoUnit videoUnit = new VideoUnit(videoBoxApplication, z, createRendererInfo, rendererUnitInfo);
                return videoUnit;
            }
            destroyRendererInfo(this.mNativeHandle, createRendererInfo);
        }
        return null;
    }

    public void destroyVideoUnit(@Nullable VideoUnit videoUnit) {
        if (videoUnit != null) {
            long rendererInfo = videoUnit.getRendererInfo();
            destroyRenderer(this.mNativeHandle, rendererInfo);
            destroyRendererInfo(this.mNativeHandle, rendererInfo);
        }
    }

    @Nullable
    public GLButton createGLButton(@NonNull RendererUnitInfo rendererUnitInfo) {
        long createRendererInfo = createRendererInfo(this.mNativeHandle, false, 0, this.mViewWidth, this.mViewHeight, rendererUnitInfo.left, rendererUnitInfo.top, rendererUnitInfo.width, rendererUnitInfo.height);
        if (createRendererInfo != 0) {
            if (prepareRenderer(this.mNativeHandle, createRendererInfo)) {
                return new GLButton(createRendererInfo, rendererUnitInfo);
            }
            destroyRendererInfo(this.mNativeHandle, createRendererInfo);
        }
        return null;
    }

    public void destroyGLButton(@Nullable GLButton gLButton) {
        if (gLButton != null) {
            long rendererInfo = gLButton.getRendererInfo();
            destroyRenderer(this.mNativeHandle, rendererInfo);
            destroyRendererInfo(this.mNativeHandle, rendererInfo);
        }
    }

    @Nullable
    public GLImage createGLImage(@NonNull RendererUnitInfo rendererUnitInfo) {
        long createRendererInfo = createRendererInfo(this.mNativeHandle, false, 0, this.mViewWidth, this.mViewHeight, rendererUnitInfo.left, rendererUnitInfo.top, rendererUnitInfo.width, rendererUnitInfo.height);
        if (createRendererInfo != 0) {
            if (prepareRenderer(this.mNativeHandle, createRendererInfo)) {
                return new GLImage(createRendererInfo, rendererUnitInfo);
            }
            destroyRendererInfo(this.mNativeHandle, createRendererInfo);
        }
        return null;
    }

    public void destroyGLImage(@Nullable GLImage gLImage) {
        if (gLImage != null) {
            long rendererInfo = gLImage.getRendererInfo();
            destroyRenderer(this.mNativeHandle, rendererInfo);
            destroyRendererInfo(this.mNativeHandle, rendererInfo);
        }
    }

    public void updateUnitLayout(long j, @Nullable RendererUnitInfo rendererUnitInfo) {
        if (rendererUnitInfo != null) {
            updateRendererInfo(this.mNativeHandle, j, this.mViewWidth, this.mViewHeight, rendererUnitInfo.left, rendererUnitInfo.top, rendererUnitInfo.width, rendererUnitInfo.height);
        }
    }

    public void drawFrame(long j) {
        drawFrameImpl(this.mNativeHandle, j);
    }

    public boolean enable180p(boolean z) {
        return enable180pImpl(this.mNativeHandle, z);
    }

    public boolean showActiveVideo(long j, long j2, int i) {
        return showActiveVideoImpl(this.mNativeHandle, j, j2, i);
    }

    public void setDefaultDevice(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            int i = -1;
            try {
                i = Integer.parseInt(str);
            } catch (NumberFormatException unused) {
            }
            if (i >= 0) {
                setDefaultDeviceImpl(this.mNativeHandle, str, NydusUtil.isFrontCamera(i) && !NydusUtil.isCameraMirror(i));
            } else {
                setDefaultDeviceImpl(this.mNativeHandle, str, true);
            }
            this.mbHasSetDefaultDevice = true;
        }
    }

    @Nullable
    public String getDefaultDevice() {
        return getDefaultDeviceImpl(this.mNativeHandle);
    }

    public boolean switchCamera(@NonNull String str) {
        if (getNumberOfCameras() <= 0) {
            return false;
        }
        if (StringUtil.isSameString(str, getDefaultDevice()) && isVideoStarted()) {
            return true;
        }
        if (isVideoStarted() && !stopMyVideo(0)) {
            return false;
        }
        setDefaultDevice(str);
        return startMyVideo(0);
    }

    @Nullable
    public List<MediaDevice> getCamList() {
        return getCamListImpl(this.mNativeHandle);
    }

    public int getNumberOfCameras() {
        return getNumberOfCamerasImpl(this.mNativeHandle);
    }

    public int getVideoTypeByID(long j) {
        return getVideoTypeByIDImpl(this.mNativeHandle, j);
    }

    public long addPic(long j, int i, @Nullable Bitmap bitmap, int i2, int i3, int i4, int i5, int i6, int i7) {
        int[] iArr;
        System.currentTimeMillis();
        if (j == 0 || bitmap == null || i6 < i4 || i7 < i5) {
            return 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr2 = null;
        try {
            iArr = new int[(width * height)];
        } catch (OutOfMemoryError unused) {
            iArr = iArr2;
        }
        int i8 = width;
        bitmap.getPixels(iArr, 0, width, 0, 0, i8, height);
        return addPicImpl(this.mNativeHandle, j, i, iArr, i8, height, i2, i3, i4, i5, i6, i7);
    }

    @Deprecated
    public long movePic(long j, int i, long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        if (j == 0 || j2 <= 0 || i8 < i6 || i9 < i7) {
            return 0;
        }
        return movePicImpl(this.mNativeHandle, j, i, j2, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public long movePic2(long j, int i, int i2, int i3, int i4, int i5) {
        if (j == 0) {
            return 0;
        }
        if (i4 < i2 || i5 < i3) {
            return 0;
        }
        return movePic2Impl(this.mNativeHandle, j, i, i2, i3, i4, i5);
    }

    public boolean removePic(long j, int i) {
        if (j == 0) {
            return false;
        }
        return removePicImpl(this.mNativeHandle, j, i);
    }

    public void glViewSizeChanged(long j, int i, int i2) {
        if (j != 0) {
            glViewSizeChangedImpl(this.mNativeHandle, j, i, i2);
        }
    }

    public void clearRenderer(long j) {
        if (j != 0) {
            clearRendererImpl(this.mNativeHandle, j);
        }
    }

    @NonNull
    public VideoSize getMyVideoSize() {
        VideoSize videoSize = VideoCapturer.getInstance().getVideoSize();
        switch (this.mRotation) {
            case 1:
            case 3:
                int i = videoSize.width;
                videoSize.width = videoSize.height;
                videoSize.height = i;
                break;
        }
        return videoSize;
    }

    public void setRendererBackgroudColor(long j, int i) {
        setRendererBackgroudColorImpl(this.mNativeHandle, j, i);
    }

    public boolean querySubStatus(long j) {
        return querySubStatusImpl(this.mNativeHandle, j);
    }

    public void onMyVideoStarted() {
        rotateDevice(this.mRotation, 0);
    }

    public boolean rotateDevice(int i, long j) {
        this.mRotation = i;
        if (!VideoCapturer.getInstance().isCapturePaused()) {
            this.mRotationBeforeVideoPaused = i;
            return rotateDeviceImpl(this.mNativeHandle, i, j);
        }
        return rotateDeviceImpl(this.mNativeHandle, this.mRotationBeforeVideoPaused, j);
    }

    public boolean setAspectMode(long j, int i) {
        return setAspectModeImpl(this.mNativeHandle, j, i);
    }

    public boolean isSameVideo(long j, long j2) {
        return isSameVideoImpl(this.mNativeHandle, j, j2);
    }

    public boolean isSendingVideo() {
        return isSendingVideoImpl(this.mNativeHandle);
    }

    public boolean isLeadShipMode() {
        return isLeadShipModeImpl(this.mNativeHandle);
    }

    public boolean isLeaderofLeadMode(long j) {
        return isLeaderofLeadModeImpl(this.mNativeHandle, j);
    }

    public boolean setLeadShipMode(boolean z, long j) {
        return setLeadShipModeImpl(this.mNativeHandle, z, j);
    }

    public long getActiveUserID() {
        return getActiveUserIDImpl(this.mNativeHandle);
    }

    public long getActiveDeckUserID(boolean z) {
        return getActiveDeckUserIDImpl(this.mNativeHandle, z);
    }

    public int getMeetingScore() {
        return getMeetingScoreImpl(this.mNativeHandle);
    }

    public boolean handleFECCCmd(int i, long j) {
        return handleFECCCmd(i, j, 0);
    }

    public boolean handleFECCCmd(int i, long j, int i2) {
        return handleFECCCmdImpl(this.mNativeHandle, i, j, i2);
    }

    public boolean canControlltheCam(long j) {
        return iCanControlltheCamImpl(this.mNativeHandle, j);
    }

    public boolean isControlMyCam(long j) {
        return isControlMyCamImpl(this.mNativeHandle, j);
    }

    public boolean isCamInControl(long j) {
        return isCamInControlImpl(this.mNativeHandle, j);
    }

    public boolean switchToNextCam() {
        String defaultDevice = getDefaultDevice();
        List camList = getCamList();
        if (camList == null || camList.size() == 0) {
            return false;
        }
        int i = 0;
        while (i < camList.size() && !StringUtil.isSameString(((MediaDevice) camList.get(i)).getDeviceId(), defaultDevice)) {
            i++;
        }
        MediaDevice mediaDevice = (MediaDevice) camList.get((i + 1) % camList.size());
        if (mediaDevice != null) {
            return switchCamera(mediaDevice.getDeviceId());
        }
        return false;
    }

    public void turnKubiDeviceOnOFF(boolean z) {
        turnKubiDeviceOnOFFImpl(this.mNativeHandle, z);
    }

    public boolean isManualMode() {
        return isManualModeImpl(this.mNativeHandle);
    }

    public boolean setManualMode(boolean z, long j) {
        return setManualModeImpl(this.mNativeHandle, z, j);
    }

    public boolean isSelectedUser(long j) {
        return isSelectedUserImpl(this.mNativeHandle, j);
    }

    public long getSelectedUser() {
        return getSelectedUserImpl(this.mNativeHandle);
    }

    public void setMobileAppActiveStatus(boolean z) {
        setMobileAppActiveStatusImpl(this.mNativeHandle, z);
    }

    public boolean neverConfirmVideoPrivacyWhenJoinMeeting() {
        return neverConfirmVideoPrivacyWhenJoinMeetingImpl(this.mNativeHandle);
    }

    public void setNeverConfirmVideoPrivacyWhenJoinMeeting(boolean z) {
        setNeverConfirmVideoPrivacyWhenJoinMeetingImpl(this.mNativeHandle, z);
    }

    public boolean needTurnOnVideoWhenCanResend() {
        return needTurnOnVideoWhenCanResendImpl(this.mNativeHandle);
    }

    public boolean leaveVideoCompanionMode() {
        return leaveVideoCompanionModeImpl(this.mNativeHandle);
    }

    public boolean hideNoVideoUserInWallView() {
        return hideNoVideoUserInWallViewImpl(this.mNativeHandle);
    }

    public void setHideNoVideoUserInWallView(boolean z) {
        setHideNoVideoUserInWallViewImpl(this.mNativeHandle, z);
    }
}
