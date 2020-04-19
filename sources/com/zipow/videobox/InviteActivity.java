package com.zipow.videobox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.InviteFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class InviteActivity extends ZMActivity {
    public static final String RESULT_INVITATIONS_COUNT = "invitations_count";

    public static void show(@NonNull Context context, String str, long j) {
        show(context, str, j, false);
    }

    public static void show(@NonNull Context context, String str, long j, boolean z) {
        Intent intent = new Intent(context, InviteActivity.class);
        intent.putExtra(InviteFragment.ARG_MEETING_NUMBER, j);
        intent.putExtra(InviteFragment.ARG_MEETING_ID, str);
        intent.putExtra(InviteFragment.ARG_SELECT_FROM_ADDRBOOK, z);
        ActivityStartHelper.startActivityForeground(context, intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C4558R.anim.zm_slide_out_bottom);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        getWindow().addFlags(2097280);
        if (bundle == null) {
            InviteFragment inviteFragment = new InviteFragment();
            Intent intent = getIntent();
            if (intent != null) {
                long longExtra = intent.getLongExtra(InviteFragment.ARG_MEETING_NUMBER, 0);
                String stringExtra = intent.getStringExtra(InviteFragment.ARG_MEETING_ID);
                boolean booleanExtra = intent.getBooleanExtra(InviteFragment.ARG_SELECT_FROM_ADDRBOOK, false);
                boolean booleanExtra2 = intent.getBooleanExtra(InviteFragment.ARG_SELECT_FROM_ZOOMROOMS, false);
                Bundle bundle2 = new Bundle();
                bundle2.putLong(InviteFragment.ARG_MEETING_NUMBER, longExtra);
                bundle2.putString(InviteFragment.ARG_MEETING_ID, stringExtra);
                bundle2.putBoolean(InviteFragment.ARG_SELECT_FROM_ADDRBOOK, booleanExtra);
                bundle2.putBoolean(InviteFragment.ARG_SELECT_FROM_ZOOMROOMS, booleanExtra2);
                inviteFragment.setArguments(bundle2);
            }
            getSupportFragmentManager().beginTransaction().add(16908290, inviteFragment, InviteFragment.class.getName()).commit();
        }
    }

    public void onSentInvitationDone(int i) {
        Intent intent = new Intent();
        intent.putExtra("invitations_count", i);
        setResult(-1, intent);
        finish();
    }

    public boolean onSearchRequested() {
        InviteFragment inviteFragment = (InviteFragment) getSupportFragmentManager().findFragmentByTag(InviteFragment.class.getName());
        if (inviteFragment != null) {
            return inviteFragment.onSearchRequested();
        }
        return true;
    }
}
