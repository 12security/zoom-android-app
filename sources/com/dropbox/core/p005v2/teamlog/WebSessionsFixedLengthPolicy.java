package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.WebSessionsFixedLengthPolicy */
public final class WebSessionsFixedLengthPolicy {
    public static final WebSessionsFixedLengthPolicy OTHER = new WebSessionsFixedLengthPolicy().withTag(Tag.OTHER);
    public static final WebSessionsFixedLengthPolicy UNDEFINED = new WebSessionsFixedLengthPolicy().withTag(Tag.UNDEFINED);
    private Tag _tag;
    /* access modifiers changed from: private */
    public DurationLogInfo definedValue;

    /* renamed from: com.dropbox.core.v2.teamlog.WebSessionsFixedLengthPolicy$Serializer */
    static class Serializer extends UnionSerializer<WebSessionsFixedLengthPolicy> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(WebSessionsFixedLengthPolicy webSessionsFixedLengthPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (webSessionsFixedLengthPolicy.tag()) {
                case DEFINED:
                    jsonGenerator.writeStartObject();
                    writeTag("defined", jsonGenerator);
                    Serializer.INSTANCE.serialize(webSessionsFixedLengthPolicy.definedValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case UNDEFINED:
                    jsonGenerator.writeString("undefined");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public WebSessionsFixedLengthPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            WebSessionsFixedLengthPolicy webSessionsFixedLengthPolicy;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("defined".equals(str)) {
                    webSessionsFixedLengthPolicy = WebSessionsFixedLengthPolicy.defined(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("undefined".equals(str)) {
                    webSessionsFixedLengthPolicy = WebSessionsFixedLengthPolicy.UNDEFINED;
                } else {
                    webSessionsFixedLengthPolicy = WebSessionsFixedLengthPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return webSessionsFixedLengthPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.WebSessionsFixedLengthPolicy$Tag */
    public enum Tag {
        DEFINED,
        UNDEFINED,
        OTHER
    }

    private WebSessionsFixedLengthPolicy() {
    }

    private WebSessionsFixedLengthPolicy withTag(Tag tag) {
        WebSessionsFixedLengthPolicy webSessionsFixedLengthPolicy = new WebSessionsFixedLengthPolicy();
        webSessionsFixedLengthPolicy._tag = tag;
        return webSessionsFixedLengthPolicy;
    }

    private WebSessionsFixedLengthPolicy withTagAndDefined(Tag tag, DurationLogInfo durationLogInfo) {
        WebSessionsFixedLengthPolicy webSessionsFixedLengthPolicy = new WebSessionsFixedLengthPolicy();
        webSessionsFixedLengthPolicy._tag = tag;
        webSessionsFixedLengthPolicy.definedValue = durationLogInfo;
        return webSessionsFixedLengthPolicy;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isDefined() {
        return this._tag == Tag.DEFINED;
    }

    public static WebSessionsFixedLengthPolicy defined(DurationLogInfo durationLogInfo) {
        if (durationLogInfo != null) {
            return new WebSessionsFixedLengthPolicy().withTagAndDefined(Tag.DEFINED, durationLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DurationLogInfo getDefinedValue() {
        if (this._tag == Tag.DEFINED) {
            return this.definedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DEFINED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUndefined() {
        return this._tag == Tag.UNDEFINED;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.definedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof WebSessionsFixedLengthPolicy)) {
            return false;
        }
        WebSessionsFixedLengthPolicy webSessionsFixedLengthPolicy = (WebSessionsFixedLengthPolicy) obj;
        if (this._tag != webSessionsFixedLengthPolicy._tag) {
            return false;
        }
        switch (this._tag) {
            case DEFINED:
                DurationLogInfo durationLogInfo = this.definedValue;
                DurationLogInfo durationLogInfo2 = webSessionsFixedLengthPolicy.definedValue;
                if (durationLogInfo != durationLogInfo2 && !durationLogInfo.equals(durationLogInfo2)) {
                    z = false;
                }
                return z;
            case UNDEFINED:
                return true;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
