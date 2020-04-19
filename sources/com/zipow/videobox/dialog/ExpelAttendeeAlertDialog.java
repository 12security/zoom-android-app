package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ExpelAttendeeAlertDialog extends ZMDialogFragment {
    private static final String ARG_ATTENDEE_ITEM = "attendee_item";
    @Nullable
    private ConfChatAttendeeItem mConfChatAttendeeItem;

    public static void showExpelAttendeeAlertDialog(@NonNull ZMActivity zMActivity, @NonNull ConfChatAttendeeItem confChatAttendeeItem) {
        ExpelAttendeeAlertDialog expelAttendeeAlertDialog = new ExpelAttendeeAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ATTENDEE_ITEM, confChatAttendeeItem);
        expelAttendeeAlertDialog.setArguments(bundle);
        expelAttendeeAlertDialog.show(zMActivity.getSupportFragmentManager(), ExpelAttendeeAlertDialog.class.getName());
    }

    @Nullable
    public static ExpelAttendeeAlertDialog getExpelAttendeeAlertDialog(FragmentManager fragmentManager) {
        return (ExpelAttendeeAlertDialog) fragmentManager.findFragmentByTag(ExpelAttendeeAlertDialog.class.getName());
    }

    public ExpelAttendeeAlertDialog() {
        setCancelable(true);
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
        this.mConfChatAttendeeItem = (ConfChatAttendeeItem) arguments.getSerializable(ARG_ATTENDEE_ITEM);
        if (this.mConfChatAttendeeItem == null) {
            return createEmptyDialog();
        }
        return new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_alert_expel_user_confirm_webinar_63825, this.mConfChatAttendeeItem.name, this.mConfChatAttendeeItem.name)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ExpelAttendeeAlertDialog.this.expelUser();
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        if (this.mConfChatAttendeeItem == null) {
            dismissAllowingStateLoss();
        }
    }

    @Nullable
    public ConfChatAttendeeItem getConfChatAttendeeItem() {
        return this.mConfChatAttendeeItem;
    }

    /* access modifiers changed from: private */
    public void expelUser() {
        ConfChatAttendeeItem confChatAttendeeItem = this.mConfChatAttendeeItem;
        if (confChatAttendeeItem != null && !StringUtil.isEmptyOrNull(confChatAttendeeItem.jid)) {
            ConfMgr.getInstance().expelAttendee(this.mConfChatAttendeeItem.jid);
        }
    }
}
