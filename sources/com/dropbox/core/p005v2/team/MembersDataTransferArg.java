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

/* renamed from: com.dropbox.core.v2.team.MembersDataTransferArg */
class MembersDataTransferArg extends MembersDeactivateBaseArg {
    protected final UserSelectorArg transferAdminId;
    protected final UserSelectorArg transferDestId;

    /* renamed from: com.dropbox.core.v2.team.MembersDataTransferArg$Serializer */
    static class Serializer extends StructSerializer<MembersDataTransferArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersDataTransferArg membersDataTransferArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersDataTransferArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("transfer_dest_id");
            Serializer.INSTANCE.serialize(membersDataTransferArg.transferDestId, jsonGenerator);
            jsonGenerator.writeFieldName("transfer_admin_id");
            Serializer.INSTANCE.serialize(membersDataTransferArg.transferAdminId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersDataTransferArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserSelectorArg userSelectorArg = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                UserSelectorArg userSelectorArg2 = null;
                UserSelectorArg userSelectorArg3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("transfer_dest_id".equals(currentName)) {
                        userSelectorArg2 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("transfer_admin_id".equals(currentName)) {
                        userSelectorArg3 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
                } else if (userSelectorArg2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"transfer_dest_id\" missing.");
                } else if (userSelectorArg3 != null) {
                    MembersDataTransferArg membersDataTransferArg = new MembersDataTransferArg(userSelectorArg, userSelectorArg2, userSelectorArg3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersDataTransferArg, membersDataTransferArg.toStringMultiline());
                    return membersDataTransferArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"transfer_admin_id\" missing.");
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

    public MembersDataTransferArg(UserSelectorArg userSelectorArg, UserSelectorArg userSelectorArg2, UserSelectorArg userSelectorArg3) {
        super(userSelectorArg);
        if (userSelectorArg2 != null) {
            this.transferDestId = userSelectorArg2;
            if (userSelectorArg3 != null) {
                this.transferAdminId = userSelectorArg3;
                return;
            }
            throw new IllegalArgumentException("Required value for 'transferAdminId' is null");
        }
        throw new IllegalArgumentException("Required value for 'transferDestId' is null");
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public UserSelectorArg getTransferDestId() {
        return this.transferDestId;
    }

    public UserSelectorArg getTransferAdminId() {
        return this.transferAdminId;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.transferDestId, this.transferAdminId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.team.MembersDataTransferArg r5 = (com.dropbox.core.p005v2.team.MembersDataTransferArg) r5
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0028:
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.transferDestId
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.transferDestId
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0034:
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.transferAdminId
            com.dropbox.core.v2.team.UserSelectorArg r5 = r5.transferAdminId
            if (r2 == r5) goto L_0x0042
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MembersDataTransferArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
