package com.microsoft.aad.adal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec.Builder;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.DigestException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

public class StorageHelper {
    private static final String ADALKS = "adalks";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CIPHER_TRANSFORMATION_OAEP = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String CIPHER_TRANSFORMATION_PKCS = "RSA/ECB/PKCS1Padding";
    public static final int DATA_KEY_LENGTH = 16;
    private static final String ENCODE_VERSION = "E1";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HMAC_KEY_HASH_ALGORITHM = "SHA256";
    public static final int HMAC_LENGTH = 32;
    private static final String KEYSPEC_ALGORITHM = "AES";
    private static final int KEY_FILE_SIZE = 1024;
    private static final String KEY_ONE_DRIVE_CIPHER_TRANSFORMATION = "key_one_drive_cipher_transformation";
    private static final int KEY_SIZE = 256;
    private static final String KEY_STORE_CERT_ALIAS = "AdalKey";
    private static final int KEY_VERSION_BLOB_LENGTH = 4;
    private static final String LOCALE_PREFERENCE_NAME = "local_prefenrence_name";
    private static final String TAG = "StorageHelper";
    public static final String VERSION_ANDROID_KEY_STORE = "A001";
    public static final String VERSION_USER_DEFINED = "U001";
    private static String WRAP_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private String mBlobVersion;
    private final Context mContext;
    private SecretKey mHMACKey = null;
    private SecretKey mKey = null;
    private KeyPair mKeyPair;
    private final SecureRandom mRandom;
    private SecretKey mSecretKeyFromAndroidKeyStore = null;

    private char getEncodeVersionLengthPrefix() {
        return (char) 99;
    }

