package p021us.zoom.androidlib.app;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;

/* renamed from: us.zoom.androidlib.app.ZMActivityCompat */
class ZMActivityCompat {
    ZMActivityCompat() {
    }

    public static void requestPermissionsFromFragment(Fragment fragment, String[] strArr, int i) {
        if (fragment != null && strArr != null && strArr.length > 0) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                if ((-65536 & i) == 0) {
                    requestPermissionsCompat(zMActivity, strArr, ((zMActivity.getFragmentIndex(fragment) + 1) << 16) + (i & 65535));
                    return;
                }
                throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static void requestPermissionsCompat(final ZMActivity zMActivity, final String[] strArr, final int i) {
        if (zMActivity != null) {
            if (VERSION.SDK_INT >= 23) {
                zMActivity.requestPermissions(strArr, i);
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        int[] iArr = new int[strArr.length];
                        PackageManager packageManager = zMActivity.getPackageManager();
                        String packageName = zMActivity.getPackageName();
                        int length = strArr.length;
                        for (int i = 0; i < length; i++) {
                            iArr[i] = packageManager.checkPermission(strArr[i], packageName);
                        }
                        zMActivity.onRequestPermissionsResult(i, strArr, iArr);
                    }
                });
            }
        }
    }
}
