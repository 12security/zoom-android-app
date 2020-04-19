package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.dialog.FreeMeetingEndDialog;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;

public class FreeMeetingEndActivity extends ZMActivity {
    private static final String ARG_FREE_MEETING_TIMES = "arg_free_meeting_times";
    private static final String ARG_UPGRADE_URL = "arg_upgrade_url";
    public static final int AUTO_UPGRADE_FREE_MEETING_TIMES = 3;
    public static final int REQUEST_SCHEDULE = 1000;

    public static void show(@NonNull Context context, int i, @Nullable String str) {
        Intent intent = new Intent(context, FreeMeetingEndActivity.class);
        intent.setFlags(411041792);
        intent.putExtra(ARG_FREE_MEETING_TIMES, i);
        intent.putExtra(ARG_UPGRADE_URL, str);
        try {
            ActivityStartHelper.startActivityForeground(context, intent);
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VideoBoxApplication.getInstance() == null) {
            finish();
            return;
        }
        Intent intent = getIntent();
        int intExtra = intent.getIntExtra(ARG_FREE_MEETING_TIMES, -1);
        String stringExtra = intent.getStringExtra(ARG_UPGRADE_URL);
        boolean z = true;
        boolean z2 = intExtra <= 0 || intExtra > 3;
        if (!z2 && !StringUtil.isEmptyOrNull(stringExtra)) {
            FreeMeetingEndDialog.showDialog(getSupportFragmentManager(), intExtra, stringExtra);
            z = z2;
        }
        if (z) {
            finish();
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1000 && i2 == -1) {
            if (intent != null) {
                onScheduleSuccess((ScheduledMeetingItem) intent.getSerializableExtra("meetingItem"));
            }
        } else if (i2 == 0) {
            finish();
        }
    }

    private void onScheduleSuccess(final ScheduledMeetingItem scheduledMeetingItem) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("onScheduleSuccess") {
            public void run(IUIElement iUIElement) {
                MeetingInfoActivity.show((ZMActivity) iUIElement, scheduledMeetingItem, true, 104);
            }
        });
    }
}
