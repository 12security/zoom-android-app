package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.WelcomeActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.LogoutHandler.IListener;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMNetErrorDialog extends ZMDialogFragment {
    private static final String KEY_TYPE = "TYPE";
    private int mType;

    public static void showDialog(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TYPE, Integer.valueOf(i));
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (shouldShow(supportFragmentManager, ZMNetErrorDialog.class.getSimpleName(), null)) {
            ZMNetErrorDialog zMNetErrorDialog = new ZMNetErrorDialog();
            zMNetErrorDialog.setArguments(bundle);
            zMNetErrorDialog.showNow(supportFragmentManager, ZMNetErrorDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mType = ((Integer) getArguments().getSerializable(KEY_TYPE)).intValue();
        ZMAlertDialog create = new Builder(getActivity()).setMessage(C4558R.string.zm_alert_net_failed_133459).setCancelable(false).setVerticalOptionStyle(true).setNegativeButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMNetErrorDialog.this.onClickOK();
            }
        }).create();
        create.setCanceledOnTouchOutside(false);
        create.show();
        return create;
    }

    /* access modifiers changed from: private */
    public void onClickOK() {
        final FragmentActivity activity = getActivity();
        switch (this.mType) {
            case 1:
                if (activity instanceof ZMActivity) {
                    LogoutHandler.getInstance().startLogout((ZMActivity) activity, new IListener() {
                        public void afterLogout() {
                            WelcomeActivity.show(activity, false, false);
                            activity.finish();
                        }
                    });
                    return;
                }
                return;
            case 2:
                if (activity instanceof ConfActivity) {
                    ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
                    ConfLocalHelper.leaveCall((ConfActivity) activity);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void dismiss() {
        finishFragment(false);
    }
}
