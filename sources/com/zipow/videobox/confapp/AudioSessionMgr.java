package com.zipow.videobox.confapp;

import android.media.AudioManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.ParamsList;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;

public class AudioSessionMgr {
    public static final int AUDIO_ERROR_FEEDBACK_DETECTED = 10;
    public static final int DEVICE_ERROR_FOUND = 2;
    private static final long INTERVAL_CHECK_SCO_STATUS = 3000;
    public static final int LOUDSPEAKER_NOT_SET = -1;
    public static final int LOUDSPEAKER_OFF = 0;
    public static final int LOUDSPEAKER_ON = 1;
    private static final String TAG = "AudioSessionMgr";
    private static final int TRY_START_SCO_TIMES = 4;
    @Nullable
    private AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public boolean mBluetoothScoStarted = false;
    private int mDefaultAudioMode = -1;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mIsUseA2dpMode;
    private long mNativeHandle = 0;
    private int mPreferedLoudspeakerStatus = -1;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableStartSco = new Runnable() {
        public void run() {
            if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
                if (AudioSessionMgr.this.mBluetoothScoStarted) {
                    HeadsetUtil.getInstance().stopBluetoothSco();
                    AudioSessionMgr.this.mBluetoothScoStarted = false;
                }
                AudioSessionMgr.this.mStartScoCountDown = 0;
            } else if (HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
                AudioSessionMgr.this.mBluetoothScoStarted = true;
                AudioSessionMgr.this.mStartScoCountDown = 0;
                AudioSessionMgr.this.notifyVoiceSwitchedToHeadsetOrEarSpeaker(true);
            } else if (AudioSessionMgr.access$106(AudioSessionMgr.this) < 0) {
                HeadsetUtil.getInstance().stopBluetoothSco();
                VoiceEngineCompat.blacklistBluetoothSco(true);
                AudioSessionMgr.this.startBluetoothHeadset();
            } else {
                if (!AudioSessionMgr.this.mBluetoothScoStarted) {
                    HeadsetUtil.getInstance().startBluetoothSco();
                }
                AudioSessionMgr.this.mHandler.postDelayed(AudioSessionMgr.this.mRunnableStartSco, AudioSessionMgr.INTERVAL_CHECK_SCO_STATUS);
            }
        }
    };
    private int mScoUnexpectedDisconnectTimes = 0;
    /* access modifiers changed from: private */
    public int mStartScoCountDown = 0;
    private boolean mbPreferedLoudspeakerStatusLoaded = false;

    private native int getAudioSessionTypeImpl(long j);

    private native boolean getLoudSpeakerStatusImpl(long j);

    private native Object isMicKeepOriInputEnabledImpl(long j);

    private native boolean isMuteOnEntryOnImpl(long j);

    private native Object isOriginalSoundChangableImpl(long j);

    private native boolean notifyChipAECEnabledImpl(long j, boolean z);

    private native boolean notifyHeadsetStatusChangedImpl(long j, boolean z);

    private native boolean notifyIsTabletImpl(long j, boolean z);

    private native boolean notifyVolumeChangedImpl(long j, boolean z, int i);

    private native boolean selectDefaultMicrophoneImpl(long j, boolean z);

    private native void setEnableMicKeepOriInputImpl(long j, boolean z);

    private native int setLoudSpeakerStatusImpl(long j, boolean z);

    private native void setMuteOnEntryImpl(long j, boolean z);

    private native boolean setMutebySelfFlagImpl(long j, boolean z);

    private native boolean startAudioImpl(long j);

    private native int startPlayoutImpl(long j);

    private native boolean startVoIPImpl(long j);

    private native boolean stopAudioImpl(long j);

    private native int stopPlayoutImpl(long j);

    private native boolean stopVoIPImpl(long j);

    private native boolean turnOnOffAudioSessionImpl(long j, boolean z);

    private native void unSelectMicrophoneImpl(long j);

    static /* synthetic */ int access$106(AudioSessionMgr audioSessionMgr) {
        int i = audioSessionMgr.mStartScoCountDown - 1;
        audioSessionMgr.mStartScoCountDown = i;
        return i;
    }

    public AudioSessionMgr(long j) {
        this.mNativeHandle = j;
    }

    public boolean startAudio() {
        return startAudioImpl(this.mNativeHandle);
    }

    public boolean stopAudio() {
        return stopAudioImpl(this.mNativeHandle);
    }

    public int getAudioSessionType() {
        return getAudioSessionTypeImpl(this.mNativeHandle);
    }

    public boolean turnOnOffAudioSession(boolean z) {
        return turnOnOffAudioSessionImpl(this.mNativeHandle, z);
    }

    public int setLoudSpeakerStatus(boolean z) {
        return setLoudSpeakerStatus(z, false);
    }

    public int setLoudSpeakerStatus(boolean z, boolean z2) {
        int i;
        boolean isConfConnected = ConfMgr.getInstance().isConfConnected();
        if (z2 || !isConfConnected || z || HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            UIUtil.stopProximityScreenOffWakeLock();
        } else {
            UIUtil.startProximityScreenOffWakeLock(VideoBoxApplication.getInstance());
        }
        if (isConfConnected) {
            i = setLoudSpeakerStatusImpl(this.mNativeHandle, z);
        } else {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
            }
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                audioManager.setSpeakerphoneOn(z);
            }
            i = 0;
        }
        if (VoiceEnginContext.getSelectedPlayerStreamType() != 3) {
            notifyVoiceSwitchedToHeadsetOrEarSpeaker(!z);
        } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            notifyVoiceSwitchedToHeadsetOrEarSpeaker(true);
        } else {
            notifyVoiceSwitchedToHeadsetOrEarSpeaker(false);
        }
        return i;
    }

    public boolean ismIsUseA2dpMode() {
        return this.mIsUseA2dpMode;
    }

    public boolean getLoudSpeakerStatus() {
        if (ConfMgr.getInstance().isConfConnected()) {
            return getLoudSpeakerStatusImpl(this.mNativeHandle);
        }
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            return audioManager.isSpeakerphoneOn();
        }
        return false;
    }

    public void setPreferedLoudSpeakerStatus(int i) {
        this.mPreferedLoudspeakerStatus = i;
        if (-1 != i) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            setLoudSpeakerStatus(z);
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && ConfUI.getInstance().isLaunchConfParamReady()) {
            ParamsList appContextParams = confContext.getAppContextParams();
            appContextParams.putInt("AudioSessionMgr.PreferedLoudspeakerStatus", this.mPreferedLoudspeakerStatus);
            confContext.setAppContextParams(appContextParams);
        }
    }

    public int getPreferedLoudSpeakerStatus() {
        if (!this.mbPreferedLoudspeakerStatusLoaded) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && ConfUI.getInstance().isLaunchConfParamReady()) {
                this.mPreferedLoudspeakerStatus = confContext.getAppContextParams().getInt("AudioSessionMgr.PreferedLoudspeakerStatus", this.mPreferedLoudspeakerStatus);
                this.mbPreferedLoudspeakerStatusLoaded = true;
            }
        }
        return this.mPreferedLoudspeakerStatus;
    }

    public void stopPlayout() {
        stopPlayoutImpl(this.mNativeHandle);
    }

    public void startPlayout() {
        startPlayoutImpl(this.mNativeHandle);
    }

    public void notifyHeadsetStatusChanged(boolean z, boolean z2) {
        boolean z3 = false;
        if (!z) {
            this.mIsUseA2dpMode = false;
            VoiceEngineCompat.blacklistBluetoothSco(false);
            this.mHandler.removeCallbacks(this.mRunnableStartSco);
        }
        if (z || z2) {
            z3 = true;
        }
        if (getLoudSpeakerStatus() || z3) {
            UIUtil.stopProximityScreenOffWakeLock();
        } else {
            UIUtil.startProximityScreenOffWakeLock(VideoBoxApplication.getInstance());
        }
    }

    public void notifyVolumeChanged(boolean z, int i) {
        notifyVolumeChangedImpl(this.mNativeHandle, z, i);
    }

    public void notifyChipAECEnabled(boolean z) {
        notifyChipAECEnabledImpl(this.mNativeHandle, z);
    }

    public void notifyIsTablet(boolean z) {
        notifyIsTabletImpl(this.mNativeHandle, z);
    }

    public void notifyBluetoothScoAudioStatus(boolean z, boolean z2) {
        boolean z3 = this.mBluetoothScoStarted;
        this.mBluetoothScoStarted = z;
        if (!z2 && z3 && !z && this.mStartScoCountDown == 0 && getPreferedLoudSpeakerStatus() != 1 && HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            this.mScoUnexpectedDisconnectTimes++;
            if (this.mScoUnexpectedDisconnectTimes > 2) {
                ZMLog.m286i(TAG, "notifyBluetoothScoAudioStatus, fallback to A2DP mode", new Object[0]);
                VoiceEngineCompat.blacklistBluetoothSco(true);
            }
            startBluetoothHeadset();
        }
    }

    /* access modifiers changed from: private */
    public void notifyVoiceSwitchedToHeadsetOrEarSpeaker(boolean z) {
        notifyHeadsetStatusChangedImpl(this.mNativeHandle, z);
    }

    public boolean isStarttingSco() {
        return this.mStartScoCountDown > 0;
    }

    public void startWiredHeadset() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        if (this.mAudioManager != null) {
            this.mIsUseA2dpMode = false;
            notifyVoiceSwitchedToHeadsetOrEarSpeaker(HeadsetUtil.getInstance().isWiredHeadsetOn());
        }
    }

    public void startBluetoothHeadset() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        if (this.mAudioManager != null && HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            if (!isBluetoothScoSupported()) {
                this.mIsUseA2dpMode = true;
                HeadsetUtil.getInstance().enterA2dpMode();
                if (this.mDefaultAudioMode < 0) {
                    this.mDefaultAudioMode = this.mAudioManager.getMode();
                }
                this.mAudioManager.setMode(0);
                notifyVoiceSwitchedToHeadsetOrEarSpeaker(true);
            } else if (this.mStartScoCountDown <= 0 && !this.mBluetoothScoStarted) {
                this.mStartScoCountDown = 4;
                this.mIsUseA2dpMode = false;
                this.mHandler.removeCallbacks(this.mRunnableStartSco);
                this.mHandler.post(this.mRunnableStartSco);
            }
        }
    }

    public void stopBluetoothHeadset() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        if (this.mAudioManager != null) {
            this.mHandler.removeCallbacks(this.mRunnableStartSco);
            this.mStartScoCountDown = 0;
            if (!isBluetoothScoSupported()) {
                int i = this.mDefaultAudioMode;
                if (i >= 0) {
                    this.mAudioManager.setMode(i);
                    this.mDefaultAudioMode = -1;
                }
            } else if (this.mBluetoothScoStarted) {
                if (HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
                    HeadsetUtil.getInstance().stopBluetoothSco();
                }
                this.mBluetoothScoStarted = false;
            }
            notifyVoiceSwitchedToHeadsetOrEarSpeaker(false);
        }
    }

    public boolean isBluetoothHeadsetStarted() {
        return (isBluetoothScoSupported() && this.mBluetoothScoStarted) || (!isBluetoothScoSupported() && this.mDefaultAudioMode >= 0);
    }

    private boolean isBluetoothScoSupported() {
        return VoiceEngineCompat.isBluetoothScoSupported();
    }

    public void setMuteOnEntry(boolean z) {
        setMuteOnEntryImpl(this.mNativeHandle, z);
    }

    public boolean isMuteOnEntryOn() {
        return isMuteOnEntryOnImpl(this.mNativeHandle);
    }

    public void unSelectMicrophone() {
        unSelectMicrophoneImpl(this.mNativeHandle);
    }

    public boolean selectDefaultMicrophone(boolean z) {
        return selectDefaultMicrophoneImpl(this.mNativeHandle, z);
    }

    public boolean setMutebySelfFlag(boolean z) {
        return setMutebySelfFlagImpl(this.mNativeHandle, z);
    }

    public void setEnableMicKeepOriInput(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setEnableMicKeepOriInputImpl(j, z);
        }
    }

    @Nullable
    public BooleanQueryResult isOriginalSoundChangable() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        Object isOriginalSoundChangableImpl = isOriginalSoundChangableImpl(j);
        if (isOriginalSoundChangableImpl instanceof BooleanQueryResult) {
            return (BooleanQueryResult) isOriginalSoundChangableImpl;
        }
        return new BooleanQueryResult(false, false, false, false);
    }

    @Nullable
    public BooleanQueryResult isMicKeepOriInputEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        Object isMicKeepOriInputEnabledImpl = isMicKeepOriInputEnabledImpl(j);
        if (isMicKeepOriInputEnabledImpl instanceof BooleanQueryResult) {
            return (BooleanQueryResult) isMicKeepOriInputEnabledImpl;
        }
        return new BooleanQueryResult(false, false, false, false);
    }
}
