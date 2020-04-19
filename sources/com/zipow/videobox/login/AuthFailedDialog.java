package com.zipow.videobox.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AuthFailedDialog extends ZMDialogFragment {
    public AuthFailedDialog() {
        setCancelable(true);
    }

    public static void show(@NonNull ZMActivity zMActivity, final String str) {
        zMActivity.getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(IUIElement iUIElement) {
                AuthFailedDialog authFailedDialog = new AuthFailedDialog();
                Bundle bundle = new Bundle();
                bundle.putString("message", str);
                authFailedDialog.setArguments(bundle);
                authFailedDialog.show(((ZMActivity) iUIElement).getSupportFragmentManager(), AuthFailedDialog.class.getName());
            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_login_failed).setMessage(arguments.getString("message")).setNegativeButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    public void onDismiss(@NonNull DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }
}
