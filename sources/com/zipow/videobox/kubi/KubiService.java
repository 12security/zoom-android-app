package com.zipow.videobox.kubi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revolverobotics.kubisdk.IKubiManagerDelegate;
import com.revolverobotics.kubisdk.Kubi;
import com.revolverobotics.kubisdk.KubiManager;
import com.revolverobotics.kubisdk.KubiSearchResult;
import com.zipow.videobox.ZMBaseService;
import com.zipow.videobox.kubi.IKubiService.Stub;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class KubiService extends ZMBaseService {
    private static final String TAG = "KubiService";
    private ServiceBinder mServiceBinder;

    private static class ServiceBinder extends Stub implements IKubiManagerDelegate {
        private static final int DEVICE_FIND_DELAY = 1;
        private Context mContext;
        private KubiDevice mCurrentKubi;
        @NonNull
        private Handler mHandler = new Handler();
        private KubiManager mKubiManager;

        public ServiceBinder(Context context) {
            this.mContext = context;
            this.mKubiManager = new KubiManager(context, this);
        }

        private boolean isMainThread() {
            return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
        }

        public int getKubiStatus() throws RemoteException {
            if (isMainThread()) {
                return getKubiStatusInternal().intValue();
            }
            FutureTask futureTask = new FutureTask(new Callable<Integer>() {
                public Integer call() throws Exception {
                    return ServiceBinder.this.getKubiStatusInternal();
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Integer) futureTask.get()).intValue();
            } catch (Exception unused) {
                return 0;
            }
        }

        /* access modifiers changed from: private */
        public Integer getKubiStatusInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                return Integer.valueOf(kubiManager.getStatus());
            }
            return Integer.valueOf(0);
        }

        public boolean findKubiDevice() throws RemoteException {
            if (isMainThread()) {
                return findKubiDeviceInternal();
            }
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(ServiceBinder.this.findKubiDeviceInternal());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception unused) {
                return false;
            }
        }

        /* access modifiers changed from: private */
        public boolean findKubiDeviceInternal() {
            if (this.mKubiManager == null || !checkBluetoothStatus() || !checkLocationPermission()) {
                return false;
            }
            if (4 == this.mKubiManager.getStatus()) {
                resetDevicePositionInternal();
                notifyKubiConnectionStatus(true);
            } else {
                this.mKubiManager.disconnect();
                this.mKubiManager.findKubi(1);
            }
            return true;
        }

        public boolean disconnectKubi() throws RemoteException {
            if (isMainThread()) {
                return disconnectKubi();
            }
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(ServiceBinder.this.disconnectKubiInternal());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception unused) {
                return false;
            }
        }

        /* access modifiers changed from: private */
        public boolean disconnectKubiInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                try {
                    kubiManager.disconnect();
                } catch (Exception unused) {
                }
            }
            setCurrentKubi(null);
            return true;
        }

        public void findAllKubiDevices() throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ServiceBinder.this.findAllKubiDevicesInternal();
                }
            });
        }

        /* access modifiers changed from: private */
        public void findAllKubiDevicesInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                kubiManager.findAllKubis();
            }
        }

        public void connectToKubi(final KubiDevice kubiDevice) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ServiceBinder.this.connectToKubiInternal(kubiDevice);
                }
            });
        }

        /* access modifiers changed from: private */
        public void connectToKubiInternal(@Nullable KubiDevice kubiDevice) {
            if (this.mKubiManager != null && kubiDevice != null) {
                BluetoothDevice bluetoothDevice = kubiDevice.getBluetoothDevice();
                if (bluetoothDevice != null) {
                    this.mKubiManager.connectToKubi(new KubiSearchResult(bluetoothDevice, kubiDevice.getRSSI()));
                    setCurrentKubi(kubiDevice);
                }
            }
        }

        public KubiDevice getCurrentKubi() throws RemoteException {
            return getCurrentKubiInternal();
        }

        public synchronized KubiDevice getCurrentKubiInternal() {
            return this.mCurrentKubi;
        }

        private synchronized void setCurrentKubi(KubiDevice kubiDevice) {
            this.mCurrentKubi = kubiDevice;
        }

        public float getPan() throws RemoteException {
            if (isMainThread()) {
                return getPanInternal();
            }
            FutureTask futureTask = new FutureTask(new Callable<Float>() {
                public Float call() throws Exception {
                    return Float.valueOf(ServiceBinder.this.getPanInternal());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Float) futureTask.get()).floatValue();
            } catch (Exception unused) {
                return 0.0f;
            }
        }

        /* access modifiers changed from: private */
        public float getPanInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    return kubi.getPan();
                }
            }
            return 0.0f;
        }

        public float getTilt() throws RemoteException {
            if (isMainThread()) {
                return getTilt();
            }
            FutureTask futureTask = new FutureTask(new Callable<Float>() {
                public Float call() throws Exception {
                    return Float.valueOf(ServiceBinder.this.getTiltInternal());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Float) futureTask.get()).floatValue();
            } catch (Exception unused) {
                return 0.0f;
            }
        }

        /* access modifiers changed from: private */
        public float getTiltInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    return kubi.getTilt();
                }
            }
            return 0.0f;
        }

        public void moveInPanDirectionWithSpeed(final int i, final int i2) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ServiceBinder.this.moveInPanDirectionWithSpeedInternal(i, i2);
                }
            });
        }

        /* access modifiers changed from: private */
        public void moveInPanDirectionWithSpeedInternal(int i, int i2) {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    kubi.moveInPanDirectionWithSpeed(i, i2);
                }
            }
        }

        public void moveInTiltDirectionWithSpeed(final int i, final int i2) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ServiceBinder.this.moveInTiltDirectionWithSpeedInternal(i, i2);
                }
            });
        }

        /* access modifiers changed from: private */
        public void moveInTiltDirectionWithSpeedInternal(int i, int i2) {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    kubi.moveInTiltDirectionWithSpeed(i, i2);
                }
            }
        }

        public void moveTo(final float f, final float f2, final float f3) throws RemoteException {
            this.mHandler.post(new Runnable() {
                public void run() {
                    ServiceBinder.this.moveToInternal(f, f2, f3);
                }
            });
        }

        /* access modifiers changed from: private */
        public void moveToInternal(float f, float f2, float f3) {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    kubi.moveTo(f, f2, f3);
                }
            }
        }

        public boolean resetDevicePosition() throws RemoteException {
            if (isMainThread()) {
                return resetDevicePositionInternal();
            }
            FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return Boolean.valueOf(ServiceBinder.this.resetDevicePositionInternal());
                }
            });
            this.mHandler.post(futureTask);
            try {
                return ((Boolean) futureTask.get()).booleanValue();
            } catch (Exception unused) {
                return false;
            }
        }

        public boolean resetDevicePositionInternal() {
            KubiManager kubiManager = this.mKubiManager;
            if (kubiManager != null) {
                Kubi kubi = kubiManager.getKubi();
                if (kubi != null) {
                    kubi.moveTo(0.0f, 0.0f, 52.3f);
                    return true;
                }
            }
            return false;
        }

        public void kubiDeviceFound(KubiManager kubiManager, KubiSearchResult kubiSearchResult) {
            KubiDevice fromKubiSearchResult = KubiDevice.fromKubiSearchResult(kubiSearchResult);
            if (fromKubiSearchResult != null) {
                connectToKubiInternal(fromKubiSearchResult);
                notifyKubiDeviceFound(kubiManager, fromKubiSearchResult);
            }
        }

        public void kubiManagerStatusChanged(KubiManager kubiManager, int i, int i2) {
            if (i == 4 && i2 != 4) {
                setCurrentKubi(null);
                notifyKubiConnectionStatus(false);
            } else if (i != 4 && i2 == 4) {
                notifyKubiConnectionStatus(true);
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        ServiceBinder.this.resetDevicePositionInternal();
                    }
                }, 1);
            }
            notifyKubiManagerStatusChanged(kubiManager, i, i2);
        }

        public void kubiManagerFailed(KubiManager kubiManager, int i) {
            notifyKubiManagerFailed(kubiManager, i);
        }

        public void kubiScanComplete(KubiManager kubiManager, @Nullable ArrayList<KubiSearchResult> arrayList) {
            if (arrayList != null) {
                ArrayList arrayList2 = new ArrayList(arrayList.size());
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    KubiDevice fromKubiSearchResult = KubiDevice.fromKubiSearchResult((KubiSearchResult) it.next());
                    if (fromKubiSearchResult != null) {
                        arrayList2.add(fromKubiSearchResult);
                    }
                }
                notifyKubiScanComplete(kubiManager, arrayList2);
            }
        }

        private boolean checkBluetoothStatus() {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            if (defaultAdapter == null) {
                return false;
            }
            return defaultAdapter.isEnabled();
        }

        private boolean checkLocationPermission() {
            if (VERSION.SDK_INT <= 22) {
                return true;
            }
            Context context = this.mContext;
            if (context != null && context.checkPermission("android.permission.ACCESS_FINE_LOCATION", Process.myPid(), Process.myUid()) == 0) {
                return true;
            }
            return false;
        }

        private void notifyKubiConnectionStatus(boolean z) {
            if (this.mContext != null) {
                Intent intent = new Intent();
                intent.setAction(KubiContract.ACTION_KUBI_CONNECTION_STATUS);
                intent.putExtra(KubiContract.EXTRA_CONNECTED, z);
                Context context = this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getPackageName());
                sb.append(".permission.KUBI_MESSAGE");
                context.sendBroadcast(intent, sb.toString());
            }
        }

        private void notifyKubiDeviceFound(KubiManager kubiManager, KubiDevice kubiDevice) {
            if (this.mContext != null) {
                Intent intent = new Intent();
                intent.setAction(KubiContract.ACTION_KUBI_DEVICE_FOUND);
                intent.putExtra(KubiContract.EXTRA_DEVICE, kubiDevice);
                Context context = this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getPackageName());
                sb.append(".permission.KUBI_MESSAGE");
                context.sendBroadcast(intent, sb.toString());
            }
        }

        private void notifyKubiManagerStatusChanged(KubiManager kubiManager, int i, int i2) {
            if (this.mContext != null) {
                Intent intent = new Intent();
                intent.setAction(KubiContract.ACTION_KUBI_MANAGER_STATUS_CHANGED);
                intent.putExtra(KubiContract.EXTRA_OLD_STATUS, i);
                intent.putExtra(KubiContract.EXTRA_NEW_STATUS, i2);
                Context context = this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getPackageName());
                sb.append(".permission.KUBI_MESSAGE");
                context.sendBroadcast(intent, sb.toString());
            }
        }

        private void notifyKubiManagerFailed(KubiManager kubiManager, int i) {
            if (this.mContext != null) {
                Intent intent = new Intent();
                intent.setAction(KubiContract.ACTION_KUBI_MANAGER_FAILED);
                intent.putExtra(KubiContract.EXTRA_REASON, i);
                Context context = this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getPackageName());
                sb.append(".permission.KUBI_MESSAGE");
                context.sendBroadcast(intent, sb.toString());
            }
        }

        private void notifyKubiScanComplete(KubiManager kubiManager, ArrayList<KubiDevice> arrayList) {
            if (this.mContext != null) {
                Intent intent = new Intent();
                intent.setAction(KubiContract.ACTION_KUBI_SCAN_COMPLETE);
                intent.putParcelableArrayListExtra(KubiContract.EXTRA_DEVICES, arrayList);
                Context context = this.mContext;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getPackageName());
                sb.append(".permission.KUBI_MESSAGE");
                context.sendBroadcast(intent, sb.toString());
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return getServiceBinder();
    }

    public void onCreate() {
        super.onCreate();
    }

    private ServiceBinder getServiceBinder() {
        if (VERSION.SDK_INT < 18) {
            return null;
        }
        if (this.mServiceBinder == null) {
            this.mServiceBinder = new ServiceBinder(getApplicationContext());
        }
        return this.mServiceBinder;
    }

    public void onDestroy() {
        ServiceBinder serviceBinder = this.mServiceBinder;
        if (serviceBinder != null) {
            serviceBinder.disconnectKubiInternal();
        }
        super.onDestroy();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int onStartCommand(@Nullable Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (intent == null) {
            return 2;
        }
        String action = intent.getAction();
        ServiceBinder serviceBinder = getServiceBinder();
        if (serviceBinder == null) {
            return 2;
        }
        int intValue = serviceBinder.getKubiStatusInternal().intValue();
        if (!(serviceBinder.getCurrentKubiInternal() != null || intValue == 2 || intValue == 3 || intValue == 5 || KubiContract.ACTION_START_KUBI_SERVICE_NO_AUTO_CONNECT.equals(action))) {
            serviceBinder.findKubiDeviceInternal();
        }
        return 2;
    }
}
