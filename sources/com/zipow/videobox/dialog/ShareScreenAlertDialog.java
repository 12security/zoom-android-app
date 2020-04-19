package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ShareScreenAlertDialog extends ZMDialogFragment {
    @Nullable
    OnClickListener mClickListener = null;
    private int status = 0;

    public ShareScreenAlertDialog() {
        setCancelable(true);
    }

    public static void show(FragmentManager fragmentManager, String str, int i, OnClickListener onClickListener) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", i);
        ShareScreenAlertDialog shareScreenAlertDialog = new ShareScreenAlertDialog();
        shareScreenAlertDialog.setArguments(bundle);
        shareScreenAlertDialog.mClickListener = onClickListener;
        shareScreenAlertDialog.show(fragmentManager, str);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.status = arguments.getInt("status");
        Builder builder = new Builder(getActivity());
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_dailog_msg_txt_view, null, false);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtMsg)).setText(statusToMessage(this.status));
        builder.setView(inflate);
        builder.setNegativeButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ShareScreenAlertDialog.this.mClickListener != null) {
                    ShareScreenAlertDialog.this.mClickListener.onClick(dialogInterface, i);
                }
                ShareScreenAlertDialog.this.dismiss();
            }
        });
        ZMAlertDialog create = builder.create();
        create.setCanceledOnTouchOutside(false);
        return create;
    }

    @NonNull
    private String statusToMessage(int i) {
        String str = "";
        if (i == 35) {
            return getString(C4558R.string.zm_msg_enter_new_sharing_key_meeting_id_52777);
        }
        if (i != 40) {
            switch (i) {
                case 23:
                case 24:
                    return getString(C4558R.string.zm_msg_net_error_52777);
                case 25:
                case 26:
                    return getString(C4558R.string.zm_msg_enter_valid_sharing_key_meeting_id_52777);
                case 27:
                case 28:
                    break;
                default:
                    return str;
            }
        }
        return getString(C4558R.string.zm_msg_zr_version_is_too_old_52777);
    }
}
