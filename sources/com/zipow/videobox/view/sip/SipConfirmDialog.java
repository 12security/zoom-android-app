package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class SipConfirmDialog extends Dialog {
    private TextView mBtnClose;
    private Button mBtnStartMeeting;
    /* access modifiers changed from: private */
    public Callback mCallback;
    private CharSequence mConfirmText;
    private CharSequence mMessage;
    @Nullable
    private CharSequence mTitle;
    private TextView mTxtMsg;
    private TextView mTxtTitle;

    public interface Callback {
        void onCancel();

        void onConfirm();
    }

    public SipConfirmDialog(@NonNull Context context) {
        super(context, C4558R.style.ZMDialog_Material_RoundRect_BigCorners);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        setContentView(C4558R.layout.zm_sip_confirm_dialog);
        setCancelable(false);
        this.mBtnClose = (TextView) findViewById(C4558R.C4560id.btnCancel);
        this.mBtnClose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SipConfirmDialog.this.mCallback != null) {
                    SipConfirmDialog.this.mCallback.onCancel();
                }
                SipConfirmDialog.this.cancel();
            }
        });
        this.mBtnStartMeeting = (Button) findViewById(C4558R.C4560id.btnStartMeeting);
        this.mBtnStartMeeting.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SipConfirmDialog.this.mCallback != null) {
                    SipConfirmDialog.this.mCallback.onConfirm();
                }
                SipConfirmDialog.this.dismiss();
            }
        });
        this.mTxtTitle = (TextView) findViewById(C4558R.C4560id.title);
        if (!TextUtils.isEmpty(this.mTitle)) {
            this.mTxtTitle.setText(this.mTitle);
        }
        this.mTxtMsg = (TextView) findViewById(C4558R.C4560id.message);
        if (!TextUtils.isEmpty(this.mMessage)) {
            this.mTxtMsg.setText(this.mMessage);
        }
        if (!TextUtils.isEmpty(this.mConfirmText)) {
            this.mBtnStartMeeting.setText(this.mConfirmText);
        }
        initWindow();
    }

    public void setTitle(@Nullable CharSequence charSequence) {
        this.mTitle = charSequence;
    }

    public void setMessage(String str) {
        this.mMessage = str;
    }

    public void setConfirmText(CharSequence charSequence) {
        this.mConfirmText = charSequence;
    }

    private void initWindow() {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (((double) displayMetrics.widthPixels) * 0.9d);
            window.setAttributes(attributes);
        }
    }
}
