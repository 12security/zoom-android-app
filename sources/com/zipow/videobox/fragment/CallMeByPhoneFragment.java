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
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.UserPhoneInfo;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class CallMeByPhoneFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_SELECT_COUTRY_CODE = 100;
    private static final int UPDATE_STATUS_DELAY_MILLIS = 1000;
    private Button mBtnCall = null;
    private Button mBtnHangup = null;
    private View mBtnSelectCountryCode = null;
    private IConfUIListener mConfUIListener;
    private EditText mEdtNumber = null;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsInitCallStatus = true;
    @Nullable
    private CountryCodeItem mSelectedCountryCode;
    @Nullable
    private ArrayList<CountryCodeItem> mSupportCountryCodes;
    private TextView mTxtCountryCode;
    private TextView mTxtMessage = null;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, CallMeByPhoneFragment.class.getName(), bundle, i, true, 2);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_callme_by_phone, viewGroup, false);
        this.mBtnCall = (Button) inflate.findViewById(C4558R.C4560id.btnCall);
        this.mBtnHangup = (Button) inflate.findViewById(C4558R.C4560id.btnHangup);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mBtnSelectCountryCode = inflate.findViewById(C4558R.C4560id.btnSelectCountryCode);
        this.mTxtCountryCode = (TextView) inflate.findViewById(C4558R.C4560id.txtCountryCode);
        this.mTxtMessage = (TextView) inflate.findViewById(C4558R.C4560id.txtMessage);
        this.mBtnCall.setOnClickListener(this);
        this.mBtnHangup.setOnClickListener(this);
        this.mBtnSelectCountryCode.setOnClickListener(this);
        installNumberChangeListener();
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        initCountryCodesAndStatus(bundle);
        updateCallButton();
        this.mSupportCountryCodes = getSupportCountryCodes();
        return inflate;
    }

    private void initCountryCodesAndStatus(Bundle bundle) {
        if (ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber()) {
            disableEdtNumber();
        } else if (bundle == null) {
            loadDefaultNumber();
        } else {
            this.mSelectedCountryCode = (CountryCodeItem) bundle.get("mSelectedCountryCode");
            this.mIsInitCallStatus = bundle.getBoolean("mIsInitCallStatus");
            updateSelectedCountry();
        }
    }

    private void disableEdtNumber() {
        UserPhoneInfo userPhoneInfo = ConfLocalHelper.getUserPhoneInfo();
        if (userPhoneInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(ZMUtils.getCountryName(userPhoneInfo.getCountryId()));
            sb.append("(+");
            sb.append(userPhoneInfo.getCountryCode());
            sb.append(")");
            this.mTxtCountryCode.setText(sb.toString());
            this.mSelectedCountryCode = new CountryCodeItem(userPhoneInfo.getCountryCode(), userPhoneInfo.getCountryId(), "");
            this.mEdtNumber.setText(userPhoneInfo.getPhoneNumber());
            this.mTxtMessage.setText(C4558R.string.zm_call_by_phone_tip_129757);
        } else {
            this.mTxtMessage.setText(C4558R.string.zm_call_by_phone_have_no_number_tip_129757);
            this.mBtnCall.setVisibility(8);
            this.mBtnHangup.setVisibility(8);
            this.mEdtNumber.setHint(C4558R.string.zm_call_by_phone_have_no_number_edit_hint_129757);
        }
        this.mBtnSelectCountryCode.setEnabled(false);
        this.mEdtNumber.setEnabled(false);
    }

    private ArrayList<CountryCodeItem> getSupportCountryCodes() {
        if (ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber()) {
            ArrayList<CountryCodeItem> arrayList = new ArrayList<>();
            arrayList.add(this.mSelectedCountryCode);
            return arrayList;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        ArrayList<CountryCodeItem> arrayList2 = null;
        if (confContext == null) {
            return null;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return null;
        }
        List<CountryCode> calloutCountryCodesList = meetingItem.getCalloutCountryCodesList();
        if (calloutCountryCodesList != null && calloutCountryCodesList.size() > 0) {
            arrayList2 = new ArrayList<>();
            for (CountryCode countryCode : calloutCountryCodesList) {
                String code = countryCode.getCode();
                if (code.startsWith("+")) {
                    code = code.substring(1);
                }
                CountryCodeItem countryCodeItem = new CountryCodeItem(code, countryCode.getId(), countryCode.getName(), countryCode.getNumber(), countryCode.getDisplaynumber(), countryCode.getCalltype());
                arrayList2.add(countryCodeItem);
            }
        }
        return arrayList2;
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
        }
    }

    private void updateSelectedCountry() {
        String str;
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            if (countryCodeItem.callType == 999) {
                str = this.mSelectedCountryCode.countryName.replace("@", "");
                this.mEdtNumber.setHint(C4558R.string.zm_callout_hint_internal_extension_number_107106);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(ZMUtils.getCountryName(this.mSelectedCountryCode.isoCountryCode));
                sb.append("(+");
                sb.append(this.mSelectedCountryCode.countryCode);
                sb.append(")");
                str = sb.toString();
                this.mEdtNumber.setHint(C4558R.string.zm_callout_hint_phone_number_107106);
            }
            this.mTxtCountryCode.setText(str);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onPause() {
        super.onPause();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onConfStatusChanged2(int i, long j) {
                    return CallMeByPhoneFragment.this.onConfStatusChanged2(i, j);
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            updateUIForCallOutStatus(confStatusObj.getCallMeStatus());
        }
        if (!ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber()) {
            updateUIForSupportCalloutType();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void updateCallButton() {
        this.mBtnCall.setEnabled(!StringUtil.isEmptyOrNull(getSelectedCountryCode()) && !StringUtil.isEmptyOrNull(getPhoneNumber()));
    }

    private void installNumberChangeListener() {
        this.mEdtNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                CallMeByPhoneFragment.this.updateCallButton();
            }
        });
    }

    private void loadDefaultNumber() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            this.mSelectedCountryCode = CountryCodeItem.readFromPreference(PreferenceUtil.CALLME_SELECT_COUNTRY);
            CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
            if (countryCodeItem == null || StringUtil.isEmptyOrNull(countryCodeItem.isoCountryCode)) {
                String isoCountryCode = CountryCodeUtil.getIsoCountryCode(activity);
                if (isoCountryCode != null) {
                    this.mSelectedCountryCode = new CountryCodeItem(CountryCodeUtil.isoCountryCode2PhoneCountryCode(isoCountryCode), isoCountryCode, new Locale("", isoCountryCode.toLowerCase(Locale.US)).getDisplayCountry());
                } else {
                    return;
                }
            }
            String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, "");
            if (readStringValue != null) {
                this.mEdtNumber.setText(readStringValue);
            }
            updateSelectedCountry();
        }
    }

    private void onCallOutStatusChanged(int i) {
        updateUIForCallOutStatus(i);
    }

    /* access modifiers changed from: private */
    public boolean onConfStatusChanged2(int i, long j) {
        if (i == 105) {
            onCallOutStatusChanged((int) j);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void updateUIForCallOutStatus(int i) {
        if (i != 0) {
            this.mIsInitCallStatus = false;
        }
        int i2 = C4558R.color.zm_notification_background;
        int i3 = C4558R.color.zm_black;
        this.mTxtMessage.setVisibility(i != 0 || ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber() ? 0 : 8);
        switch (i) {
            case 0:
                if (this.mIsInitCallStatus && (!ConfLocalHelper.isOnlyUseTelephoneAndUseOwnPhoneNumber() || ConfLocalHelper.getUserPhoneInfo() != null)) {
                    i2 = C4558R.color.zm_transparent;
                    i3 = C4558R.color.zm_black_2;
                    break;
                }
            case 1:
                this.mTxtMessage.setText(getString(C4558R.string.zm_callout_msg_calling, getCallingNumber()));
                break;
            case 2:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_ringing);
                break;
            case 3:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_call_accepted);
                break;
            case 4:
            case 13:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_busy);
                updateCallStatusDelayed(1000);
                break;
            case 5:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_not_available);
                updateCallStatusDelayed(1000);
                break;
            case 6:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_user_hangup);
                updateCallStatusDelayed(1000);
                break;
            case 7:
            case 9:
                this.mTxtMessage.setText(getString(C4558R.string.zm_callout_msg_fail_to_call, getCallingNumber()));
                updateCallStatusDelayed(1000);
                break;
            case 8:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_success);
                dismissDelayed(1000);
                break;
            case 10:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_cancel_call);
                break;
            case 11:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_call_canceled);
                updateCallStatusDelayed(1000);
                break;
            case 12:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_cancel_call_fail);
                updateCallStatusDelayed(1000);
                break;
            case 14:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_no_host);
                updateCallStatusDelayed(1000);
                break;
            case 15:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_high_rate);
                updateCallStatusDelayed(1000);
                break;
            case 16:
                this.mTxtMessage.setText(C4558R.string.zm_callout_msg_block_too_frequent);
                updateCallStatusDelayed(1000);
                break;
        }
        Context context = getContext();
        if (context != null && this.mTxtMessage.getVisibility() == 0) {
            this.mTxtMessage.setBackgroundResource(i2);
            this.mTxtMessage.setTextColor(context.getResources().getColor(i3));
        }
        updateButtonsForCallOutStatus(i);
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
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    CallMeByPhoneFragment.this.updateUIForCallOutStatus(confStatusObj.getCallMeStatus());
                }
            }
        }, j);
    }

    private void dismissDelayed(long j) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                CallMeByPhoneFragment.this.dismiss();
            }
        }, j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x006d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateUIForSupportCalloutType() {
        /*
            r5 = this;
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.CmmConfContext r0 = r0.getConfContext()
            if (r0 != 0) goto L_0x000b
            return
        L_0x000b:
            com.zipow.videobox.ptapp.MeetingInfoProtos$MeetingInfoProto r0 = r0.getMeetingItem()
            if (r0 != 0) goto L_0x0012
            return
        L_0x0012:
            int r0 = r0.getSupportCallOutType()
            r1 = 0
            r2 = 1
            if (r2 != r0) goto L_0x0034
            android.view.View r0 = r5.mBtnSelectCountryCode
            r0.setEnabled(r1)
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r0 = new com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem
            java.lang.String r1 = "1"
            java.lang.String r2 = "US"
            java.util.Locale r3 = java.util.Locale.US
            java.lang.String r3 = r3.getDisplayCountry()
            r0.<init>(r1, r2, r3)
            r5.mSelectedCountryCode = r0
            r5.updateSelectedCountry()
            goto L_0x0090
        L_0x0034:
            android.view.View r0 = r5.mBtnSelectCountryCode
            r0.setEnabled(r2)
            java.util.ArrayList<com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem> r0 = r5.mSupportCountryCodes
            if (r0 == 0) goto L_0x008d
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x008d
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r0 = r5.mSelectedCountryCode
            if (r0 == 0) goto L_0x006a
            java.lang.String r0 = r0.countryCode
            if (r0 == 0) goto L_0x006a
            java.util.ArrayList<com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem> r0 = r5.mSupportCountryCodes
            java.util.Iterator r0 = r0.iterator()
        L_0x0051:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x006a
            java.lang.Object r3 = r0.next()
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r3 = (com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem) r3
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r4 = r5.mSelectedCountryCode
            java.lang.String r4 = r4.isoCountryCode
            java.lang.String r3 = r3.isoCountryCode
            boolean r3 = r4.equalsIgnoreCase(r3)
            if (r3 == 0) goto L_0x0051
            goto L_0x006b
        L_0x006a:
            r2 = 0
        L_0x006b:
            if (r2 != 0) goto L_0x008d
            java.util.ArrayList<com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem> r0 = r5.mSupportCountryCodes
            java.lang.Object r0 = r0.get(r1)
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r0 = (com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem) r0
            com.zipow.videobox.fragment.SelectCountryCodeFragment$CountryCodeItem r0 = com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem.from(r0)
            r5.mSelectedCountryCode = r0
            java.lang.String r0 = "callme.select_country"
            r1 = 0
            com.zipow.videobox.util.PreferenceUtil.saveStringValue(r0, r1)
            java.lang.String r0 = "callme.phone_number"
            com.zipow.videobox.util.PreferenceUtil.saveStringValue(r0, r1)
            android.widget.EditText r0 = r5.mEdtNumber
            java.lang.String r1 = ""
            r0.setText(r1)
        L_0x008d:
            r5.updateSelectedCountry()
        L_0x0090:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.CallMeByPhoneFragment.updateUIForSupportCalloutType():void");
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
        }
    }

    private void onClickBtnSelectCountryCode() {
        SelectCountryCodeFragment.showAsActivity(this, this.mSupportCountryCodes, true, 100);
    }

    private void onClickBtnCall() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        String fullPhoneNumber = getFullPhoneNumber();
        if (!StringUtil.isEmptyOrNull(fullPhoneNumber)) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                confStatusObj.startCallOut(fullPhoneNumber);
            }
            saveDataAsDefault();
        }
    }

    private void saveDataAsDefault() {
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            countryCodeItem.savePreference(PreferenceUtil.CALLME_SELECT_COUNTRY);
        }
        PreferenceUtil.saveStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, getPhoneNumber());
    }

    private void onClickBtnHangup() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            confStatusObj.hangUp();
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

    private String getCallingNumber() {
        return getFullPhoneNumber();
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }
}
