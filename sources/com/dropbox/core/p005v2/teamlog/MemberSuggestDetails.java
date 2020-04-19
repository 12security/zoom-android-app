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
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.MemberSuggestDetails */
public class MemberSuggestDetails {
    protected final List<String> suggestedMembers;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberSuggestDetails$Serializer */
    static class Serializer extends StructSerializer<MemberSuggestDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberSuggestDetails memberSuggestDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("suggested_members");
            StoneSerializers.list(StoneSerializers.string()).serialize(memberSuggestDetails.suggestedMembers, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberSuggestDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
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
                    if ("suggested_members".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    MemberSuggestDetails memberSuggestDetails = new MemberSuggestDetails(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberSuggestDetails, memberSuggestDetails.toStringMultiline());
                    return memberSuggestDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"suggested_members\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberSuggestDetails(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'suggestedMembers' is null");
                } else if (str.length() > 255) {
                    throw new IllegalArgumentException("Stringan item in list 'suggestedMembers' is longer than 255");
                }
            }
            this.suggestedMembers = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'suggestedMembers' is null");
    }

    public List<String> getSuggestedMembers() {
        return this.suggestedMembers;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.suggestedMembers});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MemberSuggestDetails memberSuggestDetails = (MemberSuggestDetails) obj;
        List<String> list = this.suggestedMembers;
        List<String> list2 = memberSuggestDetails.suggestedMembers;
        if (list != list2 && !list.equals(list2)) {
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
