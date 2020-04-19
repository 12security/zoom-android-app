package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.ContactSearchResponse;
import com.zipow.videobox.ptapp.IMProtos.FileFilterSearchResults;
import com.zipow.videobox.ptapp.IMProtos.LocalSearchMsgCtx;
import com.zipow.videobox.ptapp.IMProtos.MessageContentSearchResponse;
import com.zipow.videobox.ptapp.p013mm.GroupMemberSynchronizer;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.UnSupportMessageMgr;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class IMCallbackUI {
    private static final String TAG = "IMCallbackUI";
    @Nullable
    private static IMCallbackUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IIMCallbackUIListener extends IListener {
        void Indicate_LocalSearchContactResponse(String str, List<String> list);

        void Indicate_LocalSearchFileResponse(String str, FileFilterSearchResults fileFilterSearchResults);

        void Indicate_LocalSearchMSGResponse(String str, MessageContentSearchResponse messageContentSearchResponse);

        void Indicate_QueryLocalMsgCtxResponse(String str, List<String> list);

        void Indicate_SearchFileResponse(String str, int i, FileFilterSearchResults fileFilterSearchResults);

        void Indicate_SearchMessageResponse(String str, int i, MessageContentSearchResponse messageContentSearchResponse);

        void Notify_AsyncMUCGroupInfoUpdated(String str);

        void OnUnsupportMessageRecevied(int i, String str, String str2, String str3);
    }

    public static abstract class SimpleIMCallbackUIListener implements IIMCallbackUIListener {
        public void Indicate_LocalSearchContactResponse(String str, List<String> list) {
        }

        public void Indicate_LocalSearchFileResponse(String str, FileFilterSearchResults fileFilterSearchResults) {
        }

        public void Indicate_LocalSearchMSGResponse(String str, MessageContentSearchResponse messageContentSearchResponse) {
        }

        public void Indicate_QueryLocalMsgCtxResponse(String str, List<String> list) {
        }

        public void Indicate_SearchFileResponse(String str, int i, FileFilterSearchResults fileFilterSearchResults) {
        }

        public void Indicate_SearchMessageResponse(String str, int i, MessageContentSearchResponse messageContentSearchResponse) {
        }

        public void Notify_AsyncMUCGroupInfoUpdated(String str) {
        }

        public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
        }
    }

    private native long getAsynReadGroupMemberHandleImpl(long j);

    private native long getSearchMgrUICallBackHandleImpl(long j);

    private native long getUnSupportHandleImpl(long j);

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable IIMCallbackUIListener iIMCallbackUIListener) {
        if (iIMCallbackUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iIMCallbackUIListener) {
                    removeListener((IIMCallbackUIListener) all[i]);
                }
            }
            this.mListenerList.add(iIMCallbackUIListener);
        }
    }

    public void removeListener(IIMCallbackUIListener iIMCallbackUIListener) {
        this.mListenerList.remove(iIMCallbackUIListener);
    }

    @NonNull
    public static synchronized IMCallbackUI getInstance() {
        IMCallbackUI iMCallbackUI;
        synchronized (IMCallbackUI.class) {
            if (instance == null) {
                instance = new IMCallbackUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            iMCallbackUI = instance;
        }
        return iMCallbackUI;
    }

    private IMCallbackUI() {
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

    public void registerCallback() {
        registerGroupMemberCallback();
        registerUnSupportMessageMgrCallback();
        registerSearchMgrCallback();
    }

    private void registerGroupMemberCallback() {
        GroupMemberSynchronizer groupMemberSynchronizer = PTApp.getInstance().getGroupMemberSynchronizer();
        if (groupMemberSynchronizer != null) {
            groupMemberSynchronizer.registerUICallback(getInstance());
        }
    }

    private void registerUnSupportMessageMgrCallback() {
        UnSupportMessageMgr unsupportMessageMgr = PTApp.getInstance().getUnsupportMessageMgr();
        if (unsupportMessageMgr != null) {
            unsupportMessageMgr.setMsgUI(getInstance());
        }
    }

    private void registerSearchMgrCallback() {
        SearchMgr searchMgr = PTApp.getInstance().getSearchMgr();
        if (searchMgr != null) {
            searchMgr.setMsgUI(getInstance());
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
                ((IIMCallbackUIListener) iListener).Notify_AsyncMUCGroupInfoUpdated(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void OnUnsupportMessageRecevied(int i, String str, String str2, String str3) {
        try {
            OnUnsupportMessageReceviedImpl(i, str, str2, str3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void OnUnsupportMessageReceviedImpl(int i, String str, String str2, String str3) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IIMCallbackUIListener) iListener).OnUnsupportMessageRecevied(i, str, str2, str3);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_SearchMessageResponse(String str, int i, @NonNull byte[] bArr) {
        try {
            Indicate_SearchMessageResponseImpl(str, i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_SearchMessageResponseImpl(String str, int i, @NonNull byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        try {
            MessageContentSearchResponse parseFrom = MessageContentSearchResponse.parseFrom(bArr);
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_SearchMessageResponse(str, i, parseFrom);
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_SearchFileResponse(String str, int i, @NonNull byte[] bArr) {
        try {
            Indicate_SearchFileResponseImpl(str, i, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_SearchFileResponseImpl(String str, int i, @NonNull byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        try {
            FileFilterSearchResults parseFrom = FileFilterSearchResults.parseFrom(bArr);
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_SearchFileResponse(str, i, parseFrom);
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_LocalSearchFileResponse(String str, @NonNull byte[] bArr) {
        try {
            Indicate_LocalSearchFileResponseImpl(str, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_LocalSearchFileResponseImpl(String str, @NonNull byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        try {
            FileFilterSearchResults parseFrom = FileFilterSearchResults.parseFrom(bArr);
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_LocalSearchFileResponse(str, parseFrom);
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_QueryLocalMsgCtxResponse(String str, @NonNull byte[] bArr) {
        try {
            Indicate_QueryLocalMsgCtxResponsImpl(str, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_QueryLocalMsgCtxResponsImpl(String str, @NonNull byte[] bArr) {
        IListener[] all = this.mListenerList.getAll();
        try {
            LocalSearchMsgCtx parseFrom = LocalSearchMsgCtx.parseFrom(bArr);
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_QueryLocalMsgCtxResponse(str, parseFrom.getSessionidList());
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_LocalSearchMSGResponse(String str, @NonNull byte[] bArr) {
        try {
            Indicate_LocalSearchMSGResponseImpl(str, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_LocalSearchMSGResponseImpl(String str, @NonNull byte[] bArr) {
        try {
            MessageContentSearchResponse parseFrom = MessageContentSearchResponse.parseFrom(bArr);
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_LocalSearchMSGResponse(str, parseFrom);
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void Indicate_LocalSearchContactResponse(String str, @NonNull byte[] bArr) {
        try {
            Indicate_LocalSearchContactResponseImpl(str, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void Indicate_LocalSearchContactResponseImpl(String str, @NonNull byte[] bArr) {
        try {
            ContactSearchResponse parseFrom = ContactSearchResponse.parseFrom(bArr);
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IIMCallbackUIListener) iListener).Indicate_LocalSearchContactResponse(str, parseFrom.getJidList());
                }
            }
        } catch (Exception unused) {
        }
    }

    public long getUnSupportHandle() {
        return getUnSupportHandleImpl(this.mNativeHandle);
    }

    public long getAsynReadGroupMemberHandle() {
        return getAsynReadGroupMemberHandleImpl(this.mNativeHandle);
    }

    public long getSearchMgrUICallBackHandleImpl() {
        return getSearchMgrUICallBackHandleImpl(this.mNativeHandle);
    }
}
