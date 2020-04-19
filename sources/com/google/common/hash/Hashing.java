package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.Nullable;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

@Beta
public final class Hashing {
    /* access modifiers changed from: private */
    public static final int GOOD_FAST_HASH_SEED = ((int) System.currentTimeMillis());

    private static class Adler32Holder {
        static final HashFunction ADLER_32 = Hashing.checksumHashFunction(ChecksumType.ADLER_32, "Hashing.adler32()");

        private Adler32Holder() {
        }
    }

    enum ChecksumType implements Supplier<Checksum> {
        CRC_32(32) {
            public Checksum get() {
                return new CRC32();
            }
        },
        ADLER_32(32) {
            public Checksum get() {
                return new Adler32();
            }
        };
        
        /* access modifiers changed from: private */
        public final int bits;

        public abstract Checksum get();

        private ChecksumType(int i) {
            this.bits = i;
        }
    }

    private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
        private final int bits;

        private ConcatenatedHashFunction(HashFunction... hashFunctionArr) {
            super(hashFunctionArr);
            int i = 0;
            for (HashFunction hashFunction : hashFunctionArr) {
                i += hashFunction.bits();
                Preconditions.checkArgument(hashFunction.bits() % 8 == 0, "the number of bits (%s) in hashFunction (%s) must be divisible by 8", hashFunction.bits(), (Object) hashFunction);
            }
            this.bits = i;
        }

        /* access modifiers changed from: 0000 */
        public HashCode makeHash(Hasher[] hasherArr) {
            byte[] bArr = new byte[(this.bits / 8)];
            int i = 0;
            for (Hasher hash : hasherArr) {
                HashCode hash2 = hash.hash();
                i += hash2.writeBytesTo(bArr, i, hash2.bits() / 8);
            }
            return HashCode.fromBytesNoCopy(bArr);
        }

