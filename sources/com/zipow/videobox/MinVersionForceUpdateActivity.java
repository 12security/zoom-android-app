package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.dialog.MinimumVersionForceUpdateDialog;
import com.zipow.videobox.dialog.MinimumVersionForceUpdateDialog.RequestPermissionListener;
import com.zipow.videobox.fragment.NewVersionDialog;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UpgradeUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;

public class MinVersionForceUpdateActivity extends ZMActivity {
    private static final int FORCE_UPDATE_PERMISSIONS_REQUEST_CODE = 117;
    private static final String TAG = "MinVersionForceUpdateActivity";

    public static void showMinVersionForceUpdate(@NonNull Context context, String str, boolean z) {
        NewVersionDialog instance = NewVersionDialog.getInstance();
        if (instance != null) {
            instance.postDismiss();
        }
        Intent intent = new Intent(context, MinVersionForceUpdateActivity.class);
        intent.setFlags(268435456);
        intent.putExtra(MinimumVersionForceUpdateDialog.ARG_MIN_VERSION, str);
        intent.putExtra(MinimumVersionForceUpdateDialog.ARG_IS_JOIN, z);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        final String str;
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent == null) {
            str = "";
        } else {
            str = intent.getStringExtra(MinimumVersionForceUpdateDialog.ARG_MIN_VERSION);
        }
        final boolean z = false;
        if (intent != null) {
            z = intent.getBooleanExtra(MinimumVersionForceUpdateDialog.ARG_IS_JOIN, false);
        }
        getNonNullEventTaskManagerOrThrowException().push("showMinimumVersionForceUpdateDialog", new EventAction("showMinimumVersionForceUpdateDialog") {
            public void run(@NonNull IUIElement iUIElement) {
                MinimumVersionForceUpdateDialog.show(MinVersionForceUpdateActivity.this, z, str, new RequestPermissionListener() {
                    public void requestPermission() {
                        MinVersionForceUpdateActivity.this.zm_requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 117);
                    }
                });
            }
        });
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 117 && checkStoragePermission()) {
            UpgradeUtil.upgrade(this);
        }
    }

    private boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
}
