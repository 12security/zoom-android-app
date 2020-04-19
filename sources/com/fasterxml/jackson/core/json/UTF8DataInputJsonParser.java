package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.p006io.CharTypes;
import com.fasterxml.jackson.core.p006io.IOContext;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import org.apache.http.message.TokenParser;

public class UTF8DataInputJsonParser extends ParserBase {
    static final byte BYTE_LF = 10;
    protected static final int[] _icLatin1 = CharTypes.getInputCodeLatin1();
    private static final int[] _icUTF8 = CharTypes.getInputCodeUtf8();
    protected DataInput _inputData;
    protected int _nextByte = -1;
    protected ObjectCodec _objectCodec;
    private int _quad1;
    protected int[] _quadBuffer = new int[16];
    protected final ByteQuadsCanonicalizer _symbols;
    protected boolean _tokenIncomplete;

    private static final int pad(int i, int i2) {
        return i2 == 4 ? i : i | (-1 << (i2 << 3));
    }

    /* access modifiers changed from: protected */
    public void _closeInput() throws IOException {
    }

    public int releaseBuffered(OutputStream outputStream) throws IOException {
        return 0;
    }

    public UTF8DataInputJsonParser(IOContext iOContext, int i, DataInput dataInput, ObjectCodec objectCodec, ByteQuadsCanonicalizer byteQuadsCanonicalizer, int i2) {
        super(iOContext, i);
        this._objectCodec = objectCodec;
        this._symbols = byteQuadsCanonicalizer;
        this._inputData = dataInput;
        this._nextByte = i2;
    }

    public ObjectCodec getCodec() {
        return this._objectCodec;
    }

    public void setCodec(ObjectCodec objectCodec) {
        this._objectCodec = objectCodec;
    }

    public Object getInputSource() {
        return this._inputData;
    }

    /* access modifiers changed from: protected */
    public void _releaseBuffers() throws IOException {
        super._releaseBuffers();
        this._symbols.release();
    }

