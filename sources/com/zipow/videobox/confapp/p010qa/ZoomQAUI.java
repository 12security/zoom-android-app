package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQAUI */
public class ZoomQAUI {
    private static final String TAG = "ZoomQAUI";
    @Nullable
    private static ZoomQAUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle = 0;

    /* renamed from: com.zipow.videobox.confapp.qa.ZoomQAUI$IZoomQAUIListener */
    public interface IZoomQAUIListener extends IListener {
        void notifyConnectResult(boolean z);

        void notifyConnectStart();

        void onAddAnswer(String str, boolean z);

        void onAddQuestion(String str, boolean z);

        void onAnswerSenderNameChanged(String str, String str2);

        void onChattedAttendeeUpdated(long j);

        void onQuestionMarkedAsAnswered(String str);

        void onQuestionMarkedAsDismissed(String str);

        void onReceiveAnswer(String str);

        void onReceiveQuestion(String str);

        void onRefreshQAUI();

        void onReopenQuestion(String str);

        void onRevokeUpvoteQuestion(String str, boolean z);

        void onUpvoteQuestion(String str, boolean z);

        void onUserAdded(String str);

        void onUserComposing(String str);

        void onUserEndComposing(String str);

        void onUserEndLiving(String str);

        void onUserListInitialized();

        void onUserListUpdated();

        void onUserLivingReply(String str);

        void onUserRemoved(String str);

        void onWebinarAttendeeGuestStatusChanged(long j, boolean z);

        void onWebinarAttendeeLowerHand(long j);

        void onWebinarAttendeeRaisedHand(long j);
    }

    /* renamed from: com.zipow.videobox.confapp.qa.ZoomQAUI$SimpleZoomQAUIListener */
    public static abstract class SimpleZoomQAUIListener implements IZoomQAUIListener {
        public void notifyConnectResult(boolean z) {
        }

        public void notifyConnectStart() {
        }

        public void onAddAnswer(String str, boolean z) {
        }

        public void onAddQuestion(String str, boolean z) {
        }

        public void onAnswerSenderNameChanged(String str, String str2) {
        }

        public void onChattedAttendeeUpdated(long j) {
        }

        public void onQuestionMarkedAsAnswered(String str) {
        }

        public void onQuestionMarkedAsDismissed(String str) {
        }

        public void onReceiveAnswer(String str) {
        }

        public void onReceiveQuestion(String str) {
        }

        public void onRefreshQAUI() {
        }

        public void onReopenQuestion(String str) {
        }

        public void onRevokeUpvoteQuestion(String str, boolean z) {
        }

        public void onUpvoteQuestion(String str, boolean z) {
        }

        public void onUserAdded(String str) {
        }

        public void onUserComposing(String str) {
        }

        public void onUserEndComposing(String str) {
        }

        public void onUserEndLiving(String str) {
        }

        public void onUserListInitialized() {
        }

        public void onUserListUpdated() {
        }

        public void onUserLivingReply(String str) {
        }

        public void onUserRemoved(String str) {
        }

        public void onWebinarAttendeeGuestStatusChanged(long j, boolean z) {
        }

        public void onWebinarAttendeeLowerHand(long j) {
        }

