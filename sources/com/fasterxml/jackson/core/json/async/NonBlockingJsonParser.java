package com.fasterxml.jackson.core.json.async;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.async.ByteArrayFeeder;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.p006io.CharTypes;
import com.fasterxml.jackson.core.p006io.IOContext;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.google.common.primitives.UnsignedBytes;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.message.TokenParser;

public class NonBlockingJsonParser extends NonBlockingJsonParserBase implements ByteArrayFeeder {
    protected static final int[] _icLatin1 = CharTypes.getInputCodeLatin1();
    private static final int[] _icUTF8 = CharTypes.getInputCodeUtf8();
    protected byte[] _inputBuffer = NO_BYTES;
    protected int _origBufferLen;

    public ByteArrayFeeder getNonBlockingInputFeeder() {
        return this;
    }

    public NonBlockingJsonParser(IOContext iOContext, int i, ByteQuadsCanonicalizer byteQuadsCanonicalizer) {
        super(iOContext, i, byteQuadsCanonicalizer);
    }

    public final boolean needMoreInput() {
        return this._inputPtr >= this._inputEnd && !this._endOfInput;
    }

    public void feedInput(byte[] bArr, int i, int i2) throws IOException {
        if (this._inputPtr < this._inputEnd) {
            _reportError("Still have %d undecoded bytes, should not call 'feedInput'", Integer.valueOf(this._inputEnd - this._inputPtr));
        }
        if (i2 < i) {
            _reportError("Input end (%d) may not be before start (%d)", Integer.valueOf(i2), Integer.valueOf(i));
        }
        if (this._endOfInput) {
            _reportError("Already closed, can not feed more input");
        }
        this._currInputProcessed += (long) this._origBufferLen;
        this._currInputRowStart = i - (this._inputEnd - this._currInputRowStart);
        this._inputBuffer = bArr;
        this._inputPtr = i;
        this._inputEnd = i2;
        this._origBufferLen = i2 - i;
    }

    public void endOfInput() {
        this._endOfInput = true;
    }

