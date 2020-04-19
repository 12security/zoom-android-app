package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* renamed from: us.zoom.androidlib.util.EncryptUtils */
public class EncryptUtils {
    private static final String CIPHER_TRANSFORMATION_OAEP = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String CIPHER_TRANSFORMATION_PKCS = "RSA/ECB/PKCS1Padding";
    private static final String KEY_CIPHER_TRANSFORMATION = "key_cipher_transformation";
    private static final String LOCALE_PREFERENCE_NAME = "local_prefenrence_name";
    private static final String TAG = "EncryptUtils";
    static EncryptUtils encryptUtilsInstance;
    KeyStore keyStore;
    private String mCipherTransformation = CIPHER_TRANSFORMATION_PKCS;

    public static EncryptUtils getInstance() {
        synchronized (EncryptUtils.class) {
            if (encryptUtilsInstance == null) {
                encryptUtilsInstance = new EncryptUtils();
            }
        }
        return encryptUtilsInstance;
    }

    private EncryptUtils() {
    }

    private OAEPParameterSpec getOAEPParameterSpec(String str) {
        if (StringUtil.isSameStringForNotAllowNull(str, CIPHER_TRANSFORMATION_OAEP)) {
            return new OAEPParameterSpec(MessageDigestAlgorithms.SHA_256, "MGF1", new MGF1ParameterSpec(MessageDigestAlgorithms.SHA_1), PSpecified.DEFAULT);
        }
        return null;
    }

    private boolean initKeyStore(Context context, String str) {
        try {
            this.keyStore = KeyStore.getInstance("AndroidKeyStore");
            this.keyStore.load(null);
            if (OsUtil.isAtLeastJB_MR2()) {
                return createNewKeys(context, str);
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean createNewKeys(Context context, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            try {
                AlgorithmParameterSpec algorithmParameterSpec = null;
                if (!this.keyStore.containsAlias(str)) {
                    Calendar instance = Calendar.getInstance();
                    Calendar instance2 = Calendar.getInstance();
                    instance2.add(1, 20);
                    if (OsUtil.isAtLeastM()) {
                        this.mCipherTransformation = CIPHER_TRANSFORMATION_PKCS;
                        Builder builder = new Builder(str, 3);
                        StringBuilder sb = new StringBuilder();
                        sb.append("CN=");
                        sb.append(str);
                        algorithmParameterSpec = builder.setCertificateSubject(new X500Principal(sb.toString())).setEncryptionPaddings(new String[]{"PKCS1Padding"}).setCertificateSerialNumber(BigInteger.TEN).setCertificateNotBefore(instance.getTime()).setCertificateNotAfter(instance2.getTime()).build();
                    } else if (OsUtil.isAtLeastJB_MR2()) {
                        this.mCipherTransformation = CIPHER_TRANSFORMATION_PKCS;
                        KeyPairGeneratorSpec.Builder alias = new KeyPairGeneratorSpec.Builder(context).setAlias(str);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("CN=");
                        sb2.append(str);
                        algorithmParameterSpec = alias.setSubject(new X500Principal(sb2.toString())).setSerialNumber(BigInteger.TEN).setStartDate(instance.getTime()).setEndDate(instance2.getTime()).build();
                    } else {
                        this.mCipherTransformation = CIPHER_TRANSFORMATION_PKCS;
                    }
                    KeyPairGenerator instance3 = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    if (OsUtil.isAtLeastJB_MR2()) {
                        instance3.initialize(algorithmParameterSpec);
                    }
                    instance3.generateKeyPair();
                    Editor edit = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).edit();
                    edit.putString(KEY_CIPHER_TRANSFORMATION, this.mCipherTransformation);
                    edit.commit();
                } else {
                    String string = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).getString(KEY_CIPHER_TRANSFORMATION, null);
                    if (StringUtil.isEmptyOrNull(string)) {
                        this.mCipherTransformation = CIPHER_TRANSFORMATION_PKCS;
                    } else {
                        this.mCipherTransformation = string;
                    }
                }
            } catch (Exception unused) {
                return false;
            }
        }
        return true;
    }

