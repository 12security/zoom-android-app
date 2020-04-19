package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.util.PreferenceUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class NotificationSettingUI {
    private static final String TAG = "NotificationSettingUI";
    @Nullable
    private static NotificationSettingUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface INotificationSettingUIListener extends IListener {
        void OnBlockAllSettingsUpdated();

        void OnChannelsUnreadBadgeSettingUpdated();

        void OnDNDNowSettingUpdated();

        void OnDNDSettingsUpdated();

        void OnHLPersonSettingUpdated();

        void OnHintLineOptionUpdated();

        void OnInCallSettingUpdated();

        void OnKeyWordSettingUpdated();

        void OnMUCSettingUpdated();

        void OnReplyFollowThreadNotifySettingUpdated();

        void OnSnoozeSettingsUpdated();

        void OnUnreadBadgeSettingUpdated();

        void OnUnreadOnTopSettingUpdated();
    }

    public static abstract class SimpleNotificationSettingUIListener implements INotificationSettingUIListener {
        public void OnBlockAllSettingsUpdated() {
        }

        public void OnChannelsUnreadBadgeSettingUpdated() {
        }

        public void OnDNDNowSettingUpdated() {
        }

        public void OnDNDSettingsUpdated() {
        }

        public void OnHLPersonSettingUpdated() {
        }

        public void OnHintLineOptionUpdated() {
        }

        public void OnInCallSettingUpdated() {
        }

        public void OnKeyWordSettingUpdated() {
        }

        public void OnMUCSettingUpdated() {
        }

        public void OnReplyFollowThreadNotifySettingUpdated() {
        }

        public void OnSnoozeSettingsUpdated() {
        }

        public void OnUnreadBadgeSettingUpdated() {
        }

        public void OnUnreadOnTopSettingUpdated() {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable INotificationSettingUIListener iNotificationSettingUIListener) {
        if (iNotificationSettingUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iNotificationSettingUIListener) {
                    removeListener((INotificationSettingUIListener) all[i]);
                }
            }
            this.mListenerList.add(iNotificationSettingUIListener);
        }
    }

    public void removeListener(INotificationSettingUIListener iNotificationSettingUIListener) {
        this.mListenerList.remove(iNotificationSettingUIListener);
    }

    @NonNull
    public static synchronized NotificationSettingUI getInstance() {
        NotificationSettingUI notificationSettingUI;
        synchronized (NotificationSettingUI.class) {
            if (instance == null) {
                instance = new NotificationSettingUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            notificationSettingUI = instance;
        }
        return notificationSettingUI;
    }

    private NotificationSettingUI() {
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
    public void OnBlockAllSettingsUpdated() {
        try {
            OnBlockAllSettingsUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnBlockAllSettingsUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnBlockAllSettingsUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnDNDSettingsUpdated() {
        try {
            OnDNDSettingsUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnDNDSettingsUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnDNDSettingsUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSnoozeSettingsUpdated() {
        try {
            OnSnoozeSettingsUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSnoozeSettingsUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnSnoozeSettingsUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMUCSettingUpdated() {
        try {
            OnMUCSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMUCSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnMUCSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnInCallSettingUpdated() {
        try {
            OnInCallSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnInCallSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnInCallSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnDNDNowSettingUpdated() {
        try {
            OnDNDNowSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnDNDNowSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnDNDNowSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnHLPersonSettingUpdated() {
        try {
            OnHLPersonSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnHLPersonSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnHLPersonSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnKeyWordSettingUpdated() {
        try {
            OnKeyWordSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnKeyWordSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnKeyWordSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnUnreadOnTopSettingUpdated() {
        try {
            OnUnreadOnTopSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnreadOnTopSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnUnreadOnTopSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnUnreadBadgeSettingUpdated() {
        try {
            OnUnreadBadgeSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnreadBadgeSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnUnreadBadgeSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnChannelsUnreadBadgeSettingUpdated() {
        try {
            OnChannelsUnreadBadgeSettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnChannelsUnreadBadgeSettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnChannelsUnreadBadgeSettingUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnHintLineOptionUpdated() {
        try {
            OnHintLineOptionUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnHintLineOptionUpdatedImpl() {
        if (PreferenceUtil.containsKey(PreferenceUtil.UNREAD_START_FIRST)) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                int i = 1;
                if (!PreferenceUtil.readBooleanValue(PreferenceUtil.UNREAD_START_FIRST, true)) {
                    i = 2;
                }
                if (i == notificationSettingMgr.getHintLineForChannels() || notificationSettingMgr.setHintLineForChannels(i)) {
                    PreferenceUtil.removeValue(PreferenceUtil.UNREAD_START_FIRST);
                }
            }
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnHintLineOptionUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnReplyFollowThreadNotifySettingUpdated() {
        try {
            OnReplyFollowThreadNotifySettingUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnReplyFollowThreadNotifySettingUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((INotificationSettingUIListener) iListener).OnReplyFollowThreadNotifySettingUpdated();
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
