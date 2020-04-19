package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.box.androidsdk.content.models.BoxUser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.AddrBookVerifyNumberActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.PhoneRegisterResponse;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPhoneABListener;
import com.zipow.videobox.ptapp.SystemInfoHelper;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookSetNumberFragment extends ZMDialogFragment implements OnClickListener, IPhoneABListener {
    private static final int REQUEST_SELECT_COUTRY_CODE = 100;
    private final String TAG = AddrBookSetNumberFragment.class.getSimpleName();
    private Button mBtnBack = null;
    private Button mBtnNext = null;
    private View mBtnSelectCountryCode = null;
    private EditText mEdtNumber = null;
    @Nullable
    private String mIsoCountryCode = null;
    @Nullable
    private CountryCodeItem mSelectedCountryCode;
    private TextView mTxtCountryCode;

    public static class RegisterConfirmDialog extends ZMDialogFragment {
        private static final String ARG_COUNTRY_CODE = "countryCode";
        private static final String ARG_NUMBER = "number";
        @Nullable
        private String mCountryCode;
        @Nullable
        private String mNumber;

        public static void showRegisterConfirmDialog(AddrBookSetNumberFragment addrBookSetNumberFragment, String str, String str2) {
            RegisterConfirmDialog registerConfirmDialog = new RegisterConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_NUMBER, str);
            bundle.putString("countryCode", str2);
            registerConfirmDialog.setArguments(bundle);
            registerConfirmDialog.show(addrBookSetNumberFragment.getChildFragmentManager(), RegisterConfirmDialog.class.getName());
        }

        @Nullable
        public static RegisterConfirmDialog getRegisterConfirmDialog(AddrBookSetNumberFragment addrBookSetNumberFragment) {
            return (RegisterConfirmDialog) addrBookSetNumberFragment.getChildFragmentManager().findFragmentByTag(RegisterConfirmDialog.class.getName());
        }

        public RegisterConfirmDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            Bundle arguments = getArguments();
            if (arguments == null) {
                return new Builder(getActivity()).create();
            }
            this.mNumber = arguments.getString(ARG_NUMBER);
            this.mCountryCode = arguments.getString("countryCode");
            if (StringUtil.isEmptyOrNull(this.mNumber) || StringUtil.isEmptyOrNull(this.mCountryCode)) {
                return new Builder(getActivity()).create();
            }
            String formatDisplayNumber = formatDisplayNumber(this.mNumber, this.mCountryCode);
            return new Builder(getActivity()).setTitle((CharSequence) getString(C4558R.string.zm_msg_send_verification_sms_confirm, formatDisplayNumber)).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    RegisterConfirmDialog.this.onOK();
                }
            }).create();
        }

        @NonNull
        private String formatDisplayNumber(String str, String str2) {
            if (str.length() <= str2.length()) {
                return str;
            }
            String substring = str.substring(str2.length() + 1);
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(str2);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(substring);
            return sb.toString();
        }

        public void onResume() {
            super.onResume();
            if (StringUtil.isEmptyOrNull(this.mNumber) || StringUtil.isEmptyOrNull(this.mCountryCode)) {
                dismissAllowingStateLoss();
            }
        }

        /* access modifiers changed from: private */
        public void onOK() {
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof AddrBookSetNumberFragment) {
                ((AddrBookSetNumberFragment) parentFragment).onConfirmRegister(this.mNumber, this.mCountryCode);
            }
        }
    }

    public static void showInActivity(ZMActivity zMActivity) {
        AddrBookSetNumberFragment addrBookSetNumberFragment = new AddrBookSetNumberFragment();
        addrBookSetNumberFragment.setArguments(new Bundle());
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, addrBookSetNumberFragment, AddrBookSetNumberFragment.class.getName()).commit();
    }

    public void onStart() {
        super.onStart();
        PTUI.getInstance().addPhoneABListener(this);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_addrbook_set_number, viewGroup, false);
        this.mBtnNext = (Button) inflate.findViewById(C4558R.C4560id.btnNext);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mBtnSelectCountryCode = inflate.findViewById(C4558R.C4560id.btnSelectCountryCode);
        this.mTxtCountryCode = (TextView) inflate.findViewById(C4558R.C4560id.txtCountryCode);
        this.mBtnNext.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSelectCountryCode.setOnClickListener(this);
        installNumberChangeListener();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mIsoCountryCode = CountryCodeUtil.getIsoCountryCode(activity);
        }
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            loadDefaultNumber();
        }
        if (bundle == null) {
            selectCountryCode(this.mIsoCountryCode);
        } else {
            this.mSelectedCountryCode = (CountryCodeItem) bundle.get("mSelectedCountryCode");
            updateSelectedCountry();
        }
        updateNextButton();
        return inflate;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C24701 r2 = new EventAction("ABSetNumberRequestPermission") {
            public void run(@NonNull IUIElement iUIElement) {
                ((AddrBookSetNumberFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("ABSetNumberRequestPermission", r2);
    }

    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_PHONE_STATE".equals(strArr[i2]) && iArr[i2] == 0) {
                    loadDefaultNumber();
                    updateNextButton();
                }
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mSelectedCountryCode", this.mSelectedCountryCode);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            CountryCodeItem countryCodeItem = (CountryCodeItem) intent.getSerializableExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            if (countryCodeItem != null) {
                this.mSelectedCountryCode = countryCodeItem;
                updateSelectedCountry();
            }
        }
    }

    private void selectCountryCode(@Nullable String str) {
        if (str != null) {
            this.mSelectedCountryCode = new CountryCodeItem(CountryCodeUtil.isoCountryCode2PhoneCountryCode(str), str, new Locale("", str.toLowerCase(Locale.US)).getDisplayCountry());
            updateSelectedCountry();
        }
    }

    private void updateSelectedCountry() {
        if (this.mSelectedCountryCode != null) {
            TextView textView = this.mTxtCountryCode;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mSelectedCountryCode.countryName);
            sb.append("(+");
            sb.append(this.mSelectedCountryCode.countryCode);
            sb.append(")");
            textView.setText(sb.toString());
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        PTUI.getInstance().addPhoneABListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            PTUI.getInstance().removePhoneABListener(this);
        }
    }

    /* access modifiers changed from: private */
    public void updateNextButton() {
        String selectedCountryCode = getSelectedCountryCode();
        String phoneNumber = getPhoneNumber();
        this.mBtnNext.setEnabled(!StringUtil.isEmptyOrNull(selectedCountryCode) && !StringUtil.isEmptyOrNull(phoneNumber) && phoneNumber.length() > 4);
    }

    private void installNumberChangeListener() {
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                AddrBookSetNumberFragment.this.updateNextButton();
            }
        });
    }

    private void loadDefaultNumber() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(BoxUser.FIELD_PHONE);
            if (telephonyManager != null) {
                String str = null;
                try {
                    str = telephonyManager.getLine1Number();
                } catch (SecurityException unused) {
                }
                this.mIsoCountryCode = CountryCodeUtil.getIsoCountryCode(activity);
                String isoCountryCode2PhoneCountryCode = CountryCodeUtil.isoCountryCode2PhoneCountryCode(this.mIsoCountryCode);
                if (str != null) {
                    this.mEdtNumber.setText(truncateCountryCode(str, isoCountryCode2PhoneCountryCode));
                }
            }
        }
    }

    private String truncateCountryCode(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return str;
        }
        String formatNumber = PhoneNumberUtil.formatNumber(str, str2);
        int indexOf = formatNumber.indexOf(43);
        String substring = indexOf >= 0 ? formatNumber.substring(indexOf + 1) : formatNumber;
        if (substring.indexOf(str2) != 0) {
            return formatNumber;
        }
        return substring.substring(str2.length());
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnNext) {
            onClickBtnNext();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSelectCountryCode) {
            onClickBtnSelectCountryCode();
        }
    }

    private void onClickBtnSelectCountryCode() {
        SelectCountryCodeFragment.showAsActivity(this, 100);
    }

    private void onClickBtnNext() {
        String str;
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(getFragmentManager(), SimpleMessageDialog.class.getName());
            return;
        }
        if (PTApp.getInstance().getABContactsHelper() != null) {
            String phoneNumber = getPhoneNumber();
            String selectedCountryCode = getSelectedCountryCode();
            if (!StringUtil.isEmptyOrNull(phoneNumber) && !StringUtil.isEmptyOrNull(selectedCountryCode)) {
                if (phoneNumber.startsWith("+")) {
                    String formatNumber = PhoneNumberUtil.formatNumber(phoneNumber, selectedCountryCode);
                    String countryCodeFromFormatedPhoneNumber = PhoneNumberUtil.getCountryCodeFromFormatedPhoneNumber(formatNumber);
                    if (StringUtil.isEmptyOrNull(countryCodeFromFormatedPhoneNumber)) {
                        this.mEdtNumber.setText(phoneNumber.substring(1));
                        return;
                    }
                    phoneNumber = formatNumber.substring(countryCodeFromFormatedPhoneNumber.length() + 1);
                    String str2 = countryCodeFromFormatedPhoneNumber;
                    str = formatNumber;
                    selectedCountryCode = str2;
                } else if (phoneNumber.startsWith("0")) {
                    phoneNumber = phoneNumber.substring(1);
                    StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(selectedCountryCode);
                    sb.append(phoneNumber);
                    str = sb.toString();
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("+");
                    sb2.append(selectedCountryCode);
                    sb2.append(phoneNumber);
                    str = sb2.toString();
                }
                selectCountryCode(CountryCodeUtil.phoneCountryCodeToIsoCountryCode(selectedCountryCode));
                this.mEdtNumber.setText(phoneNumber);
                showConfirmationDialog(str, selectedCountryCode);
            }
        }
    }

    private void showConfirmationDialog(String str, String str2) {
        RegisterConfirmDialog.showRegisterConfirmDialog(this, str, str2);
    }

    /* access modifiers changed from: private */
    public void onConfirmRegister(String str, String str2) {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (ABContactsHelper.getRemainSMSTimeInSecond(str, str2) > 0) {
                AddrBookVerifyNumberActivity.show((ZMActivity) getActivity(), str2, getPhoneNumber(), 100);
                return;
            }
            int registerPhoneNumber = aBContactsHelper.registerPhoneNumber(str, str2, SystemInfoHelper.getDeviceId());
            if (registerPhoneNumber == 0) {
                WaitingDialog.newInstance(C4558R.string.zm_msg_waiting).show(getFragmentManager(), WaitingDialog.class.getName());
            } else {
                showErrorDialog(registerPhoneNumber);
            }
        }
    }

    private String getSelectedCountryCode() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem == null) {
            return null;
        }
        return countryCodeItem.countryCode;
    }

    private String getPhoneNumber() {
        return PhoneNumberUtil.getPhoneNumber(this.mEdtNumber.getText().toString());
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
        C24723 r1 = new EventAction("handlePhoneABEvent") {
            public void run(@NonNull IUIElement iUIElement) {
                ((AddrBookSetNumberFragment) iUIElement).handlePhoneABEvent(i2, j2, obj2);
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
            default:
                return;
        }
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
                } else if (phoneRegisterResponse.getNeedVerifySMS()) {
                    startToVerifyPhoneNumber();
                } else {
                    onRegisterOK();
                }
            }
        }
    }

    private void startToVerifyPhoneNumber() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            AddrBookVerifyNumberActivity.show(zMActivity, getSelectedCountryCode(), getPhoneNumber(), 100);
            PTUI.getInstance().removePhoneABListener(this);
        }
    }

    private void showErrorDialog(int i) {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_register_phone_number_failed).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void onRegisterOK() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
        }
        if (getShowsDialog()) {
            dismiss();
        } else if (activity != null) {
            String selectedCountryCode = getSelectedCountryCode();
            String phoneNumber = getPhoneNumber();
            Intent intent = new Intent();
            intent.putExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE, selectedCountryCode);
            intent.putExtra("number", phoneNumber);
            activity.setResult(-1, intent);
            activity.finish();
        }
    }
}
