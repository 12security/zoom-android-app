package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsArg */
class MembersSetPermissionsArg {
    protected final AdminTier newRole;
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsArg$Serializer */
    static class Serializer extends StructSerializer<MembersSetPermissionsArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersSetPermissionsArg membersSetPermissionsArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersSetPermissionsArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("new_role");
            Serializer.INSTANCE.serialize(membersSetPermissionsArg.newRole, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersSetPermissionsArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserSelectorArg userSelectorArg = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AdminTier adminTier = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("new_role".equals(currentName)) {
                        adminTier = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
                } else if (adminTier != null) {
                    MembersSetPermissionsArg membersSetPermissionsArg = new MembersSetPermissionsArg(userSelectorArg, adminTier);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersSetPermissionsArg, membersSetPermissionsArg.toStringMultiline());
                    return membersSetPermissionsArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_role\" missing.");
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

    public MembersSetPermissionsArg(UserSelectorArg userSelectorArg, AdminTier adminTier) {
        if (userSelectorArg != null) {
            this.user = userSelectorArg;
            if (adminTier != null) {
                this.newRole = adminTier;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newRole' is null");
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public AdminTier getNewRole() {
        return this.newRole;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.user, this.newRole});
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
            com.dropbox.core.v2.team.MembersSetPermissionsArg r5 = (com.dropbox.core.p005v2.team.MembersSetPermissionsArg) r5
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.team.AdminTier r2 = r4.newRole
            com.dropbox.core.v2.team.AdminTier r5 = r5.newRole
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MembersSetPermissionsArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
