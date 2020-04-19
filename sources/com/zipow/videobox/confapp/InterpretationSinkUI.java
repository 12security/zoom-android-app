package com.zipow.videobox.confapp;

import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class InterpretationSinkUI {
    private static final String TAG = "InterpretationSinkUI";
    private static InterpretationSinkUI instance;
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IInterpretationSinkUIListener extends IListener {
        void OnInterpretationStart();

        void OnInterpretationStop();

        void OnInterpreterInfoChanged(long j, int i);

        void OnInterpreterListChanged();

        void OnParticipantActiveLanChanged(long j);

        void OnParticipantActiveLanInvalid();
    }

    public static abstract class SimpleInterpretationSinkUIListener implements IInterpretationSinkUIListener {
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public static synchronized InterpretationSinkUI getInstance() {
        InterpretationSinkUI interpretationSinkUI;
        synchronized (InterpretationSinkUI.class) {
            if (instance == null) {
                instance = new InterpretationSinkUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            interpretationSinkUI = instance;
        }
        return interpretationSinkUI;
    }

    private InterpretationSinkUI() {
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

    public void addListener(IInterpretationSinkUIListener iInterpretationSinkUIListener) {
        if (iInterpretationSinkUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iInterpretationSinkUIListener) {
                    removeListener((IInterpretationSinkUIListener) all[i]);
                }
            }
            this.mListenerList.add(iInterpretationSinkUIListener);
        }
    }

    public void removeListener(IInterpretationSinkUIListener iInterpretationSinkUIListener) {
        this.mListenerList.remove(iInterpretationSinkUIListener);
    }

    public void OnInterpretationStart() {
        try {
            OnInterpretationStartImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnInterpretationStartImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnInterpretationStart();
            }
        }
    }

    public void OnInterpreterListChanged() {
        try {
            OnInterpreterListChangedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnInterpreterListChangedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnInterpreterListChanged();
            }
        }
    }

    public void OnInterpretationStop() {
        try {
            OnInterpretationStopImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnInterpretationStopImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnInterpretationStop();
            }
        }
    }

    public void OnInterpreterInfoChanged(long j, int i) {
        try {
            OnInterpreterInfoChangedImpl(j, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnInterpreterInfoChangedImpl(long j, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnInterpreterInfoChanged(j, i);
            }
        }
    }

    public void OnParticipantActiveLanChanged(long j) {
        try {
            OnParticipantActiveLanChangedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnParticipantActiveLanChangedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnParticipantActiveLanChanged(j);
            }
        }
    }

    public void OnParticipantActiveLanInvalid() {
        try {
            OnParticipantActiveLanInvalidImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnParticipantActiveLanInvalidImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IInterpretationSinkUIListener) iListener).OnParticipantActiveLanInvalid();
            }
        }
    }
}