    public String encryptString(Context context, String str, String str2) {
        return encryptString(context, str.getBytes(CompatUtils.getStardardCharSetUTF8()), str2);
    }

    public String encryptString(Context context, byte[] bArr, String str) {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        CipherOutputStream cipherOutputStream;
        Throwable th2;
        Throwable th3;
        if (StringUtil.isEmptyOrNull(str) || bArr == null || bArr.length <= 0) {
            return "";
        }
        if (!(OsUtil.isAtLeastJB_MR2() ? initKeyStore(context, str) : false)) {
            return "";
        }
        try {
            PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) this.keyStore.getEntry(str, null);
            Cipher instance = Cipher.getInstance(this.mCipherTransformation);
            OAEPParameterSpec oAEPParameterSpec = getOAEPParameterSpec(this.mCipherTransformation);
            if (oAEPParameterSpec != null) {
                instance.init(1, privateKeyEntry.getCertificate().getPublicKey(), oAEPParameterSpec);
            } else {
                instance.init(1, privateKeyEntry.getCertificate().getPublicKey());
            }
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, instance);
                    try {
                        cipherOutputStream.write(bArr);
                        cipherOutputStream.close();
                        Arrays.fill(bArr, 0);
                        String encodeToString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                        cipherOutputStream.close();
                        byteArrayOutputStream.close();
                        return encodeToString;
                    } catch (Throwable th4) {
                        Throwable th5 = th4;
                        th3 = r8;
                        th2 = th5;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    throw th;
                }
            } catch (IOException unused) {
                Arrays.fill(bArr, 0);
                return "";
            } catch (Throwable th7) {
                th.addSuppressed(th7);
            }
        } catch (Exception unused2) {
            return "";
        }
        throw th2;
        if (th3 != null) {
            cipherOutputStream.close();
        } else {
            cipherOutputStream.close();
        }
        throw th2;
        throw th;
    }

    public String decryptString(Context context, String str, String str2) {
        byte[] decryptStringInByte = decryptStringInByte(context, str, str2);
        return (decryptStringInByte == null || decryptStringInByte.length <= 0) ? "" : new String(decryptStringInByte, 0, decryptStringInByte.length, CompatUtils.getStardardCharSetUTF8());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00a0, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00a1, code lost:
        r8 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a5, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a6, code lost:
        r4 = r8;
        r8 = r7;
        r7 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a0 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:17:0x004c] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] decryptStringInByte(android.content.Context r6, java.lang.String r7, java.lang.String r8) {
        /*
            r5 = this;
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            r1 = 0
            if (r0 != 0) goto L_0x00ba
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r7)
            if (r0 != 0) goto L_0x00ba
            boolean r0 = p021us.zoom.androidlib.util.OsUtil.isAtLeastJB_MR2()
            r2 = 0
            if (r0 == 0) goto L_0x0019
            boolean r6 = r5.initKeyStore(r6, r8)
            goto L_0x001a
        L_0x0019:
            r6 = 0
        L_0x001a:
            if (r6 != 0) goto L_0x001d
            return r1
        L_0x001d:
            java.security.KeyStore r6 = r5.keyStore     // Catch:{ Exception -> 0x00b9 }
            java.security.KeyStore$Entry r6 = r6.getEntry(r8, r1)     // Catch:{ Exception -> 0x00b9 }
            java.security.KeyStore$PrivateKeyEntry r6 = (java.security.KeyStore.PrivateKeyEntry) r6     // Catch:{ Exception -> 0x00b9 }
            java.lang.String r8 = r5.mCipherTransformation     // Catch:{ Exception -> 0x00b9 }
            javax.crypto.Cipher r8 = javax.crypto.Cipher.getInstance(r8)     // Catch:{ Exception -> 0x00b9 }
            java.lang.String r0 = r5.mCipherTransformation     // Catch:{ Exception -> 0x00b9 }
            javax.crypto.spec.OAEPParameterSpec r0 = r5.getOAEPParameterSpec(r0)     // Catch:{ Exception -> 0x00b9 }
            r3 = 2
            if (r0 == 0) goto L_0x003c
            java.security.PrivateKey r6 = r6.getPrivateKey()     // Catch:{ Exception -> 0x00b9 }
            r8.init(r3, r6, r0)     // Catch:{ Exception -> 0x00b9 }
            goto L_0x0043
        L_0x003c:
            java.security.PrivateKey r6 = r6.getPrivateKey()     // Catch:{ Exception -> 0x00b9 }
            r8.init(r3, r6)     // Catch:{ Exception -> 0x00b9 }
        L_0x0043:
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x00b8 }
            byte[] r7 = android.util.Base64.decode(r7, r2)     // Catch:{ Exception -> 0x00b8 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00b8 }
            javax.crypto.CipherInputStream r7 = new javax.crypto.CipherInputStream     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            r7.<init>(r6, r8)     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            r8.<init>()     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
        L_0x0056:
            int r0 = r7.read()     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            r3 = -1
            if (r0 == r3) goto L_0x0066
            byte r0 = (byte) r0     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            java.lang.Byte r0 = java.lang.Byte.valueOf(r0)     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            r8.add(r0)     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            goto L_0x0056
        L_0x0066:
            int r0 = r8.size()     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
        L_0x006c:
            int r3 = r0.length     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            if (r2 >= r3) goto L_0x007e
            java.lang.Object r3 = r8.get(r2)     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            java.lang.Byte r3 = (java.lang.Byte) r3     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            byte r3 = r3.byteValue()     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            r0[r2] = r3     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            int r2 = r2 + 1
            goto L_0x006c
        L_0x007e:
            r7.close()     // Catch:{ Throwable -> 0x008b, all -> 0x0088 }
            r7.close()     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            r6.close()     // Catch:{ Exception -> 0x00b8 }
            return r0
        L_0x0088:
            r8 = move-exception
            r0 = r1
            goto L_0x0091
        L_0x008b:
            r8 = move-exception
            throw r8     // Catch:{ all -> 0x008d }
        L_0x008d:
            r0 = move-exception
            r4 = r0
            r0 = r8
            r8 = r4
        L_0x0091:
            if (r0 == 0) goto L_0x009c
            r7.close()     // Catch:{ Throwable -> 0x0097, all -> 0x00a0 }
            goto L_0x009f
        L_0x0097:
            r7 = move-exception
            r0.addSuppressed(r7)     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
            goto L_0x009f
        L_0x009c:
            r7.close()     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
        L_0x009f:
            throw r8     // Catch:{ Throwable -> 0x00a3, all -> 0x00a0 }
        L_0x00a0:
            r7 = move-exception
            r8 = r1
            goto L_0x00a9
        L_0x00a3:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x00a5 }
        L_0x00a5:
            r8 = move-exception
            r4 = r8
            r8 = r7
            r7 = r4
        L_0x00a9:
            if (r8 == 0) goto L_0x00b4
            r6.close()     // Catch:{ Throwable -> 0x00af }
            goto L_0x00b7
        L_0x00af:
            r6 = move-exception
            r8.addSuppressed(r6)     // Catch:{ Exception -> 0x00b8 }
            goto L_0x00b7
        L_0x00b4:
            r6.close()     // Catch:{ Exception -> 0x00b8 }
        L_0x00b7:
            throw r7     // Catch:{ Exception -> 0x00b8 }
        L_0x00b8:
            return r1
        L_0x00b9:
            return r1
        L_0x00ba:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.EncryptUtils.decryptStringInByte(android.content.Context, java.lang.String, java.lang.String):byte[]");
    }
}
