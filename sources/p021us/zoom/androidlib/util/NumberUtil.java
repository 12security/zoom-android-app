package p021us.zoom.androidlib.util;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;

/* renamed from: us.zoom.androidlib.util.NumberUtil */
public class NumberUtil {
    public static byte[] intToByteArray(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 24) & 255)};
    }

    public static int byteArrayToInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | (bArr[i] & UnsignedBytes.MAX_VALUE) | ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i + 2] & UnsignedBytes.MAX_VALUE) << Ascii.DLE) | 0;
    }
}
