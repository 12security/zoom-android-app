package com.fasterxml.jackson.core.json.async;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.p006io.IOContext;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public abstract class NonBlockingJsonParserBase extends ParserBase {
    protected static final int MAJOR_ARRAY_ELEMENT_FIRST = 5;
    protected static final int MAJOR_ARRAY_ELEMENT_NEXT = 6;
    protected static final int MAJOR_CLOSED = 7;
    protected static final int MAJOR_INITIAL = 0;
    protected static final int MAJOR_OBJECT_FIELD_FIRST = 2;
    protected static final int MAJOR_OBJECT_FIELD_NEXT = 3;
    protected static final int MAJOR_OBJECT_VALUE = 4;
    protected static final int MAJOR_ROOT = 1;
    protected static final int MINOR_COMMENT_C = 53;
    protected static final int MINOR_COMMENT_CLOSING_ASTERISK = 52;
    protected static final int MINOR_COMMENT_CPP = 54;
    protected static final int MINOR_COMMENT_LEADING_SLASH = 51;
    protected static final int MINOR_COMMENT_YAML = 55;
    protected static final int MINOR_FIELD_APOS_NAME = 9;
    protected static final int MINOR_FIELD_LEADING_COMMA = 5;
    protected static final int MINOR_FIELD_LEADING_WS = 4;
    protected static final int MINOR_FIELD_NAME = 7;
    protected static final int MINOR_FIELD_NAME_ESCAPE = 8;
    protected static final int MINOR_FIELD_UNQUOTED_NAME = 10;
    protected static final int MINOR_NUMBER_EXPONENT_DIGITS = 32;
    protected static final int MINOR_NUMBER_EXPONENT_MARKER = 31;
    protected static final int MINOR_NUMBER_FRACTION_DIGITS = 30;
    protected static final int MINOR_NUMBER_INTEGER_DIGITS = 26;
    protected static final int MINOR_NUMBER_MINUS = 23;
    protected static final int MINOR_NUMBER_MINUSZERO = 25;
    protected static final int MINOR_NUMBER_ZERO = 24;
    protected static final int MINOR_ROOT_BOM = 1;
    protected static final int MINOR_ROOT_GOT_SEPARATOR = 3;
    protected static final int MINOR_ROOT_NEED_SEPARATOR = 2;
    protected static final int MINOR_VALUE_APOS_STRING = 45;
    protected static final int MINOR_VALUE_EXPECTING_COLON = 14;
    protected static final int MINOR_VALUE_EXPECTING_COMMA = 13;
    protected static final int MINOR_VALUE_LEADING_WS = 12;
    protected static final int MINOR_VALUE_STRING = 40;
    protected static final int MINOR_VALUE_STRING_ESCAPE = 41;
    protected static final int MINOR_VALUE_STRING_UTF8_2 = 42;
    protected static final int MINOR_VALUE_STRING_UTF8_3 = 43;
    protected static final int MINOR_VALUE_STRING_UTF8_4 = 44;
    protected static final int MINOR_VALUE_TOKEN_ERROR = 50;
    protected static final int MINOR_VALUE_TOKEN_FALSE = 18;
    protected static final int MINOR_VALUE_TOKEN_NON_STD = 19;
    protected static final int MINOR_VALUE_TOKEN_NULL = 16;
    protected static final int MINOR_VALUE_TOKEN_TRUE = 17;
    protected static final int MINOR_VALUE_WS_AFTER_COMMA = 15;
    protected static final String[] NON_STD_TOKENS = {"NaN", "Infinity", "+Infinity", "-Infinity"};
    protected static final int NON_STD_TOKEN_INFINITY = 1;
    protected static final int NON_STD_TOKEN_MINUS_INFINITY = 3;
    protected static final int NON_STD_TOKEN_NAN = 0;
    protected static final int NON_STD_TOKEN_PLUS_INFINITY = 2;
    protected static final double[] NON_STD_TOKEN_VALUES = {Double.NaN, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    protected int _currBufferStart = 0;
    protected int _currInputRowAlt = 1;
    protected boolean _endOfInput = false;
    protected int _majorState;
    protected int _majorStateAfterValue;
    protected int _minorState;
    protected int _minorStateAfterSplit;
    protected int _nonStdTokenType;
    protected int _pending32;
    protected int _pendingBytes;
    protected int _quad1;
    protected int[] _quadBuffer = new int[8];
    protected int _quadLength;
    protected int _quoted32;
    protected int _quotedDigits;
    protected final ByteQuadsCanonicalizer _symbols;

    protected static final int _padLastQuad(int i, int i2) {
        return i2 == 4 ? i : i | (-1 << (i2 << 3));
    }

    public boolean canParseAsync() {
        return true;
    }

    public ObjectCodec getCodec() {
        return null;
    }

    public Object getInputSource() {
        return null;
    }

    public abstract int releaseBuffered(OutputStream outputStream) throws IOException;

    public NonBlockingJsonParserBase(IOContext iOContext, int i, ByteQuadsCanonicalizer byteQuadsCanonicalizer) {
        super(iOContext, i);
        this._symbols = byteQuadsCanonicalizer;
        this._currToken = null;
        this._majorState = 0;
        this._majorStateAfterValue = 1;
    }

    public void setCodec(ObjectCodec objectCodec) {
        throw new UnsupportedOperationException("Can not use ObjectMapper with non-blocking parser");
    }

    /* access modifiers changed from: protected */
    public ByteQuadsCanonicalizer symbolTableForTests() {
        return this._symbols;
    }

    /* access modifiers changed from: protected */
    public void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        this._symbols.release();
    }

    /* access modifiers changed from: protected */
    public void _closeInput() throws IOException {
        this._currBufferStart = 0;
        this._inputEnd = 0;
    }

    public boolean hasTextCharacters() {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.hasTextAsCharacters();
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return this._nameCopied;
        }
        return false;
    }

    public JsonLocation getCurrentLocation() {
        int i = (this._inputPtr - this._currInputRowStart) + 1;
        JsonLocation jsonLocation = new JsonLocation(_getSourceReference(), this._currInputProcessed + ((long) (this._inputPtr - this._currBufferStart)), -1, Math.max(this._currInputRow, this._currInputRowAlt), i);
        return jsonLocation;
    }

    public JsonLocation getTokenLocation() {
        JsonLocation jsonLocation = new JsonLocation(_getSourceReference(), this._tokenInputTotal, -1, this._tokenInputRow, this._tokenInputCol);
        return jsonLocation;
    }

    public String getText() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        return _getText2(this._currToken);
    }

    /* access modifiers changed from: protected */
    public final String _getText2(JsonToken jsonToken) {
        if (jsonToken == null) {
            return null;
        }
        int id = jsonToken.mo21951id();
        if (id == -1) {
            return null;
        }
        switch (id) {
            case 5:
                return this._parsingContext.getCurrentName();
            case 6:
            case 7:
            case 8:
                return this._textBuffer.contentsAsString();
            default:
                return jsonToken.asString();
        }
    }

    public int getText(Writer writer) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsToWriter(writer);
        }
        if (jsonToken == JsonToken.FIELD_NAME) {
            String currentName = this._parsingContext.getCurrentName();
            writer.write(currentName);
            return currentName.length();
        } else if (jsonToken == null) {
            return 0;
        } else {
            if (jsonToken.isNumeric()) {
                return this._textBuffer.contentsToWriter(writer);
            }
            if (jsonToken == JsonToken.NOT_AVAILABLE) {
                _reportError("Current token not available: can not call this method");
            }
            char[] asCharArray = jsonToken.asCharArray();
            writer.write(asCharArray);
            return asCharArray.length;
        }
    }

    public String getValueAsString() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        }
        return super.getValueAsString(null);
    }

    public String getValueAsString(String str) throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return this._textBuffer.contentsAsString();
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        }
        return super.getValueAsString(str);
    }

    public char[] getTextCharacters() throws IOException {
        if (this._currToken == null) {
            return null;
        }
        switch (this._currToken.mo21951id()) {
            case 5:
                if (!this._nameCopied) {
                    String currentName = this._parsingContext.getCurrentName();
                    int length = currentName.length();
                    if (this._nameCopyBuffer == null) {
                        this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(length);
                    } else if (this._nameCopyBuffer.length < length) {
                        this._nameCopyBuffer = new char[length];
                    }
                    currentName.getChars(0, length, this._nameCopyBuffer, 0);
                    this._nameCopied = true;
                }
                return this._nameCopyBuffer;
            case 6:
            case 7:
            case 8:
                return this._textBuffer.getTextBuffer();
            default:
                return this._currToken.asCharArray();
        }
    }

    public int getTextLength() throws IOException {
        if (this._currToken == null) {
            return 0;
        }
        switch (this._currToken.mo21951id()) {
            case 5:
                return this._parsingContext.getCurrentName().length();
            case 6:
            case 7:
            case 8:
                return this._textBuffer.size();
            default:
                return this._currToken.asCharArray().length;
        }
    }

    public int getTextOffset() throws IOException {
        if (this._currToken != null) {
            switch (this._currToken.mo21951id()) {
                case 5:
                    return 0;
                case 6:
                case 7:
                case 8:
                    return this._textBuffer.getTextOffset();
            }
        }
        return 0;
    }

    public byte[] getBinaryValue(Base64Variant base64Variant) throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            _reportError("Current token (%s) not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary", this._currToken);
        }
        if (this._binaryValue == null) {
            ByteArrayBuilder _getByteArrayBuilder = _getByteArrayBuilder();
            _decodeBase64(getText(), _getByteArrayBuilder, base64Variant);
            this._binaryValue = _getByteArrayBuilder.toByteArray();
        }
        return this._binaryValue;
    }

    public int readBinaryValue(Base64Variant base64Variant, OutputStream outputStream) throws IOException {
        byte[] binaryValue = getBinaryValue(base64Variant);
        outputStream.write(binaryValue);
        return binaryValue.length;
    }

    public Object getEmbeddedObject() throws IOException {
        if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
            return this._binaryValue;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _startArrayScope() throws IOException {
        this._parsingContext = this._parsingContext.createChildArrayContext(-1, -1);
        this._majorState = 5;
        this._majorStateAfterValue = 6;
        JsonToken jsonToken = JsonToken.START_ARRAY;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _startObjectScope() throws IOException {
        this._parsingContext = this._parsingContext.createChildObjectContext(-1, -1);
        this._majorState = 2;
        this._majorStateAfterValue = 3;
        JsonToken jsonToken = JsonToken.START_OBJECT;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _closeArrayScope() throws IOException {
        if (!this._parsingContext.inArray()) {
            _reportMismatchedEndMarker(93, '}');
        }
        JsonReadContext parent = this._parsingContext.getParent();
        this._parsingContext = parent;
        int i = parent.inObject() ? 3 : parent.inArray() ? 6 : 1;
        this._majorState = i;
        this._majorStateAfterValue = i;
        JsonToken jsonToken = JsonToken.END_ARRAY;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _closeObjectScope() throws IOException {
        if (!this._parsingContext.inObject()) {
            _reportMismatchedEndMarker(125, ']');
        }
        JsonReadContext parent = this._parsingContext.getParent();
        this._parsingContext = parent;
        int i = parent.inObject() ? 3 : parent.inArray() ? 6 : 1;
        this._majorState = i;
        this._majorStateAfterValue = i;
        JsonToken jsonToken = JsonToken.END_OBJECT;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final String _findName(int i, int i2) throws JsonParseException {
        int _padLastQuad = _padLastQuad(i, i2);
        String findName = this._symbols.findName(_padLastQuad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = _padLastQuad;
        return _addName(iArr, 1, i2);
    }

    /* access modifiers changed from: protected */
    public final String _findName(int i, int i2, int i3) throws JsonParseException {
        int _padLastQuad = _padLastQuad(i2, i3);
        String findName = this._symbols.findName(i, _padLastQuad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        iArr[1] = _padLastQuad;
        return _addName(iArr, 2, i3);
    }

    /* access modifiers changed from: protected */
    public final String _findName(int i, int i2, int i3, int i4) throws JsonParseException {
        int _padLastQuad = _padLastQuad(i3, i4);
        String findName = this._symbols.findName(i, i2, _padLastQuad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        iArr[1] = i2;
        iArr[2] = _padLastQuad(_padLastQuad, i4);
        return _addName(iArr, 3, i4);
    }

    /* access modifiers changed from: protected */
    public final String _addName(int[] iArr, int i, int i2) throws JsonParseException {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int[] iArr2 = iArr;
        int i8 = i;
        int i9 = i2;
        int i10 = ((i8 << 2) - 4) + i9;
        if (i9 < 4) {
            int i11 = i8 - 1;
            i3 = iArr2[i11];
            iArr2[i11] = i3 << ((4 - i9) << 3);
        } else {
            i3 = 0;
        }
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int i12 = 0;
        int i13 = 0;
        while (i12 < i10) {
            int i14 = (iArr2[i12 >> 2] >> ((3 - (i12 & 3)) << 3)) & 255;
            i12++;
            if (i14 > 127) {
                if ((i14 & DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession) == 192) {
                    i4 = i14 & 31;
                    i5 = 1;
                } else if ((i14 & DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP) == 224) {
                    i4 = i14 & 15;
                    i5 = 2;
                } else if ((i14 & 248) == 240) {
                    i4 = i14 & 7;
                    i5 = 3;
                } else {
                    _reportInvalidInitial(i14);
                    i5 = 1;
                    i4 = 1;
                }
                if (i12 + i5 > i10) {
                    _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
                }
                int i15 = iArr2[i12 >> 2] >> ((3 - (i12 & 3)) << 3);
                i12++;
                if ((i15 & 192) != 128) {
                    _reportInvalidOther(i15);
                }
                int i16 = (i15 & 63) | (i4 << 6);
                if (i5 > 1) {
                    int i17 = iArr2[i12 >> 2] >> ((3 - (i12 & 3)) << 3);
                    i12++;
                    if ((i17 & 192) != 128) {
                        _reportInvalidOther(i17);
                    }
                    i7 = (i17 & 63) | (i16 << 6);
                    i6 = 2;
                    if (i5 > 2) {
                        int i18 = iArr2[i12 >> 2] >> ((3 - (i12 & 3)) << 3);
                        i12++;
                        if ((i18 & 192) != 128) {
                            _reportInvalidOther(i18 & 255);
                        }
                        i7 = (i7 << 6) | (i18 & 63);
                        i6 = 2;
                    }
                } else {
                    i7 = i16;
                    i6 = 2;
                }
                if (i5 > i6) {
                    int i19 = i7 - 65536;
                    if (i13 >= emptyAndGetCurrentSegment.length) {
                        emptyAndGetCurrentSegment = this._textBuffer.expandCurrentSegment();
                    }
                    int i20 = i13 + 1;
                    emptyAndGetCurrentSegment[i13] = (char) ((i19 >> 10) + GeneratorBase.SURR1_FIRST);
                    i14 = (i19 & ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW) | GeneratorBase.SURR2_FIRST;
                    i13 = i20;
                } else {
                    i14 = i7;
                }
            }
            if (i13 >= emptyAndGetCurrentSegment.length) {
                emptyAndGetCurrentSegment = this._textBuffer.expandCurrentSegment();
            }
            int i21 = i13 + 1;
            emptyAndGetCurrentSegment[i13] = (char) i14;
            i13 = i21;
        }
        String str = new String(emptyAndGetCurrentSegment, 0, i13);
        if (i9 < 4) {
            iArr2[i8 - 1] = i3;
        }
        return this._symbols.addName(str, iArr2, i8);
    }

    /* access modifiers changed from: protected */
    public final JsonToken _eofAsNextToken() throws IOException {
        this._majorState = 7;
        if (!this._parsingContext.inRoot()) {
            _handleEOF();
        }
        close();
        this._currToken = null;
        return null;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _fieldComplete(String str) throws IOException {
        this._majorState = 4;
        this._parsingContext.setCurrentName(str);
        JsonToken jsonToken = JsonToken.FIELD_NAME;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _valueComplete(JsonToken jsonToken) throws IOException {
        this._majorState = this._majorStateAfterValue;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _valueCompleteInt(int i, String str) throws IOException {
        this._textBuffer.resetWithString(str);
        this._intLength = str.length();
        this._numTypesValid = 1;
        this._numberInt = i;
        this._majorState = this._majorStateAfterValue;
        JsonToken jsonToken = JsonToken.VALUE_NUMBER_INT;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final JsonToken _valueNonStdNumberComplete(int i) throws IOException {
        String str = NON_STD_TOKENS[i];
        this._textBuffer.resetWithString(str);
        if (!isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
            _reportError("Non-standard token '%s': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow", str);
        }
        this._intLength = 0;
        this._numTypesValid = 8;
        this._numberDouble = NON_STD_TOKEN_VALUES[i];
        this._majorState = this._majorStateAfterValue;
        JsonToken jsonToken = JsonToken.VALUE_NUMBER_FLOAT;
        this._currToken = jsonToken;
        return jsonToken;
    }

    /* access modifiers changed from: protected */
    public final String _nonStdToken(int i) {
        return NON_STD_TOKENS[i];
    }

    /* access modifiers changed from: protected */
    public final void _updateTokenLocation() {
        this._tokenInputRow = Math.max(this._currInputRow, this._currInputRowAlt);
        int i = this._inputPtr;
        this._tokenInputCol = i - this._currInputRowStart;
        this._tokenInputTotal = this._currInputProcessed + ((long) (i - this._currBufferStart));
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidChar(int i) throws JsonParseException {
        if (i < 32) {
            _throwInvalidSpace(i);
        }
        _reportInvalidInitial(i);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidInitial(int i) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid UTF-8 start byte 0x");
        sb.append(Integer.toHexString(i));
        _reportError(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidOther(int i, int i2) throws JsonParseException {
        this._inputPtr = i2;
        _reportInvalidOther(i);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidOther(int i) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid UTF-8 middle byte 0x");
        sb.append(Integer.toHexString(i));
        _reportError(sb.toString());
    }
}
