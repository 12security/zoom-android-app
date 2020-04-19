package org.apache.commons.codec.binary;

import com.google.common.base.Ascii;
import java.nio.charset.Charset;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex implements BinaryEncoder, BinaryDecoder {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final Charset charset;

    public static byte[] decodeHex(char[] cArr) throws DecoderException {
        int length = cArr.length;
        if ((length & 1) == 0) {
            byte[] bArr = new byte[(length >> 1)];
            int i = 0;
            int i2 = 0;
            while (i < length) {
                int i3 = i + 1;
                i = i3 + 1;
                bArr[i2] = (byte) (((toDigit(cArr[i], i) << 4) | toDigit(cArr[i3], i3)) & 255);
                i2++;
            }
            return bArr;
        }
        throw new DecoderException("Odd number of characters.");
    }

    public static char[] encodeHex(byte[] bArr) {
        return encodeHex(bArr, true);
    }

    public static char[] encodeHex(byte[] bArr, boolean z) {
        return encodeHex(bArr, z ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[(length << 1)];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr2[i] = cArr[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr2[i3] = cArr[bArr[i2] & Ascii.f228SI];
        }
        return cArr2;
    }

    public static String encodeHexString(byte[] bArr) {
        return new String(encodeHex(bArr));
    }

    protected static int toDigit(char c, int i) throws DecoderException {
        int digit = Character.digit(c, 16);
        if (digit != -1) {
            return digit;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Illegal hexadecimal character ");
        sb.append(c);
        sb.append(" at index ");
        sb.append(i);
        throw new DecoderException(sb.toString());
    }

    public Hex() {
        this.charset = DEFAULT_CHARSET;
    }

    public Hex(Charset charset2) {
        this.charset = charset2;
    }

    public Hex(String str) {
        this(Charset.forName(str));
    }

    public byte[] decode(byte[] bArr) throws DecoderException {
        return decodeHex(new String(bArr, getCharset()).toCharArray());
    }

    public Object decode(Object obj) throws DecoderException {
        try {
            return decodeHex(obj instanceof String ? ((String) obj).toCharArray() : (char[]) obj);
        } catch (ClassCastException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public byte[] encode(byte[] bArr) {
        return encodeHexString(bArr).getBytes(getCharset());
    }

    public Object encode(Object obj) throws EncoderException {
        try {
            return encodeHex(obj instanceof String ? ((String) obj).getBytes(getCharset()) : (byte[]) obj);
        } catch (ClassCastException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getCharsetName() {
        return this.charset.name();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("[charsetName=");
        sb.append(this.charset);
        sb.append("]");
        return sb.toString();
    }
}
