package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MeetingInfoFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class MeetingInfoActivity extends ZMActivity {
    private static final String ARG_AUTO_ADD_INVITEE = "autoAddInvitee";
    private static final String ARG_MEETING_ITEM = "meetingItem";
    public static final int REQUEST_EDIT = 103;

    public static void show(@NonNull ZMActivity zMActivity, ScheduledMeetingItem scheduledMeetingItem, boolean z, int i) {
        Intent intent = new Intent(zMActivity, MeetingInfoActivity.class);
        intent.putExtra(ARG_MEETING_ITEM, scheduledMeetingItem);
        intent.putExtra(ARG_AUTO_ADD_INVITEE, z);
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
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
            MeetingInfoFragment.showInActivity(this, (ScheduledMeetingItem) intent.getSerializableExtra(ARG_MEETING_ITEM), intent.getBooleanExtra(ARG_AUTO_ADD_INVITEE, false));
        }
    }

    private void finishSubActivities() {
        finishActivity(103);
    }

    public void onResume() {
        super.onResume();
        finishSubActivities();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 103 && i2 == -1 && intent != null) {
            ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) intent.getSerializableExtra(ARG_MEETING_ITEM);
            MeetingInfoFragment meetingInfoFragment = MeetingInfoFragment.getMeetingInfoFragment(getSupportFragmentManager());
            if (meetingInfoFragment != null) {
                meetingInfoFragment.onEditSuccess(scheduledMeetingItem);
            }
        }
    }
}
