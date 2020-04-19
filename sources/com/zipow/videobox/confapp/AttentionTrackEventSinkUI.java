package com.zipow.videobox.confapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class AttentionTrackEventSinkUI {
    private static final String TAG = "AttentionTrackEventSinkUI";
    @Nullable
    private static AttentionTrackEventSinkUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IAttentionTrackEventSinkUIListener extends IListener {
        void OnConfAttentionTrackStatusChanged(boolean z);

        void OnUserAttentionStatusChanged(int i, boolean z);

        void OnWebinarAttendeeAttentionStatusChanged(int i, boolean z);
    }

    public static abstract class SimpleAttentionTrackEventSinkUIListener implements IAttentionTrackEventSinkUIListener {
        public void OnConfAttentionTrackStatusChanged(boolean z) {
        }

        public void OnUserAttentionStatusChanged(int i, boolean z) {
        }

        public void OnWebinarAttendeeAttentionStatusChanged(int i, boolean z) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    private AttentionTrackEventSinkUI() {
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

    /* access modifiers changed from: protected */
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

    public void addListener(@Nullable IAttentionTrackEventSinkUIListener iAttentionTrackEventSinkUIListener) {
        if (iAttentionTrackEventSinkUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iAttentionTrackEventSinkUIListener) {
                    removeListener((IAttentionTrackEventSinkUIListener) all[i]);
                }
            }
            this.mListenerList.add(iAttentionTrackEventSinkUIListener);
        }
    }

    public void removeListener(IAttentionTrackEventSinkUIListener iAttentionTrackEventSinkUIListener) {
        this.mListenerList.remove(iAttentionTrackEventSinkUIListener);
    }

    public void initialize() {
        nativeInit();
    }

    @NonNull
    public static synchronized AttentionTrackEventSinkUI getInstance() {
        AttentionTrackEventSinkUI attentionTrackEventSinkUI;
        synchronized (AttentionTrackEventSinkUI.class) {
            if (instance == null) {
                instance = new AttentionTrackEventSinkUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            attentionTrackEventSinkUI = instance;
        }
        return attentionTrackEventSinkUI;
    }

    public void OnConfAttentionTrackStatusChanged(boolean z) {
        try {
            OnConfAttentionTrackStatusChangedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnConfAttentionTrackStatusChangedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAttentionTrackEventSinkUIListener) iListener).OnConfAttentionTrackStatusChanged(z);
            }
        }
    }

    public void OnUserAttentionStatusChanged(int i, boolean z) {
        try {
            OnUserAttentionStatusChangedImpl(i, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnUserAttentionStatusChangedImpl(int i, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAttentionTrackEventSinkUIListener) iListener).OnUserAttentionStatusChanged(i, z);
            }
        }
    }

    public void OnWebinarAttendeeAttentionStatusChanged(int i, boolean z) {
        try {
            OnWebinarAttendeeAttentionStatusChangedImpl(i, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnWebinarAttendeeAttentionStatusChangedImpl(int i, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAttentionTrackEventSinkUIListener) iListener).OnWebinarAttendeeAttentionStatusChanged(i, z);
            }
        }
    }
}
