package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ShareAlertDialog extends ZMDialogFragment {
    private static final String ARG_PROCESS_TYPE = "process_type";
    private static final String ARG_SHARE_ALERT_MESSAGE = "share_alert_message";
    private static final String ARG_SHOW_TITLE = "show_title";

    public static void showDialog(FragmentManager fragmentManager, String str, boolean z, int i) {
        if (!StringUtil.isEmptyOrNull(str)) {
            ShareAlertDialog shareAlertDialog = new ShareAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_SHARE_ALERT_MESSAGE, str);
            bundle.putBoolean(ARG_SHOW_TITLE, z);
            bundle.putInt(ARG_PROCESS_TYPE, i);
            shareAlertDialog.setArguments(bundle);
            shareAlertDialog.show(fragmentManager, ShareAlertDialog.class.getName());
        }
    }

    public static void showDialog(FragmentManager fragmentManager, String str, boolean z) {
        showDialog(fragmentManager, str, z, 0);
    }

    public static void showConfDialog(FragmentManager fragmentManager, String str, boolean z) {
        showDialog(fragmentManager, str, z, 1);
    }

    public static void showConfDialog(@NonNull Context context, FragmentManager fragmentManager, @StringRes int i, boolean z) {
        String string = context.getResources().getString(i);
        if (!StringUtil.isEmptyOrNull(string)) {
            showConfDialog(fragmentManager, string, z);
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(ARG_SHARE_ALERT_MESSAGE);
        boolean z = arguments.getBoolean(ARG_SHOW_TITLE);
        final int i = arguments.getInt(ARG_PROCESS_TYPE, 0);
        Builder positiveButton = new Builder(getActivity()).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1 && ConfLocalHelper.isDirectShareClient()) {
                    PTAppDelegation.getInstance().stopPresentToRoom(false);
                }
            }
        });
        if (z) {
            positiveButton.setTitle(C4558R.string.zm_title_error);
        }
        positiveButton.setMessage(string);
        return positiveButton.create();
    }
}
