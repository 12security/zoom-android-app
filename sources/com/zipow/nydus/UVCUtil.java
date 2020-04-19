package com.zipow.nydus;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.kubi.KubiContract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;

@SuppressLint({"NewApi"})
public class UVCUtil {
    private static final String ACTION_USB_PERMISSION = "us.zoom.videomeetings.USB_PERMISSION_REQUEST_RESULT";
    private static final String TAG = "UVCUtil";
    @Nullable
    private static UVCUtil instance;
    private Context mContext;
    private DeviceFilter mFilter;
    @NonNull
    private ListenerList mListeners = new ListenerList();
    private PendingIntent mPermissionIntent;
    @Nullable
    private UsbManager mUsbManager;
    @Nullable
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UVCUtil.ACTION_USB_PERMISSION.equals(action)) {
                UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE);
                boolean booleanExtra = intent.getBooleanExtra("permission", false);
                if (usbDevice != null) {
                    UVCUtil.this.processPermissionGranted(usbDevice, booleanExtra);
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE);
                if (usbDevice2 != null) {
                    UVCUtil.this.processDeviceAttached(usbDevice2);
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                UsbDevice usbDevice3 = (UsbDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE);
                if (usbDevice3 != null) {
                    UVCUtil.this.processDeviceDettached(usbDevice3);
                }
            }
        }
    };

    public interface IUVCListener extends IListener {
        void onDeviceAttached(UsbDevice usbDevice);

        void onDeviceDetatched(UsbDevice usbDevice);

        void onPermissionGranted(UsbDevice usbDevice, boolean z);
    }

    public static class SimpleUVCListener implements IUVCListener {
        public void onDeviceAttached(UsbDevice usbDevice) {
        }

        public void onDeviceDetatched(UsbDevice usbDevice) {
        }

        public void onPermissionGranted(UsbDevice usbDevice, boolean z) {
        }
    }

    public static int getMinimumSupportedSdkInt() {
        return 17;
    }

    public static synchronized UVCUtil getInstance(@Nullable Context context) {
        synchronized (UVCUtil.class) {
            if (VERSION.SDK_INT < getMinimumSupportedSdkInt()) {
                return null;
            }
            if (instance == null && context != null) {
                instance = new UVCUtil(context);
            }
            UVCUtil uVCUtil = instance;
            return uVCUtil;
        }
    }

    private UVCUtil(Context context) {
        this.mContext = context.getApplicationContext();
        this.mUsbManager = getUsbManager(context);
        this.mPermissionIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        this.mFilter = DeviceFilter.buildUVCDeviceFilter();
        registerReceiver();
    }

    public void addUVCListener(IUVCListener iUVCListener) {
        this.mListeners.add(iUVCListener);
    }

    public void removeUVCListener(IUVCListener iUVCListener) {
        this.mListeners.remove(iUVCListener);
    }

    @NonNull
    public List<UsbDevice> getUVCDevices() {
        ArrayList arrayList = new ArrayList();
        UsbManager usbManager = this.mUsbManager;
        if (usbManager == null) {
            return arrayList;
        }
        HashMap hashMap = null;
        try {
            hashMap = usbManager.getDeviceList();
        } catch (Exception unused) {
        }
        if (hashMap != null) {
            for (UsbDevice usbDevice : hashMap.values()) {
                if (usbDevice != null && this.mFilter.matches(this.mContext, usbDevice)) {
                    arrayList.add(usbDevice);
                }
            }
        }
        return arrayList;
    }

    public boolean hasPermission(UsbDevice usbDevice) {
        UsbManager usbManager = this.mUsbManager;
        if (usbManager == null) {
            return false;
        }
        try {
            return usbManager.hasPermission(usbDevice);
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean requestPermission(UsbDevice usbDevice) {
        UsbManager usbManager = this.mUsbManager;
        if (usbManager == null) {
            return false;
        }
        try {
            usbManager.requestPermission(usbDevice, this.mPermissionIntent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.mContext.registerReceiver(this.mUsbReceiver, intentFilter);
    }

    /* access modifiers changed from: private */
    public void processDeviceDettached(@NonNull UsbDevice usbDevice) {
        if (this.mFilter.matches(this.mContext, usbDevice)) {
            for (IListener iListener : this.mListeners.getAll()) {
                ((IUVCListener) iListener).onDeviceDetatched(usbDevice);
            }
        }
    }

    /* access modifiers changed from: private */
    public void processDeviceAttached(@NonNull UsbDevice usbDevice) {
        if (this.mFilter.matches(this.mContext, usbDevice)) {
            for (IListener iListener : this.mListeners.getAll()) {
                ((IUVCListener) iListener).onDeviceAttached(usbDevice);
            }
        }
    }

    /* access modifiers changed from: private */
    public void processPermissionGranted(@NonNull UsbDevice usbDevice, boolean z) {
        if (this.mFilter.matches(this.mContext, usbDevice)) {
            for (IListener iListener : this.mListeners.getAll()) {
                ((IUVCListener) iListener).onPermissionGranted(usbDevice, z);
            }
        }
    }

    @Nullable
    private UsbManager getUsbManager(@Nullable Context context) {
        if (context == null) {
            return null;
        }
        try {
            return (UsbManager) context.getSystemService("usb");
        } catch (Exception unused) {
            return null;
        }
    }
}
