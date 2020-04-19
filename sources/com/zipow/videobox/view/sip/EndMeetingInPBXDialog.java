package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.DialogUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class EndMeetingInPBXDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public ButtonClickListener mButtonClickListener;

    public interface ButtonClickListener {
        void onNegativeClick();

        void onNeutralClick();

        void onPositiveClick();
    }

    public static void show(Context context, ButtonClickListener buttonClickListener) {
        FragmentManager supportFragmentManager = context instanceof ZMActivity ? ((ZMActivity) context).getSupportFragmentManager() : null;
        if (supportFragmentManager != null) {
            EndMeetingInPBXDialog endMeetingInPBXDialog = new EndMeetingInPBXDialog();
            endMeetingInPBXDialog.setButtonClickListener(buttonClickListener);
            endMeetingInPBXDialog.show(supportFragmentManager, EndMeetingInPBXDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        ZMAlertDialog zMAlertDialog;
        if (isMeetingHost()) {
            zMAlertDialog = getHostDialog();
        } else {
            zMAlertDialog = getParticipantDialog();
        }
        if (zMAlertDialog != null) {
            return zMAlertDialog;
        }
        return super.onCreateDialog(bundle);
    }

    private ZMAlertDialog getHostDialog() {
        if (getActivity() == null) {
            return null;
        }
        return DialogUtils.createVerticalActionDialog(getActivity(), getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), getString(C4558R.string.zm_sip_meet_inmeeting_dialog_msg_108086), getString(C4558R.string.zm_sip_meet_inmeeting_dialog_leave_108086), new OnClickListener() {
            public void onClick(View view) {
                CmmSIPCallManager.getInstance().leaveMeeting();
                if (EndMeetingInPBXDialog.this.mButtonClickListener != null) {
                    EndMeetingInPBXDialog.this.mButtonClickListener.onPositiveClick();
                }
                EndMeetingInPBXDialog.this.dismiss();
            }
        }, getString(C4558R.string.zm_sip_meet_inmeeting_dialog_end_108086), new OnClickListener() {
            public void onClick(View view) {
                CmmSIPCallManager.getInstance().endMeeting();
                if (EndMeetingInPBXDialog.this.mButtonClickListener != null) {
                    EndMeetingInPBXDialog.this.mButtonClickListener.onNeutralClick();
                }
                EndMeetingInPBXDialog.this.dismiss();
            }
        }, getString(C4558R.string.zm_btn_cancel), new OnClickListener() {
            public void onClick(View view) {
                if (EndMeetingInPBXDialog.this.mButtonClickListener != null) {
                    EndMeetingInPBXDialog.this.mButtonClickListener.onNegativeClick();
                }
                EndMeetingInPBXDialog.this.dismiss();
            }
        });
    }

    private ZMAlertDialog getParticipantDialog() {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_sip_callpeer_inmeeting_title_108086).setMessage(C4558R.string.zm_sip_meet_inmeeting_participant_dialog_msg_108086).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_continue, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                CmmSIPCallManager.getInstance().leaveMeeting();
                if (EndMeetingInPBXDialog.this.mButtonClickListener != null) {
                    EndMeetingInPBXDialog.this.mButtonClickListener.onPositiveClick();
                }
                EndMeetingInPBXDialog.this.dismiss();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EndMeetingInPBXDialog.this.mButtonClickListener != null) {
                    EndMeetingInPBXDialog.this.mButtonClickListener.onNegativeClick();
                }
                EndMeetingInPBXDialog.this.dismiss();
            }
        }).create();
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.mButtonClickListener = buttonClickListener;
    }

    private boolean isMeetingHost() {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService == null) {
            return false;
        }
        try {
            return confService.isCurrentMeetingHost();
        } catch (RemoteException unused) {
            return false;
        }
    }
}
