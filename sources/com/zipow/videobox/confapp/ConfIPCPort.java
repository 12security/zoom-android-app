package com.zipow.videobox.confapp;

import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IPTService;
import com.zipow.videobox.VideoBoxApplication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.NumberUtil;

public class ConfIPCPort {
    private static final String TAG = "ConfIPCPort";
    @Nullable
    private static ConfIPCPort instance;
    private long mNativeHandle = 0;
    @NonNull
    private List<byte[]> mReceiveMessageBuff = new ArrayList();
    @NonNull
    private List<byte[]> mSendMessageBuff = new ArrayList();

    private native void nativeInit();

    private native void onMessageReceivedImpl(long j, byte[] bArr, int i);

    private ConfIPCPort() {
    }

    @NonNull
    public static synchronized ConfIPCPort getInstance() {
        ConfIPCPort confIPCPort;
        synchronized (ConfIPCPort.class) {
            if (instance == null) {
                instance = new ConfIPCPort();
            }
            confIPCPort = instance;
        }
        return confIPCPort;
    }

    public void initialize() {
        nativeInit();
    }

    public boolean sendMessage(@Nullable byte[] bArr) {
        boolean z = false;
        if (bArr == null) {
            return false;
        }
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService == null) {
            this.mSendMessageBuff.add(bArr);
            return false;
        }
        try {
            sendBufferedMessages(pTService);
            sendMessageImpl(bArr, pTService);
            z = true;
        } catch (Exception unused) {
            this.mSendMessageBuff.add(bArr);
        }
        return z;
    }

    public void sendBufferedMessages() {
        IPTService pTService = VideoBoxApplication.getInstance().getPTService();
        if (pTService != null) {
            try {
                sendBufferedMessages(pTService);
            } catch (RemoteException unused) {
            }
        }
    }

    private void sendBufferedMessages(@NonNull IPTService iPTService) throws RemoteException {
        if (this.mSendMessageBuff.size() > 0) {
            for (int size = this.mSendMessageBuff.size() - 1; size >= 0; size--) {
                sendMessageImpl((byte[]) this.mSendMessageBuff.get(size), iPTService);
                this.mSendMessageBuff.remove(size);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x002f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onMessageReceived(@androidx.annotation.Nullable byte[] r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 == 0) goto L_0x002e
            int r0 = r7.length     // Catch:{ all -> 0x002b }
            r1 = 4
            if (r0 > r1) goto L_0x0008
            goto L_0x002e
        L_0x0008:
            int r0 = android.os.Process.myPid()     // Catch:{ all -> 0x002b }
            r2 = 0
            int r2 = p021us.zoom.androidlib.util.NumberUtil.byteArrayToInt(r7, r2)     // Catch:{ all -> 0x002b }
            if (r2 == r0) goto L_0x0015
            monitor-exit(r6)
            return
        L_0x0015:
            long r2 = r6.mNativeHandle     // Catch:{ all -> 0x002b }
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0024
            java.util.List<byte[]> r0 = r6.mReceiveMessageBuff     // Catch:{ all -> 0x002b }
            r0.add(r7)     // Catch:{ all -> 0x002b }
            monitor-exit(r6)
            return
        L_0x0024:
            long r2 = r6.mNativeHandle     // Catch:{ UnsatisfiedLinkError -> 0x0029 }
            r6.onMessageReceivedImpl(r2, r7, r1)     // Catch:{ UnsatisfiedLinkError -> 0x0029 }
        L_0x0029:
            monitor-exit(r6)
            return
        L_0x002b:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        L_0x002e:
            monitor-exit(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.ConfIPCPort.onMessageReceived(byte[]):void");
    }

    public synchronized void setNativeHandle(long j) {
        this.mNativeHandle = j;
        receiveBufferedMessages();
    }

    private void receiveBufferedMessages() {
        if (this.mReceiveMessageBuff.size() > 0) {
            for (byte[] onMessageReceivedImpl : this.mReceiveMessageBuff) {
                try {
                    onMessageReceivedImpl(this.mNativeHandle, onMessageReceivedImpl, 4);
                } catch (UnsatisfiedLinkError unused) {
                }
            }
            this.mReceiveMessageBuff.clear();
        }
    }

    private void sendMessageImpl(@NonNull byte[] bArr, @NonNull IPTService iPTService) throws RemoteException {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        int myPid = Process.myPid();
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(NumberUtil.intToByteArray(myPid));
            byteArrayOutputStream.write(bArr);
            iPTService.sendMessage(byteArrayOutputStream.toByteArray());
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
