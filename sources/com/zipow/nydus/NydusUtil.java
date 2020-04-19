package com.zipow.nydus;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.util.HashMap;

public class NydusUtil {
    private static final String[] HTC_ISSUE_FRONT_CAMERA_DEVICES = {"HTC Glacier", "HTC Desire S", "HTC Thunderbolt", "HTC EVO 4G"};
    private static final HashMap<String, String> PREVIEW_CAMERA_DEVICES_ISSUE_LIST = new HashMap<>();
    private static final String[] SAMSUNG_ISSUE_FRONT_CAMERA_DEVICES = {"GT-I9003", "SPH-D700", "GT-P1000"};
    private static final String TAG = "NydusUtil";
    @NonNull
    private static HashMap<Integer, CameraInfo> gCameraInfoMap = new HashMap<>();
    private static int gNumberOfCameras = -1;

    static {
        PREVIEW_CAMERA_DEVICES_ISSUE_LIST.put("xiaomi", "mi 5");
    }

    public static boolean isSamsungIncorrectPreviewSizeFrontCameraDevice() {
        if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
            int i = 0;
            while (true) {
                String[] strArr = SAMSUNG_ISSUE_FRONT_CAMERA_DEVICES;
                if (i >= strArr.length) {
                    break;
                } else if (strArr[i].equals(Build.MODEL)) {
                    return true;
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    public static int getRealCameraOrientation(int i) {
        if (i == 1) {
            if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
                int i2 = 0;
                while (true) {
                    String[] strArr = SAMSUNG_ISSUE_FRONT_CAMERA_DEVICES;
                    if (i2 >= strArr.length) {
                        break;
                    } else if (strArr[i2].equals(Build.MODEL)) {
                        return 0;
                    } else {
                        i2++;
                    }
                }
            }
            if ("HTC".equalsIgnoreCase(Build.MANUFACTURER)) {
                int i3 = 0;
                while (true) {
                    String[] strArr2 = HTC_ISSUE_FRONT_CAMERA_DEVICES;
                    if (i3 >= strArr2.length) {
                        break;
                    } else if (strArr2[i3].equals(Build.MODEL)) {
                        return 0;
                    } else {
                        i3++;
                    }
                }
            }
        }
        if (getNumberOfCameras() <= i) {
            return 0;
        }
        CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(i, cameraInfo);
        return cameraInfo.orientation;
    }

    public static int getFrontCameraId() {
        int numberOfCameras = getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo cameraInfo = new CameraInfo();
            getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 1) {
                return i;
            }
        }
        return -1;
    }

    public static int getBackCameraId() {
        int numberOfCameras = getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo cameraInfo = new CameraInfo();
            getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == 0) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isFrontCamera(int i) {
        boolean z = false;
        if (i < 0) {
            return false;
        }
        CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(i, cameraInfo);
        if (cameraInfo.facing == 1) {
            z = true;
        }
        return z;
    }

    public static boolean isFrontCamera(@Nullable String str) {
        if (str == null) {
            return false;
        }
        try {
            return isFrontCamera(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static boolean isBackCamera(int i) {
        boolean z = false;
        if (i < 0) {
            return false;
        }
        CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(i, cameraInfo);
        if (cameraInfo.facing == 0) {
            z = true;
        }
        return z;
    }

    public static boolean isBackCamera(@Nullable String str) {
        if (str == null) {
            return false;
        }
        try {
            return isBackCamera(Integer.parseInt(str));
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static boolean isCameraMirror(int i) {
        if (i == 1) {
            if ("samsung".equalsIgnoreCase(Build.MANUFACTURER)) {
                int i2 = 0;
                while (true) {
                    String[] strArr = SAMSUNG_ISSUE_FRONT_CAMERA_DEVICES;
                    if (i2 >= strArr.length) {
                        break;
                    } else if (strArr[i2].equals(Build.MODEL)) {
                        return true;
                    } else {
                        i2++;
                    }
                }
            }
            if ("HTC".equals(Build.MANUFACTURER)) {
                int i3 = 0;
                while (true) {
                    String[] strArr2 = HTC_ISSUE_FRONT_CAMERA_DEVICES;
                    if (i3 >= strArr2.length) {
                        break;
                    } else if (strArr2[i3].equals(Build.MODEL)) {
                        return true;
                    } else {
                        i3++;
                    }
                }
            }
        }
        return false;
    }

    public static int getNumberOfCameras() {
        int i = gNumberOfCameras;
        if (i >= 0) {
            return i;
        }
        gNumberOfCameras = Camera.getNumberOfCameras();
        return gNumberOfCameras;
    }

    public static void getCameraInfo(int i, @Nullable CameraInfo cameraInfo) {
        CameraInfo cameraInfo2;
        if (i >= 0 && cameraInfo != null) {
            synchronized (gCameraInfoMap) {
                cameraInfo2 = (CameraInfo) gCameraInfoMap.get(Integer.valueOf(i));
                if (cameraInfo2 == null && i < getNumberOfCameras()) {
                    cameraInfo2 = new CameraInfo();
                    try {
                        Camera.getCameraInfo(i, cameraInfo2);
                        gCameraInfoMap.put(Integer.valueOf(i), cameraInfo2);
                    } catch (Exception unused) {
                        if (i == 0) {
                            cameraInfo2.facing = 0;
                            cameraInfo2.orientation = 90;
                        } else {
                            cameraInfo2.facing = 1;
                            cameraInfo2.orientation = SubsamplingScaleImageView.ORIENTATION_270;
                        }
                    }
                }
            }
            if (cameraInfo2 != null) {
                cameraInfo.facing = cameraInfo2.facing;
                cameraInfo.orientation = cameraInfo2.orientation;
            }
        }
    }

    public static boolean hasIssueForDevicePreview() {
        boolean z = false;
        if (Build.MANUFACTURER == null) {
            return false;
        }
        if (PREVIEW_CAMERA_DEVICES_ISSUE_LIST.containsKey(Build.MANUFACTURER.toLowerCase()) && Build.MODEL != null && ((String) PREVIEW_CAMERA_DEVICES_ISSUE_LIST.get(Build.MANUFACTURER.toLowerCase())).equalsIgnoreCase(Build.MODEL.toLowerCase())) {
            z = true;
        }
        return z;
    }
}
