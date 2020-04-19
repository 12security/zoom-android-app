package com.google.firebase.iid;

import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

final class zzaj {
    private final Messenger zzao;
    private final zzm zzcl;

    zzaj(IBinder iBinder) throws RemoteException {
        String interfaceDescriptor = iBinder.getInterfaceDescriptor();
        if ("android.os.IMessenger".equals(interfaceDescriptor)) {
            this.zzao = new Messenger(iBinder);
            this.zzcl = null;
        } else if ("com.google.android.gms.iid.IMessengerCompat".equals(interfaceDescriptor)) {
            this.zzcl = new zzm(iBinder);
            this.zzao = null;
        } else {
            String str = "Invalid interface descriptor: ";
            String valueOf = String.valueOf(interfaceDescriptor);
            Log.w("MessengerIpcClient", valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
            throw new RemoteException();
        }
    }

    /* access modifiers changed from: 0000 */
    public final void send(Message message) throws RemoteException {
        Messenger messenger = this.zzao;
        if (messenger != null) {
            messenger.send(message);
            return;
        }
        zzm zzm = this.zzcl;
        if (zzm != null) {
            zzm.send(message);
            return;
        }
        throw new IllegalStateException("Both messengers are null");
    }
}
