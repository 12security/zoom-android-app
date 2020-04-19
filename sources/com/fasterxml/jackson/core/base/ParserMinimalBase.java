package com.fasterxml.jackson.core.base;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.p006io.JsonEOFException;
import com.fasterxml.jackson.core.p006io.NumberInput;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.core.util.VersionUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class ParserMinimalBase extends JsonParser {
    protected static final BigDecimal BD_MAX_INT = new BigDecimal(BI_MAX_INT);
    protected static final BigDecimal BD_MAX_LONG = new BigDecimal(BI_MAX_LONG);
    protected static final BigDecimal BD_MIN_INT = new BigDecimal(BI_MIN_INT);
    protected static final BigDecimal BD_MIN_LONG = new BigDecimal(BI_MIN_LONG);
    protected static final BigInteger BI_MAX_INT = BigInteger.valueOf(MAX_INT_L);
    protected static final BigInteger BI_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    protected static final BigInteger BI_MIN_INT = BigInteger.valueOf(MIN_INT_L);
    protected static final BigInteger BI_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    protected static final char CHAR_NULL = '\u0000';
    protected static final int INT_0 = 48;
    protected static final int INT_9 = 57;
    protected static final int INT_APOS = 39;
    protected static final int INT_ASTERISK = 42;
    protected static final int INT_BACKSLASH = 92;
    protected static final int INT_COLON = 58;
    protected static final int INT_COMMA = 44;
    protected static final int INT_CR = 13;
    protected static final int INT_E = 69;
    protected static final int INT_HASH = 35;
    protected static final int INT_LBRACKET = 91;
    protected static final int INT_LCURLY = 123;
    protected static final int INT_LF = 10;
    protected static final int INT_MINUS = 45;
    protected static final int INT_PERIOD = 46;
    protected static final int INT_PLUS = 43;
    protected static final int INT_QUOTE = 34;
    protected static final int INT_RBRACKET = 93;
    protected static final int INT_RCURLY = 125;
    protected static final int INT_SLASH = 47;
    protected static final int INT_SPACE = 32;
    protected static final int INT_TAB = 9;
    protected static final int INT_e = 101;
    protected static final int MAX_ERROR_TOKEN_LENGTH = 256;
    protected static final double MAX_INT_D = 2.147483647E9d;
    protected static final long MAX_INT_L = 2147483647L;
    protected static final double MAX_LONG_D = 9.223372036854776E18d;
    protected static final double MIN_INT_D = -2.147483648E9d;
    protected static final long MIN_INT_L = -2147483648L;
    protected static final double MIN_LONG_D = -9.223372036854776E18d;
    protected static final byte[] NO_BYTES = new byte[0];
    protected static final int[] NO_INTS = new int[0];
    protected static final int NR_BIGDECIMAL = 16;
    protected static final int NR_BIGINT = 4;
    protected static final int NR_DOUBLE = 8;
    protected static final int NR_FLOAT = 32;
    protected static final int NR_INT = 1;
    protected static final int NR_LONG = 2;
    protected static final int NR_UNKNOWN = 0;
    protected JsonToken _currToken;
    protected JsonToken _lastClearedToken;

    /* access modifiers changed from: protected */
    public abstract void _handleEOF() throws JsonParseException;

    public abstract void close() throws IOException;

    public abstract byte[] getBinaryValue(Base64Variant base64Variant) throws IOException;

    public abstract String getCurrentName() throws IOException;

    public abstract JsonStreamContext getParsingContext();

    public abstract String getText() throws IOException;

    public abstract char[] getTextCharacters() throws IOException;

    public abstract int getTextLength() throws IOException;

    public abstract int getTextOffset() throws IOException;

    public abstract boolean hasTextCharacters();

    public abstract boolean isClosed();

    public abstract JsonToken nextToken() throws IOException;

    public abstract void overrideCurrentName(String str);

    protected ParserMinimalBase() {
    }

    protected ParserMinimalBase(int i) {
        super(i);
    }

    public JsonToken currentToken() {
        return this._currToken;
    }

    public int currentTokenId() {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == null) {
            return 0;
        }
        return jsonToken.mo21951id();
    }

    public JsonToken getCurrentToken() {
        return this._currToken;
    }

    public int getCurrentTokenId() {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == null) {
            return 0;
        }
        return jsonToken.mo21951id();
    }

    public boolean hasCurrentToken() {
        return this._currToken != null;
    }

    public boolean hasTokenId(int i) {
        JsonToken jsonToken = this._currToken;
        boolean z = true;
        if (jsonToken == null) {
            if (i != 0) {
                z = false;
            }
            return z;
        }
        if (jsonToken.mo21951id() != i) {
            z = false;
        }
        return z;
    }

    public boolean hasToken(JsonToken jsonToken) {
        return this._currToken == jsonToken;
    }

    public boolean isExpectedStartArrayToken() {
        return this._currToken == JsonToken.START_ARRAY;
    }

    public boolean isExpectedStartObjectToken() {
        return this._currToken == JsonToken.START_OBJECT;
    }

    public JsonToken nextValue() throws IOException {
        JsonToken nextToken = nextToken();
        return nextToken == JsonToken.FIELD_NAME ? nextToken() : nextToken;
    }

    public JsonParser skipChildren() throws IOException {
        if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            return this;
        }
        int i = 1;
        while (true) {
            JsonToken nextToken = nextToken();
            if (nextToken == null) {
                _handleEOF();
                return this;
            } else if (nextToken.isStructStart()) {
                i++;
            } else if (nextToken.isStructEnd()) {
                i--;
                if (i == 0) {
                    return this;
                }
            } else if (nextToken == JsonToken.NOT_AVAILABLE) {
                _reportError("Not enough content available for `skipChildren()`: non-blocking parser? (%s)", getClass().getName());
            }
        }
    }

    public void clearCurrentToken() {
        JsonToken jsonToken = this._currToken;
        if (jsonToken != null) {
            this._lastClearedToken = jsonToken;
            this._currToken = null;
        }
    }

    public JsonToken getLastClearedToken() {
        return this._lastClearedToken;
    }

    public boolean getValueAsBoolean(boolean z) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken != null) {
            boolean z2 = true;
            switch (jsonToken.mo21951id()) {
                case 6:
                    String trim = getText().trim();
                    if ("true".equals(trim)) {
                        return true;
                    }
                    if (!"false".equals(trim) && !_hasTextualNull(trim)) {
                        return z;
                    }
                    return false;
                case 7:
                    if (getIntValue() == 0) {
                        z2 = false;
                    }
                    return z2;
                case 9:
                    return true;
                case 10:
                case 11:
                    return false;
                case 12:
                    Object embeddedObject = getEmbeddedObject();
                    if (embeddedObject instanceof Boolean) {
                        return ((Boolean) embeddedObject).booleanValue();
                    }
                    break;
            }
        }
        return z;
    }

    public int getValueAsInt() throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT || jsonToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return getIntValue();
        }
        return getValueAsInt(0);
    }

    public int getValueAsInt(int i) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT || jsonToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return getIntValue();
        }
        if (jsonToken != null) {
            int id = jsonToken.mo21951id();
            if (id != 6) {
                switch (id) {
                    case 9:
                        return 1;
                    case 10:
                        return 0;
                    case 11:
                        return 0;
                    case 12:
                        Object embeddedObject = getEmbeddedObject();
                        if (embeddedObject instanceof Number) {
                            return ((Number) embeddedObject).intValue();
                        }
                        break;
                }
            } else {
                String text = getText();
                if (_hasTextualNull(text)) {
                    return 0;
                }
                return NumberInput.parseAsInt(text, i);
            }
        }
        return i;
    }

    public long getValueAsLong() throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT || jsonToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return getLongValue();
        }
        return getValueAsLong(0);
    }

    public long getValueAsLong(long j) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == JsonToken.VALUE_NUMBER_INT || jsonToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return getLongValue();
        }
        if (jsonToken != null) {
            int id = jsonToken.mo21951id();
            if (id != 6) {
                switch (id) {
                    case 9:
                        return 1;
                    case 10:
                    case 11:
                        return 0;
                    case 12:
                        Object embeddedObject = getEmbeddedObject();
                        if (embeddedObject instanceof Number) {
                            return ((Number) embeddedObject).longValue();
                        }
                        break;
                }
            } else {
                String text = getText();
                if (_hasTextualNull(text)) {
                    return 0;
                }
                return NumberInput.parseAsLong(text, j);
            }
        }
        return j;
    }

    public double getValueAsDouble(double d) throws IOException {
        JsonToken jsonToken = this._currToken;
        if (jsonToken != null) {
            switch (jsonToken.mo21951id()) {
                case 6:
                    String text = getText();
                    if (_hasTextualNull(text)) {
                        return 0.0d;
                    }
                    return NumberInput.parseAsDouble(text, d);
                case 7:
                case 8:
                    return getDoubleValue();
                case 9:
                    return 1.0d;
                case 10:
                case 11:
                    return 0.0d;
                case 12:
                    Object embeddedObject = getEmbeddedObject();
                    if (embeddedObject instanceof Number) {
                        return ((Number) embeddedObject).doubleValue();
                    }
                    break;
            }
        }
        return d;
    }

    public String getValueAsString() throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return getText();
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        }
        return getValueAsString(null);
    }

    public String getValueAsString(String str) throws IOException {
        if (this._currToken == JsonToken.VALUE_STRING) {
            return getText();
        }
        if (this._currToken == JsonToken.FIELD_NAME) {
            return getCurrentName();
        }
        JsonToken jsonToken = this._currToken;
        return (jsonToken == null || jsonToken == JsonToken.VALUE_NULL || !this._currToken.isScalarValue()) ? str : getText();
    }

    /* access modifiers changed from: protected */
    public void _decodeBase64(String str, ByteArrayBuilder byteArrayBuilder, Base64Variant base64Variant) throws IOException {
        try {
            base64Variant.decode(str, byteArrayBuilder);
        } catch (IllegalArgumentException e) {
            _reportError(e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public boolean _hasTextualNull(String str) {
        return "null".equals(str);
    }

    /* access modifiers changed from: protected */
    public void reportUnexpectedNumberChar(int i, String str) throws JsonParseException {
        String format = String.format("Unexpected character (%s) in numeric value", new Object[]{_getCharDesc(i)});
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(format);
            sb.append(": ");
            sb.append(str);
            format = sb.toString();
        }
        _reportError(format);
    }

    /* access modifiers changed from: protected */
    public void reportInvalidNumber(String str) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid numeric value: ");
        sb.append(str);
        _reportError(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void reportOverflowInt() throws IOException {
        _reportError(String.format("Numeric value (%s) out of range of int (%d - %s)", new Object[]{getText(), Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE)}));
    }

    /* access modifiers changed from: protected */
    public void reportOverflowLong() throws IOException {
        _reportError(String.format("Numeric value (%s) out of range of long (%d - %s)", new Object[]{getText(), Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE)}));
    }

    /* access modifiers changed from: protected */
    public void _reportUnexpectedChar(int i, String str) throws JsonParseException {
        if (i < 0) {
            _reportInvalidEOF();
        }
        String format = String.format("Unexpected character (%s)", new Object[]{_getCharDesc(i)});
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(format);
            sb.append(": ");
            sb.append(str);
            format = sb.toString();
        }
        _reportError(format);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidEOF() throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append(" in ");
        sb.append(this._currToken);
        _reportInvalidEOF(sb.toString(), this._currToken);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidEOFInValue(JsonToken jsonToken) throws JsonParseException {
        String str = jsonToken == JsonToken.VALUE_STRING ? " in a String value" : (jsonToken == JsonToken.VALUE_NUMBER_INT || jsonToken == JsonToken.VALUE_NUMBER_FLOAT) ? " in a Number value" : " in a value";
        _reportInvalidEOF(str, jsonToken);
    }

    /* access modifiers changed from: protected */
    public void _reportInvalidEOF(String str, JsonToken jsonToken) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected end-of-input");
        sb.append(str);
        throw new JsonEOFException(this, jsonToken, sb.toString());
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void _reportInvalidEOFInValue() throws JsonParseException {
        _reportInvalidEOF(" in a value");
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void _reportInvalidEOF(String str) throws JsonParseException {
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected end-of-input");
        sb.append(str);
        throw new JsonEOFException(this, null, sb.toString());
    }

    /* access modifiers changed from: protected */
    public void _reportMissingRootWS(int i) throws JsonParseException {
        _reportUnexpectedChar(i, "Expected space separating root-level values");
    }

    /* access modifiers changed from: protected */
    public void _throwInvalidSpace(int i) throws JsonParseException {
        char c = (char) i;
        StringBuilder sb = new StringBuilder();
        sb.append("Illegal character (");
        sb.append(_getCharDesc(c));
        sb.append("): only regular white space (\\r, \\n, \\t) is allowed between tokens");
        _reportError(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void _throwUnquotedSpace(int i, String str) throws JsonParseException {
        if (!isEnabled(Feature.ALLOW_UNQUOTED_CONTROL_CHARS) || i > 32) {
            char c = (char) i;
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal unquoted character (");
            sb.append(_getCharDesc(c));
            sb.append("): has to be escaped using backslash to be included in ");
            sb.append(str);
            _reportError(sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public char _handleUnrecognizedCharacterEscape(char c) throws JsonProcessingException {
        if (isEnabled(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
            return c;
        }
        if (c == '\'' && isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
            return c;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unrecognized character escape ");
        sb.append(_getCharDesc(c));
        _reportError(sb.toString());
        return c;
    }

    protected static final String _getCharDesc(int i) {
        char c = (char) i;
        if (Character.isISOControl(c)) {
            StringBuilder sb = new StringBuilder();
            sb.append("(CTRL-CHAR, code ");
            sb.append(i);
            sb.append(")");
            return sb.toString();
        } else if (i > 255) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("'");
            sb2.append(c);
            sb2.append("' (code ");
            sb2.append(i);
            sb2.append(" / 0x");
            sb2.append(Integer.toHexString(i));
            sb2.append(")");
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("'");
            sb3.append(c);
            sb3.append("' (code ");
            sb3.append(i);
            sb3.append(")");
            return sb3.toString();
        }
    }

    /* access modifiers changed from: protected */
    public final void _reportError(String str) throws JsonParseException {
        throw _constructError(str);
    }

    /* access modifiers changed from: protected */
    public final void _reportError(String str, Object obj) throws JsonParseException {
        throw _constructError(String.format(str, new Object[]{obj}));
    }

    /* access modifiers changed from: protected */
    public final void _reportError(String str, Object obj, Object obj2) throws JsonParseException {
        throw _constructError(String.format(str, new Object[]{obj, obj2}));
    }

    /* access modifiers changed from: protected */
    public final void _wrapError(String str, Throwable th) throws JsonParseException {
        throw _constructError(str, th);
    }

    /* access modifiers changed from: protected */
    public final void _throwInternal() {
        VersionUtil.throwInternal();
    }

    /* access modifiers changed from: protected */
    public final JsonParseException _constructError(String str, Throwable th) {
        return new JsonParseException((JsonParser) this, str, th);
    }

    protected static byte[] _asciiBytes(String str) {
        byte[] bArr = new byte[str.length()];
        int length = str.length();
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) str.charAt(i);
        }
        return bArr;
    }

    protected static String _ascii(byte[] bArr) {
        try {
            return new String(bArr, "US-ASCII");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
