package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import com.fasterxml.jackson.core.p006io.IOContext;
import com.fasterxml.jackson.core.p006io.MergedStream;
import com.fasterxml.jackson.core.p006io.UTF32Reader;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
import com.google.common.primitives.UnsignedBytes;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class ByteSourceJsonBootstrapper {
    public static final byte UTF8_BOM_1 = -17;
    public static final byte UTF8_BOM_2 = -69;
    public static final byte UTF8_BOM_3 = -65;
    private boolean _bigEndian = true;
    private final boolean _bufferRecyclable;
    private int _bytesPerChar;
    private final IOContext _context;
    private final InputStream _in;
    private final byte[] _inputBuffer;
    private int _inputEnd;
    private int _inputPtr;

    public ByteSourceJsonBootstrapper(IOContext iOContext, InputStream inputStream) {
        this._context = iOContext;
        this._in = inputStream;
        this._inputBuffer = iOContext.allocReadIOBuffer();
        this._inputPtr = 0;
        this._inputEnd = 0;
        this._bufferRecyclable = true;
    }

    public ByteSourceJsonBootstrapper(IOContext iOContext, byte[] bArr, int i, int i2) {
        this._context = iOContext;
        this._in = null;
        this._inputBuffer = bArr;
        this._inputPtr = i;
        this._inputEnd = i + i2;
        this._bufferRecyclable = false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x005e, code lost:
        if (checkUTF16((r1[r4 + 1] & com.google.common.primitives.UnsignedBytes.MAX_VALUE) | ((r1[r4] & com.google.common.primitives.UnsignedBytes.MAX_VALUE) << 8)) != false) goto L_0x0062;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.core.JsonEncoding detectEncoding() throws java.io.IOException {
        /*
            r7 = this;
            r0 = 4
            boolean r1 = r7.ensureLoaded(r0)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0043
            byte[] r1 = r7._inputBuffer
            int r4 = r7._inputPtr
            byte r5 = r1[r4]
            int r5 = r5 << 24
            int r6 = r4 + 1
            byte r6 = r1[r6]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r6 = r6 << 16
            r5 = r5 | r6
            int r6 = r4 + 2
            byte r6 = r1[r6]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r6 = r6 << 8
            r5 = r5 | r6
            int r4 = r4 + 3
            byte r1 = r1[r4]
            r1 = r1 & 255(0xff, float:3.57E-43)
            r1 = r1 | r5
            boolean r4 = r7.handleBOM(r1)
            if (r4 == 0) goto L_0x0031
            goto L_0x0062
        L_0x0031:
            boolean r4 = r7.checkUTF32(r1)
            if (r4 == 0) goto L_0x0038
            goto L_0x0062
        L_0x0038:
            int r1 = r1 >>> 16
            boolean r1 = r7.checkUTF16(r1)
            if (r1 == 0) goto L_0x0041
            goto L_0x0062
        L_0x0041:
            r2 = 0
            goto L_0x0062
        L_0x0043:
            r1 = 2
            boolean r1 = r7.ensureLoaded(r1)
            if (r1 == 0) goto L_0x0061
            byte[] r1 = r7._inputBuffer
            int r4 = r7._inputPtr
            byte r5 = r1[r4]
            r5 = r5 & 255(0xff, float:3.57E-43)
            int r5 = r5 << 8
            int r4 = r4 + r2
            byte r1 = r1[r4]
            r1 = r1 & 255(0xff, float:3.57E-43)
            r1 = r1 | r5
            boolean r1 = r7.checkUTF16(r1)
            if (r1 == 0) goto L_0x0061
            goto L_0x0062
        L_0x0061:
            r2 = 0
        L_0x0062:
            if (r2 != 0) goto L_0x0067
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF8
            goto L_0x008c
        L_0x0067:
            int r1 = r7._bytesPerChar
            if (r1 == r0) goto L_0x0083
            switch(r1) {
                case 1: goto L_0x0080;
                case 2: goto L_0x0076;
                default: goto L_0x006e;
            }
        L_0x006e:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Internal error"
            r0.<init>(r1)
            throw r0
        L_0x0076:
            boolean r0 = r7._bigEndian
            if (r0 == 0) goto L_0x007d
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF16_BE
            goto L_0x008c
        L_0x007d:
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF16_LE
            goto L_0x008c
        L_0x0080:
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF8
            goto L_0x008c
        L_0x0083:
            boolean r0 = r7._bigEndian
            if (r0 == 0) goto L_0x008a
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF32_BE
            goto L_0x008c
        L_0x008a:
            com.fasterxml.jackson.core.JsonEncoding r0 = com.fasterxml.jackson.core.JsonEncoding.UTF32_LE
        L_0x008c:
            com.fasterxml.jackson.core.io.IOContext r1 = r7._context
            r1.setEncoding(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper.detectEncoding():com.fasterxml.jackson.core.JsonEncoding");
    }

    public static int skipUTF8BOM(DataInput dataInput) throws IOException {
        int readUnsignedByte = dataInput.readUnsignedByte();
        if (readUnsignedByte != 239) {
            return readUnsignedByte;
        }
        int readUnsignedByte2 = dataInput.readUnsignedByte();
        if (readUnsignedByte2 == 187) {
            int readUnsignedByte3 = dataInput.readUnsignedByte();
            if (readUnsignedByte3 == 191) {
                return dataInput.readUnsignedByte();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected byte 0x");
            sb.append(Integer.toHexString(readUnsignedByte3));
            sb.append(" following 0xEF 0xBB; should get 0xBF as part of UTF-8 BOM");
            throw new IOException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unexpected byte 0x");
        sb2.append(Integer.toHexString(readUnsignedByte2));
        sb2.append(" following 0xEF; should get 0xBB as part of UTF-8 BOM");
        throw new IOException(sb2.toString());
    }

    public Reader constructReader() throws IOException {
        JsonEncoding encoding = this._context.getEncoding();
        int bits = encoding.bits();
        if (bits == 8 || bits == 16) {
            InputStream inputStream = this._in;
            if (inputStream == 0) {
                inputStream = new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd);
            } else {
                int i = this._inputPtr;
                int i2 = this._inputEnd;
                if (i < i2) {
                    MergedStream mergedStream = new MergedStream(this._context, inputStream, this._inputBuffer, i, i2);
                    inputStream = mergedStream;
                }
            }
            return new InputStreamReader(inputStream, encoding.getJavaName());
        } else if (bits == 32) {
            IOContext iOContext = this._context;
            UTF32Reader uTF32Reader = new UTF32Reader(iOContext, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, iOContext.getEncoding().isBigEndian());
            return uTF32Reader;
        } else {
            throw new RuntimeException("Internal error");
        }
    }

    public JsonParser constructParser(int i, ObjectCodec objectCodec, ByteQuadsCanonicalizer byteQuadsCanonicalizer, CharsToNameCanonicalizer charsToNameCanonicalizer, int i2) throws IOException {
        int i3 = i2;
        if (detectEncoding() != JsonEncoding.UTF8 || !Feature.CANONICALIZE_FIELD_NAMES.enabledIn(i3)) {
            ReaderBasedJsonParser readerBasedJsonParser = new ReaderBasedJsonParser(this._context, i, constructReader(), objectCodec, charsToNameCanonicalizer.makeChild(i2));
            return readerBasedJsonParser;
        }
        int i4 = i;
        ObjectCodec objectCodec2 = objectCodec;
        UTF8StreamJsonParser uTF8StreamJsonParser = new UTF8StreamJsonParser(this._context, i4, this._in, objectCodec2, byteQuadsCanonicalizer.makeChild(i3), this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
        return uTF8StreamJsonParser;
    }

    public static MatchStrength hasJSONFormat(InputAccessor inputAccessor) throws IOException {
        if (!inputAccessor.hasMoreBytes()) {
            return MatchStrength.INCONCLUSIVE;
        }
        byte nextByte = inputAccessor.nextByte();
        if (nextByte == -17) {
            if (!inputAccessor.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (inputAccessor.nextByte() != -69) {
                return MatchStrength.NO_MATCH;
            }
            if (!inputAccessor.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (inputAccessor.nextByte() != -65) {
                return MatchStrength.NO_MATCH;
            }
            if (!inputAccessor.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            nextByte = inputAccessor.nextByte();
        }
        int skipSpace = skipSpace(inputAccessor, nextByte);
        if (skipSpace < 0) {
            return MatchStrength.INCONCLUSIVE;
        }
        if (skipSpace == 123) {
            int skipSpace2 = skipSpace(inputAccessor);
            if (skipSpace2 < 0) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (skipSpace2 == 34 || skipSpace2 == 125) {
                return MatchStrength.SOLID_MATCH;
            }
            return MatchStrength.NO_MATCH;
        } else if (skipSpace == 91) {
            int skipSpace3 = skipSpace(inputAccessor);
            if (skipSpace3 < 0) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (skipSpace3 == 93 || skipSpace3 == 91) {
                return MatchStrength.SOLID_MATCH;
            }
            return MatchStrength.SOLID_MATCH;
        } else {
            MatchStrength matchStrength = MatchStrength.WEAK_MATCH;
            if (skipSpace == 34) {
                return matchStrength;
            }
            if (skipSpace <= 57 && skipSpace >= 48) {
                return matchStrength;
            }
            if (skipSpace == 45) {
                int skipSpace4 = skipSpace(inputAccessor);
                if (skipSpace4 < 0) {
                    return MatchStrength.INCONCLUSIVE;
                }
                if (skipSpace4 > 57 || skipSpace4 < 48) {
                    matchStrength = MatchStrength.NO_MATCH;
                }
                return matchStrength;
            } else if (skipSpace == 110) {
                return tryMatch(inputAccessor, "ull", matchStrength);
            } else {
                if (skipSpace == 116) {
                    return tryMatch(inputAccessor, "rue", matchStrength);
                }
                if (skipSpace == 102) {
                    return tryMatch(inputAccessor, "alse", matchStrength);
                }
                return MatchStrength.NO_MATCH;
            }
        }
    }

    private static MatchStrength tryMatch(InputAccessor inputAccessor, String str, MatchStrength matchStrength) throws IOException {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!inputAccessor.hasMoreBytes()) {
                return MatchStrength.INCONCLUSIVE;
            }
            if (inputAccessor.nextByte() != str.charAt(i)) {
                return MatchStrength.NO_MATCH;
            }
        }
        return matchStrength;
    }

    private static int skipSpace(InputAccessor inputAccessor) throws IOException {
        if (!inputAccessor.hasMoreBytes()) {
            return -1;
        }
        return skipSpace(inputAccessor, inputAccessor.nextByte());
    }

    private static int skipSpace(InputAccessor inputAccessor, byte b) throws IOException {
        while (true) {
            byte b2 = b & UnsignedBytes.MAX_VALUE;
            if (b2 != 32 && b2 != 13 && b2 != 10 && b2 != 9) {
                return b2;
            }
            if (!inputAccessor.hasMoreBytes()) {
                return -1;
            }
            b = inputAccessor.nextByte();
        }
    }

    private boolean handleBOM(int i) throws IOException {
        if (i == -16842752) {
            reportWeirdUCS4("3412");
        } else if (i == -131072) {
            this._inputPtr += 4;
            this._bytesPerChar = 4;
            this._bigEndian = false;
            return true;
        } else if (i == 65279) {
            this._bigEndian = true;
            this._inputPtr += 4;
            this._bytesPerChar = 4;
            return true;
        } else if (i == 65534) {
            reportWeirdUCS4("2143");
        }
        int i2 = i >>> 16;
        if (i2 == 65279) {
            this._inputPtr += 2;
            this._bytesPerChar = 2;
            this._bigEndian = true;
            return true;
        } else if (i2 == 65534) {
            this._inputPtr += 2;
            this._bytesPerChar = 2;
            this._bigEndian = false;
            return true;
        } else if ((i >>> 8) != 15711167) {
            return false;
        } else {
            this._inputPtr += 3;
            this._bytesPerChar = 1;
            this._bigEndian = true;
            return true;
        }
    }

    private boolean checkUTF32(int i) throws IOException {
        if ((i >> 8) == 0) {
            this._bigEndian = true;
        } else if ((16777215 & i) == 0) {
            this._bigEndian = false;
        } else if ((-16711681 & i) == 0) {
            reportWeirdUCS4("3412");
        } else if ((i & -65281) != 0) {
            return false;
        } else {
            reportWeirdUCS4("2143");
        }
        this._bytesPerChar = 4;
        return true;
    }

    private boolean checkUTF16(int i) {
        if ((65280 & i) == 0) {
            this._bigEndian = true;
        } else if ((i & 255) != 0) {
            return false;
        } else {
            this._bigEndian = false;
        }
        this._bytesPerChar = 2;
        return true;
    }

    private void reportWeirdUCS4(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported UCS-4 endianness (");
        sb.append(str);
        sb.append(") detected");
        throw new CharConversionException(sb.toString());
    }

    /* access modifiers changed from: protected */
    public boolean ensureLoaded(int i) throws IOException {
        int i2;
        int i3 = this._inputEnd - this._inputPtr;
        while (i3 < i) {
            InputStream inputStream = this._in;
            if (inputStream == null) {
                i2 = -1;
            } else {
                byte[] bArr = this._inputBuffer;
                int i4 = this._inputEnd;
                i2 = inputStream.read(bArr, i4, bArr.length - i4);
            }
            if (i2 < 1) {
                return false;
            }
            this._inputEnd += i2;
            i3 += i2;
        }
        return true;
    }
}
