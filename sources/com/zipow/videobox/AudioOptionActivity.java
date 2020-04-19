package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.confapp.meeting.AudioOptionParcelItem;
import com.zipow.videobox.fragment.AudioOptionFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class AudioOptionActivity extends ZMActivity {
    public static final String ARG_SELECT_AUDIO_OPTION_ITEM = "ARG_SELECT_AUDIO_OPTION_ITEM";
    public static final String RESULT_SELECT_AUDIO_OPTION_ITEM = "RESULT_SELECT_AUDIO_OPTION_ITEM";

    public static void show(@Nullable Fragment fragment, int i, AudioOptionParcelItem audioOptionParcelItem) {
        if (fragment != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                Intent intent = new Intent(activity, AudioOptionActivity.class);
                intent.putExtra(ARG_SELECT_AUDIO_OPTION_ITEM, audioOptionParcelItem);
                ActivityStartHelper.startActivityForResult(fragment, intent, i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_slide_out_bottom);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        disableFinishActivityByGesture(true);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            AudioOptionFragment.showInActivity(this, (AudioOptionParcelItem) getIntent().getParcelableExtra(ARG_SELECT_AUDIO_OPTION_ITEM));
        }
    }

    public void onOkDone(AudioOptionParcelItem audioOptionParcelItem) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_SELECT_AUDIO_OPTION_ITEM, audioOptionParcelItem);
        setResult(-1, intent);
        finish();
    }
}
