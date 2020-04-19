package com.revolverobotics.kubisdk;

import android.bluetooth.BluetoothDevice;

public class KubiSearchResult {
    BluetoothDevice mDevice;
    int mRSSI;

    public KubiSearchResult(BluetoothDevice bluetoothDevice, int i) {
        this.mDevice = bluetoothDevice;
        this.mRSSI = i;
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public int getRSSI() {
        return this.mRSSI;
    }

    public String getName() {
        return this.mDevice.getName();
    }

    public String getMac() {
        return this.mDevice.getAddress();
    }
}
