package com.google.android.gms.internal.measurement;

public enum zzif {
    DOUBLE(zzik.DOUBLE, 1),
    FLOAT(zzik.FLOAT, 5),
    INT64(zzik.LONG, 0),
    UINT64(zzik.LONG, 0),
    INT32(zzik.INT, 0),
    FIXED64(zzik.LONG, 1),
    FIXED32(zzik.INT, 5),
    BOOL(zzik.BOOLEAN, 0),
    STRING(zzik.STRING, 2),
    GROUP(zzik.MESSAGE, 3),
    MESSAGE(zzik.MESSAGE, 2),
    BYTES(zzik.BYTE_STRING, 2),
    UINT32(zzik.INT, 0),
    ENUM(zzik.ENUM, 0),
    SFIXED32(zzik.INT, 5),
    SFIXED64(zzik.LONG, 1),
    SINT32(zzik.INT, 0),
    SINT64(zzik.LONG, 0);
    
    private final zzik zzamj;
    private final int zzamk;

    private zzif(zzik zzik, int i) {
        this.zzamj = zzik;
        this.zzamk = i;
    }

    public final zzik zzpb() {
        return this.zzamj;
    }

    public final int zzpc() {
        return this.zzamk;
    }
}
