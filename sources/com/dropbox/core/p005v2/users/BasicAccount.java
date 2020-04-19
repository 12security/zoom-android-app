package com.dropbox.core.p005v2.users;

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

/* renamed from: com.dropbox.core.v2.users.BasicAccount */
public class BasicAccount extends Account {
    protected final boolean isTeammate;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.users.BasicAccount$Builder */
    public static class Builder {
        protected final String accountId;
        protected final boolean disabled;
        protected final String email;
        protected final boolean emailVerified;
        protected final boolean isTeammate;
        protected final Name name;
        protected String profilePhotoUrl;
        protected String teamMemberId;

        protected Builder(String str, Name name2, String str2, boolean z, boolean z2, boolean z3) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'accountId' is null");
            } else if (str.length() < 40) {
                throw new IllegalArgumentException("String 'accountId' is shorter than 40");
            } else if (str.length() <= 40) {
                this.accountId = str;
                if (name2 != null) {
                    this.name = name2;
                    if (str2 != null) {
                        this.email = str2;
                        this.emailVerified = z;
                        this.disabled = z2;
                        this.isTeammate = z3;
                        this.profilePhotoUrl = null;
                        this.teamMemberId = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'email' is null");
                }
                throw new IllegalArgumentException("Required value for 'name' is null");
            } else {
                throw new IllegalArgumentException("String 'accountId' is longer than 40");
            }
        }

        public Builder withProfilePhotoUrl(String str) {
            this.profilePhotoUrl = str;
            return this;
        }

        public Builder withTeamMemberId(String str) {
            this.teamMemberId = str;
            return this;
        }

        public BasicAccount build() {
            BasicAccount basicAccount = new BasicAccount(this.accountId, this.name, this.email, this.emailVerified, this.disabled, this.isTeammate, this.profilePhotoUrl, this.teamMemberId);
            return basicAccount;
        }
    }

    /* renamed from: com.dropbox.core.v2.users.BasicAccount$Serializer */
    public static class Serializer extends StructSerializer<BasicAccount> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(BasicAccount basicAccount, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_id");
            StoneSerializers.string().serialize(basicAccount.accountId, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.serialize(basicAccount.name, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(basicAccount.email, jsonGenerator);
            jsonGenerator.writeFieldName("email_verified");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(basicAccount.emailVerified), jsonGenerator);
            jsonGenerator.writeFieldName("disabled");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(basicAccount.disabled), jsonGenerator);
            jsonGenerator.writeFieldName("is_teammate");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(basicAccount.isTeammate), jsonGenerator);
            if (basicAccount.profilePhotoUrl != null) {
                jsonGenerator.writeFieldName("profile_photo_url");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(basicAccount.profilePhotoUrl, jsonGenerator);
            }
            if (basicAccount.teamMemberId != null) {
                jsonGenerator.writeFieldName("team_member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(basicAccount.teamMemberId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public BasicAccount deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool2 = null;
                Boolean bool3 = null;
                String str2 = null;
                Name name = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("name".equals(currentName)) {
                        name = (Name) com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("email".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("email_verified".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("disabled".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("is_teammate".equals(currentName)) {
                        bool3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("profile_photo_url".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"account_id\" missing.");
                } else if (name == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"email\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"email_verified\" missing.");
                } else if (bool2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"disabled\" missing.");
                } else if (bool3 != null) {
                    BasicAccount basicAccount = new BasicAccount(str2, name, str3, bool.booleanValue(), bool2.booleanValue(), bool3.booleanValue(), str4, str5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(basicAccount, basicAccount.toStringMultiline());
                    return basicAccount;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"is_teammate\" missing.");
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

    public BasicAccount(String str, Name name, String str2, boolean z, boolean z2, boolean z3, String str3, String str4) {
        super(str, name, str2, z, z2, str3);
        this.isTeammate = z3;
        this.teamMemberId = str4;
    }

    public BasicAccount(String str, Name name, String str2, boolean z, boolean z2, boolean z3) {
        this(str, name, str2, z, z2, z3, null, null);
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Name getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean getEmailVerified() {
        return this.emailVerified;
    }

    public boolean getDisabled() {
        return this.disabled;
    }

    public boolean getIsTeammate() {
        return this.isTeammate;
    }

    public String getProfilePhotoUrl() {
        return this.profilePhotoUrl;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public static Builder newBuilder(String str, Name name, String str2, boolean z, boolean z2, boolean z3) {
        Builder builder = new Builder(str, name, str2, z, z2, z3);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Boolean.valueOf(this.isTeammate), this.teamMemberId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007a, code lost:
        if (r2.equals(r5) == false) goto L_0x007d;
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
            if (r2 == 0) goto L_0x007f
            com.dropbox.core.v2.users.BasicAccount r5 = (com.dropbox.core.p005v2.users.BasicAccount) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0028:
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0038:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0048
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0048:
            boolean r2 = r4.emailVerified
            boolean r3 = r5.emailVerified
            if (r2 != r3) goto L_0x007d
            boolean r2 = r4.disabled
            boolean r3 = r5.disabled
            if (r2 != r3) goto L_0x007d
            boolean r2 = r4.isTeammate
            boolean r3 = r5.isTeammate
            if (r2 != r3) goto L_0x007d
            java.lang.String r2 = r4.profilePhotoUrl
            java.lang.String r3 = r5.profilePhotoUrl
            if (r2 == r3) goto L_0x006e
            java.lang.String r2 = r4.profilePhotoUrl
            if (r2 == 0) goto L_0x007d
            java.lang.String r2 = r4.profilePhotoUrl
            java.lang.String r3 = r5.profilePhotoUrl
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x006e:
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r5 = r5.teamMemberId
            if (r2 == r5) goto L_0x007e
            if (r2 == 0) goto L_0x007d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x007d
            goto L_0x007e
        L_0x007d:
            r0 = 0
        L_0x007e:
            return r0
        L_0x007f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.BasicAccount.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
