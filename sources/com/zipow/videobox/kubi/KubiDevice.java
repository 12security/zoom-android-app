package com.zipow.videobox.kubi;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.revolverobotics.kubisdk.KubiSearchResult;
import p021us.zoom.androidlib.util.StringUtil;

public class KubiDevice implements Parcelable {
    public static final Creator<KubiDevice> CREATOR = new Creator<KubiDevice>() {
        public KubiDevice createFromParcel(@NonNull Parcel parcel) {
            return new KubiDevice(parcel);
        }

        public KubiDevice[] newArray(int i) {
            return new KubiDevice[i];
        }
    };
    @Nullable
    private BluetoothDevice mBtDevice;
    private int mRSSI;

    public int describeContents() {
        return 0;
    }

    @Nullable
    public static KubiDevice fromKubiSearchResult(@Nullable KubiSearchResult kubiSearchResult) {
        if (kubiSearchResult == null) {
            return null;
        }
        BluetoothDevice device = kubiSearchResult.getDevice();
        if (device == null) {
            return null;
        }
        return new KubiDevice(device, kubiSearchResult.getRSSI());
    }

    public KubiDevice() {
        this.mBtDevice = null;
        this.mRSSI = 0;
    }

    private KubiDevice(@NonNull Parcel parcel) {
        this.mBtDevice = null;
        this.mRSSI = 0;
        readFromParcel(parcel);
    }

    public KubiDevice(@Nullable BluetoothDevice bluetoothDevice, int i) {
        this.mBtDevice = null;
        this.mRSSI = 0;
        this.mBtDevice = bluetoothDevice;
        this.mRSSI = i;
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(this.mBtDevice, 0);
        parcel.writeInt(this.mRSSI);
    }

    private void readFromParcel(Parcel parcel) {
        this.mBtDevice = (BluetoothDevice) parcel.readParcelable(getClass().getClassLoader());
        this.mRSSI = parcel.readInt();
    }

    @Nullable
    public BluetoothDevice getBluetoothDevice() {
        return this.mBtDevice;
    }

    public int getRSSI() {
        return this.mRSSI;
    }

    @Nullable
    public String getName() {
        BluetoothDevice bluetoothDevice = this.mBtDevice;
        if (bluetoothDevice == null) {
            return null;
        }
        return bluetoothDevice.getName();
    }

    @Nullable
    public String getMac() {
        BluetoothDevice bluetoothDevice = this.mBtDevice;
        if (bluetoothDevice == null) {
            return null;
        }
        return bluetoothDevice.getAddress();
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof KubiDevice)) {
            return false;
        }
        KubiDevice kubiDevice = (KubiDevice) obj;
        if (StringUtil.isSameString(getMac(), kubiDevice.getMac()) && StringUtil.isSameString(getName(), kubiDevice.getName())) {
            z = true;
        }
        return z;
    }
}
