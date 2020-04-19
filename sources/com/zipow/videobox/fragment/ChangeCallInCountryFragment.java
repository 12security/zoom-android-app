package com.zipow.videobox.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.SelectCallInCountryFragment.CallInNumberItem;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodePT;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IProfileListener;
import com.zipow.videobox.ptapp.PTUI.SimpleProfileListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ChangeCallInCountryFragment extends SelectCallInCountryFragment {
    /* access modifiers changed from: private */
    @NonNull
    public static String WAITING_DIALOG_TAG = "select_callin_waiting_dialog";
    @NonNull
    private IProfileListener mListener = new SimpleProfileListener() {
        public void OnProfileFieldUpdated(@NonNull String str, int i, int i2, String str2) {
            if (!StringUtil.isEmptyOrNull(str) && str.equals(ChangeCallInCountryFragment.this.mRequestID)) {
                UIUtil.dismissWaitingDialog(ChangeCallInCountryFragment.this.getFragmentManager(), ChangeCallInCountryFragment.WAITING_DIALOG_TAG);
                ChangeCallInCountryFragment.this.handleProfileUpdate(i, i2);
            }
        }
    };
    /* access modifiers changed from: private */
    public String mRequestID;

    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, ChangeCallInCountryFragment.class.getName(), bundle, i, false, 1);
        }
    }

    /* access modifiers changed from: private */
    public void handleProfileUpdate(int i, int i2) {
        if (i == 0) {
            dismiss();
        } else {
            showErrorMessage(i);
        }
    }

    public void onResume() {
        PTUI.getInstance().addProfileListener(this.mListener);
        super.onResume();
    }

    public void onPause() {
        PTUI.getInstance().removeProfileListener(this.mListener);
        super.onPause();
    }

    public void loadAllCountryCodes(@Nullable Map<String, CallInNumberItem> map) {
        if (map != null) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                CountryCodelistProto callinCountryCodes = currentUserProfile.getCallinCountryCodes();
                if (callinCountryCodes != null) {
                    List<CountryCodePT> callinCountryCodesList = callinCountryCodes.getCallinCountryCodesList();
                    if (callinCountryCodesList != null) {
                        for (CountryCodePT countryCodePT : callinCountryCodesList) {
                            String id = countryCodePT.getId();
                            if (!map.containsKey(id)) {
                                map.put(id, new CallInNumberItem(countryCodePT.getName(), countryCodePT.getCode(), countryCodePT.getId()));
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSelectPhoneNumber(@Nullable CallInNumberItem callInNumberItem) {
        if (callInNumberItem != null) {
            this.mRequestID = PTApp.getInstance().modifyCountryCode(callInNumberItem.countryId);
            if (StringUtil.isEmptyOrNull(this.mRequestID)) {
                showErrorMessage(5000);
            } else {
                UIUtil.showWaitingDialog(getFragmentManager(), C4558R.string.zm_msg_waiting, WAITING_DIALOG_TAG);
            }
        }
    }

    private void showErrorMessage(int i) {
        String str;
        if (i != 0) {
            if (i == 5000 || i == 5003) {
                str = getString(C4558R.string.zm_lbl_profile_change_fail_cannot_connect_service);
            } else {
                str = getString(C4558R.string.zm_lbl_callin_country_change_fail_104883);
            }
            String string = getString(C4558R.string.zm_title_callin_country_change_fail_104883);
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            ZMErrorMessageDialog.show(getFragmentManager(), string, arrayList, "ChangeCallInCountryFragment error dialog");
        }
    }
}
