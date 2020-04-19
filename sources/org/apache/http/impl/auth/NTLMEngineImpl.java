package org.apache.http.impl.auth;

import com.google.common.base.Ascii;
import com.google.common.primitives.UnsignedBytes;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.http.Consts;

final class NTLMEngineImpl implements NTLMEngine {
    /* access modifiers changed from: private */
    public static final Charset DEFAULT_CHARSET = Consts.ASCII;
    static final int FLAG_DOMAIN_PRESENT = 4096;
    static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
    static final int FLAG_REQUEST_56BIT_ENCRYPTION = Integer.MIN_VALUE;
    static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
    static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
    static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
    static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
    static final int FLAG_REQUEST_NTLMv1 = 512;
    static final int FLAG_REQUEST_OEM_ENCODING = 2;
    static final int FLAG_REQUEST_SEAL = 32;
    static final int FLAG_REQUEST_SIGN = 16;
    static final int FLAG_REQUEST_TARGET = 4;
    static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
    static final int FLAG_REQUEST_VERSION = 33554432;
    static final int FLAG_TARGETINFO_PRESENT = 8388608;
    static final int FLAG_WORKSTATION_PRESENT = 8192;
    /* access modifiers changed from: private */
    public static final byte[] MAGIC_TLS_SERVER_ENDPOINT = "tls-server-end-point:".getBytes(Consts.ASCII);
    static final int MSV_AV_CHANNEL_BINDINGS = 10;
    static final int MSV_AV_DNS_COMPUTER_NAME = 3;
    static final int MSV_AV_DNS_DOMAIN_NAME = 4;
    static final int MSV_AV_DNS_TREE_NAME = 5;
    static final int MSV_AV_EOL = 0;
    static final int MSV_AV_FLAGS = 6;
    static final int MSV_AV_FLAGS_ACCOUNT_AUTH_CONSTAINED = 1;
    static final int MSV_AV_FLAGS_MIC = 2;
    static final int MSV_AV_FLAGS_UNTRUSTED_TARGET_SPN = 4;
    static final int MSV_AV_NB_COMPUTER_NAME = 1;
    static final int MSV_AV_NB_DOMAIN_NAME = 2;
    static final int MSV_AV_SINGLE_HOST = 8;
    static final int MSV_AV_TARGET_NAME = 9;
    static final int MSV_AV_TIMESTAMP = 7;
    /* access modifiers changed from: private */
    public static final SecureRandom RND_GEN;
    /* access modifiers changed from: private */
    public static final byte[] SEAL_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server sealing key magic constant");
    /* access modifiers changed from: private */
    public static final byte[] SEAL_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client sealing key magic constant");
    /* access modifiers changed from: private */
    public static final byte[] SIGNATURE = getNullTerminatedAsciiString("NTLMSSP");
    /* access modifiers changed from: private */
    public static final byte[] SIGN_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server signing key magic constant");
    /* access modifiers changed from: private */
    public static final byte[] SIGN_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client signing key magic constant");
    private static final String TYPE_1_MESSAGE = new Type1Message().getResponse();
    /* access modifiers changed from: private */
    public static final Charset UNICODE_LITTLE_UNMARKED = Charset.forName("UnicodeLittleUnmarked");

    protected static class CipherGen {
        protected final byte[] challenge;
        protected byte[] clientChallenge;
        protected byte[] clientChallenge2;
        protected final long currentTime;
        protected final String domain;
        protected byte[] lanManagerSessionKey;
        protected byte[] lm2SessionResponse;
        protected byte[] lmHash;
        protected byte[] lmResponse;
        protected byte[] lmUserSessionKey;
        protected byte[] lmv2Hash;
        protected byte[] lmv2Response;
        protected byte[] ntlm2SessionResponse;
        protected byte[] ntlm2SessionResponseUserSessionKey;
        protected byte[] ntlmHash;
        protected byte[] ntlmResponse;
        protected byte[] ntlmUserSessionKey;
        protected byte[] ntlmv2Blob;
        protected byte[] ntlmv2Hash;
        protected byte[] ntlmv2Response;
        protected byte[] ntlmv2UserSessionKey;
        protected final String password;
        protected final Random random;
        protected byte[] secondaryKey;
        protected final String target;
        protected final byte[] targetInformation;
        protected byte[] timestamp;
        protected final String user;

