package com.google.firebase.iid;

final class zzy implements InstanceIdResult {
    private final String zzbw;
    private final String zzbx;

    zzy(String str, String str2) {
        this.zzbw = str;
        this.zzbx = str2;
    }

    public final String getId() {
        return this.zzbw;
    }

    public final String getToken() {
        return this.zzbx;
    }
}
