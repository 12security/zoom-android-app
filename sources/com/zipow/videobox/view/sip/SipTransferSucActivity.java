package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.ActivityStartHelper;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SipTransferSucActivity extends ZMActivity {
    private static final int MSG_TRANSFER_EXPIRE = 5;
    @NonNull
    private Handler mHandler = new TransferHandler(this);

    private static class TransferHandler extends Handler {
        private WeakReference<SipTransferSucActivity> activity;

        public TransferHandler(SipTransferSucActivity sipTransferSucActivity) {
            this.activity = new WeakReference<>(sipTransferSucActivity);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            SipTransferSucActivity sipTransferSucActivity = (SipTransferSucActivity) this.activity.get();
            if (sipTransferSucActivity != null) {
                sipTransferSucActivity.finish();
            }
        }
    }

    public static void show(@NonNull Activity activity) {
        ActivityStartHelper.startActivityForeground(activity, new Intent(activity, SipTransferSucActivity.class));
        activity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(2097280);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        setContentView(C4558R.layout.zm_sip_transfer_suc_ac);
        postTransferExpire();
    }

    private void postTransferExpire() {
        this.mHandler.sendEmptyMessageDelayed(5, 3000);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C4558R.anim.zm_slide_out_bottom);
    }
}
