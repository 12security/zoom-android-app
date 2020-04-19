package com.zipow.videobox.confapp.p009bo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

/* renamed from: com.zipow.videobox.confapp.bo.BOUI */
public class BOUI {
    private static final String TAG = "BOUI";
    @Nullable
    private static BOUI instance;
    @Nullable
    private BOMgr mBoMgr = null;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    /* renamed from: com.zipow.videobox.confapp.bo.BOUI$IBOUIListener */
    public interface IBOUIListener extends IListener {
        void OnBONewBroadcastMessageReceived(String str, long j);

        void onBOControlStatusChanged(int i);

        void onBORunTimeElapsed(int i, int i2);

        void onBOStartRequestReceived(BOObject bOObject);

        void onBOStopRequestReceived(int i);

        void onBOStoppingTick(int i);

        void onBOSwitchRequestReceived(BOObject bOObject);

        void onBOTokenReady();

        void onBOUserUpdated(BOObject bOObject, List<BOUpdatedUser> list);

        void onConfigDataChanged(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2);

        void onHelpRequestHandleResultReceived(int i);

        void onHelpRequestReceived(String str);

        void onMasterConfHostChanged(String str, boolean z);

        void onMasterConfUserListUpdated(List<String> list, List<String> list2, List<String> list3);
    }

    /* renamed from: com.zipow.videobox.confapp.bo.BOUI$SimpleBOUIListener */
    public static abstract class SimpleBOUIListener implements IBOUIListener {
        public void OnBONewBroadcastMessageReceived(String str, long j) {
        }

        public void onBOControlStatusChanged(int i) {
        }

        public void onBORunTimeElapsed(int i, int i2) {
        }

        public void onBOStartRequestReceived(BOObject bOObject) {
        }

        public void onBOStopRequestReceived(int i) {
        }

        public void onBOStoppingTick(int i) {
        }

        public void onBOSwitchRequestReceived(BOObject bOObject) {
        }

        public void onBOTokenReady() {
        }

        public void onBOUserUpdated(BOObject bOObject, List<BOUpdatedUser> list) {
        }

        public void onConfigDataChanged(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2) {
        }

        public void onHelpRequestHandleResultReceived(int i) {
        }

        public void onHelpRequestReceived(String str) {
        }

        public void onMasterConfHostChanged(String str, boolean z) {
        }

        public void onMasterConfUserListUpdated(List<String> list, List<String> list2, List<String> list3) {
        }
    }

    private native long nativeInitImpl(long j);

    private native void nativeUninitImpl(long j, long j2);

    private BOUI() {
        init();
    }

    private boolean initialized() {
        return this.mNativeHandle != 0;
    }

    private void init() {
        this.mBoMgr = ConfMgr.getInstance().getBOMgr();
        BOMgr bOMgr = this.mBoMgr;
        if (bOMgr != null) {
            try {
                this.mNativeHandle = nativeInitImpl(bOMgr.getNativeHandle());
            } catch (Throwable unused) {
            }
        }
    }

    @NonNull
    public static BOUI getInstance() {
        if (instance == null) {
            instance = new BOUI();
        }
        if (!instance.initialized()) {
            instance.init();
        }
        return instance;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (this.mNativeHandle != 0) {
            BOMgr bOMgr = this.mBoMgr;
            if (bOMgr != null) {
                nativeUninitImpl(bOMgr.getNativeHandle(), this.mNativeHandle);
            }
        }
        super.finalize();
    }

    public void addListener(@Nullable IBOUIListener iBOUIListener) {
        if (iBOUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iBOUIListener) {
                    removeListener((IBOUIListener) all[i]);
                }
            }
            this.mListenerList.add(iBOUIListener);
        }
    }

    public void removeListener(IBOUIListener iBOUIListener) {
        this.mListenerList.remove(iBOUIListener);
    }

    /* access modifiers changed from: protected */
    public void onBOUserUpdated(long j, List<BOUpdatedUser> list) {
        try {
            onBOUserUpdatedImpl(j, list);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOUserUpdatedImpl(long j, List<BOUpdatedUser> list) {
        if (j != 0) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                BOObject bOObject = new BOObject(j);
                for (IListener iListener : all) {
                    ((IBOUIListener) iListener).onBOUserUpdated(bOObject, list);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMasterConfUserListUpdated(List<String> list, List<String> list2, List<String> list3) {
        try {
            onMasterConfUserListUpdatedImpl(list, list2, list3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onMasterConfUserListUpdatedImpl(List<String> list, List<String> list2, List<String> list3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onMasterConfUserListUpdated(list, list2, list3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMasterConfHostChanged(String str, boolean z) {
        try {
            onMasterConfHostChangedImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onMasterConfHostChangedImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onMasterConfHostChanged(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOControlStatusChanged(int i) {
        try {
            onBOControlStatusChangedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOControlStatusChangedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onBOControlStatusChanged(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOTokenReady() {
        try {
            onBOTokenReadyImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOTokenReadyImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onBOTokenReady();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOStoppingTick(int i) {
        try {
            onBOStoppingTickImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOStoppingTickImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onBOStoppingTick(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onHelpRequestReceived(String str) {
        try {
            onHelpRequestReceivedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onHelpRequestReceivedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onHelpRequestReceived(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOStartRequestReceived(long j) {
        try {
            onBOStartRequestReceivedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOStartRequestReceivedImpl(long j) {
        if (!ConfUI.getInstance().inSilentMode() && j != 0) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                BOObject bOObject = new BOObject(j);
                for (IListener iListener : all) {
                    ((IBOUIListener) iListener).onBOStartRequestReceived(bOObject);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOStopRequestReceived(int i) {
        try {
            onBOStopRequestReceivedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOStopRequestReceivedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onBOStopRequestReceived(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onHelpRequestHandleResultReceived(int i) {
        try {
            onHelpRequestHandleResultReceivedImpl(i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onHelpRequestHandleResultReceivedImpl(int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onHelpRequestHandleResultReceived(i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnBONewBroadcastMessageReceived(String str, long j) {
        try {
            onBONewBroadcastMessageReceivedImpl(str, j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBONewBroadcastMessageReceivedImpl(String str, long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).OnBONewBroadcastMessageReceived(str, j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBOSwitchRequestReceived(long j) {
        try {
            onBOSwitchRequestReceivedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBOSwitchRequestReceivedImpl(long j) {
        if (j != 0) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                BOObject bOObject = new BOObject(j);
                for (IListener iListener : all) {
                    ((IBOUIListener) iListener).onBOSwitchRequestReceived(bOObject);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onBORunTimeElapsed(int i, int i2) {
        try {
            onBORunTimeElapsedImpl(i, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onBORunTimeElapsedImpl(int i, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IBOUIListener) iListener).onBORunTimeElapsed(i, i2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnConfigDataChanged(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2) {
        try {
            OnConfigDataChangedImpl(z, z2, i, z3, z4, i2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnConfigDataChangedImpl(boolean z, boolean z2, int i, boolean z3, boolean z4, int i2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            int length = all.length;
            for (int i3 = 0; i3 < length; i3++) {
                ((IBOUIListener) all[i3]).onConfigDataChanged(z, z2, i, z3, z4, i2);
            }
        }
    }
}
