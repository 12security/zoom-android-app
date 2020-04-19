package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodePT;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CountryCodeUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SelectCallOutNumberFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_SELECT_COUTRY_CODE = 100;
    private Button mBtnSave = null;
    private View mBtnSelectCountryCode = null;
    private EditText mEdtNumber = null;
    @Nullable
    private CountryCodeItem mSelectedCountryCode;
    private TextView mTxtCountryCode;

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, SelectCallOutNumberFragment.class.getName(), bundle, i, true, 1);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_select_call_out_number, viewGroup, false);
        this.mBtnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.mEdtNumber = (EditText) inflate.findViewById(C4558R.C4560id.edtNumber);
        this.mBtnSelectCountryCode = inflate.findViewById(C4558R.C4560id.btnSelectCountryCode);
        this.mTxtCountryCode = (TextView) inflate.findViewById(C4558R.C4560id.txtCountryCode);
        this.mBtnSave.setOnClickListener(this);
        this.mBtnSelectCountryCode.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        if (bundle == null) {
            loadDefaulCountry();
        } else {
            this.mSelectedCountryCode = (CountryCodeItem) bundle.get("mSelectedCountryCode");
        }
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, "");
        if (!StringUtil.isEmptyOrNull(readStringValue)) {
            this.mEdtNumber.setText(readStringValue);
            EditText editText = this.mEdtNumber;
            editText.setSelection(editText.getText().length());
        }
        updateSelectedCountry();
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mSelectedCountryCode", this.mSelectedCountryCode);
    }

    public void onResume() {
        super.onResume();
        updateSelectedCountry();
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

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSave) {
            onClickBtnSave();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnSelectCountryCode) {
            onClickBtnSelectCountryCode();
        }
    }

    private void updateSelectedCountry() {
        if (this.mSelectedCountryCode != null) {
            TextView textView = this.mTxtCountryCode;
            StringBuilder sb = new StringBuilder();
            sb.append(ZMUtils.getCountryName(this.mSelectedCountryCode.isoCountryCode));
            sb.append("(+");
            sb.append(this.mSelectedCountryCode.countryCode);
            sb.append(")");
            textView.setText(sb.toString());
        }
    }

    private void loadDefaulCountry() {
        boolean z;
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
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
                if (callinCountryCodes != null) {
                    List callinCountryCodesList = callinCountryCodes.getCallinCountryCodesList();
                    if (!CollectionsUtil.isListEmpty(callinCountryCodesList)) {
                        Iterator it = callinCountryCodesList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z = false;
                                break;
                            }
                            if (this.mSelectedCountryCode.isoCountryCode.equalsIgnoreCase(((CountryCodePT) it.next()).getId())) {
                                z = true;
                                break;
                            }
                        }
                        if (!z) {
                            CountryCodePT countryCodePT = (CountryCodePT) callinCountryCodesList.get(0);
                            String code = countryCodePT.getCode();
                            CountryCodeItem countryCodeItem2 = new CountryCodeItem((code == null || !code.startsWith("+")) ? code : code.substring(1), countryCodePT.getId(), countryCodePT.getName(), countryCodePT.getNumber(), countryCodePT.getDisplaynumber(), countryCodePT.getCalltype());
                            this.mSelectedCountryCode = countryCodeItem2;
                        }
                    }
                }
            }
        }
    }

    private void onClickBtnSelectCountryCode() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
            if (callinCountryCodes != null) {
                List<CountryCodePT> callinCountryCodesList = callinCountryCodes.getCallinCountryCodesList();
                if (!CollectionsUtil.isListEmpty(callinCountryCodesList)) {
                    ArrayList arrayList = new ArrayList();
                    for (CountryCodePT countryCodePT : callinCountryCodesList) {
                        String code = countryCodePT.getCode();
                        if (code.startsWith("+")) {
                            code = code.substring(1);
                        }
                        CountryCodeItem countryCodeItem = new CountryCodeItem(code, countryCodePT.getId(), countryCodePT.getName(), countryCodePT.getNumber(), countryCodePT.getDisplaynumber(), countryCodePT.getCalltype());
                        arrayList.add(countryCodeItem);
                    }
                    SelectCountryCodeFragment.showAsActivity(this, arrayList, false, 100);
                }
            }
        }
    }

    private void onClickBtnSave() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        CountryCodeItem countryCodeItem = this.mSelectedCountryCode;
        if (countryCodeItem != null) {
            countryCodeItem.savePreference(PreferenceUtil.CALLME_SELECT_COUNTRY);
        }
        String phoneNumber = getPhoneNumber();
        PreferenceUtil.saveStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, phoneNumber);
        if (activity != null) {
            Intent intent = new Intent();
            intent.putExtra(PreferenceUtil.CALLME_SELECT_COUNTRY, this.mSelectedCountryCode);
            intent.putExtra(PreferenceUtil.CALLME_PHONE_NUMBER, phoneNumber);
            activity.setResult(-1, intent);
            dismiss();
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

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }
}
