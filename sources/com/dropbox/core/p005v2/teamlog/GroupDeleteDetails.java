package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.GroupDeleteDetails */
public class GroupDeleteDetails {
    protected final Boolean isCompanyManaged;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupDeleteDetails$Serializer */
    static class Serializer extends StructSerializer<GroupDeleteDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupDeleteDetails groupDeleteDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (groupDeleteDetails.isCompanyManaged != null) {
                jsonGenerator.writeFieldName("is_company_managed");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(groupDeleteDetails.isCompanyManaged, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupDeleteDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("is_company_managed".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                GroupDeleteDetails groupDeleteDetails = new GroupDeleteDetails(bool);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(groupDeleteDetails, groupDeleteDetails.toStringMultiline());
                return groupDeleteDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupDeleteDetails(Boolean bool) {
        this.isCompanyManaged = bool;
    }

    public GroupDeleteDetails() {
        this(null);
    }

    public Boolean getIsCompanyManaged() {
        return this.isCompanyManaged;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.isCompanyManaged});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GroupDeleteDetails groupDeleteDetails = (GroupDeleteDetails) obj;
        Boolean bool = this.isCompanyManaged;
        Boolean bool2 = groupDeleteDetails.isCompanyManaged;
        if (bool != bool2 && (bool == null || !bool.equals(bool2))) {
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
