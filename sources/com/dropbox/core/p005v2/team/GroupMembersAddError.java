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

/* renamed from: com.dropbox.core.v2.team.GroupMembersAddError */
public final class GroupMembersAddError {
    public static final GroupMembersAddError DUPLICATE_USER = new GroupMembersAddError().withTag(Tag.DUPLICATE_USER);
    public static final GroupMembersAddError GROUP_NOT_FOUND = new GroupMembersAddError().withTag(Tag.GROUP_NOT_FOUND);
    public static final GroupMembersAddError GROUP_NOT_IN_TEAM = new GroupMembersAddError().withTag(Tag.GROUP_NOT_IN_TEAM);
    public static final GroupMembersAddError OTHER = new GroupMembersAddError().withTag(Tag.OTHER);
    public static final GroupMembersAddError SYSTEM_MANAGED_GROUP_DISALLOWED = new GroupMembersAddError().withTag(Tag.SYSTEM_MANAGED_GROUP_DISALLOWED);
    public static final GroupMembersAddError USER_MUST_BE_ACTIVE_TO_BE_OWNER = new GroupMembersAddError().withTag(Tag.USER_MUST_BE_ACTIVE_TO_BE_OWNER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<String> membersNotInTeamValue;
    /* access modifiers changed from: private */
    public List<String> userCannotBeManagerOfCompanyManagedGroupValue;
    /* access modifiers changed from: private */
    public List<String> usersNotFoundValue;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersAddError$Serializer */
    static class Serializer extends UnionSerializer<GroupMembersAddError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersAddError groupMembersAddError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupMembersAddError.tag()) {
                case GROUP_NOT_FOUND:
                    jsonGenerator.writeString("group_not_found");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYSTEM_MANAGED_GROUP_DISALLOWED:
                    jsonGenerator.writeString("system_managed_group_disallowed");
                    return;
                case DUPLICATE_USER:
                    jsonGenerator.writeString("duplicate_user");
                    return;
                case GROUP_NOT_IN_TEAM:
                    jsonGenerator.writeString("group_not_in_team");
                    return;
                case MEMBERS_NOT_IN_TEAM:
                    jsonGenerator.writeStartObject();
                    writeTag("members_not_in_team", jsonGenerator);
                    jsonGenerator.writeFieldName("members_not_in_team");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupMembersAddError.membersNotInTeamValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USERS_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("users_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("users_not_found");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupMembersAddError.usersNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_MUST_BE_ACTIVE_TO_BE_OWNER:
                    jsonGenerator.writeString("user_must_be_active_to_be_owner");
                    return;
                case USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP:
                    jsonGenerator.writeStartObject();
                    writeTag("user_cannot_be_manager_of_company_managed_group", jsonGenerator);
                    jsonGenerator.writeFieldName("user_cannot_be_manager_of_company_managed_group");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupMembersAddError.userCannotBeManagerOfCompanyManagedGroupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupMembersAddError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupMembersAddError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupMembersAddError groupMembersAddError;
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
                    groupMembersAddError = GroupMembersAddError.GROUP_NOT_FOUND;
                } else if ("other".equals(str)) {
                    groupMembersAddError = GroupMembersAddError.OTHER;
                } else if ("system_managed_group_disallowed".equals(str)) {
                    groupMembersAddError = GroupMembersAddError.SYSTEM_MANAGED_GROUP_DISALLOWED;
                } else if ("duplicate_user".equals(str)) {
                    groupMembersAddError = GroupMembersAddError.DUPLICATE_USER;
                } else if ("group_not_in_team".equals(str)) {
                    groupMembersAddError = GroupMembersAddError.GROUP_NOT_IN_TEAM;
                } else if ("members_not_in_team".equals(str)) {
                    expectField("members_not_in_team", jsonParser);
                    groupMembersAddError = GroupMembersAddError.membersNotInTeam((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else if ("users_not_found".equals(str)) {
                    expectField("users_not_found", jsonParser);
                    groupMembersAddError = GroupMembersAddError.usersNotFound((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else if ("user_must_be_active_to_be_owner".equals(str)) {
                    groupMembersAddError = GroupMembersAddError.USER_MUST_BE_ACTIVE_TO_BE_OWNER;
                } else if ("user_cannot_be_manager_of_company_managed_group".equals(str)) {
                    expectField("user_cannot_be_manager_of_company_managed_group", jsonParser);
                    groupMembersAddError = GroupMembersAddError.userCannotBeManagerOfCompanyManagedGroup((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
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
                return groupMembersAddError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupMembersAddError$Tag */
    public enum Tag {
        GROUP_NOT_FOUND,
        OTHER,
        SYSTEM_MANAGED_GROUP_DISALLOWED,
        DUPLICATE_USER,
        GROUP_NOT_IN_TEAM,
        MEMBERS_NOT_IN_TEAM,
        USERS_NOT_FOUND,
        USER_MUST_BE_ACTIVE_TO_BE_OWNER,
        USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP
    }

    private GroupMembersAddError() {
    }

    private GroupMembersAddError withTag(Tag tag) {
        GroupMembersAddError groupMembersAddError = new GroupMembersAddError();
        groupMembersAddError._tag = tag;
        return groupMembersAddError;
    }

    private GroupMembersAddError withTagAndMembersNotInTeam(Tag tag, List<String> list) {
        GroupMembersAddError groupMembersAddError = new GroupMembersAddError();
        groupMembersAddError._tag = tag;
        groupMembersAddError.membersNotInTeamValue = list;
        return groupMembersAddError;
    }

    private GroupMembersAddError withTagAndUsersNotFound(Tag tag, List<String> list) {
        GroupMembersAddError groupMembersAddError = new GroupMembersAddError();
        groupMembersAddError._tag = tag;
        groupMembersAddError.usersNotFoundValue = list;
        return groupMembersAddError;
    }

    private GroupMembersAddError withTagAndUserCannotBeManagerOfCompanyManagedGroup(Tag tag, List<String> list) {
        GroupMembersAddError groupMembersAddError = new GroupMembersAddError();
        groupMembersAddError._tag = tag;
        groupMembersAddError.userCannotBeManagerOfCompanyManagedGroupValue = list;
        return groupMembersAddError;
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

    public boolean isDuplicateUser() {
        return this._tag == Tag.DUPLICATE_USER;
    }

    public boolean isGroupNotInTeam() {
        return this._tag == Tag.GROUP_NOT_IN_TEAM;
    }

    public boolean isMembersNotInTeam() {
        return this._tag == Tag.MEMBERS_NOT_IN_TEAM;
    }

    public static GroupMembersAddError membersNotInTeam(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupMembersAddError().withTagAndMembersNotInTeam(Tag.MEMBERS_NOT_IN_TEAM, list);
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

    public static GroupMembersAddError usersNotFound(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupMembersAddError().withTagAndUsersNotFound(Tag.USERS_NOT_FOUND, list);
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

    public boolean isUserMustBeActiveToBeOwner() {
        return this._tag == Tag.USER_MUST_BE_ACTIVE_TO_BE_OWNER;
    }

    public boolean isUserCannotBeManagerOfCompanyManagedGroup() {
        return this._tag == Tag.USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP;
    }

    public static GroupMembersAddError userCannotBeManagerOfCompanyManagedGroup(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupMembersAddError().withTagAndUserCannotBeManagerOfCompanyManagedGroup(Tag.USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<String> getUserCannotBeManagerOfCompanyManagedGroupValue() {
        if (this._tag == Tag.USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP) {
            return this.userCannotBeManagerOfCompanyManagedGroupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.membersNotInTeamValue, this.usersNotFoundValue, this.userCannotBeManagerOfCompanyManagedGroupValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GroupMembersAddError)) {
            return false;
        }
        GroupMembersAddError groupMembersAddError = (GroupMembersAddError) obj;
        if (this._tag != groupMembersAddError._tag) {
            return false;
        }
        switch (this._tag) {
            case GROUP_NOT_FOUND:
                return true;
            case OTHER:
                return true;
            case SYSTEM_MANAGED_GROUP_DISALLOWED:
                return true;
            case DUPLICATE_USER:
                return true;
            case GROUP_NOT_IN_TEAM:
                return true;
            case MEMBERS_NOT_IN_TEAM:
                List<String> list = this.membersNotInTeamValue;
                List<String> list2 = groupMembersAddError.membersNotInTeamValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case USERS_NOT_FOUND:
                List<String> list3 = this.usersNotFoundValue;
                List<String> list4 = groupMembersAddError.usersNotFoundValue;
                if (list3 != list4 && !list3.equals(list4)) {
                    z = false;
                }
                return z;
            case USER_MUST_BE_ACTIVE_TO_BE_OWNER:
                return true;
            case USER_CANNOT_BE_MANAGER_OF_COMPANY_MANAGED_GROUP:
                List<String> list5 = this.userCannotBeManagerOfCompanyManagedGroupValue;
                List<String> list6 = groupMembersAddError.userCannotBeManagerOfCompanyManagedGroupValue;
                if (list5 != list6 && !list5.equals(list6)) {
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
