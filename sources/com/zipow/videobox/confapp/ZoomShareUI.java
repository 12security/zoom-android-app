package com.zipow.videobox.confapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ZoomShareUI {
    private static final String TAG = "ZoomShareUI";
    @Nullable
    private static ZoomShareUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface IZoomShareUIListener extends IListener {
        void OnActiveShareSourceChanged(long j);

        void OnDeclineRemoteControlResponseReceived(long j);

        void OnEnterRemoteControllingStatus(long j);

        void OnGotRemoteControlPrivilege(long j);

        void OnLeaveRemoteControllingStatus(long j);

        void OnLostRemoteControlPrivilege(long j);

        void OnNewShareSourceViewable(long j);

        void OnRemoteControlPrivilegeChanged(long j, long j2);

        void OnRemoteControlRequestReceived(long j);

        void OnRemoteControllingStatusChanged(long j, long j2);

        void OnShareCapturerStatusChanged(int i, int i2, int i3);

        void OnShareContentSizeChanged(long j);

        void OnShareSettingTypeChanged(int i);

        void OnShareSourceAnnotationSupportPropertyChanged(long j, boolean z);

        void OnShareSourceAudioSharingPropertyChanged(long j, boolean z);

        void OnShareSourceClosed(long j);

        void OnShareSourceContentTypeChanged(long j, int i);

        void OnShareSourceRemoteControlSupportPropertyChanged(long j, boolean z);

        void OnShareSourceSendStatusChanged(long j, boolean z);

        void OnShareSourceVideoSharingPropertyChanged(long j, boolean z);

        void OnStartReceivingShareContent(long j);

        void OnStartSendShare();

        void OnStartViewPureComputerAudio(long j);

        void OnStopSendShare();

        void OnStopViewPureComputerAudio(long j);
    }

    public static abstract class SimpleZoomShareUIListener implements IZoomShareUIListener {
        public void OnActiveShareSourceChanged(long j) {
        }

        public void OnDeclineRemoteControlResponseReceived(long j) {
        }

        public void OnEnterRemoteControllingStatus(long j) {
        }

        public void OnGotRemoteControlPrivilege(long j) {
        }

        public void OnLeaveRemoteControllingStatus(long j) {
        }

        public void OnLostRemoteControlPrivilege(long j) {
        }

        public void OnNewShareSourceViewable(long j) {
        }

        public void OnRemoteControlPrivilegeChanged(long j, long j2) {
        }

        public void OnRemoteControlRequestReceived(long j) {
        }

        public void OnRemoteControllingStatusChanged(long j, long j2) {
        }

        public void OnShareCapturerStatusChanged(int i, int i2, int i3) {
        }

        public void OnShareContentSizeChanged(long j) {
        }

        public void OnShareSettingTypeChanged(int i) {
        }

        public void OnShareSourceAnnotationSupportPropertyChanged(long j, boolean z) {
        }

        public void OnShareSourceAudioSharingPropertyChanged(long j, boolean z) {
        }

        public void OnShareSourceClosed(long j) {
        }

        public void OnShareSourceContentTypeChanged(long j, int i) {
        }

        public void OnShareSourceRemoteControlSupportPropertyChanged(long j, boolean z) {
        }

        public void OnShareSourceSendStatusChanged(long j, boolean z) {
        }

        public void OnShareSourceVideoSharingPropertyChanged(long j, boolean z) {
        }

        public void OnStartReceivingShareContent(long j) {
        }

        public void OnStartSendShare() {
        }

        public void OnStartViewPureComputerAudio(long j) {
        }

        public void OnStopSendShare() {
        }

        public void OnStopViewPureComputerAudio(long j) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized ZoomShareUI getInstance() {
        ZoomShareUI zoomShareUI;
        synchronized (ZoomShareUI.class) {
            if (instance == null) {
                instance = new ZoomShareUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            zoomShareUI = instance;
        }
        return zoomShareUI;
    }

    private ZoomShareUI() {
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

    public void addListener(@Nullable IZoomShareUIListener iZoomShareUIListener) {
        if (iZoomShareUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomShareUIListener) {
                    removeListener((IZoomShareUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomShareUIListener);
        }
    }

    public void removeListener(IZoomShareUIListener iZoomShareUIListener) {
        this.mListenerList.remove(iZoomShareUIListener);
    }

    public void OnShareSettingTypeChanged(int i) {
        try {
            OnShareSettingTypeChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSettingTypeChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSettingTypeChanged(i);
            }
        }
    }

    public void OnActiveShareSourceChanged(long j) {
        try {
            OnActiveShareSourceChangedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnActiveShareSourceChangedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnActiveShareSourceChanged(j);
            }
        }
    }

    public void OnShareContentSizeChanged(long j) {
        try {
            OnShareContentSizeChangedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareContentSizeChangedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareContentSizeChanged(j);
            }
        }
    }

    public void OnShareSourceSendStatusChanged(long j, boolean z) {
        try {
            OnShareSourceSendStatusChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceSendStatusChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceSendStatusChanged(j, z);
            }
        }
    }

    public void OnShareSourceContentTypeChanged(long j, int i) {
        try {
            OnShareSourceContentTypeChangedImpl(j, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceContentTypeChangedImpl(long j, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceContentTypeChanged(j, i);
            }
        }
    }

    public void OnShareSourceRemoteControlSupportPropertyChanged(long j, boolean z) {
        try {
            OnShareSourceRemoteControlSupportPropertyChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceRemoteControlSupportPropertyChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceRemoteControlSupportPropertyChanged(j, z);
            }
        }
    }

    public void OnShareSourceAnnotationSupportPropertyChanged(long j, boolean z) {
        try {
            OnShareSourceAnnotationSupportPropertyChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceAnnotationSupportPropertyChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceAnnotationSupportPropertyChanged(j, z);
            }
        }
    }

    public void OnShareSourceAudioSharingPropertyChanged(long j, boolean z) {
        try {
            OnShareSourceAudioSharingPropertyChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceAudioSharingPropertyChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceAudioSharingPropertyChanged(j, z);
            }
        }
    }

    public void OnShareSourceVideoSharingPropertyChanged(long j, boolean z) {
        try {
            OnShareSourceVideoSharingPropertyChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceVideoSharingPropertyChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceVideoSharingPropertyChanged(j, z);
            }
        }
    }

    public void OnStartSendShare() {
        try {
            OnStartSendShareImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnStartSendShareImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnStartSendShare();
            }
        }
    }

    public void OnStopSendShare() {
        try {
            OnStopSendShareImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnStopSendShareImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnStopSendShare();
            }
        }
    }

    public void OnShareCapturerStatusChanged(int i, int i2, int i3) {
        try {
            OnShareCapturerStatusChangedImpl(i, i2, i3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareCapturerStatusChangedImpl(int i, int i2, int i3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareCapturerStatusChanged(i, i2, i3);
            }
        }
    }

    public void OnNewShareSourceViewable(long j) {
        try {
            OnNewShareSourceViewableImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnNewShareSourceViewableImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnNewShareSourceViewable(j);
            }
        }
    }

    public void OnShareSourceClosed(long j) {
        try {
            OnShareSourceClosedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnShareSourceClosedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnShareSourceClosed(j);
            }
        }
    }

    public void OnStartReceivingShareContent(long j) {
        try {
            OnStartReceivingShareContentImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnStartReceivingShareContentImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnStartReceivingShareContent(j);
            }
        }
    }

    public void OnRemoteControlRequestReceived(long j) {
        try {
            OnRemoteControlRequestReceivedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnRemoteControlRequestReceivedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnRemoteControlRequestReceived(j);
            }
        }
    }

    public void OnRemoteControlPrivilegeChanged(long j, long j2) {
        try {
            OnRemoteControlPrivilegeChangedImpl(j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnRemoteControlPrivilegeChangedImpl(long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnRemoteControlPrivilegeChanged(j, j2);
            }
        }
    }

    public void OnRemoteControllingStatusChanged(long j, long j2) {
        try {
            OnRemoteControllingStatusChangedImpl(j, j2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnRemoteControllingStatusChangedImpl(long j, long j2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnRemoteControllingStatusChanged(j, j2);
            }
        }
    }

    public void OnDeclineRemoteControlResponseReceived(long j) {
        try {
            OnDeclineRemoteControlResponseReceivedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnDeclineRemoteControlResponseReceivedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnDeclineRemoteControlResponseReceived(j);
            }
        }
    }

    public void OnGotRemoteControlPrivilege(long j) {
        try {
            OnGotRemoteControlPrivilegeImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnGotRemoteControlPrivilegeImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnGotRemoteControlPrivilege(j);
            }
        }
    }

    public void OnLostRemoteControlPrivilege(long j) {
        try {
            OnLostRemoteControlPrivilegeImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnLostRemoteControlPrivilegeImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnLostRemoteControlPrivilege(j);
            }
        }
    }

    public void OnEnterRemoteControllingStatus(long j) {
        try {
            OnEnterRemoteControllingStatusImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnEnterRemoteControllingStatusImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnEnterRemoteControllingStatus(j);
            }
        }
    }

    public void OnLeaveRemoteControllingStatus(long j) {
        try {
            OnLeaveRemoteControllingStatusImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnLeaveRemoteControllingStatusImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnLeaveRemoteControllingStatus(j);
            }
        }
    }

    public void OnStartViewPureComputerAudio(long j) {
        try {
            OnStartViewPureComputerAudioImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnStartViewPureComputerAudioImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnStartViewPureComputerAudio(j);
            }
        }
    }

    public void OnStopViewPureComputerAudio(long j) {
        try {
            OnStopViewPureComputerAudioImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnStopViewPureComputerAudioImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomShareUIListener) iListener).OnStopViewPureComputerAudio(j);
            }
        }
    }
}
