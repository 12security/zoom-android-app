package com.zipow.videobox.ptapp;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IConfService;
import com.zipow.videobox.VideoBoxApplication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.NumberUtil;

public class PTIPCPort {
    private static final String TAG = "PTIPCPort";
    @Nullable
    private static PTIPCPort instance;
    private long mNativeHandle = 0;
    @NonNull
    private List<byte[]> mReceiveMessageBuff = new ArrayList();
    @NonNull
    private List<byte[]> mSendMessageBuff = new ArrayList();

    private native void nativeInit();

    private native void onMessageReceivedImpl(long j, byte[] bArr, int i);

    private PTIPCPort() {
    }

    @NonNull
    public static synchronized PTIPCPort getInstance() {
        PTIPCPort pTIPCPort;
        synchronized (PTIPCPort.class) {
            if (instance == null) {
                instance = new PTIPCPort();
            }
            pTIPCPort = instance;
        }
        return pTIPCPort;
    }

    public void initialize() {
        nativeInit();
    }

    public boolean sendMessage(@Nullable byte[] bArr) {
        boolean z = false;
        if (bArr == null) {
            return false;
        }
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService == null) {
            this.mSendMessageBuff.add(bArr);
            return false;
        }
        try {
            sendBufferedMessages(confService);
            sendMessageImpl(bArr, confService);
            z = true;
        } catch (Exception unused) {
            this.mSendMessageBuff.add(bArr);
        }
        return z;
    }

    public void sendBufferedMessages() {
        IConfService confService = VideoBoxApplication.getInstance().getConfService();
        if (confService != null) {
            try {
                sendBufferedMessages(confService);
            } catch (RemoteException unused) {
            }
        }
    }

    private void sendBufferedMessages(@NonNull IConfService iConfService) throws RemoteException {
        if (this.mSendMessageBuff.size() > 0) {
            for (int size = this.mSendMessageBuff.size() - 1; size >= 0; size--) {
                sendMessageImpl((byte[]) this.mSendMessageBuff.get(size), iConfService);
                this.mSendMessageBuff.remove(size);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0033, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onMessageReceived(@androidx.annotation.Nullable byte[] r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 == 0) goto L_0x0032
            int r0 = r7.length     // Catch:{ all -> 0x002f }
            r1 = 4
            if (r0 > r1) goto L_0x0008
            goto L_0x0032
        L_0x0008:
            r0 = 0
            int r0 = p021us.zoom.androidlib.util.NumberUtil.byteArrayToInt(r7, r0)     // Catch:{ all -> 0x002f }
            com.zipow.videobox.ptapp.ConfProcessMgr r2 = com.zipow.videobox.ptapp.ConfProcessMgr.getInstance()     // Catch:{ all -> 0x002f }
            int r2 = r2.getCurrentConfProcessId()     // Catch:{ all -> 0x002f }
            if (r0 == r2) goto L_0x0019
            monitor-exit(r6)
            return
        L_0x0019:
            long r2 = r6.mNativeHandle     // Catch:{ all -> 0x002f }
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0028
            java.util.List<byte[]> r0 = r6.mReceiveMessageBuff     // Catch:{ all -> 0x002f }
            r0.add(r7)     // Catch:{ all -> 0x002f }
            monitor-exit(r6)
            return
        L_0x0028:
            long r2 = r6.mNativeHandle     // Catch:{ UnsatisfiedLinkError -> 0x002d }
            r6.onMessageReceivedImpl(r2, r7, r1)     // Catch:{ UnsatisfiedLinkError -> 0x002d }
        L_0x002d:
            monitor-exit(r6)
            return
        L_0x002f:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        L_0x0032:
            monitor-exit(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.PTIPCPort.onMessageReceived(byte[]):void");
    }

    public synchronized void setNativeHandle(long j) {
        this.mNativeHandle = j;
        if (this.mNativeHandle != 0) {
            receiveBufferedMessages();
        }
    }

    private void receiveBufferedMessages() {
        if (this.mReceiveMessageBuff.size() > 0) {
            int currentConfProcessId = ConfProcessMgr.getInstance().getCurrentConfProcessId();
            for (byte[] bArr : this.mReceiveMessageBuff) {
                if (NumberUtil.byteArrayToInt(bArr, 0) == currentConfProcessId) {
                    try {
                        onMessageReceivedImpl(this.mNativeHandle, bArr, 4);
                    } catch (UnsatisfiedLinkError unused) {
                    }
                }
            }
            this.mReceiveMessageBuff.clear();
        }
    }

    private void sendMessageImpl(@NonNull byte[] bArr, @NonNull IConfService iConfService) throws RemoteException {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        int currentConfProcessId = ConfProcessMgr.getInstance().getCurrentConfProcessId();
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(NumberUtil.intToByteArray(currentConfProcessId));
            byteArrayOutputStream.write(bArr);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            iConfService.sendMessage(byteArray);
            byteArrayOutputStream.close();
            return;
        } catch (IOException unused) {
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        throw th;
    }
}
