package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ISIPAudioFilePlayerEventSinkListenerUI {
    private static final String TAG = "ISIPAudioFilePlayerEventSinkListenerUI";
    @Nullable
    private static ISIPAudioFilePlayerEventSinkListenerUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface ISIPAudioFilePlayerEventSinkListener extends IListener {
        void OnPlayProgressChanged(int i);

        void OnPlayTerminated();
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized ISIPAudioFilePlayerEventSinkListenerUI getInstance() {
        ISIPAudioFilePlayerEventSinkListenerUI iSIPAudioFilePlayerEventSinkListenerUI;
        synchronized (ISIPAudioFilePlayerEventSinkListenerUI.class) {
            if (instance == null) {
                instance = new ISIPAudioFilePlayerEventSinkListenerUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            iSIPAudioFilePlayerEventSinkListenerUI = instance;
        }
        return iSIPAudioFilePlayerEventSinkListenerUI;
    }

    private ISIPAudioFilePlayerEventSinkListenerUI() {
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

    public void addListener(@Nullable ISIPAudioFilePlayerEventSinkListener iSIPAudioFilePlayerEventSinkListener) {
        if (iSIPAudioFilePlayerEventSinkListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iSIPAudioFilePlayerEventSinkListener) {
                    removeListener((ISIPAudioFilePlayerEventSinkListener) all[i]);
                }
            }
            this.mListenerList.add(iSIPAudioFilePlayerEventSinkListener);
        }
    }

    public void removeListener(ISIPAudioFilePlayerEventSinkListener iSIPAudioFilePlayerEventSinkListener) {
        this.mListenerList.remove(iSIPAudioFilePlayerEventSinkListener);
    }

    /* access modifiers changed from: protected */
    public void OnPlayProgressChanged(int i) {
        try {
            OnPlayProgressChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPlayProgressChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPAudioFilePlayerEventSinkListener) iListener).OnPlayProgressChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPlayTerminated() {
        try {
            OnPlayTerminatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPlayTerminatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPAudioFilePlayerEventSinkListener) iListener).OnPlayTerminated();
            }
        }
    }
}
