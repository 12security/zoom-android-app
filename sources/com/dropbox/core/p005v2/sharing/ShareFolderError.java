package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderError */
public final class ShareFolderError {
    public static final ShareFolderError DISALLOWED_SHARED_LINK_POLICY = new ShareFolderError().withTag(Tag.DISALLOWED_SHARED_LINK_POLICY);
    public static final ShareFolderError EMAIL_UNVERIFIED = new ShareFolderError().withTag(Tag.EMAIL_UNVERIFIED);
    public static final ShareFolderError NO_PERMISSION = new ShareFolderError().withTag(Tag.NO_PERMISSION);
    public static final ShareFolderError OTHER = new ShareFolderError().withTag(Tag.OTHER);
    public static final ShareFolderError TEAM_POLICY_DISALLOWS_MEMBER_POLICY = new ShareFolderError().withTag(Tag.TEAM_POLICY_DISALLOWS_MEMBER_POLICY);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharePathError badPathValue;

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderError$Serializer */
    static class Serializer extends UnionSerializer<ShareFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShareFolderError shareFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (shareFolderError.tag()) {
                case EMAIL_UNVERIFIED:
                    jsonGenerator.writeString("email_unverified");
                    return;
                case BAD_PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("bad_path", jsonGenerator);
                    jsonGenerator.writeFieldName("bad_path");
                    Serializer.INSTANCE.serialize(shareFolderError.badPathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_POLICY_DISALLOWS_MEMBER_POLICY:
                    jsonGenerator.writeString("team_policy_disallows_member_policy");
                    return;
                case DISALLOWED_SHARED_LINK_POLICY:
                    jsonGenerator.writeString("disallowed_shared_link_policy");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(shareFolderError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ShareFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ShareFolderError shareFolderError;
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
                if ("email_unverified".equals(str)) {
                    shareFolderError = ShareFolderError.EMAIL_UNVERIFIED;
                } else if ("bad_path".equals(str)) {
                    expectField("bad_path", jsonParser);
                    shareFolderError = ShareFolderError.badPath(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_policy_disallows_member_policy".equals(str)) {
                    shareFolderError = ShareFolderError.TEAM_POLICY_DISALLOWS_MEMBER_POLICY;
                } else if ("disallowed_shared_link_policy".equals(str)) {
                    shareFolderError = ShareFolderError.DISALLOWED_SHARED_LINK_POLICY;
                } else if ("other".equals(str)) {
                    shareFolderError = ShareFolderError.OTHER;
                } else if ("no_permission".equals(str)) {
                    shareFolderError = ShareFolderError.NO_PERMISSION;
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
                return shareFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderError$Tag */
    public enum Tag {
        EMAIL_UNVERIFIED,
        BAD_PATH,
        TEAM_POLICY_DISALLOWS_MEMBER_POLICY,
        DISALLOWED_SHARED_LINK_POLICY,
        OTHER,
        NO_PERMISSION
    }

    private ShareFolderError() {
    }

    private ShareFolderError withTag(Tag tag) {
        ShareFolderError shareFolderError = new ShareFolderError();
        shareFolderError._tag = tag;
        return shareFolderError;
    }

    private ShareFolderError withTagAndBadPath(Tag tag, SharePathError sharePathError) {
        ShareFolderError shareFolderError = new ShareFolderError();
        shareFolderError._tag = tag;
        shareFolderError.badPathValue = sharePathError;
        return shareFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isEmailUnverified() {
        return this._tag == Tag.EMAIL_UNVERIFIED;
    }

    public boolean isBadPath() {
        return this._tag == Tag.BAD_PATH;
    }

    public static ShareFolderError badPath(SharePathError sharePathError) {
        if (sharePathError != null) {
            return new ShareFolderError().withTagAndBadPath(Tag.BAD_PATH, sharePathError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharePathError getBadPathValue() {
        if (this._tag == Tag.BAD_PATH) {
            return this.badPathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.BAD_PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTeamPolicyDisallowsMemberPolicy() {
        return this._tag == Tag.TEAM_POLICY_DISALLOWS_MEMBER_POLICY;
    }

    public boolean isDisallowedSharedLinkPolicy() {
        return this._tag == Tag.DISALLOWED_SHARED_LINK_POLICY;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.badPathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ShareFolderError)) {
            return false;
        }
        ShareFolderError shareFolderError = (ShareFolderError) obj;
        if (this._tag != shareFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case EMAIL_UNVERIFIED:
                return true;
            case BAD_PATH:
                SharePathError sharePathError = this.badPathValue;
                SharePathError sharePathError2 = shareFolderError.badPathValue;
                if (sharePathError != sharePathError2 && !sharePathError.equals(sharePathError2)) {
                    z = false;
                }
                return z;
            case TEAM_POLICY_DISALLOWS_MEMBER_POLICY:
                return true;
            case DISALLOWED_SHARED_LINK_POLICY:
                return true;
            case OTHER:
                return true;
            case NO_PERMISSION:
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
