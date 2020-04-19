package com.zipow.nydus;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.UVCUtil.IUVCListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UVCPermissionUtil {
    private static final String TAG = UVCUtil.class.getSimpleName();
    @Nullable
    private static UVCPermissionUtil instance;
    private Context mContext;
    /* access modifiers changed from: private */
    @NonNull
    public HashSet<UsbDevice> mNewDevices = new HashSet<>();
    @NonNull
    private IUVCListener mUVCListener = new IUVCListener() {
        public void onPermissionGranted(UsbDevice usbDevice, boolean z) {
            UVCPermissionUtil.this.mNewDevices.remove(usbDevice);
        }

        public void onDeviceAttached(UsbDevice usbDevice) {
            UVCPermissionUtil.this.mNewDevices.add(usbDevice);
        }

        public void onDeviceDetatched(UsbDevice usbDevice) {
            UVCPermissionUtil.this.mNewDevices.remove(usbDevice);
        }
    };
    @Nullable
    private UVCUtil mUVCUtil;

    public static synchronized UVCPermissionUtil getInstance(@Nullable Context context) {
        synchronized (UVCPermissionUtil.class) {
            if (VERSION.SDK_INT < UVCUtil.getMinimumSupportedSdkInt()) {
                return null;
            }
            if (instance == null && context != null) {
                instance = new UVCPermissionUtil(context);
            }
            UVCPermissionUtil uVCPermissionUtil = instance;
            return uVCPermissionUtil;
        }
    }

    private UVCPermissionUtil(Context context) {
        this.mContext = context.getApplicationContext();
        this.mUVCUtil = UVCUtil.getInstance(this.mContext);
        UVCUtil uVCUtil = this.mUVCUtil;
        if (uVCUtil != null) {
            uVCUtil.addUVCListener(this.mUVCListener);
            this.mNewDevices.addAll(this.mUVCUtil.getUVCDevices());
        }
    }

    public void addUVCListener(IUVCListener iUVCListener) {
        this.mUVCUtil.addUVCListener(iUVCListener);
    }

    public void removeUVCListener(IUVCListener iUVCListener) {
        this.mUVCUtil.removeUVCListener(iUVCListener);
    }

    @NonNull
    public Set<UsbDevice> getDevicesWithPermissionNotRequested() {
        List uVCDevices = this.mUVCUtil.getUVCDevices();
        Iterator it = this.mNewDevices.iterator();
        while (it.hasNext()) {
            UsbDevice usbDevice = (UsbDevice) it.next();
            if (!uVCDevices.contains(usbDevice) || this.mUVCUtil.hasPermission(usbDevice)) {
                it.remove();
            }
        }
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.mNewDevices);
        return hashSet;
    }

    public void requestPermission(UsbDevice usbDevice) {
        this.mUVCUtil.requestPermission(usbDevice);
        this.mNewDevices.remove(usbDevice);
    }
}
