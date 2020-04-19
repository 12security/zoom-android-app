package com.fasterxml.jackson.core.p006io;

import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.util.Arrays;
import org.apache.http.message.TokenParser;

/* renamed from: com.fasterxml.jackson.core.io.CharTypes */
public final class CharTypes {

    /* renamed from: HB */
    private static final byte[] f168HB;

    /* renamed from: HC */
    private static final char[] f169HC = "0123456789ABCDEF".toCharArray();
    private static final int[] sHexValues = new int[128];
    private static final int[] sInputCodes;
    private static final int[] sInputCodesComment;
    private static final int[] sInputCodesJsNames;
    private static final int[] sInputCodesUTF8;
    private static final int[] sInputCodesUtf8JsNames;
    private static final int[] sInputCodesWS;
    private static final int[] sOutputEscapes128;

    static {
        int length = f169HC.length;
        f168HB = new byte[length];
        for (int i = 0; i < length; i++) {
            f168HB[i] = (byte) f169HC[i];
        }
        int[] iArr = new int[256];
        for (int i2 = 0; i2 < 32; i2++) {
            iArr[i2] = -1;
        }
        iArr[34] = 1;
        iArr[92] = 1;
        sInputCodes = iArr;
        int[] iArr2 = sInputCodes;
        int[] iArr3 = new int[iArr2.length];
        System.arraycopy(iArr2, 0, iArr3, 0, iArr3.length);
        for (int i3 = 128; i3 < 256; i3++) {
            int i4 = (i3 & DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession) == 192 ? 2 : (i3 & DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP) == 224 ? 3 : (i3 & 248) == 240 ? 4 : -1;
            iArr3[i3] = i4;
        }
        sInputCodesUTF8 = iArr3;
        int[] iArr4 = new int[256];
        Arrays.fill(iArr4, -1);
        for (int i5 = 33; i5 < 256; i5++) {
            if (Character.isJavaIdentifierPart((char) i5)) {
                iArr4[i5] = 0;
            }
        }
        iArr4[64] = 0;
        iArr4[35] = 0;
        iArr4[42] = 0;
        iArr4[45] = 0;
        iArr4[43] = 0;
        sInputCodesJsNames = iArr4;
        int[] iArr5 = new int[256];
        System.arraycopy(sInputCodesJsNames, 0, iArr5, 0, iArr5.length);
        Arrays.fill(iArr5, 128, 128, 0);
        sInputCodesUtf8JsNames = iArr5;
        int[] iArr6 = new int[256];
        System.arraycopy(sInputCodesUTF8, 128, iArr6, 128, 128);
        Arrays.fill(iArr6, 0, 32, -1);
        iArr6[9] = 0;
        iArr6[10] = 10;
        iArr6[13] = 13;
        iArr6[42] = 42;
        sInputCodesComment = iArr6;
        int[] iArr7 = new int[256];
        System.arraycopy(sInputCodesUTF8, 128, iArr7, 128, 128);
        Arrays.fill(iArr7, 0, 32, -1);
        iArr7[32] = 1;
        iArr7[9] = 1;
        iArr7[10] = 10;
        iArr7[13] = 13;
        iArr7[47] = 47;
        iArr7[35] = 35;
        sInputCodesWS = iArr7;
        int[] iArr8 = new int[128];
        for (int i6 = 0; i6 < 32; i6++) {
            iArr8[i6] = -1;
        }
        iArr8[34] = 34;
        iArr8[92] = 92;
        iArr8[8] = 98;
        iArr8[9] = 116;
        iArr8[12] = 102;
        iArr8[10] = 110;
        iArr8[13] = 114;
        sOutputEscapes128 = iArr8;
        Arrays.fill(sHexValues, -1);
        for (int i7 = 0; i7 < 10; i7++) {
            sHexValues[i7 + 48] = i7;
        }
        for (int i8 = 0; i8 < 6; i8++) {
            int[] iArr9 = sHexValues;
            int i9 = i8 + 10;
            iArr9[i8 + 97] = i9;
            iArr9[i8 + 65] = i9;
        }
    }

    public static int[] getInputCodeLatin1() {
        return sInputCodes;
    }

    public static int[] getInputCodeUtf8() {
        return sInputCodesUTF8;
    }

    public static int[] getInputCodeLatin1JsNames() {
        return sInputCodesJsNames;
    }

    public static int[] getInputCodeUtf8JsNames() {
        return sInputCodesUtf8JsNames;
    }

    public static int[] getInputCodeComment() {
        return sInputCodesComment;
    }

    public static int[] getInputCodeWS() {
        return sInputCodesWS;
    }

    public static int[] get7BitOutputEscapes() {
        return sOutputEscapes128;
    }

    public static int charToHex(int i) {
        if (i > 127) {
            return -1;
        }
        return sHexValues[i];
    }

    public static void appendQuoted(StringBuilder sb, String str) {
        int[] iArr = sOutputEscapes128;
        int length = iArr.length;
        int length2 = str.length();
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            if (charAt >= length || iArr[charAt] == 0) {
                sb.append(charAt);
            } else {
                sb.append(TokenParser.ESCAPE);
                int i2 = iArr[charAt];
                if (i2 < 0) {
                    sb.append('u');
                    sb.append('0');
                    sb.append('0');
                    sb.append(f169HC[charAt >> 4]);
                    sb.append(f169HC[charAt & 15]);
                } else {
                    sb.append((char) i2);
                }
            }
        }
    }

    public static char[] copyHexChars() {
        return (char[]) f169HC.clone();
    }

    public static byte[] copyHexBytes() {
        return (byte[]) f168HB.clone();
    }
}
