package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.sticker.StickerManager;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class PrivateStickerUICallBack {
    private static final String TAG = "PrivateStickerUICallBack";
    @Nullable
    private static PrivateStickerUICallBack instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IZoomPrivateStickerUIListener extends IListener {
        void OnDiscardPrivateSticker(int i, String str);

        void OnMakePrivateSticker(int i, String str, String str2);

        void OnNewStickerUploaded(String str, int i, String str2);

        void OnPrivateStickersUpdated();

        void OnSendPrivateSticker(String str, int i, String str2, String str3);

        void OnSendStickerMsgAppended(String str, String str2);

        void OnStickerDownloaded(String str, int i);
    }

    public static abstract class SimpleZoomPrivateStickerUIListener implements IZoomPrivateStickerUIListener {
        public void OnDiscardPrivateSticker(int i, String str) {
        }

        public void OnMakePrivateSticker(int i, String str, String str2) {
        }

        public void OnNewStickerUploaded(String str, int i, String str2) {
        }

        public void OnPrivateStickersUpdated() {
        }

        public void OnSendPrivateSticker(String str, int i, String str2, String str3) {
        }

        public void OnSendStickerMsgAppended(String str, String str2) {
        }

        public void OnStickerDownloaded(String str, int i) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable IZoomPrivateStickerUIListener iZoomPrivateStickerUIListener) {
        if (iZoomPrivateStickerUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomPrivateStickerUIListener) {
                    removeListener((IZoomPrivateStickerUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomPrivateStickerUIListener);
        }
    }

    public void removeListener(IZoomPrivateStickerUIListener iZoomPrivateStickerUIListener) {
        this.mListenerList.remove(iZoomPrivateStickerUIListener);
    }

    @NonNull
    public static synchronized PrivateStickerUICallBack getInstance() {
        PrivateStickerUICallBack privateStickerUICallBack;
        synchronized (PrivateStickerUICallBack.class) {
            if (instance == null) {
                instance = new PrivateStickerUICallBack();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            privateStickerUICallBack = instance;
        }
        return privateStickerUICallBack;
    }

    private PrivateStickerUICallBack() {
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
    public void OnNewStickerUploaded(String str, int i, String str2) {
        try {
            OnNewStickerUploadedImpl(str, i, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewStickerUploadedImpl(String str, int i, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnNewStickerUploaded(str, i, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMakePrivateSticker(int i, String str, String str2) {
        try {
            OnMakePrivateStickerImpl(i, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMakePrivateStickerImpl(int i, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnMakePrivateSticker(i, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnDiscardPrivateSticker(int i, String str) {
        try {
            OnDiscardPrivateStickerImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnDiscardPrivateStickerImpl(int i, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnDiscardPrivateSticker(i, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSendPrivateSticker(String str, int i, String str2, String str3) {
        try {
            OnSendPrivateStickerImpl(str, i, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSendPrivateStickerImpl(String str, int i, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnSendPrivateSticker(str, i, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnPrivateStickersUpdated() {
        try {
            OnPrivateStickersUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnPrivateStickersUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnPrivateStickersUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnStickerDownloaded(String str, int i) {
        try {
            OnStickerDownloadedImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnStickerDownloadedImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnStickerDownloaded(str, i);
            }
        }
        StickerManager.removeStickerPendingDownloadByReqId(str);
    }

    /* access modifiers changed from: protected */
    public void OnSendStickerMsgAppended(String str, String str2) {
        try {
            OnSendStickerMsgAppendedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSendStickerMsgAppendedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPrivateStickerUIListener) iListener).OnSendStickerMsgAppended(str, str2);
            }
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
}
