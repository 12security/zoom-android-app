package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.MembersRemoveArg */
class MembersRemoveArg extends MembersDeactivateArg {
    protected final boolean keepAccount;
    protected final UserSelectorArg transferAdminId;
    protected final UserSelectorArg transferDestId;

    /* renamed from: com.dropbox.core.v2.team.MembersRemoveArg$Builder */
    public static class Builder {
        protected boolean keepAccount;
        protected UserSelectorArg transferAdminId;
        protected UserSelectorArg transferDestId;
        protected final UserSelectorArg user;
        protected boolean wipeData;

        protected Builder(UserSelectorArg userSelectorArg) {
            if (userSelectorArg != null) {
                this.user = userSelectorArg;
                this.wipeData = true;
                this.transferDestId = null;
                this.transferAdminId = null;
                this.keepAccount = false;
                return;
            }
            throw new IllegalArgumentException("Required value for 'user' is null");
        }

        public Builder withWipeData(Boolean bool) {
            if (bool != null) {
                this.wipeData = bool.booleanValue();
            } else {
                this.wipeData = true;
            }
            return this;
        }

        public Builder withTransferDestId(UserSelectorArg userSelectorArg) {
            this.transferDestId = userSelectorArg;
            return this;
        }

        public Builder withTransferAdminId(UserSelectorArg userSelectorArg) {
            this.transferAdminId = userSelectorArg;
            return this;
        }

        public Builder withKeepAccount(Boolean bool) {
            if (bool != null) {
                this.keepAccount = bool.booleanValue();
            } else {
                this.keepAccount = false;
            }
            return this;
        }

        public MembersRemoveArg build() {
            MembersRemoveArg membersRemoveArg = new MembersRemoveArg(this.user, this.wipeData, this.transferDestId, this.transferAdminId, this.keepAccount);
            return membersRemoveArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersRemoveArg$Serializer */
    static class Serializer extends StructSerializer<MembersRemoveArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersRemoveArg membersRemoveArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersRemoveArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("wipe_data");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membersRemoveArg.wipeData), jsonGenerator);
            if (membersRemoveArg.transferDestId != null) {
                jsonGenerator.writeFieldName("transfer_dest_id");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(membersRemoveArg.transferDestId, jsonGenerator);
            }
            if (membersRemoveArg.transferAdminId != null) {
                jsonGenerator.writeFieldName("transfer_admin_id");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(membersRemoveArg.transferAdminId, jsonGenerator);
            }
            jsonGenerator.writeFieldName("keep_account");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membersRemoveArg.keepAccount), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersRemoveArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                Boolean valueOf2 = Boolean.valueOf(false);
                UserSelectorArg userSelectorArg = null;
                UserSelectorArg userSelectorArg2 = null;
                UserSelectorArg userSelectorArg3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("wipe_data".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("transfer_dest_id".equals(currentName)) {
                        userSelectorArg2 = (UserSelectorArg) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("transfer_admin_id".equals(currentName)) {
                        userSelectorArg3 = (UserSelectorArg) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("keep_account".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg != null) {
                    MembersRemoveArg membersRemoveArg = new MembersRemoveArg(userSelectorArg, valueOf.booleanValue(), userSelectorArg2, userSelectorArg3, valueOf2.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersRemoveArg, membersRemoveArg.toStringMultiline());
                    return membersRemoveArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembersRemoveArg(UserSelectorArg userSelectorArg, boolean z, UserSelectorArg userSelectorArg2, UserSelectorArg userSelectorArg3, boolean z2) {
        super(userSelectorArg, z);
        this.transferDestId = userSelectorArg2;
        this.transferAdminId = userSelectorArg3;
        this.keepAccount = z2;
    }

    public MembersRemoveArg(UserSelectorArg userSelectorArg) {
        this(userSelectorArg, true, null, null, false);
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public boolean getWipeData() {
        return this.wipeData;
    }

    public UserSelectorArg getTransferDestId() {
        return this.transferDestId;
    }

    public UserSelectorArg getTransferAdminId() {
        return this.transferAdminId;
    }

    public boolean getKeepAccount() {
        return this.keepAccount;
    }

    public static Builder newBuilder(UserSelectorArg userSelectorArg) {
        return new Builder(userSelectorArg);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.transferDestId, this.transferAdminId, Boolean.valueOf(this.keepAccount)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
        if (r2.equals(r3) == false) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
        if (r4.keepAccount != r5.keepAccount) goto L_0x0051;
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
            if (r2 == 0) goto L_0x0053
            com.dropbox.core.v2.team.MembersRemoveArg r5 = (com.dropbox.core.p005v2.team.MembersRemoveArg) r5
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0028:
            boolean r2 = r4.wipeData
            boolean r3 = r5.wipeData
            if (r2 != r3) goto L_0x0051
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.transferDestId
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.transferDestId
            if (r2 == r3) goto L_0x003c
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x003c:
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.transferAdminId
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.transferAdminId
            if (r2 == r3) goto L_0x004a
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x004a:
            boolean r2 = r4.keepAccount
            boolean r5 = r5.keepAccount
            if (r2 != r5) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r0 = 0
        L_0x0052:
            return r0
        L_0x0053:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MembersRemoveArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
