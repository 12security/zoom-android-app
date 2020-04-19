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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.team.UserSelectorArg */
public final class UserSelectorArg {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String emailValue;
    /* access modifiers changed from: private */
    public String externalIdValue;
    /* access modifiers changed from: private */
    public String teamMemberIdValue;

    /* renamed from: com.dropbox.core.v2.team.UserSelectorArg$Serializer */
    static class Serializer extends UnionSerializer<UserSelectorArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserSelectorArg userSelectorArg, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (userSelectorArg.tag()) {
                case TEAM_MEMBER_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("team_member_id", jsonGenerator);
                    jsonGenerator.writeFieldName("team_member_id");
                    StoneSerializers.string().serialize(userSelectorArg.teamMemberIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EXTERNAL_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("external_id", jsonGenerator);
                    jsonGenerator.writeFieldName("external_id");
                    StoneSerializers.string().serialize(userSelectorArg.externalIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EMAIL:
                    jsonGenerator.writeStartObject();
                    writeTag("email", jsonGenerator);
                    jsonGenerator.writeFieldName("email");
                    StoneSerializers.string().serialize(userSelectorArg.emailValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(userSelectorArg.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public UserSelectorArg deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UserSelectorArg userSelectorArg;
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
                if ("team_member_id".equals(str)) {
                    expectField("team_member_id", jsonParser);
                    userSelectorArg = UserSelectorArg.teamMemberId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("external_id".equals(str)) {
                    expectField("external_id", jsonParser);
                    userSelectorArg = UserSelectorArg.externalId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("email".equals(str)) {
                    expectField("email", jsonParser);
                    userSelectorArg = UserSelectorArg.email((String) StoneSerializers.string().deserialize(jsonParser));
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
                return userSelectorArg;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.UserSelectorArg$Tag */
    public enum Tag {
        TEAM_MEMBER_ID,
        EXTERNAL_ID,
        EMAIL
    }

    private UserSelectorArg() {
    }

    private UserSelectorArg withTag(Tag tag) {
        UserSelectorArg userSelectorArg = new UserSelectorArg();
        userSelectorArg._tag = tag;
        return userSelectorArg;
    }

    private UserSelectorArg withTagAndTeamMemberId(Tag tag, String str) {
        UserSelectorArg userSelectorArg = new UserSelectorArg();
        userSelectorArg._tag = tag;
        userSelectorArg.teamMemberIdValue = str;
        return userSelectorArg;
    }

    private UserSelectorArg withTagAndExternalId(Tag tag, String str) {
        UserSelectorArg userSelectorArg = new UserSelectorArg();
        userSelectorArg._tag = tag;
        userSelectorArg.externalIdValue = str;
        return userSelectorArg;
    }

    private UserSelectorArg withTagAndEmail(Tag tag, String str) {
        UserSelectorArg userSelectorArg = new UserSelectorArg();
        userSelectorArg._tag = tag;
        userSelectorArg.emailValue = str;
        return userSelectorArg;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTeamMemberId() {
        return this._tag == Tag.TEAM_MEMBER_ID;
    }

    public static UserSelectorArg teamMemberId(String str) {
        if (str != null) {
            return new UserSelectorArg().withTagAndTeamMemberId(Tag.TEAM_MEMBER_ID, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getTeamMemberIdValue() {
        if (this._tag == Tag.TEAM_MEMBER_ID) {
            return this.teamMemberIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_MEMBER_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isExternalId() {
        return this._tag == Tag.EXTERNAL_ID;
    }

    public static UserSelectorArg externalId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() <= 64) {
            return new UserSelectorArg().withTagAndExternalId(Tag.EXTERNAL_ID, str);
        } else {
            throw new IllegalArgumentException("String is longer than 64");
        }
    }

    public String getExternalIdValue() {
        if (this._tag == Tag.EXTERNAL_ID) {
            return this.externalIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.EXTERNAL_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isEmail() {
        return this._tag == Tag.EMAIL;
    }

    public static UserSelectorArg email(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new UserSelectorArg().withTagAndEmail(Tag.EMAIL, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getEmailValue() {
        if (this._tag == Tag.EMAIL) {
            return this.emailValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.EMAIL, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.teamMemberIdValue, this.externalIdValue, this.emailValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UserSelectorArg)) {
            return false;
        }
        UserSelectorArg userSelectorArg = (UserSelectorArg) obj;
        if (this._tag != userSelectorArg._tag) {
            return false;
        }
        switch (this._tag) {
            case TEAM_MEMBER_ID:
                String str = this.teamMemberIdValue;
                String str2 = userSelectorArg.teamMemberIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case EXTERNAL_ID:
                String str3 = this.externalIdValue;
                String str4 = userSelectorArg.externalIdValue;
                if (str3 != str4 && !str3.equals(str4)) {
                    z = false;
                }
                return z;
            case EMAIL:
                String str5 = this.emailValue;
                String str6 = userSelectorArg.emailValue;
                if (str5 != str6 && !str5.equals(str6)) {
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
