package org.webrtc.voiceengine;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Process;
import androidx.annotation.Nullable;

class AudioDeviceAndroidOpenSLESHelper {
    private static final int LOUDSPEAKER_STATUS_NOTSET = -1;
    private static final int LOUDSPEAKER_STATUS_OFF = 0;
    private static final int LOUDSPEAKER_STATUS_ON = 1;
    @Nullable
    private AudioManager _audioManager;
    private Context _context;
    final String logTag = "webrtc";
    private int mLoudSpeakerStatus = -1;

    private void DoLog(String str) {
    }

    private void DoLogErr(String str) {
    }

    AudioDeviceAndroidOpenSLESHelper() {
    }

    private int SetPlayoutSpeaker(boolean z) {
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        AudioManager audioManager = this._audioManager;
        if (audioManager == null) {
            return -1;
        }
        this.mLoudSpeakerStatus = z ? 1 : 0;
        audioManager.setSpeakerphoneOn(z);
        return 0;
    }

    private boolean CheckAudioRecordPermission() {
        Context context = this._context;
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (context.checkPermission("android.permission.RECORD_AUDIO", Process.myPid(), Process.myUid()) == 0) {
            z = true;
        }
        return z;
    }

    private void SetAudioMode(boolean z, boolean z2) {
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        if (this._audioManager != null) {
            int i = !z2 ? 0 : 3;
            if (!z) {
                i = 0;
            }
            this._audioManager.setMode(i);
            this._audioManager.getMode();
            if (z) {
                if (i == 3) {
                    VoiceEnginContext.setSelectedPlayerStreamType(0);
                } else {
                    VoiceEnginContext.setSelectedPlayerStreamType(3);
                }
                if ("Amazon".equals(Build.MANUFACTURER) && !VoiceEngineCompat.isFeatureTelephonySupported(this._context)) {
                    float streamVolume = ((float) this._audioManager.getStreamVolume(3)) / ((float) this._audioManager.getStreamMaxVolume(3));
                    if (streamVolume < 0.6f) {
                        this._audioManager.setStreamVolume(3, (int) (((float) this._audioManager.getStreamMaxVolume(3)) * 0.6f), 0);
                        streamVolume = 0.6f;
                    }
                    this._audioManager.setStreamVolume(0, (int) (((float) this._audioManager.getStreamMaxVolume(0)) * streamVolume), 0);
                }
            }
        }
    }

    private boolean isRecorderConfigurationNativeAPIDisabled() {
        return VoiceEngineCompat.isRecorderConfigurationNativeAPIDisabled(this._context);
    }

    private boolean isPlayerConfigurationNativeAPIDisabled() {
        return VoiceEngineCompat.isPlayerConfigurationNativeAPIDisabled(this._context);
    }
}
