package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.MemberAddResult */
public final class MemberAddResult {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String duplicateExternalMemberIdValue;
    /* access modifiers changed from: private */
    public String duplicateMemberPersistentIdValue;
    /* access modifiers changed from: private */
    public String freeTeamMemberLimitReachedValue;
    /* access modifiers changed from: private */
    public String persistentIdDisabledValue;
    /* access modifiers changed from: private */
    public TeamMemberInfo successValue;
    /* access modifiers changed from: private */
    public String teamLicenseLimitValue;
    /* access modifiers changed from: private */
    public String userAlreadyOnTeamValue;
    /* access modifiers changed from: private */
    public String userAlreadyPairedValue;
    /* access modifiers changed from: private */
    public String userCreationFailedValue;
    /* access modifiers changed from: private */
    public String userMigrationFailedValue;
    /* access modifiers changed from: private */
    public String userOnAnotherTeamValue;

    /* renamed from: com.dropbox.core.v2.team.MemberAddResult$Serializer */
    static class Serializer extends UnionSerializer<MemberAddResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberAddResult memberAddResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberAddResult.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(memberAddResult.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_LICENSE_LIMIT:
                    jsonGenerator.writeStartObject();
                    writeTag("team_license_limit", jsonGenerator);
                    jsonGenerator.writeFieldName("team_license_limit");
                    StoneSerializers.string().serialize(memberAddResult.teamLicenseLimitValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FREE_TEAM_MEMBER_LIMIT_REACHED:
                    jsonGenerator.writeStartObject();
                    writeTag("free_team_member_limit_reached", jsonGenerator);
                    jsonGenerator.writeFieldName("free_team_member_limit_reached");
                    StoneSerializers.string().serialize(memberAddResult.freeTeamMemberLimitReachedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_ALREADY_ON_TEAM:
                    jsonGenerator.writeStartObject();
                    writeTag("user_already_on_team", jsonGenerator);
                    jsonGenerator.writeFieldName("user_already_on_team");
                    StoneSerializers.string().serialize(memberAddResult.userAlreadyOnTeamValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_ON_ANOTHER_TEAM:
                    jsonGenerator.writeStartObject();
                    writeTag("user_on_another_team", jsonGenerator);
                    jsonGenerator.writeFieldName("user_on_another_team");
                    StoneSerializers.string().serialize(memberAddResult.userOnAnotherTeamValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_ALREADY_PAIRED:
                    jsonGenerator.writeStartObject();
                    writeTag("user_already_paired", jsonGenerator);
                    jsonGenerator.writeFieldName("user_already_paired");
                    StoneSerializers.string().serialize(memberAddResult.userAlreadyPairedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_MIGRATION_FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("user_migration_failed", jsonGenerator);
                    jsonGenerator.writeFieldName("user_migration_failed");
                    StoneSerializers.string().serialize(memberAddResult.userMigrationFailedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case DUPLICATE_EXTERNAL_MEMBER_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("duplicate_external_member_id", jsonGenerator);
                    jsonGenerator.writeFieldName("duplicate_external_member_id");
                    StoneSerializers.string().serialize(memberAddResult.duplicateExternalMemberIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case DUPLICATE_MEMBER_PERSISTENT_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("duplicate_member_persistent_id", jsonGenerator);
                    jsonGenerator.writeFieldName("duplicate_member_persistent_id");
                    StoneSerializers.string().serialize(memberAddResult.duplicateMemberPersistentIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PERSISTENT_ID_DISABLED:
                    jsonGenerator.writeStartObject();
                    writeTag("persistent_id_disabled", jsonGenerator);
                    jsonGenerator.writeFieldName("persistent_id_disabled");
                    StoneSerializers.string().serialize(memberAddResult.persistentIdDisabledValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case USER_CREATION_FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("user_creation_failed", jsonGenerator);
                    jsonGenerator.writeFieldName("user_creation_failed");
                    StoneSerializers.string().serialize(memberAddResult.userCreationFailedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(memberAddResult.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MemberAddResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            MemberAddResult memberAddResult;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if (Param.SUCCESS.equals(str)) {
                    memberAddResult = MemberAddResult.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("team_license_limit".equals(str)) {
                    expectField("team_license_limit", jsonParser);
                    memberAddResult = MemberAddResult.teamLicenseLimit((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("free_team_member_limit_reached".equals(str)) {
                    expectField("free_team_member_limit_reached", jsonParser);
                    memberAddResult = MemberAddResult.freeTeamMemberLimitReached((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("user_already_on_team".equals(str)) {
                    expectField("user_already_on_team", jsonParser);
                    memberAddResult = MemberAddResult.userAlreadyOnTeam((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("user_on_another_team".equals(str)) {
                    expectField("user_on_another_team", jsonParser);
                    memberAddResult = MemberAddResult.userOnAnotherTeam((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("user_already_paired".equals(str)) {
                    expectField("user_already_paired", jsonParser);
                    memberAddResult = MemberAddResult.userAlreadyPaired((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("user_migration_failed".equals(str)) {
                    expectField("user_migration_failed", jsonParser);
                    memberAddResult = MemberAddResult.userMigrationFailed((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("duplicate_external_member_id".equals(str)) {
                    expectField("duplicate_external_member_id", jsonParser);
                    memberAddResult = MemberAddResult.duplicateExternalMemberId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("duplicate_member_persistent_id".equals(str)) {
                    expectField("duplicate_member_persistent_id", jsonParser);
                    memberAddResult = MemberAddResult.duplicateMemberPersistentId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("persistent_id_disabled".equals(str)) {
                    expectField("persistent_id_disabled", jsonParser);
                    memberAddResult = MemberAddResult.persistentIdDisabled((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("user_creation_failed".equals(str)) {
                    expectField("user_creation_failed", jsonParser);
                    memberAddResult = MemberAddResult.userCreationFailed((String) StoneSerializers.string().deserialize(jsonParser));
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
                return memberAddResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MemberAddResult$Tag */
    public enum Tag {
        SUCCESS,
        TEAM_LICENSE_LIMIT,
        FREE_TEAM_MEMBER_LIMIT_REACHED,
        USER_ALREADY_ON_TEAM,
        USER_ON_ANOTHER_TEAM,
        USER_ALREADY_PAIRED,
        USER_MIGRATION_FAILED,
        DUPLICATE_EXTERNAL_MEMBER_ID,
        DUPLICATE_MEMBER_PERSISTENT_ID,
        PERSISTENT_ID_DISABLED,
        USER_CREATION_FAILED
    }

    private MemberAddResult() {
    }

    private MemberAddResult withTag(Tag tag) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        return memberAddResult;
    }

    private MemberAddResult withTagAndSuccess(Tag tag, TeamMemberInfo teamMemberInfo) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.successValue = teamMemberInfo;
        return memberAddResult;
    }

    private MemberAddResult withTagAndTeamLicenseLimit(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.teamLicenseLimitValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndFreeTeamMemberLimitReached(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.freeTeamMemberLimitReachedValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndUserAlreadyOnTeam(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.userAlreadyOnTeamValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndUserOnAnotherTeam(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.userOnAnotherTeamValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndUserAlreadyPaired(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.userAlreadyPairedValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndUserMigrationFailed(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.userMigrationFailedValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndDuplicateExternalMemberId(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.duplicateExternalMemberIdValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndDuplicateMemberPersistentId(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.duplicateMemberPersistentIdValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndPersistentIdDisabled(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.persistentIdDisabledValue = str;
        return memberAddResult;
    }

    private MemberAddResult withTagAndUserCreationFailed(Tag tag, String str) {
        MemberAddResult memberAddResult = new MemberAddResult();
        memberAddResult._tag = tag;
        memberAddResult.userCreationFailedValue = str;
        return memberAddResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static MemberAddResult success(TeamMemberInfo teamMemberInfo) {
        if (teamMemberInfo != null) {
            return new MemberAddResult().withTagAndSuccess(Tag.SUCCESS, teamMemberInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamMemberInfo getSuccessValue() {
        if (this._tag == Tag.SUCCESS) {
            return this.successValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SUCCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTeamLicenseLimit() {
        return this._tag == Tag.TEAM_LICENSE_LIMIT;
    }

    public static MemberAddResult teamLicenseLimit(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndTeamLicenseLimit(Tag.TEAM_LICENSE_LIMIT, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getTeamLicenseLimitValue() {
        if (this._tag == Tag.TEAM_LICENSE_LIMIT) {
            return this.teamLicenseLimitValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_LICENSE_LIMIT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFreeTeamMemberLimitReached() {
        return this._tag == Tag.FREE_TEAM_MEMBER_LIMIT_REACHED;
    }

    public static MemberAddResult freeTeamMemberLimitReached(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndFreeTeamMemberLimitReached(Tag.FREE_TEAM_MEMBER_LIMIT_REACHED, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getFreeTeamMemberLimitReachedValue() {
        if (this._tag == Tag.FREE_TEAM_MEMBER_LIMIT_REACHED) {
            return this.freeTeamMemberLimitReachedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FREE_TEAM_MEMBER_LIMIT_REACHED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUserAlreadyOnTeam() {
        return this._tag == Tag.USER_ALREADY_ON_TEAM;
    }

    public static MemberAddResult userAlreadyOnTeam(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndUserAlreadyOnTeam(Tag.USER_ALREADY_ON_TEAM, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUserAlreadyOnTeamValue() {
        if (this._tag == Tag.USER_ALREADY_ON_TEAM) {
            return this.userAlreadyOnTeamValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ALREADY_ON_TEAM, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUserOnAnotherTeam() {
        return this._tag == Tag.USER_ON_ANOTHER_TEAM;
    }

    public static MemberAddResult userOnAnotherTeam(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndUserOnAnotherTeam(Tag.USER_ON_ANOTHER_TEAM, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUserOnAnotherTeamValue() {
        if (this._tag == Tag.USER_ON_ANOTHER_TEAM) {
            return this.userOnAnotherTeamValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ON_ANOTHER_TEAM, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUserAlreadyPaired() {
        return this._tag == Tag.USER_ALREADY_PAIRED;
    }

    public static MemberAddResult userAlreadyPaired(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndUserAlreadyPaired(Tag.USER_ALREADY_PAIRED, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUserAlreadyPairedValue() {
        if (this._tag == Tag.USER_ALREADY_PAIRED) {
            return this.userAlreadyPairedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ALREADY_PAIRED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUserMigrationFailed() {
        return this._tag == Tag.USER_MIGRATION_FAILED;
    }

    public static MemberAddResult userMigrationFailed(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndUserMigrationFailed(Tag.USER_MIGRATION_FAILED, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUserMigrationFailedValue() {
        if (this._tag == Tag.USER_MIGRATION_FAILED) {
            return this.userMigrationFailedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_MIGRATION_FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDuplicateExternalMemberId() {
        return this._tag == Tag.DUPLICATE_EXTERNAL_MEMBER_ID;
    }

    public static MemberAddResult duplicateExternalMemberId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndDuplicateExternalMemberId(Tag.DUPLICATE_EXTERNAL_MEMBER_ID, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getDuplicateExternalMemberIdValue() {
        if (this._tag == Tag.DUPLICATE_EXTERNAL_MEMBER_ID) {
            return this.duplicateExternalMemberIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DUPLICATE_EXTERNAL_MEMBER_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isDuplicateMemberPersistentId() {
        return this._tag == Tag.DUPLICATE_MEMBER_PERSISTENT_ID;
    }

    public static MemberAddResult duplicateMemberPersistentId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndDuplicateMemberPersistentId(Tag.DUPLICATE_MEMBER_PERSISTENT_ID, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getDuplicateMemberPersistentIdValue() {
        if (this._tag == Tag.DUPLICATE_MEMBER_PERSISTENT_ID) {
            return this.duplicateMemberPersistentIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.DUPLICATE_MEMBER_PERSISTENT_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPersistentIdDisabled() {
        return this._tag == Tag.PERSISTENT_ID_DISABLED;
    }

    public static MemberAddResult persistentIdDisabled(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndPersistentIdDisabled(Tag.PERSISTENT_ID_DISABLED, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getPersistentIdDisabledValue() {
        if (this._tag == Tag.PERSISTENT_ID_DISABLED) {
            return this.persistentIdDisabledValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PERSISTENT_ID_DISABLED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUserCreationFailed() {
        return this._tag == Tag.USER_CREATION_FAILED;
    }

    public static MemberAddResult userCreationFailed(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new MemberAddResult().withTagAndUserCreationFailed(Tag.USER_CREATION_FAILED, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUserCreationFailedValue() {
        if (this._tag == Tag.USER_CREATION_FAILED) {
            return this.userCreationFailedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_CREATION_FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.teamLicenseLimitValue, this.freeTeamMemberLimitReachedValue, this.userAlreadyOnTeamValue, this.userOnAnotherTeamValue, this.userAlreadyPairedValue, this.userMigrationFailedValue, this.duplicateExternalMemberIdValue, this.duplicateMemberPersistentIdValue, this.persistentIdDisabledValue, this.userCreationFailedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MemberAddResult)) {
            return false;
        }
        MemberAddResult memberAddResult = (MemberAddResult) obj;
        if (this._tag != memberAddResult._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                TeamMemberInfo teamMemberInfo = this.successValue;
                TeamMemberInfo teamMemberInfo2 = memberAddResult.successValue;
                if (teamMemberInfo != teamMemberInfo2 && !teamMemberInfo.equals(teamMemberInfo2)) {
                    z = false;
                }
                return z;
            case TEAM_LICENSE_LIMIT:
                String str = this.teamLicenseLimitValue;
                String str2 = memberAddResult.teamLicenseLimitValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case FREE_TEAM_MEMBER_LIMIT_REACHED:
                String str3 = this.freeTeamMemberLimitReachedValue;
                String str4 = memberAddResult.freeTeamMemberLimitReachedValue;
                if (str3 != str4 && !str3.equals(str4)) {
                    z = false;
                }
                return z;
            case USER_ALREADY_ON_TEAM:
                String str5 = this.userAlreadyOnTeamValue;
                String str6 = memberAddResult.userAlreadyOnTeamValue;
                if (str5 != str6 && !str5.equals(str6)) {
                    z = false;
                }
                return z;
            case USER_ON_ANOTHER_TEAM:
                String str7 = this.userOnAnotherTeamValue;
                String str8 = memberAddResult.userOnAnotherTeamValue;
                if (str7 != str8 && !str7.equals(str8)) {
                    z = false;
                }
                return z;
            case USER_ALREADY_PAIRED:
                String str9 = this.userAlreadyPairedValue;
                String str10 = memberAddResult.userAlreadyPairedValue;
                if (str9 != str10 && !str9.equals(str10)) {
                    z = false;
                }
                return z;
            case USER_MIGRATION_FAILED:
                String str11 = this.userMigrationFailedValue;
                String str12 = memberAddResult.userMigrationFailedValue;
                if (str11 != str12 && !str11.equals(str12)) {
                    z = false;
                }
                return z;
            case DUPLICATE_EXTERNAL_MEMBER_ID:
                String str13 = this.duplicateExternalMemberIdValue;
                String str14 = memberAddResult.duplicateExternalMemberIdValue;
                if (str13 != str14 && !str13.equals(str14)) {
                    z = false;
                }
                return z;
            case DUPLICATE_MEMBER_PERSISTENT_ID:
                String str15 = this.duplicateMemberPersistentIdValue;
                String str16 = memberAddResult.duplicateMemberPersistentIdValue;
                if (str15 != str16 && !str15.equals(str16)) {
                    z = false;
                }
                return z;
            case PERSISTENT_ID_DISABLED:
                String str17 = this.persistentIdDisabledValue;
                String str18 = memberAddResult.persistentIdDisabledValue;
                if (str17 != str18 && !str17.equals(str18)) {
                    z = false;
                }
                return z;
            case USER_CREATION_FAILED:
                String str19 = this.userCreationFailedValue;
                String str20 = memberAddResult.userCreationFailedValue;
                if (str19 != str20 && !str19.equals(str20)) {
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
