package com.zipow.videobox.poll;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.poll.PollingMgr;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class WebinarPollingActivity extends PollingActivity {
    public static void show(@Nullable Activity activity, String str, int i, int i2) {
        if (activity != null && !StringUtil.isEmptyOrNull(str)) {
            Intent intent = new Intent(activity, WebinarPollingActivity.class);
            intent.putExtra("pollingId", str);
            if (i == 1 || i == 2) {
                intent.putExtra("isReadOnly", true);
                intent.putExtra("readOnlyMessageRes", C4558R.string.zm_polling_msg_host_and_panelist_cannot_vote);
            }
            ActivityStartHelper.startActivityForResult(activity, intent, i2);
            activity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
        }
    }

    /* access modifiers changed from: protected */
    public void loadPollingMgr() {
        PollingMgr pollObj = ConfMgr.getInstance().getPollObj();
        if (pollObj != null) {
            setPollingMgr(pollObj);
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C4558R.anim.zm_slide_out_bottom);
    }
}
