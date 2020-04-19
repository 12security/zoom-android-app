package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.users.Name;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.TeamMemberProfile */
public class TeamMemberProfile extends MemberProfile {
    protected final List<String> groups;
    protected final String memberFolderId;

    /* renamed from: com.dropbox.core.v2.team.TeamMemberProfile$Builder */
    public static class Builder extends com.dropbox.core.p005v2.team.MemberProfile.Builder {
        protected final List<String> groups;
        protected final String memberFolderId;

        protected Builder(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name, TeamMembershipType teamMembershipType, List<String> list, String str3) {
            super(str, str2, z, teamMemberStatus, name, teamMembershipType);
            if (list != null) {
                for (String str4 : list) {
                    if (str4 == null) {
                        throw new IllegalArgumentException("An item in list 'groups' is null");
                    }
                }
                this.groups = list;
                if (str3 == null) {
                    throw new IllegalArgumentException("Required value for 'memberFolderId' is null");
                } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str3)) {
                    this.memberFolderId = str3;
                } else {
                    throw new IllegalArgumentException("String 'memberFolderId' does not match pattern");
                }
            } else {
                throw new IllegalArgumentException("Required value for 'groups' is null");
            }
        }

        public Builder withExternalId(String str) {
            super.withExternalId(str);
            return this;
        }

        public Builder withAccountId(String str) {
            super.withAccountId(str);
            return this;
        }

        public Builder withJoinedOn(Date date) {
            super.withJoinedOn(date);
            return this;
        }

        public Builder withPersistentId(String str) {
            super.withPersistentId(str);
            return this;
        }

        public Builder withIsDirectoryRestricted(Boolean bool) {
            super.withIsDirectoryRestricted(bool);
            return this;
        }

        public TeamMemberProfile build() {
            TeamMemberProfile teamMemberProfile = new TeamMemberProfile(this.teamMemberId, this.email, this.emailVerified, this.status, this.name, this.membershipType, this.groups, this.memberFolderId, this.externalId, this.accountId, this.joinedOn, this.persistentId, this.isDirectoryRestricted);
            return teamMemberProfile;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamMemberProfile$Serializer */
    static class Serializer extends StructSerializer<TeamMemberProfile> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamMemberProfile teamMemberProfile, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(teamMemberProfile.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(teamMemberProfile.email, jsonGenerator);
            jsonGenerator.writeFieldName("email_verified");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(teamMemberProfile.emailVerified), jsonGenerator);
            jsonGenerator.writeFieldName("status");
            Serializer.INSTANCE.serialize(teamMemberProfile.status, jsonGenerator);
            jsonGenerator.writeFieldName("name");
            com.dropbox.core.p005v2.users.Name.Serializer.INSTANCE.serialize(teamMemberProfile.name, jsonGenerator);
            jsonGenerator.writeFieldName("membership_type");
            Serializer.INSTANCE.serialize(teamMemberProfile.membershipType, jsonGenerator);
            jsonGenerator.writeFieldName("groups");
            StoneSerializers.list(StoneSerializers.string()).serialize(teamMemberProfile.groups, jsonGenerator);
            jsonGenerator.writeFieldName("member_folder_id");
            StoneSerializers.string().serialize(teamMemberProfile.memberFolderId, jsonGenerator);
            if (teamMemberProfile.externalId != null) {
                jsonGenerator.writeFieldName("external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberProfile.externalId, jsonGenerator);
            }
            if (teamMemberProfile.accountId != null) {
                jsonGenerator.writeFieldName("account_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberProfile.accountId, jsonGenerator);
            }
            if (teamMemberProfile.joinedOn != null) {
                jsonGenerator.writeFieldName("joined_on");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(teamMemberProfile.joinedOn, jsonGenerator);
            }
            if (teamMemberProfile.persistentId != null) {
                jsonGenerator.writeFieldName("persistent_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberProfile.persistentId, jsonGenerator);
            }
            if (teamMemberProfile.isDirectoryRestricted != null) {
                jsonGenerator.writeFieldName("is_directory_restricted");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(teamMemberProfile.isDirectoryRestricted, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamMemberProfile deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                List list = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                Date date = null;
                String str7 = null;
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
                    } else if ("groups".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("member_folder_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("external_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("account_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
                    } else if ("joined_on".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser2);
                    } else if ("persistent_id".equals(currentName)) {
                        str7 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser2);
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
                } else if (teamMembershipType == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"membership_type\" missing.");
                } else if (list == null) {
                    throw new JsonParseException(jsonParser2, "Required field \"groups\" missing.");
                } else if (str4 != null) {
                    TeamMemberProfile teamMemberProfile = new TeamMemberProfile(str2, str3, bool.booleanValue(), teamMemberStatus, name, teamMembershipType, list, str4, str5, str6, date, str7, bool2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamMemberProfile, teamMemberProfile.toStringMultiline());
                    return teamMemberProfile;
                } else {
                    throw new JsonParseException(jsonParser2, "Required field \"member_folder_id\" missing.");
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

    public TeamMemberProfile(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name, TeamMembershipType teamMembershipType, List<String> list, String str3, String str4, String str5, Date date, String str6, Boolean bool) {
        List<String> list2 = list;
        String str7 = str3;
        super(str, str2, z, teamMemberStatus, name, teamMembershipType, str4, str5, date, str6, bool);
        if (list2 != null) {
            for (String str8 : list) {
                if (str8 == null) {
                    throw new IllegalArgumentException("An item in list 'groups' is null");
                }
            }
            this.groups = list2;
            if (str7 == null) {
                throw new IllegalArgumentException("Required value for 'memberFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str7)) {
                this.memberFolderId = str7;
            } else {
                throw new IllegalArgumentException("String 'memberFolderId' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'groups' is null");
        }
    }

    public TeamMemberProfile(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name, TeamMembershipType teamMembershipType, List<String> list, String str3) {
        this(str, str2, z, teamMemberStatus, name, teamMembershipType, list, str3, null, null, null, null, null);
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

    public List<String> getGroups() {
        return this.groups;
    }

    public String getMemberFolderId() {
        return this.memberFolderId;
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

    public static Builder newBuilder(String str, String str2, boolean z, TeamMemberStatus teamMemberStatus, Name name, TeamMembershipType teamMembershipType, List<String> list, String str3) {
        Builder builder = new Builder(str, str2, z, teamMemberStatus, name, teamMembershipType, list, str3);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.groups, this.memberFolderId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0084, code lost:
        if (r2.equals(r3) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0098, code lost:
        if (r4.externalId.equals(r5.externalId) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00ac, code lost:
        if (r4.accountId.equals(r5.accountId) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c0, code lost:
        if (r4.joinedOn.equals(r5.joinedOn) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00d4, code lost:
        if (r4.persistentId.equals(r5.persistentId) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00e8, code lost:
        if (r4.isDirectoryRestricted.equals(r5.isDirectoryRestricted) == false) goto L_0x00eb;
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
            if (r2 == 0) goto L_0x00ed
            com.dropbox.core.v2.team.TeamMemberProfile r5 = (com.dropbox.core.p005v2.team.TeamMemberProfile) r5
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x0028:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x0038:
            boolean r2 = r4.emailVerified
            boolean r3 = r5.emailVerified
            if (r2 != r3) goto L_0x00eb
            com.dropbox.core.v2.team.TeamMemberStatus r2 = r4.status
            com.dropbox.core.v2.team.TeamMemberStatus r3 = r5.status
            if (r2 == r3) goto L_0x004e
            com.dropbox.core.v2.team.TeamMemberStatus r2 = r4.status
            com.dropbox.core.v2.team.TeamMemberStatus r3 = r5.status
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x004e:
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            if (r2 == r3) goto L_0x005e
            com.dropbox.core.v2.users.Name r2 = r4.name
            com.dropbox.core.v2.users.Name r3 = r5.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x005e:
            com.dropbox.core.v2.team.TeamMembershipType r2 = r4.membershipType
            com.dropbox.core.v2.team.TeamMembershipType r3 = r5.membershipType
            if (r2 == r3) goto L_0x006e
            com.dropbox.core.v2.team.TeamMembershipType r2 = r4.membershipType
            com.dropbox.core.v2.team.TeamMembershipType r3 = r5.membershipType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x006e:
            java.util.List<java.lang.String> r2 = r4.groups
            java.util.List<java.lang.String> r3 = r5.groups
            if (r2 == r3) goto L_0x007a
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x007a:
            java.lang.String r2 = r4.memberFolderId
            java.lang.String r3 = r5.memberFolderId
            if (r2 == r3) goto L_0x0086
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x0086:
            java.lang.String r2 = r4.externalId
            java.lang.String r3 = r5.externalId
            if (r2 == r3) goto L_0x009a
            java.lang.String r2 = r4.externalId
            if (r2 == 0) goto L_0x00eb
            java.lang.String r2 = r4.externalId
            java.lang.String r3 = r5.externalId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x009a:
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x00ae
            java.lang.String r2 = r4.accountId
            if (r2 == 0) goto L_0x00eb
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x00ae:
            java.util.Date r2 = r4.joinedOn
            java.util.Date r3 = r5.joinedOn
            if (r2 == r3) goto L_0x00c2
            java.util.Date r2 = r4.joinedOn
            if (r2 == 0) goto L_0x00eb
            java.util.Date r2 = r4.joinedOn
            java.util.Date r3 = r5.joinedOn
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x00c2:
            java.lang.String r2 = r4.persistentId
            java.lang.String r3 = r5.persistentId
            if (r2 == r3) goto L_0x00d6
            java.lang.String r2 = r4.persistentId
            if (r2 == 0) goto L_0x00eb
            java.lang.String r2 = r4.persistentId
            java.lang.String r3 = r5.persistentId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00eb
        L_0x00d6:
            java.lang.Boolean r2 = r4.isDirectoryRestricted
            java.lang.Boolean r3 = r5.isDirectoryRestricted
            if (r2 == r3) goto L_0x00ec
            java.lang.Boolean r2 = r4.isDirectoryRestricted
            if (r2 == 0) goto L_0x00eb
            java.lang.Boolean r2 = r4.isDirectoryRestricted
            java.lang.Boolean r5 = r5.isDirectoryRestricted
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00eb
            goto L_0x00ec
        L_0x00eb:
            r0 = 0
        L_0x00ec:
            return r0
        L_0x00ed:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamMemberProfile.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
