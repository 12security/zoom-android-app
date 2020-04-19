package com.zipow.videobox.confapp;

public class InviteRoomDeviceInfo {
    private String e164num;
    private int encrypted_type = 2;

    /* renamed from: ip */
    private String f310ip;
    private String name;
    private int type = 1;

    public InviteRoomDeviceInfo() {
    }

    public InviteRoomDeviceInfo(String str, String str2, String str3, int i, int i2) {
        this.name = str;
        this.f310ip = str2;
        this.e164num = str3;
        this.type = i;
        this.encrypted_type = i2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getIp() {
        return this.f310ip;
    }

    public void setIp(String str) {
        this.f310ip = str;
    }

    public String getE164num() {
        return this.e164num;
    }

    public void setE164num(String str) {
        this.e164num = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public int getEncrypted_type() {
        return this.encrypted_type;
    }

    public void setEncrypted_type(int i) {
        this.encrypted_type = i;
    }
}
