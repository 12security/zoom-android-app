package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMDialogFragment.ZMDialogParam;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.BuildConfig;
import p021us.zoom.videomeetings.C4558R;

public class IncompatibleClientDialog extends ZMDialogFragment {
    private static final String TAG = "IncompatibleClientDialog";
    private ZMDialogParam param;

    public static void showDialog(FragmentManager fragmentManager, long j) {
        ZMDialogParam zMDialogParam = new ZMDialogParam(j);
        if (shouldShow(fragmentManager, TAG, zMDialogParam)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ZMDialogFragment.PARAMS, zMDialogParam);
            IncompatibleClientDialog incompatibleClientDialog = new IncompatibleClientDialog();
            incompatibleClientDialog.setArguments(bundle);
            incompatibleClientDialog.showNow(fragmentManager, TAG);
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.param = (ZMDialogParam) arguments.getParcelable(ZMDialogFragment.PARAMS);
        ZMDialogParam zMDialogParam = this.param;
        if (zMDialogParam == null) {
            return createEmptyDialog();
        }
        long j = zMDialogParam.longParam;
        Builder builder = null;
        if (j == 1037) {
            builder = new Builder(getActivity()).setTitle(C4558R.string.zm_alert_force_normal_client_login_title_132149).setMessage(C4558R.string.zm_alert_force_normal_client_login_message_132149).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_open_70707, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    IncompatibleClientDialog.this.openTargetClientInStore(BuildConfig.APPLICATION_ID);
                }
            });
        } else if (this.param.longParam == 1038) {
            builder = new Builder(getActivity()).setTitle(C4558R.string.zm_alert_force_intune_client_login_title_132149).setMessage(C4558R.string.zm_alert_force_intune_client_login_message_132149).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.zm_btn_open_70707, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    IncompatibleClientDialog.this.openTargetClientInStore("us.zoom.videomeetings4intune");
                }
            });
        }
        return builder == null ? createEmptyDialog() : builder.create();
    }

    /* access modifiers changed from: private */
    public void openTargetClientInStore(String str) {
        if (getActivity() != null) {
            try {
                FragmentActivity activity = getActivity();
                StringBuilder sb = new StringBuilder();
                sb.append("market://details?id=");
                sb.append(str);
                activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
            } catch (ActivityNotFoundException unused) {
            }
        }
    }
}
