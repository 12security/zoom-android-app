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

/* renamed from: com.dropbox.core.v2.users.Account */
public class Account {
    protected final String accountId;
    protected final boolean disabled;
    protected final String email;
    protected final boolean emailVerified;
    protected final Name name;
    protected final String profilePhotoUrl;

    /* renamed from: com.dropbox.core.v2.users.Account$Serializer */
    private static class Serializer extends StructSerializer<Account> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(Account account, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_id");
            StoneSerializers.string().serialize(account.accountId, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.serialize(account.name, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(account.email, jsonGenerator);
            jsonGenerator.writeFieldName("email_verified");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(account.emailVerified), jsonGenerator);
            jsonGenerator.writeFieldName("disabled");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(account.disabled), jsonGenerator);
            if (account.profilePhotoUrl != null) {
                jsonGenerator.writeFieldName("profile_photo_url");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(account.profilePhotoUrl, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public Account deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str2 = null;
                Name name = null;
                String str3 = null;
                String str4 = null;
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
                    } else if ("profile_photo_url".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
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
                } else if (bool2 != null) {
                    Account account = new Account(str2, name, str3, bool.booleanValue(), bool2.booleanValue(), str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(account, account.toStringMultiline());
                    return account;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"disabled\" missing.");
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

    public Account(String str, Name name2, String str2, boolean z, boolean z2, String str3) {
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
                    this.profilePhotoUrl = str3;
                    this.disabled = z2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'email' is null");
            }
            throw new IllegalArgumentException("Required value for 'name' is null");
        } else {
            throw new IllegalArgumentException("String 'accountId' is longer than 40");
        }
    }

    public Account(String str, Name name2, String str2, boolean z, boolean z2) {
        this(str, name2, str2, z, z2, null);
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

    public String getProfilePhotoUrl() {
        return this.profilePhotoUrl;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accountId, this.name, this.email, Boolean.valueOf(this.emailVerified), this.profilePhotoUrl, Boolean.valueOf(this.disabled)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0054, code lost:
        if (r2.equals(r5) == false) goto L_0x0057;
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
            if (r2 == 0) goto L_0x0059
            com.dropbox.core.v2.users.Account r5 = (com.dropbox.core.p005v2.users.Account) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x0024:
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x0030:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0057
        L_0x003c:
            boolean r2 = r4.emailVerified
            boolean r3 = r5.emailVerified
            if (r2 != r3) goto L_0x0057
            boolean r2 = r4.disabled
            boolean r3 = r5.disabled
            if (r2 != r3) goto L_0x0057
            java.lang.String r2 = r4.profilePhotoUrl
            java.lang.String r5 = r5.profilePhotoUrl
            if (r2 == r5) goto L_0x0058
            if (r2 == 0) goto L_0x0057
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0057
            goto L_0x0058
        L_0x0057:
            r0 = 0
        L_0x0058:
            return r0
        L_0x0059:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.Account.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
