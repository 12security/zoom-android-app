package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ConfAllowTalkDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public void stayMuted() {
    }

    public static void showConfAllowTalkDialog(ZMActivity zMActivity) {
        new ConfAllowTalkDialog().show(zMActivity.getSupportFragmentManager(), ConfAllowTalkDialog.class.getName());
    }

    public static void dismiss(@NonNull FragmentManager fragmentManager) {
        ConfAllowTalkDialog confAllowTalkDialog = getConfAllowTalkDialog(fragmentManager);
        if (confAllowTalkDialog != null) {
            confAllowTalkDialog.dismiss();
        }
    }

    @Nullable
    private static ConfAllowTalkDialog getConfAllowTalkDialog(FragmentManager fragmentManager) {
        return (ConfAllowTalkDialog) fragmentManager.findFragmentByTag(ConfAllowTalkDialog.class.getName());
    }

    public ConfAllowTalkDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_title_host_allow_talk_15294).setPositiveButton(C4558R.string.zm_btn_unmute_now_15294, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfAllowTalkDialog.this.unmuteNow();
            }
        }).setNegativeButton(C4558R.string.zm_btn_stay_muted_15294, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfAllowTalkDialog.this.stayMuted();
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void unmuteNow() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            confActivity.onHostAskUnmute();
        }
    }
}
