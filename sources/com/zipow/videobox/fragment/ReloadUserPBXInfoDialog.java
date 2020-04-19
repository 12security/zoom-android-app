package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ReloadUserPBXInfoDialog extends ZMDialogFragment {
    @Nullable
    public static ReloadUserPBXInfoDialog getErrorMsgActionDialog(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null) {
            return null;
        }
        return (ReloadUserPBXInfoDialog) fragmentActivity.getSupportFragmentManager().findFragmentByTag(ReloadUserPBXInfoDialog.class.getName());
    }

    @Nullable
    public static ReloadUserPBXInfoDialog show(ZMActivity zMActivity) {
        if (zMActivity == null) {
            return null;
        }
        ReloadUserPBXInfoDialog reloadUserPBXInfoDialog = new ReloadUserPBXInfoDialog();
        reloadUserPBXInfoDialog.show(zMActivity.getSupportFragmentManager(), ReloadUserPBXInfoDialog.class.getName());
        return reloadUserPBXInfoDialog;
    }

    public static void hide(ZMActivity zMActivity) {
        if (zMActivity != null) {
            ReloadUserPBXInfoDialog errorMsgActionDialog = getErrorMsgActionDialog(zMActivity);
            if (errorMsgActionDialog != null) {
                errorMsgActionDialog.dismiss();
            }
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_sip_user_info_invalid_title_82865).setMessage(C4558R.string.zm_sip_user_info_invalid_message_82865).setNegativeButton(C4558R.string.zm_sip_user_info_invalid_dismiss_82865, (OnClickListener) null).setPositiveButton(C4558R.string.zm_sip_user_info_invalid_retry_82865, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                CmmSIPCallManager.getInstance().queryUserPbxInfo();
            }
        }).create();
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }
}
