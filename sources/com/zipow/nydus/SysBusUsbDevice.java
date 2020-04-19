package com.zipow.nydus;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SysBusUsbDevice implements Parcelable {
    public static final Creator<SysBusUsbDevice> CREATOR = new Creator<SysBusUsbDevice>() {
        public SysBusUsbDevice createFromParcel(@NonNull Parcel parcel) {
            return new SysBusUsbDevice(parcel);
        }

        public SysBusUsbDevice[] newArray(int i) {
            return new SysBusUsbDevice[i];
        }
    };
    @Nullable
    private String BusNumber;
    @Nullable
    private String DeviceClass;
    @Nullable
    private String DeviceNumber;
    @Nullable
    private String DevicePath;
    @Nullable
    private String DeviceProtocol;
    @Nullable
    private String DeviceSubClass;
    @Nullable
    private String MaxPower;
    @Nullable
    private String PID;
    @Nullable
    private String ReportedProductName;
    @Nullable
    private String ReportedVendorName;
    @Nullable
    private String SerialNumber;
    @Nullable
    private String Speed;
    @Nullable
    private String UsbVersion;
    @Nullable
    private String VID;

    public int describeContents() {
        return 0;
    }

    public SysBusUsbDevice() {
    }

    public SysBusUsbDevice(Parcel parcel) {
        this.VID = parcel.readString();
        this.PID = parcel.readString();
        this.ReportedProductName = parcel.readString();
        this.ReportedVendorName = parcel.readString();
        this.SerialNumber = parcel.readString();
        this.Speed = parcel.readString();
        this.DeviceClass = parcel.readString();
        this.DeviceProtocol = parcel.readString();
        this.MaxPower = parcel.readString();
        this.DeviceSubClass = parcel.readString();
        this.BusNumber = parcel.readString();
        this.DeviceNumber = parcel.readString();
        this.UsbVersion = parcel.readString();
        this.DevicePath = parcel.readString();
    }

    @Nullable
    public String getBusNumber() {
        return this.BusNumber;
    }

    @Nullable
    public String getDeviceClass() {
        return this.DeviceClass;
    }

    @Nullable
    public String getDeviceNumber() {
        return this.DeviceNumber;
    }

    @Nullable
    public String getDevicePath() {
        return this.DevicePath;
    }

    @Nullable
    public String getDeviceProtocol() {
        return this.DeviceProtocol;
    }

    @Nullable
    public String getDeviceSubClass() {
        return this.DeviceSubClass;
    }

    @Nullable
    public String getMaxPower() {
        return this.MaxPower;
    }

    @Nullable
    public String getPID() {
        return this.PID;
    }

    @Nullable
    public String getReportedProductName() {
        return this.ReportedProductName;
    }

    @Nullable
    public String getReportedVendorName() {
        return this.ReportedVendorName;
    }

    @Nullable
    public String getSerialNumber() {
        return this.SerialNumber;
    }

    @Nullable
    public String getSpeed() {
        return this.Speed;
    }

    @Nullable
    public String getUsbVersion() {
        return this.UsbVersion;
    }

    @Nullable
    public String getVID() {
        return this.VID;
    }

    public void setBusNumber(@Nullable String str) {
        this.BusNumber = str;
    }

    public void setDeviceClass(@Nullable String str) {
        this.DeviceClass = str;
    }

    public void setDeviceNumber(@Nullable String str) {
        this.DeviceNumber = str;
    }

    public void setDevicePath(@Nullable String str) {
        this.DevicePath = str;
    }

    public void setDeviceProtocol(@Nullable String str) {
        this.DeviceProtocol = str;
    }

    public void setDeviceSubClass(@Nullable String str) {
        this.DeviceSubClass = str;
    }

    public void setMaxPower(@Nullable String str) {
        this.MaxPower = str;
    }

    public void setPID(@Nullable String str) {
        this.PID = str;
    }

    public void setReportedProductName(@Nullable String str) {
        this.ReportedProductName = str;
    }

    public void setReportedVendorName(@Nullable String str) {
        this.ReportedVendorName = str;
    }

    public void setSerialNumber(@Nullable String str) {
        this.SerialNumber = str;
    }

    public void setSpeed(@Nullable String str) {
        this.Speed = str;
    }

    public void setUsbVersion(@Nullable String str) {
        this.UsbVersion = str;
    }

    public void setVID(@Nullable String str) {
        this.VID = str;
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.VID);
        parcel.writeString(this.PID);
        parcel.writeString(this.ReportedProductName);
        parcel.writeString(this.ReportedVendorName);
        parcel.writeString(this.SerialNumber);
        parcel.writeString(this.Speed);
        parcel.writeString(this.DeviceClass);
        parcel.writeString(this.DeviceProtocol);
        parcel.writeString(this.MaxPower);
        parcel.writeString(this.DeviceSubClass);
        parcel.writeString(this.BusNumber);
        parcel.writeString(this.DeviceNumber);
        parcel.writeString(this.UsbVersion);
        parcel.writeString(this.DevicePath);
    }
}
