package com.zipow.videobox.confapp;

public class MediaDevice {
    private String mDeviceId;
    private String mDeviceName;
    private boolean mSelectedDevice;

    public MediaDevice(String str, String str2, boolean z) {
        this.mDeviceId = str;
        this.mDeviceName = str2;
        this.mSelectedDevice = z;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public void setDeviceId(String str) {
        this.mDeviceId = str;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public void setDeviceName(String str) {
        this.mDeviceName = str;
    }

    public boolean isSelectedDevice() {
        return this.mSelectedDevice;
    }

    public void setSelectedDevice(boolean z) {
        this.mSelectedDevice = z;
    }
}