    public int releaseBuffered(OutputStream outputStream) throws IOException {
        int i = this._inputEnd - this._inputPtr;
        if (i > 0) {
            outputStream.write(this._inputBuffer, this._inputPtr, i);
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public char _decodeEscaped() throws IOException {
        VersionUtil.throwInternal();
        return TokenParser.f498SP;
    }

    public JsonToken nextToken() throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            if (this._closed) {
                return null;
            }
            if (!this._endOfInput) {
                return JsonToken.NOT_AVAILABLE;
            }
            if (this._currToken == JsonToken.NOT_AVAILABLE) {
                return _finishTokenWithEOF();
            }
            return _eofAsNextToken();
        } else if (this._currToken == JsonToken.NOT_AVAILABLE) {
            return _finishToken();
        } else {
            this._numTypesValid = 0;
            this._tokenInputTotal = this._currInputProcessed + ((long) this._inputPtr);
            this._binaryValue = null;
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
            switch (this._majorState) {
                case 0:
                    return _startDocument(b);
                case 1:
                    return _startValue(b);
                case 2:
                    return _startFieldName(b);
                case 3:
                    return _startFieldNameAfterComma(b);
                case 4:
                    return _startValueExpectColon(b);
                case 5:
                    return _startValue(b);
                case 6:
                    return _startValueExpectComma(b);
                default:
                    VersionUtil.throwInternal();
                    return null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final JsonToken _finishToken() throws IOException {
        switch (this._minorState) {
            case 1:
                return _finishBOM(this._pending32);
            case 4:
                byte[] bArr = this._inputBuffer;
                int i = this._inputPtr;
                this._inputPtr = i + 1;
                return _startFieldName(bArr[i] & UnsignedBytes.MAX_VALUE);
            case 5:
                byte[] bArr2 = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                return _startFieldNameAfterComma(bArr2[i2] & UnsignedBytes.MAX_VALUE);
            case 7:
                return _parseEscapedName(this._quadLength, this._pending32, this._pendingBytes);
            case 8:
                return _finishFieldWithEscape();
            case 9:
                return _finishAposName(this._quadLength, this._pending32, this._pendingBytes);
            case 10:
                return _finishUnquotedName(this._quadLength, this._pending32, this._pendingBytes);
            case 12:
                byte[] bArr3 = this._inputBuffer;
                int i3 = this._inputPtr;
                this._inputPtr = i3 + 1;
                return _startValue(bArr3[i3] & UnsignedBytes.MAX_VALUE);
            case 13:
                byte[] bArr4 = this._inputBuffer;
                int i4 = this._inputPtr;
                this._inputPtr = i4 + 1;
                return _startValueExpectComma(bArr4[i4] & UnsignedBytes.MAX_VALUE);
            case 14:
                byte[] bArr5 = this._inputBuffer;
                int i5 = this._inputPtr;
                this._inputPtr = i5 + 1;
                return _startValueExpectColon(bArr5[i5] & UnsignedBytes.MAX_VALUE);
            case 15:
                byte[] bArr6 = this._inputBuffer;
                int i6 = this._inputPtr;
                this._inputPtr = i6 + 1;
                return _startValueAfterComma(bArr6[i6] & UnsignedBytes.MAX_VALUE);
            case 16:
                return _finishKeywordToken("null", this._pending32, JsonToken.VALUE_NULL);
            case 17:
                return _finishKeywordToken("true", this._pending32, JsonToken.VALUE_TRUE);
            case 18:
                return _finishKeywordToken("false", this._pending32, JsonToken.VALUE_FALSE);
            case 19:
                return _finishNonStdToken(this._nonStdTokenType, this._pending32);
            case 23:
                byte[] bArr7 = this._inputBuffer;
                int i7 = this._inputPtr;
                this._inputPtr = i7 + 1;
                return _finishNumberMinus(bArr7[i7] & UnsignedBytes.MAX_VALUE);
            case 24:
                return _finishNumberLeadingZeroes();
            case 25:
                return _finishNumberLeadingNegZeroes();
            case 26:
                return _finishNumberIntegralPart(this._textBuffer.getBufferWithoutReset(), this._textBuffer.getCurrentSegmentSize());
            case 30:
                return _finishFloatFraction();
            case 31:
                byte[] bArr8 = this._inputBuffer;
                int i8 = this._inputPtr;
                this._inputPtr = i8 + 1;
                return _finishFloatExponent(true, bArr8[i8] & UnsignedBytes.MAX_VALUE);
            case 32:
                byte[] bArr9 = this._inputBuffer;
                int i9 = this._inputPtr;
                this._inputPtr = i9 + 1;
                return _finishFloatExponent(false, bArr9[i9] & UnsignedBytes.MAX_VALUE);
            case 40:
                return _finishRegularString();
            case 41:
                int _decodeSplitEscaped = _decodeSplitEscaped(this._quoted32, this._quotedDigits);
                if (_decodeSplitEscaped < 0) {
                    return JsonToken.NOT_AVAILABLE;
                }
                this._textBuffer.append((char) _decodeSplitEscaped);
                if (this._minorStateAfterSplit == 45) {
                    return _finishAposString();
                }
                return _finishRegularString();
            case 42:
                TextBuffer textBuffer = this._textBuffer;
                int i10 = this._pending32;
                byte[] bArr10 = this._inputBuffer;
                int i11 = this._inputPtr;
                this._inputPtr = i11 + 1;
                textBuffer.append((char) _decodeUTF8_2(i10, bArr10[i11]));
                if (this._minorStateAfterSplit == 45) {
                    return _finishAposString();
                }
                return _finishRegularString();
            case 43:
                int i12 = this._pending32;
                int i13 = this._pendingBytes;
                byte[] bArr11 = this._inputBuffer;
                int i14 = this._inputPtr;
                this._inputPtr = i14 + 1;
                if (!_decodeSplitUTF8_3(i12, i13, bArr11[i14])) {
                    return JsonToken.NOT_AVAILABLE;
                }
                if (this._minorStateAfterSplit == 45) {
                    return _finishAposString();
                }
                return _finishRegularString();
            case 44:
                int i15 = this._pending32;
                int i16 = this._pendingBytes;
                byte[] bArr12 = this._inputBuffer;
                int i17 = this._inputPtr;
                this._inputPtr = i17 + 1;
                if (!_decodeSplitUTF8_4(i15, i16, bArr12[i17])) {
                    return JsonToken.NOT_AVAILABLE;
                }
                if (this._minorStateAfterSplit == 45) {
                    return _finishAposString();
                }
                return _finishRegularString();
            case 45:
                return _finishAposString();
            case 50:
                return _finishErrorToken();
            case 51:
                return _startSlashComment(this._pending32);
            case 52:
                return _finishCComment(this._pending32, true);
            case 53:
                return _finishCComment(this._pending32, false);
            case 54:
                return _finishCppComment(this._pending32);
            case 55:
                return _finishHashComment(this._pending32);
            default:
                VersionUtil.throwInternal();
                return null;
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        _reportInvalidEOF(": was expecting closing '*/' for comment", com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004c, code lost:
        return _eofAsNextToken();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0055, code lost:
        return _valueComplete(com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.fasterxml.jackson.core.JsonToken _finishTokenWithEOF() throws java.io.IOException {
        /*
            r3 = this;
            com.fasterxml.jackson.core.JsonToken r0 = r3._currToken
            int r1 = r3._minorState
            r2 = 3
            if (r1 == r2) goto L_0x00a6
            r2 = 12
            if (r1 == r2) goto L_0x00a1
            r2 = 50
            if (r1 == r2) goto L_0x009c
            switch(r1) {
                case 16: goto L_0x0091;
                case 17: goto L_0x0086;
                case 18: goto L_0x007b;
                case 19: goto L_0x0072;
                default: goto L_0x0012;
            }
        L_0x0012:
            r2 = 0
            switch(r1) {
                case 24: goto L_0x006b;
                case 25: goto L_0x006b;
                case 26: goto L_0x0056;
                default: goto L_0x0016;
            }
        L_0x0016:
            switch(r1) {
                case 30: goto L_0x004d;
                case 31: goto L_0x003a;
                case 32: goto L_0x004f;
                default: goto L_0x0019;
            }
        L_0x0019:
            switch(r1) {
                case 52: goto L_0x0041;
                case 53: goto L_0x0041;
                case 54: goto L_0x0048;
                case 55: goto L_0x0048;
                default: goto L_0x001c;
            }
        L_0x001c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ": was expecting rest of token (internal state: "
            r1.append(r2)
            int r2 = r3._minorState
            r1.append(r2)
            java.lang.String r2 = ")"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.fasterxml.jackson.core.JsonToken r2 = r3._currToken
            r3._reportInvalidEOF(r1, r2)
            return r0
        L_0x003a:
            java.lang.String r0 = ": was expecting fraction after exponent marker"
            com.fasterxml.jackson.core.JsonToken r1 = com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT
            r3._reportInvalidEOF(r0, r1)
        L_0x0041:
            java.lang.String r0 = ": was expecting closing '*/' for comment"
            com.fasterxml.jackson.core.JsonToken r1 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            r3._reportInvalidEOF(r0, r1)
        L_0x0048:
            com.fasterxml.jackson.core.JsonToken r0 = r3._eofAsNextToken()
            return r0
        L_0x004d:
            r3._expLength = r2
        L_0x004f:
            com.fasterxml.jackson.core.JsonToken r0 = com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT
            com.fasterxml.jackson.core.JsonToken r0 = r3._valueComplete(r0)
            return r0
        L_0x0056:
            com.fasterxml.jackson.core.util.TextBuffer r0 = r3._textBuffer
            int r0 = r0.getCurrentSegmentSize()
            boolean r1 = r3._numberNegative
            if (r1 == 0) goto L_0x0062
            int r0 = r0 + -1
        L_0x0062:
            r3._intLength = r0
            com.fasterxml.jackson.core.JsonToken r0 = com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT
            com.fasterxml.jackson.core.JsonToken r0 = r3._valueComplete(r0)
            return r0
        L_0x006b:
            java.lang.String r0 = "0"
            com.fasterxml.jackson.core.JsonToken r0 = r3._valueCompleteInt(r2, r0)
            return r0
        L_0x0072:
            int r0 = r3._nonStdTokenType
            int r1 = r3._pending32
            com.fasterxml.jackson.core.JsonToken r0 = r3._finishNonStdTokenWithEOF(r0, r1)
            return r0
        L_0x007b:
            java.lang.String r0 = "false"
            int r1 = r3._pending32
            com.fasterxml.jackson.core.JsonToken r2 = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE
            com.fasterxml.jackson.core.JsonToken r0 = r3._finishKeywordTokenWithEOF(r0, r1, r2)
            return r0
        L_0x0086:
            java.lang.String r0 = "true"
            int r1 = r3._pending32
            com.fasterxml.jackson.core.JsonToken r2 = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE
            com.fasterxml.jackson.core.JsonToken r0 = r3._finishKeywordTokenWithEOF(r0, r1, r2)
            return r0
        L_0x0091:
            java.lang.String r0 = "null"
            int r1 = r3._pending32
            com.fasterxml.jackson.core.JsonToken r2 = com.fasterxml.jackson.core.JsonToken.VALUE_NULL
            com.fasterxml.jackson.core.JsonToken r0 = r3._finishKeywordTokenWithEOF(r0, r1, r2)
            return r0
        L_0x009c:
            com.fasterxml.jackson.core.JsonToken r0 = r3._finishErrorTokenWithEOF()
            return r0
        L_0x00a1:
            com.fasterxml.jackson.core.JsonToken r0 = r3._eofAsNextToken()
            return r0
        L_0x00a6:
            com.fasterxml.jackson.core.JsonToken r0 = r3._eofAsNextToken()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._finishTokenWithEOF():com.fasterxml.jackson.core.JsonToken");
    }

    private final JsonToken _startDocument(int i) throws IOException {
        byte b = i & UnsignedBytes.MAX_VALUE;
        if (b == 239 && this._minorState != 1) {
            return _finishBOM(1);
        }
        while (b <= 32) {
            if (b != 32) {
                if (b == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b == 13) {
                    this._currInputRowAlt++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b != 9) {
                    _throwInvalidSpace(b);
                }
            }
            if (this._inputPtr >= this._inputEnd) {
                this._minorState = 3;
                if (this._closed) {
                    return null;
                }
                if (this._endOfInput) {
                    return _eofAsNextToken();
                }
                return JsonToken.NOT_AVAILABLE;
            }
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            b = bArr[i2] & UnsignedBytes.MAX_VALUE;
        }
        return _startValue(b);
    }

    private final JsonToken _finishBOM(int i) throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b = bArr[i2] & UnsignedBytes.MAX_VALUE;
            switch (i) {
                case 1:
                    if (b == 187) {
                        break;
                    } else {
                        _reportError("Unexpected byte 0x%02x following 0xEF; should get 0xBB as second byte UTF-8 BOM", Integer.valueOf(b));
                        break;
                    }
                case 2:
                    if (b == 191) {
                        break;
                    } else {
                        _reportError("Unexpected byte 0x%02x following 0xEF 0xBB; should get 0xBF as third byte of UTF-8 BOM", Integer.valueOf(b));
                        break;
                    }
                case 3:
                    this._currInputProcessed -= 3;
                    return _startDocument(b);
            }
            i++;
        }
        this._pending32 = i;
        this._minorState = 1;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final JsonToken _startFieldName(int i) throws IOException {
        if (i <= 32) {
            i = _skipWS(i);
            if (i <= 0) {
                this._minorState = 4;
                return this._currToken;
            }
        }
        _updateTokenLocation();
        if (i == 34) {
            if (this._inputPtr + 13 <= this._inputEnd) {
                String _fastParseName = _fastParseName();
                if (_fastParseName != null) {
                    return _fieldComplete(_fastParseName);
                }
            }
            return _parseEscapedName(0, 0, 0);
        } else if (i == 125) {
            return _closeObjectScope();
        } else {
            return _handleOddName(i);
        }
    }

    /* JADX WARNING: type inference failed for: r1v4, types: [int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v3, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _startFieldNameAfterComma(int r5) throws java.io.IOException {
        /*
            r4 = this;
            r0 = 32
            r1 = 5
            if (r5 > r0) goto L_0x0010
            int r5 = r4._skipWS(r5)
            if (r5 > 0) goto L_0x0010
            r4._minorState = r1
            com.fasterxml.jackson.core.JsonToken r5 = r4._currToken
            return r5
        L_0x0010:
            r2 = 44
            r3 = 125(0x7d, float:1.75E-43)
            if (r5 == r2) goto L_0x004e
            if (r5 != r3) goto L_0x001d
            com.fasterxml.jackson.core.JsonToken r5 = r4._closeObjectScope()
            return r5
        L_0x001d:
            r2 = 35
            if (r5 != r2) goto L_0x0026
            com.fasterxml.jackson.core.JsonToken r5 = r4._finishHashComment(r1)
            return r5
        L_0x0026:
            r2 = 47
            if (r5 != r2) goto L_0x002f
            com.fasterxml.jackson.core.JsonToken r5 = r4._startSlashComment(r1)
            return r5
        L_0x002f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "was expecting comma to separate "
            r1.append(r2)
            com.fasterxml.jackson.core.json.JsonReadContext r2 = r4._parsingContext
            java.lang.String r2 = r2.typeDesc()
            r1.append(r2)
            java.lang.String r2 = " entries"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r4._reportUnexpectedChar(r5, r1)
        L_0x004e:
            int r5 = r4._inputPtr
            int r1 = r4._inputEnd
            r2 = 4
            if (r5 < r1) goto L_0x005c
            r4._minorState = r2
            com.fasterxml.jackson.core.JsonToken r5 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            r4._currToken = r5
            return r5
        L_0x005c:
            byte[] r1 = r4._inputBuffer
            byte r1 = r1[r5]
            int r5 = r5 + 1
            r4._inputPtr = r5
            if (r1 > r0) goto L_0x0071
            int r1 = r4._skipWS(r1)
            if (r1 > 0) goto L_0x0071
            r4._minorState = r2
            com.fasterxml.jackson.core.JsonToken r5 = r4._currToken
            return r5
        L_0x0071:
            r4._updateTokenLocation()
            r5 = 34
            if (r1 == r5) goto L_0x008e
            if (r1 != r3) goto L_0x0089
            com.fasterxml.jackson.core.JsonParser$Feature r5 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA
            int r0 = r4._features
            boolean r5 = r5.enabledIn(r0)
            if (r5 == 0) goto L_0x0089
            com.fasterxml.jackson.core.JsonToken r5 = r4._closeObjectScope()
            return r5
        L_0x0089:
            com.fasterxml.jackson.core.JsonToken r5 = r4._handleOddName(r1)
            return r5
        L_0x008e:
            int r5 = r4._inputPtr
            int r5 = r5 + 13
            int r0 = r4._inputEnd
            if (r5 > r0) goto L_0x00a1
            java.lang.String r5 = r4._fastParseName()
            if (r5 == 0) goto L_0x00a1
            com.fasterxml.jackson.core.JsonToken r5 = r4._fieldComplete(r5)
            return r5
        L_0x00a1:
            r5 = 0
            com.fasterxml.jackson.core.JsonToken r5 = r4._parseEscapedName(r5, r5, r5)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._startFieldNameAfterComma(int):com.fasterxml.jackson.core.JsonToken");
    }

    private final JsonToken _startValue(int i) throws IOException {
        if (i <= 32) {
            i = _skipWS(i);
            if (i <= 0) {
                this._minorState = 12;
                return this._currToken;
            }
        }
        _updateTokenLocation();
        if (i == 34) {
            return _startString();
        }
        if (i == 35) {
            return _finishHashComment(12);
        }
        if (i == 45) {
            return _startNegativeNumber();
        }
        if (i == 91) {
            return _startArrayScope();
        }
        if (i == 93) {
            return _closeArrayScope();
        }
        if (i == 102) {
            return _startFalseToken();
        }
        if (i == 110) {
            return _startNullToken();
        }
        if (i == 116) {
            return _startTrueToken();
        }
        if (i == 123) {
            return _startObjectScope();
        }
        if (i == 125) {
            return _closeObjectScope();
        }
        switch (i) {
            case 47:
                return _startSlashComment(12);
            case 48:
                return _startNumberLeadingZero();
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                return _startPositiveNumber(i);
            default:
                return _startUnexpectedValue(false, i);
        }
    }

    /* JADX WARNING: type inference failed for: r1v4, types: [int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v3, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _startValueExpectComma(int r8) throws java.io.IOException {
        /*
            r7 = this;
            r0 = 32
            r1 = 13
            if (r8 > r0) goto L_0x0011
            int r8 = r7._skipWS(r8)
            if (r8 > 0) goto L_0x0011
            r7._minorState = r1
            com.fasterxml.jackson.core.JsonToken r8 = r7._currToken
            return r8
        L_0x0011:
            r2 = 44
            r3 = 35
            r4 = 125(0x7d, float:1.75E-43)
            r5 = 93
            if (r8 == r2) goto L_0x0058
            if (r8 != r5) goto L_0x0022
            com.fasterxml.jackson.core.JsonToken r8 = r7._closeArrayScope()
            return r8
        L_0x0022:
            if (r8 != r4) goto L_0x0029
            com.fasterxml.jackson.core.JsonToken r8 = r7._closeObjectScope()
            return r8
        L_0x0029:
            r2 = 47
            if (r8 != r2) goto L_0x0032
            com.fasterxml.jackson.core.JsonToken r8 = r7._startSlashComment(r1)
            return r8
        L_0x0032:
            if (r8 != r3) goto L_0x0039
            com.fasterxml.jackson.core.JsonToken r8 = r7._finishHashComment(r1)
            return r8
        L_0x0039:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "was expecting comma to separate "
            r1.append(r2)
            com.fasterxml.jackson.core.json.JsonReadContext r2 = r7._parsingContext
            java.lang.String r2 = r2.typeDesc()
            r1.append(r2)
            java.lang.String r2 = " entries"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r7._reportUnexpectedChar(r8, r1)
        L_0x0058:
            int r8 = r7._inputPtr
            int r1 = r7._inputEnd
            r2 = 15
            if (r8 < r1) goto L_0x0067
            r7._minorState = r2
            com.fasterxml.jackson.core.JsonToken r8 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            r7._currToken = r8
            return r8
        L_0x0067:
            byte[] r1 = r7._inputBuffer
            byte r1 = r1[r8]
            r6 = 1
            int r8 = r8 + r6
            r7._inputPtr = r8
            if (r1 > r0) goto L_0x007c
            int r1 = r7._skipWS(r1)
            if (r1 > 0) goto L_0x007c
            r7._minorState = r2
            com.fasterxml.jackson.core.JsonToken r8 = r7._currToken
            return r8
        L_0x007c:
            r7._updateTokenLocation()
            r8 = 34
            if (r1 != r8) goto L_0x0088
            com.fasterxml.jackson.core.JsonToken r8 = r7._startString()
            return r8
        L_0x0088:
            if (r1 == r3) goto L_0x00f6
            r8 = 45
            if (r1 == r8) goto L_0x00f1
            r8 = 91
            if (r1 == r8) goto L_0x00ec
            if (r1 == r5) goto L_0x00da
            r8 = 102(0x66, float:1.43E-43)
            if (r1 == r8) goto L_0x00d5
            r8 = 110(0x6e, float:1.54E-43)
            if (r1 == r8) goto L_0x00d0
            r8 = 116(0x74, float:1.63E-43)
            if (r1 == r8) goto L_0x00cb
            r8 = 123(0x7b, float:1.72E-43)
            if (r1 == r8) goto L_0x00c6
            if (r1 == r4) goto L_0x00b9
            switch(r1) {
                case 47: goto L_0x00b4;
                case 48: goto L_0x00af;
                case 49: goto L_0x00aa;
                case 50: goto L_0x00aa;
                case 51: goto L_0x00aa;
                case 52: goto L_0x00aa;
                case 53: goto L_0x00aa;
                case 54: goto L_0x00aa;
                case 55: goto L_0x00aa;
                case 56: goto L_0x00aa;
                case 57: goto L_0x00aa;
                default: goto L_0x00a9;
            }
        L_0x00a9:
            goto L_0x00e7
        L_0x00aa:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startPositiveNumber(r1)
            return r8
        L_0x00af:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startNumberLeadingZero()
            return r8
        L_0x00b4:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startSlashComment(r2)
            return r8
        L_0x00b9:
            com.fasterxml.jackson.core.JsonParser$Feature r8 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA
            boolean r8 = r7.isEnabled(r8)
            if (r8 == 0) goto L_0x00e7
            com.fasterxml.jackson.core.JsonToken r8 = r7._closeObjectScope()
            return r8
        L_0x00c6:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startObjectScope()
            return r8
        L_0x00cb:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startTrueToken()
            return r8
        L_0x00d0:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startNullToken()
            return r8
        L_0x00d5:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startFalseToken()
            return r8
        L_0x00da:
            com.fasterxml.jackson.core.JsonParser$Feature r8 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA
            boolean r8 = r7.isEnabled(r8)
            if (r8 == 0) goto L_0x00e7
            com.fasterxml.jackson.core.JsonToken r8 = r7._closeArrayScope()
            return r8
        L_0x00e7:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startUnexpectedValue(r6, r1)
            return r8
        L_0x00ec:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startArrayScope()
            return r8
        L_0x00f1:
            com.fasterxml.jackson.core.JsonToken r8 = r7._startNegativeNumber()
            return r8
        L_0x00f6:
            com.fasterxml.jackson.core.JsonToken r8 = r7._finishHashComment(r2)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._startValueExpectComma(int):com.fasterxml.jackson.core.JsonToken");
    }

    /* JADX WARNING: type inference failed for: r1v4, types: [int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v3, types: [int, byte] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _startValueExpectColon(int r5) throws java.io.IOException {
        /*
            r4 = this;
            r0 = 32
            r1 = 14
            if (r5 > r0) goto L_0x0011
            int r5 = r4._skipWS(r5)
            if (r5 > 0) goto L_0x0011
            r4._minorState = r1
            com.fasterxml.jackson.core.JsonToken r5 = r4._currToken
            return r5
        L_0x0011:
            r2 = 58
            r3 = 35
            if (r5 == r2) goto L_0x002c
            r2 = 47
            if (r5 != r2) goto L_0x0020
            com.fasterxml.jackson.core.JsonToken r5 = r4._startSlashComment(r1)
            return r5
        L_0x0020:
            if (r5 != r3) goto L_0x0027
            com.fasterxml.jackson.core.JsonToken r5 = r4._finishHashComment(r1)
            return r5
        L_0x0027:
            java.lang.String r1 = "was expecting a colon to separate field name and value"
            r4._reportUnexpectedChar(r5, r1)
        L_0x002c:
            int r5 = r4._inputPtr
            int r1 = r4._inputEnd
            r2 = 12
            if (r5 < r1) goto L_0x003b
            r4._minorState = r2
            com.fasterxml.jackson.core.JsonToken r5 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            r4._currToken = r5
            return r5
        L_0x003b:
            byte[] r1 = r4._inputBuffer
            byte r1 = r1[r5]
            int r5 = r5 + 1
            r4._inputPtr = r5
            if (r1 > r0) goto L_0x0050
            int r1 = r4._skipWS(r1)
            if (r1 > 0) goto L_0x0050
            r4._minorState = r2
            com.fasterxml.jackson.core.JsonToken r5 = r4._currToken
            return r5
        L_0x0050:
            r4._updateTokenLocation()
            r5 = 34
            if (r1 != r5) goto L_0x005c
            com.fasterxml.jackson.core.JsonToken r5 = r4._startString()
            return r5
        L_0x005c:
            if (r1 == r3) goto L_0x00ac
            r5 = 45
            if (r1 == r5) goto L_0x00a7
            r5 = 91
            if (r1 == r5) goto L_0x00a2
            r5 = 102(0x66, float:1.43E-43)
            if (r1 == r5) goto L_0x009d
            r5 = 110(0x6e, float:1.54E-43)
            if (r1 == r5) goto L_0x0098
            r5 = 116(0x74, float:1.63E-43)
            if (r1 == r5) goto L_0x0093
            r5 = 123(0x7b, float:1.72E-43)
            if (r1 == r5) goto L_0x008e
            switch(r1) {
                case 47: goto L_0x0089;
                case 48: goto L_0x0084;
                case 49: goto L_0x007f;
                case 50: goto L_0x007f;
                case 51: goto L_0x007f;
                case 52: goto L_0x007f;
                case 53: goto L_0x007f;
                case 54: goto L_0x007f;
                case 55: goto L_0x007f;
                case 56: goto L_0x007f;
                case 57: goto L_0x007f;
                default: goto L_0x0079;
            }
        L_0x0079:
            r5 = 0
            com.fasterxml.jackson.core.JsonToken r5 = r4._startUnexpectedValue(r5, r1)
            return r5
        L_0x007f:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startPositiveNumber(r1)
            return r5
        L_0x0084:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startNumberLeadingZero()
            return r5
        L_0x0089:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startSlashComment(r2)
            return r5
        L_0x008e:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startObjectScope()
            return r5
        L_0x0093:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startTrueToken()
            return r5
        L_0x0098:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startNullToken()
            return r5
        L_0x009d:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startFalseToken()
            return r5
        L_0x00a2:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startArrayScope()
            return r5
        L_0x00a7:
            com.fasterxml.jackson.core.JsonToken r5 = r4._startNegativeNumber()
            return r5
        L_0x00ac:
            com.fasterxml.jackson.core.JsonToken r5 = r4._finishHashComment(r2)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._startValueExpectColon(int):com.fasterxml.jackson.core.JsonToken");
    }

    private final JsonToken _startValueAfterComma(int i) throws IOException {
        if (i <= 32) {
            i = _skipWS(i);
            if (i <= 0) {
                this._minorState = 15;
                return this._currToken;
            }
        }
        _updateTokenLocation();
        if (i == 34) {
            return _startString();
        }
        if (i == 35) {
            return _finishHashComment(15);
        }
        if (i == 45) {
            return _startNegativeNumber();
        }
        if (i == 91) {
            return _startArrayScope();
        }
        if (i != 93) {
            if (i == 102) {
                return _startFalseToken();
            }
            if (i == 110) {
                return _startNullToken();
            }
            if (i == 116) {
                return _startTrueToken();
            }
            if (i == 123) {
                return _startObjectScope();
            }
            if (i != 125) {
                switch (i) {
                    case 47:
                        return _startSlashComment(15);
                    case 48:
                        return _startNumberLeadingZero();
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                        return _startPositiveNumber(i);
                }
            } else if (isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
                return _closeObjectScope();
            }
        } else if (isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
            return _closeArrayScope();
        }
        return _startUnexpectedValue(true, i);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        if (r1._parsingContext.inArray() == false) goto L_0x0054;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.core.JsonToken _startUnexpectedValue(boolean r2, int r3) throws java.io.IOException {
        /*
            r1 = this;
            r2 = 39
            if (r3 == r2) goto L_0x0047
            r2 = 73
            r0 = 1
            if (r3 == r2) goto L_0x0042
            r2 = 78
            if (r3 == r2) goto L_0x003c
            r2 = 93
            if (r3 == r2) goto L_0x001f
            r2 = 125(0x7d, float:1.75E-43)
            if (r3 == r2) goto L_0x0054
            switch(r3) {
                case 43: goto L_0x0019;
                case 44: goto L_0x0028;
                default: goto L_0x0018;
            }
        L_0x0018:
            goto L_0x0054
        L_0x0019:
            r2 = 2
            com.fasterxml.jackson.core.JsonToken r2 = r1._finishNonStdToken(r2, r0)
            return r2
        L_0x001f:
            com.fasterxml.jackson.core.json.JsonReadContext r2 = r1._parsingContext
            boolean r2 = r2.inArray()
            if (r2 != 0) goto L_0x0028
            goto L_0x0054
        L_0x0028:
            com.fasterxml.jackson.core.JsonParser$Feature r2 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES
            boolean r2 = r1.isEnabled(r2)
            if (r2 == 0) goto L_0x0054
            int r2 = r1._inputPtr
            int r2 = r2 - r0
            r1._inputPtr = r2
            com.fasterxml.jackson.core.JsonToken r2 = com.fasterxml.jackson.core.JsonToken.VALUE_NULL
            com.fasterxml.jackson.core.JsonToken r2 = r1._valueComplete(r2)
            return r2
        L_0x003c:
            r2 = 0
            com.fasterxml.jackson.core.JsonToken r2 = r1._finishNonStdToken(r2, r0)
            return r2
        L_0x0042:
            com.fasterxml.jackson.core.JsonToken r2 = r1._finishNonStdToken(r0, r0)
            return r2
        L_0x0047:
            com.fasterxml.jackson.core.JsonParser$Feature r2 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES
            boolean r2 = r1.isEnabled(r2)
            if (r2 == 0) goto L_0x0054
            com.fasterxml.jackson.core.JsonToken r2 = r1._startAposString()
            return r2
        L_0x0054:
            java.lang.String r2 = "expected a valid value (number, String, array, object, 'true', 'false' or 'null')"
            r1._reportUnexpectedChar(r3, r2)
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._startUnexpectedValue(boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final int _skipWS(int i) throws IOException {
        do {
            if (i != 32) {
                if (i == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i == 13) {
                    this._currInputRowAlt++;
                    this._currInputRowStart = this._inputPtr;
                } else if (i != 9) {
                    _throwInvalidSpace(i);
                }
            }
            if (this._inputPtr >= this._inputEnd) {
                this._currToken = JsonToken.NOT_AVAILABLE;
                return 0;
            }
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            i = bArr[i2] & UnsignedBytes.MAX_VALUE;
        } while (i <= 32);
        return i;
    }

    private final JsonToken _startSlashComment(int i) throws IOException {
        if (!Feature.ALLOW_COMMENTS.enabledIn(this._features)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (this._inputPtr >= this._inputEnd) {
            this._pending32 = i;
            this._minorState = 51;
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2];
        if (b == 42) {
            return _finishCComment(i, false);
        }
        if (b == 47) {
            return _finishCppComment(i);
        }
        _reportUnexpectedChar(b & UnsignedBytes.MAX_VALUE, "was expecting either '*' or '/' for a comment");
        return null;
    }

    private final JsonToken _finishHashComment(int i) throws IOException {
        if (!Feature.ALLOW_YAML_COMMENTS.enabledIn(this._features)) {
            _reportUnexpectedChar(35, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_YAML_COMMENTS' not enabled for parser)");
        }
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b = bArr[i2] & UnsignedBytes.MAX_VALUE;
            if (b < 32) {
                if (b == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b == 13) {
                    this._currInputRowAlt++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b != 9) {
                    _throwInvalidSpace(b);
                }
                return _startAfterComment(i);
            }
        }
        this._minorState = 55;
        this._pending32 = i;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final JsonToken _finishCppComment(int i) throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b = bArr[i2] & UnsignedBytes.MAX_VALUE;
            if (b < 32) {
                if (b == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b == 13) {
                    this._currInputRowAlt++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b != 9) {
                    _throwInvalidSpace(b);
                }
                return _startAfterComment(i);
            }
        }
        this._minorState = 54;
        this._pending32 = i;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final JsonToken _finishCComment(int i, boolean z) throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = this._inputPtr;
            this._inputPtr = i2 + 1;
            byte b = bArr[i2] & UnsignedBytes.MAX_VALUE;
            if (b < 32) {
                if (b == 10) {
                    this._currInputRow++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b == 13) {
                    this._currInputRowAlt++;
                    this._currInputRowStart = this._inputPtr;
                } else if (b != 9) {
                    _throwInvalidSpace(b);
                }
            } else if (b == 42) {
                z = true;
            } else if (b == 47 && z) {
                return _startAfterComment(i);
            }
            z = false;
        }
        this._minorState = z ? 52 : 53;
        this._pending32 = i;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private final JsonToken _startAfterComment(int i) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            this._minorState = i;
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        byte[] bArr = this._inputBuffer;
        int i2 = this._inputPtr;
        this._inputPtr = i2 + 1;
        byte b = bArr[i2] & UnsignedBytes.MAX_VALUE;
        switch (i) {
            case 4:
                return _startFieldName(b);
            case 5:
                return _startFieldNameAfterComma(b);
            default:
                switch (i) {
                    case 12:
                        return _startValue(b);
                    case 13:
                        return _startValueExpectComma(b);
                    case 14:
                        return _startValueExpectColon(b);
                    case 15:
                        return _startValueAfterComma(b);
                    default:
                        VersionUtil.throwInternal();
                        return null;
                }
        }
    }

    /* access modifiers changed from: protected */
    public JsonToken _startFalseToken() throws IOException {
        int i = this._inputPtr;
        if (i + 4 < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = i + 1;
            if (bArr[i] == 97) {
                int i3 = i2 + 1;
                if (bArr[i2] == 108) {
                    int i4 = i3 + 1;
                    if (bArr[i3] == 115) {
                        int i5 = i4 + 1;
                        if (bArr[i4] == 101) {
                            byte b = bArr[i5] & UnsignedBytes.MAX_VALUE;
                            if (b < 48 || b == 93 || b == 125) {
                                this._inputPtr = i5;
                                return _valueComplete(JsonToken.VALUE_FALSE);
                            }
                        }
                    }
                }
            }
        }
        this._minorState = 18;
        return _finishKeywordToken("false", 1, JsonToken.VALUE_FALSE);
    }

    /* access modifiers changed from: protected */
    public JsonToken _startTrueToken() throws IOException {
        int i = this._inputPtr;
        if (i + 3 < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = i + 1;
            if (bArr[i] == 114) {
                int i3 = i2 + 1;
                if (bArr[i2] == 117) {
                    int i4 = i3 + 1;
                    if (bArr[i3] == 101) {
                        byte b = bArr[i4] & UnsignedBytes.MAX_VALUE;
                        if (b < 48 || b == 93 || b == 125) {
                            this._inputPtr = i4;
                            return _valueComplete(JsonToken.VALUE_TRUE);
                        }
                    }
                }
            }
        }
        this._minorState = 17;
        return _finishKeywordToken("true", 1, JsonToken.VALUE_TRUE);
    }

    /* access modifiers changed from: protected */
    public JsonToken _startNullToken() throws IOException {
        int i = this._inputPtr;
        if (i + 3 < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i2 = i + 1;
            if (bArr[i] == 117) {
                int i3 = i2 + 1;
                if (bArr[i2] == 108) {
                    int i4 = i3 + 1;
                    if (bArr[i3] == 108) {
                        byte b = bArr[i4] & UnsignedBytes.MAX_VALUE;
                        if (b < 48 || b == 93 || b == 125) {
                            this._inputPtr = i4;
                            return _valueComplete(JsonToken.VALUE_NULL);
                        }
                    }
                }
            }
        }
        this._minorState = 16;
        return _finishKeywordToken("null", 1, JsonToken.VALUE_NULL);
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishKeywordToken(String str, int i, JsonToken jsonToken) throws IOException {
        int length = str.length();
        while (this._inputPtr < this._inputEnd) {
            byte b = this._inputBuffer[this._inputPtr];
            if (i == length) {
                if (b < 48 || b == 93 || b == 125) {
                    return _valueComplete(jsonToken);
                }
            } else if (b == str.charAt(i)) {
                i++;
                this._inputPtr++;
            }
            this._minorState = 50;
            this._textBuffer.resetWithCopy(str, 0, i);
            return _finishErrorToken();
        }
        this._pending32 = i;
        JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken2;
        return jsonToken2;
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishKeywordTokenWithEOF(String str, int i, JsonToken jsonToken) throws IOException {
        if (i == str.length()) {
            this._currToken = jsonToken;
            return jsonToken;
        }
        this._textBuffer.resetWithCopy(str, 0, i);
        return _finishErrorTokenWithEOF();
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNonStdToken(int i, int i2) throws IOException {
        String _nonStdToken = _nonStdToken(i);
        int length = _nonStdToken.length();
        while (this._inputPtr < this._inputEnd) {
            byte b = this._inputBuffer[this._inputPtr];
            if (i2 == length) {
                if (b < 48 || b == 93 || b == 125) {
                    return _valueNonStdNumberComplete(i);
                }
            } else if (b == _nonStdToken.charAt(i2)) {
                i2++;
                this._inputPtr++;
            }
            this._minorState = 50;
            this._textBuffer.resetWithCopy(_nonStdToken, 0, i2);
            return _finishErrorToken();
        }
        this._nonStdTokenType = i;
        this._pending32 = i2;
        this._minorState = 19;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNonStdTokenWithEOF(int i, int i2) throws IOException {
        String _nonStdToken = _nonStdToken(i);
        if (i2 == _nonStdToken.length()) {
            return _valueNonStdNumberComplete(i);
        }
        this._textBuffer.resetWithCopy(_nonStdToken, 0, i2);
        return _finishErrorTokenWithEOF();
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishErrorToken() throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            char c = (char) bArr[i];
            if (Character.isJavaIdentifierPart(c)) {
                this._textBuffer.append(c);
                if (this._textBuffer.size() >= 256) {
                }
            }
            return _reportErrorToken(this._textBuffer.contentsAsString());
        }
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishErrorTokenWithEOF() throws IOException {
        return _reportErrorToken(this._textBuffer.contentsAsString());
    }

    /* access modifiers changed from: protected */
    public JsonToken _reportErrorToken(String str) throws IOException {
        _reportError("Unrecognized token '%s': was expecting %s", this._textBuffer.contentsAsString(), "'null', 'true' or 'false'");
        return JsonToken.NOT_AVAILABLE;
    }

    /* access modifiers changed from: protected */
    public JsonToken _startPositiveNumber(int i) throws IOException {
        this._numberNegative = false;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        emptyAndGetCurrentSegment[0] = (char) i;
        if (this._inputPtr >= this._inputEnd) {
            this._minorState = 26;
            this._textBuffer.setCurrentLength(1);
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        byte b = this._inputBuffer[this._inputPtr] & UnsignedBytes.MAX_VALUE;
        int i2 = 1;
        while (true) {
            if (b < 48) {
                if (b == 46) {
                    this._intLength = i2;
                    this._inputPtr++;
                    return _startFloat(emptyAndGetCurrentSegment, i2, b);
                }
            } else if (b <= 57) {
                if (i2 >= emptyAndGetCurrentSegment.length) {
                    emptyAndGetCurrentSegment = this._textBuffer.expandCurrentSegment();
                }
                int i3 = i2 + 1;
                emptyAndGetCurrentSegment[i2] = (char) b;
                int i4 = this._inputPtr + 1;
                this._inputPtr = i4;
                if (i4 >= this._inputEnd) {
                    this._minorState = 26;
                    this._textBuffer.setCurrentLength(i3);
                    JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
                    this._currToken = jsonToken2;
                    return jsonToken2;
                }
                b = this._inputBuffer[this._inputPtr] & UnsignedBytes.MAX_VALUE;
                i2 = i3;
            } else if (b == 101 || b == 69) {
                this._intLength = i2;
                this._inputPtr++;
                return _startFloat(emptyAndGetCurrentSegment, i2, b);
            }
        }
        this._intLength = i2;
        this._textBuffer.setCurrentLength(i2);
        return _valueComplete(JsonToken.VALUE_NUMBER_INT);
    }

    /* access modifiers changed from: protected */
    public JsonToken _startNegativeNumber() throws IOException {
        this._numberNegative = true;
        if (this._inputPtr >= this._inputEnd) {
            this._minorState = 23;
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
        int i2 = 2;
        if (b <= 48) {
            if (b == 48) {
                return _finishNumberLeadingNegZeroes();
            }
            reportUnexpectedNumberChar(b, "expected digit (0-9) to follow minus sign, for valid numeric value");
        } else if (b > 57) {
            if (b == 73) {
                return _finishNonStdToken(3, 2);
            }
            reportUnexpectedNumberChar(b, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        emptyAndGetCurrentSegment[0] = '-';
        emptyAndGetCurrentSegment[1] = (char) b;
        if (this._inputPtr >= this._inputEnd) {
            this._minorState = 26;
            this._textBuffer.setCurrentLength(2);
            this._intLength = 1;
            JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken2;
            return jsonToken2;
        }
        byte b2 = this._inputBuffer[this._inputPtr];
        while (true) {
            if (b2 < 48) {
                if (b2 == 46) {
                    this._intLength = i2 - 1;
                    this._inputPtr++;
                    return _startFloat(emptyAndGetCurrentSegment, i2, b2);
                }
            } else if (b2 <= 57) {
                if (i2 >= emptyAndGetCurrentSegment.length) {
                    emptyAndGetCurrentSegment = this._textBuffer.expandCurrentSegment();
                }
                int i3 = i2 + 1;
                emptyAndGetCurrentSegment[i2] = (char) b2;
                int i4 = this._inputPtr + 1;
                this._inputPtr = i4;
                if (i4 >= this._inputEnd) {
                    this._minorState = 26;
                    this._textBuffer.setCurrentLength(i3);
                    JsonToken jsonToken3 = JsonToken.NOT_AVAILABLE;
                    this._currToken = jsonToken3;
                    return jsonToken3;
                }
                b2 = this._inputBuffer[this._inputPtr] & UnsignedBytes.MAX_VALUE;
                i2 = i3;
            } else if (b2 == 101 || b2 == 69) {
                this._intLength = i2 - 1;
                this._inputPtr++;
                return _startFloat(emptyAndGetCurrentSegment, i2, b2);
            }
        }
        this._intLength = i2 - 1;
        this._textBuffer.setCurrentLength(i2);
        return _valueComplete(JsonToken.VALUE_NUMBER_INT);
    }

    /* access modifiers changed from: protected */
    public JsonToken _startNumberLeadingZero() throws IOException {
        int i = this._inputPtr;
        if (i >= this._inputEnd) {
            this._minorState = 24;
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        int i2 = i + 1;
        byte b = this._inputBuffer[i] & UnsignedBytes.MAX_VALUE;
        if (b < 48) {
            if (b == 46) {
                this._inputPtr = i2;
                this._intLength = 1;
                char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
                emptyAndGetCurrentSegment[0] = '0';
                return _startFloat(emptyAndGetCurrentSegment, 1, b);
            }
        } else if (b <= 57) {
            return _finishNumberLeadingZeroes();
        } else {
            if (b == 101 || b == 69) {
                this._inputPtr = i2;
                this._intLength = 1;
                char[] emptyAndGetCurrentSegment2 = this._textBuffer.emptyAndGetCurrentSegment();
                emptyAndGetCurrentSegment2[0] = '0';
                return _startFloat(emptyAndGetCurrentSegment2, 1, b);
            } else if (!(b == 93 || b == 125)) {
                reportUnexpectedNumberChar(b, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
            }
        }
        return _valueCompleteInt(0, "0");
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNumberMinus(int i) throws IOException {
        if (i <= 48) {
            if (i == 48) {
                return _finishNumberLeadingNegZeroes();
            }
            reportUnexpectedNumberChar(i, "expected digit (0-9) to follow minus sign, for valid numeric value");
        } else if (i > 57) {
            if (i == 73) {
                return _finishNonStdToken(3, 2);
            }
            reportUnexpectedNumberChar(i, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        emptyAndGetCurrentSegment[0] = '-';
        emptyAndGetCurrentSegment[1] = (char) i;
        this._intLength = 1;
        return _finishNumberIntegralPart(emptyAndGetCurrentSegment, 2);
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNumberLeadingZeroes() throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
            if (b < 48) {
                if (b == 46) {
                    char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
                    emptyAndGetCurrentSegment[0] = '0';
                    this._intLength = 1;
                    return _startFloat(emptyAndGetCurrentSegment, 1, b);
                }
            } else if (b <= 57) {
                if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                    reportInvalidNumber("Leading zeroes not allowed");
                    continue;
                }
                if (b != 48) {
                    char[] emptyAndGetCurrentSegment2 = this._textBuffer.emptyAndGetCurrentSegment();
                    emptyAndGetCurrentSegment2[0] = (char) b;
                    this._intLength = 1;
                    return _finishNumberIntegralPart(emptyAndGetCurrentSegment2, 1);
                }
            } else if (b == 101 || b == 69) {
                char[] emptyAndGetCurrentSegment3 = this._textBuffer.emptyAndGetCurrentSegment();
                emptyAndGetCurrentSegment3[0] = '0';
                this._intLength = 1;
                return _startFloat(emptyAndGetCurrentSegment3, 1, b);
            } else if (!(b == 93 || b == 125)) {
                reportUnexpectedNumberChar(b, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
            }
            this._inputPtr--;
            return _valueCompleteInt(0, "0");
        }
        this._minorState = 24;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNumberLeadingNegZeroes() throws IOException {
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i = this._inputPtr;
            this._inputPtr = i + 1;
            byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
            if (b < 48) {
                if (b == 46) {
                    char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
                    emptyAndGetCurrentSegment[0] = '-';
                    emptyAndGetCurrentSegment[1] = '0';
                    this._intLength = 1;
                    return _startFloat(emptyAndGetCurrentSegment, 2, b);
                }
            } else if (b <= 57) {
                if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                    reportInvalidNumber("Leading zeroes not allowed");
                    continue;
                }
                if (b != 48) {
                    char[] emptyAndGetCurrentSegment2 = this._textBuffer.emptyAndGetCurrentSegment();
                    emptyAndGetCurrentSegment2[0] = '-';
                    emptyAndGetCurrentSegment2[1] = (char) b;
                    this._intLength = 1;
                    return _finishNumberIntegralPart(emptyAndGetCurrentSegment2, 2);
                }
            } else if (b == 101 || b == 69) {
                char[] emptyAndGetCurrentSegment3 = this._textBuffer.emptyAndGetCurrentSegment();
                emptyAndGetCurrentSegment3[0] = '-';
                emptyAndGetCurrentSegment3[1] = '0';
                this._intLength = 1;
                return _startFloat(emptyAndGetCurrentSegment3, 2, b);
            } else if (!(b == 93 || b == 125)) {
                reportUnexpectedNumberChar(b, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
            }
            this._inputPtr--;
            return _valueCompleteInt(0, "0");
        }
        this._minorState = 25;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public JsonToken _finishNumberIntegralPart(char[] cArr, int i) throws IOException {
        int i2 = this._numberNegative ? -1 : 0;
        while (this._inputPtr < this._inputEnd) {
            byte b = this._inputBuffer[this._inputPtr] & UnsignedBytes.MAX_VALUE;
            if (b < 48) {
                if (b == 46) {
                    this._intLength = i2 + i;
                    this._inputPtr++;
                    return _startFloat(cArr, i, b);
                }
            } else if (b <= 57) {
                this._inputPtr++;
                if (i >= cArr.length) {
                    cArr = this._textBuffer.expandCurrentSegment();
                }
                int i3 = i + 1;
                cArr[i] = (char) b;
                i = i3;
            } else if (b == 101 || b == 69) {
                this._intLength = i2 + i;
                this._inputPtr++;
                return _startFloat(cArr, i, b);
            }
            this._intLength = i2 + i;
            this._textBuffer.setCurrentLength(i);
            return _valueComplete(JsonToken.VALUE_NUMBER_INT);
        }
        this._minorState = 26;
        this._textBuffer.setCurrentLength(i);
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public JsonToken _startFloat(char[] cArr, int i, int i2) throws IOException {
        char[] cArr2;
        int i3;
        int i4;
        int i5;
        int i6 = 0;
        if (i2 == 46) {
            if (i >= cArr.length) {
                cArr = this._textBuffer.expandCurrentSegment();
            }
            int i7 = i + 1;
            cArr[i] = '.';
            int i8 = i7;
            char[] cArr3 = cArr;
            i3 = 0;
            while (this._inputPtr < this._inputEnd) {
                byte[] bArr = this._inputBuffer;
                int i9 = this._inputPtr;
                this._inputPtr = i9 + 1;
                byte b = bArr[i9];
                if (b < 48 || b > 57) {
                    byte b2 = b & UnsignedBytes.MAX_VALUE;
                    if (i3 == 0) {
                        reportUnexpectedNumberChar(b2, "Decimal point not followed by a digit");
                    }
                    int i10 = b2;
                    i4 = i8;
                    cArr2 = cArr3;
                    i2 = i10;
                } else {
                    if (i8 >= cArr3.length) {
                        cArr3 = this._textBuffer.expandCurrentSegment();
                    }
                    int i11 = i8 + 1;
                    cArr3[i8] = (char) b;
                    i3++;
                    i8 = i11;
                }
            }
            this._textBuffer.setCurrentLength(i8);
            this._minorState = 30;
            this._fractLength = i3;
            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
            this._currToken = jsonToken;
            return jsonToken;
        }
        i4 = i;
        cArr2 = cArr;
        i3 = 0;
        this._fractLength = i3;
        if (i2 == 101 || i2 == 69) {
            if (i4 >= cArr2.length) {
                cArr2 = this._textBuffer.expandCurrentSegment();
            }
            int i12 = i4 + 1;
            cArr2[i4] = (char) i2;
            if (this._inputPtr >= this._inputEnd) {
                this._textBuffer.setCurrentLength(i12);
                this._minorState = 31;
                this._expLength = 0;
                JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
                this._currToken = jsonToken2;
                return jsonToken2;
            }
            byte[] bArr2 = this._inputBuffer;
            int i13 = this._inputPtr;
            this._inputPtr = i13 + 1;
            byte b3 = bArr2[i13];
            if (b3 == 45 || b3 == 43) {
                if (i12 >= cArr2.length) {
                    cArr2 = this._textBuffer.expandCurrentSegment();
                }
                i5 = i12 + 1;
                cArr2[i12] = (char) b3;
                if (this._inputPtr >= this._inputEnd) {
                    this._textBuffer.setCurrentLength(i5);
                    this._minorState = 32;
                    this._expLength = 0;
                    JsonToken jsonToken3 = JsonToken.NOT_AVAILABLE;
                    this._currToken = jsonToken3;
                    return jsonToken3;
                }
                byte[] bArr3 = this._inputBuffer;
                int i14 = this._inputPtr;
                this._inputPtr = i14 + 1;
                b3 = bArr3[i14];
            } else {
                i5 = i12;
            }
            while (b3 >= 48 && b3 <= 57) {
                i6++;
                if (i4 >= cArr2.length) {
                    cArr2 = this._textBuffer.expandCurrentSegment();
                }
                int i15 = i4 + 1;
                cArr2[i4] = (char) b3;
                if (this._inputPtr >= this._inputEnd) {
                    this._textBuffer.setCurrentLength(i15);
                    this._minorState = 32;
                    this._expLength = i6;
                    JsonToken jsonToken4 = JsonToken.NOT_AVAILABLE;
                    this._currToken = jsonToken4;
                    return jsonToken4;
                }
                byte[] bArr4 = this._inputBuffer;
                int i16 = this._inputPtr;
                this._inputPtr = i16 + 1;
                b3 = bArr4[i16];
                i5 = i15;
            }
            byte b4 = b3 & UnsignedBytes.MAX_VALUE;
            if (i6 == 0) {
                reportUnexpectedNumberChar(b4, "Exponent indicator not followed by a digit");
            }
        }
        this._inputPtr--;
        this._textBuffer.setCurrentLength(i4);
        this._expLength = i6;
        return _valueComplete(JsonToken.VALUE_NUMBER_FLOAT);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.core.JsonToken _finishFloatFraction() throws java.io.IOException {
        /*
            r6 = this;
            int r0 = r6._fractLength
            com.fasterxml.jackson.core.util.TextBuffer r1 = r6._textBuffer
            char[] r1 = r1.getBufferWithoutReset()
            com.fasterxml.jackson.core.util.TextBuffer r2 = r6._textBuffer
            int r2 = r2.getCurrentSegmentSize()
        L_0x000e:
            byte[] r3 = r6._inputBuffer
            int r4 = r6._inputPtr
            int r5 = r4 + 1
            r6._inputPtr = r5
            byte r3 = r3[r4]
            r4 = 48
            if (r3 < r4) goto L_0x0042
            r4 = 57
            if (r3 > r4) goto L_0x0042
            int r0 = r0 + 1
            int r4 = r1.length
            if (r2 < r4) goto L_0x002b
            com.fasterxml.jackson.core.util.TextBuffer r1 = r6._textBuffer
            char[] r1 = r1.expandCurrentSegment()
        L_0x002b:
            int r4 = r2 + 1
            char r3 = (char) r3
            r1[r2] = r3
            int r2 = r6._inputPtr
            int r3 = r6._inputEnd
            if (r2 < r3) goto L_0x0040
            com.fasterxml.jackson.core.util.TextBuffer r1 = r6._textBuffer
            r1.setCurrentLength(r4)
            r6._fractLength = r0
            com.fasterxml.jackson.core.JsonToken r0 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            return r0
        L_0x0040:
            r2 = r4
            goto L_0x000e
        L_0x0042:
            if (r0 != 0) goto L_0x0049
            java.lang.String r1 = "Decimal point not followed by a digit"
            r6.reportUnexpectedNumberChar(r3, r1)
        L_0x0049:
            r6._fractLength = r0
            com.fasterxml.jackson.core.util.TextBuffer r0 = r6._textBuffer
            r0.setCurrentLength(r2)
            r0 = 101(0x65, float:1.42E-43)
            r1 = 0
            r4 = 1
            if (r3 == r0) goto L_0x006e
            r0 = 69
            if (r3 != r0) goto L_0x005b
            goto L_0x006e
        L_0x005b:
            int r0 = r6._inputPtr
            int r0 = r0 - r4
            r6._inputPtr = r0
            com.fasterxml.jackson.core.util.TextBuffer r0 = r6._textBuffer
            r0.setCurrentLength(r2)
            r6._expLength = r1
            com.fasterxml.jackson.core.JsonToken r0 = com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT
            com.fasterxml.jackson.core.JsonToken r0 = r6._valueComplete(r0)
            return r0
        L_0x006e:
            com.fasterxml.jackson.core.util.TextBuffer r0 = r6._textBuffer
            char r2 = (char) r3
            r0.append(r2)
            r6._expLength = r1
            int r0 = r6._inputPtr
            int r1 = r6._inputEnd
            if (r0 < r1) goto L_0x0083
            r0 = 31
            r6._minorState = r0
            com.fasterxml.jackson.core.JsonToken r0 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            return r0
        L_0x0083:
            r0 = 32
            r6._minorState = r0
            byte[] r0 = r6._inputBuffer
            int r1 = r6._inputPtr
            int r2 = r1 + 1
            r6._inputPtr = r2
            byte r0 = r0[r1]
            r0 = r0 & 255(0xff, float:3.57E-43)
            com.fasterxml.jackson.core.JsonToken r0 = r6._finishFloatExponent(r4, r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._finishFloatFraction():com.fasterxml.jackson.core.JsonToken");
    }

    /* JADX WARNING: type inference failed for: r6v6, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r6v7, types: [byte] */
    /* JADX WARNING: type inference failed for: r5v16, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r6v11, types: [byte] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r6v11, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r6v7, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r5v16, types: [byte[]] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r6v6, types: [byte[]] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.core.JsonToken _finishFloatExponent(boolean r5, int r6) throws java.io.IOException {
        /*
            r4 = this;
            if (r5 == 0) goto L_0x002c
            r5 = 32
            r4._minorState = r5
            r0 = 45
            if (r6 == r0) goto L_0x000e
            r0 = 43
            if (r6 != r0) goto L_0x002c
        L_0x000e:
            com.fasterxml.jackson.core.util.TextBuffer r0 = r4._textBuffer
            char r6 = (char) r6
            r0.append(r6)
            int r6 = r4._inputPtr
            int r0 = r4._inputEnd
            if (r6 < r0) goto L_0x0022
            r4._minorState = r5
            r5 = 0
            r4._expLength = r5
            com.fasterxml.jackson.core.JsonToken r5 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            return r5
        L_0x0022:
            byte[] r5 = r4._inputBuffer
            int r6 = r4._inputPtr
            int r0 = r6 + 1
            r4._inputPtr = r0
            byte r6 = r5[r6]
        L_0x002c:
            com.fasterxml.jackson.core.util.TextBuffer r5 = r4._textBuffer
            char[] r5 = r5.getBufferWithoutReset()
            com.fasterxml.jackson.core.util.TextBuffer r0 = r4._textBuffer
            int r0 = r0.getCurrentSegmentSize()
            int r1 = r4._expLength
        L_0x003a:
            r2 = 48
            if (r6 < r2) goto L_0x006e
            r2 = 57
            if (r6 > r2) goto L_0x006e
            int r1 = r1 + 1
            int r2 = r5.length
            if (r0 < r2) goto L_0x004d
            com.fasterxml.jackson.core.util.TextBuffer r5 = r4._textBuffer
            char[] r5 = r5.expandCurrentSegment()
        L_0x004d:
            int r2 = r0 + 1
            char r6 = (char) r6
            r5[r0] = r6
            int r6 = r4._inputPtr
            int r0 = r4._inputEnd
            if (r6 < r0) goto L_0x0062
            com.fasterxml.jackson.core.util.TextBuffer r5 = r4._textBuffer
            r5.setCurrentLength(r2)
            r4._expLength = r1
            com.fasterxml.jackson.core.JsonToken r5 = com.fasterxml.jackson.core.JsonToken.NOT_AVAILABLE
            return r5
        L_0x0062:
            byte[] r6 = r4._inputBuffer
            int r0 = r4._inputPtr
            int r3 = r0 + 1
            r4._inputPtr = r3
            byte r6 = r6[r0]
            r0 = r2
            goto L_0x003a
        L_0x006e:
            r5 = r6 & 255(0xff, float:3.57E-43)
            if (r1 != 0) goto L_0x0077
            java.lang.String r6 = "Exponent indicator not followed by a digit"
            r4.reportUnexpectedNumberChar(r5, r6)
        L_0x0077:
            int r5 = r4._inputPtr
            int r5 = r5 + -1
            r4._inputPtr = r5
            com.fasterxml.jackson.core.util.TextBuffer r5 = r4._textBuffer
            r5.setCurrentLength(r0)
            r4._expLength = r1
            com.fasterxml.jackson.core.JsonToken r5 = com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT
            com.fasterxml.jackson.core.JsonToken r5 = r4._valueComplete(r5)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._finishFloatExponent(boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final String _fastParseName() throws IOException {
        byte[] bArr = this._inputBuffer;
        int[] iArr = _icLatin1;
        int i = this._inputPtr;
        int i2 = i + 1;
        byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
        if (iArr[b] == 0) {
            int i3 = i2 + 1;
            byte b2 = bArr[i2] & UnsignedBytes.MAX_VALUE;
            if (iArr[b2] == 0) {
                byte b3 = (b << 8) | b2;
                int i4 = i3 + 1;
                byte b4 = bArr[i3] & UnsignedBytes.MAX_VALUE;
                if (iArr[b4] == 0) {
                    byte b5 = (b3 << 8) | b4;
                    int i5 = i4 + 1;
                    byte b6 = bArr[i4] & UnsignedBytes.MAX_VALUE;
                    if (iArr[b6] == 0) {
                        byte b7 = (b5 << 8) | b6;
                        int i6 = i5 + 1;
                        byte b8 = bArr[i5] & UnsignedBytes.MAX_VALUE;
                        if (iArr[b8] == 0) {
                            this._quad1 = b7;
                            return _parseMediumName(i6, b8);
                        } else if (b8 != 34) {
                            return null;
                        } else {
                            this._inputPtr = i6;
                            return _findName(b7, 4);
                        }
                    } else if (b6 != 34) {
                        return null;
                    } else {
                        this._inputPtr = i5;
                        return _findName(b5, 3);
                    }
                } else if (b4 != 34) {
                    return null;
                } else {
                    this._inputPtr = i4;
                    return _findName(b3, 2);
                }
            } else if (b2 != 34) {
                return null;
            } else {
                this._inputPtr = i3;
                return _findName(b, 1);
            }
        } else if (b != 34) {
            return null;
        } else {
            this._inputPtr = i2;
            return "";
        }
    }

    private final String _parseMediumName(int i, int i2) throws IOException {
        byte[] bArr = this._inputBuffer;
        int[] iArr = _icLatin1;
        int i3 = i + 1;
        byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
        if (iArr[b] == 0) {
            byte b2 = b | (i2 << 8);
            int i4 = i3 + 1;
            byte b3 = bArr[i3] & UnsignedBytes.MAX_VALUE;
            if (iArr[b3] == 0) {
                byte b4 = (b2 << 8) | b3;
                int i5 = i4 + 1;
                byte b5 = bArr[i4] & UnsignedBytes.MAX_VALUE;
                if (iArr[b5] == 0) {
                    byte b6 = (b4 << 8) | b5;
                    int i6 = i5 + 1;
                    byte b7 = bArr[i5] & UnsignedBytes.MAX_VALUE;
                    if (iArr[b7] == 0) {
                        return _parseMediumName2(i6, b7, b6);
                    }
                    if (b7 != 34) {
                        return null;
                    }
                    this._inputPtr = i6;
                    return _findName(this._quad1, b6, 4);
                } else if (b5 != 34) {
                    return null;
                } else {
                    this._inputPtr = i5;
                    return _findName(this._quad1, b4, 3);
                }
            } else if (b3 != 34) {
                return null;
            } else {
                this._inputPtr = i4;
                return _findName(this._quad1, b2, 2);
            }
        } else if (b != 34) {
            return null;
        } else {
            this._inputPtr = i3;
            return _findName(this._quad1, i2, 1);
        }
    }

    private final String _parseMediumName2(int i, int i2, int i3) throws IOException {
        byte[] bArr = this._inputBuffer;
        int[] iArr = _icLatin1;
        int i4 = i + 1;
        byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
        if (iArr[b] == 0) {
            byte b2 = b | (i2 << 8);
            int i5 = i4 + 1;
            byte b3 = bArr[i4] & UnsignedBytes.MAX_VALUE;
            if (iArr[b3] == 0) {
                byte b4 = (b2 << 8) | b3;
                int i6 = i5 + 1;
                byte b5 = bArr[i5] & UnsignedBytes.MAX_VALUE;
                if (iArr[b5] == 0) {
                    byte b6 = (b4 << 8) | b5;
                    int i7 = i6 + 1;
                    if ((bArr[i6] & UnsignedBytes.MAX_VALUE) != 34) {
                        return null;
                    }
                    this._inputPtr = i7;
                    return _findName(this._quad1, i3, b6, 4);
                } else if (b5 != 34) {
                    return null;
                } else {
                    this._inputPtr = i6;
                    return _findName(this._quad1, i3, b4, 3);
                }
            } else if (b3 != 34) {
                return null;
            } else {
                this._inputPtr = i5;
                return _findName(this._quad1, i3, b2, 2);
            }
        } else if (b != 34) {
            return null;
        } else {
            this._inputPtr = i4;
            return _findName(this._quad1, i3, i2, 1);
        }
    }

    private final JsonToken _parseEscapedName(int i, int i2, int i3) throws IOException {
        int[] iArr = this._quadBuffer;
        int[] iArr2 = _icLatin1;
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            int i5 = bArr[i4] & UnsignedBytes.MAX_VALUE;
            if (iArr2[i5] == 0) {
                if (r11 < 4) {
                    i3 = r11 + 1;
                    i2 = (r10 << 8) | i5;
                } else {
                    if (i >= iArr.length) {
                        int[] growArrayBy = growArrayBy(iArr, iArr.length);
                        this._quadBuffer = growArrayBy;
                        iArr = growArrayBy;
                    }
                    int i6 = i + 1;
                    iArr[i] = r10;
                    i = i6;
                    i2 = i5;
                    i3 = 1;
                }
            } else if (i5 == 34) {
                if (r11 > 0) {
                    if (i >= iArr.length) {
                        iArr = growArrayBy(iArr, iArr.length);
                        this._quadBuffer = iArr;
                    }
                    int i7 = i + 1;
                    iArr[i] = _padLastQuad(r10, r11);
                    i = i7;
                } else if (i == 0) {
                    return _fieldComplete("");
                }
                String findName = this._symbols.findName(iArr, i);
                if (findName == null) {
                    findName = _addName(iArr, i, r11);
                }
                return _fieldComplete(findName);
            } else {
                if (i5 != 92) {
                    _throwUnquotedSpace(i5, "name");
                } else {
                    i5 = _decodeCharEscape();
                    if (i5 < 0) {
                        this._minorState = 8;
                        this._minorStateAfterSplit = 7;
                        this._quadLength = i;
                        this._pending32 = r10;
                        this._pendingBytes = r11;
                        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
                        this._currToken = jsonToken;
                        return jsonToken;
                    }
                }
                if (i >= iArr.length) {
                    iArr = growArrayBy(iArr, iArr.length);
                    this._quadBuffer = iArr;
                }
                if (i5 > 127) {
                    if (r11 >= 4) {
                        int i8 = i + 1;
                        iArr[i] = r10;
                        i = i8;
                        r10 = 0;
                        r11 = 0;
                    }
                    if (i5 < 2048) {
                        r10 = (r10 << 8) | (i5 >> 6) | 192;
                        r11++;
                    } else {
                        int i9 = (r10 << 8) | (i5 >> 12) | DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession;
                        int i10 = r11 + 1;
                        if (i10 >= 4) {
                            int i11 = i + 1;
                            iArr[i] = i9;
                            i = i11;
                            i9 = 0;
                            i10 = 0;
                        }
                        r10 = (i9 << 8) | ((i5 >> 6) & 63) | 128;
                        r11 = i10 + 1;
                    }
                    i5 = (i5 & 63) | 128;
                }
                if (r11 < 4) {
                    i3 = r11 + 1;
                    i2 = (r10 << 8) | i5;
                } else {
                    int i12 = i + 1;
                    iArr[i] = r10;
                    i = i12;
                    i2 = i5;
                    i3 = 1;
                }
            }
        }
        this._quadLength = i;
        this._pending32 = r10;
        this._pendingBytes = r11;
        this._minorState = 7;
        JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken2;
        return jsonToken2;
    }

    private JsonToken _handleOddName(int i) throws IOException {
        if (i != 35) {
            if (i != 39) {
                if (i == 47) {
                    return _startSlashComment(4);
                }
                if (i == 93) {
                    return _closeArrayScope();
                }
            } else if (isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                return _finishAposName(0, 0, 0);
            }
        } else if (Feature.ALLOW_YAML_COMMENTS.enabledIn(this._features)) {
            return _finishHashComment(4);
        }
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar((char) i, "was expecting double-quote to start field name");
        }
        if (CharTypes.getInputCodeUtf8JsNames()[i] != 0) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        return _finishUnquotedName(0, i, 1);
    }

    private JsonToken _finishUnquotedName(int i, int i2, int i3) throws IOException {
        int[] iArr = this._quadBuffer;
        int[] inputCodeUtf8JsNames = CharTypes.getInputCodeUtf8JsNames();
        while (this._inputPtr < this._inputEnd) {
            byte b = this._inputBuffer[this._inputPtr] & UnsignedBytes.MAX_VALUE;
            if (inputCodeUtf8JsNames[b] != 0) {
                if (i3 > 0) {
                    if (i >= iArr.length) {
                        iArr = growArrayBy(iArr, iArr.length);
                        this._quadBuffer = iArr;
                    }
                    int i4 = i + 1;
                    iArr[i] = i2;
                    i = i4;
                }
                String findName = this._symbols.findName(iArr, i);
                if (findName == null) {
                    findName = _addName(iArr, i, i3);
                }
                return _fieldComplete(findName);
            }
            this._inputPtr++;
            if (i3 < 4) {
                i3++;
                i2 = (i2 << 8) | b;
            } else {
                if (i >= iArr.length) {
                    iArr = growArrayBy(iArr, iArr.length);
                    this._quadBuffer = iArr;
                }
                int i5 = i + 1;
                iArr[i] = i2;
                i = i5;
                i2 = b;
                i3 = 1;
            }
        }
        this._quadLength = i;
        this._pending32 = i2;
        this._pendingBytes = i3;
        this._minorState = 10;
        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken;
        return jsonToken;
    }

    private JsonToken _finishAposName(int i, int i2, int i3) throws IOException {
        int[] iArr = this._quadBuffer;
        int[] iArr2 = _icLatin1;
        while (this._inputPtr < this._inputEnd) {
            byte[] bArr = this._inputBuffer;
            int i4 = this._inputPtr;
            this._inputPtr = i4 + 1;
            int i5 = bArr[i4] & UnsignedBytes.MAX_VALUE;
            if (i5 == 39) {
                if (r11 > 0) {
                    if (i >= iArr.length) {
                        iArr = growArrayBy(iArr, iArr.length);
                        this._quadBuffer = iArr;
                    }
                    int i6 = i + 1;
                    iArr[i] = _padLastQuad(r10, r11);
                    i = i6;
                } else if (i == 0) {
                    return _fieldComplete("");
                }
                String findName = this._symbols.findName(iArr, i);
                if (findName == null) {
                    findName = _addName(iArr, i, r11);
                }
                return _fieldComplete(findName);
            }
            if (!(i5 == 34 || iArr2[i5] == 0)) {
                if (i5 != 92) {
                    _throwUnquotedSpace(i5, "name");
                } else {
                    i5 = _decodeCharEscape();
                    if (i5 < 0) {
                        this._minorState = 8;
                        this._minorStateAfterSplit = 9;
                        this._quadLength = i;
                        this._pending32 = r10;
                        this._pendingBytes = r11;
                        JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
                        this._currToken = jsonToken;
                        return jsonToken;
                    }
                }
                if (i5 > 127) {
                    if (r11 >= 4) {
                        if (i >= iArr.length) {
                            iArr = growArrayBy(iArr, iArr.length);
                            this._quadBuffer = iArr;
                        }
                        int i7 = i + 1;
                        iArr[i] = r10;
                        i = i7;
                        r10 = 0;
                        r11 = 0;
                    }
                    if (i5 < 2048) {
                        r10 = (r10 << 8) | (i5 >> 6) | 192;
                        r11++;
                    } else {
                        int i8 = (r10 << 8) | (i5 >> 12) | DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession;
                        int i9 = r11 + 1;
                        if (i9 >= 4) {
                            if (i >= iArr.length) {
                                int[] growArrayBy = growArrayBy(iArr, iArr.length);
                                this._quadBuffer = growArrayBy;
                                iArr = growArrayBy;
                            }
                            int i10 = i + 1;
                            iArr[i] = i8;
                            i = i10;
                            i8 = 0;
                            i9 = 0;
                        }
                        r10 = (i8 << 8) | ((i5 >> 6) & 63) | 128;
                        r11 = i9 + 1;
                    }
                    i5 = (i5 & 63) | 128;
                }
            }
            if (r11 < 4) {
                i3 = r11 + 1;
                i2 = (r10 << 8) | i5;
            } else {
                if (i >= iArr.length) {
                    iArr = growArrayBy(iArr, iArr.length);
                    this._quadBuffer = iArr;
                }
                int i11 = i + 1;
                iArr[i] = r10;
                i = i11;
                i2 = i5;
                i3 = 1;
            }
        }
        this._quadLength = i;
        this._pending32 = r10;
        this._pendingBytes = r11;
        this._minorState = 9;
        JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken2;
        return jsonToken2;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _finishFieldWithEscape() throws IOException {
        int _decodeSplitEscaped = _decodeSplitEscaped(this._quoted32, this._quotedDigits);
        if (_decodeSplitEscaped < 0) {
            this._minorState = 8;
            return JsonToken.NOT_AVAILABLE;
        }
        if (this._quadLength >= this._quadBuffer.length) {
            this._quadBuffer = growArrayBy(this._quadBuffer, 32);
        }
        int i = this._pending32;
        int i2 = this._pendingBytes;
        int i3 = 1;
        if (_decodeSplitEscaped > 127) {
            if (i2 >= 4) {
                int[] iArr = this._quadBuffer;
                int i4 = this._quadLength;
                this._quadLength = i4 + 1;
                iArr[i4] = i;
                i = 0;
                i2 = 0;
            }
            if (_decodeSplitEscaped < 2048) {
                i = (i << 8) | (_decodeSplitEscaped >> 6) | 192;
                i2++;
            } else {
                int i5 = (i << 8) | (_decodeSplitEscaped >> 12) | DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession;
                int i6 = i2 + 1;
                if (i6 >= 4) {
                    int[] iArr2 = this._quadBuffer;
                    int i7 = this._quadLength;
                    this._quadLength = i7 + 1;
                    iArr2[i7] = i5;
                    i5 = 0;
                    i6 = 0;
                }
                i = (i5 << 8) | ((_decodeSplitEscaped >> 6) & 63) | 128;
                i2 = i6 + 1;
            }
            _decodeSplitEscaped = (_decodeSplitEscaped & 63) | 128;
        }
        if (i2 < 4) {
            i3 = 1 + i2;
            _decodeSplitEscaped |= i << 8;
        } else {
            int[] iArr3 = this._quadBuffer;
            int i8 = this._quadLength;
            this._quadLength = i8 + 1;
            iArr3[i8] = i;
        }
        if (this._minorStateAfterSplit == 9) {
            return _finishAposName(this._quadLength, _decodeSplitEscaped, i3);
        }
        return _parseEscapedName(this._quadLength, _decodeSplitEscaped, i3);
    }

    private int _decodeSplitEscaped(int i, int i2) throws IOException {
        if (this._inputPtr >= this._inputEnd) {
            this._quoted32 = i;
            this._quotedDigits = i2;
            return -1;
        }
        byte[] bArr = this._inputBuffer;
        int i3 = this._inputPtr;
        this._inputPtr = i3 + 1;
        byte b = bArr[i3];
        if (i2 == -1) {
            if (b == 34 || b == 47 || b == 92) {
                return b;
            }
            if (b == 98) {
                return 8;
            }
            if (b == 102) {
                return 12;
            }
            if (b == 110) {
                return 10;
            }
            if (b == 114) {
                return 13;
            }
            switch (b) {
                case 116:
                    return 9;
                case 117:
                    if (this._inputPtr < this._inputEnd) {
                        byte[] bArr2 = this._inputBuffer;
                        int i4 = this._inputPtr;
                        this._inputPtr = i4 + 1;
                        b = bArr2[i4];
                        i2 = 0;
                        break;
                    } else {
                        this._quotedDigits = 0;
                        this._quoted32 = 0;
                        return -1;
                    }
                default:
                    return _handleUnrecognizedCharacterEscape((char) b);
            }
        }
        while (true) {
            byte b2 = b & UnsignedBytes.MAX_VALUE;
            int charToHex = CharTypes.charToHex(b2);
            if (charToHex < 0) {
                _reportUnexpectedChar(b2, "expected a hex-digit for character escape sequence");
            }
            i = (i << 4) | charToHex;
            i2++;
            if (i2 == 4) {
                return i;
            }
            if (this._inputPtr >= this._inputEnd) {
                this._quotedDigits = i2;
                this._quoted32 = i;
                return -1;
            }
            byte[] bArr3 = this._inputBuffer;
            int i5 = this._inputPtr;
            this._inputPtr = i5 + 1;
            b = bArr3[i5];
        }
    }

    /* access modifiers changed from: protected */
    public JsonToken _startString() throws IOException {
        int i = this._inputPtr;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = _icUTF8;
        int min = Math.min(this._inputEnd, emptyAndGetCurrentSegment.length + i);
        byte[] bArr = this._inputBuffer;
        int i2 = 0;
        while (true) {
            if (i >= min) {
                break;
            }
            byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
            if (iArr[b] == 0) {
                i++;
                int i3 = i2 + 1;
                emptyAndGetCurrentSegment[i2] = (char) b;
                i2 = i3;
            } else if (b == 34) {
                this._inputPtr = i + 1;
                this._textBuffer.setCurrentLength(i2);
                return _valueComplete(JsonToken.VALUE_STRING);
            }
        }
        this._textBuffer.setCurrentLength(i2);
        this._inputPtr = i;
        return _finishRegularString();
    }

    private final JsonToken _finishRegularString() throws IOException {
        int i;
        int[] iArr = _icUTF8;
        byte[] bArr = this._inputBuffer;
        char[] bufferWithoutReset = this._textBuffer.getBufferWithoutReset();
        int currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
        int i2 = this._inputPtr;
        int i3 = this._inputEnd - 5;
        while (i2 < this._inputEnd) {
            boolean z = false;
            if (currentSegmentSize >= bufferWithoutReset.length) {
                bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                currentSegmentSize = 0;
            }
            int min = Math.min(this._inputEnd, (bufferWithoutReset.length - currentSegmentSize) + i2);
            while (true) {
                if (i2 < min) {
                    int i4 = i2 + 1;
                    int i5 = bArr[i2] & UnsignedBytes.MAX_VALUE;
                    if (iArr[i5] == 0) {
                        int i6 = currentSegmentSize + 1;
                        bufferWithoutReset[currentSegmentSize] = (char) i5;
                        i2 = i4;
                        currentSegmentSize = i6;
                    } else if (i5 == 34) {
                        this._inputPtr = i4;
                        this._textBuffer.setCurrentLength(currentSegmentSize);
                        return _valueComplete(JsonToken.VALUE_STRING);
                    } else if (i4 >= i3) {
                        this._inputPtr = i4;
                        this._textBuffer.setCurrentLength(currentSegmentSize);
                        int i7 = iArr[i5];
                        if (i4 < this._inputEnd) {
                            z = true;
                        }
                        if (!_decodeSplitMultiByte(i5, i7, z)) {
                            this._minorStateAfterSplit = 40;
                            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
                            this._currToken = jsonToken;
                            return jsonToken;
                        }
                        bufferWithoutReset = this._textBuffer.getBufferWithoutReset();
                        currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
                        i2 = this._inputPtr;
                    } else {
                        switch (iArr[i5]) {
                            case 1:
                                this._inputPtr = i4;
                                i5 = _decodeFastCharEscape();
                                i = this._inputPtr;
                                break;
                            case 2:
                                int i8 = i4 + 1;
                                i5 = _decodeUTF8_2(i5, this._inputBuffer[i4]);
                                i = i8;
                                break;
                            case 3:
                                byte[] bArr2 = this._inputBuffer;
                                int i9 = i4 + 1;
                                int i10 = i9 + 1;
                                i5 = _decodeUTF8_3(i5, bArr2[i4], bArr2[i9]);
                                i = i10;
                                break;
                            case 4:
                                byte[] bArr3 = this._inputBuffer;
                                int i11 = i4 + 1;
                                int i12 = i11 + 1;
                                int i13 = i12 + 1;
                                int _decodeUTF8_4 = _decodeUTF8_4(i5, bArr3[i4], bArr3[i11], bArr3[i12]);
                                int i14 = currentSegmentSize + 1;
                                bufferWithoutReset[currentSegmentSize] = (char) (55296 | (_decodeUTF8_4 >> 10));
                                if (i14 >= bufferWithoutReset.length) {
                                    bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                                    i14 = 0;
                                }
                                i5 = (_decodeUTF8_4 & ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW) | GeneratorBase.SURR2_FIRST;
                                currentSegmentSize = i14;
                                i = i13;
                                break;
                            default:
                                if (i5 < 32) {
                                    _throwUnquotedSpace(i5, "string value");
                                } else {
                                    _reportInvalidChar(i5);
                                }
                                i = i4;
                                break;
                        }
                        if (currentSegmentSize >= bufferWithoutReset.length) {
                            bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                            currentSegmentSize = 0;
                        }
                        int i15 = currentSegmentSize + 1;
                        bufferWithoutReset[currentSegmentSize] = (char) i5;
                        i2 = i;
                        currentSegmentSize = i15;
                    }
                }
            }
        }
        this._inputPtr = i2;
        this._minorState = 40;
        this._textBuffer.setCurrentLength(currentSegmentSize);
        JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken2;
        return jsonToken2;
    }

    /* access modifiers changed from: protected */
    public JsonToken _startAposString() throws IOException {
        int i = this._inputPtr;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = _icUTF8;
        int min = Math.min(this._inputEnd, emptyAndGetCurrentSegment.length + i);
        byte[] bArr = this._inputBuffer;
        int i2 = 0;
        while (i < min) {
            byte b = bArr[i] & UnsignedBytes.MAX_VALUE;
            if (b == 39) {
                this._inputPtr = i + 1;
                this._textBuffer.setCurrentLength(i2);
                return _valueComplete(JsonToken.VALUE_STRING);
            } else if (iArr[b] != 0) {
                break;
            } else {
                i++;
                int i3 = i2 + 1;
                emptyAndGetCurrentSegment[i2] = (char) b;
                i2 = i3;
            }
        }
        this._textBuffer.setCurrentLength(i2);
        this._inputPtr = i;
        return _finishAposString();
    }

    private final JsonToken _finishAposString() throws IOException {
        int i;
        int[] iArr = _icUTF8;
        byte[] bArr = this._inputBuffer;
        char[] bufferWithoutReset = this._textBuffer.getBufferWithoutReset();
        int currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
        int i2 = this._inputPtr;
        int i3 = this._inputEnd - 5;
        while (i2 < this._inputEnd) {
            boolean z = false;
            if (currentSegmentSize >= bufferWithoutReset.length) {
                bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                currentSegmentSize = 0;
            }
            int min = Math.min(this._inputEnd, (bufferWithoutReset.length - currentSegmentSize) + i2);
            while (true) {
                if (i2 < min) {
                    int i4 = i2 + 1;
                    int i5 = bArr[i2] & UnsignedBytes.MAX_VALUE;
                    if (iArr[i5] == 0 || i5 == 34) {
                        if (i5 == 39) {
                            this._inputPtr = i4;
                            this._textBuffer.setCurrentLength(currentSegmentSize);
                            return _valueComplete(JsonToken.VALUE_STRING);
                        }
                        int i6 = currentSegmentSize + 1;
                        bufferWithoutReset[currentSegmentSize] = (char) i5;
                        i2 = i4;
                        currentSegmentSize = i6;
                    } else if (i4 >= i3) {
                        this._inputPtr = i4;
                        this._textBuffer.setCurrentLength(currentSegmentSize);
                        int i7 = iArr[i5];
                        if (i4 < this._inputEnd) {
                            z = true;
                        }
                        if (!_decodeSplitMultiByte(i5, i7, z)) {
                            this._minorStateAfterSplit = 45;
                            JsonToken jsonToken = JsonToken.NOT_AVAILABLE;
                            this._currToken = jsonToken;
                            return jsonToken;
                        }
                        bufferWithoutReset = this._textBuffer.getBufferWithoutReset();
                        currentSegmentSize = this._textBuffer.getCurrentSegmentSize();
                        i2 = this._inputPtr;
                    } else {
                        switch (iArr[i5]) {
                            case 1:
                                this._inputPtr = i4;
                                i5 = _decodeFastCharEscape();
                                i = this._inputPtr;
                                break;
                            case 2:
                                int i8 = i4 + 1;
                                i5 = _decodeUTF8_2(i5, this._inputBuffer[i4]);
                                i = i8;
                                break;
                            case 3:
                                byte[] bArr2 = this._inputBuffer;
                                int i9 = i4 + 1;
                                int i10 = i9 + 1;
                                i5 = _decodeUTF8_3(i5, bArr2[i4], bArr2[i9]);
                                i = i10;
                                break;
                            case 4:
                                byte[] bArr3 = this._inputBuffer;
                                int i11 = i4 + 1;
                                int i12 = i11 + 1;
                                int i13 = i12 + 1;
                                int _decodeUTF8_4 = _decodeUTF8_4(i5, bArr3[i4], bArr3[i11], bArr3[i12]);
                                int i14 = currentSegmentSize + 1;
                                bufferWithoutReset[currentSegmentSize] = (char) (55296 | (_decodeUTF8_4 >> 10));
                                if (i14 >= bufferWithoutReset.length) {
                                    bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                                    i14 = 0;
                                }
                                i5 = (_decodeUTF8_4 & ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW) | GeneratorBase.SURR2_FIRST;
                                currentSegmentSize = i14;
                                i = i13;
                                break;
                            default:
                                if (i5 < 32) {
                                    _throwUnquotedSpace(i5, "string value");
                                } else {
                                    _reportInvalidChar(i5);
                                }
                                i = i4;
                                break;
                        }
                        if (currentSegmentSize >= bufferWithoutReset.length) {
                            bufferWithoutReset = this._textBuffer.finishCurrentSegment();
                            currentSegmentSize = 0;
                        }
                        int i15 = currentSegmentSize + 1;
                        bufferWithoutReset[currentSegmentSize] = (char) i5;
                        i2 = i;
                        currentSegmentSize = i15;
                    }
                }
            }
        }
        this._inputPtr = i2;
        this._minorState = 45;
        this._textBuffer.setCurrentLength(currentSegmentSize);
        JsonToken jsonToken2 = JsonToken.NOT_AVAILABLE;
        this._currToken = jsonToken2;
        return jsonToken2;
    }

    private final boolean _decodeSplitMultiByte(int i, int i2, boolean z) throws IOException {
        switch (i2) {
            case 1:
                int _decodeSplitEscaped = _decodeSplitEscaped(0, -1);
                if (_decodeSplitEscaped < 0) {
                    this._minorState = 41;
                    return false;
                }
                this._textBuffer.append((char) _decodeSplitEscaped);
                return true;
            case 2:
                if (z) {
                    byte[] bArr = this._inputBuffer;
                    int i3 = this._inputPtr;
                    this._inputPtr = i3 + 1;
                    this._textBuffer.append((char) _decodeUTF8_2(i, bArr[i3]));
                    return true;
                }
                this._minorState = 42;
                this._pending32 = i;
                return false;
            case 3:
                int i4 = i & 15;
                if (z) {
                    byte[] bArr2 = this._inputBuffer;
                    int i5 = this._inputPtr;
                    this._inputPtr = i5 + 1;
                    return _decodeSplitUTF8_3(i4, 1, bArr2[i5]);
                }
                this._minorState = 43;
                this._pending32 = i4;
                this._pendingBytes = 1;
                return false;
            case 4:
                int i6 = i & 7;
                if (z) {
                    byte[] bArr3 = this._inputBuffer;
                    int i7 = this._inputPtr;
                    this._inputPtr = i7 + 1;
                    return _decodeSplitUTF8_4(i6, 1, bArr3[i7]);
                }
                this._pending32 = i6;
                this._pendingBytes = 1;
                this._minorState = 44;
                return false;
            default:
                if (i < 32) {
                    _throwUnquotedSpace(i, "string value");
                } else {
                    _reportInvalidChar(i);
                }
                this._textBuffer.append((char) i);
                return true;
        }
    }

    /* JADX WARNING: type inference failed for: r5v7, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r6v5, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r6v5, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r5v7, types: [byte[]] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean _decodeSplitUTF8_3(int r4, int r5, int r6) throws java.io.IOException {
        /*
            r3 = this;
            r0 = 128(0x80, float:1.794E-43)
            r1 = 1
            if (r5 != r1) goto L_0x0030
            r5 = r6 & 192(0xc0, float:2.69E-43)
            if (r5 == r0) goto L_0x0010
            r5 = r6 & 255(0xff, float:3.57E-43)
            int r2 = r3._inputPtr
            r3._reportInvalidOther(r5, r2)
        L_0x0010:
            int r4 = r4 << 6
            r5 = r6 & 63
            r4 = r4 | r5
            int r5 = r3._inputPtr
            int r6 = r3._inputEnd
            if (r5 < r6) goto L_0x0026
            r5 = 43
            r3._minorState = r5
            r3._pending32 = r4
            r4 = 2
            r3._pendingBytes = r4
            r4 = 0
            return r4
        L_0x0026:
            byte[] r5 = r3._inputBuffer
            int r6 = r3._inputPtr
            int r2 = r6 + 1
            r3._inputPtr = r2
            byte r6 = r5[r6]
        L_0x0030:
            r5 = r6 & 192(0xc0, float:2.69E-43)
            if (r5 == r0) goto L_0x003b
            r5 = r6 & 255(0xff, float:3.57E-43)
            int r0 = r3._inputPtr
            r3._reportInvalidOther(r5, r0)
        L_0x003b:
            com.fasterxml.jackson.core.util.TextBuffer r5 = r3._textBuffer
            int r4 = r4 << 6
            r6 = r6 & 63
            r4 = r4 | r6
            char r4 = (char) r4
            r5.append(r4)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._decodeSplitUTF8_3(int, int, int):boolean");
    }

    /* JADX WARNING: type inference failed for: r8v12, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r9v8, types: [byte] */
    /* JADX WARNING: type inference failed for: r8v17, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r9v11, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r9v11, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r9v8, types: [byte] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r8v12, types: [byte[]] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=null, for r8v17, types: [byte[]] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean _decodeSplitUTF8_4(int r7, int r8, int r9) throws java.io.IOException {
        /*
            r6 = this;
            r0 = 0
            r1 = 44
            r2 = 128(0x80, float:1.794E-43)
            r3 = 2
            r4 = 1
            if (r8 != r4) goto L_0x0031
            r8 = r9 & 192(0xc0, float:2.69E-43)
            if (r8 == r2) goto L_0x0014
            r8 = r9 & 255(0xff, float:3.57E-43)
            int r5 = r6._inputPtr
            r6._reportInvalidOther(r8, r5)
        L_0x0014:
            int r7 = r7 << 6
            r8 = r9 & 63
            r7 = r7 | r8
            int r8 = r6._inputPtr
            int r9 = r6._inputEnd
            if (r8 < r9) goto L_0x0026
            r6._minorState = r1
            r6._pending32 = r7
            r6._pendingBytes = r3
            return r0
        L_0x0026:
            byte[] r8 = r6._inputBuffer
            int r9 = r6._inputPtr
            int r5 = r9 + 1
            r6._inputPtr = r5
            byte r9 = r8[r9]
            r8 = 2
        L_0x0031:
            if (r8 != r3) goto L_0x005b
            r8 = r9 & 192(0xc0, float:2.69E-43)
            if (r8 == r2) goto L_0x003e
            r8 = r9 & 255(0xff, float:3.57E-43)
            int r3 = r6._inputPtr
            r6._reportInvalidOther(r8, r3)
        L_0x003e:
            int r7 = r7 << 6
            r8 = r9 & 63
            r7 = r7 | r8
            int r8 = r6._inputPtr
            int r9 = r6._inputEnd
            if (r8 < r9) goto L_0x0051
            r6._minorState = r1
            r6._pending32 = r7
            r7 = 3
            r6._pendingBytes = r7
            return r0
        L_0x0051:
            byte[] r8 = r6._inputBuffer
            int r9 = r6._inputPtr
            int r0 = r9 + 1
            r6._inputPtr = r0
            byte r9 = r8[r9]
        L_0x005b:
            r8 = r9 & 192(0xc0, float:2.69E-43)
            if (r8 == r2) goto L_0x0066
            r8 = r9 & 255(0xff, float:3.57E-43)
            int r0 = r6._inputPtr
            r6._reportInvalidOther(r8, r0)
        L_0x0066:
            int r7 = r7 << 6
            r8 = r9 & 63
            r7 = r7 | r8
            r8 = 65536(0x10000, float:9.18355E-41)
            int r7 = r7 - r8
            com.fasterxml.jackson.core.util.TextBuffer r8 = r6._textBuffer
            r9 = 55296(0xd800, float:7.7486E-41)
            int r0 = r7 >> 10
            r9 = r9 | r0
            char r9 = (char) r9
            r8.append(r9)
            r8 = 56320(0xdc00, float:7.8921E-41)
            r7 = r7 & 1023(0x3ff, float:1.434E-42)
            r7 = r7 | r8
            com.fasterxml.jackson.core.util.TextBuffer r8 = r6._textBuffer
            char r7 = (char) r7
            r8.append(r7)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.async.NonBlockingJsonParser._decodeSplitUTF8_4(int, int, int):boolean");
    }

    private final int _decodeCharEscape() throws IOException {
        if (this._inputEnd - this._inputPtr < 5) {
            return _decodeSplitEscaped(0, -1);
        }
        return _decodeFastCharEscape();
    }

    private final int _decodeFastCharEscape() throws IOException {
        byte[] bArr = this._inputBuffer;
        int i = this._inputPtr;
        this._inputPtr = i + 1;
        byte b = bArr[i];
        if (b == 34 || b == 47 || b == 92) {
            return (char) b;
        }
        if (b == 98) {
            return 8;
        }
        if (b == 102) {
            return 12;
        }
        if (b == 110) {
            return 10;
        }
        if (b == 114) {
            return 13;
        }
        switch (b) {
            case 116:
                return 9;
            case 117:
                byte[] bArr2 = this._inputBuffer;
                int i2 = this._inputPtr;
                this._inputPtr = i2 + 1;
                byte b2 = bArr2[i2];
                int charToHex = CharTypes.charToHex(b2);
                if (charToHex >= 0) {
                    byte[] bArr3 = this._inputBuffer;
                    int i3 = this._inputPtr;
                    this._inputPtr = i3 + 1;
                    b2 = bArr3[i3];
                    int charToHex2 = CharTypes.charToHex(b2);
                    if (charToHex2 >= 0) {
                        int i4 = (charToHex << 4) | charToHex2;
                        byte[] bArr4 = this._inputBuffer;
                        int i5 = this._inputPtr;
                        this._inputPtr = i5 + 1;
                        byte b3 = bArr4[i5];
                        int charToHex3 = CharTypes.charToHex(b3);
                        if (charToHex3 >= 0) {
                            int i6 = (i4 << 4) | charToHex3;
                            byte[] bArr5 = this._inputBuffer;
                            int i7 = this._inputPtr;
                            this._inputPtr = i7 + 1;
                            byte b4 = bArr5[i7];
                            int charToHex4 = CharTypes.charToHex(b4);
                            if (charToHex4 >= 0) {
                                return (i6 << 4) | charToHex4;
                            }
                            b2 = b4;
                        } else {
                            b2 = b3;
                        }
                    }
                }
                _reportUnexpectedChar(b2 & UnsignedBytes.MAX_VALUE, "expected a hex-digit for character escape sequence");
                return -1;
            default:
                return _handleUnrecognizedCharacterEscape((char) b);
        }
    }

    private final int _decodeUTF8_2(int i, int i2) throws IOException {
        if ((i2 & 192) != 128) {
            _reportInvalidOther(i2 & 255, this._inputPtr);
        }
        return ((i & 31) << 6) | (i2 & 63);
    }

    private final int _decodeUTF8_3(int i, int i2, int i3) throws IOException {
        int i4 = i & 15;
        if ((i2 & 192) != 128) {
            _reportInvalidOther(i2 & 255, this._inputPtr);
        }
        int i5 = (i4 << 6) | (i2 & 63);
        if ((i3 & 192) != 128) {
            _reportInvalidOther(i3 & 255, this._inputPtr);
        }
        return (i5 << 6) | (i3 & 63);
    }

    private final int _decodeUTF8_4(int i, int i2, int i3, int i4) throws IOException {
        if ((i2 & 192) != 128) {
            _reportInvalidOther(i2 & 255, this._inputPtr);
        }
        int i5 = ((i & 7) << 6) | (i2 & 63);
        if ((i3 & 192) != 128) {
            _reportInvalidOther(i3 & 255, this._inputPtr);
        }
        int i6 = (i5 << 6) | (i3 & 63);
        if ((i4 & 192) != 128) {
            _reportInvalidOther(i4 & 255, this._inputPtr);
        }
        return ((i6 << 6) | (i4 & 63)) - 65536;
    }
}
