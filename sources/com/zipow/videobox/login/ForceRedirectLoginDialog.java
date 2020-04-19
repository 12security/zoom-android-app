package com.zipow.videobox.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.LoginActivity;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ForceRedirectLoginDialog extends ZMDialogFragment implements OnClickListener {
    private static final String ARG_MODE = "mode";
    public static final int MODE_GOOGLE = 1;
    public static final int MODE_SSO = 2;
    private int mMode = 1;

    public static ForceRedirectLoginDialog newInstance(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_MODE, i);
        ForceRedirectLoginDialog forceRedirectLoginDialog = new ForceRedirectLoginDialog();
        forceRedirectLoginDialog.setArguments(bundle);
        forceRedirectLoginDialog.setCancelable(true);
        return forceRedirectLoginDialog;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mMode = arguments.getInt(ARG_MODE);
        }
        ZMAlertDialog create = new Builder(getActivity()).setCancelable(true).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setView(createContent(), true).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    @Nullable
    private View createContent() {
        if (getActivity() == null) {
            return null;
        }
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.zm_login_force_redirect, null);
        inflate.findViewById(C4558R.C4560id.llRedirect).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.llCancel).setOnClickListener(this);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtSubTitle);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtOk);
        switch (this.mMode) {
            case 1:
                textView.setText(C4558R.string.zm_title_login_with_google_13762);
                textView2.setText(C4558R.string.zm_alert_login_with_google_13762);
                textView3.setText(C4558R.string.zm_title_login_with_google);
                break;
            case 2:
                textView.setText(C4558R.string.zm_title_login_with_sso_13762);
                textView2.setText(C4558R.string.zm_alert_login_with_sso_13762);
                textView3.setText(C4558R.string.zm_btn_login_with_sso_13762);
                break;
        }
        return inflate;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (((LoginActivity) getActivity()) != null) {
            if (id == C4558R.C4560id.llRedirect) {
                ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
                if (zmInternationalMultiLogin != null) {
                    dismissAllowingStateLoss();
                    switch (this.mMode) {
                        case 1:
                            zmInternationalMultiLogin.onClickBtnLoginGoogle();
                            break;
                        case 2:
                            zmInternationalMultiLogin.onClickBtnLoginSSO();
                            break;
                    }
                }
            } else if (id == C4558R.C4560id.llCancel) {
                dismissAllowingStateLoss();
            }
        }
    }
}
