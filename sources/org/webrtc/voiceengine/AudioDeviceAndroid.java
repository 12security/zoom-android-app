package org.webrtc.voiceengine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import androidx.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;
import p021us.zoom.androidlib.util.OsUtil;

@SuppressLint({"NewApi"})
class AudioDeviceAndroid {
    private static final int AUDIO_SOURCE_UNDER_ORIGINAL_SOUND_MODE = 1;
    private static final int RECORD_TYPE_NORMAL = 0;
    private static final int RECORD_TYPE_ORIGINAL_AUDIO = 1;
    @Nullable
    private AudioManager _audioManager;
    @Nullable
    private AudioRecord _audioRecord = null;
    @Nullable
    private AudioTrack _audioTrack = null;
    private int _bufferedPlaySamples = 0;
    private int _bufferedRecSamples = 0;
    private Context _context;
    @Nullable
    private AutomaticGainControl _deviceAGC;
    @Nullable
    private NoiseSuppressor _deviceNS;
    private boolean _doPlayInit = true;
    private boolean _doRecInit = true;
    @Nullable
    private AcousticEchoCanceler _echoCanceler;
    private boolean _isPlaying = false;
    private boolean _isRecording = false;
    private ByteBuffer _playBuffer;
    private final ReentrantLock _playLock = new ReentrantLock();
    private int _playPosition = 0;
    private ByteBuffer _recBuffer;
    private final ReentrantLock _recLock = new ReentrantLock();
    private byte[] _tempBufPlay;
    private byte[] _tempBufRec;
    final String logTag = "webrtc";
    private int mBestAudioSource;
    private int mRecBufSize;
    private int mRecSampleRate;
    private boolean mRecordTypeChangedByUser = false;
    private boolean mUseOriginalSound = false;

    private void DoLog(String str) {
    }

    private void DoLogErr(String str) {
    }

    AudioDeviceAndroid() {
        DoLog("AudioDeviceAndroid.<init>");
        try {
            this._playBuffer = ByteBuffer.allocateDirect(960);
            this._recBuffer = ByteBuffer.allocateDirect(960);
        } catch (Exception e) {
            DoLog(e.getMessage());
        }
        this._tempBufPlay = new byte[960];
        this._tempBufRec = new byte[960];
    }

    public boolean initAEC(int i) {
        if (this._echoCanceler != null) {
            return false;
        }
        if (!AcousticEchoCanceler.isAvailable()) {
            DoLog("AudioDeviceAndroid.initAEC, AcousticEchoCanceler.isAvailable() false");
            return false;
        }
        try {
            this._echoCanceler = AcousticEchoCanceler.create(i);
            AcousticEchoCanceler acousticEchoCanceler = this._echoCanceler;
            if (acousticEchoCanceler == null) {
                DoLog("AudioDeviceAndroid.initAEC, _echoCanceler create fail");
                return false;
            }
            acousticEchoCanceler.setEnabled(true);
            StringBuilder sb = new StringBuilder();
            sb.append("AudioDeviceAndroid.initAEC, _echoCanceler Enabled = ");
            sb.append(this._echoCanceler.getEnabled());
            DoLog(sb.toString());
            return this._echoCanceler.getEnabled();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("AudioDeviceAndroid.initAEC create echoCanceler, ");
            sb2.append(e.getMessage());
            DoLog(sb2.toString());
            return false;
        }
    }

    public boolean releaseAEC() {
        AcousticEchoCanceler acousticEchoCanceler = this._echoCanceler;
        if (acousticEchoCanceler == null) {
            return false;
        }
        acousticEchoCanceler.setEnabled(false);
        this._echoCanceler.release();
        this._echoCanceler = null;
        return true;
    }

