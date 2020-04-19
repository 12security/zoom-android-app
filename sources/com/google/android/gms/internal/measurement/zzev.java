package com.google.android.gms.internal.measurement;

enum zzev {
    SCALAR(false),
    VECTOR(true),
    PACKED_VECTOR(true),
    MAP(false);
    
    private final boolean zzagh;

    private zzev(boolean z) {
        this.zzagh = z;
    }
}
