package com.google.api.client.testing.util;

import com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper;
import com.google.api.client.util.Beta;
import com.google.api.client.util.SecurityUtils;
import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Beta
public final class SecurityTestUtils {
    private static final byte[] ENCODED_PRIVATE_KEY = {48, -126, 2, 118, 2, 1, 0, 48, Ascii.f219CR, 6, 9, 42, -122, 72, -122, -9, Ascii.f219CR, 1, 1, 1, 5, 0, 4, -126, 2, 96, 48, -126, 2, 92, 2, 1, 0, 2, -127, -127, 0, -89, 33, 8, -124, 110, -60, 89, 8, -62, 69, 120, 95, -59, -43, Ascii.f219CR, -18, 123, Ascii.f223GS, -31, Ascii.f219CR, -80, -76, 109, -62, -79, 2, 104, -94, 76, 59, -73, -26, 99, 123, -57, -92, -100, 116, 50, -25, 96, 53, 124, 95, 76, -59, -84, 70, Ascii.ESC, 0, 72, -63, 84, -77, -2, -107, -66, -32, -119, Ascii.ESC, -95, 54, -44, -89, 1, 71, 44, 7, -55, 126, 5, -78, 87, -105, -114, 65, -19, 58, -78, -95, 0, 118, 83, 76, -88, 2, -21, Ascii.DEL, SignedBytes.MAX_POWER_OF_TWO, 74, -103, -114, -127, -70, -81, -127, 125, -37, Ascii.NAK, 113, Ascii.DC4, -102, 46, -37, -111, -97, 97, -127, 32, 87, -80, 105, Ascii.DC2, -19, 107, -73, -50, -97, Ascii.f232VT, -23, -59, -107, -107, 83, -25, Ascii.f228SI, -93, -21, 2, 3, 1, 0, 1, 2, -127, UnsignedBytes.MAX_POWER_OF_TWO, 45, -34, -104, Ascii.SUB, -40, -41, -44, -29, -35, -123, -7, -110, -73, -106, 80, -5, -118, Ascii.CAN, -38, 66, -54, -93, -54, -104, 43, -62, -48, 122, -14, -41, 85, Ascii.DC2, -53, 109, Ascii.SYN, -113, 44, 77, -116, 7, 10, -43, -61, 43, -40, -61, 76, 19, -11, -89, 47, 80, -72, 113, -86, 70, -23, Ascii.ESC, 113, 37, -1, 42, 48, 84, -80, Ascii.f227RS, 86, 36, -124, -22, 79, -44, 87, -40, Ascii.f231US, -41, -44, -16, -74, 85, 61, -122, -22, 10, -31, 78, 92, -123, -77, Ascii.f221FF, -80, 62, -52, 68, -46, ByteSourceJsonBootstrapper.UTF8_BOM_1, 67, 124, -78, -23, -105, -77, -2, 89, -16, -12, -56, -51, Ascii.SUB, 102, 46, 39, -61, -13, -79, ByteSourceJsonBootstrapper.UTF8_BOM_3, -5, 126, 70, Ascii.f223GS, Ascii.f231US, 104, -109, 65, -23, ByteSourceJsonBootstrapper.UTF8_BOM_2, Ascii.ETB, -7, 2, 65, 0, -42, Ascii.DC2, 101, 10, -21, 37, 107, -3, -114, -29, -40, 76, 107, -122, 40, 8, -58, -32, -12, 55, -4, -61, -66, 91, -56, -50, 78, -124, Ascii.f232VT, -49, -62, -121, -56, 70, -92, 90, 32, -112, 49, Ascii.SUB, -99, 113, 44, Ascii.SUB, 42, -99, -40, -123, 17, 93, 114, 125, 35, -118, -32, 125, -64, 61, 58, -58, -105, -105, -39, 93, 2, 65, 0, -57, -36, -22, -107, -42, -79, 0, -118, 121, -76, 120, 52, 110, Ascii.DEL, 115, 68, -86, -4, 96, -50, 72, -60, -57, 125, 57, Ascii.NAK, -81, -44, Ascii.f220EM, 112, -75, 83, 57, -55, 61, Ascii.CAN, Ascii.f222FS, -112, -103, -8, 120, 110, -52, -108, -41, -76, -96, 87, -117, 69, 0, SignedBytes.MAX_POWER_OF_TWO, Ascii.SUB, 4, 122, Ascii.f219CR, 6, -106, 112, -51, -1, 79, 117, -25, 2, SignedBytes.MAX_POWER_OF_TWO, Ascii.DEL, 68, 60, 81, -5, 110, 41, -1, 122, 93, -74, -113, -24, 52, ByteSourceJsonBootstrapper.UTF8_BOM_3, -60, 72, 8, 32, -24, -48, Ascii.SUB, -57, 38, -26, 0, -48, -24, -21, -28, -66, 47, -33, 63, 48, 34, 108, -51, -116, -125, -40, 42, Ascii.SUB, 32, Ascii.f221FF, 73, -1, Ascii.f220EM, 77, 51, -109, 7, Ascii.SYN, -124, 79, -26, 50, -51, -76, Ascii.f219CR, -80, -66, 19, -7, 2, 65, 0, -90, 99, -20, 68, -4, -84, -11, -105, 83, -123, -124, -63, -103, -16, -81, 101, 78, -72, -72, 91, 100, -57, -74, -111, 49, Ascii.DC2, 54, 4, -19, 125, 32, -24, 125, -26, 100, -33, -117, 0, 115, ByteSourceJsonBootstrapper.UTF8_BOM_3, 33, 124, -107, 3, -95, -91, 118, Ascii.f221FF, Ascii.f221FF, Ascii.f223GS, 80, -3, Ascii.f221FF, -20, 7, 52, -118, -12, 122, 75, 117, -81, -112, -89, 2, SignedBytes.MAX_POWER_OF_TWO, 93, -21, -52, -110, -54, -9, 79, -123, 105, 125, -56, 75, -77, -26, 125, -123, ByteSourceJsonBootstrapper.UTF8_BOM_2, 62, -2, 79, 8, 72, -76, -67, 5, 33, -121, 1, -42, ByteSourceJsonBootstrapper.UTF8_BOM_1, Ascii.f223GS, 69, -20, -68, -26, -23, 95, -7, -70, -50, -10, 58, Ascii.DLE, -15, -89, -24, -121, -14, -72, -127, -89, -63, 66, 7, 77, -89, -54, -95, -90, 45, -44, -118, 69, -1};
    private static final byte[] ENCODED_PUBLIC_KEY;

