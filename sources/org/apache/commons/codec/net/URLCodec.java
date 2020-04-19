package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    protected static final byte ESCAPE_CHAR = 37;
    static final int RADIX = 16;
    protected static final BitSet WWW_FORM_URL = new BitSet(256);
    @Deprecated
    protected String charset;

    static {
        for (int i = 97; i <= 122; i++) {
            WWW_FORM_URL.set(i);
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            WWW_FORM_URL.set(i2);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            WWW_FORM_URL.set(i3);
        }
        WWW_FORM_URL.set(45);
        WWW_FORM_URL.set(95);
        WWW_FORM_URL.set(46);
        WWW_FORM_URL.set(42);
        WWW_FORM_URL.set(32);
    }

    public URLCodec() {
        this("UTF-8");
    }

    public URLCodec(String str) {
        this.charset = str;
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [int] */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r3v0, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final byte[] encodeUrl(java.util.BitSet r6, byte[] r7) {
        /*
            if (r7 != 0) goto L_0x0004
            r6 = 0
            return r6
        L_0x0004:
            if (r6 != 0) goto L_0x0008
            java.util.BitSet r6 = WWW_FORM_URL
        L_0x0008:
            java.io.ByteArrayOutputStream r0 = new java.io.ByteArrayOutputStream
            r0.<init>()
            int r1 = r7.length
            r2 = 0
        L_0x000f:
            if (r2 >= r1) goto L_0x004d
            byte r3 = r7[r2]
            if (r3 >= 0) goto L_0x0017
            int r3 = r3 + 256
        L_0x0017:
            boolean r4 = r6.get(r3)
            if (r4 == 0) goto L_0x0027
            r4 = 32
            if (r3 != r4) goto L_0x0023
            r3 = 43
        L_0x0023:
            r0.write(r3)
            goto L_0x004a
        L_0x0027:
            r4 = 37
            r0.write(r4)
            int r4 = r3 >> 4
            r4 = r4 & 15
            r5 = 16
            char r4 = java.lang.Character.forDigit(r4, r5)
            char r4 = java.lang.Character.toUpperCase(r4)
            r3 = r3 & 15
            char r3 = java.lang.Character.forDigit(r3, r5)
            char r3 = java.lang.Character.toUpperCase(r3)
            r0.write(r4)
            r0.write(r3)
        L_0x004a:
            int r2 = r2 + 1
            goto L_0x000f
        L_0x004d:
            byte[] r6 = r0.toByteArray()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.codec.net.URLCodec.encodeUrl(java.util.BitSet, byte[]):byte[]");
    }

    public static final byte[] decodeUrl(byte[] bArr) throws DecoderException {
        if (bArr == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i < bArr.length) {
            byte b = bArr[i];
            if (b == 43) {
                byteArrayOutputStream.write(32);
            } else if (b == 37) {
                int i2 = i + 1;
                try {
                    int digit16 = Utils.digit16(bArr[i2]);
                    i = i2 + 1;
                    byteArrayOutputStream.write((char) ((digit16 << 4) + Utils.digit16(bArr[i])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding: ", e);
                }
            } else {
                byteArrayOutputStream.write(b);
            }
            i++;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] encode(byte[] bArr) {
        return encodeUrl(WWW_FORM_URL, bArr);
    }

    public byte[] decode(byte[] bArr) throws DecoderException {
        return decodeUrl(bArr);
    }

    public String encode(String str, String str2) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(encode(str.getBytes(str2)));
    }

    public String encode(String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return encode(str, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String decode(String str, String str2) throws DecoderException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(decode(StringUtils.getBytesUsAscii(str)), str2);
    }

    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return decode(str, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return encode((byte[]) obj);
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Objects of type ");
        sb.append(obj.getClass().getName());
        sb.append(" cannot be URL encoded");
        throw new EncoderException(sb.toString());
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return decode((byte[]) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Objects of type ");
        sb.append(obj.getClass().getName());
        sb.append(" cannot be URL decoded");
        throw new DecoderException(sb.toString());
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    @Deprecated
    public String getEncoding() {
        return this.charset;
    }
}