    public String getText() throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING) {
            return _getText2(this._currToken);
        }
        if (!this._tokenIncomplete) {
            return this._textBuffer.contentsAsString();
        }
        this._tokenIncomplete = false;
        return _finishAndReturnString();
    }

    public int getText(Writer writer) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                _finishString();
            }
            return this._textBuffer.contentsToWriter(writer);
        } else if (jsonToken == JsonToken.FIELD_NAME) {
            String currentName = this._parsingContext.getCurrentName();
            writer.write(currentName);
            return currentName.length();
        } else if (jsonToken == null) {
            return 0;
        } else {
            if (jsonToken.isNumeric()) {
                return this._textBuffer.contentsToWriter(writer);
            }
            char[] asCharArray = jsonToken.asCharArray();
            writer.write(asCharArray);
            return asCharArray.length;
        }
    }

    public String getValueAsString() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (!this._tokenIncomplete) {
                return this._textBuffer.contentsAsString();
            }
            this._tokenIncomplete = false;
            return _finishAndReturnString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(null);
        }
    }

    public String getValueAsString(String str) throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (!this._tokenIncomplete) {
                return this._textBuffer.contentsAsString();
            }
            this._tokenIncomplete = false;
            return _finishAndReturnString();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        } else {
            return super.getValueAsString(str);
        }
    }

    public int getValueAsInt() throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken != JsonToken.VALUE_NUMBER_INT && jsonToken != JsonToken.VALUE_NUMBER_FLOAT) {
            return super.getValueAsInt(0);
        }
        if ((this._numTypesValid & 1) == 0) {
            if (this._numTypesValid == 0) {
                return _parseIntValue();
            }
            if ((this._numTypesValid & 1) == 0) {
                convertNumberToInt();
            }
        }
        return this._numberInt;
    }

    public int getValueAsInt(int i) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken != JsonToken.VALUE_NUMBER_INT && jsonToken != JsonToken.VALUE_NUMBER_FLOAT) {
            return super.getValueAsInt(i);
        }
        if ((this._numTypesValid & 1) == 0) {
            if (this._numTypesValid == 0) {
                return _parseIntValue();
            }
            if ((this._numTypesValid & 1) == 0) {
                convertNumberToInt();
            }
        }
        return this._numberInt;
    }

    /* access modifiers changed from: protected */
    public final String _getText2(JsonToken jsonToken) {
        if (jsonToken == null) {
            return null;
        }
        switch (jsonToken.mo21951id()) {
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
                if (this._tokenIncomplete) {
                    this._tokenIncomplete = false;
                    _finishString();
                    break;
                }
                break;
            case 7:
            case 8:
                break;
            default:
                return this._currToken.asCharArray();
        }
        return this._textBuffer.getTextBuffer();
    }

    public int getTextLength() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            if (this._tokenIncomplete) {
                this._tokenIncomplete = false;
                _finishString();
            }
            return this._textBuffer.size();
        } else if (this._currToken == JsonToken.FIELD_NAME) {
            return this._parsingContext.getCurrentName().length();
        } else {
            if (this._currToken == null) {
                return 0;
            }
            if (this._currToken.isNumeric()) {
                return this._textBuffer.size();
            }
            return this._currToken.asCharArray().length;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        return r2._textBuffer.getTextOffset();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getTextOffset() throws java.io.IOException {
        /*
            r2 = this;
            com.fasterxml.jackson.core.JsonToken r0 = r2._currToken
            r1 = 0
            if (r0 == 0) goto L_0x0020
            com.fasterxml.jackson.core.JsonToken r0 = r2._currToken
            int r0 = r0.mo21951id()
            switch(r0) {
                case 5: goto L_0x001f;
                case 6: goto L_0x000f;
                case 7: goto L_0x0018;
                case 8: goto L_0x0018;
                default: goto L_0x000e;
            }
        L_0x000e:
            goto L_0x0020
        L_0x000f:
            boolean r0 = r2._tokenIncomplete
            if (r0 == 0) goto L_0x0018
            r2._tokenIncomplete = r1
            r2._finishString()
        L_0x0018:
            com.fasterxml.jackson.core.util.TextBuffer r0 = r2._textBuffer
            int r0 = r0.getTextOffset()
            return r0
        L_0x001f:
            return r1
        L_0x0020:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8DataInputJsonParser.getTextOffset():int");
    }

    public byte[] getBinaryValue(Base64Variant base64Variant) throws IOException {
        if (this._currToken != JsonToken.VALUE_STRING && (this._currToken != JsonToken.VALUE_EMBEDDED_OBJECT || this._binaryValue == null)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Current token (");
            sb.append(this._currToken);
            sb.append(") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
            _reportError(sb.toString());
        }
        if (this._tokenIncomplete) {
            try {
                this._binaryValue = _decodeBase64(base64Variant);
                this._tokenIncomplete = false;
            } catch (IllegalArgumentException e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to decode VALUE_STRING as base64 (");
                sb2.append(base64Variant);
                sb2.append("): ");
                sb2.append(e.getMessage());
                throw _constructError(sb2.toString());
            }
        } else if (this._binaryValue == null) {
            ByteArrayBuilder _getByteArrayBuilder = _getByteArrayBuilder();
            _decodeBase64(getText(), _getByteArrayBuilder, base64Variant);
            this._binaryValue = _getByteArrayBuilder.toByteArray();
        }
        return this._binaryValue;
    }

    public int readBinaryValue(Base64Variant base64Variant, OutputStream outputStream) throws IOException {
        if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
            byte[] binaryValue = getBinaryValue(base64Variant);
            outputStream.write(binaryValue);
            return binaryValue.length;
        }
        byte[] allocBase64Buffer = this._ioContext.allocBase64Buffer();
        try {
            int _readBinary = _readBinary(base64Variant, outputStream, allocBase64Buffer);
            return _readBinary;
        } finally {
            this._ioContext.releaseBase64Buffer(allocBase64Buffer);
        }
    }

    /* access modifiers changed from: protected */
    public int _readBinary(Base64Variant base64Variant, OutputStream outputStream, byte[] bArr) throws IOException {
        int length = bArr.length - 3;
        int i = 0;
        int i2 = 0;
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (readUnsignedByte > 32) {
                int decodeBase64Char = base64Variant.decodeBase64Char(readUnsignedByte);
                if (decodeBase64Char < 0) {
                    if (readUnsignedByte == 34) {
                        break;
                    }
                    decodeBase64Char = _decodeBase64Escape(base64Variant, readUnsignedByte, 0);
                    if (decodeBase64Char < 0) {
                        continue;
                    }
                }
                if (i2 > length) {
                    i += i2;
                    outputStream.write(bArr, 0, i2);
                    i2 = 0;
                }
                int readUnsignedByte2 = this._inputData.readUnsignedByte();
                int decodeBase64Char2 = base64Variant.decodeBase64Char(readUnsignedByte2);
                if (decodeBase64Char2 < 0) {
                    decodeBase64Char2 = _decodeBase64Escape(base64Variant, readUnsignedByte2, 1);
                }
                int i3 = (decodeBase64Char << 6) | decodeBase64Char2;
                int readUnsignedByte3 = this._inputData.readUnsignedByte();
                int decodeBase64Char3 = base64Variant.decodeBase64Char(readUnsignedByte3);
                if (decodeBase64Char3 < 0) {
                    if (decodeBase64Char3 != -2) {
                        if (readUnsignedByte3 == 34 && !base64Variant.usesPadding()) {
                            int i4 = i2 + 1;
                            bArr[i2] = (byte) (i3 >> 4);
                            i2 = i4;
                            break;
                        }
                        decodeBase64Char3 = _decodeBase64Escape(base64Variant, readUnsignedByte3, 2);
                    }
                    if (decodeBase64Char3 == -2) {
                        int readUnsignedByte4 = this._inputData.readUnsignedByte();
                        if (base64Variant.usesPaddingChar(readUnsignedByte4)) {
                            int i5 = i2 + 1;
                            bArr[i2] = (byte) (i3 >> 4);
                            i2 = i5;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("expected padding character '");
                            sb.append(base64Variant.getPaddingChar());
                            sb.append("'");
                            throw reportInvalidBase64Char(base64Variant, readUnsignedByte4, 3, sb.toString());
                        }
                    }
                }
                int i6 = (i3 << 6) | decodeBase64Char3;
                int readUnsignedByte5 = this._inputData.readUnsignedByte();
                int decodeBase64Char4 = base64Variant.decodeBase64Char(readUnsignedByte5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        if (readUnsignedByte5 == 34 && !base64Variant.usesPadding()) {
                            int i7 = i6 >> 2;
                            int i8 = i2 + 1;
                            bArr[i2] = (byte) (i7 >> 8);
                            i2 = i8 + 1;
                            bArr[i8] = (byte) i7;
                            break;
                        }
                        decodeBase64Char4 = _decodeBase64Escape(base64Variant, readUnsignedByte5, 3);
                    }
                    if (decodeBase64Char4 == -2) {
                        int i9 = i6 >> 2;
                        int i10 = i2 + 1;
                        bArr[i2] = (byte) (i9 >> 8);
                        i2 = i10 + 1;
                        bArr[i10] = (byte) i9;
                    }
                }
                int i11 = (i6 << 6) | decodeBase64Char4;
                int i12 = i2 + 1;
                bArr[i2] = (byte) (i11 >> 16);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i11 >> 8);
                int i14 = i13 + 1;
                bArr[i13] = (byte) i11;
                i2 = i14;
            }
        }
        this._tokenIncomplete = false;
        if (i2 <= 0) {
            return i;
        }
        int i15 = i + i2;
        outputStream.write(bArr, 0, i2);
        return i15;
    }

    public JsonToken nextToken() throws IOException {
        JsonToken jsonToken;
        if (this._closed) {
            return null;
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return _nextAfterName();
        }
        this._numTypesValid = 0;
        if (this._tokenIncomplete) {
            _skipString();
        }
        int _skipWSOrEnd = _skipWSOrEnd();
        if (_skipWSOrEnd < 0) {
            close();
            this._currToken = null;
            return null;
        }
        this._binaryValue = null;
        this._tokenInputRow = this._currInputRow;
        if (_skipWSOrEnd == 93 || _skipWSOrEnd == 125) {
            _closeScope(_skipWSOrEnd);
            return this._currToken;
        }
        if (this._parsingContext.expectComma()) {
            if (_skipWSOrEnd != 44) {
                StringBuilder sb = new StringBuilder();
                sb.append("was expecting comma to separate ");
                sb.append(this._parsingContext.typeDesc());
                sb.append(" entries");
                _reportUnexpectedChar(_skipWSOrEnd, sb.toString());
            }
            _skipWSOrEnd = _skipWS();
            if (Feature.ALLOW_TRAILING_COMMA.enabledIn(this._features) && (_skipWSOrEnd == 93 || _skipWSOrEnd == 125)) {
                _closeScope(_skipWSOrEnd);
                return this._currToken;
            }
        }
        if (!this._parsingContext.inObject()) {
            return _nextTokenNotInObject(_skipWSOrEnd);
        }
        this._parsingContext.setCurrentName(_parseName(_skipWSOrEnd));
        this._currToken = JsonToken.FIELD_NAME;
        int _skipColon = _skipColon();
        if (_skipColon == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
            return this._currToken;
        }
        if (_skipColon == 45) {
            jsonToken = _parseNegNumber();
        } else if (_skipColon == 91) {
            jsonToken = JsonToken.START_ARRAY;
        } else if (_skipColon == 102) {
            _matchToken("false", 1);
            jsonToken = JsonToken.VALUE_FALSE;
        } else if (_skipColon == 110) {
            _matchToken("null", 1);
            jsonToken = JsonToken.VALUE_NULL;
        } else if (_skipColon == 116) {
            _matchToken("true", 1);
            jsonToken = JsonToken.VALUE_TRUE;
        } else if (_skipColon != 123) {
            switch (_skipColon) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    jsonToken = _parsePosNumber(_skipColon);
                    break;
                default:
                    jsonToken = _handleUnexpectedValue(_skipColon);
                    break;
            }
        } else {
            jsonToken = JsonToken.START_OBJECT;
        }
        this._nextToken = jsonToken;
        return this._currToken;
    }

    private final JsonToken _nextTokenNotInObject(int i) throws IOException {
        if (i == 34) {
            this._tokenIncomplete = true;
            JsonToken jsonToken = JsonToken.VALUE_STRING;
            this._currToken = jsonToken;
            return jsonToken;
        } else if (i == 45) {
            JsonToken _parseNegNumber = _parseNegNumber();
            this._currToken = _parseNegNumber;
            return _parseNegNumber;
        } else if (i == 91) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            JsonToken jsonToken2 = JsonToken.START_ARRAY;
            this._currToken = jsonToken2;
            return jsonToken2;
        } else if (i == 102) {
            _matchToken("false", 1);
            JsonToken jsonToken3 = JsonToken.VALUE_FALSE;
            this._currToken = jsonToken3;
            return jsonToken3;
        } else if (i == 110) {
            _matchToken("null", 1);
            JsonToken jsonToken4 = JsonToken.VALUE_NULL;
            this._currToken = jsonToken4;
            return jsonToken4;
        } else if (i == 116) {
            _matchToken("true", 1);
            JsonToken jsonToken5 = JsonToken.VALUE_TRUE;
            this._currToken = jsonToken5;
            return jsonToken5;
        } else if (i != 123) {
            switch (i) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    JsonToken _parsePosNumber = _parsePosNumber(i);
                    this._currToken = _parsePosNumber;
                    return _parsePosNumber;
                default:
                    JsonToken _handleUnexpectedValue = _handleUnexpectedValue(i);
                    this._currToken = _handleUnexpectedValue;
                    return _handleUnexpectedValue;
            }
        } else {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            JsonToken jsonToken6 = JsonToken.START_OBJECT;
            this._currToken = jsonToken6;
            return jsonToken6;
        }
    }

    private final JsonToken _nextAfterName() {
        this._nameCopied = false;
        JsonToken jsonToken = this._nextToken;
        this._nextToken = null;
        if (jsonToken == JsonToken.START_ARRAY) {
            this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
        } else if (jsonToken == JsonToken.START_OBJECT) {
            this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
        }
        this._currToken = jsonToken;
        return jsonToken;
    }

    public void finishToken() throws IOException {
        if (this._tokenIncomplete) {
            this._tokenIncomplete = false;
            _finishString();
        }
    }

    public String nextFieldName() throws IOException {
        JsonToken jsonToken;
        this._numTypesValid = 0;
        if (this._currToken == JsonToken.FIELD_NAME) {
            _nextAfterName();
            return null;
        }
        if (this._tokenIncomplete) {
            _skipString();
        }
        int _skipWS = _skipWS();
        this._binaryValue = null;
        this._tokenInputRow = this._currInputRow;
        if (_skipWS == 93 || _skipWS == 125) {
            _closeScope(_skipWS);
            return null;
        }
        if (this._parsingContext.expectComma()) {
            if (_skipWS != 44) {
                StringBuilder sb = new StringBuilder();
                sb.append("was expecting comma to separate ");
                sb.append(this._parsingContext.typeDesc());
                sb.append(" entries");
                _reportUnexpectedChar(_skipWS, sb.toString());
            }
            _skipWS = _skipWS();
            if (Feature.ALLOW_TRAILING_COMMA.enabledIn(this._features) && (_skipWS == 93 || _skipWS == 125)) {
                _closeScope(_skipWS);
                return null;
            }
        }
        if (!this._parsingContext.inObject()) {
            _nextTokenNotInObject(_skipWS);
            return null;
        }
        String _parseName = _parseName(_skipWS);
        this._parsingContext.setCurrentName(_parseName);
        this._currToken = JsonToken.FIELD_NAME;
        int _skipColon = _skipColon();
        if (_skipColon == 34) {
            this._tokenIncomplete = true;
            this._nextToken = JsonToken.VALUE_STRING;
            return _parseName;
        }
        if (_skipColon == 45) {
            jsonToken = _parseNegNumber();
        } else if (_skipColon == 91) {
            jsonToken = JsonToken.START_ARRAY;
        } else if (_skipColon == 102) {
            _matchToken("false", 1);
            jsonToken = JsonToken.VALUE_FALSE;
        } else if (_skipColon == 110) {
            _matchToken("null", 1);
            jsonToken = JsonToken.VALUE_NULL;
        } else if (_skipColon == 116) {
            _matchToken("true", 1);
            jsonToken = JsonToken.VALUE_TRUE;
        } else if (_skipColon != 123) {
            switch (_skipColon) {
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                    jsonToken = _parsePosNumber(_skipColon);
                    break;
                default:
                    jsonToken = _handleUnexpectedValue(_skipColon);
                    break;
            }
        } else {
            jsonToken = JsonToken.START_OBJECT;
        }
        this._nextToken = jsonToken;
        return _parseName;
    }

    public String nextTextValue() throws IOException {
        String str = null;
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken != JsonToken.VALUE_STRING) {
                if (jsonToken == JsonToken.START_ARRAY) {
                    this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
                } else if (jsonToken == JsonToken.START_OBJECT) {
                    this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
                }
                return null;
            } else if (!this._tokenIncomplete) {
                return this._textBuffer.contentsAsString();
            } else {
                this._tokenIncomplete = false;
                return _finishAndReturnString();
            }
        } else {
            if (nextToken() == JsonToken.VALUE_STRING) {
                str = getText();
            }
            return str;
        }
    }

    public int nextIntValue(int i) throws IOException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
                return getIntValue();
            }
            if (jsonToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            } else if (jsonToken == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            return i;
        }
        if (nextToken() == JsonToken.VALUE_NUMBER_INT) {
            i = getIntValue();
        }
        return i;
    }

    public long nextLongValue(long j) throws IOException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
                return getLongValue();
            }
            if (jsonToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            } else if (jsonToken == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            return j;
        }
        if (nextToken() == JsonToken.VALUE_NUMBER_INT) {
            j = getLongValue();
        }
        return j;
    }

    public Boolean nextBooleanValue() throws IOException {
        if (this._currToken == JsonToken.FIELD_NAME) {
            this._nameCopied = false;
            JsonToken jsonToken = this._nextToken;
            this._nextToken = null;
            this._currToken = jsonToken;
            if (jsonToken == JsonToken.VALUE_TRUE) {
                return Boolean.TRUE;
            }
            if (jsonToken == JsonToken.VALUE_FALSE) {
                return Boolean.FALSE;
            }
            if (jsonToken == JsonToken.START_ARRAY) {
                this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
            } else if (jsonToken == JsonToken.START_OBJECT) {
                this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
            }
            return null;
        }
        JsonToken nextToken = nextToken();
        if (nextToken == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (nextToken == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public JsonToken _parsePosNumber(int i) throws IOException {
        int i2;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int i3 = 1;
        if (i == 48) {
            i2 = _handleLeadingZeroes();
            if (i2 > 57 || i2 < 48) {
                emptyAndGetCurrentSegment[0] = '0';
            } else {
                i3 = 0;
            }
        } else {
            emptyAndGetCurrentSegment[0] = (char) i;
            i2 = this._inputData.readUnsignedByte();
        }
        int i4 = i3;
        int i5 = i4;
        while (i2 <= 57 && i2 >= 48) {
            i5++;
            int i6 = i4 + 1;
            emptyAndGetCurrentSegment[i4] = (char) i2;
            i2 = this._inputData.readUnsignedByte();
            i4 = i6;
        }
        if (i2 == 46 || i2 == 101 || i2 == 69) {
            return _parseFloat(emptyAndGetCurrentSegment, i4, i2, false, i5);
        }
        this._textBuffer.setCurrentLength(i4);
        if (this._parsingContext.inRoot()) {
            _verifyRootSpace();
        } else {
            this._nextByte = i2;
        }
        return resetInt(false, i5);
    }

    /* access modifiers changed from: protected */
    public JsonToken _parseNegNumber() throws IOException {
        int i;
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        emptyAndGetCurrentSegment[0] = '-';
        int readUnsignedByte = this._inputData.readUnsignedByte();
        emptyAndGetCurrentSegment[1] = (char) readUnsignedByte;
        if (readUnsignedByte <= 48) {
            if (readUnsignedByte != 48) {
                return _handleInvalidNumberStart(readUnsignedByte, true);
            }
            i = _handleLeadingZeroes();
        } else if (readUnsignedByte > 57) {
            return _handleInvalidNumberStart(readUnsignedByte, true);
        } else {
            i = this._inputData.readUnsignedByte();
        }
        int i2 = 2;
        int i3 = 1;
        while (i <= 57 && i >= 48) {
            i3++;
            int i4 = i2 + 1;
            emptyAndGetCurrentSegment[i2] = (char) i;
            i = this._inputData.readUnsignedByte();
            i2 = i4;
        }
        if (i == 46 || i == 101 || i == 69) {
            return _parseFloat(emptyAndGetCurrentSegment, i2, i, true, i3);
        }
        this._textBuffer.setCurrentLength(i2);
        this._nextByte = i;
        if (this._parsingContext.inRoot()) {
            _verifyRootSpace();
        }
        return resetInt(true, i3);
    }

    private final int _handleLeadingZeroes() throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (readUnsignedByte < 48 || readUnsignedByte > 57) {
            return readUnsignedByte;
        }
        if (!isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        while (readUnsignedByte == 48) {
            readUnsignedByte = this._inputData.readUnsignedByte();
        }
        return readUnsignedByte;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0031  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.fasterxml.jackson.core.JsonToken _parseFloat(char[] r8, int r9, int r10, boolean r11, int r12) throws java.io.IOException {
        /*
            r7 = this;
            r0 = 57
            r1 = 48
            r2 = 0
            r3 = 46
            if (r10 != r3) goto L_0x003b
            int r3 = r9 + 1
            char r10 = (char) r10
            r8[r9] = r10
            r10 = r8
            r9 = r3
            r8 = 0
        L_0x0011:
            java.io.DataInput r3 = r7._inputData
            int r3 = r3.readUnsignedByte()
            if (r3 < r1) goto L_0x002f
            if (r3 <= r0) goto L_0x001c
            goto L_0x002f
        L_0x001c:
            int r8 = r8 + 1
            int r4 = r10.length
            if (r9 < r4) goto L_0x0028
            com.fasterxml.jackson.core.util.TextBuffer r9 = r7._textBuffer
            char[] r10 = r9.finishCurrentSegment()
            r9 = 0
        L_0x0028:
            int r4 = r9 + 1
            char r3 = (char) r3
            r10[r9] = r3
            r9 = r4
            goto L_0x0011
        L_0x002f:
            if (r8 != 0) goto L_0x0036
            java.lang.String r4 = "Decimal point not followed by a digit"
            r7.reportUnexpectedNumberChar(r3, r4)
        L_0x0036:
            r6 = r3
            r3 = r8
            r8 = r10
            r10 = r6
            goto L_0x003c
        L_0x003b:
            r3 = 0
        L_0x003c:
            r4 = 101(0x65, float:1.42E-43)
            if (r10 == r4) goto L_0x0047
            r4 = 69
            if (r10 != r4) goto L_0x0045
            goto L_0x0047
        L_0x0045:
            r8 = 0
            goto L_0x00a7
        L_0x0047:
            int r4 = r8.length
            if (r9 < r4) goto L_0x0051
            com.fasterxml.jackson.core.util.TextBuffer r8 = r7._textBuffer
            char[] r8 = r8.finishCurrentSegment()
            r9 = 0
        L_0x0051:
            int r4 = r9 + 1
            char r10 = (char) r10
            r8[r9] = r10
            java.io.DataInput r9 = r7._inputData
            int r9 = r9.readUnsignedByte()
            r10 = 45
            if (r9 == r10) goto L_0x0069
            r10 = 43
            if (r9 != r10) goto L_0x0065
            goto L_0x0069
        L_0x0065:
            r10 = r9
            r9 = r8
            r8 = 0
            goto L_0x0082
        L_0x0069:
            int r10 = r8.length
            if (r4 < r10) goto L_0x0073
            com.fasterxml.jackson.core.util.TextBuffer r8 = r7._textBuffer
            char[] r8 = r8.finishCurrentSegment()
            r4 = 0
        L_0x0073:
            int r10 = r4 + 1
            char r9 = (char) r9
            r8[r4] = r9
            java.io.DataInput r9 = r7._inputData
            int r9 = r9.readUnsignedByte()
            r4 = r10
            r10 = r9
            r9 = r8
            r8 = 0
        L_0x0082:
            if (r10 > r0) goto L_0x009f
            if (r10 < r1) goto L_0x009f
            int r8 = r8 + 1
            int r5 = r9.length
            if (r4 < r5) goto L_0x0092
            com.fasterxml.jackson.core.util.TextBuffer r9 = r7._textBuffer
            char[] r9 = r9.finishCurrentSegment()
            r4 = 0
        L_0x0092:
            int r5 = r4 + 1
            char r10 = (char) r10
            r9[r4] = r10
            java.io.DataInput r10 = r7._inputData
            int r10 = r10.readUnsignedByte()
            r4 = r5
            goto L_0x0082
        L_0x009f:
            if (r8 != 0) goto L_0x00a6
            java.lang.String r9 = "Exponent indicator not followed by a digit"
            r7.reportUnexpectedNumberChar(r10, r9)
        L_0x00a6:
            r9 = r4
        L_0x00a7:
            r7._nextByte = r10
            com.fasterxml.jackson.core.json.JsonReadContext r10 = r7._parsingContext
            boolean r10 = r10.inRoot()
            if (r10 == 0) goto L_0x00b4
            r7._verifyRootSpace()
        L_0x00b4:
            com.fasterxml.jackson.core.util.TextBuffer r10 = r7._textBuffer
            r10.setCurrentLength(r9)
            com.fasterxml.jackson.core.JsonToken r8 = r7.resetFloat(r11, r12, r3, r8)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8DataInputJsonParser._parseFloat(char[], int, int, boolean, int):com.fasterxml.jackson.core.JsonToken");
    }

    private final void _verifyRootSpace() throws IOException {
        int i = this._nextByte;
        if (i <= 32) {
            this._nextByte = -1;
            if (i == 13 || i == 10) {
                this._currInputRow++;
            }
            return;
        }
        _reportMissingRootWS(i);
    }

    /* access modifiers changed from: protected */
    public final String _parseName(int i) throws IOException {
        if (i != 34) {
            return _handleOddName(i);
        }
        int[] iArr = _icLatin1;
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (iArr[readUnsignedByte] == 0) {
            int readUnsignedByte2 = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte2] == 0) {
                int i2 = (readUnsignedByte << 8) | readUnsignedByte2;
                int readUnsignedByte3 = this._inputData.readUnsignedByte();
                if (iArr[readUnsignedByte3] == 0) {
                    int i3 = (i2 << 8) | readUnsignedByte3;
                    int readUnsignedByte4 = this._inputData.readUnsignedByte();
                    if (iArr[readUnsignedByte4] == 0) {
                        int i4 = (i3 << 8) | readUnsignedByte4;
                        int readUnsignedByte5 = this._inputData.readUnsignedByte();
                        if (iArr[readUnsignedByte5] == 0) {
                            this._quad1 = i4;
                            return _parseMediumName(readUnsignedByte5);
                        } else if (readUnsignedByte5 == 34) {
                            return findName(i4, 4);
                        } else {
                            return parseName(i4, readUnsignedByte5, 4);
                        }
                    } else if (readUnsignedByte4 == 34) {
                        return findName(i3, 3);
                    } else {
                        return parseName(i3, readUnsignedByte4, 3);
                    }
                } else if (readUnsignedByte3 == 34) {
                    return findName(i2, 2);
                } else {
                    return parseName(i2, readUnsignedByte3, 2);
                }
            } else if (readUnsignedByte2 == 34) {
                return findName(readUnsignedByte, 1);
            } else {
                return parseName(readUnsignedByte, readUnsignedByte2, 1);
            }
        } else if (readUnsignedByte == 34) {
            return "";
        } else {
            return parseName(0, readUnsignedByte, 0);
        }
    }

    private final String _parseMediumName(int i) throws IOException {
        int[] iArr = _icLatin1;
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (iArr[readUnsignedByte] == 0) {
            int i2 = (i << 8) | readUnsignedByte;
            int readUnsignedByte2 = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte2] == 0) {
                int i3 = (i2 << 8) | readUnsignedByte2;
                int readUnsignedByte3 = this._inputData.readUnsignedByte();
                if (iArr[readUnsignedByte3] == 0) {
                    int i4 = (i3 << 8) | readUnsignedByte3;
                    int readUnsignedByte4 = this._inputData.readUnsignedByte();
                    if (iArr[readUnsignedByte4] == 0) {
                        return _parseMediumName2(readUnsignedByte4, i4);
                    }
                    if (readUnsignedByte4 == 34) {
                        return findName(this._quad1, i4, 4);
                    }
                    return parseName(this._quad1, i4, readUnsignedByte4, 4);
                } else if (readUnsignedByte3 == 34) {
                    return findName(this._quad1, i3, 3);
                } else {
                    return parseName(this._quad1, i3, readUnsignedByte3, 3);
                }
            } else if (readUnsignedByte2 == 34) {
                return findName(this._quad1, i2, 2);
            } else {
                return parseName(this._quad1, i2, readUnsignedByte2, 2);
            }
        } else if (readUnsignedByte == 34) {
            return findName(this._quad1, i, 1);
        } else {
            return parseName(this._quad1, i, readUnsignedByte, 1);
        }
    }

    private final String _parseMediumName2(int i, int i2) throws IOException {
        int[] iArr = _icLatin1;
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (iArr[readUnsignedByte] == 0) {
            int i3 = (i << 8) | readUnsignedByte;
            int readUnsignedByte2 = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte2] == 0) {
                int i4 = (i3 << 8) | readUnsignedByte2;
                int readUnsignedByte3 = this._inputData.readUnsignedByte();
                if (iArr[readUnsignedByte3] == 0) {
                    int i5 = (i4 << 8) | readUnsignedByte3;
                    int readUnsignedByte4 = this._inputData.readUnsignedByte();
                    if (iArr[readUnsignedByte4] == 0) {
                        return _parseLongName(readUnsignedByte4, i2, i5);
                    }
                    if (readUnsignedByte4 == 34) {
                        return findName(this._quad1, i2, i5, 4);
                    }
                    return parseName(this._quad1, i2, i5, readUnsignedByte4, 4);
                } else if (readUnsignedByte3 == 34) {
                    return findName(this._quad1, i2, i4, 3);
                } else {
                    return parseName(this._quad1, i2, i4, readUnsignedByte3, 3);
                }
            } else if (readUnsignedByte2 == 34) {
                return findName(this._quad1, i2, i3, 2);
            } else {
                return parseName(this._quad1, i2, i3, readUnsignedByte2, 2);
            }
        } else if (readUnsignedByte == 34) {
            return findName(this._quad1, i2, i, 1);
        } else {
            return parseName(this._quad1, i2, i, readUnsignedByte, 1);
        }
    }

    private final String _parseLongName(int i, int i2, int i3) throws IOException {
        int[] iArr = this._quadBuffer;
        iArr[0] = this._quad1;
        iArr[1] = i2;
        iArr[2] = i3;
        int[] iArr2 = _icLatin1;
        int i4 = i;
        int i5 = 3;
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (iArr2[readUnsignedByte] == 0) {
                int i6 = (i4 << 8) | readUnsignedByte;
                int readUnsignedByte2 = this._inputData.readUnsignedByte();
                if (iArr2[readUnsignedByte2] == 0) {
                    int i7 = (i6 << 8) | readUnsignedByte2;
                    int readUnsignedByte3 = this._inputData.readUnsignedByte();
                    if (iArr2[readUnsignedByte3] == 0) {
                        int i8 = (i7 << 8) | readUnsignedByte3;
                        int readUnsignedByte4 = this._inputData.readUnsignedByte();
                        if (iArr2[readUnsignedByte4] == 0) {
                            int[] iArr3 = this._quadBuffer;
                            if (i5 >= iArr3.length) {
                                this._quadBuffer = _growArrayBy(iArr3, i5);
                            }
                            int i9 = i5 + 1;
                            this._quadBuffer[i5] = i8;
                            i5 = i9;
                            i4 = readUnsignedByte4;
                        } else if (readUnsignedByte4 == 34) {
                            return findName(this._quadBuffer, i5, i8, 4);
                        } else {
                            return parseEscapedName(this._quadBuffer, i5, i8, readUnsignedByte4, 4);
                        }
                    } else if (readUnsignedByte3 == 34) {
                        return findName(this._quadBuffer, i5, i7, 3);
                    } else {
                        return parseEscapedName(this._quadBuffer, i5, i7, readUnsignedByte3, 3);
                    }
                } else if (readUnsignedByte2 == 34) {
                    return findName(this._quadBuffer, i5, i6, 2);
                } else {
                    return parseEscapedName(this._quadBuffer, i5, i6, readUnsignedByte2, 2);
                }
            } else if (readUnsignedByte == 34) {
                return findName(this._quadBuffer, i5, i4, 1);
            } else {
                return parseEscapedName(this._quadBuffer, i5, i4, readUnsignedByte, 1);
            }
        }
    }

    private final String parseName(int i, int i2, int i3) throws IOException {
        return parseEscapedName(this._quadBuffer, 0, i, i2, i3);
    }

    private final String parseName(int i, int i2, int i3, int i4) throws IOException {
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        return parseEscapedName(iArr, 1, i2, i3, i4);
    }

    private final String parseName(int i, int i2, int i3, int i4, int i5) throws IOException {
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        iArr[1] = i2;
        return parseEscapedName(iArr, 2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public final String parseEscapedName(int[] iArr, int i, int i2, int i3, int i4) throws IOException {
        int[] iArr2 = _icLatin1;
        while (true) {
            if (iArr2[i3] != 0) {
                if (i3 == 34) {
                    break;
                }
                if (i3 != 92) {
                    _throwUnquotedSpace(i3, "name");
                } else {
                    i3 = _decodeEscaped();
                }
                if (i3 > 127) {
                    if (r10 >= 4) {
                        if (i >= iArr.length) {
                            iArr = _growArrayBy(iArr, iArr.length);
                            this._quadBuffer = iArr;
                        }
                        int i5 = i + 1;
                        iArr[i] = r8;
                        i = i5;
                        r8 = 0;
                        r10 = 0;
                    }
                    if (i3 < 2048) {
                        r8 = (r8 << 8) | (i3 >> 6) | 192;
                        r10++;
                    } else {
                        int i6 = (r8 << 8) | (i3 >> 12) | DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession;
                        int i7 = r10 + 1;
                        if (i7 >= 4) {
                            if (i >= iArr.length) {
                                iArr = _growArrayBy(iArr, iArr.length);
                                this._quadBuffer = iArr;
                            }
                            int i8 = i + 1;
                            iArr[i] = i6;
                            i = i8;
                            i6 = 0;
                            i7 = 0;
                        }
                        r8 = (i6 << 8) | ((i3 >> 6) & 63) | 128;
                        r10 = i7 + 1;
                    }
                    i3 = (i3 & 63) | 128;
                }
            }
            if (r10 < 4) {
                i4 = r10 + 1;
                i2 = (r8 << 8) | i3;
            } else {
                if (i >= iArr.length) {
                    iArr = _growArrayBy(iArr, iArr.length);
                    this._quadBuffer = iArr;
                }
                int i9 = i + 1;
                iArr[i] = r8;
                i2 = i3;
                i = i9;
                i4 = 1;
            }
            i3 = this._inputData.readUnsignedByte();
        }
        if (r10 > 0) {
            if (i >= iArr.length) {
                iArr = _growArrayBy(iArr, iArr.length);
                this._quadBuffer = iArr;
            }
            int i10 = i + 1;
            iArr[i] = pad(r8, r10);
            i = i10;
        }
        String findName = this._symbols.findName(iArr, i);
        if (findName == null) {
            return addName(iArr, i, r10);
        }
        return findName;
    }

    /* access modifiers changed from: protected */
    public String _handleOddName(int i) throws IOException {
        if (i == 39 && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return _parseAposName();
        }
        if (!isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            _reportUnexpectedChar((char) _decodeCharForError(i), "was expecting double-quote to start field name");
        }
        int[] inputCodeUtf8JsNames = CharTypes.getInputCodeUtf8JsNames();
        if (inputCodeUtf8JsNames[i] != 0) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int[] iArr = this._quadBuffer;
        int i2 = 0;
        int i3 = i;
        int i4 = 0;
        int i5 = 0;
        do {
            if (i2 < 4) {
                i2++;
                i5 = (i5 << 8) | i3;
            } else {
                if (i4 >= iArr.length) {
                    iArr = _growArrayBy(iArr, iArr.length);
                    this._quadBuffer = iArr;
                }
                int i6 = i4 + 1;
                iArr[i4] = i5;
                i4 = i6;
                i5 = i3;
                i2 = 1;
            }
            i3 = this._inputData.readUnsignedByte();
        } while (inputCodeUtf8JsNames[i3] == 0);
        this._nextByte = i3;
        if (i2 > 0) {
            if (i4 >= iArr.length) {
                int[] _growArrayBy = _growArrayBy(iArr, iArr.length);
                this._quadBuffer = _growArrayBy;
                iArr = _growArrayBy;
            }
            int i7 = i4 + 1;
            iArr[i4] = i5;
            i4 = i7;
        }
        String findName = this._symbols.findName(iArr, i4);
        if (findName == null) {
            findName = addName(iArr, i4, i2);
        }
        return findName;
    }

    /* access modifiers changed from: protected */
    public String _parseAposName() throws IOException {
        int i;
        int i2;
        int i3;
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (readUnsignedByte == 39) {
            return "";
        }
        int[] iArr = this._quadBuffer;
        int[] iArr2 = _icLatin1;
        int[] iArr3 = iArr;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (readUnsignedByte != 39) {
            if (!(readUnsignedByte == 34 || iArr2[readUnsignedByte] == 0)) {
                if (readUnsignedByte != 92) {
                    _throwUnquotedSpace(readUnsignedByte, "name");
                } else {
                    readUnsignedByte = _decodeEscaped();
                }
                if (readUnsignedByte > 127) {
                    if (i2 >= 4) {
                        if (i5 >= iArr3.length) {
                            iArr3 = _growArrayBy(iArr3, iArr3.length);
                            this._quadBuffer = iArr3;
                        }
                        int i7 = i5 + 1;
                        iArr3[i5] = i;
                        i5 = i7;
                        i2 = 0;
                        i = 0;
                    }
                    if (readUnsignedByte < 2048) {
                        i = (i << 8) | (readUnsignedByte >> 6) | 192;
                        i2++;
                    } else {
                        int i8 = (i << 8) | (readUnsignedByte >> 12) | DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession;
                        int i9 = i2 + 1;
                        if (i9 >= 4) {
                            if (i5 >= iArr3.length) {
                                int[] _growArrayBy = _growArrayBy(iArr3, iArr3.length);
                                this._quadBuffer = _growArrayBy;
                                iArr3 = _growArrayBy;
                            }
                            int i10 = i5 + 1;
                            iArr3[i5] = i8;
                            i5 = i10;
                            i9 = 0;
                            i8 = 0;
                        }
                        i = (i8 << 8) | ((readUnsignedByte >> 6) & 63) | 128;
                        i2 = i9 + 1;
                    }
                    readUnsignedByte = (readUnsignedByte & 63) | 128;
                }
            }
            if (i2 < 4) {
                i4 = i2 + 1;
                i6 = readUnsignedByte | (i << 8);
            } else {
                if (i5 >= iArr3.length) {
                    iArr3 = _growArrayBy(iArr3, iArr3.length);
                    this._quadBuffer = iArr3;
                }
                int i11 = i5 + 1;
                iArr3[i5] = i;
                i6 = readUnsignedByte;
                i5 = i11;
                i4 = 1;
            }
            readUnsignedByte = this._inputData.readUnsignedByte();
        }
        if (i2 > 0) {
            if (i5 >= iArr3.length) {
                int[] _growArrayBy2 = _growArrayBy(iArr3, iArr3.length);
                this._quadBuffer = _growArrayBy2;
                iArr3 = _growArrayBy2;
            }
            i3 = i5 + 1;
            iArr3[i5] = pad(i, i2);
        } else {
            i3 = i5;
        }
        String findName = this._symbols.findName(iArr3, i3);
        if (findName == null) {
            findName = addName(iArr3, i3, i2);
        }
        return findName;
    }

    private final String findName(int i, int i2) throws JsonParseException {
        int pad = pad(i, i2);
        String findName = this._symbols.findName(pad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = pad;
        return addName(iArr, 1, i2);
    }

    private final String findName(int i, int i2, int i3) throws JsonParseException {
        int pad = pad(i2, i3);
        String findName = this._symbols.findName(i, pad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        iArr[1] = pad;
        return addName(iArr, 2, i3);
    }

    private final String findName(int i, int i2, int i3, int i4) throws JsonParseException {
        int pad = pad(i3, i4);
        String findName = this._symbols.findName(i, i2, pad);
        if (findName != null) {
            return findName;
        }
        int[] iArr = this._quadBuffer;
        iArr[0] = i;
        iArr[1] = i2;
        iArr[2] = pad(pad, i4);
        return addName(iArr, 3, i4);
    }

    private final String findName(int[] iArr, int i, int i2, int i3) throws JsonParseException {
        if (i >= iArr.length) {
            iArr = _growArrayBy(iArr, iArr.length);
            this._quadBuffer = iArr;
        }
        int i4 = i + 1;
        iArr[i] = pad(i2, i3);
        String findName = this._symbols.findName(iArr, i4);
        return findName == null ? addName(iArr, i4, i3) : findName;
    }

    private final String addName(int[] iArr, int i, int i2) throws JsonParseException {
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
    public void _finishString() throws IOException {
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = _icUTF8;
        int length = emptyAndGetCurrentSegment.length;
        int i = 0;
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte] == 0) {
                int i2 = i + 1;
                emptyAndGetCurrentSegment[i] = (char) readUnsignedByte;
                if (i2 >= length) {
                    _finishString2(emptyAndGetCurrentSegment, i2, this._inputData.readUnsignedByte());
                    return;
                }
                i = i2;
            } else if (readUnsignedByte == 34) {
                this._textBuffer.setCurrentLength(i);
                return;
            } else {
                _finishString2(emptyAndGetCurrentSegment, i, readUnsignedByte);
                return;
            }
        }
    }

    private String _finishAndReturnString() throws IOException {
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = _icUTF8;
        int length = emptyAndGetCurrentSegment.length;
        int i = 0;
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte] == 0) {
                int i2 = i + 1;
                emptyAndGetCurrentSegment[i] = (char) readUnsignedByte;
                if (i2 >= length) {
                    _finishString2(emptyAndGetCurrentSegment, i2, this._inputData.readUnsignedByte());
                    return this._textBuffer.contentsAsString();
                }
                i = i2;
            } else if (readUnsignedByte == 34) {
                return this._textBuffer.setCurrentAndReturn(i);
            } else {
                _finishString2(emptyAndGetCurrentSegment, i, readUnsignedByte);
                return this._textBuffer.contentsAsString();
            }
        }
    }

    private final void _finishString2(char[] cArr, int i, int i2) throws IOException {
        int[] iArr = _icUTF8;
        int length = cArr.length;
        while (true) {
            if (iArr[i2] == 0) {
                if (r8 >= length) {
                    cArr = this._textBuffer.finishCurrentSegment();
                    length = cArr.length;
                    r8 = 0;
                }
                int i3 = r8 + 1;
                cArr[r8] = (char) i2;
                i2 = this._inputData.readUnsignedByte();
                i = i3;
            } else if (i2 == 34) {
                this._textBuffer.setCurrentLength(r8);
                return;
            } else {
                switch (iArr[i2]) {
                    case 1:
                        i2 = _decodeEscaped();
                        break;
                    case 2:
                        i2 = _decodeUtf8_2(i2);
                        break;
                    case 3:
                        i2 = _decodeUtf8_3(i2);
                        break;
                    case 4:
                        int _decodeUtf8_4 = _decodeUtf8_4(i2);
                        int i4 = r8 + 1;
                        cArr[r8] = (char) (55296 | (_decodeUtf8_4 >> 10));
                        if (i4 >= cArr.length) {
                            cArr = this._textBuffer.finishCurrentSegment();
                            length = cArr.length;
                            r8 = 0;
                        } else {
                            r8 = i4;
                        }
                        i2 = (_decodeUtf8_4 & ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW) | GeneratorBase.SURR2_FIRST;
                        break;
                    default:
                        if (i2 >= 32) {
                            _reportInvalidChar(i2);
                            break;
                        } else {
                            _throwUnquotedSpace(i2, "string value");
                            break;
                        }
                }
                if (r8 >= cArr.length) {
                    cArr = this._textBuffer.finishCurrentSegment();
                    length = cArr.length;
                    r8 = 0;
                }
                int i5 = r8 + 1;
                cArr[r8] = (char) i2;
                i2 = this._inputData.readUnsignedByte();
                i = i5;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void _skipString() throws IOException {
        this._tokenIncomplete = false;
        int[] iArr = _icUTF8;
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (iArr[readUnsignedByte] != 0) {
                if (readUnsignedByte != 34) {
                    switch (iArr[readUnsignedByte]) {
                        case 1:
                            _decodeEscaped();
                            break;
                        case 2:
                            _skipUtf8_2();
                            break;
                        case 3:
                            _skipUtf8_3();
                            break;
                        case 4:
                            _skipUtf8_4();
                            break;
                        default:
                            if (readUnsignedByte >= 32) {
                                _reportInvalidChar(readUnsignedByte);
                                break;
                            } else {
                                _throwUnquotedSpace(readUnsignedByte, "string value");
                                break;
                            }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        if (r2._parsingContext.inArray() == false) goto L_0x0086;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x008c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.fasterxml.jackson.core.JsonToken _handleUnexpectedValue(int r3) throws java.io.IOException {
        /*
            r2 = this;
            r0 = 39
            if (r3 == r0) goto L_0x0079
            r0 = 73
            r1 = 1
            if (r3 == r0) goto L_0x005d
            r0 = 78
            if (r3 == r0) goto L_0x0041
            r0 = 93
            if (r3 == r0) goto L_0x0025
            r0 = 125(0x7d, float:1.75E-43)
            if (r3 == r0) goto L_0x003b
            switch(r3) {
                case 43: goto L_0x0019;
                case 44: goto L_0x002e;
                default: goto L_0x0018;
            }
        L_0x0018:
            goto L_0x0086
        L_0x0019:
            java.io.DataInput r3 = r2._inputData
            int r3 = r3.readUnsignedByte()
            r0 = 0
            com.fasterxml.jackson.core.JsonToken r3 = r2._handleInvalidNumberStart(r3, r0)
            return r3
        L_0x0025:
            com.fasterxml.jackson.core.json.JsonReadContext r0 = r2._parsingContext
            boolean r0 = r0.inArray()
            if (r0 != 0) goto L_0x002e
            goto L_0x0086
        L_0x002e:
            com.fasterxml.jackson.core.JsonParser$Feature r0 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES
            boolean r0 = r2.isEnabled(r0)
            if (r0 == 0) goto L_0x003b
            r2._nextByte = r3
            com.fasterxml.jackson.core.JsonToken r3 = com.fasterxml.jackson.core.JsonToken.VALUE_NULL
            return r3
        L_0x003b:
            java.lang.String r0 = "expected a value"
            r2._reportUnexpectedChar(r3, r0)
            goto L_0x0079
        L_0x0041:
            java.lang.String r0 = "NaN"
            r2._matchToken(r0, r1)
            com.fasterxml.jackson.core.JsonParser$Feature r0 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS
            boolean r0 = r2.isEnabled(r0)
            if (r0 == 0) goto L_0x0057
            java.lang.String r3 = "NaN"
            r0 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            com.fasterxml.jackson.core.JsonToken r3 = r2.resetAsNaN(r3, r0)
            return r3
        L_0x0057:
            java.lang.String r0 = "Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow"
            r2._reportError(r0)
            goto L_0x0086
        L_0x005d:
            java.lang.String r0 = "Infinity"
            r2._matchToken(r0, r1)
            com.fasterxml.jackson.core.JsonParser$Feature r0 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS
            boolean r0 = r2.isEnabled(r0)
            if (r0 == 0) goto L_0x0073
            java.lang.String r3 = "Infinity"
            r0 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
            com.fasterxml.jackson.core.JsonToken r3 = r2.resetAsNaN(r3, r0)
            return r3
        L_0x0073:
            java.lang.String r0 = "Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow"
            r2._reportError(r0)
            goto L_0x0086
        L_0x0079:
            com.fasterxml.jackson.core.JsonParser$Feature r0 = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES
            boolean r0 = r2.isEnabled(r0)
            if (r0 == 0) goto L_0x0086
            com.fasterxml.jackson.core.JsonToken r3 = r2._handleApos()
            return r3
        L_0x0086:
            boolean r0 = java.lang.Character.isJavaIdentifierStart(r3)
            if (r0 == 0) goto L_0x00a3
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = ""
            r0.append(r1)
            char r1 = (char) r3
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "('true', 'false' or 'null')"
            r2._reportInvalidToken(r3, r0, r1)
        L_0x00a3:
            java.lang.String r0 = "expected a valid value (number, String, array, object, 'true', 'false' or 'null')"
            r2._reportUnexpectedChar(r3, r0)
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.core.json.UTF8DataInputJsonParser._handleUnexpectedValue(int):com.fasterxml.jackson.core.JsonToken");
    }

    /* access modifiers changed from: protected */
    public JsonToken _handleApos() throws IOException {
        char[] emptyAndGetCurrentSegment = this._textBuffer.emptyAndGetCurrentSegment();
        int[] iArr = _icUTF8;
        int i = 0;
        while (true) {
            int length = emptyAndGetCurrentSegment.length;
            if (i >= emptyAndGetCurrentSegment.length) {
                emptyAndGetCurrentSegment = this._textBuffer.finishCurrentSegment();
                length = emptyAndGetCurrentSegment.length;
                i = 0;
            }
            while (true) {
                int readUnsignedByte = this._inputData.readUnsignedByte();
                if (readUnsignedByte == 39) {
                    this._textBuffer.setCurrentLength(i);
                    return JsonToken.VALUE_STRING;
                } else if (iArr[readUnsignedByte] != 0) {
                    switch (iArr[readUnsignedByte]) {
                        case 1:
                            readUnsignedByte = _decodeEscaped();
                            break;
                        case 2:
                            readUnsignedByte = _decodeUtf8_2(readUnsignedByte);
                            break;
                        case 3:
                            readUnsignedByte = _decodeUtf8_3(readUnsignedByte);
                            break;
                        case 4:
                            int _decodeUtf8_4 = _decodeUtf8_4(readUnsignedByte);
                            int i2 = i + 1;
                            emptyAndGetCurrentSegment[i] = (char) (55296 | (_decodeUtf8_4 >> 10));
                            if (i2 >= emptyAndGetCurrentSegment.length) {
                                emptyAndGetCurrentSegment = this._textBuffer.finishCurrentSegment();
                                i2 = 0;
                            }
                            int i3 = i2;
                            readUnsignedByte = 56320 | (_decodeUtf8_4 & ZMConfRequestConstant.REQUEST_ANNOTATE_WRITE_STORAGE_BY_NEW);
                            i = i3;
                            break;
                        default:
                            if (readUnsignedByte < 32) {
                                _throwUnquotedSpace(readUnsignedByte, "string value");
                            }
                            _reportInvalidChar(readUnsignedByte);
                            break;
                    }
                    if (i >= emptyAndGetCurrentSegment.length) {
                        emptyAndGetCurrentSegment = this._textBuffer.finishCurrentSegment();
                        i = 0;
                    }
                    int i4 = i + 1;
                    emptyAndGetCurrentSegment[i] = (char) readUnsignedByte;
                    i = i4;
                } else {
                    int i5 = i + 1;
                    emptyAndGetCurrentSegment[i] = (char) readUnsignedByte;
                    if (i5 >= length) {
                        i = i5;
                    } else {
                        i = i5;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public JsonToken _handleInvalidNumberStart(int i, boolean z) throws IOException {
        String str;
        while (i == 73) {
            i = this._inputData.readUnsignedByte();
            if (i != 78) {
                if (i != 110) {
                    break;
                }
                str = z ? "-Infinity" : "+Infinity";
            } else {
                str = z ? "-INF" : "+INF";
            }
            _matchToken(str, 3);
            if (isEnabled(Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                return resetAsNaN(str, z ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Non-standard token '");
            sb.append(str);
            sb.append("': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
            _reportError(sb.toString());
        }
        reportUnexpectedNumberChar(i, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void _matchToken(String str, int i) throws IOException {
        int length = str.length();
        do {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (readUnsignedByte != str.charAt(i)) {
                _reportInvalidToken(readUnsignedByte, str.substring(0, i));
            }
            i++;
        } while (i < length);
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if (!(readUnsignedByte2 < 48 || readUnsignedByte2 == 93 || readUnsignedByte2 == 125)) {
            _checkMatchEnd(str, i, readUnsignedByte2);
        }
        this._nextByte = readUnsignedByte2;
    }

    private final void _checkMatchEnd(String str, int i, int i2) throws IOException {
        char _decodeCharForError = (char) _decodeCharForError(i2);
        if (Character.isJavaIdentifierPart(_decodeCharForError)) {
            _reportInvalidToken(_decodeCharForError, str.substring(0, i));
        }
    }

    private final int _skipWS() throws IOException {
        int i = this._nextByte;
        if (i < 0) {
            i = this._inputData.readUnsignedByte();
        } else {
            this._nextByte = -1;
        }
        while (i <= 32) {
            if (i == 13 || i == 10) {
                this._currInputRow++;
            }
            i = this._inputData.readUnsignedByte();
        }
        if (i == 47 || i == 35) {
            return _skipWSComment(i);
        }
        return i;
    }

    private final int _skipWSOrEnd() throws IOException {
        int i = this._nextByte;
        if (i < 0) {
            try {
                i = this._inputData.readUnsignedByte();
            } catch (EOFException unused) {
                return _eofAsNextChar();
            }
        } else {
            this._nextByte = -1;
        }
        while (i <= 32) {
            if (i == 13 || i == 10) {
                this._currInputRow++;
            }
            try {
                i = this._inputData.readUnsignedByte();
            } catch (EOFException unused2) {
                return _eofAsNextChar();
            }
        }
        if (i == 47 || i == 35) {
            return _skipWSComment(i);
        }
        return i;
    }

    private final int _skipWSComment(int i) throws IOException {
        while (true) {
            if (i > 32) {
                if (i == 47) {
                    _skipComment();
                } else if (i != 35 || !_skipYAMLComment()) {
                    return i;
                }
            } else if (i == 13 || i == 10) {
                this._currInputRow++;
            }
            i = this._inputData.readUnsignedByte();
        }
    }

    private final int _skipColon() throws IOException {
        int i = this._nextByte;
        if (i < 0) {
            i = this._inputData.readUnsignedByte();
        } else {
            this._nextByte = -1;
        }
        if (i == 58) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (readUnsignedByte <= 32) {
                if (readUnsignedByte == 32 || readUnsignedByte == 9) {
                    readUnsignedByte = this._inputData.readUnsignedByte();
                    if (readUnsignedByte > 32) {
                        if (readUnsignedByte == 47 || readUnsignedByte == 35) {
                            return _skipColon2(readUnsignedByte, true);
                        }
                        return readUnsignedByte;
                    }
                }
                return _skipColon2(readUnsignedByte, true);
            } else if (readUnsignedByte == 47 || readUnsignedByte == 35) {
                return _skipColon2(readUnsignedByte, true);
            } else {
                return readUnsignedByte;
            }
        } else {
            if (i == 32 || i == 9) {
                i = this._inputData.readUnsignedByte();
            }
            if (i != 58) {
                return _skipColon2(i, false);
            }
            int readUnsignedByte2 = this._inputData.readUnsignedByte();
            if (readUnsignedByte2 <= 32) {
                if (readUnsignedByte2 == 32 || readUnsignedByte2 == 9) {
                    readUnsignedByte2 = this._inputData.readUnsignedByte();
                    if (readUnsignedByte2 > 32) {
                        if (readUnsignedByte2 == 47 || readUnsignedByte2 == 35) {
                            return _skipColon2(readUnsignedByte2, true);
                        }
                        return readUnsignedByte2;
                    }
                }
                return _skipColon2(readUnsignedByte2, true);
            } else if (readUnsignedByte2 == 47 || readUnsignedByte2 == 35) {
                return _skipColon2(readUnsignedByte2, true);
            } else {
                return readUnsignedByte2;
            }
        }
    }

    private final int _skipColon2(int i, boolean z) throws IOException {
        while (true) {
            if (i > 32) {
                if (i == 47) {
                    _skipComment();
                } else if (i != 35 || !_skipYAMLComment()) {
                    if (z) {
                        return i;
                    }
                    if (i != 58) {
                        _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
                    }
                    z = true;
                }
            } else if (i == 13 || i == 10) {
                this._currInputRow++;
            }
            i = this._inputData.readUnsignedByte();
        }
    }

    private final void _skipComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_COMMENTS)) {
            _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (readUnsignedByte == 47) {
            _skipLine();
        } else if (readUnsignedByte == 42) {
            _skipCComment();
        } else {
            _reportUnexpectedChar(readUnsignedByte, "was expecting either '*' or '/' for a comment");
        }
    }

    private final void _skipCComment() throws IOException {
        int[] inputCodeComment = CharTypes.getInputCodeComment();
        int readUnsignedByte = this._inputData.readUnsignedByte();
        while (true) {
            int i = inputCodeComment[readUnsignedByte];
            if (i != 0) {
                if (i == 10 || i == 13) {
                    this._currInputRow++;
                } else if (i != 42) {
                    switch (i) {
                        case 2:
                            _skipUtf8_2();
                            break;
                        case 3:
                            _skipUtf8_3();
                            break;
                        case 4:
                            _skipUtf8_4();
                            break;
                        default:
                            _reportInvalidChar(readUnsignedByte);
                            break;
                    }
                } else {
                    readUnsignedByte = this._inputData.readUnsignedByte();
                    if (readUnsignedByte == 47) {
                        return;
                    }
                }
            }
            readUnsignedByte = this._inputData.readUnsignedByte();
        }
    }

    private final boolean _skipYAMLComment() throws IOException {
        if (!isEnabled(Feature.ALLOW_YAML_COMMENTS)) {
            return false;
        }
        _skipLine();
        return true;
    }

    private final void _skipLine() throws IOException {
        int[] inputCodeComment = CharTypes.getInputCodeComment();
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            int i = inputCodeComment[readUnsignedByte];
            if (i != 0) {
                if (i == 10 || i == 13) {
                    this._currInputRow++;
                } else if (i != 42) {
                    switch (i) {
                        case 2:
                            _skipUtf8_2();
                            break;
                        case 3:
                            _skipUtf8_3();
                            break;
                        case 4:
                            _skipUtf8_4();
                            break;
                        default:
                            if (i >= 0) {
                                break;
                            } else {
                                _reportInvalidChar(readUnsignedByte);
                                break;
                            }
                    }
                }
            }
        }
        this._currInputRow++;
    }

    /* access modifiers changed from: protected */
    public char _decodeEscaped() throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if (readUnsignedByte == 34 || readUnsignedByte == 47 || readUnsignedByte == 92) {
            return (char) readUnsignedByte;
        }
        if (readUnsignedByte == 98) {
            return 8;
        }
        if (readUnsignedByte == 102) {
            return 12;
        }
        if (readUnsignedByte == 110) {
            return 10;
        }
        if (readUnsignedByte == 114) {
            return TokenParser.f495CR;
        }
        switch (readUnsignedByte) {
            case 116:
                return 9;
            case 117:
                int i = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    int readUnsignedByte2 = this._inputData.readUnsignedByte();
                    int charToHex = CharTypes.charToHex(readUnsignedByte2);
                    if (charToHex < 0) {
                        _reportUnexpectedChar(readUnsignedByte2, "expected a hex-digit for character escape sequence");
                    }
                    i = (i << 4) | charToHex;
                }
                return (char) i;
            default:
                return _handleUnrecognizedCharacterEscape((char) _decodeCharForError(readUnsignedByte));
        }
    }

    /* access modifiers changed from: protected */
    public int _decodeCharForError(int i) throws IOException {
        char c;
        int i2 = i & 255;
        if (i2 <= 127) {
            return i2;
        }
        if ((i2 & DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession) == 192) {
            i2 &= 31;
            c = 1;
        } else if ((i2 & DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP) == 224) {
            i2 &= 15;
            c = 2;
        } else if ((i2 & 248) == 240) {
            i2 &= 7;
            c = 3;
        } else {
            _reportInvalidInitial(i2 & 255);
            c = 1;
        }
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        int i3 = (i2 << 6) | (readUnsignedByte & 63);
        if (c <= 1) {
            return i3;
        }
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte2 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte2 & 255);
        }
        int i4 = (i3 << 6) | (readUnsignedByte2 & 63);
        if (c <= 2) {
            return i4;
        }
        int readUnsignedByte3 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte3 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte3 & 255);
        }
        return (i4 << 6) | (readUnsignedByte3 & 63);
    }

    private final int _decodeUtf8_2(int i) throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        return ((i & 31) << 6) | (readUnsignedByte & 63);
    }

    private final int _decodeUtf8_3(int i) throws IOException {
        int i2 = i & 15;
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        int i3 = (i2 << 6) | (readUnsignedByte & 63);
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte2 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte2 & 255);
        }
        return (i3 << 6) | (readUnsignedByte2 & 63);
    }

    private final int _decodeUtf8_4(int i) throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        int i2 = ((i & 7) << 6) | (readUnsignedByte & 63);
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte2 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte2 & 255);
        }
        int i3 = (i2 << 6) | (readUnsignedByte2 & 63);
        int readUnsignedByte3 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte3 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte3 & 255);
        }
        return ((i3 << 6) | (readUnsignedByte3 & 63)) - 65536;
    }

    private final void _skipUtf8_2() throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
    }

    private final void _skipUtf8_3() throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte2 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte2 & 255);
        }
    }

    private final void _skipUtf8_4() throws IOException {
        int readUnsignedByte = this._inputData.readUnsignedByte();
        if ((readUnsignedByte & 192) != 128) {
            _reportInvalidOther(readUnsignedByte & 255);
        }
        int readUnsignedByte2 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte2 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte2 & 255);
        }
        int readUnsignedByte3 = this._inputData.readUnsignedByte();
        if ((readUnsignedByte3 & 192) != 128) {
            _reportInvalidOther(readUnsignedByte3 & 255);
        }
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidToken(int i, String str) throws IOException {
        _reportInvalidToken(i, str, "'null', 'true', 'false' or NaN");
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidToken(int i, String str, String str2) throws IOException {
        StringBuilder sb = new StringBuilder(str);
        while (true) {
            char _decodeCharForError = (char) _decodeCharForError(i);
            if (!Character.isJavaIdentifierPart(_decodeCharForError)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unrecognized token '");
                sb2.append(sb.toString());
                sb2.append("': was expecting ");
                sb2.append(str2);
                _reportError(sb2.toString());
                return;
            }
            sb.append(_decodeCharForError);
            i = this._inputData.readUnsignedByte();
        }
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

    private void _reportInvalidOther(int i) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid UTF-8 middle byte 0x");
        sb.append(Integer.toHexString(i));
        _reportError(sb.toString());
    }

    private static int[] _growArrayBy(int[] iArr, int i) {
        if (iArr == null) {
            return new int[i];
        }
        return Arrays.copyOf(iArr, iArr.length + i);
    }

    /* access modifiers changed from: protected */
    public final byte[] _decodeBase64(Base64Variant base64Variant) throws IOException {
        ByteArrayBuilder _getByteArrayBuilder = _getByteArrayBuilder();
        while (true) {
            int readUnsignedByte = this._inputData.readUnsignedByte();
            if (readUnsignedByte > 32) {
                int decodeBase64Char = base64Variant.decodeBase64Char(readUnsignedByte);
                if (decodeBase64Char < 0) {
                    if (readUnsignedByte == 34) {
                        return _getByteArrayBuilder.toByteArray();
                    }
                    decodeBase64Char = _decodeBase64Escape(base64Variant, readUnsignedByte, 0);
                    if (decodeBase64Char < 0) {
                        continue;
                    }
                }
                int readUnsignedByte2 = this._inputData.readUnsignedByte();
                int decodeBase64Char2 = base64Variant.decodeBase64Char(readUnsignedByte2);
                if (decodeBase64Char2 < 0) {
                    decodeBase64Char2 = _decodeBase64Escape(base64Variant, readUnsignedByte2, 1);
                }
                int i = (decodeBase64Char << 6) | decodeBase64Char2;
                int readUnsignedByte3 = this._inputData.readUnsignedByte();
                int decodeBase64Char3 = base64Variant.decodeBase64Char(readUnsignedByte3);
                if (decodeBase64Char3 < 0) {
                    if (decodeBase64Char3 != -2) {
                        if (readUnsignedByte3 != 34 || base64Variant.usesPadding()) {
                            decodeBase64Char3 = _decodeBase64Escape(base64Variant, readUnsignedByte3, 2);
                        } else {
                            _getByteArrayBuilder.append(i >> 4);
                            return _getByteArrayBuilder.toByteArray();
                        }
                    }
                    if (decodeBase64Char3 == -2) {
                        int readUnsignedByte4 = this._inputData.readUnsignedByte();
                        if (base64Variant.usesPaddingChar(readUnsignedByte4)) {
                            _getByteArrayBuilder.append(i >> 4);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("expected padding character '");
                            sb.append(base64Variant.getPaddingChar());
                            sb.append("'");
                            throw reportInvalidBase64Char(base64Variant, readUnsignedByte4, 3, sb.toString());
                        }
                    }
                }
                int i2 = (i << 6) | decodeBase64Char3;
                int readUnsignedByte5 = this._inputData.readUnsignedByte();
                int decodeBase64Char4 = base64Variant.decodeBase64Char(readUnsignedByte5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        if (readUnsignedByte5 != 34 || base64Variant.usesPadding()) {
                            decodeBase64Char4 = _decodeBase64Escape(base64Variant, readUnsignedByte5, 3);
                        } else {
                            _getByteArrayBuilder.appendTwoBytes(i2 >> 2);
                            return _getByteArrayBuilder.toByteArray();
                        }
                    }
                    if (decodeBase64Char4 == -2) {
                        _getByteArrayBuilder.appendTwoBytes(i2 >> 2);
                    }
                }
                _getByteArrayBuilder.appendThreeBytes((i2 << 6) | decodeBase64Char4);
            }
        }
    }

    public JsonLocation getTokenLocation() {
        JsonLocation jsonLocation = new JsonLocation(_getSourceReference(), -1, -1, this._tokenInputRow, -1);
        return jsonLocation;
    }

    public JsonLocation getCurrentLocation() {
        JsonLocation jsonLocation = new JsonLocation(_getSourceReference(), -1, -1, this._currInputRow, -1);
        return jsonLocation;
    }

    private void _closeScope(int i) throws JsonParseException {
        if (i == 93) {
            if (!this._parsingContext.inArray()) {
                _reportMismatchedEndMarker(i, '}');
            }
            this._parsingContext = this._parsingContext.clearAndGetParent();
            this._currToken = JsonToken.END_ARRAY;
        }
        if (i == 125) {
            if (!this._parsingContext.inObject()) {
                _reportMismatchedEndMarker(i, ']');
            }
            this._parsingContext = this._parsingContext.clearAndGetParent();
            this._currToken = JsonToken.END_OBJECT;
        }
    }
}
