package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMWebPageUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ZMHtmlUtil;
import p021us.zoom.androidlib.util.ZMHtmlUtil.OnURLSpanClickListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMRealNameConfirmDialog extends ZMDialogFragment {
    public void onClickBackPressed() {
    }

    public static void showDialog(@NonNull ZMActivity zMActivity) {
        new ZMRealNameConfirmDialog().show(zMActivity.getSupportFragmentManager(), ZMRealNameConfirmDialog.class.getName());
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Builder view = new Builder(getActivity()).setView(createContent());
        view.setCancelable(true);
        view.setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(@Nullable DialogInterface dialogInterface, int i) {
                ZMRealNameConfirmDialog.this.onClickPositiveBtn();
                if (dialogInterface != null) {
                    dialogInterface.cancel();
                }
            }
        });
        ZMAlertDialog create = view.create();
        create.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i != 4) {
                    return false;
                }
                ZMRealNameConfirmDialog.this.onClickBackPressed();
                return true;
            }
        });
        create.setCanceledOnTouchOutside(false);
        create.show();
        return create;
    }

    private View createContent() {
        String str;
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_real_name_layout, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtMsgContent);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        if (PTAppDelegation.getInstance().isWebSignedOn()) {
            str = getString(C4558R.string.zm_msg_real_name_confirm_signin_88890, ConfMgr.getInstance().getBindPhoneUrlForRealNameAuth());
        } else {
            str = getString(C4558R.string.zm_msg_real_name_confirm_none_signin_88890, ConfMgr.getInstance().getBindPhoneUrlForRealNameAuth(), ConfMgr.getInstance().getSignUpUrlForRealNameAuth());
        }
        textView.setText(ZMHtmlUtil.fromHtml(getContext(), str, new OnURLSpanClickListener() {
            public void onClick(View view, String str, String str2) {
                ZMWebPageUtil.startWebPage((Fragment) ZMRealNameConfirmDialog.this, str, str2);
            }
        }));
        return inflate;
    }

    /* access modifiers changed from: private */
    public void onClickPositiveBtn() {
        FragmentActivity activity = getActivity();
        if (activity instanceof ConfActivity) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
            ConfLocalHelper.leaveCall((ConfActivity) activity);
        }
    }

    public void dismiss() {
        finishFragment(false);
    }
}
