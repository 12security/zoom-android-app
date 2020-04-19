package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SwitchCallDialog extends ZMDialogFragment {
    public static final String ARG_IS_START = "isStart";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_URL_ACTION = "urlAction";

    public SwitchCallDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        final String string = arguments.getString("screenName");
        final String string2 = arguments.getString("urlAction");
        return new Builder(getActivity()).setTitle(arguments.getBoolean("isStart", false) ? C4558R.string.zm_alert_switch_call_start : C4558R.string.zm_alert_switch_call).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivity confActivity = (ConfActivity) SwitchCallDialog.this.getActivity();
                if (confActivity != null) {
                    confActivity.switchCall(string2, string);
                }
            }
        }).create();
    }
}
