package com.zipow.videobox.util;

import android.content.Context;
import android.hardware.Camera.CameraInfo;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zipow.nydus.NydusUtil;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;

public class ZMConfCameraUtils {
    private static final String TAG = "ZMConfCameraUtils";

    public static int getNumberOfCameras() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            return 0;
        }
        return videoObj.getNumberOfCameras();
    }

    @Nullable
    public static String getCameraId() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        String str = null;
        if (videoObj != null) {
            if (videoObj.isVideoStarted() || videoObj.isPreviewing()) {
                str = videoObj.getDefaultDevice();
            }
            if (StringUtil.isEmptyOrNull(str)) {
                return videoObj.getDefaultCameraToUse();
            }
            return str;
        }
        int frontCameraId = NydusUtil.getFrontCameraId();
        if (frontCameraId >= 0) {
            return String.valueOf(frontCameraId);
        }
        return null;
    }

    public static int getCameraFace() {
        String cameraId = getCameraId();
        if (StringUtil.isEmptyOrNull(cameraId)) {
            return 0;
        }
        int i = -1;
        try {
            i = Integer.parseInt(cameraId);
        } catch (NumberFormatException e) {
            ZMLog.m281e(TAG, e, "getRotation NumberFormatException", new Object[0]);
        }
        if (i < 0) {
            return 1;
        }
        CameraInfo cameraInfo = new CameraInfo();
        NydusUtil.getCameraInfo(i, cameraInfo);
        return cameraInfo.facing;
    }

    public static int getCurrentMyVideoRotation(@Nullable Context context) {
        int i = 0;
        if (context == null) {
            return 0;
        }
        String cameraId = getCameraId();
        if (StringUtil.isEmptyOrNull(cameraId)) {
            return 0;
        }
        switch (((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 1:
                i = SubsamplingScaleImageView.ORIENTATION_270;
                break;
            case 2:
                i = 180;
                break;
            case 3:
                i = 90;
                break;
        }
        return getRotation(cameraId, i);
    }

    private static int getRotation(@Nullable String str, int i) {
        int i2;
        if (str == null) {
            return 0;
        }
        int i3 = -1;
        try {
            i3 = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            ZMLog.m281e(TAG, e, "getRotation NumberFormatException", new Object[0]);
        }
        if (i3 < 0) {
            return 0;
        }
        CameraInfo cameraInfo = new CameraInfo();
        NydusUtil.getCameraInfo(i3, cameraInfo);
        int realCameraOrientation = NydusUtil.getRealCameraOrientation(i3);
        int i4 = ((i + 45) / 90) * 90;
        if (!NydusUtil.isCameraMirror(i3)) {
            if (cameraInfo.facing == 1) {
                i2 = ((realCameraOrientation - i4) + 360) % 360;
            } else {
                i2 = (realCameraOrientation + i4) % 360;
            }
        } else if (cameraInfo.facing == 0) {
            i2 = ((realCameraOrientation - i4) + 360) % 360;
        } else {
            i2 = (realCameraOrientation + i4) % 360;
        }
        return i2;
    }
}
