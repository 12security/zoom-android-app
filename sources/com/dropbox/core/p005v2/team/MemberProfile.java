package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.users.Name;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.team.MemberProfile */
public class MemberProfile {
    protected final String accountId;
    protected final String email;
    protected final boolean emailVerified;
    protected final String externalId;
    protected final Boolean isDirectoryRestricted;
    protected final Date joinedOn;
    protected final TeamMembershipType membershipType;
    protected final Name name;
    protected final String persistentId;
    protected final TeamMemberStatus status;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.team.MemberProfile$Builder */
    public static class Builder {
        protected String accountId;
        protected final String email;
        protected final boolean emailVerified;
        protected String externalId;
        protected Boolean isDirectoryRestricted;
        protected Date joinedOn;
        protected final TeamMembershipType membershipType;
        protected final Name name;
        protected String persistentId;
        protected final TeamMemberStatus status;
        protected final String teamMemberId;

        protected Builder(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name2, TeamMembershipType teamMembershipType) {
            if (str != null) {
                this.teamMemberId = str;
                if (str2 != null) {
                    this.email = str2;
                    this.emailVerified = z;
                    if (teamMemberStatus != null) {
                        this.status = teamMemberStatus;
                        if (name2 != null) {
                            this.name = name2;
                            if (teamMembershipType != null) {
                                this.membershipType = teamMembershipType;
                                this.externalId = null;
                                this.accountId = null;
                                this.joinedOn = null;
                                this.persistentId = null;
                                this.isDirectoryRestricted = null;
                                return;
                            }
                            throw new IllegalArgumentException("Required value for 'membershipType' is null");
                        }
                        throw new IllegalArgumentException("Required value for 'name' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'status' is null");
                }
                throw new IllegalArgumentException("Required value for 'email' is null");
            }
            throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
        }

        public Builder withExternalId(String str) {
            this.externalId = str;
            return this;
        }

        public Builder withAccountId(String str) {
            if (str != null) {
                if (str.length() < 40) {
                    throw new IllegalArgumentException("String 'accountId' is shorter than 40");
                } else if (str.length() > 40) {
                    throw new IllegalArgumentException("String 'accountId' is longer than 40");
                }
            }
            this.accountId = str;
            return this;
        }

        public Builder withJoinedOn(Date date) {
            this.joinedOn = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withPersistentId(String str) {
            this.persistentId = str;
            return this;
        }

        public Builder withIsDirectoryRestricted(Boolean bool) {
            this.isDirectoryRestricted = bool;
            return this;
        }

        public MemberProfile build() {
            MemberProfile memberProfile = new MemberProfile(this.teamMemberId, this.email, this.emailVerified, this.status, this.name, this.membershipType, this.externalId, this.accountId, this.joinedOn, this.persistentId, this.isDirectoryRestricted);
            return memberProfile;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MemberProfile$Serializer */
    static class Serializer extends StructSerializer<MemberProfile> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberProfile memberProfile, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(memberProfile.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(memberProfile.email, jsonGenerator);
            jsonGenerator.writeFieldName("email_verified");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(memberProfile.emailVerified), jsonGenerator);
            jsonGenerator.writeFieldName("status");
            Serializer.INSTANCE.serialize(memberProfile.status, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.serialize(memberProfile.name, jsonGenerator);
            jsonGenerator.writeFieldName("membership_type");
            Serializer.INSTANCE.serialize(memberProfile.membershipType, jsonGenerator);
            if (memberProfile.externalId != null) {
                jsonGenerator.writeFieldName("external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberProfile.externalId, jsonGenerator);
            }
            if (memberProfile.accountId != null) {
                jsonGenerator.writeFieldName("account_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberProfile.accountId, jsonGenerator);
            }
            if (memberProfile.joinedOn != null) {
                jsonGenerator.writeFieldName("joined_on");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(memberProfile.joinedOn, jsonGenerator);
            }
            if (memberProfile.persistentId != null) {
                jsonGenerator.writeFieldName("persistent_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(memberProfile.persistentId, jsonGenerator);
            }
            if (memberProfile.isDirectoryRestricted != null) {
                jsonGenerator.writeFieldName("is_directory_restricted");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(memberProfile.isDirectoryRestricted, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberProfile deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str2 = null;
                String str3 = null;
                TeamMemberStatus teamMemberStatus = null;
                Name name = null;
                TeamMembershipType teamMembershipType = null;
                String str4 = null;
                String str5 = null;
                Date date = null;
                String str6 = null;
                Boolean bool2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_member_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("email".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("email_verified".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("status".equals(currentName)) {
                        teamMemberStatus = Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("name".equals(currentName)) {
                        name = (Name) com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("membership_type".equals(currentName)) {
                        teamMembershipType = Serializer.INSTANCE.deserialize(jsonParser2);
                    } else if ("external_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("account_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("joined_on".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else if ("persistent_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("is_directory_restricted".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"team_member_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"email\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"email_verified\" missing.");
                } else if (teamMemberStatus == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"status\" missing.");
                } else if (name == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"name\" missing.");
                } else if (teamMembershipType != null) {
                    MemberProfile memberProfile = new MemberProfile(str2, str3, bool.booleanValue(), teamMemberStatus, name, teamMembershipType, str4, str5, date, str6, bool2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberProfile, memberProfile.toStringMultiline());
                    return memberProfile;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"membership_type\" missing.");
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

    public MemberProfile(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name2, TeamMembershipType teamMembershipType, String str3, String str4, Date date, String str5, Boolean bool) {
        if (str != null) {
            this.teamMemberId = str;
            this.externalId = str3;
            if (str4 != null) {
                if (str4.length() < 40) {
                    throw new IllegalArgumentException("String 'accountId' is shorter than 40");
                } else if (str4.length() > 40) {
                    throw new IllegalArgumentException("String 'accountId' is longer than 40");
                }
            }
            this.accountId = str4;
            if (str2 != null) {
                this.email = str2;
                this.emailVerified = z;
                if (teamMemberStatus != null) {
                    this.status = teamMemberStatus;
                    if (name2 != null) {
                        this.name = name2;
                        if (teamMembershipType != null) {
                            this.membershipType = teamMembershipType;
                            this.joinedOn = LangUtil.truncateMillis(date);
                            this.persistentId = str5;
                            this.isDirectoryRestricted = bool;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'membershipType' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'name' is null");
                }
                throw new IllegalArgumentException("Required value for 'status' is null");
            }
            throw new IllegalArgumentException("Required value for 'email' is null");
        }
        throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
    }

    public MemberProfile(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name2, TeamMembershipType teamMembershipType) {
        this(str, str2, z, teamMemberStatus, name2, teamMembershipType, null, null, null, null, null);
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean getEmailVerified() {
        return this.emailVerified;
    }

    public TeamMemberStatus getStatus() {
        return this.status;
    }

    public Name getName() {
        return this.name;
    }

    public TeamMembershipType getMembershipType() {
        return this.membershipType;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Date getJoinedOn() {
        return this.joinedOn;
    }

    public String getPersistentId() {
        return this.persistentId;
    }

    public Boolean getIsDirectoryRestricted() {
        return this.isDirectoryRestricted;
    }

    public static Builder newBuilder(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name2, TeamMembershipType teamMembershipType) {
        Builder builder = new Builder(str, str2, z, teamMemberStatus, name2, teamMembershipType);
        return builder;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamMemberId, this.externalId, this.accountId, this.email, Boolean.valueOf(this.emailVerified), this.status, this.name, this.membershipType, this.joinedOn, this.persistentId, this.isDirectoryRestricted});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x009e, code lost:
        if (r2.equals(r5) == false) goto L_0x00a1;
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
            if (r2 == 0) goto L_0x00a3
            com.dropbox.core.v2.team.MemberProfile r5 = (com.dropbox.core.p005v2.team.MemberProfile) r5
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0024:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0030:
            boolean r2 = r4.emailVerified
            boolean r3 = r5.emailVerified
            if (r2 != r3) goto L_0x00a1
            com.dropbox.core.v2.team.TeamMemberStatus r2 = r4.status
            com.dropbox.core.v2.team.TeamMemberStatus r3 = r5.status
            if (r2 == r3) goto L_0x0042
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0042:
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            if (r2 == r3) goto L_0x004e
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x004e:
            com.dropbox.core.v2.team.TeamMembershipType r2 = r4.membershipType
            com.dropbox.core.v2.team.TeamMembershipType r3 = r5.membershipType
            if (r2 == r3) goto L_0x005a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x005a:
            java.lang.String r2 = r4.externalId
            java.lang.String r3 = r5.externalId
            if (r2 == r3) goto L_0x0068
            if (r2 == 0) goto L_0x00a1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0068:
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0076
            if (r2 == 0) goto L_0x00a1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0076:
            java.util.Date r2 = r4.joinedOn
            java.util.Date r3 = r5.joinedOn
            if (r2 == r3) goto L_0x0084
            if (r2 == 0) goto L_0x00a1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0084:
            java.lang.String r2 = r4.persistentId
            java.lang.String r3 = r5.persistentId
            if (r2 == r3) goto L_0x0092
            if (r2 == 0) goto L_0x00a1
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00a1
        L_0x0092:
            java.lang.Boolean r2 = r4.isDirectoryRestricted
            java.lang.Boolean r5 = r5.isDirectoryRestricted
            if (r2 == r5) goto L_0x00a2
            if (r2 == 0) goto L_0x00a1
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00a1
            goto L_0x00a2
        L_0x00a1:
            r0 = 0
        L_0x00a2:
            return r0
        L_0x00a3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MemberProfile.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
