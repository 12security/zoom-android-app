package com.google.protobuf;

import com.google.common.primitives.UnsignedBytes;
import java.io.UnsupportedEncodingException;

public class Internal {

    public interface EnumLite {
        int getNumber();
    }

    public interface EnumLiteMap<T extends EnumLite> {
        T findValueByNumber(int i);
    }

    public static String stringDefaultValue(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }

    public static ByteString bytesDefaultValue(String str) {
        try {
            return ByteString.copyFrom(str.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Java VM does not support a standard character set.", e);
        }
    }

    public static boolean isValidUtf8(ByteString byteString) {
        int size = byteString.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            byte byteAt = byteString.byteAt(i) & UnsignedBytes.MAX_VALUE;
            if (byteAt < 128) {
                i = i2;
            } else if (byteAt < 194 || byteAt > 244 || i2 >= size) {
                return false;
            } else {
                int i3 = i2 + 1;
                byte byteAt2 = byteString.byteAt(i2) & UnsignedBytes.MAX_VALUE;
                if (byteAt2 < 128 || byteAt2 > 191) {
                    return false;
                }
                if (byteAt <= 223) {
                    i = i3;
                } else if (i3 >= size) {
                    return false;
                } else {
                    int i4 = i3 + 1;
                    byte byteAt3 = byteString.byteAt(i3) & UnsignedBytes.MAX_VALUE;
                    if (byteAt3 < 128 || byteAt3 > 191) {
                        return false;
                    }
                    if (byteAt <= 239) {
                        if ((byteAt == 224 && byteAt2 < 160) || (byteAt == 237 && byteAt2 > 159)) {
                            return false;
                        }
                        i = i4;
                    } else if (i4 >= size) {
                        return false;
                    } else {
                        int i5 = i4 + 1;
                        byte byteAt4 = byteString.byteAt(i4) & UnsignedBytes.MAX_VALUE;
                        if (byteAt4 < 128 || byteAt4 > 191) {
                            return false;
                        }
                        if ((byteAt == 240 && byteAt2 < 144) || (byteAt == 244 && byteAt2 > 143)) {
                            return false;
                        }
                        i = i5;
                    }
                }
            }
        }
        return true;
    }
}
