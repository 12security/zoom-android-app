package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberError */
public final class AddFolderMemberError {
    public static final AddFolderMemberError CANT_SHARE_OUTSIDE_TEAM = new AddFolderMemberError().withTag(Tag.CANT_SHARE_OUTSIDE_TEAM);
    public static final AddFolderMemberError EMAIL_UNVERIFIED = new AddFolderMemberError().withTag(Tag.EMAIL_UNVERIFIED);
    public static final AddFolderMemberError INSUFFICIENT_PLAN = new AddFolderMemberError().withTag(Tag.INSUFFICIENT_PLAN);
    public static final AddFolderMemberError NO_PERMISSION = new AddFolderMemberError().withTag(Tag.NO_PERMISSION);
    public static final AddFolderMemberError OTHER = new AddFolderMemberError().withTag(Tag.OTHER);
    public static final AddFolderMemberError RATE_LIMIT = new AddFolderMemberError().withTag(Tag.RATE_LIMIT);
    public static final AddFolderMemberError TEAM_FOLDER = new AddFolderMemberError().withTag(Tag.TEAM_FOLDER);
    public static final AddFolderMemberError TOO_MANY_INVITEES = new AddFolderMemberError().withTag(Tag.TOO_MANY_INVITEES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public AddMemberSelectorError badMemberValue;
    /* access modifiers changed from: private */
    public Long tooManyMembersValue;
    /* access modifiers changed from: private */
    public Long tooManyPendingInvitesValue;

    /* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberError$Serializer */
    static class Serializer extends UnionSerializer<AddFolderMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddFolderMemberError addFolderMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (addFolderMemberError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(addFolderMemberError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EMAIL_UNVERIFIED:
                    jsonGenerator.writeString("email_unverified");
                    return;
                case BAD_MEMBER:
                    jsonGenerator.writeStartObject();
                    writeTag("bad_member", jsonGenerator);
                    jsonGenerator.writeFieldName("bad_member");
                    Serializer.INSTANCE.serialize(addFolderMemberError.badMemberValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case CANT_SHARE_OUTSIDE_TEAM:
                    jsonGenerator.writeString("cant_share_outside_team");
                    return;
                case TOO_MANY_MEMBERS:
                    jsonGenerator.writeStartObject();
                    writeTag("too_many_members", jsonGenerator);
                    jsonGenerator.writeFieldName("too_many_members");
                    StoneSerializers.uInt64().serialize(addFolderMemberError.tooManyMembersValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TOO_MANY_PENDING_INVITES:
                    jsonGenerator.writeStartObject();
                    writeTag("too_many_pending_invites", jsonGenerator);
                    jsonGenerator.writeFieldName("too_many_pending_invites");
                    StoneSerializers.uInt64().serialize(addFolderMemberError.tooManyPendingInvitesValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RATE_LIMIT:
                    jsonGenerator.writeString("rate_limit");
                    return;
                case TOO_MANY_INVITEES:
                    jsonGenerator.writeString("too_many_invitees");
                    return;
                case INSUFFICIENT_PLAN:
                    jsonGenerator.writeString("insufficient_plan");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AddFolderMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AddFolderMemberError addFolderMemberError;
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
                if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    addFolderMemberError = AddFolderMemberError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("email_unverified".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.EMAIL_UNVERIFIED;
                } else if ("bad_member".equals(str)) {
                    expectField("bad_member", jsonParser);
                    addFolderMemberError = AddFolderMemberError.badMember(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("cant_share_outside_team".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.CANT_SHARE_OUTSIDE_TEAM;
                } else if ("too_many_members".equals(str)) {
                    expectField("too_many_members", jsonParser);
                    addFolderMemberError = AddFolderMemberError.tooManyMembers(((Long) StoneSerializers.uInt64().deserialize(jsonParser)).longValue());
                } else if ("too_many_pending_invites".equals(str)) {
                    expectField("too_many_pending_invites", jsonParser);
                    addFolderMemberError = AddFolderMemberError.tooManyPendingInvites(((Long) StoneSerializers.uInt64().deserialize(jsonParser)).longValue());
                } else if ("rate_limit".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.RATE_LIMIT;
                } else if ("too_many_invitees".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.TOO_MANY_INVITEES;
                } else if ("insufficient_plan".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.INSUFFICIENT_PLAN;
                } else if ("team_folder".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.TEAM_FOLDER;
                } else if ("no_permission".equals(str)) {
                    addFolderMemberError = AddFolderMemberError.NO_PERMISSION;
                } else {
                    addFolderMemberError = AddFolderMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return addFolderMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        EMAIL_UNVERIFIED,
        BAD_MEMBER,
        CANT_SHARE_OUTSIDE_TEAM,
        TOO_MANY_MEMBERS,
        TOO_MANY_PENDING_INVITES,
        RATE_LIMIT,
        TOO_MANY_INVITEES,
        INSUFFICIENT_PLAN,
        TEAM_FOLDER,
        NO_PERMISSION,
        OTHER
    }

    private AddFolderMemberError() {
    }

    private AddFolderMemberError withTag(Tag tag) {
        AddFolderMemberError addFolderMemberError = new AddFolderMemberError();
        addFolderMemberError._tag = tag;
        return addFolderMemberError;
    }

    private AddFolderMemberError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        AddFolderMemberError addFolderMemberError = new AddFolderMemberError();
        addFolderMemberError._tag = tag;
        addFolderMemberError.accessErrorValue = sharedFolderAccessError;
        return addFolderMemberError;
    }

    private AddFolderMemberError withTagAndBadMember(Tag tag, AddMemberSelectorError addMemberSelectorError) {
        AddFolderMemberError addFolderMemberError = new AddFolderMemberError();
        addFolderMemberError._tag = tag;
        addFolderMemberError.badMemberValue = addMemberSelectorError;
        return addFolderMemberError;
    }

    private AddFolderMemberError withTagAndTooManyMembers(Tag tag, Long l) {
        AddFolderMemberError addFolderMemberError = new AddFolderMemberError();
        addFolderMemberError._tag = tag;
        addFolderMemberError.tooManyMembersValue = l;
        return addFolderMemberError;
    }

    private AddFolderMemberError withTagAndTooManyPendingInvites(Tag tag, Long l) {
        AddFolderMemberError addFolderMemberError = new AddFolderMemberError();
        addFolderMemberError._tag = tag;
        addFolderMemberError.tooManyPendingInvitesValue = l;
        return addFolderMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static AddFolderMemberError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new AddFolderMemberError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isEmailUnverified() {
        return this._tag == Tag.EMAIL_UNVERIFIED;
    }

    public boolean isBadMember() {
        return this._tag == Tag.BAD_MEMBER;
    }

    public static AddFolderMemberError badMember(AddMemberSelectorError addMemberSelectorError) {
        if (addMemberSelectorError != null) {
            return new AddFolderMemberError().withTagAndBadMember(Tag.BAD_MEMBER, addMemberSelectorError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public AddMemberSelectorError getBadMemberValue() {
        if (this._tag == Tag.BAD_MEMBER) {
            return this.badMemberValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.BAD_MEMBER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isCantShareOutsideTeam() {
        return this._tag == Tag.CANT_SHARE_OUTSIDE_TEAM;
    }

    public boolean isTooManyMembers() {
        return this._tag == Tag.TOO_MANY_MEMBERS;
    }

    public static AddFolderMemberError tooManyMembers(long j) {
        return new AddFolderMemberError().withTagAndTooManyMembers(Tag.TOO_MANY_MEMBERS, Long.valueOf(j));
    }

    public long getTooManyMembersValue() {
        if (this._tag == Tag.TOO_MANY_MEMBERS) {
            return this.tooManyMembersValue.longValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TOO_MANY_MEMBERS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTooManyPendingInvites() {
        return this._tag == Tag.TOO_MANY_PENDING_INVITES;
    }

    public static AddFolderMemberError tooManyPendingInvites(long j) {
        return new AddFolderMemberError().withTagAndTooManyPendingInvites(Tag.TOO_MANY_PENDING_INVITES, Long.valueOf(j));
    }

    public long getTooManyPendingInvitesValue() {
        if (this._tag == Tag.TOO_MANY_PENDING_INVITES) {
            return this.tooManyPendingInvitesValue.longValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TOO_MANY_PENDING_INVITES, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRateLimit() {
        return this._tag == Tag.RATE_LIMIT;
    }

    public boolean isTooManyInvitees() {
        return this._tag == Tag.TOO_MANY_INVITEES;
    }

    public boolean isInsufficientPlan() {
        return this._tag == Tag.INSUFFICIENT_PLAN;
    }

    public boolean isTeamFolder() {
        return this._tag == Tag.TEAM_FOLDER;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.badMemberValue, this.tooManyMembersValue, this.tooManyPendingInvitesValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AddFolderMemberError)) {
            return false;
        }
        AddFolderMemberError addFolderMemberError = (AddFolderMemberError) obj;
        if (this._tag != addFolderMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = addFolderMemberError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case EMAIL_UNVERIFIED:
                return true;
            case BAD_MEMBER:
                AddMemberSelectorError addMemberSelectorError = this.badMemberValue;
                AddMemberSelectorError addMemberSelectorError2 = addFolderMemberError.badMemberValue;
                if (addMemberSelectorError != addMemberSelectorError2 && !addMemberSelectorError.equals(addMemberSelectorError2)) {
                    z = false;
                }
                return z;
            case CANT_SHARE_OUTSIDE_TEAM:
                return true;
            case TOO_MANY_MEMBERS:
                if (this.tooManyMembersValue != addFolderMemberError.tooManyMembersValue) {
                    z = false;
                }
                return z;
            case TOO_MANY_PENDING_INVITES:
                if (this.tooManyPendingInvitesValue != addFolderMemberError.tooManyPendingInvitesValue) {
                    z = false;
                }
                return z;
            case RATE_LIMIT:
                return true;
            case TOO_MANY_INVITEES:
                return true;
            case INSUFFICIENT_PLAN:
                return true;
            case TEAM_FOLDER:
                return true;
            case NO_PERMISSION:
                return true;
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
