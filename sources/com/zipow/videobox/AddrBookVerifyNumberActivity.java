package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.AddrBookVerifyNumberFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;

public class AddrBookVerifyNumberActivity extends ZMActivity {
    private static final String ARG_COUNTRY_CODE = "countryCode";
    private static final String ARG_PHONE_NUMBER = "phoneNumber";

    public static void show(@NonNull ZMActivity zMActivity, String str, String str2, int i) {
        Intent intent = new Intent(zMActivity, AddrBookVerifyNumberActivity.class);
        intent.putExtra("countryCode", str);
        intent.putExtra("phoneNumber", str2);
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            Intent intent = getIntent();
            AddrBookVerifyNumberFragment.showInActivity(this, intent.getStringExtra("countryCode"), intent.getStringExtra("phoneNumber"));
        }
    }

    public void onResume() {
        super.onResume();
        if (!PTApp.getInstance().isWebSignedOn()) {
            setResult(0);
            finish();
        }
    }
}
