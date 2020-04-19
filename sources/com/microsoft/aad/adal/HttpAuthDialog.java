package com.microsoft.aad.adal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.microsoft.onedrivesdk.C1720R;

class HttpAuthDialog {
    /* access modifiers changed from: private */
    public CancelListener mCancelListener;
    private final Context mContext;
    /* access modifiers changed from: private */
    public AlertDialog mDialog = null;
    /* access modifiers changed from: private */
    public final String mHost;
    /* access modifiers changed from: private */
    public OkListener mOkListener;
    /* access modifiers changed from: private */
    public EditText mPasswordView;
    /* access modifiers changed from: private */
    public final String mRealm;
    /* access modifiers changed from: private */
    public EditText mUsernameView;

    public interface CancelListener {
        void onCancel();
    }

    public interface OkListener {
        void onOk(String str, String str2, String str3, String str4);
    }

    HttpAuthDialog(Context context, String str, String str2) {
        this.mContext = context;
        this.mHost = str;
        this.mRealm = str2;
        createDialog();
    }

    public void setOkListener(OkListener okListener) {
        this.mOkListener = okListener;
    }

    public void setCancelListener(CancelListener cancelListener) {
        this.mCancelListener = cancelListener;
    }

    public void show() {
        this.mDialog.show();
        this.mUsernameView.requestFocus();
    }

    @SuppressLint({"InflateParams"})
    private void createDialog() {
        View inflate = LayoutInflater.from(this.mContext).inflate(C1720R.layout.http_auth_dialog, null);
        this.mUsernameView = (EditText) inflate.findViewById(C1720R.C1722id.editUserName);
        this.mPasswordView = (EditText) inflate.findViewById(C1720R.C1722id.editPassword);
        this.mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                HttpAuthDialog.this.mDialog.getButton(-1).performClick();
                return true;
            }
        });
        this.mDialog = new Builder(this.mContext).setTitle(this.mContext.getText(C1720R.string.http_auth_dialog_title).toString()).setView(inflate).setPositiveButton(C1720R.string.http_auth_dialog_login, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (HttpAuthDialog.this.mOkListener != null) {
                    HttpAuthDialog.this.mOkListener.onOk(HttpAuthDialog.this.mHost, HttpAuthDialog.this.mRealm, HttpAuthDialog.this.mUsernameView.getText().toString(), HttpAuthDialog.this.mPasswordView.getText().toString());
                }
            }
        }).setNegativeButton(C1720R.string.http_auth_dialog_cancel, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (HttpAuthDialog.this.mCancelListener != null) {
                    HttpAuthDialog.this.mCancelListener.onCancel();
                }
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                if (HttpAuthDialog.this.mCancelListener != null) {
                    HttpAuthDialog.this.mCancelListener.onCancel();
                }
            }
        }).create();
    }
}
