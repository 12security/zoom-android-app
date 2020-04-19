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

/* renamed from: com.dropbox.core.v2.teamlog.DomainVerificationRemoveDomainDetails */
public class DomainVerificationRemoveDomainDetails {
    protected final List<String> domainNames;

    /* renamed from: com.dropbox.core.v2.teamlog.DomainVerificationRemoveDomainDetails$Serializer */
    static class Serializer extends StructSerializer<DomainVerificationRemoveDomainDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DomainVerificationRemoveDomainDetails domainVerificationRemoveDomainDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("domain_names");
            StoneSerializers.list(StoneSerializers.string()).serialize(domainVerificationRemoveDomainDetails.domainNames, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DomainVerificationRemoveDomainDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("domain_names".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    DomainVerificationRemoveDomainDetails domainVerificationRemoveDomainDetails = new DomainVerificationRemoveDomainDetails(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(domainVerificationRemoveDomainDetails, domainVerificationRemoveDomainDetails.toStringMultiline());
                    return domainVerificationRemoveDomainDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"domain_names\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DomainVerificationRemoveDomainDetails(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'domainNames' is null");
                }
            }
            this.domainNames = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'domainNames' is null");
    }

    public List<String> getDomainNames() {
        return this.domainNames;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.domainNames});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DomainVerificationRemoveDomainDetails domainVerificationRemoveDomainDetails = (DomainVerificationRemoveDomainDetails) obj;
        List<String> list = this.domainNames;
        List<String> list2 = domainVerificationRemoveDomainDetails.domainNames;
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
