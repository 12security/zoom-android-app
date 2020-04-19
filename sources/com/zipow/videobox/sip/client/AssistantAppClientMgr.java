package com.zipow.videobox.sip.client;

import com.zipow.videobox.mainboard.Mainboard;

public class AssistantAppClientMgr {
    private static final String TAG = "AssistantAppClientMgr";
    private static AssistantAppClientMgr mInstance;

    private native void dispatchIdleMessageImpl();

    private native void initImpl();

    private native boolean isInitImpl();

    private native boolean isSpeakerPhoneOnImpl();

    private native void notifyAppStopImpl();

    private native int selectDefaultMicrophoneImpl();

    private native int startPlayoutImpl();

    private native int stopPlayoutImpl();

    private native boolean switchHeadsetOrEarSpeakerImpl(boolean z);

    private native boolean toggleSpeakerPhoneImpl(boolean z);

    private native void unInitImpl();

    private native int unSelectMicrophoneImpl();

    private AssistantAppClientMgr() {
    }

    public static synchronized AssistantAppClientMgr getInstance() {
        AssistantAppClientMgr assistantAppClientMgr;
        synchronized (AssistantAppClientMgr.class) {
            if (mInstance == null) {
                mInstance = new AssistantAppClientMgr();
            }
            assistantAppClientMgr = mInstance;
        }
        return assistantAppClientMgr;
    }

    public void dispatchIdleMessage() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            dispatchIdleMessageImpl();
        }
    }

    public void init() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            initImpl();
        }
    }

    public void unInit() {
        unInitImpl();
    }

    public boolean isInit() {
        return isInitImpl();
    }

    public boolean toggleSpeakerPhone(boolean z) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return false;
        }
        return toggleSpeakerPhoneImpl(z);
    }

    public boolean isSpeakerPhoneOn() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return false;
        }
        return isSpeakerPhoneOnImpl();
    }

    public void startPlayout() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            startPlayoutImpl();
        }
    }

    public void stopPlayout() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            stopPlayoutImpl();
        }
    }

    public boolean switchHeadsetOrEarSpeaker(boolean z) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return false;
        }
        return switchHeadsetOrEarSpeakerImpl(z);
    }

    public void unSelectMicrophone() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            unSelectMicrophoneImpl();
        }
    }

    public void selectDefaultMicrophone() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard != null && mainboard.isInitialized()) {
            selectDefaultMicrophoneImpl();
        }
    }

    public void notifyAppStop() {
        notifyAppStopImpl();
    }
}
