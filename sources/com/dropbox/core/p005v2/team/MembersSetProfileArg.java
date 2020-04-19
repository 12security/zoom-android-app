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

/* renamed from: com.dropbox.core.v2.team.MembersSetProfileArg */
class MembersSetProfileArg {
    protected final String newEmail;
    protected final String newExternalId;
    protected final String newGivenName;
    protected final Boolean newIsDirectoryRestricted;
    protected final String newPersistentId;
    protected final String newSurname;
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.MembersSetProfileArg$Builder */
    public static class Builder {
        protected String newEmail;
        protected String newExternalId;
        protected String newGivenName;
        protected Boolean newIsDirectoryRestricted;
        protected String newPersistentId;
        protected String newSurname;
        protected final UserSelectorArg user;

        protected Builder(UserSelectorArg userSelectorArg) {
            if (userSelectorArg != null) {
                this.user = userSelectorArg;
                this.newEmail = null;
                this.newExternalId = null;
                this.newGivenName = null;
                this.newSurname = null;
                this.newPersistentId = null;
                this.newIsDirectoryRestricted = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'user' is null");
        }

        public Builder withNewEmail(String str) {
            if (str != null) {
                if (str.length() > 255) {
                    throw new IllegalArgumentException("String 'newEmail' is longer than 255");
                } else if (!Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
                    throw new IllegalArgumentException("String 'newEmail' does not match pattern");
                }
            }
            this.newEmail = str;
            return this;
        }

        public Builder withNewExternalId(String str) {
            if (str == null || str.length() <= 64) {
                this.newExternalId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'newExternalId' is longer than 64");
        }

        public Builder withNewGivenName(String str) {
            if (str != null) {
                if (str.length() > 100) {
                    throw new IllegalArgumentException("String 'newGivenName' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str)) {
                    throw new IllegalArgumentException("String 'newGivenName' does not match pattern");
                }
            }
            this.newGivenName = str;
            return this;
        }

        public Builder withNewSurname(String str) {
            if (str != null) {
                if (str.length() > 100) {
                    throw new IllegalArgumentException("String 'newSurname' is longer than 100");
                } else if (!Pattern.matches("[^/:?*<>\"|]*", str)) {
                    throw new IllegalArgumentException("String 'newSurname' does not match pattern");
                }
            }
            this.newSurname = str;
            return this;
        }

        public Builder withNewPersistentId(String str) {
            this.newPersistentId = str;
            return this;
        }

        public Builder withNewIsDirectoryRestricted(Boolean bool) {
            this.newIsDirectoryRestricted = bool;
            return this;
        }

        public MembersSetProfileArg build() {
            MembersSetProfileArg membersSetProfileArg = new MembersSetProfileArg(this.user, this.newEmail, this.newExternalId, this.newGivenName, this.newSurname, this.newPersistentId, this.newIsDirectoryRestricted);
            return membersSetProfileArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersSetProfileArg$Serializer */
    static class Serializer extends StructSerializer<MembersSetProfileArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersSetProfileArg membersSetProfileArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersSetProfileArg.user, jsonGenerator);
            if (membersSetProfileArg.newEmail != null) {
                jsonGenerator.writeFieldName("new_email");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membersSetProfileArg.newEmail, jsonGenerator);
            }
            if (membersSetProfileArg.newExternalId != null) {
                jsonGenerator.writeFieldName("new_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membersSetProfileArg.newExternalId, jsonGenerator);
            }
            if (membersSetProfileArg.newGivenName != null) {
                jsonGenerator.writeFieldName("new_given_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membersSetProfileArg.newGivenName, jsonGenerator);
            }
            if (membersSetProfileArg.newSurname != null) {
                jsonGenerator.writeFieldName("new_surname");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membersSetProfileArg.newSurname, jsonGenerator);
            }
            if (membersSetProfileArg.newPersistentId != null) {
                jsonGenerator.writeFieldName("new_persistent_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(membersSetProfileArg.newPersistentId, jsonGenerator);
            }
            if (membersSetProfileArg.newIsDirectoryRestricted != null) {
                jsonGenerator.writeFieldName("new_is_directory_restricted");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(membersSetProfileArg.newIsDirectoryRestricted, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersSetProfileArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                UserSelectorArg userSelectorArg = null;
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("new_email".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_external_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_given_name".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_surname".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_persistent_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_is_directory_restricted".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg != null) {
                    MembersSetProfileArg membersSetProfileArg = new MembersSetProfileArg(userSelectorArg, str2, str3, str4, str5, str6, bool);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersSetProfileArg, membersSetProfileArg.toStringMultiline());
                    return membersSetProfileArg;
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

    public MembersSetProfileArg(UserSelectorArg userSelectorArg, String str, String str2, String str3, String str4, String str5, Boolean bool) {
        if (userSelectorArg != null) {
            this.user = userSelectorArg;
            if (str != null) {
                if (str.length() > 255) {
                    throw new IllegalArgumentException("String 'newEmail' is longer than 255");
                } else if (!Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
                    throw new IllegalArgumentException("String 'newEmail' does not match pattern");
                }
            }
            this.newEmail = str;
            if (str2 == null || str2.length() <= 64) {
                this.newExternalId = str2;
                if (str3 != null) {
                    if (str3.length() > 100) {
                        throw new IllegalArgumentException("String 'newGivenName' is longer than 100");
                    } else if (!Pattern.matches("[^/:?*<>\"|]*", str3)) {
                        throw new IllegalArgumentException("String 'newGivenName' does not match pattern");
                    }
                }
                this.newGivenName = str3;
                if (str4 != null) {
                    if (str4.length() > 100) {
                        throw new IllegalArgumentException("String 'newSurname' is longer than 100");
                    } else if (!Pattern.matches("[^/:?*<>\"|]*", str4)) {
                        throw new IllegalArgumentException("String 'newSurname' does not match pattern");
                    }
                }
                this.newSurname = str4;
                this.newPersistentId = str5;
                this.newIsDirectoryRestricted = bool;
                return;
            }
            throw new IllegalArgumentException("String 'newExternalId' is longer than 64");
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public MembersSetProfileArg(UserSelectorArg userSelectorArg) {
        this(userSelectorArg, null, null, null, null, null, null);
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public String getNewEmail() {
        return this.newEmail;
    }

    public String getNewExternalId() {
        return this.newExternalId;
    }

    public String getNewGivenName() {
        return this.newGivenName;
    }

    public String getNewSurname() {
        return this.newSurname;
    }

    public String getNewPersistentId() {
        return this.newPersistentId;
    }

    public Boolean getNewIsDirectoryRestricted() {
        return this.newIsDirectoryRestricted;
    }

    public static Builder newBuilder(UserSelectorArg userSelectorArg) {
        return new Builder(userSelectorArg);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.user, this.newEmail, this.newExternalId, this.newGivenName, this.newSurname, this.newPersistentId, this.newIsDirectoryRestricted});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        if (r2.equals(r5) == false) goto L_0x0079;
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
            if (r2 == 0) goto L_0x007b
            com.dropbox.core.v2.team.MembersSetProfileArg r5 = (com.dropbox.core.p005v2.team.MembersSetProfileArg) r5
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0024:
            java.lang.String r2 = r4.newEmail
            java.lang.String r3 = r5.newEmail
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0032:
            java.lang.String r2 = r4.newExternalId
            java.lang.String r3 = r5.newExternalId
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0040:
            java.lang.String r2 = r4.newGivenName
            java.lang.String r3 = r5.newGivenName
            if (r2 == r3) goto L_0x004e
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x004e:
            java.lang.String r2 = r4.newSurname
            java.lang.String r3 = r5.newSurname
            if (r2 == r3) goto L_0x005c
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x005c:
            java.lang.String r2 = r4.newPersistentId
            java.lang.String r3 = r5.newPersistentId
            if (r2 == r3) goto L_0x006a
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x006a:
            java.lang.Boolean r2 = r4.newIsDirectoryRestricted
            java.lang.Boolean r5 = r5.newIsDirectoryRestricted
            if (r2 == r5) goto L_0x007a
            if (r2 == 0) goto L_0x0079
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0079
            goto L_0x007a
        L_0x0079:
            r0 = 0
        L_0x007a:
            return r0
        L_0x007b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MembersSetProfileArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
