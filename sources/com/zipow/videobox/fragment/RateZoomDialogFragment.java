package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class RateZoomDialogFragment extends ZMDialogFragment {
    public static void show(FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        RateZoomDialogFragment rateZoomDialogFragment = new RateZoomDialogFragment();
        rateZoomDialogFragment.setArguments(bundle);
        rateZoomDialogFragment.show(fragmentManager, RateZoomDialogFragment.class.getName());
    }

    public RateZoomDialogFragment() {
        setCancelable(false);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Builder(getActivity()).setMessage(C4558R.string.zm_alert_rate_on_googleplay_content_58802).setTitle(C4558R.string.zm_alert_rate_on_googleplay_title_58802).setNegativeButton(C4558R.string.zm_btn_rate_on_googleplay_no_58802, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(C4558R.string.zm_btn_rate_on_googleplay_yes_58802, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZoomRateHelper.launchGooglePlayAppDetail(RateZoomDialogFragment.this.getActivity());
            }
        }).create();
    }
}
