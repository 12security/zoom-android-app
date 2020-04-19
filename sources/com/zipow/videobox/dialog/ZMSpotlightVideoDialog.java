package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ZMSpotlightVideoDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    public void stayMuted() {
    }

    public static void showSpotlightVideoDialog(ZMActivity zMActivity) {
        new ZMSpotlightVideoDialog().show(zMActivity.getSupportFragmentManager(), ZMSpotlightVideoDialog.class.getName());
    }

    public static void dismiss(FragmentManager fragmentManager) {
        ZMSpotlightVideoDialog spotlightVideoDialog = getSpotlightVideoDialog(fragmentManager);
        if (spotlightVideoDialog != null) {
            spotlightVideoDialog.dismiss();
        }
    }

    private static ZMSpotlightVideoDialog getSpotlightVideoDialog(FragmentManager fragmentManager) {
        return (ZMSpotlightVideoDialog) fragmentManager.findFragmentByTag(ZMSpotlightVideoDialog.class.getName());
    }

    public ZMSpotlightVideoDialog() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        int i = C4558R.string.zm_title_host_spotlight_unmute_98431;
        int i2 = C4558R.string.zm_btn_unmute;
        CmmAudioStatus mySelfAudioStatus = ConfLocalHelper.getMySelfAudioStatus();
        if (mySelfAudioStatus == null || mySelfAudioStatus.getAudiotype() == 2) {
            i = C4558R.string.zm_title_host_spotlight_join_audio_98431;
            i2 = C4558R.string.zm_btn_join_audio_98431;
        }
        return new Builder(getActivity()).setTitle(i).setPositiveButton(i2, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMSpotlightVideoDialog.this.unmuteNow();
            }
        }).setNegativeButton(C4558R.string.zm_btn_later, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMSpotlightVideoDialog.this.stayMuted();
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void unmuteNow() {
        ConfActivity confActivity = (ConfActivity) getActivity();
        if (confActivity != null) {
            confActivity.onHostAskUnmute();
        }
    }
}
