package com.revolverobotics.kubisdk;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.ArrayList;

@TargetApi(18)
public class KubiManager implements LeScanCallback {
    public static final int FAIL_CONNECTION_LOST = 1;
    public static final int FAIL_DISTANCE = 2;
    public static final int FAIL_NONE = 0;
    public static final int FAIL_NO_BLE = 4;
    public static final int FAIL_NO_BLUETOOTH = 3;
    public static final int STATUS_CONNECTED = 4;
    public static final int STATUS_CONNECTING = 3;
    public static final int STATUS_DISCONNECTED = 0;
    public static final int STATUS_DISCONNECTING = 1;
    public static final int STATUS_FINDING = 2;
    public static final int STATUS_RECONNECTING = 5;
    private final int AUTO_SCAN_INTERVAL = 0;
    private final int RSSI_CONNECT = -80;
    private final int RSSI_DISCONNECT = -100;
    private BluetoothAdapter adapter;
    @Nullable
    private BroadcastReceiver bluetoothBroadcastReceiver;
    boolean cancelScan = false;
    private BluetoothDevice connectDevice;
    @Nullable
    private Kubi connectedKubi;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable findFinish = new Runnable() {
        public void run() {
            KubiManager.this.finishScan(false);
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<String> foundMACs = new ArrayList<>();
    @NonNull
    private Runnable fullScanFinish = new Runnable() {
        public void run() {
            KubiManager.this.finishScan(true);
        }
    };
    boolean mAutoDisconnect = false;
    boolean mAutoFind = false;
    int mBluetoothDiscoveryTime = 5000;
    private Context mContext;
    IKubiManagerDelegate mDelegate;
    int mFailure = 0;
    Handler mHandler;
    @NonNull
    ArrayList<KubiSearchResult> mKubiList = new ArrayList<>();
    int mStatus = 0;
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<KubiSearchResult> nearKubis = new ArrayList<>();

    public IKubiManagerDelegate getDelegate() {
        return this.mDelegate;
    }

    public void setDelegate(IKubiManagerDelegate iKubiManagerDelegate) {
        this.mDelegate = iKubiManagerDelegate;
    }

    public Boolean getAutoFind() {
        return Boolean.valueOf(this.mAutoFind);
    }

    public void setAutoFind(Boolean bool) {
        this.mAutoFind = bool.booleanValue();
    }

    @NonNull
    public ArrayList<KubiSearchResult> getKubiList() {
        return this.mKubiList;
    }

    public int getBluetoothDiscoveryTime() {
        return this.mBluetoothDiscoveryTime;
    }

    public void setBluetoothDiscoveryTime(int i) {
        this.mBluetoothDiscoveryTime = i;
    }

    public int getFailure() {
        return this.mFailure;
    }

    public int getStatus() {
        return this.mStatus;
    }

    @Nullable
    public Kubi getKubi() {
        return this.connectedKubi;
    }

    public KubiManager(Context context, IKubiManagerDelegate iKubiManagerDelegate) {
        this.mContext = context;
        this.mDelegate = iKubiManagerDelegate;
        this.mHandler = new Handler();
        startScanning();
    }

    public KubiManager(Context context, IKubiManagerDelegate iKubiManagerDelegate, boolean z) {
        this.mContext = context;
        this.mDelegate = iKubiManagerDelegate;
        this.mAutoFind = z;
        this.mHandler = new Handler();
        startScanning();
    }

    public void disconnect() {
        if (this.connectedKubi != null) {
            setStatus(1);
            this.connectedKubi.disconnect();
        }
    }

    public void connectToKubi(@NonNull KubiSearchResult kubiSearchResult) {
        Kubi kubi = this.connectedKubi;
        if (kubi != null) {
            this.connectedKubi = null;
            kubi.disconnect();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Connecting to kubi with ID ");
        sb.append(kubiSearchResult.getName());
        Log.i("Kubi Manager", sb.toString());
        this.connectDevice = kubiSearchResult.getDevice();
        setStatus(3);
        this.mHandler.post(new Runnable() {
            public void run() {
                KubiManager.this.connectKubi();
            }
        });
    }

    public void findKubi(int i) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                KubiManager.this.findKubi();
            }
        }, (long) i);
    }

    public void findKubi() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Log.i("Kubi SDK: findKubi", "Bluetooth not available. Cannot connect to Kubi.");
            sendFail(3);
            setStatus(0);
            return;
        }
        int i = this.mStatus;
        if (i == 0 || i == 2) {
            this.mFailure = 0;
            if (this.adapter == null) {
                this.adapter = BluetoothAdapter.getDefaultAdapter();
            }
            startScan(false);
            setStatus(2);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.mContext.getSystemService(Param.LOCATION);
        if (locationManager == null) {
            return false;
        }
        return locationManager.isProviderEnabled("gps");
    }

    public void stopFinding() {
        this.cancelScan = true;
        this.mHandler.removeCallbacks(this.fullScanFinish);
        this.mHandler.removeCallbacks(this.findFinish);
        if (VERSION.SDK_INT < 23 || isLocationEnabled()) {
            this.adapter.stopLeScan(this);
        } else if (this.adapter.isDiscovering()) {
            this.adapter.cancelDiscovery();
            try {
                this.mContext.unregisterReceiver(this.bluetoothBroadcastReceiver);
            } catch (IllegalArgumentException unused) {
            }
        }
        setStatus(0);
    }

    public void findAllKubis() {
        this.mFailure = 0;
        if (this.adapter == null) {
            this.adapter = BluetoothAdapter.getDefaultAdapter();
        }
        startScan(true);
    }

    private void setStatus(final int i) {
        final int i2 = this.mStatus;
        if (i != i2) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    KubiManager.this.notifyChangeStatus(i2, i);
                }
            });
            this.mStatus = i;
        }
    }

    private void sendFail(final int i) {
        if (this.mFailure == 0) {
            this.mFailure = i;
            this.mHandler.post(new Runnable() {
                public void run() {
                    KubiManager.this.notifyFailure(i);
                }
            });
        }
    }

    public void onKubiReady(@NonNull Kubi kubi) {
        if (kubi == this.connectedKubi) {
            setStatus(4);
        } else {
            kubi.disconnect();
        }
    }

    public void onKubiDisconnect(Kubi kubi) {
        if (kubi != this.connectedKubi) {
            return;
        }
        if (this.mStatus != 1) {
            sendFail(1);
            setStatus(5);
            return;
        }
        this.connectedKubi = null;
        setStatus(0);
    }

    public void onKubiUpdateRSSI(@NonNull Kubi kubi, int i) {
        if (kubi == this.connectedKubi && i < -100 && this.mAutoDisconnect) {
            sendFail(2);
            kubi.disconnect();
        }
    }

    private void startScanning() {
        this.adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter = this.adapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            if (this.mDelegate != null) {
                sendFail(3);
                setStatus(0);
            }
        } else if (this.mAutoFind) {
            findKubi(0);
        }
    }

    private void startScan(boolean z) {
        BluetoothAdapter bluetoothAdapter = this.adapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            sendFail(3);
            return;
        }
        this.cancelScan = false;
        if (z) {
            if (VERSION.SDK_INT < 23 || isLocationEnabled()) {
                this.mHandler.postDelayed(this.fullScanFinish, 2000);
            } else {
                this.mHandler.postDelayed(this.fullScanFinish, (long) getBluetoothDiscoveryTime());
            }
        } else if (VERSION.SDK_INT < 23 || isLocationEnabled()) {
            this.mHandler.postDelayed(this.findFinish, 2000);
        } else {
            this.mHandler.postDelayed(this.findFinish, (long) getBluetoothDiscoveryTime());
        }
        new Thread(new Runnable() {
            public void run() {
                KubiManager.this.doScan();
            }
        }).start();
        this.nearKubis.clear();
        this.foundMACs.clear();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:31:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void finishScan(boolean r4) {
        /*
            r3 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r0 < r1) goto L_0x0023
            boolean r0 = r3.isLocationEnabled()
            if (r0 != 0) goto L_0x0023
            android.bluetooth.BluetoothAdapter r0 = r3.adapter
            boolean r0 = r0.isDiscovering()
            if (r0 == 0) goto L_0x0028
            android.bluetooth.BluetoothAdapter r0 = r3.adapter
            r0.cancelDiscovery()
            android.content.Context r0 = r3.mContext     // Catch:{ IllegalArgumentException -> 0x0021 }
            android.content.BroadcastReceiver r1 = r3.bluetoothBroadcastReceiver     // Catch:{ IllegalArgumentException -> 0x0021 }
            r0.unregisterReceiver(r1)     // Catch:{ IllegalArgumentException -> 0x0021 }
            goto L_0x0028
        L_0x0021:
            goto L_0x0028
        L_0x0023:
            android.bluetooth.BluetoothAdapter r0 = r3.adapter
            r0.stopLeScan(r3)
        L_0x0028:
            boolean r0 = r3.cancelScan
            if (r0 != 0) goto L_0x0084
            java.util.ArrayList<com.revolverobotics.kubisdk.KubiSearchResult> r0 = r3.nearKubis
            com.revolverobotics.kubisdk.KubiManager$8 r1 = new com.revolverobotics.kubisdk.KubiManager$8
            r1.<init>()
            java.util.Collections.sort(r0, r1)
            java.util.ArrayList r0 = new java.util.ArrayList
            java.util.ArrayList<com.revolverobotics.kubisdk.KubiSearchResult> r1 = r3.nearKubis
            r0.<init>(r1)
            r3.mKubiList = r0
            if (r4 != 0) goto L_0x007a
            java.util.ArrayList<com.revolverobotics.kubisdk.KubiSearchResult> r4 = r3.nearKubis
            int r4 = r4.size()
            r0 = 0
            if (r4 <= 0) goto L_0x006b
            java.util.ArrayList<com.revolverobotics.kubisdk.KubiSearchResult> r4 = r3.mKubiList
            java.lang.Object r4 = r4.get(r0)
            com.revolverobotics.kubisdk.KubiSearchResult r4 = (com.revolverobotics.kubisdk.KubiSearchResult) r4
            if (r4 == 0) goto L_0x006b
            int r1 = r4.getRSSI()
            r2 = -80
            if (r1 <= r2) goto L_0x006b
            android.os.Handler r1 = r3.mHandler
            com.revolverobotics.kubisdk.KubiManager$9 r2 = new com.revolverobotics.kubisdk.KubiManager$9
            r2.<init>(r4)
            r1.post(r2)
            r3.setStatus(r0)
            r4 = 1
            goto L_0x006c
        L_0x006b:
            r4 = 0
        L_0x006c:
            if (r4 != 0) goto L_0x0084
            boolean r4 = r3.mAutoFind
            if (r4 == 0) goto L_0x0076
            r3.findKubi(r0)
            goto L_0x0084
        L_0x0076:
            r3.setStatus(r0)
            goto L_0x0084
        L_0x007a:
            android.os.Handler r4 = r3.mHandler
            com.revolverobotics.kubisdk.KubiManager$10 r0 = new com.revolverobotics.kubisdk.KubiManager$10
            r0.<init>()
            r4.post(r0)
        L_0x0084:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.revolverobotics.kubisdk.KubiManager.finishScan(boolean):void");
    }

    /* access modifiers changed from: private */
    public void doScan() {
        boolean z;
        if (!this.cancelScan) {
            if (VERSION.SDK_INT < 23 || isLocationEnabled()) {
                z = this.adapter.startLeScan(this);
            } else {
                if (this.adapter.isDiscovering()) {
                    this.adapter.cancelDiscovery();
                    try {
                        this.mContext.unregisterReceiver(this.bluetoothBroadcastReceiver);
                    } catch (IllegalArgumentException unused) {
                    }
                }
                this.bluetoothBroadcastReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, @NonNull Intent intent) {
                        String action = intent.getAction();
                        if ("android.bluetooth.device.action.FOUND".equals(action)) {
                            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                            short shortExtra = intent.getShortExtra("android.bluetooth.device.extra.RSSI", Short.MIN_VALUE);
                            if (bluetoothDevice != null && !KubiManager.this.foundMACs.contains(bluetoothDevice.getAddress())) {
                                KubiManager.this.foundMACs.add(bluetoothDevice.getAddress());
                                try {
                                    String substring = bluetoothDevice.getName().substring(0, 4);
                                    if (substring.equals("kubi") || substring.equals("Rev-")) {
                                        KubiManager.this.nearKubis.add(new KubiSearchResult(bluetoothDevice, shortExtra));
                                        if (KubiManager.this.mStatus == 2 && shortExtra > -80) {
                                            KubiManager.this.mHandler.removeCallbacks(KubiManager.this.findFinish);
                                            KubiManager.this.mHandler.post(KubiManager.this.findFinish);
                                        }
                                    }
                                } catch (Exception unused) {
                                }
                            }
                        }
                        "android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action);
                    }
                };
                IntentFilter intentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
                intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
                this.mContext.registerReceiver(this.bluetoothBroadcastReceiver, intentFilter);
                z = this.adapter.startDiscovery();
            }
            if (!z) {
                sendFail(4);
            }
        }
    }

    /* access modifiers changed from: private */
    public void connectKubi() {
        BluetoothDevice bluetoothDevice = this.connectDevice;
        if (bluetoothDevice != null) {
            this.connectedKubi = new Kubi(this.mContext, this, bluetoothDevice);
        }
    }

    /* access modifiers changed from: private */
    public void notifyChangeStatus(int i, int i2) {
        IKubiManagerDelegate iKubiManagerDelegate = this.mDelegate;
        if (iKubiManagerDelegate != null) {
            iKubiManagerDelegate.kubiManagerStatusChanged(this, i, i2);
        }
    }

    /* access modifiers changed from: private */
    public void notifyFailure(int i) {
        IKubiManagerDelegate iKubiManagerDelegate = this.mDelegate;
        if (iKubiManagerDelegate != null) {
            iKubiManagerDelegate.kubiManagerFailed(this, i);
        }
    }

    /* access modifiers changed from: private */
    public void notifyKubiDeviceFound(KubiSearchResult kubiSearchResult) {
        IKubiManagerDelegate iKubiManagerDelegate = this.mDelegate;
        if (iKubiManagerDelegate != null) {
            iKubiManagerDelegate.kubiDeviceFound(this, kubiSearchResult);
        }
    }

    /* access modifiers changed from: private */
    public void notifyScanComplete(ArrayList<KubiSearchResult> arrayList) {
        IKubiManagerDelegate iKubiManagerDelegate = this.mDelegate;
        if (iKubiManagerDelegate != null) {
            iKubiManagerDelegate.kubiScanComplete(this, arrayList);
        }
    }

    public void onLeScan(@NonNull BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        if (!this.foundMACs.contains(bluetoothDevice.getAddress())) {
            this.foundMACs.add(bluetoothDevice.getAddress());
            try {
                String substring = bluetoothDevice.getName().substring(0, 4);
                if (substring.equals("kubi") || substring.equals("Rev-")) {
                    this.nearKubis.add(new KubiSearchResult(bluetoothDevice, i));
                }
            } catch (Exception unused) {
            }
        }
    }
}
