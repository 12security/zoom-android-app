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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.AddMemberSelectorError */
public final class AddMemberSelectorError {
    public static final AddMemberSelectorError AUTOMATIC_GROUP = new AddMemberSelectorError().withTag(Tag.AUTOMATIC_GROUP);
    public static final AddMemberSelectorError GROUP_DELETED = new AddMemberSelectorError().withTag(Tag.GROUP_DELETED);
    public static final AddMemberSelectorError GROUP_NOT_ON_TEAM = new AddMemberSelectorError().withTag(Tag.GROUP_NOT_ON_TEAM);
    public static final AddMemberSelectorError OTHER = new AddMemberSelectorError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String invalidDropboxIdValue;
    /* access modifiers changed from: private */
    public String invalidEmailValue;
    /* access modifiers changed from: private */
    public String unverifiedDropboxIdValue;

    /* renamed from: com.dropbox.core.v2.sharing.AddMemberSelectorError$Serializer */
    static class Serializer extends UnionSerializer<AddMemberSelectorError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddMemberSelectorError addMemberSelectorError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (addMemberSelectorError.tag()) {
                case AUTOMATIC_GROUP:
                    jsonGenerator.writeString("automatic_group");
                    return;
                case INVALID_DROPBOX_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_dropbox_id", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_dropbox_id");
                    StoneSerializers.string().serialize(addMemberSelectorError.invalidDropboxIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_EMAIL:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_email", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_email");
                    StoneSerializers.string().serialize(addMemberSelectorError.invalidEmailValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case UNVERIFIED_DROPBOX_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("unverified_dropbox_id", jsonGenerator);
                    jsonGenerator.writeFieldName("unverified_dropbox_id");
                    StoneSerializers.string().serialize(addMemberSelectorError.unverifiedDropboxIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP_DELETED:
                    jsonGenerator.writeString("group_deleted");
                    return;
                case GROUP_NOT_ON_TEAM:
                    jsonGenerator.writeString("group_not_on_team");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AddMemberSelectorError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AddMemberSelectorError addMemberSelectorError;
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
                if ("automatic_group".equals(str)) {
                    addMemberSelectorError = AddMemberSelectorError.AUTOMATIC_GROUP;
                } else if ("invalid_dropbox_id".equals(str)) {
                    expectField("invalid_dropbox_id", jsonParser);
                    addMemberSelectorError = AddMemberSelectorError.invalidDropboxId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("invalid_email".equals(str)) {
                    expectField("invalid_email", jsonParser);
                    addMemberSelectorError = AddMemberSelectorError.invalidEmail((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("unverified_dropbox_id".equals(str)) {
                    expectField("unverified_dropbox_id", jsonParser);
                    addMemberSelectorError = AddMemberSelectorError.unverifiedDropboxId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("group_deleted".equals(str)) {
                    addMemberSelectorError = AddMemberSelectorError.GROUP_DELETED;
                } else if ("group_not_on_team".equals(str)) {
                    addMemberSelectorError = AddMemberSelectorError.GROUP_NOT_ON_TEAM;
                } else {
                    addMemberSelectorError = AddMemberSelectorError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return addMemberSelectorError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.AddMemberSelectorError$Tag */
    public enum Tag {
        AUTOMATIC_GROUP,
        INVALID_DROPBOX_ID,
        INVALID_EMAIL,
        UNVERIFIED_DROPBOX_ID,
        GROUP_DELETED,
        GROUP_NOT_ON_TEAM,
        OTHER
    }

    private AddMemberSelectorError() {
    }

    private AddMemberSelectorError withTag(Tag tag) {
        AddMemberSelectorError addMemberSelectorError = new AddMemberSelectorError();
        addMemberSelectorError._tag = tag;
        return addMemberSelectorError;
    }

    private AddMemberSelectorError withTagAndInvalidDropboxId(Tag tag, String str) {
        AddMemberSelectorError addMemberSelectorError = new AddMemberSelectorError();
        addMemberSelectorError._tag = tag;
        addMemberSelectorError.invalidDropboxIdValue = str;
        return addMemberSelectorError;
    }

    private AddMemberSelectorError withTagAndInvalidEmail(Tag tag, String str) {
        AddMemberSelectorError addMemberSelectorError = new AddMemberSelectorError();
        addMemberSelectorError._tag = tag;
        addMemberSelectorError.invalidEmailValue = str;
        return addMemberSelectorError;
    }

    private AddMemberSelectorError withTagAndUnverifiedDropboxId(Tag tag, String str) {
        AddMemberSelectorError addMemberSelectorError = new AddMemberSelectorError();
        addMemberSelectorError._tag = tag;
        addMemberSelectorError.unverifiedDropboxIdValue = str;
        return addMemberSelectorError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAutomaticGroup() {
        return this._tag == Tag.AUTOMATIC_GROUP;
    }

    public boolean isInvalidDropboxId() {
        return this._tag == Tag.INVALID_DROPBOX_ID;
    }

    public static AddMemberSelectorError invalidDropboxId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new AddMemberSelectorError().withTagAndInvalidDropboxId(Tag.INVALID_DROPBOX_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getInvalidDropboxIdValue() {
        if (this._tag == Tag.INVALID_DROPBOX_ID) {
            return this.invalidDropboxIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INVALID_DROPBOX_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInvalidEmail() {
        return this._tag == Tag.INVALID_EMAIL;
    }

    public static AddMemberSelectorError invalidEmail(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new AddMemberSelectorError().withTagAndInvalidEmail(Tag.INVALID_EMAIL, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getInvalidEmailValue() {
        if (this._tag == Tag.INVALID_EMAIL) {
            return this.invalidEmailValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INVALID_EMAIL, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isUnverifiedDropboxId() {
        return this._tag == Tag.UNVERIFIED_DROPBOX_ID;
    }

    public static AddMemberSelectorError unverifiedDropboxId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new AddMemberSelectorError().withTagAndUnverifiedDropboxId(Tag.UNVERIFIED_DROPBOX_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getUnverifiedDropboxIdValue() {
        if (this._tag == Tag.UNVERIFIED_DROPBOX_ID) {
            return this.unverifiedDropboxIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.UNVERIFIED_DROPBOX_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroupDeleted() {
        return this._tag == Tag.GROUP_DELETED;
    }

    public boolean isGroupNotOnTeam() {
        return this._tag == Tag.GROUP_NOT_ON_TEAM;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.invalidDropboxIdValue, this.invalidEmailValue, this.unverifiedDropboxIdValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AddMemberSelectorError)) {
            return false;
        }
        AddMemberSelectorError addMemberSelectorError = (AddMemberSelectorError) obj;
        if (this._tag != addMemberSelectorError._tag) {
            return false;
        }
        switch (this._tag) {
            case AUTOMATIC_GROUP:
                return true;
            case INVALID_DROPBOX_ID:
                String str = this.invalidDropboxIdValue;
                String str2 = addMemberSelectorError.invalidDropboxIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case INVALID_EMAIL:
                String str3 = this.invalidEmailValue;
                String str4 = addMemberSelectorError.invalidEmailValue;
                if (str3 != str4 && !str3.equals(str4)) {
                    z = false;
                }
                return z;
            case UNVERIFIED_DROPBOX_ID:
                String str5 = this.unverifiedDropboxIdValue;
                String str6 = addMemberSelectorError.unverifiedDropboxIdValue;
                if (str5 != str6 && !str5.equals(str6)) {
                    z = false;
                }
                return z;
            case GROUP_DELETED:
                return true;
            case GROUP_NOT_ON_TEAM:
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