    public StorageHelper(Context context) {
        this.mContext = context;
        this.mRandom = new SecureRandom();
        String string = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).getString(KEY_ONE_DRIVE_CIPHER_TRANSFORMATION, null);
        if (string == null || string.trim().length() == 0) {
            WRAP_ALGORITHM = CIPHER_TRANSFORMATION_PKCS;
        } else {
            WRAP_ALGORITHM = string;
        }
    }

    public String encrypt(String str) throws GeneralSecurityException, IOException {
        Logger.m236v("StorageHelper:encrypt", "Starting encryption");
        if (!StringExtensions.isNullOrBlank(str)) {
            this.mKey = loadSecretKeyForEncryption();
            this.mHMACKey = getHMacKey(this.mKey);
            StringBuilder sb = new StringBuilder();
            sb.append("Encrypt version:");
            sb.append(this.mBlobVersion);
            Logger.m234i("StorageHelper:encrypt", "", sb.toString());
            byte[] bytes = this.mBlobVersion.getBytes("UTF_8");
            byte[] bytes2 = str.getBytes("UTF_8");
            byte[] bArr = new byte[16];
            this.mRandom.nextBytes(bArr);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
            Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
            Mac instance2 = Mac.getInstance(HMAC_ALGORITHM);
            instance.init(1, this.mKey, ivParameterSpec);
            byte[] doFinal = instance.doFinal(bytes2);
            instance2.init(this.mHMACKey);
            instance2.update(bytes);
            instance2.update(doFinal);
            instance2.update(bArr);
            byte[] doFinal2 = instance2.doFinal();
            byte[] bArr2 = new byte[(bytes.length + doFinal.length + bArr.length + doFinal2.length)];
            System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
            System.arraycopy(doFinal, 0, bArr2, bytes.length, doFinal.length);
            System.arraycopy(bArr, 0, bArr2, bytes.length + doFinal.length, bArr.length);
            System.arraycopy(doFinal2, 0, bArr2, bytes.length + doFinal.length + bArr.length, doFinal2.length);
            String str2 = new String(Base64.encode(bArr2, 2), "UTF_8");
            Logger.m236v("StorageHelper:encrypt", "Finished encryption");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getEncodeVersionLengthPrefix());
            sb2.append(ENCODE_VERSION);
            sb2.append(str2);
            return sb2.toString();
        }
        throw new IllegalArgumentException("Input is empty or null");
    }

    public String decrypt(String str) throws GeneralSecurityException, IOException {
        Logger.m236v("StorageHelper:decrypt", "Starting decryption");
        if (!StringExtensions.isNullOrBlank(str)) {
            int charAt = str.charAt(0) - 'a';
            if (charAt > 0) {
                int i = charAt + 1;
                if (str.substring(1, i).equals(ENCODE_VERSION)) {
                    byte[] decode = Base64.decode(str.substring(i), 0);
                    String str2 = new String(decode, 0, 4, "UTF_8");
                    StringBuilder sb = new StringBuilder();
                    sb.append("Encrypt version:");
                    sb.append(str2);
                    Logger.m234i("StorageHelper:decrypt", "", sb.toString());
                    SecretKey key = getKey(str2);
                    SecretKey hMacKey = getHMacKey(key);
                    int length = (decode.length - 16) - 32;
                    int length2 = decode.length - 32;
                    int i2 = length - 4;
                    if (length < 0 || length2 < 0 || i2 < 0) {
                        throw new IOException("Invalid byte array input for decryption.");
                    }
                    Cipher instance = Cipher.getInstance(CIPHER_ALGORITHM);
                    Mac instance2 = Mac.getInstance(HMAC_ALGORITHM);
                    instance2.init(hMacKey);
                    instance2.update(decode, 0, length2);
                    assertHMac(decode, length2, decode.length, instance2.doFinal());
                    instance.init(2, key, new IvParameterSpec(decode, length, 16));
                    String str3 = new String(instance.doFinal(decode, 4, i2), "UTF_8");
                    Logger.m236v("StorageHelper:decrypt", "Finished decryption");
                    return str3;
                }
                throw new IllegalArgumentException(String.format("Encode version received was: '%s', Encode version supported is: '%s'", new Object[]{str, ENCODE_VERSION}));
            }
            throw new IllegalArgumentException(String.format("Encode version length: '%s' is not valid, it must be greater of equal to 0", new Object[]{Integer.valueOf(charAt)}));
        }
        throw new IllegalArgumentException("Input is empty or null");
    }

    /* access modifiers changed from: 0000 */
    public synchronized SecretKey loadSecretKeyForEncryption() throws IOException, GeneralSecurityException {
        return loadSecretKeyForEncryption(AuthenticationSettings.INSTANCE.getSecretKeyData() == null ? VERSION_ANDROID_KEY_STORE : VERSION_USER_DEFINED);
    }

    /* access modifiers changed from: 0000 */
    public synchronized SecretKey loadSecretKeyForEncryption(String str) throws IOException, GeneralSecurityException {
        if (this.mKey == null || this.mHMACKey == null) {
            this.mBlobVersion = str;
            return getKeyOrCreate(this.mBlobVersion);
        }
        return this.mKey;
    }

    private synchronized SecretKey getKeyOrCreate(String str) throws GeneralSecurityException, IOException {
        if (VERSION_USER_DEFINED.equals(str)) {
            return getSecretKey(AuthenticationSettings.INSTANCE.getSecretKeyData());
        }
        try {
            this.mSecretKeyFromAndroidKeyStore = getKey(str);
        } catch (IOException | GeneralSecurityException unused) {
            Logger.m236v("StorageHelper:getKeyOrCreate", "Key does not exist in AndroidKeyStore, try to generate new keys.");
        }
        if (this.mSecretKeyFromAndroidKeyStore == null) {
            this.mKeyPair = generateKeyPairFromAndroidKeyStore();
            this.mSecretKeyFromAndroidKeyStore = generateSecretKey();
            writeKeyData(wrap(this.mSecretKeyFromAndroidKeyStore));
        }
        return this.mSecretKeyFromAndroidKeyStore;
    }

    private synchronized SecretKey getKey(String str) throws GeneralSecurityException, IOException {
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != 1984080) {
            if (hashCode == 2579900) {
                if (str.equals(VERSION_USER_DEFINED)) {
                    c = 0;
                }
            }
        } else if (str.equals(VERSION_ANDROID_KEY_STORE)) {
            c = 1;
        }
        switch (c) {
            case 0:
                return getSecretKey(AuthenticationSettings.INSTANCE.getSecretKeyData());
            case 1:
                if (this.mSecretKeyFromAndroidKeyStore != null) {
                    return this.mSecretKeyFromAndroidKeyStore;
                }
                this.mKeyPair = readKeyPair();
                this.mSecretKeyFromAndroidKeyStore = getUnwrappedSecretKey();
                return this.mSecretKeyFromAndroidKeyStore;
            default:
                throw new IOException("Unknown keyVersion.");
        }
    }

    @TargetApi(18)
    private synchronized KeyPair generateKeyPairFromAndroidKeyStore() throws GeneralSecurityException, IOException {
        KeyPairGenerator instance;
        KeyStore.getInstance(ANDROID_KEY_STORE).load(null);
        Logger.m236v("StorageHelper:generateKeyPairFromAndroidKeyStore", "Generate KeyPair from AndroidKeyStore");
        Calendar instance2 = Calendar.getInstance();
        Calendar instance3 = Calendar.getInstance();
        instance3.add(1, 100);
        instance = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);
        instance.initialize(getKeyPairGeneratorSpec(this.mContext, instance2.getTime(), instance3.getTime()));
        try {
        } catch (IllegalStateException e) {
            throw new KeyStoreException(e);
        }
        return instance.generateKeyPair();
    }

    private synchronized KeyPair readKeyPair() throws GeneralSecurityException, IOException {
        PrivateKeyEntry privateKeyEntry;
        if (doesKeyPairExist()) {
            Logger.m236v("StorageHelper:readKeyPair", "Reading Key entry");
            KeyStore instance = KeyStore.getInstance(ANDROID_KEY_STORE);
            instance.load(null);
            try {
                privateKeyEntry = (PrivateKeyEntry) instance.getEntry(KEY_STORE_CERT_ALIAS, null);
            } catch (RuntimeException e) {
                throw new KeyStoreException(e);
            }
        } else {
            throw new KeyStoreException("KeyPair entry does not exist.");
        }
        return new KeyPair(privateKeyEntry.getCertificate().getPublicKey(), privateKeyEntry.getPrivateKey());
    }

    private synchronized boolean doesKeyPairExist() throws GeneralSecurityException, IOException {
        KeyStore instance;
        instance = KeyStore.getInstance(ANDROID_KEY_STORE);
        instance.load(null);
        try {
        } catch (NullPointerException e) {
            throw new KeyStoreException(e);
        }
        return instance.containsAlias(KEY_STORE_CERT_ALIAS);
    }

    @TargetApi(18)
    private AlgorithmParameterSpec getKeyPairGeneratorSpec(Context context, Date date, Date date2) {
        AlgorithmParameterSpec algorithmParameterSpec;
        String format = String.format(Locale.ROOT, "CN=%s, OU=%s", new Object[]{KEY_STORE_CERT_ALIAS, context.getPackageName()});
        if (VERSION.SDK_INT >= 23) {
            WRAP_ALGORITHM = CIPHER_TRANSFORMATION_OAEP;
            algorithmParameterSpec = new Builder(KEY_STORE_CERT_ALIAS, 3).setCertificateSubject(new X500Principal(format)).setDigests(new String[]{MessageDigestAlgorithms.SHA_256, MessageDigestAlgorithms.SHA_512}).setEncryptionPaddings(new String[]{"OAEPPadding"}).setCertificateSerialNumber(BigInteger.TEN).setCertificateNotBefore(date).setCertificateNotAfter(date2).build();
        } else {
            WRAP_ALGORITHM = CIPHER_TRANSFORMATION_PKCS;
            algorithmParameterSpec = new KeyPairGeneratorSpec.Builder(context).setAlias(KEY_STORE_CERT_ALIAS).setSubject(new X500Principal(format)).setSerialNumber(BigInteger.TEN).setStartDate(date).setEndDate(date2).build();
        }
        Editor edit = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).edit();
        edit.putString(KEY_ONE_DRIVE_CIPHER_TRANSFORMATION, WRAP_ALGORITHM);
        edit.commit();
        return algorithmParameterSpec;
    }

    private SecretKey getSecretKey(byte[] bArr) {
        if (bArr != null) {
            return new SecretKeySpec(bArr, KEYSPEC_ALGORITHM);
        }
        throw new IllegalArgumentException("rawBytes");
    }

    private SecretKey getHMacKey(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] encoded = secretKey.getEncoded();
        return encoded != null ? new SecretKeySpec(MessageDigest.getInstance(HMAC_KEY_HASH_ALGORITHM).digest(encoded), KEYSPEC_ALGORITHM) : secretKey;
    }

    private void assertHMac(byte[] bArr, int i, int i2, byte[] bArr2) throws DigestException {
        if (bArr2.length == i2 - i) {
            byte b = 0;
            for (int i3 = i; i3 < i2; i3++) {
                b = (byte) (b | (bArr2[i3 - i] ^ bArr[i3]));
            }
            if (b != 0) {
                throw new DigestException();
            }
            return;
        }
        throw new IllegalArgumentException("Unexpected HMAC length");
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator instance = KeyGenerator.getInstance(KEYSPEC_ALGORITHM);
        instance.init(256, this.mRandom);
        return instance.generateKey();
    }

    @TargetApi(18)
    private synchronized SecretKey getUnwrappedSecretKey() throws GeneralSecurityException, IOException {
        SecretKey unwrap;
        Logger.m236v("StorageHelper:getUnwrappedSecretKey", "Reading SecretKey");
        try {
            unwrap = unwrap(readKeyData());
            Logger.m236v("StorageHelper:getUnwrappedSecretKey", "Finished reading SecretKey");
        } catch (IOException | GeneralSecurityException e) {
            Logger.m232e("StorageHelper:getUnwrappedSecretKey", "Unwrap failed for AndroidKeyStore", "", ADALError.ANDROIDKEYSTORE_FAILED, e);
            this.mKeyPair = null;
            deleteKeyFile();
            resetKeyPairFromAndroidKeyStore();
            Logger.m236v("StorageHelper:getUnwrappedSecretKey", "Removed previous key pair info.");
            throw e;
        }
        return unwrap;
    }

    private void deleteKeyFile() {
        Context context = this.mContext;
        File file = new File(context.getDir(context.getPackageName(), 0), ADALKS);
        if (file.exists()) {
            Logger.m236v("StorageHelper:deleteKeyFile", "Delete KeyFile");
            if (!file.delete()) {
                Logger.m236v("StorageHelper:deleteKeyFile", "Delete KeyFile failed");
            }
        }
    }

    @TargetApi(18)
    private synchronized void resetKeyPairFromAndroidKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore instance = KeyStore.getInstance(ANDROID_KEY_STORE);
        instance.load(null);
        instance.deleteEntry(KEY_STORE_CERT_ALIAS);
    }

    @SuppressLint({"GetInstance"})
    @TargetApi(18)
    private byte[] wrap(SecretKey secretKey) throws GeneralSecurityException {
        Logger.m236v(TAG, "Wrap secret key.");
        Cipher instance = Cipher.getInstance(WRAP_ALGORITHM);
        OAEPParameterSpec oAEPParameterSpec = getOAEPParameterSpec(WRAP_ALGORITHM);
        if (oAEPParameterSpec != null) {
            instance.init(3, this.mKeyPair.getPublic(), oAEPParameterSpec);
        } else {
            instance.init(3, this.mKeyPair.getPublic());
        }
        return instance.wrap(secretKey);
    }

    @SuppressLint({"GetInstance"})
    @TargetApi(18)
    private SecretKey unwrap(byte[] bArr) throws GeneralSecurityException {
        Cipher instance = Cipher.getInstance(WRAP_ALGORITHM);
        instance.init(4, this.mKeyPair.getPrivate());
        OAEPParameterSpec oAEPParameterSpec = getOAEPParameterSpec(WRAP_ALGORITHM);
        if (oAEPParameterSpec != null) {
            instance.init(4, this.mKeyPair.getPrivate(), oAEPParameterSpec);
        } else {
            instance.init(4, this.mKeyPair.getPrivate());
        }
        try {
            return (SecretKey) instance.unwrap(bArr, KEYSPEC_ALGORITHM, 3);
        } catch (IllegalArgumentException e) {
            throw new KeyStoreException(e);
        }
    }

    private void writeKeyData(byte[] bArr) throws IOException {
        Logger.m236v(TAG, "Writing key data to a file");
        Context context = this.mContext;
        FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getDir(context.getPackageName(), 0), ADALKS));
        try {
            fileOutputStream.write(bArr);
        } finally {
            fileOutputStream.close();
        }
    }

    private byte[] readKeyData() throws IOException {
        Context context = this.mContext;
        File file = new File(context.getDir(context.getPackageName(), 0), ADALKS);
        if (file.exists()) {
            Logger.m236v(TAG, "Reading key data from a file");
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        return byteArrayOutputStream.toByteArray();
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
            } finally {
                fileInputStream.close();
            }
        } else {
            throw new IOException("Key file to read does not exist");
        }
    }

    private OAEPParameterSpec getOAEPParameterSpec(String str) {
        if (CIPHER_TRANSFORMATION_OAEP.equals(str)) {
            return new OAEPParameterSpec(MessageDigestAlgorithms.SHA_256, "MGF1", new MGF1ParameterSpec(MessageDigestAlgorithms.SHA_1), PSpecified.DEFAULT);
        }
        return null;
    }
}
