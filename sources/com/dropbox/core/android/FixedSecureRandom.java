package com.dropbox.core.android;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
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
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;

public final class FixedSecureRandom extends SecureRandom {
    private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
    private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;
    private static final long serialVersionUID = 0;

    private static class LinuxPrngSecureRandomProvider extends Provider {
        private static final long serialVersionUID = 0;

        public LinuxPrngSecureRandomProvider() {
            super("LinuxPRNG", 1.0d, "A Linux-specific random number provider that uses /dev/urandom");
            put("SecureRandom.SHA1PRNG", LinuxPrngSecureRandomSpi.class.getName());
            put("SecureRandom.SHA1PRNG ImplementedIn", ExifInterface.TAG_SOFTWARE);
        }
    }

    public static class LinuxPrngSecureRandomSpi extends SecureRandomSpi {
        private static final File URANDOM_FILE = new File("/dev/urandom");
        private static final Object sLock = new Object();
        private static DataInputStream sUrandomIn;
        private static OutputStream sUrandomOut;
        private static final long serialVersionUID = 0;
        private boolean mSeeded;

        /* access modifiers changed from: protected */
        public void engineSetSeed(byte[] bArr) {
            OutputStream urandomOutputStream;
            try {
                synchronized (sLock) {
                    urandomOutputStream = getUrandomOutputStream();
                }
                urandomOutputStream.write(bArr);
                urandomOutputStream.flush();
            } catch (IOException unused) {
                try {
                    String simpleName = LinuxPrngSecureRandomSpi.class.getSimpleName();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to mix seed into ");
                    sb.append(URANDOM_FILE);
                    Log.w(simpleName, sb.toString());
                } catch (Throwable th) {
                    this.mSeeded = true;
                    throw th;
                }
            }
            this.mSeeded = true;
        }

        /* access modifiers changed from: protected */
        public void engineNextBytes(byte[] bArr) {
            DataInputStream urandomInputStream;
            if (!this.mSeeded) {
                engineSetSeed(FixedSecureRandom.generateSeed());
            }
            try {
                synchronized (sLock) {
                    urandomInputStream = getUrandomInputStream();
                }
                synchronized (urandomInputStream) {
                    urandomInputStream.readFully(bArr);
                }
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to read from ");
                sb.append(URANDOM_FILE);
                throw new SecurityException(sb.toString(), e);
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
            synchronized (sLock) {
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
            synchronized (sLock) {
                if (sUrandomOut == null) {
                    sUrandomOut = new FileOutputStream(URANDOM_FILE);
                }
                outputStream = sUrandomOut;
            }
            return outputStream;
        }
    }

    public static SecureRandom get() {
        if (VERSION.SDK_INT > 18) {
            return new SecureRandom();
        }
        return new FixedSecureRandom();
    }

    private FixedSecureRandom() {
        super(new LinuxPrngSecureRandomSpi(), new LinuxPrngSecureRandomProvider());
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
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("UTF-8 encoding not supported");
        }
    }
}
