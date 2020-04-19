package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class FilteringParserDelegate extends JsonParserDelegate {
    protected boolean _allowMultipleMatches;
    protected JsonToken _currToken;
    protected TokenFilterContext _exposedContext;
    protected TokenFilterContext _headContext;
    @Deprecated
    protected boolean _includeImmediateParent;
    protected boolean _includePath;
    protected TokenFilter _itemFilter;
    protected JsonToken _lastClearedToken;
    protected int _matchCount;
    protected TokenFilter rootFilter;

    public FilteringParserDelegate(JsonParser jsonParser, TokenFilter tokenFilter, boolean z, boolean z2) {
        super(jsonParser);
        this.rootFilter = tokenFilter;
        this._itemFilter = tokenFilter;
        this._headContext = TokenFilterContext.createRootContext(tokenFilter);
        this._includePath = z;
        this._allowMultipleMatches = z2;
    }

    public TokenFilter getFilter() {
        return this.rootFilter;
    }

    public int getMatchCount() {
        return this._matchCount;
    }

    public JsonToken getCurrentToken() {
        return this._currToken;
    }

    public JsonToken currentToken() {
        return this._currToken;
    }

    public final int getCurrentTokenId() {
        JsonToken jsonToken = this._currToken;
        if (jsonToken == null) {
            return 0;
        }
        return jsonToken.mo21951id();
    }

    public final int currentTokenId() {
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

    public final boolean hasToken(JsonToken jsonToken) {
        return this._currToken == jsonToken;
    }

    public boolean isExpectedStartArrayToken() {
        return this._currToken == JsonToken.START_ARRAY;
    }

    public boolean isExpectedStartObjectToken() {
        return this._currToken == JsonToken.START_OBJECT;
    }

    public JsonLocation getCurrentLocation() {
        return this.delegate.getCurrentLocation();
    }

    public JsonStreamContext getParsingContext() {
        return _filterContext();
    }

    public String getCurrentName() throws IOException {
        String str;
        JsonStreamContext _filterContext = _filterContext();
        if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            return _filterContext.getCurrentName();
        }
        JsonStreamContext parent = _filterContext.getParent();
        if (parent == null) {
            str = null;
        } else {
            str = parent.getCurrentName();
        }
        return str;
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

    public void overrideCurrentName(String str) {
        throw new UnsupportedOperationException("Can not currently override name during filtering read");
    }

    public JsonToken nextToken() throws IOException {
        if (!this._allowMultipleMatches) {
            JsonToken jsonToken = this._currToken;
            if (jsonToken != null && this._exposedContext == null && jsonToken.isScalarValue() && !this._headContext.isStartHandled() && !this._includePath && this._itemFilter == TokenFilter.INCLUDE_ALL) {
                this._currToken = null;
                return null;
            }
        }
        TokenFilterContext tokenFilterContext = this._exposedContext;
        if (tokenFilterContext != null) {
            do {
                JsonToken nextTokenToRead = tokenFilterContext.nextTokenToRead();
                if (nextTokenToRead != null) {
                    this._currToken = nextTokenToRead;
                    return nextTokenToRead;
                }
                TokenFilterContext tokenFilterContext2 = this._headContext;
                if (tokenFilterContext == tokenFilterContext2) {
                    this._exposedContext = null;
                    if (tokenFilterContext.inArray()) {
                        JsonToken currentToken = this.delegate.getCurrentToken();
                        this._currToken = currentToken;
                        return currentToken;
                    }
                } else {
                    tokenFilterContext = tokenFilterContext2.findChildOf(tokenFilterContext);
                    this._exposedContext = tokenFilterContext;
                }
            } while (tokenFilterContext != null);
            throw _constructError("Unexpected problem: chain of filtered context broken");
        }
        JsonToken nextToken = this.delegate.nextToken();
        if (nextToken == null) {
            this._currToken = nextToken;
            return nextToken;
        }
        switch (nextToken.mo21951id()) {
            case 1:
                TokenFilter tokenFilter = this._itemFilter;
                if (tokenFilter == TokenFilter.INCLUDE_ALL) {
                    this._headContext = this._headContext.createChildObjectContext(tokenFilter, true);
                    this._currToken = nextToken;
                    return nextToken;
                } else if (tokenFilter == null) {
                    this.delegate.skipChildren();
                    break;
                } else {
                    TokenFilter checkValue = this._headContext.checkValue(tokenFilter);
                    if (checkValue == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        if (checkValue != TokenFilter.INCLUDE_ALL) {
                            checkValue = checkValue.filterStartObject();
                        }
                        this._itemFilter = checkValue;
                        if (checkValue == TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildObjectContext(checkValue, true);
                            this._currToken = nextToken;
                            return nextToken;
                        }
                        this._headContext = this._headContext.createChildObjectContext(checkValue, false);
                        if (this._includePath) {
                            JsonToken _nextTokenWithBuffering = _nextTokenWithBuffering(this._headContext);
                            if (_nextTokenWithBuffering != null) {
                                this._currToken = _nextTokenWithBuffering;
                                return _nextTokenWithBuffering;
                            }
                        }
                    }
                }
                break;
            case 2:
            case 4:
                boolean isStartHandled = this._headContext.isStartHandled();
                TokenFilter filter = this._headContext.getFilter();
                if (!(filter == null || filter == TokenFilter.INCLUDE_ALL)) {
                    filter.filterFinishArray();
                }
                this._headContext = this._headContext.getParent();
                this._itemFilter = this._headContext.getFilter();
                if (isStartHandled) {
                    this._currToken = nextToken;
                    return nextToken;
                }
                break;
            case 3:
                TokenFilter tokenFilter2 = this._itemFilter;
                if (tokenFilter2 == TokenFilter.INCLUDE_ALL) {
                    this._headContext = this._headContext.createChildArrayContext(tokenFilter2, true);
                    this._currToken = nextToken;
                    return nextToken;
                } else if (tokenFilter2 == null) {
                    this.delegate.skipChildren();
                    break;
                } else {
                    TokenFilter checkValue2 = this._headContext.checkValue(tokenFilter2);
                    if (checkValue2 == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        if (checkValue2 != TokenFilter.INCLUDE_ALL) {
                            checkValue2 = checkValue2.filterStartArray();
                        }
                        this._itemFilter = checkValue2;
                        if (checkValue2 == TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildArrayContext(checkValue2, true);
                            this._currToken = nextToken;
                            return nextToken;
                        }
                        this._headContext = this._headContext.createChildArrayContext(checkValue2, false);
                        if (this._includePath) {
                            JsonToken _nextTokenWithBuffering2 = _nextTokenWithBuffering(this._headContext);
                            if (_nextTokenWithBuffering2 != null) {
                                this._currToken = _nextTokenWithBuffering2;
                                return _nextTokenWithBuffering2;
                            }
                        }
                    }
                }
                break;
            case 5:
                String currentName = this.delegate.getCurrentName();
                TokenFilter fieldName = this._headContext.setFieldName(currentName);
                if (fieldName == TokenFilter.INCLUDE_ALL) {
                    this._itemFilter = fieldName;
                    if (!this._includePath && this._includeImmediateParent && !this._headContext.isStartHandled()) {
                        nextToken = this._headContext.nextTokenToRead();
                        this._exposedContext = this._headContext;
                    }
                    this._currToken = nextToken;
                    return nextToken;
                } else if (fieldName == null) {
                    this.delegate.nextToken();
                    this.delegate.skipChildren();
                    break;
                } else {
                    TokenFilter includeProperty = fieldName.includeProperty(currentName);
                    if (includeProperty == null) {
                        this.delegate.nextToken();
                        this.delegate.skipChildren();
                        break;
                    } else {
                        this._itemFilter = includeProperty;
                        if (includeProperty == TokenFilter.INCLUDE_ALL) {
                            if (!_verifyAllowedMatches()) {
                                this.delegate.nextToken();
                                this.delegate.skipChildren();
                            } else if (this._includePath) {
                                this._currToken = nextToken;
                                return nextToken;
                            }
                        }
                        if (this._includePath) {
                            JsonToken _nextTokenWithBuffering3 = _nextTokenWithBuffering(this._headContext);
                            if (_nextTokenWithBuffering3 != null) {
                                this._currToken = _nextTokenWithBuffering3;
                                return _nextTokenWithBuffering3;
                            }
                        }
                    }
                }
                break;
            default:
                TokenFilter tokenFilter3 = this._itemFilter;
                if (tokenFilter3 == TokenFilter.INCLUDE_ALL) {
                    this._currToken = nextToken;
                    return nextToken;
                } else if (tokenFilter3 != null) {
                    TokenFilter checkValue3 = this._headContext.checkValue(tokenFilter3);
                    if ((checkValue3 == TokenFilter.INCLUDE_ALL || (checkValue3 != null && checkValue3.includeValue(this.delegate))) && _verifyAllowedMatches()) {
                        this._currToken = nextToken;
                        return nextToken;
                    }
                }
                break;
        }
        return _nextToken2();
    }

    /* access modifiers changed from: protected */
    public final JsonToken _nextToken2() throws IOException {
        while (true) {
            JsonToken nextToken = this.delegate.nextToken();
            if (nextToken == null) {
                this._currToken = nextToken;
                return nextToken;
            }
            switch (nextToken.mo21951id()) {
                case 1:
                    TokenFilter tokenFilter = this._itemFilter;
                    if (tokenFilter == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildObjectContext(tokenFilter, true);
                        this._currToken = nextToken;
                        return nextToken;
                    } else if (tokenFilter == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        TokenFilter checkValue = this._headContext.checkValue(tokenFilter);
                        if (checkValue == null) {
                            this.delegate.skipChildren();
                            break;
                        } else {
                            if (checkValue != TokenFilter.INCLUDE_ALL) {
                                checkValue = checkValue.filterStartObject();
                            }
                            this._itemFilter = checkValue;
                            if (checkValue == TokenFilter.INCLUDE_ALL) {
                                this._headContext = this._headContext.createChildObjectContext(checkValue, true);
                                this._currToken = nextToken;
                                return nextToken;
                            }
                            this._headContext = this._headContext.createChildObjectContext(checkValue, false);
                            if (this._includePath) {
                                JsonToken _nextTokenWithBuffering = _nextTokenWithBuffering(this._headContext);
                                if (_nextTokenWithBuffering == null) {
                                    break;
                                } else {
                                    this._currToken = _nextTokenWithBuffering;
                                    return _nextTokenWithBuffering;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                case 2:
                case 4:
                    boolean isStartHandled = this._headContext.isStartHandled();
                    TokenFilter filter = this._headContext.getFilter();
                    if (!(filter == null || filter == TokenFilter.INCLUDE_ALL)) {
                        filter.filterFinishArray();
                    }
                    this._headContext = this._headContext.getParent();
                    this._itemFilter = this._headContext.getFilter();
                    if (!isStartHandled) {
                        break;
                    } else {
                        this._currToken = nextToken;
                        return nextToken;
                    }
                case 3:
                    TokenFilter tokenFilter2 = this._itemFilter;
                    if (tokenFilter2 == TokenFilter.INCLUDE_ALL) {
                        this._headContext = this._headContext.createChildArrayContext(tokenFilter2, true);
                        this._currToken = nextToken;
                        return nextToken;
                    } else if (tokenFilter2 == null) {
                        this.delegate.skipChildren();
                        break;
                    } else {
                        TokenFilter checkValue2 = this._headContext.checkValue(tokenFilter2);
                        if (checkValue2 == null) {
                            this.delegate.skipChildren();
                            break;
                        } else {
                            if (checkValue2 != TokenFilter.INCLUDE_ALL) {
                                checkValue2 = checkValue2.filterStartArray();
                            }
                            this._itemFilter = checkValue2;
                            if (checkValue2 == TokenFilter.INCLUDE_ALL) {
                                this._headContext = this._headContext.createChildArrayContext(checkValue2, true);
                                this._currToken = nextToken;
                                return nextToken;
                            }
                            this._headContext = this._headContext.createChildArrayContext(checkValue2, false);
                            if (this._includePath) {
                                JsonToken _nextTokenWithBuffering2 = _nextTokenWithBuffering(this._headContext);
                                if (_nextTokenWithBuffering2 == null) {
                                    break;
                                } else {
                                    this._currToken = _nextTokenWithBuffering2;
                                    return _nextTokenWithBuffering2;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                case 5:
                    String currentName = this.delegate.getCurrentName();
                    TokenFilter fieldName = this._headContext.setFieldName(currentName);
                    if (fieldName == TokenFilter.INCLUDE_ALL) {
                        this._itemFilter = fieldName;
                        this._currToken = nextToken;
                        return nextToken;
                    } else if (fieldName == null) {
                        this.delegate.nextToken();
                        this.delegate.skipChildren();
                        break;
                    } else {
                        TokenFilter includeProperty = fieldName.includeProperty(currentName);
                        if (includeProperty == null) {
                            this.delegate.nextToken();
                            this.delegate.skipChildren();
                            break;
                        } else {
                            this._itemFilter = includeProperty;
                            if (includeProperty == TokenFilter.INCLUDE_ALL) {
                                if (_verifyAllowedMatches() && this._includePath) {
                                    this._currToken = nextToken;
                                    return nextToken;
                                }
                            } else if (this._includePath) {
                                JsonToken _nextTokenWithBuffering3 = _nextTokenWithBuffering(this._headContext);
                                if (_nextTokenWithBuffering3 == null) {
                                    break;
                                } else {
                                    this._currToken = _nextTokenWithBuffering3;
                                    return _nextTokenWithBuffering3;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                default:
                    TokenFilter tokenFilter3 = this._itemFilter;
                    if (tokenFilter3 == TokenFilter.INCLUDE_ALL) {
                        this._currToken = nextToken;
                        return nextToken;
                    } else if (tokenFilter3 != null) {
                        TokenFilter checkValue3 = this._headContext.checkValue(tokenFilter3);
                        if ((checkValue3 == TokenFilter.INCLUDE_ALL || (checkValue3 != null && checkValue3.includeValue(this.delegate))) && _verifyAllowedMatches()) {
                            this._currToken = nextToken;
                            return nextToken;
                        }
                    } else {
                        continue;
                    }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final JsonToken _nextTokenWithBuffering(TokenFilterContext tokenFilterContext) throws IOException {
        while (true) {
            JsonToken nextToken = this.delegate.nextToken();
            if (nextToken == null) {
                return nextToken;
            }
            boolean z = false;
            switch (nextToken.mo21951id()) {
                case 1:
                    TokenFilter tokenFilter = this._itemFilter;
                    if (tokenFilter != TokenFilter.INCLUDE_ALL) {
                        if (tokenFilter != null) {
                            TokenFilter checkValue = this._headContext.checkValue(tokenFilter);
                            if (checkValue != null) {
                                if (checkValue != TokenFilter.INCLUDE_ALL) {
                                    checkValue = checkValue.filterStartObject();
                                }
                                this._itemFilter = checkValue;
                                if (checkValue != TokenFilter.INCLUDE_ALL) {
                                    this._headContext = this._headContext.createChildObjectContext(checkValue, false);
                                    break;
                                } else {
                                    this._headContext = this._headContext.createChildObjectContext(checkValue, true);
                                    return _nextBuffered(tokenFilterContext);
                                }
                            } else {
                                this.delegate.skipChildren();
                                break;
                            }
                        } else {
                            this.delegate.skipChildren();
                            break;
                        }
                    } else {
                        this._headContext = this._headContext.createChildObjectContext(tokenFilter, true);
                        return nextToken;
                    }
                case 2:
                case 4:
                    TokenFilter filter = this._headContext.getFilter();
                    if (!(filter == null || filter == TokenFilter.INCLUDE_ALL)) {
                        filter.filterFinishArray();
                    }
                    if ((this._headContext == tokenFilterContext) && this._headContext.isStartHandled()) {
                        z = true;
                    }
                    this._headContext = this._headContext.getParent();
                    this._itemFilter = this._headContext.getFilter();
                    if (!z) {
                        break;
                    } else {
                        return nextToken;
                    }
                case 3:
                    TokenFilter checkValue2 = this._headContext.checkValue(this._itemFilter);
                    if (checkValue2 != null) {
                        if (checkValue2 != TokenFilter.INCLUDE_ALL) {
                            checkValue2 = checkValue2.filterStartArray();
                        }
                        this._itemFilter = checkValue2;
                        if (checkValue2 != TokenFilter.INCLUDE_ALL) {
                            this._headContext = this._headContext.createChildArrayContext(checkValue2, false);
                            break;
                        } else {
                            this._headContext = this._headContext.createChildArrayContext(checkValue2, true);
                            return _nextBuffered(tokenFilterContext);
                        }
                    } else {
                        this.delegate.skipChildren();
                        break;
                    }
                case 5:
                    String currentName = this.delegate.getCurrentName();
                    TokenFilter fieldName = this._headContext.setFieldName(currentName);
                    if (fieldName != TokenFilter.INCLUDE_ALL) {
                        if (fieldName != null) {
                            TokenFilter includeProperty = fieldName.includeProperty(currentName);
                            if (includeProperty != null) {
                                this._itemFilter = includeProperty;
                                if (includeProperty == TokenFilter.INCLUDE_ALL) {
                                    if (!_verifyAllowedMatches()) {
                                        this._itemFilter = this._headContext.setFieldName(currentName);
                                        break;
                                    } else {
                                        return _nextBuffered(tokenFilterContext);
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                this.delegate.nextToken();
                                this.delegate.skipChildren();
                                break;
                            }
                        } else {
                            this.delegate.nextToken();
                            this.delegate.skipChildren();
                            break;
                        }
                    } else {
                        this._itemFilter = fieldName;
                        return _nextBuffered(tokenFilterContext);
                    }
                default:
                    TokenFilter tokenFilter2 = this._itemFilter;
                    if (tokenFilter2 == TokenFilter.INCLUDE_ALL) {
                        return _nextBuffered(tokenFilterContext);
                    }
                    if (tokenFilter2 != null) {
                        TokenFilter checkValue3 = this._headContext.checkValue(tokenFilter2);
                        if ((checkValue3 == TokenFilter.INCLUDE_ALL || (checkValue3 != null && checkValue3.includeValue(this.delegate))) && _verifyAllowedMatches()) {
                            return _nextBuffered(tokenFilterContext);
                        }
                    } else {
                        continue;
                    }
            }
        }
    }

    private JsonToken _nextBuffered(TokenFilterContext tokenFilterContext) throws IOException {
        this._exposedContext = tokenFilterContext;
        JsonToken nextTokenToRead = tokenFilterContext.nextTokenToRead();
        if (nextTokenToRead != null) {
            return nextTokenToRead;
        }
        while (tokenFilterContext != this._headContext) {
            tokenFilterContext = this._exposedContext.findChildOf(tokenFilterContext);
            this._exposedContext = tokenFilterContext;
            if (tokenFilterContext != null) {
                JsonToken nextTokenToRead2 = this._exposedContext.nextTokenToRead();
                if (nextTokenToRead2 != null) {
                    return nextTokenToRead2;
                }
            } else {
                throw _constructError("Unexpected problem: chain of filtered context broken");
            }
        }
        throw _constructError("Internal error: failed to locate expected buffered tokens");
    }

    private final boolean _verifyAllowedMatches() throws IOException {
        if (this._matchCount != 0 && !this._allowMultipleMatches) {
            return false;
        }
        this._matchCount++;
        return true;
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
                return this;
            }
            if (nextToken.isStructStart()) {
                i++;
            } else if (nextToken.isStructEnd()) {
                i--;
                if (i == 0) {
                    return this;
                }
            } else {
                continue;
            }
        }
    }

    public String getText() throws IOException {
        return this.delegate.getText();
    }

    public boolean hasTextCharacters() {
        return this.delegate.hasTextCharacters();
    }

    public char[] getTextCharacters() throws IOException {
        return this.delegate.getTextCharacters();
    }

    public int getTextLength() throws IOException {
        return this.delegate.getTextLength();
    }

    public int getTextOffset() throws IOException {
        return this.delegate.getTextOffset();
    }

    public BigInteger getBigIntegerValue() throws IOException {
        return this.delegate.getBigIntegerValue();
    }

    public boolean getBooleanValue() throws IOException {
        return this.delegate.getBooleanValue();
    }

    public byte getByteValue() throws IOException {
        return this.delegate.getByteValue();
    }

    public short getShortValue() throws IOException {
        return this.delegate.getShortValue();
    }

    public BigDecimal getDecimalValue() throws IOException {
        return this.delegate.getDecimalValue();
    }

    public double getDoubleValue() throws IOException {
        return this.delegate.getDoubleValue();
    }

    public float getFloatValue() throws IOException {
        return this.delegate.getFloatValue();
    }

    public int getIntValue() throws IOException {
        return this.delegate.getIntValue();
    }

    public long getLongValue() throws IOException {
        return this.delegate.getLongValue();
    }

    public NumberType getNumberType() throws IOException {
        return this.delegate.getNumberType();
    }

    public Number getNumberValue() throws IOException {
        return this.delegate.getNumberValue();
    }

    public int getValueAsInt() throws IOException {
        return this.delegate.getValueAsInt();
    }

    public int getValueAsInt(int i) throws IOException {
        return this.delegate.getValueAsInt(i);
    }

    public long getValueAsLong() throws IOException {
        return this.delegate.getValueAsLong();
    }

    public long getValueAsLong(long j) throws IOException {
        return this.delegate.getValueAsLong(j);
    }

    public double getValueAsDouble() throws IOException {
        return this.delegate.getValueAsDouble();
    }

    public double getValueAsDouble(double d) throws IOException {
        return this.delegate.getValueAsDouble(d);
    }

    public boolean getValueAsBoolean() throws IOException {
        return this.delegate.getValueAsBoolean();
    }

    public boolean getValueAsBoolean(boolean z) throws IOException {
        return this.delegate.getValueAsBoolean(z);
    }

    public String getValueAsString() throws IOException {
        return this.delegate.getValueAsString();
    }

    public String getValueAsString(String str) throws IOException {
        return this.delegate.getValueAsString(str);
    }

    public Object getEmbeddedObject() throws IOException {
        return this.delegate.getEmbeddedObject();
    }

    public byte[] getBinaryValue(Base64Variant base64Variant) throws IOException {
        return this.delegate.getBinaryValue(base64Variant);
    }

    public int readBinaryValue(Base64Variant base64Variant, OutputStream outputStream) throws IOException {
        return this.delegate.readBinaryValue(base64Variant, outputStream);
    }

    public JsonLocation getTokenLocation() {
        return this.delegate.getTokenLocation();
    }

    /* access modifiers changed from: protected */
    public JsonStreamContext _filterContext() {
        TokenFilterContext tokenFilterContext = this._exposedContext;
        if (tokenFilterContext != null) {
            return tokenFilterContext;
        }
        return this._headContext;
    }
}
