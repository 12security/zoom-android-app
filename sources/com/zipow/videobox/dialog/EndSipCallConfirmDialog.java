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
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class EndSipCallConfirmDialog extends ZMDialogFragment {
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
            EndSipCallConfirmDialog endSipCallConfirmDialog = new EndSipCallConfirmDialog();
            endSipCallConfirmDialog.setOnButtonClickListener(onButtonClickListener2);
            endSipCallConfirmDialog.show(supportFragmentManager, EndSipCallConfirmDialog.class.getName());
        }
    }

    public static void checkExistingSipCallAndIfNeedShow(@NonNull Context context, @NonNull OnButtonClickListener onButtonClickListener2) {
        if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            show(context, onButtonClickListener2);
        } else {
            onButtonClickListener2.onPositiveClick();
        }
    }

    public static void hide(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            EndSipCallConfirmDialog endSipCallConfirmDialog = (EndSipCallConfirmDialog) fragmentManager.findFragmentByTag(EndSipCallConfirmDialog.class.getName());
            if (endSipCallConfirmDialog != null) {
                endSipCallConfirmDialog.dismiss();
            }
        }
    }

    public EndSipCallConfirmDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setCancelable(false).setTitle(C4558R.string.zm_sip_incall_start_meeting_diallog_title_85332).setMessage(C4558R.string.zm_sip_incall_start_meeting_diallog_msg_85332).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EndSipCallConfirmDialog.this.onButtonClickListener != null) {
                    EndSipCallConfirmDialog.this.onButtonClickListener.onNegativeClick();
                }
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (EndSipCallConfirmDialog.this.onButtonClickListener != null) {
                    CmmSIPCallManager.getInstance().hangupAllCalls();
                    EndSipCallConfirmDialog.this.onButtonClickListener.onPositiveClick();
                }
            }
        }).create();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener2) {
        this.onButtonClickListener = onButtonClickListener2;
    }
}
