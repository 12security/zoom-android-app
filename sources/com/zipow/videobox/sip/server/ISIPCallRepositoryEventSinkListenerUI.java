package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ISIPCallRepositoryEventSinkListenerUI {
    private static final String TAG = "ISIPCallRepositoryEventSinkListenerUI";
    @Nullable
    private static ISIPCallRepositoryEventSinkListenerUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    public interface ISIPCallRepositoryEventSinkListener extends IListener {
        @NonNull
        String FindNameByPhoneNumber(String str);

        void OnAudioFileDownloadFinished(String str, int i, int i2);

        void OnAudioFileDownloadProgress(String str, int i, int i2);

        void OnBlockPhoneNumerDone(int i, List<String> list);

        void OnCallHistoryAllCleared(boolean z);

        void OnCallHistoryDeleted(List<String> list, boolean z);

        void OnCallHistorySyncStarted();

        void OnFullCallHistorySyncFinished(boolean z);

        void OnFullVoiceMailSyncFinished(boolean z);

        void OnMissedCallHistoryChanged(int i);

        void OnMoreCallHistorySyncFinished(List<String> list, List<String> list2, boolean z);

        void OnMoreVoiceMailSyncFinished(List<String> list, List<String> list2, boolean z);

        void OnRequestDoneForVoiceMailTranscript(int i, String str, String str2);

        void OnTotalUnreadVoiceMailCountChanged(int i);

        void OnUpdateVoicemailSharedRelationship();

        void OnVoiceMailDeleted(List<String> list, boolean z);

        void OnVoiceMailStatusChanged(List<String> list, boolean z, boolean z2);

        void OnVoiceMailSyncStarted();
    }

    public static class SimpleISIPCallRepositoryEventSinkListener implements ISIPCallRepositoryEventSinkListener {
        @NonNull
        public String FindNameByPhoneNumber(String str) {
            return "";
        }

        public void OnAudioFileDownloadFinished(String str, int i, int i2) {
        }

        public void OnAudioFileDownloadProgress(String str, int i, int i2) {
        }

        public void OnBlockPhoneNumerDone(int i, List<String> list) {
        }

        public void OnCallHistoryAllCleared(boolean z) {
        }

        public void OnCallHistoryDeleted(List<String> list, boolean z) {
        }

        public void OnCallHistorySyncStarted() {
        }

        public void OnFullCallHistorySyncFinished(boolean z) {
        }

        public void OnFullVoiceMailSyncFinished(boolean z) {
        }

        public void OnMissedCallHistoryChanged(int i) {
        }

        public void OnMoreCallHistorySyncFinished(List<String> list, List<String> list2, boolean z) {
        }

        public void OnMoreVoiceMailSyncFinished(List<String> list, List<String> list2, boolean z) {
        }

        public void OnRequestDoneForVoiceMailTranscript(int i, String str, String str2) {
        }

        public void OnTotalUnreadVoiceMailCountChanged(int i) {
        }

        public void OnUpdateVoicemailSharedRelationship() {
        }

        public void OnVoiceMailDeleted(List<String> list, boolean z) {
        }

        public void OnVoiceMailStatusChanged(List<String> list, boolean z, boolean z2) {
        }

        public void OnVoiceMailSyncStarted() {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized ISIPCallRepositoryEventSinkListenerUI getInstance() {
        ISIPCallRepositoryEventSinkListenerUI iSIPCallRepositoryEventSinkListenerUI;
        synchronized (ISIPCallRepositoryEventSinkListenerUI.class) {
            if (instance == null) {
                instance = new ISIPCallRepositoryEventSinkListenerUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            iSIPCallRepositoryEventSinkListenerUI = instance;
        }
        return iSIPCallRepositoryEventSinkListenerUI;
    }

    private ISIPCallRepositoryEventSinkListenerUI() {
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

    public void addListener(@Nullable ISIPCallRepositoryEventSinkListener iSIPCallRepositoryEventSinkListener) {
        if (iSIPCallRepositoryEventSinkListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iSIPCallRepositoryEventSinkListener) {
                    removeListener((ISIPCallRepositoryEventSinkListener) all[i]);
                }
            }
            this.mListenerList.add(iSIPCallRepositoryEventSinkListener);
        }
    }

    public void removeListener(ISIPCallRepositoryEventSinkListener iSIPCallRepositoryEventSinkListener) {
        this.mListenerList.remove(iSIPCallRepositoryEventSinkListener);
    }

    /* access modifiers changed from: protected */
    public void OnCallHistorySyncStarted() {
        try {
            OnCallHistorySyncStartedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallHistorySyncStartedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnCallHistorySyncStarted();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnFullCallHistorySyncFinished(boolean z) {
        try {
            OnFullCallHistorySyncFinishedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFullCallHistorySyncFinishedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnFullCallHistorySyncFinished(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMoreCallHistorySyncFinished(List<String> list, List<String> list2, boolean z) {
        try {
            OnMoreCallHistorySyncFinishedImpl(list, list2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMoreCallHistorySyncFinishedImpl(@Nullable List<String> list, @Nullable List<String> list2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnMoreCallHistorySyncFinished(list, list2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallHistoryDeleted(List<String> list, boolean z) {
        try {
            OnCallHistoryDeletedImpl(list, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallHistoryDeletedImpl(List<String> list, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnCallHistoryDeleted(list, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMissedCallHistoryChanged(int i) {
        try {
            OnMissedCallHistoryChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMissedCallHistoryChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnMissedCallHistoryChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnCallHistoryAllCleared(boolean z) {
        try {
            OnCallHistoryAllClearedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnCallHistoryAllClearedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnCallHistoryAllCleared(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnVoiceMailSyncStarted() {
        try {
            OnVoiceMailSyncStartedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnVoiceMailSyncStartedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnVoiceMailSyncStarted();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnFullVoiceMailSyncFinished(boolean z) {
        try {
            OnFullVoiceMailSyncFinishedImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFullVoiceMailSyncFinishedImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnFullVoiceMailSyncFinished(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMoreVoiceMailSyncFinished(List<String> list, List<String> list2, boolean z) {
        try {
            OnMoreVoiceMailSyncFinishedImpl(list, list2, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMoreVoiceMailSyncFinishedImpl(@Nullable List<String> list, @Nullable List<String> list2, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnMoreVoiceMailSyncFinished(list, list2, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnVoiceMailDeleted(List<String> list, boolean z) {
        try {
            OnVoiceMailDeletedImpl(list, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnVoiceMailDeletedImpl(List<String> list, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnVoiceMailDeleted(list, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnVoiceMailStatusChanged(List<String> list, boolean z, boolean z2) {
        try {
            OnVoiceMailStatusChangedImpl(list, z, z2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnVoiceMailStatusChangedImpl(List<String> list, boolean z, boolean z2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnVoiceMailStatusChanged(list, z, z2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRequestDoneForVoiceMailTranscript(int i, String str, String str2) {
        try {
            OnRequestDoneForVoiceMailTranscriptImpl(i, str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRequestDoneForVoiceMailTranscriptImpl(int i, String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnRequestDoneForVoiceMailTranscript(i, str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnTotalUnreadVoiceMailCountChanged(int i) {
        try {
            OnTotalUnreadVoiceMailCountChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnTotalUnreadVoiceMailCountChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnTotalUnreadVoiceMailCountChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnAudioFileDownloadFinished(String str, int i, int i2) {
        try {
            OnAudioFileDownloadFinishedImpl(str, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnAudioFileDownloadFinishedImpl(String str, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnAudioFileDownloadFinished(str, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnAudioFileDownloadProgress(String str, int i, int i2) {
        try {
            OnAudioFileDownloadProgressImpl(str, i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnAudioFileDownloadProgressImpl(String str, int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnAudioFileDownloadProgress(str, i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public String FindNameByPhoneNumber(@NonNull String str) {
        try {
            return FindNameByPhoneNumberImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            return "";
        }
    }

    @NonNull
    private String FindNameByPhoneNumberImpl(@Nullable String str) {
        return ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(str);
    }

    /* access modifiers changed from: protected */
    public void OnUpdateVoicemailSharedRelationship() {
        try {
            OnUpdateVoicemailSharedRelationshipImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUpdateVoicemailSharedRelationshipImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnUpdateVoicemailSharedRelationship();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnBlockPhoneNumerDone(int i, List<String> list) {
        try {
            OnBlockPhoneNumerDoneImpl(i, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    /* access modifiers changed from: protected */
    public void OnBlockPhoneNumerDoneImpl(int i, List<String> list) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPCallRepositoryEventSinkListener) iListener).OnBlockPhoneNumerDone(i, list);
            }
        }
    }
}
