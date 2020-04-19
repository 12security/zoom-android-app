package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RootedWarningDialog extends ZMDialogFragment {
    private static final String TAG = "RootedWarningDialog";
    /* access modifiers changed from: private */
    public RootedWarningDialogCallback listener;

    public interface RootedWarningDialogCallback {
        void onCancel();

        void onConfirm();
    }

    public RootedWarningDialog() {
        setCancelable(false);
    }

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            Fragment findFragmentByTag = zMActivity.getSupportFragmentManager().findFragmentByTag(TAG);
            if (findFragmentByTag == null || !findFragmentByTag.isAdded()) {
                new RootedWarningDialog().show(zMActivity.getSupportFragmentManager(), TAG);
            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (RootedWarningDialogCallback) context;
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setCancelable(true).setView(createContent()).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material_RoundRect), C4558R.layout.zm_rooted_warning_dialog, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.rooted_warning_dialog_quit_btn);
        ((TextView) inflate.findViewById(C4558R.C4560id.rooted_warning_dialog_continue_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RootedWarningDialog.this.listener != null) {
                    RootedWarningDialog.this.listener.onConfirm();
                }
                RootedWarningDialog.this.dismiss();
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (RootedWarningDialog.this.listener != null) {
                    RootedWarningDialog.this.listener.onCancel();
                }
                RootedWarningDialog.this.dismiss();
            }
        });
        return inflate;
    }
}
