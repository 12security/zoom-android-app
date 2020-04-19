package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.fragment.ConfCmrFragment;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class CmrFullStorageDialog extends ZMDialogFragment implements OnClickListener {
    public CmrFullStorageDialog() {
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent(), true).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private View createContent() {
        if (getActivity() == null) {
            return null;
        }
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_cmr_full_storage, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtSubTitle);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(C4558R.C4560id.llGoToWeb);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(C4558R.C4560id.llClose);
        linearLayout.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return inflate;
        }
        if (confContext.getOrginalHost()) {
            if (confContext.getMyRole() == 1 || confContext.getMyRole() == 0) {
                textView.setText(C4558R.string.zm_msg_cmr_storage_full_reminder_original_host_5537);
            } else {
                textView.setText(C4558R.string.f533xfcb6ab38);
            }
            linearLayout2.setBackgroundResource(C4558R.C4559drawable.zm_btn_dialog_bg);
            linearLayout.setVisibility(0);
        } else {
            textView.setText(C4558R.string.zm_msg_cmr_storage_full_reminder_attendee_5537);
            linearLayout2.setBackgroundResource(C4558R.C4559drawable.zm_btn_dialog_bg_bottom_corner);
            linearLayout.setVisibility(8);
        }
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            if (id == C4558R.C4560id.llGoToWeb) {
                dismissAllowingStateLoss();
                ConfCmrFragment.showAsActivity(confActivity, 0);
            } else if (id == C4558R.C4560id.llClose) {
                dismissAllowingStateLoss();
            }
        }
    }
}
