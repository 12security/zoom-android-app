package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class PayerReminderDialog extends ZMDialogFragment {
    private static final String ARG_IS_ORIGINAL_HOST = "isOriginalHost";

    @NonNull
    public static PayerReminderDialog newPlayerReminderDialog(boolean z) {
        PayerReminderDialog payerReminderDialog = new PayerReminderDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_IS_ORIGINAL_HOST, z);
        payerReminderDialog.setArguments(bundle);
        return payerReminderDialog;
    }

    public PayerReminderDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        boolean z = arguments != null ? arguments.getBoolean(ARG_IS_ORIGINAL_HOST) : false;
        Builder cancelable = new Builder(getActivity()).setCancelable(true);
        if (z) {
            cancelable.setTitle(C4558R.string.zm_msg_meeting_player_reminder_for_host_title).setMessage(C4558R.string.zm_msg_meeting_player_reminder_for_host).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        } else {
            cancelable.setTitle(C4558R.string.zm_msg_meeting_player_reminder_for_attendee_title).setMessage(C4558R.string.zm_msg_meeting_player_reminder_for_attendee).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }
        return cancelable.create();
    }
}
