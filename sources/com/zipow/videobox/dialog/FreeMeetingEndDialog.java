package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ScheduleActivity;
import com.zipow.videobox.util.ZMDomainUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class FreeMeetingEndDialog extends ZMDialogFragment {
    private static final String ARG_FREE_MEETING_TIMES = "arg_free_meeting_times";
    private static final String ARG_UPGRADE_URL = "arg_upgrade_url";
    private boolean mIsNeedEndActivity = true;

    public static void showDialog(@Nullable FragmentManager fragmentManager, int i, @NonNull String str) {
        if (fragmentManager != null) {
            FreeMeetingEndDialog freeMeetingEndDialog = new FreeMeetingEndDialog();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_FREE_MEETING_TIMES, i);
            bundle.putString(ARG_UPGRADE_URL, str);
            freeMeetingEndDialog.setArguments(bundle);
            freeMeetingEndDialog.show(fragmentManager, FreeMeetingEndDialog.class.getName());
        }
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(FreeMeetingEndDialog.class.getName());
            if (zMDialogFragment != null) {
                zMDialogFragment.dismiss();
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        int i = arguments.getInt(ARG_FREE_MEETING_TIMES, -1);
        if (i <= 0) {
            return createEmptyDialog();
        }
        arguments.getString(ARG_UPGRADE_URL);
        Builder builder = new Builder(getActivity());
        if (i == 1) {
            builder.setCancelable(false).setTitle(C4558R.string.zm_title_upgrade_another_gift_45927).setMessage(C4558R.string.zm_msg_upgrade_first_end_free_meeting_45927).setPositiveButton(C4558R.string.zm_btn_schedule_now_45927, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    FreeMeetingEndDialog.this.onClickScheduleMeeting();
                }
            });
        } else if (i == 2) {
            builder.setCancelable(false).setMessage(getResources().getString(C4558R.string.zm_msg_upgrade_second_end_free_meeting_45927, new Object[]{ZMDomainUtil.getZmUrlWebServerWWW()})).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null);
        } else {
            builder.setCancelable(false).setTitle(C4558R.string.zm_title_upgrade_new_gift_45927).setMessage(getResources().getString(C4558R.string.zm_msg_upgrade_end_free_meeting_45927, new Object[]{ZMDomainUtil.getZmUrlWebServerWWW()})).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null);
        }
        ZMAlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.mIsNeedEndActivity) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickScheduleMeeting() {
        this.mIsNeedEndActivity = false;
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ScheduleActivity.show(zMActivity, 1000);
        }
    }
}
