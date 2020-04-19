package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZmPtUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SwitchToJoinMeetingDialog extends ZMDialogFragment {
    private static final String ARG_MEETING_NUMBER = "meetingNumber";
    private static final String ARG_PASSWORD = "passWord";
    private static final String ARG_PERSONAL_LINK = "personalLink";

    @NonNull
    public static SwitchToJoinMeetingDialog newSwitchToJoinMeetingDialog(long j, String str, String str2) {
        SwitchToJoinMeetingDialog switchToJoinMeetingDialog = new SwitchToJoinMeetingDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("meetingNumber", j);
        bundle.putString(ARG_PERSONAL_LINK, str);
        bundle.putString(ARG_PASSWORD, str2);
        switchToJoinMeetingDialog.setArguments(bundle);
        return switchToJoinMeetingDialog;
    }

    public SwitchToJoinMeetingDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_switch_call_direct_share_97592).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SwitchToJoinMeetingDialog.this.onClickYes();
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void onClickYes() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            final long j = arguments.getLong("meetingNumber", 0);
            final String string = arguments.getString(ARG_PERSONAL_LINK);
            final String string2 = arguments.getString(ARG_PASSWORD);
            if (j != 0 || !StringUtil.isEmptyOrNull(string)) {
                PTApp.getInstance().forceSyncLeaveCurrentCall();
                PTApp.getInstance().dispatchIdleMessage();
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    final ZMActivity zMActivity2 = zMActivity;
                    C23563 r4 = new Runnable() {
                        public void run() {
                            if (PTApp.getInstance().hasActiveCall()) {
                                zMActivity2.getWindow().getDecorView().postDelayed(this, 100);
                            } else {
                                ZmPtUtils.joinMeeting(zMActivity2, j, string, string2);
                            }
                        }
                    };
                    zMActivity.getWindow().getDecorView().postDelayed(r4, 100);
                }
            }
        }
    }
}
