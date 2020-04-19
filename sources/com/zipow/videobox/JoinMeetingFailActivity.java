package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.zipow.videobox.dialog.JoinFailedDialog;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;

public class JoinMeetingFailActivity extends ZMActivity {
    private static final String ARG_ERROR_CODE = "errorCode";
    private static final String ARG_TAG = "tag";
    private static final String ARG_VALUE = "value";
    private static final String TAG = "JoinMeetingFailActivity";

    public static void showJoinFailedMessage(@NonNull Context context, @NonNull String str, int i, int i2) {
        Intent intent = new Intent(context, JoinMeetingFailActivity.class);
        intent.setFlags(268435456);
        intent.putExtra(ARG_TAG, str);
        intent.putExtra(ARG_ERROR_CODE, i);
        intent.putExtra("value", i2);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    public void onResume() {
        super.onResume();
        if (VideoBoxApplication.getInstance() == null) {
            finish();
            return;
        }
        Intent intent = getIntent();
        boolean z = true;
        if (intent.hasExtra(ARG_TAG) && intent.hasExtra(ARG_ERROR_CODE)) {
            JoinFailedDialog.show(getSupportFragmentManager(), intent.getStringExtra(ARG_TAG), intent.getIntExtra(ARG_ERROR_CODE, -1), intent.getIntExtra("value", -1));
            z = false;
        }
        if (z) {
            finish();
        }
    }
}
