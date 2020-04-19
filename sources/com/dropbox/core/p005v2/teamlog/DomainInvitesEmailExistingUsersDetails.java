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

/* renamed from: com.dropbox.core.v2.teamlog.DomainInvitesEmailExistingUsersDetails */
public class DomainInvitesEmailExistingUsersDetails {
    protected final String domainName;
    protected final long numRecipients;

    /* renamed from: com.dropbox.core.v2.teamlog.DomainInvitesEmailExistingUsersDetails$Serializer */
    static class Serializer extends StructSerializer<DomainInvitesEmailExistingUsersDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DomainInvitesEmailExistingUsersDetails domainInvitesEmailExistingUsersDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("domain_name");
            StoneSerializers.string().serialize(domainInvitesEmailExistingUsersDetails.domainName, jsonGenerator);
            jsonGenerator.writeFieldName("num_recipients");
            StoneSerializers.uInt64().serialize(Long.valueOf(domainInvitesEmailExistingUsersDetails.numRecipients), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DomainInvitesEmailExistingUsersDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("domain_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("num_recipients".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"domain_name\" missing.");
                } else if (l != null) {
                    DomainInvitesEmailExistingUsersDetails domainInvitesEmailExistingUsersDetails = new DomainInvitesEmailExistingUsersDetails(str2, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(domainInvitesEmailExistingUsersDetails, domainInvitesEmailExistingUsersDetails.toStringMultiline());
                    return domainInvitesEmailExistingUsersDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"num_recipients\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public DomainInvitesEmailExistingUsersDetails(String str, long j) {
        if (str != null) {
            this.domainName = str;
            this.numRecipients = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'domainName' is null");
    }

    public String getDomainName() {
        return this.domainName;
    }

    public long getNumRecipients() {
        return this.numRecipients;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.domainName, Long.valueOf(this.numRecipients)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DomainInvitesEmailExistingUsersDetails domainInvitesEmailExistingUsersDetails = (DomainInvitesEmailExistingUsersDetails) obj;
        String str = this.domainName;
        String str2 = domainInvitesEmailExistingUsersDetails.domainName;
        if ((str != str2 && !str.equals(str2)) || this.numRecipients != domainInvitesEmailExistingUsersDetails.numRecipients) {
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
