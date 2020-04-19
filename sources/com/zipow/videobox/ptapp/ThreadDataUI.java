package com.zipow.videobox.ptapp;

import com.zipow.videobox.ptapp.IMProtos.CommentDataResult;
import com.zipow.videobox.ptapp.IMProtos.ThreadDataResult;
import com.zipow.videobox.util.MMMessageHelper.MessageSyncer;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ThreadDataUI {
    private static final String TAG = "ThreadDataUI";
    private static ThreadDataUI instance;
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IThreadDataUIListener extends IListener {
        void OnEmojiCountInfoLoadedFromDB(String str);

        void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z);

        void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z);

        void OnGetCommentData(CommentDataResult commentDataResult);

        void OnGetThreadData(ThreadDataResult threadDataResult);

        void OnMSGDBExistence(String str, String str2, String str3, boolean z);

        void OnMessageEmojiInfoUpdated(String str, String str2);

        void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z);

        void OnThreadContextSynced(String str, String str2, String str3);

        void OnThreadContextUpdate(String str, String str2);
    }

    public static abstract class SimpleThreadDataUIListener implements IThreadDataUIListener {
        public void OnEmojiCountInfoLoadedFromDB(String str) {
        }

        public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        }

        public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
        }

        public void OnGetCommentData(CommentDataResult commentDataResult) {
        }

        public void OnGetThreadData(ThreadDataResult threadDataResult) {
        }

        public void OnMSGDBExistence(String str, String str2, String str3, boolean z) {
        }

        public void OnMessageEmojiInfoUpdated(String str, String str2) {
        }

        public void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z) {
        }

        public void OnThreadContextSynced(String str, String str2, String str3) {
        }

        public void OnThreadContextUpdate(String str, String str2) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    /* access modifiers changed from: protected */
    public void OnGetThreadData(byte[] bArr) {
        try {
            OnGetThreadDataImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnGetThreadDataImpl(byte[] bArr) {
        ThreadDataResult threadDataResult;
        try {
            threadDataResult = ThreadDataResult.parseFrom(bArr);
        } catch (Exception unused) {
            threadDataResult = null;
        }
        if (threadDataResult != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IThreadDataUIListener) iListener).OnGetThreadData(threadDataResult);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnGetCommentData(byte[] bArr) {
        try {
            OnGetCommentDataImpl(bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnGetCommentDataImpl(byte[] bArr) {
        CommentDataResult commentDataResult;
        try {
            commentDataResult = CommentDataResult.parseFrom(bArr);
        } catch (Exception unused) {
            commentDataResult = null;
        }
        if (commentDataResult != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IThreadDataUIListener) iListener).OnGetCommentData(commentDataResult);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnThreadContextUpdate(String str, String str2) {
        try {
            OnThreadContextUpdateImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnThreadContextUpdateImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnThreadContextUpdate(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnThreadContextSynced(String str, String str2, String str3) {
        try {
            OnThreadContextSyncedImpl(str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnThreadContextSyncedImpl(String str, String str2, String str3) {
        MessageSyncer.getInstance().OnThreadContextSynced(str, str2, str3);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnThreadContextSynced(str, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnFetchEmojiDetailInfo(String str, String str2, String str3, String str4, boolean z) {
        try {
            OnFetchEmojiDetailInfoImpl(str, str2, str3, str4, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFetchEmojiDetailInfoImpl(String str, String str2, String str3, String str4, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i = 0; i < length; i++) {
                ((IThreadDataUIListener) all[i]).OnFetchEmojiDetailInfo(str, str2, str3, str4, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnFetchEmojiCountInfo(String str, String str2, List<String> list, boolean z) {
        try {
            OnFetchEmojiCountInfoImpl(str, str2, list, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnFetchEmojiCountInfoImpl(String str, String str2, List<String> list, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnFetchEmojiCountInfo(str, str2, list, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMessageEmojiInfoUpdated(String str, String str2) {
        try {
            OnMessageEmojiInfoUpdatedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMessageEmojiInfoUpdatedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnMessageEmojiInfoUpdated(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnEmojiCountInfoLoadedFromDB(String str) {
        try {
            OnEmojiCountInfoLoadedFromDBImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnEmojiCountInfoLoadedFromDBImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnEmojiCountInfoLoadedFromDB(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnMSGDBExistence(String str, String str2, String str3, boolean z) {
        try {
            OnMSGDBExistenceImpl(str, str2, str3, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMSGDBExistenceImpl(String str, String str2, String str3, boolean z) {
        MessageSyncer.getInstance().OnMSGDBExistence(str, str2, str3, z);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnMSGDBExistence(str, str2, str3, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSyncThreadCommentCount(String str, String str2, List<String> list, boolean z) {
        try {
            OnSyncThreadCommentCountImpl(str, str2, list, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSyncThreadCommentCountImpl(String str, String str2, List<String> list, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IThreadDataUIListener) iListener).OnSyncThreadCommentCount(str, str2, list, z);
            }
        }
    }

    public void addListener(IThreadDataUIListener iThreadDataUIListener) {
        if (iThreadDataUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iThreadDataUIListener) {
                    removeListener((IThreadDataUIListener) all[i]);
                }
            }
            this.mListenerList.add(iThreadDataUIListener);
        }
    }

    public void removeListener(IThreadDataUIListener iThreadDataUIListener) {
        this.mListenerList.remove(iThreadDataUIListener);
    }

    public static synchronized ThreadDataUI getInstance() {
        ThreadDataUI threadDataUI;
        synchronized (ThreadDataUI.class) {
            if (instance == null) {
                instance = new ThreadDataUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            threadDataUI = instance;
        }
        return threadDataUI;
    }

    private ThreadDataUI() {
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
}
