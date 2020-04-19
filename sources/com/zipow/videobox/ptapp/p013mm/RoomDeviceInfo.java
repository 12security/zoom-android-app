package com.zipow.videobox.ptapp.p013mm;

import java.io.Serializable;

/* renamed from: com.zipow.videobox.ptapp.mm.RoomDeviceInfo */
public class RoomDeviceInfo implements Serializable {
    private int mDeviceType = 1;
    private String mE164num;
    private int mEncrypt = 2;
    private String mIp;
    private String mName;

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getIp() {
        return this.mIp;
    }

    public void setIp(String str) {
        this.mIp = str;
    }

    public String getE164num() {
        return this.mE164num;
    }

    public void setE164num(String str) {
        this.mE164num = str;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }

    public void setDeviceType(int i) {
        this.mDeviceType = i;
    }

    public int getEncrypt() {
        return this.mEncrypt;
    }

    public void setEncrypt(int i) {
        this.mEncrypt = i;
    }
}
