package com.zipow.videobox.ptapp;

public class ZoomProductHelper {
    private static final String TAG = "ZoomProductHelper";
    private long mNativeHandle = 0;

    private final native int getCurrentVendorImpl(long j);

    private native void initCurrentLocaleImpl(long j, int i);

    private native void setCurrentVendorImpl(long j, int i);

    private native void setDeviceJailBreakImpl(long j, boolean z);

    private final native void vendorSwitchToImpl(long j, int i);

    public ZoomProductHelper(long j) {
        this.mNativeHandle = j;
    }

    public void vendorSwitchTo(int i) {
        long j = this.mNativeHandle;
        if (j != 0) {
            vendorSwitchToImpl(j, i);
        }
    }

    public int getCurrentVendor() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCurrentVendorImpl(j);
    }

    public void setCurrentVendor(int i) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setCurrentVendorImpl(j, i);
        }
    }

    public void initCurrentLocale(int i) {
        long j = this.mNativeHandle;
        if (j != 0) {
            initCurrentLocaleImpl(j, i);
        }
    }

    public void setDeviceJailBreak(boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setDeviceJailBreakImpl(j, z);
        }
    }
}
