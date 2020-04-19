package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.internal.view.SupportMenu;
import com.zipow.videobox.util.DialogUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.FingerprintUtil;
import p021us.zoom.androidlib.util.FingerprintUtil.IFingerprintResultListener;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

@RequiresApi(api = 23)
public class FingerprintAuthenticationDialog extends ZMDialogFragment {
    private static final String TAG = "com.zipow.videobox.dialog.FingerprintAuthenticationDialog";
    /* access modifiers changed from: private */
    public View mBtnCancel;
    /* access modifiers changed from: private */
    public TextView mBtnEnterPasswd;
    /* access modifiers changed from: private */
    public FingerprintAuthCallBack mFingerprintAuthCallBack;
    private FingerprintUtil mFingerprintUtil;
    /* access modifiers changed from: private */
    public LayoutParams mLayoutParams;
    /* access modifiers changed from: private */
    public LinearLayout mLinealayoutButton;
    /* access modifiers changed from: private */
    public TextView mTxtDesc;
    /* access modifiers changed from: private */
    public TextView mTxtTitle;

    public interface FingerprintAuthCallBack {
        void onAuthenticateSucceeded(AuthenticationResult authenticationResult);

        void onEnterPasswd();
    }

    public FingerprintAuthenticationDialog() {
        setCancelable(true);
    }

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            new FingerprintAuthenticationDialog().show(zMActivity.getSupportFragmentManager(), TAG);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mFingerprintAuthCallBack = (FingerprintAuthCallBack) context;
        this.mFingerprintUtil = new FingerprintUtil((ZMActivity) context);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mFingerprintUtil.onDestroy();
    }

    public void onPause() {
        super.onPause();
        this.mFingerprintUtil.cancelAuthenticate();
    }

    public void onResume() {
        super.onResume();
        if (!this.mFingerprintUtil.isSupportFingerprint()) {
            dismiss();
        } else {
            this.mFingerprintUtil.callFingerPrintVerify(new IFingerprintResultListener() {
                boolean isAlreadyFailed;

                public void onAuthenticateStart() {
                }

                public void onInSecurity() {
                }

                public void onNoEnroll() {
                }

                public void onNoHardwareDetected() {
                }

                public void onSupport() {
                }

                public void onAuthenticateError(int i, @NonNull CharSequence charSequence) {
                    if (FingerprintAuthenticationDialog.this.isResumed()) {
                        FingerprintAuthenticationDialog.this.dismissAllowingStateLoss();
                        if (this.isAlreadyFailed) {
                            DialogUtils.showAlertDialog((ZMActivity) FingerprintAuthenticationDialog.this.getActivity(), charSequence.toString(), C4558R.string.zm_btn_ok);
                        }
                    }
                }

                public void onAuthenticateFailed() {
                    this.isAlreadyFailed = true;
                    FingerprintAuthenticationDialog.this.mTxtTitle.setVisibility(8);
                    FingerprintAuthenticationDialog.this.mBtnEnterPasswd.setVisibility(0);
                    FingerprintAuthenticationDialog.this.mLayoutParams.gravity = 5;
                    FingerprintAuthenticationDialog.this.mLayoutParams.width = -2;
                    FingerprintAuthenticationDialog.this.mLinealayoutButton.setLayoutParams(FingerprintAuthenticationDialog.this.mLayoutParams);
                    LayoutParams layoutParams = (LayoutParams) FingerprintAuthenticationDialog.this.mBtnCancel.getLayoutParams();
                    layoutParams.width = -2;
                    FingerprintAuthenticationDialog.this.mBtnCancel.setLayoutParams(layoutParams);
                    FingerprintAuthenticationDialog.this.mTxtDesc.setText(C4558R.string.zm_alert_fingerprint_mismatch_22438);
                    FingerprintAuthenticationDialog.this.mTxtDesc.setTextColor(SupportMenu.CATEGORY_MASK);
                    Context context = FingerprintAuthenticationDialog.this.getContext();
                    if (context != null) {
                        FingerprintAuthenticationDialog.this.mTxtDesc.clearAnimation();
                        FingerprintAuthenticationDialog.this.mTxtDesc.startAnimation(AnimationUtils.loadAnimation(context, C4558R.anim.zm_shake));
                    }
                }

                public void onAuthenticateHelp(int i, CharSequence charSequence) {
                    FingerprintAuthenticationDialog.this.mTxtTitle.setVisibility(0);
                    FingerprintAuthenticationDialog.this.mBtnEnterPasswd.setVisibility(8);
                    FingerprintAuthenticationDialog.this.mLayoutParams.gravity = 1;
                    FingerprintAuthenticationDialog.this.mLayoutParams.width = -1;
                    FingerprintAuthenticationDialog.this.mLinealayoutButton.setLayoutParams(FingerprintAuthenticationDialog.this.mLayoutParams);
                    LayoutParams layoutParams = (LayoutParams) FingerprintAuthenticationDialog.this.mBtnCancel.getLayoutParams();
                    layoutParams.width = -1;
                    FingerprintAuthenticationDialog.this.mBtnCancel.setLayoutParams(layoutParams);
                    FingerprintAuthenticationDialog.this.mTxtDesc.setText(charSequence);
                    FingerprintAuthenticationDialog.this.mTxtDesc.setTextColor(FingerprintAuthenticationDialog.this.getResources().getColor(C4558R.color.zm_setting_option));
                    Context context = FingerprintAuthenticationDialog.this.getContext();
                    if (context != null) {
                        FingerprintAuthenticationDialog.this.mTxtDesc.clearAnimation();
                        FingerprintAuthenticationDialog.this.mTxtDesc.startAnimation(AnimationUtils.loadAnimation(context, C4558R.anim.zm_shake));
                    }
                }

                public void onAuthenticateSucceeded(AuthenticationResult authenticationResult) {
                    FingerprintAuthenticationDialog.this.dismissAllowingStateLoss();
                    FingerprintAuthenticationDialog.this.mFingerprintAuthCallBack.onAuthenticateSucceeded(authenticationResult);
                }
            });
        }
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        ZMAlertDialog create = new Builder(getActivity()).setTheme(C4558R.style.ZMDialog_Material_RoundRect).setCancelable(true).setView(createContent()).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    private View createContent() {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material_RoundRect), C4558R.layout.zm_fingerprint_authentication_dialog, null);
        this.mLinealayoutButton = (LinearLayout) inflate.findViewById(C4558R.C4560id.ll_button);
        this.mLayoutParams = (LayoutParams) this.mLinealayoutButton.getLayoutParams();
        this.mTxtDesc = (TextView) inflate.findViewById(C4558R.C4560id.txtDesc);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnEnterPasswd = (TextView) inflate.findViewById(C4558R.C4560id.btn_enter_passwd);
        this.mBtnCancel = inflate.findViewById(C4558R.C4560id.btn_cancel);
        this.mBtnEnterPasswd.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FingerprintAuthenticationDialog.this.dismiss();
                if (FingerprintAuthenticationDialog.this.mFingerprintAuthCallBack != null) {
                    FingerprintAuthenticationDialog.this.mFingerprintAuthCallBack.onEnterPasswd();
                }
            }
        });
        this.mBtnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FingerprintAuthenticationDialog.this.dismiss();
            }
        });
        return inflate;
    }
}
