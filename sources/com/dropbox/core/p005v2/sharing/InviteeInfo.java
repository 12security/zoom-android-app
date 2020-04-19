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

/* renamed from: com.dropbox.core.v2.sharing.InviteeInfo */
public final class InviteeInfo {
    public static final InviteeInfo OTHER = new InviteeInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String emailValue;

    /* renamed from: com.dropbox.core.v2.sharing.InviteeInfo$Serializer */
    public static class Serializer extends UnionSerializer<InviteeInfo> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(InviteeInfo inviteeInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07471.$SwitchMap$com$dropbox$core$v2$sharing$InviteeInfo$Tag[inviteeInfo.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("email", jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(inviteeInfo.emailValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public InviteeInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            InviteeInfo inviteeInfo;
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
                if ("email".equals(str)) {
                    expectField("email", jsonParser);
                    inviteeInfo = InviteeInfo.email((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    inviteeInfo = InviteeInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return inviteeInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.InviteeInfo$Tag */
    public enum Tag {
        EMAIL,
        OTHER
    }

    private InviteeInfo() {
    }

    private InviteeInfo withTag(Tag tag) {
        InviteeInfo inviteeInfo = new InviteeInfo();
        inviteeInfo._tag = tag;
        return inviteeInfo;
    }

    private InviteeInfo withTagAndEmail(Tag tag, String str) {
        InviteeInfo inviteeInfo = new InviteeInfo();
        inviteeInfo._tag = tag;
        inviteeInfo.emailValue = str;
        return inviteeInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isEmail() {
        return this._tag == Tag.EMAIL;
    }

    public static InviteeInfo email(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() > 255) {
            throw new IllegalArgumentException("String is longer than 255");
        } else if (Pattern.matches("^['&A-Za-z0-9._%+-]+@[A-Za-z0-9-][A-Za-z0-9.-]*.[A-Za-z]{2,15}$", str)) {
            return new InviteeInfo().withTagAndEmail(Tag.EMAIL, str);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.emailValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof InviteeInfo)) {
            return false;
        }
        InviteeInfo inviteeInfo = (InviteeInfo) obj;
        if (this._tag != inviteeInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case EMAIL:
                String str = this.emailValue;
                String str2 = inviteeInfo.emailValue;
                if (str != str2 && !str.equals(str2)) {
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
