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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeMemberRoleDetails */
public class SharedContentChangeMemberRoleDetails {
    protected final AccessLevel newAccessLevel;
    protected final AccessLevel previousAccessLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeMemberRoleDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentChangeMemberRoleDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentChangeMemberRoleDetails sharedContentChangeMemberRoleDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedContentChangeMemberRoleDetails.newAccessLevel, jsonGenerator);
            if (sharedContentChangeMemberRoleDetails.previousAccessLevel != null) {
                jsonGenerator.writeFieldName("previous_access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(sharedContentChangeMemberRoleDetails.previousAccessLevel, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentChangeMemberRoleDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AccessLevel accessLevel2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_access_level".equals(currentName)) {
                        accessLevel2 = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel != null) {
                    SharedContentChangeMemberRoleDetails sharedContentChangeMemberRoleDetails = new SharedContentChangeMemberRoleDetails(accessLevel, accessLevel2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentChangeMemberRoleDetails, sharedContentChangeMemberRoleDetails.toStringMultiline());
                    return sharedContentChangeMemberRoleDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_access_level\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentChangeMemberRoleDetails(AccessLevel accessLevel, AccessLevel accessLevel2) {
        this.previousAccessLevel = accessLevel2;
        if (accessLevel != null) {
            this.newAccessLevel = accessLevel;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newAccessLevel' is null");
    }

    public SharedContentChangeMemberRoleDetails(AccessLevel accessLevel) {
        this(accessLevel, null);
    }

    public AccessLevel getNewAccessLevel() {
        return this.newAccessLevel;
    }

    public AccessLevel getPreviousAccessLevel() {
        return this.previousAccessLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousAccessLevel, this.newAccessLevel});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.SharedContentChangeMemberRoleDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedContentChangeMemberRoleDetails) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.newAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.newAccessLevel
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.previousAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r5 = r5.previousAccessLevel
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedContentChangeMemberRoleDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
