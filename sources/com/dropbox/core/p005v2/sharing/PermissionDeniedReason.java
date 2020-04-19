package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.PermissionDeniedReason */
public final class PermissionDeniedReason {
    public static final PermissionDeniedReason FOLDER_IS_INSIDE_SHARED_FOLDER = new PermissionDeniedReason().withTag(Tag.FOLDER_IS_INSIDE_SHARED_FOLDER);
    public static final PermissionDeniedReason FOLDER_IS_LIMITED_TEAM_FOLDER = new PermissionDeniedReason().withTag(Tag.FOLDER_IS_LIMITED_TEAM_FOLDER);
    public static final PermissionDeniedReason OTHER = new PermissionDeniedReason().withTag(Tag.OTHER);
    public static final PermissionDeniedReason OWNER_NOT_ON_TEAM = new PermissionDeniedReason().withTag(Tag.OWNER_NOT_ON_TEAM);
    public static final PermissionDeniedReason PERMISSION_DENIED = new PermissionDeniedReason().withTag(Tag.PERMISSION_DENIED);
    public static final PermissionDeniedReason RESTRICTED_BY_PARENT_FOLDER = new PermissionDeniedReason().withTag(Tag.RESTRICTED_BY_PARENT_FOLDER);
    public static final PermissionDeniedReason RESTRICTED_BY_TEAM = new PermissionDeniedReason().withTag(Tag.RESTRICTED_BY_TEAM);
    public static final PermissionDeniedReason TARGET_IS_INDIRECT_MEMBER = new PermissionDeniedReason().withTag(Tag.TARGET_IS_INDIRECT_MEMBER);
    public static final PermissionDeniedReason TARGET_IS_OWNER = new PermissionDeniedReason().withTag(Tag.TARGET_IS_OWNER);
    public static final PermissionDeniedReason TARGET_IS_SELF = new PermissionDeniedReason().withTag(Tag.TARGET_IS_SELF);
    public static final PermissionDeniedReason TARGET_NOT_ACTIVE = new PermissionDeniedReason().withTag(Tag.TARGET_NOT_ACTIVE);
    public static final PermissionDeniedReason USER_ACCOUNT_TYPE = new PermissionDeniedReason().withTag(Tag.USER_ACCOUNT_TYPE);
    public static final PermissionDeniedReason USER_NOT_ALLOWED_BY_OWNER = new PermissionDeniedReason().withTag(Tag.USER_NOT_ALLOWED_BY_OWNER);
    public static final PermissionDeniedReason USER_NOT_ON_TEAM = new PermissionDeniedReason().withTag(Tag.USER_NOT_ON_TEAM);
    public static final PermissionDeniedReason USER_NOT_SAME_TEAM_AS_OWNER = new PermissionDeniedReason().withTag(Tag.USER_NOT_SAME_TEAM_AS_OWNER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public InsufficientPlan insufficientPlanValue;

    /* renamed from: com.dropbox.core.v2.sharing.PermissionDeniedReason$Serializer */
    static class Serializer extends UnionSerializer<PermissionDeniedReason> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PermissionDeniedReason permissionDeniedReason, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (permissionDeniedReason.tag()) {
                case USER_NOT_SAME_TEAM_AS_OWNER:
                    jsonGenerator.writeString("user_not_same_team_as_owner");
                    return;
                case USER_NOT_ALLOWED_BY_OWNER:
                    jsonGenerator.writeString("user_not_allowed_by_owner");
                    return;
                case TARGET_IS_INDIRECT_MEMBER:
                    jsonGenerator.writeString("target_is_indirect_member");
                    return;
                case TARGET_IS_OWNER:
                    jsonGenerator.writeString("target_is_owner");
                    return;
                case TARGET_IS_SELF:
                    jsonGenerator.writeString("target_is_self");
                    return;
                case TARGET_NOT_ACTIVE:
                    jsonGenerator.writeString("target_not_active");
                    return;
                case FOLDER_IS_LIMITED_TEAM_FOLDER:
                    jsonGenerator.writeString("folder_is_limited_team_folder");
                    return;
                case OWNER_NOT_ON_TEAM:
                    jsonGenerator.writeString("owner_not_on_team");
                    return;
                case PERMISSION_DENIED:
                    jsonGenerator.writeString("permission_denied");
                    return;
                case RESTRICTED_BY_TEAM:
                    jsonGenerator.writeString("restricted_by_team");
                    return;
                case USER_ACCOUNT_TYPE:
                    jsonGenerator.writeString("user_account_type");
                    return;
                case USER_NOT_ON_TEAM:
                    jsonGenerator.writeString("user_not_on_team");
                    return;
                case FOLDER_IS_INSIDE_SHARED_FOLDER:
                    jsonGenerator.writeString("folder_is_inside_shared_folder");
                    return;
                case RESTRICTED_BY_PARENT_FOLDER:
                    jsonGenerator.writeString("restricted_by_parent_folder");
                    return;
                case INSUFFICIENT_PLAN:
                    jsonGenerator.writeStartObject();
                    writeTag("insufficient_plan", jsonGenerator);
                    Serializer.INSTANCE.serialize(permissionDeniedReason.insufficientPlanValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PermissionDeniedReason deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            PermissionDeniedReason permissionDeniedReason;
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
                if ("user_not_same_team_as_owner".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.USER_NOT_SAME_TEAM_AS_OWNER;
                } else if ("user_not_allowed_by_owner".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.USER_NOT_ALLOWED_BY_OWNER;
                } else if ("target_is_indirect_member".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.TARGET_IS_INDIRECT_MEMBER;
                } else if ("target_is_owner".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.TARGET_IS_OWNER;
                } else if ("target_is_self".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.TARGET_IS_SELF;
                } else if ("target_not_active".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.TARGET_NOT_ACTIVE;
                } else if ("folder_is_limited_team_folder".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.FOLDER_IS_LIMITED_TEAM_FOLDER;
                } else if ("owner_not_on_team".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.OWNER_NOT_ON_TEAM;
                } else if ("permission_denied".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.PERMISSION_DENIED;
                } else if ("restricted_by_team".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.RESTRICTED_BY_TEAM;
                } else if ("user_account_type".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.USER_ACCOUNT_TYPE;
                } else if ("user_not_on_team".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.USER_NOT_ON_TEAM;
                } else if ("folder_is_inside_shared_folder".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.FOLDER_IS_INSIDE_SHARED_FOLDER;
                } else if ("restricted_by_parent_folder".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.RESTRICTED_BY_PARENT_FOLDER;
                } else if ("insufficient_plan".equals(str)) {
                    permissionDeniedReason = PermissionDeniedReason.insufficientPlan(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    permissionDeniedReason = PermissionDeniedReason.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return permissionDeniedReason;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.PermissionDeniedReason$Tag */
    public enum Tag {
        USER_NOT_SAME_TEAM_AS_OWNER,
        USER_NOT_ALLOWED_BY_OWNER,
        TARGET_IS_INDIRECT_MEMBER,
        TARGET_IS_OWNER,
        TARGET_IS_SELF,
        TARGET_NOT_ACTIVE,
        FOLDER_IS_LIMITED_TEAM_FOLDER,
        OWNER_NOT_ON_TEAM,
        PERMISSION_DENIED,
        RESTRICTED_BY_TEAM,
        USER_ACCOUNT_TYPE,
        USER_NOT_ON_TEAM,
        FOLDER_IS_INSIDE_SHARED_FOLDER,
        RESTRICTED_BY_PARENT_FOLDER,
        INSUFFICIENT_PLAN,
        OTHER
    }

    private PermissionDeniedReason() {
    }

    private PermissionDeniedReason withTag(Tag tag) {
        PermissionDeniedReason permissionDeniedReason = new PermissionDeniedReason();
        permissionDeniedReason._tag = tag;
        return permissionDeniedReason;
    }

    private PermissionDeniedReason withTagAndInsufficientPlan(Tag tag, InsufficientPlan insufficientPlan) {
        PermissionDeniedReason permissionDeniedReason = new PermissionDeniedReason();
        permissionDeniedReason._tag = tag;
        permissionDeniedReason.insufficientPlanValue = insufficientPlan;
        return permissionDeniedReason;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserNotSameTeamAsOwner() {
        return this._tag == Tag.USER_NOT_SAME_TEAM_AS_OWNER;
    }

    public boolean isUserNotAllowedByOwner() {
        return this._tag == Tag.USER_NOT_ALLOWED_BY_OWNER;
    }

    public boolean isTargetIsIndirectMember() {
        return this._tag == Tag.TARGET_IS_INDIRECT_MEMBER;
    }

    public boolean isTargetIsOwner() {
        return this._tag == Tag.TARGET_IS_OWNER;
    }

    public boolean isTargetIsSelf() {
        return this._tag == Tag.TARGET_IS_SELF;
    }

    public boolean isTargetNotActive() {
        return this._tag == Tag.TARGET_NOT_ACTIVE;
    }

    public boolean isFolderIsLimitedTeamFolder() {
        return this._tag == Tag.FOLDER_IS_LIMITED_TEAM_FOLDER;
    }

    public boolean isOwnerNotOnTeam() {
        return this._tag == Tag.OWNER_NOT_ON_TEAM;
    }

    public boolean isPermissionDenied() {
        return this._tag == Tag.PERMISSION_DENIED;
    }

    public boolean isRestrictedByTeam() {
        return this._tag == Tag.RESTRICTED_BY_TEAM;
    }

    public boolean isUserAccountType() {
        return this._tag == Tag.USER_ACCOUNT_TYPE;
    }

    public boolean isUserNotOnTeam() {
        return this._tag == Tag.USER_NOT_ON_TEAM;
    }

    public boolean isFolderIsInsideSharedFolder() {
        return this._tag == Tag.FOLDER_IS_INSIDE_SHARED_FOLDER;
    }

    public boolean isRestrictedByParentFolder() {
        return this._tag == Tag.RESTRICTED_BY_PARENT_FOLDER;
    }

    public boolean isInsufficientPlan() {
        return this._tag == Tag.INSUFFICIENT_PLAN;
    }

    public static PermissionDeniedReason insufficientPlan(InsufficientPlan insufficientPlan) {
        if (insufficientPlan != null) {
            return new PermissionDeniedReason().withTagAndInsufficientPlan(Tag.INSUFFICIENT_PLAN, insufficientPlan);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public InsufficientPlan getInsufficientPlanValue() {
        if (this._tag == Tag.INSUFFICIENT_PLAN) {
            return this.insufficientPlanValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INSUFFICIENT_PLAN, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.insufficientPlanValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PermissionDeniedReason)) {
            return false;
        }
        PermissionDeniedReason permissionDeniedReason = (PermissionDeniedReason) obj;
        if (this._tag != permissionDeniedReason._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_NOT_SAME_TEAM_AS_OWNER:
                return true;
            case USER_NOT_ALLOWED_BY_OWNER:
                return true;
            case TARGET_IS_INDIRECT_MEMBER:
                return true;
            case TARGET_IS_OWNER:
                return true;
            case TARGET_IS_SELF:
                return true;
            case TARGET_NOT_ACTIVE:
                return true;
            case FOLDER_IS_LIMITED_TEAM_FOLDER:
                return true;
            case OWNER_NOT_ON_TEAM:
                return true;
            case PERMISSION_DENIED:
                return true;
            case RESTRICTED_BY_TEAM:
                return true;
            case USER_ACCOUNT_TYPE:
                return true;
            case USER_NOT_ON_TEAM:
                return true;
            case FOLDER_IS_INSIDE_SHARED_FOLDER:
                return true;
            case RESTRICTED_BY_PARENT_FOLDER:
                return true;
            case INSUFFICIENT_PLAN:
                InsufficientPlan insufficientPlan = this.insufficientPlanValue;
                InsufficientPlan insufficientPlan2 = permissionDeniedReason.insufficientPlanValue;
                if (insufficientPlan != insufficientPlan2 && !insufficientPlan.equals(insufficientPlan2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
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
