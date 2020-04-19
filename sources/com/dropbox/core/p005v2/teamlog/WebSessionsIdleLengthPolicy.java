package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.WebSessionsIdleLengthPolicy */
public final class WebSessionsIdleLengthPolicy {
    public static final WebSessionsIdleLengthPolicy OTHER = new WebSessionsIdleLengthPolicy().withTag(Tag.OTHER);
    public static final WebSessionsIdleLengthPolicy UNDEFINED = new WebSessionsIdleLengthPolicy().withTag(Tag.UNDEFINED);
    private Tag _tag;
    /* access modifiers changed from: private */
    public DurationLogInfo definedValue;

    /* renamed from: com.dropbox.core.v2.teamlog.WebSessionsIdleLengthPolicy$Serializer */
    static class Serializer extends UnionSerializer<WebSessionsIdleLengthPolicy> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(WebSessionsIdleLengthPolicy webSessionsIdleLengthPolicy, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (webSessionsIdleLengthPolicy.tag()) {
                case DEFINED:
                    jsonGenerator.writeStartObject();
                    writeTag("defined", jsonGenerator);
                    Serializer.INSTANCE.serialize(webSessionsIdleLengthPolicy.definedValue, jsonGenerator, true);
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

        public WebSessionsIdleLengthPolicy deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            WebSessionsIdleLengthPolicy webSessionsIdleLengthPolicy;
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
                    webSessionsIdleLengthPolicy = WebSessionsIdleLengthPolicy.defined(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("undefined".equals(str)) {
                    webSessionsIdleLengthPolicy = WebSessionsIdleLengthPolicy.UNDEFINED;
                } else {
                    webSessionsIdleLengthPolicy = WebSessionsIdleLengthPolicy.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return webSessionsIdleLengthPolicy;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.WebSessionsIdleLengthPolicy$Tag */
    public enum Tag {
        DEFINED,
        UNDEFINED,
        OTHER
    }

    private WebSessionsIdleLengthPolicy() {
    }

    private WebSessionsIdleLengthPolicy withTag(Tag tag) {
        WebSessionsIdleLengthPolicy webSessionsIdleLengthPolicy = new WebSessionsIdleLengthPolicy();
        webSessionsIdleLengthPolicy._tag = tag;
        return webSessionsIdleLengthPolicy;
    }

    private WebSessionsIdleLengthPolicy withTagAndDefined(Tag tag, DurationLogInfo durationLogInfo) {
        WebSessionsIdleLengthPolicy webSessionsIdleLengthPolicy = new WebSessionsIdleLengthPolicy();
        webSessionsIdleLengthPolicy._tag = tag;
        webSessionsIdleLengthPolicy.definedValue = durationLogInfo;
        return webSessionsIdleLengthPolicy;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isDefined() {
        return this._tag == Tag.DEFINED;
    }

    public static WebSessionsIdleLengthPolicy defined(DurationLogInfo durationLogInfo) {
        if (durationLogInfo != null) {
            return new WebSessionsIdleLengthPolicy().withTagAndDefined(Tag.DEFINED, durationLogInfo);
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
        if (obj == null || !(obj instanceof WebSessionsIdleLengthPolicy)) {
            return false;
        }
        WebSessionsIdleLengthPolicy webSessionsIdleLengthPolicy = (WebSessionsIdleLengthPolicy) obj;
        if (this._tag != webSessionsIdleLengthPolicy._tag) {
            return false;
        }
        switch (this._tag) {
            case DEFINED:
                DurationLogInfo durationLogInfo = this.definedValue;
                DurationLogInfo durationLogInfo2 = webSessionsIdleLengthPolicy.definedValue;
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
