package com.zipow.annotate.annoDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnotateDialogFragment extends ZMDialogFragment {
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        Window window = onCreateDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(C4558R.color.zm_transparent);
        }
        return onCreateDialog;
    }

    public void onResume() {
        super.onResume();
    }

    public void onStart() {
        super.onStart();
        Context context = getContext();
        if (context != null) {
            getDialog().getWindow().setLayout((int) (((float) UIUtil.getDisplayWidth(context)) * 0.6f), (int) (((float) UIUtil.getDisplayHeight(context)) * 0.65f));
        }
    }

    /* access modifiers changed from: protected */
    public void requestWindowFeature(int i) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(i);
        }
    }

    /* access modifiers changed from: protected */
    public void setCanceledOnTouchOutside(boolean z) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(z);
        }
    }

    /* access modifiers changed from: protected */
    public void requestLargeLayout() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            Log.w(getClass().getSimpleName(), "requestLargeLayout dialog has not init yet!");
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            Log.w(getClass().getSimpleName(), "requestLargeLayout window has not init yet!");
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            float displayWidthInDip = UIUtil.getDisplayWidthInDip(activity);
            float displayHeightInDip = UIUtil.getDisplayHeightInDip(activity);
            int dimensionPixelSize = getResources().getDimensionPixelSize(C4558R.dimen.annotate_dialog_min_width);
            if (displayWidthInDip < displayHeightInDip) {
                displayHeightInDip = displayWidthInDip;
            }
            if (displayHeightInDip < ((float) dimensionPixelSize)) {
                window.setLayout(dimensionPixelSize, -1);
            }
        }
    }

    public void dismissAllowingStateLoss() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                View currentFocus = window.getCurrentFocus();
                if (currentFocus != null) {
                    UIUtil.closeSoftKeyboard(currentFocus.getContext(), currentFocus, 2);
                }
            }
        }
        super.dismissAllowingStateLoss();
    }

    public void dismiss() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                View currentFocus = window.getCurrentFocus();
                if (currentFocus != null) {
                    UIUtil.closeSoftKeyboard(currentFocus.getContext(), currentFocus, 2);
                }
            }
        }
        super.dismiss();
    }
}
