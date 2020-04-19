package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.SetPasswordFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;

public class SetPasswordActivity extends ZMActivity {
    private static final String ARG_CODE = "code";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_TYPE = "type";
    private static final String ARG_UNAME = "uname";

    public static void show(@NonNull ZMActivity zMActivity, String str, String str2, String str3) {
        Intent intent = new Intent(zMActivity, SetPasswordActivity.class);
        intent.putExtra(ARG_UNAME, str);
        intent.putExtra("email", str2);
        intent.putExtra("code", str3);
        ActivityStartHelper.startActivityForeground(zMActivity, intent);
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
            SetPasswordFragment.show(this, intent.getStringExtra(ARG_UNAME), intent.getStringExtra("email"), intent.getStringExtra("code"));
        }
    }
}
