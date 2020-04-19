package com.zipow.videobox.view.sip.sms;

import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;

public class PBXMessageSearchFragment extends ZMDialogFragment implements OnClickListener {
    public void onClick(View view) {
    }

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, new PBXMessageSearchFragment(), PBXMessageSearchFragment.class.getName()).commit();
        }
    }
}
