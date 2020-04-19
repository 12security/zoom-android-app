package com.zipow.nydus;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.kubi.KubiContract;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;

public class USBMonitor {
    private static final String ACTION_USB_PERMISSION = "com.zoomus.USB_PERMISSION";
    private static final String TAG = "USBMonitor";
    /* access modifiers changed from: private */
    public final HashMap<UsbDevice, UsbControlBlock> mCtrlBlocks = new HashMap<>();
    /* access modifiers changed from: private */
    public final OnDeviceConnectListener mOnDeviceConnectListener;
    @Nullable
    private PendingIntent mPermissionIntent;
    /* access modifiers changed from: private */
    @Nullable
    public final UsbManager mUsbManager;
    @Nullable
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            UsbControlBlock usbControlBlock;
            String action = intent.getAction();
            if (USBMonitor.ACTION_USB_PERMISSION.equals(action)) {
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE);
                if (intent.getBooleanExtra("permission", false) && usbDevice != null) {
                    USBMonitor.this.processConnect(usbDevice);
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                if (USBMonitor.this.mOnDeviceConnectListener != null) {
                    USBMonitor.this.mOnDeviceConnectListener.onAttach((UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE));
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE);
                if (usbDevice2 != null) {
                    synchronized (USBMonitor.this.mCtrlBlocks) {
                        usbControlBlock = (UsbControlBlock) USBMonitor.this.mCtrlBlocks.remove(usbDevice2);
                    }
                    if (usbControlBlock != null) {
                        usbControlBlock.close();
                    }
                    if (USBMonitor.this.mOnDeviceConnectListener != null) {
                        USBMonitor.this.mOnDeviceConnectListener.onDettach(usbDevice2);
                    }
                }
            }
        }
    };
    @NonNull
    private final WeakReference<Context> mWeakContext;

    public interface OnDeviceConnectListener {
        void onAttach(UsbDevice usbDevice);

        boolean onConnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock, boolean z);

        void onDettach(UsbDevice usbDevice);

        void onDisconnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock);
    }

    @SuppressLint({"NewApi"})
    public final class UsbControlBlock {
        @Nullable
        private UsbDeviceConnection mConnection;
        private final SparseArray<UsbInterface> mInterfaces = new SparseArray<>();
        @NonNull
        private final WeakReference<UsbDevice> mWeakDevice;
        @NonNull
        private final WeakReference<USBMonitor> mWeakMonitor;

        public UsbControlBlock(USBMonitor uSBMonitor, UsbDevice usbDevice) {
            this.mWeakMonitor = new WeakReference<>(uSBMonitor);
            this.mWeakDevice = new WeakReference<>(usbDevice);
            this.mConnection = USBMonitor.this.mUsbManager.openDevice(usbDevice);
        }

        public UsbDevice getDevice() {
            return (UsbDevice) this.mWeakDevice.get();
        }

        public String getDeviceName() {
            UsbDevice usbDevice = (UsbDevice) this.mWeakDevice.get();
            return usbDevice != null ? usbDevice.getDeviceName() : "";
        }

        @Nullable
        public UsbDeviceConnection getUsbDeviceConnection() {
            return this.mConnection;
        }

        public int getFileDescriptor() {
            UsbDeviceConnection usbDeviceConnection = this.mConnection;
            if (usbDeviceConnection != null) {
                return usbDeviceConnection.getFileDescriptor();
            }
            return 0;
        }

        public byte[] getRawDescriptors() {
            UsbDeviceConnection usbDeviceConnection = this.mConnection;
            if (usbDeviceConnection != null) {
                return usbDeviceConnection.getRawDescriptors();
            }
            return null;
        }

        public int getDeviceId() {
            UsbDevice usbDevice = (UsbDevice) this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getDeviceId();
            }
            return 0;
        }

        public int getVendorId() {
            UsbDevice usbDevice = (UsbDevice) this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getVendorId();
            }
            return 0;
        }

        public int getProductId() {
            UsbDevice usbDevice = (UsbDevice) this.mWeakDevice.get();
            if (usbDevice != null) {
                return usbDevice.getProductId();
            }
            return 0;
        }

        @SuppressLint({"NewApi"})
        public void close(int i) {
            synchronized (this.mInterfaces) {
                UsbInterface usbInterface = (UsbInterface) this.mInterfaces.get(i);
                if (usbInterface != null) {
                    this.mInterfaces.delete(i);
                    this.mConnection.releaseInterface(usbInterface);
                }
            }
        }

        @SuppressLint({"NewApi"})
        public void close() {
            if (this.mConnection != null) {
                USBMonitor uSBMonitor = (USBMonitor) this.mWeakMonitor.get();
                if (uSBMonitor != null) {
                    UsbDevice usbDevice = (UsbDevice) this.mWeakDevice.get();
                    if (USBMonitor.this.mOnDeviceConnectListener != null) {
                        USBMonitor.this.mOnDeviceConnectListener.onDisconnect(usbDevice, this);
                    }
                    uSBMonitor.mCtrlBlocks.remove(usbDevice);
                }
                synchronized (this.mInterfaces) {
                    int size = this.mInterfaces.size();
                    for (int i = 0; i < size; i++) {
                        this.mConnection.releaseInterface((UsbInterface) this.mInterfaces.get(this.mInterfaces.keyAt(i)));
                    }
                }
                this.mConnection.close();
                this.mConnection = null;
            }
        }
    }

    public USBMonitor(@NonNull Context context, OnDeviceConnectListener onDeviceConnectListener) {
        this.mWeakContext = new WeakReference<>(context);
        this.mUsbManager = (UsbManager) context.getSystemService("usb");
        this.mOnDeviceConnectListener = onDeviceConnectListener;
    }

    public void destroy() {
        unregister();
        synchronized (this.mCtrlBlocks) {
            Set<UsbDevice> keySet = this.mCtrlBlocks.keySet();
            if (!CollectionsUtil.isCollectionEmpty(keySet)) {
                for (UsbDevice remove : keySet) {
                    UsbControlBlock usbControlBlock = (UsbControlBlock) this.mCtrlBlocks.remove(remove);
                    if (usbControlBlock != null) {
                        usbControlBlock.close();
                    }
                }
                this.mCtrlBlocks.clear();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void closeDevice(int i) {
        synchronized (this.mCtrlBlocks) {
            Set<UsbDevice> keySet = this.mCtrlBlocks.keySet();
            if (!CollectionsUtil.isCollectionEmpty(keySet)) {
                for (UsbDevice usbDevice : keySet) {
                    if (i == usbDevice.getDeviceId()) {
                        UsbControlBlock usbControlBlock = (UsbControlBlock) this.mCtrlBlocks.remove(usbDevice);
                        if (usbControlBlock != null) {
                            usbControlBlock.close();
                        }
                    }
                }
            }
        }
    }

    public void register() {
        unregister();
        Context context = (Context) this.mWeakContext.get();
        if (context != null) {
            this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            context.registerReceiver(this.mUsbReceiver, intentFilter);
        }
    }

    public void unregister() {
        if (this.mPermissionIntent != null) {
            Context context = (Context) this.mWeakContext.get();
            if (context != null) {
                context.unregisterReceiver(this.mUsbReceiver);
            }
            this.mPermissionIntent = null;
        }
    }

    @Nullable
    public List<UsbDevice> getDeviceList() {
        return getDeviceList(null);
    }

    @Nullable
    public List<UsbDevice> getDeviceList(@Nullable DeviceFilter deviceFilter) {
        HashMap usbDeviceList = getUsbDeviceList(this.mUsbManager);
        Context context = (Context) this.mWeakContext.get();
        if (usbDeviceList == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (UsbDevice usbDevice : usbDeviceList.values()) {
            if (deviceFilter == null || deviceFilter.matches(context, usbDevice)) {
                arrayList.add(usbDevice);
            }
        }
        return arrayList;
    }

    @Nullable
    public Iterator<UsbDevice> getDevices() {
        HashMap usbDeviceList = getUsbDeviceList(this.mUsbManager);
        if (usbDeviceList != null) {
            return usbDeviceList.values().iterator();
        }
        return null;
    }

    public void dumpDevices() {
        HashMap usbDeviceList = getUsbDeviceList(this.mUsbManager);
        if (usbDeviceList != null) {
            Set<String> keySet = usbDeviceList.keySet();
            if (!CollectionsUtil.isCollectionEmpty(keySet)) {
                for (String str : keySet) {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("key=");
                    sb.append(str);
                    sb.append(":");
                    sb.append(usbDeviceList.get(str));
                    Log.i(str2, sb.toString());
                }
                return;
            }
            Log.i(TAG, "no device");
            return;
        }
        Log.i(TAG, "no device");
    }

    public boolean hasPermission(UsbDevice usbDevice) {
        return this.mUsbManager.hasPermission(usbDevice);
    }

    @SuppressLint({"NewApi"})
    public boolean requestPermission(@Nullable UsbDevice usbDevice) {
        if (usbDevice != null) {
            if (this.mPermissionIntent != null && !this.mUsbManager.hasPermission(usbDevice)) {
                this.mUsbManager.requestPermission(usbDevice, this.mPermissionIntent);
            } else if (this.mUsbManager.hasPermission(usbDevice)) {
                return processConnect(usbDevice);
            }
        }
        return true;
    }

    @SuppressLint({"NewApi"})
    @Nullable
    private HashMap<String, UsbDevice> getUsbDeviceList(@NonNull UsbManager usbManager) {
        try {
            return usbManager.getDeviceList();
        } catch (Exception unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public final boolean processConnect(UsbDevice usbDevice) {
        UsbControlBlock usbControlBlock;
        boolean z;
        synchronized (this.mCtrlBlocks) {
            usbControlBlock = (UsbControlBlock) this.mCtrlBlocks.get(usbDevice);
            if (usbControlBlock == null) {
                usbControlBlock = new UsbControlBlock(this, usbDevice);
                this.mCtrlBlocks.put(usbDevice, usbControlBlock);
                z = true;
            } else {
                z = false;
            }
        }
        OnDeviceConnectListener onDeviceConnectListener = this.mOnDeviceConnectListener;
        if (onDeviceConnectListener != null) {
            return onDeviceConnectListener.onConnect(usbDevice, usbControlBlock, z);
        }
        return false;
    }
}
