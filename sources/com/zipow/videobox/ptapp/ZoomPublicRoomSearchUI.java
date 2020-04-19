package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ZoomPublicRoomSearchUI {
    private static final String TAG = "ZoomPublicRoomSearchUI";
    @Nullable
    private static ZoomPublicRoomSearchUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IZoomPublicRoomSearchUIListener extends IListener {
        void onForbidJoinRoom(String str, int i);

        void onJoinRoom(String str, int i);

        void onSearchResponse(int i, int i2, int i3);
    }

    public static abstract class SimpleZoomPublicRoomSearchUIListener implements IZoomPublicRoomSearchUIListener {
        public void onForbidJoinRoom(String str, int i) {
        }

        public void onJoinRoom(String str, int i) {
        }

        public void onSearchResponse(int i, int i2, int i3) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable IZoomPublicRoomSearchUIListener iZoomPublicRoomSearchUIListener) {
        if (iZoomPublicRoomSearchUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomPublicRoomSearchUIListener) {
                    removeListener((IZoomPublicRoomSearchUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomPublicRoomSearchUIListener);
        }
    }

    public void removeListener(IZoomPublicRoomSearchUIListener iZoomPublicRoomSearchUIListener) {
        this.mListenerList.remove(iZoomPublicRoomSearchUIListener);
    }

    @NonNull
    public static synchronized ZoomPublicRoomSearchUI getInstance() {
        ZoomPublicRoomSearchUI zoomPublicRoomSearchUI;
        synchronized (ZoomPublicRoomSearchUI.class) {
            if (instance == null) {
                instance = new ZoomPublicRoomSearchUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            zoomPublicRoomSearchUI = instance;
        }
        return zoomPublicRoomSearchUI;
    }

    private ZoomPublicRoomSearchUI() {
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
    public void onSearchResponse(int i, int i2, int i3) {
        try {
            onSearchResponseImpl(i, i2, i3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onSearchResponseImpl(int i, int i2, int i3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPublicRoomSearchUIListener) iListener).onSearchResponse(i, i2, i3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onJoinRoom(String str, int i) {
        try {
            onJoinRoomImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onJoinRoomImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPublicRoomSearchUIListener) iListener).onJoinRoom(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onForbidJoinRoom(String str, int i) {
        try {
            onForbidJoinRoomImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onForbidJoinRoomImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomPublicRoomSearchUIListener) iListener).onForbidJoinRoom(str, i);
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
