package com.zipow.videobox.kubi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.kubi.IKubiService.Stub;
import com.zipow.videobox.util.ZMServiceHelper;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.StringUtil;

public class KubiServiceManager {
    private static final String TAG = "KubiServiceManager";
    private static KubiServiceManager instance;
    @NonNull
    private ListenerList mConnectionListenerList = new ListenerList();
    @Nullable
    private Context mContext = null;
    @Nullable
    private IKubiService mKubiService = null;
    @Nullable
    private ServiceConnection mKubiServiceConnection = null;

    public interface IKubiServiceConnectionListener extends IListener {
        void onKubiServiceConnected(IKubiService iKubiService);

        void onKubiServiceDisconnected();
    }

    public static synchronized KubiServiceManager getInstance(Context context) {
        KubiServiceManager kubiServiceManager;
        synchronized (KubiServiceManager.class) {
            if (instance == null) {
                instance = new KubiServiceManager(context);
            }
            kubiServiceManager = instance;
        }
        return kubiServiceManager;
    }

    private KubiServiceManager(@Nullable Context context) {
        if (context != null) {
            this.mContext = context.getApplicationContext();
            return;
        }
        throw new RuntimeException("context is null");
    }

    public void addConnectionListener(IKubiServiceConnectionListener iKubiServiceConnectionListener) {
        this.mConnectionListenerList.add(iKubiServiceConnectionListener);
    }

    public void removeConnectionListener(IKubiServiceConnectionListener iKubiServiceConnectionListener) {
        this.mConnectionListenerList.remove(iKubiServiceConnectionListener);
    }

    public void startKubiService() {
        startKubiService(null);
    }

    private boolean isKubiSupported() {
        Context context = this.mContext;
        return context != null && HardwareUtil.isBluetoothLESupported(context);
    }

    public void startKubiService(String str) {
        if (this.mContext != null && isKubiSupported()) {
            Intent intent = new Intent();
            intent.setClassName(this.mContext.getPackageName(), KubiService.class.getName());
            if (!StringUtil.isEmptyOrNull(str)) {
                intent.setAction(KubiContract.ACTION_START_KUBI_SERVICE_NO_AUTO_CONNECT);
            }
            VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
            CompatUtils.startService(this.mContext, intent, !instance2.isAtFront(), instance2.isMultiProcess());
        }
    }

    public void stopKubiService() {
        if (this.mContext != null && isKubiSupported()) {
            Context context = this.mContext;
            ZMServiceHelper.stopService(context, context.getPackageName(), KubiService.class.getName());
        }
    }

    @Nullable
    public IKubiService getKubiService() {
        return this.mKubiService;
    }

    public void connectKubiService(boolean z) {
        if (this.mKubiService == null && this.mContext != null && isKubiSupported()) {
            if (this.mKubiServiceConnection == null) {
                this.mKubiServiceConnection = new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        KubiServiceManager.this.onKubiServiceConnected(Stub.asInterface(iBinder));
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                        KubiServiceManager.this.onKubiServiceDisconnected();
                    }
                };
            }
            Intent intent = new Intent();
            intent.setClassName(this.mContext.getPackageName(), KubiService.class.getName());
            int i = 64;
            if (z) {
                i = 65;
            }
            this.mContext.bindService(intent, this.mKubiServiceConnection, i);
        }
    }

    /* access modifiers changed from: private */
    public void onKubiServiceConnected(IKubiService iKubiService) {
        this.mKubiService = iKubiService;
        for (IListener iListener : this.mConnectionListenerList.getAll()) {
            ((IKubiServiceConnectionListener) iListener).onKubiServiceConnected(this.mKubiService);
        }
    }

    /* access modifiers changed from: private */
    public void onKubiServiceDisconnected() {
        this.mKubiService = null;
        this.mKubiServiceConnection = null;
        for (IListener iListener : this.mConnectionListenerList.getAll()) {
            ((IKubiServiceConnectionListener) iListener).onKubiServiceDisconnected();
        }
    }
}