    static {
        byte[] bArr = new byte[DummyPolicyIDType.zPolicy_SetOriginalSoundMicID];
        // fill-array-data instruction
        bArr[0] = 48;
        bArr[1] = -127;
        bArr[2] = -97;
        bArr[3] = 48;
        bArr[4] = 13;
        bArr[5] = 6;
        bArr[6] = 9;
        bArr[7] = 42;
        bArr[8] = -122;
        bArr[9] = 72;
        bArr[10] = -122;
        bArr[11] = -9;
        bArr[12] = 13;
        bArr[13] = 1;
        bArr[14] = 1;
        bArr[15] = 1;
        bArr[16] = 5;
        bArr[17] = 0;
        bArr[18] = 3;
        bArr[19] = -127;
        bArr[20] = -115;
        bArr[21] = 0;
        bArr[22] = 48;
        bArr[23] = -127;
        bArr[24] = -119;
        bArr[25] = 2;
        bArr[26] = -127;
        bArr[27] = -127;
        bArr[28] = 0;
        bArr[29] = -89;
        bArr[30] = 33;
        bArr[31] = 8;
        bArr[32] = -124;
        bArr[33] = 110;
        bArr[34] = -60;
        bArr[35] = 89;
        bArr[36] = 8;
        bArr[37] = -62;
        bArr[38] = 69;
        bArr[39] = 120;
        bArr[40] = 95;
        bArr[41] = -59;
        bArr[42] = -43;
        bArr[43] = 13;
        bArr[44] = -18;
        bArr[45] = 123;
        bArr[46] = 29;
        bArr[47] = -31;
        bArr[48] = 13;
        bArr[49] = -80;
        bArr[50] = -76;
        bArr[51] = 109;
        bArr[52] = -62;
        bArr[53] = -79;
        bArr[54] = 2;
        bArr[55] = 104;
        bArr[56] = -94;
        bArr[57] = 76;
        bArr[58] = 59;
        bArr[59] = -73;
        bArr[60] = -26;
        bArr[61] = 99;
        bArr[62] = 123;
        bArr[63] = -57;
        bArr[64] = -92;
        bArr[65] = -100;
        bArr[66] = 116;
        bArr[67] = 50;
        bArr[68] = -25;
        bArr[69] = 96;
        bArr[70] = 53;
        bArr[71] = 124;
        bArr[72] = 95;
        bArr[73] = 76;
        bArr[74] = -59;
        bArr[75] = -84;
        bArr[76] = 70;
        bArr[77] = 27;
        bArr[78] = 0;
        bArr[79] = 72;
        bArr[80] = -63;
        bArr[81] = 84;
        bArr[82] = -77;
        bArr[83] = -2;
        bArr[84] = -107;
        bArr[85] = -66;
        bArr[86] = -32;
        bArr[87] = -119;
        bArr[88] = 27;
        bArr[89] = -95;
        bArr[90] = 54;
        bArr[91] = -44;
        bArr[92] = -89;
        bArr[93] = 1;
        bArr[94] = 71;
        bArr[95] = 44;
        bArr[96] = 7;
        bArr[97] = -55;
        bArr[98] = 126;
        bArr[99] = 5;
        bArr[100] = -78;
        bArr[101] = 87;
        bArr[102] = -105;
        bArr[103] = -114;
        bArr[104] = 65;
        bArr[105] = -19;
        bArr[106] = 58;
        bArr[107] = -78;
        bArr[108] = -95;
        bArr[109] = 0;
        bArr[110] = 118;
        bArr[111] = 83;
        bArr[112] = 76;
        bArr[113] = -88;
        bArr[114] = 2;
        bArr[115] = -21;
        bArr[116] = 127;
        bArr[117] = 64;
        bArr[118] = 74;
        bArr[119] = -103;
        bArr[120] = -114;
        bArr[121] = -127;
        bArr[122] = -70;
        bArr[123] = -81;
        bArr[124] = -127;
        bArr[125] = 125;
        bArr[126] = -37;
        bArr[127] = 21;
        bArr[128] = 113;
        bArr[129] = 20;
        bArr[130] = -102;
        bArr[131] = 46;
        bArr[132] = -37;
        bArr[133] = -111;
        bArr[134] = -97;
        bArr[135] = 97;
        bArr[136] = -127;
        bArr[137] = 32;
        bArr[138] = 87;
        bArr[139] = -80;
        bArr[140] = 105;
        bArr[141] = 18;
        bArr[142] = -19;
        bArr[143] = 107;
        bArr[144] = -73;
        bArr[145] = -50;
        bArr[146] = -97;
        bArr[147] = 11;
        bArr[148] = -23;
        bArr[149] = -59;
        bArr[150] = -107;
        bArr[151] = -107;
        bArr[152] = 83;
        bArr[153] = -25;
        bArr[154] = 15;
        bArr[155] = -93;
        bArr[156] = -21;
        bArr[157] = 2;
        bArr[158] = 3;
        bArr[159] = 1;
        bArr[160] = 0;
        bArr[161] = 1;
        ENCODED_PUBLIC_KEY = bArr;
    }

    public static byte[] newEncodedRsaPrivateKeyBytes() {
        return (byte[]) ENCODED_PRIVATE_KEY.clone();
    }

    public static byte[] newEncodedRsaPublicKeyBytes() {
        return (byte[]) ENCODED_PUBLIC_KEY.clone();
    }

    public static RSAPrivateKey newRsaPrivateKey() throws GeneralSecurityException {
        return (RSAPrivateKey) SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(ENCODED_PRIVATE_KEY));
    }

    public static RSAPublicKey newRsaPublicKey() throws GeneralSecurityException {
        return (RSAPublicKey) SecurityUtils.getRsaKeyFactory().generatePublic(new X509EncodedKeySpec(ENCODED_PUBLIC_KEY));
    }

    private SecurityTestUtils() {
    }
}
