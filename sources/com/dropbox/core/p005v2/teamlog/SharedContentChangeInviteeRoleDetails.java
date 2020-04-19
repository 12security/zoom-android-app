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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeInviteeRoleDetails */
public class SharedContentChangeInviteeRoleDetails {
    protected final String invitee;
    protected final AccessLevel newAccessLevel;
    protected final AccessLevel previousAccessLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentChangeInviteeRoleDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentChangeInviteeRoleDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentChangeInviteeRoleDetails sharedContentChangeInviteeRoleDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedContentChangeInviteeRoleDetails.newAccessLevel, jsonGenerator);
            jsonGenerator.writeFieldName("invitee");
            StoneSerializers.string().serialize(sharedContentChangeInviteeRoleDetails.invitee, jsonGenerator);
            if (sharedContentChangeInviteeRoleDetails.previousAccessLevel != null) {
                jsonGenerator.writeFieldName("previous_access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(sharedContentChangeInviteeRoleDetails.previousAccessLevel, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentChangeInviteeRoleDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                AccessLevel accessLevel2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("invitee".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("previous_access_level".equals(currentName)) {
                        accessLevel2 = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"new_access_level\" missing.");
                } else if (str2 != null) {
                    SharedContentChangeInviteeRoleDetails sharedContentChangeInviteeRoleDetails = new SharedContentChangeInviteeRoleDetails(accessLevel, str2, accessLevel2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentChangeInviteeRoleDetails, sharedContentChangeInviteeRoleDetails.toStringMultiline());
                    return sharedContentChangeInviteeRoleDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"invitee\" missing.");
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

    public SharedContentChangeInviteeRoleDetails(AccessLevel accessLevel, String str, AccessLevel accessLevel2) {
        this.previousAccessLevel = accessLevel2;
        if (accessLevel != null) {
            this.newAccessLevel = accessLevel;
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'invitee' is null");
            } else if (str.length() <= 255) {
                this.invitee = str;
            } else {
                throw new IllegalArgumentException("String 'invitee' is longer than 255");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'newAccessLevel' is null");
        }
    }

    public SharedContentChangeInviteeRoleDetails(AccessLevel accessLevel, String str) {
        this(accessLevel, str, null);
    }

    public AccessLevel getNewAccessLevel() {
        return this.newAccessLevel;
    }

    public String getInvitee() {
        return this.invitee;
    }

    public AccessLevel getPreviousAccessLevel() {
        return this.previousAccessLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousAccessLevel, this.newAccessLevel, this.invitee});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r5) == false) goto L_0x003f;
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
            if (r2 == 0) goto L_0x0041
            com.dropbox.core.v2.teamlog.SharedContentChangeInviteeRoleDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedContentChangeInviteeRoleDetails) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.newAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.newAccessLevel
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            java.lang.String r2 = r4.invitee
            java.lang.String r3 = r5.invitee
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.previousAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r5 = r5.previousAccessLevel
            if (r2 == r5) goto L_0x0040
            if (r2 == 0) goto L_0x003f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = 0
        L_0x0040:
            return r0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedContentChangeInviteeRoleDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
