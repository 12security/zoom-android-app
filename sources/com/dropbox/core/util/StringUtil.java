package com.dropbox.core.util;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class StringUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String Base64Digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final char[] HexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String UrlSafeBase64Digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    public static char hexDigit(int i) {
        return HexDigits[i];
    }

    public static String utf8ToString(byte[] bArr) throws CharacterCodingException {
        return utf8ToString(bArr, 0, bArr.length);
    }

    public static String utf8ToString(byte[] bArr, int i, int i2) throws CharacterCodingException {
        return UTF8.newDecoder().decode(ByteBuffer.wrap(bArr, i, i2)).toString();
    }

    public static byte[] stringToUtf8(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw LangUtil.mkAssert("UTF-8 should always be supported", e);
        }
    }

    public static String javaQuotedLiteral(String str) {
        StringBuilder sb = new StringBuilder(str.length() * 2);
        sb.append('\"');
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == 0) {
                sb.append("\\000");
            } else if (charAt == 13) {
                sb.append("\\t");
            } else if (charAt == '\"') {
                sb.append("\\\"");
            } else if (charAt != '\\') {
                switch (charAt) {
                    case 9:
                        sb.append("\\r");
                        break;
                    case 10:
                        sb.append("\\n");
                        break;
                    default:
                        if (charAt >= ' ' && charAt <= '~') {
                            sb.append(charAt);
                            break;
                        } else {
                            int i2 = (charAt >> 12) & 15;
                            int i3 = (charAt >> 8) & 15;
                            int i4 = (charAt >> 4) & 15;
                            char c = charAt & 15;
                            sb.append("\\u");
                            sb.append(hexDigit(i2));
                            sb.append(hexDigit(i3));
                            sb.append(hexDigit(i4));
                            sb.append(hexDigit(c));
                            break;
                        }
                }
            } else {
                sb.append("\\\\");
            }
        }
        sb.append('\"');
        return sb.toString();
    }

    /* renamed from: jq */
    public static String m33jq(String str) {
        return javaQuotedLiteral(str);
    }

    public static String binaryToHex(byte[] bArr) {
        return binaryToHex(bArr, 0, bArr.length);
    }

    public static String binaryToHex(byte[] bArr, int i, int i2) {
        int i3 = i + i2;
        char[] cArr = new char[(i2 * 2)];
        int i4 = 0;
        while (i < i3) {
            byte b = bArr[i];
            int i5 = i4 + 1;
            cArr[i4] = hexDigit((b >>> 4) & 15);
            i4 = i5 + 1;
            cArr[i5] = hexDigit(b & Ascii.f228SI);
            i++;
        }
        return new String(cArr);
    }

    public static boolean secureStringEquals(String str, String str2) {
        boolean z = false;
        if (str.length() != str2.length()) {
            return false;
        }
        char c = 0;
        for (int i = 0; i < str.length(); i++) {
            c |= str.charAt(i) ^ str2.charAt(i);
        }
        if (c == 0) {
            z = true;
        }
        return z;
    }

    public static String base64Encode(byte[] bArr) {
        return base64EncodeGeneric(Base64Digits, bArr);
    }

    public static String urlSafeBase64Encode(byte[] bArr) {
        return base64EncodeGeneric(UrlSafeBase64Digits, bArr);
    }

    public static String base64EncodeGeneric(String str, byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("'data' can't be null");
        } else if (str == null) {
            throw new IllegalArgumentException("'digits' can't be null");
        } else if (str.length() == 64) {
            StringBuilder sb = new StringBuilder(((bArr.length + 2) / 3) * 4);
            int i = 0;
            while (i + 3 <= bArr.length) {
                int i2 = i + 1;
                byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
                int i3 = i2 + 1;
                byte b2 = bArr[i2] & UnsignedBytes.MAX_VALUE;
                int i4 = i3 + 1;
                byte b3 = bArr[i3] & UnsignedBytes.MAX_VALUE;
                int i5 = b >>> 2;
                int i6 = ((b & 3) << 4) | (b2 >>> 4);
                int i7 = ((b2 & Ascii.f228SI) << 2) | (b3 >>> 6);
                byte b4 = b3 & 63;
                sb.append(str.charAt(i5));
                sb.append(str.charAt(i6));
                sb.append(str.charAt(i7));
                sb.append(str.charAt(b4));
                i = i4;
            }
            int length = bArr.length - i;
            if (length != 0) {
                if (length == 1) {
                    byte b5 = bArr[i] & UnsignedBytes.MAX_VALUE;
                    int i8 = b5 >>> 2;
                    int i9 = (b5 & 3) << 4;
                    sb.append(str.charAt(i8));
                    sb.append(str.charAt(i9));
                    sb.append("==");
                } else if (length == 2) {
                    int i10 = i + 1;
                    byte b6 = bArr[i] & UnsignedBytes.MAX_VALUE;
                    byte b7 = bArr[i10] & UnsignedBytes.MAX_VALUE;
                    int i11 = b6 >>> 2;
                    int i12 = ((b6 & 3) << 4) | (b7 >>> 4);
                    int i13 = (b7 & Ascii.f228SI) << 2;
                    sb.append(str.charAt(i11));
                    sb.append(str.charAt(i12));
                    sb.append(str.charAt(i13));
                    sb.append('=');
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("data.length: ");
                    sb2.append(bArr.length);
                    sb2.append(", i: ");
                    sb2.append(i);
                    throw new AssertionError(sb2.toString());
                }
            }
            return sb.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("'digits' must be 64 characters long: ");
            sb3.append(m33jq(str));
            throw new IllegalArgumentException(sb3.toString());
        }
    }
}