        public int bits() {
            return this.bits;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ConcatenatedHashFunction)) {
                return false;
            }
            return Arrays.equals(this.functions, ((ConcatenatedHashFunction) obj).functions);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.functions) * 31) + this.bits;
        }
    }

    private static class Crc32Holder {
        static final HashFunction CRC_32 = Hashing.checksumHashFunction(ChecksumType.CRC_32, "Hashing.crc32()");

        private Crc32Holder() {
        }
    }

    private static final class Crc32cHolder {
        static final HashFunction CRC_32_C = new Crc32cHashFunction();

        private Crc32cHolder() {
        }
    }

    private static class FarmHashFingerprint64Holder {
        static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();

        private FarmHashFingerprint64Holder() {
        }
    }

    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long j) {
            this.state = j;
        }

        public double nextDouble() {
            this.state = (this.state * 2862933555777941757L) + 1;
            return ((double) (((int) (this.state >>> 33)) + 1)) / 2.147483648E9d;
        }
    }

    private static class Md5Holder {
        static final HashFunction MD5 = new MessageDigestHashFunction(MessageDigestAlgorithms.MD5, "Hashing.md5()");

        private Md5Holder() {
        }
    }

    private static class Murmur3_128Holder {
        static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.GOOD_FAST_HASH_SEED);
        static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);

        private Murmur3_128Holder() {
        }
    }

    private static class Murmur3_32Holder {
        static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.GOOD_FAST_HASH_SEED);
        static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);

        private Murmur3_32Holder() {
        }
    }

    private static class Sha1Holder {
        static final HashFunction SHA_1 = new MessageDigestHashFunction(MessageDigestAlgorithms.SHA_1, "Hashing.sha1()");

        private Sha1Holder() {
        }
    }

    private static class Sha256Holder {
        static final HashFunction SHA_256 = new MessageDigestHashFunction(MessageDigestAlgorithms.SHA_256, "Hashing.sha256()");

        private Sha256Holder() {
        }
    }

    private static class Sha384Holder {
        static final HashFunction SHA_384 = new MessageDigestHashFunction(MessageDigestAlgorithms.SHA_384, "Hashing.sha384()");

        private Sha384Holder() {
        }
    }

    private static class Sha512Holder {
        static final HashFunction SHA_512 = new MessageDigestHashFunction(MessageDigestAlgorithms.SHA_512, "Hashing.sha512()");

        private Sha512Holder() {
        }
    }

    private static class SipHash24Holder {
        static final HashFunction SIP_HASH_24;

        private SipHash24Holder() {
        }

        static {
            SipHashFunction sipHashFunction = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
            SIP_HASH_24 = sipHashFunction;
        }
    }

    public static HashFunction goodFastHash(int i) {
        int checkPositiveAndMakeMultipleOf32 = checkPositiveAndMakeMultipleOf32(i);
        if (checkPositiveAndMakeMultipleOf32 == 32) {
            return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
        }
        if (checkPositiveAndMakeMultipleOf32 <= 128) {
            return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        }
        int i2 = (checkPositiveAndMakeMultipleOf32 + 127) / 128;
        HashFunction[] hashFunctionArr = new HashFunction[i2];
        hashFunctionArr[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        int i3 = GOOD_FAST_HASH_SEED;
        for (int i4 = 1; i4 < i2; i4++) {
            i3 += 1500450271;
            hashFunctionArr[i4] = murmur3_128(i3);
        }
        return new ConcatenatedHashFunction(hashFunctionArr);
    }

    public static HashFunction murmur3_32(int i) {
        return new Murmur3_32HashFunction(i);
    }

    public static HashFunction murmur3_32() {
        return Murmur3_32Holder.MURMUR3_32;
    }

    public static HashFunction murmur3_128(int i) {
        return new Murmur3_128HashFunction(i);
    }

    public static HashFunction murmur3_128() {
        return Murmur3_128Holder.MURMUR3_128;
    }

    public static HashFunction sipHash24() {
        return SipHash24Holder.SIP_HASH_24;
    }

    public static HashFunction sipHash24(long j, long j2) {
        SipHashFunction sipHashFunction = new SipHashFunction(2, 4, j, j2);
        return sipHashFunction;
    }

    public static HashFunction md5() {
        return Md5Holder.MD5;
    }

    public static HashFunction sha1() {
        return Sha1Holder.SHA_1;
    }

    public static HashFunction sha256() {
        return Sha256Holder.SHA_256;
    }

    public static HashFunction sha384() {
        return Sha384Holder.SHA_384;
    }

    public static HashFunction sha512() {
        return Sha512Holder.SHA_512;
    }

    public static HashFunction hmacMd5(Key key) {
        return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
    }

    public static HashFunction hmacMd5(byte[] bArr) {
        return hmacMd5((Key) new SecretKeySpec((byte[]) Preconditions.checkNotNull(bArr), "HmacMD5"));
    }

    public static HashFunction hmacSha1(Key key) {
        return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
    }

    public static HashFunction hmacSha1(byte[] bArr) {
        return hmacSha1((Key) new SecretKeySpec((byte[]) Preconditions.checkNotNull(bArr), "HmacSHA1"));
    }

    public static HashFunction hmacSha256(Key key) {
        return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
    }

    public static HashFunction hmacSha256(byte[] bArr) {
        return hmacSha256((Key) new SecretKeySpec((byte[]) Preconditions.checkNotNull(bArr), "HmacSHA256"));
    }

    public static HashFunction hmacSha512(Key key) {
        return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
    }

    public static HashFunction hmacSha512(byte[] bArr) {
        return hmacSha512((Key) new SecretKeySpec((byte[]) Preconditions.checkNotNull(bArr), "HmacSHA512"));
    }

    private static String hmacToString(String str, Key key) {
        return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[]{str, key.getAlgorithm(), key.getFormat()});
    }

    public static HashFunction crc32c() {
        return Crc32cHolder.CRC_32_C;
    }

    public static HashFunction crc32() {
        return Crc32Holder.CRC_32;
    }

    public static HashFunction adler32() {
        return Adler32Holder.ADLER_32;
    }

    /* access modifiers changed from: private */
    public static HashFunction checksumHashFunction(ChecksumType checksumType, String str) {
        return new ChecksumHashFunction(checksumType, checksumType.bits, str);
    }

    public static HashFunction farmHashFingerprint64() {
        return FarmHashFingerprint64Holder.FARMHASH_FINGERPRINT_64;
    }

    public static int consistentHash(HashCode hashCode, int i) {
        return consistentHash(hashCode.padToLong(), i);
    }

    public static int consistentHash(long j, int i) {
        int i2 = 0;
        Preconditions.checkArgument(i > 0, "buckets must be positive: %s", i);
        LinearCongruentialGenerator linearCongruentialGenerator = new LinearCongruentialGenerator(j);
        while (true) {
            int nextDouble = (int) (((double) (i2 + 1)) / linearCongruentialGenerator.nextDouble());
            if (nextDouble < 0 || nextDouble >= i) {
                return i2;
            }
            i2 = nextDouble;
        }
        return i2;
    }

    public static HashCode combineOrdered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) ((bArr[i] * 37) ^ asBytes2[i]);
            }
        }
        return HashCode.fromBytesNoCopy(bArr);
    }

    public static HashCode combineUnordered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) (bArr[i] + asBytes2[i]);
            }
        }
        return HashCode.fromBytesNoCopy(bArr);
    }

    static int checkPositiveAndMakeMultipleOf32(int i) {
        Preconditions.checkArgument(i > 0, "Number of bits must be positive");
        return (i + 31) & -32;
    }

    public static HashFunction concatenating(HashFunction hashFunction, HashFunction hashFunction2, HashFunction... hashFunctionArr) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(hashFunction);
        arrayList.add(hashFunction2);
        for (HashFunction add : hashFunctionArr) {
            arrayList.add(add);
        }
        return new ConcatenatedHashFunction((HashFunction[]) arrayList.toArray(new HashFunction[0]));
    }

    public static HashFunction concatenating(Iterable<HashFunction> iterable) {
        Preconditions.checkNotNull(iterable);
        ArrayList arrayList = new ArrayList();
        for (HashFunction add : iterable) {
            arrayList.add(add);
        }
        Preconditions.checkArgument(arrayList.size() > 0, "number of hash functions (%s) must be > 0", arrayList.size());
        return new ConcatenatedHashFunction((HashFunction[]) arrayList.toArray(new HashFunction[0]));
    }

    private Hashing() {
    }
}