        @Deprecated
        public CipherGen(String str, String str2, String str3, byte[] bArr, String str4, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) {
            this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), str, str2, str3, bArr, str4, bArr2, bArr3, bArr4, bArr5, bArr6);
        }

        public CipherGen(Random random2, long j, String str, String str2, String str3, byte[] bArr, String str4, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6) {
            this.lmHash = null;
            this.lmResponse = null;
            this.ntlmHash = null;
            this.ntlmResponse = null;
            this.ntlmv2Hash = null;
            this.lmv2Hash = null;
            this.lmv2Response = null;
            this.ntlmv2Blob = null;
            this.ntlmv2Response = null;
            this.ntlm2SessionResponse = null;
            this.lm2SessionResponse = null;
            this.lmUserSessionKey = null;
            this.ntlmUserSessionKey = null;
            this.ntlmv2UserSessionKey = null;
            this.ntlm2SessionResponseUserSessionKey = null;
            this.lanManagerSessionKey = null;
            this.random = random2;
            this.currentTime = j;
            this.domain = str;
            this.target = str4;
            this.user = str2;
            this.password = str3;
            this.challenge = bArr;
            this.targetInformation = bArr2;
            this.clientChallenge = bArr3;
            this.clientChallenge2 = bArr4;
            this.secondaryKey = bArr5;
            this.timestamp = bArr6;
        }

        @Deprecated
        public CipherGen(String str, String str2, String str3, byte[] bArr, String str4, byte[] bArr2) {
            this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), str, str2, str3, bArr, str4, bArr2);
        }

        public CipherGen(Random random2, long j, String str, String str2, String str3, byte[] bArr, String str4, byte[] bArr2) {
            this(random2, j, str, str2, str3, bArr, str4, bArr2, null, null, null, null);
        }

        public byte[] getClientChallenge() throws NTLMEngineException {
            if (this.clientChallenge == null) {
                this.clientChallenge = NTLMEngineImpl.makeRandomChallenge(this.random);
            }
            return this.clientChallenge;
        }

        public byte[] getClientChallenge2() throws NTLMEngineException {
            if (this.clientChallenge2 == null) {
                this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge(this.random);
            }
            return this.clientChallenge2;
        }

        public byte[] getSecondaryKey() throws NTLMEngineException {
            if (this.secondaryKey == null) {
                this.secondaryKey = NTLMEngineImpl.makeSecondaryKey(this.random);
            }
            return this.secondaryKey;
        }

        public byte[] getLMHash() throws NTLMEngineException {
            if (this.lmHash == null) {
                this.lmHash = NTLMEngineImpl.lmHash(this.password);
            }
            return this.lmHash;
        }

        public byte[] getLMResponse() throws NTLMEngineException {
            if (this.lmResponse == null) {
                this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
            }
            return this.lmResponse;
        }

        public byte[] getNTLMHash() throws NTLMEngineException {
            if (this.ntlmHash == null) {
                this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
            }
            return this.ntlmHash;
        }

        public byte[] getNTLMResponse() throws NTLMEngineException {
            if (this.ntlmResponse == null) {
                this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
            }
            return this.ntlmResponse;
        }

        public byte[] getLMv2Hash() throws NTLMEngineException {
            if (this.lmv2Hash == null) {
                this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash());
            }
            return this.lmv2Hash;
        }

        public byte[] getNTLMv2Hash() throws NTLMEngineException {
            if (this.ntlmv2Hash == null) {
                this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash());
            }
            return this.ntlmv2Hash;
        }

        public byte[] getTimestamp() {
            if (this.timestamp == null) {
                long j = (this.currentTime + 11644473600000L) * 10000;
                this.timestamp = new byte[8];
                for (int i = 0; i < 8; i++) {
                    this.timestamp[i] = (byte) ((int) j);
                    j >>>= 8;
                }
            }
            return this.timestamp;
        }

        public byte[] getNTLMv2Blob() throws NTLMEngineException {
            if (this.ntlmv2Blob == null) {
                this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp());
            }
            return this.ntlmv2Blob;
        }

        public byte[] getNTLMv2Response() throws NTLMEngineException {
            if (this.ntlmv2Response == null) {
                this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob());
            }
            return this.ntlmv2Response;
        }

        public byte[] getLMv2Response() throws NTLMEngineException {
            if (this.lmv2Response == null) {
                this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge());
            }
            return this.lmv2Response;
        }

        public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
            if (this.ntlm2SessionResponse == null) {
                this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
            }
            return this.ntlm2SessionResponse;
        }

        public byte[] getLM2SessionResponse() throws NTLMEngineException {
            if (this.lm2SessionResponse == null) {
                byte[] clientChallenge3 = getClientChallenge();
                this.lm2SessionResponse = new byte[24];
                System.arraycopy(clientChallenge3, 0, this.lm2SessionResponse, 0, clientChallenge3.length);
                byte[] bArr = this.lm2SessionResponse;
                Arrays.fill(bArr, clientChallenge3.length, bArr.length, 0);
            }
            return this.lm2SessionResponse;
        }

        public byte[] getLMUserSessionKey() throws NTLMEngineException {
            if (this.lmUserSessionKey == null) {
                this.lmUserSessionKey = new byte[16];
                System.arraycopy(getLMHash(), 0, this.lmUserSessionKey, 0, 8);
                Arrays.fill(this.lmUserSessionKey, 8, 16, 0);
            }
            return this.lmUserSessionKey;
        }

        public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
            if (this.ntlmUserSessionKey == null) {
                MD4 md4 = new MD4();
                md4.update(getNTLMHash());
                this.ntlmUserSessionKey = md4.getOutput();
            }
            return this.ntlmUserSessionKey;
        }

        public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
            if (this.ntlmv2UserSessionKey == null) {
                byte[] nTLMv2Hash = getNTLMv2Hash();
                byte[] bArr = new byte[16];
                System.arraycopy(getNTLMv2Response(), 0, bArr, 0, 16);
                this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(bArr, nTLMv2Hash);
            }
            return this.ntlmv2UserSessionKey;
        }

        public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
            if (this.ntlm2SessionResponseUserSessionKey == null) {
                byte[] lM2SessionResponse = getLM2SessionResponse();
                byte[] bArr = this.challenge;
                byte[] bArr2 = new byte[(bArr.length + lM2SessionResponse.length)];
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                System.arraycopy(lM2SessionResponse, 0, bArr2, this.challenge.length, lM2SessionResponse.length);
                this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(bArr2, getNTLMUserSessionKey());
            }
            return this.ntlm2SessionResponseUserSessionKey;
        }

        public byte[] getLanManagerSessionKey() throws NTLMEngineException {
            if (this.lanManagerSessionKey == null) {
                try {
                    byte[] bArr = new byte[14];
                    System.arraycopy(getLMHash(), 0, bArr, 0, 8);
                    Arrays.fill(bArr, 8, bArr.length, -67);
                    Key access$1000 = NTLMEngineImpl.createDESKey(bArr, 0);
                    Key access$10002 = NTLMEngineImpl.createDESKey(bArr, 7);
                    byte[] bArr2 = new byte[8];
                    System.arraycopy(getLMResponse(), 0, bArr2, 0, bArr2.length);
                    Cipher instance = Cipher.getInstance("DES/ECB/NoPadding");
                    instance.init(1, access$1000);
                    byte[] doFinal = instance.doFinal(bArr2);
                    Cipher instance2 = Cipher.getInstance("DES/ECB/NoPadding");
                    instance2.init(1, access$10002);
                    byte[] doFinal2 = instance2.doFinal(bArr2);
                    this.lanManagerSessionKey = new byte[16];
                    System.arraycopy(doFinal, 0, this.lanManagerSessionKey, 0, doFinal.length);
                    System.arraycopy(doFinal2, 0, this.lanManagerSessionKey, doFinal.length, doFinal2.length);
                } catch (Exception e) {
                    throw new NTLMEngineException(e.getMessage(), e);
                }
            }
            return this.lanManagerSessionKey;
        }
    }

    static class HMACMD5 {
        protected final byte[] ipad = new byte[64];
        protected final MessageDigest md5 = NTLMEngineImpl.getMD5();
        protected final byte[] opad = new byte[64];

        HMACMD5(byte[] bArr) {
            int length = bArr.length;
            if (length > 64) {
                this.md5.update(bArr);
                bArr = this.md5.digest();
                length = bArr.length;
            }
            int i = 0;
            while (i < length) {
                this.ipad[i] = (byte) (54 ^ bArr[i]);
                this.opad[i] = (byte) (92 ^ bArr[i]);
                i++;
            }
            while (i < 64) {
                this.ipad[i] = 54;
                this.opad[i] = 92;
                i++;
            }
            this.md5.reset();
            this.md5.update(this.ipad);
        }

        /* access modifiers changed from: 0000 */
        public byte[] getOutput() {
            byte[] digest = this.md5.digest();
            this.md5.update(this.opad);
            return this.md5.digest(digest);
        }

        /* access modifiers changed from: 0000 */
        public void update(byte[] bArr) {
            this.md5.update(bArr);
        }

        /* access modifiers changed from: 0000 */
        public void update(byte[] bArr, int i, int i2) {
            this.md5.update(bArr, i, i2);
        }
    }

    static class Handle {
        private final byte[] exportedSessionKey;
        private final boolean isConnection;
        final Mode mode;
        private final Cipher rc4;
        private byte[] sealingKey;
        int sequenceNumber = 0;
        private byte[] signingKey;

        Handle(byte[] bArr, Mode mode2, boolean z) throws NTLMEngineException {
            this.exportedSessionKey = bArr;
            this.isConnection = z;
            this.mode = mode2;
            try {
                MessageDigest md5 = NTLMEngineImpl.getMD5();
                MessageDigest md52 = NTLMEngineImpl.getMD5();
                md5.update(bArr);
                md52.update(bArr);
                if (mode2 == Mode.CLIENT) {
                    md5.update(NTLMEngineImpl.SIGN_MAGIC_CLIENT);
                    md52.update(NTLMEngineImpl.SEAL_MAGIC_CLIENT);
                } else {
                    md5.update(NTLMEngineImpl.SIGN_MAGIC_SERVER);
                    md52.update(NTLMEngineImpl.SEAL_MAGIC_SERVER);
                }
                this.signingKey = md5.digest();
                this.sealingKey = md52.digest();
                this.rc4 = initCipher();
            } catch (Exception e) {
                throw new NTLMEngineException(e.getMessage(), e);
            }
        }

        public byte[] getSigningKey() {
            return this.signingKey;
        }

        public byte[] getSealingKey() {
            return this.sealingKey;
        }

        private Cipher initCipher() throws NTLMEngineException {
            try {
                Cipher instance = Cipher.getInstance("RC4");
                if (this.mode == Mode.CLIENT) {
                    instance.init(1, new SecretKeySpec(this.sealingKey, "RC4"));
                } else {
                    instance.init(2, new SecretKeySpec(this.sealingKey, "RC4"));
                }
                return instance;
            } catch (Exception e) {
                throw new NTLMEngineException(e.getMessage(), e);
            }
        }

        private void advanceMessageSequence() throws NTLMEngineException {
            if (!this.isConnection) {
                MessageDigest md5 = NTLMEngineImpl.getMD5();
                md5.update(this.sealingKey);
                byte[] bArr = new byte[4];
                NTLMEngineImpl.writeULong(bArr, this.sequenceNumber, 0);
                md5.update(bArr);
                this.sealingKey = md5.digest();
                initCipher();
            }
            this.sequenceNumber++;
        }

        private byte[] encrypt(byte[] bArr) throws NTLMEngineException {
            return this.rc4.update(bArr);
        }

        private byte[] decrypt(byte[] bArr) throws NTLMEngineException {
            return this.rc4.update(bArr);
        }

        private byte[] computeSignature(byte[] bArr) throws NTLMEngineException {
            byte[] bArr2 = new byte[16];
            bArr2[0] = 1;
            bArr2[1] = 0;
            bArr2[2] = 0;
            bArr2[3] = 0;
            HMACMD5 hmacmd5 = new HMACMD5(this.signingKey);
            hmacmd5.update(NTLMEngineImpl.encodeLong(this.sequenceNumber));
            hmacmd5.update(bArr);
            byte[] bArr3 = new byte[8];
            System.arraycopy(hmacmd5.getOutput(), 0, bArr3, 0, 8);
            System.arraycopy(encrypt(bArr3), 0, bArr2, 4, 8);
            NTLMEngineImpl.encodeLong(bArr2, 12, this.sequenceNumber);
            return bArr2;
        }

        private boolean validateSignature(byte[] bArr, byte[] bArr2) throws NTLMEngineException {
            return Arrays.equals(bArr, computeSignature(bArr2));
        }

        public byte[] signAndEncryptMessage(byte[] bArr) throws NTLMEngineException {
            byte[] encrypt = encrypt(bArr);
            byte[] computeSignature = computeSignature(bArr);
            byte[] bArr2 = new byte[(computeSignature.length + encrypt.length)];
            System.arraycopy(computeSignature, 0, bArr2, 0, computeSignature.length);
            System.arraycopy(encrypt, 0, bArr2, computeSignature.length, encrypt.length);
            advanceMessageSequence();
            return bArr2;
        }

        public byte[] decryptAndVerifySignedMessage(byte[] bArr) throws NTLMEngineException {
            byte[] bArr2 = new byte[16];
            System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
            byte[] bArr3 = new byte[(bArr.length - 16)];
            System.arraycopy(bArr, 16, bArr3, 0, bArr3.length);
            byte[] decrypt = decrypt(bArr3);
            if (validateSignature(bArr2, decrypt)) {
                advanceMessageSequence();
                return decrypt;
            }
            throw new NTLMEngineException("Wrong signature");
        }
    }

    static class MD4 {

        /* renamed from: A */
        protected int f483A = 1732584193;

        /* renamed from: B */
        protected int f484B = -271733879;

        /* renamed from: C */
        protected int f485C = -1732584194;

        /* renamed from: D */
        protected int f486D = 271733878;
        protected long count = 0;
        protected final byte[] dataBuffer = new byte[64];

        MD4() {
        }

        /* access modifiers changed from: 0000 */
        public void update(byte[] bArr) {
            byte[] bArr2;
            int i = (int) (this.count & 63);
            int i2 = 0;
            while (true) {
                int length = (bArr.length - i2) + i;
                bArr2 = this.dataBuffer;
                if (length < bArr2.length) {
                    break;
                }
                int length2 = bArr2.length - i;
                System.arraycopy(bArr, i2, bArr2, i, length2);
                this.count += (long) length2;
                i2 += length2;
                processBuffer();
                i = 0;
            }
            if (i2 < bArr.length) {
                int length3 = bArr.length - i2;
                System.arraycopy(bArr, i2, bArr2, i, length3);
                this.count += (long) length3;
            }
        }

        /* access modifiers changed from: 0000 */
        public byte[] getOutput() {
            int i = (int) (this.count & 63);
            int i2 = i < 56 ? 56 - i : 120 - i;
            byte[] bArr = new byte[(i2 + 8)];
            bArr[0] = UnsignedBytes.MAX_POWER_OF_TWO;
            for (int i3 = 0; i3 < 8; i3++) {
                bArr[i2 + i3] = (byte) ((int) ((this.count * 8) >>> (i3 * 8)));
            }
            update(bArr);
            byte[] bArr2 = new byte[16];
            NTLMEngineImpl.writeULong(bArr2, this.f483A, 0);
            NTLMEngineImpl.writeULong(bArr2, this.f484B, 4);
            NTLMEngineImpl.writeULong(bArr2, this.f485C, 8);
            NTLMEngineImpl.writeULong(bArr2, this.f486D, 12);
            return bArr2;
        }

        /* access modifiers changed from: protected */
        public void processBuffer() {
            int[] iArr = new int[16];
            for (int i = 0; i < 16; i++) {
                byte[] bArr = this.dataBuffer;
                int i2 = i * 4;
                iArr[i] = (bArr[i2] & UnsignedBytes.MAX_VALUE) + ((bArr[i2 + 1] & UnsignedBytes.MAX_VALUE) << 8) + ((bArr[i2 + 2] & UnsignedBytes.MAX_VALUE) << Ascii.DLE) + ((bArr[i2 + 3] & UnsignedBytes.MAX_VALUE) << Ascii.CAN);
            }
            int i3 = this.f483A;
            int i4 = this.f484B;
            int i5 = this.f485C;
            int i6 = this.f486D;
            round1(iArr);
            round2(iArr);
            round3(iArr);
            this.f483A += i3;
            this.f484B += i4;
            this.f485C += i5;
            this.f486D += i6;
        }

        /* access modifiers changed from: protected */
        public void round1(int[] iArr) {
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m275F(this.f484B, this.f485C, this.f486D) + iArr[0], 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m275F(this.f483A, this.f484B, this.f485C) + iArr[1], 7);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m275F(this.f486D, this.f483A, this.f484B) + iArr[2], 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m275F(this.f485C, this.f486D, this.f483A) + iArr[3], 19);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m275F(this.f484B, this.f485C, this.f486D) + iArr[4], 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m275F(this.f483A, this.f484B, this.f485C) + iArr[5], 7);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m275F(this.f486D, this.f483A, this.f484B) + iArr[6], 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m275F(this.f485C, this.f486D, this.f483A) + iArr[7], 19);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m275F(this.f484B, this.f485C, this.f486D) + iArr[8], 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m275F(this.f483A, this.f484B, this.f485C) + iArr[9], 7);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m275F(this.f486D, this.f483A, this.f484B) + iArr[10], 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m275F(this.f485C, this.f486D, this.f483A) + iArr[11], 19);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m275F(this.f484B, this.f485C, this.f486D) + iArr[12], 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m275F(this.f483A, this.f484B, this.f485C) + iArr[13], 7);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m275F(this.f486D, this.f483A, this.f484B) + iArr[14], 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m275F(this.f485C, this.f486D, this.f483A) + iArr[15], 19);
        }

        /* access modifiers changed from: protected */
        public void round2(int[] iArr) {
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m276G(this.f484B, this.f485C, this.f486D) + iArr[0] + 1518500249, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m276G(this.f483A, this.f484B, this.f485C) + iArr[4] + 1518500249, 5);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m276G(this.f486D, this.f483A, this.f484B) + iArr[8] + 1518500249, 9);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m276G(this.f485C, this.f486D, this.f483A) + iArr[12] + 1518500249, 13);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m276G(this.f484B, this.f485C, this.f486D) + iArr[1] + 1518500249, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m276G(this.f483A, this.f484B, this.f485C) + iArr[5] + 1518500249, 5);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m276G(this.f486D, this.f483A, this.f484B) + iArr[9] + 1518500249, 9);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m276G(this.f485C, this.f486D, this.f483A) + iArr[13] + 1518500249, 13);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m276G(this.f484B, this.f485C, this.f486D) + iArr[2] + 1518500249, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m276G(this.f483A, this.f484B, this.f485C) + iArr[6] + 1518500249, 5);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m276G(this.f486D, this.f483A, this.f484B) + iArr[10] + 1518500249, 9);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m276G(this.f485C, this.f486D, this.f483A) + iArr[14] + 1518500249, 13);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m276G(this.f484B, this.f485C, this.f486D) + iArr[3] + 1518500249, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m276G(this.f483A, this.f484B, this.f485C) + iArr[7] + 1518500249, 5);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m276G(this.f486D, this.f483A, this.f484B) + iArr[11] + 1518500249, 9);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m276G(this.f485C, this.f486D, this.f483A) + iArr[15] + 1518500249, 13);
        }

        /* access modifiers changed from: protected */
        public void round3(int[] iArr) {
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m277H(this.f484B, this.f485C, this.f486D) + iArr[0] + 1859775393, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m277H(this.f483A, this.f484B, this.f485C) + iArr[8] + 1859775393, 9);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m277H(this.f486D, this.f483A, this.f484B) + iArr[4] + 1859775393, 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m277H(this.f485C, this.f486D, this.f483A) + iArr[12] + 1859775393, 15);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m277H(this.f484B, this.f485C, this.f486D) + iArr[2] + 1859775393, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m277H(this.f483A, this.f484B, this.f485C) + iArr[10] + 1859775393, 9);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m277H(this.f486D, this.f483A, this.f484B) + iArr[6] + 1859775393, 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m277H(this.f485C, this.f486D, this.f483A) + iArr[14] + 1859775393, 15);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m277H(this.f484B, this.f485C, this.f486D) + iArr[1] + 1859775393, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m277H(this.f483A, this.f484B, this.f485C) + iArr[9] + 1859775393, 9);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m277H(this.f486D, this.f483A, this.f484B) + iArr[5] + 1859775393, 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m277H(this.f485C, this.f486D, this.f483A) + iArr[13] + 1859775393, 15);
            this.f483A = NTLMEngineImpl.rotintlft(this.f483A + NTLMEngineImpl.m277H(this.f484B, this.f485C, this.f486D) + iArr[3] + 1859775393, 3);
            this.f486D = NTLMEngineImpl.rotintlft(this.f486D + NTLMEngineImpl.m277H(this.f483A, this.f484B, this.f485C) + iArr[11] + 1859775393, 9);
            this.f485C = NTLMEngineImpl.rotintlft(this.f485C + NTLMEngineImpl.m277H(this.f486D, this.f483A, this.f484B) + iArr[7] + 1859775393, 11);
            this.f484B = NTLMEngineImpl.rotintlft(this.f484B + NTLMEngineImpl.m277H(this.f485C, this.f486D, this.f483A) + iArr[15] + 1859775393, 15);
        }
    }

    enum Mode {
        CLIENT,
        SERVER
    }

    static class NTLMMessage {
        protected int currentOutputPosition;
        protected byte[] messageContents;

        NTLMMessage() {
            this.messageContents = null;
            this.currentOutputPosition = 0;
        }

        NTLMMessage(String str, int i) throws NTLMEngineException {
            this(Base64.decodeBase64(str.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)), i);
        }

        NTLMMessage(byte[] bArr, int i) throws NTLMEngineException {
            this.messageContents = null;
            int i2 = 0;
            this.currentOutputPosition = 0;
            this.messageContents = bArr;
            if (this.messageContents.length >= NTLMEngineImpl.SIGNATURE.length) {
                while (i2 < NTLMEngineImpl.SIGNATURE.length) {
                    if (this.messageContents[i2] == NTLMEngineImpl.SIGNATURE[i2]) {
                        i2++;
                    } else {
                        throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
                    }
                }
                int readULong = readULong(NTLMEngineImpl.SIGNATURE.length);
                if (readULong == i) {
                    this.currentOutputPosition = this.messageContents.length;
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("NTLM type ");
                sb.append(Integer.toString(i));
                sb.append(" message expected - instead got type ");
                sb.append(Integer.toString(readULong));
                throw new NTLMEngineException(sb.toString());
            }
            throw new NTLMEngineException("NTLM message decoding error - packet too short");
        }

        /* access modifiers changed from: protected */
        public int getPreambleLength() {
            return NTLMEngineImpl.SIGNATURE.length + 4;
        }

        /* access modifiers changed from: protected */
        public int getMessageLength() {
            return this.currentOutputPosition;
        }

        /* access modifiers changed from: protected */
        public byte readByte(int i) throws NTLMEngineException {
            byte[] bArr = this.messageContents;
            if (bArr.length >= i + 1) {
                return bArr[i];
            }
            throw new NTLMEngineException("NTLM: Message too short");
        }

        /* access modifiers changed from: protected */
        public void readBytes(byte[] bArr, int i) throws NTLMEngineException {
            byte[] bArr2 = this.messageContents;
            if (bArr2.length >= bArr.length + i) {
                System.arraycopy(bArr2, i, bArr, 0, bArr.length);
                return;
            }
            throw new NTLMEngineException("NTLM: Message too short");
        }

        /* access modifiers changed from: protected */
        public int readUShort(int i) throws NTLMEngineException {
            return NTLMEngineImpl.readUShort(this.messageContents, i);
        }

        /* access modifiers changed from: protected */
        public int readULong(int i) throws NTLMEngineException {
            return NTLMEngineImpl.readULong(this.messageContents, i);
        }

        /* access modifiers changed from: protected */
        public byte[] readSecurityBuffer(int i) throws NTLMEngineException {
            return NTLMEngineImpl.readSecurityBuffer(this.messageContents, i);
        }

        /* access modifiers changed from: protected */
        public void prepareResponse(int i, int i2) {
            this.messageContents = new byte[i];
            this.currentOutputPosition = 0;
            addBytes(NTLMEngineImpl.SIGNATURE);
            addULong(i2);
        }

        /* access modifiers changed from: protected */
        public void addByte(byte b) {
            byte[] bArr = this.messageContents;
            int i = this.currentOutputPosition;
            bArr[i] = b;
            this.currentOutputPosition = i + 1;
        }

        /* access modifiers changed from: protected */
        public void addBytes(byte[] bArr) {
            if (bArr != null) {
                for (byte b : bArr) {
                    byte[] bArr2 = this.messageContents;
                    int i = this.currentOutputPosition;
                    bArr2[i] = b;
                    this.currentOutputPosition = i + 1;
                }
            }
        }

        /* access modifiers changed from: protected */
        public void addUShort(int i) {
            addByte((byte) (i & 255));
            addByte((byte) ((i >> 8) & 255));
        }

        /* access modifiers changed from: protected */
        public void addULong(int i) {
            addByte((byte) (i & 255));
            addByte((byte) ((i >> 8) & 255));
            addByte((byte) ((i >> 16) & 255));
            addByte((byte) ((i >> 24) & 255));
        }

        public String getResponse() {
            return new String(Base64.encodeBase64(getBytes()), Consts.ASCII);
        }

        public byte[] getBytes() {
            if (this.messageContents == null) {
                buildMessage();
            }
            byte[] bArr = this.messageContents;
            int length = bArr.length;
            int i = this.currentOutputPosition;
            if (length > i) {
                byte[] bArr2 = new byte[i];
                System.arraycopy(bArr, 0, bArr2, 0, i);
                this.messageContents = bArr2;
            }
            return this.messageContents;
        }

        /* access modifiers changed from: protected */
        public void buildMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append("Message builder not implemented for ");
            sb.append(getClass().getName());
            throw new RuntimeException(sb.toString());
        }
    }

    static class Type1Message extends NTLMMessage {
        private final byte[] domainBytes;
        private final int flags;
        private final byte[] hostBytes;

        private int getDefaultFlags() {
            return -1576500735;
        }

        Type1Message(String str, String str2) throws NTLMEngineException {
            this(str, str2, null);
        }

        Type1Message(String str, String str2, Integer num) throws NTLMEngineException {
            this.flags = num == null ? getDefaultFlags() : num.intValue();
            String access$2200 = NTLMEngineImpl.convertHost(str2);
            String access$2300 = NTLMEngineImpl.convertDomain(str);
            byte[] bArr = null;
            this.hostBytes = access$2200 != null ? access$2200.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
            if (access$2300 != null) {
                bArr = access$2300.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
            }
            this.domainBytes = bArr;
        }

        Type1Message() {
            this.hostBytes = null;
            this.domainBytes = null;
            this.flags = getDefaultFlags();
        }

        /* access modifiers changed from: protected */
        public void buildMessage() {
            byte[] bArr = this.domainBytes;
            int i = 0;
            int length = bArr != null ? bArr.length : 0;
            byte[] bArr2 = this.hostBytes;
            if (bArr2 != null) {
                i = bArr2.length;
            }
            prepareResponse(i + 40 + length, 1);
            addULong(this.flags);
            addUShort(length);
            addUShort(length);
            addULong(i + 32 + 8);
            addUShort(i);
            addUShort(i);
            addULong(40);
            addUShort(261);
            addULong(2600);
            addUShort(3840);
            byte[] bArr3 = this.hostBytes;
            if (bArr3 != null) {
                addBytes(bArr3);
            }
            byte[] bArr4 = this.domainBytes;
            if (bArr4 != null) {
                addBytes(bArr4);
            }
        }
    }

    static class Type2Message extends NTLMMessage {
        protected final byte[] challenge;
        protected final int flags;
        protected String target;
        protected byte[] targetInfo;

        Type2Message(String str) throws NTLMEngineException {
            this(Base64.decodeBase64(str.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)));
        }

        Type2Message(byte[] bArr) throws NTLMEngineException {
            super(bArr, 2);
            this.challenge = new byte[8];
            readBytes(this.challenge, 24);
            this.flags = readULong(20);
            this.target = null;
            if (getMessageLength() >= 20) {
                byte[] readSecurityBuffer = readSecurityBuffer(12);
                if (readSecurityBuffer.length != 0) {
                    this.target = new String(readSecurityBuffer, NTLMEngineImpl.getCharset(this.flags));
                }
            }
            this.targetInfo = null;
            if (getMessageLength() >= 48) {
                byte[] readSecurityBuffer2 = readSecurityBuffer(40);
                if (readSecurityBuffer2.length != 0) {
                    this.targetInfo = readSecurityBuffer2;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public byte[] getChallenge() {
            return this.challenge;
        }

        /* access modifiers changed from: 0000 */
        public String getTarget() {
            return this.target;
        }

        /* access modifiers changed from: 0000 */
        public byte[] getTargetInfo() {
            return this.targetInfo;
        }

        /* access modifiers changed from: 0000 */
        public int getFlags() {
            return this.flags;
        }
    }

    static class Type3Message extends NTLMMessage {
        protected final boolean computeMic;
        protected final byte[] domainBytes;
        protected final byte[] exportedSessionKey;
        protected final byte[] hostBytes;
        protected byte[] lmResp;
        protected byte[] ntResp;
        protected final byte[] sessionKey;
        protected final byte[] type1Message;
        protected final int type2Flags;
        protected final byte[] type2Message;
        protected final byte[] userBytes;

        Type3Message(String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2) throws NTLMEngineException {
            this(str, str2, str3, str4, bArr, i, str5, bArr2, null, null, null);
        }

        Type3Message(Random random, long j, String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2) throws NTLMEngineException {
            this(random, j, str, str2, str3, str4, bArr, i, str5, bArr2, null, null, null);
        }

        Type3Message(String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2, Certificate certificate, byte[] bArr3, byte[] bArr4) throws NTLMEngineException {
            this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), str, str2, str3, str4, bArr, i, str5, bArr2, certificate, bArr3, bArr4);
        }

        Type3Message(Random random, long j, String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2, Certificate certificate, byte[] bArr3, byte[] bArr4) throws NTLMEngineException {
            byte[] bArr5;
            byte[] bArr6;
            int i2 = i;
            byte[] bArr7 = bArr2;
            Certificate certificate2 = certificate;
            if (random != null) {
                this.type2Flags = i2;
                this.type1Message = bArr3;
                this.type2Message = bArr4;
                String access$2200 = NTLMEngineImpl.convertHost(str2);
                String access$2300 = NTLMEngineImpl.convertDomain(str);
                if (certificate2 != null) {
                    byte[] addGssMicAvsToTargetInfo = addGssMicAvsToTargetInfo(bArr7, certificate2);
                    this.computeMic = true;
                    bArr5 = addGssMicAvsToTargetInfo;
                } else {
                    this.computeMic = false;
                    bArr5 = bArr7;
                }
                CipherGen cipherGen = new CipherGen(random, j, access$2300, str3, str4, bArr, str5, bArr5);
                if ((8388608 & i2) != 0 && bArr7 != null && str5 != null) {
                    try {
                        this.ntResp = cipherGen.getNTLMv2Response();
                        this.lmResp = cipherGen.getLMv2Response();
                        if ((i2 & 128) != 0) {
                            bArr6 = cipherGen.getLanManagerSessionKey();
                        } else {
                            bArr6 = cipherGen.getNTLMv2UserSessionKey();
                        }
                    } catch (NTLMEngineException unused) {
                        this.ntResp = new byte[0];
                        this.lmResp = cipherGen.getLMResponse();
                        if ((i2 & 128) != 0) {
                            bArr6 = cipherGen.getLanManagerSessionKey();
                        } else {
                            bArr6 = cipherGen.getLMUserSessionKey();
                        }
                    }
                } else if ((524288 & i2) != 0) {
                    this.ntResp = cipherGen.getNTLM2SessionResponse();
                    this.lmResp = cipherGen.getLM2SessionResponse();
                    if ((i2 & 128) != 0) {
                        bArr6 = cipherGen.getLanManagerSessionKey();
                    } else {
                        bArr6 = cipherGen.getNTLM2SessionResponseUserSessionKey();
                    }
                } else {
                    this.ntResp = cipherGen.getNTLMResponse();
                    this.lmResp = cipherGen.getLMResponse();
                    if ((i2 & 128) != 0) {
                        bArr6 = cipherGen.getLanManagerSessionKey();
                    } else {
                        bArr6 = cipherGen.getNTLMUserSessionKey();
                    }
                }
                byte[] bArr8 = null;
                if ((i2 & 16) != 0) {
                    if ((1073741824 & i2) != 0) {
                        this.exportedSessionKey = cipherGen.getSecondaryKey();
                        this.sessionKey = NTLMEngineImpl.RC4(this.exportedSessionKey, bArr6);
                    } else {
                        this.sessionKey = bArr6;
                        this.exportedSessionKey = this.sessionKey;
                    }
                } else if (!this.computeMic) {
                    this.sessionKey = null;
                    this.exportedSessionKey = null;
                } else {
                    throw new NTLMEngineException("Cannot sign/seal: no exported session key");
                }
                Charset access$2500 = NTLMEngineImpl.getCharset(i);
                this.hostBytes = access$2200 != null ? access$2200.getBytes(access$2500) : null;
                if (access$2300 != null) {
                    bArr8 = access$2300.toUpperCase(Locale.ROOT).getBytes(access$2500);
                }
                this.domainBytes = bArr8;
                this.userBytes = str3.getBytes(access$2500);
                return;
            }
            throw new NTLMEngineException("Random generator not available");
        }

        public byte[] getEncryptedRandomSessionKey() {
            return this.sessionKey;
        }

        public byte[] getExportedSessionKey() {
            return this.exportedSessionKey;
        }

        /* access modifiers changed from: protected */
        public void buildMessage() {
            int length = this.ntResp.length;
            int length2 = this.lmResp.length;
            byte[] bArr = this.domainBytes;
            int length3 = bArr != null ? bArr.length : 0;
            byte[] bArr2 = this.hostBytes;
            int length4 = bArr2 != null ? bArr2.length : 0;
            int length5 = this.userBytes.length;
            byte[] bArr3 = this.sessionKey;
            int length6 = bArr3 != null ? bArr3.length : 0;
            int i = (this.computeMic ? 16 : 0) + 72;
            int i2 = i + length2;
            int i3 = i2 + length;
            int i4 = i3 + length3;
            int i5 = i4 + length5;
            int i6 = i5 + length4;
            prepareResponse(i6 + length6, 3);
            addUShort(length2);
            addUShort(length2);
            addULong(i);
            addUShort(length);
            addUShort(length);
            addULong(i2);
            addUShort(length3);
            addUShort(length3);
            addULong(i3);
            addUShort(length5);
            addUShort(length5);
            addULong(i4);
            addUShort(length4);
            addUShort(length4);
            addULong(i5);
            addUShort(length6);
            addUShort(length6);
            addULong(i6);
            addULong(this.type2Flags);
            addUShort(261);
            addULong(2600);
            addUShort(3840);
            int i7 = -1;
            if (this.computeMic) {
                i7 = this.currentOutputPosition;
                this.currentOutputPosition += 16;
            }
            addBytes(this.lmResp);
            addBytes(this.ntResp);
            addBytes(this.domainBytes);
            addBytes(this.userBytes);
            addBytes(this.hostBytes);
            byte[] bArr4 = this.sessionKey;
            if (bArr4 != null) {
                addBytes(bArr4);
            }
            if (this.computeMic) {
                HMACMD5 hmacmd5 = new HMACMD5(this.exportedSessionKey);
                hmacmd5.update(this.type1Message);
                hmacmd5.update(this.type2Message);
                hmacmd5.update(this.messageContents);
                byte[] output = hmacmd5.getOutput();
                System.arraycopy(output, 0, this.messageContents, i7, output.length);
            }
        }

        private byte[] addGssMicAvsToTargetInfo(byte[] bArr, Certificate certificate) throws NTLMEngineException {
            byte[] bArr2 = new byte[(bArr.length + 8 + 20)];
            int length = bArr.length - 4;
            System.arraycopy(bArr, 0, bArr2, 0, length);
            NTLMEngineImpl.writeUShort(bArr2, 6, length);
            NTLMEngineImpl.writeUShort(bArr2, 4, length + 2);
            NTLMEngineImpl.writeULong(bArr2, 2, length + 4);
            NTLMEngineImpl.writeUShort(bArr2, 10, length + 8);
            NTLMEngineImpl.writeUShort(bArr2, 16, length + 10);
            try {
                byte[] digest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256).digest(certificate.getEncoded());
                byte[] bArr3 = new byte[(NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length + 20 + digest.length)];
                NTLMEngineImpl.writeULong(bArr3, 53, 16);
                System.arraycopy(NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT, 0, bArr3, 20, NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length);
                System.arraycopy(digest, 0, bArr3, NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length + 20, digest.length);
                System.arraycopy(NTLMEngineImpl.getMD5().digest(bArr3), 0, bArr2, length + 12, 16);
                return bArr2;
            } catch (CertificateEncodingException e) {
                throw new NTLMEngineException(e.getMessage(), e);
            } catch (NoSuchAlgorithmException e2) {
                throw new NTLMEngineException(e2.getMessage(), e2);
            }
        }
    }

    /* renamed from: F */
    static int m275F(int i, int i2, int i3) {
        return ((~i) & i3) | (i2 & i);
    }

    /* renamed from: G */
    static int m276G(int i, int i2, int i3) {
        return (i & i3) | (i & i2) | (i2 & i3);
    }

    /* renamed from: H */
    static int m277H(int i, int i2, int i3) {
        return (i ^ i2) ^ i3;
    }

    static int rotintlft(int i, int i2) {
        return (i >>> (32 - i2)) | (i << i2);
    }

    static {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception unused) {
            secureRandom = null;
        }
        RND_GEN = secureRandom;
    }

    private static byte[] getNullTerminatedAsciiString(String str) {
        byte[] bytes = str.getBytes(Consts.ASCII);
        byte[] bArr = new byte[(bytes.length + 1)];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        bArr[bytes.length] = 0;
        return bArr;
    }

    NTLMEngineImpl() {
    }

    static String getType1Message(String str, String str2) {
        return TYPE_1_MESSAGE;
    }

    static String getType3Message(String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2) throws NTLMEngineException {
        Type3Message type3Message = new Type3Message(str4, str3, str, str2, bArr, i, str5, bArr2);
        return type3Message.getResponse();
    }

    static String getType3Message(String str, String str2, String str3, String str4, byte[] bArr, int i, String str5, byte[] bArr2, Certificate certificate, byte[] bArr3, byte[] bArr4) throws NTLMEngineException {
        Type3Message type3Message = new Type3Message(str4, str3, str, str2, bArr, i, str5, bArr2, certificate, bArr3, bArr4);
        return type3Message.getResponse();
    }

    /* access modifiers changed from: private */
    public static int readULong(byte[] bArr, int i) throws NTLMEngineException {
        if (bArr.length < i + 4) {
            return 0;
        }
        return ((bArr[i + 3] & UnsignedBytes.MAX_VALUE) << Ascii.CAN) | (bArr[i] & UnsignedBytes.MAX_VALUE) | ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | ((bArr[i + 2] & UnsignedBytes.MAX_VALUE) << Ascii.DLE);
    }

    /* access modifiers changed from: private */
    public static int readUShort(byte[] bArr, int i) throws NTLMEngineException {
        if (bArr.length < i + 2) {
            return 0;
        }
        return ((bArr[i + 1] & UnsignedBytes.MAX_VALUE) << 8) | (bArr[i] & UnsignedBytes.MAX_VALUE);
    }

    /* access modifiers changed from: private */
    public static byte[] readSecurityBuffer(byte[] bArr, int i) throws NTLMEngineException {
        int readUShort = readUShort(bArr, i);
        int readULong = readULong(bArr, i + 4);
        if (bArr.length < readULong + readUShort) {
            return new byte[readUShort];
        }
        byte[] bArr2 = new byte[readUShort];
        System.arraycopy(bArr, readULong, bArr2, 0, readUShort);
        return bArr2;
    }

    /* access modifiers changed from: private */
    public static byte[] makeRandomChallenge(Random random) throws NTLMEngineException {
        byte[] bArr = new byte[8];
        synchronized (random) {
            random.nextBytes(bArr);
        }
        return bArr;
    }

    /* access modifiers changed from: private */
    public static byte[] makeSecondaryKey(Random random) throws NTLMEngineException {
        byte[] bArr = new byte[16];
        synchronized (random) {
            random.nextBytes(bArr);
        }
        return bArr;
    }

    static byte[] hmacMD5(byte[] bArr, byte[] bArr2) throws NTLMEngineException {
        HMACMD5 hmacmd5 = new HMACMD5(bArr2);
        hmacmd5.update(bArr);
        return hmacmd5.getOutput();
    }

    static byte[] RC4(byte[] bArr, byte[] bArr2) throws NTLMEngineException {
        try {
            Cipher instance = Cipher.getInstance("RC4");
            instance.init(1, new SecretKeySpec(bArr2, "RC4"));
            return instance.doFinal(bArr);
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    static byte[] ntlm2SessionResponse(byte[] bArr, byte[] bArr2, byte[] bArr3) throws NTLMEngineException {
        try {
            MessageDigest md5 = getMD5();
            md5.update(bArr2);
            md5.update(bArr3);
            byte[] digest = md5.digest();
            byte[] bArr4 = new byte[8];
            System.arraycopy(digest, 0, bArr4, 0, 8);
            return lmResponse(bArr, bArr4);
        } catch (Exception e) {
            if (e instanceof NTLMEngineException) {
                throw ((NTLMEngineException) e);
            }
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public static byte[] lmHash(String str) throws NTLMEngineException {
        try {
            byte[] bytes = str.toUpperCase(Locale.ROOT).getBytes(Consts.ASCII);
            byte[] bArr = new byte[14];
            System.arraycopy(bytes, 0, bArr, 0, Math.min(bytes.length, 14));
            Key createDESKey = createDESKey(bArr, 0);
            Key createDESKey2 = createDESKey(bArr, 7);
            byte[] bytes2 = "KGS!@#$%".getBytes(Consts.ASCII);
            Cipher instance = Cipher.getInstance("DES/ECB/NoPadding");
            instance.init(1, createDESKey);
            byte[] doFinal = instance.doFinal(bytes2);
            instance.init(1, createDESKey2);
            byte[] doFinal2 = instance.doFinal(bytes2);
            byte[] bArr2 = new byte[16];
            System.arraycopy(doFinal, 0, bArr2, 0, 8);
            System.arraycopy(doFinal2, 0, bArr2, 8, 8);
            return bArr2;
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public static byte[] ntlmHash(String str) throws NTLMEngineException {
        Charset charset = UNICODE_LITTLE_UNMARKED;
        if (charset != null) {
            byte[] bytes = str.getBytes(charset);
            MD4 md4 = new MD4();
            md4.update(bytes);
            return md4.getOutput();
        }
        throw new NTLMEngineException("Unicode not supported");
    }

    /* access modifiers changed from: private */
    public static byte[] lmv2Hash(String str, String str2, byte[] bArr) throws NTLMEngineException {
        if (UNICODE_LITTLE_UNMARKED != null) {
            HMACMD5 hmacmd5 = new HMACMD5(bArr);
            hmacmd5.update(str2.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
            if (str != null) {
                hmacmd5.update(str.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
            }
            return hmacmd5.getOutput();
        }
        throw new NTLMEngineException("Unicode not supported");
    }

    /* access modifiers changed from: private */
    public static byte[] ntlmv2Hash(String str, String str2, byte[] bArr) throws NTLMEngineException {
        if (UNICODE_LITTLE_UNMARKED != null) {
            HMACMD5 hmacmd5 = new HMACMD5(bArr);
            hmacmd5.update(str2.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
            if (str != null) {
                hmacmd5.update(str.getBytes(UNICODE_LITTLE_UNMARKED));
            }
            return hmacmd5.getOutput();
        }
        throw new NTLMEngineException("Unicode not supported");
    }

    /* access modifiers changed from: private */
    public static byte[] lmResponse(byte[] bArr, byte[] bArr2) throws NTLMEngineException {
        try {
            byte[] bArr3 = new byte[21];
            System.arraycopy(bArr, 0, bArr3, 0, 16);
            Key createDESKey = createDESKey(bArr3, 0);
            Key createDESKey2 = createDESKey(bArr3, 7);
            Key createDESKey3 = createDESKey(bArr3, 14);
            Cipher instance = Cipher.getInstance("DES/ECB/NoPadding");
            instance.init(1, createDESKey);
            byte[] doFinal = instance.doFinal(bArr2);
            instance.init(1, createDESKey2);
            byte[] doFinal2 = instance.doFinal(bArr2);
            instance.init(1, createDESKey3);
            byte[] doFinal3 = instance.doFinal(bArr2);
            byte[] bArr4 = new byte[24];
            System.arraycopy(doFinal, 0, bArr4, 0, 8);
            System.arraycopy(doFinal2, 0, bArr4, 8, 8);
            System.arraycopy(doFinal3, 0, bArr4, 16, 8);
            return bArr4;
        } catch (Exception e) {
            throw new NTLMEngineException(e.getMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public static byte[] lmv2Response(byte[] bArr, byte[] bArr2, byte[] bArr3) throws NTLMEngineException {
        HMACMD5 hmacmd5 = new HMACMD5(bArr);
        hmacmd5.update(bArr2);
        hmacmd5.update(bArr3);
        byte[] output = hmacmd5.getOutput();
        byte[] bArr4 = new byte[(output.length + bArr3.length)];
        System.arraycopy(output, 0, bArr4, 0, output.length);
        System.arraycopy(bArr3, 0, bArr4, output.length, bArr3.length);
        return bArr4;
    }

    /* access modifiers changed from: private */
    public static byte[] encodeLong(int i) {
        byte[] bArr = new byte[4];
        encodeLong(bArr, 0, i);
        return bArr;
    }

    /* access modifiers changed from: private */
    public static void encodeLong(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 & 255);
        bArr[i + 1] = (byte) ((i2 >> 8) & 255);
        bArr[i + 2] = (byte) ((i2 >> 16) & 255);
        bArr[i + 3] = (byte) ((i2 >> 24) & 255);
    }

    /* access modifiers changed from: private */
    public static byte[] createBlob(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArr4 = {1, 1, 0, 0};
        byte[] bArr5 = {0, 0, 0, 0};
        byte[] bArr6 = {0, 0, 0, 0};
        byte[] bArr7 = {0, 0, 0, 0};
        byte[] bArr8 = new byte[(bArr4.length + bArr5.length + bArr3.length + 8 + bArr6.length + bArr2.length + bArr7.length)];
        System.arraycopy(bArr4, 0, bArr8, 0, bArr4.length);
        int length = bArr4.length + 0;
        System.arraycopy(bArr5, 0, bArr8, length, bArr5.length);
        int length2 = length + bArr5.length;
        System.arraycopy(bArr3, 0, bArr8, length2, bArr3.length);
        int length3 = length2 + bArr3.length;
        System.arraycopy(bArr, 0, bArr8, length3, 8);
        int i = length3 + 8;
        System.arraycopy(bArr6, 0, bArr8, i, bArr6.length);
        int length4 = i + bArr6.length;
        System.arraycopy(bArr2, 0, bArr8, length4, bArr2.length);
        System.arraycopy(bArr7, 0, bArr8, length4 + bArr2.length, bArr7.length);
        int length5 = bArr7.length;
        return bArr8;
    }

    /* access modifiers changed from: private */
    public static Key createDESKey(byte[] bArr, int i) {
        byte[] bArr2 = new byte[7];
        System.arraycopy(bArr, i, bArr2, 0, 7);
        byte[] bArr3 = {bArr2[0], (byte) ((bArr2[0] << 7) | ((bArr2[1] & UnsignedBytes.MAX_VALUE) >>> 1)), (byte) ((bArr2[1] << 6) | ((bArr2[2] & UnsignedBytes.MAX_VALUE) >>> 2)), (byte) ((bArr2[2] << 5) | ((bArr2[3] & UnsignedBytes.MAX_VALUE) >>> 3)), (byte) ((bArr2[3] << 4) | ((bArr2[4] & UnsignedBytes.MAX_VALUE) >>> 4)), (byte) ((bArr2[4] << 3) | ((bArr2[5] & UnsignedBytes.MAX_VALUE) >>> 5)), (byte) ((bArr2[5] << 2) | ((bArr2[6] & UnsignedBytes.MAX_VALUE) >>> 6)), (byte) (bArr2[6] << 1)};
        oddParity(bArr3);
        return new SecretKeySpec(bArr3, "DES");
    }

    private static void oddParity(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            if ((((b >>> 1) ^ ((((((b >>> 7) ^ (b >>> 6)) ^ (b >>> 5)) ^ (b >>> 4)) ^ (b >>> 3)) ^ (b >>> 2))) & 1) == 0) {
                bArr[i] = (byte) (bArr[i] | 1);
            } else {
                bArr[i] = (byte) (bArr[i] & -2);
            }
        }
    }

    /* access modifiers changed from: private */
    public static Charset getCharset(int i) throws NTLMEngineException {
        if ((i & 1) == 0) {
            return DEFAULT_CHARSET;
        }
        Charset charset = UNICODE_LITTLE_UNMARKED;
        if (charset != null) {
            return charset;
        }
        throw new NTLMEngineException("Unicode not supported");
    }

    private static String stripDotSuffix(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(46);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    /* access modifiers changed from: private */
    public static String convertHost(String str) {
        return stripDotSuffix(str);
    }

    /* access modifiers changed from: private */
    public static String convertDomain(String str) {
        return stripDotSuffix(str);
    }

    static void writeUShort(byte[] bArr, int i, int i2) {
        bArr[i2] = (byte) (i & 255);
        bArr[i2 + 1] = (byte) ((i >> 8) & 255);
    }

    static void writeULong(byte[] bArr, int i, int i2) {
        bArr[i2] = (byte) (i & 255);
        bArr[i2 + 1] = (byte) ((i >> 8) & 255);
        bArr[i2 + 2] = (byte) ((i >> 16) & 255);
        bArr[i2 + 3] = (byte) ((i >> 24) & 255);
    }

    static MessageDigest getMD5() {
        try {
            return MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("MD5 message digest doesn't seem to exist - fatal error: ");
            sb.append(e.getMessage());
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public String generateType1Msg(String str, String str2) throws NTLMEngineException {
        return getType1Message(str2, str);
    }

    public String generateType3Msg(String str, String str2, String str3, String str4, String str5) throws NTLMEngineException {
        Type2Message type2Message = new Type2Message(str5);
        return getType3Message(str, str2, str4, str3, type2Message.getChallenge(), type2Message.getFlags(), type2Message.getTarget(), type2Message.getTargetInfo());
    }
}
