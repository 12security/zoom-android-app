package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ExpelUserAlertDialog extends ZMDialogFragment {
    private static final String ARG_USERID = "userId";
    private long mUserId = 0;

    public static void showExpelUserAlertDialog(ZMActivity zMActivity, long j) {
        ExpelUserAlertDialog expelUserAlertDialog = new ExpelUserAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", j);
        expelUserAlertDialog.setArguments(bundle);
        expelUserAlertDialog.show(zMActivity.getSupportFragmentManager(), ExpelUserAlertDialog.class.getName());
    }

    @Nullable
    public static ExpelUserAlertDialog getExpelUserAlertDialog(FragmentManager fragmentManager) {
        return (ExpelUserAlertDialog) fragmentManager.findFragmentByTag(ExpelUserAlertDialog.class.getName());
    }

    public ExpelUserAlertDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        boolean z;
        Object[] objArr;
        int i;
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.mUserId = arguments.getLong("userId");
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            z = false;
        } else {
            z = confContext.isAllowUserRejoinAfterRemove();
        }
        if (userById == null) {
            this.mUserId = 0;
            return createEmptyDialog();
        }
        if (z) {
            i = C4558R.string.zm_alert_expel_user_confirm_63825;
            objArr = new Object[]{userById.getScreenName()};
        } else {
            i = C4558R.string.zm_alert_expel_user_confirm_meeting_63825;
            objArr = new Object[]{userById.getScreenName(), userById.getScreenName()};
        }
        return new Builder(getActivity()).setTitle((CharSequence) getString(i, objArr)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ExpelUserAlertDialog.this.expelUser();
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        if (this.mUserId == 0) {
            dismissAllowingStateLoss();
        }
    }

    public long getUserId() {
        return this.mUserId;
    }

    /* access modifiers changed from: private */
    public void expelUser() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            long j = arguments.getLong("userId");
            if (ConfMgr.getInstance().getUserById(j) != null) {
                ConfMgr.getInstance().handleUserCmd(30, j);
            }
        }
    }
}
