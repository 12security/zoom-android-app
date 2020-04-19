package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AlertFreeMeetingDialog extends ZMDialogFragment {
    private static final String KEY_IS_BASIC_USER = "KEY_IS_BASIC_USER";

    public static void showDialog(FragmentManager fragmentManager, boolean z) {
        AlertFreeMeetingDialog alertFreeMeetingDialog = new AlertFreeMeetingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_IS_BASIC_USER, z);
        alertFreeMeetingDialog.setArguments(bundle);
        alertFreeMeetingDialog.show(fragmentManager, AlertFreeMeetingDialog.class.getName());
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(AlertFreeMeetingDialog.class.getName());
            if (zMDialogFragment != null) {
                zMDialogFragment.dismiss();
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        boolean z = arguments != null ? arguments.getBoolean(KEY_IS_BASIC_USER, false) : false;
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(false).setTitle(z ? C4558R.string.zm_title_basic_user_upgrade_free_meeting_45927 : C4558R.string.zm_title_upgrade_third_time_30_minutes_45927).setMessage(z ? C4558R.string.zm_msg_basic_user_upgrade_end_free_meeting_45927 : C4558R.string.zm_msg_upgrade_free_meeting_45927).setCancelable(true).setPositiveButton(z ? C4558R.string.zm_btn_ok : C4558R.string.zm_btn_love_it_45772, (OnClickListener) null).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }
}
