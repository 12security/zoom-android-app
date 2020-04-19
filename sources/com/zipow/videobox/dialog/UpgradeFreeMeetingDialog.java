package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.util.ZMDomainUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class UpgradeFreeMeetingDialog extends ZMDialogFragment {
    public static void showDialog(FragmentManager fragmentManager) {
        new UpgradeFreeMeetingDialog().show(fragmentManager, UpgradeFreeMeetingDialog.class.getName());
    }

    public static void dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(UpgradeFreeMeetingDialog.class.getName());
            if (zMDialogFragment != null) {
                zMDialogFragment.dismiss();
            }
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setTitle(C4558R.string.zm_title_upgrade_third_time_30_minutes_45927).setMessage(getResources().getString(C4558R.string.zm_msg_upgrade_third_time_30_minutes_desc_45927, new Object[]{ZMDomainUtil.getZmUrlWebServerWWW()})).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }
}
