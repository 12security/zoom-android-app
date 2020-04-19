package com.zipow.videobox.confapp.poll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class PollingUI {
    private static final String TAG = "PollingUI";
    @Nullable
    private static PollingUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IPollingUIListener extends IListener {
        void onPollingActionResult(int i, String str, int i2);

        void onPollingDocReceived();

        void onPollingResultUpdated(String str);

        void onPollingStatusChanged(int i, String str);
    }

    public static abstract class SimplePollingUIListener implements IPollingUIListener {
        public void onPollingActionResult(int i, String str, int i2) {
        }

        public void onPollingDocReceived() {
        }

        public void onPollingResultUpdated(String str) {
        }

        public void onPollingStatusChanged(int i, String str) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized PollingUI getInstance() {
        PollingUI pollingUI;
        synchronized (PollingUI.class) {
            if (instance == null) {
                instance = new PollingUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            pollingUI = instance;
        }
        return pollingUI;
    }

    private PollingUI() {
        init();
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    private void init() {
        try {
            this.mNativeHandle = nativeInit();
        } catch (Throwable unused) {
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        long j = this.mNativeHandle;
        if (j != 0) {
            nativeUninit(j);
        }
        super.finalize();
    }

    public void addListener(@Nullable IPollingUIListener iPollingUIListener) {
        if (iPollingUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iPollingUIListener) {
                    removeListener((IPollingUIListener) all[i]);
                }
            }
            this.mListenerList.add(iPollingUIListener);
        }
    }

    public void removeListener(IPollingUIListener iPollingUIListener) {
        this.mListenerList.remove(iPollingUIListener);
    }

    /* access modifiers changed from: protected */
    public void onPollingDocReceived() {
        try {
            onPollingDocReceivedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onPollingDocReceivedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPollingUIListener) iListener).onPollingDocReceived();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPollingStatusChanged(int i, String str) {
        try {
            onPollingStatusChangedImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onPollingStatusChangedImpl(int i, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPollingUIListener) iListener).onPollingStatusChanged(i, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPollingResultUpdated(String str) {
        try {
            onPollingResultUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onPollingResultUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPollingUIListener) iListener).onPollingResultUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPollingActionResult(int i, String str, int i2) {
        try {
            onPollingActionResultImpl(i, str, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onPollingActionResultImpl(int i, String str, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPollingUIListener) iListener).onPollingActionResult(i, str, i2);
            }
        }
    }
}
