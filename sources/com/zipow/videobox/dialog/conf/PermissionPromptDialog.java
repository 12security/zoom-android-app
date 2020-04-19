package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PermissionPromptDialog extends ZMDialogFragment {
    public static void showDialog(FragmentManager fragmentManager) {
        new PermissionPromptDialog().show(fragmentManager, PermissionPromptDialog.class.getName());
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTitle(C4558R.string.zm_title_permission_prompt).setMessage(C4558R.string.zm_msg_meeting_permission).setPositiveButton(C4558R.string.zm_btn_got_it, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionPromptDialog.this.handlePermissionRequest();
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        handlePermissionRequest();
    }

    /* access modifiers changed from: private */
    public void handlePermissionRequest() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            confActivity.doRequestPermission();
        }
    }
}
