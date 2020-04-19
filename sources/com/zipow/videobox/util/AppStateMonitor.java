package com.zipow.videobox.util;

import android.os.Handler;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.ConfProcessMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class AppStateMonitor {
    private static final String TAG = "AppStateMonitor";
    @Nullable
    private static AppStateMonitor instance;
    @NonNull
    private Handler mHandler = new Handler();
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private boolean mbConfAtForground = false;
    private boolean mbPTAtForground = false;

    public interface IAppStateListener extends IListener {
        void onAppActivated();

        void onAppInactivated();
    }

    @NonNull
    public static synchronized AppStateMonitor getInstance() {
        AppStateMonitor appStateMonitor;
        synchronized (AppStateMonitor.class) {
            if (instance == null) {
                instance = new AppStateMonitor();
            }
            appStateMonitor = instance;
        }
        return appStateMonitor;
    }

    private AppStateMonitor() {
    }

    private boolean isConfProcessRunning() {
        return ConfProcessMgr.getInstance().isConfProcessRunning();
    }

    public void onPTUIMoveToFront() {
        if (!this.mbPTAtForground && (!isConfProcessRunning() || !this.mbConfAtForground)) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.this.notifyAppActive();
                }
            });
        }
        this.mbPTAtForground = true;
    }

    public void onPTUIMoveToBackground() {
        if (!isConfProcessRunning() || !this.mbConfAtForground) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.this.notifyAppInactive();
                }
            });
        }
        this.mbPTAtForground = false;
    }

    public void onConfUIMoveToFront() {
        if (!this.mbPTAtForground && !this.mbConfAtForground) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.this.notifyAppActive();
                }
            });
        }
        this.mbConfAtForground = true;
    }

    public void onConfUIMoveToBackground() {
        if (!this.mbPTAtForground) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    AppStateMonitor.this.notifyAppInactive();
                }
            });
        }
        this.mbConfAtForground = false;
    }

    public void addListener(@Nullable IAppStateListener iAppStateListener) {
        if (iAppStateListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i].getClass() == iAppStateListener.getClass()) {
                    removeListener((IAppStateListener) all[i]);
                }
            }
            this.mListenerList.add(iAppStateListener);
        }
    }

    public void removeListener(IAppStateListener iAppStateListener) {
        this.mListenerList.remove(iAppStateListener);
    }

    public void start() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            boolean z = true;
            if (instance2.isPTApp()) {
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                if (frontActivity == null || !frontActivity.isActive()) {
                    z = false;
                }
                this.mbPTAtForground = z;
                IConfService confService = VideoBoxApplication.getInstance().getConfService();
                if (confService != null) {
                    try {
                        this.mbConfAtForground = confService.isConfAppAtFront();
                    } catch (RemoteException unused) {
                    }
                }
            } else if (instance2.isConfApp()) {
                ZMActivity frontActivity2 = ZMActivity.getFrontActivity();
                if (frontActivity2 == null || !frontActivity2.isActive()) {
                    z = false;
                }
                this.mbConfAtForground = z;
                IPTService pTService = VideoBoxApplication.getInstance().getPTService();
                if (pTService != null) {
                    try {
                        this.mbPTAtForground = pTService.isPTAppAtFront();
                    } catch (RemoteException unused2) {
                    }
                }
            }
            if (this.mbPTAtForground || this.mbConfAtForground) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AppStateMonitor.this.notifyAppActive();
                    }
                });
            } else {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        AppStateMonitor.this.notifyAppInactive();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyAppInactive() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAppStateListener) iListener).onAppInactivated();
            }
        }
    }

    /* access modifiers changed from: private */
    public void notifyAppActive() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IAppStateListener) iListener).onAppActivated();
            }
        }
    }
}
