package com.microsoft.aad.adal;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import androidx.exifinterface.media.ExifInterface;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;

final class PRNGFixes {
    private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
    private static final int ONE_KB = 1024;
    private static final String TAG = "PRNGFixes";
    private static final int VERSION_CODE_JELLY_BEAN = 16;
    private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;

    public static class LinuxPRNGSecureRandom extends SecureRandomSpi {
        private static final Object SLOCK = new Object();
        private static final File URANDOM_FILE = new File("/dev/urandom");
        private static DataInputStream sUrandomIn = null;
        private static OutputStream sUrandomOut = null;
        private static final long serialVersionUID = 1;
        private boolean mSeeded;

        /* access modifiers changed from: protected */
        public void engineSetSeed(byte[] bArr) {
            String str;
            StringBuilder sb;
            OutputStream outputStream = null;
            try {
                outputStream = getUrandomOutputStream();
                outputStream.write(bArr);
                outputStream.flush();
                this.mSeeded = true;
                if (outputStream != null) {
                    try {
                        outputStream.close();
                        return;
                    } catch (IOException e) {
                        e = e;
                        str = "PRNGFixesengineSetSeed";
                        sb = new StringBuilder();
                    }
                } else {
                    return;
                }
                sb.append("Failed to close the output stream to \"/dev/urandom\" . Exception: ");
                sb.append(e.toString());
                Logger.m236v(str, sb.toString());
            } catch (IOException unused) {
                String simpleName = PRNGFixes.class.getSimpleName();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to mix seed into ");
                sb2.append(URANDOM_FILE);
                Logger.m238w(simpleName, sb2.toString());
                this.mSeeded = true;
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e2) {
                        e = e2;
                        str = "PRNGFixesengineSetSeed";
                        sb = new StringBuilder();
                    }
                }
            } catch (Throwable th) {
                this.mSeeded = true;
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e3) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Failed to close the output stream to \"/dev/urandom\" . Exception: ");
                        sb3.append(e3.toString());
                        Logger.m236v("PRNGFixesengineSetSeed", sb3.toString());
                    }
                }
                throw th;
            }
        }

        /* access modifiers changed from: protected */
        public void engineNextBytes(byte[] bArr) {
            if (!this.mSeeded) {
                engineSetSeed(PRNGFixes.generateSeed());
            }
            DataInputStream dataInputStream = null;
            try {
                dataInputStream = getUrandomInputStream();
                synchronized (SLOCK) {
                    dataInputStream.readFully(bArr);
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to close the input stream to \"/dev/urandom\" . Exception: ");
                        sb.append(e.toString());
                        Logger.m236v("PRNGFixesengineNextBytes", sb.toString());
                    }
                }
            } catch (IOException e2) {
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to read from ");
                    sb2.append(URANDOM_FILE);
                    throw new SecurityException(sb2.toString(), e2);
                } catch (Throwable th) {
                    if (dataInputStream != null) {
                        try {
                            dataInputStream.close();
                        } catch (IOException e3) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Failed to close the input stream to \"/dev/urandom\" . Exception: ");
                            sb3.append(e3.toString());
                            Logger.m236v("PRNGFixesengineNextBytes", sb3.toString());
                        }
                    }
                    throw th;
                }
            }
        }

        /* access modifiers changed from: protected */
        public byte[] engineGenerateSeed(int i) {
            byte[] bArr = new byte[i];
            engineNextBytes(bArr);
            return bArr;
        }

        private DataInputStream getUrandomInputStream() {
            DataInputStream dataInputStream;
            synchronized (SLOCK) {
                if (sUrandomIn == null) {
                    try {
                        sUrandomIn = new DataInputStream(new FileInputStream(URANDOM_FILE));
                    } catch (IOException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to open ");
                        sb.append(URANDOM_FILE);
                        sb.append(" for reading");
                        throw new SecurityException(sb.toString(), e);
                    }
                }
                dataInputStream = sUrandomIn;
            }
            return dataInputStream;
        }

        private OutputStream getUrandomOutputStream() throws IOException {
            OutputStream outputStream;
            synchronized (SLOCK) {
                if (sUrandomOut == null) {
                    sUrandomOut = new FileOutputStream(URANDOM_FILE);
                }
                outputStream = sUrandomOut;
            }
            return outputStream;
        }
    }

    private static class LinuxPRNGSecureRandomProvider extends Provider {
        private static final long serialVersionUID = 1;

        LinuxPRNGSecureRandomProvider() {
            super("LinuxPRNG", 1.0d, "A Linux-specific random number provider that uses /dev/urandom");
            put("SecureRandom.SHA1PRNG", LinuxPRNGSecureRandom.class.getName());
            put("SecureRandom.SHA1PRNG ImplementedIn", ExifInterface.TAG_SOFTWARE);
        }
    }

    private PRNGFixes() {
    }

    public static void apply() {
        applyOpenSSLFix();
        installLinuxPRNGSecureRandom();
    }

    private static void applyOpenSSLFix() throws SecurityException {
        if (VERSION.SDK_INT < 16 || VERSION.SDK_INT > 18) {
            Logger.m236v("PRNGFixes:applyOpenSSLFix", "No need to apply the OpenSSL fix.");
            return;
        }
        try {
            Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", new Class[]{byte[].class}).invoke(null, new Object[]{generateSeed()});
            int intValue = ((Integer) Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_load_file", new Class[]{String.class, Long.TYPE}).invoke(null, new Object[]{"/dev/urandom", Integer.valueOf(1024)})).intValue();
            if (intValue != 1024) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected number of bytes read from Linux PRNG: ");
                sb.append(intValue);
                throw new IOException(sb.toString());
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Logger.m232e("PRNGFixes:applyOpenSSLFix", "Failed to seed OpenSSL PRNG. ", "", ADALError.DEVICE_PRNG_FIX_ERROR, e);
            throw new SecurityException("Failed to seed OpenSSL PRNG", e);
        }
    }

    private static void installLinuxPRNGSecureRandom() throws SecurityException {
        if (VERSION.SDK_INT > 18) {
            Logger.m236v("PRNGFixes:installLinuxPRNGSecureRandom", "No need to apply the fix.");
            return;
        }
        Provider[] providers = Security.getProviders("SecureRandom.SHA1PRNG");
        if (providers == null || providers.length < 1 || !LinuxPRNGSecureRandomProvider.class.equals(providers[0].getClass())) {
            Logger.m236v("PRNGFixes:installLinuxPRNGSecureRandom", "Insert provider as LinuxPRNGSecureRandomProvider.");
            Security.insertProviderAt(new LinuxPRNGSecureRandomProvider(), 1);
        }
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        sb.append("Provider: ");
        sb.append(secureRandom.getProvider().getClass().getName());
        Logger.m234i("PRNGFixes:installLinuxPRNGSecureRandom", "LinuxPRNGSecureRandomProvider for SecureRandom. ", sb.toString());
        try {
            SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Provider: ");
            sb2.append(instance.getProvider().getClass().getName());
            Logger.m234i("PRNGFixes:installLinuxPRNGSecureRandom", "LinuxPRNGSecureRandomProvider for SecureRandom with alg SHA1PRNG. ", sb2.toString());
        } catch (NoSuchAlgorithmException e) {
            Logger.m236v("PRNGFixes:installLinuxPRNGSecureRandom", "SHA1PRNG not available.");
            throw new SecurityException("SHA1PRNG not available", e);
        }
    }

    /* access modifiers changed from: private */
    public static byte[] generateSeed() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(System.currentTimeMillis());
            dataOutputStream.writeLong(System.nanoTime());
            dataOutputStream.writeInt(Process.myPid());
            dataOutputStream.writeInt(Process.myUid());
            dataOutputStream.write(BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
            dataOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SecurityException("Failed to generate seed", e);
        }
    }

    private static String getDeviceSerialNumber() {
        try {
            return (String) Build.class.getField("SERIAL").get(null);
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] getBuildFingerprintAndDeviceSerial() {
        StringBuilder sb = new StringBuilder();
        String str = Build.FINGERPRINT;
        if (str != null) {
            sb.append(str);
        }
        String deviceSerialNumber = getDeviceSerialNumber();
        if (deviceSerialNumber != null) {
            sb.append(deviceSerialNumber);
        }
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }
}