        public void onWebinarAttendeeRaisedHand(long j) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    @NonNull
    public static synchronized ZoomQAUI getInstance() {
        ZoomQAUI zoomQAUI;
        synchronized (ZoomQAUI.class) {
            if (instance == null) {
                instance = new ZoomQAUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            zoomQAUI = instance;
        }
        return zoomQAUI;
    }

    public ZoomQAUI() {
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

    public void addListener(@Nullable IZoomQAUIListener iZoomQAUIListener) {
        if (iZoomQAUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomQAUIListener) {
                    removeListener((IZoomQAUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomQAUIListener);
        }
    }

    public void removeListener(IZoomQAUIListener iZoomQAUIListener) {
        this.mListenerList.remove(iZoomQAUIListener);
    }

    /* access modifiers changed from: protected */
    public void notifyConnectStart() {
        try {
            notifyConnectStartImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyConnectStartImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).notifyConnectStart();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyConnectResult(boolean z) {
        try {
            notifyConnectResultImpl(z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyConnectResultImpl(boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).notifyConnectResult(z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRefreshQAUI() {
        try {
            onRefreshQAUIImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onRefreshQAUIImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onRefreshQAUI();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAddQuestion(String str, boolean z) {
        try {
            onAddQuestionImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onAddQuestionImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onAddQuestion(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAddAnswer(String str, boolean z) {
        try {
            onAddAnswerImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onAddAnswerImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onAddAnswer(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onReceiveQuestion(String str) {
        try {
            onReceiveQuestionImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onReceiveQuestionImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onReceiveQuestion(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onReceiveAnswer(String str) {
        try {
            onReceiveAnswerImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onReceiveAnswerImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onReceiveAnswer(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onQuestionMarkedAsAnswered(String str) {
        try {
            onQuestionMarkedAsAnsweredImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onQuestionMarkedAsAnsweredImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onQuestionMarkedAsAnswered(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserComposing(String str) {
        try {
            onUserComposingImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserComposingImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserComposing(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserEndComposing(String str) {
        try {
            onUserEndComposingImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserEndComposingImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserEndComposing(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserLivingReply(String str) {
        try {
            onUserLivingReplyImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserLivingReplyImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserLivingReply(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserEndLiving(String str) {
        try {
            onUserEndLivingImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserEndLivingImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserEndLiving(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserListUpdated() {
        try {
            onUserListUpdatedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserListUpdatedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserListUpdated();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserListInitialized() {
        try {
            onUserListInitializedImpl();
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserListInitializedImpl() {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserListInitialized();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserAdded(String str) {
        try {
            onUserAddedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserAddedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserAdded(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUserRemoved(String str) {
        try {
            onUserRemovedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUserRemovedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUserRemoved(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWebinarAttendeeRaisedHand(long j) {
        try {
            onWebinarAttendeeRaisedHandImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onWebinarAttendeeRaisedHandImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onWebinarAttendeeRaisedHand(j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWebinarAttendeeLowerHand(long j) {
        try {
            onWebinarAttendeeLowerHandImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onWebinarAttendeeLowerHandImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onWebinarAttendeeLowerHand(j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onChattedAttendeeUpdated(long j) {
        try {
            onChattedAttendeeUpdatedImpl(j);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onChattedAttendeeUpdatedImpl(long j) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onChattedAttendeeUpdated(j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onWebinarAttendeeGuestStatusChanged(long j, boolean z) {
        try {
            onWebinarAttendeeGuestStatusChangedImpl(j, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onWebinarAttendeeGuestStatusChangedImpl(long j, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onWebinarAttendeeGuestStatusChanged(j, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onQuestionMarkedAsDismissed(String str) {
        try {
            onQuestionMarkedAsDismissedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onQuestionMarkedAsDismissedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onQuestionMarkedAsDismissed(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAnswerSenderNameChanged(String str, String str2) {
        try {
            onAnswerSenderNameChangedImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onAnswerSenderNameChangedImpl(String str, String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onAnswerSenderNameChanged(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onReopenQuestion(String str) {
        try {
            onReopenQuestionImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onReopenQuestionImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onReopenQuestion(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpvoteQuestion(String str, boolean z) {
        try {
            onUpvoteQuestionImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onUpvoteQuestionImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onUpvoteQuestion(str, z);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRevokeUpvoteQuestion(String str, boolean z) {
        try {
            onRevokeUpvoteQuestionImpl(str, z);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void onRevokeUpvoteQuestionImpl(String str, boolean z) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomQAUIListener) iListener).onRevokeUpvoteQuestion(str, z);
            }
        }
    }
}
