package com.zipow.videobox.util.photopicker;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionsUtils {
    public static boolean checkReadStoragePermission(@NonNull Activity activity) {
        boolean z = ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_EXTERNAL_READ, 2);
        }
        return z;
    }

    public static boolean checkWriteStoragePermission(Fragment fragment) {
        boolean z = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!z) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return z;
    }

    public static boolean checkCameraPermission(Fragment fragment) {
        boolean z = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.CAMERA") == 0;
        if (!z) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA, 1);
        }
        return z;
    }
}
