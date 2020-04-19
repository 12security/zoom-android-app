package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ConfirmStopRecordDialog extends ZMDialogFragment {
    public static void showConfirmStopRecordDialog(ZMActivity zMActivity) {
        new ConfirmStopRecordDialog().show(zMActivity.getSupportFragmentManager(), ConfirmStopRecordDialog.class.getName());
    }

    public static void dismiss(@NonNull FragmentManager fragmentManager) {
        ConfirmStopRecordDialog confirmStopRecordDialog = getConfirmStopRecordDialog(fragmentManager);
        if (confirmStopRecordDialog != null) {
            confirmStopRecordDialog.dismiss();
        }
    }

    @Nullable
    private static ConfirmStopRecordDialog getConfirmStopRecordDialog(FragmentManager fragmentManager) {
        return (ConfirmStopRecordDialog) fragmentManager.findFragmentByTag(ConfirmStopRecordDialog.class.getName());
    }

    public ConfirmStopRecordDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setTitle(C4558R.string.zm_title_confim_stop_record_25514).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfLocalHelper.stopRecord(false);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }
}
