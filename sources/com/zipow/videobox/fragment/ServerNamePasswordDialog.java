package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ServerNamePasswordDialog extends ZMDialogFragment implements TextWatcher {
    private static final String ARG_FINISH_ACTIVITY_ON_DISMISS = "finishActivityOnDismiss";
    private static final String ARG_HANDLE_WEBVIEW = "handleWebView";
    private static final String ARG_IS_PROXY_SERVER = "isProxyServer";
    private static final String ARG_PORT = "port";
    private static final String ARG_SERVER = "server";
    private Button mBtnOK = null;
    private EditText mEdtPassword = null;
    private EditText mEdtUserName = null;
    private boolean mFinishActivityOnDismiss = false;
    private String mHost;
    @Nullable
    private HttpAuthHandler mHttpAuthHandler;
    private boolean mIsProxyServer = true;
    private int mPort = 0;
    private String mRealm;
    @Nullable
    private String mServer = "";
    private WebView mWebView;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @NonNull
    public static ServerNamePasswordDialog newInstance(String str, int i, boolean z, boolean z2) {
        return newInstance(str, i, z, z2, null, null, null, null);
    }

    @NonNull
    public static ServerNamePasswordDialog newInstance(String str, int i, boolean z, boolean z2, String str2, String str3, WebView webView, @Nullable HttpAuthHandler httpAuthHandler) {
        ServerNamePasswordDialog serverNamePasswordDialog = new ServerNamePasswordDialog();
        Bundle bundle = new Bundle();
        bundle.putString("server", str);
        bundle.putInt("port", i);
        bundle.putBoolean(ARG_IS_PROXY_SERVER, z);
        bundle.putBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS, z2);
        if (httpAuthHandler != null) {
            bundle.putBoolean(ARG_HANDLE_WEBVIEW, true);
        }
        serverNamePasswordDialog.setArguments(bundle);
        serverNamePasswordDialog.mHttpAuthHandler = httpAuthHandler;
        serverNamePasswordDialog.mWebView = webView;
        serverNamePasswordDialog.mHost = str2;
        serverNamePasswordDialog.mRealm = str3;
        return serverNamePasswordDialog;
    }

    public ServerNamePasswordDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        int i;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mServer = arguments.getString("server");
            this.mPort = arguments.getInt("port");
            this.mIsProxyServer = arguments.getBoolean(ARG_IS_PROXY_SERVER);
            this.mFinishActivityOnDismiss = arguments.getBoolean(ARG_FINISH_ACTIVITY_ON_DISMISS);
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_proxy_user_password, null, false);
        this.mEdtUserName = (EditText) inflate.findViewById(C4558R.C4560id.edtUserName);
        this.mEdtPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtPassword);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtInstructions);
        if (this.mIsProxyServer) {
            int i2 = C4558R.string.zm_lbl_proxy_name_password_instructions;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mServer);
            sb.append(":");
            sb.append(this.mPort);
            textView.setText(getString(i2, sb.toString()));
            i = C4558R.string.zm_title_proxy_settings;
        } else {
            textView.setText(getString(C4558R.string.zm_lbl_server_name_password_instructions, this.mServer));
            i = C4558R.string.zm_title_login;
        }
        this.mEdtUserName.addTextChangedListener(this);
        this.mEdtPassword.addTextChangedListener(this);
        return new Builder(getActivity()).setTitle(i).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.getBoolean(ARG_HANDLE_WEBVIEW) || this.mHttpAuthHandler != null) {
            this.mBtnOK = ((ZMAlertDialog) getDialog()).getButton(-1);
            Button button = this.mBtnOK;
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ServerNamePasswordDialog.this.checkInput()) {
                            ServerNamePasswordDialog.this.onOK();
                        }
                    }
                });
            }
            updateButtons();
            return;
        }
        getDialog().cancel();
    }

    public void onDestroy() {
        HttpAuthHandler httpAuthHandler = this.mHttpAuthHandler;
        if (httpAuthHandler != null) {
            httpAuthHandler.cancel();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void onOK() {
        String obj = this.mEdtUserName.getText().toString();
        String obj2 = this.mEdtPassword.getText().toString();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, this.mEdtUserName);
        }
        WebView webView = this.mWebView;
        if (webView != null) {
            String str = this.mHost;
            if (str != null) {
                webView.setHttpAuthUsernamePassword(str, this.mRealm, obj, obj2);
                this.mWebView = null;
            }
        }
        HttpAuthHandler httpAuthHandler = this.mHttpAuthHandler;
        if (httpAuthHandler != null) {
            httpAuthHandler.proceed(obj, obj2);
            this.mHttpAuthHandler = null;
        }
        if (this.mIsProxyServer) {
            PTApp.getInstance().userInputUsernamePasswordForProxy(this.mServer, this.mPort, obj, obj2, false);
        }
        dismissAllowingStateLoss();
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, this.mEdtUserName);
        }
        if (this.mIsProxyServer) {
            PTApp.getInstance().userInputUsernamePasswordForProxy(this.mServer, this.mPort, "", "", true);
        }
        if (this.mFinishActivityOnDismiss && activity != null) {
            activity.finish();
        }
    }

    public void afterTextChanged(Editable editable) {
        updateButtons();
    }

    private void updateButtons() {
        if (this.mBtnOK == null) {
            return;
        }
        if (this.mIsProxyServer || (!StringUtil.isEmptyOrNull(this.mEdtUserName.getText().toString()) && !StringUtil.isEmptyOrNull(this.mEdtPassword.getText().toString()))) {
            this.mBtnOK.setEnabled(true);
        } else {
            this.mBtnOK.setEnabled(false);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkInput() {
        return !StringUtil.isEmptyOrNull(this.mEdtUserName.getText().toString()) && !StringUtil.isEmptyOrNull(this.mEdtPassword.getText().toString());
    }
}
