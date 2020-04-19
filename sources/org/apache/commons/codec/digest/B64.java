package org.apache.commons.codec.digest;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.util.Random;

class B64 {
    static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    B64() {
    }

    static void b64from24bit(byte b, byte b2, byte b3, int i, StringBuilder sb) {
        int i2 = ((b << Ascii.DLE) & UnsignedBytes.MAX_VALUE) | ((b2 << 8) & UnsignedBytes.MAX_VALUE) | (b3 & UnsignedBytes.MAX_VALUE);
        while (true) {
            int i3 = i - 1;
            if (i > 0) {
                sb.append(B64T.charAt(i2 & 63));
                i2 >>= 6;
                i = i3;
            } else {
                return;
            }
        }
    }

    static String getRandomSalt(int i) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 1; i2 <= i; i2++) {
            sb.append(B64T.charAt(new Random().nextInt(64)));
        }
        return sb.toString();
    }
}
