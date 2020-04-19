package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class CloseOtherMeetingDialog extends ZMDialogFragment {
    public static void show(FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        CloseOtherMeetingDialog closeOtherMeetingDialog = new CloseOtherMeetingDialog();
        closeOtherMeetingDialog.setArguments(bundle);
        closeOtherMeetingDialog.show(fragmentManager, CloseOtherMeetingDialog.class.getName());
    }

    public CloseOtherMeetingDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_meeting_alert).setMessage(C4558R.string.zm_msg_conffail_single_meeting_restricted_confirm).setPositiveButton(C4558R.string.zm_btn_end_other_meeting, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (((ConfActivity) CloseOtherMeetingDialog.this.getActivity()) != null) {
                    ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                    ConfLocalHelper.endOtherMeeting();
                }
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivity confActivity = (ConfActivity) CloseOtherMeetingDialog.this.getActivity();
                if (confActivity != null) {
                    ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(17), true);
                    ConfLocalHelper.leaveCall(confActivity);
                }
            }
        }).create();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(17), true);
            ConfLocalHelper.leaveCall(confActivity);
        }
    }
}
