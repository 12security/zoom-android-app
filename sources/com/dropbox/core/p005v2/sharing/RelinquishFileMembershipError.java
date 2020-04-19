package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipError */
public final class RelinquishFileMembershipError {
    public static final RelinquishFileMembershipError GROUP_ACCESS = new RelinquishFileMembershipError().withTag(Tag.GROUP_ACCESS);
    public static final RelinquishFileMembershipError NO_PERMISSION = new RelinquishFileMembershipError().withTag(Tag.NO_PERMISSION);
    public static final RelinquishFileMembershipError OTHER = new RelinquishFileMembershipError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipError$Serializer */
    static class Serializer extends UnionSerializer<RelinquishFileMembershipError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelinquishFileMembershipError relinquishFileMembershipError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relinquishFileMembershipError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(relinquishFileMembershipError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP_ACCESS:
                    jsonGenerator.writeString("group_access");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RelinquishFileMembershipError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RelinquishFileMembershipError relinquishFileMembershipError;
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
                    relinquishFileMembershipError = RelinquishFileMembershipError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("group_access".equals(str)) {
                    relinquishFileMembershipError = RelinquishFileMembershipError.GROUP_ACCESS;
                } else if ("no_permission".equals(str)) {
                    relinquishFileMembershipError = RelinquishFileMembershipError.NO_PERMISSION;
                } else {
                    relinquishFileMembershipError = RelinquishFileMembershipError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return relinquishFileMembershipError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        GROUP_ACCESS,
        NO_PERMISSION,
        OTHER
    }

    private RelinquishFileMembershipError() {
    }

    private RelinquishFileMembershipError withTag(Tag tag) {
        RelinquishFileMembershipError relinquishFileMembershipError = new RelinquishFileMembershipError();
        relinquishFileMembershipError._tag = tag;
        return relinquishFileMembershipError;
    }

    private RelinquishFileMembershipError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        RelinquishFileMembershipError relinquishFileMembershipError = new RelinquishFileMembershipError();
        relinquishFileMembershipError._tag = tag;
        relinquishFileMembershipError.accessErrorValue = sharingFileAccessError;
        return relinquishFileMembershipError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static RelinquishFileMembershipError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new RelinquishFileMembershipError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingFileAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroupAccess() {
        return this._tag == Tag.GROUP_ACCESS;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
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
        if (obj == null || !(obj instanceof RelinquishFileMembershipError)) {
            return false;
        }
        RelinquishFileMembershipError relinquishFileMembershipError = (RelinquishFileMembershipError) obj;
        if (this._tag != relinquishFileMembershipError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = relinquishFileMembershipError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
            case GROUP_ACCESS:
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
