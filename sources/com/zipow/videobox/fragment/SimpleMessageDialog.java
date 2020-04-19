package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SimpleMessageDialog extends ZMDialogFragment {
    private static final String ARG_FINISH_ACTIVITY_ON_DISMISS = "finishActivityOnDismiss";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_MESSAGE_ID = "messageId";
    private static final String ARG_TITLE = "title";
    private static final String ARG_TITLE_ID = "titleId";

    @NonNull
    public static SimpleMessageDialog newInstance(String str) {
        return newInstance(str, (String) null);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(String str, boolean z) {
        return newInstance(str, (String) null, z);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(String str, String str2) {
        return newInstance(str, str2, false);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(String str, String str2, boolean z) {
        SimpleMessageDialog simpleMessageDialog = new SimpleMessageDialog();
        simpleMessageDialog.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putString("message", str);
        bundle.putString("title", str2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, z);
        simpleMessageDialog.setArguments(bundle);
        return simpleMessageDialog;
    }

    @NonNull
    public static SimpleMessageDialog newInstance(int i) {
        return newInstance(i, 0);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(int i, boolean z) {
        return newInstance(i, 0, z);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(int i, int i2) {
        return newInstance(i, i2, false);
    }

    @NonNull
    public static SimpleMessageDialog newInstance(int i, int i2, boolean z) {
        SimpleMessageDialog simpleMessageDialog = new SimpleMessageDialog();
        simpleMessageDialog.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putInt("messageId", i);
        bundle.putInt("titleId", i2);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, z);
        simpleMessageDialog.setArguments(bundle);
        return simpleMessageDialog;
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
        String string = arguments.getString("message");
        String string2 = arguments.getString("title");
        final boolean z = arguments.getBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, false);
        if (string == null) {
            int i = arguments.getInt("messageId");
            if (i > 0) {
                string = getActivity().getString(i);
            }
        }
        if (string2 == null && getActivity() != null) {
            int i2 = arguments.getInt("titleId");
            if (i2 > 0) {
                string2 = getActivity().getString(i2);
            }
        }
        return new Builder(getActivity()).setMessage(string).setTitle((CharSequence) string2).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentActivity activity = SimpleMessageDialog.this.getActivity();
                if (activity != null && z) {
                    activity.finish();
                }
            }
        }).create();
    }
}
