package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.fragment.SelectPhoneNumberFragment.PhoneNumberItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IInviteByCallOutListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class InviteByPhoneFragment extends ZMDialogFragment implements OnClickListener {
    private static final String ARG_SUPPORT_CALLOUT_TYPE = "supportCalloutType";
    private static final String ARG_SUPPORT_COUNTRY_CODES = "supportCountryCodes";
    private static final int REQUEST_SELECT_COUTRY_CODE = 100;
    private static final int REQUEST_SELECT_PHONE_NUMBER = 101;
    private static final int UPDATE_STATUS_DELAY_MILLIS = 3000;
    @Nullable
    private static String s_lastCalloutName;
    @Nullable
    private static String s_lastCalloutNumber;
    private Button mBtnBack = null;
    private Button mBtnCall = null;
    private Button mBtnHangup = null;
    private View mBtnSelectCountryCode = null;
    private View mBtnSelectPhoneNumber = null;
    private EditText mEdtName = null;
    private EditText mEdtNumber = null;
    @NonNull
    private Handler mHandler = new Handler();
    private IInviteByCallOutListener mInviteByCallOutListener;
    private boolean mIsInitCallStatus = true;
    private IPTUIListener mPTUIListener;
    @Nullable
    private CountryCodeItem mSelectedCountryCode;
    private String mStrSelectedCountryCode = null;
    private int mSupportCalloutType = 2;
    @Nullable
    private ArrayList<CountryCodeItem> mSupportCountryCodes;
    private TextView mTxtCountryCode;
    private TextView mTxtMessage = null;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i, ArrayList<CountryCodeItem> arrayList, int i2) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SUPPORT_CALLOUT_TYPE, i);
        bundle.putSerializable(ARG_SUPPORT_COUNTRY_CODES, arrayList);
        SimpleActivity.show(zMActivity, InviteByPhoneFragment.class.getName(), bundle, i2, true, 1);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_invite_by_phone, viewGroup, false);
        this.mBtnCall = (Button) inflate.findViewById(C4558R.C4560id.btnCall);
        this.mBtnHangup = (Button) inflate.findViewById(C4558R.C4560id.btnHangup);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mEdtName = (EditText) inflate.findViewById(C4558R.C4560id.edtName);
        this.mBtnSelectCountryCode = inflate.findViewById(C4558R.C4560id.btnSelectCountryCode);
        this.mTxtCountryCode = (TextView) inflate.findViewById(C4558R.C4560id.txtCountryCode);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mBtnSelectPhoneNumber = inflate.findViewById(C4558R.C4560id.btnSelectPhoneNumber);
        if (OsUtil.isAtLeastL_MR1()) {
            this.mBtnSelectCountryCode.setAccessibilityTraversalBefore(C4558R.C4560id.btnBack);
        }
        this.mBtnCall.setOnClickListener(this);
        this.mBtnHangup.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSelectCountryCode.setOnClickListener(this);
        this.mBtnSelectPhoneNumber.setOnClickListener(this);
        installNumberChangeListener();
        installNameChangeListener();
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSupportCalloutType = arguments.getInt(ARG_SUPPORT_CALLOUT_TYPE, this.mSupportCalloutType);
            this.mSupportCountryCodes = (ArrayList) arguments.getSerializable(ARG_SUPPORT_COUNTRY_CODES);
        }
        if (bundle == null) {
            loadDefaultNumber();
        } else {
            this.mSelectedCountryCode = (CountryCodeItem) bundle.get("mSelectedCountryCode");
            this.mIsInitCallStatus = bundle.getBoolean("mIsInitCallStatus");
            updateSelectedCountry();
        }
        updateCallButton();
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mSelectedCountryCode", this.mSelectedCountryCode);
        bundle.putBoolean("mIsInitCallStatus", this.mIsInitCallStatus);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            CountryCodeItem countryCodeItem = (CountryCodeItem) intent.getSerializableExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
            if (countryCodeItem != null) {
                this.mSelectedCountryCode = countryCodeItem;
                updateSelectedCountry();
            }
        } else if (i == 101 && i2 == -1 && intent != null) {
            PhoneNumberItem phoneNumberItem = (PhoneNumberItem) intent.getSerializableExtra("phoneNumber");
            if (phoneNumberItem != null) {
                updateSelectedPhoneNumber(phoneNumberItem);
            }
        }
    }

    @Nullable
    private String getCountryDisplayName(@Nullable String str, String str2) {
        ArrayList<CountryCodeItem> arrayList = this.mSupportCountryCodes;
        String str3 = null;
        if (arrayList == null) {
            return null;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            CountryCodeItem countryCodeItem = (CountryCodeItem) it.next();
            if (countryCodeItem != null && StringUtil.isSameString(countryCodeItem.countryCode, str2)) {
                str3 = countryCodeItem.countryName;
            }
        }
        if (StringUtil.isEmptyOrNull(str3) && str != null) {
            str3 = new Locale("", str.toLowerCase(Locale.US)).getDisplayCountry();
        }
        return str3;
    }

    private void updateSelectedCountry() {
        String str;
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            if (countryCodeItem.callType == 999) {
                str = this.mSelectedCountryCode.countryName.replace("@", "");
                this.mEdtNumber.setHint(C4558R.string.zm_callout_hint_internal_extension_number_107106);
                if (getString(C4558R.string.zm_callout_msg_invite_indication).equalsIgnoreCase(this.mTxtMessage.getText().toString())) {
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_invite_internal_extension_indication_107106);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(ZMUtils.getCountryName(this.mSelectedCountryCode.isoCountryCode));
                sb.append("(+");
                sb.append(this.mSelectedCountryCode.countryCode);
                sb.append(")");
                str = sb.toString();
                this.mEdtNumber.setHint(C4558R.string.zm_callout_hint_phone_number_107106);
                if (getString(C4558R.string.zm_callout_msg_invite_internal_extension_indication_107106).equalsIgnoreCase(this.mTxtMessage.getText().toString())) {
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_invite_indication);
                }
            }
            this.mTxtCountryCode.setText(str);
            this.mBtnSelectCountryCode.setContentDescription(getString(C4558R.string.zm_accessibility_region_country_code_46328, str));
            if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                String str2 = this.mStrSelectedCountryCode;
                if (str2 != null && !StringUtil.isSameString(str2, str)) {
                    View view = this.mBtnSelectCountryCode;
                    AccessibilityUtil.announceForAccessibilityCompat(view, view.getContentDescription());
                }
            }
            this.mStrSelectedCountryCode = str;
        }
    }

    private void updateSelectedPhoneNumber(PhoneNumberItem phoneNumberItem) {
        this.mEdtName.setText(phoneNumberItem.contactName);
        String str = phoneNumberItem.countryCode;
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem == null || !StringUtil.isSameString(countryCodeItem.countryCode, str)) {
            String phoneCountryCodeToIsoCountryCode = CountryCodeUtil.phoneCountryCodeToIsoCountryCode(str);
            this.mSelectedCountryCode = new CountryCodeItem(str, phoneCountryCodeToIsoCountryCode, getCountryDisplayName(phoneCountryCodeToIsoCountryCode, str));
            updateSelectedCountry();
        }
        this.mEdtNumber.setText(truncateCountryCode(phoneNumberItem.normalizedNumber, str));
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeInviteByCallOutListener(this.mInviteByCallOutListener);
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mInviteByCallOutListener == null) {
            this.mInviteByCallOutListener = new IInviteByCallOutListener() {
                public void onCallOutStatusChanged(int i) {
                    InviteByPhoneFragment.this.onCallOutStatusChanged(i);
                }
            };
        }
        PTUI.getInstance().addInviteByCallOutListener(this.mInviteByCallOutListener);
        if (this.mPTUIListener == null) {
            this.mPTUIListener = new SimplePTUIListener() {
                public void onPTAppEvent(int i, long j) {
                    InviteByPhoneFragment.this.onPTAppEvent(i, j);
                }
            };
        }
        PTUI.getInstance().addPTUIListener(this.mPTUIListener);
        updateUIForCallOutStatus(PTApp.getInstance().getCallOutStatus());
        updateUIForConfCallStatus(PTApp.getInstance().getCallStatus());
        updateUIForSupportCalloutType(this.mSupportCalloutType);
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null && zMActivity.isFinishing()) {
            finishActivity(100);
            finishActivity(101);
        }
    }

    /* access modifiers changed from: private */
    public void updateCallButton() {
        String selectedCountryCode = getSelectedCountryCode();
        String selectedISOCountryCode = getSelectedISOCountryCode();
        String phoneNumber = getPhoneNumber();
        this.mBtnCall.setEnabled(!StringUtil.isEmptyOrNull(selectedCountryCode) && !StringUtil.isEmptyOrNull(getUserName()) && !StringUtil.isEmptyOrNull(phoneNumber) && ((!StringUtil.isEmptyOrNull(selectedISOCountryCode) && selectedISOCountryCode.toLowerCase().equals("internal")) || phoneNumber.length() > 4));
    }

    private void installNumberChangeListener() {
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                InviteByPhoneFragment.this.updateCallButton();
            }
        });
    }

    private void installNameChangeListener() {
        this.mEdtName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                InviteByPhoneFragment.this.updateCallButton();
            }
        });
    }

    private void loadDefaultNumber() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mSelectedCountryCode = CountryCodeItem.readFromPreference(PreferenceUtil.CALLOUT_INVITE_SELECT_COUNTRY);
            CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
            if (countryCodeItem == null || StringUtil.isEmptyOrNull(countryCodeItem.isoCountryCode)) {
                String isoCountryCode = CountryCodeUtil.getIsoCountryCode(activity);
                if (isoCountryCode != null) {
                    this.mSelectedCountryCode = new CountryCodeItem(CountryCodeUtil.isoCountryCode2PhoneCountryCode(isoCountryCode), isoCountryCode, new Locale("", isoCountryCode.toLowerCase(Locale.US)).getDisplayCountry());
                } else {
                    return;
                }
            }
            if (!(s_lastCalloutNumber == null || PTApp.getInstance().getCallOutStatus() == 0)) {
                this.mEdtNumber.setText(s_lastCalloutNumber);
                String str = s_lastCalloutName;
                if (str != null) {
                    this.mEdtName.setText(str);
                }
            }
            updateSelectedCountry();
        }
    }

    private String truncateCountryCode(String str, @NonNull String str2) {
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

    /* access modifiers changed from: private */
    public void onCallOutStatusChanged(int i) {
        updateUIForCallOutStatus(i);
    }

    /* access modifiers changed from: private */
    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            updateUIForConfCallStatus((int) j);
        }
    }

    private void updateUIForConfCallStatus(int i) {
        switch (i) {
            case 0:
            case 1:
                dismiss();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void updateUIForCallOutStatus(int i) {
        if (isAdded()) {
            if (i != 0) {
                this.mIsInitCallStatus = false;
            }
            int i2 = C4558R.color.zm_notification_background;
            int i3 = C4558R.color.zm_black;
            switch (i) {
                case 0:
                    if (this.mIsInitCallStatus) {
                        this.mTxtMessage.setText(C4558R.string.zm_callout_msg_invite_indication);
                        i2 = C4558R.color.zm_transparent;
                        i3 = C4558R.color.zm_black_2;
                        break;
                    }
                    break;
                case 1:
                    this.mTxtMessage.setText(getString(C4558R.string.zm_callout_msg_calling, getFullPhoneNumber()));
                    break;
                case 2:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_ringing);
                    break;
                case 3:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_call_accepted);
                    break;
                case 4:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_busy);
                    updateCallStatusDelayed(3000);
                    break;
                case 5:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_not_available);
                    updateCallStatusDelayed(3000);
                    break;
                case 6:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_user_hangup);
                    updateCallStatusDelayed(3000);
                    break;
                case 7:
                case 9:
                    this.mTxtMessage.setText(getString(C4558R.string.zm_callout_msg_fail_to_call, getFullPhoneNumber()));
                    updateCallStatusDelayed(3000);
                    break;
                case 8:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_success);
                    dismissDelayed(3000);
                    break;
                case 10:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_cancel_call);
                    break;
                case 11:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_call_canceled);
                    updateCallStatusDelayed(3000);
                    break;
                case 12:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_cancel_call_fail);
                    updateCallStatusDelayed(3000);
                    break;
                case 13:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_busy);
                    break;
                case 14:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_no_host);
                    updateCallStatusDelayed(3000);
                    break;
                case 15:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_high_rate);
                    updateCallStatusDelayed(3000);
                    break;
                case 16:
                    this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_too_frequent);
                    updateCallStatusDelayed(3000);
                    break;
            }
            Context context = getContext();
            if (context != null) {
                this.mTxtMessage.setBackgroundResource(i2);
                this.mTxtMessage.setTextColor(context.getResources().getColor(i3));
            }
            updateButtonsForCallOutStatus(i);
        }
    }

    private void updateButtonsForCallOutStatus(int i) {
        switch (i) {
            case 0:
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 11:
            case 12:
            case 13:
                this.mBtnCall.setVisibility(0);
                this.mBtnHangup.setVisibility(8);
                return;
            case 1:
            case 2:
            case 3:
            case 8:
                this.mBtnCall.setVisibility(8);
                this.mBtnHangup.setVisibility(0);
                this.mBtnHangup.setEnabled(true);
                return;
            case 10:
                this.mBtnCall.setVisibility(8);
                this.mBtnHangup.setVisibility(0);
                this.mBtnHangup.setEnabled(false);
                return;
            default:
                return;
        }
    }

    private void updateCallStatusDelayed(long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                InviteByPhoneFragment.this.updateUIForCallOutStatus(PTApp.getInstance().getCallOutStatus());
            }
        }, j);
    }

    private void dismissDelayed(long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                InviteByPhoneFragment.this.dismiss();
            }
        }, j);
    }

    private void updateUIForSupportCalloutType(int i) {
        boolean z = true;
        if (1 == i) {
            this.mBtnSelectCountryCode.setEnabled(false);
            this.mSelectedCountryCode = new CountryCodeItem("1", CountryCodeUtil.US_ISO_COUNTRY_CODE, Locale.US.getDisplayCountry());
            updateSelectedCountry();
            return;
        }
        this.mBtnSelectCountryCode.setEnabled(true);
        ArrayList<CountryCodeItem> arrayList = this.mSupportCountryCodes;
        if (arrayList != null && arrayList.size() > 0) {
            CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
            if (countryCodeItem != null && countryCodeItem.countryCode != null) {
                Iterator it = this.mSupportCountryCodes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (this.mSelectedCountryCode.countryCode.equalsIgnoreCase(((CountryCodeItem) it.next()).countryCode)) {
                        break;
                    }
                }
            }
            z = false;
            if (!z) {
                this.mSelectedCountryCode = CountryCodeItem.from((CountryCodeItem) this.mSupportCountryCodes.get(0));
                PreferenceUtil.saveStringValue(PreferenceUtil.CALLOUT_INVITE_SELECT_COUNTRY, null);
                s_lastCalloutNumber = null;
                this.mEdtNumber.setText(null);
                s_lastCalloutName = null;
                this.mEdtName.setText(null);
            }
        }
        updateSelectedCountry();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCall) {
            onClickBtnCall();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSelectCountryCode) {
            onClickBtnSelectCountryCode();
        } else if (id == C4558R.C4560id.btnHangup) {
            onClickBtnHangup();
        } else if (id == C4558R.C4560id.btnSelectPhoneNumber) {
            onClickBtnSelectPhoneNumber();
        }
    }

    private void onClickBtnSelectCountryCode() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        ArrayList<CountryCodeItem> arrayList = null;
        int i = this.mSupportCalloutType;
        if (i == 1) {
            arrayList = new ArrayList<>();
            arrayList.add(new CountryCodeItem("1", CountryCodeUtil.US_ISO_COUNTRY_CODE, Locale.US.getDisplayCountry()));
        } else if (i == 2) {
            arrayList = this.mSupportCountryCodes;
        }
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (!(countryCodeItem == null || arrayList == null)) {
            int indexOf = arrayList.indexOf(countryCodeItem);
            if (indexOf != -1) {
                ((CountryCodeItem) arrayList.get(indexOf)).isSelected = true;
            }
        }
        SelectCountryCodeFragment.showAsActivity(this, arrayList, true, 100);
    }

    private void onClickBtnSelectPhoneNumber() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        ArrayList<CountryCodeItem> arrayList = null;
        int i = this.mSupportCalloutType;
        if (i == 1) {
            arrayList = new ArrayList<>();
            arrayList.add(new CountryCodeItem("1", CountryCodeUtil.US_ISO_COUNTRY_CODE, Locale.US.getDisplayCountry()));
        } else if (i == 2) {
            arrayList = this.mSupportCountryCodes;
        }
        SelectPhoneNumberFragment.showAsActivity(this, arrayList, 101);
    }

    private void onClickBtnCall() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        String userName = getUserName();
        String fullPhoneNumber = getFullPhoneNumber();
        if (!StringUtil.isEmptyOrNull(fullPhoneNumber) && !StringUtil.isEmptyOrNull(userName)) {
            PTApp.getInstance().inviteCallOutUser(fullPhoneNumber, userName);
            saveDataAsDefault();
        }
    }

    private void saveDataAsDefault() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            countryCodeItem.savePreference(PreferenceUtil.CALLOUT_INVITE_SELECT_COUNTRY);
        }
        s_lastCalloutNumber = getPhoneNumber();
        s_lastCalloutName = getUserName();
    }

    private void onClickBtnHangup() {
        PTApp.getInstance().cancelCallOut();
    }

    private String getSelectedCountryCode() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem == null) {
            return null;
        }
        return countryCodeItem.countryCode;
    }

    private String getSelectedISOCountryCode() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem == null) {
            return "";
        }
        return countryCodeItem.isoCountryCode;
    }

    @NonNull
    private String getUserName() {
        return this.mEdtName.getText().toString().trim();
    }

    private String getPhoneNumber() {
        String obj = this.mEdtNumber.getText().toString();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < obj.length(); i++) {
            char charAt = obj.charAt(i);
            if (charAt >= '0' && charAt <= '9') {
                sb.append(charAt);
            } else if (charAt != '+' || sb.length() != 0) {
                if (charAt == ',' || charAt == ';') {
                    break;
                }
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    private String getFullPhoneNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("+");
        sb.append(getSelectedCountryCode());
        sb.append(getPhoneNumber());
        return sb.toString();
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        finishFragment(0);
    }
}
