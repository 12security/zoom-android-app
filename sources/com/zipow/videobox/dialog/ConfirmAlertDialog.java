package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ConfirmAlertDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }

    public static abstract class SimpleOnButtonClickListener implements OnButtonClickListener {
        public void onNegativeClick() {
        }
    }

    public static void show(@NonNull Context context, String str, String str2, @NonNull OnButtonClickListener onButtonClickListener2) {
        FragmentManager supportFragmentManager = context instanceof ZMActivity ? ((ZMActivity) context).getSupportFragmentManager() : null;
        if (supportFragmentManager != null) {
            ConfirmAlertDialog confirmAlertDialog = new ConfirmAlertDialog();
            confirmAlertDialog.setOnButtonClickListener(onButtonClickListener2);
            Bundle bundle = new Bundle();
            bundle.putString("title", str);
            bundle.putString(NotificationCompat.CATEGORY_MESSAGE, str2);
            confirmAlertDialog.setArguments(bundle);
            confirmAlertDialog.show(supportFragmentManager, ConfirmAlertDialog.class.getName());
        }
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ConfirmAlertDialog confirmAlertDialog = (ConfirmAlertDialog) fragmentManager.findFragmentByTag(ConfirmAlertDialog.class.getName());
            if (confirmAlertDialog != null) {
                confirmAlertDialog.dismiss();
            }
        }
    }

    public ConfirmAlertDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        Bundle arguments = getArguments();
        String str2 = null;
        if (arguments != null) {
            str2 = arguments.getString("title");
            str = arguments.getString(NotificationCompat.CATEGORY_MESSAGE);
        } else {
            str = null;
        }
        return new Builder(getActivity()).setCancelable(false).setTitle((CharSequence) str2).setMessage(str).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ConfirmAlertDialog.this.onButtonClickListener != null) {
                    ConfirmAlertDialog.this.onButtonClickListener.onNegativeClick();
                }
            }
        }).setPositiveButton(C4558R.string.zm_btn_continue, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ConfirmAlertDialog.this.onButtonClickListener != null) {
                    ConfirmAlertDialog.this.onButtonClickListener.onPositiveClick();
                }
            }
        }).create();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener2) {
        this.onButtonClickListener = onButtonClickListener2;
    }
}
