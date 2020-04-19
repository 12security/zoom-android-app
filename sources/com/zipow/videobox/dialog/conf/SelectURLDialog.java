package com.zipow.videobox.dialog.conf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.dialog.ShareAlertDialog;
import com.zipow.videobox.util.ZMDomainUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SelectURLDialog extends ZMDialogFragment {
    public static void show(FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        SelectURLDialog selectURLDialog = new SelectURLDialog();
        selectURLDialog.setArguments(bundle);
        selectURLDialog.show(fragmentManager, SelectURLDialog.class.getName());
    }

    public SelectURLDialog() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_inputurl, null, false);
        final EditText editText = (EditText) inflate.findViewById(C4558R.C4560id.inputurl);
        Builder negativeButton = new Builder(getActivity()).setTitle(C4558R.string.zm_btn_share_url).setView(inflate).setPositiveButton(C4558R.string.zm_btn_share, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String obj = editText.getText().toString();
                ConfActivityNormal confActivityNormal = (ConfActivityNormal) SelectURLDialog.this.getActivity();
                if ("".equals(obj.trim())) {
                    if (confActivityNormal != null) {
                        ShareAlertDialog.showConfDialog(confActivityNormal, confActivityNormal.getSupportFragmentManager(), C4558R.string.zm_alert_invlid_url, true);
                    }
                    return;
                }
                if (!obj.startsWith(ZMDomainUtil.ZM_URL_HTTP) && !obj.startsWith("https://")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ZMDomainUtil.ZM_URL_HTTP);
                    sb.append(obj);
                    obj = sb.toString();
                }
                ZMConfComponentMgr.getInstance().startShareWebview(obj);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMConfComponentMgr.getInstance().stopShare();
            }
        });
        negativeButton.create().setCanceledOnTouchOutside(false);
        return negativeButton.create();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        ZMConfComponentMgr.getInstance().stopShare();
    }
}
