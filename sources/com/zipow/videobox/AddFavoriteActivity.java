package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.AddFavoriteFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AddFavoriteActivity extends ZMActivity {
    public static final String RESULT_INVITATIONS_COUNT = "invitations_count";

    public static void show(@NonNull Activity activity, int i) {
        ActivityStartHelper.startActivityForResult(activity, new Intent(activity, AddFavoriteActivity.class), i);
        activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
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
        getWindow().addFlags(2097280);
        if (bundle == null) {
            AddFavoriteFragment addFavoriteFragment = new AddFavoriteFragment();
            if (getIntent() != null) {
                addFavoriteFragment.setArguments(new Bundle());
            }
            getSupportFragmentManager().beginTransaction().add(16908290, addFavoriteFragment, AddFavoriteFragment.class.getName()).commit();
        }
    }

    public void onSentInvitationDone(int i) {
        UIUtil.closeSoftKeyboard(this, getWindow().getDecorView());
        Intent intent = new Intent();
        intent.putExtra("invitations_count", i);
        setResult(-1, intent);
        finish();
    }

    public boolean onSearchRequested() {
        AddFavoriteFragment addFavoriteFragment = (AddFavoriteFragment) getSupportFragmentManager().findFragmentByTag(AddFavoriteFragment.class.getName());
        if (addFavoriteFragment != null) {
            return addFavoriteFragment.onSearchRequested();
        }
        return true;
    }
}
