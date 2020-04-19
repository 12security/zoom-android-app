package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PermissionUnableAccessDialog extends ZMDialogFragment {
    private static final String ARG_PERMISSION = "arg_permission";

    public static void showDialog(FragmentManager fragmentManager, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            PermissionUnableAccessDialog permissionUnableAccessDialog = new PermissionUnableAccessDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PERMISSION, str);
            permissionUnableAccessDialog.setArguments(bundle);
            permissionUnableAccessDialog.show(fragmentManager, PermissionUnableAccessDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(ARG_PERMISSION);
        String str = "";
        String str2 = "";
        if ("android.permission.CAMERA".equals(string)) {
            str = getString(C4558R.string.zm_title_unable_access_camera);
            str2 = getString(C4558R.string.zm_msg_unable_access_camera);
        } else if ("android.permission.RECORD_AUDIO".equals(string)) {
            str = getString(C4558R.string.zm_title_unable_access_mic);
            str2 = getString(C4558R.string.zm_msg_unable_access_mic);
        } else if ("android.permission.READ_EXTERNAL_STORAGE".equals(string)) {
            str = getString(C4558R.string.zm_title_unable_access_storage);
            str2 = getString(C4558R.string.zm_msg_unable_access_storage);
        } else if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(string)) {
            str = getString(C4558R.string.zm_title_unable_access_storage);
            str2 = getString(C4558R.string.zm_msg_unable_access_storage);
        }
        return new Builder(getActivity()).setCancelable(true).setTitle((CharSequence) str).setMessage(str2).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }
}
