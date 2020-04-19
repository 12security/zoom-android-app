package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog.ErrorInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.ZMPhoneNumberHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class CallToCarrierFragment extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public String mCallId;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mDialog = null;

    public static void show(@Nullable ZMActivity zMActivity, @Nullable String str) {
        if (zMActivity != null && !TextUtils.isEmpty(str)) {
            String name = CallToCarrierFragment.class.getName();
            if (zMActivity.getSupportFragmentManager().findFragmentByTag(name) == null) {
                Bundle bundle = new Bundle();
                bundle.putString("callId", str);
                CallToCarrierFragment callToCarrierFragment = new CallToCarrierFragment();
                callToCarrierFragment.setArguments(bundle);
                zMActivity.getSupportFragmentManager().beginTransaction().add((Fragment) callToCarrierFragment, name).commitAllowingStateLoss();
            }
        }
    }

    public static void dismiss(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(CallToCarrierFragment.class.getName());
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mCallId = getArguments().getString("callId");
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), C4558R.style.ZMDialog_Material), C4558R.layout.fragment_call_to_carrier, null);
        final EditText editText = (EditText) inflate.findViewById(C4558R.C4560id.editNumber);
        String myCarrierNumber = CmmSIPCallManager.getInstance().getMyCarrierNumber(getActivity());
        if (myCarrierNumber != null) {
            editText.setText(myCarrierNumber);
            editText.setSelection(myCarrierNumber.length());
        }
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity())) {
            inflate.setFocusable(true);
            inflate.setFocusableInTouchMode(true);
            editText.setAccessibilityDelegate(new AccessibilityDelegate() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    String obj = editText.getText().toString();
                    if (obj.length() > 0) {
                        obj = StringUtil.digitJoin(obj.split(""), PreferencesConstants.COOKIE_DELIMITER);
                    } else if (editText.getHint() != null) {
                        obj = editText.getHint().toString();
                    }
                    accessibilityNodeInfo.setText(obj);
                    accessibilityNodeInfo.setContentDescription(obj);
                }
            });
            editText.postDelayed(new Runnable() {
                public void run() {
                    if (CallToCarrierFragment.this.getContext() != null) {
                        AccessibilityUtil.announceForAccessibilityCompat((View) editText, (CharSequence) CallToCarrierFragment.this.getContext().getString(C4558R.string.zm_pbx_switch_to_carrier_title_102668));
                    }
                    editText.postDelayed(new Runnable() {
                        public void run() {
                            editText.requestFocus();
                            if (CallToCarrierFragment.this.getContext() != null) {
                                AccessibilityUtil.announceForAccessibilityCompat((View) editText, editText.getHint());
                            }
                        }
                    }, 2000);
                }
            }, 1000);
        } else {
            editText.postDelayed(new Runnable() {
                public void run() {
                    editText.requestFocus();
                    if (editText.getText().length() <= 0) {
                        UIUtil.openSoftKeyboard(CallToCarrierFragment.this.getActivity(), editText, 2);
                    }
                }
            }, 300);
        }
        this.mDialog = new Builder(getActivity()).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CallToCarrierFragment.this.mDialog != null) {
                    UIUtil.closeSoftKeyboard(CallToCarrierFragment.this.getActivity(), CallToCarrierFragment.this.mDialog.getCurrentFocus());
                }
            }
        }).setPositiveButton(C4558R.string.zm_pbx_switch_button_102668, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CallToCarrierFragment.this.mDialog != null) {
                    UIUtil.closeSoftKeyboard(CallToCarrierFragment.this.getActivity(), CallToCarrierFragment.this.mDialog.getCurrentFocus());
                }
                String trim = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(trim) && CallToCarrierFragment.this.mDialog != null) {
                    ZMPhoneNumberHelper zMPhoneNumberHelper = PTApp.getInstance().getZMPhoneNumberHelper();
                    if (zMPhoneNumberHelper == null || zMPhoneNumberHelper.isValidPhoneNumber(trim)) {
                        ZMPhoneUtils.saveStringValue(PreferenceUtil.PBX_SIP_SWITCH_TO_CARRIER_NUMBER, trim);
                        if (!CmmSIPCallManager.getInstance().switchCallToCarrier(CallToCarrierFragment.this.mCallId, ZMPhoneUtils.formatPhoneNumberAsE164(trim))) {
                            Resources resources = CallToCarrierFragment.this.mDialog.getContext().getResources();
                            CallToCarrierFragment.this.showErrorMsgDialog(resources.getString(C4558R.string.zm_sip_callout_failed_27110), resources.getString(C4558R.string.zm_pbx_switch_to_carrier_error_des_102668), 1);
                        } else if (CallToCarrierFragment.this.getActivity() instanceof SipInCallActivity) {
                            ((SipInCallActivity) CallToCarrierFragment.this.getActivity()).switchToCarrierSendSuccess();
                        }
                        return;
                    }
                    Resources resources2 = CallToCarrierFragment.this.mDialog.getContext().getResources();
                    CallToCarrierFragment.this.showErrorMsgDialog(resources2.getString(C4558R.string.zm_sip_callout_failed_27110), resources2.getString(C4558R.string.zm_pbx_call_failed_msg_102668), 0);
                }
            }
        }).create();
        this.mDialog.setCanceledOnTouchOutside(false);
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                CallToCarrierFragment.this.checkOKButtonState(editable);
            }
        });
        editText.post(new Runnable() {
            public void run() {
                CallToCarrierFragment.this.checkOKButtonState(editText.getText());
            }
        });
        return this.mDialog;
    }

    /* access modifiers changed from: private */
    public void checkOKButtonState(CharSequence charSequence) {
        ZMAlertDialog zMAlertDialog = this.mDialog;
        if (zMAlertDialog != null) {
            Button button = zMAlertDialog.getButton(-1);
            if (button != null) {
                button.setEnabled(charSequence.length() > 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showErrorMsgDialog(String str, String str2, int i) {
        ErrorInfo errorInfo = new ErrorInfo(str, str2, i);
        errorInfo.setFinishActivityOnDismiss(false);
        ErrorMsgConfirmDialog.show((ZMActivity) getActivity(), errorInfo);
    }

    public void onPause() {
        super.onPause();
        if (this.mDialog != null) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mDialog.getCurrentFocus());
        }
    }
}
