package com.zipow.videobox.confapp.component;

import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.component.sink.audio.IAudioSink;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.NormalMessageTip;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

class ZmConfAudioComponent extends ZmBaseConfComponent implements IAudioSink {
    private static final String TAG = "ZmConfAudioComponent";
    private ZMAlertDialog mKmsKeyNotReadDialog;

    public void onActivityCreate(Bundle bundle) {
    }

    public void onActivityPause() {
    }

    public void onActivityResume() {
    }

    public void onConfReady() {
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
    }

    public ZmConfAudioComponent(@NonNull ConfActivity confActivity) {
        super(confActivity);
    }

    public void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode) {
        if (zMConfEnumViewMode == ZMConfEnumViewMode.SILENT_VIEW) {
            ZMAlertDialog zMAlertDialog = this.mKmsKeyNotReadDialog;
            if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
                this.mKmsKeyNotReadDialog.dismiss();
                this.mKmsKeyNotReadDialog = null;
            }
        }
    }

    public void sinkConfKmsKeyNotReady() {
        if (this.mContext != null) {
            ZMAlertDialog zMAlertDialog = this.mKmsKeyNotReadDialog;
            if (zMAlertDialog == null) {
                this.mKmsKeyNotReadDialog = new Builder(this.mContext).setVerticalOptionStyle(true).setMessage(C4558R.string.zm_msg_unable_to_record_114474).setTitle(C4558R.string.zm_title_unable_to_record_114474).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) null).create();
                this.mKmsKeyNotReadDialog.show();
            } else if (!zMAlertDialog.isShowing()) {
                this.mKmsKeyNotReadDialog.show();
            }
        }
    }

    public void sinkPreemptionAudio(int i) {
        if (this.mContext != null) {
            this.mContext.showToolbar(true, false);
            NormalMessageTip.show(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_RECONNECT_AUDIO.name(), (String) null, this.mContext.getString(C4558R.string.zm_msg_reconnect_meeting_audio_108086), C4558R.C4560id.btnAudio, UIMgr.isLargeMode(this.mContext) ? 1 : 3);
        }
    }
}
