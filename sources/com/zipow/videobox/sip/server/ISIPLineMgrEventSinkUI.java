package com.zipow.videobox.sip.server;

import com.zipow.videobox.sip.CmmSIPCallRegResult;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class ISIPLineMgrEventSinkUI {
    private static final String TAG = "ISIPLineMgrEventSinkUI";
    private static ISIPLineMgrEventSinkUI instance;
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface ISIPLineMgrEventSinkListener extends IListener {
        void OnCurrentSelectedLineChanged(String str);

        void OnLineCallItemCreated(String str, int i);

        void OnLineCallItemMerged(String str, String str2);

        void OnLineCallItemTerminate(String str);

        void OnLineCallItemUpdate(String str, int i);

        void OnMySelfInfoUpdated(boolean z, int i);

        void OnNewSharedLineAdded(String str);

        void OnNewSharedUserAdded(String str);

        void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult);

        void OnSharedLineDeleted(String str);

        void OnSharedLineUpdated(String str, boolean z, int i);

        void OnSharedUserDeleted(String str);

        void OnSharedUserUpdated(String str);
    }

    public static class SimpleISIPLineMgrEventSinkListener implements ISIPLineMgrEventSinkListener {
        public void OnCurrentSelectedLineChanged(String str) {
        }

        public void OnLineCallItemCreated(String str, int i) {
        }

        public void OnLineCallItemMerged(String str, String str2) {
        }

        public void OnLineCallItemTerminate(String str) {
        }

        public void OnLineCallItemUpdate(String str, int i) {
        }

        public void OnMySelfInfoUpdated(boolean z, int i) {
        }

        public void OnNewSharedLineAdded(String str) {
        }

        public void OnNewSharedUserAdded(String str) {
        }

        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
        }

        public void OnSharedLineDeleted(String str) {
        }

        public void OnSharedLineUpdated(String str, boolean z, int i) {
        }

        public void OnSharedUserDeleted(String str) {
        }

        public void OnSharedUserUpdated(String str) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public static synchronized ISIPLineMgrEventSinkUI getInstance() {
        ISIPLineMgrEventSinkUI iSIPLineMgrEventSinkUI;
        synchronized (ISIPLineMgrEventSinkUI.class) {
            if (instance == null) {
                instance = new ISIPLineMgrEventSinkUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            iSIPLineMgrEventSinkUI = instance;
        }
        return iSIPLineMgrEventSinkUI;
    }

    private ISIPLineMgrEventSinkUI() {
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

    public void addListener(ISIPLineMgrEventSinkListener iSIPLineMgrEventSinkListener) {
        if (iSIPLineMgrEventSinkListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iSIPLineMgrEventSinkListener) {
                    removeListener((ISIPLineMgrEventSinkListener) all[i]);
                }
            }
            this.mListenerList.add(iSIPLineMgrEventSinkListener);
        }
    }

    public void removeListener(ISIPLineMgrEventSinkListener iSIPLineMgrEventSinkListener) {
        this.mListenerList.remove(iSIPLineMgrEventSinkListener);
    }

    /* access modifiers changed from: protected */
    public void OnMySelfInfoUpdated(boolean z, int i) {
        try {
            OnMySelfInfoUpdatedImpl(z, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnMySelfInfoUpdatedImpl(boolean z, int i) {
        CmmSIPLineManager.getInstance().updateSelfInfo();
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnMySelfInfoUpdated(z, i);
            }
        }
    }

    public void OnCurrentSelectedLineChanged(String str) {
        try {
            OnCurrentSelectedLineChangedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    public void OnCurrentSelectedLineChangedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnCurrentSelectedLineChanged(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewSharedUserAdded(String str) {
        try {
            OnNewSharedUserAddedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewSharedUserAddedImpl(String str) {
        CmmSIPLineManager.getInstance().addSharedUser(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnNewSharedUserAdded(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSharedUserDeleted(String str) {
        try {
            OnSharedUserDeletedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSharedUserDeletedImpl(String str) {
        CmmSIPLineManager.getInstance().removeSharedUser(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnSharedUserDeleted(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSharedUserUpdated(String str) {
        try {
            OnSharedUserUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSharedUserUpdatedImpl(String str) {
        CmmSIPLineManager.getInstance().addSharedUser(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnSharedUserUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnRegisterResult(String str, int i, int i2, String str2, String str3) {
        try {
            OnRegisterResultImpl(str, new CmmSIPCallRegResult(i, i2, str2, str3));
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnRegisterResultImpl(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnRegisterResult(str, cmmSIPCallRegResult);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnNewSharedLineAdded(String str) {
        try {
            OnNewSharedLineAddedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnNewSharedLineAddedImpl(String str) {
        CmmSIPLineManager.getInstance().addSharedLine(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnNewSharedLineAdded(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSharedLineDeleted(String str) {
        try {
            OnSharedLineDeletedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSharedLineDeletedImpl(String str) {
        CmmSIPLineManager.getInstance().removeSharedLine(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnSharedLineDeleted(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnSharedLineUpdated(String str, boolean z, int i) {
        try {
            OnSharedLineUpdatedImpl(str, z, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnSharedLineUpdatedImpl(String str, boolean z, int i) {
        CmmSIPLineManager.getInstance().addSharedLine(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnSharedLineUpdated(str, z, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLineCallItemCreated(String str, int i) {
        try {
            OnLineCallItemCreatedImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnLineCallItemCreatedImpl(String str, int i) {
        CmmSIPLineManager.getInstance().addLineCallItem(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnLineCallItemCreated(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLineCallItemTerminate(String str) {
        try {
            OnLineCallItemTerminateImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnLineCallItemTerminateImpl(String str) {
        CmmSIPLineManager.getInstance().removeLineCallItem(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnLineCallItemTerminate(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLineCallItemUpdate(String str, int i) {
        try {
            OnLineCallItemUpdateImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnLineCallItemUpdateImpl(String str, int i) {
        CmmSIPLineManager.getInstance().addLineCallItem(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnLineCallItemUpdate(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnLineCallItemMerged(String str, String str2) {
        try {
            OnLineCallItemMergedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnLineCallItemMergedImpl(String str, String str2) {
        CmmSIPLineManager.getInstance().addLineCallItem(str);
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((ISIPLineMgrEventSinkListener) iListener).OnLineCallItemMerged(str, str2);
            }
        }
    }
}
