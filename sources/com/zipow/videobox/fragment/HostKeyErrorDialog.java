package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class HostKeyErrorDialog extends ZMDialogFragment {
    public static void show(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            HostKeyErrorDialog hostKeyErrorDialog = new HostKeyErrorDialog();
            hostKeyErrorDialog.setArguments(bundle);
            hostKeyErrorDialog.show(fragmentManager, HostKeyErrorDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setCancelable(true).setMessage(C4558R.string.zm_lbl_hostkey_error_desc).setPositiveButton(C4558R.string.zm_btn_enter_again, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivityNormal confActivityNormal = (ConfActivityNormal) HostKeyErrorDialog.this.getActivity();
                if (confActivityNormal != null) {
                    HostKeyEnterDialog.show(confActivityNormal.getSupportFragmentManager(), ConfLocalHelper.getMeetingHostName(), HostKeyEnterDialog.class.getName());
                }
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }
}
