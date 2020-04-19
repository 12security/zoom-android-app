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

/* renamed from: com.dropbox.core.v2.sharing.LinkPassword */
public final class LinkPassword {
    public static final LinkPassword OTHER = new LinkPassword().withTag(Tag.OTHER);
    public static final LinkPassword REMOVE_PASSWORD = new LinkPassword().withTag(Tag.REMOVE_PASSWORD);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String setPasswordValue;

    /* renamed from: com.dropbox.core.v2.sharing.LinkPassword$Serializer */
    static class Serializer extends UnionSerializer<LinkPassword> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkPassword linkPassword, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (linkPassword.tag()) {
                case REMOVE_PASSWORD:
                    jsonGenerator.writeString("remove_password");
                    return;
                case SET_PASSWORD:
                    jsonGenerator.writeStartObject();
                    writeTag("set_password", jsonGenerator);
                    jsonGenerator.writeFieldName("set_password");
                    StoneSerializers.string().serialize(linkPassword.setPasswordValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LinkPassword deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LinkPassword linkPassword;
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
                if ("remove_password".equals(str)) {
                    linkPassword = LinkPassword.REMOVE_PASSWORD;
                } else if ("set_password".equals(str)) {
                    expectField("set_password", jsonParser);
                    linkPassword = LinkPassword.setPassword((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    linkPassword = LinkPassword.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return linkPassword;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.LinkPassword$Tag */
    public enum Tag {
        REMOVE_PASSWORD,
        SET_PASSWORD,
        OTHER
    }

    private LinkPassword() {
    }

    private LinkPassword withTag(Tag tag) {
        LinkPassword linkPassword = new LinkPassword();
        linkPassword._tag = tag;
        return linkPassword;
    }

    private LinkPassword withTagAndSetPassword(Tag tag, String str) {
        LinkPassword linkPassword = new LinkPassword();
        linkPassword._tag = tag;
        linkPassword.setPasswordValue = str;
        return linkPassword;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isRemovePassword() {
        return this._tag == Tag.REMOVE_PASSWORD;
    }

    public boolean isSetPassword() {
        return this._tag == Tag.SET_PASSWORD;
    }

    public static LinkPassword setPassword(String str) {
        if (str != null) {
            return new LinkPassword().withTagAndSetPassword(Tag.SET_PASSWORD, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getSetPasswordValue() {
        if (this._tag == Tag.SET_PASSWORD) {
            return this.setPasswordValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SET_PASSWORD, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.setPasswordValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LinkPassword)) {
            return false;
        }
        LinkPassword linkPassword = (LinkPassword) obj;
        if (this._tag != linkPassword._tag) {
            return false;
        }
        switch (this._tag) {
            case REMOVE_PASSWORD:
                return true;
            case SET_PASSWORD:
                String str = this.setPasswordValue;
                String str2 = linkPassword.setPasswordValue;
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
