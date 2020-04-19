package com.zipow.nydus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.kubi.IKubiService;
import com.zipow.videobox.kubi.KubiContract;
import com.zipow.videobox.kubi.KubiDevice;
import com.zipow.videobox.kubi.KubiServiceManager;
import com.zipow.videobox.kubi.KubiServiceManager.IKubiServiceConnectionListener;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

public class KUBIDeviceController implements IKubiServiceConnectionListener {
    private static final int KubiRelativePanAction_Left = -1;
    private static final int KubiRelativePanAction_Right = 1;
    private static final int KubiRelativePanAction_Stop = 0;
    private static final int KubiRelativeTiltAction_Down = -1;
    private static final int KubiRelativeTiltAction_Stop = 0;
    private static final int KubiRelativeTiltAction_Up = 1;
    private static final String TAG = "KUBIDeviceController";
    public static final int deviceFindDelay = 1;
    public static final int deviceNotificationDelay = 1;
    private static KUBIDeviceController instance = null;
    public static final int kubiDeviceConnected = 1;
    public static final int kubiDeviceDisConnected = 0;
    private Handler mHandler;
    @NonNull
    private ListenerList mKubiListeners = new ListenerList();
    private BroadcastReceiver mKubiMsgReceiver;
    private KubiServiceManager mKubiServiceMgr;
    private long mNotificationNativePtr = 0;

    public interface IKubiListener extends IListener {
        void onKubiDeviceFound(KubiDevice kubiDevice);

        void onKubiManagerFailed(int i);

        void onKubiManagerStatusChanged(int i, int i2);

        void onKubiScanComplete(ArrayList<KubiDevice> arrayList);
    }

    public static class SimpleKubiListener implements IKubiListener {
        public void onKubiDeviceFound(KubiDevice kubiDevice) {
        }

        public void onKubiManagerFailed(int i) {
        }

        public void onKubiManagerStatusChanged(int i, int i2) {
        }

        public void onKubiScanComplete(ArrayList<KubiDevice> arrayList) {
        }
    }

    private final native void nativeKubiDeviceConnected(long j, int i);

    public static synchronized KUBIDeviceController getInstance() {
        synchronized (KUBIDeviceController.class) {
            if (!HardwareUtil.isBluetoothLESupported(VideoBoxApplication.getInstance())) {
                return null;
            }
            if (instance == null) {
                instance = new KUBIDeviceController();
            }
            KUBIDeviceController kUBIDeviceController = instance;
            return kUBIDeviceController;
        }
    }

