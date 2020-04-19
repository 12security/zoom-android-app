package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.ButtonParam;
import com.zipow.videobox.ptapp.IMProtos.EditParam;
import com.zipow.videobox.ptapp.IMProtos.FieldsEditParam;
import com.zipow.videobox.ptapp.IMProtos.SelectParam;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.StringUtil;

public class ZoomMessageTemplateUI {
    private static final String TAG = "ZoomMessageTemplateUI";
    @Nullable
    private static ZoomMessageTemplateUI instance;
    @NonNull
    private ListenerList mListenerList = new ListenerList();
    private long mNativeHandle;

    public interface IZoomMessageTemplateUIListener extends IListener {
        void Notify_ButtonCommandResponse(boolean z, ButtonParam buttonParam);

        void Notify_EditCommandResponse(boolean z, EditParam editParam);

        void Notify_EditRobotMessage(String str, String str2);

        void Notify_FieldsEditCommandResponse(boolean z, FieldsEditParam fieldsEditParam);

        void Notify_RevokeRobotMessage(String str, String str2, String str3);

        void Notify_SelectCommandResponse(boolean z, SelectParam selectParam);

        void Notify_SendGetHttpMessageDone(String str, int i);

        void Notify_SendPostHttpMessageDone(String str, int i);
    }

    public static abstract class SimpleZoomMessageTemplateUIListener implements IZoomMessageTemplateUIListener {
        public void Notify_ButtonCommandResponse(boolean z, ButtonParam buttonParam) {
        }

        public void Notify_EditCommandResponse(boolean z, EditParam editParam) {
        }

        public void Notify_EditRobotMessage(String str, String str2) {
        }

        public void Notify_FieldsEditCommandResponse(boolean z, FieldsEditParam fieldsEditParam) {
        }

        public void Notify_RevokeRobotMessage(String str, String str2, String str3) {
        }

        public void Notify_SelectCommandResponse(boolean z, SelectParam selectParam) {
        }

        public void Notify_SendGetHttpMessageDone(String str, int i) {
        }

        public void Notify_SendPostHttpMessageDone(String str, int i) {
        }
    }

    private native long nativeInit();

    private native void nativeUninit(long j);

    public void addListener(@Nullable IZoomMessageTemplateUIListener iZoomMessageTemplateUIListener) {
        if (iZoomMessageTemplateUIListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == iZoomMessageTemplateUIListener) {
                    removeListener((IZoomMessageTemplateUIListener) all[i]);
                }
            }
            this.mListenerList.add(iZoomMessageTemplateUIListener);
        }
    }

    public void removeListener(IZoomMessageTemplateUIListener iZoomMessageTemplateUIListener) {
        this.mListenerList.remove(iZoomMessageTemplateUIListener);
    }

    @NonNull
    public static synchronized ZoomMessageTemplateUI getInstance() {
        ZoomMessageTemplateUI zoomMessageTemplateUI;
        synchronized (ZoomMessageTemplateUI.class) {
            if (instance == null) {
                instance = new ZoomMessageTemplateUI();
            }
            if (!instance.initialized()) {
                instance.init();
            }
            zoomMessageTemplateUI = instance;
        }
        return zoomMessageTemplateUI;
    }

    private ZoomMessageTemplateUI() {
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
    public void notifySendPostHttpMessageDone(String str, int i) {
        try {
            notifySendPostHttpMessageDoneImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifySendPostHttpMessageDoneImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_SendPostHttpMessageDone(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifySendGetHttpMessageDone(String str, int i) {
        try {
            notifySendGetHttpMessageDoneImpl(str, i);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifySendGetHttpMessageDoneImpl(String str, int i) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_SendGetHttpMessageDone(str, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifySelectCommandResponse(boolean z, @NonNull byte[] bArr) {
        try {
            notifySelectCommandResponseImpl(z, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifySelectCommandResponseImpl(boolean z, @NonNull byte[] bArr) {
        SelectParam selectParam;
        try {
            selectParam = SelectParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException unused) {
            selectParam = null;
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_SelectCommandResponse(z, selectParam);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyButtonCommandResponse(boolean z, @NonNull byte[] bArr) {
        try {
            notifyButtonCommandResponseImpl(z, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyButtonCommandResponseImpl(boolean z, @NonNull byte[] bArr) {
        ButtonParam buttonParam;
        try {
            buttonParam = ButtonParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException unused) {
            buttonParam = null;
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_ButtonCommandResponse(z, buttonParam);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyEditCommandResponse(boolean z, @NonNull byte[] bArr) {
        try {
            notifyEditCommandResponseImpl(z, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyEditCommandResponseImpl(boolean z, @NonNull byte[] bArr) {
        EditParam editParam;
        try {
            editParam = EditParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException unused) {
            editParam = null;
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_EditCommandResponse(z, editParam);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notifyFieldsEditCommandResponse(boolean z, @NonNull byte[] bArr) {
        try {
            notifyFieldsEditCommandResponseImpl(z, bArr);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notifyFieldsEditCommandResponseImpl(boolean z, @NonNull byte[] bArr) {
        FieldsEditParam fieldsEditParam;
        try {
            fieldsEditParam = FieldsEditParam.parseFrom(bArr);
        } catch (InvalidProtocolBufferException unused) {
            fieldsEditParam = null;
        }
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_FieldsEditCommandResponse(z, fieldsEditParam);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_EditRobotMessage(String str, String str2) {
        try {
            notify_EditRobotMessageImpl(str, str2);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_EditRobotMessageImpl(@NonNull String str, @NonNull String str2) {
        IListener[] all = this.mListenerList.getAll();
        if (all != null) {
            for (IListener iListener : all) {
                ((IZoomMessageTemplateUIListener) iListener).Notify_EditRobotMessage(str, str2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void notify_RevokeRobotMessage(String str, String str2, String str3, long j, long j2, long j3) {
        try {
            notify_RevokeRobotMessageImpl(str, str2, str3, j, j2, j3);
        } catch (Throwable th) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
    }

    private void notify_RevokeRobotMessageImpl(@NonNull String str, @NonNull String str2, String str3, long j, long j2, long j3) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (!StringUtil.isEmptyOrNull(str) && zoomMessenger != null && zoomMessenger.getSessionById(str) != null && zoomMessenger.getMyself() != null) {
            IListener[] all = this.mListenerList.getAll();
            if (all != null) {
                for (IListener iListener : all) {
                    ((IZoomMessageTemplateUIListener) iListener).Notify_RevokeRobotMessage(str, str2, null);
                }
            }
        }
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }
}
