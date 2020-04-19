package com.zipow.videobox.sip.server;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.box.androidsdk.content.models.BoxUser;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.client.AssistantAppClientMgr;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.IPCHelper;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.videomeetings.C4558R;

public class CmmSipAudioMgr extends SimpleSIPCallEventListener implements IHeadsetConnectionListener {
    private static final long INTERVAL_CHECK_SCO_STATUS = 3000;
    private static final String TAG = "CmmSipAudioMgr";
    private static final int TRY_START_SCO_TIMES = 4;
    private static CmmSipAudioMgr mInstance;
    @Nullable
    private AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public boolean mBluetoothScoStarted = false;
    private int mCurAudioSourceType = 0;
    private int mDefaultAudioMode = -1;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsAudioMutedByCallOffHook;
    private boolean mIsAudioStoppedByCallOffHook;
    private boolean mIsAudioStoppedByPauseAudio;
    private boolean mIsCallOffHook;
    private boolean mIsUseA2dpMode;
    private boolean mLastBluetoothHeadsetConnected;
    private boolean mLastWiredHeadsetConnected;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private boolean mMeetingAudioSessionStatus = false;
    private ListenerList mPhoneCallListener = new ListenerList();
    private PhoneStateListener mPhoneStateListener;
    private int mPreferAudioType = -1;
    private int mPreferedLoudspeakerStatus = 0;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableStartSco = new Runnable() {
        public void run() {
            if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
                if (CmmSipAudioMgr.this.mBluetoothScoStarted) {
                    HeadsetUtil.getInstance().stopBluetoothSco();
                    CmmSipAudioMgr.this.mBluetoothScoStarted = false;
                }
                CmmSipAudioMgr.this.mStartScoCountDown = 0;
            } else if (HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
                CmmSipAudioMgr.this.mBluetoothScoStarted = true;
                CmmSipAudioMgr.this.mStartScoCountDown = 0;
                CmmSipAudioMgr.this.notifyVoiceSwitchToHeadsetOrEarSpeaker(true);
            } else if (CmmSipAudioMgr.access$206(CmmSipAudioMgr.this) < 0) {
                HeadsetUtil.getInstance().stopBluetoothSco();
                VoiceEngineCompat.blacklistBluetoothSco(true);
                CmmSipAudioMgr.this.startBluetoothHeadset();
            } else {
                if (!CmmSipAudioMgr.this.mBluetoothScoStarted) {
                    HeadsetUtil.getInstance().startBluetoothSco();
                }
                CmmSipAudioMgr.this.mHandler.postDelayed(CmmSipAudioMgr.this.mRunnableStartSco, CmmSipAudioMgr.INTERVAL_CHECK_SCO_STATUS);
            }
        }
    };
    private int mScoUnexpectedDisconnectTimes = 0;
    private ICmmSipAudioListener mSipAudioListener = new ICmmSipAudioListener() {
        public void onAudioSourceTypeChanged(int i) {
            String str;
            String str2;
            switch (i) {
                case 0:
                    StringBuilder sb = new StringBuilder();
                    sb.append(Mainboard.getDeviceDefaultName());
                    sb.append(".AUDIO_SOURCE_SPEAKER_PHONE");
                    str2 = sb.toString();
                    str = Mainboard.getDeviceDefaultName();
                    break;
                case 1:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Mainboard.getDeviceDefaultName());
                    sb2.append(".AUDIO_SOURCE_EAR_PHONE");
                    str2 = sb2.toString();
                    str = Mainboard.getDeviceDefaultName();
                    break;
                case 2:
                case 3:
                    str = null;
                    if (OsUtil.isAtLeastM()) {
                        AudioDeviceInfo access$000 = CmmSipAudioMgr.this.getAudioDeviceInfo(false, i);
                        str2 = access$000 != null ? access$000.getProductName().toString() : null;
                    } else {
                        str2 = i == 2 ? "AUDIO_SOURCE_WIRED" : HeadsetUtil.getInstance().getConnectedBTName();
                    }
                    if (TextUtils.isEmpty(str2)) {
                        str2 = Mainboard.getDeviceDefaultName();
                    }
                    if (OsUtil.isAtLeastM()) {
                        AudioDeviceInfo access$0002 = CmmSipAudioMgr.this.getAudioDeviceInfo(true, i);
                        if (access$0002 != null) {
                            str = access$0002.getProductName().toString();
                        }
                    } else {
                        str = i == 2 ? "AUDIO_SOURCE_WIRED" : HeadsetUtil.getInstance().getConnectedBTName();
                    }
                    if (TextUtils.isEmpty(str2)) {
                        str = Mainboard.getDeviceDefaultName();
                        break;
                    }
                    break;
                default:
                    str2 = "None";
                    str = "None";
                    break;
            }
            CmmSipAudioMgr.this.audioDeviceChanged(str2, str);
        }
    };
    /* access modifiers changed from: private */
    public int mStartScoCountDown = 0;
    private int mSwitchableAudioSourceType = 0;

    public interface ICmmSipAudioListener extends IListener {
        void onAudioSourceTypeChanged(int i);
    }

    public interface PhoneCallListener extends IListener {
        void onPhoneCallIdle();

        void onPhoneCallOffHook();
    }

    public long getMyAudioType() {
        return 0;
    }

    static /* synthetic */ int access$206(CmmSipAudioMgr cmmSipAudioMgr) {
        int i = cmmSipAudioMgr.mStartScoCountDown - 1;
        cmmSipAudioMgr.mStartScoCountDown = i;
        return i;
    }

    /* access modifiers changed from: private */
    @RequiresApi(api = 23)
    public AudioDeviceInfo getAudioDeviceInfo(boolean z, int i) {
        AudioDeviceInfo audioDeviceInfo;
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        AudioManager audioManager = this.mAudioManager;
        AudioDeviceInfo audioDeviceInfo2 = null;
        if (audioManager == null) {
            return null;
        }
        AudioDeviceInfo[] devices = audioManager.getDevices(z ? 1 : 2);
        new StringBuilder();
        int length = devices.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            audioDeviceInfo = devices[i2];
            int type = audioDeviceInfo.getType();
            if (i == 2) {
                if (type == 3 || type == 4 || type == 11) {
                    break;
                }
                i2++;
            } else {
                if (i == 3) {
                    if (type == 8 || type == 7) {
                        break;
                    }
                } else {
                    continue;
                }
                i2++;
            }
        }
        audioDeviceInfo2 = audioDeviceInfo;
        return audioDeviceInfo2;
    }

    public static CmmSipAudioMgr getInstance() {
        if (mInstance == null) {
            mInstance = new CmmSipAudioMgr();
        }
        return mInstance;
    }

    public void init() {
        startToListenPhoneState();
        HeadsetUtil.getInstance().addListener(this);
        addSipAudioListener(this.mSipAudioListener);
    }

    private CmmSipAudioMgr() {
    }

    private boolean isSpeakerPhoneOn() {
        return AssistantAppClientMgr.getInstance().isSpeakerPhoneOn();
    }

    public void resetAudioDevice() {
        if (isBluetoothHeadsetStarted()) {
            stopBluetoothHeadset();
        }
        toggleSpeakerState(false);
    }

    public void audioStartBluetoothSco() {
        if (HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !isBluetoothHeadsetStarted()) {
            startBluetoothHeadset();
            updateAudioSourceType();
        }
    }

    public void audioStopBluetoothSco() {
        if (HeadsetUtil.getInstance().isBluetoothHeadsetOn() && isBluetoothHeadsetStarted()) {
            stopBluetoothHeadset();
            updateAudioSourceType();
        }
    }

    private boolean toggleSpeakerPhone(boolean z) {
        return AssistantAppClientMgr.getInstance().toggleSpeakerPhone(z);
    }

    public void setPreferedLoudSpeakerStatus(int i) {
        this.mPreferedLoudspeakerStatus = i;
        if (-1 != i) {
            boolean z = false;
            toggleLocalSpeakerState(i == 1);
            if (i == 1) {
                z = true;
            }
            toggleSipSpeakerState(z);
        }
    }

    public int getPreferedLoudSpeakerStatus() {
        return this.mPreferedLoudspeakerStatus;
    }

    public void checkOpenLoudSpeaker() {
        boolean z = false;
        if (this.mIsCallOffHook) {
            if (!this.mIsAudioStoppedByCallOffHook) {
                setLoudSpeakerStatus(false);
                if (getMyAudioType() == 0) {
                    CmmSIPCallManager.getInstance().holdCall();
                    this.mIsAudioMutedByCallOffHook = true;
                }
                this.mIsAudioStoppedByCallOffHook = true;
            }
        } else if (getPreferedLoudSpeakerStatus() == 1) {
            stopBluetoothHeadset();
            setLoudSpeakerStatus(true);
        } else {
            if (HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !ismIsUseA2dpMode()) {
                int i = this.mPreferAudioType;
                if (i == 3 || i == -1 || !HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                    startBluetoothHeadset();
                    updateAudioSourceType();
                    return;
                }
            }
            stopBluetoothHeadset();
            if (getPreferedLoudSpeakerStatus() == 0) {
                setLoudSpeakerStatus(false);
            } else {
                if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                    z = true;
                }
                setLoudSpeakerStatus(z);
            }
        }
        updateAudioSourceType();
    }

    public void addListener(@Nullable PhoneCallListener phoneCallListener) {
        if (phoneCallListener != null) {
            IListener[] all = this.mPhoneCallListener.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == phoneCallListener) {
                    removeListener((PhoneCallListener) all[i]);
                }
            }
            this.mPhoneCallListener.add(phoneCallListener);
        }
    }

    public void removeListener(PhoneCallListener phoneCallListener) {
        this.mPhoneCallListener.remove(phoneCallListener);
    }

    private void performanceOnPhoneCallIdle() {
        IListener[] all = this.mPhoneCallListener.getAll();
        for (IListener iListener : all) {
            ((PhoneCallListener) iListener).onPhoneCallIdle();
        }
    }

    private void performanceOnPhoneCallOffhook() {
        IListener[] all = this.mPhoneCallListener.getAll();
        for (IListener iListener : all) {
            ((PhoneCallListener) iListener).onPhoneCallOffHook();
        }
    }

    private void startToListenPhoneState() {
        Context globalContext = VideoBoxApplication.getGlobalContext();
        if (globalContext != null) {
            TelephonyManager telephonyManager = (TelephonyManager) globalContext.getSystemService(BoxUser.FIELD_PHONE);
            if (telephonyManager != null) {
                this.mIsCallOffHook = telephonyManager.getCallState() == 2;
                this.mPhoneStateListener = new PhoneStateListener() {
                    public void onDataConnectionStateChanged(int i, int i2) {
                    }

                    public void onCallStateChanged(int i, String str) {
                        super.onCallStateChanged(i, str);
                        if (i == 0) {
                            CmmSipAudioMgr.this.onPhoneCallIdle();
                        } else if (i == 2) {
                            CmmSipAudioMgr.this.onPhoneCallOffHook();
                        }
                    }
                };
                try {
                    telephonyManager.listen(this.mPhoneStateListener, 96);
                } catch (Exception unused) {
                }
            }
        }
    }

    public void enablePhoneAudio() {
        IConfService confService = VideoBoxApplication.getNonNullInstance().getConfService();
        if (confService != null) {
            try {
                confService.disableConfAudio();
            } catch (Exception unused) {
            }
        }
        if (CmmSIPCallManager.getInstance().isCallExists()) {
            AssistantAppClientMgr.getInstance().startPlayout();
            AssistantAppClientMgr.getInstance().unSelectMicrophone();
            AssistantAppClientMgr.getInstance().selectDefaultMicrophone();
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    CmmSipAudioMgr.this.checkOpenLoudSpeaker();
                }
            }, 1000);
        }
    }

    public boolean disablePhoneAudio() {
        return disablePhoneAudio(false);
    }

    public boolean disablePhoneAudio(boolean z) {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null) {
            return false;
        }
        if (!z && !CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            return false;
        }
        if (checkHoldCurrentCall()) {
            Toast.makeText(instance, C4558R.string.zm_sip_inhold_in_call_offhook_66040, 1).show();
        }
        AssistantAppClientMgr.getInstance().stopPlayout();
        AssistantAppClientMgr.getInstance().unSelectMicrophone();
        new Thread(new Runnable() {
            public void run() {
                IPCHelper.getInstance().tryRetrieveConfMicrophone();
            }
        }).start();
        return true;
    }

    public void forceSwitchAudioToMeeting() {
        disablePhoneAudio(true);
    }

    public void onPhoneCallOffHook() {
        this.mIsCallOffHook = true;
        performanceOnPhoneCallOffhook();
        if (!this.mIsAudioStoppedByPauseAudio) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            if (instance.isInCall()) {
                VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
                if (instance2 != null) {
                    if (NetworkUtil.getDataNetworkType(instance2) == 0) {
                        instance.hangupAllCalls();
                    } else if (!instance.isInSwitchingToCarrier(instance.getCurrentCallID())) {
                        instance.holdCall();
                        Toast.makeText(instance2, C4558R.string.zm_sip_inhold_in_call_offhook_66040, 1).show();
                    }
                    toggleSpeakerState(false);
                    AssistantAppClientMgr.getInstance().stopPlayout();
                    this.mIsAudioStoppedByCallOffHook = true;
                    if (HeadsetUtil.getInstance().isBluetoothHeadsetOn() && HeadsetUtil.getInstance().isBluetoothScoAudioOn()) {
                        stopBluetoothHeadset();
                    }
                }
            }
        }
    }

    public void onPhoneCallIdle() {
        this.mIsCallOffHook = false;
        performanceOnPhoneCallIdle();
        if (this.mIsAudioStoppedByCallOffHook) {
            AssistantAppClientMgr.getInstance().startPlayout();
            this.mIsAudioStoppedByCallOffHook = false;
            this.mIsAudioMutedByCallOffHook = false;
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    CmmSipAudioMgr.this.checkOpenLoudSpeaker();
                }
            }, 1000);
        }
    }

    public void OnNewCallGenerate(String str, int i) {
        super.OnNewCallGenerate(str, i);
        performAudioSourceTypeChange();
    }

    public void OnCallStatusUpdate(String str, int i) {
        if (CmmSIPCallManager.getInstance().isValidInCallStatus(i)) {
            this.mIsAudioStoppedByCallOffHook = false;
            this.mIsAudioMutedByCallOffHook = false;
            if (isAudioInMeeting()) {
                checkHoldCurrentCall();
            }
        }
    }

    public void OnMeetingAudioSessionStatus(boolean z) {
        super.OnMeetingAudioSessionStatus(z);
        this.mMeetingAudioSessionStatus = z;
        if (z) {
            disablePhoneAudio();
        }
    }

    private boolean checkHoldCurrentCall() {
        if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            return CmmSIPCallManager.getInstance().checkHoldCurrentCall();
        }
        return false;
    }

    public void toggleSpeakerState(boolean z) {
        this.mIsUseA2dpMode = false;
        setPreferedLoudSpeakerStatus(z ? 1 : 0);
        checkOpenLoudSpeaker();
    }

    private void toggleSipSpeakerState(boolean z) {
        toggleSpeakerPhone(z);
    }

    private void toggleLocalSpeakerState(boolean z) {
        AudioManager audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(z);
        }
    }

    public boolean isSpeakerOn() {
        if (CmmSIPCallManager.getInstance().isInSipAudio()) {
            return isSipSpeakerOn();
        }
        return isLocalSpeakerOn();
    }

    private boolean isLocalSpeakerOn() {
        AudioManager audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        if (audioManager != null) {
            return audioManager.isSpeakerphoneOn();
        }
        return false;
    }

    private boolean isSipSpeakerOn() {
        return getInstance().isSpeakerPhoneOn();
    }

    private void notifyBluetoothScoAudioStatus(boolean z) {
        boolean z2 = this.mBluetoothScoStarted;
        this.mBluetoothScoStarted = z;
        if (z2 && !z && this.mStartScoCountDown == 0 && getPreferedLoudSpeakerStatus() != 1 && HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            this.mScoUnexpectedDisconnectTimes++;
            if (this.mScoUnexpectedDisconnectTimes > 2) {
                ZMLog.m286i(TAG, "notifyBluetoothScoAudioStatus, fallback to A2DP mode", new Object[0]);
                VoiceEngineCompat.blacklistBluetoothSco(true);
            }
            startBluetoothHeadset();
        }
    }

    /* access modifiers changed from: private */
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
                notifyVoiceSwitchToHeadsetOrEarSpeaker(true);
            } else if (this.mStartScoCountDown <= 0 && !this.mBluetoothScoStarted) {
                this.mStartScoCountDown = 4;
                this.mIsUseA2dpMode = false;
                this.mHandler.removeCallbacks(this.mRunnableStartSco);
                this.mHandler.post(this.mRunnableStartSco);
            }
        }
    }

    private void stopBluetoothHeadset() {
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
            notifyVoiceSwitchToHeadsetOrEarSpeaker(false);
        }
    }

    public void startWiredHeadset() {
        this.mIsUseA2dpMode = false;
        notifyVoiceSwitchToHeadsetOrEarSpeaker(true);
        updateAudioSourceType();
    }

    public void switchAudioSource(@NonNull Context context, long j, int i) {
        HeadsetUtil instance = HeadsetUtil.getInstance();
        int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
        boolean z = selectedPlayerStreamType == 0 || (selectedPlayerStreamType < 0 && isCallOffHook());
        boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(context);
        boolean z2 = instance.isBluetoothHeadsetOn() || instance.isWiredHeadsetOn();
        if (!z) {
            return;
        }
        if (!isFeatureTelephonySupported && !z2) {
            return;
        }
        if (j == 0 || isCallOffHook()) {
            if ((i == 3 && instance.isBluetoothHeadsetOn()) || i == 2 || i == 1) {
                setPreferedLoudSpeakerStatus(0);
            } else {
                setPreferedLoudSpeakerStatus(1);
            }
            changeAudioOutput(i);
        }
    }

    private void changeAudioOutput(int i) {
        this.mPreferAudioType = i;
        boolean z = false;
        if (this.mIsCallOffHook) {
            if (!this.mIsAudioStoppedByCallOffHook) {
                setLoudSpeakerStatus(false);
                if (getMyAudioType() == 0) {
                    CmmSIPCallManager.getInstance().holdCall();
                    this.mIsAudioMutedByCallOffHook = true;
                }
                this.mIsAudioStoppedByCallOffHook = true;
            }
        } else if (getPreferedLoudSpeakerStatus() == 1) {
            stopBluetoothHeadset();
            setLoudSpeakerStatus(true);
        } else if (i != 3 || !HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            stopBluetoothHeadset();
            if (i == 2) {
                startWiredHeadset();
            }
            if (getPreferedLoudSpeakerStatus() == 0) {
                setLoudSpeakerStatus(false);
            } else {
                if (!HeadsetUtil.getInstance().isBluetoothHeadsetOn() && !HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                    z = true;
                }
                setLoudSpeakerStatus(z);
            }
        } else {
            startBluetoothHeadset();
            updateAudioSourceType();
            return;
        }
        updateAudioSourceType();
    }

    public void setLoudSpeakerStatus(boolean z) {
        toggleLocalSpeakerState(z);
        toggleSipSpeakerState(z);
        if (VoiceEnginContext.getSelectedPlayerStreamType() != 3) {
            notifyVoiceSwitchToHeadsetOrEarSpeaker(!z);
        } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            notifyVoiceSwitchToHeadsetOrEarSpeaker(true);
        } else {
            notifyVoiceSwitchToHeadsetOrEarSpeaker(false);
        }
    }

    private void updateAudioSourceType() {
        HeadsetUtil instance = HeadsetUtil.getInstance();
        boolean z = instance.isBluetoothHeadsetOn() || instance.isWiredHeadsetOn();
        int i = this.mCurAudioSourceType;
        if (!z) {
            this.mCurAudioSourceType = 0;
            this.mSwitchableAudioSourceType = -1;
        } else if (!isSpeakerOn() || (instance.isBluetoothScoAudioOn() && VoiceEngineCompat.isBluetoothScoSupported())) {
            if (instance.isBluetoothScoAudioOn() && VoiceEngineCompat.isBluetoothScoSupported()) {
                this.mCurAudioSourceType = 3;
            } else if (instance.isBluetoothHeadsetOn() && (ismIsUseA2dpMode() || isStarttingSco())) {
                this.mCurAudioSourceType = 3;
            } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                this.mCurAudioSourceType = 2;
            } else {
                this.mCurAudioSourceType = 1;
            }
            this.mSwitchableAudioSourceType = 0;
        } else {
            this.mCurAudioSourceType = 0;
            if (instance.isBluetoothHeadsetOn()) {
                this.mSwitchableAudioSourceType = 0;
            } else if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
                this.mSwitchableAudioSourceType = 2;
            } else {
                this.mSwitchableAudioSourceType = 1;
            }
        }
        if (i != this.mCurAudioSourceType) {
            performAudioSourceTypeChange();
        }
    }

    private void performAudioSourceTypeChange() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ICmmSipAudioListener) iListener).onAudioSourceTypeChanged(this.mCurAudioSourceType);
            }
        }
    }

    public boolean isCallOffHook() {
        return this.mIsCallOffHook;
    }

    public static boolean isCallOffHook(@Nullable Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(BoxUser.FIELD_PHONE);
        if (telephonyManager == null) {
            return false;
        }
        if (telephonyManager.getCallState() == 2) {
            z = true;
        }
        return z;
    }

    private boolean isBluetoothHeadsetStarted() {
        return this.mBluetoothScoStarted;
    }

    public boolean ismIsUseA2dpMode() {
        return this.mIsUseA2dpMode;
    }

    private boolean isBluetoothScoSupported() {
        return VoiceEngineCompat.isBluetoothScoSupported();
    }

    public boolean isStarttingSco() {
        return this.mStartScoCountDown > 0;
    }

    public int getCurrentAudioSourceType() {
        return this.mCurAudioSourceType;
    }

    public void addSipAudioListener(ICmmSipAudioListener iCmmSipAudioListener) {
        this.mListenerList.add(iCmmSipAudioListener);
    }

    public void removeSipAudioListener(ICmmSipAudioListener iCmmSipAudioListener) {
        this.mListenerList.remove(iCmmSipAudioListener);
    }

    /* access modifiers changed from: private */
    public boolean notifyVoiceSwitchToHeadsetOrEarSpeaker(boolean z) {
        return AssistantAppClientMgr.getInstance().switchHeadsetOrEarSpeaker(z);
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        if (CmmSIPCallManager.isInit() && CmmSIPCallManager.getInstance().isInSipAudio()) {
            if (z || z2) {
                int targetAudioSourceTypeOnChanged = getTargetAudioSourceTypeOnChanged(z, z2);
                if (targetAudioSourceTypeOnChanged != getCurrentAudioSourceType()) {
                    switchAudioSource(VideoBoxApplication.getGlobalContext(), getMyAudioType(), targetAudioSourceTypeOnChanged);
                }
            } else {
                setPreferedLoudSpeakerStatus(getInstance().isSpeakerOn() ? 1 : 0);
                checkOpenLoudSpeaker();
            }
        }
        this.mLastBluetoothHeadsetConnected = z2;
        this.mLastWiredHeadsetConnected = z;
    }

    private int getTargetAudioSourceTypeOnChanged(boolean z, boolean z2) {
        int i;
        int currentAudioSourceType = getCurrentAudioSourceType();
        boolean z3 = currentAudioSourceType == 0;
        if (z || z2) {
            int i2 = (!z || (this.mLastWiredHeadsetConnected && (z2 || z3))) ? -1 : 2;
            i = (i2 != -1 || !z2 || (this.mLastBluetoothHeadsetConnected && (z || z3))) ? i2 : 3;
        } else {
            i = -1;
        }
        return i == -1 ? currentAudioSourceType : i;
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        notifyBluetoothScoAudioStatus(z);
    }

    public boolean audioDeviceChanged(String str, String str2) {
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return false;
        }
        return sipCallAPI.audioDeviceChanged(str, str2);
    }

    public boolean isAudioInMeeting() {
        return this.mMeetingAudioSessionStatus;
    }
}
