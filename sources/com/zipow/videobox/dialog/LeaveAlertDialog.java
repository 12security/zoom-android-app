package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class LeaveAlertDialog extends ZMDialogFragment implements OnCancelListener {
    private static final String TAG = "com.zipow.videobox.dialog.LeaveAlertDialog";

    public LeaveAlertDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_Transparent).setView(createContent()).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_conf_leave_menu, null);
        View findViewById = inflate.findViewById(C4558R.C4560id.panelEndMeeting);
        View findViewById2 = inflate.findViewById(C4558R.C4560id.panelLeaveMeeting);
        View findViewById3 = inflate.findViewById(C4558R.C4560id.panelLeaveWithCall);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtLeavePromt);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtLeaveMeeting);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtEndMeeting);
        if (textView2 != null) {
            textView2.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_btn_leave_webinar_150183 : C4558R.string.zm_btn_leave_conference);
        }
        if (textView3 != null) {
            textView3.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_btn_end_webinar_150183 : C4558R.string.zm_btn_end_conference);
        }
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ConfActivity confActivity = (ConfActivity) LeaveAlertDialog.this.getActivity();
                if (confActivity != null) {
                    IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.LOG_MEETING.ordinal(), 46);
                    ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                    ConfLocalHelper.endCall(confActivity);
                }
            }
        });
        findViewById2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ConfActivity confActivity = (ConfActivity) LeaveAlertDialog.this.getActivity();
                if (confActivity != null) {
                    IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.LOG_MEETING.ordinal(), 45);
                    if (ConfLocalHelper.isDirectShareClient()) {
                        PTAppDelegation.getInstance().stopPresentToRoom(true);
                        return;
                    }
                    ConfLocalHelper.leaveCallWithNotify(confActivity);
                }
            }
        });
        findViewById3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (((ConfActivity) LeaveAlertDialog.this.getActivity()) != null) {
                    int confStatus = ConfMgr.getInstance().getConfStatus();
                    if (confStatus == 8 || confStatus == 9) {
                        ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(8), true);
                    } else {
                        ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                    }
                    ConfMgr.getInstance().handleConfCmd(52);
                }
            }
        });
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (!ConfLocalHelper.isHost()) {
            textView.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_alert_leave_webinar_150183 : C4558R.string.zm_alert_leave_conf);
            findViewById.setVisibility(8);
        } else if (PreferenceUtil.readBooleanValue(PreferenceUtil.NO_LEAVE_MEETING_BUTTON_FOR_HOST, false)) {
            textView.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_alert_confirm_end_webinar_150183 : C4558R.string.zm_alert_confirm_end_conf);
            findViewById2.setVisibility(8);
        } else {
            textView.setText(ConfLocalHelper.isWebinar() ? C4558R.string.zm_alert_end_webinar_150183 : C4558R.string.zm_alert_end_conf);
        }
        long j = 0;
        if (myself != null) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                j = audioStatusObj.getAudiotype();
            }
        }
        if (j != 1) {
            findViewById3.setVisibility(8);
        }
        return inflate;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.LOG_MEETING.ordinal(), 47);
    }
}
