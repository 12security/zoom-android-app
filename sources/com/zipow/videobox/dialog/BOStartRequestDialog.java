package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.meeting.confhelper.BOComponent;
import com.zipow.videobox.confapp.p009bo.BOUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class BOStartRequestDialog extends ZMDialogFragment {
    private static final String ARG_BOOBJECT_BID = "boobject_bid";

    public static void showDialog(FragmentManager fragmentManager, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            BOStartRequestDialog bOStartRequestDialog = new BOStartRequestDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_BOOBJECT_BID, str);
            bOStartRequestDialog.setArguments(bundle);
            bOStartRequestDialog.show(fragmentManager, BOStartRequestDialog.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        final String string = arguments.getString(ARG_BOOBJECT_BID);
        String bOMeetingNameByBid = BOUtil.getBOMeetingNameByBid(string);
        return new Builder(getActivity()).setCancelable(true).setMessage(getString(C4558R.string.zm_bo_msg_start_request, bOMeetingNameByBid)).setNegativeButton(C4558R.string.zm_btn_later, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivity confActivity = (ConfActivity) BOStartRequestDialog.this.getActivity();
                if (confActivity != null) {
                    BOComponent bOComponent = confActivity.getmBOComponent();
                    if (bOComponent != null) {
                        bOComponent.pendingBOStartRequest();
                    }
                }
            }
        }).setPositiveButton(C4558R.string.zm_bo_btn_join_bo, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ConfActivity confActivity = (ConfActivity) BOStartRequestDialog.this.getActivity();
                if (confActivity != null) {
                    BOComponent bOComponent = confActivity.getmBOComponent();
                    if (bOComponent != null) {
                        bOComponent.joinBO(string);
                    }
                }
            }
        }).create();
    }

    public void dismiss() {
        super.dismiss();
    }
}
