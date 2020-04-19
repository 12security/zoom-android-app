package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxList;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.MembersListArg */
class MembersListArg {
    protected final boolean includeRemoved;
    protected final long limit;

    /* renamed from: com.dropbox.core.v2.team.MembersListArg$Builder */
    public static class Builder {
        protected boolean includeRemoved = false;
        protected long limit = 1000;

        protected Builder() {
        }

        public Builder withLimit(Long l) {
            if (l.longValue() < 1) {
                throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
            } else if (l.longValue() <= 1000) {
                if (l != null) {
                    this.limit = l.longValue();
                } else {
                    this.limit = 1000;
                }
                return this;
            } else {
                throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
            }
        }

        public Builder withIncludeRemoved(Boolean bool) {
            if (bool != null) {
                this.includeRemoved = bool.booleanValue();
            } else {
                this.includeRemoved = false;
            }
            return this;
        }

        public MembersListArg build() {
            return new MembersListArg(this.limit, this.includeRemoved);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersListArg$Serializer */
    static class Serializer extends StructSerializer<MembersListArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersListArg membersListArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
            StoneSerializers.uInt32().serialize(Long.valueOf(membersListArg.limit), jsonGenerator);
            jsonGenerator.writeFieldName("include_removed");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membersListArg.includeRemoved), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersListArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long valueOf = Long.valueOf(1000);
                Boolean valueOf2 = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        valueOf = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("include_removed".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                MembersListArg membersListArg = new MembersListArg(valueOf.longValue(), valueOf2.booleanValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(membersListArg, membersListArg.toStringMultiline());
                return membersListArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembersListArg(long j, boolean z) {
        if (j < 1) {
            throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
        } else if (j <= 1000) {
            this.limit = j;
            this.includeRemoved = z;
        } else {
            throw new IllegalArgumentException("Number 'limit' is larger than 1000L");
        }
    }

    public MembersListArg() {
        this(1000, false);
    }

    public long getLimit() {
        return this.limit;
    }

    public boolean getIncludeRemoved() {
        return this.includeRemoved;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.limit), Boolean.valueOf(this.includeRemoved)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MembersListArg membersListArg = (MembersListArg) obj;
        if (!(this.limit == membersListArg.limit && this.includeRemoved == membersListArg.includeRemoved)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