    public boolean initAGC(int i) {
        if (this._deviceAGC != null) {
            return false;
        }
        try {
            if (!AutomaticGainControl.isAvailable()) {
                DoLog("AudioDeviceAndroid.initAGC, AutomaticGainControl.isAvailable() false");
                return false;
            }
            this._deviceAGC = AutomaticGainControl.create(i);
            AutomaticGainControl automaticGainControl = this._deviceAGC;
            if (automaticGainControl == null) {
                DoLog("AudioDeviceAndroid.initAGC, _deviceAGC create fail");
                return false;
            }
            automaticGainControl.setEnabled(true);
            StringBuilder sb = new StringBuilder();
            sb.append("AudioDeviceAndroid.initAGC, _deviceAGC Enabled = ");
            sb.append(this._deviceAGC.getEnabled());
            DoLog(sb.toString());
            return this._deviceAGC.getEnabled();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("AudioDeviceAndroid.initAGC create deviceAGC exception, ");
            sb2.append(e.getMessage());
            DoLog(sb2.toString());
            return false;
        } catch (Error e2) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("AudioDeviceAndroid.initAGC create deviceAGC error, ");
            sb3.append(e2.getMessage());
            DoLog(sb3.toString());
            return false;
        }
    }

    public boolean releaseAGC() {
        AutomaticGainControl automaticGainControl = this._deviceAGC;
        if (automaticGainControl == null) {
            return false;
        }
        automaticGainControl.setEnabled(false);
        this._deviceAGC.release();
        this._deviceAGC = null;
        return true;
    }

    public boolean initNS(int i) {
        if (this._deviceNS != null) {
            return false;
        }
        try {
            if (!NoiseSuppressor.isAvailable()) {
                DoLog("AudioDeviceAndroid.initNS, NoiseSuppressor.isAvailable() false");
                return false;
            }
            this._deviceNS = NoiseSuppressor.create(i);
            NoiseSuppressor noiseSuppressor = this._deviceNS;
            if (noiseSuppressor == null) {
                DoLog("AudioDeviceAndroid.initNS, _deviceNS create fail");
                return false;
            }
            noiseSuppressor.setEnabled(true);
            StringBuilder sb = new StringBuilder();
            sb.append("AudioDeviceAndroid.initNS, _deviceNS Enabled = ");
            sb.append(this._deviceNS.getEnabled());
            DoLog(sb.toString());
            return this._deviceNS.getEnabled();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("AudioDeviceAndroid.initNS create NoiseSuppressor exception, ");
            sb2.append(e.getMessage());
            DoLog(sb2.toString());
            return false;
        } catch (Error e2) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("AudioDeviceAndroid.initNS create NoiseSuppressor error, ");
            sb3.append(e2.getMessage());
            DoLog(sb3.toString());
            return false;
        }
    }

    public boolean releaseNS() {
        NoiseSuppressor noiseSuppressor = this._deviceNS;
        if (noiseSuppressor == null) {
            return false;
        }
        noiseSuppressor.setEnabled(false);
        this._deviceNS.release();
        this._deviceNS = null;
        return true;
    }

    private boolean tryInitAudioRecord() {
        int i = this.mBestAudioSource;
        if (i != 7 && i != 1) {
            return false;
        }
        this.mBestAudioSource = 6;
        try {
            AudioRecord audioRecord = new AudioRecord(this.mBestAudioSource, this.mRecSampleRate, 16, 2, this.mRecBufSize);
            this._audioRecord = audioRecord;
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private int InitRecording(int i, int i2) {
        this.mRecSampleRate = i2;
        StringBuilder sb = new StringBuilder();
        sb.append("AudioDeviceAndroid.InitRecording, recordType=");
        sb.append(i);
        sb.append(", sampleRate=");
        sb.append(this.mRecSampleRate);
        DoLog(sb.toString());
        boolean z = false;
        if (i != 1) {
            if (OsUtil.isAtLeastL_MR1()) {
                this.mBestAudioSource = 7;
            } else {
                this.mBestAudioSource = 6;
            }
            this.mUseOriginalSound = false;
        } else {
            this.mBestAudioSource = 1;
            this.mUseOriginalSound = true;
        }
        int minBufferSize = AudioRecord.getMinBufferSize(this.mRecSampleRate, 16, 2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("AudioDeviceAndroid.InitRecording, min rec buf size is ");
        sb2.append(minBufferSize);
        DoLog(sb2.toString());
        this.mRecBufSize = minBufferSize * 2;
        this._bufferedRecSamples = (this.mRecSampleRate * 5) / 200;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("AudioDeviceAndroid.InitRecording, rough rec delay set to ");
        sb3.append(this._bufferedRecSamples);
        DoLog(sb3.toString());
        AudioRecord audioRecord = this._audioRecord;
        if (audioRecord != null) {
            audioRecord.release();
            this._audioRecord = null;
        }
        try {
            AudioRecord audioRecord2 = new AudioRecord(this.mBestAudioSource, this.mRecSampleRate, 16, 2, this.mRecBufSize);
            this._audioRecord = audioRecord2;
        } catch (Exception e) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("AudioDeviceAndroid.InitRecording, ");
            sb4.append(e.getMessage());
            sb4.append("audioSource [");
            sb4.append(this.mBestAudioSource);
            sb4.append("]");
            DoLog(sb4.toString());
            if (!tryInitAudioRecord()) {
                z = true;
            }
        }
        if (!z && this._audioRecord.getState() != 1) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("rec not initialized ");
            sb5.append(this.mRecSampleRate);
            sb5.append("audioSource [");
            sb5.append(this.mBestAudioSource);
            sb5.append("], bestAudioSourceCreatFailed");
            DoLog(sb5.toString());
            z = true;
        }
        if (z) {
            AudioRecord audioRecord3 = this._audioRecord;
            if (audioRecord3 != null) {
                try {
                    audioRecord3.release();
                } catch (Exception e2) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("release _audioRecord failed: e=");
                    sb6.append(e2.getMessage());
                    DoLogErr(sb6.toString());
                }
                this._audioRecord = null;
            }
            try {
                AudioRecord audioRecord4 = new AudioRecord(1, this.mRecSampleRate, 16, 2, this.mRecBufSize);
                this._audioRecord = audioRecord4;
                if (this._audioRecord.getState() != 1) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("rec not initialized ");
                    sb7.append(this.mRecSampleRate);
                    sb7.append("recordType [");
                    sb7.append(i);
                    sb7.append("]");
                    DoLog(sb7.toString());
                    return -1;
                }
            } catch (Exception e3) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("AudioDeviceAndroid.InitRecording, ");
                sb8.append(e3.getMessage());
                sb8.append("recordType [");
                sb8.append(i);
                sb8.append("]");
                DoLog(sb8.toString());
                return -1;
            }
        }
        if (VERSION.SDK_INT >= 16) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("AudioDeviceAndroid.InitRecording, rec sample rate set to ");
            sb9.append(this.mRecSampleRate);
            sb9.append("audioSource=[");
            sb9.append(this._audioRecord.getAudioSource());
            sb9.append("], audioSessionID [");
            sb9.append(this._audioRecord.getAudioSessionId());
            sb9.append("]");
            DoLog(sb9.toString());
        } else {
            StringBuilder sb10 = new StringBuilder();
            sb10.append("AudioDeviceAndroid.InitRecording, rec sample rate set to ");
            sb10.append(this.mRecSampleRate);
            sb10.append("audioSource=[");
            sb10.append(this._audioRecord.getAudioSource());
            sb10.append("]");
            DoLog(sb10.toString());
        }
        return this._bufferedRecSamples;
    }

    private int StartRecording() {
        DoLog("StartRecording");
        if (this.mUseOriginalSound) {
            SetAudioMode(false);
            this.mRecordTypeChangedByUser = true;
        } else if (this.mRecordTypeChangedByUser || !this._isPlaying) {
            SetAudioMode(true);
            this.mRecordTypeChangedByUser = false;
        }
        try {
            this._audioRecord.startRecording();
            DoLog("StartRecording, success");
            this._isRecording = true;
            return 0;
        } catch (IllegalStateException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("StartRecording, e=");
            sb.append(e);
            DoLog(sb.toString());
            return -1;
        }
    }

    private int InitPlayback(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("InitPlayback, sampleRate=");
        sb.append(i);
        sb.append("_audioRecord = ");
        sb.append(this._audioRecord);
        DoLog(sb.toString());
        int minBufferSize = AudioTrack.getMinBufferSize(i, 4, 2);
        if (minBufferSize < 6000) {
            minBufferSize *= 2;
        }
        this._bufferedPlaySamples = 0;
        AudioTrack audioTrack = this._audioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this._audioTrack = null;
        }
        if (VERSION.SDK_INT >= 16) {
            AudioRecord audioRecord = this._audioRecord;
            if (audioRecord != null) {
                try {
                    AudioTrack audioTrack2 = new AudioTrack(0, i, 4, 2, minBufferSize, 1, audioRecord.getAudioSessionId());
                    this._audioTrack = audioTrack2;
                } catch (Exception e) {
                    DoLog(e.getMessage());
                }
            }
        }
        if (this._audioTrack == null) {
            try {
                AudioTrack audioTrack3 = new AudioTrack(0, i, 4, 2, minBufferSize, 1);
                this._audioTrack = audioTrack3;
            } catch (Exception e2) {
                DoLog(e2.getMessage());
                return -1;
            }
        }
        if (this._audioTrack.getState() != 1) {
            return -1;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("AudioDeviceAndroid.InitPlayback, AudioSessionID[");
        sb2.append(this._audioTrack.getAudioSessionId());
        sb2.append("]");
        DoLogErr(sb2.toString());
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        AudioManager audioManager = this._audioManager;
        if (audioManager == null) {
            return 0;
        }
        return audioManager.getStreamMaxVolume(0);
    }

    private int StartPlayback() {
        DoLog("StartPlayback");
        if (this.mUseOriginalSound) {
            SetAudioMode(false);
            this.mRecordTypeChangedByUser = true;
        } else if (this.mRecordTypeChangedByUser || !this._isRecording) {
            SetAudioMode(true);
            this.mRecordTypeChangedByUser = false;
        }
        try {
            this._audioTrack.play();
            DoLog("StartPlayback, success");
            this._isPlaying = true;
            return 0;
        } catch (IllegalStateException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("StartPlayback, e=");
            sb.append(e);
            DoLogErr(sb.toString());
            e.printStackTrace();
            return -1;
        }
    }

    private int StopRecording() {
        DoLog("StopRecording");
        this._recLock.lock();
        try {
            DoLog("StopRecording before stop");
            if (this._audioRecord.getRecordingState() == 3) {
                this._audioRecord.stop();
            }
            DoLog("StopRecording after stop");
            this._audioRecord.release();
            this._audioRecord = null;
            this._doRecInit = true;
            this._recLock.unlock();
            if (!this._isPlaying) {
                SetAudioMode(false);
            }
            this._isRecording = false;
            this.mUseOriginalSound = false;
            DoLog("StopRecording end");
            return 0;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append("StopRecording, e=");
            sb.append(e);
            DoLogErr(sb.toString());
            this.mUseOriginalSound = false;
            this._doRecInit = true;
            this._recLock.unlock();
            return -1;
        } catch (Throwable th) {
            this._doRecInit = true;
            this._recLock.unlock();
            throw th;
        }
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

    private int StopPlayback() {
        DoLog("StopPlayback");
        this._playLock.lock();
        try {
            if (this._audioTrack.getPlayState() == 3) {
                DoLog("StopPlayback before stop");
                this._audioTrack.stop();
                DoLog("StopPlayback after stop");
                this._audioTrack.flush();
            }
            this._audioTrack.release();
            this._audioTrack = null;
            this._doPlayInit = true;
            this._playLock.unlock();
            if (!this._isRecording) {
                try {
                    SetAudioMode(false);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("StopPlayback try to SetAudioMode failed: ");
                    sb.append(e.getMessage());
                    DoLogErr(sb.toString());
                }
            }
            this._isPlaying = false;
            DoLog("StopPlayback end");
            return 0;
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("StopPlayback, e=");
            sb2.append(e2);
            DoLogErr(sb2.toString());
            this._doPlayInit = true;
            this._playLock.unlock();
            return -1;
        } catch (Throwable th) {
            this._doPlayInit = true;
            this._playLock.unlock();
            throw th;
        }
    }

    private int PlayAudio(int i) {
        this._playLock.lock();
        int i2 = 0;
        try {
            if (this._audioTrack == null) {
                this._playLock.unlock();
                return -2;
            }
            if (this._doPlayInit) {
                Process.setThreadPriority(-19);
                this._doPlayInit = false;
            }
            this._playBuffer.get(this._tempBufPlay);
            int write = (this._audioTrack.getState() == 1 && this._audioTrack.getPlayState() == 3) ? this._audioTrack.write(this._tempBufPlay, 0, i) : 0;
            this._playBuffer.rewind();
            this._bufferedPlaySamples += write >> 1;
            int playbackHeadPosition = this._audioTrack.getPlaybackHeadPosition();
            if (playbackHeadPosition < this._playPosition) {
                this._playPosition = 0;
            }
            this._bufferedPlaySamples -= playbackHeadPosition - this._playPosition;
            this._playPosition = playbackHeadPosition;
            if (!this._isRecording) {
                i2 = this._bufferedPlaySamples;
            }
            if (write != i) {
                this._playLock.unlock();
                return -1;
            }
            this._playLock.unlock();
            return i2;
        } catch (Throwable th) {
            try {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            } catch (Throwable th2) {
                this._playLock.unlock();
                throw th2;
            }
        }
    }

    private int RecordAudio(int i) {
        this._recLock.lock();
        try {
            if (this._audioRecord == null) {
                this._recLock.unlock();
                return -2;
            }
            if (this._doRecInit) {
                try {
                    Process.setThreadPriority(-19);
                } catch (Throwable th) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Set rec thread priority failed: ");
                    sb.append(th.getMessage());
                    DoLog(sb.toString());
                }
                this._doRecInit = false;
            }
            this._recBuffer.rewind();
            int read = this._audioRecord.read(this._tempBufRec, 0, i);
            this._recBuffer.put(this._tempBufRec);
            if (read != i) {
                this._recLock.unlock();
                return -1;
            }
            this._recLock.unlock();
            return this._bufferedPlaySamples;
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("RecordAudio try failed: ");
            sb2.append(e.getMessage());
            DoLogErr(sb2.toString());
        } catch (Error e2) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
        } catch (Throwable th2) {
            this._recLock.unlock();
            throw th2;
        }
    }

    private int SetPlayoutSpeaker(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("SetPlayoutSpeaker, loudspeakerOn=");
        sb.append(z);
        DoLog(sb.toString());
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        AudioManager audioManager = this._audioManager;
        if (audioManager == null) {
            DoLogErr("Could not change audio routing - no audio manager");
            return -1;
        }
        audioManager.setSpeakerphoneOn(z);
        return 0;
    }

    private int SetPlayoutVolume(int i) {
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
        audioManager.setStreamVolume(0, i, 0);
        return 0;
    }

    private int GetPlayoutVolume() {
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        AudioManager audioManager = this._audioManager;
        if (audioManager != null) {
            return audioManager.getStreamVolume(0);
        }
        return -1;
    }

    private void SetAudioMode(boolean z) {
        if (this._audioManager == null) {
            Context context = this._context;
            if (context != null) {
                this._audioManager = (AudioManager) context.getSystemService("audio");
            }
        }
        if (this._audioManager != null) {
            int i = z ? 3 : 0;
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
}
