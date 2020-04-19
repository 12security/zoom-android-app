package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.JoinConfFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class JoinConfActivity extends ZMActivity {
    public static final String ARG_CONF_NUMBER = "hangoutNumber";
    public static final String ARG_SCREEN_NAME = "screenName";
    public static final String ARG_URL_ACTION = "urlAction";

    public static void showJoinByNumber(@Nullable Context context, String str, String str2) {
        if (context != null) {
            Intent intent = new Intent(context, JoinConfActivity.class);
            intent.addFlags(131072);
            intent.putExtra("hangoutNumber", str);
            intent.putExtra("screenName", str2);
            ActivityStartHelper.startActivityForeground(context, intent);
            if (context instanceof Activity) {
                ((Activity) context).overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_slide_out_bottom);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_slide_out_bottom);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            JoinConfFragment joinConfFragment = new JoinConfFragment();
            Intent intent = getIntent();
            if (intent != null) {
                String stringExtra = intent.getStringExtra("hangoutNumber");
                String stringExtra2 = intent.getStringExtra("screenName");
                String stringExtra3 = intent.getStringExtra("urlAction");
                Bundle bundle2 = new Bundle();
                bundle2.putString("hangoutNumber", stringExtra);
                bundle2.putString("screenName", stringExtra2);
                bundle2.putString("urlAction", stringExtra3);
                joinConfFragment.setArguments(bundle2);
            }
            getSupportFragmentManager().beginTransaction().add(16908290, joinConfFragment, JoinConfFragment.class.getName()).commit();
        }
    }

    public void onResume() {
        super.onResume();
        if (PTApp.getInstance().hasActiveCall()) {
            finish();
        }
    }
}
