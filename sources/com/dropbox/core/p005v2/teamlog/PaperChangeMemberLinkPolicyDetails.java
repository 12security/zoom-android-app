package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.PaperChangeMemberLinkPolicyDetails */
public class PaperChangeMemberLinkPolicyDetails {
    protected final PaperMemberPolicy newValue;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperChangeMemberLinkPolicyDetails$Serializer */
    static class Serializer extends StructSerializer<PaperChangeMemberLinkPolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperChangeMemberLinkPolicyDetails paperChangeMemberLinkPolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(paperChangeMemberLinkPolicyDetails.newValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperChangeMemberLinkPolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            PaperMemberPolicy paperMemberPolicy = null;
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
                    if ("new_value".equals(currentName)) {
                        paperMemberPolicy = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (paperMemberPolicy != null) {
                    PaperChangeMemberLinkPolicyDetails paperChangeMemberLinkPolicyDetails = new PaperChangeMemberLinkPolicyDetails(paperMemberPolicy);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperChangeMemberLinkPolicyDetails, paperChangeMemberLinkPolicyDetails.toStringMultiline());
                    return paperChangeMemberLinkPolicyDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PaperChangeMemberLinkPolicyDetails(PaperMemberPolicy paperMemberPolicy) {
        if (paperMemberPolicy != null) {
            this.newValue = paperMemberPolicy;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public PaperMemberPolicy getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        PaperChangeMemberLinkPolicyDetails paperChangeMemberLinkPolicyDetails = (PaperChangeMemberLinkPolicyDetails) obj;
        PaperMemberPolicy paperMemberPolicy = this.newValue;
        PaperMemberPolicy paperMemberPolicy2 = paperChangeMemberLinkPolicyDetails.newValue;
        if (paperMemberPolicy != paperMemberPolicy2 && !paperMemberPolicy.equals(paperMemberPolicy2)) {
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
