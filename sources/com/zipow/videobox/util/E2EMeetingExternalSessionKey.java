package com.zipow.videobox.util;

import java.io.Serializable;

public final class E2EMeetingExternalSessionKey implements Serializable {
    private static final long serialVersionUID = 1;
    public final byte[] external_secure_iv;
    public final byte[] external_secure_key;

    public E2EMeetingExternalSessionKey(byte[] bArr, byte[] bArr2) {
        this.external_secure_key = bArr;
        this.external_secure_iv = bArr2;
    }
}
