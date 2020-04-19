package com.dropbox.core.p005v2.users;

import com.dropbox.core.p005v2.common.RootInfo;
import com.dropbox.core.p005v2.userscommon.AccountType;
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

/* renamed from: com.dropbox.core.v2.users.FullAccount */
public class FullAccount extends Account {
    protected final AccountType accountType;
    protected final String country;
    protected final boolean isPaired;
    protected final String locale;
    protected final String referralLink;
    protected final RootInfo rootInfo;
    protected final FullTeam team;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.users.FullAccount$Builder */
    public static class Builder {
        protected final String accountId;
        protected final AccountType accountType;
        protected String country;
        protected final boolean disabled;
        protected final String email;
        protected final boolean emailVerified;
        protected final boolean isPaired;
        protected final String locale;
        protected final Name name;
        protected String profilePhotoUrl;
        protected final String referralLink;
        protected final RootInfo rootInfo;
        protected FullTeam team;
        protected String teamMemberId;

        protected Builder(String str, Name name2, String str2, boolean z, boolean z2, String str3, String str4, boolean z3, AccountType accountType2, RootInfo rootInfo2) {
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
                        if (str3 == null) {
                            throw new IllegalArgumentException("Required value for 'locale' is null");
                        } else if (str3.length() >= 2) {
                            this.locale = str3;
                            if (str4 != null) {
                                this.referralLink = str4;
                                this.isPaired = z3;
                                if (accountType2 != null) {
                                    this.accountType = accountType2;
                                    if (rootInfo2 != null) {
                                        this.rootInfo = rootInfo2;
                                        this.profilePhotoUrl = null;
                                        this.country = null;
                                        this.team = null;
                                        this.teamMemberId = null;
                                        return;
                                    }
                                    throw new IllegalArgumentException("Required value for 'rootInfo' is null");
                                }
                                throw new IllegalArgumentException("Required value for 'accountType' is null");
                            }
                            throw new IllegalArgumentException("Required value for 'referralLink' is null");
                        } else {
                            throw new IllegalArgumentException("String 'locale' is shorter than 2");
                        }
                    } else {
                        throw new IllegalArgumentException("Required value for 'email' is null");
                    }
                } else {
                    throw new IllegalArgumentException("Required value for 'name' is null");
                }
            } else {
                throw new IllegalArgumentException("String 'accountId' is longer than 40");
            }
        }

        public Builder withProfilePhotoUrl(String str) {
            this.profilePhotoUrl = str;
            return this;
        }

        public Builder withCountry(String str) {
            if (str != null) {
                if (str.length() < 2) {
                    throw new IllegalArgumentException("String 'country' is shorter than 2");
                } else if (str.length() > 2) {
                    throw new IllegalArgumentException("String 'country' is longer than 2");
                }
            }
            this.country = str;
            return this;
        }

        public Builder withTeam(FullTeam fullTeam) {
            this.team = fullTeam;
            return this;
        }

        public Builder withTeamMemberId(String str) {
            this.teamMemberId = str;
            return this;
        }

        public FullAccount build() {
            FullAccount fullAccount = new FullAccount(this.accountId, this.name, this.email, this.emailVerified, this.disabled, this.locale, this.referralLink, this.isPaired, this.accountType, this.rootInfo, this.profilePhotoUrl, this.country, this.team, this.teamMemberId);
            return fullAccount;
        }
    }

    /* renamed from: com.dropbox.core.v2.users.FullAccount$Serializer */
    static class Serializer extends StructSerializer<FullAccount> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FullAccount fullAccount, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_id");
            StoneSerializers.string().serialize(fullAccount.accountId, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.serialize(fullAccount.name, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(fullAccount.email, jsonGenerator);
            jsonGenerator.writeFieldName("email_verified");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(fullAccount.emailVerified), jsonGenerator);
            jsonGenerator.writeFieldName("disabled");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(fullAccount.disabled), jsonGenerator);
            jsonGenerator.writeFieldName(OAuth.LOCALE);
            StoneSerializers.string().serialize(fullAccount.locale, jsonGenerator);
            jsonGenerator.writeFieldName("referral_link");
            StoneSerializers.string().serialize(fullAccount.referralLink, jsonGenerator);
            jsonGenerator.writeFieldName("is_paired");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(fullAccount.isPaired), jsonGenerator);
            jsonGenerator.writeFieldName("account_type");
            com.dropbox.core.p005v2.userscommon.AccountType.Serializer.INSTANCE.serialize(fullAccount.accountType, jsonGenerator);
            jsonGenerator.writeFieldName("root_info");
            com.dropbox.core.p005v2.common.RootInfo.Serializer.INSTANCE.serialize(fullAccount.rootInfo, jsonGenerator);
            if (fullAccount.profilePhotoUrl != null) {
                jsonGenerator.writeFieldName("profile_photo_url");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fullAccount.profilePhotoUrl, jsonGenerator);
            }
            if (fullAccount.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fullAccount.country, jsonGenerator);
            }
            if (fullAccount.team != null) {
                jsonGenerator.writeFieldName("team");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(fullAccount.team, jsonGenerator);
            }
            if (fullAccount.teamMemberId != null) {
                jsonGenerator.writeFieldName("team_member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fullAccount.teamMemberId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FullAccount deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
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
                AccountType accountType = null;
                RootInfo rootInfo = null;
                String str6 = null;
                String str7 = null;
                FullTeam fullTeam = null;
                String str8 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("name".equals(currentName)) {
                        name = (Name) com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("email".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("email_verified".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("disabled".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if (OAuth.LOCALE.equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("referral_link".equals(currentName)) {
                        str5 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("is_paired".equals(currentName)) {
                        bool3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("account_type".equals(currentName)) {
                        accountType = com.dropbox.core.p005v2.userscommon.AccountType.Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("root_info".equals(currentName)) {
                        rootInfo = (RootInfo) com.dropbox.core.p005v2.common.RootInfo.Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("profile_photo_url".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("country".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("team".equals(currentName)) {
                        fullTeam = (FullTeam) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("team_member_id".equals(currentName)) {
                        str8 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"account_id\" missing.");
                } else if (name == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"email\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"email_verified\" missing.");
                } else if (bool2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"disabled\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"locale\" missing.");
                } else if (str5 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"referral_link\" missing.");
                } else if (bool3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"is_paired\" missing.");
                } else if (accountType == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"account_type\" missing.");
                } else if (rootInfo != null) {
                    boolean booleanValue = bool.booleanValue();
                    boolean booleanValue2 = bool2.booleanValue();
                    FullAccount fullAccount = r3;
                    FullAccount fullAccount2 = new FullAccount(str2, name, str3, booleanValue, booleanValue2, str4, str5, bool3.booleanValue(), accountType, rootInfo, str6, str7, fullTeam, str8);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(fullAccount, fullAccount.toStringMultiline());
                    return fullAccount;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"root_info\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser2, sb.toString());
            }
        }
    }

    public FullAccount(String str, Name name, String str2, boolean z, boolean z2, String str3, String str4, boolean z3, AccountType accountType2, RootInfo rootInfo2, String str5, String str6, FullTeam fullTeam, String str7) {
        String str8 = str3;
        String str9 = str4;
        AccountType accountType3 = accountType2;
        RootInfo rootInfo3 = rootInfo2;
        String str10 = str6;
        super(str, name, str2, z, z2, str5);
        if (str10 != null) {
            if (str6.length() < 2) {
                throw new IllegalArgumentException("String 'country' is shorter than 2");
            } else if (str6.length() > 2) {
                throw new IllegalArgumentException("String 'country' is longer than 2");
            }
        }
        this.country = str10;
        if (str8 == null) {
            throw new IllegalArgumentException("Required value for 'locale' is null");
        } else if (str3.length() >= 2) {
            this.locale = str8;
            if (str9 != null) {
                this.referralLink = str9;
                this.team = fullTeam;
                this.teamMemberId = str7;
                this.isPaired = z3;
                if (accountType3 != null) {
                    this.accountType = accountType3;
                    if (rootInfo3 != null) {
                        this.rootInfo = rootInfo3;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'rootInfo' is null");
                }
                throw new IllegalArgumentException("Required value for 'accountType' is null");
            }
            throw new IllegalArgumentException("Required value for 'referralLink' is null");
        } else {
            throw new IllegalArgumentException("String 'locale' is shorter than 2");
        }
    }

    public FullAccount(String str, Name name, String str2, boolean z, boolean z2, String str3, String str4, boolean z3, AccountType accountType2, RootInfo rootInfo2) {
        this(str, name, str2, z, z2, str3, str4, z3, accountType2, rootInfo2, null, null, null, null);
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

    public String getLocale() {
        return this.locale;
    }

    public String getReferralLink() {
        return this.referralLink;
    }

    public boolean getIsPaired() {
        return this.isPaired;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public RootInfo getRootInfo() {
        return this.rootInfo;
    }

    public String getProfilePhotoUrl() {
        return this.profilePhotoUrl;
    }

    public String getCountry() {
        return this.country;
    }

    public FullTeam getTeam() {
        return this.team;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public static Builder newBuilder(String str, Name name, String str2, boolean z, boolean z2, String str3, String str4, boolean z3, AccountType accountType2, RootInfo rootInfo2) {
        Builder builder = new Builder(str, name, str2, z, z2, str3, str4, z3, accountType2, rootInfo2);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.country, this.locale, this.referralLink, this.team, this.teamMemberId, Boolean.valueOf(this.isPaired), this.accountType, this.rootInfo});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00c6, code lost:
        if (r2.equals(r5) == false) goto L_0x00c9;
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
            if (r2 == 0) goto L_0x00cb
            com.dropbox.core.v2.users.FullAccount r5 = (com.dropbox.core.p005v2.users.FullAccount) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x0028:
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x0038:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0048
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x0048:
            boolean r2 = r4.emailVerified
            boolean r3 = r5.emailVerified
            if (r2 != r3) goto L_0x00c9
            boolean r2 = r4.disabled
            boolean r3 = r5.disabled
            if (r2 != r3) goto L_0x00c9
            java.lang.String r2 = r4.locale
            java.lang.String r3 = r5.locale
            if (r2 == r3) goto L_0x0060
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x0060:
            java.lang.String r2 = r4.referralLink
            java.lang.String r3 = r5.referralLink
            if (r2 == r3) goto L_0x006c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x006c:
            boolean r2 = r4.isPaired
            boolean r3 = r5.isPaired
            if (r2 != r3) goto L_0x00c9
            com.dropbox.core.v2.userscommon.AccountType r2 = r4.accountType
            com.dropbox.core.v2.userscommon.AccountType r3 = r5.accountType
            if (r2 == r3) goto L_0x007e
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x007e:
            com.dropbox.core.v2.common.RootInfo r2 = r4.rootInfo
            com.dropbox.core.v2.common.RootInfo r3 = r5.rootInfo
            if (r2 == r3) goto L_0x008a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x008a:
            java.lang.String r2 = r4.profilePhotoUrl
            java.lang.String r3 = r5.profilePhotoUrl
            if (r2 == r3) goto L_0x009e
            java.lang.String r2 = r4.profilePhotoUrl
            if (r2 == 0) goto L_0x00c9
            java.lang.String r2 = r4.profilePhotoUrl
            java.lang.String r3 = r5.profilePhotoUrl
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x009e:
            java.lang.String r2 = r4.country
            java.lang.String r3 = r5.country
            if (r2 == r3) goto L_0x00ac
            if (r2 == 0) goto L_0x00c9
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x00ac:
            com.dropbox.core.v2.users.FullTeam r2 = r4.team
            com.dropbox.core.v2.users.FullTeam r3 = r5.team
            if (r2 == r3) goto L_0x00ba
            if (r2 == 0) goto L_0x00c9
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00c9
        L_0x00ba:
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r5 = r5.teamMemberId
            if (r2 == r5) goto L_0x00ca
            if (r2 == 0) goto L_0x00c9
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00c9
            goto L_0x00ca
        L_0x00c9:
            r0 = 0
        L_0x00ca:
            return r0
        L_0x00cb:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.FullAccount.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
