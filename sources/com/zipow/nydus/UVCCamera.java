package com.zipow.nydus;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.USBMonitor.UsbControlBlock;

public class UVCCamera {
    private static final String DEFAULT_USBFS = "/dev/bus/usb";
    private static final String TAG = "UVCCamera";
    private static boolean isLoaded;
    @Nullable
    private UsbControlBlock mCtrlBlock;
    private final int mDeviceId;
    private long mNativePtr;
    private final int mProductId;
    private final int mVendorId;

    private static final native int nativeConnect(long j, int i, int i2, int i3, String str);

    private static final native int nativeStart(long j);

    private static final native int nativeStop(long j);

    static {
        boolean z = isLoaded;
    }

    public void setNativePtr(long j) {
        this.mNativePtr = j;
    }

    public UVCCamera(int i, int i2, int i3) {
        this.mDeviceId = i;
        this.mVendorId = i2;
        this.mProductId = i3;
    }

    @NonNull
    public Boolean IsSameCamera(@NonNull UsbControlBlock usbControlBlock) {
        if (this.mDeviceId == usbControlBlock.getDeviceId() && this.mVendorId == usbControlBlock.getVendorId() && this.mProductId == usbControlBlock.getProductId()) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    @NonNull
    public Boolean IsSameCamera(@NonNull USBDeviceInfo uSBDeviceInfo) {
        if (this.mDeviceId == uSBDeviceInfo.deviceId && this.mVendorId == uSBDeviceInfo.vendorId && this.mProductId == uSBDeviceInfo.productId) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    @NonNull
    public Boolean IsSameCamera(int i, int i2, int i3) {
        if (this.mDeviceId == i && this.mVendorId == i2 && this.mProductId == i3) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    @NonNull
    private final String getUSBFSName(UsbControlBlock usbControlBlock) {
        String deviceName = usbControlBlock.getDeviceName();
        String str = null;
        String[] split = !TextUtils.isEmpty(deviceName) ? deviceName.split("/") : null;
        if (split != null && split.length > 2) {
            StringBuilder sb = new StringBuilder(split[0]);
            for (int i = 1; i < split.length - 2; i++) {
                sb.append("/");
                sb.append(split[i]);
            }
            str = sb.toString();
        }
        return TextUtils.isEmpty(str) ? DEFAULT_USBFS : str;
    }

    @NonNull
    public Boolean open(UsbControlBlock usbControlBlock) {
        this.mCtrlBlock = usbControlBlock;
        if (nativeConnect(this.mNativePtr, this.mCtrlBlock.getVendorId(), this.mCtrlBlock.getProductId(), this.mCtrlBlock.getFileDescriptor(), getUSBFSName(this.mCtrlBlock)) == 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public void close() {
        this.mNativePtr = 0;
        this.mCtrlBlock = null;
    }

    public void destroy() {
        close();
    }

    @NonNull
    public Boolean StartRecord() {
        if (this.mCtrlBlock != null) {
            long j = this.mNativePtr;
            if (j != 0) {
                if (nativeStart(j) == 0) {
                    return Boolean.valueOf(true);
                }
                return Boolean.valueOf(false);
            }
        }
        return Boolean.valueOf(false);
    }

    public void StopRecord() {
        if (this.mCtrlBlock != null) {
            long j = this.mNativePtr;
            if (j != 0) {
                nativeStop(j);
            }
        }
    }
}
