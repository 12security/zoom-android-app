package com.zipow.videobox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMDialogFragment.ZMDialogParam;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SeparateAudioDialog extends ZMDialogFragment {
    private static final String TAG = "SeparateAudioDialog";
    private ZMDialogParam param;

    public static void showDialog(FragmentManager fragmentManager, long j, String str) {
        ZMDialogParam zMDialogParam = new ZMDialogParam(0, j, str);
        if (shouldShow(fragmentManager, TAG, zMDialogParam)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ZMDialogFragment.PARAMS, zMDialogParam);
            SeparateAudioDialog separateAudioDialog = new SeparateAudioDialog();
            separateAudioDialog.setArguments(bundle);
            separateAudioDialog.showNow(fragmentManager, TAG);
        }
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        Builder builder;
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        this.param = (ZMDialogParam) arguments.getParcelable(ZMDialogFragment.PARAMS);
        ZMDialogParam zMDialogParam = this.param;
        if (zMDialogParam == null) {
            return createEmptyDialog();
        }
        String str = zMDialogParam.strParam;
        final long j = this.param.longParam;
        if (StringUtil.isEmptyOrNull(str)) {
            builder = new Builder(getActivity()).setTitle(C4558R.string.zm_mi_separate_audio_116180).setMessage(C4558R.string.am_alert_separate_my_audio_message_116180).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.am_alert_separate_my_audio_confirm_button_116180, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SeparateAudioDialog.this.onClickSeparate(j);
                }
            });
        } else {
            if (str.length() > 20) {
                StringBuilder sb = new StringBuilder();
                sb.append(str.substring(0, 20));
                sb.append("...");
                str = sb.toString();
            }
            builder = new Builder(getActivity()).setTitle(C4558R.string.zm_mi_separate_audio_116180).setMessage(getString(C4558R.string.am_alert_separate_participant_audio_message_116180, str)).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).setPositiveButton(C4558R.string.am_alert_separate_my_audio_confirm_button_116180, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SeparateAudioDialog.this.onClickSeparate(j);
                }
            });
        }
        return builder == null ? createEmptyDialog() : builder.create();
    }

    /* access modifiers changed from: private */
    public void onClickSeparate(long j) {
        ConfMgr.getInstance().unbindTelephoneUser(j);
    }
}
