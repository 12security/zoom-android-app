package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsContinueError */
public final class GetTeamEventsContinueError {
    public static final GetTeamEventsContinueError BAD_CURSOR = new GetTeamEventsContinueError().withTag(Tag.BAD_CURSOR);
    public static final GetTeamEventsContinueError OTHER = new GetTeamEventsContinueError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public Date resetValue;

    /* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsContinueError$Serializer */
    static class Serializer extends UnionSerializer<GetTeamEventsContinueError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetTeamEventsContinueError getTeamEventsContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getTeamEventsContinueError.tag()) {
                case BAD_CURSOR:
                    jsonGenerator.writeString("bad_cursor");
                    return;
                case RESET:
                    jsonGenerator.writeStartObject();
                    writeTag("reset", jsonGenerator);
                    jsonGenerator.writeFieldName("reset");
                    StoneSerializers.timestamp().serialize(getTeamEventsContinueError.resetValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GetTeamEventsContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetTeamEventsContinueError getTeamEventsContinueError;
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
                if ("bad_cursor".equals(str)) {
                    getTeamEventsContinueError = GetTeamEventsContinueError.BAD_CURSOR;
                } else if ("reset".equals(str)) {
                    expectField("reset", jsonParser);
                    getTeamEventsContinueError = GetTeamEventsContinueError.reset((Date) StoneSerializers.timestamp().deserialize(jsonParser));
                } else {
                    getTeamEventsContinueError = GetTeamEventsContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getTeamEventsContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.GetTeamEventsContinueError$Tag */
    public enum Tag {
        BAD_CURSOR,
        RESET,
        OTHER
    }

    private GetTeamEventsContinueError() {
    }

    private GetTeamEventsContinueError withTag(Tag tag) {
        GetTeamEventsContinueError getTeamEventsContinueError = new GetTeamEventsContinueError();
        getTeamEventsContinueError._tag = tag;
        return getTeamEventsContinueError;
    }

    private GetTeamEventsContinueError withTagAndReset(Tag tag, Date date) {
        GetTeamEventsContinueError getTeamEventsContinueError = new GetTeamEventsContinueError();
        getTeamEventsContinueError._tag = tag;
        getTeamEventsContinueError.resetValue = date;
        return getTeamEventsContinueError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isBadCursor() {
        return this._tag == Tag.BAD_CURSOR;
    }

    public boolean isReset() {
        return this._tag == Tag.RESET;
    }

    public static GetTeamEventsContinueError reset(Date date) {
        if (date != null) {
            return new GetTeamEventsContinueError().withTagAndReset(Tag.RESET, date);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public Date getResetValue() {
        if (this._tag == Tag.RESET) {
            return this.resetValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.RESET, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.resetValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetTeamEventsContinueError)) {
            return false;
        }
        GetTeamEventsContinueError getTeamEventsContinueError = (GetTeamEventsContinueError) obj;
        if (this._tag != getTeamEventsContinueError._tag) {
            return false;
        }
        switch (this._tag) {
            case BAD_CURSOR:
                return true;
            case RESET:
                Date date = this.resetValue;
                Date date2 = getTeamEventsContinueError.resetValue;
                if (date != date2 && !date.equals(date2)) {
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
