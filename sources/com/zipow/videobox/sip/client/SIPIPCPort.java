package com.zipow.videobox.sip.client;

import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PT4SIPIPCPort;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.NumberUtil;

public class SIPIPCPort {
    private static final String TAG = "SIPIPCPort";
    @Nullable
    private static SIPIPCPort instance;
    private long mNativeHandle = 0;
    @NonNull
    private List<byte[]> mReceiveMessageBuff = new ArrayList();
    @NonNull
    private List<byte[]> mSendMessageBuff = new ArrayList();

    private native void nativeInit();

    private native void onMessageReceivedImpl(long j, byte[] bArr, int i);

    private SIPIPCPort() {
    }

    @NonNull
    public static synchronized SIPIPCPort getInstance() {
        SIPIPCPort sIPIPCPort;
        synchronized (SIPIPCPort.class) {
            if (instance == null) {
                instance = new SIPIPCPort();
            }
            sIPIPCPort = instance;
        }
        return sIPIPCPort;
    }

    public void initialize() {
        nativeInit();
    }

    public boolean sendMessage(@Nullable byte[] bArr) {
        boolean z = false;
        if (bArr == null) {
            return false;
        }
        try {
            sendBufferedMessages();
            sendMessageImpl(bArr);
            z = true;
        } catch (Exception unused) {
            this.mSendMessageBuff.add(bArr);
        }
        return z;
    }

    public void sendBufferedMessages() throws RemoteException {
        if (this.mSendMessageBuff.size() > 0) {
            for (int size = this.mSendMessageBuff.size() - 1; size >= 0; size--) {
                sendMessageImpl((byte[]) this.mSendMessageBuff.get(size));
                this.mSendMessageBuff.remove(size);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0022, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onMessageReceived(@androidx.annotation.Nullable byte[] r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 == 0) goto L_0x0021
            int r0 = r7.length     // Catch:{ all -> 0x001e }
            r1 = 4
            if (r0 > r1) goto L_0x0008
            goto L_0x0021
        L_0x0008:
            long r2 = r6.mNativeHandle     // Catch:{ all -> 0x001e }
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0017
            java.util.List<byte[]> r0 = r6.mReceiveMessageBuff     // Catch:{ all -> 0x001e }
            r0.add(r7)     // Catch:{ all -> 0x001e }
            monitor-exit(r6)
            return
        L_0x0017:
            long r2 = r6.mNativeHandle     // Catch:{ UnsatisfiedLinkError -> 0x001c }
            r6.onMessageReceivedImpl(r2, r7, r1)     // Catch:{ UnsatisfiedLinkError -> 0x001c }
        L_0x001c:
            monitor-exit(r6)
            return
        L_0x001e:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        L_0x0021:
            monitor-exit(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.sip.client.SIPIPCPort.onMessageReceived(byte[]):void");
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

    private void sendMessageImpl(@NonNull byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        int myPid = Process.myPid();
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(NumberUtil.intToByteArray(myPid));
            byteArrayOutputStream.write(bArr);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            PT4SIPIPCPort.getInstance().onMessageReceived(byteArray);
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
