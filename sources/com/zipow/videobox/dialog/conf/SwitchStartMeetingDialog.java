package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SwitchStartMeetingDialog extends ZMDialogFragment {
    private static final String ARG_MEETING_ID = "meetingId";
    private static final String ARG_MEETING_NUMBER = "meetingNumber";

    @NonNull
    public static SwitchStartMeetingDialog newSwitchStartMeetingDialog(long j, String str) {
        SwitchStartMeetingDialog switchStartMeetingDialog = new SwitchStartMeetingDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("meetingNumber", j);
        bundle.putString("meetingId", str);
        switchStartMeetingDialog.setArguments(bundle);
        return switchStartMeetingDialog;
    }

    public SwitchStartMeetingDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_switch_start_meeting).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SwitchStartMeetingDialog.this.onClickYes();
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void onClickYes() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            final long j = arguments.getLong("meetingNumber", 0);
            final String string = arguments.getString("meetingId");
            if (j != 0 || !StringUtil.isEmptyOrNull(string)) {
                PTApp.getInstance().forceSyncLeaveCurrentCall();
                PTApp.getInstance().dispatchIdleMessage();
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    final ZMActivity zMActivity2 = zMActivity;
                    C24153 r4 = new Runnable() {
                        public void run() {
                            if (PTApp.getInstance().hasActiveCall()) {
                                zMActivity2.getWindow().getDecorView().postDelayed(this, 100);
                            } else if (ConfActivity.startMeeting(zMActivity2, j, string)) {
                                ZMConfEventTracking.logStartMeetingInShortCut(j);
                            }
                        }
                    };
                    zMActivity.getWindow().getDecorView().postDelayed(r4, 100);
                }
            }
        }
    }
}
