package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.JoinByURLActivity;
import com.zipow.videobox.util.UpgradeUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ForceUpdateDialogFragment extends ZMDialogFragment {
    public static void show(FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        ForceUpdateDialogFragment forceUpdateDialogFragment = new ForceUpdateDialogFragment();
        forceUpdateDialogFragment.setArguments(bundle);
        forceUpdateDialogFragment.show(fragmentManager, ForceUpdateDialogFragment.class.getName());
    }

    public ForceUpdateDialogFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_alert_start_conf_failed).setMessage(getResources().getString(C4558R.string.zm_msg_conffail_needupdate_confirm)).setPositiveButton(C4558R.string.zm_btn_update, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ForceUpdateDialogFragment.this.updateClient();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void updateClient() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            UpgradeUtil.upgrade(zMActivity);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (isRemoving() && (getActivity() instanceof JoinByURLActivity)) {
            getActivity().finish();
        }
    }
}
