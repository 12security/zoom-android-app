package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.fragment.ScheduleFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class ScheduleActivity extends ZMActivity {
    private static final String ARG_IS_EDIT_MEETING = "isEditMeeting";
    private static final String ARG_MEETING_ITEM = "meetingItem";
    public static final int REQUEST_SELECT_END_REPEAT = 100;

    public static void show(@NonNull ZMActivity zMActivity, int i) {
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, new Intent(zMActivity, ScheduleActivity.class), i);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
    }

    public static void show(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                ActivityStartHelper.startActivityForResult(fragment, new Intent(activity, ScheduleActivity.class), i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
            }
        }
    }

    public static void showEditMeeting(@NonNull ZMActivity zMActivity, int i, ScheduledMeetingItem scheduledMeetingItem) {
        Intent intent = new Intent(zMActivity, ScheduleActivity.class);
        intent.putExtra(ARG_IS_EDIT_MEETING, true);
        intent.putExtra(ARG_MEETING_ITEM, scheduledMeetingItem);
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
    }

    public void finish() {
        super.finish();
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
            Intent intent = getIntent();
            boolean booleanExtra = intent.getBooleanExtra(ARG_IS_EDIT_MEETING, false);
            ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) intent.getSerializableExtra(ARG_MEETING_ITEM);
            if (booleanExtra) {
                ScheduleFragment.showEditMeetingInActivity(this, scheduledMeetingItem);
            } else {
                ScheduleFragment.showInActivity(this);
            }
        }
    }
}
