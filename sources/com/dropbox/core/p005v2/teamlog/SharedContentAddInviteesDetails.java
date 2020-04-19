package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.sharing.AccessLevel;
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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddInviteesDetails */
public class SharedContentAddInviteesDetails {
    protected final List<String> invitees;
    protected final AccessLevel sharedContentAccessLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddInviteesDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentAddInviteesDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentAddInviteesDetails sharedContentAddInviteesDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_content_access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedContentAddInviteesDetails.sharedContentAccessLevel, jsonGenerator);
            jsonGenerator.writeFieldName("invitees");
            StoneSerializers.list(StoneSerializers.string()).serialize(sharedContentAddInviteesDetails.invitees, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentAddInviteesDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_content_access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("invitees".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_content_access_level\" missing.");
                } else if (list != null) {
                    SharedContentAddInviteesDetails sharedContentAddInviteesDetails = new SharedContentAddInviteesDetails(accessLevel, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentAddInviteesDetails, sharedContentAddInviteesDetails.toStringMultiline());
                    return sharedContentAddInviteesDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"invitees\" missing.");
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

    public SharedContentAddInviteesDetails(AccessLevel accessLevel, List<String> list) {
        if (accessLevel != null) {
            this.sharedContentAccessLevel = accessLevel;
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'invitees' is null");
                    } else if (str.length() > 255) {
                        throw new IllegalArgumentException("Stringan item in list 'invitees' is longer than 255");
                    }
                }
                this.invitees = list;
                return;
            }
            throw new IllegalArgumentException("Required value for 'invitees' is null");
        }
        throw new IllegalArgumentException("Required value for 'sharedContentAccessLevel' is null");
    }

    public AccessLevel getSharedContentAccessLevel() {
        return this.sharedContentAccessLevel;
    }

    public List<String> getInvitees() {
        return this.invitees;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedContentAccessLevel, this.invitees});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.teamlog.SharedContentAddInviteesDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedContentAddInviteesDetails) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.sharedContentAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.sharedContentAccessLevel
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.util.List<java.lang.String> r2 = r4.invitees
            java.util.List<java.lang.String> r5 = r5.invitees
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedContentAddInviteesDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