    private KUBIDeviceController() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            this.mKubiServiceMgr = KubiServiceManager.getInstance(instance2);
            this.mHandler = new Handler();
            this.mKubiServiceMgr.addConnectionListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(KubiContract.ACTION_KUBI_CONNECTION_STATUS);
            intentFilter.addAction(KubiContract.ACTION_KUBI_DEVICE_FOUND);
            intentFilter.addAction(KubiContract.ACTION_KUBI_MANAGER_FAILED);
            intentFilter.addAction(KubiContract.ACTION_KUBI_MANAGER_STATUS_CHANGED);
            intentFilter.addAction(KubiContract.ACTION_KUBI_SCAN_COMPLETE);
            this.mKubiMsgReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    KUBIDeviceController.this.onKubiMessageReceived(intent);
                }
            };
            BroadcastReceiver broadcastReceiver = this.mKubiMsgReceiver;
            StringBuilder sb = new StringBuilder();
            sb.append(instance2.getPackageName());
            sb.append(".permission.KUBI_MESSAGE");
            instance2.registerReceiver(broadcastReceiver, intentFilter, sb.toString(), this.mHandler);
        }
    }

    public void addKubiListener(IKubiListener iKubiListener) {
        this.mKubiListeners.add(iKubiListener);
    }

    public void removeKubiListener(IKubiListener iKubiListener) {
        this.mKubiListeners.remove(iKubiListener);
    }

    private IKubiService getKubiService() {
        KubiServiceManager kubiServiceManager = this.mKubiServiceMgr;
        if (kubiServiceManager == null) {
            return null;
        }
        return kubiServiceManager.getKubiService();
    }

    public int getKubiStatus() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.getKubiStatus();
            } catch (RemoteException unused) {
            }
        }
        return 0;
    }

    public void destroy() {
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            BroadcastReceiver broadcastReceiver = this.mKubiMsgReceiver;
            if (broadcastReceiver != null) {
                instance2.unregisterReceiver(broadcastReceiver);
            }
        }
    }

    public boolean findKubiDevice() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.findKubiDevice();
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public boolean releaseKubiDevice() {
        resetDevicePosition();
        return true;
    }

    public boolean disconnectKubi() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.disconnectKubi();
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public void findAllKubiDevices() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                kubiService.findAllKubiDevices();
            } catch (RemoteException unused) {
            }
        }
    }

    public void connectToKubi(KubiDevice kubiDevice) {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                kubiService.connectToKubi(kubiDevice);
            } catch (RemoteException unused) {
            }
        }
    }

    @Nullable
    public KubiDevice getCurrentKubi() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.getCurrentKubi();
            } catch (RemoteException unused) {
            }
        }
        return null;
    }

    public float devicePan() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.getPan();
            } catch (RemoteException unused) {
            }
        }
        return 0.0f;
    }

    public float deviceTilt() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                return kubiService.getTilt();
            } catch (RemoteException unused) {
            }
        }
        return 0.0f;
    }

    public boolean devicePanTo(float f) {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                kubiService.moveTo(f, deviceTilt(), 52.3f);
                return true;
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public boolean deviceTiltTo(float f) {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                kubiService.moveTo(devicePan(), f, 52.3f);
                return true;
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public boolean panAction(int i) {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                if (kubiService.getCurrentKubi() != null) {
                    switch (i) {
                        case -1:
                            kubiService.moveInPanDirectionWithSpeed(1, 78);
                            break;
                        case 0:
                            kubiService.moveInPanDirectionWithSpeed(0, 0);
                            break;
                        case 1:
                            kubiService.moveInPanDirectionWithSpeed(-1, 78);
                            break;
                    }
                    return true;
                }
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public boolean tiltAction(int i) {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                if (kubiService.getCurrentKubi() != null) {
                    switch (i) {
                        case -1:
                            kubiService.moveInTiltDirectionWithSpeed(-1, 47);
                            break;
                        case 0:
                            kubiService.moveInTiltDirectionWithSpeed(0, 0);
                            break;
                        case 1:
                            kubiService.moveInTiltDirectionWithSpeed(1, 47);
                            break;
                    }
                    return true;
                }
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    private boolean resetDevicePosition() {
        IKubiService kubiService = getKubiService();
        if (kubiService != null) {
            try {
                kubiService.resetDevicePosition();
                return true;
            } catch (RemoteException unused) {
            }
        }
        return false;
    }

    public boolean SetNotificationNativePtr(long j) {
        this.mNotificationNativePtr = j;
        return true;
    }

    /* access modifiers changed from: private */
    public void onKubiMessageReceived(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (KubiContract.ACTION_KUBI_CONNECTION_STATUS.equals(action)) {
                onKubiDeviceConnectionStatus(intent.getBooleanExtra(KubiContract.EXTRA_CONNECTED, false));
            } else if (KubiContract.ACTION_KUBI_DEVICE_FOUND.equals(action)) {
                onKubiDeviceFound((KubiDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE));
            } else if (KubiContract.ACTION_KUBI_MANAGER_FAILED.equals(action)) {
                onKubiManagerFailed(intent.getIntExtra(KubiContract.EXTRA_REASON, 0));
            } else if (KubiContract.ACTION_KUBI_MANAGER_STATUS_CHANGED.equals(action)) {
                onKubiManagerStatusChanged(intent.getIntExtra(KubiContract.EXTRA_OLD_STATUS, 0), intent.getIntExtra(KubiContract.EXTRA_NEW_STATUS, 0));
            } else if (KubiContract.ACTION_KUBI_SCAN_COMPLETE.equals(action)) {
                onKubiScanComplete(intent.getParcelableArrayListExtra(KubiContract.EXTRA_DEVICES));
            }
        }
    }

    private void onKubiDeviceConnectionStatus(boolean z) {
        nativeKubiDeviceConnected(this.mNotificationNativePtr, z ? 1 : 0);
    }

    private void onKubiDeviceFound(@Nullable KubiDevice kubiDevice) {
        for (IListener iListener : this.mKubiListeners.getAll()) {
            ((IKubiListener) iListener).onKubiDeviceFound(kubiDevice);
        }
    }

    private void onKubiManagerStatusChanged(int i, int i2) {
        for (IListener iListener : this.mKubiListeners.getAll()) {
            ((IKubiListener) iListener).onKubiManagerStatusChanged(i, i2);
        }
    }

    private void onKubiManagerFailed(int i) {
        for (IListener iListener : this.mKubiListeners.getAll()) {
            ((IKubiListener) iListener).onKubiManagerFailed(i);
        }
    }

    private void onKubiScanComplete(@Nullable ArrayList<KubiDevice> arrayList) {
        for (IListener iListener : this.mKubiListeners.getAll()) {
            ((IKubiListener) iListener).onKubiScanComplete(arrayList);
        }
    }

    public void onKubiServiceConnected(@Nullable IKubiService iKubiService) {
        if (iKubiService != null) {
            try {
                int kubiStatus = iKubiService.getKubiStatus();
                onKubiDeviceConnectionStatus(kubiStatus == 4);
                onKubiManagerStatusChanged(0, kubiStatus);
                if (kubiStatus == 4) {
                    iKubiService.resetDevicePosition();
                }
            } catch (RemoteException unused) {
            }
        }
    }

    public void onKubiServiceDisconnected() {
        onKubiDeviceConnectionStatus(false);
    }
}
