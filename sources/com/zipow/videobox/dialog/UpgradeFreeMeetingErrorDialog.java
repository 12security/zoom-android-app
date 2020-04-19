package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class UpgradeFreeMeetingErrorDialog extends ZMDialogFragment {
    private static final String ARG_ERROR_CODE = "arg_error_code";

    public static void showDialog(FragmentManager fragmentManager, int i) {
        UpgradeFreeMeetingErrorDialog upgradeFreeMeetingErrorDialog = new UpgradeFreeMeetingErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ERROR_CODE, i);
        upgradeFreeMeetingErrorDialog.setArguments(bundle);
        upgradeFreeMeetingErrorDialog.show(fragmentManager, UpgradeFreeMeetingErrorDialog.class.getName());
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(UpgradeFreeMeetingErrorDialog.class.getName());
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
        int i = arguments.getInt(ARG_ERROR_CODE, 0);
        return new Builder(getActivity()).setCancelable(true).setTitle(C4558R.string.zm_msg_upgrade_free_meeting_failed_title_15609).setMessage(getString(C4558R.string.zm_msg_upgrade_free_meeting_failed_15609, Integer.valueOf(i))).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }
}
