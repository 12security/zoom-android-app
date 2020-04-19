package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxList;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.UploadApiRateLimitValue */
public final class UploadApiRateLimitValue {
    public static final UploadApiRateLimitValue OTHER = new UploadApiRateLimitValue().withTag(Tag.OTHER);
    public static final UploadApiRateLimitValue UNLIMITED = new UploadApiRateLimitValue().withTag(Tag.UNLIMITED);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Long limitValue;

    /* renamed from: com.dropbox.core.v2.team.UploadApiRateLimitValue$Serializer */
    static class Serializer extends UnionSerializer<UploadApiRateLimitValue> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadApiRateLimitValue uploadApiRateLimitValue, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadApiRateLimitValue.tag()) {
                case UNLIMITED:
                    jsonGenerator.writeString("unlimited");
                    return;
                case LIMIT:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxList.FIELD_LIMIT, jsonGenerator);
                    jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
                    StoneSerializers.uInt32().serialize(uploadApiRateLimitValue.limitValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UploadApiRateLimitValue deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UploadApiRateLimitValue uploadApiRateLimitValue;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("unlimited".equals(str)) {
                    uploadApiRateLimitValue = UploadApiRateLimitValue.UNLIMITED;
                } else if (BoxList.FIELD_LIMIT.equals(str)) {
                    expectField(BoxList.FIELD_LIMIT, jsonParser);
                    uploadApiRateLimitValue = UploadApiRateLimitValue.limit(((Long) StoneSerializers.uInt32().deserialize(jsonParser)).longValue());
                } else {
                    uploadApiRateLimitValue = UploadApiRateLimitValue.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return uploadApiRateLimitValue;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.UploadApiRateLimitValue$Tag */
    public enum Tag {
        UNLIMITED,
        LIMIT,
        OTHER
    }

    private UploadApiRateLimitValue() {
    }

    private UploadApiRateLimitValue withTag(Tag tag) {
        UploadApiRateLimitValue uploadApiRateLimitValue = new UploadApiRateLimitValue();
        uploadApiRateLimitValue._tag = tag;
        return uploadApiRateLimitValue;
    }

    private UploadApiRateLimitValue withTagAndLimit(Tag tag, Long l) {
        UploadApiRateLimitValue uploadApiRateLimitValue = new UploadApiRateLimitValue();
        uploadApiRateLimitValue._tag = tag;
        uploadApiRateLimitValue.limitValue = l;
        return uploadApiRateLimitValue;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUnlimited() {
        return this._tag == Tag.UNLIMITED;
    }

    public boolean isLimit() {
        return this._tag == Tag.LIMIT;
    }

    public static UploadApiRateLimitValue limit(long j) {
        return new UploadApiRateLimitValue().withTagAndLimit(Tag.LIMIT, Long.valueOf(j));
    }

    public long getLimitValue() {
        if (this._tag == Tag.LIMIT) {
            return this.limitValue.longValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.LIMIT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.limitValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadApiRateLimitValue)) {
            return false;
        }
        UploadApiRateLimitValue uploadApiRateLimitValue = (UploadApiRateLimitValue) obj;
        if (this._tag != uploadApiRateLimitValue._tag) {
            return false;
        }
        switch (this._tag) {
            case UNLIMITED:
                return true;
            case LIMIT:
                if (this.limitValue != uploadApiRateLimitValue.limitValue) {
                    z = false;
                }
                return z;
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
