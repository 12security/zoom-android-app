package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.IMAddrBookSettingFragment;
import com.zipow.videobox.fragment.SelectCountryCodeFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookSettingActivity extends ZMActivity {
    private static final String ARG_IS_PHONE_NUMBER_REGISTERED_ON_START = "isPhoneNumberRegisteredOnStart";
    public static final String ARG_RESULT_DISABLED = "disabled";
    public static final String ARG_RESULT_ENABLED = "enabled";
    public static final int REQUEST_SET_PHONE_NUMBER = 100;
    private boolean mIsPhoneNumberRegisteredOnStart = false;

    public static void show(@NonNull ZMActivity zMActivity, int i) {
        Intent intent = new Intent(zMActivity, AddrBookSettingActivity.class);
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        intent.putExtra(ARG_IS_PHONE_NUMBER_REGISTERED_ON_START, aBContactsHelper != null && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()));
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }

    public static void show(Fragment fragment, int i) {
        ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, AddrBookSettingActivity.class);
            ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
            intent.putExtra(ARG_IS_PHONE_NUMBER_REGISTERED_ON_START, aBContactsHelper != null && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()));
            ActivityStartHelper.startActivityForResult(fragment, intent, i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public void finish() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper == null || StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
            if (this.mIsPhoneNumberRegisteredOnStart) {
                Intent intent = new Intent();
                intent.putExtra("disabled", true);
                setResult(-1, intent);
            } else {
                setResult(0);
            }
        } else if (!this.mIsPhoneNumberRegisteredOnStart) {
            ABContactsHelper.setAddrBookEnabledDone(true);
            Intent intent2 = new Intent();
            intent2.putExtra(ARG_RESULT_ENABLED, true);
            setResult(-1, intent2);
        } else {
            setResult(0);
        }
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent != null) {
            this.mIsPhoneNumberRegisteredOnStart = intent.getBooleanExtra(ARG_IS_PHONE_NUMBER_REGISTERED_ON_START, false);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            IMAddrBookSettingFragment.showInActivity(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (!PTApp.getInstance().isWebSignedOn()) {
            setResult(0);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        String str;
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            String str2 = null;
            if (intent != null) {
                str2 = intent.getStringExtra(SelectCountryCodeFragment.RESULT_ARG_COUNTRY_CODE);
                str = intent.getStringExtra("number");
            } else {
                str = null;
            }
            IMAddrBookSettingFragment findFragment = IMAddrBookSettingFragment.findFragment(this);
            if (findFragment != null) {
                findFragment.onSetPhoneNumberDone(str2, str);
            }
        }
    }
}
