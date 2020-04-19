package com.zipow.videobox.ptapp;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.client.SIPIPCPort;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.NumberUtil;

public class PT4SIPIPCPort {
    private static final String TAG = "PT4SIPIPCPort";
    @Nullable
    private static PT4SIPIPCPort instance;
    private long mNativeHandle = 0;
    @NonNull
    private List<byte[]> mReceiveMessageBuff = new ArrayList();
    @NonNull
    private List<byte[]> mSendMessageBuff = new ArrayList();

    private native void nativeInit();

    private native void onMessageReceivedImpl(long j, byte[] bArr, int i);

    private PT4SIPIPCPort() {
    }

    @NonNull
    public static synchronized PT4SIPIPCPort getInstance() {
        PT4SIPIPCPort pT4SIPIPCPort;
        synchronized (PT4SIPIPCPort.class) {
            if (instance == null) {
                instance = new PT4SIPIPCPort();
            }
            pT4SIPIPCPort = instance;
        }
        return pT4SIPIPCPort;
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

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onMessageReceived(@androidx.annotation.Nullable byte[] r7) {
        /*
            r6 = this;
            monitor-enter(r6)
            if (r7 == 0) goto L_0x002c
            int r0 = r7.length     // Catch:{ all -> 0x0029 }
            r1 = 4
            if (r0 > r1) goto L_0x0008
            goto L_0x002c
        L_0x0008:
            r0 = 0
            p021us.zoom.androidlib.util.NumberUtil.byteArrayToInt(r7, r0)     // Catch:{ all -> 0x0029 }
            com.zipow.videobox.VideoBoxApplication r0 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ all -> 0x0029 }
            r0.getSipProcessId()     // Catch:{ all -> 0x0029 }
            long r2 = r6.mNativeHandle     // Catch:{ all -> 0x0029 }
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0022
            java.util.List<byte[]> r0 = r6.mReceiveMessageBuff     // Catch:{ all -> 0x0029 }
            r0.add(r7)     // Catch:{ all -> 0x0029 }
            monitor-exit(r6)
            return
        L_0x0022:
            long r2 = r6.mNativeHandle     // Catch:{ UnsatisfiedLinkError -> 0x0027 }
            r6.onMessageReceivedImpl(r2, r7, r1)     // Catch:{ UnsatisfiedLinkError -> 0x0027 }
        L_0x0027:
            monitor-exit(r6)
            return
        L_0x0029:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
        L_0x002c:
            monitor-exit(r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.PT4SIPIPCPort.onMessageReceived(byte[]):void");
    }

    public synchronized void setNativeHandle(long j) {
        this.mNativeHandle = j;
        if (this.mNativeHandle != 0) {
            receiveBufferedMessages();
        }
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

    private void sendMessageImpl(@NonNull byte[] bArr) throws RemoteException {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        int sipProcessId = VideoBoxApplication.getInstance().getSipProcessId();
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(NumberUtil.intToByteArray(sipProcessId));
            byteArrayOutputStream.write(bArr);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            SIPIPCPort.getInstance().onMessageReceived(byteArray);
            byteArrayOutputStream.close();
            return;
        } catch (Exception unused) {
            return;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        throw th;
    }
}
