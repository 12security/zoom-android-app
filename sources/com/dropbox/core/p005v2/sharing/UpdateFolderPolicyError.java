package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyError */
public final class UpdateFolderPolicyError {
    public static final UpdateFolderPolicyError DISALLOWED_SHARED_LINK_POLICY = new UpdateFolderPolicyError().withTag(Tag.DISALLOWED_SHARED_LINK_POLICY);
    public static final UpdateFolderPolicyError NOT_ON_TEAM = new UpdateFolderPolicyError().withTag(Tag.NOT_ON_TEAM);
    public static final UpdateFolderPolicyError NO_PERMISSION = new UpdateFolderPolicyError().withTag(Tag.NO_PERMISSION);
    public static final UpdateFolderPolicyError OTHER = new UpdateFolderPolicyError().withTag(Tag.OTHER);
    public static final UpdateFolderPolicyError TEAM_FOLDER = new UpdateFolderPolicyError().withTag(Tag.TEAM_FOLDER);
    public static final UpdateFolderPolicyError TEAM_POLICY_DISALLOWS_MEMBER_POLICY = new UpdateFolderPolicyError().withTag(Tag.TEAM_POLICY_DISALLOWS_MEMBER_POLICY);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyError$Serializer */
    static class Serializer extends UnionSerializer<UpdateFolderPolicyError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFolderPolicyError updateFolderPolicyError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (updateFolderPolicyError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(updateFolderPolicyError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NOT_ON_TEAM:
                    jsonGenerator.writeString("not_on_team");
                    return;
                case TEAM_POLICY_DISALLOWS_MEMBER_POLICY:
                    jsonGenerator.writeString("team_policy_disallows_member_policy");
                    return;
                case DISALLOWED_SHARED_LINK_POLICY:
                    jsonGenerator.writeString("disallowed_shared_link_policy");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UpdateFolderPolicyError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UpdateFolderPolicyError updateFolderPolicyError;
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
                    updateFolderPolicyError = UpdateFolderPolicyError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("not_on_team".equals(str)) {
                    updateFolderPolicyError = UpdateFolderPolicyError.NOT_ON_TEAM;
                } else if ("team_policy_disallows_member_policy".equals(str)) {
                    updateFolderPolicyError = UpdateFolderPolicyError.TEAM_POLICY_DISALLOWS_MEMBER_POLICY;
                } else if ("disallowed_shared_link_policy".equals(str)) {
                    updateFolderPolicyError = UpdateFolderPolicyError.DISALLOWED_SHARED_LINK_POLICY;
                } else if ("no_permission".equals(str)) {
                    updateFolderPolicyError = UpdateFolderPolicyError.NO_PERMISSION;
                } else if ("team_folder".equals(str)) {
                    updateFolderPolicyError = UpdateFolderPolicyError.TEAM_FOLDER;
                } else {
                    updateFolderPolicyError = UpdateFolderPolicyError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return updateFolderPolicyError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        NOT_ON_TEAM,
        TEAM_POLICY_DISALLOWS_MEMBER_POLICY,
        DISALLOWED_SHARED_LINK_POLICY,
        NO_PERMISSION,
        TEAM_FOLDER,
        OTHER
    }

    private UpdateFolderPolicyError() {
    }

    private UpdateFolderPolicyError withTag(Tag tag) {
        UpdateFolderPolicyError updateFolderPolicyError = new UpdateFolderPolicyError();
        updateFolderPolicyError._tag = tag;
        return updateFolderPolicyError;
    }

    private UpdateFolderPolicyError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        UpdateFolderPolicyError updateFolderPolicyError = new UpdateFolderPolicyError();
        updateFolderPolicyError._tag = tag;
        updateFolderPolicyError.accessErrorValue = sharedFolderAccessError;
        return updateFolderPolicyError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static UpdateFolderPolicyError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new UpdateFolderPolicyError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isNotOnTeam() {
        return this._tag == Tag.NOT_ON_TEAM;
    }

    public boolean isTeamPolicyDisallowsMemberPolicy() {
        return this._tag == Tag.TEAM_POLICY_DISALLOWS_MEMBER_POLICY;
    }

    public boolean isDisallowedSharedLinkPolicy() {
        return this._tag == Tag.DISALLOWED_SHARED_LINK_POLICY;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isTeamFolder() {
        return this._tag == Tag.TEAM_FOLDER;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UpdateFolderPolicyError)) {
            return false;
        }
        UpdateFolderPolicyError updateFolderPolicyError = (UpdateFolderPolicyError) obj;
        if (this._tag != updateFolderPolicyError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = updateFolderPolicyError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case NOT_ON_TEAM:
                return true;
            case TEAM_POLICY_DISALLOWS_MEMBER_POLICY:
                return true;
            case DISALLOWED_SHARED_LINK_POLICY:
                return true;
            case NO_PERMISSION:
                return true;
            case TEAM_FOLDER:
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
