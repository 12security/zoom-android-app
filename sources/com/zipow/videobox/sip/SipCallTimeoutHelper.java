package com.zipow.videobox.sip;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.util.HashMap;

public class SipCallTimeoutHelper {
    private static final long DEFAULT_TIMEOUT = 60000;
    public static final long LOCAL_CALLOUT_TIMEOUT = 15000;
    private static final String TAG = "SipCallTimeoutHelper";
    private HashMap<String, SipCallTimeoutWrapper> cache = new HashMap<>(4);

    public class SipCallTimeoutWrapper {
        public static final int MSG_TIMEOUT = 0;
        /* access modifiers changed from: private */
        public String mCallId;
        /* access modifiers changed from: private */
        public Handler mHandler = new Handler() {
            public void handleMessage(@NonNull Message message) {
                super.handleMessage(message);
                if (message.what == 0) {
                    SipCallTimeoutWrapper.this.mHandler.removeMessages(0);
                    if (SipCallTimeoutWrapper.this.mTimeoutCallback != null) {
                        SipCallTimeoutWrapper.this.mTimeoutCallback.onSipCallTimeout(SipCallTimeoutWrapper.this.mCallId);
                    }
                }
            }
        };
        /* access modifiers changed from: private */
        public TimeoutCallback mTimeoutCallback;

        public SipCallTimeoutWrapper(String str, TimeoutCallback timeoutCallback) {
            this.mCallId = str;
            this.mTimeoutCallback = timeoutCallback;
        }

        public void start(long j) {
            this.mHandler.sendEmptyMessageDelayed(0, j);
        }

        public void stop() {
            this.mHandler.removeMessages(0);
        }
    }

    public interface TimeoutCallback {
        void onSipCallTimeout(String str);
    }

    public void start(String str, TimeoutCallback timeoutCallback) {
        start(str, DEFAULT_TIMEOUT, timeoutCallback);
    }

    public void start(String str, long j, TimeoutCallback timeoutCallback) {
        if (!TextUtils.isEmpty(str) && !this.cache.containsKey(str)) {
            SipCallTimeoutWrapper sipCallTimeoutWrapper = new SipCallTimeoutWrapper(str, timeoutCallback);
            this.cache.put(str, sipCallTimeoutWrapper);
            sipCallTimeoutWrapper.start(j);
        }
    }

    public void stop(String str) {
        if (!TextUtils.isEmpty(str)) {
            SipCallTimeoutWrapper sipCallTimeoutWrapper = (SipCallTimeoutWrapper) this.cache.get(str);
            if (sipCallTimeoutWrapper != null) {
                sipCallTimeoutWrapper.stop();
            }
            this.cache.remove(str);
        }
    }

    public void stopAll() {
        if (!this.cache.isEmpty()) {
            for (String str : this.cache.keySet()) {
                if (!TextUtils.isEmpty(str)) {
                    SipCallTimeoutWrapper sipCallTimeoutWrapper = (SipCallTimeoutWrapper) this.cache.get(str);
                    if (sipCallTimeoutWrapper != null) {
                        sipCallTimeoutWrapper.stop();
                    }
                } else {
                    return;
                }
            }
            this.cache.clear();
        }
    }
}
