package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
    private static final byte BLANK = 32;
    private static final BitSet PRINTABLE_CHARS = new BitSet(256);
    private static final byte UNDERSCORE = 95;
    private final Charset charset;
    private boolean encodeBlanks;

    /* access modifiers changed from: protected */
    public String getEncoding() {
        return "Q";
    }

    static {
        PRINTABLE_CHARS.set(32);
        PRINTABLE_CHARS.set(33);
        PRINTABLE_CHARS.set(34);
        PRINTABLE_CHARS.set(35);
        PRINTABLE_CHARS.set(36);
        PRINTABLE_CHARS.set(37);
        PRINTABLE_CHARS.set(38);
        PRINTABLE_CHARS.set(39);
        PRINTABLE_CHARS.set(40);
        PRINTABLE_CHARS.set(41);
        PRINTABLE_CHARS.set(42);
        PRINTABLE_CHARS.set(43);
        PRINTABLE_CHARS.set(44);
        PRINTABLE_CHARS.set(45);
        PRINTABLE_CHARS.set(46);
        PRINTABLE_CHARS.set(47);
        for (int i = 48; i <= 57; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(58);
        PRINTABLE_CHARS.set(59);
        PRINTABLE_CHARS.set(60);
        PRINTABLE_CHARS.set(62);
        PRINTABLE_CHARS.set(64);
        for (int i2 = 65; i2 <= 90; i2++) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(91);
        PRINTABLE_CHARS.set(92);
        PRINTABLE_CHARS.set(93);
        PRINTABLE_CHARS.set(94);
        PRINTABLE_CHARS.set(96);
        for (int i3 = 97; i3 <= 122; i3++) {
            PRINTABLE_CHARS.set(i3);
        }
        PRINTABLE_CHARS.set(123);
        PRINTABLE_CHARS.set(124);
        PRINTABLE_CHARS.set(125);
        PRINTABLE_CHARS.set(126);
    }

    public QCodec() {
        this(Charsets.UTF_8);
    }

    public QCodec(Charset charset2) {
        this.encodeBlanks = false;
        this.charset = charset2;
    }

    public QCodec(String str) {
        this(Charset.forName(str));
    }

    /* access modifiers changed from: protected */
    public byte[] doEncoding(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] encodeQuotedPrintable = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bArr);
        if (this.encodeBlanks) {
            for (int i = 0; i < encodeQuotedPrintable.length; i++) {
                if (encodeQuotedPrintable[i] == 32) {
                    encodeQuotedPrintable[i] = UNDERSCORE;
                }
            }
        }
        return encodeQuotedPrintable;
    }

    /* access modifiers changed from: protected */
    public byte[] doDecoding(byte[] bArr) throws DecoderException {
        boolean z;
        if (bArr == null) {
            return null;
        }
        int length = bArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            } else if (bArr[i] == 95) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (!z) {
            return QuotedPrintableCodec.decodeQuotedPrintable(bArr);
        }
        byte[] bArr2 = new byte[bArr.length];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            byte b = bArr[i2];
            if (b != 95) {
                bArr2[i2] = b;
            } else {
                bArr2[i2] = 32;
            }
        }
        return QuotedPrintableCodec.decodeQuotedPrintable(bArr2);
    }

    public String encode(String str, Charset charset2) throws EncoderException {
        if (str == null) {
            return null;
        }
        return encodeText(str, charset2);
    }

    public String encode(String str, String str2) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return encodeText(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String encode(String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        return encode(str, getCharset());
    }

    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return decodeText(str);
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Objects of type ");
        sb.append(obj.getClass().getName());
        sb.append(" cannot be encoded using Q codec");
        throw new EncoderException(sb.toString());
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Objects of type ");
        sb.append(obj.getClass().getName());
        sb.append(" cannot be decoded using Q codec");
        throw new DecoderException(sb.toString());
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }

    public void setEncodeBlanks(boolean z) {
        this.encodeBlanks = z;
    }
}
