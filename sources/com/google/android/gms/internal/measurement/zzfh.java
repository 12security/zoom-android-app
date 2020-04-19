package com.google.android.gms.internal.measurement;

import java.io.IOException;

public class zzfh extends IOException {
    private zzgh zzahn = null;

    public zzfh(String str) {
        super(str);
    }

    public final zzfh zzg(zzgh zzgh) {
        this.zzahn = zzgh;
        return this;
    }

    static zzfh zzmu() {
        return new zzfh("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    static zzfh zzmv() {
        return new zzfh("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    static zzfh zzmw() {
        return new zzfh("CodedInputStream encountered a malformed varint.");
    }

    static zzfh zzmx() {
        return new zzfh("Protocol message contained an invalid tag (zero).");
    }

    static zzfh zzmy() {
        return new zzfh("Protocol message end-group tag did not match expected tag.");
    }

    static zzfi zzmz() {
        return new zzfi("Protocol message tag had invalid wire type.");
    }

    static zzfh zzna() {
        return new zzfh("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    static zzfh zznb() {
        return new zzfh("Failed to parse the message.");
    }

    static zzfh zznc() {
        return new zzfh("Protocol message had invalid UTF-8.");
    }
}
