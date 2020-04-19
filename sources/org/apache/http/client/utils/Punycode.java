package org.apache.http.client.utils;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class Punycode {
    private static final Idn impl;

    static {
        Idn idn;
        try {
            idn = new JdkIdn();
        } catch (Exception unused) {
            idn = new Rfc3492Idn();
        }
        impl = idn;
    }

    public static String toUnicode(String str) {
        return impl.toUnicode(str);
    }
}
