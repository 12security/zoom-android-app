package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.PTAppProtos.PBXSessionUnreadCountList;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class IPBXMessageEventSinkUI {
    private static final String TAG = "IPBXMessageEventSinkUI";
    private static IPBXMessageEventSinkUI instance;
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IPBXMessageEventSinkUIListener extends IListener {
        void OnFullSyncedMessages(int i, String str);

        void OnFullSyncedSessions(int i);

        void OnLocalSessionDeleted(String str);

        void OnLocalSessionMessageDeleted(String str, String str2);

        void OnMessageUpdated(String str, String str2);

        void OnMessagesDeleted(String str, List<String> list);

        void OnNewLocalSessionCreated(String str);

        void OnNewLocalSessionMessageCreated(String str, String str2);

        void OnNewMessageCreated(String str, String str2);

        void OnNewSessionCreated(String str, String str2);

        void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list);

        void OnRequestDoneForDeleteSessions(int i, String str, List<String> list);

        void OnRequestDoneForMarkSessionAsRead(int i, String str, String str2);

        void OnRequestDoneForQuerySessionByFromToNumbers(int i, String str, String str2);

        void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4);

        void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list);

        void OnRequestDoneForSyncOldSessions(int i, String str, List<String> list);

        void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list);

        void OnSessionUnreadMessageCountUpdated(PBXSessionUnreadCountList pBXSessionUnreadCountList);

        void OnSessionUpdated(String str);

        void OnSessionsDeleted(List<String> list);

        void OnSyncedNewMessages(int i, String str, String str2, List<String> list, List<String> list2, List<String> list3);

        void OnSyncedNewSessions(int i, String str, List<String> list, List<String> list2, List<String> list3);

        void OnTotalUnreadCountChanged();
    }

    public static class SimpleIPBXMessageEventSinkUIListener implements IPBXMessageEventSinkUIListener {
        public void OnFullSyncedMessages(int i, String str) {
        }

        public void OnFullSyncedSessions(int i) {
        }

        public void OnLocalSessionDeleted(String str) {
        }

        public void OnLocalSessionMessageDeleted(String str, String str2) {
        }

        public void OnMessageUpdated(String str, String str2) {
        }

        public void OnMessagesDeleted(String str, List<String> list) {
        }

        public void OnNewLocalSessionCreated(String str) {
        }

        public void OnNewLocalSessionMessageCreated(String str, String str2) {
        }

        public void OnNewMessageCreated(String str, String str2) {
        }

        public void OnNewSessionCreated(String str, String str2) {
        }

        public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
        }

        public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
        }

        public void OnRequestDoneForMarkSessionAsRead(int i, String str, String str2) {
        }

        public void OnRequestDoneForQuerySessionByFromToNumbers(int i, String str, String str2) {
        }

        public void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4) {
        }

        public void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list) {
        }

        public void OnRequestDoneForSyncOldSessions(int i, String str, List<String> list) {
        }

        public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
        }

        public void OnSessionUnreadMessageCountUpdated(PBXSessionUnreadCountList pBXSessionUnreadCountList) {
        }

        public void OnSessionUpdated(String str) {
        }

        public void OnSessionsDeleted(List<String> list) {
        }

        public void OnSyncedNewMessages(int i, String str, String str2, List<String> list, List<String> list2, List<String> list3) {
        }

        public void OnSyncedNewSessions(int i, String str, List<String> list, List<String> list2, List<String> list3) {
        }

        public void OnTotalUnreadCountChanged() {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public static synchronized IPBXMessageEventSinkUI getInstance() {
        IPBXMessageEventSinkUI iPBXMessageEventSinkUI;
        synchronized (IPBXMessageEventSinkUI.class) {
            if (instance == null) {
                instance = new IPBXMessageEventSinkUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            iPBXMessageEventSinkUI = instance;
        }
        return iPBXMessageEventSinkUI;
    }

    private IPBXMessageEventSinkUI() {
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

    public void addListener(IPBXMessageEventSinkUIListener iPBXMessageEventSinkUIListener) {
        IListener[] all;
        if (iPBXMessageEventSinkUIListener != null) {
            for (IListener iListener : this.mListenerList.getAll()) {
                if (iListener == iPBXMessageEventSinkUIListener) {
                    removeListener((IPBXMessageEventSinkUIListener) iListener);
                }
            }
            this.mListenerList.add(iPBXMessageEventSinkUIListener);
        }
    }

    public void removeListener(IPBXMessageEventSinkUIListener iPBXMessageEventSinkUIListener) {
        this.mListenerList.remove(iPBXMessageEventSinkUIListener);
    }

    /* access modifiers changed from: protected */
    public void OnFullSyncedSessions(int i) {
        try {
            OnFullSyncedSessionsImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFullSyncedSessionsImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnFullSyncedSessions(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSyncedNewSessions(int i, String str, List<String> list, List<String> list2, List<String> list3) {
        try {
            OnSyncedNewSessionsImpl(i, str, list, list2, list3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSyncedNewSessionsImpl(int i, String str, List<String> list, List<String> list2, List<String> list3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IPBXMessageEventSinkUIListener) all[i2]).OnSyncedNewSessions(i, str, list, list2, list3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForSyncOldSessions(int i, String str, List<String> list) {
        try {
            OnRequestDoneForSyncOldSessionsImpl(i, str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForSyncOldSessionsImpl(int i, String str, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForSyncOldSessions(i, str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForQuerySessionByFromToNumbers(int i, String str, String str2) {
        try {
            OnRequestDoneForQuerySessionByFromToNumbersImpl(i, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForQuerySessionByFromToNumbersImpl(int i, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForQuerySessionByFromToNumbers(i, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForDeleteSessions(int i, String str, List<String> list) {
        try {
            OnRequestDoneForDeleteSessionsImpl(i, str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForDeleteSessionsImpl(int i, String str, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForDeleteSessions(i, str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnFullSyncedMessages(int i, String str) {
        try {
            OnFullSyncedMessagesImpl(i, str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFullSyncedMessagesImpl(int i, String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnFullSyncedMessages(i, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSyncedNewMessages(int i, String str, String str2, List<String> list, List<String> list2, List<String> list3) {
        try {
            OnSyncedNewMessagesImpl(i, str, str2, list, list2, list3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSyncedNewMessagesImpl(int i, String str, String str2, List<String> list, List<String> list2, List<String> list3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IPBXMessageEventSinkUIListener) all[i2]).OnSyncedNewMessages(i, str, str2, list, list2, list3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForSyncOldMessages(int i, String str, String str2, List<String> list) {
        try {
            OnRequestDoneForSyncOldMessagesImpl(i, str, str2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForSyncOldMessagesImpl(int i, String str, String str2, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForSyncOldMessages(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewSessionCreated(String str, String str2) {
        try {
            OnNewSessionCreatedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewSessionCreatedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnNewSessionCreated(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSessionUpdated(String str) {
        try {
            OnSessionUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSessionUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnSessionUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForSendMessage(int i, String str, String str2, String str3, String str4) {
        try {
            OnRequestDoneForSendMessageImpl(i, str, str2, str3, str4);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForSendMessageImpl(int i, String str, String str2, String str3, String str4) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i2 = 0; i2 < length; i2++) {
                ((IPBXMessageEventSinkUIListener) all[i2]).OnRequestDoneForSendMessage(i, str, str2, str3, str4);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForDeleteMessage(int i, String str, String str2, List<String> list) {
        try {
            OnRequestDoneForDeleteMessageImpl(i, str, str2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForDeleteMessageImpl(int i, String str, String str2, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForDeleteMessage(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForUpdateMessageReadStatus(int i, String str, String str2, List<String> list) {
        try {
            OnRequestDoneForUpdateMessageReadStatusImpl(i, str, str2, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForUpdateMessageReadStatusImpl(int i, String str, String str2, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForUpdateMessageReadStatus(i, str, str2, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForMarkSessionAsRead(int i, String str, String str2) {
        try {
            OnRequestDoneForMarkSessionAsReadImpl(i, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForMarkSessionAsReadImpl(int i, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnRequestDoneForMarkSessionAsRead(i, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewMessageCreated(String str, String str2) {
        try {
            OnNewMessageCreatedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewMessageCreatedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnNewMessageCreated(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMessageUpdated(String str, String str2) {
        try {
            OnMessageUpdatedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMessageUpdatedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnMessageUpdated(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMessagesDeleted(String str, List<String> list) {
        try {
            OnMessagesDeletedImpl(str, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMessagesDeletedImpl(String str, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnMessagesDeleted(str, list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSessionsDeleted(List<String> list) {
        try {
            OnSessionsDeletedImpl(list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSessionsDeletedImpl(List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnSessionsDeleted(list);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSessionUnreadMessageCountUpdated(byte[] bArr) {
        try {
            OnSessionUnreadMessageCountUpdatedImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSessionUnreadMessageCountUpdatedImpl(byte[] bArr) {
        PBXSessionUnreadCountList pBXSessionUnreadCountList;
        try {
            pBXSessionUnreadCountList = PBXSessionUnreadCountList.parseFrom(bArr);
        } catch (Exception unused) {
            pBXSessionUnreadCountList = null;
        }
        if (pBXSessionUnreadCountList != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IPBXMessageEventSinkUIListener) iListener).OnSessionUnreadMessageCountUpdated(pBXSessionUnreadCountList);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnTotalUnreadCountChanged() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnTotalUnreadCountChanged();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewLocalSessionCreated(@NonNull String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnNewLocalSessionCreated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLocalSessionDeleted(@NonNull String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnLocalSessionDeleted(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewLocalSessionMessageCreated(@NonNull String str, @NonNull String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnNewLocalSessionMessageCreated(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLocalSessionMessageDeleted(@NonNull String str, @NonNull String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IPBXMessageEventSinkUIListener) iListener).OnLocalSessionMessageDeleted(str, str2);
            }
        }
    }
}
