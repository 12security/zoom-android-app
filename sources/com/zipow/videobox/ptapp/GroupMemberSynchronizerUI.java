package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class GroupMemberSynchronizerUI {
    private static final String TAG = "GroupMemberSynchronizerUI";
    @Nullable
    private static GroupMemberSynchronizerUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IGroupMemberSynchronizerUIListener extends IListener {
        void Notify_AsyncMUCGroupInfoUpdated(String str);
    }

    public static abstract class SimpleGroupMemberSynchronizerUIListener implements IGroupMemberSynchronizerUIListener {
        public void Notify_AsyncMUCGroupInfoUpdated(String str) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable IGroupMemberSynchronizerUIListener iGroupMemberSynchronizerUIListener) {
        if (iGroupMemberSynchronizerUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iGroupMemberSynchronizerUIListener) {
                    removeListener((IGroupMemberSynchronizerUIListener) all[i]);
                }
            }
            this.mListenerList.add(iGroupMemberSynchronizerUIListener);
        }
    }

    public void removeListener(IGroupMemberSynchronizerUIListener iGroupMemberSynchronizerUIListener) {
        this.mListenerList.remove(iGroupMemberSynchronizerUIListener);
    }

    @Nullable
    public static synchronized GroupMemberSynchronizerUI getInstance() {
        GroupMemberSynchronizerUI groupMemberSynchronizerUI;
        synchronized (GroupMemberSynchronizerUI.class) {
            if (instance == null) {
                instance = new GroupMemberSynchronizerUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            groupMemberSynchronizerUI = instance;
        }
        return groupMemberSynchronizerUI;
    }

    private GroupMemberSynchronizerUI() {
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
    public void notifyAsyncMUCGroupInfoUpdated(String str) {
        try {
            notifyAsyncMUCGroupInfoUpdatedImpl(str);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyAsyncMUCGroupInfoUpdatedImpl(String str) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IGroupMemberSynchronizerUIListener) iListener).Notify_AsyncMUCGroupInfoUpdated(str);
            }
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }
}
