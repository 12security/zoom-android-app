package p021us.zoom.androidlib.util;

import java.security.SecureRandom;

/* renamed from: us.zoom.androidlib.util.ZMSecureRandom */
public class ZMSecureRandom {
    public static int nextInt(int i) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[32]);
        return secureRandom.nextInt(i);
    }

    public static double nextDouble() {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[32]);
        return secureRandom.nextDouble();
    }

    public static float nextFloatAround(float f, float f2) {
        return nextFloatInRange(f - f2, f + f2);
    }

    public static float nextFloatInRange(float f, float f2) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[32]);
        return f + ((f2 - f) * secureRandom.nextFloat());
    }

    public static int nextIntInRange(int i, int i2) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[32]);
        return i + secureRandom.nextInt(i2 - i);
    }
}
