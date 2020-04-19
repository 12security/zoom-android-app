package p021us.zoom.androidlib.cache.naming;

import android.util.Log;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* renamed from: us.zoom.androidlib.cache.naming.SH1FileNameGenerator */
public class SH1FileNameGenerator implements FileNameGenerator {
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final int RADIX = 36;

    public String generate(String str) {
        return new BigInteger(getSHA1(str.getBytes())).abs().toString(36);
    }

    private byte[] getSHA1(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(bArr);
            return instance.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(SH1FileNameGenerator.class.getSimpleName(), e.getMessage(), e);
            return null;
        }
    }
}
