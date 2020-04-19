package com.zipow.videobox.sip;

import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.SipCallTimeoutHelper.TimeoutCallback;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallItemLocal;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import p021us.zoom.videomeetings.C4558R;

public class SipCallTimeoutMgr extends SimpleSIPCallEventListener implements TimeoutCallback {
    private static final String TAG = "SipCallTimeoutMgr";
    private static final SipCallTimeoutMgr ourInstance = new SipCallTimeoutMgr();
    private SipCallTimeoutHelper mSipCallTimeoutHelper;

    public static SipCallTimeoutMgr getInstance() {
        return ourInstance;
    }

    private SipCallTimeoutMgr() {
    }

    public void OnHangupAllCallsResult(boolean z) {
        super.OnHangupAllCallsResult(z);
        if (z) {
            stopAllSipCallTimeout();
        }
    }

    public void OnCallTerminate(String str, int i) {
        super.OnCallTerminate(str, i);
        stopSipCallTimeout(str);
    }

    public void OnNewCallGenerate(String str, int i) {
        super.OnNewCallGenerate(str, i);
        if (i == 0) {
            startSipCallTimeout(str);
        } else if (i == 1 || i == 4) {
            if (CmmSIPCallItemLocal.isLocal(str)) {
                startSipCallTimeout(str, SipCallTimeoutHelper.LOCAL_CALLOUT_TIMEOUT);
            }
        } else if (i == 6) {
            startSipCallTimeout(str);
        }
    }

    public void OnCallStatusUpdate(String str, int i) {
        super.OnCallStatusUpdate(str, i);
        if (CmmSIPCallManager.getInstance().isValidInCallStatus(i)) {
            stopSipCallTimeout(str);
        }
    }

    public void init() {
        this.mSipCallTimeoutHelper = new SipCallTimeoutHelper();
    }

    private void startSipCallTimeout(String str) {
        if (this.mSipCallTimeoutHelper == null) {
            this.mSipCallTimeoutHelper = new SipCallTimeoutHelper();
        }
        this.mSipCallTimeoutHelper.start(str, this);
    }

    private void startSipCallTimeout(String str, long j) {
        if (this.mSipCallTimeoutHelper == null) {
            this.mSipCallTimeoutHelper = new SipCallTimeoutHelper();
        }
        this.mSipCallTimeoutHelper.start(str, j, this);
    }

    public void stopSipCallTimeout(String str) {
        if (this.mSipCallTimeoutHelper == null) {
            this.mSipCallTimeoutHelper = new SipCallTimeoutHelper();
        }
        this.mSipCallTimeoutHelper.stop(str);
    }

    public void stopAllSipCallTimeout() {
        if (this.mSipCallTimeoutHelper == null) {
            this.mSipCallTimeoutHelper = new SipCallTimeoutHelper();
        }
        this.mSipCallTimeoutHelper.stopAll();
    }

    public void onSipCallTimeout(String str) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(str);
        if (callItemByCallID == null) {
            stopSipCallTimeout(str);
            return;
        }
        if (instance.isCallout(callItemByCallID)) {
            if (instance.isCurrentCallLocal()) {
                VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
                if (instance2 != null) {
                    instance.showErrorDialog(instance2.getString(C4558R.string.zm_title_error), instance2.getString(C4558R.string.zm_sip_callout_failed_27110), 1024);
                }
            }
            instance.hangupCall(str);
        } else {
            instance.declineCallWithNotAvailable(str);
        }
    }
}
