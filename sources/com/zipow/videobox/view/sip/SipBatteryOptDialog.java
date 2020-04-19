package com.zipow.videobox.view.sip;

import android.app.Dialog;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SipBatteryOptDialog extends ZMDialogFragment {
    public static boolean checkIfShow() {
        boolean z = true;
        if (!(OsUtil.isAtLeastM() ? !((PowerManager) VideoBoxApplication.getInstance().getSystemService("power")).isIgnoringBatteryOptimizations(VideoBoxApplication.getInstance().getPackageName()) : true)) {
            return false;
        }
        int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.FIRST_OPEN_SIP, 0);
        if (!(readIntValue == 0 || readIntValue == 1)) {
            z = false;
        }
        return z;
    }

    @NonNull
    public static SipBatteryOptDialog newInstance() {
        return newInstance(true);
    }

    @NonNull
    public static SipBatteryOptDialog newInstance(boolean z) {
        SipBatteryOptDialog sipBatteryOptDialog = new SipBatteryOptDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean("requestOpen", z);
        sipBatteryOptDialog.setArguments(bundle);
        return sipBatteryOptDialog;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Builder builder = new Builder(getActivity());
        String string = getResources().getString(C4558R.string.zm_sip_battery_optimization_dialog_title);
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_sip_battery_opt_dialog, null);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.tv1);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.tv2);
        int readIntValue = PreferenceUtil.readIntValue(PreferenceUtil.FIRST_OPEN_SIP, 0);
        if (readIntValue == 0) {
            textView.setVisibility(4);
        } else if (readIntValue == 1) {
            textView.setVisibility(0);
        }
        PreferenceUtil.saveIntValue(PreferenceUtil.FIRST_OPEN_SIP, 1);
        textView2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VideoBoxApplication.getInstance().requestIgnoreBatteryOptimization();
                SipBatteryOptDialog.this.dismiss();
            }
        });
        textView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PreferenceUtil.saveIntValue(PreferenceUtil.FIRST_OPEN_SIP, 2);
                SipBatteryOptDialog.this.dismiss();
            }
        });
        builder.setView(inflate);
        builder.setTitle((CharSequence) string);
        return builder.create();
    }
}
