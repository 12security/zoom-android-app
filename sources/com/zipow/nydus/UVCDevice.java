package com.zipow.nydus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.USBMonitor.OnDeviceConnectListener;
import com.zipow.nydus.USBMonitor.UsbControlBlock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class UVCDevice {
    private static final String TAG = "UVCDevice";
    @Nullable
    private static UVCDevice instance;
    @Nullable
    private DeviceFilter mFilter = null;
    /* access modifiers changed from: private */
    @NonNull
    public Object mLockCamera = new Object();
    /* access modifiers changed from: private */
    public long mNotificationNativePtr = 0;
    @SuppressLint({"NewApi"})
    private final OnDeviceConnectListener mOnDeviceConnectListener = new OnDeviceConnectListener() {
        public void onDisconnect(UsbDevice usbDevice, UsbControlBlock usbControlBlock) {
        }

        public void onAttach(@NonNull UsbDevice usbDevice) {
            if (UVCDevice.this.mNotificationNativePtr != 0) {
                UVCDevice uVCDevice = UVCDevice.this;
                uVCDevice.nativeDeviceAttach(uVCDevice.mNotificationNativePtr, usbDevice.getDeviceId(), usbDevice.getVendorId(), usbDevice.getProductId(), 1);
            }
        }

        public boolean onConnect(@NonNull UsbDevice usbDevice, @NonNull UsbControlBlock usbControlBlock, boolean z) {
            boolean z2;
            synchronized (UVCDevice.this.mLockCamera) {
                Iterator it = UVCDevice.this.mUVCCamera.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    UVCCamera uVCCamera = (UVCCamera) it.next();
                    if (uVCCamera.IsSameCamera(usbControlBlock).booleanValue()) {
                        if (true == uVCCamera.open(usbControlBlock).booleanValue()) {
                            z2 = uVCCamera.StartRecord().booleanValue();
                        }
                    }
                }
                z2 = false;
            }
            return z2;
        }

        public void onDettach(@NonNull UsbDevice usbDevice) {
            if (UVCDevice.this.mNotificationNativePtr != 0) {
                UVCDevice uVCDevice = UVCDevice.this;
                uVCDevice.nativeDeviceAttach(uVCDevice.mNotificationNativePtr, usbDevice.getDeviceId(), usbDevice.getVendorId(), usbDevice.getProductId(), 0);
            }
        }
    };
    @NonNull
    private USBMonitor mUSBMonitor;
    /* access modifiers changed from: private */
    @NonNull
    public final List<UVCCamera> mUVCCamera;

    /* access modifiers changed from: private */
    public final native void nativeDeviceAttach(long j, int i, int i2, int i3, int i4);

    public static synchronized UVCDevice getInstance(@NonNull Context context) {
        synchronized (UVCDevice.class) {
            if (VERSION.SDK_INT < 17) {
                return null;
            }
            if (instance == null) {
                instance = new UVCDevice(context.getApplicationContext());
            }
            UVCDevice uVCDevice = instance;
            return uVCDevice;
        }
    }

    private UVCDevice(@NonNull Context context) {
        this.mUSBMonitor = new USBMonitor(context, this.mOnDeviceConnectListener);
        this.mFilter = DeviceFilter.buildUVCDeviceFilter();
        this.mUVCCamera = new ArrayList();
        this.mUSBMonitor.register();
    }

    @SuppressLint({"NewApi"})
    @NonNull
    public USBDeviceInfo[] getUVCDeviceList() {
        List deviceList = this.mUSBMonitor.getDeviceList(this.mFilter);
        int i = 0;
        if (deviceList == null) {
            return new USBDeviceInfo[0];
        }
        USBDeviceInfo[] uSBDeviceInfoArr = new USBDeviceInfo[deviceList.size()];
        Iterator it = deviceList.iterator();
        if (VERSION.SDK_INT >= 21) {
            TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            while (it.hasNext()) {
                UsbDevice usbDevice = (UsbDevice) it.next();
                if (usbDevice != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(usbDevice.getProductName());
                    sb.append(usbDevice.getDeviceId());
                    treeMap.put(sb.toString(), usbDevice);
                }
            }
            for (String str : treeMap.keySet()) {
                UsbDevice usbDevice2 = (UsbDevice) treeMap.get(str);
                if (usbDevice2 != null) {
                    USBDeviceInfo uSBDeviceInfo = new USBDeviceInfo();
                    uSBDeviceInfo.deviceId = usbDevice2.getDeviceId();
                    uSBDeviceInfo.vendorId = usbDevice2.getVendorId();
                    uSBDeviceInfo.productId = usbDevice2.getProductId();
                    uSBDeviceInfoArr[i] = uSBDeviceInfo;
                    i++;
                }
            }
        } else {
            while (it.hasNext()) {
                UsbDevice usbDevice3 = (UsbDevice) it.next();
                if (usbDevice3 != null) {
                    USBDeviceInfo uSBDeviceInfo2 = new USBDeviceInfo();
                    uSBDeviceInfo2.deviceId = usbDevice3.getDeviceId();
                    uSBDeviceInfo2.vendorId = usbDevice3.getVendorId();
                    uSBDeviceInfo2.productId = usbDevice3.getProductId();
                    uSBDeviceInfoArr[i] = uSBDeviceInfo2;
                    i++;
                }
            }
        }
        return uSBDeviceInfoArr;
    }

    @SuppressLint({"NewApi"})
    @Nullable
    public String GetDeviceProductName(int i) {
        List<UsbDevice> deviceList = this.mUSBMonitor.getDeviceList(this.mFilter);
        if (deviceList == null) {
            return "";
        }
        for (UsbDevice usbDevice : deviceList) {
            if (i == usbDevice.getDeviceId() && VERSION.SDK_INT >= 21) {
                String productName = usbDevice.getProductName();
                return productName != null ? productName : "";
            }
        }
        return "";
    }

    @SuppressLint({"NewApi"})
    public boolean StartCapture(int i, int i2, int i3, long j) {
        Iterator it = this.mUVCCamera.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            UVCCamera uVCCamera = (UVCCamera) it.next();
            if (uVCCamera.IsSameCamera(i, i2, i3).booleanValue()) {
                uVCCamera.StopRecord();
                uVCCamera.destroy();
                it.remove();
                break;
            }
        }
        for (UsbDevice usbDevice : this.mUSBMonitor.getDeviceList(this.mFilter)) {
            if (usbDevice != null && i == usbDevice.getDeviceId() && i2 == usbDevice.getVendorId() && i3 == usbDevice.getProductId()) {
                UVCCamera uVCCamera2 = new UVCCamera(i, i2, i3);
                uVCCamera2.setNativePtr(j);
                this.mUVCCamera.add(uVCCamera2);
                return this.mUSBMonitor.requestPermission(usbDevice);
            }
        }
        return false;
    }

    public boolean StopCapture(int i, int i2, int i3) {
        synchronized (this.mLockCamera) {
            Iterator it = this.mUVCCamera.iterator();
            while (it.hasNext()) {
                UVCCamera uVCCamera = (UVCCamera) it.next();
                if (uVCCamera.IsSameCamera(i, i2, i3).booleanValue()) {
                    this.mUSBMonitor.closeDevice(i);
                    uVCCamera.StopRecord();
                    uVCCamera.destroy();
                    it.remove();
                }
            }
        }
        return true;
    }

    public boolean SetNotificationNativePtr(long j) {
        this.mNotificationNativePtr = j;
        return true;
    }
}
