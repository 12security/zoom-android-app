package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MinVersionForceUpdateActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.UpgradeUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MinimumVersionForceUpdateDialog extends ZMDialogFragment {
    public static final String ARG_IS_JOIN = "arg_is_join";
    public static final String ARG_MIN_VERSION = "arg_min_version";
    private static final String TAG = "MinimumVersionForceUpdateDialog";
    /* access modifiers changed from: private */
    public RequestPermissionListener mRequestPermissionListener;

    public interface RequestPermissionListener {
        void requestPermission();
    }

    public static void show(@NonNull ZMActivity zMActivity, boolean z, String str, RequestPermissionListener requestPermissionListener) {
        MinimumVersionForceUpdateDialog minimumVersionForceUpdateDialog = new MinimumVersionForceUpdateDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_IS_JOIN, z);
        bundle.putString(ARG_MIN_VERSION, str);
        minimumVersionForceUpdateDialog.setRequestPermissionListener(requestPermissionListener);
        minimumVersionForceUpdateDialog.setArguments(bundle);
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            minimumVersionForceUpdateDialog.show(supportFragmentManager, MinimumVersionForceUpdateDialog.class.getName());
        }
    }

    public MinimumVersionForceUpdateDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        boolean z;
        FragmentActivity activity = getActivity();
        String str2 = "";
        Bundle arguments = getArguments();
        if (arguments != null) {
            z = arguments.getBoolean(ARG_IS_JOIN);
            str = arguments.getString(ARG_MIN_VERSION);
        } else {
            str = str2;
            z = false;
        }
        String string = getString(C4558R.string.zm_title_update_required_62061);
        Builder builder = new Builder(activity);
        builder.setTitle((CharSequence) string);
        builder.setPositiveButton(C4558R.string.zm_btn_update_62061, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMActivity zMActivity = (ZMActivity) MinimumVersionForceUpdateDialog.this.getActivity();
                if (zMActivity != null) {
                    if (!NetworkUtil.hasDataNetwork(zMActivity)) {
                        MinimumVersionForceUpdateDialog.this.showConnectionError();
                        return;
                    }
                    if (MinimumVersionForceUpdateDialog.this.checkStoragePermission()) {
                        UpgradeUtil.upgrade(zMActivity);
                    } else if (MinimumVersionForceUpdateDialog.this.mRequestPermissionListener != null) {
                        MinimumVersionForceUpdateDialog.this.mRequestPermissionListener.requestPermission();
                    }
                }
            }
        });
        builder.setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentActivity activity = MinimumVersionForceUpdateDialog.this.getActivity();
                if (activity instanceof MinVersionForceUpdateActivity) {
                    activity.finish();
                }
            }
        });
        if (z) {
            builder.setMessage(getString(C4558R.string.zm_msg_update_required_join_62061, str));
        } else {
            builder.setMessage(getString(C4558R.string.zm_msg_update_required_sign_62061, PTApp.getInstance().getMinClientVersion()));
        }
        ZMAlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        create.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
        return create;
    }

    public void setRequestPermissionListener(RequestPermissionListener requestPermissionListener) {
        this.mRequestPermissionListener = requestPermissionListener;
    }

    /* access modifiers changed from: private */
    public boolean checkStoragePermission() {
        return VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    public void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }
}
