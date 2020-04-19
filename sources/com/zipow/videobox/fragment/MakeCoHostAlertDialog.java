package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MakeCoHostAlertDialog extends ZMDialogFragment {
    private static final String ARG_USERID = "userId";
    private long mUserId = 0;

    public static void showMakeCoHostAlertDialog(ZMActivity zMActivity, long j) {
        MakeCoHostAlertDialog makeCoHostAlertDialog = new MakeCoHostAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", j);
        makeCoHostAlertDialog.setArguments(bundle);
        makeCoHostAlertDialog.show(zMActivity.getSupportFragmentManager(), MakeCoHostAlertDialog.class.getName());
    }

    @Nullable
    public static MakeCoHostAlertDialog getMakeCoHostAlertDialog(FragmentManager fragmentManager) {
        return (MakeCoHostAlertDialog) fragmentManager.findFragmentByTag(MakeCoHostAlertDialog.class.getName());
    }

    public MakeCoHostAlertDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mUserId = arguments.getLong("userId");
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        if (userById == null) {
            this.mUserId = 0;
            return createEmptyDialog();
        }
        return new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_alert_change_cohost_confirm, userById.getScreenName())).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MakeCoHostAlertDialog.this.makeCoHost();
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        if (this.mUserId == 0) {
            dismiss();
        }
    }

    public long getUserId() {
        return this.mUserId;
    }

    /* access modifiers changed from: private */
    public void makeCoHost() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            long j = arguments.getLong("userId");
            if (ConfMgr.getInstance().getUserById(j) != null) {
                ConfMgr.getInstance().handleUserCmd(44, j);
            }
        }
    }
}
