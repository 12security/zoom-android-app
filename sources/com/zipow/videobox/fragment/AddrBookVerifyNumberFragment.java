package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.PhoneRegisterResponse;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookVerifyNumberFragment extends ZMDialogFragment implements OnClickListener, IPhoneABListener {
    private static final String ARG_COUNTRY_CODE = "countryCode";
    private static final String ARG_PHONE_NUMBER = "phoneNumber";
    private final String TAG = AddrBookVerifyNumberFragment.class.getSimpleName();
    private Button mBtnBack = null;
    private Button mBtnNext = null;
    private Button mBtnResend;
    @Nullable
    private String mCountryCode;
    private EditText mEdtCode = null;
    @NonNull
    private MyHandler mHandler = new MyHandler(this);
    @Nullable
    private String mPhoneNumber;
    private TextView mTxtNumber = null;

    static class MyHandler extends Handler {
        static final int MSG_UPDATE_SMS_SECONDS = 1;
        WeakReference<AddrBookVerifyNumberFragment> mWeakReference;

        MyHandler(AddrBookVerifyNumberFragment addrBookVerifyNumberFragment) {
            this.mWeakReference = new WeakReference<>(addrBookVerifyNumberFragment);
        }

        public void handleMessage(@NonNull Message message) {
            if (message.what == 1) {
                WeakReference<AddrBookVerifyNumberFragment> weakReference = this.mWeakReference;
                if (weakReference != null) {
                    AddrBookVerifyNumberFragment addrBookVerifyNumberFragment = (AddrBookVerifyNumberFragment) weakReference.get();
                    if (addrBookVerifyNumberFragment != null && addrBookVerifyNumberFragment.isAdded()) {
                        addrBookVerifyNumberFragment.updateSmsSeconds();
                    }
                }
            }
        }
    }

    public static void showInActivity(ZMActivity zMActivity, String str, String str2) {
        AddrBookVerifyNumberFragment addrBookVerifyNumberFragment = new AddrBookVerifyNumberFragment();
        Bundle bundle = new Bundle();
        bundle.putString("countryCode", str);
        bundle.putString("phoneNumber", str2);
        addrBookVerifyNumberFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, addrBookVerifyNumberFragment, AddrBookVerifyNumberFragment.class.getName()).commit();
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_addrbook_verify_number, viewGroup, false);
        this.mBtnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtNumber);
        this.mEdtCode = (EditText) inflate.findViewById(C4558R.C4560id.edtCode);
        this.mBtnResend = (Button) inflate.findViewById(C4558R.C4560id.btnResend);
        this.mBtnNext.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnResend.setOnClickListener(this);
        installVerificationCodeChangeListener();
        initViewData();
        updateNextButton();
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        PTUI.getInstance().addPhoneABListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mCountryCode = arguments.getString("countryCode");
            this.mPhoneNumber = arguments.getString("phoneNumber");
        }
    }

    public void onResume() {
        super.onResume();
        updateSmsSeconds();
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            PTUI.getInstance().removePhoneABListener(this);
        }
    }

    private void initViewData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString("countryCode");
            String string2 = arguments.getString("phoneNumber");
            if (string != null && string2 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("+");
                sb.append(string);
                if (string2.startsWith(sb.toString())) {
                    string2 = string2.substring(string.length() + 1);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("+");
                sb2.append(string);
                sb2.append(OAuth.SCOPE_DELIMITER);
                sb2.append(string2);
                this.mTxtNumber.setText(sb2.toString());
            }
        }
    }

    private void installVerificationCodeChangeListener() {
        this.mEdtCode.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                AddrBookVerifyNumberFragment.this.updateNextButton();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateNextButton() {
        this.mBtnNext.setEnabled(this.mEdtCode.getText().toString().length() >= 6);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnNext) {
            onClickBtnNext();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnResend) {
            onClickBtnResend();
        }
    }

    private void onClickBtnResend() {
        String str;
        if (ABContactsHelper.getRemainSMSTimeInSecond(this.mPhoneNumber, this.mCountryCode) <= 0) {
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null) {
                String str2 = this.mPhoneNumber;
                if (str2 != null) {
                    if (str2.startsWith("+")) {
                        str = this.mPhoneNumber;
                    } else if (str2.startsWith("0")) {
                        String substring = str2.substring(1);
                        StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(this.mCountryCode);
                        sb.append(substring);
                        str = sb.toString();
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("+");
                        sb2.append(this.mCountryCode);
                        sb2.append(str2);
                        str = sb2.toString();
                    }
                    int registerPhoneNumber = aBContactsHelper.registerPhoneNumber(str, this.mCountryCode, SystemInfoHelper.getDeviceId());
                    if (registerPhoneNumber == 0) {
                        WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
                    } else {
                        showErrorDialog(registerPhoneNumber);
                    }
                }
            }
        }
    }

    private void onClickBtnNext() {
        String str;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
        }
        Bundle arguments = getArguments();
        String str2 = null;
        if (arguments != null) {
            str2 = arguments.getString("countryCode");
            str = arguments.getString("phoneNumber");
        } else {
            str = null;
        }
        if (!StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str)) {
            String format = String.format(CompatUtils.getLocalDefault(), "+%s%s", new Object[]{str2, str});
            String obj = this.mEdtCode.getText().toString();
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            if (aBContactsHelper != null) {
                int verifyPhoneNumber = aBContactsHelper.verifyPhoneNumber(format, SystemInfoHelper.getDeviceId(), obj);
                if (verifyPhoneNumber == 0) {
                    WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
                } else {
                    showErrorDialog(verifyPhoneNumber);
                }
            }
        }
    }

    private void onClickBtnBack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        if (getShowsDialog()) {
            dismiss();
        } else if (activity != null) {
            activity.setResult(0);
            activity.finish();
        }
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        EventTaskManager nonNullEventTaskManagerOrThrowException = getNonNullEventTaskManagerOrThrowException();
        final int i2 = i;
        final long j2 = j;
        final Object obj2 = obj;
        C24762 r1 = new EventAction("handlePhoneABEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((AddrBookVerifyNumberFragment) iUIElement).handlePhoneABEvent(i2, j2, obj2);
            }
        };
        nonNullEventTaskManagerOrThrowException.push(r1);
    }

    /* access modifiers changed from: private */
    public void handlePhoneABEvent(int i, long j, Object obj) {
        switch (i) {
            case 0:
                onPhoneRegisterComplete(j, obj);
                return;
            case 2:
                onPhoneNumberVerifyComplete(j);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void updateSmsSeconds() {
        this.mHandler.removeMessages(1);
        int remainSMSTimeInSecond = ABContactsHelper.getRemainSMSTimeInSecond(this.mPhoneNumber, this.mCountryCode);
        if (remainSMSTimeInSecond > 0) {
            this.mBtnResend.setText(getString(C4558R.string.zm_lbl_seconds_33300, Integer.valueOf(remainSMSTimeInSecond)));
            this.mHandler.sendEmptyMessageDelayed(1, 1000);
            return;
        }
        this.mBtnResend.setText(C4558R.string.zm_btn_resend_code_33300);
    }

    private void onPhoneRegisterComplete(long j, Object obj) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            int i = (int) j;
            if (i != 0) {
                showErrorDialog(i);
            } else {
                byte[] bArr = (byte[]) obj;
                PhoneRegisterResponse phoneRegisterResponse = null;
                if (bArr != null) {
                    try {
                        phoneRegisterResponse = PhoneRegisterResponse.parseFrom(bArr);
                    } catch (InvalidProtocolBufferException unused) {
                        return;
                    }
                }
                if (phoneRegisterResponse == null) {
                    showErrorDialog(i);
                } else {
                    updateSmsSeconds();
                }
            }
        }
    }

    private void onPhoneNumberVerifyComplete(long j) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
            int i = (int) j;
            if (i != 0) {
                showErrorDialog(i);
            } else {
                onPhoneNumberVerifyOK();
            }
        }
    }

    private void onPhoneNumberVerifyOK() {
        String str;
        FragmentActivity activity = getActivity();
        if (getShowsDialog()) {
            dismiss();
        } else if (activity != null) {
            Bundle arguments = getArguments();
            String str2 = null;
            if (arguments != null) {
                str2 = arguments.getString("countryCode");
                str = arguments.getString("phoneNumber");
            } else {
                str = null;
            }
            if (!StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str)) {
                String format = String.format(CompatUtils.getLocalDefault(), "+%s%s", new Object[]{str2, str});
                Intent intent = new Intent();
                intent.putExtra("countryCode", str2);
                intent.putExtra("number", format);
                activity.setResult(-1, intent);
                activity.finish();
            }
        }
    }

    private void showErrorDialog(int i) {
        int i2 = C4558R.string.zm_msg_verify_phone_number_failed;
        if (i == 406) {
            i2 = C4558R.string.zm_alert_phone_bypass_40122;
        }
        SimpleMessageDialog.newInstance(i2).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }
}
