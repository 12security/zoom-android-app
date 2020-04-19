package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MeetingInSipCallConfirmDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public OnButtonClickListener onButtonClickListener;

    public interface OnButtonClickListener {
        void onNegativeClick();

        void onPositiveClick();
    }

    public static abstract class SimpleOnButtonClickListener implements OnButtonClickListener {
        public void onNegativeClick() {
        }
    }

    public static void show(@NonNull Context context, @NonNull OnButtonClickListener onButtonClickListener2) {
        FragmentManager supportFragmentManager = context instanceof ZMActivity ? ((ZMActivity) context).getSupportFragmentManager() : null;
        if (supportFragmentManager != null) {
            MeetingInSipCallConfirmDialog meetingInSipCallConfirmDialog = new MeetingInSipCallConfirmDialog();
            meetingInSipCallConfirmDialog.setOnButtonClickListener(onButtonClickListener2);
            meetingInSipCallConfirmDialog.show(supportFragmentManager, MeetingInSipCallConfirmDialog.class.getName());
        }
    }

    public static void checkExistingSipCallAndIfNeedShow(@NonNull Context context, @NonNull OnButtonClickListener onButtonClickListener2) {
        if (!CmmSIPCallManager.getInstance().hasSipCallsInCache() || CmmSIPCallManager.getInstance().hasMeetings()) {
            onButtonClickListener2.onPositiveClick();
        } else {
            show(context, onButtonClickListener2);
        }
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            MeetingInSipCallConfirmDialog meetingInSipCallConfirmDialog = (MeetingInSipCallConfirmDialog) fragmentManager.findFragmentByTag(MeetingInSipCallConfirmDialog.class.getName());
            if (meetingInSipCallConfirmDialog != null) {
                meetingInSipCallConfirmDialog.dismiss();
            }
        }
    }

    public MeetingInSipCallConfirmDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setCancelable(false).setTitle(C4558R.string.zm_sip_incall_start_meeting_dialog_title_108086).setMessage(C4558R.string.zm_sip_incall_start_meeting_dialog_msg_108086).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (MeetingInSipCallConfirmDialog.this.onButtonClickListener != null) {
                    MeetingInSipCallConfirmDialog.this.onButtonClickListener.onNegativeClick();
                }
            }
        }).setPositiveButton(C4558R.string.zm_btn_continue, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                CmmSipAudioMgr.getInstance().disablePhoneAudio();
                if (MeetingInSipCallConfirmDialog.this.onButtonClickListener != null) {
                    MeetingInSipCallConfirmDialog.this.onButtonClickListener.onPositiveClick();
                }
            }
        }).create();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener2) {
        this.onButtonClickListener = onButtonClickListener2;
    }
}
