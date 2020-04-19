package com.google.api.client.util;

public class Base64 {
    public static byte[] encodeBase64(byte[] bArr) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(bArr);
    }

    public static String encodeBase64String(byte[] bArr) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bArr);
    }

    public static byte[] encodeBase64URLSafe(byte[] bArr) {
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(bArr);
    }

    public static String encodeBase64URLSafeString(byte[] bArr) {
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(bArr);
    }

    public static byte[] decodeBase64(byte[] bArr) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(bArr);
    }

    public static byte[] decodeBase64(String str) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(str);
    }

    private Base64() {
    }
}
