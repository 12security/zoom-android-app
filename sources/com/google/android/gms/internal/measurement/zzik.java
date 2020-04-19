package com.google.android.gms.internal.measurement;

public enum zzik {
    INT(Integer.valueOf(0)),
    LONG(Long.valueOf(0)),
    FLOAT(Float.valueOf(0.0f)),
    DOUBLE(Double.valueOf(0.0d)),
    BOOLEAN(Boolean.valueOf(false)),
    STRING(""),
    BYTE_STRING(zzdp.zzaby),
    ENUM(null),
    MESSAGE(null);
    
    private final Object zzaia;

    private zzik(Object obj) {
        this.zzaia = obj;
    }
}
