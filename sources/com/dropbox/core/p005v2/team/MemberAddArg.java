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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.MemberAddArg */
public class MemberAddArg {
    protected final Boolean isDirectoryRestricted;
    protected final String memberEmail;
    protected final String memberExternalId;
    protected final String memberGivenName;
    protected final String memberPersistentId;
    protected final String memberSurname;
    protected final AdminTier role;
    protected final boolean sendWelcomeEmail;

    /* renamed from: com.dropbox.core.v2.team.MemberAddArg$Builder */
    public static class Builder {
        protected Boolean isDirectoryRestricted;
        protected final String memberEmail;
        protected String memberExternalId;
        protected String memberGivenName;
        protected String memberPersistentId;
        protected String memberSurname;
        protected AdminTier role;
        protected boolean sendWelcomeEmail;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'memberEmail' is null");
            } else if (str.length() > 255) {
                throw new IllegalArgumentException("String 'memberEmail' is longer than 255");
            } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
                this.memberEmail = str;
                this.memberGivenName = null;
                this.memberSurname = null;
                this.memberExternalId = null;
                this.memberPersistentId = null;
                this.sendWelcomeEmail = true;
                this.role = AdminTier.MEMBER_ONLY;
                this.isDirectoryRestricted = null;
            } else {
                throw new IllegalArgumentException("String 'memberEmail' does not match pattern");
            }
        }

        public Builder withMemberGivenName(String str) {
            if (str != null) {
                if (str.length() > 100) {
                    throw new IllegalArgumentException("String 'memberGivenName' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str)) {
                    throw new IllegalArgumentException("String 'memberGivenName' does not match pattern");
                }
            }
            this.memberGivenName = str;
            return this;
        }

        public Builder withMemberSurname(String str) {
            if (str != null) {
                if (str.length() > 100) {
                    throw new IllegalArgumentException("String 'memberSurname' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str)) {
                    throw new IllegalArgumentException("String 'memberSurname' does not match pattern");
                }
            }
            this.memberSurname = str;
            return this;
        }

        public Builder withMemberExternalId(String str) {
            if (str == null || str.length() <= 64) {
                this.memberExternalId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'memberExternalId' is longer than 64");
        }

        public Builder withMemberPersistentId(String str) {
            this.memberPersistentId = str;
            return this;
        }

        public Builder withSendWelcomeEmail(Boolean bool) {
            if (bool != null) {
                this.sendWelcomeEmail = bool.booleanValue();
            } else {
                this.sendWelcomeEmail = true;
            }
            return this;
        }

        public Builder withRole(AdminTier adminTier) {
            if (adminTier != null) {
                this.role = adminTier;
            } else {
                this.role = AdminTier.MEMBER_ONLY;
            }
            return this;
        }

        public Builder withIsDirectoryRestricted(Boolean bool) {
            this.isDirectoryRestricted = bool;
            return this;
        }

        public MemberAddArg build() {
            MemberAddArg memberAddArg = new MemberAddArg(this.memberEmail, this.memberGivenName, this.memberSurname, this.memberExternalId, this.memberPersistentId, this.sendWelcomeEmail, this.role, this.isDirectoryRestricted);
            return memberAddArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MemberAddArg$Serializer */
    static class Serializer extends StructSerializer<MemberAddArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberAddArg memberAddArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("member_email");
            StoneSerializers.string().serialize(memberAddArg.memberEmail, jsonGenerator);
            if (memberAddArg.memberGivenName != null) {
                jsonGenerator.writeFieldName("member_given_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberAddArg.memberGivenName, jsonGenerator);
            }
            if (memberAddArg.memberSurname != null) {
                jsonGenerator.writeFieldName("member_surname");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberAddArg.memberSurname, jsonGenerator);
            }
            if (memberAddArg.memberExternalId != null) {
                jsonGenerator.writeFieldName("member_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberAddArg.memberExternalId, jsonGenerator);
            }
            if (memberAddArg.memberPersistentId != null) {
                jsonGenerator.writeFieldName("member_persistent_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberAddArg.memberPersistentId, jsonGenerator);
            }
            jsonGenerator.writeFieldName("send_welcome_email");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(memberAddArg.sendWelcomeEmail), jsonGenerator);
            jsonGenerator.writeFieldName("role");
            Serializer.INSTANCE.serialize(memberAddArg.role, jsonGenerator);
            if (memberAddArg.isDirectoryRestricted != null) {
                jsonGenerator.writeFieldName("is_directory_restricted");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(memberAddArg.isDirectoryRestricted, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberAddArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                Boolean bool = null;
                AdminTier adminTier = AdminTier.MEMBER_ONLY;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("member_email".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member_given_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_surname".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_external_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_persistent_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("send_welcome_email".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("role".equals(currentName)) {
                        adminTier = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("is_directory_restricted".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    MemberAddArg memberAddArg = new MemberAddArg(str2, str3, str4, str5, str6, valueOf.booleanValue(), adminTier, bool);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberAddArg, memberAddArg.toStringMultiline());
                    return memberAddArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"member_email\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberAddArg(String str, String str2, String str3, String str4, String str5, boolean z, AdminTier adminTier, Boolean bool) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'memberEmail' is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String 'memberEmail' is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            this.memberEmail = str;
            if (str2 != null) {
                if (str2.length() > 100) {
                    throw new IllegalArgumentException("String 'memberGivenName' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str2)) {
                    throw new IllegalArgumentException("String 'memberGivenName' does not match pattern");
                }
            }
            this.memberGivenName = str2;
            if (str3 != null) {
                if (str3.length() > 100) {
                    throw new IllegalArgumentException("String 'memberSurname' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str3)) {
                    throw new IllegalArgumentException("String 'memberSurname' does not match pattern");
                }
            }
            this.memberSurname = str3;
            if (str4 == null || str4.length() <= 64) {
                this.memberExternalId = str4;
                this.memberPersistentId = str5;
                this.sendWelcomeEmail = z;
                if (adminTier != null) {
                    this.role = adminTier;
                    this.isDirectoryRestricted = bool;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'role' is null");
            }
            throw new IllegalArgumentException("String 'memberExternalId' is longer than 64");
        } else {
            throw new IllegalArgumentException("String 'memberEmail' does not match pattern");
        }
    }

    public MemberAddArg(String str) {
        this(str, null, null, null, null, true, AdminTier.MEMBER_ONLY, null);
    }

    public String getMemberEmail() {
        return this.memberEmail;
    }

    public String getMemberGivenName() {
        return this.memberGivenName;
    }

    public String getMemberSurname() {
        return this.memberSurname;
    }

    public String getMemberExternalId() {
        return this.memberExternalId;
    }

    public String getMemberPersistentId() {
        return this.memberPersistentId;
    }

    public boolean getSendWelcomeEmail() {
        return this.sendWelcomeEmail;
    }

    public AdminTier getRole() {
        return this.role;
    }

    public Boolean getIsDirectoryRestricted() {
        return this.isDirectoryRestricted;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.memberEmail, this.memberGivenName, this.memberSurname, this.memberExternalId, this.memberPersistentId, Boolean.valueOf(this.sendWelcomeEmail), this.role, this.isDirectoryRestricted});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x007a, code lost:
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
            com.dropbox.core.v2.team.MemberAddArg r5 = (com.dropbox.core.p005v2.team.MemberAddArg) r5
            java.lang.String r2 = r4.memberEmail
            java.lang.String r3 = r5.memberEmail
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0024:
            java.lang.String r2 = r4.memberGivenName
            java.lang.String r3 = r5.memberGivenName
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x007d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0032:
            java.lang.String r2 = r4.memberSurname
            java.lang.String r3 = r5.memberSurname
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x007d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x0040:
            java.lang.String r2 = r4.memberExternalId
            java.lang.String r3 = r5.memberExternalId
            if (r2 == r3) goto L_0x004e
            if (r2 == 0) goto L_0x007d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x004e:
            java.lang.String r2 = r4.memberPersistentId
            java.lang.String r3 = r5.memberPersistentId
            if (r2 == r3) goto L_0x005c
            if (r2 == 0) goto L_0x007d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x005c:
            boolean r2 = r4.sendWelcomeEmail
            boolean r3 = r5.sendWelcomeEmail
            if (r2 != r3) goto L_0x007d
            com.dropbox.core.v2.team.AdminTier r2 = r4.role
            com.dropbox.core.v2.team.AdminTier r3 = r5.role
            if (r2 == r3) goto L_0x006e
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x007d
        L_0x006e:
            java.lang.Boolean r2 = r4.isDirectoryRestricted
            java.lang.Boolean r5 = r5.isDirectoryRestricted
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MemberAddArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
