package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveError */
public final class GroupMembersRemoveError {
    public static final GroupMembersRemoveError GROUP_NOT_FOUND = new GroupMembersRemoveError().withTag(Tag.GROUP_NOT_FOUND);
    public static final GroupMembersRemoveError GROUP_NOT_IN_TEAM = new GroupMembersRemoveError().withTag(Tag.GROUP_NOT_IN_TEAM);
    public static final GroupMembersRemoveError MEMBER_NOT_IN_GROUP = new GroupMembersRemoveError().withTag(Tag.MEMBER_NOT_IN_GROUP);
    public static final GroupMembersRemoveError OTHER = new GroupMembersRemoveError().withTag(Tag.OTHER);
    public static final GroupMembersRemoveError SYSTEM_MANAGED_GROUP_DISALLOWED = new GroupMembersRemoveError().withTag(Tag.SYSTEM_MANAGED_GROUP_DISALLOWED);
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<String> membersNotInTeamValue;
    /* access modifiers changed from: private */
    public List<String> usersNotFoundValue;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveError$Serializer */
    static class Serializer extends UnionSerializer<GroupMembersRemoveError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersRemoveError groupMembersRemoveError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupMembersRemoveError.tag()) {
                case GROUP_NOT_FOUND:
                    jsonGenerator.writeString("group_not_found");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                case MEMBER_NOT_IN_GROUP:
                    jsonGenerator.writeString("member_not_in_group");
                    return;
                case GROUP_NOT_IN_TEAM:
                    jsonGenerator.writeString("group_not_in_team");
                    return;
                case MEMBERS_NOT_IN_TEAM:
                    jsonGenerator.writeStartObject();
                    writeTag("members_not_in_team", jsonGenerator);
                    jsonGenerator.writeFieldName("members_not_in_team");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupMembersRemoveError.membersNotInTeamValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USERS_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("users_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("users_not_found");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupMembersRemoveError.usersNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupMembersRemoveError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupMembersRemoveError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupMembersRemoveError groupMembersRemoveError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("group_not_found".equals(str)) {
                    groupMembersRemoveError = GroupMembersRemoveError.GROUP_NOT_FOUND;
                } else if ("other".equals(str)) {
                    groupMembersRemoveError = GroupMembersRemoveError.OTHER;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupMembersRemoveError = GroupMembersRemoveError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else if ("member_not_in_group".equals(str)) {
                    groupMembersRemoveError = GroupMembersRemoveError.MEMBER_NOT_IN_GROUP;
                } else if ("group_not_in_team".equals(str)) {
                    groupMembersRemoveError = GroupMembersRemoveError.GROUP_NOT_IN_TEAM;
                } else if ("members_not_in_team".equals(str)) {
                    expectField("members_not_in_team", jsonParser);
                    groupMembersRemoveError = GroupMembersRemoveError.membersNotInTeam((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else if ("users_not_found".equals(str)) {
                    expectField("users_not_found", jsonParser);
                    groupMembersRemoveError = GroupMembersRemoveError.usersNotFound((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupMembersRemoveError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveError$Tag */
    public enum Tag {
        GROUP_NOT_FOUND,
        OTHER,
        SYSTEM_MANAGED_GROUP_DISALLOWED,
        MEMBER_NOT_IN_GROUP,
        GROUP_NOT_IN_TEAM,
        MEMBERS_NOT_IN_TEAM,
        USERS_NOT_FOUND
    }

    private GroupMembersRemoveError() {
    }

    private GroupMembersRemoveError withTag(Tag tag) {
        GroupMembersRemoveError groupMembersRemoveError = new GroupMembersRemoveError();
        groupMembersRemoveError._tag = tag;
        return groupMembersRemoveError;
    }

    private GroupMembersRemoveError withTagAndMembersNotInTeam(Tag tag, List<String> list) {
        GroupMembersRemoveError groupMembersRemoveError = new GroupMembersRemoveError();
        groupMembersRemoveError._tag = tag;
        groupMembersRemoveError.membersNotInTeamValue = list;
        return groupMembersRemoveError;
    }

    private GroupMembersRemoveError withTagAndUsersNotFound(Tag tag, List<String> list) {
        GroupMembersRemoveError groupMembersRemoveError = new GroupMembersRemoveError();
        groupMembersRemoveError._tag = tag;
        groupMembersRemoveError.usersNotFoundValue = list;
        return groupMembersRemoveError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isGroupNotFound() {
        return this._tag == Tag.GROUP_NOT_FOUND;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isSystemManagedGroupDisallowed() {
        return this._tag == Tag.SYSTEM_MANAGED_GROUP_DISALLOWED;
    }

    public boolean isMemberNotInGroup() {
        return this._tag == Tag.MEMBER_NOT_IN_GROUP;
    }

    public boolean isGroupNotInTeam() {
        return this._tag == Tag.GROUP_NOT_IN_TEAM;
    }

    public boolean isMembersNotInTeam() {
        return this._tag == Tag.MEMBERS_NOT_IN_TEAM;
    }

    public static GroupMembersRemoveError membersNotInTeam(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupMembersRemoveError().withTagAndMembersNotInTeam(Tag.MEMBERS_NOT_IN_TEAM, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<String> getMembersNotInTeamValue() {
        if (this._tag == Tag.MEMBERS_NOT_IN_TEAM) {
            return this.membersNotInTeamValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MEMBERS_NOT_IN_TEAM, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUsersNotFound() {
        return this._tag == Tag.USERS_NOT_FOUND;
    }

    public static GroupMembersRemoveError usersNotFound(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupMembersRemoveError().withTagAndUsersNotFound(Tag.USERS_NOT_FOUND, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<String> getUsersNotFoundValue() {
        if (this._tag == Tag.USERS_NOT_FOUND) {
            return this.usersNotFoundValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USERS_NOT_FOUND, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.membersNotInTeamValue, this.usersNotFoundValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GroupMembersRemoveError)) {
            return false;
        }
        GroupMembersRemoveError groupMembersRemoveError = (GroupMembersRemoveError) obj;
        if (this._tag != groupMembersRemoveError._tag) {
            return false;
        }
        switch (this._tag) {
            case GROUP_NOT_FOUND:
                return true;
            case OTHER:
                return true;
            case SYSTEM_MANAGED_GROUP_DISALLOWED:
                return true;
            case MEMBER_NOT_IN_GROUP:
                return true;
            case GROUP_NOT_IN_TEAM:
                return true;
            case MEMBERS_NOT_IN_TEAM:
                List<String> list = this.membersNotInTeamValue;
                List<String> list2 = groupMembersRemoveError.membersNotInTeamValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case USERS_NOT_FOUND:
                List<String> list3 = this.usersNotFoundValue;
                List<String> list4 = groupMembersRemoveError.usersNotFoundValue;
                if (list3 != list4 && !list3.equals(list4)) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
