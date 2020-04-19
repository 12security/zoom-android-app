package com.zipow.videobox.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IRealNameAuthEventListener;
import com.zipow.videobox.fragment.SelectCountryCodeFragment;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.RealNameAuthCountryCodes;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMWebPageUtil;
import com.zipow.videobox.view.ZMVerifyCodeView;
import com.zipow.videobox.view.ZMVerifyCodeView.VerifyCodeCallBack;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMSpanny;
import p021us.zoom.videomeetings.C4558R;

public class ZMRealNameAuthDialog extends ZMDialogFragment implements OnClickListener, IRealNameAuthEventListener, VerifyCodeCallBack {
    private static final int REQUEST_SELECT_COUNTRY_CODE = 10000;
    private static final String TAG = "com.zipow.videobox.dialog.ZMRealNameAuthDialog";
    private Button mBtnCountryCode;
    private Button mBtnVerify;
    private EditText mEdtCode;
    private EditText mEdtNumber;
    @Nullable
    private CountryCodeItem mSelectedCountryCode;
    private TextView mTxtPrivacy;
    private TextView mTxtSignInToJoin;
    private ZMVerifyCodeView mZMVerifyCodeView;

    public static ZMRealNameAuthDialog show(@NonNull ZMActivity zMActivity) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return null;
        }
        dismiss(supportFragmentManager);
        if (VERSION.SDK_INT == 26) {
            zMActivity.setRequestedOrientation(7);
        } else {
            zMActivity.setRequestedOrientation(1);
        }
        ZMRealNameAuthDialog zMRealNameAuthDialog = new ZMRealNameAuthDialog();
        zMRealNameAuthDialog.show(supportFragmentManager, TAG);
        return zMRealNameAuthDialog;
    }

    public static void dismiss(@NonNull ZMActivity zMActivity, boolean z) {
        if (z) {
            zMActivity.setRequestedOrientation(-1);
        }
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            ZMRealNameAuthDialog zMRealNameAuthDialog = (ZMRealNameAuthDialog) supportFragmentManager.findFragmentByTag(TAG);
            if (zMRealNameAuthDialog != null) {
                zMRealNameAuthDialog.dismiss();
            }
        }
    }

    private static void dismiss(FragmentManager fragmentManager) {
        ZMRealNameAuthDialog zMRealNameAuthDialog = (ZMRealNameAuthDialog) fragmentManager.findFragmentByTag(TAG);
        if (zMRealNameAuthDialog != null) {
            zMRealNameAuthDialog.dismiss();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, C4558R.style.ZMDialog_NoTitle);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setCancelable(false);
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_verify_phone_dialog, null, false);
        inflate.findViewById(C4558R.C4560id.btnClose).setOnClickListener(this);
        this.mZMVerifyCodeView = (ZMVerifyCodeView) inflate.findViewById(C4558R.C4560id.zmVerifyCodeView);
        this.mBtnCountryCode = (Button) inflate.findViewById(C4558R.C4560id.btnCountryCode);
        this.mBtnCountryCode.setOnClickListener(this);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mEdtCode = (EditText) inflate.findViewById(C4558R.C4560id.edtCode);
        this.mBtnVerify = (Button) inflate.findViewById(C4558R.C4560id.btnVerify);
        this.mBtnVerify.setOnClickListener(this);
        this.mTxtSignInToJoin = (TextView) inflate.findViewById(C4558R.C4560id.txtSignInToJoin);
        this.mTxtPrivacy = (TextView) inflate.findViewById(C4558R.C4560id.txtPrivacy);
        if (bundle == null) {
            loadDefaultNumber();
        } else {
            this.mSelectedCountryCode = (CountryCodeItem) bundle.get("mSelectedCountryCode");
            if (this.mSelectedCountryCode == null) {
                loadDefaultNumber();
            } else {
                updateSelectedCountry();
            }
        }
        setUpView();
        this.mZMVerifyCodeView.setmVerifyCodeCallBack(this);
        ConfUI.getInstance().addIRealNameAuthEventListener(this);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mSelectedCountryCode", this.mSelectedCountryCode);
    }

    public void onDestroyView() {
        ConfUI.getInstance().removeIRealNameAuthEventListener(this);
        ZMVerifyCodeView zMVerifyCodeView = this.mZMVerifyCodeView;
        if (zMVerifyCodeView != null) {
            zMVerifyCodeView.setmVerifyCodeCallBack(null);
        }
        super.onDestroyView();
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 10000 && i2 == -1 && intent != null) {
            CountryCodeItem countryCodeItem = (CountryCodeItem) intent.getSerializableExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            if (countryCodeItem != null) {
                this.mSelectedCountryCode = countryCodeItem;
                updateSelectedCountry();
            }
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnClose) {
            onClickClose();
        } else if (id == C4558R.C4560id.btnVerify) {
            onClickVerify();
        } else if (id == C4558R.C4560id.btnCountryCode) {
            onClickCountryCode();
        }
    }

    public void onClickSendCode() {
        if (ConfLocalHelper.checkNetWork(this)) {
            CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
            if (countryCodeItem != null) {
                String str = countryCodeItem.countryCode;
                String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
                if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(phoneNumber)) {
                    if (ConfMgr.getInstance().requestRealNameAuthSMS(str, phoneNumber)) {
                        WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
                    } else {
                        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_verify_phone_number_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
                    }
                }
            }
        }
    }

    public void onRequestRealNameAuthSMS(final int i) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onRequestRealNameAuthSMS") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ZMRealNameAuthDialog) iUIElement).sinkRequestRealNameAuthSMS(i);
            }
        });
    }

    public void onVerifyRealNameAuthResult(final int i, final int i2) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onVerifyRealNameAuthResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((ZMRealNameAuthDialog) iUIElement).sinkVerifyRealNameAuthResult(i, i2);
            }
        });
    }

    /* access modifiers changed from: private */
    public void sinkRequestRealNameAuthSMS(int i) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            if (i != 0) {
                if (i == 6) {
                    closeDialog();
                    return;
                }
                int i2 = C4558R.string.zm_msg_verify_send_sms_failed_109213;
                if (i == 3) {
                    i2 = C4558R.string.zm_msg_verify_invalid_phone_num_109213;
                    this.mZMVerifyCodeView.forceEnableSendCode();
                } else if (i == 4) {
                    i2 = C4558R.string.zm_msg_verify_phone_num_already_bound_109213;
                    this.mZMVerifyCodeView.forceEnableSendCode();
                } else if (i == 5) {
                    i2 = C4558R.string.zm_msg_verify_phone_num_send_too_frequent_109213;
                }
                SimpleMessageDialog.newInstance(i2).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            }
        }
    }

    private void closeDialog() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
            zMActivity.setRequestedOrientation(-1);
        }
        dismiss();
    }

    /* access modifiers changed from: private */
    public void sinkVerifyRealNameAuthResult(int i, int i2) {
        int i3;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            if (i == 1) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    if (i2 == 1) {
                        i3 = C4558R.string.zm_msg_error_verification_code_109213;
                    } else if (i2 == 2) {
                        i3 = C4558R.string.zm_msg_expired_verification_code_109213;
                    } else if (i2 == 4 || i2 == 0 || i2 == 3) {
                        closeDialog();
                        return;
                    } else {
                        i3 = -1;
                    }
                    if (i3 != -1) {
                        new Builder(zMActivity).setMessage(i3).setCancelable(true).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).create().show();
                    }
                }
            }
        }
    }

    private void onClickVerify() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            String str = countryCodeItem.countryCode;
            String phoneNumber = PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
            String obj = this.mEdtCode.getText().toString();
            if (!StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(phoneNumber) && !StringUtil.isEmptyOrNull(obj)) {
                if (getActivity() != null) {
                    UIUtil.closeSoftKeyboard(getActivity(), getView());
                }
                if (ConfLocalHelper.checkNetWork(this)) {
                    WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
                    ConfMgr.getInstance().onUserConfirmRealNameAuth(str, phoneNumber, obj);
                }
            }
        }
    }

    private void onClickCountryCode() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            RealNameAuthCountryCodes realNameAuthCountryCodes = confContext.getRealNameAuthCountryCodes();
            if (realNameAuthCountryCodes != null) {
                List<CountryCode> realNameAuthCountryCodesList = realNameAuthCountryCodes.getRealNameAuthCountryCodesList();
                if (realNameAuthCountryCodesList != null && !realNameAuthCountryCodesList.isEmpty()) {
                    if (getActivity() != null) {
                        UIUtil.closeSoftKeyboard(getActivity(), getView());
                    }
                    ArrayList arrayList = new ArrayList();
                    for (CountryCode countryCode : realNameAuthCountryCodesList) {
                        if (countryCode != null) {
                            String code = countryCode.getCode();
                            if (code.startsWith("+")) {
                                code = code.substring(1);
                            }
                            CountryCodeItem countryCodeItem = new CountryCodeItem(code, countryCode.getId(), countryCode.getName(), countryCode.getNumber(), countryCode.getDisplaynumber(), countryCode.getCalltype());
                            arrayList.add(countryCodeItem);
                        }
                    }
                    SelectCountryCodeFragment.showAsActivity(this, arrayList, true, 10000);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickSignInToJoin() {
        ConfMgr.getInstance().loginToJoinMeetingForRealNameAuth();
        dismiss();
    }

    /* access modifiers changed from: private */
    public void onClickPrivacy() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            String realNameAuthPrivacyURL = confContext.getRealNameAuthPrivacyURL();
            if (!StringUtil.isEmptyOrNull(realNameAuthPrivacyURL)) {
                ZMWebPageUtil.startWebPage((Fragment) this, realNameAuthPrivacyURL, getString(C4558R.string.zm_title_privacy_policy));
            }
        }
    }

    private void onClickClose() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            UIUtil.closeSoftKeyboard(confActivity, getView());
            ConfLocalHelper.leaveConfBeforeConnected(confActivity);
        }
    }

    private void setUpView() {
        String string = getString(C4558R.string.zm_title_privacy_policy);
        ZMSpanny zMSpanny = new ZMSpanny(getString(C4558R.string.zm_lbl_cn_join_meeting_privacy_109213, string));
        zMSpanny.setSpans(string, new StyleSpan(1), new ForegroundColorSpan(getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), new RelativeSizeSpan(1.2f), new ClickableSpan() {
            public void onClick(@NonNull View view) {
                ZMRealNameAuthDialog.this.onClickPrivacy();
            }
        });
        this.mTxtPrivacy.setText(zMSpanny);
        this.mTxtPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null || !confContext.isPTLogin()) {
            this.mTxtSignInToJoin.setVisibility(0);
            String string2 = getString(C4558R.string.zm_alert_sign_in_to_join_title_87408);
            ZMSpanny zMSpanny2 = new ZMSpanny(getString(C4558R.string.zm_lbl_already_have_verified_number_109213, string2));
            zMSpanny2.setSpans(string2, new StyleSpan(1), new ForegroundColorSpan(getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB)), new RelativeSizeSpan(1.2f), new ClickableSpan() {
                public void onClick(@NonNull View view) {
                    ZMRealNameAuthDialog.this.onClickSignInToJoin();
                }
            });
            this.mTxtSignInToJoin.setText(zMSpanny2);
            this.mTxtSignInToJoin.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            this.mTxtSignInToJoin.setVisibility(8);
        }
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMRealNameAuthDialog.this.onPhoneNumberChanged();
            }
        });
        this.mEdtCode.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMRealNameAuthDialog.this.onCodeChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onPhoneNumberChanged() {
        if (this.mEdtCode != null) {
            EditText editText = this.mEdtNumber;
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnVerify == null)) {
                String phoneNumber = PhoneNumberUtil.getPhoneNumber(editText.getText().toString());
                String obj = this.mEdtCode.getText().toString();
                boolean z = true;
                boolean z2 = phoneNumber.length() > 4;
                boolean z3 = obj.length() == 6;
                this.mZMVerifyCodeView.enableSendCode(z2);
                Button button = this.mBtnVerify;
                if (!z2 || !z3) {
                    z = false;
                }
                button.setEnabled(z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onCodeChanged() {
        if (this.mEdtCode != null) {
            EditText editText = this.mEdtNumber;
            if (!(editText == null || this.mZMVerifyCodeView == null || this.mBtnVerify == null)) {
                this.mBtnVerify.setEnabled(PhoneNumberUtil.getPhoneNumber(editText.getText().toString()).length() > 4 && this.mEdtCode.getText().toString().length() == 6);
            }
        }
    }

    private void loadDefaultNumber() {
        if (getActivity() != null) {
            String str = CountryCodeUtil.CN_ISO_COUNTRY_CODE;
            this.mSelectedCountryCode = new CountryCodeItem(CountryCodeUtil.isoCountryCode2PhoneCountryCode(str), str, new Locale("", str.toLowerCase(Locale.US)).getDisplayCountry());
            updateSelectedCountry();
        }
    }

    private void updateSelectedCountry() {
        if (this.mSelectedCountryCode != null) {
            Button button = this.mBtnCountryCode;
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(this.mSelectedCountryCode.countryCode);
            button.setText(sb.toString());
        }
    }
}
